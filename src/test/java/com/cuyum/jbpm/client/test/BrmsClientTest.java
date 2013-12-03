/**
 * 
 */
package com.cuyum.jbpm.client.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cuyum.jbpm.client.BRMSClient;
import com.cuyum.jbpm.client.BRMSClientImpl;
import com.cuyum.jbpm.client.artifacts.HumanTask;
import com.cuyum.jbpm.client.artifacts.ProcessInstanceToken;
import com.cuyum.jbpm.client.artifacts.responses.GETAssignedTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETDatasetInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETParticipationsTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETRoleCheckResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETServerStatusResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETUnassignedTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTClaimTaskResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTCreateInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTNewInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTUpdateTaskResponse;


/**
 * @author german
 *
 */
public class BrmsClientTest {
	
	private static final String BRMS_ADRESS = "localhost";
	private static final String BRMS_PORT = "8080";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";
	
	private BRMSClient client;

	private Logger log = Logger.getLogger(BrmsClientTest.class);
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log.info("Entro al setup");
		client = new BRMSClientImpl(BRMS_ADRESS, BRMS_PORT);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.info("Entro al tearDown");
	}

	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#getServerStatus()}.
	 */
	@Test
	public void testGetServerStatus() {
		try {

			GETServerStatusResponse status = client.getServerStatus();
			assertNotNull(status);
			
			client.login(USERNAME, PASSWORD);
			status = client.getServerStatus();
			assertNotNull(status);
			
			log.info("Server Status: "+client.getServerStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#getUnassignedTasks(java.lang.String)}.
	 */
	@Test
	public void testGetUnassignedTasks() {
		try {
			
			client.login(USERNAME, PASSWORD);
		
			
			GETUnassignedTasksResponse tasks = client.getUnassignedTasks(USERNAME);
			assertNotNull(tasks);
			
			List<HumanTask> ll = tasks.getTasks();
			
			for (HumanTask humanTask : ll) {
				log.info(humanTask);
			}
			
			client.logout();
			
			
		} catch (Exception e) {
			log.error(e);
			fail(e.getMessage());
		}
	}
	
	
//	@Test
//	public void testClaimTaskResponse() {
//		try {
//			
//			client.login("jorge", "jorge");
//			
//			GETUnassignedTasksResponse tasks = client.getUnassignedTasks("jorge");
//			assertNotNull(tasks);
//			
//			List<HumanTask> ll = tasks.getTasks();
//			assertTrue(ll.size()>0);
//			
//			HumanTask hm = ll.get(0);
//		
//			log.info("tarea antes de asignar: "+hm);
//			POSTClaimTaskResponse claimResonse = client.claimTask(hm.getId()+"", "jorge");
//			
//			//log.info("tarea después asignar: "+hm);
//			assertTrue(claimResonse.getStatus().equals("200"));
//			
//			
//			
//			client.logout();
//			
//			
//		} catch (Exception e) {
//			log.error(e);
//			fail(e.getMessage());
//		}
//	}
//	
	
	
	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#getUnassignedTasks(java.lang.String)}.
	 */
	@Test
	public void testGetParticipationsTasks() {
		try {
			
			client.login(USERNAME, PASSWORD);
		
			
			GETParticipationsTasksResponse tasks = client.getParticipationsTasks(USERNAME);
			assertNotNull(tasks);
			
			List<HumanTask> ll = tasks.getTasks();
			
			for (HumanTask humanTask : ll) {
				log.info(humanTask);
			}
			
			client.logout();
			
			
		} catch (Exception e) {
			log.error(e);
			fail(e.getMessage());
		}
	}


	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#getAssignedTasks(java.lang.String)}.
	 */
	@Test
	public void testGetAssignedTasks() {
		try {
			
			client.login(USERNAME, PASSWORD);
		
			
			GETAssignedTasksResponse tasks = client.getAssignedTasks(USERNAME);
			assertNotNull(tasks);
			
			List<HumanTask> ll = tasks.getTasks();
			
			for (HumanTask humanTask : ll) {
				log.info(humanTask);
			}
			
			client.logout();
			
			
		} catch (Exception e) {
			log.error(e);
			fail(e.getMessage());
		}
		
		
	}
	
	@Test
	public void testGetDatasetInstance() {
		try {
			
			client.login(USERNAME, PASSWORD);
		
			
			GETDatasetInstanceResponse dataset = client.getDatasetInstance("1");
			assertNotNull(dataset);
			
			log.info(dataset.getDataset());
			
			client.logout();
			
			
		} catch (Exception e) {
			log.error(e);
			fail(e.getMessage());
		}
		
		
	}
	
	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#updateTask(java.lang.String)}.
	 */
//	@Test
//	public void tssestUpdateTask() {
//		try {
//			
//			client.login("john", "john");
//		
//			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//			
//			params.add(new BasicNameValuePair("externalizacionOut","true"));
//			params.add(new BasicNameValuePair("asignacionOut","false"));
//			params.add(new BasicNameValuePair("informacionOut","false"));
//			params.add(new BasicNameValuePair("respuestaOut","false"));
//			params.add(new BasicNameValuePair("contenidoInformacionOut","false"));
//			
//			POSTClaimTaskResponse claim = client.claimTask("23", "john");
//			
//			POSTUpdateTaskResponse ur = client.updateTask("23", params);
//			
//			assertNotNull(ur);
//			
//			client.logout();
//			
//			
//		} catch (Exception e) {
//			log.error(e);
//			fail(e.getMessage());
//		}
//		
//		
//	}

	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#login(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLogin() {
		try {
			assertTrue(!client.isLogged());
			
			client.login(USERNAME, PASSWORD);
			
			assertTrue(client.isLogged());
	
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void createInstance() {
		try {
			assertTrue(!client.isLogged());
			
			client.login(USERNAME, PASSWORD);
			
			assertTrue(client.isLogged());
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("cuentaMedicaId", "10000");
			POSTCreateInstanceResponse resp =  client.createInstance("cl.isl.spm.cm.ingreso", params);
			
			System.out.println("STATUS: "+resp.getStatus());
			
			System.out.println("STATUS: "+resp.getMessage());
			
	
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testNewInstance() {
		try {
			assertTrue(!client.isLogged());
			
			client.login(USERNAME, PASSWORD);
			
			assertTrue(client.isLogged());
			
			POSTNewInstanceResponse resp =  client.newInstance("paquete.prueba.PruebaSignal2");
			
			List<ProcessInstanceToken> pit = resp.getRootToken().getChildren();
			
			for (ProcessInstanceToken token : pit) {
				System.out.println("Token: "+token);
				if (token.isCanBeSignaled()) {
					client.signalToken(token.getId()+"", token.getName(), null);
				}
			}
			System.out.println("STATUS: "+resp);
			
		
			
	
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.cuyum.jbpm.client.BRMSClientImpl#logout()}.
	 */
	@Test
	public void testLogout() {
		try {
			
			client.login(USERNAME,PASSWORD);

			
			client.logout();
			assertTrue(!client.isLogged());
	
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testUserRoles() {
		try {
			List<String> roles = new ArrayList<String>();
			roles.add("admin");
			roles.add("user");
			roles.add("manager");
			
			client.login(USERNAME, PASSWORD);
			GETRoleCheckResponse status = client.getRoleChecks(roles);
			assertNotNull(status);
			
			log.info("Server Status: "+client.getServerStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}

}
