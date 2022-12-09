package model.tournament;

public enum KOOriginType {
	PREQUALIFIED("PQ:", false),
	PREVIOUS_GROUP_STAGE("PG:", true),
	PREVIOUS_KNOCKOUT_ROUND("PK:", true),
	PREVIOUS_LEAGUE("PL:", true),
	OTHER_COMPETITION("OC:", false),
	TWO_ORIGINS("TO:", true);
	
	private boolean fromPreviousRound;
	
	private String prefix;
	
	KOOriginType(String prefix, boolean fromPreviousRound) {
		this.prefix = prefix;
		this.fromPreviousRound = fromPreviousRound;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public boolean isFromPreviousRound() {
		return fromPreviousRound;
	}
	
	public static KOOriginType getTypeFromOrigin(String origin) {
		for (KOOriginType type : values()) {
			if (origin.startsWith(type.prefix))	return type;
		}
		
		return null;
	}
}
