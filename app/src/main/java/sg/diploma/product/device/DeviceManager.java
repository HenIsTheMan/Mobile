package sg.diploma.product.device;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class DeviceManager{
	private DeviceManager(){
		final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

		scaledDensity = displayMetrics.scaledDensity;
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		screenWidthF = (float)screenWidth;
		screenHeightF = (float)screenHeight;
	}

	static final DeviceManager Instance;

	static{
		Instance = new DeviceManager();
	}

	public static float scaledDensity;
	public static float screenWidthF;
	public static float screenHeightF;
	public static int screenWidth;
	public static int screenHeight;
}
