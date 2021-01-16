package sg.diploma.product.easing;

public final class EaseInBack extends Easing{
	@Override
	public float Ease(final float x){
		return c3 * x * x * x - c1 * x * x;
	}

	private static final float c1 = 1.70158f;
	private static final float c3 = c1 + 1.0f;

	public static EaseInBack globalObj;

	static{
		globalObj = new EaseInBack();
	}
}