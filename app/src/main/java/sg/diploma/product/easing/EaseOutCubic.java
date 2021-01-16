package sg.diploma.product.easing;

public final class EaseOutCubic extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - (float)Math.pow(1.0 - (double)x, 3.0);
	}

	public static EaseOutCubic globalObj;

	static{
		globalObj = new EaseOutCubic();
	}
}