package sg.diploma.product.easing;

public class EaseInOutCubic extends Easing{
	@Override
	public float Ease(final float x){
		return x < 0.5f ? 4.0f * x * x * x : 1.0f - (float)Math.pow(-2.0f * x + 2.0f, 3.0f) * 0.5f;
	}

	public static EaseInOutCubic globalObj;

	static{
		globalObj = new EaseInOutCubic();
	}
}