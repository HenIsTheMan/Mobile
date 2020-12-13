package sg.diploma.product.math;

public final class CollisionDataBoxBoxAABB{
	public CollisionDataBoxBoxAABB(){
		halfWidth = 0.0f;
		halfHeight = 0.0f;

		xMin = 0.0f;
		xMax = 0.0f;
		yMin = 0.0f;
		yMax = 0.0f;

		vel = Vector2.zero;
	}

	public float halfWidth;
	public float halfHeight;

	public float xMin;
	public float xMax;
	public float yMin;
	public float yMax;

	public Vector2 vel;
}
