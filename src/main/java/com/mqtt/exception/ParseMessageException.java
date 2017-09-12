package com.mqtt.exception;

/**
 * 
 * @author panxincheng
 * @date 2011-7-20 
 */
public class ParseMessageException extends Exception {

	private static final long serialVersionUID = 4907927045508345966L;

	/**
	 * 
	 */
	public ParseMessageException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ParseMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ParseMessageException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ParseMessageException(Throwable cause) {
		super(cause);
	}

}
