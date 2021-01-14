package sg.diploma.product.activities;

import android.annotation.SuppressLint;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.common.collect.SortedSetMultimap;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.rankings.RankingsManager;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;

public final class RankingsScreenActivity extends Activity implements View.OnTouchListener, IState{
	public RankingsScreenActivity(){
		backButtonDownAnimSet = null;
		backButtonUpAnimSet = null;

		backButton = null;

		leftArrowIcon = null;

		rankingsScrollView = null;
		rankingsLinearLayout = null;
	}

	@SuppressLint("ClickableViewAccessibility")
	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.rankings_screen_layout);

		final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
		final float textTranslationX = DeviceManager.screenWidthF * 0.5f;

		final TextView rankingsText = findViewById(R.id.rankingsText);
		rankingsText.setTypeface(font);
		rankingsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.18f / DeviceManager.scaledDensity);
		rankingsText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		rankingsText.setTranslationX(textTranslationX - (float)rankingsText.getMeasuredWidth() * 0.5f);
		rankingsText.setTranslationY(DeviceManager.screenHeightF * 0.05f);

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

		leftArrowIcon = findViewById(R.id.leftArrowIcon);
		leftArrowIcon.getLayoutParams().width = (int)(buttonSize * 0.65f);
		leftArrowIcon.getLayoutParams().height = (int)(buttonSize * 0.65f);
		leftArrowIcon.setTranslationX(backButton.getTranslationX()
				+ (backButton.getLayoutParams().width
				- leftArrowIcon.getLayoutParams().width) * 0.5f);
		leftArrowIcon.setTranslationY(backButton.getTranslationY()
				+ (backButton.getLayoutParams().height
				- leftArrowIcon.getLayoutParams().height) * 0.5f);

		rankingsScrollView = findViewById(R.id.rankingsScrollView);
		rankingsScrollView.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 0.65f);
		rankingsScrollView.setTranslationY(
			(backButton.getTranslationY() + backButton.getLayoutParams().height * 0.5f
			+ (rankingsText.getTranslationY() + (float)rankingsText.getMeasuredHeight() * 0.5f))
			* 0.5f
			- rankingsScrollView.getLayoutParams().height * 0.5f
		);

		RankingsManager.Instance.LoadRankings(Instance, "Scores.ser", "Names.ser");
		SortedSetMultimap<Integer, String> rankings = RankingsManager.Instance.GetRankings();
		final int rankingsSize = rankings.size();

		rankingsLinearLayout = findViewById(R.id.rankingsLinearLayout);
		final int amtOfChildren = rankingsLinearLayout.getChildCount();

		if(rankingsSize > amtOfChildren){
			for(int i = amtOfChildren; i < rankingsSize; ++i){
				TextView textView = new TextView(this);
				textView.setTypeface(font);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
				rankingsLinearLayout.addView(textView);
			}
		}

		for(int i = 0; i < rankingsSize; ++i){
			TextView textView = (TextView)rankingsLinearLayout.getChildAt(rankingsSize - 1 - i);
			textView.setText(String.format("%s (%s)", rankings.values().toArray()[i], rankings.keys().toArray()[i]));
		}
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
		return "RankingsScreen";
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

		finish();
		startActivity(new Intent(this, MenuScreenActivity.class));
	}

	private AnimationSet backButtonDownAnimSet;
	private AnimationSet backButtonUpAnimSet;

	private Button backButton;

	private ImageView leftArrowIcon;

	private ScrollView rankingsScrollView;
	private LinearLayout rankingsLinearLayout;

	public static RankingsScreenActivity Instance;

	static{
		Instance = null;
	}
}