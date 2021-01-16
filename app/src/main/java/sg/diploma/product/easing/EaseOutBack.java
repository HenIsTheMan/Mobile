package sg.diploma.product.easing;

public final class EaseOutBack extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f + c3 * (float)Math.pow((double)x - 1.0, 3.0) + c1 * (float)Math.pow((double)x - 1.0, 2.0);
	}

	private static final float c1 = 1.70158f;
	private static final float c3 = c1 + 1.0f;

	public static EaseOutBack globalObj;

	static{
		globalObj = new EaseOutBack();
	}
}