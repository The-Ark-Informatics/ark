/**
 * XPDLReader.java
 * Copyright 2004 Neuragenix, Pty. Ltd. All rights reserved.
 * Created Date: 03/02/2004
 * 
 * Last Modified: (Date\Author\Comments)
 * 12/02/2004		Agustian Agustian		Commenting
 * 19/02/2004		Agustian Agustian		read activity parameter
 * 24/02/2004		Agustian Agustian		modify activity parameter to be an object
 *
 * 
 */

package neuragenix.genix.workflow.util;

import java.util.Vector;
import java.util.Hashtable;
import java.io.InputStream;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

// neuragenix libraries
import neuragenix.genix.workflow.*;
import neuragenix.genix.workflow.Package;

/**
 * XPDLReader - Reads Process Definition objects from XPDL
 * @author     <a href='mailto:aagustian@neuragenix.com'>Agustian Agustian</a>
 */
public class XPDLReader 
{
	// Error types
	private static final String DUPLICATE = "DUPLICATE";
	private static final String NON_EXISTENCE_ACTIVITY = "NON_EXISTENCE_ACTIVITY";
	
	// List of domains in the workflow world
	private static final String WORKFLOW_PACKAGE = "WORKFLOW_PACKAGE";
	private static final String WORKFLOW_PACKAGE_HEADER = "WORKFLOW_PACKAGE_HEADER";
	private static final String WORKFLOW_PROCESS = "WORKFLOW_TEMPLATE";
	private static final String WORKFLOW_PROCESS_HEADER = "WORKFLOW_TEMPLATE_HEADER";
	private static final String WORKFLOW_TRIGGER = "WORKFLOW_TRIGGER";
	private static final String WORKFLOW_ACTIVITY = "WORKFLOW_ACTIVITY";
	private static final String WORKFLOW_ACTIVITY_TRANSITION = "WORKFLOW_ACTIVITY_TRANSITION";
	private static final String WORKFLOW_PARTICIPANT = "WORKFLOW_PARTICIPANT";

	// the workflow Package
	private static Package workflowPackage = null;

	// the error message
	private static String strErrMsg = null;
	
	/**
	 * Default constructor
	 */
	public XPDLReader()
	{
	}

	/**
	 * parse the XML to object model.
	 * 
	 * @param file		  the XML file to be parse
	 */
    public static String parseXML(InputStream file)
    {
		// create a document factory instance
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
	    XPDLReader xpdlReader = new XPDLReader();
		
		try
		{
			Node currentPackage = null;
			NamedNodeMap packageAttributes = null;
			String strPackageId = null;
			String strPackageName = null;
			NodeList childNodes = null;

			// parse the XML file
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(file);

			// add package
			NodeList packageNodes = document.getElementsByTagName("Package");
			for(int intIndex=0; intIndex < packageNodes.getLength(); intIndex++)
			{
				currentPackage = packageNodes.item(intIndex);
				packageAttributes = currentPackage.getAttributes();

				// getting the Package Id and Name
				strPackageId = packageAttributes.getNamedItem("Id").getNodeValue();
				strPackageName = packageAttributes.getNamedItem("Name").getNodeValue();

				// instantiate the package object to be filled while parsing the XML
				workflowPackage = new Package();

				// set the id and name of the package
				workflowPackage.setId(strPackageId);
				workflowPackage.setName(strPackageName);
			
				// traverse through all the child nodes
				childNodes = currentPackage.getChildNodes();
				for(int intIndex1=0; intIndex1 < childNodes.getLength(); intIndex1++)
				{
					Node currentNode = childNodes.item(intIndex1);
					
					if(currentNode.getNodeType() == Node.ELEMENT_NODE)
					{
						if(currentNode.getNodeName().equals("PackageHeader"))
						{
							// read the Package Header
							xpdlReader.readPackageHeader(currentNode, workflowPackage);
						}
						else if(currentNode.getNodeName().equals("Participants"))
						{
							// read the Participants
							xpdlReader.readParticipants(currentNode, workflowPackage);

							if(XPDLReader.strErrMsg != null)
							{
								return XPDLReader.strErrMsg;
							}
						}
						else if(currentNode.getNodeName().equals("WorkflowProcesses"))
						{
							// read the Workflow Processes/Templates 
							xpdlReader.readWorkflowProcesses(currentNode, workflowPackage);

							if(XPDLReader.strErrMsg != null)
							{
								return XPDLReader.strErrMsg;
							}
						}
						else
						{
							System.err.println("XPDLReader::parseXML - Unknown Tag " +
									currentNode.getNodeName() + " XPDL file corrupted");
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//System.err.println("in parseXML --> strErrMsg = " + XPDLReader.strErrMsg);

		return XPDLReader.strErrMsg;
    }

	/**
	 * parse the Package Header
	 * 
	 * @param parentNode		the parent node of package header which is package
	 * @param workflowPackage	the workflow package
	 */
	private void readPackageHeader(Node parentNode, Package workflowPackage)
	{

		Node currentNode = null;
		PackageHeader packageHeader = new PackageHeader();

		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("XPDLVersion"))
				{
					packageHeader.setXPDLVersion(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Vendor"))
				{
					packageHeader.setVendor(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Created"))
				{
					packageHeader.setCreated(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Description"))
				{
					packageHeader.setDescription(nodeValue(currentNode));
				}
				else
				{
					System.err.println("XPDLReader::readPackageHeader - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowPackage.setPackageHeader(packageHeader);
	}

	/**
	 * parse the Participants
	 * 
	 * @param parentNode		the parent node of participants which is package
	 * @param workflowPackage	the workflow package
	 */
	private void readParticipants(Node parentNode, Package workflowPackage)
	{
		Node currentNode = null;
		Hashtable hashParticipants = new Hashtable();
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Participant"))
				{
					readParticipant(currentNode, hashParticipants);
				}
				else
				{
					System.err.println("XPDLReader::readParticipants - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowPackage.setParticipants(hashParticipants);
	}

	/**
	 * parse the Participant
	 * 
	 * @param parentNode		the parent node of participant which is participants
	 * @param hashParticipants	the workflow participants
	 */
	private void readParticipant(Node parentNode, Hashtable hashParticipants)
	{
		Node currentNode = null;
		NamedNodeMap participantTypeAttributes = null;

		NamedNodeMap participantAttributes = parentNode.getAttributes();

		// getting the Participant Id and Name
		String strParticipantId = participantAttributes.getNamedItem("Id").getNodeValue();
		String strParticipantName = participantAttributes.getNamedItem("Name").getNodeValue();

		// instantiate the participant object to be filled while parsing the XML
		Participant participant = new Participant();

		// set the id and name of the participant 
		participant.setId(strParticipantId);
		participant.setName(strParticipantName);
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("ParticipantType"))
				{
					participantTypeAttributes = currentNode.getAttributes();
					participant.setType(participantTypeAttributes.getNamedItem("Type").getNodeValue());
				}
				else if(currentNode.getNodeName().equals("Description"))
				{
					participant.setDescription(nodeValue(currentNode));
				}
				else
				{
					System.err.println("XPDLReader::readParticipant - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		if(hashParticipants.put(strParticipantId, participant) != null)
		{
			XPDLReader.appendErrorMessage(DUPLICATE, WORKFLOW_PARTICIPANT,
											  strParticipantId);
		}
	}

	/**
	 * parse the Workflow Processes
	 * 
	 * @param parentNode		the parent node of workflow processes which is package
	 * @param workflowPackage	the workflow package
	 */
	private void readWorkflowProcesses(Node parentNode, Package workflowPackage)
	{
		Node currentNode = null;
		Hashtable hashWorkflowProcesses = new Hashtable();
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("WorkflowProcess"))
				{
					readWorkflowProcess(currentNode, hashWorkflowProcesses);
				}
				else
				{
					System.err.println("XPDLReader::readWorkflowProcesses - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowPackage.setWorkflowProcesses(hashWorkflowProcesses);
	}

	/**
	 * parse the Workflow Process
	 * 
	 * @param parentNode		the parent node of workflow process which is workflow processes
	 * @param hashWorkflowProcesses 	the workflow processes
	 */
	private void readWorkflowProcess(Node parentNode, Hashtable hashWorkflowProcesses)
	{
		Node currentNode = null;
		Node activitiesNode = null;

		NamedNodeMap workflowProcessAttributes = parentNode.getAttributes();

		// getting the workflow process Id and Name
		String strWorkflowProcessId = workflowProcessAttributes.getNamedItem("Id").getNodeValue();
		String strWorkflowProcessName = workflowProcessAttributes.getNamedItem("Name").getNodeValue();
		String strInitialActivityId = workflowProcessAttributes.getNamedItem("InitialActivityId").getNodeValue();

		// instantiate the WorkflowProcess object to be filled while parsing the XML
		WorkflowProcess workflowProcess = new WorkflowProcess();

		// set the id and name of the WorkflowProcess 
		workflowProcess.setId(strWorkflowProcessId);
		workflowProcess.setName(strWorkflowProcessName);
		workflowProcess.setInitialActivityId(strInitialActivityId);

		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("ProcessHeader"))
				{
					readProcessHeader(currentNode, workflowProcess);
				}
				//agus start
				else if(currentNode.getNodeName().equals("Parameters"))
				{
					readParameters(currentNode, workflowProcess);		
				}
				//agus end
				else if(currentNode.getNodeName().equals("Triggers"))
				{
					readTriggers(currentNode, workflowProcess);		
				}
				else if(currentNode.getNodeName().equals("Activities"))
				{
					activitiesNode = currentNode;
					readActivities(currentNode, workflowProcess);
				}
				else if(currentNode.getNodeName().equals("Transitions"))
				{
					readTransitions(currentNode, workflowProcess,
									activitiesNode);
				}
				else
				{
					System.err.println("XPDLReader::readWorkflowProcess - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		if(hashWorkflowProcesses.put(strWorkflowProcessId, workflowProcess) != null)
		{
			XPDLReader.appendErrorMessage(DUPLICATE, WORKFLOW_PROCESS,
											   strWorkflowProcessId);
		}
	}

	/**
	 * parse the Process Header
	 * 
	 * @param parentNode		the parent node of process header which is workflow process
	 * @param workflowProcess	the workflow process 
	 */
	private void readProcessHeader(Node parentNode, WorkflowProcess workflowProcess)
	{
		Node currentNode = null;
		ProcessHeader processHeader = new ProcessHeader();

		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Created"))
				{
					processHeader.setCreated(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Description"))
				{
					processHeader.setDescription(nodeValue(currentNode));
				}
				else
				{
					System.err.println("XPDLReader::readProcessHeader - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowProcess.setProcessHeader(processHeader);
	}

	/**
	 * parse the Parameters
	 * 
	 * @param parentNode		the parent node of parameters is workflow process
	 * @param workflowProcess	the workflow process
	 */
	private void readParameters(Node parentNode, WorkflowProcess workflowProcess)
	{
		Node currentNode = null;
		//Vector vecParameters = new Vector();
		Hashtable hashParameters = new Hashtable(); 
		
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Parameter"))
				{
					//readParameter(currentNode, vecParameters);
					readParameter(currentNode, hashParameters);
				}
				else
				{
					System.err.println("XPDLReader::readParameters - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		//activity.setParameters(vecParameters);
		workflowProcess.setParameters(hashParameters);
	}

	/**
	 * parse the Triggers
	 * 
	 * @param parentNode		the parent node of triggers is workflow process
	 * @param workflowProcess   the workflow process
	 */
	private void readTriggers(Node parentNode, WorkflowProcess workflowProcess)
	{
		Node currentNode = null;
		Hashtable hashTriggers = new Hashtable();
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Trigger"))
				{
					readTrigger(currentNode, hashTriggers);
				}
				else
				{
					System.err.println("XPDLReader::readTriggers - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowProcess.setTriggers(hashTriggers);
	}

	/**
	 * parse the Trigger
	 * 
	 * @param parentNode		the parent node of trigger is triggers
	 * @param hashTriggers		the workflow triggers
	 */
	private void readTrigger(Node parentNode, Hashtable hashTriggers)
	{
		Node currentNode = null;

		NamedNodeMap TriggerAttributes = parentNode.getAttributes();

		// getting the Trigger Id, Name and Action
		String strTriggerId = TriggerAttributes.getNamedItem("Id").getNodeValue();
		String strTriggerName = TriggerAttributes.getNamedItem("Name").getNodeValue();
		String strTriggerAction = TriggerAttributes.getNamedItem("Action").getNodeValue();

		// instantiate the trigger object to be filled while parsing the XML
		Trigger trigger = new Trigger();

		// set the id, name and action of the Trigger 
		trigger.setId(strTriggerId);
		trigger.setName(strTriggerName);
		trigger.setAction(strTriggerAction);
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Domain"))
				{
					trigger.setDomain(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Field"))
				{
					trigger.setField(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Type"))
				{
					trigger.setType(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Connection1"))
				{
					trigger.setConnection1(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Operator1"))
				{
					trigger.setOperator1(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Value1"))
				{
					trigger.setValue1(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Connection2"))
				{
					trigger.setConnection2(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Operator2"))
				{
					trigger.setOperator2(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Value2"))
				{
					trigger.setValue2(nodeValue(currentNode));
				}
				else
				{
					System.err.println("XPDLReader::readTrigger - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		if(hashTriggers.put(strTriggerId, trigger) != null)
		{
			XPDLReader.appendErrorMessage(DUPLICATE, WORKFLOW_TRIGGER, strTriggerId);
		}
	}

	/**
	 * parse the Activities
	 * 
	 * @param parentNode		the parent node of activities is workflow process 
	 * @param workflowProcess	the workflow process
	 */
	private void readActivities(Node parentNode, WorkflowProcess workflowProcess)
	{
		Node currentNode = null;
		Hashtable hashActivities = new Hashtable();
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Activity"))
				{
					readActivity(currentNode, hashActivities);
				}
				else
				{
					System.err.println("XPDLReader::readActivities - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowProcess.setActivities(hashActivities);
	}

	/**
	 * parse the Activity
	 * 
	 * @param parentNode		the parent node of activity is activities 
	 * @param hashActivities 	the workflow activities
	 */
	private void readActivity(Node parentNode, Hashtable hashActivities)
	{
		Node currentNode = null;

		NamedNodeMap ActivityAttributes = parentNode.getAttributes();

		// getting the Activity Id and Name
		String strActivityId = ActivityAttributes.getNamedItem("Id").getNodeValue();
		String strActivityName = ActivityAttributes.getNamedItem("Name").getNodeValue();

		// instantiate the Activity object to be filled while parsing the XML
		Activity activity = new Activity();

		// set the id and name of the Activity 
		activity.setId(strActivityId);
		activity.setName(strActivityName);
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Description"))
				{
					activity.setDescription(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Performer"))
				{
					activity.setPerformer(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("PerformerType"))
				{
					activity.setPerformerType(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Priority"))
				{
					activity.setPriority(nodeValue(currentNode));
				}
				else if(currentNode.getNodeName().equals("Params"))
				{
					readParameters(currentNode,activity);
				}
				else if(currentNode.getNodeName().equals("ExtendedAttributes"))
				{
					readExtendedAttributes(currentNode, activity);
				}
				else
				{
					System.err.println("XPDLReader::readActivity - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		if(hashActivities.put(strActivityId, activity) != null)
		{
			XPDLReader.appendErrorMessage(DUPLICATE, WORKFLOW_ACTIVITY, strActivityId);
		}
	}

	/**
	 * parse the Parameters
	 * 
	 * @param parentNode		the parent node of params is activity 
	 * @param activity			the workflow activity
	 */
	private void readParameters(Node parentNode, Activity activity)
	{
		Node currentNode = null;
		//Vector vecParameters = new Vector();
		Hashtable hashParameters = new Hashtable(); 
		
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Param"))
				{
					//readParameter(currentNode, vecParameters);
					readParameter(currentNode, hashParameters);
				}
				else
				{
					System.err.println("XPDLReader::readParameters - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		//activity.setParameters(vecParameters);
		activity.setParameters(hashParameters);
	}

	/**
	 * parse the parameter 
	 * 
	 * @param parentNode		the parent node of extended attribute is extended attributes 
	 * @param vecParameters		the parameters
	 */
	private void readParameter(Node parentNode, Hashtable hashParameters)
	{
		Parameter parameter = new Parameter();
		NamedNodeMap parameterAttributes = parentNode.getAttributes();

		// getting the Parameter Name, Type and Description
		String strParameterName = parameterAttributes.getNamedItem("Name").getNodeValue();
		String strParameterType = parameterAttributes.getNamedItem("Type").getNodeValue();
		String strParameterDescription = parameterAttributes.getNamedItem("Desc").getNodeValue();

		parameter.setName(strParameterName);
		parameter.setType(strParameterType);
		parameter.setDescription(strParameterDescription);

		hashParameters.put(strParameterName, parameter);
	}

	/**
	 * parse the Extended Attributes 
	 * 
	 * @param parentNode		the parent node of extended attributes is activity 
	 * @param activity 			the workflow activity
	 */
	private void readExtendedAttributes(Node parentNode, Activity activity)
	{
		Node currentNode = null;
		Hashtable hashExtendedAttributes = new Hashtable();
	
		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("ExtendedAttribute"))
				{
					readExtendedAttribute(currentNode, hashExtendedAttributes);
				}
				else
				{
					System.err.println("XPDLReader::readExtendedAttributes - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

//System.err.println("hashExtendedAttributes = " + hashExtendedAttributes);		
		
		activity.setExtendedAttributes(hashExtendedAttributes);
	}

	/**
	 * parse the Extended Attribute 
	 * 
	 * @param parentNode		the parent node of extended attribute is extended attributes 
	 * @param hashExtendedAttributes the extended attributes
	 */
	private void readExtendedAttribute(Node parentNode,
									   Hashtable hashExtendedAttributes)
	{
		//Node currentNode = null;
		NamedNodeMap ExtendedAttributeAttributes = parentNode.getAttributes();

		// getting the ExtendedAttribute Name and Value
		String strExtendedAttributeName = ExtendedAttributeAttributes.
											getNamedItem("Name").getNodeValue();
		String strExtendedAttributeValue = ExtendedAttributeAttributes.
											getNamedItem("Value").getNodeValue();

		hashExtendedAttributes.put(strExtendedAttributeName, strExtendedAttributeValue);
	}

	/**
	 * parse the Transitions 
	 * 
	 * @param parentNode		the parent node of transtions is workflow process 
	 * @param workflowPackage	the workflow package
	 * @param activitiesNode	the activities node
	 */
	private void readTransitions(Node parentNode, WorkflowProcess workflowProcess, 
							     Node activitiesNode)
	{
		Node currentNode = null;
		Hashtable hashTransitions = new Hashtable();

		Vector vecActivityId = getActivityIds(activitiesNode);

		// traverse through all the child nodes
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Transition"))
				{
					readTransition(currentNode, hashTransitions, vecActivityId);
				}
				else
				{
					System.err.println("XPDLReader::readTransitions - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		workflowProcess.setTransitions(hashTransitions);
	}

	/**
	 * parse the Transition 
	 * 
	 * @param parentNode		the parent node of transtion is transitions 
	 * @param workflowPackage	the workflow package
	 * @param vecActivityId		the activity Ids
	 */
	private void readTransition(Node parentNode, Hashtable hashTransitions, 
								Vector vecActivityId)
	{
		Node currentNode = null;

		NamedNodeMap TransitionAttributes = parentNode.getAttributes();

		// getting the Transition Id, Name, From and To attribute
		String strTransitionId = TransitionAttributes.getNamedItem("Id").getNodeValue();
		String strTransitionName = TransitionAttributes.getNamedItem("Name").getNodeValue();
		String strTransitionFrom = TransitionAttributes.getNamedItem("From").getNodeValue();
		String strTransitionTo = TransitionAttributes.getNamedItem("To").getNodeValue();
		String strTransitionCondition = TransitionAttributes.getNamedItem("Condition").getNodeValue();
		String strTransitionType = TransitionAttributes.getNamedItem("Type").getNodeValue();
		String strTransitionXCoordinate = TransitionAttributes.getNamedItem("XCoord").getNodeValue();
		String strTransitionYCoordinate = TransitionAttributes.getNamedItem("YCoord").getNodeValue();

		// instantiate the Transition object to be filled while parsing the XML
		Transition transition = new Transition();

		// check if the TransitionFrom and TransitionTo are valid
		if(!vecActivityId.contains(strTransitionFrom))
		{
			XPDLReader.appendErrorMessage(NON_EXISTENCE_ACTIVITY, WORKFLOW_ACTIVITY_TRANSITION,
											   strTransitionFrom);
		}

		if(!vecActivityId.contains(strTransitionTo))
		{
			XPDLReader.appendErrorMessage(NON_EXISTENCE_ACTIVITY, WORKFLOW_ACTIVITY_TRANSITION,
											   strTransitionTo);
		}

		// set the id, name from and to attribute to the Transition object
		transition.setId(strTransitionId);
		transition.setName(strTransitionName);
		transition.setFrom(strTransitionFrom);
		transition.setTo(strTransitionTo);
		transition.setCondition(strTransitionCondition);
		transition.setType(strTransitionType);
		transition.setXCoordinate(strTransitionXCoordinate);
		transition.setYCoordinate(strTransitionYCoordinate);

		// traverse through all the child nodes
		/*
		NodeList childNodes = parentNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Condition"))
				{
					//TODO: implement consition in transition!!!!!!
				}
				else
				{
					System.err.println("XPDLReader::readTransition - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}
		*/

		if(hashTransitions.put(strTransitionId, transition) != null)
		{
			XPDLReader.appendErrorMessage(DUPLICATE, WORKFLOW_ACTIVITY_TRANSITION,
											   strTransitionId);
		}
	}

	/**
	 * get the Workflow Package 
	 *
	 * @return the WorkflowPackage object
	 */
	public static Package getWorkflowPackage()
	{
		return XPDLReader.workflowPackage;
	}

	/**
	 * append the error message
	 *
	 * @param strErrorType	the error type e.g. duplicate
	 * @param strDomain	the domain where the error occur
	 * @param strDomainId	the domain id
	 */
	private static void appendErrorMessage(String strErrorType, String strDomain,
										   String strDomainId)
	{
		if(XPDLReader.strErrMsg == null)
		{
			XPDLReader.strErrMsg = strErrorType + "," + strDomain + "," + strDomainId + ";";
		}
		else
		{
			XPDLReader.strErrMsg += strErrorType + "," + strDomain + "," + strDomainId + ";";
		}

		System.err.println("strErrMsg = " + XPDLReader.strErrMsg);
	}

	/**
	 * get the activity ids
	 *
	 * @param activitiesNode the activities node
	 */
	private Vector getActivityIds(Node activitiesNode)
	{
		Vector vecActivityId = new Vector();
		Node currentNode = null;
		NamedNodeMap ActivityAttributes = null;
		
		// traverse through all the child nodes
		NodeList childNodes = activitiesNode.getChildNodes();
		for(int intIndex=0; intIndex < childNodes.getLength(); intIndex++)
		{
			currentNode = childNodes.item(intIndex);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(currentNode.getNodeName().equals("Activity"))
				{
					ActivityAttributes = currentNode.getAttributes();

					// adding the activity id to the vector
					vecActivityId.add(ActivityAttributes.getNamedItem("Id").getNodeValue());
				}
				else
				{
					System.err.println("XPDLReader::getActivityIds - Unknown Tag " +
							currentNode.getNodeName() + " XPDL file corrupted");
				}
			}
		}

		return vecActivityId;
	}
	
    /** 
	 * Return the text (node value) of the first node under this, works best if normalized. 
	 *
	 * @return the node value
	 * */
    private static String nodeValue(Node node) 
	{
        if(node == null)
		{
			return null;
		}

        // make sure we get all the text there...
        node.normalize();
        Node textNode = node.getFirstChild();

        if(textNode == null)
		{
			return null;
		}

        // should be of type text
        return textNode.getNodeValue();
    }

}
