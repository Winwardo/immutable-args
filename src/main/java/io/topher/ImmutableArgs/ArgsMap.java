package io.topher.ImmutableArgs;

import java.util.HashMap;
import java.util.Map;

public class ArgsMap {
	private final Map<String, String>	argsData;

	public ArgsMap(String[] args) {
		argsData = new HashMap<>();

		boolean lastWasAKey = false;
		String key = null;
		String value = null;

		for (int i = 0; i < args.length; i++) {
			String token = args[i];
			final boolean tokenIsAKey = token.startsWith("-");

			if (tokenIsAKey) {
				if (lastWasAKey) {
					addBools(key);
				}

				lastWasAKey = true;
				key = token.substring(1);
			} else {
				value = token;
				if (lastWasAKey) {
					argsData.put(key, value);
				}

				lastWasAKey = false;
			}
		}

		if (lastWasAKey) {
			addBools(key);
		}
	}

	private void addBools(String bools) {
		for (char bool : bools.toCharArray()) {
			argsData.put("" + bool, "true");
		}
	}

	public Map<String, String> get() {
		return argsData;
	}
}
