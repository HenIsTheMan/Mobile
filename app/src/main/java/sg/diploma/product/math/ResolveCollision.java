package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

import static sg.diploma.product.math.Constants.epsilon;

public final class ResolveCollision{
	public static void CircleCircle(final EntityAbstract circle0, final EntityAbstract circle1){
		circle0.Collided(circle1);
		circle1.Collided(circle0);
	}

	public static void CircleAABB(final EntityAbstract circle, final EntityAbstract AABB){
		circle.Collided(AABB);
		AABB.Collided(circle);
	}

	public static void AABBAABB(EntityAbstract entity, EntityAbstract other, CollisionDataAABBAABB myData, CollisionDataAABBAABB otherData){
		entity.Collided(other);

		if(entity.attribs.vel.x <= epsilon
			&& -entity.attribs.vel.x <= epsilon
			&& entity.attribs.vel.y <= epsilon
			&& -entity.attribs.vel.y <= epsilon){
			return; //Return if vel is a zero/null/isotropic vec
		}

		if(entity.attribs.prevPos.y + myData.halfHeight >= otherData.yMin
			&& (myData.xMin <= otherData.xMax
			|| myData.xMax >= otherData.xMin)
			&& entity.attribs.vel.y > 0.0f
		){
			entity.attribs.colliderPos.y = otherData.yMin - myData.halfHeight;
			entity.attribs.pos.y = entity.attribs.colliderPos.y - entity.attribs.colliderScale.y * 0.075f;
		}
	}
}
