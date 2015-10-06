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
 *
 */
package org.apache.airavata.gfac.impl.task;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.airavata.common.exception.AiravataException;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.common.utils.ServerSettings;
import org.apache.airavata.common.utils.ThriftUtils;
import org.apache.airavata.credential.store.credential.Credential;
import org.apache.airavata.credential.store.credential.impl.ssh.SSHCredential;
import org.apache.airavata.credential.store.store.CredentialReader;
import org.apache.airavata.credential.store.store.CredentialStoreException;
import org.apache.airavata.gfac.core.GFacException;
import org.apache.airavata.gfac.core.GFacUtils;
import org.apache.airavata.gfac.core.SSHApiException;
import org.apache.airavata.gfac.core.authentication.AuthenticationInfo;
import org.apache.airavata.gfac.core.authentication.SSHKeyAuthentication;
import org.apache.airavata.gfac.core.authentication.SSHPasswordAuthentication;
import org.apache.airavata.gfac.core.cluster.CommandInfo;
import org.apache.airavata.gfac.core.cluster.RawCommandInfo;
import org.apache.airavata.gfac.core.cluster.ServerInfo;
import org.apache.airavata.gfac.core.context.TaskContext;
import org.apache.airavata.gfac.core.task.Task;
import org.apache.airavata.gfac.core.task.TaskException;
import org.apache.airavata.gfac.impl.Factory;
import org.apache.airavata.gfac.impl.SSHUtils;
import org.apache.airavata.model.application.io.InputDataObjectType;
import org.apache.airavata.model.application.io.OutputDataObjectType;
import org.apache.airavata.model.commons.ErrorModel;
import org.apache.airavata.model.status.ProcessState;
import org.apache.airavata.model.status.TaskState;
import org.apache.airavata.model.status.TaskStatus;
import org.apache.airavata.model.task.DataStagingTaskModel;
import org.apache.airavata.model.task.TaskTypes;
import org.apache.airavata.registry.cpi.ExperimentCatalog;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * This will be used for both Input file staging and output file staging, hence if you do any changes to a part of logic
 * in this class please consider that will works with both input and output cases.
 */
public class AdvancedSCPDataStageTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(AdvancedSCPDataStageTask.class);
    private static final int DEFAULT_SSH_PORT = 22;
    private String password;
    private String publicKeyPath;
    private String passPhrase;
    private String privateKeyPath;
    private String userName;
    private String hostName;
    private String inputPath;

    @Override
    public void init(Map<String, String> propertyMap) throws TaskException {
        inputPath = propertyMap.get("inputPath");
        hostName = propertyMap.get("hostName");
        userName = propertyMap.get("userName");
    }

    @Override
    public TaskStatus execute(TaskContext taskContext) {
        TaskStatus status = new TaskStatus(TaskState.CREATED);
        AuthenticationInfo authenticationInfo = null;
        DataStagingTaskModel subTaskModel = null;
        String localDataDir = null;
        ProcessState processState = taskContext.getParentProcessContext().getProcessState();
        try {
            subTaskModel = (DataStagingTaskModel) ThriftUtils.getSubTaskModel
                    (taskContext.getTaskModel());
            if (processState == ProcessState.OUTPUT_DATA_STAGING) {
                OutputDataObjectType processOutput = taskContext.getProcessOutput();
                if (processOutput != null && processOutput.getValue() == null) {
                    log.error("expId: {}, processId:{}, taskId: {}:- Couldn't stage file {} , file name shouldn't be null",
                            taskContext.getExperimentId(), taskContext.getProcessId(), taskContext.getTaskId(),
                            processOutput.getName());
                    status = new TaskStatus(TaskState.FAILED);
                    if (processOutput.isIsRequired()) {
                        status.setReason("File name is null, but this output's isRequired bit is not set");
                    } else {
                        status.setReason("File name is null");
                    }
                    localDataDir = subTaskModel.getDestination();
                    return status;
                }
            } else if (processState == ProcessState.INPUT_DATA_STAGING) {
                InputDataObjectType processInput = taskContext.getProcessInput();
                if (processInput != null && processInput.getValue() == null) {
                    log.error("expId: {}, processId:{}, taskId: {}:- Couldn't stage file {} , file name shouldn't be null",
                            taskContext.getExperimentId(), taskContext.getProcessId(), taskContext.getTaskId(),
                            processInput.getName());
                    status = new TaskStatus(TaskState.FAILED);
                    if (processInput.isIsRequired()) {
                        status.setReason("File name is null, but this input's isRequired bit is not set");
                    } else {
                        status.setReason("File name is null");
                    }
                    return status;
                }
            } else {
                status.setState(TaskState.FAILED);
                status.setReason("Invalid task invocation, Support " + ProcessState.INPUT_DATA_STAGING.name() + " and " +
                        "" + ProcessState.OUTPUT_DATA_STAGING.name() + " process phases. found " + processState.name());
            }

            // use cp instead of scp if source and destination host and user name is same.
            URI sourceURI = new URI(subTaskModel.getSource());
            String fileName = sourceURI.getPath().substring(sourceURI.getPath().lastIndexOf(File.separator) + 1,
                    sourceURI.getPath().length());
            String targetPath = null;
            String targetFilePath = null;
            URI destinationURI = null;
            if (localDataDir != null) {
                targetPath = (inputPath.endsWith(File.separator) ? inputPath : inputPath + File.separator) +
                        taskContext.getParentProcessContext().getProcessId();
                targetFilePath = targetPath + File.separator + fileName;
                destinationURI = new URI("SCP", hostName, targetFilePath, null);
                subTaskModel.setDestination(destinationURI.toString());

            } else {
                destinationURI = new URI(subTaskModel.getDestination());
            }

            if (sourceURI.getHost().equalsIgnoreCase(destinationURI.getHost())
                    && sourceURI.getUserInfo().equalsIgnoreCase(destinationURI.getUserInfo())) {
                localDataCopy(taskContext, sourceURI, destinationURI);
                status.setState(TaskState.COMPLETED);
                status.setReason("Locally copied file using 'cp' command ");
                return status;
            }


            String tokenId = taskContext.getParentProcessContext().getTokenId();
            CredentialReader credentialReader = GFacUtils.getCredentialReader();
            Credential credential = credentialReader.getCredential(taskContext.getParentProcessContext().getGatewayId(), tokenId);
            if (credential instanceof SSHCredential) {
                SSHCredential sshCredential = (SSHCredential) credential;
                byte[] publicKey = sshCredential.getPublicKey();
                publicKeyPath = writeFileToDisk(publicKey);
                byte[] privateKey = sshCredential.getPrivateKey();
                privateKeyPath = writeFileToDisk(privateKey);
                passPhrase = sshCredential.getPassphrase();
//                userName = sshCredential.getPortalUserName(); // this might not same as login user name
                authenticationInfo = getSSHKeyAuthentication();
            } else {
                String msg = "Provided credential store token is not valid. Please provide the correct credential store token";
                log.error(msg);
                status.setState(TaskState.FAILED);
                status.setReason(msg);
                ErrorModel errorModel = new ErrorModel();
                errorModel.setActualErrorMessage(msg);
                errorModel.setUserFriendlyMessage(msg);
                taskContext.getTaskModel().setTaskError(errorModel);
                return status;
            }
            status = new TaskStatus(TaskState.COMPLETED);


            File templocalDataDir = GFacUtils.getLocalDataDir(taskContext);
            if (!templocalDataDir.exists()) {
                if (!templocalDataDir.mkdirs()) {
                    // failed to create temp output location
                }
            }

            String filePath = templocalDataDir + File.separator + fileName;

            ServerInfo serverInfo = new ServerInfo(userName, hostName, DEFAULT_SSH_PORT);
            Session sshSession = Factory.getSSHSession(authenticationInfo, serverInfo);
            if (processState == ProcessState.INPUT_DATA_STAGING) {
                inputDataStaging(taskContext, sshSession, sourceURI, destinationURI, filePath);
                status.setReason("Successfully staged input data");
            } else if (processState == ProcessState.OUTPUT_DATA_STAGING) {
                SSHUtils.makeDirectory(targetPath, sshSession);
                // TODO - save updated subtask model with new destination
                outputDataStaging(taskContext, sshSession, sourceURI, destinationURI, filePath);
                status.setReason("Successfully staged output data");
            }
        } catch (TException e) {
            String msg = "Couldn't create subTask model thrift model";
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
            return status;
        } catch (ApplicationSettingsException | FileNotFoundException | CredentialStoreException | IllegalAccessException | InstantiationException e) {
            String msg = "Failed while reading credentials";
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
        } catch (URISyntaxException e) {
            String msg = "Sorce or destination uri is not correct source : " + subTaskModel.getSource() + ", " +
                    "destination : " + subTaskModel.getDestination();
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
        } catch (SSHApiException e) {
            String msg = "Failed to do scp with compute resource";
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
        } catch (AiravataException e) {
            String msg = "Error while creating ssh session with client";
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
        } catch (JSchException | IOException e) {
            String msg = "Failed to do scp with client";
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
        } catch (GFacException e) {
            String msg = "Failed update experiment and process inputs and outputs";
            log.error(msg, e);
            status.setState(TaskState.FAILED);
            status.setReason(msg);
            ErrorModel errorModel = new ErrorModel();
            errorModel.setActualErrorMessage(e.getMessage());
            errorModel.setUserFriendlyMessage(msg);
            taskContext.getTaskModel().setTaskError(errorModel);
        }
        return status;
    }

    private void localDataCopy(TaskContext taskContext, URI sourceURI, URI destinationURI) throws SSHApiException {
        StringBuilder sb = new StringBuilder("rsync -cr ");
        sb.append(sourceURI.getPath()).append(" ").append(destinationURI.getPath());
        CommandInfo commandInfo = new RawCommandInfo(sb.toString());
        taskContext.getParentProcessContext().getRemoteCluster().execute(commandInfo);
    }

    private void inputDataStaging(TaskContext taskContext, Session sshSession, URI sourceURI, URI
            destinationURI, String filePath) throws SSHApiException, IOException, JSchException {
        /**
         * scp remote client file to airavata local dir.
         */
        SSHUtils.scpFrom(sourceURI.getPath(), filePath, sshSession);

        /**
         * scp local file to compute resource.
         */
        taskContext.getParentProcessContext().getRemoteCluster().scpTo(filePath, destinationURI.getPath());
    }

    private void outputDataStaging(TaskContext taskContext, Session sshSession, URI sourceURI, URI destinationURI,
                                   String filePath) throws SSHApiException, AiravataException, IOException, JSchException, GFacException {
        /**
         * scp remote file from comute resource to airavata local
         */
        taskContext.getParentProcessContext().getRemoteCluster().scpFrom(sourceURI.getPath(), filePath);

        /**
         * scp local file to remote client
         */
        SSHUtils.scpTo(filePath, destinationURI.getPath(), sshSession);
        // update output locations
        GFacUtils.saveExperimentOutput(taskContext.getParentProcessContext(), taskContext.getProcessOutput().getName(), destinationURI.getPath());
        GFacUtils.saveProcessOutput(taskContext.getParentProcessContext(), taskContext.getProcessOutput().getName(), destinationURI.getPath());

    }

    @Override
    public TaskStatus recover(TaskContext taskContext) {
        TaskState state = taskContext.getTaskStatus().getState();
        if (state == TaskState.EXECUTING || state == TaskState.CREATED) {
            return execute(taskContext);
        } else {
            // files already transferred or failed
            return taskContext.getTaskStatus();
        }
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.DATA_STAGING;
    }

    private SSHPasswordAuthentication getSSHPasswordAuthentication() {
        return new SSHPasswordAuthentication(userName, password);
    }

    private SSHKeyAuthentication getSSHKeyAuthentication() {
        SSHKeyAuthentication sshKA = new SSHKeyAuthentication();
        sshKA.setUserName(userName);
        sshKA.setPassphrase(passPhrase);
        sshKA.setPrivateKeyFilePath(privateKeyPath);
        sshKA.setPublicKeyFilePath(publicKeyPath);
        sshKA.setStrictHostKeyChecking("no");
        return sshKA;
    }

    private String writeFileToDisk(byte[] data) {
        File temp = null;
        try {
            temp = File.createTempFile("id_rsa", "");
            //write it
            FileOutputStream bw = new FileOutputStream(temp);
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return temp.getAbsolutePath();
    }
}