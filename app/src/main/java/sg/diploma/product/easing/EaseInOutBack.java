package sg.diploma.product.easing;

public final class EaseInOutBack extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f
			? ((float)Math.pow(2.0 * (double)x, 2.0) * ((c2 + 1.0f) * 2.0f * x - c2)) * 0.5f
			: ((float)Math.pow(2.0 * (double)x - 2.0, 2.0) * ((c2 + 1.0f) * (x * 2.0f - 2.0f) + c2) + 2.0f) * 0.5f;
	}

	private static final float c1 = 1.70158f;
	private static final float c2 = c1 * 1.525f;

	public static EaseInOutBack globalObj;

	static{
		globalObj = new EaseInOutBack();
	}
}