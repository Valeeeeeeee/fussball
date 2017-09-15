package model;

import static util.Utilities.isIn;

public class Minute {
	
	private static final Integer[] injuryTimes = new Integer[] {45, 90, 105, 120};
	
	private int regularTime;
	private int injuryTime;
	
	public Minute(int regularTime, int injuryTime) {
		this.regularTime = regularTime;
		if (isIn(regularTime, injuryTimes)) {
			this.injuryTime = injuryTime;
		}
	}
	
	public int getRegularTime() {
		return regularTime;
	}
	
	public int getInjuryTime() {
		return injuryTime;
	}
	
	public boolean isBefore(Minute other) {
		if (this.regularTime < other.regularTime)	return true;
		if (this.regularTime > other.regularTime)	return false;
		return this.injuryTime < other.injuryTime;
	}
	
	public boolean isAfter(Minute other) {
		if (this.regularTime > other.regularTime)	return true;
		if (this.regularTime < other.regularTime)	return false;
		return this.injuryTime > other.injuryTime;
	}
	
	public String asString() {
		return regularTime + "'" + (injuryTime > 0 ? "+" + injuryTime : "");
	}
	
	public static Minute parse(String value) {
		String[] split = value.split("\\+");
		int index = 0;
		Minute minute = new Minute(0, 0);
		minute.regularTime = Integer.parseInt(split[index++]);
		if (split.length > 1 && isIn(minute.regularTime, injuryTimes))	minute.injuryTime = Integer.parseInt(split[index++]);
		return minute;
	}
	
	public String toString() {
		return regularTime + (injuryTime > 0 ? "+" + injuryTime : "");
	}
}
