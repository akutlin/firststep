package coollife;

public class OnlyNorthMigrationTactic implements MigrationTactic {

	@Override
	public Direction decideDirection(Cell[] cells) {
		return Direction.N;
	}

}
