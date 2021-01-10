package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
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
	private EntityPauseButton(final int notPausedBitmapID, final int pausedBitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.UI;
		attribs.type = EntityTypes.EntityType.PauseButton;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		paused = false;
		BT = 0.0f;
		elapsedTime = 0.0f;

		notPausedBitmap = ResourceManager.Instance.GetBitmap(notPausedBitmapID, Bitmap.Config.RGB_565);
		pausedBitmap = ResourceManager.Instance.GetBitmap(pausedBitmapID, Bitmap.Config.RGB_565);
	}

	@Override
	public void Update(float _dt){
		elapsedTime += _dt;

		if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()
			&& DetectCollision.CircleCircle(new Vector2(
				TouchManager.Instance.GetXPos(),
				TouchManager.Instance.GetYPos()),
				attribs.pos,
				0.0f,
				attribs.scale.x * 0.5f
			)
		){
			TouchManager.Instance.SetMotionEventAction(-999);

			if(BT <= elapsedTime){
				paused = !paused;

				AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);
				GameManager.Instance.SetIsPaused(paused);

				BT = elapsedTime + 0.2f;
			}
		}
	}

	@Override
	public void Render(Canvas _canvas){
	}

	@Override
	public void Collided(EntityAbstract other){
	}

	@Override
	public void LateUpdate(final float dt){
	}

	@Override
	public void SpecialRender(final Canvas canvas){
		if(paused){
			Rect src = new Rect(0, 0, pausedBitmap.getWidth(), pausedBitmap.getHeight());
			RectF dst = new RectF(
				attribs.pos.x - attribs.scale.x * 0.5f,
				attribs.pos.y - attribs.scale.y * 0.5f,
				attribs.pos.x + attribs.scale.x * 0.5f,
				attribs.pos.y + attribs.scale.y * 0.5f
			);
			canvas.drawBitmap(pausedBitmap, src, dst, null);
		} else{
			Rect src = new Rect(0, 0, notPausedBitmap.getWidth(), notPausedBitmap.getHeight());
			RectF dst = new RectF(
				attribs.pos.x - attribs.scale.x * 0.5f,
				attribs.pos.y - attribs.scale.y * 0.5f,
				attribs.pos.x + attribs.scale.x * 0.5f,
				attribs.pos.y + attribs.scale.y * 0.5f
			);
			canvas.drawBitmap(notPausedBitmap, src, dst, null);
		}
	}

	public static EntityPauseButton Create(final String key, final int notPausedBitmapID, final int pausedBitmapID){
		EntityPauseButton result = new EntityPauseButton(notPausedBitmapID, pausedBitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	private boolean paused;
	private float BT;
	private float elapsedTime;

	private final Bitmap notPausedBitmap;
	private final Bitmap pausedBitmap;
}