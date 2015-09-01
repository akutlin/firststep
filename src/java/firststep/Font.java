package firststep;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import firststep.internal.NVG;

public class Font {

	private static HashMap<Integer, WeakReference<Font>> allFonts = new HashMap<>();
	
	@SuppressWarnings("serial")
	class FontExistsException extends Exception {
		public final int fontId;
		public FontExistsException(int fontId) {
			this.fontId = fontId;
		}
	}
	
	int id;

	private Canvas canvas;

	Font(Canvas cnv, String name, String path) throws FontExistsException {
		canvas = cnv;
		id = NVG.createFont(canvas.nanoVGContext, name, path);
		if (allFonts.containsKey(id)) throw new FontExistsException(id);
		allFonts.put(id, new WeakReference<>(this));
	}
	
	static Font find(Canvas cnv, String name) {
		return find(NVG.findFont(cnv.nanoVGContext, name));
	}
	
	static Font find(int id) {
		WeakReference<Font> f = allFonts.get(id);
		if (f != null) {
			return f.get();
		} else {
			return null;
		}
	}
}
