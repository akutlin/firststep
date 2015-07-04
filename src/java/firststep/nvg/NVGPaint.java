package firststep.nvg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class NVGPaint {
	
	FloatBuffer buff;  // must be called buff see void* getBuffPtr(JNIEnv *e, jobject jo)
	
	public NVGPaint() {
		buff = ByteBuffer.allocateDirect(76)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	public NVGPaint imagePattern(long ctx, float ox, float oy, float ex, float ey,
											float angle, int image, float alpha)
	{
		NVG.__imagePattern(ctx, this, ox, oy, ex, ey, angle, image, alpha);
		return this; // for chaining with constructor 
	}
	
	public NVGPaint linearGradient(long ctx, float sx, float sy, float ex, float ey, 
											NVGColor icol, NVGColor ocol)
	{
		NVG.__linearGradient(ctx, this, sx, sy, ex, ey, icol, ocol);
		return this;
	}
	
	public NVGPaint boxGradient(long ctx, float x, float y, float w, float h,
						float r, float f, NVGColor icol, NVGColor ocol)
	{
		NVG.__boxGradient(ctx, this, x, y, w, h, r, f, icol, ocol);
		return this;
	}
	
	public NVGPaint radialGradient(long ctx, float cx, float cy, float inr, float outr, NVGColor icol, NVGColor ocol)
	{
		NVG.__radialGradient(ctx, this, cx, cy, inr, outr, icol, ocol);
		return this;
	}
}