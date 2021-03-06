package com.cuyum.jbpm.client.config;

import javax.ws.rs.core.MediaType;

import com.cuyum.jbpm.client.artifacts.responses.BRMSClientWSResponse;



/**
 * @author Jorge Morando
 *
 */
public interface WsConfig {
	
	public enum WsMethod {
		GET,POST;
	}
	
	/**
	 * Retrieves the Context path configuration for the ws
	 * @return the string ws context path
	 */
	public String path();
	
	/**
	 * Boolean Flag indicating authentication requirement
	 * @return true/false
	 */
	public boolean requiresAuth();
	
	/**
	 * Indicates What Media Type the WS accepts
	 * @return MediaType the ws accepts
	 */
	public MediaType accepts();
	
	/**
	 * Retrieves the response class object for the ws
	 * @return
	 */
	public Class<? extends BRMSClientWSResponse> responseClass();
	
	/**
	 * Retrieves the request method of the web service
	 * @return
	 */
	public WsMethod getMethod();
	
	/**
	 * Checks if ws has path params
	 * @return
	 */
	public boolean hasPathParams();
	
	/**
	 * Injects path params in ws path
	 * @param params
	 */
	public String injectPathParams2(String[]...params);
	
}
