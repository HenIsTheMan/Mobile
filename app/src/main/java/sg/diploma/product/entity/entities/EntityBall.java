package sg.diploma.product.entity.entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.Color;

public final class EntityBall extends EntityAbstract{
	private EntityBall(){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Ball;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		color = new Color(0.2f, 0.2f, 0.2f, 0.6f);
		paint = new Paint();

		paint.setARGB(153, 51, 51, 51);
		paint.setStrokeWidth(50.0f);
		paint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void Update(final float dt){
	}

	@Override
	public void Render(final Canvas canvas){
		canvas.drawCircle(
			attribs.pos.x,
			attribs.pos.y,
			attribs.scale.x,
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
	}

	public static EntityBall Create(final String key){
		EntityBall result = new EntityBall();
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetColor(final Color color){
		this.color = color;
		paint.setARGB((int)(color.a * 255.0f), (int)(color.r * 255.0f), (int)(color.g * 255.0f), (int)(color.b * 255.0f));
	}

	private Color color;
	private final Paint paint;
}
