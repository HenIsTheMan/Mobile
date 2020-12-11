package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.IEntity;
import sg.diploma.product.resource.ResourceManager;

public final class EntityBG implements IEntity{
	public EntityBG(){
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.BG;
		attribs.type = EntityTypes.EntityType.BG;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;
		bmp = ResourceManager.Instance.GetBitmap(R.mipmap.ic_launcher_foreground);
	}

	@Override
	public void Update(float dt){
	}

	@Override
	public void Render (Canvas canvas){ //Render with img centered
		assert bmp != null;
		canvas.drawBitmap(bmp, attribs.pos.x - bmp.getWidth() * 0.5f, attribs.pos.y - bmp.getHeight() * 0.5f, null);
	}

	public static EntityBG Create(){
		EntityBG result = new EntityBG();
		EntityManager.Instance.AddEntity(result, attribs.type);
		return result;
	}

	private final Bitmap bmp;
}