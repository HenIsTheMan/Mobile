package sg.diploma.product.object_pooling;

import java.util.ArrayList;

import sg.diploma.product.BuildConfig;

public abstract class ObjPool<T>{
	public ObjPool(){
		activeObjs = null;
		inactiveObjs = null;
	}

	public void Init(final int size, Class<T> cls) throws InstantiationException, IllegalAccessException{
		activeObjs = new ArrayList<>();

		inactiveObjs = new ArrayList<>();
		for(int i = 0; i < size; ++i){
			inactiveObjs.add(cls.newInstance());
		}
	}

	T ActivateObj(){
		if(BuildConfig.DEBUG && inactiveObjs.size() != 0){
			throw new AssertionError("Assertion failed");
		}

		T obj = inactiveObjs.get(0);
		activeObjs.add(obj);
		inactiveObjs.remove(obj);
		return obj;
	}

	public void DeactivateObj(final T obj){
		if(BuildConfig.DEBUG && activeObjs.size() == 0){
			throw new AssertionError("Assertion failed");
		}

		if(activeObjs.remove(obj)){
			inactiveObjs.add(obj);
		}
	}

	private ArrayList<T> activeObjs;
	private ArrayList<T> inactiveObjs;
}