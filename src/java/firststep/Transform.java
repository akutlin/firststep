package firststep;

import firststep.internal.NVG;

public class Transform {
	NVG.Transform transform;
	
	Transform(NVG.Transform transform) {
		this.transform = transform;
	}
	
	public static Transform identity() {
		NVG.Transform res = new NVG.Transform();
		res.identity();
		return new Transform(res);
	}
	
	public static Transform translation(float x, float y) {
		NVG.Transform res = new NVG.Transform();
		res.translate(x, y);
		return new Transform(res);
	}
	
	public static Transform scaling(float x, float y) {
		NVG.Transform res = new NVG.Transform();
		res.scale(x, y);
		return new Transform(res);
	}

	public static Transform rotating(float a) {
		NVG.Transform res = new NVG.Transform();
		res.rotate(a);
		return new Transform(res);
	}

	public static Transform skewingX(float a) {
		NVG.Transform res = new NVG.Transform();
		res.skewX(a);
		return new Transform(res);
	}

	public static Transform skewingY(float a) {
		NVG.Transform res = new NVG.Transform();
		res.skewY(a);
		return new Transform(res);
	}

	/**
	 * Multiplies the {@link Transform} by another one.
	 * Supports chain calling.
	 * @param other
	 * @return self after modification
	 */
	public Transform multiply(Transform other) {
		transform.multiply(other.transform);
		return this;
	}
	
	/**
	 * Multiplies the {@link Transform} by translation.
	 * Supports chain calling.
	 * @param other
	 * @return self after modification
	 */
	public Transform translate(float x,float y) {
		NVG.Transform t = new NVG.Transform();
		t.translate(x, y);
		transform.multiply(t);
		return this;
	}
	
	/**
	 * Multiplies the {@link Transform} by scaling.
	 * Supports chain calling.
	 * @param other
	 * @return self after modification
	 */
	public Transform scale(float x,float y) {
		NVG.Transform t = new NVG.Transform();
		t.scale(x, y);
		transform.multiply(t);
		return this;
	}

	/**
	 * Multiplies the {@link Transform} by rotation.
	 * Supports chain calling.
	 * @param other
	 * @return self after modification
	 */
	public Transform rotate(float a) {
		NVG.Transform t = new NVG.Transform();
		t.rotate(a);
		transform.multiply(t);
		return this;
	}

	/**
	 * Multiplies the {@link Transform} by x-skewing.
	 * Supports chain calling.
	 * @param other
	 * @return self after modification
	 */
	public Transform skewX(float a) {
		NVG.Transform t = new NVG.Transform();
		t.skewX(a);
		transform.multiply(t);
		return this;
	}

	/**
	 * Multiplies the {@link Transform} by y-skewing.
	 * Supports chain calling.
	 * @param other
	 * @return self after modification
	 */
	public Transform skewY(float a) {
		NVG.Transform t = new NVG.Transform();
		t.skewY(a);
		transform.multiply(t);
		return this;
	}

}
