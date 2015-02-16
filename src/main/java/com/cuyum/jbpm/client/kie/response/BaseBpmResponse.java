/**
 * 
 */
package com.cuyum.jbpm.client.kie.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Jorge Morando
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseBpmResponse {

	private String status;
	
	private String url;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
