package firststep;

import firststep.internal.NVG;

public class Color {
	NVG.Color nvgColor;
	
	Color(NVG.Color c) {
		this.nvgColor = c;
	}
	
	public static Color fromRGBA(float red, float green, float blue, float alpha) {
		NVG.Color nvgColor = NVG.Color.fromRGBA(red, green, blue, alpha);
		return new Color(nvgColor);
	}
	
	public static Color fromHSLA(float hue, float saturation, float luminosity, float alpha) {
		NVG.Color nvgColor = NVG.Color.fromHSLA(hue, saturation, luminosity, alpha);
		return new Color(nvgColor);
	}
	
	public float getRed() {
		return nvgColor.getRed();
	}
	
	public float getGreen() {
		return nvgColor.getGreen();
	}
	
	public float getBlue() {
		return nvgColor.getBlue();
	}
	
	public float getAlpha() {
		return nvgColor.getAlpha();
	}
}
