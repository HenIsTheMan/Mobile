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

    public static boolean CircleCircle(final EntityAbstract circle0, final EntityAbstract circle1){
        final float xVec = circle1.attribs.pos.x - circle0.attribs.pos.x;
        final float yVec = circle1.attribs.pos.y - circle0.attribs.pos.y;

        final float distSquared = xVec * xVec + yVec * yVec;
        final float rSum = (circle0.attribs.scale.x + circle1.attribs.scale.x) * 0.5f;
        final float rSquared = rSum * rSum;

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

    public static boolean AABBAABB(final EntityAbstract AABB0, final EntityAbstract AABB1){
        Vector2 pos0 = AABB0.attribs.colliderPos;
        Vector2 pos1 = AABB1.attribs.colliderPos;

        Vector2 scale0 = AABB0.attribs.colliderScale;
        Vector2 scale1 = AABB1.attribs.colliderScale;

        final float halfWidth0 = scale0.x * 0.5f;
        final float halfWidth1 = scale1.x * 0.5f;

        final float halfHeight0 = scale0.y * 0.5f;
        final float halfHeight1 = scale1.y * 0.5f;

        final float minX0 = pos0.x - halfWidth0;
        final float minX1 = pos1.x - halfWidth1;

        final float maxX0 = pos0.x + halfWidth0;
        final float maxX1 = pos1.x + halfWidth1;

        final float minY0 = pos0.y - halfHeight0;
        final float minY1 = pos1.y - halfHeight1;

        final float maxY0 = pos0.y + halfHeight0;
        final float maxY1 = pos1.y + halfHeight1;

        return minX0 < maxX1
            && maxX0 > minX1
            && minY0 < maxY1
            && maxY0 > minY1;
    }

    public static boolean PlatAABB(final EntityAbstract plat, final EntityAbstract AABB){
        final float halfWidth = AABB.attribs.colliderScale.x * 0.5f;
        final float halfHeight = AABB.attribs.colliderScale.y * 0.5f;

        final float platHalfWidth = plat.attribs.colliderScale.x * 0.5f;
        final float platHalfHeight = plat.attribs.colliderScale.y * 0.5f;

        final float minX = AABB.attribs.colliderPos.x - halfWidth;
        final float maxX = AABB.attribs.colliderPos.x + halfWidth;
        final float maxY = AABB.attribs.colliderPos.y + halfHeight;

        final float platMinX = plat.attribs.colliderPos.x - platHalfWidth;
        final float platMaxX = plat.attribs.colliderPos.x + platHalfWidth;
        final float platMinY = plat.attribs.colliderPos.y - platHalfHeight;

        return minX < platMaxX
            && maxX > platMinX
            && maxY > platMinY
            && AABB.attribs.vel.y - plat.attribs.vel.y >= 0.0f; //Check relative vel
    }

    public static boolean PtCircle(final Vector2 ptPos, final Vector2 circlePos, final float circleRadius){
        final float x = ptPos.x - circlePos.x;
        final float y = ptPos.y - circlePos.y;

        return x * x + y * y < circleRadius * circleRadius;
    }
}