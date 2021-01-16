package sg.diploma.product.easing;

public final class EaseInQuint extends Easing{
	@Override
	public float Ease(final float x){
		return x * x * x * x * x;
	}

	public static EaseInQuint globalObj;

	static{
		globalObj = new EaseInQuint();
	}
}