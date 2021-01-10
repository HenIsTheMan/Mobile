package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;

public final class EntityParticle extends EntityAbstract{
	private EntityParticle(){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Particle;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		tag = null;
	}

	@Override
	public void Update(float dt){
		attribs.vel.x += attribs.accel.x * dt;
		attribs.vel.y = attribs.accel.y * dt;

		attribs.pos.x += attribs.vel.x * dt;
		attribs.pos.y += attribs.vel.y * dt;

		if(attribs.xMin != null){
			attribs.pos.x = Math.max(attribs.xMin.val, attribs.pos.x);
		}
		if(attribs.xMax != null){
			attribs.pos.x = Math.min(attribs.xMax.val, attribs.pos.x);
		}

		if(attribs.yMin != null){
			attribs.pos.y = Math.max(attribs.yMin.val, attribs.pos.y);
		}
		if(attribs.yMax != null){
			attribs.pos.y = Math.min(attribs.yMax.val, attribs.pos.y);
		}
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

	public static EntityParticle Create(final String tag){
		EntityParticle particle = new EntityParticle();
		particle.tag = tag;
		return particle;
	}

	public String GetTag(){
		return tag;
	}

	private String tag;
}