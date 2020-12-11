package sg.diploma.product.entity;

import sg.diploma.product.math.Vector2;

public final class EntityAttribs{
	public EntityCollidableTypes.EntityCollidableType collidableType = EntityCollidableTypes.EntityCollidableType.Amt;
	public EntityRenderLayers.EntityRenderLayer renderLayer = EntityRenderLayers.EntityRenderLayer.Amt;
	public EntityTypes.EntityType type = EntityTypes.EntityType.Amt;

	public Vector2 pos = new Vector2();
	public Vector2 dir = new Vector2();
	public float life = 0.0f;
}