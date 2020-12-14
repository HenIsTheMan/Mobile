package sg.diploma.product.entity.entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventAddScore;
import sg.diploma.product.event.events.EventSpawnPlat;
import sg.diploma.product.graphics.Color;

public final class EntityPlat extends EntityAbstract{
	private EntityPlat(final EntityGamePlayerChar gamePlayerChar){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Plat;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		color = new Color(0.2f, 0.2f, 0.2f, 0.6f);
		strokeWidth = 50.0f;
		paintStyle = Paint.Style.FILL;
		paint = new Paint();

		paint.setARGB(153, 51, 51, 51);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(paintStyle);

		steppedOn = false;
		myIndex = 0;
		this.gamePlayerChar = gamePlayerChar;
		assert this.gamePlayerChar != null;
	}

	@Override
	public void Update(final float dt){
		/*if(steppedOn && attribs.pos.y - gamePlayerChar.attribs.pos.y < 50.0f){
			Publisher.Broadcast(new EventEndGame());
			return;
		}*/

		if(attribs.pos.y - gamePlayerChar.attribs.pos.y > DeviceManager.screenHeightF * 0.25f){
			EntityManager.Instance.SendEntityForRemoval("plat_" + myIndex);
		}
	}

	@Override
	public void Render(final Canvas canvas){
		canvas.drawRect(
			attribs.pos.x - attribs.scale.x * 0.5f,
			attribs.pos.y - attribs.scale.y * 0.5f,
			attribs.pos.x + attribs.scale.x * 0.5f,
			attribs.pos.y + attribs.scale.y * 0.5f,
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
		if(!steppedOn){
			steppedOn = true;
			SetColor(new Color(1.0f, 1.0f, 0.0f, 1.0f));
			Publisher.Broadcast(new EventSpawnPlat());
			Publisher.Broadcast(new EventAddScore(1));
		}
	}

	public static EntityPlat Create(final String key, final EntityGamePlayerChar gamePlayerChar){
		EntityPlat result = new EntityPlat(gamePlayerChar);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetColor(final Color color){
		this.color = color;
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

	private Color color;
	private float strokeWidth;
	private Paint.Style paintStyle;
	private final Paint paint;

	private boolean steppedOn;
	private int myIndex;
	private final EntityGamePlayerChar gamePlayerChar;
}