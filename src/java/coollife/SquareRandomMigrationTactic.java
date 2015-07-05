package coollife;

public class SquareRandomMigrationTactic implements MigrationTactic {

	@Override
	public Direction decideDirection(Cell[] cells) {
		return Direction.fromIndex((int)(Math.random() * 8));
	}

}
