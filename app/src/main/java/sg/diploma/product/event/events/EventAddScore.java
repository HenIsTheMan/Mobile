package sg.diploma.product.event.events;

import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.EventIDs;

public final class EventAddScore extends EventAbstract{
	public EventAddScore(final int scoreAdd){
		super(EventIDs.EventID.AddScore);
		this.scoreAdd = scoreAdd;
	}

	public int GetScoreAdd(){
		return scoreAdd;
	}

	private final int scoreAdd;
}
