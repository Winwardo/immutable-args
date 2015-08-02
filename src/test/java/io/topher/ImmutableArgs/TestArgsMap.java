package io.topher.ImmutableArgs;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class TestArgsMap {
	private static final String	TRUE	= "true";
	private static final String	VALUE_1	= "Some value";
	private static final String	VALUE_2	= "Some other value";

	@Test
	public void singleImpliedBool() {
		Map<String, String> out = new ArgsMap(new String[] { "-x" }).get();
		assertEquals(TRUE, out.get("x"));
	}

	@Test
	public void singleValue() {
		Map<String, String> out = new ArgsMap(new String[] { "-x", VALUE_1 })
			.get();
		assertEquals(VALUE_1, out.get("x"));
	}

	@Test
	public void twoValues() {
		Map<String, String> out = new ArgsMap(new String[] { "-x", VALUE_1,
				"-y", VALUE_2 }).get();
		assertEquals(VALUE_1, out.get("x"));
		assertEquals(VALUE_2, out.get("y"));
	}

	@Test
	public void twoValues_firstIsImpliedBoolean() {
		Map<String, String> out = new ArgsMap(new String[] { "-x", "-y",
				VALUE_2 }).get();
		assertEquals(TRUE, out.get("x"));
		assertEquals(VALUE_2, out.get("y"));
	}

	@Test
	public void twoValues_secondIsImpliedBoolean() {
		Map<String, String> out = new ArgsMap(new String[] { "-x", VALUE_1,
				"-y", }).get();
		assertEquals(VALUE_1, out.get("x"));
		assertEquals(TRUE, out.get("y"));
	}

	@Test
	public void twoValues_bothAreImpliedBoolean() {
		Map<String, String> out = new ArgsMap(new String[] { "-x", "-y", })
			.get();
		assertEquals(TRUE, out.get("x"));
		assertEquals(TRUE, out.get("y"));
	}

	@Test
	public void twoBoolsInOneFlag() {
		Map<String, String> out = new ArgsMap(new String[] { "-xy", }).get();
		assertEquals(TRUE, out.get("x"));
		assertEquals(TRUE, out.get("y"));
	}
}
