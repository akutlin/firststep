package coollife;

import coollife.MigrationTactic.Direction;

public class AnimalGrowingEngine {
	
	private MigrationTactic migrationTactic;
	
	private class FieldTurnRunnable implements Runnable {

		private int j1, j2;
		private Field cells;
		
		public FieldTurnRunnable(Field cells, int j1, int j2) {
			this.j1 = j1;
			this.j2 = j2;
			this.cells = cells;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < cells.getWidth(); i++)
			for (int j = j1; j < j2; j++) {
				Cell cell = cells.getCell(i, j);
				
				Cell[] sideCells = new Cell[8];
				sideCells[Direction.N.getIndex()] = cells.getCell(i, j - 1);
				sideCells[Direction.NE.getIndex()] = cells.getCell(i + 1, j - 1);
				sideCells[Direction.E.getIndex()] = cells.getCell(i + 1, j);
				sideCells[Direction.SE.getIndex()] = cells.getCell(i + 1, j + 1);
				sideCells[Direction.S.getIndex()] = cells.getCell(i, j + 1);
				sideCells[Direction.SW.getIndex()] = cells.getCell(i - 1, j + 1);
				sideCells[Direction.W.getIndex()] = cells.getCell(i - 1, j);
				sideCells[Direction.NW.getIndex()] = cells.getCell(i - 1, j - 1);

				double plantsCount = cell.getPlants();
				int animalsCount = cell.getAnimals();
				
				int animalsCountOld = animalsCount;
				for (int k = 0; k < animalsCountOld; k++) {
					
					// Eats plant for life
					if (Math.random() < plantsCount * cell.getAnimalSize()) {
						plantsCount -= 0.75;
		
						// Search for someone to breed a child
						for (int si = 0; si < sideCells.length; si++) {
							if (Math.random() < sideCells[si].getAnimalSize() * animalsCountOld) {
								animalsCount++;
								break;
							}
						}
						
						if (Math.random() < cell.getAnimalSize() * animalsCountOld) {
							animalsCount++;
						}
						
					} else {
						// Migrate or starve
						double migrationFactor = cell.getAnimalSize() * 4;
						boolean migrate = Math.random() < migrationFactor;
						
						if (migrate) {
							
							// Decide direction
							Cell decidedDir = sideCells[migrationTactic.decideDirection(sideCells).getIndex()];
							decidedDir.setAnimals(decidedDir.getAnimals() + 1);
							
							animalsCount--;
						} else {
							// Starvation death
							animalsCount--;
						}
					}
				}
				
				// Natural death
				animalsCount *= 0.9;
				
				cell.setAnimals(animalsCount);
				cell.setPlants(plantsCount);
			}
		}
		
	}
	
	public AnimalGrowingEngine(MigrationTactic migrationTactic) {
		this.migrationTactic = migrationTactic;
	}
	
	public void turnField(Field cells) {
		int threads = 4;
		Thread[] ths = new Thread[threads];
		for (int t = 0; t < threads; t++) {
			int j1 = cells.getHeight() / threads * t;
			int j2 = cells.getHeight() / threads * (t + 1);
			if (j2 > cells.getHeight()) {
				j2 = cells.getHeight();   
			}
			ths[t] = new Thread(new FieldTurnRunnable(cells, j1, j2));
			
			ths[t].start();
		}
		
		for (int t = 0; t < threads; t++) {
			try {
				ths[t].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
