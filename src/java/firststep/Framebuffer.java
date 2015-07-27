package firststep;

import firststep.internal.GL3W;
import firststep.internal.NVG;

public class Framebuffer {
	
	long id;
	int width, height;

	private Canvas canvas;
	private boolean isDeleted;
	
	Framebuffer(Canvas cnv, int width, int height, Image.Flags imageFlags) {
		canvas = cnv;
		id = NVG.createFramebuffer(cnv.nanoVGContext, width, height, imageFlags.toFlags());
		this.width = width;
		this.height = height;
	}
	
	Framebuffer(Canvas cnv, int width, int height, long id) {
		canvas = cnv;
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public void delete() {
		NVG.deleteFramebuffer(id);
		isDeleted = true;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public Image getImage() {
		if (id != 0) {
			int imgId = NVG.getImageFromFramebuffer(id);
			Image img = Image.forId(canvas, imgId);
			return img != null ? img : new Image(canvas, imgId);
		} else {
			return null;
		}
	}
	
	public void beginDrawing(float pxRatio) {
		IntXY fboSize;
		if (id == 0) {
			// TODO Implement getFramebufferSize
			fboSize = new IntXY(width, height);
		} else {
			fboSize = getImage().getSize();
		}

		int winWidth = (int)(fboSize.getX() / pxRatio);
		int winHeight = (int)(fboSize.getY() / pxRatio);

		NVG.bindFramebuffer(id);
		GL3W.glViewport(0, 0, fboSize.getX(), fboSize.getY());
		if (id != 0) {
			GL3W.glClearColor(0, 0, 0, 0);
		} else {
			GL3W.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		}
		
		GL3W.glClear(GL3W.GL_COLOR_BUFFER_BIT | GL3W.GL_STENCIL_BUFFER_BIT | GL3W.GL_DEPTH_BUFFER_BIT);
		NVG.beginFrame(canvas.nanoVGContext, winWidth, winHeight, pxRatio);
	}
	
	public void endDrawing() {
		NVG.endFrame(canvas.nanoVGContext);
		NVG.bindFramebuffer(0);
	}
}
