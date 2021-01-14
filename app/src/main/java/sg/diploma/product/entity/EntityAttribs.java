package sg.diploma.product.entity;

import sg.diploma.product.math.Vector2;

public final class EntityAttribs{
	public EntityCollidableTypes.EntityCollidableType collidableType = EntityCollidableTypes.EntityCollidableType.Amt;
	public EntityRenderLayers.EntityRenderLayer renderLayer = EntityRenderLayers.EntityRenderLayer.Amt;
	public EntityTypes.EntityType type = EntityTypes.EntityType.Amt;

	public Vector2 pos = new Vector2();
	public Vector2 scale = new Vector2(1.0f, 1.0f);
	public Vector2 targetPos = null;

	public Vector2 colliderPos = new Vector2();
	public Vector2 colliderScale = new Vector2();

	public EntityConstraint xMin = null;
	public EntityConstraint xMax = null;
	public EntityConstraint yMin = null;
	public EntityConstraint yMax = null;

	public Vector2 dir = new Vector2(1.0f, 0.0f);
	public float spd = 0.0f;

	public Vector2 accel = new Vector2();
	public Vector2 vel = new Vector2();
	public int facing = 1;
}