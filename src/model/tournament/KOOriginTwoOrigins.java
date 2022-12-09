package model.tournament;

public class KOOriginTwoOrigins extends KOOrigin {

	private KOOrigin originFirstTeam;
	private KOOrigin originSecondTeam;
	
	public KOOriginTwoOrigins(KOOrigin originFirstTeam, KOOrigin originSecondTeam) {
		this.koOriginType = KOOriginType.TWO_ORIGINS;
		this.originFirstTeam = originFirstTeam;
		this.originSecondTeam = originSecondTeam;
	}
	
	public KOOrigin getOriginFirstTeam() {
		return originFirstTeam;
	}
	
	public KOOrigin getOriginSecondTeam() {
		return originSecondTeam;
	}
	
	public String toDisplay() {
		return "";
	}
	
	public String toString() {
		return String.format("");
	}
}
