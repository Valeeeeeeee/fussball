package model.tournament;

public class KOOriginPreviousQualificationGroupStage extends KOOrigin {
	
	private String previousQualificationGroupStageIndex;
	
	private int placeIndex;
	
	private boolean groupXth;
	
	public KOOriginPreviousQualificationGroupStage(String origin) {
		this.koOriginType = KOOriginType.PREVIOUS_QUALIFICATION_GROUP_STAGE;
		this.origin = origin;
		this.previousQualificationGroupStageIndex = origin.substring(1, 2);
		this.placeIndex = Integer.parseInt(origin.substring(2));
		int secondChar = (int) origin.charAt(1);
		groupXth = secondChar >= 48 && secondChar <= 57;
	}
	
	public String getPreviousQualificationGroupStageIndex() {
		return previousQualificationGroupStageIndex;
	}

	public int getPlaceIndex() {
		return placeIndex;
	}

	public boolean isGroupXth() {
		return groupXth;
	}
	
	public String toString() {
		return String.format("G%s%d", previousQualificationGroupStageIndex, placeIndex);
	}
}
