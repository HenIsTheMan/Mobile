package sg.diploma.product.easing;

public final class EaseInExpo extends Easing{
	@Override
	public float Ease(final float x){
		return (int)x == 0 ? 0 : (float)Math.pow(2.0, 10.0 * (double)x - 10.0);
	}

	public static EaseInExpo globalObj;

	static{
		globalObj = new EaseInExpo();
	}
}