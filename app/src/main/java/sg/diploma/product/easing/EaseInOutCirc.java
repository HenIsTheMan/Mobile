package sg.diploma.product.easing;

public final class EaseInOutCirc extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f ? (1.0f - (float)Math.sqrt(1.0 - 2.0 * (double)x * 2.0 * (double)x)) * 0.5f : ((float)Math.sqrt(1.0 - (float)Math.pow(-2.0 * (double)x + 2.0, 2.0)) + 1.0f) * 0.5f;
	}

	public static EaseInOutCirc globalObj;

	static{
		globalObj = new EaseInOutCirc();
	}
}