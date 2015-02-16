/**
 * Response package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.artifacts.responses;

import java.util.List;

import com.cuyum.jbpm.client.artifacts.ServerPlugins;


/**
 * GET Server Status Response
 * @author Jorge Morando
 *
 */
public class GETServerStatusResponse extends BRMSClientWSResponse {

	/**
	 * @return the plugins
	 */
	public List<ServerPlugins> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins the plugins to set
	 */
	public void setPlugins(List<ServerPlugins> plugins) {
		this.plugins = plugins;
	}

	List<ServerPlugins> plugins;

	@Override
	public String toString() {
		return "GETServerStatusResponse [status=" + status + ", message="
				+ message + "]";
	}
	
	
}
