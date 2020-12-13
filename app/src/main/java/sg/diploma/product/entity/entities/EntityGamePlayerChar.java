package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.IEntityCollidable;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;

public final class EntityGamePlayerChar extends EntityAbstract implements IEntityCollidable{
	private EntityGamePlayerChar(final int bitmapID){
		super();
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
	}

	@Override
	public void Update(final float dt){
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
	public void Render(final Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, (int)attribs.pos.x, (int)attribs.pos.y);
	}

	public static EntityGamePlayerChar Create(final String key, final int bitmapID){
		EntityGamePlayerChar result = new EntityGamePlayerChar(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	@Override
	public void OnHit(final IEntityCollidable other){
		/*if(other.collidableType == EntityCollidableTypes.EntityCollidableType.Box){
			EntityManager.Instance.SendEntityForRemoval(this);
		}*/
	}

	public void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private final SpriteAnim spriteAnim;
}