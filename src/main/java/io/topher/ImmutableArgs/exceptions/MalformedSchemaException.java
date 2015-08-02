package io.topher.ImmutableArgs.exceptions;

public class MalformedSchemaException extends ArgsException {
	private static final long	serialVersionUID	= 4307386696148143104L;

	public MalformedSchemaException() {
	}

	public MalformedSchemaException(String message) {
		super(message);
	}

	public MalformedSchemaException(Throwable cause) {
		super(cause);
	}

	public MalformedSchemaException(String message,
		Throwable cause) {
		super(message, cause);
	}

	public MalformedSchemaException(String message,
		Throwable cause,
		boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
