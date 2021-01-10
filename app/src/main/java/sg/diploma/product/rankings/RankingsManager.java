package sg.diploma.product.rankings;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class RankingsManager{
	RankingsManager(){
		rankings = TreeMultimap.create(Ordering.arbitrary(), Ordering.arbitrary());
	}

	public void LoadRankings(final String name){
		InputStream is = null;
		ObjectInputStream ois = null;
		try{
			is = new FileInputStream(name);
			ois = new ObjectInputStream(is);

			rankings = (TreeMultimap<Integer, String>)ois.readObject();
		} catch(IOException e){
			android.util.Log.e("me000", "HERE");
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			android.util.Log.e("me010", "HERE");
		} finally{
			try{
				if (ois != null) ois.close();
				if (is != null) is.close();
			} catch(IOException e){
				android.util.Log.e("me111", "HERE");
			}
		}
	}

	public void SaveRankings(final String name){
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try{
			os = new FileOutputStream(name);
			oos = new ObjectOutputStream(os);

			oos.writeObject(rankings);
		} catch(IOException e){
			android.util.Log.e("me222", "HERE");
			e.printStackTrace();
		} finally{
			try{
				if (oos != null) oos.close();
				if (os != null) os.close();
			} catch(IOException e){
				android.util.Log.e("me333", "HERE");
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
