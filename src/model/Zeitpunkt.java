package model;

public class Zeitpunkt {
	
	public static final int MINUTES_PER_DAY = 1440;

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
	
	public static Zeitpunkt now() {
		return new Zeitpunkt(new Datum(), new Uhrzeit());
	}
	
	public Zeitpunkt plusMinutes(int minutes) {
		int days = 0;
		while (minutes >= MINUTES_PER_DAY) {
			minutes -= MINUTES_PER_DAY;
			days++;
		}
		while (minutes < 0) {
			minutes += MINUTES_PER_DAY;
			days--;
		}
		Uhrzeit time = new Uhrzeit(this.time, minutes);
		if (time.isBefore(this.time))	days++;
		Datum date = new Datum(this.date, days);
		return new Zeitpunkt(date, time);
	}
	
	public long comparable() {
		return 10000L * date.comparable() + time.comparable();
	}
}
