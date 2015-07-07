package firststep;

import java.util.HashMap;

import firststep.internal.NVG;

public class Font {
	
	@SuppressWarnings("serial")
	class FontExistsException extends Exception {
		public final int fontId;
		public FontExistsException(int fontId) {
			this.fontId = fontId;
		}
	}
	
	private static HashMap<Integer, Font> allFonts = new HashMap<>();
	
	int id;

	private Canvas canvas;

	Font(Canvas cnv, String name, String path) throws FontExistsException {
		canvas = cnv;
		id = NVG.createFont(canvas.nanoVGContext, name, path);
		if (allFonts.containsKey(id)) throw new FontExistsException(id);
		allFonts.put(id, this);
			
	}
	
	static Font find(Canvas cnv, String name) {
		return allFonts.get(NVG.findFont(cnv.nanoVGContext, name));
	}
	
	static Font find(int id) {
		return allFonts.get(id);
	}
}
