package io.topher.ImmutableArgs.exceptions;

public class MalformedArgsException extends ArgsException {
	private static final long	serialVersionUID	= 4307386696148143104L;

	public MalformedArgsException(String message) {
		super(message);
	}

	public MalformedArgsException(String message,
		Throwable cause) {
		super(message, cause);
	}
}
