/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Parent BRMS 5.3 REST Client Utility Exception
 * @author Jorge Morando
 */
public class BRMSClientException extends RuntimeException {

	private static final long serialVersionUID = 3353019518984337839L;

	/**
	 * Parent BRMS 5.3 REST Client Utility Exception
	 */
	public BRMSClientException() {
		super();
	}

	/**
	 * Parent BRMS 5.3 REST Client Utility Exception
	 * @param message
	 * @param cause
	 */
	public BRMSClientException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Parent BRMS 5.3 REST Client Utility Exception
	 * @param message
	 */
	public BRMSClientException(String message) {
		super(message);
	}

	/**
	 * Parent BRMS 5.3 REST Client Utility Exception
	 * @param cause
	 */
	public BRMSClientException(Throwable cause) {
		super(cause);
	}

}
