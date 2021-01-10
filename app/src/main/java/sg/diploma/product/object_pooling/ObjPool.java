package sg.diploma.product.object_pooling;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public abstract class ObjPool<T>{
	public ObjPool(){
		activeObjs = null;
		inactiveObjs = null;
	}

	public void Init(final int size, @NonNull final Class<T> cls) throws InstantiationException, IllegalAccessException{
		activeObjs = new ArrayList<>();

		inactiveObjs = new ArrayList<>();
		for(int i = 0; i < size; ++i){
			inactiveObjs.add(cls.newInstance());
		}

		android.util.Log.e("me000", String.valueOf(inactiveObjs.size()));
	}

	public T ActivateObj(){
		final T obj = inactiveObjs.get(0);
		activeObjs.add(obj);
		inactiveObjs.remove(obj);
		return obj;
	}

	public void DeactivateObj(final T obj){
		if(activeObjs.remove(obj)){
			inactiveObjs.add(obj);
		}
	}

	private ArrayList<T> activeObjs;
	private ArrayList<T> inactiveObjs;
}