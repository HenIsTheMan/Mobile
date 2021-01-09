package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityManager;

public final class EntityParticle extends EntityAbstract{
	public EntityParticle(final int notPausedBitmapID, final int pausedBitmapID){
	}

	@Override
	public void Update(float _dt){
	}

	@Override
	public void Render(Canvas _canvas){
	}

	@Override
	public void Collided(EntityAbstract other){
	}

	@Override
	public void LateUpdate(final float dt){
	}

	@Override
	public void SpecialRender(final Canvas canvas){
	}

	public static EntityParticle Create(final String key, final int notPausedBitmapID, final int pausedBitmapID){
		EntityParticle result = new EntityParticle(notPausedBitmapID, pausedBitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}
}