package br.eti.amazu.infra.exception;

public class NullContextException extends Exception {

	private static final long serialVersionUID = 1L;

	public NullContextException(String message) {
		super(message);
	}
}
