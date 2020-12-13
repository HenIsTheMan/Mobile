package sg.diploma.product.event;

public abstract class EventAbstract{
	protected EventAbstract(){
		ID = EventIDs.EventID.Amt;
	}

	protected EventAbstract(final EventIDs.EventID ID){
		this.ID = ID;
	}

	public EventIDs.EventID GetID(){
		return ID;
	}

	protected EventIDs.EventID ID;
}