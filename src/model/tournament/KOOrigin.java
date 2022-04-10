package model.tournament;

public abstract class KOOrigin {
	
	protected KOOriginType koOriginType;
	
	protected String origin;
	
	public KOOriginType getKOOriginType() {
		return koOriginType;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public abstract String toDisplay();
}

