package coollife;

import firststep.gl3w.GL3W;
import firststep.glfw.GLFW;
import firststep.glfw.GLFWCallback;
import firststep.glfw.GLFWCallbackAdapter;
import firststep.nvg.NVGColor;
import firststep.nvg.NVG;

public class LifeMain {
	
	private static final String APPNAME = "CoolLife";
	
	private long window; 
	private long vg;
	private float pxRatio;
	private double fps = 25.0;
	private int mouseI, mouseJ;
	private boolean isPaused;

	private Cells field;
	
	private GLFWCallback cb = new GLFWCallbackAdapter() {
		
		@Override
		public void windowSize(long window, int width, int height) {
			drawContents(vg);
		}
		
		@Override
		public void windowRefresh(long window) {
			drawContents(vg);
		}
		
		@Override
		public void error(int error, String description) {
			System.out.println("GLFW error " + error + ": " + description);
		}
		
		@Override
		public void key(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
				GLFW.glfwSetWindowShouldClose(window, 1);
			} else if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
				isPaused = !isPaused;
		        updateTitle();
		    }
		}
		
		public void mouseButton(long window, int button, int action, int mods) {
		    if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS)
		    {
		        if (!field.getCell(mouseI, mouseJ))
		        {
		        	field.setCell(mouseI, mouseJ, true);
		        }
		        else
		        {
		        	field.setCell(mouseI, mouseJ, false);
		        }
		    }
		}
	};
	
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
	    double mx, my;
	    mx = GLFW.glfwGetCursorPosX(window);
	    my = GLFW.glfwGetCursorPosY(window);

	    float k = calcScale(winWidth, winHeight);
	    float x0 = winWidth / 2 - (field.getWidth() * k / 2);
	    float y0 = winHeight / 2 - (field.getHeight() * k / 2);
	    
	    mouseI = (int) ((mx - x0) / k);
	    mouseJ = (int) ((my - y0) / k);
	}
	
	private void updateTitle()
	{
	    if (isPaused)
	    {
	        GLFW.glfwSetWindowTitle(window, APPNAME + " [ Pause ]");
	    }
	    else
	    {
	    	GLFW.glfwSetWindowTitle(window, APPNAME);
	    }
	}
	
	private void drawLife(int winWidth, int winHeight) {
	    float k = calcScale(winWidth, winHeight);
	    
	    float x0 = winWidth / 2 - (field.getWidth() * k / 2);
	    float y0 = winHeight / 2 - (field.getHeight() * k / 2);
	    
	    // Painting dark cells
        NVG.beginPath(vg);
        NVG.fillColor(vg, new NVGColor(0, 0, 0));
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
        NVG.fillColor(vg, new NVGColor(192, 192, 192));
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
        NVG.strokeColor(vg, new NVGColor(64, 64, 64, 255));
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
	        NVG.fillColor(vg, new NVGColor(255, 255, 255, 64));
	        NVG.beginPath(vg);
	        NVG.rect(vg, x0 + mouseI * k, y0 + mouseJ * k, k, k);
	        NVG.fill(vg);
	    }

	}

	private void drawContents(long vg) {
		int winWidth = GLFW.glfwGetWindowWidth(window);
		int winHeight = GLFW.glfwGetWindowHeight(window);
		cellUnderMouse(winWidth, winHeight);

		int fbWidth = winWidth;	// TODO FramebufferSize
		int fbHeight = winHeight;	// TODO FramebufferSize
		
		// Calculate pixel ration for hi-dpi devices.
		pxRatio = (float)fbWidth / (float)winWidth;

		GL3W.glViewport(0, 0, fbWidth, fbHeight);
		GL3W.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GL3W.glClear(GL3W.GL_COLOR_BUFFER_BIT | GL3W.GL_STENCIL_BUFFER_BIT | GL3W.GL_DEPTH_BUFFER_BIT);
		
		NVG.beginFrame(vg, winWidth, winHeight, pxRatio);

		drawLife(winWidth, winHeight);
		
        NVG.endFrame(vg);

        GLFW.glfwSwapBuffers(window);
	}
	
	private void frame() {
		double t1 = GLFW.glfwGetTime();
		drawContents(vg);
		double t2 = GLFW.glfwGetTime();

		try {
			Thread.sleep((long) Math.max((1.0 / fps - (t2 - t1)) * 1000, 0));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LifeMain() {
		boolean initSuccess = GLFW.glfwInit();
		if (!initSuccess) {
			throw new RuntimeException("GLFW initialization failed");
		}
		System.out.println("GLFW initialized");
		
		GLFW.glfwSetCallback(cb);
		
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, 1);

		window = GLFW.glfwCreateWindow(600, 400, "Cool life", 0, 0);
		if (window == 0) {
			GLFW.glfwTerminate();
			throw new RuntimeException("GLFW can't create a window");
		}
		
		GLFW.glfwMakeContextCurrent(window);
		if (!GL3W.init()) {
			GLFW.glfwTerminate();
			throw new RuntimeException("GL3W initialization failed");
		}
		System.out.println("GL version: " + GL3W.getGLVersionMajor() + "." + GL3W.getGLVersionMinor());
		System.out.println("GLSL version: " + GL3W.getGLSLVersionMajor() + "." + GL3W.getGLSLVersionMinor());

		vg = NVG.create(firststep.nvg.NVG.NVG_ANTIALIAS | firststep.nvg.NVG.NVG_STENCIL_STROKES | firststep.nvg.NVG.NVG_DEBUG);
		
		field = new Cells(100, 70);
	}
	
	public void loop() {
		int frameIndex = 0;
		while (!GLFW.glfwWindowShouldClose(window))
		{
			frame();
			if (!isPaused && frameIndex % 2 == 0) {
				field = LifeEngine.turn(field);
			}
			GLFW.glfwPollEvents();
			frameIndex ++;
		}
	}
	
	public static void main(String... args) {
		LifeMain main = new LifeMain();
		main.loop();
	}
}