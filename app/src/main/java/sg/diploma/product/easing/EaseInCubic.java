package sg.diploma.product.easing;

public final class EaseInCubic extends Easing{
	@Override
	public float Ease(final float x){
		return x * x * x;
	}

	public static EaseInCubic globalObj;

	static{
		globalObj = new EaseInCubic();
	}
}