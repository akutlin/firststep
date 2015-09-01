package firststep;

import firststep.internal.NVG;

public class Paint {
	NVG.Paint paint;
	private Image image;	// Saved just to keep the image from GC
	
	Paint(NVG.Paint paint) {
		this.paint = paint;
		this.image = null;
	}

	Paint(NVG.Paint paint, Image image) {
		this.paint = paint;
		this.image = image;
	}
	
	/**
	 * If the brush is made of an image, returns this image
	 */
	public Image getImage() {
		return image;
	}
}
