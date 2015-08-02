package io.topher.ImmutableArgs;

public class MalformedArgsException extends ArgsException {
	private static final long	serialVersionUID	= 4307386696148143104L;

	public MalformedArgsException() {
	}

	public MalformedArgsException(String message) {
		super(message);
	}

	public MalformedArgsException(Throwable cause) {
		super(cause);
	}

	public MalformedArgsException(String message,
		Throwable cause) {
		super(message, cause);
	}

	public MalformedArgsException(String message,
		Throwable cause,
		boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
