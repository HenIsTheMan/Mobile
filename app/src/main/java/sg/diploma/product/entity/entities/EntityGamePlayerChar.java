package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.R;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.entity.ParticleSystem;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventEndGame;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;
import sg.diploma.product.math.Pseudorand;

public final class EntityGamePlayerChar extends EntityAbstract{
	private EntityGamePlayerChar(final int bitmapID, final ParticleSystem particleSystem){
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

		this.particleSystem = particleSystem;
	}

	@Override
	public void Update(final float dt){
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

		//* Check for end of game
		if(attribs.pos.y + attribs.boxColliderScale.y * 0.5f >= EntityManager.Instance.cam.GetPos().y + DeviceManager.screenHeightF){
			Publisher.Broadcast(new EventEndGame());
			return;
		}
		//*/

		//* Spawning of particles
		EntityParticle particle = particleSystem.ActivateParticle();
		particle.attribs.pos.x = attribs.pos.x;
		particle.attribs.pos.y = attribs.pos.y;
		particle.attribs.vel.y = -1000.0f;
		//*/

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

			if(attribs.pos.x - attribs.boxColliderScale.x * 0.5f < flipMinX){
				attribs.pos.x = flipMinX + attribs.boxColliderScale.x * 0.5f;
				SwitchFacing();
			}
			if(attribs.pos.x + attribs.boxColliderScale.x * 0.5f > flipMaxX){
				attribs.pos.x = flipMaxX - attribs.boxColliderScale.x * 0.5f;
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

	public static EntityGamePlayerChar Create(final String key, final int bitmapID, final ParticleSystem particleSystem){
		EntityGamePlayerChar result = new EntityGamePlayerChar(bitmapID, particleSystem);
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

	public void Jump(final float jumpMag){
		if(currPlat != null){
			attribs.vel.y = jumpMag;
		}
	}

	public void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private EntityPlat currPlat;
	private final ParticleSystem particleSystem;

	private final SpriteAnim spriteAnim;

	static final float playerCharHalfWidth;

	static{
		playerCharHalfWidth = ((float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f) * 0.5f;
	}
}