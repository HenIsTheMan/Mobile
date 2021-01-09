package sg.diploma.product.entity;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	ParticleSystem(){
	}

	public void Init(){
		ParticlePool particlePool = new ParticlePool();

		EntityParticle particle = particlePool.checkOut();

		particlePool.checkIn(particle);
	}
}