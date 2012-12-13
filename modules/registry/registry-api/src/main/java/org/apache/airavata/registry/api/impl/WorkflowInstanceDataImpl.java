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

package org.apache.airavata.registry.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.airavata.registry.api.impl.ExperimentDataImpl;
import org.apache.airavata.registry.api.workflow.*;
import org.apache.airavata.registry.api.workflow.WorkflowInstanceStatus.ExecutionStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class WorkflowInstanceDataImpl implements WorkflowInstanceData {
	private WorkflowInstance workflowInstance;

    @XmlTransient
	private ExperimentDataImpl experimentData;
	private WorkflowInstanceStatus workflowInstanceStatus;
	
	private List<WorkflowInstanceNodeData> nodeDataList;

    public WorkflowInstanceDataImpl() {
    }

    public WorkflowInstanceDataImpl(ExperimentData experimentData, WorkflowInstance workflowInstance, WorkflowInstanceStatus workflowInstanceStatus, List<WorkflowInstanceNodeData> nodeDataList) {
		this.experimentData= (ExperimentDataImpl)experimentData;
		this.workflowInstance=workflowInstance;
		this.workflowInstanceStatus=workflowInstanceStatus;
		this.nodeDataList=nodeDataList;
	}

    public WorkflowInstance getWorkflowInstance() {
        return workflowInstance;
    }

    public List<WorkflowInstanceNodeData> getNodeDataList() {
		if (nodeDataList==null){
			nodeDataList=new ArrayList<WorkflowInstanceNodeData>();
		}
		return nodeDataList;
	}
	
	public void addNodeData(WorkflowInstanceNodeData...nodeData){
		getNodeDataList().addAll(Arrays.asList(nodeData));
	}
	
	public String getExperimentId(){
		return workflowInstance.getExperimentId();
	}

    /**
     * @deprecated Use "getWorkflowInstanceID() instead
     * @return
     */
	public String getTopicId(){
		return workflowInstance.getWorkflowInstanceId();
	}

    public String getWorkflowInstanceId(){
        return workflowInstance.getWorkflowInstanceId();
    }
	
	/**
	 * @deprecated Use "WorkflowInstanceData.getTemplateName()" instead
	 * @return
	 */
	public String getWorkflowName(){
		return getTemplateName();
	}
	
	public String getTemplateName(){
		return workflowInstance.getTemplateName();
	}
	
	public ExecutionStatus getStatus(){
		return workflowInstanceStatus.getExecutionStatus();
	}
	
	public Date getStatusUpdateTime(){
		return workflowInstanceStatus.getStatusUpdateTime();
	}

	public WorkflowInstanceNodeData getNodeData(String nodeId){
		for (WorkflowInstanceNodeData nodeData : getNodeDataList()) {
			if (nodeData.getWorkflowInstanceNode().getNodeId().equals(nodeId)){
				return nodeData;
			}
		}
		return null;
	}

	public ExperimentDataImpl getExperimentData() {
		return experimentData;
	}

	public void setExperimentData(ExperimentDataImpl experimentData) {
		this.experimentData = experimentData;
	}

    @Override
    public List<WorkflowInstanceNodePortData> getWorkflowInput(String worklfowInstanceID) {
        List<WorkflowInstanceNodePortData> workflowInstanceNodePortDatas = new ArrayList<WorkflowInstanceNodePortData>();
        for (WorkflowInstanceNodeData workflowInstanceNodeData : getNodeDataList()){
            if(getWorkflowNodeType(workflowInstanceNodeData, WorkflowNodeType.WorkflowNode.INPUTNODE)){
                 workflowInstanceNodePortDatas.addAll(workflowInstanceNodeData.getInputData());
            }
        }
        return workflowInstanceNodePortDatas;
    }

    private boolean getWorkflowNodeType(WorkflowInstanceNodeData workflowInstanceNodeData, WorkflowNodeType.WorkflowNode nodeType ) {
        return workflowInstanceNodeData.getType().equals(nodeType);
    }

    @Override
    public List<WorkflowInstanceNodePortData> getWorkflowOutput(String worklfowInstanceID) {
        List<WorkflowInstanceNodePortData> workflowInstanceNodePortDatas = new ArrayList<WorkflowInstanceNodePortData>();
        for (WorkflowInstanceNodeData workflowInstanceNodeData : getNodeDataList()){
            if(getWorkflowNodeType(workflowInstanceNodeData, WorkflowNodeType.WorkflowNode.OUTPUTNODE)){
                workflowInstanceNodePortDatas.addAll(workflowInstanceNodeData.getInputData());
            }
        }
        return workflowInstanceNodePortDatas;
    }

}
