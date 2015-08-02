package io.topher.ImmutableArgs.exceptions;

public class ArgsException extends RuntimeException {
	private static final long	serialVersionUID	= -3965931086098716472L;

	public ArgsException(String message) {
		super(message);
	}

	public ArgsException(String message,
		Throwable cause) {
		super(message, cause);
	}
}
