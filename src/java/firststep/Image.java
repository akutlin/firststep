package firststep;

import firststep.internal.NVG;

public class Image {
	
	/**
	 * Image construction flags
	 */
	public enum Flags {
	    GENERATE_MIPMAPS(1<<0),	// Generate mipmaps during creation of the image.
	    REPEATX(1<<1),			// Repeat image in X direction.
	    REPEATY(1<<2),			// Repeat image in Y direction.
	    FLIPY(1<<3),				// Flips (inverses) image in Y direction when rendered.
	    PREMULTIPLIED(1<<4);		// Image data has premultiplied alpha.
		
		final int id;
		Flags(int id) {
			this.id = id;
		}
		static Flags fromId(int id) {
			for (Flags v : Flags.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	int id;

	private Canvas canvas;
	private boolean isDeleted;
	
	Image(Canvas cnv, String filename, Flags imageFlags) {
		canvas = cnv;
		id = NVG.createImage(canvas.nanoVGContext, filename, imageFlags.id);
	}
	
	public void delete() {
		NVG.deleteImage(canvas.nanoVGContext, id);
		isDeleted = true;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
}
