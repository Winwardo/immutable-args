package io.topher.ImmutableArgs;

public class Arg {
	private final String	name;
	private final String	type;

	public Arg(final String name,
		final String type) {
		this.name = name;
		this.type = type;
	}

	public final String getName() {
		return name;
	}

	public final String getType() {
		return type;
	}
}
