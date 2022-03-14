package model.tournament;

public enum KOOriginType {
	PREQUALIFIED(false),
	PREVIOUS_GROUP_STAGE(true),
	PREVIOUS_KNOCKOUT_ROUND(true),
	PREVIOUS_LEAGUE(true),
	OTHER_COMPETITION(false);
	
	private boolean fromPreviousRound;
	
	KOOriginType(boolean fromPreviousRound) {
		this.fromPreviousRound = fromPreviousRound;
	}
	
	public boolean isFromPreviousRound() {
		return fromPreviousRound;
	}
}
