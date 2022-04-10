package model.tournament;

public class KOOriginPreviousLeague extends KOOrigin {
	
	private int placeIndex;
	
	private final String displayString;
	
	public KOOriginPreviousLeague(String origin) {
		this.koOriginType = KOOriginType.PREVIOUS_LEAGUE;
		this.origin = origin;
		this.placeIndex = Integer.parseInt(origin.substring(1));
		
		this.displayString = String.format("%d. Platz", placeIndex);
	}

	public int getPlaceIndex() {
		return placeIndex;
	}

	public String toDisplay() {
		return displayString;
	}
	
	public String toString() {
		return String.format("P%d", placeIndex);
	}
}
