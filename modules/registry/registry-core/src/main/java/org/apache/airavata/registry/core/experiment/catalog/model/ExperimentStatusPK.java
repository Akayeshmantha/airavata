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
package org.apache.airavata.registry.core.experiment.catalog.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class ExperimentStatusPK implements Serializable {
    private final static Logger logger = LoggerFactory.getLogger(ExperimentStatusPK.class);
    private String statusId;
    private String experimentId;

    @Id
    @Column(name = "STATUS_ID")
    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    @Id
    @Column(name = "EXPERIMENT_ID")
    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExperimentStatusPK that = (ExperimentStatusPK) o;

        if (statusId != null ? !statusId.equals(that.statusId) : that.statusId != null) return false;
        if (experimentId != null ? !experimentId.equals(that.experimentId) : that.experimentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusId != null ? statusId.hashCode() : 0;
        result = 31 * result + (experimentId != null ? experimentId.hashCode() : 0);
        return result;
    }
}