package sg.diploma.product.math;

public final class DetectCollision{
    public static boolean CircleCircle(final Vector2 pos0, final Vector2 pos1, final float radius0, final float radius1){
        final float xVec = pos1.x - pos0.x;
        final float yVec = pos1.y - pos1.y;

        final float distSquared = xVec * xVec + yVec * yVec;
        final float rSquared = (radius0 + radius1) * (radius0 + radius1);

        return distSquared <= rSquared;
    }

    public static boolean BoxBoxAABB(final Vector2 pos0, final Vector2 pos1, final Vector2 scale0, final Vector2 scale1){
        float[] halfWidth = new float[]{scale0.x * 0.5f, scale1.x * 0.5f};
        float[] halfHeight = new float[]{scale0.y * 0.5f, scale1.y * 0.5f};

        float[] xMin = new float[]{pos0.x - halfWidth[0], pos1.x - halfWidth[1]};
        float[] xMax = new float[]{pos0.x + halfWidth[0], pos1.x + halfWidth[1]};
        float[] yMin = new float[]{pos0.y - halfHeight[0], pos1.y - halfHeight[1]};
        float[] yMax = new float[]{pos0.y + halfHeight[0], pos1.y + halfHeight[1]};

        return xMin[0] < xMax[1] && xMax[0] > xMin[1] && yMin[0] < yMax[1] && yMax[0] > yMin[1];
    }
}