package model.tournament;

public class KOOriginPreviousGroupStage extends KOOrigin {

	private String previousGroupStageIndex;
	
	private int placeIndex;
	
	private boolean qualification;
	
	private boolean groupXth;
	
	public KOOriginPreviousGroupStage(String origin, boolean qualification) {
		this.koOriginType = KOOriginType.PREVIOUS_GROUP_STAGE;
		this.origin = origin;
		this.previousGroupStageIndex = origin.substring(1, 2);
		this.placeIndex = Integer.parseInt(origin.substring(2));
		this.qualification = qualification;
		int secondChar = (int) origin.charAt(1);
		groupXth = secondChar >= 48 && secondChar <= 57;
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
	
	public String toString() {
		return String.format("G%s%d", previousGroupStageIndex, placeIndex);
	}
}
