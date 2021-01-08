package sg.diploma.product.entity.entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.easing.Easing;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventAddScore;
import sg.diploma.product.graphics.Color;

public final class EntityPlat extends EntityAbstract{
	private EntityPlat(final EntityGamePlayerChar gamePlayerChar){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Plat;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		strokeWidth = 50.0f;
		paintStyle = Paint.Style.FILL;
		paint = new Paint();

		paint.setARGB(153, 51, 51, 51);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(paintStyle);

		collided = false;
		myIndex = 0;
		this.gamePlayerChar = gamePlayerChar;
		assert this.gamePlayerChar != null;

		currPopTime = 0.0f;
		maxPopTime = 0.4f;
		renderScaleX = 1.0f;
		renderScaleY = 1.0f;
	}

	@Override
	public void Update(final float dt){
		if(attribs.pos.y - gamePlayerChar.attribs.pos.y > DeviceManager.screenHeightF * 0.5f){ //0.25f if want exact
			EntityManager.Instance.SendEntityForRemoval("plat_" + myIndex);
		}

		if(currPopTime >= 0.0f){
			final float startScale = 1.0f;
			final float endScale = 1.2f;
			final float lerpFactor = Easing.EaseInOutCubic(currPopTime / maxPopTime);
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
		if(!collided && other.attribs.type == EntityTypes.EntityType.GamePlayerChar){
			collided = true;
			SetColor(new Color(1.0f, 1.0f, 0.0f, 1.0f));

			//Publisher.Broadcast(new EventSpawnPlat());
			Publisher.Broadcast(new EventAddScore(1));

			currPopTime = maxPopTime;
		}
	}

	public static EntityPlat Create(final String key, final EntityGamePlayerChar gamePlayerChar){
		EntityPlat result = new EntityPlat(gamePlayerChar);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetColor(final Color color){
		paint.setARGB((int)(color.a * 255.0f), (int)(color.r * 255.0f), (int)(color.g * 255.0f), (int)(color.b * 255.0f));
	}

	public void SetStrokeWidth(final float strokeWidth){
		this.strokeWidth = strokeWidth;
		paint.setStrokeWidth(strokeWidth);
	}

	public void SetPaintStyle(final Paint.Style paintStyle){
		this.paintStyle = paintStyle;
		paint.setStyle(paintStyle);
	}

	public void SetMyIndex(final int myIndex){
		this.myIndex = myIndex;
	}

	private float strokeWidth;
	private Paint.Style paintStyle;
	private final Paint paint;

	private boolean collided;
	private int myIndex;
	private final EntityGamePlayerChar gamePlayerChar;

	private float currPopTime;
	private final float maxPopTime;
	private float renderScaleX;
	private float renderScaleY;
}