package firststep;

import java.util.logging.Level;
import java.util.logging.Logger;

import firststep.internal.GLFW;
import firststep.internal.NVG;

public class Canvas {
	private static Logger getLogger() {
		return Logger.getLogger(Window.class.getName(), null);
	}

	private long nanoVGContext;
	
	Canvas(Window window) {
		window.makeContextCurrent();
		nanoVGContext = NVG.create(firststep.internal.NVG.NVG_ANTIALIAS | firststep.internal.NVG.NVG_STENCIL_STROKES | firststep.internal.NVG.NVG_DEBUG);
		if (nanoVGContext == 0) {
			GLFW.terminate();
			throw new RuntimeException("NanoVG can't create a context for the window");
		}
		getLogger().log(Level.INFO, "NanoVG context is created");

	}
	
	void beginFrame(int width, int height, float ratio) {
		NVG.beginFrame(nanoVGContext, width, height, ratio);
	}
	
	void endFrame() {
		NVG.endFrame(nanoVGContext);
	}
	
	public void beginPath() {
		NVG.beginPath(nanoVGContext);
	}
	
	public void stroke() {
		NVG.stroke(nanoVGContext);
	}
	
	public void fill() {
		NVG.fill(nanoVGContext);
	}
	
	public void strokeColor(Color color) {
		NVG.strokeColor(nanoVGContext, color.getNVGColor());
	}

	public void fillColor(Color color) {
		NVG.fillColor(nanoVGContext, color.getNVGColor());
	}
	
	public void rect(float x, float y, float width, float height) {
		NVG.rect(nanoVGContext, x, y, width, height);
	}
}
