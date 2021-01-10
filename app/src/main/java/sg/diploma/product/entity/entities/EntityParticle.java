package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityManager;

public final class EntityParticle extends EntityAbstract{
	EntityParticle(){
		super();
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

	public static EntityParticle Create(final String key){
		EntityParticle result = new EntityParticle();
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}
}