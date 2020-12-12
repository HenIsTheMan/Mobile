package sg.diploma.product.graphics;

public final class Color{
	public Color(){
		this(1.0f);
	}

	public Color(final float rgba){
		r = g = b = a = rgba;
	}

	public Color(final float rgb, final float a){
		r = g = b = rgb;
		this.a = a;
	}

	public Color(final float r, final float g, final float b){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f;
	}

	public Color(final float r, final float g, final float b, final float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public float r;
	public float g;
	public float b;
	public float a;

	public static final Color red;
	public static final Color green;
	public static final Color blue;
	public static final Color black;
	public static final Color white;

	static{
		red = new Color(1.0f, 0.0f, 0.0f, 1.0f);
		green = new Color(0.0f, 1.0f, 0.0f, 1.0f);
		blue = new Color(0.0f, 0.0f, 1.0f, 1.0f);
		black = new Color(0.0f, 1.0f);
		white = new Color();
	}
}
