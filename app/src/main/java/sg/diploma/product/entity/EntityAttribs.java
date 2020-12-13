package sg.diploma.product.entity;

import sg.diploma.product.math.Vector2;

public final class EntityAttribs{
	public EntityCollidableTypes.EntityCollidableType collidableType = EntityCollidableTypes.EntityCollidableType.Amt;
	public EntityRenderLayers.EntityRenderLayer renderLayer = EntityRenderLayers.EntityRenderLayer.Amt;
	public EntityTypes.EntityType type = EntityTypes.EntityType.Amt;

	public Vector2 pos = Vector2.zero;
	public Vector2 scale = new Vector2(1.0f, 1.0f);
	public Vector2 targetPos = null;

	public EntityConstraint xMin = null;
	public EntityConstraint xMax = null;
	public EntityConstraint yMin = null;
	public EntityConstraint yMax = null;

	public Vector2 dir = Vector2.right;
	public float spd = 0.0f;

	public Vector2 accel = Vector2.zero;
	public Vector2 vel = Vector2.zero;
	public int facing = 1;
}