package sg.diploma.product.entity;

public final class EntityRenderLayers{
	public enum EntityRenderLayer{
		BG(0),
		Normal(500),
		UI(9999),
		Amt(4);

		EntityRenderLayer(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	};
}
