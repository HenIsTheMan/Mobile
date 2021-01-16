package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseInOutSine extends Easing{
	@Override
	public float Ease(final float x){
		return -((float)Math.cos(Constants.pi * x) - 1.0f) * 0.5f;
	}

	public static EaseInOutSine globalObj;

	static{
		globalObj = new EaseInOutSine();
	}
}