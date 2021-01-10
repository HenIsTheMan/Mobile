package sg.diploma.product.event.events;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventDeactivateParticle extends EventAbstract{
	public EventDeactivateParticle(final EntityParticle particle){
		super(EventIDs.EventID.DeactivateParticle);
		this.particle = particle;
	}

	public EntityParticle GetParticle(){
		return particle;
	}

	private final EntityParticle particle;
}