package sg.diploma.product.math;

public final class Constants{
	public static final float epsilon;
	public static final float pi;

	static{
		epsilon = Math.ulp(1.0f);
		pi = (float)Math.PI;
	}
}
