package sg.diploma.product.event.events;

import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventEndGame extends EventAbstract{
	public EventEndGame(){
		super(EventIDs.EventID.EndGame);
	}
}
