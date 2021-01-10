package sg.diploma.product.entity;

public final class EntityRenderLayers{
	public enum EntityRenderLayer{
		NormalBack(380),
		Normal(400),
		UI(9999),
		Amt(3);

		EntityRenderLayer(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	};
}
