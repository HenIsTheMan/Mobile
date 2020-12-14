package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.game.GameManager;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.math.DetectCollision;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class EntityPauseButton extends EntityAbstract{
	public EntityPauseButton(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.UI;
		attribs.type = EntityTypes.EntityType.PauseButton;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		paused = false;
		bitmap = ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565);
	}

	@Override
	public void Update(float _dt){
		if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()
			&& DetectCollision.CircleCircle(new Vector2(
				TouchManager.Instance.GetXPos(),
				TouchManager.Instance.GetYPos()),
				attribs.pos,
				0.0f,
				attribs.scale.x * 0.5f
			)
		){
			paused = !paused;
			AudioManager.Instance.PlayAudio(R.raw.button_press, 5);
			GameManager.Instance.SetIsPaused(paused);
		}
	}

	@Override
	public void Render(Canvas _canvas){
		assert bitmap != null;
		_canvas.drawBitmap(bitmap, attribs.pos.x - attribs.scale.x * 0.5f, attribs.pos.y - attribs.scale.y * 0.5f, null);
	}

	@Override
	public void Collided(EntityAbstract other){
	}

	@Override
	public void LateUpdate(final float dt){
	}

	@Override
	public void SpecialRender(final Canvas canvas){
		assert bitmap != null;

		Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF dst = new RectF(attribs.pos.x, attribs.pos.y, attribs.pos.x + attribs.scale.x, attribs.pos.y + attribs.scale.y);
		canvas.drawBitmap(bitmap, src, dst, null);
	}

	public static EntityPauseButton Create(final String key, final int bitmapID){
		EntityPauseButton result = new EntityPauseButton(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	private final Bitmap bitmap;
	private boolean paused;
}