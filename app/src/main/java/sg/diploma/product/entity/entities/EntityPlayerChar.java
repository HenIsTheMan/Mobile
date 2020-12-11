package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.IEntity;
import sg.diploma.product.entity.IEntityCollidable;

public final class EntityPlayerChar implements IEntity, IEntityCollidable{
	private final Bitmap bmp;

	public EntityPlayerChar(){
		bmp = null;
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.PlayerChar;
		//bmp = BitmapFactory.decodeResource(EntityManager.Instance.view.getResources(), R.mipmap.ic_launcher_foreground);
	}

	@Override
	public void Update(float dt){
		/*if(TouchManager.Instance.HasTouch()){
			if(CheckCollision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, imgRadius) || hasTouched){
				hasTouched = true;
				pos.x = TouchManager.Instance.GetPosX();
				pos.y = TouchManager.Instance.GetPosY();
			}
		}*/
	}

	@Override
	public void Render (Canvas canvas){ //Render with img centered
		assert bmp != null;
		canvas.drawBitmap(bmp, attribs.pos.x - bmp.getWidth() * 0.5f, attribs.pos.y - bmp.getHeight() * 0.5f, null);
	}

	public static EntityPlayerChar Create(){
		EntityPlayerChar result = new EntityPlayerChar();
		EntityManager.Instance.AddEntity(result, EntityTypes.EntityType.PlayerChar);
		return result;
	}

	public static EntityPlayerChar Create(EntityRenderLayers.EntityRenderLayer renderLayer){
		EntityPlayerChar result = Create();
		result.attribs.renderLayer = renderLayer;
		return result;
	}

	@Override
	public void OnHit(IEntityCollidable _other){
		/*if(_other.collidableType == EntityCollidableTypes.EntityCollidableType.Box){
			EntityManager.Instance.SendEntityForRemoval(this);
		}*/
	}
}