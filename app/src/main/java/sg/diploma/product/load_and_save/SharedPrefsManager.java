package sg.diploma.product.load_and_save;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.SurfaceView;

public final class SharedPrefsManager{
	SharedPrefsManager(){
		view = null;
	}

	public void Init(final SurfaceView view){
		this.view = view;
	}

	public Float LoadDataFloat(final String name, final String key){
		SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
		if(!sharedPrefs.contains(key)){
			return null;
		}
		return sharedPrefs.getFloat(key, 0.0f);
	}

	public void SaveDataFloat(final String name, final String key, final float val){
		SharedPreferences.Editor editor = view.getContext().getSharedPreferences(name, Context.MODE_PRIVATE).edit();
		editor.putFloat(key, val);
		editor.apply();
	}

	public static SharedPrefsManager Instance;

	static{
		Instance = new SharedPrefsManager();
	}

	public SurfaceView view;
}
