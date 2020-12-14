package sg.diploma.product.event;

public final class EventIDs{
	public enum EventID{
		EndGame(0),
		Amt(1);

		EventID(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}