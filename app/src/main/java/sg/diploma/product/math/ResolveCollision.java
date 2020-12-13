package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

import static sg.diploma.product.math.Constants.epsilon;

public final class ResolveCollision{
	public static void BoxBoxAABB(EntityAbstract entity, CollisionDataBoxBoxAABB myData, CollisionDataBoxBoxAABB otherData){
		entity.Collided();

		if(entity.attribs.vel.x <= epsilon
			&& -entity.attribs.vel.x <= epsilon
			&& entity.attribs.vel.y <= epsilon
			&& -entity.attribs.vel.y <= epsilon){
			return; //Return if vel is a zero/null/isotropic vec
		}

		/*if(entity.attribs.prevPos.x - myData.halfWidth >= otherData.xMax){
			entity.attribs.pos.x = otherData.xMax + myData.halfWidth;
		}
		if(entity.attribs.prevPos.x + myData.halfWidth <= otherData.xMin){
			entity.attribs.pos.x = otherData.xMin - myData.halfWidth;
		}
		if(entity.attribs.prevPos.y - myData.halfHeight <= otherData.yMin){
			entity.attribs.pos.y = otherData.yMin - myData.halfHeight;
		}
		if(entity.attribs.prevPos.y + myData.halfHeight >= otherData.yMax){
			entity.attribs.pos.y = otherData.yMax + myData.halfHeight;
		}*/

		entity.attribs.vel.y = 0.0f;
		entity.attribs.accel.y = 0.0f;
	}
}
