package sg.diploma.product.event;

public final class EventIDs{
	public enum EventID{
		EndGame(0),
		SpawnPlat(1),
		AddScore(2),
		EndProg(3),
		Amt(4);

		EventID(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}