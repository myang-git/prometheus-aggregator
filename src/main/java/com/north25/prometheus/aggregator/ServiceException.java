package com.north25.prometheus.aggregator;

class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8197369835903039837L;
	private final int httpStatus;

	public ServiceException(String message, int httpStatus, Exception cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}
	
	public int getHttpStatus() {
		return this.httpStatus;
	}
	
}
