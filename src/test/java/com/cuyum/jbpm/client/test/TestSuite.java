package com.cuyum.jbpm.client.test;
//package com.cuyum.jbpm.client;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.drools.runtime.StatefulKnowledgeSession;
//import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
//import org.jbpm.process.workitem.wsht.SyncWSHumanTaskHandler;
//import org.jbpm.task.TaskService;
//import org.jbpm.test.JbpmJUnitTestCase;
//
//import com.cuyum.jbpm.client.artifacts.ProcessInstance;
//import com.cuyum.jbpm.client.artifacts.responses.GETAssignedTasksResponse;
//import com.cuyum.jbpm.client.artifacts.responses.GETProcessDefinitionsResponse;
//import com.cuyum.jbpm.client.artifacts.responses.GETProcessInstancesResponse;
//import com.cuyum.jbpm.client.artifacts.responses.GETServerStatusResponse;
//import com.cuyum.jbpm.client.artifacts.responses.GETUnassignedTasksResponse;
//import com.cuyum.jbpm.client.artifacts.responses.POSTClaimTaskResponse;
//import com.cuyum.jbpm.client.artifacts.responses.POSTReleaseTaskResponse;
//
//
///**
// * Unit test suite for BRMSClient.
// */
//public class TestSuite extends JbpmJUnitTestCase {
//
//	private Logger log = Logger.getLogger(TestSuite.class);
//	
//	private static StatefulKnowledgeSession ksession;
//	private static TaskService taskService;
//	private static Map<String, Object> params;
//	private static ProcessInstance processInstance;
//	
//	private BRMSClient client;
//	
//	public TestSuite() {
//		super(true);
//	}
//	
//    public void setUp() {
//		log.debug("Setting up BRMSClient");
//		
//		ksession = createKnowledgeSession("htTest.bpmn2");
//		taskService = getTaskService(ksession);
//		
//		// register human task work item.
//		SyncWSHumanTaskHandler humanTaskHandler = new SyncWSHumanTaskHandler(
//				taskService, ksession);
//			humanTaskHandler.setLocal(true);
//			humanTaskHandler.connect();
//			ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);
//	
//		// register other work items
//		ksession.getWorkItemManager().registerWorkItemHandler("Log", new SystemOutWorkItemHandler());
//		ksession.getWorkItemManager().registerWorkItemHandler("Email", new SystemOutWorkItemHandler());
//
//		params = new HashMap<String, Object>();
//		// initialize process parameters.
//		params.put("employee", "erics");
//		params.put("reason", "Amazing demos for JBoss World!");
//		
//		client = new BRMSClientImpl("localhost","8080","admin","admin");
//    }
//	
//	public void testServerStatus() {
//		try {
//			GETServerStatusResponse response =  client.getServerStatus();
//			assertNotNull(response);
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	public void testProcessDefinitions() {
//		try {
//			GETProcessDefinitionsResponse response =  client.getProcessDefinitions();
//			assertNotNull(response);
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	public void testProcessInstances() {
//		try {
//			GETProcessInstancesResponse response =  client.getProcessIntances("org.jbpm.approval.rewards");
//			assertNotNull(response);
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	public void testUnassignedTasks() {
//		try {
//			GETUnassignedTasksResponse response =  client.getUnassignedTasks("bruce");
//			assertNotNull(response);
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	public void testAssignedTasks() {
//		try {
//			GETAssignedTasksResponse response =  client.getAssignedTasks("bruce");
//			assertNotNull(response);
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	public void testClaimTask() {
//		try {
//			POSTClaimTaskResponse response =  client.claimTask("2", "admin");
//			assertNotNull(response);
//			assertEquals("success", response.getStatus());
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	public void testReleaseTask() {
//		try {
//			POSTReleaseTaskResponse response =  client.releaseTask("2");
//			assertNotNull(response);
//			assertEquals("success", response.getStatus());
//			log.debug(response.toString());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
//}
