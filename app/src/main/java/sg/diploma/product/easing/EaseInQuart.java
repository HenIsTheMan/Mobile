package sg.diploma.product.easing;

public final class EaseInQuart extends Easing{
	@Override
	public float Ease(final float x){
		return x * x * x * x;
	}

	public static EaseInQuart globalObj;

	static{
		globalObj = new EaseInQuart();
	}
}