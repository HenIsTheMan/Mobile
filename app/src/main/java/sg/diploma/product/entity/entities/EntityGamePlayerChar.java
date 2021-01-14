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

import static java.lang.Math.abs;
import static sg.diploma.product.math.Constants.epsilon;

public final class EntityGamePlayerChar extends EntityAbstract{
	private EntityGamePlayerChar(final int bitmapID, final ParticleSystem particleSystem){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.NormalFront;
		attribs.type = EntityTypes.EntityType.GamePlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		spawnParticleBT = 0.0f;
		elapsedTime = 0.0f;

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
		elapsedTime += dt;

		if(currPlat != null){
			attribs.vel.x = attribs.facing * 500.f; //##lvl??
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

		attribs.colliderPos.x = attribs.pos.x;
		attribs.colliderPos.y = attribs.pos.y + attribs.colliderScale.y * 0.075f;

		//* Check for end of game
		if(attribs.pos.y + attribs.colliderScale.y * 0.5f >= EntityManager.Instance.cam.GetPos().y + DeviceManager.screenHeightF
			|| attribs.pos.y - attribs.colliderScale.y * 0.5f <= EntityManager.Instance.cam.GetPos().y){
			Publisher.Broadcast(new EventEndGame());
			return;
		}
		//*/

		//* Spawning of particles
		if(spawnParticleBT <= elapsedTime && !(attribs.vel.x <= epsilon && -attribs.vel.x <= epsilon) && currPlat != null){
			EntityParticle particle = particleSystem.ActivateParticle();
			particle.SetLife(0.8f);
			particle.SetMaxLife(0.8f);
			particle.SetMinScale(attribs.colliderScale.x * 0.3f);
			particle.SetMaxScale(attribs.colliderScale.x * 0.9f);

			particle.attribs.pos.x = attribs.colliderPos.x;
			particle.attribs.pos.y = attribs.colliderPos.y + attribs.colliderScale.y * 0.5f - particle.attribs.scale.y * 0.5f;

			particle.attribs.vel.x = attribs.vel.x / abs(attribs.vel.x) * 10.0f;
			particle.attribs.vel.y = -10.0f;

			particle.attribs.accel.y = 80.0f;
			particle.attribs.accel.y = -200.0f;

			spawnParticleBT = elapsedTime + 0.1f;
		}
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

			if(attribs.pos.x - attribs.colliderScale.x * 0.5f < flipMinX){
				attribs.pos.x = flipMinX + attribs.colliderScale.x * 0.5f;
				SwitchFacing();
			}
			if(attribs.pos.x + attribs.colliderScale.x * 0.5f > flipMaxX){
				attribs.pos.x = flipMaxX - attribs.colliderScale.x * 0.5f;
				SwitchFacing();
			}
		}
	}

	@Override
	public void Collided(EntityAbstract other){
		if(other.attribs.type == EntityTypes.EntityType.Plat && currPlat == null && attribs.vel.y >= 0.0f){
			currPlat = (EntityPlat)other;
			attribs.pos.y = attribs.colliderPos.y = other.attribs.colliderPos.y - (other.attribs.colliderScale.y + attribs.colliderScale.y) * 0.5f;
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
		attribs.vel.x = attribs.facing * 500.f; //##lvl??
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

	private float spawnParticleBT;
	private float elapsedTime;

	private EntityPlat currPlat;
	private final ParticleSystem particleSystem;

	private final SpriteAnim spriteAnim;

	static final float playerCharHalfWidth;

	static{
		playerCharHalfWidth = ((float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f) * 0.5f;
	}
}