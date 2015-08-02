package io.topher.ImmutableArgs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestArgsMap {
	private static final String	TRUE	= "true";
	private static final String	VALUE_1	= "Some value";
	private static final String	VALUE_2	= "Some other value";

	@Test
	public void singleImpliedBool() {
		ArgsMap out = new ArgsMap(new String[] { "-x" });
		assertEquals(TRUE, out.get("x"));
	}

	@Test
	public void singleValue() {
		ArgsMap out = new ArgsMap(new String[] { "-x", VALUE_1 });
		assertEquals(VALUE_1, out.get("x"));
	}

	@Test
	public void twoValues() {
		ArgsMap out = new ArgsMap(new String[] { "-x", VALUE_1, "-y", VALUE_2 });
		assertEquals(VALUE_1, out.get("x"));
		assertEquals(VALUE_2, out.get("y"));
	}

	@Test
	public void twoValues_firstIsImpliedBoolean() {
		ArgsMap out = new ArgsMap(new String[] { "-x", "-y", VALUE_2 });
		assertEquals(TRUE, out.get("x"));
		assertEquals(VALUE_2, out.get("y"));
	}

	@Test
	public void twoValues_secondIsImpliedBoolean() {
		ArgsMap out = new ArgsMap(new String[] { "-x", VALUE_1, "-y", });
		assertEquals(VALUE_1, out.get("x"));
		assertEquals(TRUE, out.get("y"));
	}

	@Test
	public void twoValues_bothAreImpliedBoolean() {
		ArgsMap out = new ArgsMap(new String[] { "-x", "-y", });
		assertEquals(TRUE, out.get("x"));
		assertEquals(TRUE, out.get("y"));
	}

	@Test
	public void twoBoolsInOneFlag() {
		ArgsMap out = new ArgsMap(new String[] { "-xy", });
		assertEquals(TRUE, out.get("x"));
		assertEquals(TRUE, out.get("y"));
	}
}
