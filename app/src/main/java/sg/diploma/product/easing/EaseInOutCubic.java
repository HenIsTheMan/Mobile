package sg.diploma.product.easing;

public final class EaseInOutCubic extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f ? 4.0f * x * x * x : 1.0f - (float)Math.pow(-2.0 * (double)x + 2.0, 3.0) * 0.5f;
	}

	public static EaseInOutCubic globalObj;

	static{
		globalObj = new EaseInOutCubic();
	}
}