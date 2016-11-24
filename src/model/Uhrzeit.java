package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static util.Utilities.UNDEFINED;

public class Uhrzeit {
	private int hourOfDay;
	private int minute;
	
	public Uhrzeit(Uhrzeit ref, int minutes) {
		GregorianCalendar greg = new GregorianCalendar(1970, 0, 1, ref.hourOfDay, ref.minute);
		greg.add(Calendar.MINUTE, minutes);
		hourOfDay = greg.get(Calendar.HOUR_OF_DAY);
		minute = greg.get(Calendar.MINUTE);
	}
	
	public Uhrzeit() {
		GregorianCalendar greg = new GregorianCalendar();
		hourOfDay = greg.get(Calendar.HOUR_OF_DAY);
		minute = greg.get(Calendar.MINUTE);
	}
	
	public Uhrzeit(String time) {
		this(Integer.parseInt(time));
	}
	
	public Uhrzeit(int time) {
		hourOfDay = time / 100;
		minute = time % 100;
	}
	
	public Uhrzeit(int hourOfDay, int minute) {
		this.hourOfDay = hourOfDay;
		this.minute = minute;
	}
	
	public int getHourOfDay() {
		return hourOfDay;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int comparable() {
		return 100 * hourOfDay + minute;
	}
	
	public boolean isUndefined() {
		return equals(UNDEFINED);
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Uhrzeit))	return false;
		Uhrzeit uhrzeit = (Uhrzeit) other;
		if (hourOfDay != uhrzeit.hourOfDay)	return false;
		if (minute != uhrzeit.minute)		return false;
		return true;
	}
	
	public boolean isAfter(Uhrzeit other) {
		return isAfter(other, false);
	}
	
	private boolean isAfter(Uhrzeit other, boolean orEqual) {
		if (hourOfDay < other.hourOfDay)	return false;
		if (hourOfDay > other.hourOfDay)	return true;
		if (minute < other.minute)			return false;
		if (minute == other.minute)			return orEqual;
		return true;
	}
	
	public boolean isBefore(Uhrzeit other) {
		return isBefore(other, false);
	}
	
	private boolean isBefore(Uhrzeit other, boolean orEqual) {
		if (hourOfDay > other.hourOfDay)	return false;
		if (hourOfDay < other.hourOfDay)	return true;
		if (minute > other.minute)			return false;
		if (minute == other.minute)			return orEqual;
		return true;
	}
	
	public String withDividers() {
		if (isUndefined())	return "k. A.";
		return String.format("%d%d:%d%d", hourOfDay / 10, hourOfDay % 10, minute / 10, minute % 10);
	}
	
	public String toString() {
		return "" + comparable();
	}
}
