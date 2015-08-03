package firststep;

import java.util.ArrayList;
import java.util.List;

import firststep.internal.GL3W;
import firststep.internal.NVG;

public class Framebuffer {
	
	public interface DrawListener {
		void draw(Canvas cnv);
	}
	
	private long id;
	private int width, height;
	private Image.Flags imageFlags;
	private Color background = new Color(0f, 0f, 0f, 0f);

	private Canvas canvas;
	private boolean isDeleted;
	
	private DrawListener drawListener;
	private List<Framebuffer> dependencies = new ArrayList<>();
	
	Framebuffer(Canvas cnv, int width, int height, Image.Flags imageFlags) {
		canvas = cnv;
		id = NVG.createFramebuffer(cnv.nanoVGContext, width, height, imageFlags.toFlags());
		this.width = width;
		this.height = height;
		this.imageFlags = imageFlags;
	}
	
	Framebuffer(Canvas cnv, int width, int height, long id) {
		canvas = cnv;
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public void resize(int newWidth, int newHeight) {
		if (id == 0) {
			this.width = newWidth;
			this.height = newHeight;
		} else {
			NVG.deleteFramebuffer(id);
			id = NVG.createFramebuffer(canvas.nanoVGContext, width, height, imageFlags.toFlags());
		}
	}
	
	public void delete() {
		if (!isDeleted) {
			NVG.deleteFramebuffer(id);
			isDeleted = true;
		}
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
	
	private void beginDrawing(float pxRatio) {
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
		GL3W.glClearColor(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
		
		GL3W.glClear(GL3W.GL_COLOR_BUFFER_BIT | GL3W.GL_STENCIL_BUFFER_BIT | GL3W.GL_DEPTH_BUFFER_BIT);
		NVG.beginFrame(canvas.nanoVGContext, winWidth, winHeight, pxRatio);
	}
	
	private void endDrawing() {
		NVG.endFrame(canvas.nanoVGContext);
		NVG.bindFramebuffer(0);
	}
	
	void draw(Canvas canvas) {
		if (drawListener != null) {
			for (Framebuffer fb : dependencies) {
				fb.draw(canvas);
			}
			beginDrawing(1.0f); // TODO: fix
			drawListener.draw(canvas);
			endDrawing();
		}
	}

	public void setDrawListener(DrawListener drawListener) {
		this.drawListener = drawListener;
	}
	
	public void addDependency(Framebuffer dep) {
		dependencies.add(dep);
	}
	
	public void removeDependency(Framebuffer dep) {
		dependencies.remove(dep);
	}
	
	public void clearDependencies() {
		dependencies.clear();
	}
	
	void setBackground(Color background) {
		this.background = background;
	}
	
	@Override
	protected void finalize() throws Throwable {
		delete();
		super.finalize();
	}
}
