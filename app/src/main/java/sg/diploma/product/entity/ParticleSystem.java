package sg.diploma.product.entity;

import android.graphics.Canvas;

import java.util.ArrayList;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	public ParticleSystem(){
		particlePool = null;
		particlesToDeactivate = null;
	}

	public void Init(final int size, final int bitmapID){
		particlePool = new ParticlePool();
		particlesToDeactivate = new ArrayList<>(size);

		try{
			particlePool.Init(size, ()->EntityParticle.Create(bitmapID));
		} catch(final Exception e){
			e.printStackTrace();
		}
	}

	public void Update(final float dt){
		ArrayList<EntityParticle> activeParticles = particlePool.RetrieveActiveParticles();
		final int activeParticlesSize = activeParticles.size();

		for(int i = 0; i < activeParticlesSize; ++i){
			activeParticles.get(i).Update(dt);
		}

		DeactivateParticles();
	}

	public void Render(final Canvas canvas){
		ArrayList<EntityParticle> activeParticles = particlePool.RetrieveActiveParticles();
		final int activeParticlesSize = activeParticles.size();

		for(int i = 0; i < activeParticlesSize; ++i){
			activeParticles.get(i).Render(canvas);
		}
	}

	public EntityParticle ActivateParticle(){
		return particlePool.ActivateObj();
	}

	public void DeactivateParticle(final EntityParticle particle){
		particlePool.DeactivateObj(particle);
	}

	public void AddParticleToDeactivate(final EntityParticle particle){
		particlesToDeactivate.add(particle);
	}

	private void DeactivateParticles(){
		for(EntityParticle particle: particlesToDeactivate){
			DeactivateParticle(particle);
		}
	}

	private ParticlePool particlePool;
	private ArrayList<EntityParticle> particlesToDeactivate;
}