package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.IEntity;
import sg.diploma.product.entity.IEntityCollidable;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.resource.ResourceManager;
import sg.diploma.product.resource.SpriteAnim;

public final class EntityPlayerChar implements IEntity, IEntityCollidable{
	public EntityPlayerChar(){
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.PlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(R.drawable.player_char),
			21,
			13,
			7
		);
		spriteAnim.SetFrames(11 * 13, 11 * 13 + 9);

		attribs.dir = new Vector2((int)Math.random() % 2 == 1 ? 1.0f : -1.0f, 0.0f);
	}

	@Override
	public void Update(float dt){
		attribs.pos.x += attribs.dir.x * attribs.spd * dt;
		attribs.pos.y += attribs.dir.y * attribs.spd * dt;

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
		EntityManager.Instance.AddEntity(result);
		return result;
	}

	@Override
	public void OnHit(IEntityCollidable other){
		/*if(other.collidableType == EntityCollidableTypes.EntityCollidableType.Box){
			EntityManager.Instance.SendEntityForRemoval(this);
		}*/
	}

	public void StartMoving(){
		attribs.spd = 100.0f;
		spriteAnim.SetFrames(11 * 13, 11 * 13 + 1 + 8);
	}

	public void StopMoving(){
		attribs.spd = 0.0f;
		spriteAnim.SetFrames(11 * 13, 11 * 13);
	}

	public void GenScaledBitmap(){
		spriteAnim.GenScaledBitmap(attribs.scale);
	}

	private final SpriteAnim spriteAnim;
}