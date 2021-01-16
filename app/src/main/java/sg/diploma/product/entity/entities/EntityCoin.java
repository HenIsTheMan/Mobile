package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.game.GameData;
import sg.diploma.product.graphics.ResourceManager;

public final class EntityCoin extends EntityAbstract{
	private EntityCoin(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Coin;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Circle;

		elapsedTime = 0.0f;
		yOffsetMag = 0.0f;
		yOffsetSpd = 0.0f;
		myIndex = 0;

		bitmap = ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565);
		paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
	}

	@Override
	public void Update(float dt){
		//* Despawn if outside view
		if(attribs.pos.y - attribs.scale.y * 0.5f >= EntityManager.Instance.cam.GetPos().y + DeviceManager.screenHeightF){
			EntityManager.Instance.SendEntityForRemoval("coin_" + myIndex);
		}
		//*/

		elapsedTime += dt;

		final float yOffset = (float)Math.sin(elapsedTime * yOffsetSpd) * yOffsetMag * dt;
		attribs.pos.y += yOffset;
		attribs.colliderPos.y += yOffset;
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
		if(other.attribs.type == EntityTypes.EntityType.GamePlayerChar){
			++GameData.collectedCoins;
			EntityManager.Instance.SendEntityForRemoval("coin_" + myIndex);
			AudioManager.Instance.PlayAudio(R.raw.coin, AudioTypes.AudioType.Sound);
		}
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

	public static EntityCoin Create(final String key, final int bitmapID){
		EntityCoin result = new EntityCoin(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetYOffsetMag(final float yOffsetMag){
		this.yOffsetMag = yOffsetMag;
	}

	public void SetYOffsetSpd(final float yOffsetSpd){
		this.yOffsetSpd = yOffsetSpd;
	}

	public void SetMyIndex(final int myIndex){
		this.myIndex = myIndex;
	}

	private float elapsedTime;
	private float yOffsetMag;
	private float yOffsetSpd;
	private int myIndex;

	private final Bitmap bitmap;
	private final Paint paint;
}