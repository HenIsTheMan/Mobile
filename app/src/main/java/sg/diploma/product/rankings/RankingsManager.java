package sg.diploma.product.rankings;

import java.util.TreeMap;

public class RankingsManager{
	RankingsManager(){
		rankings = null;
	}

	public void LoadRankings(){
	}

	public void SaveRankings(){
	}

	private final TreeMap<Integer, String> rankings;

	public final static RankingsManager Instance;

	static{
		Instance = new RankingsManager();
	}
}
