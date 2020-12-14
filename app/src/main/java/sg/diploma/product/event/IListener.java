package sg.diploma.product.event;

public interface IListener{
	enum ListenerFlags{ //Flags enum
		EndGame(0);

		ListenerFlags(final int val){
			this.val = 1 << val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}

	void OnEvent(EventAbstract event);
}