package model;

import static util.Utilities.DATE_UNDEFINED;
import static util.Utilities.UNDEFINED;

public class AnstossZeit {
	
	private int index;
	private int daysSince;
	private Uhrzeit time;
	
	private String[] weekdays = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
	
	public AnstossZeit(int index, int daysSince, Uhrzeit time) {
		this.index = index;
		this.daysSince = daysSince;
		this.time = time;
	}

	public AnstossZeit(int index, String data) {
		this.index = index;
		fromString(data);
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getDateAndTime(Datum startDate) {
		Datum date = new Datum(startDate, !time.isUndefined() || daysSince != UNDEFINED ? daysSince : 0);
		return date.withDividers() + " " + time.withDividers();
	}
	
	public Datum getDate(Datum startDate) {
		if (startDate.equals(DATE_UNDEFINED))	return DATE_UNDEFINED;
		return new Datum(startDate, daysSince);
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
		return weekdays[new Datum(date, daysSince).getDayOfWeek() - 1] + " " + getTime().withDividers() + (daysSince < 0 || daysSince > 6 ? " (" + daysSince + "d)" : "");
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
