package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.IEntityCollidable;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.resource.ResourceManager;
import sg.diploma.product.resource.SpriteAnim;

public final class EntityPlayerChar extends EntityAbstract implements IEntityCollidable{
	public EntityPlayerChar(final int bitmapID){
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.PlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(bitmapID),
			4,
			9,
			10
		);

		if((int)Math.random() % 2 == 1){
			spriteAnim.SetFrames(3 * 13, 3 * 9);
			attribs.dir = new Vector2(1.0f, 0.0f);
		} else{
			spriteAnim.SetFrames(9, 9);
			attribs.dir = new Vector2(-1.0f, 0.0f);
		}
		storedVal = attribs.dir.x;
	}

	@Override
	public void Update(float dt){
		if(attribs.targetPos != null){
			Vector2 vec = new Vector2(attribs.targetPos.x - attribs.pos.x, attribs.targetPos.y - attribs.pos.y);
			if(vec.Len() < attribs.spd * dt){
				if(storedVal > 0.0f){
					spriteAnim.SetFrames(3 * 9, 3 * 9);
				} else{
					spriteAnim.SetFrames(9, 9);
				}

				attribs.spd = 0.0f;
				attribs.pos = attribs.targetPos; //Snap if super close
			} else{
				attribs.spd = 400.0f;
				attribs.dir = new Vector2(attribs.targetPos.x - attribs.pos.x, attribs.targetPos.y - attribs.pos.y).Normalized();
			}
		} else{
			if(storedVal > 0.0f){
				spriteAnim.SetFrames(3 * 9, 3 * 9);
			} else{
				spriteAnim.SetFrames(9, 9);
			}
		}

		attribs.pos.x += attribs.dir.x * attribs.spd * dt;
		if(attribs.xMin != null){
			attribs.pos.x = Math.max(attribs.xMin.val, attribs.pos.x);
		}
		if(attribs.xMax != null){
			attribs.pos.x = Math.min(attribs.xMax.val, attribs.pos.x);
		}

		attribs.pos.y += attribs.dir.y * attribs.spd * dt;
		if(attribs.yMin != null){
			attribs.pos.y = Math.max(attribs.yMin.val, attribs.pos.y);
		}
		if(attribs.yMax != null){
			attribs.pos.y = Math.min(attribs.yMax.val, attribs.pos.y);
		}

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

	public static EntityPlayerChar Create(String key, final int bitmapID){
		EntityPlayerChar result = new EntityPlayerChar(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	@Override
	public void OnHit(IEntityCollidable other){
		/*if(other.collidableType == EntityCollidableTypes.EntityCollidableType.Box){
			EntityManager.Instance.SendEntityForRemoval(this);
		}*/
	}

	public void StartMoving(final float xPos, final float yPos){
		attribs.targetPos = new Vector2(xPos, yPos);
		storedVal = xPos - attribs.pos.x;

		if(storedVal > 0.0f){
			spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
		} else{
			spriteAnim.SetFrames(9 + 1, 9 + 9);
		}
	}

	public void GenScaledBitmap(){ //Slow
		spriteAnim.GenScaledBitmap(attribs.scale);
	}

	public void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private final SpriteAnim spriteAnim;
	private float storedVal;
}