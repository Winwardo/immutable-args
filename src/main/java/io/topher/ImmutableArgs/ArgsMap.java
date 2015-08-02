package io.topher.ImmutableArgs;

import java.util.HashMap;
import java.util.Map;

public class ArgsMap {
	private final Map<String, String>	argsData;

	public ArgsMap(String[] args) {
		argsData = new HashMap<>();

		boolean lastTokenWasAKey = false;
		String key = null;
		String value = null;

		for (int i = 0; i < args.length; i++) {
			final String token = args[i];
			final boolean tokenIsAKey = token.startsWith("-");

			if (tokenIsAKey) {
				if (lastTokenWasAKey) {
					addBooleans(key);
				}

				lastTokenWasAKey = true;
				key = token.substring(1); // Remove the leading hyphen
			} else {
				value = token;
				if (lastTokenWasAKey) {
					argsData.put(key, value);
				}

				lastTokenWasAKey = false;
			}
		}

		// Capture any trailing booleans
		if (lastTokenWasAKey) {
			addBooleans(key);
		}
	}

	private void addBooleans(String bools) {
		for (char bool : bools.toCharArray()) {
			argsData.put("" + bool, "true");
		}
	}

	public String get(String key) {
		return argsData.get(key);
	}
}
