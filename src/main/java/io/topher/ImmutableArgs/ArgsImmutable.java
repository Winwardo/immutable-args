package io.topher.ImmutableArgs;

import io.topher.ImmutableArgs.exceptions.ArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedArgsException;
import io.topher.ImmutableArgs.exceptions.MalformedSchemaException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArgsImmutable implements Args {
	private static final String			SPECIFIER_BOOLEAN	= "b";
	private static final char			SPECIFIER_INTEGER	= '#';
	private static final char			SPECIFIER_STRING	= '*';

	private final Map<String, Boolean>	booleans			= new HashMap<String, Boolean>();
	private final Map<String, String>	strings				= new HashMap<String, String>();
	private final Map<String, Integer>	integers			= new HashMap<String, Integer>();

	public ArgsImmutable(String schema,
		String[] args) throws ArgsException {
		Map<String, String> argsData = new ArgsMap(args).get();

		Stream<String> tokenStream = tokenStream(schema);
		List<String> tokens = tokenStream.collect(Collectors.toList());

		if (argsData.size() != tokens.size()) { throw new MalformedArgsException(
			"Number of arguments does not match the schema."); }

		// split stream into token names and types
		tokenStream(schema)
			.map(
				token -> {
					Pattern p = Pattern.compile("^([a-zA-Z])(.*?)$");
					Matcher m = p.matcher(token);
					if (m.matches()) {
						if (m.groupCount() != 2) { throw new MalformedSchemaException(
							String.format(
								"Invalid token `%s`. Bad things eep",
								token)); }

						final String name = m.group(1);
						String type = m.group(2);
						if (type.length() == 0) {
							type = SPECIFIER_BOOLEAN;
						}

						return new Arg(name, type);
					} else {
						throw new MalformedSchemaException(String.format(
							"Invalid token `%s`. REAL bad things eep",
							token));
					}
				})
			.forEach(
				arg -> {
					System.out.println(arg.getName());
					System.out.println(arg.getType());

					String argName = arg.getName();
					switch (arg.getType()) {
						case SPECIFIER_BOOLEAN:
							final String value = argsData.get(argName);
							final boolean parsedBoolean = Boolean
								.parseBoolean(value);
							booleans.put(argName, parsedBoolean);
							break;
					}
				});

		for (String token : tokens) {
			if (token.length() <= 1) {
				continue;
			}

			String key;

			final int lastCharIndex = token.length() - 1;
			char lastChar = token.charAt(lastCharIndex);
			key = token.substring(0, lastCharIndex);
			final String value = argsData.get(key);
			switch (lastChar) {
				case SPECIFIER_STRING:
					strings.put(key, value);
					break;
				case SPECIFIER_INTEGER:
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
		return tokenStream(schema).collect(Collectors.toList());
	}

	private Stream<String> tokenStream(String schema) {
		return Arrays
			.asList(schema.split(","))
			.stream()
			.map(s -> s.trim())
			.filter(s -> s.length() > 0);
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
