package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.resource.ResourceManager;
import sg.diploma.product.resource.SpriteAnim;

public final class EntityBG extends EntityAbstract{
	public EntityBG(final int bitmapID, final float xScale, final float yScale){
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.BG;
		attribs.type = EntityTypes.EntityType.BG;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		Bitmap bmp = ResourceManager.Instance.GetBitmap(bitmapID);
		final int iWidth = bmp.getWidth();
		final int iHeight = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(xScale, yScale);
		Bitmap newImage = Bitmap.createBitmap(bmp, 0, 0, iWidth, iHeight, matrix, true);

		spriteAnim = new SpriteAnim(
			newImage,
			2,
			5,
			4
		);
		spriteAnim.SetFrames(0, 8);

		attribs.scale.x = xScale;
		attribs.scale.y = yScale;
	}

	@Override
	public void Update(float dt){
		spriteAnim.Update(dt);
	}

	@Override
	public void Render(Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, (int)attribs.pos.x, (int)attribs.pos.y);
	}

	public static EntityBG Create(final int bitmapID, final float xScale, final float yScale){
		EntityBG result = new EntityBG(bitmapID, xScale, yScale);
		EntityManager.Instance.AddEntity(result);
		return result;
	}

	private final SpriteAnim spriteAnim;
}