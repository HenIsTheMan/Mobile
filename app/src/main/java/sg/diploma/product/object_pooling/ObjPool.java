package sg.diploma.product.object_pooling;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.Callable;

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
	}

	public void Init(final int size, Callable<T> func) throws Exception{ //Command SDP
		activeObjs = new ArrayList<>();

		inactiveObjs = new ArrayList<>();
		for(int i = 0; i < size; ++i){
			inactiveObjs.add(func.call());
		}
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

	public ArrayList<T> RetrieveAtiveObjs(){
		return activeObjs;
	}

	private ArrayList<T> activeObjs;
	private ArrayList<T> inactiveObjs;
}