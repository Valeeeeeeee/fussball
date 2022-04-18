package model.tournament;

import model.KORunde;

public class KOOriginOtherCompetition extends KOOrigin {
	
	private String otherCompetitionOrigin;
	
	private KORunde knockoutRound;
	
	private int teamIndex;
	
	private final String displayString;
	
	public KOOriginOtherCompetition(String otherCompetitionOrigin, KORunde knockoutRound, int teamIndex) {
		this.koOriginType = KOOriginType.OTHER_COMPETITION;
		this.origin = otherCompetitionOrigin;
		this.otherCompetitionOrigin = otherCompetitionOrigin;
		this.knockoutRound = knockoutRound;
		this.teamIndex = teamIndex;
		
		if (this.origin.endsWith("W")) {
			this.displayString = String.format("Sieger %s", this.origin.substring(0, this.origin.length() - 1));
		} else if (this.origin.endsWith("L")) {
			this.displayString = String.format("Verlierer %s", this.origin.substring(0, this.origin.length() - 1));
		} else {
			this.displayString = otherCompetitionOrigin;
		}
	}

	public String getOtherCompetitionOrigin() {
		return otherCompetitionOrigin;
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
		return String.format("%s%s", koOriginType.getPrefix(), otherCompetitionOrigin);
	}
}
