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
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.background.BackgroundManager;
import sg.diploma.product.background.BackgroundStatuses;
import sg.diploma.product.currency.CurrencyManager;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;

public final class ShopScreenActivity extends Activity implements View.OnTouchListener, IState{
	public ShopScreenActivity(){
		backButtonDownAnimSet = null;
		backButtonUpAnimSet = null;

		currencyText = null;
		currencyImg = null;

		shopHorizontalScrollView = null;
		shopLinearLayout = null;

		backButton = null;

		leftArrowIcon = null;
	}

	@SuppressLint("ClickableViewAccessibility")
	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.shop_screen_layout);

		CurrencyManager.Instance.LoadCurrencyData();
		BackgroundManager.Instance.LoadBackgroundData(Instance, "Backgrounds.ser");
		final ArrayList<BackgroundStatuses.BackgroundStatus> backgrounds = BackgroundManager.Instance.GetBackgrounds();
		final int backgroundsSize = backgrounds.size();

		final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
		final float textTranslationX = DeviceManager.screenWidthF * 0.5f;

		TextView shopText = findViewById(R.id.shopText);
		shopText.setTypeface(font);
		shopText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.18f / DeviceManager.scaledDensity);
		shopText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		shopText.setTranslationX(textTranslationX - (float)shopText.getMeasuredWidth() * 0.5f);
		shopText.setTranslationY(DeviceManager.screenHeightF * 0.05f);

		currencyText = findViewById(R.id.currencyText);
		currencyText.setTypeface(font);
		currencyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.1f / DeviceManager.scaledDensity);
		currencyText.setText(String.valueOf(CurrencyManager.Instance.GetAmtOfCoins()));
		currencyText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
		currencyText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		currencyText.setTranslationX(DeviceManager.screenWidthF * 0.75f - (float)currencyText.getMeasuredWidth() * 0.5f);
		currencyText.setTranslationY(DeviceManager.screenHeightF * 0.925f - (float)currencyText.getMeasuredHeight() * 0.5f);

		currencyImg = findViewById(R.id.currencyImg);
		currencyImg.getLayoutParams().width = currencyImg.getLayoutParams().height = (int)(DeviceManager.screenWidthF * 0.1f);
		currencyImg.setTranslationX(DeviceManager.screenWidthF * 0.9f - currencyImg.getLayoutParams().width * 0.5f);
		currencyImg.setTranslationY(DeviceManager.screenHeightF * 0.925f - currencyImg.getLayoutParams().height * 0.5f);

		final float buttonFactor = DeviceManager.screenWidthF * 0.25f / 300.0f;
		final int buttonSize = (int)(300.0f * buttonFactor * 0.7f);
		final float buttonTranslateY = DeviceManager.screenHeightF - (DeviceManager.screenWidthF - (DeviceManager.screenWidthF * 0.85f - buttonSize * 0.5f));

		backButton = findViewById(R.id.backButton);
		backButton.setOnTouchListener(this);
		backButton.getLayoutParams().width = buttonSize;
		backButton.getLayoutParams().height = buttonSize;
		backButton.setTranslationX(DeviceManager.screenWidthF * 0.15f - buttonSize * 0.5f);
		backButton.setTranslationY(buttonTranslateY);

		backButtonDownAnimSet = new AnimationSet(true);
		backButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, Animation.ABSOLUTE, backButton.getTranslationX() + buttonSize * 0.5f, Animation.ABSOLUTE, backButton.getTranslationY() + buttonSize * 0.5f));
		backButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		backButtonDownAnimSet.setDuration(400);
		backButtonDownAnimSet.setFillEnabled(true);
		backButtonDownAnimSet.setFillAfter(true);
		backButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

		backButtonUpAnimSet = new AnimationSet(true);
		backButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, Animation.ABSOLUTE, backButton.getTranslationX() + buttonSize * 0.5f, Animation.ABSOLUTE, backButton.getTranslationY() + buttonSize * 0.5f));
		backButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
		backButtonUpAnimSet.setDuration(400);
		backButtonUpAnimSet.setFillEnabled(true);
		backButtonUpAnimSet.setFillAfter(true);
		backButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

		leftArrowIcon = findViewById(R.id.leftArrowIcon);
		leftArrowIcon.getLayoutParams().width = (int)(buttonSize * 0.65f);
		leftArrowIcon.getLayoutParams().height = (int)(buttonSize * 0.65f);
		leftArrowIcon.setTranslationX(backButton.getTranslationX() + (backButton.getLayoutParams().width - leftArrowIcon.getLayoutParams().width) * 0.5f);
		leftArrowIcon.setTranslationY(backButton.getTranslationY() + (backButton.getLayoutParams().height - leftArrowIcon.getLayoutParams().height) * 0.5f);

		shopHorizontalScrollView = findViewById(R.id.shopHorizontalScrollView);
		shopHorizontalScrollView.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 0.65f);
		shopHorizontalScrollView.setTranslationY((backButton.getTranslationY() + backButton.getLayoutParams().height * 0.5f + (shopText.getTranslationY() + (float)shopText.getMeasuredHeight() * 0.5f)) * 0.5f - shopHorizontalScrollView.getLayoutParams().height * 0.5f);

		shopLinearLayout = findViewById(R.id.shopLinearLayout);
		final int amtOfChildren = shopLinearLayout.getChildCount();
		final int myHeight = shopHorizontalScrollView.getLayoutParams().height; //As only shopHorizontalScrollView is alr sized
		final int[] drawableIDs = {
			R.drawable.simple_place,
			R.drawable.cool_place,
			R.drawable.future_place,
			R.drawable.day_jp,
			R.drawable.night_jp,
			R.drawable.night_place,
			R.drawable.sunset0_place,
			R.drawable.sunset1_place
		};

		if(backgroundsSize > amtOfChildren){
			for(int i = amtOfChildren; i < backgroundsSize; ++i){
				final RelativeLayout shopItemRelativeLayout = new RelativeLayout(this);
				RelativeLayout.LayoutParams shopItemRelativeLayoutLayoutParams = new RelativeLayout.LayoutParams((int)(DeviceManager.screenWidthF * 0.75f), shopLinearLayout.getLayoutParams().height);
				shopItemRelativeLayout.setLayoutParams(shopItemRelativeLayoutLayoutParams);
				shopItemRelativeLayout.setBackgroundColor((i & 1) == 1 ? 0x77FF00FF : 0x77FFFF00);

				final ImageView shopItemImgView = new ImageView(this);
				shopItemImgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableIDs[i], null));
				shopItemImgView.setScaleType(ImageView.ScaleType.FIT_XY);
				shopItemImgView.setScaleX(0.95f);
				shopItemImgView.setScaleY(0.95f);
				shopItemRelativeLayout.addView(shopItemImgView);

				final TextView priceText = new TextView(this);
				priceText.setVisibility(View.INVISIBLE);
				priceText.setTypeface(font);
				priceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				priceText.setText(getString(R.string.CoinsPostfix, CurrencyManager.Instance.GetAmtOfCoins()));
				priceText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
				priceText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				priceText.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - (float)priceText.getMeasuredWidth() * 0.5f);
				priceText.setTranslationY(myHeight * 0.4f - (float)priceText.getMeasuredHeight() * 0.5f);
				shopItemRelativeLayout.addView(priceText);

				final Button buyButton = new Button(this);
				buyButton.setClickable(false);
				buyButton.setVisibility(View.INVISIBLE);
				buyButton.setText(getResources().getString(R.string.BuyButtonText));
				buyButton.setTextColor(0xFF000000);
				buyButton.setBackgroundColor(0xFF00FF00);
				buyButton.setClickable(true);
				buyButton.setTypeface(font);
				buyButton.setLayoutParams(new ViewGroup.LayoutParams(buttonSize * 3, buttonSize));
				buyButton.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - buyButton.getLayoutParams().width * 0.5f);
				buyButton.setTranslationY(myHeight * 0.6f - buyButton.getLayoutParams().height * 0.5f);
				buyButton.setGravity(Gravity.CENTER);
				buyButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				shopItemRelativeLayout.addView(buyButton);

				final AnimationSet buyButtonDownAnimSet = new AnimationSet(true);
				buyButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
					Animation.ABSOLUTE,
					buyButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					buyButton.getTranslationY() + buttonSize * 0.5f)
				);
				buyButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
				buyButtonDownAnimSet.setDuration(400);
				buyButtonDownAnimSet.setFillEnabled(true);
				buyButtonDownAnimSet.setFillAfter(true);
				buyButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

				final AnimationSet buyButtonUpAnimSet = new AnimationSet(true);
				buyButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
					Animation.ABSOLUTE,
					buyButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					buyButton.getTranslationY() + buttonSize * 0.5f)
				);
				buyButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
				buyButtonUpAnimSet.setDuration(400);
				buyButtonUpAnimSet.setFillEnabled(true);
				buyButtonUpAnimSet.setFillAfter(true);
				buyButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

				buyButton.setOnTouchListener((view, motionEvent)->{
					switch(motionEvent.getAction()){
						case MotionEvent.ACTION_DOWN:
							buyButton.startAnimation(buyButtonDownAnimSet);
							return true;
						case MotionEvent.ACTION_UP:
							buyButton.startAnimation(buyButtonUpAnimSet);
							AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

							//??
							return true;
					}
					return false;
				});

				final Button cancelButton = new Button(this);
				cancelButton.setClickable(false);
				cancelButton.setVisibility(View.INVISIBLE);
				cancelButton.setText(getResources().getString(R.string.CancelButtonText));
				cancelButton.setTextColor(0xFF000000);
				cancelButton.setBackgroundColor(0xFFFF0000);
				cancelButton.setClickable(true);
				cancelButton.setTypeface(font);
				cancelButton.setLayoutParams(new ViewGroup.LayoutParams(buttonSize * 3, buttonSize));
				cancelButton.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - cancelButton.getLayoutParams().width * 0.5f);
				cancelButton.setTranslationY(myHeight * 0.8f - cancelButton.getLayoutParams().height * 0.5f);
				cancelButton.setGravity(Gravity.CENTER);
				cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				shopItemRelativeLayout.addView(cancelButton);

				final AnimationSet cancelButtonDownAnimSet = new AnimationSet(true);
				cancelButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
					Animation.ABSOLUTE,
					cancelButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					cancelButton.getTranslationY() + buttonSize * 0.5f)
				);
				cancelButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
				cancelButtonDownAnimSet.setDuration(400);
				cancelButtonDownAnimSet.setFillEnabled(true);
				cancelButtonDownAnimSet.setFillAfter(true);
				cancelButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

				final AnimationSet cancelButtonUpAnimSet = new AnimationSet(true);
				cancelButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
					Animation.ABSOLUTE,
					cancelButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					cancelButton.getTranslationY() + buttonSize * 0.5f)
				);
				cancelButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
				cancelButtonUpAnimSet.setDuration(400);
				cancelButtonUpAnimSet.setAnimationListener(new Animation.AnimationListener(){
					@Override
					public void onAnimationStart(Animation animation){
					}

					@Override
					public void onAnimationEnd(Animation animation){
						shopItemImgView.setColorFilter(0x00000000);
						priceText.setVisibility(View.INVISIBLE);

						buyButton.setVisibility(View.INVISIBLE);
						buyButton.setClickable(false);
						cancelButton.setVisibility(View.INVISIBLE);
						cancelButton.setClickable(false);
					}

					@Override
					public void onAnimationRepeat(Animation animation){
					}
				});
				cancelButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

				cancelButton.setOnTouchListener((view, motionEvent)->{
					switch(motionEvent.getAction()){
						case MotionEvent.ACTION_DOWN:
							cancelButton.startAnimation(cancelButtonDownAnimSet);
							return true;
						case MotionEvent.ACTION_UP:
							cancelButton.startAnimation(cancelButtonUpAnimSet);
							AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

							return true;
					}
					return false;
				});

				shopItemRelativeLayout.setOnClickListener(view->{
					shopItemImgView.setColorFilter(0xAA000000);
					priceText.setVisibility(View.VISIBLE);

					buyButton.setVisibility(View.VISIBLE);
					buyButton.setClickable(true);
					cancelButton.setVisibility(View.VISIBLE);
					cancelButton.setClickable(true);
				});
				shopLinearLayout.addView(shopItemRelativeLayout);
			}
		}
	}

	@Override
	protected void onDestroy(){
		BackgroundManager.Instance.SaveBackgroundData(Instance, "Backgrounds.ser");
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
		if(view == backButton){
			switch(motionEvent.getAction()){
				case MotionEvent.ACTION_DOWN:
					backButton.startAnimation(backButtonDownAnimSet);
					return true;
				case MotionEvent.ACTION_UP:
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
		return "ShopScreen";
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

		startActivity(new Intent(this, MenuScreenActivity.class));
		finish();
	}

	private AnimationSet backButtonDownAnimSet;
	private AnimationSet backButtonUpAnimSet;

	private TextView currencyText;
	private ImageView currencyImg;

	private HorizontalScrollView shopHorizontalScrollView;
	private LinearLayout shopLinearLayout;

	private Button backButton;

	private ImageView leftArrowIcon;

	public static ShopScreenActivity Instance;

	static{
		Instance = null;
	}
}