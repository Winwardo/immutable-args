package io.topher.ImmutableArgs;

import io.topher.ImmutableArgs.exceptions.ArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedSchemaException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class ArgsImmutable implements Args {
	private static final String			SPECIFIER_BOOLEAN	= "";
	private static final String			SPECIFIER_INTEGER	= "#";
	private static final String			SPECIFIER_STRING	= "*";

	private final Map<String, Boolean>	booleans			= new HashMap<String, Boolean>();
	private final Map<String, String>	strings				= new HashMap<String, String>();
	private final Map<String, Integer>	integers			= new HashMap<String, Integer>();

	public ArgsImmutable(String schema,
		String[] args) throws ArgsException {

		final ArgsMap argsMap = new ArgsMap(args);
		makeTokenStream(schema).map(token -> makeArg(token)).forEach(
			arg -> insertArgIntoRelevantMap(arg, argsMap));
	}

	private Stream<String> makeTokenStream(String schema) {
		return Arrays
			.asList(schema.split(","))
			.stream()
			.map(s -> s.trim())
			.filter(s -> s.length() > 0);
	}

	private Arg makeArg(String token) {
		final Pattern pattern = Pattern.compile("^([a-zA-Z])(.*?)$");
		final Matcher matcher = pattern.matcher(token);

		if (matcher.matches()) {
			final String name = matcher.group(1);
			final String type = matcher.group(2);

			return new Arg(name, type);
		} else {
			throw new MalformedSchemaException(String.format(
				"Invalid token `%s`.",
				token));
		}
	}

	private void insertArgIntoRelevantMap(Arg arg, ArgsMap argsMap) {
		final String argName = arg.getName();
		final String value = argsMap.get(argName);

		switch (arg.getType()) {
			case SPECIFIER_BOOLEAN:
				putBooleanArg(argName, value);
				break;
			case SPECIFIER_STRING:
				putStringArg(argName, value);
				break;
			case SPECIFIER_INTEGER:
				putIntegerArg(argName, value);
				break;
			default:
				throw new MalformedSchemaException(String.format(
					"Invalid token type `%s`.",
					argName));
		}
	}

	private void putStringArg(String argName, String value) {
		strings.put(argName, value);
	}

	private void putIntegerArg(String argName, String value) {
		try {
			final int parsedInt = Integer.parseInt(value);
			integers.put(argName, parsedInt);
		} catch (NumberFormatException e) {
			throw new MalformedArgsException(String.format(
				"Invalid integer `%s` supplied.",
				value), e);
		}
	}

	private void putBooleanArg(String argName, String value) {
		final String lowerValue = value == null ? "" : value.toLowerCase();
		if ("true".equals(lowerValue)) {
			booleans.put(argName, true);
		} else if ("false".equals(lowerValue)) {
			booleans.put(argName, false);
		} else {
			throw new MalformedArgsException(String.format(
				"Invalid boolean `%s` supplied.",
				value));
		}
	}

	@Override
	public Optional<Boolean> getBoolean(String key) {
		return Optional.ofNullable(booleans.get(key));
	}

	@Override
	public Optional<String> getString(String key) {
		return Optional.ofNullable(strings.get(key));
	}

	@Override
	public Optional<Integer> getInteger(String key) {
		return Optional.ofNullable(integers.get(key));
	}

	@Override
	public int cardinality() {
		return booleans.size() + strings.size() + integers.size();
	}
}
