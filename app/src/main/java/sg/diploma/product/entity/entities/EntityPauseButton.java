package sg.diploma.product.entity.entities;

/*
public final class EntityPauseButton extends EntityAbstract{
	public EntityPauseButton(final int bitmapID){
		paused = false;
		bitmap = ResourceManager.Instance.GetBitmap(bitmapID, Bitmap.Config.RGB_565);
	}

	@Override
	public void Update(float _dt){
		if(TouchManager.Instance.GetMotionEventAction() == MotionEvent.I ){
			if (TouchManager.Instance.IsDown() && !Paused) {   // Check touch collision here
				float imgRadius = scaledbmpP.getHeight() * 0.5f;

				if (Collision.SphereToSphere(TouchManager.Instance.GetPosX(), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, imgRadius)) {
					Paused = true; // Meant user had pressed the Pause button!!!

					// When button is pressed, U can play an audio clip
					// AudioManager.Instance.PlayAudio(R.raw.clicksound);

					// If just want a pause without the (popup dialog --> No done yet.)
					// Method already written in your GameSystem class from Week 5
					GameSystem.Instance.SetIsPaused(!GameSystem.Instance.GetIsPaused());
				}
			}
		} else{
			paused = false;
		}
	}

	@Override
	public void Render(Canvas _canvas){
		*/
/*if (Paused == false)
			_canvas.drawBitmap(scaledbmpP, xPos - scaledbmpP.getWidth() * 0.5f, yPos - scaledbmpP.getHeight() * 0.5f, null);
		else*//*

		//_canvas.drawBitmap(scaledbmpUP, xPos - scaledbmpUP.getWidth() * 0.5f, yPos - scaledbmpUP.getHeight() * 0.5f, null);
	}

	public static EntityPauseButton Create(final String key, final int bitmapID){
		EntityPauseButton result = new EntityPauseButton(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	private Bitmap bitmap;
	private boolean paused;
}*/