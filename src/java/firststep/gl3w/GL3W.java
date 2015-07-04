package firststep.gl3w;

public class GL3W {
	private GL3W() { }
	
	public static native boolean init();

	public static native int getGLVersionMajor();
	public static native int getGLVersionMinor();
	public static native int getGLSLVersionMajor();
	public static native int getGLSLVersionMinor();
}
