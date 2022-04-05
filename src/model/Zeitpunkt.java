package model;

public class Zeitpunkt {

	protected Datum date;
	
	protected Uhrzeit time;
	
	public Zeitpunkt(Datum date, Uhrzeit time) {
		this.date = date;
		this.time = time;
	}
	
	public Datum getDate() {
		return date;
	}
	
	public Uhrzeit getTime() {
		return time;
	}

	public String toDisplay() {
		return date.withDividers() + " " + time.withDividers();
	}
	
	public boolean isBefore(Zeitpunkt other) {
		if (date.isBefore(other.date))	return true;
		if (date.isAfter(other.date))	return false;
		return time.isBefore(other.time);
	}
	
	public boolean isAfter(Zeitpunkt other) {
		if (date.isAfter(other.date))	return true;
		if (date.isBefore(other.date))	return false;
		return time.isAfter(other.time);
	}
}
