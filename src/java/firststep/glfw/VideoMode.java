package firststep.glfw;

public class VideoMode {
	public int width;
	public int height;
	public int redBits;
	public int greenBits;
	public int blueBits;
	@Override
	public String toString() {
		return "GlfwVideoMode [width=" + width + ", height=" + height
				+ ", redBits=" + redBits + ", greenBits=" + greenBits
				+ ", blueBits=" + blueBits + "]";
	}
}