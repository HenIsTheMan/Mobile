package sg.diploma.product.rankings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

public class RankingsManager{
	RankingsManager(){
		rankings = new TreeMap<>();
	}

	public void LoadRankings(final String name){
		try{
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);

			rankings = (TreeMap<Integer, String>)ois.readObject();

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

	public TreeMap<Integer, String> GetRankings(){
		return rankings;
	}

	private TreeMap<Integer, String> rankings;

	public final static RankingsManager Instance;

	static{
		Instance = new RankingsManager();
	}
}
