package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseOutSine extends Easing{
	@Override
	public float Ease(final float x){
		return (float)Math.sin(x * Constants.pi * 0.5f);
	}

	public static EaseOutSine globalObj;

	static{
		globalObj = new EaseOutSine();
	}
}