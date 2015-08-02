package io.topher.ImmutableArgs;

import java.util.Optional;

public interface Args {
	Optional<Boolean> getBoolean(String key);

	Optional<String> getString(String key);

	Optional<Integer> getInteger(String key);

	int cardinality();
}
