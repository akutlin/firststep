package coollife;

public interface MigrationTactic {
	public enum Direction {
		N(0), NE(1), E(2), SE(3), S(4), SW(5), W(6), NW(7);
		
		private int index;
		Direction(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
		
		public static Direction fromIndex(int index) {
			return Direction.values()[index];
		}
	}
	
	/**
	 * 
	 * @param cells Indexes of these cells correspond to the indexes
	 * of {@link Direction} enum items.
	 * @return the migration target direction 
	 */
	public Direction decideDirection(Cell[] cells);
}
