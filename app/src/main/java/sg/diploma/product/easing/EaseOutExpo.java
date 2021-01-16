package sg.diploma.product.easing;

public final class EaseOutExpo extends Easing{
	@Override
	public float Ease(final float x){
		return (int)x == 1 ? 1.0f : 1.0f - (float)Math.pow(2.0, -10.0 * (double)x);
	}

	public static EaseOutExpo globalObj;

	static{
		globalObj = new EaseOutExpo();
	}
}