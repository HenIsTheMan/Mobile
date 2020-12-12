package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.resource.ResourceManager;
import sg.diploma.product.resource.SpriteAnim;

public final class EntityBG extends EntityAbstract{
	public EntityBG(final int bitmapID){
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
	public void Update(float dt){
		spriteAnim.Update(dt);
	}

	@Override
	public void Render(Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, (int)attribs.pos.x, (int)attribs.pos.y);
	}

	public static EntityBG Create(final int bitmapID){
		EntityBG result = new EntityBG(bitmapID);
		EntityManager.Instance.AddEntity(result);
		return result;
	}

	private final SpriteAnim spriteAnim;
}