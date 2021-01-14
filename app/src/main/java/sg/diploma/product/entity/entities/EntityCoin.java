package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.ResourceManager;

public final class EntityCoin extends EntityAbstract{
	private EntityCoin(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Coin;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Circle;

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

	public static EntityCoin Create(final String key, final int bitmapID){
		EntityCoin result = new EntityCoin(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetMyIndex(final int myIndex){
		this.myIndex = myIndex;
	}

	private int myIndex;

	private final Bitmap bitmap;
	private final Paint paint;
}