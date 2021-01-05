package sg.diploma.product.load_and_save;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.SurfaceView;

import java.util.HashMap;

public class SharedPrefsManager{
	SharedPrefsManager(){
		view = null;
	}

	public void Init(final SurfaceView view){ //??
		this.view = view;
	}

	public void Load(final String name){
		//SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(name, Context.MODE_APPEND);

		//                                                          float val = sharedPrefs.getFloat("name", "");
	}

	public void Save(final String name, final HashMap<String, Object> objs){
		SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPrefs.edit();
		Object sample = objs.values().stream().findFirst();
		final String[] keys = objs.keySet().toArray(new String[0]);

		if(sample instanceof Float){
			for(final String key: keys){
				editor.putFloat(key, (float)objs.get(key));
			}
			return;
		}

		if(sample instanceof String){
			for(final String key: keys){
				editor.putString(key, (String)objs.get(key));
			}
		}

		editor.apply();
	}

	static SharedPrefsManager Instance;

	static{
		Instance = new SharedPrefsManager();
	}

	public SurfaceView view;
}
