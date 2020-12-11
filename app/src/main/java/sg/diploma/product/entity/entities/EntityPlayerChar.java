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
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normmal;
		attribs.type = EntityTypes.EntityType.PlayerChar;
		//bmp = BitmapFactory.decodeResource(EntityManager.Instance.view.getResources(), R.mipmap.ic_launcher_foreground);
	}

	@Override
	public void Update(float _dt){
		/*if(TouchManager.Instance.HasTouch()){
			if(CheckCollision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, imgRadius) || hasTouched){
				hasTouched = true;
				pos.x = TouchManager.Instance.GetPosX();
				pos.y = TouchManager.Instance.GetPosY();
			}
		}*/
	}

	@Override
	public void Render (Canvas _canvas){ //Render with img centered
		assert bmp != null;
		_canvas.drawBitmap(bmp, pos.x - bmp.getWidth() * 0.5f, pos.y - bmp.getHeight() * 0.5f, null);
	}

	@Override
    public LayerTypes.LayerType GetRenderLayer(){
		return renderLayer;
	}

	@Override
	public void SetRenderLayer(LayerTypes.LayerType _newLayer){
		renderLayer = _newLayer;
	}

	public static EntityPlayerChar Create(){
		EntityPlayerChar result = new EntityPlayerChar();
		EntityManager.Instance.AddEntity(result, EntityType.PlayerChar);
		return result;
	}

	public static EntityPlayerChar Create(LayerTypes.LayerType _layer){
		EntityPlayerChar result = Create();
		result.SetRenderLayer(_layer);
		return result;
	}

	@Override
	public EntityType GetEntityType(){
		return type;
	}

	@Override
	public void SetEntityType(EntityType type){
		this.type = type;
	}

	@Override
	public String GetType () {
		return "EntityPlayerChar";
	}

	@Override
	public void OnHit(IEntityCollidable _other){
		if(_other.GetType().equals("NextEntity")){
			EntityManager.Instance.SendEntityForRemoval(this);
		}
	}
}