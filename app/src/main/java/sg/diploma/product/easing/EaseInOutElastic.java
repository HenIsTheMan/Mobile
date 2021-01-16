package sg.diploma.product.easing;

import sg.diploma.product.math.Constants;

public final class EaseInOutElastic extends Easing{
	@Override
	public float Ease(final float x){
		return x <= Constants.epsilon && -x <= Constants.epsilon
			? 0.0f
			: (x - 1.0f <= Constants.epsilon && 1.0f - x <= Constants.epsilon
			? 1.0f
			: (x < 0.5f
			? -((float)Math.pow(2.0, 20.0 * (double)x - 10.0) * (float)Math.sin((20.0 * (double)x - 11.125) * c5)) * 0.5f
			: ((float)Math.pow(2.0, -20.0 * (double)x + 10.0) * (float)Math.sin((20.0 * (double)x - 11.125) * c5)) * 0.5f + 1));
	}

	private static final double c5 = 2.0 * (double)Constants.pi / 4.5;

	public static EaseInOutElastic globalObj;

	static{
		globalObj = new EaseInOutElastic();
	}
}