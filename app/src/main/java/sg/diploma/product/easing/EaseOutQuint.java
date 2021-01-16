package sg.diploma.product.easing;

public final class EaseOutQuint extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - (float)Math.pow(1.0 - (double)x, 5.0);
	}

	public static EaseOutQuint globalObj;

	static{
		globalObj = new EaseOutQuint();
	}
}