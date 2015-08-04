package firststep;

import firststep.internal.NVG;

public class Color {
	NVG.Color nvgColor;
	
	Color(NVG.Color c) {
		this.nvgColor = c;
	}
	
	private float red, green, blue, alpha;
	
	public Color(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		
		nvgColor = NVG.Color.fromRGBA(red, green, blue, alpha);
	}
	
	public float getRed() {
		return red;
	}
	
	public float getGreen() {
		return green;
	}
	
	public float getBlue() {
		return blue;
	}
	
	public float getAlpha() {
		return alpha;
	}
}
