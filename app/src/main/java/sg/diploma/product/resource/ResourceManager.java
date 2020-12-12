package sg.diploma.product.resource;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

import java.util.HashMap;

public final class ResourceManager{
	private ResourceManager(){
		res = null;
		resMap = new HashMap<Integer, Bitmap>();
	}

	public void Init(SurfaceView _view){
		res = _view.getResources();
	}

	public Bitmap GetBitmap(int _id){
		if(resMap.containsKey(_id)){
			return resMap.get(_id);
		}

		Bitmap result = BitmapFactory.decodeResource(res, _id);
		resMap.put(_id, result);
		return result;
	}

	private Resources res;
	private final HashMap<Integer, Bitmap> resMap;

	public static final ResourceManager Instance;

	static{
		Instance = new ResourceManager();
	}
}