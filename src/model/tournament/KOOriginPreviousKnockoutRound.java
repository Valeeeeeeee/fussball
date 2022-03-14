package model.tournament;

import static util.Utilities.twoDigit;

public class KOOriginPreviousKnockoutRound extends KOOrigin {
	
	private String previousKnockoutRound;
	
	private int matchIndex;
	
	private boolean qualification;
	
	private boolean teamIsWinner;
	
	public KOOriginPreviousKnockoutRound(String origin, boolean qualification, boolean teamIsWinner) {
		this.koOriginType = KOOriginType.PREVIOUS_KNOCKOUT_ROUND;
		this.origin = origin;
		this.previousKnockoutRound = origin.substring(0, 2);
		this.matchIndex = Integer.parseInt(origin.substring(2));
		this.qualification = qualification;
		this.teamIsWinner = teamIsWinner;
	}
	
	public String getPreviousKnockoutRound() {
		return previousKnockoutRound;
	}

	public int getMatchIndex() {
		return matchIndex;
	}
	
	public boolean isQualification() {
		return qualification;
	}
	
	public boolean teamIsWinner() {
		return teamIsWinner;
	}
	
	public String toString() {
		return String.format("%s%s", previousKnockoutRound, twoDigit(matchIndex));
	}
}
