package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventSendForDeactivation;
import sg.diploma.product.graphics.ResourceManager;

public final class EntityParticle extends EntityAbstract{
	private EntityParticle(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Particle;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		bitmap = ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565);
		paint = new Paint();
		paint.setARGB(255, 255, 255, 255);

		maxLife = 0.0f;
		life = 0.0f;
		minScale = 1.0f;
		maxScale = 1.0f;
	}

	@Override
	public void Update(float dt){
		life -= dt;
		if(life <= 0.0f){
			Publisher.Broadcast(new EventSendForDeactivation(this));
			return;
		}

		final float lifeRatio = life / maxLife;
		final int val = (int)(lifeRatio * 255.0f);
		paint.setARGB(val, val, val, val);
		attribs.scale.x = attribs.scale.y = (1.0f - lifeRatio) * maxScale + lifeRatio * minScale; //Lerp

		attribs.vel.x += attribs.accel.x * dt;
		attribs.vel.y += attribs.accel.y * dt;

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
	}

	@Override
	public void Render(Canvas _canvas){
		Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF dst = new RectF(
				attribs.pos.x - attribs.scale.x * 0.5f,
				attribs.pos.y - attribs.scale.y * 0.5f,
				attribs.pos.x + attribs.scale.x * 0.5f,
				attribs.pos.y + attribs.scale.y * 0.5f
		);
		_canvas.drawBitmap(bitmap, src, dst, paint);
	}

	@Override
	public void Collided(EntityAbstract other){
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

	public static EntityParticle Create(final int bitmapID){
		return new EntityParticle(bitmapID);
	}

	public void SetLife(final float life){
		this.life = life;
	}

	public void SetMaxLife(final float maxLife){
		this.maxLife = maxLife;
	}

	public void SetMinScale(final float minScale){
		this.minScale = minScale;
	}

	public void SetMaxScale(final float maxScale){
		this.maxScale = maxScale;
	}

	private final Bitmap bitmap;
	private final Paint paint;

	private float life;
	private float maxLife;
	private float minScale;
	private float maxScale;
}