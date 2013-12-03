/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Thrown when a WS fails client-side (HTTP STATUS CODE RANGE 400)
 * @author Jorge Morando
 *
 */
public class WSClientException extends WSException {

	private static final long serialVersionUID = -3069598718114195119L;

	public WSClientException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WSClientException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public WSClientException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WSClientException(Throwable cause) {
		super(cause);
	}

}
