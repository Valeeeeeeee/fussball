package model;

import static util.Utilities.KICK_OFF_TIME_UNDEFINED;

public class AnstossZeit extends Zeitpunkt {
	
	private RelativeAnstossZeit relativeKickOffTime;
	
	public AnstossZeit(RelativeAnstossZeit relativeKickOffTime, Datum referenceDate) {
		super(new Datum(referenceDate, relativeKickOffTime.getDaysSince()), relativeKickOffTime.getTime());
		
		this.relativeKickOffTime = relativeKickOffTime;
	}
	
	public RelativeAnstossZeit getRelativeKickOffTime() {
		return relativeKickOffTime;
	}

	public String toDisplay() {
		if (!hasDate())		return "nicht terminiert";
		return super.toDisplay();
	}
	
	public boolean hasDate() {
		return this != KICK_OFF_TIME_UNDEFINED;
	}
}
