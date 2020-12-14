package sg.diploma.product.event.events;

import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventSpawnPlat extends EventAbstract{
	public EventSpawnPlat(){
		ID = EventIDs.EventID.SpawnPlat;
	}
}
