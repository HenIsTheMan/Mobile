package sg.diploma.product.event.events;

import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventReturnToMenuWithoutSaving extends EventAbstract{
	public EventReturnToMenuWithoutSaving(){
		super(EventIDs.EventID.ReturnToMenuWithoutSaving);
	}
}