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
import sg.diploma.product.math.Pseudorand;

public final class EntityBall extends EntityAbstract{
	private EntityBall(){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.NormalBack;
		attribs.type = EntityTypes.EntityType.Ball;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		final int red = Pseudorand.PseudorandIntMinMax(0, 255);
		final int green = Pseudorand.PseudorandIntMinMax(0, 255);
		final int blue = Pseudorand.PseudorandIntMinMax(0, 255);
		color = new Color((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f, 1.0f);
		paint = new Paint();

		paint.setARGB(255, red, green, blue);
		paint.setStrokeWidth(50.0f);
		paint.setStyle(Paint.Style.FILL);

		vals = new float[]{0.0f, 0.0f, 0.0f};
	}

	@Override
	public void Update(final float dt){
		final float accelFactor = 20000.0f;
		attribs.accel.x = -vals[0] * accelFactor * dt;
		attribs.accel.y = vals[1] * accelFactor * dt;

		attribs.vel.x += attribs.accel.x * dt;
		attribs.vel.y += attribs.accel.y * dt;

		attribs.vel.x = Math.max(-2000.0f, Math.min(attribs.vel.x, 2000.0f));
		attribs.vel.y = Math.max(-2000.0f, Math.min(attribs.vel.y, 2000.0f));

		attribs.pos.x += attribs.vel.x * dt;
		attribs.pos.y += attribs.vel.y * dt;

		//* Bounds checking
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
		//*/
	}

	@Override
	public void Render(final Canvas canvas){
		canvas.drawCircle(
			attribs.pos.x,
			attribs.pos.y,
			attribs.scale.x * 0.5f,
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

	public void SetVals(final float[] vals){
		if((this.vals[0] > 0.0f && vals[0] < 0.0f) || (this.vals[0] < 0.0f && vals[0] > 0.0f)){
			attribs.vel.x = vals[0] < 0.0f ? 40.0f : -40.0f;
		}
		if((this.vals[1] > 0.0f && vals[1] < 0.0f) || (this.vals[1] < 0.0f && vals[1] > 0.0f)){
			attribs.vel.y = vals[1] < 0.0f ? -40.0f : 40.0f;
		}

		this.vals = vals;
	}

	private Color color;
	private final Paint paint;

	private float[] vals;
}
