package model.tournament;

import static util.Utilities.twoDigit;

public class KOOriginPreviousKnockoutRound extends KOOrigin {
	
	private String previousKnockoutRound;
	
	private int matchIndex;
	
	public KOOriginPreviousKnockoutRound(String origin) {
		this.koOriginType = KOOriginType.PREVIOUS_KNOCKOUT_ROUND;
		this.origin = origin;
		this.previousKnockoutRound = origin.substring(0, 2);
		this.matchIndex = Integer.parseInt(origin.substring(2));
	}
	
	public String getPreviousKnockoutRound() {
		return previousKnockoutRound;
	}

	public int getMatchIndex() {
		return matchIndex;
	}
	
	public String toString() {
		return String.format("%s%s", previousKnockoutRound, twoDigit(matchIndex));
	}
}
