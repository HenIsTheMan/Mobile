package sg.diploma.product.easing;

public final class EaseInOutQuint extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f ? 16.0f * x * x * x * x * x : 1.0f - (float)Math.pow(-2.0 * (double)x + 2.0, 5.0) * 0.5f;
	}

	public static EaseInOutQuint globalObj;

	static{
		globalObj = new EaseInOutQuint();
	}
}