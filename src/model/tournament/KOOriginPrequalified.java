package model.tournament;

import model.KORunde;

public class KOOriginPrequalified extends KOOrigin {
	
	private String teamName;
	
	private KORunde knockoutRound;
	
	private int teamIndex;
	
	public KOOriginPrequalified(String teamName, KORunde knockoutRound, int teamIndex) {
		this.koOriginType = KOOriginType.PREQUALIFIED;
		this.origin = teamName;
		this.teamName = teamName;
		this.knockoutRound = knockoutRound;
		this.teamIndex = teamIndex;
	}

	public String getTeamName() {
		return teamName;
	}
	
	public KORunde getKnockoutRound() {
		return knockoutRound;
	}
	
	public int getTeamIndex() {
		return teamIndex;
	}
	
	public String toString() {
		return teamName;
	}
}
