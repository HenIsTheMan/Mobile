package sg.diploma.product.easing;

public final class EaseInOutQuart extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f ? 8.0f * x * x * x * x : 1.0f - (float)Math.pow(-2.0 * (double)x + 2.0, 4.0) * 0.5f;
	}

	public static EaseInOutQuart globalObj;

	static{
		globalObj = new EaseInOutQuart();
	}
}