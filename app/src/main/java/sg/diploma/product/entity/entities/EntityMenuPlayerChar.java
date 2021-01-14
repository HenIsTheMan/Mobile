package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;
import sg.diploma.product.math.Vector2;

public final class EntityMenuPlayerChar extends EntityAbstract{
	private EntityMenuPlayerChar(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.MenuPlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565),
			4,
			9,
			10
		);
		
		attribs.dir = new Vector2((int)Math.random() % 2 == 1 ? 1.0f : -1.0f, 0.0f);
		storedVal = attribs.dir.x;
	}

	@Override
	public void Update(final float dt){
		if(attribs.targetPos != null){
			Vector2 vec = new Vector2(attribs.targetPos.x - attribs.pos.x, attribs.targetPos.y - attribs.pos.y);
			if(vec.LenSquared() < (attribs.spd * dt) * (attribs.spd * dt)){
				if(storedVal > 0.0f){
					spriteAnim.SetFrames(3 * 9, 3 * 9);
				} else{
					spriteAnim.SetFrames(9, 9);
				}

				attribs.spd = 0.0f;
				attribs.pos = attribs.targetPos; //Snap if super close
				attribs.targetPos = null;
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
		attribs.pos.y += attribs.dir.y * attribs.spd * dt;

		spriteAnim.Update(dt);
	}

	@Override
	public void Render(final Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, attribs.pos.x, attribs.pos.y);
	}

	@Override
	public void LateUpdate(final float dt){
	}

	@Override
	public void SpecialRender(final Canvas canvas){
		if(BuildConfig.DEBUG){
			throw new AssertionError("Assertion failed");
		}
	}

	@Override
	public void Collided(EntityAbstract other){
	}

	public static EntityMenuPlayerChar Create(final String key, final int bitmapID){
		EntityMenuPlayerChar result = new EntityMenuPlayerChar(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void StartMoving(final float xPos, final float yPos){
		attribs.targetPos = new Vector2(xPos, yPos);

		//* Bounds checking for attribs.targetPos
		if(attribs.xMin != null){
			attribs.targetPos.x = Math.max(attribs.xMin.val, attribs.targetPos.x);
		}
		if(attribs.xMax != null){
			attribs.targetPos.x = Math.min(attribs.xMax.val, attribs.targetPos.x);
		}

		if(attribs.yMin != null){
			attribs.targetPos.y = Math.max(attribs.yMin.val, attribs.targetPos.y);
		}
		if(attribs.yMax != null){
			attribs.targetPos.y = Math.min(attribs.yMax.val, attribs.targetPos.y);
		}
		//*/

		storedVal = xPos - attribs.pos.x;

		if(storedVal > 0.0f){
			spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
		} else{
			spriteAnim.SetFrames(9 + 1, 9 + 9);
		}
	}

	public float GetWidth(){
		return spriteAnim.GetWidth();
	}

	public float GetHeight(){
		return spriteAnim.GetHeight();
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