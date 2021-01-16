package sg.diploma.product.easing;

public final class EaseOutBounce extends Easing{
	@Override
	public float Ease(float x){
		if(x < 1.0f / d1){
			return n1 * x * x;
		} else if(x < 2.0f / d1){
			return n1 * (x -= 1.5f / d1) * x + 0.75f;
		} else if(x < 2.5f / d1){
			return n1 * (x -= 2.25f / d1) * x + 0.9375f;
		} else{
			return n1 * (x -= 2.625f / d1) * x + 0.984375f;
		}
	}

	private static final float n1 = 7.5625f;
	private static final float d1 = 2.75f;

	public static EaseOutBounce globalObj;

	static{
		globalObj = new EaseOutBounce();
	}
}
