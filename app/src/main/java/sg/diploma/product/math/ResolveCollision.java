package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

public final class ResolveCollision{
	public static void CircleCircle(final EntityAbstract circle0, final EntityAbstract circle1){
		circle0.Collided(circle1);
		circle1.Collided(circle0);
	}

	public static void CircleAABB(final EntityAbstract circle, final EntityAbstract AABB){
		circle.Collided(AABB);
		AABB.Collided(circle);
	}

	public static void AABBAABB(final EntityAbstract AABB0, final EntityAbstract AABB1){
		AABB0.Collided(AABB1);
		AABB1.Collided(AABB0);
	}
}