package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseInExpo extends Easing{
	@Override
	public float Ease(final float x){
		return x <= Constants.epsilon && -x <= Constants.epsilon ? 0 : (float)Math.pow(2.0, 10.0 * (double)x - 10.0);
	}

	public static EaseInExpo globalObj;

	static{
		globalObj = new EaseInExpo();
	}
}