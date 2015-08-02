package io.topher.ImmutableArgs;

import io.topher.ImmutableArgs.exceptions.ArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedSchemaException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArgsImmutable implements Args {
	private static final char			INTEGER_SPECIFIER	= '#';
	private static final char			STRING_SPECIFIER	= '*';

	private final Map<String, Boolean>	booleans			= new HashMap<String, Boolean>();
	private final Map<String, String>	strings				= new HashMap<String, String>();
	private final Map<String, Integer>	integers			= new HashMap<String, Integer>();

	public ArgsImmutable(String schema,
		String[] args) throws ArgsException {
		Map<String, String> argsData = new ArgsMap(args).get();

		List<String> tokens = getTokens(schema);

		if (argsData.size() != tokens.size()) { throw new MalformedArgsException(
			"Number of arguments does not match the schema."); }

		for (String token : tokens) {
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

	private List<String> getTokens(String schema) {
		return Arrays
			.asList(schema.split(","))
			.stream()
			.map(s -> s.trim())
			.filter(s -> s.length() > 0)
			.collect(Collectors.toList());
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
