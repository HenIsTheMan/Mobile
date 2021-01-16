package sg.diploma.product.easing;

public final class EaseInQuad extends Easing{
	@Override
	public float Ease(final float x){
		return x * x;
	}

	public static EaseInQuad globalObj;

	static{
		globalObj = new EaseInQuad();
	}
}