package sg.diploma.product.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

import java.util.HashMap;

public final class ResourceManager{
	private ResourceManager(){
		res = null;
		resMap = new HashMap<Integer, Bitmap>();

		options = new BitmapFactory.Options();
	}

	public void Init(final SurfaceView _view){
		res = _view.getResources();
	}

	public Bitmap GetBitmap(final int _id, final Bitmap.Config config){
		if(resMap.containsKey(_id)){
			return resMap.get(_id);
		}

		options.inPreferredConfig = config;

		Bitmap result = BitmapFactory.decodeResource(res, _id, options);
		resMap.put(_id, result);
		return result;
	}

	private Resources res;
	private final HashMap<Integer, Bitmap> resMap;
	private final BitmapFactory.Options options;

	public static final ResourceManager Instance;

	static{
		Instance = new ResourceManager();
	}
}