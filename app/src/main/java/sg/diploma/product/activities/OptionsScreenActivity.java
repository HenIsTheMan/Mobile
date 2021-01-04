package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;

public final class OptionsScreenActivity extends Activity implements View.OnClickListener, IState{
	public OptionsScreenActivity(){
		backButton = null;
		seekBarMusic = null;
		seekBarSounds = null;
	}

	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.options_screen_layout);

		final int relativePadding = (int)(DeviceManager.screenWidthF * 0.05f);

		seekBarMusic = findViewById(R.id.seekBarMusic);
		seekBarMusic.setProgress(50);
		seekBarMusic.setPaddingRelative(relativePadding, 0, relativePadding, 0);
		seekBarMusic.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 0.7f);
		seekBarMusic.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 0.1f);
		seekBarMusic.setTranslationX(DeviceManager.screenWidthF * 0.5f - seekBarMusic.getLayoutParams().width * 0.5f);
		seekBarMusic.setTranslationY(DeviceManager.screenHeightF * 0.4f - seekBarMusic.getLayoutParams().height * 0.5f);

		seekBarSounds = findViewById(R.id.seekBarSounds);
		seekBarSounds.setProgress(50);
		seekBarSounds.setPaddingRelative(relativePadding, 0, relativePadding, 0);
		seekBarSounds.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 0.7f);
		seekBarSounds.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 0.1f);
		seekBarSounds.setTranslationX(DeviceManager.screenWidthF * 0.5f - seekBarSounds.getLayoutParams().width * 0.5f);
		seekBarSounds.setTranslationY(DeviceManager.screenHeightF * 0.7f - seekBarSounds.getLayoutParams().height * 0.5f);

		final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
		final float smallTextSize = DeviceManager.screenWidthF * 0.1f / DeviceManager.scaledDensity;
		final float textTranslationX = DeviceManager.screenWidthF * 0.5f;

		TextView optionsText = findViewById(R.id.optionsText);
		optionsText.setTypeface(font);
		optionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.18f / DeviceManager.scaledDensity);
		optionsText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		optionsText.setTranslationX(textTranslationX - (float)optionsText.getMeasuredWidth() * 0.5f);
		optionsText.setTranslationY(DeviceManager.screenHeightF * 0.05f);

		TextView musicVolText = findViewById(R.id.musicVolText);
		musicVolText.setTypeface(font);
		musicVolText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallTextSize);
		musicVolText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		musicVolText.setTranslationX(textTranslationX - (float)musicVolText.getMeasuredWidth() * 0.5f);
		musicVolText.setTranslationY(DeviceManager.screenHeightF * 0.28f);

		TextView soundVolText = findViewById(R.id.soundVolText);
		soundVolText.setTypeface(font);
		soundVolText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallTextSize);
		soundVolText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		soundVolText.setTranslationX(textTranslationX - (float)soundVolText.getMeasuredWidth() * 0.5f);
		soundVolText.setTranslationY(DeviceManager.screenHeightF * 0.58f);

		final float buttonFactor = DeviceManager.screenWidthF * 0.1f / 300.0f;
		final int buttonSize = (int)(300.0f * buttonFactor);

		backButton = findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		backButton.getLayoutParams().width = buttonSize;
		backButton.getLayoutParams().height = buttonSize;
		backButton.setTranslationX((float)buttonSize);
		backButton.setTranslationY(DeviceManager.screenHeightF - (float)buttonSize * 2.0f);

/*		final int musicProgress = seekBarMusic.getProgress();
		final int musicMax = seekBarMusic.getMax();

		final int soundProgress = seekBarMusic.getProgress();
		final int soundMax = seekBarMusic.getMax();*/
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
		return true;
	}

	@Override
	public void onClick(View v){
		AudioManager.Instance.PlayAudio(R.raw.button_press, 5.0f);
		if(v == backButton){
			EntityManager.Instance.SendAllEntitiesForRemoval();
			StateManager.Instance.ChangeState("MenuScreen");

			startActivity(new Intent(this, MenuScreenActivity.class));
		}
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
		return "OptionsScreen";
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

	@Override
	protected void onPause(){
		super.onPause();
	}

	@Override
	protected void onStop(){
		super.onStop();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	private Button backButton;
	private SeekBar seekBarMusic;
	private SeekBar seekBarSounds;

	public static OptionsScreenActivity Instance;

	static{
		Instance = null;
	}
}
