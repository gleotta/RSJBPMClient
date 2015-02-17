package com.cuyum.jbpm.client;

import static com.cuyum.jbpm.client.config.BRMSWsConfig.ASSIGNED_TASKS_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.ASSIGN_TASK_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.CREATE_PROCESS_INSTANCE_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.DATASET_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.PARTICIPATIONS_TASKS_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.PROCESS_DEFINITIONS_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.PROCESS_INSTANCES_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.RELEASE_TASK_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.ROLE_CHECK_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.SERVER_STATUS_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.UPDATE_TASK_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.TRANSITION_TOKEN_WS;
import static com.cuyum.jbpm.client.config.BRMSWsConfig.NEW_INSTANCE_WS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import javax.enterprise.inject.Default;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.cuyum.jbpm.client.artifacts.HumanTask;
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
import com.cuyum.jbpm.client.exception.MissingAuthInformationException;
import com.cuyum.jbpm.client.exception.WSClientException;

/**
 * BRMS 5.3 REST Connectivity Client Utility
 * 
 * @author Jorge Morando
 * 
 */
// @Default
@Deprecated
public class BRMSClientImpl extends BRMSBaseClient implements BRMSClient {

	public static final Logger log = Logger.getLogger(BRMSClientImpl.class);

	public BRMSClientImpl(String host, String port) {
		this.host = host;
		this.port = port;
		httpclient = new DefaultHttpClient();
	}

	/*-************SERVER STATUS************-*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getServerStatus()
	 */
	@Override
	public GETServerStatusResponse getServerStatus() throws BRMSClientException {

		HttpRequestBase method = getMethod(SERVER_STATUS_WS);

		// execute ws
		try {

			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			GETServerStatusResponse response = processResponse(rawResponse,
					GETServerStatusResponse.class);

			return response;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*-***********PROCESS MANAGEMENT************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getProcessDefinitions()
	 */
	@Override
	public GETProcessDefinitionsResponse getProcessDefinitions()
			throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(PROCESS_DEFINITIONS_WS);
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			GETProcessDefinitionsResponse response = processResponse(
					rawResponse, PROCESS_DEFINITIONS_WS.responseClass());

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#getProcessIntances(java.lang.String)
	 */
	@Override
	public GETProcessInstancesResponse getProcessIntances(String processId)
			throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(PROCESS_INSTANCES_WS, new String[] {
				"processDefinitionId", processId });
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			GETProcessInstancesResponse response = processResponse(rawResponse,
					PROCESS_INSTANCES_WS.responseClass());

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	@Override
	public GETUnassignedTasksResponse getUnassignedTasks(String actorId) {

		List<HumanTask> hms = getParticipationsTasks(actorId).getTasks();
		List<HumanTask> ret = new ArrayList<HumanTask>();
		for (HumanTask hm : hms) {
			if (hm.getCurrentState().equals("OPEN")) {
				ret.add(hm);
			}
		}

		GETUnassignedTasksResponse rett = new GETUnassignedTasksResponse();
		rett.setTasks(ret);
		return rett;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#getUnassignedTasks(java.lang.String)
	 */
	@Override
	public GETParticipationsTasksResponse getParticipationsTasks(String actorId)
			throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(PARTICIPATIONS_TASKS_WS,
				new String[] { "actorId", actorId });
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			GETParticipationsTasksResponse response = processResponse(
					rawResponse, PARTICIPATIONS_TASKS_WS.responseClass());

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getAssignedTasks(java.lang.String)
	 */
	@Override
	public GETAssignedTasksResponse getAssignedTasks(String actorId)
			throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(ASSIGNED_TASKS_WS, new String[] {
				"actorId", actorId });

		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			log.debug("STATUS " + sl);
			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			GETAssignedTasksResponse response = processResponse(rawResponse,
					ASSIGNED_TASKS_WS.responseClass());

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#claimTask(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public POSTClaimTaskResponse claimTask(String taskId, String actorId)
			throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(ASSIGN_TASK_WS, new String[] {
				"actorId", actorId }, new String[] { "taskId", taskId });

		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			POSTClaimTaskResponse response = new POSTClaimTaskResponse();

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#releaseTask(java.lang.String)
	 */
	@Override
	public POSTReleaseTaskResponse releaseTask(String taskId)
			throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(RELEASE_TASK_WS, new String[] {
				"taskId", taskId });
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);
			POSTReleaseTaskResponse response = new POSTReleaseTaskResponse();

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			return response;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	@Override
	public POSTCreateInstanceResponse createInstance(String processId)
			throws BRMSClientException {
		return createInstance(processId, new HashMap<String, Object>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#createInstance(java.lang.String,java
	 * .util.List&lt;BasicNameValuePair&gt;)
	 */
	@Override
	public POSTCreateInstanceResponse createInstance(String processId,
			Map<String, Object> params) throws BRMSClientException {
		Set<Entry<String, Object>> es = params.entrySet();
		List<BasicNameValuePair> bnm = new ArrayList<BasicNameValuePair>(
				es.size());

		for (Entry<String, Object> val : es) {
			bnm.add(new BasicNameValuePair(val.getKey(), val.getValue().toString()));
		}

		return this.createInstance(processId, bnm);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#createInstance(java.lang.String,java
	 * .util.List&lt;BasicNameValuePair&gt;)
	 */
	@Override
	public POSTCreateInstanceResponse createInstance(String processId,
			List<BasicNameValuePair> params) throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();
		}

		POSTCreateInstanceResponse response;
		HttpRequestBase method = getMethod(CREATE_PROCESS_INSTANCE_WS, params,
				new String[] { "processId", processId });
		try {

			HttpResponse rawResponse = execute(method);

			response = new POSTCreateInstanceResponse();

			StatusLine sl = rawResponse.getStatusLine();
			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);
		} finally {
			method.releaseConnection();
		}

		return response;
	}

	@Override
	public POSTUpdateTaskResponse updateTask(String taskId,
			Map<String, Object> params) throws BRMSClientException {
		Set<Entry<String, Object>> es = params.entrySet();
		List<BasicNameValuePair> bnm = new ArrayList<BasicNameValuePair>(
				es.size());

		for (Entry<String, Object> val : es) {
			bnm.add(new BasicNameValuePair(val.getKey(), val.getValue().toString()));
		}

		return this.updateTask(taskId, bnm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#updateTask(java.lang.String,java.util
	 * .List&lt;BasicNameValuePair&gt;)
	 */
	@Override
	public POSTUpdateTaskResponse updateTask(String taskId,
			List<BasicNameValuePair> params) throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		POSTUpdateTaskResponse response;
		HttpRequestBase method = getMethod(UPDATE_TASK_WS, params,
				new String[] { "taskId", taskId });
		try {

			HttpResponse rawResponse = execute(method);

			response = new POSTUpdateTaskResponse();

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);
		} finally {
			method.releaseConnection();
		}

		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#roleChecks(java.util.List&lt;String&gt;)
	 */
	@Override
	public GETRoleCheckResponse getRoleChecks(List<String> roles)
			throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException(
					"El usuario no ha podido autentificarse contra la plataforma BRMS.");
		}

		StringBuilder rolesStr = new StringBuilder();
		;
		for (String role : roles) {
			if (!rolesStr.toString().trim().isEmpty())
				rolesStr.append(",");
			rolesStr.append(role);
		}
		GETRoleCheckResponse response;
		HttpRequestBase method = getMethod(ROLE_CHECK_WS, new String[] {
				"roleCheck", rolesStr.toString() });
		try {

			HttpResponse rawResponse = execute(method);

			response = processResponse(rawResponse,
					ROLE_CHECK_WS.responseClass());

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);
		} finally {
			method.releaseConnection();
		}

		return response;
	}

	@Override
	public GETDatasetInstanceResponse getDatasetInstance(
			String processInstanceId) throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();
		}

		HttpRequestBase method = getMethod(DATASET_WS, new String[] { "id",
				processInstanceId });

		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			log.debug("STATUS " + sl);
			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			GETDatasetInstanceResponse response = processDatasetInstanceResponse(rawResponse);

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}

	}

	@Override
	public POSTSignalTokenResponse signalToken(String tokenId,
			String signalRef, String value) {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}
		
		String signalValue = signalRef;
		if (value!=null)
			signalValue=signalValue+"^"+value; //%5E	
		

																			
		HttpRequestBase method = getMethod(TRANSITION_TOKEN_WS,
				new String[] { "tokenId", tokenId }, new String[] { "signalValue", signalValue });

		
		
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			POSTSignalTokenResponse response = new POSTSignalTokenResponse();

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	@Override
	public POSTNewInstanceResponse newInstance(String processId) {
		
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		HttpRequestBase method = getMethod(NEW_INSTANCE_WS, new String[] {
				"idProceso", processId });
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			POSTNewInstanceResponse response = processResponse(rawResponse,
					NEW_INSTANCE_WS.responseClass());

			return response;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

}
