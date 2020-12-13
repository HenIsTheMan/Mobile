package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

import static sg.diploma.product.math.Constants.epsilon;

public final class ResolveCollision{
	public static void BoxBoxAABB(EntityAbstract entity, EntityAbstract other, CollisionDataBoxBoxAABB myData, CollisionDataBoxBoxAABB otherData){
		entity.Collided();

		if(entity.attribs.vel.x <= epsilon
			&& -entity.attribs.vel.x <= epsilon
			&& entity.attribs.vel.y <= epsilon
			&& -entity.attribs.vel.y <= epsilon){
			return; //Return if vel is a zero/null/isotropic vec
		}

		/*if(entity.attribs.prevPos.x - myData.halfWidth >= otherData.xMax){
			entity.attribs.boxColliderPos.x = otherData.xMax + myData.halfWidth;
			entity.attribs.pos.x = entity.attribs.boxColliderPos.x;
		}
		if(entity.attribs.prevPos.x + myData.halfWidth <= otherData.xMin){
			entity.attribs.boxColliderPos.x = otherData.xMin - myData.halfWidth;
			entity.attribs.pos.x = entity.attribs.boxColliderPos.x;
		}
		if(entity.attribs.prevPos.y - myData.halfHeight <= otherData.yMin){
			entity.attribs.pos.y = otherData.yMin + myData.halfHeight - entity.attribs.boxColliderScale.y * 0.075f * 4.0f;
			//entity.attribs.pos.y = entity.attribs.boxColliderPos.y - entity.attribs.boxColliderScale.y * 0.075f;
		}
		if(entity.attribs.prevPos.y + myData.halfHeight >= otherData.yMax){
			entity.attribs.pos.y = otherData.yMax - myData.halfHeight - entity.attribs.boxColliderScale.y * 0.075f * 4.0f;
			//entity.attribs.pos.y = entity.attribs.boxColliderPos.y - entity.attribs.boxColliderScale.y * 0.075f;
		}*/

		entity.attribs.vel.y = 0.0f;
		entity.attribs.vel.y = 0.0f;
	}
}
