package sg.diploma.product.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.dialog_frags.SaveDialogFrag;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.IListener;
import sg.diploma.product.event.ListenerFlagsWrapper;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;

public final class OptionsScreenActivity
	extends FragmentActivity
	implements View.OnTouchListener, IState, SeekBar.OnSeekBarChangeListener, IListener{

	public OptionsScreenActivity(){
		areNewVolsSaved = true;
		initialMusicVol = 0;
		initialSoundVol = 0;

		backButtonDownAnimSet = null;
		backButtonUpAnimSet = null;
		saveButtonDownAnimSet = null;
		saveButtonUpAnimSet = null;
		resetButtonDownAnimSet = null;
		resetButtonUpAnimSet = null;

		backButton = null;
		saveButton = null;
		resetButton = null;

		leftArrowIcon = null;
		floppyDiskIcon = null;
		gearResetIcon = null;
	}

	@SuppressLint("ClickableViewAccessibility")
	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.options_screen_layout);

		Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.OptionsScreenActivity.GetVal(), this);

		AudioManager.Instance.LoadAudioVolData();

		final SeekBar seekBarMusic = findViewById(R.id.seekBarMusic);
		seekBarMusic.setOnSeekBarChangeListener(this);
		seekBarMusic.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 0.7f);
		seekBarMusic.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 0.1f);
		seekBarMusic.setTranslationX(DeviceManager.screenWidthF * 0.5f - seekBarMusic.getLayoutParams().width * 0.5f);
		seekBarMusic.setTranslationY(DeviceManager.screenHeightF * 0.4f - seekBarMusic.getLayoutParams().height * 0.5f);

		final SeekBar seekBarSounds = findViewById(R.id.seekBarSounds);
		seekBarSounds.setOnSeekBarChangeListener(this);
		seekBarSounds.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 0.7f);
		seekBarSounds.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 0.1f);
		seekBarSounds.setTranslationX(DeviceManager.screenWidthF * 0.5f - seekBarSounds.getLayoutParams().width * 0.5f);
		seekBarSounds.setTranslationY(DeviceManager.screenHeightF * 0.7f - seekBarSounds.getLayoutParams().height * 0.5f);

		final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
		final float smallTextSize = DeviceManager.screenWidthF * 0.1f / DeviceManager.scaledDensity;
		final float smallerTextSize = DeviceManager.screenWidthF * 0.05f / DeviceManager.scaledDensity;
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

		final float musicVolProgress = AudioManager.Instance.GetMusicVol() * 100.0f;
		final float musicVolMax = (float)seekBarMusic.getMax();
		final float musicVolPercentage = musicVolProgress / musicVolMax * 100.0f;
		initialMusicVol = (int)musicVolProgress;

		final TextView musicVolPercentageText = findViewById(R.id.musicVolPercentageText);
		musicVolPercentageText.setTypeface(font);
		musicVolPercentageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallerTextSize);
		musicVolPercentageText.setText(getString(R.string.PercentPostfix, musicVolPercentage));
		musicVolPercentageText.setTranslationY(DeviceManager.screenHeightF * 0.45f);
		seekBarMusic.setProgress((int)musicVolProgress);

		final float soundVolProgress = AudioManager.Instance.GetSoundVol() * 100.0f;
		final float soundVolMax = seekBarSounds.getMax();
		final float soundVolPercentage = soundVolProgress / soundVolMax * 100.0f;
		initialSoundVol = (int)soundVolProgress;

		final TextView soundVolPercentageText = findViewById(R.id.soundVolPercentageText);
		soundVolPercentageText.setTypeface(font);
		soundVolPercentageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallerTextSize);
		soundVolPercentageText.setText(getString(R.string.PercentPostfix, soundVolPercentage));
		soundVolPercentageText.setTranslationY(DeviceManager.screenHeightF * 0.75f);
		seekBarSounds.setProgress((int)soundVolProgress);

		//* Hack
		musicVolPercentageText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		musicVolPercentageText.setTranslationX(
			seekBarMusic.getX()
			+ seekBarMusic.getLayoutParams().width / 100.0f * (float)seekBarMusic.getProgress()
			- musicVolPercentageText.getMeasuredWidth() * 0.5f
		);

		soundVolPercentageText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		soundVolPercentageText.setTranslationX(
			seekBarSounds.getX()
			+ seekBarSounds.getLayoutParams().width / 100.0f * (float)seekBarSounds.getProgress()
			- soundVolPercentageText.getMeasuredWidth() * 0.5f
		);
		//*/

		final float buttonFactor = DeviceManager.screenWidthF * 0.25f / 300.0f;
		final int buttonSize = (int)(300.0f * buttonFactor * 0.7f);
		final float buttonTranslateY = DeviceManager.screenHeightF
				- (DeviceManager.screenWidthF - (DeviceManager.screenWidthF * 0.85f - buttonSize * 0.5f));

		backButton = findViewById(R.id.backButton);
		backButton.setOnTouchListener(this);
		backButton.getLayoutParams().width = buttonSize;
		backButton.getLayoutParams().height = buttonSize;
		backButton.setTranslationX(DeviceManager.screenWidthF * 0.15f - buttonSize * 0.5f);
		backButton.setTranslationY(buttonTranslateY);

		backButtonDownAnimSet = new AnimationSet(true);
		backButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
				Animation.ABSOLUTE, backButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, backButton.getTranslationY() + buttonSize * 0.5f));
		backButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		backButtonDownAnimSet.setDuration(400);
		backButtonDownAnimSet.setFillEnabled(true);
		backButtonDownAnimSet.setFillAfter(true);
		backButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

		backButtonUpAnimSet = new AnimationSet(true);
		backButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
				Animation.ABSOLUTE, backButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, backButton.getTranslationY() + buttonSize * 0.5f));
		backButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
		backButtonUpAnimSet.setDuration(400);
		backButtonUpAnimSet.setFillEnabled(true);
		backButtonUpAnimSet.setFillAfter(true);
		backButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

		saveButton = findViewById(R.id.saveButton);
		saveButton.setOnTouchListener(this);
		saveButton.getLayoutParams().width = buttonSize;
		saveButton.getLayoutParams().height = buttonSize;
		saveButton.setTranslationX(DeviceManager.screenWidthF * 0.5f - buttonSize * 0.5f);
		saveButton.setTranslationY(buttonTranslateY);

		saveButtonDownAnimSet = new AnimationSet(true);
		saveButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
				Animation.ABSOLUTE, saveButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, saveButton.getTranslationY() + buttonSize * 0.5f));
		saveButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		saveButtonDownAnimSet.setDuration(400);
		saveButtonDownAnimSet.setFillEnabled(true);
		saveButtonDownAnimSet.setFillAfter(true);
		saveButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

		saveButtonUpAnimSet = new AnimationSet(true);
		saveButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
				Animation.ABSOLUTE, saveButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, saveButton.getTranslationY() + buttonSize * 0.5f));
		saveButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
		saveButtonUpAnimSet.setDuration(400);
		saveButtonUpAnimSet.setAnimationListener(new Animation.AnimationListener(){
			@Override
			public void onAnimationStart(Animation anim){
			}

			@Override
			public void onAnimationRepeat(Animation anim){
			}

			@Override
			public void onAnimationEnd(Animation anim){
				saveButton.setVisibility(View.INVISIBLE);
				floppyDiskIcon.setVisibility(View.INVISIBLE);
			}
		});
		saveButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

		resetButton = findViewById(R.id.resetButton);
		resetButton.setOnTouchListener(this);
		resetButton.getLayoutParams().width = buttonSize;
		resetButton.getLayoutParams().height = buttonSize;
		resetButton.setTranslationX(DeviceManager.screenWidthF * 0.85f - buttonSize * 0.5f);
		resetButton.setTranslationY(buttonTranslateY);

		resetButtonDownAnimSet = new AnimationSet(true);
		resetButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
				Animation.ABSOLUTE, resetButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, resetButton.getTranslationY() + buttonSize * 0.5f));
		resetButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		resetButtonDownAnimSet.setDuration(400);
		resetButtonDownAnimSet.setFillEnabled(true);
		resetButtonDownAnimSet.setFillAfter(true);
		resetButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

		resetButtonUpAnimSet = new AnimationSet(true);
		resetButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
				Animation.ABSOLUTE, resetButton.getTranslationX() + buttonSize * 0.5f,
				Animation.ABSOLUTE, resetButton.getTranslationY() + buttonSize * 0.5f));
		resetButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
		resetButtonUpAnimSet.setDuration(400);
		resetButtonUpAnimSet.setFillEnabled(true);
		resetButtonUpAnimSet.setFillAfter(true);
		resetButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

		leftArrowIcon = findViewById(R.id.leftArrowIcon);
		leftArrowIcon.getLayoutParams().width = (int)(buttonSize * 0.65f);
		leftArrowIcon.getLayoutParams().height = (int)(buttonSize * 0.65f);
		leftArrowIcon.setTranslationX(backButton.getTranslationX()
				+ (backButton.getLayoutParams().width
				- leftArrowIcon.getLayoutParams().width) * 0.5f);
		leftArrowIcon.setTranslationY(backButton.getTranslationY()
				+ (backButton.getLayoutParams().height
				- leftArrowIcon.getLayoutParams().height) * 0.5f);

		floppyDiskIcon = findViewById(R.id.floppyDiskIcon);
		floppyDiskIcon.getLayoutParams().width = (int)(buttonSize * 0.6f);
		floppyDiskIcon.getLayoutParams().height = (int)(buttonSize * 0.6f);
		floppyDiskIcon.setTranslationX(saveButton.getTranslationX()
				+ (saveButton.getLayoutParams().width
				- floppyDiskIcon.getLayoutParams().width) * 0.5f);
		floppyDiskIcon.setTranslationY(saveButton.getTranslationY()
				+ (saveButton.getLayoutParams().height
				- floppyDiskIcon.getLayoutParams().height) * 0.5f);

		gearResetIcon = findViewById(R.id.gearResetIcon);
		gearResetIcon.getLayoutParams().width = (int)(buttonSize * 0.75f);
		gearResetIcon.getLayoutParams().height = (int)(buttonSize * 0.75f);
		gearResetIcon.setTranslationX(resetButton.getTranslationX()
				+ (resetButton.getLayoutParams().width
				- gearResetIcon.getLayoutParams().width) * 0.5f);
		gearResetIcon.setTranslationY(resetButton.getTranslationY()
				+ (resetButton.getLayoutParams().height
				- gearResetIcon.getLayoutParams().height) * 0.5f);
	}

	@Override
	protected void onStop(){
		super.onStop();
		Publisher.RemoveListener(ListenerFlagsWrapper.ListenerFlags.OptionsScreenActivity.GetVal());
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event){
		TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent){
		if(view == backButton){
			switch(motionEvent.getAction()){
				case MotionEvent.ACTION_DOWN:
					backButton.clearAnimation();
					backButton.startAnimation(backButtonDownAnimSet);

					return true;
				case MotionEvent.ACTION_UP:
					backButton.clearAnimation();
					backButton.startAnimation(backButtonUpAnimSet);
					AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

					if(!areNewVolsSaved){
						if(SaveDialogFrag.isShown){
							return false;
						}

						SaveDialogFrag dialogFrag = new SaveDialogFrag();
						dialogFrag.show(getSupportFragmentManager(), "SaveDialogFrag");
						return true;
					}

					ReturnToMenu();
					return true;
			}
			return false;
		}
		if(view == saveButton){
			if(saveButton.isClickable()){
				switch(motionEvent.getAction()){
					case MotionEvent.ACTION_DOWN:
						saveButton.clearAnimation();
						saveButton.startAnimation(saveButtonDownAnimSet);

						return true;
					case MotionEvent.ACTION_UP:
						saveButton.clearAnimation();
						saveButton.startAnimation(saveButtonUpAnimSet);
						AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

						AudioManager.Instance.SaveAudioVolData();
						areNewVolsSaved = true;
						saveButton.setClickable(false);

						return true;
				}
			}
			return false;
		}
		if(view == resetButton){
			switch(motionEvent.getAction()){
				case MotionEvent.ACTION_DOWN:
					resetButton.clearAnimation();
					resetButton.startAnimation(resetButtonDownAnimSet);

					return true;
				case MotionEvent.ACTION_UP:
					resetButton.clearAnimation();
					resetButton.startAnimation(resetButtonUpAnimSet);
					AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

					final SeekBar seekBarMusic = findViewById(R.id.seekBarMusic);
					final SeekBar seekBarSounds = findViewById(R.id.seekBarSounds);
					seekBarMusic.setProgress(100);
					seekBarSounds.setProgress(100);

					areNewVolsSaved = false;
					saveButton.setClickable(true);
					saveButton.setVisibility(View.VISIBLE);
					floppyDiskIcon.setVisibility(View.VISIBLE);

					return true;
			}
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
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
		if(fromUser){
			areNewVolsSaved = false;
			saveButton.setClickable(true);
			saveButton.setVisibility(View.VISIBLE);
			floppyDiskIcon.setVisibility(View.VISIBLE);
		}

		final float percentage = (float)progress / (float)seekBar.getMax() * 100.0f;
		final String seekBarTag = (String)seekBar.getTag();

		if(seekBarTag.equals("seekBarMusicTag")){
			final TextView musicVolPercentageText = findViewById(R.id.musicVolPercentageText);
			musicVolPercentageText.setText(getString(R.string.PercentPostfix, percentage));

			final Rect bounds = seekBar.getThumb().getBounds();
			musicVolPercentageText.setTranslationX(seekBar.getX()
				- musicVolPercentageText.getWidth() * 0.5f
				- seekBar.getThumbOffset()
				+ bounds.exactCenterX()
			);

			AudioManager.Instance.OnMusicVolChanged(percentage / 100.0f);
			return;
		}
		if(seekBarTag.equals("seekBarSoundsTag")){
			final TextView soundVolPercentageText = findViewById(R.id.soundVolPercentageText);
			soundVolPercentageText.setText(getString(R.string.PercentPostfix, percentage));

			final Rect bounds = seekBar.getThumb().getBounds();
			soundVolPercentageText.setTranslationX(seekBar.getX()
				- soundVolPercentageText.getWidth() * 0.5f
				- seekBar.getThumbOffset()
				+ bounds.exactCenterX()
			);

			AudioManager.Instance.OnSoundVolChanged(percentage / 100.0f);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar){
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar){
	}

	@Override
	public void OnEvent(@NonNull EventAbstract event){
		switch(event.GetID()){
			case ReturnToMenuWithoutSaving:
				final SeekBar seekBarMusic = findViewById(R.id.seekBarMusic);
				final SeekBar seekBarSounds = findViewById(R.id.seekBarSounds);
				seekBarMusic.setProgress(initialMusicVol);
				seekBarSounds.setProgress(initialSoundVol);

				ReturnToMenu();
				break;
		}
	}

	private void ReturnToMenu(){
		EntityManager.Instance.SendAllEntitiesForRemoval();
		StateManager.Instance.ChangeState("MenuScreen");

		finish();
		startActivity(new Intent(this, MenuScreenActivity.class));
	}

	private boolean areNewVolsSaved;
	private int initialMusicVol;
	private int initialSoundVol;

	private AnimationSet backButtonDownAnimSet;
	private AnimationSet backButtonUpAnimSet;
	private AnimationSet saveButtonDownAnimSet;
	private AnimationSet saveButtonUpAnimSet;
	private AnimationSet resetButtonDownAnimSet;
	private AnimationSet resetButtonUpAnimSet;

	private Button backButton;
	private Button saveButton;
	private Button resetButton;

	private ImageView leftArrowIcon;
	private ImageView floppyDiskIcon;
	private ImageView gearResetIcon;

	public static OptionsScreenActivity Instance;

	static{
		Instance = null;
	}
}