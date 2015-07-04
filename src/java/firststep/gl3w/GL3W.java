package firststep.gl3w;

public class GL3W {
	private GL3W() { }
	
	public static long GL_DEPTH_BUFFER_BIT = 0x00000100L;
	public static long GL_STENCIL_BUFFER_BIT = 0x00000400L;
	public static long GL_COLOR_BUFFER_BIT = 0x00004000L;

	public static native boolean init();

	public static native int getGLVersionMajor();
	public static native int getGLVersionMinor();
	public static native int getGLSLVersionMajor();
	public static native int getGLSLVersionMinor();
	
	public static native void glViewport(int x, int y, int width, int height);
	public static native void glClearColor(float red, float green, float blue, float alpha);
	public static native void glClear(long mask);
}
