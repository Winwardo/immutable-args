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

public class ArgsImmutable implements Args {
	private static final String			SPECIFIER_BOOLEAN	= "";
	private static final String			SPECIFIER_INTEGER	= "#";
	private static final String			SPECIFIER_STRING	= "*";

	private final Map<String, Boolean>	booleans			= new HashMap<String, Boolean>();
	private final Map<String, String>	strings				= new HashMap<String, String>();
	private final Map<String, Integer>	integers			= new HashMap<String, Integer>();

	public ArgsImmutable(String schema,
		String[] args) throws ArgsException {

		ArgsMap argsMap = new ArgsMap(args);

		tokenStream(schema).map(token -> makeArg(token)).forEach(
			arg -> insertArgIntoRelevantMap(arg, argsMap));
	}

	private Stream<String> tokenStream(String schema) {
		return Arrays
			.asList(schema.split(","))
			.stream()
			.map(s -> s.trim())
			.filter(s -> s.length() > 0);
	}

	private Arg makeArg(String token) {
		Pattern p = Pattern.compile("^([a-zA-Z])(.*?)$");
		Matcher m = p.matcher(token);

		if (m.matches()) {
			final String name = m.group(1);
			String type = m.group(2);

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
				final boolean parsedBoolean = Boolean.parseBoolean(value);
				booleans.put(argName, parsedBoolean);
				break;
			case SPECIFIER_STRING:
				strings.put(argName, value);
				break;
			case SPECIFIER_INTEGER:
				try {
					integers.put(argName, Integer.parseInt(value));
				} catch (NumberFormatException e) {
					throw new MalformedArgsException(String.format(
						"Invalid integer `%s` supplied.",
						value), e);
				}
				break;
			default:
				throw new MalformedSchemaException(String.format(
					"Invalid token type `%s`.",
					argName));
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
