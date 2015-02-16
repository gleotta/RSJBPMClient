/**
 * 
 */
package com.cuyum.jbpm.client.kie;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jbpm.process.audit.command.AuditCommand;
import org.kie.services.client.api.RestRequestHelper;
import org.kie.services.client.api.command.RemoteConfiguration;
import org.kie.services.client.api.command.exception.RemoteApiException;
import org.kie.services.client.api.command.exception.RemoteCommunicationException;
import org.kie.services.client.api.command.exception.RemoteTaskException;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandsRequest;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandsResponse;
import org.kie.services.client.serialization.jaxb.rest.JaxbExceptionResponse;

import com.cuyum.jbpm.client.exception.WSClientException;

/**
 * @author Jorge Morando
 * 
 */
public class KieAlternative {

	private static final Logger log = Logger.getLogger(KieAlternative.class);

	private String userId;

	private String password;

	private URL baseRestUrl;

	private String deploymentId;

	private RemoteConfiguration config;

	public KieAlternative(URL baseRestUrl, String deploymentId, String userId,
			String password) {
		this.userId = userId;
		this.password = password;
		this.baseRestUrl = baseRestUrl;
		this.deploymentId = deploymentId;
		// config
		config = new RemoteConfiguration(deploymentId, baseRestUrl, userId,
				password);

	}

	public ClientResponse<?> execute(Command command) throws Exception {
		return execute(command, null, null, null);
	}

	public ClientResponse<?> execute(Command command,
			Map<String, String> pathParams, Boolean union) throws Exception {
		return execute(command, pathParams, null, null);
	}

	public ClientResponse<?> execute(Command command,
			Map<String, String> pathParams,
			HashMap<String, List<String>> queryParams, Boolean union)
			throws Exception {
		ClientRequestFactory requestFactory;
		String commandUrl = "";
		String commandQuery = "";
		if (pathParams != null) {
			commandUrl = command.getUrl(pathParams);
			
		}
		if (queryParams != null) {
			commandQuery = command.getQuery(queryParams);
		}

		
		String urlString = new URL(baseRestUrl,baseRestUrl.getPath()+commandUrl+commandQuery).toExternalForm();
		//
		requestFactory = RestRequestHelper.createRequestFactory(baseRestUrl,
				userId, password, 30);

		ClientRequest request = requestFactory.createRequest(urlString);

		request.accept(command.getContentType());

		ClientResponse<?> ret = request.get(command.getResponseClass());

		if (ret.getStatus() != 200) {

			throw new WSClientException("Error en respuesta de "
					+ command.getUrl() + ". Status: " + ret.getStatus());
		}

		return ret;

	}

	public ClientResponse<?> executePost(Command command,
			Map<String, String> pathParams,
			Map<String, List<String>> queryParams, Object body)
			throws Exception {
		ClientRequestFactory requestFactory;

		String commandUrl = "";
		String commandQuery = "";
		if (pathParams != null) {
			commandUrl = command.getUrl(pathParams);
			
		}
		if (queryParams != null) {
			commandQuery = command.getQuery(queryParams);
		}

		
		String urlString = new URL(baseRestUrl,baseRestUrl.getPath()+commandUrl+commandQuery).toExternalForm();
		//
		requestFactory = RestRequestHelper.createRequestFactory(baseRestUrl,
				userId, password, 30);

		ClientRequest request = requestFactory.createRequest(urlString);

		if (body != null) {
			JaxbSerializationProvider provider = new JaxbSerializationProvider();
			provider.addJaxbClasses(body.getClass());
			String jaxbRequestString = provider.serialize(body);
			request.body(MediaType.APPLICATION_XML, jaxbRequestString);
		}
		request.accept(command.getContentType());
		ClientResponse<?> ret = null;

		log.debug("Sending POST request with "
				+ command.getClass().getSimpleName() + " to "
				+ request.getUri());
		ret = request.post(command.getResponseClass());

		if (ret.getStatus() != 200) {

			throw new WSClientException("Error en respuesta de "
					+ command.getUrl() + ". Status: " + ret.getStatus());
		}
		return ret;

	}

}