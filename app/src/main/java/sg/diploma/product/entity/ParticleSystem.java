package sg.diploma.product.entity;

import android.graphics.Canvas;

import java.util.ArrayList;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.obj_pools.ParticlePool;

public final class ParticleSystem{
	public ParticleSystem(){
		particlePool = new ParticlePool();
	}

	public void Init(final int size, final int bitmapID){
		try{
			particlePool.Init(size, ()->EntityParticle.Create(bitmapID));
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void Update(final float dt){
		ArrayList<EntityParticle> activeParticles = particlePool.RetrieveActiveParticles();

		for(int i = 0; i < activeParticles.size(); ++i){
			activeParticles.get(i).Update(dt); //java.lang.NullPointerException: Attempt to invoke virtual method 'void sg.diploma.product.entity.entities.EntityParticle.Update(float)' on a null object reference??
		}
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

	private final ParticlePool particlePool;
}