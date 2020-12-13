package sg.diploma.product.device;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class DeviceManager{
	private DeviceManager(){
		final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		screenWidthF = (float)screenWidth;
		screenHeightF = (float)screenHeight;
	}

	static final DeviceManager Instance;

	static{
		Instance = new DeviceManager();
	}

	public float screenWidthF;
	public float screenHeightF;
	public int screenWidth;
	public int screenHeight;
}
