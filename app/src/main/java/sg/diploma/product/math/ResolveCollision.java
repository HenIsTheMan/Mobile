package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

public final class ResolveCollision{
	public static void BoxBoxAABB(EntityAbstract entity, CollisionDataBoxBoxAABB myData, CollisionDataBoxBoxAABB otherData){
		/*if(myData.xMax > otherData.xMin && myData.xMax < otherData.xMax){
			entity.attribs.pos.x = otherData.xMin - myData.halfWidth;
		} else if(myData.xMin < otherData.xMax && myData.xMin > otherData.xMin){
			entity.attribs.pos.x = otherData.xMax + myData.halfWidth;
		}
		if(myData.yMax > otherData.yMin && myData.yMax < otherData.yMax){
			entity.attribs.pos.y = otherData.yMin - myData.halfHeight;
		} else if(myData.yMin < otherData.yMax && myData.yMin > otherData.yMin){
			entity.attribs.pos.y = otherData.yMax + myData.halfHeight;
		}*/

		entity.attribs.vel.y = 0.0f;
		entity.attribs.accel.y = 0.0f;
	}
}
