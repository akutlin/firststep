package coollife;

public class AnimalGrowingEngine {
	public static void turnField(Field cells) {
		
		for (int i = 0; i < cells.getWidth(); i++)
		for (int j = 0; j < cells.getHeight(); j++) {
			Cell cell = cells.getCell(i, j);
			
			double plantsCount = cell.getPlants();
			int animalsCount = cell.getAnimals();
			
			int animalsCountOld = animalsCount;
			for (int k = 0; k < animalsCountOld; k++) {
				
				// Eats plant for life
				if (plantsCount > 0) {
					plantsCount -= 0.2;
	
					// Searches for someone to breed child
					if (Math.random() < cell.getAnimalSize() * animalsCountOld) animalsCount++;
				} else {
					// Migrate or starve
					double migrationFactor = 0.3;
					boolean migrate = Math.random() < migrationFactor;
					
					if (migrate) {
						// Decide direction
						int dir = (int)(Math.random() * 8);
						if (dir == 7) {
							cells.getCell(i + 1, j).setAnimals(cells.getCell(i + 1, j).getAnimals() + 1);
						} else if (dir == 6) {
							cells.getCell(i - 1, j).setAnimals(cells.getCell(i - 1, j).getAnimals() + 1);
						} else if (dir == 5) {
							cells.getCell(i, j + 1).setAnimals(cells.getCell(i, j + 1).getAnimals() + 1);
						} else if (dir == 4) {
							cells.getCell(i, j - 1).setAnimals(cells.getCell(i, j - 1).getAnimals() + 1);
						} else if (dir == 3) {
							cells.getCell(i + 1, j + 1).setAnimals(cells.getCell(i + 1, j + 1).getAnimals() + 1);
						} else if (dir == 2) {
							cells.getCell(i + 1, j - 1).setAnimals(cells.getCell(i + 1, j - 1).getAnimals() + 1);
						} else if (dir == 1) {
							cells.getCell(i - 1, j + 1).setAnimals(cells.getCell(i - 1, j + 1).getAnimals() + 1);
						} else {
							cells.getCell(i - 1, j - 1).setAnimals(cells.getCell(i - 1, j - 1).getAnimals() + 1);
						}
						
						animalsCount--;
					} else {
						// Starvation death
						animalsCount--;
					}
				}
			}
			
			// Natural death
			animalsCount *= 0.85;
			
			cell.setAnimals(animalsCount);
			cell.setPlants(plantsCount);
		}
	}
}
