package io.topher.ImmutableArgs;

import io.topher.ImmutableArgs.exceptions.MalformedArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedSchemaException;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArgsImmutable implements Args {
	private static PrintStream			o					= System.out;

	private static final char			INTEGER_SPECIFIER	= '#';
	private static final char			STRING_SPECIFIER	= '*';
	private final Map<String, Boolean>	booleans;
	private final Map<String, String>	strings;
	private final Map<String, Integer>	integers;

	public ArgsImmutable(String schema,
		String[] args) throws MalformedSchemaException,
		MalformedArgsException {

		Map<String, String> argsData = argsListToMap(args);

		booleans = new HashMap<String, Boolean>();
		strings = new HashMap<String, String>();
		integers = new HashMap<String, Integer>();

		String[] tokens = schema.split(",");

		if (argsData.size() != tokens.length && schema.length() != 0) { throw new MalformedArgsException(
			"Number of arguments does not match the schema."); }

		for (String token_ : tokens) {
			String token = token_.trim();
			if (token.length() == 0) {
				continue;
			}

			String key;

			if (token.length() == 1) {
				if (!token.matches("[a-zA-Z]")) { throw new MalformedSchemaException(
					String.format(
						"Invalid token `%s`. Did you forget an identifier?",
						token)); }
				// boolean
				key = token;
				final String value = argsData.get(key);
				final boolean parsedBoolean = Boolean.parseBoolean(value);
				booleans.put(key, parsedBoolean);
				continue;
			}

			final int lastCharIndex = token.length() - 1;
			char lastChar = token.charAt(lastCharIndex);
			key = token.substring(0, lastCharIndex);
			final String value = argsData.get(key);
			switch (lastChar) {
				case STRING_SPECIFIER:
					strings.put(key, value);
					break;
				case INTEGER_SPECIFIER:
					try {
						integers.put(key, Integer.parseInt(value));
					} catch (NumberFormatException e) {
						throw new MalformedArgsException(String.format(
							"Invalid integer `%s` supplied.",
							value), e);
					}
					break;
				default:
					throw new MalformedSchemaException(String.format(
						"Invalid token type `%s`.",
						lastChar));
			}

		}

	}

	public static Map<String, String> argsListToMap(String[] args) {
		return new ArgsMap(args).get();
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
