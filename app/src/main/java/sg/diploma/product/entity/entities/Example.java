package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.resource.ResourceManager;

public final class Example extends EntityAbstract{
	public Example(){
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.BG;
		attribs.type = EntityTypes.EntityType.BG;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;
		bmp = ResourceManager.Instance.GetBitmap(R.mipmap.ic_launcher_foreground);
	}

	@Override
	public void Update(float dt){
		/*Random ranGen = new Random();
		//_view.getWidth(); -- will give the length of view
		xPos = ranGen.nextFloat() * _view.getWidth();
		yPos = ranGen.nextFloat() * _view.getHeight();

		// Not used but u can use them if u want
		xDir = ranGen.nextFloat() * 100.0f - 50.0f;
		yDir = ranGen.nextFloat() * 100.0f - 50.0f;*/

		/*lifeTime -= _dt;
		if (lifeTime < 0.0f)
			SetIsDone(true);*/
	}

	@Override
	public void Render(Canvas canvas){ //Render with img centered
		assert bmp != null;
		canvas.drawBitmap(bmp, attribs.pos.x - bmp.getWidth() * 0.5f, attribs.pos.y - bmp.getHeight() * 0.5f, null);
	}

	public static Example Create(){
		Example result = new Example();
		EntityManager.Instance.AddEntity(result);
		return result;
	}

	private final Bitmap bmp;
}