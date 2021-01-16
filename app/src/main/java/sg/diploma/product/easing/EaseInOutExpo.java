package sg.diploma.product.easing;

public final class EaseInOutExpo extends Easing{
	@Override
	public float Ease(final float x){
		return (int)x == 0
			? 0.0f
			: ((int)x == 1
			? 1.0f
			: x < 0.5f ? (float)Math.pow(2.0, 20.0 * (double)x - 10.0) * 0.5f
			: (2.0f - (float)Math.pow(2.0, -20.0 * (double)x + 10.0)) * 0.5f);
	}

	public static EaseInOutExpo globalObj;

	static{
		globalObj = new EaseInOutExpo();
	}
}