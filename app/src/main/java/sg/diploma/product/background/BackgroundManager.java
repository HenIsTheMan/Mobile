package sg.diploma.product.background;

import android.content.Context;
import android.os.Environment;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public final class BackgroundManager{
	private BackgroundManager(){
		view = null;

		final int amtOfBackgrounds = 8;
		backgrounds = new ArrayList<>(amtOfBackgrounds);
		for(int i = 0; i < amtOfBackgrounds; ++i){
			backgrounds.add(BackgroundStatuses.BackgroundStatus.NotOwned);
		}
	}

	public void Init(SurfaceView _view){
		view = _view;
	}

	public void LoadBackgroundData(final Context context, final String backgroundsFileName){
		FileInputStream backgroundsFIS = null;
		ObjectInputStream backgroundsOIS = null;

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator + "backgrounds");
		final File backgroundsFile = new File(dir, backgroundsFileName);

		try{
			backgroundsFIS = new FileInputStream(backgroundsFile);
			backgroundsOIS = new ObjectInputStream(backgroundsFIS);

			final Object[] backgroundsObjArr = (Object[])backgroundsOIS.readObject();
			backgrounds.clear();
			for(final Object obj: backgroundsObjArr){
				backgrounds.add((BackgroundStatuses.BackgroundStatus)obj);
			}
		} catch(ClassNotFoundException | IOException e){
			e.printStackTrace();
		} finally{
			try{
				if(backgroundsOIS != null){
					backgroundsOIS.close();
				}
				if(backgroundsFIS != null){
					backgroundsFIS.close();
				}
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	/** @noinspection ResultOfMethodCallIgnored*/
	public void SaveBackgroundData(final Context context, final String backgroundsFileName){
		final String state = Environment.getExternalStorageState();
		if(!Environment.MEDIA_MOUNTED.equals(state)){
			return;
		}

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator + "backgrounds");
		if(dir.exists()){
			ClearDirectory(dir);
		}
		dir.mkdirs();

		final File backgroundsFile = new File(dir, backgroundsFileName);
		FileOutputStream backgroundsFOS = null;
		ObjectOutputStream backgroundsOOS = null;

		try{
			backgroundsFOS = new FileOutputStream(backgroundsFile);
			backgroundsOOS = new ObjectOutputStream(backgroundsFOS);
			backgroundsOOS.writeObject(backgrounds.toArray());
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			try{
				if(backgroundsOOS != null){
					backgroundsOOS.close();
				}
				if(backgroundsFOS != null){
					backgroundsFOS.close();
				}
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	/** @noinspection ResultOfMethodCallIgnored*/
	private void ClearDirectory(final File file){
		if(file.isDirectory()){
			for(File child: Objects.requireNonNull(file.listFiles())){
				ClearDirectory(child);
			}
		}
		file.delete();
	}

	public ArrayList<BackgroundStatuses.BackgroundStatus> GetBackgrounds(){
		return backgrounds;
	}

	private SurfaceView view;

	private final ArrayList<BackgroundStatuses.BackgroundStatus> backgrounds; //Shld be named "backgroundStatuses" instead

	public final static BackgroundManager Instance;

	static{
		Instance = new BackgroundManager();
	}
}