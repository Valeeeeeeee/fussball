package model;

import static util.Utilities.DATE_UNDEFINED;
import static util.Utilities.KICK_OFF_TIME_UNDEFINED;

public class RelativeAnstossZeit {
	
	private int index;
	private int daysSince;
	private Uhrzeit time;
	
	public RelativeAnstossZeit(int index, int daysSince, Uhrzeit time) {
		this.index = index;
		this.daysSince = daysSince;
		this.time = time;
	}

	public RelativeAnstossZeit(int index, String data) {
		this.index = index;
		fromString(data);
	}
	
	public int getIndex() {
		return index;
	}
	
	public AnstossZeit getKickOffTime(Datum startDate) {
		if (startDate.equals(DATE_UNDEFINED))	return KICK_OFF_TIME_UNDEFINED;
		return new AnstossZeit(this, startDate);
	}
	
	public int getDaysSince() {
		return daysSince;
	}
	
	public Uhrzeit getTime() {
		return time;
	}
	
	public boolean matches(int diff, Uhrzeit timeOfNewKOT) {
		if (daysSince != diff)			return false;
		if (!time.equals(timeOfNewKOT))	return false;
		return true;
	}
	
	public String weekdayAndTime(Datum date) {
		return new Datum(date, daysSince).getDayOfWeek().getShortName() + " " + getTime().withDividers() + (daysSince < 0 || daysSince > 6 ? " (" + daysSince + "d)" : "");
	}
	
	public String toString() {
		return daysSince + "," + time.comparable();
	}
	
	private void fromString(String data) {
		String[] split = data.split(",");
		int index = 0;
		
		daysSince = Integer.parseInt(split[index++]);
		time = new Uhrzeit(split[index++]);
	}
}
