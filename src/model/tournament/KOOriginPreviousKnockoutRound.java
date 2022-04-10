package model.tournament;

import static util.Utilities.twoDigit;

public class KOOriginPreviousKnockoutRound extends KOOrigin {
	
	private String previousKnockoutRound;
	
	private int matchIndex;
	
	private boolean qualification;
	
	private boolean teamIsWinner;
	
	private final String displayString;
	
	public KOOriginPreviousKnockoutRound(String origin, boolean qualification) {
		this.koOriginType = KOOriginType.PREVIOUS_KNOCKOUT_ROUND;
		this.origin = origin;
		if (origin.endsWith("W")) {
			this.teamIsWinner = true;
			origin = origin.substring(0,  origin.length() - 1);
		} else if (origin.endsWith("L")) {
			this.teamIsWinner = false;
			origin = origin.substring(0,  origin.length() - 1);
		}
		this.previousKnockoutRound = origin.substring(0, 2);
		this.matchIndex = Integer.parseInt(origin.substring(2));
		this.qualification = qualification;
		
		this.displayString = String.format("%s %s %d", this.teamIsWinner ? "Sieger" : "Verlierer", this.previousKnockoutRound, this.matchIndex);
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
	
	public String toDisplay() {
		return displayString;
	}
	
	public String toString() {
		return String.format("%s%s%s", previousKnockoutRound, twoDigit(matchIndex), teamIsWinner ? "W" : "L");
	}
}
