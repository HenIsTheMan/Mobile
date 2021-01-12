package sg.diploma.product.background;

import android.content.Context;
import android.os.Environment;
import android.view.SurfaceView;

import java.io.File;
import java.util.Objects;

public final class BackgroundManager{
	private BackgroundManager(){
		view = null;
		BGs = null;
	}

	public void Init(SurfaceView _view){
		view = _view;
	}

	public void LoadBackgroundData(final Context context, final String backgroundsFileName){
	}

	/** @noinspection ResultOfMethodCallIgnored*/
	public void SaveBackgroundData(final Context context, final String backgroundsFileName){
		final String state = Environment.getExternalStorageState();
		if(!Environment.MEDIA_MOUNTED.equals(state)){
			return;
		}

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator + "scores");
		if(dir.exists()){
			ClearDirectory(dir);
		}
		dir.mkdirs();

		/*final File scoresFile = new File(dir, scoresFileName);
		final File namesFile = new File(dir, namesFileName);

		FileOutputStream scoresFOS = null;
		ObjectOutputStream scoresOOS = null;
		FileOutputStream namesFOS = null;
		ObjectOutputStream namesOOS = null;

		try{
			scoresFOS = new FileOutputStream(scoresFile);
			scoresOOS = new ObjectOutputStream(scoresFOS);
			scoresOOS.writeObject(rankings.keys().toArray());

			namesFOS = new FileOutputStream(namesFile);
			namesOOS = new ObjectOutputStream(namesFOS);
			namesOOS.writeObject(rankings.values().toArray());
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			try{
				if(scoresOOS != null){
					scoresOOS.close();
				}
				if(scoresFOS != null){
					scoresFOS.close();
				}
				if(namesOOS != null){
					namesOOS.close();
				}
				if(namesFOS != null){
					namesFOS.close();
				}
			} catch(IOException e){
				e.printStackTrace();
			}
		}*/
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

	private SurfaceView view;

	private BackgroundStatuses.BackgroundStatus[] BGs;

	public final static BackgroundManager Instance;

	static{
		Instance = new BackgroundManager();
	}
}