package sg.diploma.product.rankings;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RankingsManager{
	RankingsManager(){
		rankings = null;
	}

	public void LoadRankings(final Context context, final String scoresFileName, final String namesFileName){
		FileInputStream scoresFIS = null;
		ObjectInputStream scoresOIS = null;
		FileInputStream namesFIS = null;
		ObjectInputStream namesOIS = null;

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator);
		final File scoresFile = new File(dir, scoresFileName);
		final File namesFile = new File(dir, namesFileName);

		try{
			scoresFIS = new FileInputStream(scoresFile);
			scoresOIS = new ObjectInputStream(scoresFIS);
			final Integer[] scores = (Integer[])scoresOIS.readObject(); //??

			namesFIS = new FileInputStream(namesFile);
			namesOIS = new ObjectInputStream(namesFIS);
			final String[] names = (String[])namesOIS.readObject(); //??

			rankings = TreeMultimap.create(Ordering.arbitrary(), Ordering.arbitrary());
			final int arrLen = scores.length;
			for(int i = 0; i < arrLen; ++i){
				rankings.put(scores[i], names[i]);
			}
		} catch(FileNotFoundException e){
			Log.e("meYES", "HERE");

			rankings = TreeMultimap.create(Ordering.arbitrary(), Ordering.arbitrary());
			e.printStackTrace();
		} catch(IOException e){
			android.util.Log.e("me000", "HERE");
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			android.util.Log.e("me010", "HERE");
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
				android.util.Log.e("me111", "HERE");
				e.printStackTrace();
			}
		}
	}

	/** @noinspection ResultOfMethodCallIgnored*/
	public void SaveRankings(final Context context, final String scoresFileName, final String namesFileName){
		final String state = Environment.getExternalStorageState();
		if(!Environment.MEDIA_MOUNTED.equals(state)){
			Log.e("no", "HERE");
			return;
		}

		final String pathToAppFolder = context.getExternalFilesDir(null).getAbsolutePath();
		final File dir = new File(pathToAppFolder + File.separator);
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
			scoresOOS.writeObject(rankings.keys().toArray()); //??

			namesFOS = new FileOutputStream(namesFile);
			namesOOS = new ObjectOutputStream(namesFOS);
			namesOOS.writeObject(rankings.values().toArray()); //??
		} catch(FileNotFoundException e){
			Log.e("me222", "HERE");
			e.printStackTrace();
		} catch(IOException e){
			Log.e("me333", "HERE");
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
				Log.e("me444", "HERE");
				e.printStackTrace();
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
