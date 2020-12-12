package sg.diploma.product.math;

public final class Constants{
	public static final float epsilon;

	static{
		epsilon = Math.ulp(1.0f);
	}
}
