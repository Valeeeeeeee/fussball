package model.tournament;

import model.KORunde;

public class KOOriginOtherCompetition extends KOOrigin {
	
	private String otherCompetitionOrigin;
	
	private KORunde knockoutRound;
	
	private int teamIndex;
	
	public KOOriginOtherCompetition(String otherCompetitionOrigin, KORunde knockoutRound, int teamIndex) {
		this.koOriginType = KOOriginType.OTHER_COMPETITION;
		this.origin = otherCompetitionOrigin;
		this.otherCompetitionOrigin = otherCompetitionOrigin;
		this.knockoutRound = knockoutRound;
		this.teamIndex = teamIndex;
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
	
	public String toString() {
		return otherCompetitionOrigin;
	}
}
