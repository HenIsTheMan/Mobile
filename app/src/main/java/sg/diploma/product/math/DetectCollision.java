package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

public final class DetectCollision{
    public static boolean CircleCircle(final Vector2 pos0, final Vector2 pos1, final float radius0, final float radius1){
        final float xVec = pos1.x - pos0.x;
        final float yVec = pos1.y - pos1.y;

        final float distSquared = xVec * xVec + yVec * yVec;
        final float rSquared = (radius0 + radius1) * (radius0 + radius1);

        return distSquared <= rSquared;
    }

    public static boolean BoxBoxAABB(final EntityAbstract entity0, final EntityAbstract entity1, CollisionDataBoxBoxAABB collisionData0, CollisionDataBoxBoxAABB collisionData1){
        collisionData0.prevPos = entity0.attribs.prevPos;
        collisionData1.prevPos = entity1.attribs.prevPos;

        Vector2 pos0 = entity0.attribs.boxColliderPos;
        Vector2 pos1 = entity1.attribs.boxColliderPos;

        Vector2 scale0 = entity0.attribs.boxColliderScale;
        Vector2 scale1 = entity1.attribs.boxColliderScale;

        collisionData0.halfWidth = scale0.x * 0.5f;
        collisionData1.halfWidth = scale1.x * 0.5f;

        collisionData0.halfHeight = scale0.y * 0.5f;
        collisionData1.halfHeight = scale1.y * 0.5f;

        collisionData0.xMin = pos0.x - collisionData0.halfWidth;
        collisionData1.xMin = pos1.x - collisionData1.halfWidth;

        collisionData0.xMax = pos0.x + collisionData0.halfWidth;
        collisionData1.xMax = pos1.x + collisionData1.halfWidth;

        collisionData0.yMin = pos0.y - collisionData0.halfHeight;
        collisionData1.yMin = pos1.y - collisionData1.halfHeight;

        collisionData0.yMax = pos0.y + collisionData0.halfHeight;
        collisionData1.yMax = pos1.y + collisionData1.halfHeight;

        return collisionData0.xMin < collisionData1.xMax
            && collisionData0.xMax > collisionData1.xMin
            && collisionData0.yMin < collisionData1.yMax
            && collisionData0.yMax > collisionData1.yMin;
    }
}