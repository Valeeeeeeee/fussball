package model.tournament;

public class KOOriginPreviousGroupStage extends KOOrigin {

	private boolean roundRobinGroup;
	
	private String previousGroupStageIndex;
	
	private int placeIndex;
	
	private boolean qualification;
	
	private boolean groupXth;
	
	private final String displayString;
	
	public KOOriginPreviousGroupStage(String origin, boolean qualification, boolean roundRobinGroup) {
		this.roundRobinGroup = roundRobinGroup;
		this.origin = origin;
		this.qualification = qualification;
		
		if (roundRobinGroup) {
			this.koOriginType = KOOriginType.PREVIOUS_ROUND_ROBIN_GROUP_STAGE ;
			this.previousGroupStageIndex = origin.substring(1, 2);
			
			this.placeIndex = Integer.parseInt(origin.substring(2));
			int secondChar = (int) origin.charAt(1);
			groupXth = secondChar >= 48 && secondChar <= 57;
			
			if (groupXth) {
				this.displayString = String.format("%d.-bester Gruppen-%c.", this.placeIndex, origin.charAt(1));
			} else {
				this.displayString = String.format("%d. Gruppe %s", this.placeIndex, previousGroupStageIndex);
			}
		} else {
			this.koOriginType = KOOriginType.PREVIOUS_SWISS_SYSTEM_GROUP_STAGE;

			this.placeIndex = Integer.parseInt(origin.substring(1));
			this.displayString = String.format("%d. der Gruppenphase", this.placeIndex);
		}
	}
	
	public boolean isRoundRobinGroup() {
		return roundRobinGroup;
	}
	
	public String getPreviousGroupStageIndex() {
		return previousGroupStageIndex;
	}

	public int getPlaceIndex() {
		return placeIndex;
	}
	
	public boolean isQualification() {
		return qualification;
	}

	public boolean isGroupXth() {
		return groupXth;
	}
	
	public String toDisplay() {
		return displayString;
	}
	
	public String toString() {
		if (roundRobinGroup)	return String.format("%sG%s%d", koOriginType.getPrefix(), previousGroupStageIndex, placeIndex);
		return String.format("%sP%d", koOriginType.getPrefix(), placeIndex);
	}
}
