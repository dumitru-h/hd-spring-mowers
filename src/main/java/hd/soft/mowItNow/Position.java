package hd.soft.mowItNow;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Data
@Slf4j
public class Position {
	public final int x, y;
	public final char direction;

	private Position(int x, int y, char direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public static Position pos(int x, int y, char direction) {
		return new Position(x, y, direction);
	}

	@Override
	public String toString() {
		return format("Position: (%d, %d) %s", x, y, direction);
	}

	public String outStr() {
		return format("%d %d %s", x, y, direction);
	}

	private static final String SPACE = String.valueOf(' ');

	public static String toOutputFormat(List<Position> list) {
		return list.stream()
				.map(Position::outStr)
				.collect(joining(SPACE));
	}

	public Position avance() {
		switch (direction) {
			case 'N':
				return pos(x, y + 1, direction);
			case 'S':
				return pos(x, y - 1, direction);
			case 'E':
				return pos(x + 1, y, direction);
			case 'W':
				return pos(x - 1, y, direction);
			default:
				log.warn("invalid direction: "+ direction);
		}

		return this;
	}

	static boolean validDirection(char dir) {
		return dir == 'N' || dir == 'S' || dir == 'E' || dir == 'W';
	}

	static boolean isValid(Position pos, int maxX, int maxY) {
		return pos != null && pos.x >= 0 && pos.y >= 0 && pos.x <= maxX && pos.y <= maxY
				&& validDirection(pos.direction);
	}

	public Position goLeft() {
		switch (direction) {
			case 'N':
				return pos(x, y, 'W');
			case 'S':
				return pos(x, y, 'E');
			case 'E':
				return pos(x, y, 'N');
			case 'W':
				return pos(x, y, 'S');
			default:
				log.warn("invalid direction: "+ direction);
		}

		return this;
	}

	public Position goRight() {
		switch (direction) {
			case 'N':
				return pos(x, y, 'E');
			case 'S':
				return pos(x, y, 'W');
			case 'E':
				return pos(x, y, 'S');
			case 'W':
				return pos(x, y, 'N');
			default:
				log.warn("invalid direction: "+ direction);
		}

		return this;
	}
}
