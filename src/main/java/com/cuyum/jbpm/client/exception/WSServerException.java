/**
 * Exception package for BRMS Rest Connectivity Utility
 */
package com.cuyum.jbpm.client.exception;

/**
 * Thrown when a WS fails server-side (HTTP STATUS CODE RANGE 500)
 * @author Jorge Morando
 *
 */
public class WSServerException extends WSException {

	private static final long serialVersionUID = -3069598718114195119L;

	public WSServerException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WSServerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public WSServerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WSServerException(Throwable cause) {
		super(cause);
	}

}
