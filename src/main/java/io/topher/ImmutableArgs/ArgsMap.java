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
				String keyName = token.substring(1);
				if (lastWasAKey) {
					argsData.put(key, "true");
				}
				lastWasAKey = true;
				key = keyName;
			} else {
				value = token;
				if (lastWasAKey) {
					argsData.put(key, value);
				}
				lastWasAKey = false;
			}
		}
		if (lastWasAKey) {
			// Clean up trailing booleans
			argsData.put(key, "true");
		}
	}

	public Map<String, String> get() {
		return argsData;
	}
}
