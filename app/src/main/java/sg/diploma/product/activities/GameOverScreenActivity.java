package sg.diploma.product.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.currency.CurrencyManager;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.game.GameData;
import sg.diploma.product.rankings.RankingsManager;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;

public final class GameOverScreenActivity extends Activity implements View.OnTouchListener, IState{
	public GameOverScreenActivity(){
		continueButtonDownAnimSet = null;
		continueButtonUpAnimSet = null;

		continueButton = null;
		finalScoreText = null;
		finalScoreVal = null;
		saveScoreCheckBox = null;
		nameTextInputBox = null;
	}

	@SuppressLint("ClickableViewAccessibility")
	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.game_over_screen_layout);

		RankingsManager.Instance.LoadRankings(Instance, "Scores.ser", "Names.ser");

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
		continueButton.setGravity(Gravity.CENTER);
		continueButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);

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

		finalScoreText = findViewById(R.id.finalScoreText);
		finalScoreText.setTextColor(0xFFFF00FF);
		finalScoreText.setTypeface(font);
		finalScoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.1f / DeviceManager.scaledDensity);
		finalScoreText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		finalScoreText.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)finalScoreText.getMeasuredWidth() * 0.5f);
		finalScoreText.setTranslationY(DeviceManager.screenHeightF * 0.35f);

		finalScoreVal = findViewById(R.id.finalScoreVal);
		finalScoreVal.setText(String.valueOf(GameData.score));
		finalScoreVal.setTextColor(0xFFFF00FF);
		finalScoreVal.setTypeface(font);
		finalScoreVal.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.2f / DeviceManager.scaledDensity);
		finalScoreVal.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		finalScoreVal.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)finalScoreVal.getMeasuredWidth() * 0.5f);
		finalScoreVal.setTranslationY(DeviceManager.screenHeightF * 0.45f);

		saveScoreCheckBox = findViewById(R.id.saveScoreCheckBox);
		saveScoreCheckBox.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		saveScoreCheckBox.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)saveScoreCheckBox.getMeasuredWidth() * 0.5f);
		saveScoreCheckBox.setTranslationY(DeviceManager.screenHeightF * 0.65f);

		nameTextInputBox = findViewById(R.id.nameTextInputBox);
		nameTextInputBox.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		nameTextInputBox.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 0.75f);
		nameTextInputBox.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)nameTextInputBox.getLayoutParams().width * 0.5f);
		nameTextInputBox.setTranslationY(DeviceManager.screenHeightF * 0.75f);
	}

	@Override
	protected void onStop(){
		if(saveScoreCheckBox.isChecked()){
			final String name = nameTextInputBox.getText().toString();
			RankingsManager.Instance.AddRanking(GameData.score, name.equals("") ? nameTextInputBox.getHint().toString() : name);
			RankingsManager.Instance.SaveRankings(Instance, "Scores.ser", "Names.ser");
		}
		super.onStop();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event){
		TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent){
		if(view == continueButton){
			switch(motionEvent.getAction()){
				case MotionEvent.ACTION_DOWN:
					continueButton.clearAnimation();
					continueButton.startAnimation(continueButtonDownAnimSet);

					return true;
				case MotionEvent.ACTION_UP:
					continueButton.clearAnimation();
					continueButton.startAnimation(continueButtonUpAnimSet);
					AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

					if(saveScoreCheckBox.isChecked()){
						final String name = nameTextInputBox.getText().toString();
						RankingsManager.Instance.AddRanking(GameData.score, name.equals("") ? nameTextInputBox.getHint().toString() : name);
						RankingsManager.Instance.SaveRankings(Instance, "Scores.ser", "Names.ser");
					}
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
		CurrencyManager.Instance.SetAmtOfCoins(CurrencyManager.Instance.GetAmtOfCoins() + GameData.collectedCoins);

		GameData.globalInstance.ResetVars();
		EntityManager.Instance.SendAllEntitiesForRemoval();
		StateManager.Instance.ChangeState("MenuScreen");

		startActivity(new Intent(this, MenuScreenActivity.class));
		finish();
	}

	private AnimationSet continueButtonDownAnimSet;
	private AnimationSet continueButtonUpAnimSet;

	private Button continueButton;
	private TextView finalScoreText;
	private TextView finalScoreVal;
	private CheckBox saveScoreCheckBox;
	private EditText nameTextInputBox;

	public static GameOverScreenActivity Instance;

	static{
		Instance = null;
	}
}