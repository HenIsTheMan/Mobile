package sg.diploma.product.math;

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