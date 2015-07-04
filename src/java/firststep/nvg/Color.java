package firststep.nvg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Color {
	
	FloatBuffer buff;  // must be called buff see void* getBuffPtr(JNIEnv *e, jobject jo)
	
	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, int a) {
		this((float)r / 255, (float)g / 255, (float)b / 255, (float)a / 255);
	}
	
	public Color(float r, float g, float b, float a) {
		buff = ByteBuffer.allocateDirect(16)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
		buff.put(0, r);
		buff.put(1, g);
		buff.put(2, b);
		buff.put(3, a);
	}
	
	public Color() {
		this(0f,0f,0f,1f);
	}
	
	public void setRed(float r) { buff.put(0,r); }
	public void setGreen(float g) { buff.put(1,g); }
	public void setBlue(float b) { buff.put(2,b); }
	public void setAlpha(float a) { buff.put(3,a); }
	
	public void set(float r, float g, float b, float a) { 
		buff.put(0, r);
		buff.put(1, g);
		buff.put(2, b);
		buff.put(3, a);
	}
}
