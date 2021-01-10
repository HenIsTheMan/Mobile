package sg.diploma.product.rankings;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RankingsManager{
	RankingsManager(){
		rankings = TreeMultimap.create(Ordering.arbitrary(), Ordering.arbitrary());
	}

	public void LoadRankings(final String name){
		try{
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);

			rankings = (TreeMultimap<Integer, String>)ois.readObject();

			ois.close();
			fis.close();
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public void SaveRankings(final String name){
		try{
			FileOutputStream fos = new FileOutputStream(name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(rankings);

			oos.close();
			fos.close();
		} catch(IOException e){
			e.printStackTrace();
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
