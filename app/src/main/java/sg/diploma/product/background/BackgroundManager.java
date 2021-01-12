package sg.diploma.product.background;

import android.content.Context;
import android.view.SurfaceView;

public final class BackgroundManager{
	private BackgroundManager(){
		view = null;
		BGs = null;
	}

	public void Init(SurfaceView _view){
		view = _view;
	}

	public void LoadBackgroundData(final Context context, final String backgroundsFileName){
	}

	public void SaveBackgroundData(final Context context, final String backgroundsFileName){
	}

	private SurfaceView view;

	private BackgroundStatuses.BackgroundStatus[] BGs;

	public final static BackgroundManager Instance;

	static{
		Instance = new BackgroundManager();
	}
}