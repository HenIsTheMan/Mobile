package sg.diploma.product.easing;

public final class EaseInCirc extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - (float)Math.sqrt(1.0 - (double)x * (double)x);
	}

	public static EaseInCirc globalObj;

	static{
		globalObj = new EaseInCirc();
	}
}