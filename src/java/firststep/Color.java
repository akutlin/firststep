package firststep;

import firststep.internal.NVG;

public class Color {
	private NVG.Color nvgColor;
	
	Color(NVG.Color c) {
		this.nvgColor = c;
	}
	
	public Color(float red, float green, float blue, float alpha) {
		nvgColor = new NVG.Color(red, green, blue, alpha);
	}
	
	public Color(int red, int green, int blue, int alpha) {
		nvgColor = new NVG.Color(red, green, blue, alpha);
	}

	public Color(int red, int green, int blue) {
		nvgColor = new NVG.Color(red, green, blue);
	}

	NVG.Color getNVGColor() {
		return nvgColor;
	}

	public float getRed() {
		return nvgColor.getRed();
	}

	public void setRed(float red) {
		nvgColor.setRed(red);
	}

	public float getGreen() {
		return nvgColor.getGreen();
	}

	public void setGreen(float green) {
		nvgColor.setGreen(green);
	}

	public float getBlue() {
		return nvgColor.getBlue();
	}

	public void setBlue(float blue) {
		nvgColor.setBlue(blue);
	}
	
	public float getAlpha() {
		return nvgColor.getAlpha();
	}

	public void setAlpha(float alpha) {
		nvgColor.setAlpha(alpha);
	}
}
