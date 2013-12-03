/**
 * 
 */
package com.cuyum.jbpm.client;

import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.cuyum.jbpm.client.artifacts.responses.GETAssignedTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETDatasetInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETParticipationsTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETProcessDefinitionsResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETProcessInstancesResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETRoleCheckResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETServerStatusResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETUnassignedTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTClaimTaskResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTCreateInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTNewInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTReleaseTaskResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTSignalTokenResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTUpdateTaskResponse;
import com.cuyum.jbpm.client.exception.BRMSClientException;


/**
 * @author Jorge Morando
 */
public interface BRMSClient {

	/**
	 * Disconnects from BRMS
	 */
	void logout();
	
	/**
	 * Login to BRMS
	 * @param user
	 * @param pass
	 * @throws Exception
	 */
	void login(String user, String pass) throws Exception;
	
	/**
	 * Checks if there is a current session.
	 * @return boolean
	 */
	boolean isLogged();
	
	/*-*******************WS****************-*/
	
	/**
	 * Retrieves Server Status
	 * @return GETServerStatusResponse
	 * @throws BRMSClientException
	 */
	GETServerStatusResponse getServerStatus() throws BRMSClientException;
	
	/**
	 * Retrieves Process Definitions
	 * @return GETProcessDefinitionsResponse
	 * @throws BRMSClientException
	 */
	GETProcessDefinitionsResponse getProcessDefinitions() throws BRMSClientException;
	
	/**
	 * Retrieves Process Instances
	 * @param processId String process identification
	 * @return GETProcessInstancesResponse
	 * @throws BRMSClientException
	 */
	GETProcessInstancesResponse getProcessIntances(String processId) throws BRMSClientException;
	
	/**
	 * Retrieves Tasks potentially assignable to a user by its groupId
	 * @param actorId
	 * @return GETUnassignedTasksResponse
	 * @throws BRMSClientException
	 */
	GETUnassignedTasksResponse getUnassignedTasks(String actorId) throws BRMSClientException;
	
	/**
	 * Retrieves Tasks assigned to specific user
	 * @param actorId
	 * @return GETAssignedTasksResponse
	 * @throws BRMSClientException
	 */
	GETAssignedTasksResponse getAssignedTasks(String actorId) throws BRMSClientException;
	
	/**
	 * Claims a task to be assigned to a specific user
	 * @param actorId
	 * @param taskId
	 * @return POSTClaimTaskResponse
	 * @throws BRMSClientException
	 */
	POSTClaimTaskResponse claimTask(String taskId, String actorId) throws BRMSClientException;
	
	/**
	 * Releases a task
	 * @param taskId
	 * @return POSTClaimTaskResponse
	 * @throws BRMSClientException
	 */
	POSTReleaseTaskResponse releaseTask(String taskId) throws BRMSClientException;
	
	/**
	 * Creates a process instance
	 * @param processId
	 * @return POSTReleaseTaskResponse
	 * @throws BRMSClientException
	 * @Deprecated use {@link #createInstance(String, Map)}
	 */
	@Deprecated
	POSTCreateInstanceResponse createInstance(String processId,List<BasicNameValuePair> params) throws BRMSClientException;
	
	
	/**
	 * Creates a process instance
	 * 
	 * @param processId
	 * @return
	 * @throws BRMSClientException
	 */
	POSTCreateInstanceResponse createInstance(String processId)
			throws BRMSClientException;
	
	/**
	 * Creates a process instance
	 * 
	 * @param processId
	 * @param params
	 * @return
	 * @throws BRMSClientException
	 */
	POSTCreateInstanceResponse createInstance(String processId,
			Map<String, String> params) throws BRMSClientException;
	
	/**
	 * Update human task
	 * @param taskId
	 * @return POSTUpdateTaskResponse
	 * @throws BRMSClientException
	 * @Deprecated use {@link #updateTask(String, Map)}
	 */
	@Deprecated
	POSTUpdateTaskResponse updateTask(String taskId,List<BasicNameValuePair> params) throws BRMSClientException;
	
	/**
	 * Update human task
	 * 
	 * @param taskId
	 * @param params
	 * @return
	 * @throws BRMSClientException
	 */
	POSTUpdateTaskResponse updateTask(String taskId, Map<String, String> params)
			throws BRMSClientException;
	
	/**
	 * Retrieve de Instances dataset
	 * @param processInstanceId
	 * @return GETDatasetInstanceResponse
	 * @throws BRMSClientException
	 */
	GETDatasetInstanceResponse getDatasetInstance(String processInstanceId) throws BRMSClientException;
	

	/**
	 * Returns actors Participation tasks
	 * @param actorId
	 * @return GETParticipationsTasksResponse
	 * @throws BRMSClientException
	 */
	GETParticipationsTasksResponse getParticipationsTasks(String actorId) throws BRMSClientException;
	
	/**
	 * Returns actors role checks
	 * @param roles
	 * @return GETRoleCheckResponse
	 * @throws BRMSClientException
	 */
	GETRoleCheckResponse getRoleChecks(List<String> roles) throws BRMSClientException;

	
	/**
	 * Send signal event to token
	 * 
	 * @param token
	 * @param signalRef
	 * @param value
	 * @return
	 */
	POSTSignalTokenResponse signalToken(String tokenId, String signalRef, String value);

	/**
	 * Crea una unica instancia sin inicializar variables de proceso
	 * 
	 * @param processId
	 * @return
	 */
	POSTNewInstanceResponse newInstance(String processId);
	

}
