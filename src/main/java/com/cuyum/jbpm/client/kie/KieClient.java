/**
 * 
 */
package com.cuyum.jbpm.client.kie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientResponse;
import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.kie.api.runtime.KieSession;
import org.kie.api.task.TaskService;
import org.kie.internal.deployment.DeploymentService;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;
import org.kie.services.client.serialization.jaxb.impl.deploy.JaxbDeploymentUnit;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceWithVariablesResponse;
import org.kie.services.client.serialization.jaxb.rest.JaxbGenericResponse;

import com.cuyum.jbpm.client.BRMSClient;
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
import com.cuyum.jbpm.client.exception.WSClientException;

/**
 * @author german
 * 
 */
public class KieClient implements BRMSClient {

	private static Logger log = Logger.getLogger(KieClient.class);

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

	private RemoteRuntimeEngine engine;
	private KieSession session;
	private TaskService taskService;
	private AuditLogService auditService;
	private DeploymentService deploymentService;
	private KieAlternative kieAlt;

	private QueryService qsrv;

	public KieClient(String deploymentUrlStr, String deploymentId) {
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

	// HECHO
	@Override
	public void login(String usr, String pwd) throws BRMSClientException {
		if (isLogged()) {
			if (this.userId.equals(usr) && this.password.equals(pwd))
				return;
			else
				throw new RuntimeException(
						"Esta sesión ya está logueado con otro usuario");
		}

		this.userId = usr;
		this.password = pwd;

		if (deploymentUrlStr != null && deploymentId != null && userId != null
				&& password != null) {
			try {

				URL deploymentUrlKA = new URL(deploymentUrlStr + "/rest");
				kieAlt = new KieAlternative(deploymentUrlKA, deploymentId,
						userId, password);

				URL deploymentUrl = new URL(deploymentUrlStr);
				// Creamos la sesion REST

				engine = RemoteRestRuntimeEngineFactory.newBuilder()
						.addDeploymentId(deploymentId).addUrl(deploymentUrl)
						.addUserName(userId).addPassword(password)
						.addTimeout(30).build().newRuntimeEngine();

				// current session
				session = engine.getKieSession();
				// remote session for process
				auditService = engine.getAuditLogService();
				// tasks
				taskService = engine.getTaskService();

			} catch (MalformedURLException e) {
				String msg = "Error de formaci\u00F3n de URL";
				throw new RuntimeException(msg, e);
			}
		} else {
			throw new MissingAuthInformationException(
					"No se han encontrado credenciales para autenticarse contra BPM");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#logout()
	 */
	@Override
	public void logout() {

		session.dispose();
		session.destroy();
		session = null;
		auditService.clear();
		auditService.dispose();
		auditService = null;
		taskService = null;
		// kieAlt = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#isLogged()
	 */
	@Override
	public boolean isLogged() {
		return (engine != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getServerStatus()
	 */
	@Override
	public GETServerStatusResponse getServerStatus() throws BRMSClientException {
		// cl.isl.procesos:spm-isl:1.0

		ClientResponse<?> response;

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("deploymentId", deploymentId);
		try {
			response = kieAlt.execute(Command.DEPLOYMENT_ID, parameters, false);
			
			JaxbDeploymentUnit resp = (JaxbDeploymentUnit) response.getEntity();
			GETServerStatusResponse ret = new GETServerStatusResponse();
			ret.setStatus(resp.getStatus().toString());
			return ret;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo "
					+ Command.DEPLOYMENT_ID.getUrl(), e);
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
		// FIXME cuando se haga upgrade de la versión
		// ClientResponse<?> response;
		//
		// JaxbCommandsRequest req = new JaxbCommandsRequest(deploymentId, new
		// GetProcessIdsCommand());
		// req.setUser(this.userId);
		// req.setUserPass(new String[]{this.password});
		// req.setVersion(ServicesVersion.VERSION);
		// req.setProcessInstanceId(0l);
		//
		//
		//
		//
		// Map<String, String> parameters = new HashMap<String, String>();
		// parameters.put("deploymentId", deploymentId);
		// try {
		// response = kieAlt.executePost(Command.RUNTIME_DEPLOYMENTID_EXECUTE,
		// parameters, null, req);
		// if (response.getStatus() != 200) {
		// throw new WSClientException("Error en respuesta de " +
		// Command.RUNTIME_DEPLOYMENTID_EXECUTE.getUrl()
		// + ". Status: " + response.getStatus());
		// }
		// } catch (Exception e) {
		// throw new WSClientException("Error al ejecutar metodo " +
		// Command.RUNTIME_DEPLOYMENTID_EXECUTE.getUrl(), e);
		// }
		//
		// // Get response
		// JaxbCommandsResponse commandResponse=null;
		// try {
		// //commandResponse = response.getEntity(JaxbCommandsResponse.class);
		// Object val = response.getEntity();
		// System.out.println(val);
		// } catch(ClientResponseFailure crf) {
		// String setCookie = (String)
		// response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		// String contentType = (String)
		// response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
		// if( setCookie != null && contentType.startsWith(MediaType.TEXT_HTML)
		// ) {
		// throw new
		// RemoteCommunicationException("The remote server requires form-based authentication.",
		// crf);
		// } else {
		// throw crf;
		// }
		// }
		// List<JaxbCommandResponse<?>> responses =
		// commandResponse.getResponses();
		// JaxbCommandResponse<?> responseObject = responses.get(0);
		//
		// GETProcessDefinitionsResponse ret = new
		// GETProcessDefinitionsResponse();
		// ret.setDefinitions(new ArrayList<ProcessDefinition>());
		//
		// List<String> results = (List<String>) responseObject.getResult();
		// for (String pid : results) {
		// ProcessDefinition pd = new ProcessDefinition();
		// pd.setId(pid);
		// pd.setName(pid);
		// pd.setDeploymentId(this.deploymentId);
		// ret.getDefinitions().add(pd);
		// }
		// return ret;
		//

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cuyum.jbpm.client.BRMSClient#getProcessIntances(java.lang.String)
	 */
	@Override
	public GETProcessInstancesResponse getProcessIntances(
			String processDefinition) throws BRMSClientException {

		if (!isLogged()) {
			throw new MissingAuthInformationException();

		}

		List<ProcessInstanceLog> remoteInstances = new ArrayList<ProcessInstanceLog>();
		// FIXME:KIE (versión 6.0.2-redhat-6) tiene un bug en el que cuando no
		// hay instancias de un proceso dispara un NullPointerException
		try {
			if (processDefinition != null && !processDefinition.isEmpty()) {

				remoteInstances = auditService
						.findProcessInstances(processDefinition);

			} else {
				throw new RuntimeException(
						"No se proporcionó información de definción de proceso");
			}

		} catch (NullPointerException e) {
			log.warn("Se ha capturado NullPointerException de un bug de KIE al pedir instancias de un proceso y no encontrar ninguna.");
			log.trace("Error de bug KIE capturado", e);
		}
		GETProcessInstancesResponse ret = new GETProcessInstancesResponse();
		ret.setDefinitions(new ArrayList<ProcessInstance>());

		for (ProcessInstanceLog processInstanceLog : remoteInstances) {
			ProcessInstance pi = new ProcessInstance();
			pi.setDefinitionId(processDefinition);
			pi.setId(processInstanceLog.getProcessInstanceId());
			pi.setEndDate(processInstanceLog.getEnd().toString());
			pi.setEndResult(processInstanceLog.getOutcome());
			pi.setRootToken(null);
			boolean active = processInstanceLog.getStatus() == org.jbpm.process.instance.ProcessInstance.STATE_ACTIVE;
			pi.setSuspended(!active);
			pi.setStatus(processInstanceLog.getStatus());
			pi.setKey(processInstanceLog.getExternalId());
			pi.setStartDate(processInstanceLog.getStart().toString());
			ret.getInstances().add(pi);
		}
		return ret;
	}

	public ProcessInstanceLog getProcessInstance(Long processInstanceId) {

		return auditService.findProcessInstance(processInstanceId);
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
		try {
			org.kie.api.runtime.process.ProcessInstance pi = session
					.startProcess(processId, params);
			log.info("Proceso: " + processId + " iniciado con id: "
					+ pi.getId());

			POSTCreateInstanceResponse ret = new POSTCreateInstanceResponse();
			ret.setId(pi.getId());
			ret.setProcessId(pi.getProcessId());
			ret.setState(pi.getState());
			ret.setProcessName(pi.getProcessName());
			return ret;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo "
					+ "createInstance", e);
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
		ClientResponse<?> response;
		JaxbProcessInstanceWithVariablesResponse pir = null;
		try {
			response = kieAlt.execute(Command.PROCESS_INSTANCE_WITH_VARIABLES,
					params, null);
			pir = (JaxbProcessInstanceWithVariablesResponse) response
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
			throw new WSClientException("Error al ejecutar metodo "
					+ Command.PROCESS_INSTANCE_WITH_VARIABLES, e);
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
		JaxbGenericResponse result;
		try {
			result = new JaxbGenericResponse();
			ClientResponse<?> response;

			response = kieAlt.executePost(Command.PROCESS_INSTANCE_SIGNAL, params,
					query, null);
			result = (JaxbGenericResponse) response.getEntity();
		} catch (Exception e) {
			throw new WSClientException("Error en respuesta de "
					+ Command.PROCESS_INSTANCE_SIGNAL ,e);
		}
		if (result.getStatus().name().equals("SUCCESS")) {
			POSTSignalTokenResponse ret = new POSTSignalTokenResponse();
			return ret;
		} else {
			throw new WSClientException("Error en respuesta de "
					+ Command.PROCESS_INSTANCE_SIGNAL + ". Status: "
					+ result.getStatus());
		}

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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getAssignedTasks(java.lang.String)
	 */
	@Override
	public GETAssignedTasksResponse getAssignedTasks(String actorId)
			throws BRMSClientException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#releaseTask(java.lang.String)
	 */
	@Override
	public POSTReleaseTaskResponse releaseTask(String taskId)
			throws BRMSClientException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#updateTask(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public POSTUpdateTaskResponse updateTask(String taskId,
			Map<String, String> params) throws BRMSClientException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#getRoleChecks(java.util.List)
	 */
	@Override
	public GETRoleCheckResponse getRoleChecks(List<String> roles)
			throws BRMSClientException {
		// TODO Auto-generated method stub
		return null;
	}

}
