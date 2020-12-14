package sg.diploma.product.event;

import java.util.HashMap;

public final class Publisher{
	private Publisher(){
	}

	public static void AddListener(final int flags, final IListener listener){
		listeners.put(flags, listener);
	}

	public static void RemoveListener(final int flags){
		listeners.remove(flags);
	}

	public static void Broadcast(EventAbstract event){
		for(IListener listener: listeners.values()){
			listener.OnEvent(event);
		}
	}

	private static final HashMap<Integer, IListener> listeners;

	static{
		listeners = new HashMap<>();
	}
}
