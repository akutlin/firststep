package coollife;

import firststep.Window;
import firststep.internal.GL3W;
import firststep.internal.GLFW;
import firststep.internal.NVG;
import firststep.internal.OS;
import firststep.internal.GLFW.Callback;
import firststep.internal.GLFW.CallbackAdapter;

public class LifeMainWindow extends Window {
	
	private static final String APPNAME = "Cool Life";
	
	private static float fps = 25.0f;
	private int mouseI, mouseJ;
	private boolean isPaused;
	private boolean isDown;
	private boolean clickedColor;

	private Cells field;
	
	@Override
	public void key(int key, int scancode, int action, int mods) {
		if (key == GLFW.KEY_ESCAPE && action == GLFW.PRESS) {
			close();
		} else if (key == GLFW.KEY_SPACE && action == GLFW.PRESS) {
			isPaused = !isPaused;
	        updateTitle();
	    }
	}
	
	public void mouseButton(int button, int action, int mods) {
	    if (button == GLFW.MOUSE_BUTTON_LEFT && action == GLFW.PRESS)
	    {
	    	isDown = true;
	        if (!field.getCell(mouseI, mouseJ))
	        {
	        	field.setCell(mouseI, mouseJ, true);
	        	clickedColor = true;
	        }
	        else
	        {
	        	field.setCell(mouseI, mouseJ, false);
	        	clickedColor = false;
	        }
	    }
	    
	    if (button == GLFW.MOUSE_BUTTON_LEFT && action == GLFW.RELEASE) {
	    	isDown = false;
	    }
	}
	
	public void cursorPos(double x, double y) {
		if (isDown) {
        	field.setCell(mouseI, mouseJ, clickedColor);
		}
	}	
	private float calcScale(int winWidth, int winHeight)
	{
	    float wpw = (float)field.getWidth() / winWidth;
	    float hph = (float)field.getHeight() / winHeight;
	    float k = 1;
	    if (wpw > hph)
	    {
	        k = 1.0f / wpw;
	    }
	    else
	    {
	        k = 1.0f / hph;
	    }
	    return k;
	}

	private void cellUnderMouse(int winWidth, int winHeight)
	{
	    Position mPos = getCursorPos();

	    float k = calcScale(winWidth, winHeight);
	    float x0 = winWidth / 2 - (field.getWidth() * k / 2);
	    float y0 = winHeight / 2 - (field.getHeight() * k / 2);
	    
	    mouseI = (int) ((mPos.getX() - x0) / k);
	    mouseJ = (int) ((mPos.getY() - y0) / k);
	}
	
	private void updateTitle()
	{
	    if (isPaused)
	    {
	    	setTitle(APPNAME + " [ Pause ]");
	    }
	    else
	    {
	    	setTitle(APPNAME);
	    }
	}
	
	private long frameIndex = 0;
	
	@Override
	protected void frame() {
		float k = calcScale(getWidth(), getHeight());
	    
	    float x0 = getWidth() / 2 - (field.getWidth() * k / 2);
	    float y0 = getHeight() / 2 - (field.getHeight() * k / 2);
	    
	    long vg = nanoVGContext;
	    // Painting dark cells
        NVG.beginPath(vg);
        NVG.fillColor(vg, new NVG.Color(0, 0, 0));
	    for (int i = 0; i < field.getWidth(); i++)
	    for (int j = 0; j < field.getHeight(); j++)
	    {
	        if (!field.getCell(i, j))
	        {
		        NVG.rect(vg, x0 + i * k, y0 + j * k, k, k);
	        }
	    }
        NVG.fill(vg);

	    // Painting light cells
        NVG.beginPath(vg);
        NVG.fillColor(vg, new NVG.Color(192, 192, 192));
	    for (int i = 0; i < field.getWidth(); i++)
	    for (int j = 0; j < field.getHeight(); j++)
	    {
	        if (field.getCell(i, j))
	        {
		        NVG.rect(vg, x0 + i * k, y0 + j * k, k, k);
	        }
	    }
        NVG.fill(vg);

	    
	    // Painting lines between cells
        NVG.beginPath(vg);
        NVG.strokeColor(vg, new NVG.Color(64, 64, 64, 255));
        if (k > 8)
        {
		    for (int i = 0; i < field.getWidth(); i++)
		    for (int j = 0; j < field.getHeight(); j++)
		    {
		        NVG.rect(vg, x0 + i * k, y0 + j * k, k, k);
		    }
        }
        NVG.stroke(vg);
	    
	    if (mouseI >= 0 && mouseI < field.getWidth() && mouseJ >= 0 && mouseJ < field.getHeight())
	    {
	        NVG.fillColor(vg, new NVG.Color(255, 255, 255, 64));
	        NVG.beginPath(vg);
	        NVG.rect(vg, x0 + mouseI * k, y0 + mouseJ * k, k, k);
	        NVG.fill(vg);
	    }

		if (!isPaused && frameIndex % 2 == 0) {
			field = LifeEngine.turn(field);
		}
		frameIndex ++;
		cellUnderMouse(getWidth(), getHeight());
	}

	
	public LifeMainWindow() {
		super (APPNAME, 600, 400);
		
		field = new Cells(100, 70);
	}
	
	public static void main(String... args) {
		LifeMainWindow mainWindow = new LifeMainWindow();
		
		Window.loop(fps);
	}
}