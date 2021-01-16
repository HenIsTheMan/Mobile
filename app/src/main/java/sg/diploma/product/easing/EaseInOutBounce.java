package sg.diploma.product.easing;

public class EaseInOutBounce extends Easing{
	@Override
	public float Ease(float x){
		return x < 0.5f ? (1.0f - EaseOutBounce.globalObj.Ease(1.0f - 2.0f * x)) * 0.5f : (1.0f + EaseOutBounce.globalObj.Ease(2.0f * x - 1.0f)) * 0.5f;
	}

	public static EaseInOutBounce globalObj;

	static{
		globalObj = new EaseInOutBounce();
	}
}