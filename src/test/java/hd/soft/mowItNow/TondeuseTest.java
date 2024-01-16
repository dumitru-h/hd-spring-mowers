package hd.soft.mowItNow;

import org.junit.jupiter.api.Test;

import static hd.soft.mowItNow.Position.pos;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TondeuseTest {

	final int MAX_X = 5, MAX_Y = 5;

	@Test
	void testProgramOne() {
		final Position startPos = pos(1, 2, 'N');
		final String program = "GAGAGAGAA";
		final Position actualEndPosition = new Tondeuse(startPos, MAX_X, MAX_Y, program).execute();
		final Position expectedPosition = pos(1, 3, 'N');

		assertEquals(expectedPosition, actualEndPosition);

	}

	@Test
	void testProgramTwo() {
		final Position startPos = pos(3, 3, 'E');
		final String program = "AADAADADDA";
		final Position actualEndPosition = new Tondeuse(startPos, MAX_X, MAX_Y, program).execute();
		final Position expectedPosition = pos(5, 1, 'E');

		assertEquals(expectedPosition, actualEndPosition);

	}

	@Test
	void testProgramTwoWithIvalidDirections() {
		final Position startPos = pos(3, 3, 'E');
		final String program = "AADAADADDA";
		final String BAD_PROGRAM = "WXYZTUVW";

		String programWithBadDir = new StringBuffer(program).insert(program.length() / 2, BAD_PROGRAM).toString();

		final Position actualEndPosition = new Tondeuse(startPos, MAX_X, MAX_Y, programWithBadDir).execute();
		final Position expectedPosition = pos(5, 1, 'E');

		assertEquals(expectedPosition, actualEndPosition);

	}

	@Test
	void testInvalidProgram() {
		final Position startPos = pos(3, 3, 'E');
		final String BAD_PROGRAM = "WXYZTUVW";

		final Position actualEndPosition = new Tondeuse(startPos, MAX_X, MAX_Y, BAD_PROGRAM).execute();
		final Position expectedPosition = startPos;

		assertEquals(expectedPosition, actualEndPosition);
	}
}
