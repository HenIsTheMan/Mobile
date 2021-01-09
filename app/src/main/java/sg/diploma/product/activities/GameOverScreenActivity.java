package sg.diploma.product.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;

public final class GameOverScreenActivity extends Activity implements View.OnTouchListener, IState{
	public GameOverScreenActivity(){
		continueButtonDownAnimSet = null;
		continueButtonUpAnimSet = null;

		continueButton = null;
	}

	@SuppressLint("ClickableViewAccessibility")
	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.game_over_screen_layout);

		final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");

		TextView gameOverText0 = findViewById(R.id.gameOverText0);
		gameOverText0.setTypeface(font);
		gameOverText0.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.18f / DeviceManager.scaledDensity);
		gameOverText0.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		gameOverText0.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)gameOverText0.getMeasuredWidth() * 0.5f);
		gameOverText0.setTranslationY(DeviceManager.screenHeightF * 0.05f);

		TextView gameOverText1 = findViewById(R.id.gameOverText1);
		gameOverText1.setTypeface(font);
		gameOverText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.18f / DeviceManager.scaledDensity);
		gameOverText1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		gameOverText1.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)gameOverText1.getMeasuredWidth() * 0.5f);
		gameOverText1.setTranslationY(DeviceManager.screenHeightF * 0.15f);

		final float buttonFactor = DeviceManager.screenWidthF * 0.25f / 300.0f;
		final int buttonSize = (int)(300.0f * buttonFactor * 0.7f);
		final float buttonTranslateY = DeviceManager.screenHeightF
				- (DeviceManager.screenWidthF - (DeviceManager.screenWidthF * 0.85f - buttonSize * 0.5f));

		continueButton = findViewById(R.id.continueButton);
		continueButton.setOnTouchListener(this);
		continueButton.setTypeface(font);
		continueButton.getLayoutParams().width = buttonSize * 3;
		continueButton.getLayoutParams().height = buttonSize;
		continueButton.setTranslationX(DeviceManager.screenWidthF * 0.5f - continueButton.getLayoutParams().width * 0.5f);
		continueButton.setTranslationY(buttonTranslateY);
		continueButton.setTextSize(buttonSize * 0.15f);

		continueButtonDownAnimSet = new AnimationSet(true);
		continueButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
				Animation.ABSOLUTE, continueButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, continueButton.getTranslationY() + buttonSize * 0.5f));
		continueButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		continueButtonDownAnimSet.setDuration(400);
		continueButtonDownAnimSet.setFillEnabled(true);
		continueButtonDownAnimSet.setFillAfter(true);
		continueButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

		continueButtonUpAnimSet = new AnimationSet(true);
		continueButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
				Animation.ABSOLUTE, continueButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, continueButton.getTranslationY() + buttonSize * 0.5f));
		continueButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
		continueButtonUpAnimSet.setDuration(400);
		continueButtonUpAnimSet.setFillEnabled(true);
		continueButtonUpAnimSet.setFillAfter(true);
		continueButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent){
		if(view == continueButton){
			switch(motionEvent.getAction()){
				case MotionEvent.ACTION_DOWN:
					continueButton.startAnimation(continueButtonDownAnimSet);
					return true;
				case MotionEvent.ACTION_UP:
					continueButton.startAnimation(continueButtonUpAnimSet);
					AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

					ReturnToMenu();
					return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public void onBackPressed(){
		//Do nth
	}

	@Override
	public void Update(float _dt){
		EntityManager.Instance.Update(_dt);
	}

	@Override
	public String GetName(){
		return "GameOverScreen";
	}

	@Override
	public void OnEnter(SurfaceView _view){
	}

	@Override
	public void OnExit(){
		Instance.finish();
	}

	@Override
	public void Render(Canvas _canvas){
		EntityManager.Instance.Render(_canvas);
	}

	private void ReturnToMenu(){
		EntityManager.Instance.SendAllEntitiesForRemoval();
		StateManager.Instance.ChangeState("MenuScreen");

		//startActivity(new Intent(this, MenuScreenActivity.class));
		finish();
	}

	private AnimationSet continueButtonDownAnimSet;
	private AnimationSet continueButtonUpAnimSet;

	private Button continueButton;

	public static GameOverScreenActivity Instance;

	static{
		Instance = null;
	}
}