package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static util.Utilities.MIN_DATE;

public class Datum {
	
	private int day;
	private int month;
	private int year;
	
	public Datum(Datum ref, int days) {
		GregorianCalendar greg = new GregorianCalendar(ref.year, ref.month - 1, ref.day);
		greg.add(Calendar.DAY_OF_MONTH, days);
		day = greg.get(Calendar.DAY_OF_MONTH);
		month = greg.get(Calendar.MONTH) + 1;
		year = greg.get(Calendar.YEAR);
	}
	
	public Datum() {
		GregorianCalendar greg = new GregorianCalendar();
		day = greg.get(Calendar.DAY_OF_MONTH);
		month = greg.get(Calendar.MONTH) + 1;
		year = greg.get(Calendar.YEAR);
	}
	
	public Datum(String date) {
		this(Integer.parseInt(date));
	}
	
	public Datum(int date) {
		day = date % 100;
		month = (date % 10000) / 100;
		year = date / 10000;
	}
	
	public Datum(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public int getDayOfWeek() {
		return (new GregorianCalendar(year, month - 1, day).get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}
	
	public int comparable() {
		return 10000 * year + 100 * month + day;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Datum))	return false;
		Datum datum = (Datum) other;
		if (year != datum.year)		return false;
		if (month != datum.month)	return false;
		if (day != datum.day)		return false;
		return true;
	}
	
	public boolean isAfter(Datum other) {
		return isAfter(other, false);
	}
	
	private boolean isAfter(Datum other, boolean orEqual) {
		if (year < other.year)		return false;
		if (year > other.year)		return true;
		if (month < other.month)	return false;
		if (month > other.month)	return true;
		if (day < other.day)		return false;
		if (day == other.day)		return orEqual;
		return true;
	}
	
	public boolean isBefore(Datum other) {
		return isBefore(other, false);
	}
	
	private boolean isBefore(Datum other, boolean orEqual) {
		if (year > other.year)		return false;
		if (year < other.year)		return true;
		if (month > other.month)	return false;
		if (month < other.month)	return true;
		if (day > other.day)		return false;
		if (day == other.day)		return orEqual;
		return true;
	}
	
	public static Datum getFirstDate(Datum dateOne, Datum dateTwo) {
		return dateOne.isBefore(dateTwo, true) ? dateOne : dateTwo;
	}
	
	public int daysUntilIncl(Datum other) {
		return daysUntil(other) + 1;
	}
	
	public int daysUntil(Datum other) {
		if (this.equals(other))	return 0;
		GregorianCalendar greg1, greg2;
		int difference = 0, yDiff, factor = 1;
		if (this.isAfter(other)) {
			greg1 = new GregorianCalendar(other.year, other.month - 1, other.day);
			greg2 = new GregorianCalendar(year, month - 1, day);
			yDiff = year - other.year - 1;
			factor = -1;
		} else {
			greg1 = new GregorianCalendar(year, month - 1, day);
			greg2 = new GregorianCalendar(other.year, other.month - 1, other.day);
			yDiff = other.year - year - 1;
		}
		for (int i = 0; i < yDiff; i++) {
			greg1.add(Calendar.DAY_OF_MONTH, 365);
			difference += 365;
		}
		while (greg1.before(greg2)) {
			greg1.add(Calendar.DAY_OF_MONTH, 1);
			difference++;
		}
		return difference * factor;
	}
	
	public String withDividers() {
		return String.format("%d%d.%d%d.%d", day / 10, day % 10, month / 10, month % 10, year);
	}
	
	public static Datum parse(String withDividers) {
		try {
			String[] split = withDividers.split("\\.");
			return new Datum(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} catch (Exception e) {
			return MIN_DATE;
		}
	}
	
	public String toString() {
		return "" + comparable();
	}
}
