package firststep.os;

public class GlobalConsts {

	public enum Platform {
		WINDOWS, OSX, OTHER;
	}

	public static native Platform getPlatform();
}