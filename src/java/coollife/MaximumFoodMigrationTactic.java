package coollife;

public class MaximumFoodMigrationTactic implements MigrationTactic {

	@Override
	public Direction decideDirection(Cell[] cells) {
		int indexMax = 0;
		double plantsMax = 0;
		for (int i = 0; i < 8; i++) {
			if (cells[i].getPlants() > plantsMax) {
				plantsMax = cells[i].getPlants();
				indexMax = i;
			}
		}
		return Direction.fromIndex(indexMax);
	}

}
