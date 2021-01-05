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

	public HashMap<String, Object> Load(final String name, final HashMap<String, Object> objs){
		HashMap<String, Object> all = new HashMap<>();

		SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
		final String[] keys = objs.keySet().toArray(new String[0]);

		for(final String key: keys){
			final Object val = objs.get(key);
			all.put(key, (Object)sharedPrefs.getFloat(key, 0.0f));
		}

		return all;
	}

	public void Save(final String name, final HashMap<String, Object> objs){
		SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPrefs.edit();
		final String[] keys = objs.keySet().toArray(new String[0]);

		for(final String key: keys){
			final Object val = objs.get(key);

			if(val instanceof Float){
				editor.putFloat(key, (float)val);
				return;
			}
			if(val instanceof String){
				editor.putString(key, (String)val);
			}
		}

		editor.apply();
	}

	public static SharedPrefsManager Instance;

	static{
		Instance = new SharedPrefsManager();
	}

	public SurfaceView view;
}
