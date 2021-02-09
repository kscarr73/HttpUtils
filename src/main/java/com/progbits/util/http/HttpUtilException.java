package com.progbits.util.http;

/**
 *
 * @author scarr
 */
public class HttpUtilException extends Exception {

	public HttpUtilException() {
	}
	
	public HttpUtilException(String message) {
		super(message);
	}
	
	public HttpUtilException(String message, Throwable ex) {
		super(message, ex);
	}
}
