package coollife;

public class Field {
	private int width, height;
	private Cell[] cells;
	
	protected Field(int width, int height, Cell[] cells) {
		this.width = width;
		this.height = height;
		this.cells = cells;
	}

	public Field(int width, int height) {
		this.width = width;
		this.height = height;
		
		cells = new Cell[width * height];
		for (int i = 0; i < width * height; i++) {
			cells[i] = new Cell();
		}
	}
	
	public void setCell(int i, int j, Cell value) {
		cells[j * width + i] = value;
	}
	
	public Cell getCell(int i, int j) {
	    i += width;
	    i = i % width;

	    j += height;
	    j = j % height;

		return cells[j * width + i];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}	
}
