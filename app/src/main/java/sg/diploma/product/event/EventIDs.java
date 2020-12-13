package sg.diploma.product.event;

public final class EventIDs{
	public enum EventID{
		Amt(0);

		EventID(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}