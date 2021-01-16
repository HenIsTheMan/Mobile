package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseOutElastic extends Easing{
	@Override
	public float Ease(final float x){
		return (int)x == 0
			? 0.0f
			: ((int)x == 1
			? 1.0f
			: (float)Math.pow(2.0, -10.0 * (double)x) * (float)Math.sin(((double)x * 10.0 - 0.75) * c4)) + 1.0f;
	}

	private static final double c4 = 2.0 * (double)Constants.pi / 3.0;

	public static EaseOutElastic globalObj;

	static{
		globalObj = new EaseOutElastic();
	}
}