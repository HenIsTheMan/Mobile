package sg.diploma.product.load_and_save;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.SurfaceView;

public class SharedPrefsManager{
	SharedPrefsManager(){
		view = null;
	}

	public void Init(final SurfaceView view){
		this.view = view;
	}

	public void Load(final String name){
		SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public void Save(final String name){
	}

	static SharedPrefsManager Instance;

	static{
		Instance = new SharedPrefsManager();
	}

	public SurfaceView view;
}
