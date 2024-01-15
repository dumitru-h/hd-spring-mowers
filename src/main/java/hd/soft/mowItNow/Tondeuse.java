package hd.soft.mowItNow;

import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static hd.soft.mowItNow.Position.isValid;
import static java.lang.String.format;

@EqualsAndHashCode
public class Tondeuse {
	private static Logger log = LoggerFactory.getLogger(Tondeuse.class);

	public final char
			CMD_AVANCE = 'A',
			CMD_GAUCHE = 'G',
			CMD_DROITE = 'D';

	public final Position startPosition;
	public final int maxX;
	public final int maxY;
	public final String program;

	public Tondeuse(Position pos, int maxX, int maxY, String program) {

		if (pos == null)
			throw new IllegalArgumentException("la position initiale de la tondeuse ne peut pas être null");

		if (!(maxX > 0 && maxY > 0 && pos.x <= maxX && pos.y <= maxY)) {
			throw new IllegalArgumentException("les dimmensions de la pelouse doivent être strictement positives et les coordonées initiales à l'interieur");
		}

		this.startPosition = pos;
		this.maxX = maxX;
		this.maxY = maxY;
		this.program = program;

	}

	class InvalidPosition extends Exception {
		private final Position pos;

		InvalidPosition(Position pos) {
			this.pos = pos;
		}

		@Override
		public String getMessage() {
			return format("%d, %d", pos.x, pos.y);
		}
	}

	private Position computeNext(Position pos, char command) {
		if (isValid(pos, maxX, maxY)) {
			switch (command) {
				case CMD_AVANCE: {
					Position next = pos.avance();
					if (isValid(next, maxX, maxY)) {
						return next;
					}
				}
				break;
				case CMD_GAUCHE: {
					Position next = pos.goLeft();
					if (isValid(next, maxX, maxY)) {
						return next;
					}
				}
				break;
				case CMD_DROITE: {
					Position next = pos.goRight();
					if (isValid(next, maxX, maxY)) {
						return next;
					}
				}
				break;
			}
		}
		log.debug("{} -> ({}, {})", command, pos.x, pos.y);

		return pos;
	}

	/**
	 * @return final position
	 */
	public Position execute() {
		Position pos = startPosition;
		for (char c : program.toCharArray()) {
			pos = computeNext(pos, c);
		}

		return pos;
	}
}
