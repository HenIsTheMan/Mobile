package sg.diploma.product.rankings;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RankingsManager{
	RankingsManager(){
		rankings = TreeMultimap.create(Ordering.arbitrary(), Ordering.arbitrary());
	}

	public void LoadRankings(final Context context, final String name){
		/*FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = context.openFileInput(name);
			ois = new ObjectInputStream(fis);

			rankings = (TreeMultimap<Integer, String>)ois.readObject();
		} catch(IOException e){
			android.util.Log.e("me000", "HERE");
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			android.util.Log.e("me010", "HERE");
		} finally{
			try{
				if(ois != null){
					ois.close();
				}
				if(fis != null){
					fis.close();
				}
			} catch(IOException e){
				android.util.Log.e("me111", "HERE");
			}
		}*/
	}

	public void SaveRankings(final Context context, final String name){
		String state = Environment.getExternalStorageState();
		if(!Environment.MEDIA_MOUNTED.equals(state)){
			Log.e("no", "HERE");
			return;
		}


/*		// create a File object for the output file
		File outputFile = new File(wallpaperDirectory, filename);
		// now attach the OutputStream to the file object, instead of a String representation
		FileOutputStream fos = new FileOutputStream(outputFile);*/

		String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		File dir = new File(pathToAppFolder + File.separator);
		dir.mkdirs();
		File file = new File(dir, name);

		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try{
			fos = new FileOutputStream(file);
			os = new ObjectOutputStream(fos);

			//os.writeObject(rankings.keys());
			os.writeObject(rankings.values().toArray());
		} catch(FileNotFoundException e){
			Log.e("me222", "HERE");
			e.printStackTrace();
		} catch(IOException e){
			Log.e("me333", "HERE");
			e.printStackTrace();
		} finally{
			try{
				if(os != null){
					os.close();
				}
				if(fos != null){
					fos.close();
				}
			} catch(IOException e){
				Log.e("me444", "HERE");
			}
		}
	}

	public void AddRanking(final int score, final String name){
		rankings.put(score, name);
	}

	public TreeMultimap<Integer, String> GetRankings(){
		return rankings;
	}

	private TreeMultimap<Integer, String> rankings;

	public final static RankingsManager Instance;

	static{
		Instance = new RankingsManager();
	}
}
