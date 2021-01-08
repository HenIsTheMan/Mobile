package sg.diploma.product.easing;

public final class Easing{
	public static float EaseInOutCubic(final float x){
		return x < 0.5f ? 4.0f * x * x * x : 1.0f - (float)Math.pow(-2.0f * x + 2.0f, 3.0f) * 0.5f;
	}

	public static float EaseOutBounce(float x){
		final float n1 = 7.5625f;
		final float d1 = 2.75f;

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

	public static float EaseInOutBounce(final float x){
		return x < 0.5f ? (1.0f - EaseOutBounce(1.0f - 2.0f * x)) * 0.5f : (1.0f + EaseOutBounce(2.0f * x - 1.0f)) * 0.5f;
	}
}