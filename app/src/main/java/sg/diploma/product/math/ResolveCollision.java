package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

import static sg.diploma.product.math.Constants.epsilon;

public final class ResolveCollision{
	public static void BoxBoxAABB(EntityAbstract entity, EntityAbstract other, CollisionDataBoxBoxAABB myData, CollisionDataBoxBoxAABB otherData){
		entity.Collided(other);

		if(entity.attribs.vel.x <= epsilon
			&& -entity.attribs.vel.x <= epsilon
			&& entity.attribs.vel.y <= epsilon
			&& -entity.attribs.vel.y <= epsilon){
			return; //Return if vel is a zero/null/isotropic vec
		}

		if(entity.attribs.boxColliderPos.y + myData.halfHeight > otherData.yMin){
			entity.attribs.boxColliderPos.y = otherData.yMin - myData.halfHeight;
			entity.attribs.pos.y = entity.attribs.boxColliderPos.y - entity.attribs.boxColliderScale.y * 0.075f;
		}
	}
}
