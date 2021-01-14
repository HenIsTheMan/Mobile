package sg.diploma.product.math;

import sg.diploma.product.entity.EntityAbstract;

public final class DetectCollision{
    public static boolean CircleCircle(final Vector2 pos0, final Vector2 pos1, final float radius0, final float radius1){
        final float xVec = pos1.x - pos0.x;
        final float yVec = pos1.y - pos0.y;

        final float distSquared = xVec * xVec + yVec * yVec;
        final float rSquared = (radius0 + radius1) * (radius0 + radius1);

        return distSquared <= rSquared;
    }

    public static boolean CircleAABB(final EntityAbstract circle, final EntityAbstract AABB){
        final float halfWidth = AABB.attribs.colliderScale.x * 0.5f;
        final float halfHeight = AABB.attribs.colliderScale.y * 0.5f;

        final float minX = AABB.attribs.colliderPos.x - halfWidth;
        final float maxX = AABB.attribs.colliderPos.x + halfWidth;
        final float minY = AABB.attribs.colliderPos.y - halfHeight;
        final float maxY = AABB.attribs.colliderPos.y + halfHeight;

        //* Calc pos of closest pt of AABB to circle
        final float x = Math.max(minX, Math.min(circle.attribs.colliderPos.x, maxX));
        final float y = Math.max(minY, Math.min(circle.attribs.colliderPos.y, maxY));
        //*/

        return PtCircle(new Vector2(x, y), circle.attribs.colliderPos, circle.attribs.colliderScale.x * 0.5f);
    }

    public static boolean AABBAABB(final EntityAbstract entity0, final EntityAbstract entity1, CollisionDataAABBAABB collisionData0, CollisionDataAABBAABB collisionData1){
        collisionData0.prevPos = entity0.attribs.prevPos;
        collisionData1.prevPos = entity1.attribs.prevPos;

        Vector2 pos0 = entity0.attribs.colliderPos;
        Vector2 pos1 = entity1.attribs.colliderPos;

        Vector2 scale0 = entity0.attribs.colliderScale;
        Vector2 scale1 = entity1.attribs.colliderScale;

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

    public static boolean PtCircle(final Vector2 ptPos, final Vector2 circlePos, final float circleRadius){
        final float x = ptPos.x - circlePos.x;
        final float y = ptPos.y - circlePos.y;

        return x * x + y * y < circleRadius * circleRadius;
    }
}