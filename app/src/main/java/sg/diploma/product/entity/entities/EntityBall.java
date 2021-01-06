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
import sg.diploma.product.graphics.Color;
import sg.diploma.product.math.Pseudorand;

public final class EntityBall extends EntityAbstract{
	private EntityBall(){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
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
		final float screenWidthF = DeviceManager.screenWidthF;
		final float screenHeightF = DeviceManager.screenHeightF;

		final float moveFactor = 100.0f;
		attribs.pos.x += -vals[0] * moveFactor * dt;
		attribs.pos.y += vals[1] * moveFactor * dt;

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
		this.vals = vals;
	}

	private Color color;
	private final Paint paint;

	private float[] vals;
}
