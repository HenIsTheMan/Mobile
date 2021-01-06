package sg.diploma.product.event.events;

import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventEndProg extends EventAbstract{
	public EventEndProg(){
		super(EventIDs.EventID.EndProg);
	}
}