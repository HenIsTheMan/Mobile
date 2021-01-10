package sg.diploma.product.entity;

import java.util.ArrayList;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	public ParticleSystem(){
		particleIndex = -1;
		particlePool = new ParticlePool();
	}

	public void Init(final int size){
		try{
			particlePool.Init(size, EntityParticle::Create);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void Update(final float dt){
		ArrayList<EntityParticle> activeParticles = particlePool.RetrieveActiveParticles();
		final int activeParticlesSize = activeParticles.size();

		for(int i = 0; i < activeParticlesSize; ++i){
			activeParticles.get(i).Update(dt);
		}
	}

	public EntityParticle ActivateParticle(){
		return particlePool.ActivateObj();
	}

	public void DeactivateParticle(final EntityParticle particle){
		particlePool.DeactivateObj(particle);
	}

	private int particleIndex;
	private final ParticlePool particlePool;
}