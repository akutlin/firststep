package firststep.glfw;

public class Exception extends RuntimeException {

	public Exception() {
		super();
	}

	public Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public Exception(String message) {
		super(message);
	}

	public Exception(Throwable cause) {
		super(cause);
	}
	
}
