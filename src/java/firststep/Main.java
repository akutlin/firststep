package firststep;

import firststep.gl3w.GL3W;
import firststep.glfw.GLFW;
import firststep.glfw.GLFWCallback;

public class Main {
	static {
		System.loadLibrary("firststep");
	}
	
	private static native void start();
	
	private static GLFWCallback cb = new GLFWCallback() {
		
		@Override
		public void windowSize(long window, int width, int height) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowRefresh(long window) {
			// TODO Auto-generated method stub
			
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
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void monitor(long monitor, boolean connected) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void key(long window, int key, int scancode, int action, int mods) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void error(int error, String description) {
			System.out.println("GLFW error " + error + ": " + description);
		}
		
		@Override
		public void cursorPos(long window, double x, double y) {
			// TODO Auto-generated method stub
			
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
	
	
	public static void main(String... args) {
		boolean b = GLFW.glfwInit();
		if (!b) {
			System.out.println("GLFW initialization failed");
			Runtime.getRuntime().exit(-1);
		}
		System.out.println("GLFW initialized");
		
		GLFW.glfwSetCallback(cb);
		
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, 1);

		long window = GLFW.glfwCreateWindow(600, 400, "Cool life", 0, 0);
		if (window == 0) {
			System.out.println("Window is NULL. Cancelling");
			GLFW.glfwTerminate();
			Runtime.getRuntime().exit(-1);
		}
		
		GLFW.glfwMakeContextCurrent(window);
		if (!GL3W.init()) {
			System.out.println("GL3W initialization failed");
			Runtime.getRuntime().exit(-1);
		}
		System.out.println("GL version: " + GL3W.getGLVersionMajor() + "." + GL3W.getGLVersionMinor());
		System.out.println("GLSL version: " + GL3W.getGLSLVersionMajor() + "." + GL3W.getGLSLVersionMinor());

		while (!GLFW.glfwWindowShouldClose(window))
		{
			GLFW.glfwPollEvents();
		}

		
		//start();
	}
}