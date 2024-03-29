package sg.diploma.product.entity.entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.easing.Easing;
import sg.diploma.product.easing.EaseInOutCubic;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.game.GameData;
import sg.diploma.product.graphics.Color;

public final class EntityPlat extends EntityAbstract{
	private EntityPlat(){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Plat;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Plat;

		strokeWidth = 50.0f;
		paintStyle = Paint.Style.FILL;
		paint = new Paint();
		steppedOnColor = new Color(1.0f, 1.0f, 0.0f, 0.7f);

		paint.setARGB(255, 51, 51, 51);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(paintStyle);

		collided = false;
		elapsedTime = 0.0f;
		myIndex = 0;

		currPopTime = 0.0f;
		maxPopTime = 0.4f;
		renderScaleX = 1.0f;
		renderScaleY = 1.0f;

		xOffset = 0.0f;
		xOffsetPrev = 0.0f;
		xOffsetMag = 0.0f;
		xOffsetSpd = 0.0f;
		easing = null;
	}

	@Override
	public void Update(final float dt){
		//* Despawn if outside view
		if(attribs.pos.y - attribs.scale.y * 0.5f >= EntityManager.Instance.cam.GetPos().y + DeviceManager.screenHeightF){
			EntityManager.Instance.SendEntityForRemoval("plat_" + myIndex);
		}
		//*/

		elapsedTime += dt;

		if(easing != null){
			xOffsetPrev = xOffset;
			final float startX = -xOffsetMag;
			final float endX = xOffsetMag;
			final float lerpFactor = easing.Ease((float)(Math.sin(elapsedTime * xOffsetSpd) * 0.5f + 0.5f));
			xOffset = (1.0f - lerpFactor) * startX + lerpFactor * endX;

			attribs.pos.x += xOffset - xOffsetPrev;
			attribs.colliderPos.x = attribs.pos.x;
		}

		if(currPopTime >= 0.0f){
			final float startScale = 1.0f;
			final float endScale = 1.2f;
			final float lerpFactor = EaseInOutCubic.globalObj.Ease(currPopTime / maxPopTime);
			final float scaleFactor = (1.0f - lerpFactor) * startScale + lerpFactor * endScale;

			renderScaleX = attribs.scale.x * scaleFactor;
			renderScaleY = attribs.scale.y * scaleFactor;

			currPopTime -= dt;
		} else{
			renderScaleX = attribs.scale.x;
			renderScaleY = attribs.scale.y;
		}
	}

	@Override
	public void Render(final Canvas canvas){
		canvas.drawRect(
			attribs.pos.x - renderScaleX * 0.5f,
			attribs.pos.y - renderScaleY * 0.5f,
			attribs.pos.x + renderScaleX * 0.5f,
			attribs.pos.y + renderScaleY * 0.5f,
			paint
		);
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
		if(!collided && other.attribs.type == EntityTypes.EntityType.GamePlayerChar && other.attribs.vel.y >= 0.0f){
			collided = true;
			ChangeColor(steppedOnColor);
			++GameData.score;

			currPopTime = maxPopTime;
		}
	}

	public static EntityPlat Create(final String key){
		EntityPlat result = new EntityPlat();
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	private void ChangeColor(final Color color){
		paint.setARGB((int)(color.a * 255.0f), (int)(color.r * 255.0f), (int)(color.g * 255.0f), (int)(color.b * 255.0f));
	}

	public float GetXOffset(){
		return xOffset;
	}

	public float GetXOffsetPrev(){
		return xOffsetPrev;
	}

	public Easing GetEasing(){
		return easing;
	}

	public void SetStrokeWidth(final float strokeWidth){
		this.strokeWidth = strokeWidth;
		paint.setStrokeWidth(strokeWidth);
	}

	public void SetPaintStyle(final Paint.Style paintStyle){
		this.paintStyle = paintStyle;
		paint.setStyle(paintStyle);
	}

	public void SetSteppedOnColor(final Color color){
		this.steppedOnColor = color;
	}

	public void SetMyIndex(final int myIndex){
		this.myIndex = myIndex;
	}

	public void SetXOffsetMag(final float xOffsetMag){
		this.xOffsetMag = xOffsetMag;
	}

	public void SetXOffsetSpd(final float xOffsetSpd){
		this.xOffsetSpd = xOffsetSpd;
	}

	public void SetEasing(final Easing easing){
		this.easing = easing;
	}

	private float strokeWidth;
	private Paint.Style paintStyle;
	private final Paint paint;
	private Color steppedOnColor;

	private boolean collided;
	private float elapsedTime;
	private int myIndex;

	private float currPopTime;
	private final float maxPopTime;
	private float renderScaleX;
	private float renderScaleY;

	private float xOffset;
	private float xOffsetPrev;
	private float xOffsetMag;
	private float xOffsetSpd;
	private Easing easing;
}