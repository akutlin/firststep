package firststep;

import java.util.logging.Level;
import java.util.logging.Logger;

import firststep.internal.GLFW;
import firststep.internal.NVG;

public class Canvas {
	
	/**
	 * Line caps
	 */
	public enum LineCap {
		NVG_BUTT(0),
		NVG_ROUND(1),
		NVG_SQUARE(2);
		
		final int id;
		LineCap(int id) {
			this.id = id;
		}
		static LineCap fromId(int id) {
			for (LineCap v : LineCap.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Line joins
	 */
	public enum LineJoin {
		NVG_ROUND(1),
		NVG_BEVEL(3),
		NVG_MITER(4);
		
		final int id;
		LineJoin(int id) {
			this.id = id;
		}
		static LineJoin fromId(int id) {
			for (LineJoin v : LineJoin.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Text horizontal alignment
	 */
	public enum HAlign {
		LEFT(1<<0),		// Default, align text horizontally to left.
		CENTER(1<<1),	// Align text horizontally to center.
		RIGHT(1<<2);	// Align text horizontally to right.
		
		final int id;
		HAlign(int id) {
			this.id = id;
		}
		static HAlign fromId(int id) {
			for (HAlign v : HAlign.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Text vertical alignment
	 */
	public enum VAlign {
		TOP(1<<3),	// Align text vertically to top.
		MIDDLE(1<<4),	// Align text vertically to middle.
		BOTTOM(1<<5),	// Align text vertically to bottom. 
		BASELINE(1<<6); // Default, align text vertically to baseline. 
		
		final int id;
		VAlign(int id) {
			this.id = id;
		}
		static VAlign fromId(int id) {
			for (VAlign v : VAlign.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	private static Logger getLogger() {
		return Logger.getLogger(Window.class.getName(), null);
	}

	long nanoVGContext;

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

	void cancelFrame() {
		NVG.cancelFrame(nanoVGContext);
	}

	public void beginPath() {
		NVG.beginPath(nanoVGContext);
	}
	
	public void stroke() {
		NVG.stroke(nanoVGContext);
	}
	
	public void moveTo(float x, float y) {
		NVG.moveTo(nanoVGContext, x, y);
	}

	public void lineTo(float x, float y) {
		NVG.lineTo(nanoVGContext, x, y);
	}

	public void strokeColor(Color color) {
		NVG.strokeColor(nanoVGContext, color.nvgColor);
	}
	
	public void strokeColor(float r, float g, float b, float a) {
		NVG.strokeColorf(nanoVGContext, r, g, b, a);
	}
	
	public void fillColor(Color color) {
		NVG.fillColor(nanoVGContext, color.nvgColor);
	}

	public void fillColor(float r, float g, float b, float a) {
		NVG.fillColorf(nanoVGContext, r, g, b, a);
	}

	public void strokeWidth(float size) {
		NVG.strokeWidth(nanoVGContext, size);
	}

	public void rect(float x, float y, float width, float height) {
		NVG.rect(nanoVGContext, x, y, width, height);
	}
	
	public void fill() {
		NVG.fill(nanoVGContext);
	}
	
	public void save() {
		NVG.save(nanoVGContext);
	}

	public void restore() {
		NVG.restore(nanoVGContext);
	}
	
	public void reset() {
		NVG.reset(nanoVGContext);
	}

	public void quadTo(float cx, float cy, float x, float y) {
		NVG.quadTo(nanoVGContext, cx, cy, x, y);
	}

	public void bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
		NVG.bezierTo(nanoVGContext, c1x, c1y, c2x, c2y, x, y);
	}

	public void lineCap(LineCap cap) {
		NVG.lineCap(nanoVGContext, cap.id);
	}

	public void lineJoin(LineJoin join) {
		NVG.lineJoin(nanoVGContext, join.id);
	}
	
	public void circle(float x, float y, float r) {
		NVG.circle(nanoVGContext, x, y, r);
	}
	
	public Image createImage(String filename, Image.Flags imageFlags) {
		return new Image(this, filename, imageFlags);
	}
	
	public void roundedRect(float x, float y, float width, float height, float r) {
		NVG.roundedRect(nanoVGContext, x, y, width, height, r);
	}
	
	public void fillPaint(Paint paint) {
		NVG.fillPaint(nanoVGContext, paint.paint);
	}
	
	public void scissor(float x, float y, float width, float height) {
		NVG.scissor(nanoVGContext, x, y, width, height);
	}

	public void intersectScissor(float x, float y, float width, float height) {
		NVG.intersectScissor(nanoVGContext, x, y, width, height);
	}
	
	public void resetScissor() {
		NVG.resetScissor(nanoVGContext);
	}
	
	public void fontSize(float size) {
		NVG.fontSize(nanoVGContext, size);
	}
	
	public void fontFace(String font) {
		NVG.fontFace(nanoVGContext, font);
	}
	
	public void textAlign(HAlign hAlign, VAlign vAlign) {
		NVG.textAlign(nanoVGContext, hAlign.id | vAlign.id);
	}
	
	public void fontBlur(float blur) {
		NVG.fontBlur(nanoVGContext, blur);
	}
	
	public void text(float x, float y, String message) {
		NVG.text(nanoVGContext, x, y, message);
	}
	
	public Font createFont(String name, String path) {
		try {
			return new Font(this, name, path);
		} catch (Font.FontExistsException e) {
			return Font.find(e.fontId);
		}
	}
	
	public void rotate(float angle) {
		NVG.rotate(nanoVGContext, angle);
	}
	
	public void resetTransform() {
		NVG.resetTransform(nanoVGContext);
	}
	
	public void translate(float x, float y) {
		NVG.translate(nanoVGContext, x, y);
	}
	
	public void scale(float x, float y) {
		NVG.scale(nanoVGContext, x, y);
	}
	
	public void strokePaint(Paint paint) {
		NVG.strokePaint(nanoVGContext, paint.paint);
	}
	
	public void miterLimit(float limit) {
		NVG.miterLimit(nanoVGContext, limit);
	}

	public void globalAlpha(float alpha) {
		NVG.globalAlpha(nanoVGContext, alpha);
	}
	
	public void setTransform(Transform t) {
		t.transform.setContextTransform(nanoVGContext);
	}
	
	public Transform getTransform() {
		NVG.Transform res = new NVG.Transform();
		res.getContextTransform(nanoVGContext);
		return new Transform(res);
	}
	
	public void skewX(float angle) {
		NVG.skewX(nanoVGContext, angle);
	}

	public void skewY(float angle) {
		NVG.skewY(nanoVGContext, angle);
	}

	public void arcTo(float x1, float y1, float x2, float y2, float radius) {
		NVG.arcTo(nanoVGContext, x1, y1, x2, y2, radius);
	}
	
	//...
	
	public Font findFont(String name) {
		return Font.find(this, name);
	}
	
	public void fontFace(Font font) {
		NVG.fontFaceId(nanoVGContext, font.id);
	}
	
	public Paint imagePattern(float ox, float oy, float ex, float ey, float angle, Image image, float alpha) {
		NVG.Paint res = new NVG.Paint();
		res.imagePattern(nanoVGContext, ox, oy, ex, ey, angle, image.id, alpha);
		return new Paint(res);
	}
	
	public Paint linearGradient(float sx, float sy, float ex, float ey, Color icol, Color ocol) {
		NVG.Paint res = new NVG.Paint();
		res.linearGradient(nanoVGContext, sx, sy, ex, ey, icol.nvgColor, ocol.nvgColor);
		return new Paint(res);
	}
	
	public Paint boxGradient(float x, float y, float w, float h, float r, float f, Color icol, Color ocol) {
		NVG.Paint res = new NVG.Paint();
		res.boxGradient(nanoVGContext, x, y, w, h, r, f, icol.nvgColor, ocol.nvgColor);
		return new Paint(res);
	}
	
	public Paint radialGradient(float cx, float cy, float inr, float outr, Color icol, Color ocol) {
		NVG.Paint res = new NVG.Paint();
		res.radialGradient(nanoVGContext, cx, cy, inr, outr, icol.nvgColor, ocol.nvgColor);
		return new Paint(res);
	}
}
