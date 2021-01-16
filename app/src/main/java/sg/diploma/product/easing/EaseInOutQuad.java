package sg.diploma.product.easing;

public final class EaseInOutQuad extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f ? 2.0f * x * x : 1.0f - (float)Math.pow(-2.0 * (double)x + 2.0, 2.0) * 0.5f;
	}

	public static EaseInOutQuad globalObj;

	static{
		globalObj = new EaseInOutQuad();
	}
}