package sg.diploma.product.easing;

public final class EaseOutCirc extends Easing{
	@Override
	public float Ease(final float x){
		return (float)Math.sqrt(1.0 - (double)(x - 1.0f) * (double)(x - 1.0f));
	}

	public static EaseOutCirc globalObj;

	static{
		globalObj = new EaseOutCirc();
	}
}