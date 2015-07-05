package coollife;

public class PlantGrowingEngine {
	private static final double DISPERSING_PERCENT = 0.01;
	
	public static void turnField(Field cells) {
		for (int i = 0; i < cells.getWidth(); i++)
		for (int j = 0; j < cells.getHeight(); j++) {
			turnCell(cells.getCell(i, j));
		}
		
		int[] dispersingCounts = new int[cells.getWidth() * cells.getHeight()];
		for (int i = 0; i < cells.getWidth(); i++)
		for (int j = 0; j < cells.getHeight(); j++) {
			int count = 0;
			count += cells.getCell(i - 1, j - 1).getPlants() * 0.7;
			count += cells.getCell(i + 1, j - 1).getPlants() * 0.7;
			count += cells.getCell(i - 1, j + 1).getPlants() * 0.7;
			count += cells.getCell(i + 1, j + 1).getPlants() * 0.7;
					
			count += cells.getCell(i - 1, j).getPlants();
			count += cells.getCell(i + 1, j).getPlants();
			count += cells.getCell(i, j - 1).getPlants();
			count += cells.getCell(i, j + 1).getPlants();

			dispersingCounts[j * cells.getWidth() + i] = (int) (count * DISPERSING_PERCENT);
			dispersingCounts[j * cells.getWidth() + i] -= cells.getCell(i, j).getPlants() * (DISPERSING_PERCENT * (0.7 * 4 + 4));
		}
		
		for (int i = 0; i < cells.getWidth(); i++)
		for (int j = 0; j < cells.getHeight(); j++) {
			cells.getCell(i, j).setPlants(cells.getCell(i, j).getPlants() + dispersingCounts[j * cells.getWidth() + i]);
		}
	}
	
	public static void turnCell(Cell cell) {
		double plantsCount = cell.getPlants();
		int plantFoodMax = cell.getPlantFoodMax();
		int plantFood = cell.getPlantFood();
		
		// Plants can't die completely 
		//if (plantsCount < 1) plantsCount = 1;

		double plantsCountOld = plantsCount;
		for (int i = 0; i < plantsCountOld; i++) {
			int foodTaken = 0;
			// Takes food for life
			if ((Math.random() * plantFoodMax) < plantFood) foodTaken++;
			// Takes food for seeds
			if ((Math.random() * plantFoodMax) < plantFood) foodTaken++;

			plantFood -= foodTaken;
			if (foodTaken == 0) plantsCount--;
			else if (foodTaken == 2) plantsCount++;
		}
		
		plantFood += 70;
		if (plantFood > plantFoodMax) plantFood = plantFoodMax;
		
		cell.setPlantFood(plantFood);
		cell.setPlants(plantsCount);
	}
}
