package com.example.connectionframework.requestframework.httpconnection;

public class HttpConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpConnectionException() {
	}

	public HttpConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public HttpConnectionException(Throwable throwable) {
		super(throwable);
	}

	public HttpConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}	
}
