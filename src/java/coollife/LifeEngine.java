package coollife;

public class LifeEngine {
	public static Cells turn(Cells field) {
		Cells newField = field.clone();
		
		int width = field.getWidth(), height = field.getHeight();
		
	    for (int i = 0; i < width; i++)
        for (int j = 0; j < height; j++)
        {
            int alive = 0;
            if (field.getCell(i - 1, j - 1)) alive ++;
            if (field.getCell(i - 1, j)) alive ++;
            if (field.getCell(i - 1, j + 1)) alive ++;
            if (field.getCell(i + 1, j - 1)) alive ++;
            if (field.getCell(i + 1, j)) alive ++;
            if (field.getCell(i + 1, j + 1)) alive ++;
            if (field.getCell(i, j - 1)) alive ++;
            if (field.getCell(i, j + 1)) alive ++;
            
            if (field.getCell(i, j) && (alive == 2 || alive == 3)) {
            	newField.setCell(i, j, true);
            } else if (!field.getCell(i, j) && (alive == 3)) {
            	newField.setCell(i, j, true);
            } else {
            	newField.setCell(i, j, false);
            }
        }
	    
	    return newField;
	}
}
