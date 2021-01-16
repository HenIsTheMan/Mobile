package sg.diploma.product.easing;

public final class EaseOutQuart extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - (float)Math.pow(1.0 - (double)x, 4.0);
	}

	public static EaseOutQuart globalObj;

	static{
		globalObj = new EaseOutQuart();
	}
}