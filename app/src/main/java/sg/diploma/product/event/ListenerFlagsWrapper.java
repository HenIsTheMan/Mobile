package sg.diploma.product.event;

public final class ListenerFlagsWrapper{
	public enum ListenerFlags{ //Flags enum
		GameScreenActivity(0),
		MenuScreenActivity(1),
		OptionsScreenActivity(2);

		ListenerFlags(final int val){
			this.val = 1 << val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}