package sg.diploma.product.rankings;

import android.content.Context;
import android.os.Environment;

import com.google.common.collect.Ordering;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class RankingsManager{
	RankingsManager(){
		rankings = TreeMultimap.create(Ordering.natural(), Ordering.natural());
	}

	public void LoadRankings(final Context context, final String scoresFileName, final String namesFileName){
		FileInputStream scoresFIS = null;
		ObjectInputStream scoresOIS = null;
		FileInputStream namesFIS = null;
		ObjectInputStream namesOIS = null;

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator + "rankings");
		final File scoresFile = new File(dir, scoresFileName);
		final File namesFile = new File(dir, namesFileName);

		try{
			scoresFIS = new FileInputStream(scoresFile);
			scoresOIS = new ObjectInputStream(scoresFIS);
			namesFIS = new FileInputStream(namesFile);
			namesOIS = new ObjectInputStream(namesFIS);

			final Object[] scoresObjArr = (Object[])scoresOIS.readObject();
			final Integer[] scores = new Integer[scoresObjArr.length];
			for(int i = 0; i < scoresObjArr.length; ++i){
				scores[i] = (int)scoresObjArr[i];
			}

			final Object[] namesObjArr = (Object[])namesOIS.readObject();
			final String[] names = new String[namesObjArr.length];
			for(int i = 0; i < namesObjArr.length; ++i){
				names[i] = (String)namesObjArr[i];
			}

			rankings.clear();
			for(int i = 0; i < scoresObjArr.length; ++i){
				rankings.put(scores[i], names[i]);
			}
		} catch(ClassNotFoundException | IOException e){
			e.printStackTrace();
		} finally{
			try{
				if(scoresOIS != null){
					scoresOIS.close();
				}
				if(scoresFIS != null){
					scoresFIS.close();
				}
				if(namesOIS != null){
					namesOIS.close();
				}
				if(namesFIS != null){
					namesFIS.close();
				}
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	/** @noinspection ResultOfMethodCallIgnored*/
	public void SaveRankings(final Context context, final String scoresFileName, final String namesFileName){
		final String state = Environment.getExternalStorageState();
		if(!Environment.MEDIA_MOUNTED.equals(state)){
			return;
		}

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator + "rankings");
		if(dir.exists()){
			ClearDirectory(dir);
		}
		dir.mkdirs();

		final File scoresFile = new File(dir, scoresFileName);
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
		}
	}

	public void AddRanking(final int score, final String name){
		rankings.put(score, name);
	}

	public SortedSetMultimap<Integer, String> GetRankings(){
		return rankings;
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

	private final SortedSetMultimap<Integer, String> rankings;

	public final static RankingsManager Instance;

	static{
		Instance = new RankingsManager();
	}
}
