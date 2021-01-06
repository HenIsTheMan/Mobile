package sg.diploma.product.event;

public final class ListenerFlagsWrapper{
	public enum ListenerFlags{ //Flags enum
		GameData(0),
		GameScreenActivity(1),
		MenuScreenActivity(2);

		ListenerFlags(final int val){
			this.val = 1 << val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}