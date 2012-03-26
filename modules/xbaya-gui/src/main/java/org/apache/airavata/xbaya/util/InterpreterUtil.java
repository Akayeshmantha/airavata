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
package org.apache.airavata.xbaya.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.airavata.xbaya.XBayaException;
import org.apache.airavata.xbaya.XBayaRuntimeException;
import org.apache.airavata.xbaya.graph.DataPort;
import org.apache.airavata.xbaya.graph.Node;
import org.apache.airavata.xbaya.graph.amazon.InstanceNode;
import org.apache.airavata.xbaya.graph.system.ConstantNode;
import org.apache.airavata.xbaya.graph.system.EndForEachNode;
import org.apache.airavata.xbaya.graph.system.EndifNode;
import org.apache.airavata.xbaya.graph.system.ForEachNode;
import org.apache.airavata.xbaya.graph.system.InputNode;
import org.apache.airavata.xbaya.graph.system.SystemDataPort;
import org.apache.airavata.xbaya.graph.system.gui.DifferedInputNode;
import org.apache.airavata.xbaya.graph.ws.WSPort;
import org.apache.airavata.xbaya.interpretor.SystemComponentInvoker;
import org.apache.airavata.xbaya.interpretor.WorkFlowInterpreterException;
import org.apache.airavata.xbaya.invoker.GenericInvoker;
import org.apache.airavata.xbaya.invoker.Invoker;
import org.apache.airavata.xbaya.invoker.WorkflowInvokerWrapperForGFacInvoker;
import org.xmlpull.infoset.XmlElement;

import xsul5.XmlConstants;

public class InterpreterUtil {
    /**
     * This method returns the input values for given foreach node
     * @param forEachNode
     * @param listOfValues
     * @param invokerMap
     * @return
     * @throws XBayaException
     */
    public static Object getInputsForForEachNode(final ForEachNode forEachNode,
			final LinkedList<String> listOfValues, Map<Node, Invoker> invokerMap) throws XBayaException {
        List<DataPort> inputPorts = forEachNode.getInputPorts();

        Object returnValForProvenance = null;
        for(DataPort inputPort:inputPorts){


            Node forEachInputNode = inputPort.getFromNode();
            // if input node for for-each is WSNode
		if (forEachInputNode instanceof InputNode) {
			for (DataPort dataPort : forEachNode.getInputPorts()) {
				returnValForProvenance = InterpreterUtil
						.findInputFromPort(dataPort, invokerMap);
				if (null == returnValForProvenance) {
					throw new WorkFlowInterpreterException(
							"Unable to find input for the node:"
									+ forEachNode.getID());
				}
				String[] vals = returnValForProvenance.toString().split(",");
				listOfValues.addAll(Arrays.asList(vals));
			}
		} else {
			Invoker workflowInvoker = invokerMap
					.get(forEachInputNode);
			if (workflowInvoker != null) {
				if (workflowInvoker instanceof GenericInvoker) {

					returnValForProvenance = ((GenericInvoker) workflowInvoker)
							.getOutputs();
					String message = returnValForProvenance.toString();

					XmlElement msgElmt = XmlConstants.BUILDER
							.parseFragmentFromString(message);
					Iterator children = msgElmt.children().iterator();
					while (children.hasNext()) {
						Object object = children.next();
						// foreachWSNode.getInputPort(0).getType()
						if (object instanceof XmlElement) {
							listOfValues.add(XmlConstants.BUILDER
									.serializeToString(object));
							// TODO fix for simple type - Done
						}
					}
				} else if (workflowInvoker instanceof WorkflowInvokerWrapperForGFacInvoker) {
					String outputName = forEachInputNode.getOutputPort(0)
							.getName();
					returnValForProvenance = workflowInvoker
							.getOutput(outputName);
					org.xmlpull.v1.builder.XmlElement msgElmt = (org.xmlpull.v1.builder.XmlElement) returnValForProvenance;
					Iterator children = msgElmt.children();
					while (children.hasNext()) {
						Object object = children.next();
						if (object instanceof org.xmlpull.v1.builder.XmlElement) {
							org.xmlpull.v1.builder.XmlElement child = (org.xmlpull.v1.builder.XmlElement) object;
							Iterator valItr = child.children();
							if (valItr.hasNext()) {
								Object object2 = valItr.next();
								if (object2 instanceof String) {
									listOfValues.add(object2.toString());
								}
							}
						}
					}
				} else if (workflowInvoker instanceof SystemComponentInvoker) {
                    int index = forEachInputNode.getOutputPorts().indexOf(inputPort.getEdge(0).getFromPort());
                    String outputName = "";
                    if(forEachInputNode.getInputPort(index) instanceof SystemDataPort){
                       outputName = ((SystemDataPort)forEachInputNode.getInputPort(index)).getWSComponentPort().getName();
                    }else if(forEachInputNode.getInputPort(index) instanceof WSPort){
                         outputName = ((SystemDataPort)forEachInputNode.getInputPort(
                        forEachInputNode.getOutputPorts().indexOf(inputPort.getEdge(0).getFromPort()))).getWSComponentPort().getName();
                    }
					returnValForProvenance = workflowInvoker
							.getOutput(outputName);
					XmlElement msgElmt = XmlConstants.BUILDER
							.parseFragmentFromString("<temp>"
									+ returnValForProvenance + "</temp>");
					Iterator valItr = msgElmt.children().iterator();
					while (valItr.hasNext()) {
						Object object2 = valItr.next();
						if (object2 instanceof XmlElement) {
							listOfValues.add(((XmlElement) object2).children()
									.iterator().next().toString());
						}
					}
				}
			} else {
				throw new WorkFlowInterpreterException(
						"Did not find inputs from WS to foreach");
			}
		}
        }
		return returnValForProvenance;
	}

    /**
     *
     * @param inputPort
     * @param invokerMap
     * @return
     * @throws XBayaException
     */
	public static Object  findInputFromPort(DataPort inputPort, Map<Node,Invoker>  invokerMap) throws XBayaException {
		Object outputVal = null;
		Node fromNode = inputPort.getFromNode();
		if (fromNode instanceof InputNode) {
			outputVal = ((InputNode) fromNode).getDefaultValue();
		} else if (fromNode instanceof ConstantNode) {
			outputVal = ((ConstantNode) fromNode).getValue();
		} else if (fromNode instanceof DifferedInputNode && ((DifferedInputNode) fromNode).isConfigured()) {
			outputVal = ((DifferedInputNode) fromNode).getDefaultValue();
		} else if (fromNode instanceof EndifNode) {
			Invoker fromInvoker = invokerMap.get(fromNode);
			outputVal = fromInvoker.getOutput(inputPort.getFromPort().getID());
		} else if (fromNode instanceof InstanceNode) {
			return ((InstanceNode) fromNode).getOutputInstanceId();
		} else if (fromNode instanceof EndForEachNode) {
			outputVal = "";
			Invoker workflowInvoker = invokerMap.get(fromNode);
			String outputName = "";
            if (inputPort instanceof SystemDataPort) {
                outputName = ((SystemDataPort) inputPort).getWSComponentPort().getName();

            } else if (inputPort instanceof WSPort) {
                outputName = ((SystemDataPort)fromNode.getInputPort(
                        fromNode.getOutputPorts().indexOf(inputPort.getEdge(0).getFromPort()))).getWSComponentPort().getName();
            }
			XmlElement msgElmt = XmlConstants.BUILDER
					.parseFragmentFromString("<temp>"
							+ workflowInvoker.getOutput(outputName) + "</temp>");
			Iterator valItr = msgElmt.children().iterator();
			while (valItr.hasNext()) {
				Object object2 = valItr.next();
				if (object2 instanceof XmlElement) {

                    if(((XmlElement) object2).children().iterator().hasNext()){
					outputVal = outputVal
							+ ","
							+ ((XmlElement) object2).children().iterator()
									.next().toString();
                    }
				}
			}

            if (((String) outputVal).length() == 0) {
                throw new XBayaException("Empty Output Generated");
            }
            outputVal = ((String) outputVal).substring(1,
                    ((String) outputVal).length());
		} else {
			Invoker fromInvoker = invokerMap.get(fromNode);
			try {
				if (fromInvoker != null)
					outputVal = fromInvoker.getOutput(inputPort.getFromPort()
							.getName());



			} catch (Exception e) {
				// if the value is still null look it up from the inputport name
				// because the value is set to the input port name at some point
				// there is no harm in doing this
				if (null == outputVal) {
					outputVal = fromInvoker.getOutput(inputPort.getName());
				}
			}

		}
		return outputVal;

	}

	/**
	 * @param node
	 * @return
	 */
	public static Node findEndForEachFor(ForEachNode node) {

		Collection<Node> toNodes = node.getOutputPort(0).getToNodes();
		if(toNodes.size() != 1){
			throw new XBayaRuntimeException("ForEach output does not contain single out-edge");
		}
		Node middleNode = toNodes.iterator().next();
		List<DataPort> outputPorts = middleNode.getOutputPorts();
		for (DataPort dataPort : outputPorts) {
			if(dataPort.getToNodes().size() == 1){
				Node possibleEndForEachNode = dataPort.getToNodes().get(0);
				if(possibleEndForEachNode instanceof EndForEachNode){
					return possibleEndForEachNode;
				}
			}
		}
		throw new XBayaRuntimeException("EndForEachNode not found");
	}

    public static Integer[] getNumberOfInputsForForEachNode(final ForEachNode forEachNode,
                                                            Map<Node,Invoker> invokerMap) throws XBayaException {
        List<DataPort> inputPorts = forEachNode.getInputPorts();
        Integer[] inputNumbers = new Integer[inputPorts.size()];
        for(DataPort forEachInputPort:inputPorts){
            // if input node for for-each is WSNode
            Node forEachInputNode = forEachInputPort.getFromNode();
            int index = 0;
            Object returnValForProvenance = null;
            if (forEachInputNode instanceof InputNode) {
                returnValForProvenance = InterpreterUtil
                        .findInputFromPort(forEachInputPort, invokerMap);
                if (null == returnValForProvenance) {
                    throw new WorkFlowInterpreterException(
                            "Unable to find input for the node:"
                                    + forEachNode.getID());
                }
                String[] vals = returnValForProvenance.toString().split(",");
                inputNumbers[inputPorts.indexOf(forEachInputPort)] = vals.length;
        } else {
			Invoker workflowInvoker = invokerMap
					.get(forEachInputNode);
			if (workflowInvoker != null) {
				if (workflowInvoker instanceof GenericInvoker) {

					returnValForProvenance = ((GenericInvoker) workflowInvoker)
							.getOutputs();
					String message = returnValForProvenance.toString();

					XmlElement msgElmt = XmlConstants.BUILDER
							.parseFragmentFromString(message);
					Iterator children = msgElmt.children().iterator();
					while (children.hasNext()) {
						Object object = children.next();
						// foreachWSNode.getInputPort(0).getType()
						if (object instanceof XmlElement) {
							index++;
						}
					}
				} else if (workflowInvoker instanceof WorkflowInvokerWrapperForGFacInvoker) {
                    String outputName = forEachInputNode.getOutputPort(0)
							.getName();
					returnValForProvenance = workflowInvoker
							.getOutput(outputName);
					org.xmlpull.v1.builder.XmlElement msgElmt = (org.xmlpull.v1.builder.XmlElement) returnValForProvenance;
					Iterator children = msgElmt.children();
					while (children.hasNext()) {
						Object object = children.next();
						if (object instanceof org.xmlpull.v1.builder.XmlElement) {
							org.xmlpull.v1.builder.XmlElement child = (org.xmlpull.v1.builder.XmlElement) object;
							Iterator valItr = child.children();
							if (valItr.hasNext()) {
								Object object2 = valItr.next();
								if (object2 instanceof String) {
									index++;
								}
							}
						}
					}
                        inputNumbers[inputPorts.indexOf(forEachInputPort)] = index;
				} else if (workflowInvoker instanceof SystemComponentInvoker) {
				    int portIndex = forEachInputNode.getOutputPorts().indexOf(forEachInputPort.getEdge(0).getFromPort());
                    String outputName = "";
                    if(forEachInputNode.getInputPort(portIndex) instanceof SystemDataPort){
                       outputName = ((SystemDataPort)forEachInputNode.getInputPort(portIndex)).getWSComponentPort().getName();
                    }else if(forEachInputNode.getInputPort(portIndex) instanceof WSPort){
                        outputName = ((WSPort)forEachInputNode.getInputPort(portIndex)).getComponentPort().getName();
                    }
					returnValForProvenance = workflowInvoker
							.getOutput(outputName);
					XmlElement msgElmt = XmlConstants.BUILDER
							.parseFragmentFromString("<temp>"
									+ returnValForProvenance + "</temp>");
					Iterator valItr = msgElmt.children().iterator();
					while (valItr.hasNext()) {
						Object object2 = valItr.next();
						if (object2 instanceof XmlElement) {
							index++;
						}
					}
                        inputNumbers[inputPorts.indexOf(forEachInputPort)] = index;
				}

            } else {
				throw new WorkFlowInterpreterException(
						"Did not find inputs from WS to foreach");
			}
        }
    }
        return inputNumbers;
    }
}
