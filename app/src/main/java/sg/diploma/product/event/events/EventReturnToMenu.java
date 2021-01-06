package sg.diploma.product.event.events;

import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventReturnToMenu extends EventAbstract{
	public EventReturnToMenu(){
		super(EventIDs.EventID.ReturnToMenu);
	}
}