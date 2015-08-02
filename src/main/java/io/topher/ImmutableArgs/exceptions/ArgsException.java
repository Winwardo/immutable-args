package io.topher.ImmutableArgs.exceptions;

public class ArgsException extends RuntimeException {
	private static final long	serialVersionUID	= -3965931086098716472L;

	public ArgsException() {
	}

	public ArgsException(String message) {
		super(message);
	}

	public ArgsException(Throwable cause) {
		super(cause);
	}

	public ArgsException(String message,
		Throwable cause) {
		super(message, cause);
	}

	public ArgsException(String message,
		Throwable cause,
		boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
