package model.tournament;

public class KOOriginOtherCompetition extends KOOrigin {
	
	private String otherCompetitionOrigin;
	
	public KOOriginOtherCompetition(String otherCompetitionOrigin) {
		this.koOriginType = KOOriginType.OTHER_COMPETITION;
		this.origin = otherCompetitionOrigin;
		this.otherCompetitionOrigin = otherCompetitionOrigin;
	}

	public String getOtherCompetitionOrigin() {
		return otherCompetitionOrigin;
	}
	
	public String toString() {
		return otherCompetitionOrigin;
	}
}
