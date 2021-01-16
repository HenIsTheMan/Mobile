package sg.diploma.product.easing;

public final class EaseInBounce extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - EaseOutBounce.globalObj.Ease(1.0f - x);
	}

	public static EaseInBounce globalObj;

	static{
		globalObj = new EaseInBounce();
	}
}