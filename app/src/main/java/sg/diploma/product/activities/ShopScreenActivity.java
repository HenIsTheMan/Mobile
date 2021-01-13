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
		backButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
			Animation.ABSOLUTE,
			backButton.getTranslationX() + buttonSize * 0.5f,
			Animation.ABSOLUTE,
			backButton.getTranslationY() + buttonSize * 0.5f)
		);
		backButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		backButtonDownAnimSet.setDuration(400);
		backButtonDownAnimSet.setFillEnabled(true);
		backButtonDownAnimSet.setFillAfter(true);
		backButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

		backButtonUpAnimSet = new AnimationSet(true);
		backButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
			Animation.ABSOLUTE,
			backButton.getTranslationX() + buttonSize * 0.5f,
			Animation.ABSOLUTE,
			backButton.getTranslationY() + buttonSize * 0.5f
		));
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
		shopHorizontalScrollView.setTranslationY(
			(backButton.getTranslationY() + backButton.getLayoutParams().height * 0.5f
			+ (shopText.getTranslationY() + (float)shopText.getMeasuredHeight() * 0.5f)) * 0.5f
			- shopHorizontalScrollView.getLayoutParams().height * 0.5f
		);

		shopLinearLayout = findViewById(R.id.shopLinearLayout);
		final int amtOfChildren = shopLinearLayout.getChildCount();
		final int myHeight = shopHorizontalScrollView.getLayoutParams().height; //As only shopHorizontalScrollView is alr sized
		final ImageView[] shopItemImgViews = new ImageView[backgroundsSize];
		final TextView[] labelTexts = new TextView[backgroundsSize];
		final Button[] topButtons =  new Button[backgroundsSize];
		final Button[] bottomButtons = new Button[backgroundsSize];

		if(backgroundsSize > amtOfChildren){
			for(int i = amtOfChildren; i < backgroundsSize; ++i){
				final RelativeLayout shopItemRelativeLayout = new RelativeLayout(this);
				RelativeLayout.LayoutParams shopItemRelativeLayoutLayoutParams = new RelativeLayout.LayoutParams((int)(DeviceManager.screenWidthF * 0.75f), shopLinearLayout.getLayoutParams().height);
				shopItemRelativeLayout.setLayoutParams(shopItemRelativeLayoutLayoutParams);
				shopItemRelativeLayout.setBackgroundColor((i & 1) == 1 ? 0x77FF00FF : 0x77FFFF00);

				final ImageView shopItemImgView = new ImageView(this);
				shopItemImgViews[i] = shopItemImgView;
				shopItemImgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), BackgroundManager.Instance.drawableIDs[i], null));
				shopItemImgView.setScaleType(ImageView.ScaleType.FIT_XY);
				shopItemImgView.setScaleX(0.95f);
				shopItemImgView.setScaleY(0.95f);
				shopItemRelativeLayout.addView(shopItemImgView);

				final BackgroundStatuses.BackgroundStatus backgroundStatus = backgrounds.get(i);

				final TextView labelText = new TextView(this);
				labelTexts[i] = labelText;
				labelText.setVisibility(View.INVISIBLE);
				labelText.setTypeface(font);
				labelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				switch(backgroundStatus){
					case Equipped:
						labelText.setText(getString(R.string.EquippedText));
						break;
					case NotEquipped:
						labelText.setText(getString(R.string.NotEquippedText));
						break;
					case NotOwned:
						labelText.setText(getString(R.string.CoinsPostfix, BackgroundManager.Instance.prices[i]));
						break;
				}
				labelText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
				labelText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				labelText.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - (float)labelText.getMeasuredWidth() * 0.5f);
				labelText.setTranslationY(myHeight * 0.4f - (float)labelText.getMeasuredHeight() * 0.5f);
				labelText.setOnClickListener(null);
				shopItemRelativeLayout.addView(labelText);

				final int index = i;

				final Button topButton = new Button(this);
				topButtons[i] = topButton;
				topButton.setClickable(false);
				topButton.setVisibility(View.INVISIBLE);
				switch(backgroundStatus){
					case Equipped:
						topButton.setText(getResources().getString(R.string.UnequipButtonText));
						topButton.setBackgroundColor(0xFFFF0000);
						break;
					case NotEquipped:
						topButton.setText(getResources().getString(R.string.EquipButtonText));
						topButton.setBackgroundColor(0xFF00FF00);
						break;
					case NotOwned:
						topButton.setText(getResources().getString(R.string.BuyButtonText));
						topButton.setBackgroundColor(0xFF00FF00);
						break;
				}
				topButton.setTextColor(0xFF000000);
				topButton.setTypeface(font);
				topButton.setLayoutParams(new ViewGroup.LayoutParams(buttonSize * 3, buttonSize));
				topButton.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - topButton.getLayoutParams().width * 0.5f);
				topButton.setTranslationY(myHeight * 0.6f - topButton.getLayoutParams().height * 0.5f);
				topButton.setGravity(Gravity.CENTER);
				topButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				shopItemRelativeLayout.addView(topButton);

				final AnimationSet topButtonDownAnimSet = new AnimationSet(true);
				topButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
					Animation.ABSOLUTE,
					topButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					topButton.getTranslationY() + buttonSize * 0.5f)
				);
				topButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
				topButtonDownAnimSet.setDuration(400);
				topButtonDownAnimSet.setFillEnabled(true);
				topButtonDownAnimSet.setFillAfter(true);
				topButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

				final AnimationSet topButtonUpAnimSet = new AnimationSet(true);
				topButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
					Animation.ABSOLUTE,
					topButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					topButton.getTranslationY() + buttonSize * 0.5f)
				);
				topButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
				topButtonUpAnimSet.setDuration(400);
				topButtonUpAnimSet.setFillEnabled(true);
				topButtonUpAnimSet.setFillAfter(true);
				topButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

				final Button bottomButton = new Button(this);
				bottomButtons[i] = bottomButton;
				bottomButton.setClickable(false);
				bottomButton.setVisibility(View.INVISIBLE);
				bottomButton.setText(getResources().getString(backgroundStatus == BackgroundStatuses.BackgroundStatus.NotOwned
						? R.string.CancelButtonText
						: R.string.BackButtonText
				));
				bottomButton.setTextColor(0xFF000000);
				bottomButton.setBackgroundColor(backgroundStatus == BackgroundStatuses.BackgroundStatus.NotOwned
					? 0xFFFF0000
					: getResources().getColor(R.color.Gray, null)
				);
				bottomButton.setTypeface(font);
				bottomButton.setLayoutParams(new ViewGroup.LayoutParams(buttonSize * 3, buttonSize));
				bottomButton.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - bottomButton.getLayoutParams().width * 0.5f);
				bottomButton.setTranslationY(myHeight * 0.8f - bottomButton.getLayoutParams().height * 0.5f);
				bottomButton.setGravity(Gravity.CENTER);
				bottomButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, DeviceManager.screenWidthF * 0.08f / DeviceManager.scaledDensity);
				shopItemRelativeLayout.addView(bottomButton);

				final AnimationSet bottomButtonDownAnimSet = new AnimationSet(true);
				bottomButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
					Animation.ABSOLUTE,
					bottomButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					bottomButton.getTranslationY() + buttonSize * 0.5f)
				);
				bottomButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
				bottomButtonDownAnimSet.setDuration(400);
				bottomButtonDownAnimSet.setFillEnabled(true);
				bottomButtonDownAnimSet.setFillAfter(true);
				bottomButtonDownAnimSet.setInterpolator(this, R.anim.my_accelerate_interpolator);

				final AnimationSet bottomButtonUpAnimSet = new AnimationSet(true);
				bottomButtonUpAnimSet.addAnimation(new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
					Animation.ABSOLUTE,
					bottomButton.getTranslationX() + buttonSize * 0.5f,
					Animation.ABSOLUTE,
					bottomButton.getTranslationY() + buttonSize * 0.5f)
				);
				bottomButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
				bottomButtonUpAnimSet.setDuration(400);
				bottomButtonUpAnimSet.setAnimationListener(new Animation.AnimationListener(){
					@Override
					public void onAnimationStart(Animation animation){
					}

					@Override
					public void onAnimationEnd(Animation animation){
						shopItemImgView.setColorFilter(0x00000000);
						labelText.setVisibility(View.INVISIBLE);

						topButton.clearAnimation();
						topButton.setVisibility(View.INVISIBLE);
						topButton.setClickable(false);
						bottomButton.setVisibility(View.INVISIBLE);
						bottomButton.setClickable(false);
					}

					@Override
					public void onAnimationRepeat(Animation animation){
					}
				});
				bottomButtonUpAnimSet.setInterpolator(this, R.anim.my_decelerate_interpolator);

				final TextView currencyTextCopy = currencyText;
				topButton.setOnTouchListener((view, motionEvent)->{
					switch(motionEvent.getAction()){
						case MotionEvent.ACTION_DOWN:
							topButton.clearAnimation();
							topButton.startAnimation(topButtonDownAnimSet);

							return true;
						case MotionEvent.ACTION_UP:
							topButton.clearAnimation();
							topButton.startAnimation(topButtonUpAnimSet);
							AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

							BackgroundStatuses.BackgroundStatus myBackgroundStatus = backgrounds.get(index);
							switch(myBackgroundStatus){
								case NotOwned:
									int amtOfCoins = CurrencyManager.Instance.GetAmtOfCoins();
									final int price = BackgroundManager.Instance.prices[index];
									if(amtOfCoins >= price){
										backgrounds.set(index, BackgroundStatuses.BackgroundStatus.NotEquipped);
										amtOfCoins -= price;
										CurrencyManager.Instance.SetAmtOfCoins(amtOfCoins);
										currencyTextCopy.setText(String.valueOf(amtOfCoins));

										labelText.setText(getString(R.string.NotEquippedText));
										labelText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
										labelText.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - (float)labelText.getMeasuredWidth() * 0.5f);

										topButton.setText(R.string.EquipButtonText);
										topButton.setBackgroundColor(0xFF00FF00);

										bottomButton.setText(getResources().getString(R.string.BackButtonText));
										bottomButton.setBackgroundColor(getResources().getColor(R.color.Gray, null));
									}

									break;
								case NotEquipped:
									backgrounds.set(index, BackgroundStatuses.BackgroundStatus.Equipped);

									labelText.setText(getString(R.string.EquippedText));
									labelText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
									labelText.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - (float)labelText.getMeasuredWidth() * 0.5f);

									topButton.setText(R.string.UnequipButtonText);
									topButton.setBackgroundColor(0xFFFF0000);

									for(int j = 0; j < backgroundsSize; ++j){
										if(j != index && backgrounds.get(j) == BackgroundStatuses.BackgroundStatus.Equipped){
											backgrounds.set(j, BackgroundStatuses.BackgroundStatus.NotEquipped);

											labelTexts[j].setText(getString(R.string.NotEquippedText));
											labelTexts[j].measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
											labelTexts[j].setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - (float)labelTexts[j].getMeasuredWidth() * 0.5f);

											topButtons[j].setText(R.string.EquipButtonText);
											topButtons[j].setBackgroundColor(0xFF00FF00);

											break;
										}
									}

									break;
								case Equipped:
									backgrounds.set(index, BackgroundStatuses.BackgroundStatus.NotEquipped);

									labelText.setText(getString(R.string.NotEquippedText));
									labelText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
									labelText.setTranslationX(shopItemRelativeLayoutLayoutParams.width * 0.5f - (float)labelText.getMeasuredWidth() * 0.5f);

									topButton.setText(R.string.EquipButtonText);
									topButton.setBackgroundColor(0xFF00FF00);

									break;
							}

							return true;
					}
					return false;
				});
				bottomButton.setOnTouchListener((view, motionEvent)->{
					switch(motionEvent.getAction()){
						case MotionEvent.ACTION_DOWN:
							bottomButton.clearAnimation();
							bottomButton.startAnimation(bottomButtonDownAnimSet);

							return true;
						case MotionEvent.ACTION_UP:
							bottomButton.clearAnimation();
							bottomButton.startAnimation(bottomButtonUpAnimSet);
							AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

							return true;
					}
					return false;
				});

				shopItemRelativeLayout.setOnClickListener(view->{
					shopItemImgView.setColorFilter(0xAA000000);
					labelText.setVisibility(View.VISIBLE);

					topButton.setVisibility(View.VISIBLE);
					topButton.setClickable(true);
					bottomButton.setVisibility(View.VISIBLE);
					bottomButton.setClickable(true);

					for(int j = 0; j < backgroundsSize; ++j){
						if(j != index){
							shopItemImgViews[j].setColorFilter(0x00000000);
							labelTexts[j].setVisibility(View.INVISIBLE);

							topButtons[j].clearAnimation();
							topButtons[j].setVisibility(View.INVISIBLE);
							topButtons[j].setClickable(false);
							bottomButtons[j].setVisibility(View.INVISIBLE);
							bottomButtons[j].setClickable(false);
						}
					}
				});
				shopLinearLayout.addView(shopItemRelativeLayout);
			}
		}
	}

	@Override
	protected void onStop(){
		CurrencyManager.Instance.SaveCurrencyData();
		BackgroundManager.Instance.SaveBackgroundData(Instance, "Backgrounds.ser");
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