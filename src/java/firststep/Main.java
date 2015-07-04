package firststep;

import firststep.gl3w.GL3W;
import firststep.glfw.GLFW;
import firststep.glfw.GLFWCallback;
import firststep.glfw.GLFWCallbackAdapter;
import firststep.nvg.Color;
import firststep.nvg.NVG;

public class Main {
	static {
		System.loadLibrary("firststep");
	}
	
	private long window; 
	private long vg;
	private float pxRatio;
	private double fps = 25.0;
	
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
		
	};
	
	private void drawContents(long vg) {
		int winWidth = GLFW.glfwGetWindowWidth(window);
		int winHeight = GLFW.glfwGetWindowHeight(window);
		
		int fbWidth = winWidth;	// TODO FramebufferSize
		int fbHeight = winHeight;	// TODO FramebufferSize
		
		// Calculate pixel ration for hi-dpi devices.
		pxRatio = (float)fbWidth / (float)winWidth;

		GL3W.glViewport(0, 0, fbWidth, fbHeight);
		GL3W.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GL3W.glClear(GL3W.GL_COLOR_BUFFER_BIT | GL3W.GL_STENCIL_BUFFER_BIT | GL3W.GL_DEPTH_BUFFER_BIT);
		
		NVG.beginFrame(vg, winWidth, winHeight, pxRatio);

		NVG.beginPath(vg);
        NVG.fillColor(vg, new Color(0.5f, 0.5f, 0.5f, 1f));
        NVG.rect(vg, 10, 20, 30, 40);
        NVG.fill(vg);

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
	
	public Main() {
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

		vg = firststep.nvg.NVG.create(firststep.nvg.NVG.NVG_ANTIALIAS | firststep.nvg.NVG.NVG_STENCIL_STROKES | firststep.nvg.NVG.NVG_DEBUG);
	}
	
	public void loop() {
		while (!GLFW.glfwWindowShouldClose(window))
		{
			frame();
			GLFW.glfwPollEvents();
		}
	}
	
	public static void main(String... args) {
		Main main = new Main();
		main.loop();
	}
}