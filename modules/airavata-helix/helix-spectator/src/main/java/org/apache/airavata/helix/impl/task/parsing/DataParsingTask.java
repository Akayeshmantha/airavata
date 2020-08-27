/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.airavata.helix.impl.task.parsing;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.apache.airavata.agents.api.AgentException;
import org.apache.airavata.agents.api.StorageResourceAdaptor;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.common.utils.ServerSettings;
import org.apache.airavata.credential.store.client.CredentialStoreClientFactory;
import org.apache.airavata.credential.store.cpi.CredentialStoreService;
import org.apache.airavata.credential.store.exception.CredentialStoreException;
import org.apache.airavata.helix.core.AbstractTask;
import org.apache.airavata.helix.impl.task.TaskOnFailException;
import org.apache.airavata.helix.impl.task.parsing.models.ParsingTaskInput;
import org.apache.airavata.helix.impl.task.parsing.models.ParsingTaskInputs;
import org.apache.airavata.helix.impl.task.parsing.models.ParsingTaskOutput;
import org.apache.airavata.helix.impl.task.parsing.models.ParsingTaskOutputs;
import org.apache.airavata.helix.task.api.TaskHelper;
import org.apache.airavata.helix.task.api.annotation.TaskDef;
import org.apache.airavata.helix.task.api.annotation.TaskParam;
import org.apache.airavata.helix.task.api.support.AdaptorSupport;
import org.apache.airavata.model.appcatalog.gatewayprofile.GatewayResourceProfile;
import org.apache.airavata.model.appcatalog.gatewayprofile.StoragePreference;
import org.apache.airavata.model.appcatalog.parser.Parser;
import org.apache.airavata.model.appcatalog.parser.ParserInput;
import org.apache.airavata.model.appcatalog.parser.ParserOutput;
import org.apache.airavata.model.appcatalog.storageresource.StorageResourceDescription;
import org.apache.airavata.model.data.movement.DataMovementProtocol;
import org.apache.airavata.model.data.replica.DataProductModel;
import org.apache.airavata.model.data.replica.DataProductType;
import org.apache.airavata.model.data.replica.DataReplicaLocationModel;
import org.apache.airavata.model.data.replica.ReplicaLocationCategory;
import org.apache.airavata.model.data.replica.ReplicaPersistentType;
import org.apache.airavata.model.process.ProcessModel;
import org.apache.airavata.registry.api.RegistryService;
import org.apache.airavata.registry.api.client.RegistryServiceClientFactory;
import org.apache.airavata.registry.api.exception.RegistryServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.helix.task.TaskResult;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the data parsing task.
 *
 * @since 1.0.0-SNAPSHOT
 */
@TaskDef(name = "Data Parsing Task")
public class DataParsingTask extends AbstractTask {

    private final static Logger logger = LoggerFactory.getLogger(DataParsingTask.class);

    @TaskParam(name = "Parser Id")
    private String parserId;

    @TaskParam(name = "Parser inputs")
    private ParsingTaskInputs parsingTaskInputs;

    @TaskParam(name = "Parser outputs")
    private ParsingTaskOutputs parsingTaskOutputs;

    @TaskParam(name = "Gateway ID")
    private String gatewayId;

    @TaskParam(name = "Group Resource Profile Id")
    private String groupResourceProfileId;

    @TaskParam(name = "Local data dir")
    private String localDataDir;

    @TaskParam(name = "Process Id")
    private String processId;

    @TaskParam(name = "DP Output Version")
    private Integer outputVersion;

    @Override
    public TaskResult onRun(TaskHelper helper) {
        logger.info("Starting data parsing task " + getTaskId());

        try {

            Parser parser = getRegistryServiceClient().getParser(parserId, gatewayId);
            logger.info("Loading parser with id {}", parser.getId());

            String containerId = getTaskId() + "_PARSER_"+ parser.getId();
            containerId = containerId.replace(" ", "-");

            String localInputDir = createLocalInputDir(containerId);
            String localOutDir = createLocalOutputDir(containerId);
            logger.info("Created local input and ouput directories : " + localInputDir + ", " + localOutDir);

            logger.info("Downloading input files to local input directory");

            Map<String, String> properties = new HashMap<>();
            for (ParserInput parserInput : parser.getInputFiles()) {
                Optional<ParsingTaskInput> filteredInputOptional = parsingTaskInputs.getInputs().stream().filter(inp -> parserInput.getId().equals(inp.getId())).findFirst();

                if (filteredInputOptional.isPresent()) {

                    ParsingTaskInput parsingTaskInput = filteredInputOptional.get();

                    String inputVal = parsingTaskInput.getValue() != null ?
                                                parsingTaskInput.getValue() :
                                                getContextVariable(parsingTaskInput.getContextVariableName());

                    logger.info("Processing parser input {} with type {} and with value {}",
                                                    parsingTaskInput.getName(), parserInput.getType().name(), inputVal);

                    if ("PROPERTY".equals(parsingTaskInput.getType())) {
                        properties.put(parsingTaskInput.getName(), inputVal);
                    } else if ("FILE".equals(parsingTaskInput.getType())) {

                        String inputDataProductUri = inputVal;

                        if (inputDataProductUri == null || inputDataProductUri.isEmpty()) {
                            logger.error("Data product uri could not be null or empty for input " + parsingTaskInput.getId()
                                    + " with name " + parserInput.getName());
                            throw new TaskOnFailException("Data product uri could not be null or empty for input "
                                    + parsingTaskInput.getId() + " with name " + parserInput.getName(), true, null);
                        }

                        logger.info("Using data product uri {} for input name {}", inputDataProductUri, parserInput.getName());

                        String[] dataPordUris = inputDataProductUri.split(",");
                        if (dataPordUris.length > 1) {
                            logger.info("Multiple data products found {}. Using last one", inputDataProductUri);
                            inputDataProductUri = dataPordUris[dataPordUris.length - 1];
                        }

                        DataProductModel inputDataProduct = getRegistryServiceClient().getDataProduct(inputDataProductUri);
                        List<DataReplicaLocationModel> replicaLocations = inputDataProduct.getReplicaLocations();

                        boolean downloadPassed = false;

                        for (DataReplicaLocationModel replicaLocationModel : replicaLocations) {
                            String storageResourceId = replicaLocationModel.getStorageResourceId();
                            String remoteFilePath = new URI(replicaLocationModel.getFilePath()).getPath();
                            String localFilePath = localInputDir + (localInputDir.endsWith(File.separator) ? "" : File.separator)
                                    + parserInput.getName();

                            downloadPassed = downloadFileFromStorageResource(storageResourceId, remoteFilePath, localFilePath, helper.getAdaptorSupport());

                            if (downloadPassed) {
                                break;
                            }
                        }

                        if (!downloadPassed) {
                            logger.error("Failed to download input file with id " + parserInput.getId() + " from data product uri " + inputDataProductUri);
                            throw new TaskOnFailException("Failed to download input file with id " + parserInput.getId() + " from data product uri " + inputDataProductUri, true, null);
                        }
                    }
                } else {
                    if (parserInput.isRequiredInput()) {
                        logger.error("Parser input with id " + parserInput.getId() + " and name " + parserInput.getName() + " is not available");
                        throw new TaskOnFailException("Parser input with id " + parserInput.getId() + " and name " + parserInput.getName() + " is not available", true, null);
                    } else {
                        logger.warn("Parser input with id with id " + parserInput.getId() + " and name " + parserInput.getName() + " is not available. But it is not required");
                    }
                }
            }

            logger.info("Running container with local input dir " + localInputDir + " local output dir " + localOutDir);
            runContainer(parser, localInputDir, localOutDir, properties);

            for (ParserOutput parserOutput : parser.getOutputFiles()) {

                Optional<ParsingTaskOutput> filteredOutputOptional = parsingTaskOutputs.getOutputs()
                        .stream().filter(out -> parserOutput.getId().equals(out.getId())).findFirst();

                if (filteredOutputOptional.isPresent()) {

                    ParsingTaskOutput parsingTaskOutput = filteredOutputOptional.get();
                    String localFilePath = Paths.get(localOutDir, parserOutput.getName()).toString();
                    String remoteFilePath = Paths.get("parseout", parserId, outputVersion + "", parserOutput.getName()).toString();

                    if (new File(localFilePath).exists()) {
                        uploadFileToStorageResource(parsingTaskOutput, remoteFilePath, localFilePath, helper.getAdaptorSupport());
                    } else if (parserOutput.isRequiredOutput()) {
                        logger.error("Expected output file " + localFilePath + " can not be found");
                        throw new TaskOnFailException("Expected output file " + localFilePath + " can not be found", false, null);
                    } else {
                        logger.error("Expected output file " + localFilePath + " can not be found but skipping as it is not mandatory");
                    }
                } else {
                    if (parserOutput.isRequiredOutput()) {
                        logger.error("File upload info with id " + parserOutput.getId() + " and name " + parserOutput.getName() + " is not available");
                        throw new TaskOnFailException("File upload info with id " + parserOutput.getId() + " and name " + parserOutput.getName() + " is not available", true, null);
                    } else {
                        logger.warn("File upload info with id " + parserOutput.getId() + " and name " + parserOutput.getName() + " is not available. But it is not required");
                    }
                }

            }

            return onSuccess("Successfully completed data parsing task " + getTaskId());
        } catch (TaskOnFailException e) {
            if (e.getError() != null) {
                logger.error(e.getReason(), e.getError());
            } else {
                logger.error(e.getReason());
            }

            return onFail(e.getReason(), e.isCritical());

        } catch (Exception e) {
            logger.error("Unknown error occurred in " + getTaskId(), e);
            return onFail("Unknown error occurred in " + getTaskId(), true);
        }
    }

    @Override
    public void onCancel() {

    }

    public static void main(String args[]) throws DockerCertificateException, DockerException, InterruptedException {
        final com.spotify.docker.client.DockerClient docker = DefaultDockerClient.fromEnv().build();
        docker.pull("dimuthuupe/gamess-rna-parser:1.0");
        HostConfig hostConfig = HostConfig.builder().build();

        ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image("dimuthuupe/gamess-rna-parser:1.0")
                .cmd("sh", "-c", "while :; do sleep 1; done")
                .build();

        ContainerCreation creation = docker.createContainer(containerConfig);
        String id = creation.id();

        ContainerInfo info = docker.inspectContainer(id);

        docker.startContainer(id);

        String[] command = {"sh", "-c", "l"};
        ExecCreation execCreation = docker.execCreate(
                id, command, com.spotify.docker.client.DockerClient.ExecCreateParam.attachStdout(),
                com.spotify.docker.client.DockerClient.ExecCreateParam.attachStderr());
        LogStream output = docker.execStart(execCreation.id());
        String execOutput = output.readFully();

        docker.killContainer(id);

        docker.removeContainer(id);
        docker.close();

        System.out.println(execOutput);
    }

    private void runContainer(Parser parser, String localInputDir, String localOutputDir, Map<String, String> properties)
            throws ApplicationSettingsException, DockerCertificateException, DockerException, InterruptedException {

        final com.spotify.docker.client.DockerClient docker = DefaultDockerClient.fromEnv().build();
        logger.info("Pulling image " + parser.getImageName());
        docker.pull(parser.getImageName());
        logger.info("Successfully pulled image " + parser.getImageName());

        HostConfig hostConfig = HostConfig.builder().binds(
                HostConfig.Bind.builder().from(localInputDir).to(parser.getInputDirPath()).build(),
                HostConfig.Bind.builder().from(localOutputDir).to(parser.getOutputDirPath()).build()
                ).build();

        ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .cmd("sh", "-c", "while :; do sleep 1; done")
                .image(parser.getImageName())
                .env(properties.entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.toList()))
                .build();

        ContainerCreation creation = docker.createContainer(containerConfig);
        String id = creation.id();

        logger.info("Created the container with id " + id);

        ContainerInfo info = docker.inspectContainer(id);

        docker.startContainer(id);

        String command[] = parser.getExecutionCommand().split(" ");

        logger.info("Starting container with id " + id);

        ExecCreation execCreation = docker.execCreate(
                id, command, com.spotify.docker.client.DockerClient.ExecCreateParam.attachStdout(),
                com.spotify.docker.client.DockerClient.ExecCreateParam.attachStderr());
        LogStream output = docker.execStart(execCreation.id());
        String execOutput = output.readFully();

        logger.info("Container output " + execOutput);

        String commandVerif[] = {"sh", "-c", "ls /opt/outputs"};
        execCreation = docker.execCreate(
                id, commandVerif, com.spotify.docker.client.DockerClient.ExecCreateParam.attachStdout(),
                com.spotify.docker.client.DockerClient.ExecCreateParam.attachStderr());
        output = docker.execStart(execCreation.id());
        execOutput = output.readFully();

        logger.info("Verification output " + execOutput);

        logger.info("Waiting for the container to stop");
        docker.killContainer(id);
        docker.removeContainer(id);
        docker.close();
        logger.info("Container {} successfully removed", id);
    }

    private StorageResourceAdaptor getStorageResourceAdaptor(String storageResourceId, AdaptorSupport adaptorSupport) throws TaskOnFailException, TException, AgentException {

        StoragePreference gatewayStoragePreference = getRegistryServiceClient().getGatewayStoragePreference(gatewayId, storageResourceId);
        GatewayResourceProfile gatewayResourceProfile = getRegistryServiceClient().getGatewayResourceProfile(gatewayId);

        String token = gatewayStoragePreference.getResourceSpecificCredentialStoreToken();
        if (token == null || token.isEmpty()) {
            token = gatewayResourceProfile.getCredentialStoreToken();
        }
        if (gatewayStoragePreference == null) {
            logger.error("Could not find a gateway storage preference for storage " + storageResourceId + " gateway id " + gatewayId);
            throw new TaskOnFailException("Could not find a gateway storage preference for storage " + storageResourceId + " gateway id " + gatewayId, false, null);
        }

        logger.info("Fetching adaptor for storage resource " + storageResourceId + " with token " + token);
        return adaptorSupport.fetchStorageAdaptor(gatewayId, storageResourceId, DataMovementProtocol.SCP, token,
                gatewayStoragePreference.getLoginUserName());

    }

    private boolean downloadFileFromStorageResource(String storageResourceId, String remoteFilePath, String localFilePath, AdaptorSupport adaptorSupport) {

        logger.info("Downloading from storage resource " + storageResourceId + " from path " + remoteFilePath + " to local path " +
                localFilePath);
        try {
            StorageResourceAdaptor storageResourceAdaptor = getStorageResourceAdaptor(storageResourceId, adaptorSupport);
            storageResourceAdaptor.downloadFile(remoteFilePath, localFilePath);
            return true;
        } catch (Exception e) {
            logger.error("Failed to download file from storage " + storageResourceId + " in path " + remoteFilePath + " to local path " + localFilePath, e);
            return false;
        }
    }

    private void uploadFileToStorageResource(ParsingTaskOutput parsingTaskOutput, String remoteFilePath, String localFilePath, AdaptorSupport adaptorSupport) throws TaskOnFailException {
        logger.info("Uploading from local path " + localFilePath + " to remote path " + remoteFilePath + " of storage resource " + parsingTaskOutput.getStorageResourceId());
        try {
            StoragePreference gatewayStoragePreference = getRegistryServiceClient().getGatewayStoragePreference(gatewayId, parsingTaskOutput.getStorageResourceId());
            StorageResourceDescription storageResource = getRegistryServiceClient().getStorageResource(parsingTaskOutput.getStorageResourceId());

            ProcessModel processModel = getRegistryServiceClient().getProcess(processId);

            String remoteFileRoot = processModel.getExperimentDataDir();
            remoteFilePath = Paths.get(remoteFileRoot, remoteFilePath).toString();

            StorageResourceAdaptor storageResourceAdaptor = getStorageResourceAdaptor(parsingTaskOutput.getStorageResourceId(), adaptorSupport);
            storageResourceAdaptor.createDirectory(new File(remoteFilePath).getParent(), true);
            storageResourceAdaptor.uploadFile(localFilePath, remoteFilePath);

            logger.info("Uploading completed. Registering data product for path " + remoteFilePath);

            DataProductModel dataProductModel = new DataProductModel();
            dataProductModel.setGatewayId(getGatewayId());
            dataProductModel.setOwnerName("ParserTask");
            dataProductModel.setProductName(parsingTaskOutput.getId());
            dataProductModel.setDataProductType(DataProductType.FILE);

            DataReplicaLocationModel replicaLocationModel = new DataReplicaLocationModel();
            replicaLocationModel.setStorageResourceId(parsingTaskOutput.getStorageResourceId());
            replicaLocationModel.setReplicaName("Parsing task output " + parsingTaskOutput.getId());
            replicaLocationModel.setReplicaLocationCategory(ReplicaLocationCategory.GATEWAY_DATA_STORE);
            replicaLocationModel.setReplicaPersistentType(ReplicaPersistentType.TRANSIENT);

            URI destinationURI = new URI("file", gatewayStoragePreference.getLoginUserName(),
                    storageResource.getHostName(), 22, remoteFilePath, null, null);

            replicaLocationModel.setFilePath(destinationURI.toString());
            dataProductModel.addToReplicaLocations(replicaLocationModel);

            String productUri = getRegistryServiceClient().registerDataProduct(dataProductModel);

            logger.info("Data product is " + productUri + " for path " + remoteFilePath);

            setContextVariable(parsingTaskOutput.getContextVariableName(), productUri);
        } catch (Exception e) {
            logger.error("Failed to upload from local path " + localFilePath + " to remote path " + remoteFilePath +
                    " of storage resource " + parsingTaskOutput.getStorageResourceId(), e);
            throw new TaskOnFailException("Failed to upload from local path " + localFilePath + " to remote path " + remoteFilePath +
                    " of storage resource " + parsingTaskOutput.getStorageResourceId(), false, e);
        }
    }

    private String createLocalInputDir(String containerName) throws TaskOnFailException {
        String localInpDir = (localDataDir.endsWith(File.separator) ? localDataDir : localDataDir + File.separator) +
                "parsers" + File.separator + containerName + File.separator + "data" + File.separator + "input" + File.separator;
        try {
            FileUtils.forceMkdir(new File(localInpDir));
            return localInpDir;

        } catch (IOException e) {
            throw new TaskOnFailException("Failed to build input directories " + localInpDir, true, e);
        }
    }

    private String createLocalOutputDir(String containerName) throws TaskOnFailException {
        String localOutDir = (localDataDir.endsWith(File.separator) ? localDataDir : localDataDir + File.separator) +
                "parsers" + File.separator + containerName + File.separator + "data" + File.separator + "output" + File.separator;
        try {
            FileUtils.forceMkdir(new File(localOutDir));
            return localOutDir;

        } catch (IOException e) {
            throw new TaskOnFailException("Failed to build output directories " + localOutDir, true, e);
        }
    }


    private static RegistryService.Client getRegistryServiceClient() throws TaskOnFailException {
        try {
            final int serverPort = Integer.parseInt(ServerSettings.getRegistryServerPort());
            final String serverHost = ServerSettings.getRegistryServerHost();
            return RegistryServiceClientFactory.createRegistryClient(serverHost, serverPort);
        } catch (RegistryServiceException |ApplicationSettingsException e) {
            throw new TaskOnFailException("Unable to create registry client...", false, e);
        }
    }

    private static CredentialStoreService.Client getCredentialServiceClient() throws TaskOnFailException {
        try {
            final int serverPort = Integer.parseInt(ServerSettings.getRegistryServerPort());
            final String serverHost = ServerSettings.getRegistryServerHost();
            return CredentialStoreClientFactory.createAiravataCSClient(serverHost, serverPort);
        } catch (CredentialStoreException |ApplicationSettingsException e) {
            throw new TaskOnFailException("Unable to create credential client...", false, e);
        }
    }

    public String getParserId() {
        return parserId;
    }

    public void setParserId(String parserId) {
        this.parserId = parserId;
    }

    public ParsingTaskInputs getParsingTaskInputs() {
        return parsingTaskInputs;
    }

    public void setParsingTaskInputs(ParsingTaskInputs parsingTaskInputs) {
        this.parsingTaskInputs = parsingTaskInputs;
    }

    public ParsingTaskOutputs getParsingTaskOutputs() {
        return parsingTaskOutputs;
    }

    public void setParsingTaskOutputs(ParsingTaskOutputs parsingTaskOutputs) {
        this.parsingTaskOutputs = parsingTaskOutputs;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getGroupResourceProfileId() {
        return groupResourceProfileId;
    }

    public void setGroupResourceProfileId(String groupResourceProfileId) {
        this.groupResourceProfileId = groupResourceProfileId;
    }

    public String getLocalDataDir() {
        return localDataDir;
    }

    public void setLocalDataDir(String localDataDir) {
        this.localDataDir = localDataDir;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Integer getOutputVersion() {
        return outputVersion;
    }

    public void setOutputVersion(Integer outputVersion) {
        this.outputVersion = outputVersion;
    }
}
