package model.tournament;

public class KOOriginPrequalified extends KOOrigin {
	
	private String teamName;
	
	public KOOriginPrequalified(String teamName) {
		this.koOriginType = KOOriginType.PREQUALIFIED;
		this.origin = teamName;
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}
	
	public String toString() {
		return teamName;
	}
}
