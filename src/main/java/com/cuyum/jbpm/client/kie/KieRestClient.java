/**
 * 
 */
package com.cuyum.jbpm.client.kie;

import java.io.IOException;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.event.AuditEvent;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbHistoryLogList;
import org.kie.services.client.serialization.jaxb.impl.deploy.JaxbDeploymentUnit;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceResponse;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceWithVariablesResponse;
import org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskSummaryListResponse;
import org.kie.services.client.serialization.jaxb.rest.JaxbGenericResponse;

import com.cuyum.jbpm.client.BRMSBaseClient;
import com.cuyum.jbpm.client.artifacts.HumanTask;
import com.cuyum.jbpm.client.artifacts.ProcessInstance;
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
import com.cuyum.jbpm.client.exception.MissingAuthPasswordInformationException;
import com.cuyum.jbpm.client.exception.MissingAuthUserInformationException;
import com.cuyum.jbpm.client.exception.UnauthorizedUserException;
import com.cuyum.jbpm.client.exception.WSClientException;
import com.cuyum.jbpm.client.kie.config.KieWsConfig;

/**
 * @author german
 * 
 */
public class KieRestClient extends BRMSBaseClient {

	private static Logger log = Logger.getLogger(KieRestClient.class);

	/* POC */

	// URL de servidor bpm6 de cuyum
	// private String deploymentUrlStr =
	// "http://162.243.12.101:8080/business-central/";
	// private String deploymentId = "org.kie.procesojorge:aduanabojorge:1.0";
	// private String definitionId = "bo.gob.aduana.registro-operadores-jorge";

	private String deploymentUrlStr;
	private String deploymentId;
	// private String definitionId;
	private String userId;
	private String password;

	private HttpClient httpClient;

	private QueryService queryService = new QueryService();

	public KieRestClient(String deploymentUrlStr, String deploymentId) {
		if (deploymentUrlStr == null || deploymentUrlStr.isEmpty()) {
			log.error("No se puede Levantar engine Remoto (KIE) de BPM porque el host no est\u00E1 correctamente configurado");
		} else if (deploymentId == null || deploymentId.isEmpty()) {
			log.error("No se puede Levantar engine Remoto (KIE) de BPM porque el contexto no est\u00E1 correctamente configurado");
		} else {
			this.deploymentUrlStr = deploymentUrlStr;
			this.deploymentId = deploymentId;
			// this.userId = user;
			// this.password = password;
			// definitionId = conf.getProcessDefinitionIds()[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#disconnect()
	 */

	public void login(String user, String pass) throws Exception {

		if (user == null)
			throw new MissingAuthUserInformationException();
		if (pass == null)
			throw new MissingAuthPasswordInformationException();

		this.userId = user;
		this.password = pass;

		// HttpResponse response = null;

		if (isLogged()) {
			log.warn("Tratando de loguearse al cliente REST mientras existe una sesi\u00F3n activa");
			logout();
		}
		httpClient = createPreemptiveAuthHttpClient(user, pass);
		boolean allowed = (httpClient != null);

		// int status = response.getStatusLine().getStatusCode();

		if (!allowed) {
			throw new UnauthorizedUserException("El usuario \"" + user
					+ "\" no se encuentra autorizado en la plataforma BRMS");
		}

		// this.getServerStatus()==null
		// if (this.getServerStatus()==null||) {
		// log.error("El usuario no se puede logear: status " + status);
		// throw new MissingAuthInformationException(
		// "El usuario no se puede logear: status " + status);
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#logout()
	 */
	@Override
	public void logout() {

		// httpClient.getConnectionManager().closeExpiredConnections();
		try {
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo logout ", e);
		}
		httpClient = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#isLogged()
	 */
	@Override
	public boolean isLogged() {
		return (httpClient != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getServerStatus()
	 */
	@Override
	public GETServerStatusResponse getServerStatus() throws BRMSClientException {
		Map<String, String> pp = new HashMap<String, String>();
		pp.put("deploymentId", deploymentId);
		HttpRequestBase method = getMethod(KieWsConfig.SERVER_STATUS_WS, pp);

		// execute ws
		try {

			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response

			JaxbDeploymentUnit resp = (JaxbDeploymentUnit) rawResponse
					.getEntity();
			GETServerStatusResponse ret = new GETServerStatusResponse();
			ret.setStatus(resp.getStatus().toString());
			return ret;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getProcessDefinitions()
	 */
	@Override
	public GETProcessDefinitionsResponse getProcessDefinitions()
			throws BRMSClientException {

		throw new UnsupportedOperationException(
				"Este metodo no es portado remotamente en version 6.0.3");

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
		Map<String, String> pp = new HashMap<String, String>();
		pp.put("deploymentId", deploymentId);
		pp.put("processDefinitionId", processId);
		HttpRequestBase method = getMethod(KieWsConfig.PROCESS_INSTANCES_WS, pp);
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			JaxbHistoryLogList response = (JaxbHistoryLogList) processResponse(
					rawResponse,
					KieWsConfig.PROCESS_INSTANCES_WS.responseClass());

			GETProcessInstancesResponse ret = new GETProcessInstancesResponse();

			ret.setDefinitions(new ArrayList<ProcessInstance>());

			List<AuditEvent> pils = response.getResult();
			for (AuditEvent auditEvent : pils) {
				ProcessInstanceLog pil = (ProcessInstanceLog) auditEvent;
				ProcessInstance pi = new ProcessInstance();
				pi.setDefinitionId(processId);
				pi.setId(pil.getProcessInstanceId());
				pi.setEndDate(pil.getEnd().toString());
				pi.setEndResult(pil.getOutcome());
				pi.setRootToken(null);
				boolean active = pil.getStatus() == org.jbpm.process.instance.ProcessInstance.STATE_ACTIVE;
				pi.setSuspended(!active);
				pi.setStatus(pil.getStatus());
				pi.setKey(pil.getExternalId());
				pi.setStartDate(pil.getStart().toString());
				ret.getInstances().add(pi);

			}
			return ret;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	public ProcessInstanceLog getProcessInstance(Long processInstanceId) {

		Map<String, String> pp = new HashMap<String, String>();
		pp.put("deploymentId", deploymentId);
		pp.put("processInstanceId", processInstanceId + "");
		HttpRequestBase method = getMethod(KieWsConfig.PROCESS_INSTANCE_WS, pp);
		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			JaxbHistoryLogList response = (JaxbHistoryLogList) processResponse(
					rawResponse,
					KieWsConfig.PROCESS_INSTANCE_WS.responseClass());

			return (ProcessInstanceLog) response.getResult().get(0);

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
		// return getProcessInstance(processInstance, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#createInstance(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public POSTCreateInstanceResponse createInstance(String processId,
			List<BasicNameValuePair> params) throws BRMSClientException {

		Map<String, Object> ret = new HashMap<String, Object>();
		for (BasicNameValuePair value : params) {
			ret.put(value.getName(), value.getName());
		}
		return createInstance(processId, ret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#createInstance(java.lang.String)
	 */
	@Override
	public POSTCreateInstanceResponse createInstance(String processId)
			throws BRMSClientException {

		return createInstance(processId, new HashMap<String, Object>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#createInstance(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public POSTCreateInstanceResponse createInstance(String processId,
			Map<String, Object> params) throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();
		}

		POSTCreateInstanceResponse response;
		Map<String, String> pp = new HashMap<String, String>();
		pp.put("processId", processId);
		HttpRequestBase method = getMethod(
				KieWsConfig.CREATE_PROCESS_INSTANCE_WS, params, pp, null);
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

			JaxbProcessInstanceResponse pi = (JaxbProcessInstanceResponse) processResponse(
					rawResponse,
					KieWsConfig.CREATE_PROCESS_INSTANCE_WS.responseClass());
			response.setId(pi.getId());
			response.setProcessId(pi.getProcessId());
			response.setState(pi.getState());
			response.setProcessName(pi.getProcessName());
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
	 * @see com.cuyum.jbpm.client.BRMSClient#newInstance(java.lang.String)
	 */
	@Override
	public POSTNewInstanceResponse newInstance(String processId) {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}
		try {
			POSTCreateInstanceResponse pi = this.createInstance(processId);
			ProcessInstanceLog pin = this.getProcessInstance(pi.getId());
			POSTNewInstanceResponse ret = new POSTNewInstanceResponse();
			ret.setDefinitionId(pin.getProcessId());
			ret.setEndDate(pin.getEnd().toString());
			ret.setEndResult(pin.getOutcome());
			ret.setId(pin.getProcessInstanceId());
			ret.setKey(pin.getExternalId());
			ret.setStartDate(pin.getStart().toString());
			boolean active = pin.getStatus() == org.jbpm.process.instance.ProcessInstance.STATE_ACTIVE;
			ret.setSuspended(!active);
			ret.setState(pin.getStatus());
			return ret;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo "
					+ "newInstance", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#getDatasetInstance(java.lang.String)
	 */
	@Override
	public GETDatasetInstanceResponse getDatasetInstance(
			String processInstanceId) throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}
		Map<String, String> params = new HashMap<String, String>(2);
		params.put("deploymentId", deploymentId);
		params.put("processInstanceId", "" + processInstanceId);
		HttpRequestBase method = getMethod(KieWsConfig.DATASET_WS, params);

		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			log.debug("STATUS " + sl);
			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			JaxbProcessInstanceWithVariablesResponse pir = (JaxbProcessInstanceWithVariablesResponse) rawResponse
					.getEntity();
			GETDatasetInstanceResponse ret = new GETDatasetInstanceResponse();
			ret.setDataset(new HashMap<String, String>());
			Set<String> variablesKey = pir.getVariables().keySet();
			for (String key : variablesKey) {
				String value = pir.getVariables().get(key);
				ret.getDataset().put(key, value);
			}
			return ret;

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#signalToken(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public POSTSignalTokenResponse signalToken(String processInstanceId,
			String signalRef, String value) {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}
		Map<String, String> params = new HashMap<String, String>(2);
		params.put("deploymentId", deploymentId);
		params.put("processInstanceId", "" + processInstanceId);

		Map<String, List<String>> query = new HashMap<String, List<String>>(2);
		query.put(
				"signal",
				new ArrayList<String>(Arrays.asList(new String[] { signalRef })));
		query.put("event",
				new ArrayList<String>(Arrays.asList(new String[] { value })));

		HttpRequestBase method = getMethod(KieWsConfig.TRANSITION_TOKEN_WS,
				null, params, query);

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

			JaxbGenericResponse result = (JaxbGenericResponse) processResponse(
					rawResponse,
					KieWsConfig.TRANSITION_TOKEN_WS.responseClass());
			if (result.getStatus().name().equals("SUCCESS")) {
				return response;
			} else {
				throw new WSClientException("Error en respuesta de "
						+ KieWsConfig.TRANSITION_TOKEN_WS + ". Status: "
						+ result.getStatus());
			}

		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}

	}

	@SuppressWarnings("unchecked")
	public List<TaskSummary> getInstanceTasks(List<Long> workItemId,
			List<Long> taskId, List<String> businessAdministrator,
			List<String> potentialOwner, List<Status> status,
			List<String> taskOwner, List<Long> processInstanceId, Boolean union) {

		Map<String, List<String>> query = queryService.createMap(workItemId,
				taskId, businessAdministrator, potentialOwner, status,
				taskOwner, processInstanceId, union);

		HttpRequestBase method = getMethod(KieWsConfig.QUERY_TASKS_WS, null,
				null, query);

		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			// process the final response
			JaxbTaskSummaryListResponse response = (JaxbTaskSummaryListResponse) processResponse(
					rawResponse, KieWsConfig.QUERY_TASKS_WS.responseClass());

			return response.getList();

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
		List<String> taskOwner = new ArrayList<String>();
		taskOwner.add(actorId);

		List<TaskSummary> lts = getInstanceTasks(null, null, null, null, null,
				taskOwner, null, null);
		GETAssignedTasksResponse ret = new GETAssignedTasksResponse();
		ret.setTasks(listTaskSummaryToListHumanTask(lts));
		ret.setTotalCount(ret.getTasks().size());
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#getUnassignedTasks(java.lang.String)
	 */
	@Override
	public GETUnassignedTasksResponse getUnassignedTasks(String actorId)
			throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}
		List<String> potencialOwner = new ArrayList<String>();
		potencialOwner.add(actorId);

		List<Status> statuses = new ArrayList<Status>();
		statuses.add(Status.Ready);

		List<TaskSummary> lts = getInstanceTasks(null, null, null,
				potencialOwner, statuses, null, null, null);
		GETUnassignedTasksResponse ret = new GETUnassignedTasksResponse();
		ret.setTasks(listTaskSummaryToListHumanTask(lts));
		ret.setTotalCount(ret.getTasks().size());
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#getParticipationsTasks(java.lang.String)
	 */
	@Override
	public GETParticipationsTasksResponse getParticipationsTasks(String actorId)
			throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}
		List<String> potencialOwner = new ArrayList<String>();
		potencialOwner.add(actorId);

		List<TaskSummary> lts = getInstanceTasks(null, null, null,
				potencialOwner, null, null, null, null);
		GETParticipationsTasksResponse ret = new GETParticipationsTasksResponse();
		ret.setTasks(listTaskSummaryToListHumanTask(lts));
		ret.setTotalCount(ret.getTasks().size());
		return ret;

	}

	private List<HumanTask> listTaskSummaryToListHumanTask(List<TaskSummary> ts) {
		List<HumanTask> ret = new ArrayList<HumanTask>(ts.size());
		for (TaskSummary item : ts) {
			HumanTask ht = taskSummaryToHumanTask(item);
			ret.add(ht);
		}
		return ret;
	}

	private HumanTask taskSummaryToHumanTask(TaskSummary ts) {
		HumanTask ret = new HumanTask();
		ret.setAssignee(ts.getActualOwner() == null ? null : ts
				.getActualOwner().getId());
		ret.setCreateDate(ts.getCreatedOn());
		ret.setCurrentState(ts.getStatus().name());
		ret.setDescription(ts.getDescription());
		ret.setDueDate(ts.getExpirationTime());
		ret.setId(ts.getId());
		ret.setIsBlocking(false);
		ret.setIsSignalling(false);
		ret.setName(ts.getName());
		ret.setOutcomes(null);// llenar si es solicitado
		ret.setParticipantGroups(null); // llenar si es solicitado
		ret.setParticipantUsers(null); // llenar si es solicitado
		ret.setPriority(ts.getPriority());
		ret.setProcessId(ts.getProcessId());
		ret.setProcessInstanceId(ts.getProcessInstanceId() + "");
		ret.setUrl(null);
		return ret;
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
		if (actorId != null && !actorId.equals(userId))
			throw new UnauthorizedUserException("El actor " + actorId
					+ " no tiene permiso para realizar esta operación");

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("taskId", taskId);

		HttpRequestBase method = getMethod(KieWsConfig.ASSIGN_TASK_WS, params);

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

			JaxbGenericResponse result = (JaxbGenericResponse) processResponse(
					rawResponse, KieWsConfig.ASSIGN_TASK_WS.responseClass());
			if (result.getStatus().name().equals("SUCCESS")) {
				return response;
			} else {
				throw new WSClientException("Error en respuesta de "
						+ KieWsConfig.ASSIGN_TASK_WS + ". Status: "
						+ result.getStatus());
			}
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

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("taskId", taskId);

		HttpRequestBase method = getMethod(KieWsConfig.RELEASE_TASK_WS, params);

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

			JaxbGenericResponse result = (JaxbGenericResponse) processResponse(
					rawResponse, KieWsConfig.RELEASE_TASK_WS.responseClass());
			if (result.getStatus().name().equals("SUCCESS")) {
				return response;
			} else {
				throw new WSClientException("Error en respuesta de "
						+ KieWsConfig.RELEASE_TASK_WS + ". Status: "
						+ result.getStatus());
			}
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#updateTask(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public POSTUpdateTaskResponse updateTask(String taskId,
			List<BasicNameValuePair> params) throws BRMSClientException {
		Map<String, Object> pp = new HashMap<String, Object>();
		for (BasicNameValuePair vp : params) {
			pp.put(vp.getName(), vp.getValue());
		}
		return updateTask(taskId, pp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#updateTask(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public POSTUpdateTaskResponse updateTask(String taskId,
			Map<String, Object> params) throws BRMSClientException {
		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		Map<String, String> pathParams = new HashMap<String, String>(1);
		pathParams.put("taskId", taskId);

		Map<String, List<String>> queryParams = new HashMap<String, List<String>>();
		Set<String> keys = params.keySet();
		for (String key : keys) {
			Object value = params.get(key);
			String nkey = "map_"+key;
			List<String> vals = new ArrayList<String>(1);
			vals.add(value+"");
			queryParams.put(nkey, vals);
		}
		HttpRequestBase method = getMethod(KieWsConfig.UPDATE_TASK_WS, null, pathParams, queryParams);

		try {
			// execute ws
			HttpResponse rawResponse = execute(method);

			POSTUpdateTaskResponse response = new POSTUpdateTaskResponse();

			StatusLine sl = rawResponse.getStatusLine();

			if (sl.getStatusCode() != 200) {
				response.setStatus("failure");
				response.setMessage(sl.getReasonPhrase() + " - "
						+ sl.getStatusCode());
				throw new WSClientException("Error en respuesta de " + method
						+ ". Status: " + sl);
			}

			JaxbGenericResponse result = (JaxbGenericResponse) processResponse(
					rawResponse, KieWsConfig.UPDATE_TASK_WS.responseClass());
			if (result.getStatus().name().equals("SUCCESS")) {
				return response;
			} else {
				throw new WSClientException("Error en respuesta de "
						+ KieWsConfig.UPDATE_TASK_WS + ". Status: "
						+ result.getStatus());
			}
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getRoleChecks(java.util.List)
	 */
	@Override
	public GETRoleCheckResponse getRoleChecks(List<String> roles)
			throws BRMSClientException {
		throw new UnsupportedOperationException("getRoleChecks en jbpm6");
	}

	/**
	 * This method is used in order to create the authenticating REST client
	 * factory.
	 * 
	 * @param userName
	 * @param password
	 * @param timeout
	 * @param localContext
	 * 
	 * @return A {@link DefaultHttpClient} instance that will authenticate using
	 *         the given username and password.
	 */
	private static DefaultHttpClient createPreemptiveAuthHttpClient(
			String userName, String password) {
		BasicHttpContext localContext = new BasicHttpContext();
		BasicHttpParams params = new BasicHttpParams();
		int timeoutMilliSeconds = 5 * 1000;
		HttpConnectionParams.setConnectionTimeout(params, timeoutMilliSeconds);
		HttpConnectionParams.setSoTimeout(params, timeoutMilliSeconds);
		DefaultHttpClient client = new DefaultHttpClient(params);

		if (userName != null && !"".equals(userName)) {
			client.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(userName, password));
			// Generate BASIC scheme object and stick it to the local execution
			// context
			BasicScheme basicAuth = new BasicScheme();

			String contextId = UUID.randomUUID().toString();
			localContext.setAttribute(contextId, basicAuth);

			// Add as the first request interceptor
			client.addRequestInterceptor(new PreemptiveAuth(contextId), 0);
		}

		String hostname = "localhost";

		try {
			hostname = Inet6Address.getLocalHost().toString();
		} catch (Exception e) {
			// do nothing
		}

		// set the following user agent with each request
		String userAgent = "org.kie.services.client ("
				+ System.currentTimeMillis() + " / " + hostname + ")";
		HttpProtocolParams.setUserAgent(client.getParams(), userAgent);

		return client;
	}

	/**
	 * This class is used in order to effect preemptive authentication in the
	 * REST request factory.
	 */
	static class PreemptiveAuth implements HttpRequestInterceptor {

		private final String contextId;

		public PreemptiveAuth(String contextId) {
			this.contextId = contextId;
		}

		public void process(final HttpRequest request, final HttpContext context)
				throws HttpException, IOException {

			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);

			// If no auth scheme available yet, try to initialize it
			// preemptively
			if (authState.getAuthScheme() == null) {
				AuthScheme authScheme = (AuthScheme) context
						.getAttribute(contextId);
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context
						.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if (authScheme != null) {
					Credentials creds = credsProvider
							.getCredentials(new AuthScope(targetHost
									.getHostName(), targetHost.getPort()));
					if (creds == null) {
						throw new HttpException(
								"No credentials for preemptive authentication");
					}
					authState.update(authScheme, creds);
				}
			}
		}
	}

}
