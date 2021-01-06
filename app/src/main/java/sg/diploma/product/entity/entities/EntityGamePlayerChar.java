package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import sg.diploma.product.R;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventEndGame;
import sg.diploma.product.game.GameData;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;
import sg.diploma.product.math.Pseudorand;
import sg.diploma.product.math.Vector2;

public final class EntityGamePlayerChar extends EntityAbstract{
	private EntityGamePlayerChar(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.GamePlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		collidingWithPlat = false;
		flipMinX = -Float.MAX_VALUE;
		flipMaxX = Float.MAX_VALUE;
		yTrigger = Float.MAX_VALUE;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565),
			4,
			9,
			14
		);

		if(Pseudorand.PseudorandInt() % 2 == 1){
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
	public final void Update(final float dt){
		if(attribs.vel.y > 0.0f && Math.abs(yTrigger - attribs.pos.y) > DeviceManager.screenHeightF){
			Publisher.Broadcast(new EventEndGame());
			return;
		}

		final float beginY = attribs.pos.y;

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

		GameData.playerTravelledY += Math.abs(attribs.pos.y - beginY);

		collidingWithPlat = false;

		spriteAnim.Update(dt);
	}

	@Override
	public final void Render(final Canvas canvas){ //Render with img centered
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
	public final void SpecialRender(final Canvas canvas){
		spriteAnim.Render(canvas, attribs.pos.x, DeviceManager.screenHeightF * 0.75f);
	}

	@Override
	public final void LateUpdate(final float dt){
		if(attribs.pos.y <= yTrigger){
			if(attribs.pos.x < flipMinX){
				attribs.pos.x = flipMinX;
				SwitchFacing();
			}
			if(attribs.pos.x > flipMaxX){
				attribs.pos.x = flipMaxX;
				SwitchFacing();
			}
		}

		flipMinX = -Float.MAX_VALUE;
		flipMaxX = Float.MAX_VALUE;
	}

	@Override
	public final void Collided(EntityAbstract other){
		collidingWithPlat = true;
		flipMinX = other.attribs.pos.x - other.attribs.scale.x * 0.5f + playerCharHalfWidth;
		flipMaxX = other.attribs.pos.x + other.attribs.scale.x * 0.5f - playerCharHalfWidth;
		yTrigger = other.attribs.pos.y - other.attribs.scale.y - attribs.scale.y * 0.5f;
	}

	public static EntityGamePlayerChar Create(final String key, final int bitmapID){
		EntityGamePlayerChar result = new EntityGamePlayerChar(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public final void SwitchFacing(){
		attribs.facing *= -1;
		if(attribs.facing == 1){
			spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
		} else{
			spriteAnim.SetFrames(9 + 1, 9 + 9);
		}
		attribs.vel.x = attribs.facing * 500.f;
	}

	public final void Jump(final Vector2 fingerDownPos, final Vector2 fingerUpPos){
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

	public final void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public final void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private boolean collidingWithPlat;
	private float flipMinX;
	private float flipMaxX;
	private float yTrigger;

	private final SpriteAnim spriteAnim;
	private final Paint paint;

	static final float playerCharHalfWidth;

	static{
		playerCharHalfWidth = ((float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f) * 0.5f;
	}
}