package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.IEntity;
import sg.diploma.product.entity.IEntityCollidable;
import sg.diploma.product.resource.SpriteAnim;

public final class EntityPlayerChar implements IEntity, IEntityCollidable{
	public EntityPlayerChar(){
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.PlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		spriteAnim = new SpriteAnim(
			BitmapFactory.decodeResource(EntityManager.Instance.view.getResources(), R.drawable.player_char),
			21,
			13,
			7
		);
		spriteAnim.SetFrames(11 * 13 + 1, 11 * 13 + 1 + 8);

		attribs.pos.x = 50.0f;
		attribs.pos.y = 50.0f;
	}

	@Override
	public void Update(float dt){
		spriteAnim.Update(dt);

		/*if(TouchManager.Instance.HasTouch()){
			if(CheckCollision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, imgRadius) || hasTouched){
				hasTouched = true;
				pos.x = TouchManager.Instance.GetPosX();
				pos.y = TouchManager.Instance.GetPosY();
			}
		}*/
	}

	@Override
	public void Render(Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, (int)attribs.pos.x, (int)attribs.pos.y);
	}

	public static EntityPlayerChar Create(){
		EntityPlayerChar result = new EntityPlayerChar();
		EntityManager.Instance.AddEntity(result, attribs.type);
		return result;
	}

	@Override
	public void OnHit(IEntityCollidable other){
		/*if(other.collidableType == EntityCollidableTypes.EntityCollidableType.Box){
			EntityManager.Instance.SendEntityForRemoval(this);
		}*/
	}

	private final SpriteAnim spriteAnim;
}