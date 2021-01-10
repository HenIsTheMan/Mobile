package sg.diploma.product.entity;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	public ParticleSystem(){
		particlePool = new ParticlePool();
	}

	public void Init(final int size){
		try{
			particlePool.Init(size, EntityParticle.class);
		} catch(IllegalAccessException e){
			android.util.Log.e("me", "HERE2");
		} catch(InstantiationException e){
			//e.printStackTrace();
			android.util.Log.e("me", "HERE");
		}
	}

	public void Update(final float dt){
		EntityParticle particle = particlePool.ActivateObj();
		particlePool.DeactivateObj(particle);
	}

	private final ParticlePool particlePool;
}