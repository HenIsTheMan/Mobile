package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;

public final class EntityGameBG extends EntityAbstract{
	public EntityGameBG(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.BG;
		attribs.type = EntityTypes.EntityType.BG;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(bitmapID),
			2,
			5,
			4
		);
		spriteAnim.SetFrames(0, 8);
	}

	@Override
	public void Update(final float dt){
		spriteAnim.Update(dt);
	}

	@Override
	public void Render(final Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, (int)attribs.pos.x, (int)attribs.pos.y);
	}

	public static EntityGameBG Create(final String key, final int bitmapID){
		EntityGameBG result = new EntityGameBG(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private final SpriteAnim spriteAnim;
}