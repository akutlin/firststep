package firststep.internal;

public class OS {

	public enum Platform {
		WINDOWS, OSX, OTHER;
	}

	public static native Platform getPlatform();
}