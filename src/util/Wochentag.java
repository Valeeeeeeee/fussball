package util;

import java.util.Calendar;

public enum Wochentag {
	MONDAY("Mo", "Montag"),
	TUESDAY("Di", "Dienstag"),
	WEDNESDAY("Mi", "Mittwoch"),
	THURSDAY("Do", "Donnerstag"),
	FRIDAY("Fr", "Freitag"),
	SATURDAY("Sa", "Samstag"),
	SUNDAY("So", "Sonntag");
	
	private String shortName;
	private String longName;
	
	Wochentag(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public static Wochentag fromCalendar(Calendar cal) {
		switch(cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				return MONDAY;
			case Calendar.TUESDAY:
				return TUESDAY;
			case Calendar.WEDNESDAY:
				return WEDNESDAY;
			case Calendar.THURSDAY:
				return THURSDAY;
			case Calendar.FRIDAY:
				return FRIDAY;
			case Calendar.SATURDAY:
				return SATURDAY;
			case Calendar.SUNDAY:
				return SUNDAY;
			default:
				return null;
		}
	}
}
