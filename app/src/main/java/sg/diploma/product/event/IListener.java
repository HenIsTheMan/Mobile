package sg.diploma.product.event;

public interface IListener{
	enum ListenerFlags{ //Flags enum
		AddScore(0);

		ListenerFlags(final int val){
			this.val = 1 << val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}

	int OnEvent(EventAbstract event);
}