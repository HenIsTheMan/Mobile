package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

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
	}

	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.options_screen_layout);

		final float buttonFactor = DeviceManager.screenWidthF * 0.1f / 300.0f;
		final int buttonSize = (int)(300.0f * buttonFactor);

		backButton = findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		backButton.getLayoutParams().width = buttonSize;
		backButton.getLayoutParams().height = buttonSize;
		backButton.setTranslationX(buttonSize * 0.5f);
		backButton.setTranslationY(buttonSize * 0.5f);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
		return true;
	}

	@Override
	public void onClick(View v){
		AudioManager.Instance.PlayAudio(R.raw.button_press, 5);
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

	public static OptionsScreenActivity Instance;

	static{
		Instance = null;
	}
}
