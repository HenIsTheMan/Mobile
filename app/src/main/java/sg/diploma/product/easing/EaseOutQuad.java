package sg.diploma.product.easing;

public final class EaseOutQuad extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - (float)Math.pow(1.0 - (double)x, 2.0);
	}

	public static EaseOutQuad globalObj;

	static{
		globalObj = new EaseOutQuad();
	}
}