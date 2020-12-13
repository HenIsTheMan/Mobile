package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.concurrent.ThreadLocalRandom;

import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;
import sg.diploma.product.math.Vector2;

public final class EntityGamePlayerChar extends EntityAbstract{
	private EntityGamePlayerChar(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.GamePlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		collidingWithPlat = false;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565),
			4,
			9,
			14
		);

		if(ThreadLocalRandom.current().nextInt() % 2 == 1){
			spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
			attribs.facing = 1;
		} else{
			spriteAnim.SetFrames(9 + 1, 9 + 9);
			attribs.facing = -1;
		}

		attribs.accel.y = 4000.0f; //Gravitational accel

		paint = new Paint();

		paint.setARGB(153, 51, 51, 51);
		paint.setStrokeWidth(50.0f);
		paint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void Update(final float dt){
		if(collidingWithPlat){
			attribs.vel.x = attribs.facing * 500.f;
		}

		attribs.vel.x += attribs.accel.x * dt;
		if(!collidingWithPlat){
			attribs.vel.y += attribs.accel.y * dt;
		}
		attribs.vel.y = Math.min(attribs.vel.y, 3000.0f);

		attribs.pos.x += attribs.vel.x * dt;
		attribs.pos.y += attribs.vel.y * dt;

		if(attribs.xMin != null){
			attribs.pos.x = Math.max(attribs.xMin.val, attribs.pos.x);
		}
		if(attribs.xMax != null){
			attribs.pos.x = Math.min(attribs.xMax.val, attribs.pos.x);
		}

		if(attribs.yMin != null){
			attribs.pos.y = Math.max(attribs.yMin.val, attribs.pos.y);
		}
		if(attribs.yMax != null){
			attribs.pos.y = Math.min(attribs.yMax.val, attribs.pos.y);
		}

		attribs.boxColliderPos.x = attribs.pos.x;
		attribs.boxColliderPos.y = attribs.pos.y + attribs.boxColliderScale.y * 0.075f;

		collidingWithPlat = false;

		spriteAnim.Update(dt);
	}

	@Override
	public void Render(final Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, attribs.pos.x, attribs.pos.y);

		canvas.drawRect(
			attribs.boxColliderPos.x - attribs.boxColliderScale.x * 0.5f,
			attribs.boxColliderPos.y - attribs.boxColliderScale.y * 0.5f,
			attribs.boxColliderPos.x + attribs.boxColliderScale.x * 0.5f,
			attribs.boxColliderPos.y + attribs.boxColliderScale.y * 0.5f,
			paint
		);
	}

	@Override
	public void SpecialRender(final Canvas canvas){
		spriteAnim.Render(canvas, attribs.pos.x, DeviceManager.screenHeightF * 0.75f);
	}

	@Override
	public void LateUpdate(final float dt){
	}

	@Override
	public void Collided(){
		collidingWithPlat = true;
	}

	public static EntityGamePlayerChar Create(final String key, final int bitmapID){
		EntityGamePlayerChar result = new EntityGamePlayerChar(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SwitchFacing(){
		attribs.facing *= -1;
		if(attribs.facing == 1){
			spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
		} else{
			spriteAnim.SetFrames(9 + 1, 9 + 9);
		}
		attribs.vel.x = attribs.facing * 500.f;
	}

	public void Jump(final Vector2 fingerDownPos, final Vector2 fingerUpPos){
		if(collidingWithPlat && fingerDownPos != null && fingerUpPos != null){
			Vector2 vec = new Vector2(fingerUpPos.x - fingerDownPos.x, fingerUpPos.y - fingerDownPos.y);
			attribs.vel.x = Math.min(vec.x * 0.25f, 800.0f);
			attribs.vel.y = Math.max(vec.y * 3.5f, -3000.0f);

			if(attribs.vel.x > 0.0f){
				spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
				attribs.facing = 1;
			} else{
				spriteAnim.SetFrames(9 + 1, 9 + 9);
				attribs.facing = -1;
			}
		}
	}

	public void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private boolean collidingWithPlat;
	private final SpriteAnim spriteAnim;
	private final Paint paint;
}