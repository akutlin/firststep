package coollife;

public class Cells implements Cloneable {
	private int width, height;
	private boolean[] cells;
	
	protected Cells(int width, int height, boolean[] cells) {
		super();
		this.width = width;
		this.height = height;
		this.cells = cells;
	}

	public Cells(int width, int height) {
		this.width = width;
		this.height = height;
		
		cells = new boolean[width * height];
	}
	
	public void setCell(int i, int j, boolean value) {
		cells[j * width + i] = value;
	}
	
	public boolean getCell(int i, int j) {
	    while (i < 0) i += width;
	    i = i % width;

	    while (j < 0) j += height;
	    j = j % height;

		return cells[j * width + i];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	protected Cells clone() {
		return new Cells(width, height, cells.clone());
	}
}
