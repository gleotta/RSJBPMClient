/**
 * 
 */
package com.cuyum.jbpm.client;

import static com.cuyum.jbpm.client.config.BRMSWsConfig.LOGOUT_WS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;


import com.cuyum.jbpm.client.artifacts.responses.BRMSClientWSResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETDatasetInstanceResponse;
import com.cuyum.jbpm.client.config.WSUrls;
import com.cuyum.jbpm.client.config.WsConfig;
import com.cuyum.jbpm.client.exception.BRMSClientException;
import com.cuyum.jbpm.client.exception.MissingAuthInformationException;
import com.cuyum.jbpm.client.exception.MissingAuthPasswordInformationException;
import com.cuyum.jbpm.client.exception.MissingAuthUserInformationException;
import com.cuyum.jbpm.client.exception.UnauthorizedUserException;
import com.cuyum.jbpm.client.exception.WSClientException;
import com.cuyum.jbpm.client.exception.WSException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author Jorge Morando
 * 
 */
public abstract class BRMSBaseClient {

	public static final Logger log = Logger.getLogger(BRMSClientImpl.class);

	protected String scheme = "http://";
	protected String host;
	protected String port;
	protected String user;
	protected String pass;

	protected HttpClient httpclient;

	protected final HttpRequestBase getMethod(WsConfig ws, String[]... args) {
		return getMethod(ws, null, args);

	}

	protected HttpRequestBase getMethod(WsConfig ws, List<BasicNameValuePair> bodyParams, String[]... args) {
		String url = null;
		
		
		
		if (ws.hasPathParams() && args != null) {
			url = ws.injectPathParams2(args);
		} else {
			url = new String(ws.path());
			
		}
		System.out.println("URL: "+url);
		HttpRequestBase theMethod = null;
		
		try {

			String urlpath = buildUrl(url);
			System.out.println("URLPath getMethod: "+urlpath);
			switch (ws.getMethod()) {
			case POST:
				theMethod = new HttpPost(urlpath);
				if (bodyParams != null) {
					if (bodyParams.size() == 0) {
						bodyParams.add(new BasicNameValuePair("submit","submit"));
					}
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					for (BasicNameValuePair nvp : bodyParams) {
						entity.addPart(nvp.getName(),
								new StringBody(nvp.getValue()));
						System.out.println("BODY NP: "+nvp.getName()+":"+nvp.getValue());
					}
					((HttpPost) theMethod).setEntity(entity);
					System.out.println("Entity:"+entity.getContentType().getName()+" "+entity.getContentType().getValue());
				}
				break;
			default: // GET
				theMethod = new HttpGet(urlpath);
				break;
			}
			System.out.println("THE METHOD:"+theMethod);
			return theMethod;
		} catch (Exception e) {
			log.error(e);
			throw new BRMSClientException(e);
		}

	}

	private String buildUrl(String wsUrl) {
		/* scheme+server+port */
		String serverURL = getServerUrl();

		/* /path/to/ws */
		String fullURL = serverURL +wsUrl;

		return fullURL;

	}

	protected String getServerUrl() {
		StringBuilder rootURL = new StringBuilder();
		// http
		rootURL.append(scheme);

		/* localhost */
		rootURL.append(host);

		/* :8080 */
		if (port != null && !port.isEmpty())
			rootURL.append(":" + port);

		String serverURL = rootURL.toString();

		return serverURL;
	}

	@SuppressWarnings("unchecked")
	protected final <R extends BRMSClientWSResponse> R processResponse(
			HttpResponse rawResponse,
			Class<? extends BRMSClientWSResponse> clazz)
			throws BRMSClientException {
		log.debug("Processing " + clazz.getSimpleName());

		// Create Jackson object mapper
		ObjectMapper mapper = new ObjectMapper();

		try {
			String entity = EntityUtils.toString(rawResponse.getEntity());
			log.debug("Atempting entity Conversion to " + clazz.getSimpleName());
			R respEntity = (R) mapper.readValue(entity, clazz);
			return respEntity;
		} catch (Exception e) {
			throw new WSClientException(e);
		}
	}
	
	protected final String processResponse(
			HttpResponse rawResponse)
			throws BRMSClientException {

		// Create Jackson object mapper
		ObjectMapper mapper = new ObjectMapper();

		try {
			String entity = EntityUtils.toString(rawResponse.getEntity());
			return entity;
		} catch (Exception e) {
			throw new WSClientException(e);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	protected final GETDatasetInstanceResponse processDatasetInstanceResponse(
			HttpResponse rawResponse) throws BRMSClientException {

		// Create Jackson object mapper
		XmlMapper mapper = new XmlMapper();
		try {
			String entity = EntityUtils.toString(rawResponse.getEntity());
			log.debug("resultado de DataInput\n" + entity);

			List values = (List) mapper.readValue(entity, List.class);
			log.debug("LIST: " + values);

			Map<String, String> data = new HashMap<String, String>();
			for (Object obj : values) {
				Map elemento = (Map) obj;
				String key = (String) elemento.get("key");
				String value = ((Map) elemento.get("value")).toString();
				value = value.substring(value.lastIndexOf("=") + 1,
						value.lastIndexOf("}"));
				data.put(key, value);
				// log.info("value: "+value);

			}

			GETDatasetInstanceResponse gdip = new GETDatasetInstanceResponse();
			gdip.setDataset(data);

			return gdip;
			// return respEntity;
		} catch (Exception e) {
			throw new WSClientException(e);
		}

	}

	/*
	 * Execute a login request
	 */
	protected final HttpResponse executeLogin(String user, String pass) {
		HttpPost authMethod = null;
		try {
			HttpResponse response = null;
			authMethod = new HttpPost(getServerUrl() + WSUrls.LOGIN_WS_URL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("j_username", user));
			nvps.add(new BasicNameValuePair("j_password", pass));
			authMethod.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			response = execute(authMethod);
			return response;
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo "
					+ authMethod, e);

		} finally {
			authMethod.releaseConnection();
		}
	}

	/*
	 * Execute a request
	 */
	protected HttpResponse execute(HttpRequestBase method) {
		HttpResponse resp = null;

		try {
			log.debug("Ejecutando: " + method);
			System.out.println("Ejecutando: " + method);
			//method.get
			resp = httpclient.execute(method);
		} catch (Exception e) {
			log.error(e.getMessage());
			System.out.println("Error: " + e.getMessage());
			throw new WSException("Error al ejecutar: " + method, e);
		}

		return resp;
	}
	
	public boolean isLogged() {
		HttpGet sidMethod = new HttpGet(getServerUrl() + WSUrls.SID_WS_URL);
		// this call will fail because it is not logged in
		// the method has to be called for cookie storage
		HttpResponse loggingScreen = execute(sidMethod);

		try {
			String entity = EntityUtils.toString(loggingScreen.getEntity());
			if (entity.indexOf("HTTP 401") == -1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e);
			throw new WSException("Error ejecutando " + sidMethod, e);
		} finally {
			sidMethod.releaseConnection();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cuyum.jbpm.client.BRMSClient#disconnect()
	 */

	public void login(String user, String pass) throws Exception{

		if (user == null)
			throw new MissingAuthUserInformationException();
		if (pass == null)
			throw new MissingAuthPasswordInformationException();

		this.user = user;
		this.pass = pass;

		HttpResponse response = null;

		if(isLogged()){
			log.warn("Tratando de loguearse al cliente REST mientras existe una sesi\u00F3n activa");
			logout();
		}
		response = executeLogin(user, pass);
		boolean allowed = processResponse(response).indexOf("401")==-1;
		int status = response.getStatusLine().getStatusCode();

		if(!allowed){
			throw new UnauthorizedUserException("El usuario \""+ user +"\" no se encuentra autorizado en la plataforma BRMS");
		}
		
		if (status != 200 && status != 302) {
			log.error("El usuario no se puede logear: status " + status);
			throw new MissingAuthInformationException(
					"El usuario no se puede logear: status " + status);
		}

	}
	
	public void logout() {
		log.debug("Disconnecting...");
		// http response

		// execute ws
		HttpRequestBase method = getMethod(LOGOUT_WS);
		try {
			this.execute(method);
		} catch (Exception e) {
			throw new WSClientException("Error al ejecutar metodo " + method, e);

		} finally {
			method.releaseConnection();
		}
		// no response to process
	}

}
