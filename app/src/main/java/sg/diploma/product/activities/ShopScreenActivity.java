package sg.diploma.product.activities;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.state.IState;
import sg.diploma.product.touch.TouchManager;

public final class ShopScreenActivity extends Activity implements View.OnClickListener, IState{
	public ShopScreenActivity(){
	}

	@RequiresApi(api = Build.VERSION_CODES.P)
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.shop_screen_layout);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
		return true;
	}

	@Override
	public final void onClick(View v){
		/*AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);
		if(v == backButton){
			EntityManager.Instance.SendAllEntitiesForRemoval();
			AudioManager.Instance.SaveAudioVolData();
			StateManager.Instance.ChangeState("MenuScreen");

			startActivity(new Intent(this, MenuScreenActivity.class));
			finish();
		}*/
	}

	@Override
	public final void onBackPressed(){
		//Do nth
	}

	@Override
	public final void Update(float _dt){
		EntityManager.Instance.Update(_dt);
	}

	@Override
	public String GetName(){
		return "ShopScreen";
	}

	@Override
	public final void OnEnter(SurfaceView _view){
	}

	@Override
	public final void OnExit(){
		Instance.finish();
	}

	@Override
	public final void Render(Canvas _canvas){
		EntityManager.Instance.Render(_canvas);
	}

	public static ShopScreenActivity Instance;

	static{
		Instance = null;
	}
}
