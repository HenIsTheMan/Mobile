package sg.diploma.product.entity;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	ParticleSystem(){
		particlePool = new ParticlePool();
	}

	public void Init(){
		try{
			particlePool.Init(10000, EntityParticle.class);
		} catch(InstantiationException | IllegalAccessException e){
			e.printStackTrace();
		}
	}

	public void Update(final float dt){
		EntityParticle particle = particlePool.ActivateObj();
		particlePool.DeactivateObj(particle);
	}

	private final ParticlePool particlePool;
}