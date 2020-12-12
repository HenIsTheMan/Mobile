package sg.diploma.product.math;

import static java.lang.Math.sqrt;
import static sg.diploma.product.math.Constants.epsilon;

public final class Vector2{
	public Vector2(){
		x = 0.0f;
		y = 0.0f;
	}

	public Vector2(final float val){
		x = y = val;
	}

	public Vector2(final float x, final float y){
		this.x = x;
		this.y = y;
	}

	public float Len(){
		return (float)sqrt(x * x + y * y);
	}

	public float LenSquared(){
		return x * x + y * y;
	}

	public Vector2 Normalize(){
		final float mag = Len();
		try{
			if(mag <= epsilon){
				throw new DivisorIs0("");
			}
		} catch(DivisorIs0 divisorIs0){
			return this;
		}
		this.x = x / mag;
		this.y = y / mag;
		return this;
	}

	public Vector2 Normalized(){
		final float mag = Len();
		try{
			if(mag <= epsilon){
				throw new DivisorIs0("");
			}
		} catch(DivisorIs0 divisorIs0){
			return this;
		}
		return new Vector2(x / mag, y / mag);
	}

	public float x;
	public float y;

	public static final Vector2 zero;
	public static final Vector2 up;
	public static final Vector2 down;
	public static final Vector2 left;
	public static final Vector2 right;

	static{
		zero = new Vector2();
		up = new Vector2(0.0f, 1.0f);
		down = new Vector2(0.0f, -1.0f);
		left = new Vector2(-1.0f, 0.0f);
		right = new Vector2(1.0f, 0.0f);
	}
}