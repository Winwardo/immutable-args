package io.topher.ImmutableArgs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

public class TestArgsImmutable {
	private static final String	EXAMPLE_VALID_FALSE_ARG	= "false";
	private static final String	EXAMPLE_VALID_TRUE_ARG	= "true";
	private static final String	EXAMPLE_VALID_INT_ARG	= "7";
	private static final String	EXAMPLE_STRING_ARG		= "Hello!";

	@Test
	public void getBoolean_trivial() throws ArgsException {
		Args args = new ArgsImmutable("x", new String[] { "-x" });

		final Optional<Boolean> x = args.getBoolean("x");
		assertTrue(x.isPresent());
		assertEquals(true, x.get());
	}

	@Test
	public void getInteger_nonExistent() throws ArgsException {
		Args args = new ArgsImmutable("x#", new String[] { "-x",
				EXAMPLE_VALID_INT_ARG });

		final Optional<Integer> x = args.getInteger("y");
		assertFalse(x.isPresent());
	}

	@Test
	public void getInteger_trivial() throws ArgsException {
		Args args = new ArgsImmutable("x#", new String[] { "-x",
				EXAMPLE_VALID_INT_ARG });

		final Optional<String> x = args.getString("x");
		assertTrue(x.isPresent());
		assertEquals(7, x.get());
	}

	@Test
	public void getString_nonExistent() throws ArgsException {
		Args args = new ArgsImmutable("x*", new String[] { "-x",
				EXAMPLE_STRING_ARG });

		final Optional<String> x = args.getString("y");
		assertFalse(x.isPresent());
	}

	@Test
	public void getString_trivial() throws ArgsException {
		Args args = new ArgsImmutable("x*", new String[] { "-x",
				EXAMPLE_STRING_ARG });

		final Optional<String> x = args.getString("x");
		assertTrue(x.isPresent());
		assertEquals(EXAMPLE_STRING_ARG, x.get());
	}

	@Test
	public void getBoolean_multiple() throws ArgsException {
		Args args = new ArgsImmutable("x,y", new String[] { "-x", "-y" });

		assertEquals(true, args.getBoolean("x").get());
		assertEquals(true, args.getBoolean("y").get());
	}

	@Test
	public void getBoolean_multipleInOneArg() throws ArgsException {
		Args args = new ArgsImmutable("x,y", new String[] { "-xy" });

		assertEquals(true, args.getBoolean("x").get());
		assertEquals(true, args.getBoolean("y").get());
	}

	@Test
	public void validSchema_noSpaces() throws ArgsException {
		Args args = new ArgsImmutable("x,y", new String[] { "-xy" });

		assertEquals(true, args.getBoolean("x").get());
		assertEquals(true, args.getBoolean("y").get());
	}

	@Test
	public void validSchema_withSpaces() throws ArgsException {
		Args args = new ArgsImmutable(" x , y ", new String[] { "-xy" });

		assertEquals(true, args.getBoolean("x").get());
		assertEquals(true, args.getBoolean("y").get());
	}

	@Test
	public void cardinality_multipleInOneArg() throws ArgsException {
		Args args = new ArgsImmutable("x,y", new String[] { "-xy" });

		assertEquals(2, args.cardinality());
	}

	@Test
	public void getBoolean_true() throws ArgsException {
		Args args = new ArgsImmutable("x", new String[] { "-x",
				EXAMPLE_VALID_TRUE_ARG });

		final Optional<Boolean> x = args.getBoolean("x");
		assertTrue(x.isPresent());
		assertEquals(true, x.get());
	}

	@Test
	public void getBoolean_false() throws ArgsException {
		Args args = new ArgsImmutable("x", new String[] { "-x",
				EXAMPLE_VALID_FALSE_ARG });

		final Optional<Boolean> x = args.getBoolean("x");
		assertTrue(x.isPresent());
		assertEquals(false, x.get());
	}

	@Test
	public void getBoolean_nonExistent() throws ArgsException {
		Args args = new ArgsImmutable("x", new String[] { "-x" });

		assertFalse(args.getBoolean("y").isPresent());
	}

	@Test
	public void empty() throws ArgsException {
		Args args = new ArgsImmutable("", new String[0]);
		assertEquals(0, args.cardinality());
	}

	@Test
	public void someSchema_emptyArgs() throws ArgsException {
		Args args = new ArgsImmutable("x,d#", new String[0]);
		assertEquals(0, args.cardinality());
	}

	@Test(expected = MalformedSchemaException.class)
	public void emptySchema_someArgs() throws ArgsException {
		new ArgsImmutable("", new String[] { "-x" });
	}

	@Test(expected = MalformedArgsException.class)
	public void malformedStringArgs_forValidSchema() throws ArgsException {
		new ArgsImmutable("x#", new String[] { "-x" });
	}

	@Test(expected = MalformedArgsException.class)
	public void malformedIntArgs_notANumber_forValidSchema()
			throws ArgsException {
		new ArgsImmutable("x*", new String[] { "-x", "Some string" });
	}

	@Test(expected = MalformedArgsException.class)
	public void malformedIntArgs_missing_forValidSchema() throws ArgsException {
		new ArgsImmutable("x*", new String[] { "-x" });
	}

	@Test
	public void multipleArgs_ofDifferentTypes() throws ArgsException {
		Args args = new ArgsImmutable("x,d#", new String[] { "-x", "-d",
				EXAMPLE_STRING_ARG });

		assertEquals(true, args.getBoolean("x").get());
		assertEquals(EXAMPLE_STRING_ARG, args.getString("d").get());
	}

	@Test(expected = MalformedSchemaException.class)
	public void malformedSchema_noLetters() throws ArgsException {
		new ArgsImmutable("*", new String[] {});
	}

	@Test(expected = MalformedSchemaException.class)
	public void malformedSchema_oddCharacter() throws ArgsException {
		new ArgsImmutable("f~", new String[] {});
	}

	@Test
	public void multipleArgs_ofDifferentTypes_inWrongOrder()
			throws ArgsException {
		Args args = new ArgsImmutable("d#,x", new String[] { "-x", "-d",
				EXAMPLE_STRING_ARG });

		assertEquals(true, args.getBoolean("x").get());
		assertEquals(EXAMPLE_STRING_ARG, args.getString("d").get());
	}
}
