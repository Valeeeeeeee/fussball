package model.tournament;

public class KOOriginPreviousLeague extends KOOrigin {
	
	private int placeIndex;
	
	public KOOriginPreviousLeague(String origin) {
		this.koOriginType = KOOriginType.PREVIOUS_LEAGUE;
		this.origin = origin;
		this.placeIndex = Integer.parseInt(origin.substring(1));
	}

	public int getPlaceIndex() {
		return placeIndex;
	}

	public String toString() {
		return String.format("P%d", placeIndex);
	}
}
