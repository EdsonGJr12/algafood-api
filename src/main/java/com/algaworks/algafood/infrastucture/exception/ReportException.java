package com.algaworks.algafood.infrastucture.exception;

public class ReportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReportException(String message, Exception e) {
		super(message, e);
	}
}
