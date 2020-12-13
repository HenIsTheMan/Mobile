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

        collisionData0.halfWidth = entity0.attribs.scale.x * 0.5f;
        collisionData1.halfWidth = entity1.attribs.scale.x * 0.5f;

        collisionData0.halfHeight = entity0.attribs.scale.y * 0.5f;
        collisionData1.halfHeight = entity1.attribs.scale.y * 0.5f;

        collisionData0.xMin = entity0.attribs.pos.x - collisionData0.halfWidth;
        collisionData1.xMin = entity1.attribs.pos.x - collisionData1.halfWidth;

        collisionData0.xMax = entity0.attribs.pos.x + collisionData0.halfWidth;
        collisionData1.xMax = entity1.attribs.pos.x + collisionData1.halfWidth;

        collisionData0.yMin = entity0.attribs.pos.y - collisionData0.halfHeight;
        collisionData1.yMin = entity1.attribs.pos.y - collisionData1.halfHeight;

        collisionData0.yMax = entity0.attribs.pos.y + collisionData0.halfHeight;
        collisionData1.yMax = entity1.attribs.pos.y + collisionData1.halfHeight;

        float[] halfWidth = new float[]{scale0.x * 0.5f, scale1.x * 0.5f};
        float[] halfHeight = new float[]{scale0.y * 0.5f, scale1.y * 0.5f};

        float[] xMin = new float[]{pos0.x - halfWidth[0], pos1.x - halfWidth[1]};
        float[] xMax = new float[]{pos0.x + halfWidth[0], pos1.x + halfWidth[1]};
        float[] yMin = new float[]{pos0.y - halfHeight[0], pos1.y - halfHeight[1]};
        float[] yMax = new float[]{pos0.y + halfHeight[0], pos1.y + halfHeight[1]};

        return xMin[0] < xMax[1] && xMax[0] > xMin[1] && yMin[0] < yMax[1] && yMax[0] > yMin[1];
    }
}