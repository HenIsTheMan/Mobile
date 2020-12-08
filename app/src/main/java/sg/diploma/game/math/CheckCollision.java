package sg.diploma.game.math;

public final class CheckCollision{
    public static boolean CircleCircle(final Vector2 pos0, final Vector2 pos1, final float radius0, final float radius1){
        final float xVec = pos1.x - pos0.x;
        final float yVec = pos1.y - pos1.y;

        final float distSquared = xVec * xVec + yVec * yVec;
        final float rSquared = (radius0 + radius1) * (radius0 + radius1);

        return distSquared <= rSquared;
    }
}