package model.tournament;

import model.KORunde;

public class KOOriginPrequalified extends KOOrigin {
	
	private String teamName;
	
	private KORunde knockoutRound;
	
	private int teamIndex;
	
	private final String displayString;
	
	public KOOriginPrequalified(String teamName, KORunde knockoutRound, int teamIndex) {
		this.koOriginType = KOOriginType.PREQUALIFIED;
		this.origin = teamName;
		this.teamName = teamName;
		this.knockoutRound = knockoutRound;
		this.teamIndex = teamIndex;
		
		this.displayString = teamName;
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
	
	public String toDisplay() {
		return displayString;
	}
	
	public String toString() {
		return teamName;
	}
}
