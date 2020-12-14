package sg.diploma.product.entity.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import sg.diploma.product.BuildConfig;
import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.game.GameManager;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.math.DetectCollision;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class EntityPauseButton extends EntityAbstract{
	public EntityPauseButton(final int bitmapID){
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
			paused = true;
			AudioManager.Instance.PlayAudio(R.raw.button_press, 5);
			GameManager.Instance.SetIsPaused(paused);
		} else{
			paused = false;
		}
	}

	@Override
	public void Render(Canvas _canvas){
		/*if (Paused == false)
			_canvas.drawBitmap(scaledbmpP, xPos - scaledbmpP.getWidth() * 0.5f, yPos - scaledbmpP.getHeight() * 0.5f, null);
		else*/

		//_canvas.drawBitmap(scaledbmpUP, xPos - scaledbmpUP.getWidth() * 0.5f, yPos - scaledbmpUP.getHeight() * 0.5f, null);
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

	public static EntityPauseButton Create(final String key, final int bitmapID){
		EntityPauseButton result = new EntityPauseButton(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	private final Bitmap bitmap;
	private boolean paused;
}