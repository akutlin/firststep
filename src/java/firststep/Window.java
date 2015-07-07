package firststep;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import firststep.internal.GL3W;
import firststep.internal.GLFW;
import firststep.internal.NVG;
import firststep.internal.OS;
import firststep.internal.GLFW.Callback;

public class Window {

	public class Position {
		private double x, y;

		public double getX() {
			return x;
		}
		public double getY() {
			return y;
		}
		
		public Position(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}
		
	}
	
	private static Callback globalCallback = new Callback() {
		
		@Override
		public void windowSize(long window, int width, int height) {
			openedWindows.get(window).internalWindowSize(width, height);
		}
		
		@Override
		public void windowRefresh(long window) {
			openedWindows.get(window).internalDraw();
		}
		
		@Override
		public void windowPos(long window, int x, int y) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowIconify(long window, boolean iconified) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowFocus(long window, boolean focused) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowClose(long window) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void scroll(long window, double scrollX, double scrollY) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseButton(long window, int button, int action, int mods) {
			openedWindows.get(window).mouseButton(button, action, mods);
		}
		
		@Override
		public void monitor(long monitor, boolean connected) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void key(long window, int key, int scancode, int action, int mods) {
			openedWindows.get(window).key(key, scancode, action, mods);
		}
		
		@Override
		public void error(int error, String description) {
			getLogger().log(Level.SEVERE, "GLFW error #" + error + ": " + description);
		}
		
		@Override
		public void cursorPos(long window, double x, double y) {
			openedWindows.get(window).cursorPos(x, y);
		}
		
		@Override
		public void cursorEnter(long window, boolean entered) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void character(long window, char character) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private static boolean gl3wInitialized = false;
	private static HashMap<Long, Window> openedWindows = new HashMap<Long, Window>();
	
	static {
		boolean initSuccess = GLFW.init();
		if (!initSuccess) {
			throw new RuntimeException("GLFW initialization failed");
		}
		getLogger().log(Level.INFO, "GLFW initialized");
		
		GLFW.setCallback(globalCallback);
	}
	
	private static Logger getLogger() {
		return Logger.getLogger(Window.class.getName(), null);
	}
	
	/**
	 * 
	 * @return <code>false</code> if the window is closed
	 */
	public static void loop(float fpsMax) {
		while (!openedWindows.isEmpty()) {
			double t1 = GLFW.getTime();
			
			// Removing closed windows from the list
			HashSet<Long> toErase = new HashSet<>();
			for (Long glfwWindow : openedWindows.keySet()) {
				if (GLFW.windowShouldClose(glfwWindow)) {
					toErase.add(glfwWindow);
				}
			}
			openedWindows.keySet().removeAll(toErase);
		
			for (Window window : openedWindows.values()) {
				window.internalDraw();
			}			
			
			GLFW.pollEvents();
			
			double t2 = GLFW.getTime();

			try {
				Thread.sleep((long) Math.max((1.0 / fpsMax - (t2 - t1)) * 1000, 0));
			} catch (InterruptedException e) {
				getLogger().log(Level.WARNING, "Thread sleep interrupted");
			}

		}
	}

	private long glfwWindow; 
	protected long nanoVGContext;
	private NVG.Color background = new NVG.Color(0f, 0f, 0f, 1f);
	private int width, height;
	
	public Window(String title, int width, int height) {
		if (OS.getPlatform() == OS.Platform.OSX) {
			// We initialize OpenGL 3.2 Core profile on OSX cause
			// it is the only GL3 that Apple knows.
			
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MAJOR, 3);
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MINOR, 2);
			
			GLFW.windowHint(GLFW.OPENGL_PROFILE, GLFW.OPENGL_CORE_PROFILE);
			GLFW.windowHint(GLFW.OPENGL_FORWARD_COMPAT, 1);
		} else {
			// On other platforms OpenGL 3.0 is sufficient for us.
			
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MAJOR, 3);
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MINOR, 0);			
		}

		glfwWindow = GLFW.createWindow(width, height, title, 0, 0);
		if (glfwWindow == 0) {
			GLFW.terminate();
			throw new RuntimeException("GLFW can't create a window");
		}
		getLogger().log(Level.INFO, "GLFW window \"" + title + "\" [" + width + "x" + height + "] is created");

		if (!gl3wInitialized) {
			GLFW.makeContextCurrent(glfwWindow);
			if (!GL3W.init()) {
				GLFW.terminate();
				throw new RuntimeException("GL3W initialization failed");
			}
			gl3wInitialized = true;
			getLogger().log(Level.INFO, "GL3W context is initialized");
		}
		
		getLogger().log(Level.INFO, "GL version: " + GL3W.getGLVersionMajor() + "." + GL3W.getGLVersionMinor());
		getLogger().log(Level.INFO, "GLSL version: " + GL3W.getGLSLVersionMajor() + "." + GL3W.getGLSLVersionMinor());

		nanoVGContext = NVG.create(firststep.internal.NVG.NVG_ANTIALIAS | firststep.internal.NVG.NVG_STENCIL_STROKES | firststep.internal.NVG.NVG_DEBUG);
		if (nanoVGContext == 0) {
			GLFW.terminate();
			throw new RuntimeException("NanoVG can't create a context for the window");
		}
		getLogger().log(Level.INFO, "NanoVG context is created");
		
		this.width = width;
		this.height = height;
		
		synchronized (openedWindows) {
			openedWindows.put(glfwWindow, this);
		}
	}
	
	private void internalDraw() {
		int fbWidth = width;	// TODO FramebufferSize
		int fbHeight = height;	// TODO FramebufferSize
		
		// Calculate pixel ration for hi-dpi devices.
		float pxRatio = (float)fbWidth / (float)width;

		GLFW.makeContextCurrent(glfwWindow);
		GL3W.glViewport(0, 0, fbWidth, fbHeight);
		GL3W.glClearColor(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
		GL3W.glClear(GL3W.GL_COLOR_BUFFER_BIT | GL3W.GL_STENCIL_BUFFER_BIT | GL3W.GL_DEPTH_BUFFER_BIT);
		
		NVG.beginFrame(nanoVGContext, width, height, pxRatio);

		frame();
		
        NVG.endFrame(nanoVGContext);

        GLFW.swapBuffers(glfwWindow);
	}

	public void internalWindowSize(int width, int height) {
		windowSize(width, height);
		this.width = width;
		this.height = height;
		internalDraw();
	}

	public void close() {
		GLFW.setWindowShouldClose(glfwWindow, 1);
	}
	
	public Position getCursorPos() {
		return new Position(GLFW.getCursorPosX(glfwWindow), GLFW.getCursorPosY(glfwWindow));
	}
	
	public void setTitle(String title) {
		GLFW.setWindowTitle(glfwWindow, title);
	}
	
	public NVG.Color getBackground() {
		return background;
	}
	
	public void setBackground(NVG.Color background) {
		this.background = background;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}

	// User events
	
	protected void frame() {
		
	}
	
	protected void windowSize(int width, int height) {
		
	}

	protected void mouseButton(int button, int action, int mods) {
		
	}
	
	protected void key(int key, int scancode, int action, int mods) {
		
	}

	protected void cursorPos(double x, double y) {

	}

}
