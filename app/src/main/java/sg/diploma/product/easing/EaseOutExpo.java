package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseOutExpo extends Easing{
	@Override
	public float Ease(final float x){
		return x - 1.0f <= Constants.epsilon && 1.0f - x <= Constants.epsilon ? 1.0f : 1.0f - (float)Math.pow(2.0, -10.0 * (double)x);
	}

	public static EaseOutExpo globalObj;

	static{
		globalObj = new EaseOutExpo();
	}
}