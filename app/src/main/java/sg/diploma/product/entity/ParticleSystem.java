package sg.diploma.product.entity;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	public ParticleSystem(){
		particleIndex = -1;
		particlePool = new ParticlePool();
	}

	public void Init(final int size){
		try{
			particlePool.Init(size, ()->EntityParticle.Create("particle_" + ++particleIndex));
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void Update(final float dt){


	}

	public EntityParticle ActivateParticle(){
		EntityParticle particle = particlePool.ActivateObj();
		EntityManager.Instance.AddEntity(particle.GetTag(), particle);
		return particle;
	}

	public void DeactivateParticle(final EntityParticle particle){
		EntityManager.Instance.SendEntityForRemoval(particle.GetTag());
		particlePool.DeactivateObj(particle);
	}

	private int particleIndex;
	private final ParticlePool particlePool;
}