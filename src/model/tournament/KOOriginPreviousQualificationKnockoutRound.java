package model.tournament;

import static util.Utilities.twoDigit;

public class KOOriginPreviousQualificationKnockoutRound extends KOOrigin {

	private String previousQualificationKnockoutRound;
	
	private int matchIndex;
	
	public KOOriginPreviousQualificationKnockoutRound(String origin) {
		this.koOriginType = KOOriginType.PREVIOUS_QUALIFICATION_KNOCKOUT_ROUND;
		this.origin = origin;
		this.previousQualificationKnockoutRound = origin.substring(0, 2);
		this.matchIndex = Integer.parseInt(origin.substring(2));
	}
	
	public String getPreviousQualificationKnockoutRound() {
		return previousQualificationKnockoutRound;
	}

	public int getMatchIndex() {
		return matchIndex;
	}
	
	public String toString() {
		return String.format("%s%s", previousQualificationKnockoutRound, twoDigit(matchIndex));
	}
}
