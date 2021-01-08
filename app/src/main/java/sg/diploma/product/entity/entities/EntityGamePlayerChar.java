package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.R;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
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

		currPlat = null;

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
	}

	@Override
	public void Update(final float dt){
		final float beginY = attribs.pos.y;

		if(currPlat != null){
			attribs.vel.x = attribs.facing * 500.f;
		} else{
			attribs.vel.y += attribs.accel.y * dt;
		}
		attribs.vel.x += attribs.accel.x * dt;
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

		currPlat = null;

		spriteAnim.Update(dt);
	}

	@Override
	public void Render(final Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, attribs.pos.x, attribs.pos.y);
	}

	@Override
	public void SpecialRender(final Canvas canvas){
		if(BuildConfig.DEBUG){
			throw new AssertionError("Assertion failed");
		}
	}

	@Override
	public void LateUpdate(final float dt){
		if(currPlat != null){
			final float flipMinX = currPlat.attribs.pos.x - currPlat.attribs.scale.x * 0.5f;
			final float flipMaxX = currPlat.attribs.pos.x + currPlat.attribs.scale.x * 0.5f;

			if(attribs.pos.x < flipMinX){
				attribs.pos.x = flipMinX;
				SwitchFacing();
			}
			if(attribs.pos.x > flipMaxX){
				attribs.pos.x = flipMaxX;
				SwitchFacing();
			}
		}
	}

	@Override
	public void Collided(EntityAbstract other){
		if(other.attribs.type == EntityTypes.EntityType.Plat && currPlat == null && attribs.vel.y >= 0.0f){
			currPlat = (EntityPlat)other;
		}
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
		if(currPlat != null && fingerDownPos != null && fingerUpPos != null){
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

	private EntityPlat currPlat;

	private final SpriteAnim spriteAnim;

	static final float playerCharHalfWidth;

	static{
		playerCharHalfWidth = ((float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f) * 0.5f;
	}
}