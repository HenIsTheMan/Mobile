package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseInSine extends Easing{
	@Override
	public float Ease(final float x){
		return 1.0f - (float)Math.cos(x * Constants.pi * 0.5f);
	}

	public static EaseInSine globalObj;

	static{
		globalObj = new EaseInSine();
	}
}