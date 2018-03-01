/**
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
package org.apache.airavata.registry.core.repositories.appcatalog;

import org.apache.airavata.model.appcatalog.appdeployment.*;
import org.apache.airavata.registry.core.entities.appcatalog.*;
import org.apache.airavata.registry.core.utils.DBConstants;
import org.apache.airavata.registry.core.utils.ObjectMapperSingleton;
import org.apache.airavata.registry.core.utils.QueryConstants;
import org.apache.airavata.registry.cpi.AppCatalogException;
import org.apache.airavata.registry.cpi.ApplicationDeployment;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationDeploymentRepository extends AppCatAbstractRepository<ApplicationDeploymentDescription, ApplicationDeploymentEntity, String> implements ApplicationDeployment {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationDeploymentRepository.class);

    public ApplicationDeploymentRepository() {
        super(ApplicationDeploymentDescription.class, ApplicationDeploymentEntity.class);
    }

    protected String saveApplicationDeploymentDescriptorData(
            ApplicationDeploymentDescription applicationDeploymentDescription) throws AppCatalogException {
        ApplicationDeploymentEntity applicationDeploymentEntity = saveApplicationDeployment(applicationDeploymentDescription);
        return applicationDeploymentEntity.getDeploymentId();
    }

    protected ApplicationDeploymentEntity saveApplicationDeployment(
            ApplicationDeploymentDescription applicationDeploymentDescription) throws AppCatalogException {
        String applicationDeploymentId = applicationDeploymentDescription.getAppDeploymentId();
        Mapper mapper = ObjectMapperSingleton.getInstance();
        ApplicationDeploymentEntity applicationDeploymentEntity = mapper.map(applicationDeploymentDescription, ApplicationDeploymentEntity.class);
        if (applicationDeploymentEntity.getPreJobCommands() != null) {
            applicationDeploymentEntity.getPreJobCommands().forEach(prejobCommandEntity -> prejobCommandEntity.setAppdeploymentId(applicationDeploymentId));
        }
        if (applicationDeploymentEntity.getPostJobCommands() != null) {
            applicationDeploymentEntity.getPostJobCommands().forEach(postjobCommandEntity -> postjobCommandEntity.setAppdeploymentId(applicationDeploymentId));
        }
        if (applicationDeploymentEntity.getLibPrependPaths() != null) {
            applicationDeploymentEntity.getLibPrependPaths().forEach(libraryPrependPathEntity -> libraryPrependPathEntity.setDeploymentId(applicationDeploymentId));
        }
        if (applicationDeploymentEntity.getLibAppendPaths() != null) {
            applicationDeploymentEntity.getLibAppendPaths().forEach(libraryApendPathEntity -> libraryApendPathEntity.setDeploymentId(applicationDeploymentId));
        }
        if (applicationDeploymentEntity.getSetEnvironment() != null) {
            applicationDeploymentEntity.getSetEnvironment().forEach(appEnvironmentEntity -> appEnvironmentEntity.setDeploymentId(applicationDeploymentId));
        }
        return execute(entityManager -> entityManager.merge(applicationDeploymentEntity));
    }

    @Override
    public String addApplicationDeployment(ApplicationDeploymentDescription applicationDeploymentDescription, String gatewayId) throws AppCatalogException {
        return saveApplicationDeploymentDescriptorData(applicationDeploymentDescription);
    }

    @Override
    public void updateApplicationDeployment(String deploymentId, ApplicationDeploymentDescription updatedApplicationDeploymentDescription) throws AppCatalogException {
        saveApplicationDeploymentDescriptorData(updatedApplicationDeploymentDescription);
    }

    @Override
    public ApplicationDeploymentDescription getApplicationDeployement(String deploymentId) throws AppCatalogException {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(DBConstants.ApplicationDeployment.APPLICATION_DEPLOYMENT_ID, deploymentId);
        ApplicationDeploymentDescription applicationDeploymentDescription = (ApplicationDeploymentDescription) select(QueryConstants.FIND_APPLICATION_DEPLOYMENT, -1, 0, queryParameters);
        return applicationDeploymentDescription;
    }

    @Override
    public List<ApplicationDeploymentDescription> getApplicationDeployements(Map<String, String> filters) throws AppCatalogException {
        if(filters.containsKey(DBConstants.ApplicationDeployment.APPLICATION_MODULE_ID)) {
            Map<String, Object> queryParameters = new HashMap<>();
            queryParameters.put(DBConstants.ApplicationDeployment.APPLICATION_MODULE_ID, filters.get(DBConstants.ApplicationDeployment.APPLICATION_MODULE_ID));
            List<ApplicationDeploymentDescription> applicationDeploymentDescriptionList = select(QueryConstants.FIND_APPLICATION_DEPLOYMENTS_FOR_APPLICATION_MODULE_ID, -1, 0, queryParameters);
            return applicationDeploymentDescriptionList;
        } else if(filters.containsKey(DBConstants.ApplicationDeployment.COMPUTE_HOST_ID)) {
            Map<String, Object> queryParameters = new HashMap<>();
            queryParameters.put(DBConstants.ApplicationDeployment.COMPUTE_HOST_ID, filters.get(DBConstants.ApplicationDeployment.COMPUTE_HOST_ID));
            List<ApplicationDeploymentDescription> applicationDeploymentDescriptionList = select(QueryConstants.FIND_APPLICATION_DEPLOYMENTS_FOR_COMPUTE_HOST_ID, -1, 0, queryParameters);
            return applicationDeploymentDescriptionList;
        } else {
            logger.error("Unsupported field name for app deployment.", new IllegalArgumentException());
            throw new IllegalArgumentException("Unsupported field name for app deployment.");
        }
    }

    @Override
    public List<ApplicationDeploymentDescription> getAllApplicationDeployements(String gatewayId) throws AppCatalogException {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(DBConstants.ApplicationDeployment.GATEWAY_ID, gatewayId);
        List<ApplicationDeploymentDescription> applicationDeploymentDescriptionList = select(QueryConstants.FIND_APPLICATION_DEPLOYMENTS_FOR_GATEWAY_ID, -1, 0, queryParameters);
        return applicationDeploymentDescriptionList;
    }

    @Override
    public List<ApplicationDeploymentDescription> getAccessibleApplicationDeployements(String gatewayId, List<String> accessibleAppIds) throws AppCatalogException {
        List<ApplicationDeploymentDescription> accessibleApplicationDeployments = new ArrayList<>();
        for(ApplicationDeploymentDescription description : getAllApplicationDeployements(gatewayId)) {
            if(accessibleAppIds.contains(description.getAppDeploymentId())) {
                accessibleApplicationDeployments.add(description);
            }
        }
        return accessibleApplicationDeployments;
    }

    @Override
    public List<String> getAllApplicationDeployementIds() throws AppCatalogException {
        List<String> applicationDeploymentIds = new ArrayList<>();
        List<ApplicationDeploymentDescription> applicationDeploymentDescriptionList = select(QueryConstants.GET_ALL_APPLICATION_DEPLOYMENTS, 0);
        if (applicationDeploymentDescriptionList != null && !applicationDeploymentDescriptionList.isEmpty()) {
            for (ApplicationDeploymentDescription applicationDeploymentDescription: applicationDeploymentDescriptionList) {
                applicationDeploymentIds.add(applicationDeploymentDescription.getAppDeploymentId());
            }
        }
        return applicationDeploymentIds;
    }

    @Override
    public boolean isAppDeploymentExists(String deploymentId) throws AppCatalogException {
        return isExists(deploymentId);
    }

    @Override
    public void removeAppDeployment(String deploymentId) throws AppCatalogException {
        delete(deploymentId);
    }

}
