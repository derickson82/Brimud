package com.brimud.service;

public class BuilderException extends Exception {

  private static final long serialVersionUID = 1L;

	public BuilderException() {
	}

	public BuilderException(String message) {
		super(message);
	}

	public BuilderException(Throwable cause) {
		super(cause);
	}

	public BuilderException(String message, Throwable cause) {
		super(message, cause);
	}

}
