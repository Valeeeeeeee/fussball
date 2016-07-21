package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyDate {
	
	public static final int UNIX_EPOCH = 19700101;
	
	public static int newMyDate() {
		Calendar gc = new GregorianCalendar();
		int dd = gc.get(Calendar.DAY_OF_MONTH);
		int mm = gc.get(Calendar.MONTH) + 1;
		int yyyy = gc.get(Calendar.YEAR);
		
		return 10000 * yyyy + 100 * mm + dd;
	}
	
	public static int newMyTime() {
		Calendar gc = new GregorianCalendar();
		int hh = gc.get(Calendar.HOUR_OF_DAY);
		int mm = gc.get(Calendar.MINUTE);
		
		return 100 * hh + mm;
	}
	
	public static int getDate(String dateString) {
		try {
			String[] split = dateString.split("\\.");
			int index = 0;
			
			int myDate = Integer.parseInt(split[index++]);
			myDate += 100 * Integer.parseInt(split[index++]);
			myDate += 10000 * Integer.parseInt(split[index++]);
			
			return myDate;
		} catch (NumberFormatException nfe) {
			return UNIX_EPOCH;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return UNIX_EPOCH;
		}
	}
	
	public static int shiftDate(int startDate, int numberOfDays) {
		int dd = startDate % 100;
		int mm = (startDate % 10000) / 100;
		int yyyy = startDate / 10000;
		
		GregorianCalendar greg = new GregorianCalendar(yyyy, mm - 1, dd);
		greg.add(Calendar.DAY_OF_YEAR, numberOfDays);
		
		dd = greg.get(Calendar.DAY_OF_MONTH);
		mm = greg.get(Calendar.MONTH) + 1;
		yyyy = greg.get(Calendar.YEAR);
		
		return 10000 * yyyy + 100 * mm + dd;
	}
	
	public static int difference(int firstDate, int secondDate) {
		int dd = firstDate % 100;
		int mm = (firstDate % 10000) / 100;
		int yyyy = firstDate / 10000;
		
		GregorianCalendar greg = new GregorianCalendar(yyyy, mm - 1, dd);
		
		dd = secondDate % 100;
		mm = (secondDate % 10000) / 100;
		yyyy = secondDate / 10000;
		
		GregorianCalendar greg2 = new GregorianCalendar(yyyy, mm - 1, dd);
		
		int difference = 0, nextLeapYearCandidate, yearType;
		boolean includesLeapYear;
		if (firstDate < secondDate) {
			yearType = greg.get(Calendar.YEAR) % 4;
			if (greg.get(Calendar.MONTH) > 2)	yearType = (yearType + 1) % 4;
			
			while (greg.compareTo(greg2) == -1) {
				if (greg2.get(Calendar.YEAR) - greg.get(Calendar.YEAR) > 4) {
					nextLeapYearCandidate = (greg.get(Calendar.YEAR) / 4 + 1) * 4;
					if (nextLeapYearCandidate % 4 == 0 && greg.get(Calendar.MONTH) < 2)	nextLeapYearCandidate -= 4;
					includesLeapYear = nextLeapYearCandidate % 100 != 0 || nextLeapYearCandidate % 400 == 0;
					
					greg.add(Calendar.YEAR, 4);
					difference += includesLeapYear ? 1461 : 1460;
				} else if (greg2.get(Calendar.YEAR) - greg.get(Calendar.YEAR) > 1) {
					greg.add(Calendar.YEAR, 1);
					difference += 365;
					if (yearType == 0)	difference++;
					yearType = (yearType + 1) % 4;
				} else {
					greg.add(Calendar.DAY_OF_MONTH, 1);
					difference++;
				}
			}
		} else {
			yearType = greg2.get(Calendar.YEAR) % 4;
			if (greg2.get(Calendar.MONTH) > 2)	yearType = (yearType + 1) % 4;
			
			while (greg.compareTo(greg2) == 1) {
				if (greg.get(Calendar.YEAR) - greg2.get(Calendar.YEAR) > 4) {
					nextLeapYearCandidate = (greg2.get(Calendar.YEAR) / 4 + 1) * 4;
					if (nextLeapYearCandidate % 4 == 0 && greg2.get(Calendar.MONTH) < 2)	nextLeapYearCandidate -= 4;
					includesLeapYear = nextLeapYearCandidate % 100 != 0 || nextLeapYearCandidate % 400 == 0;
					
					greg2.add(Calendar.YEAR, 4);
					difference += includesLeapYear ? 1461 : 1460;
				} else if (greg.get(Calendar.YEAR) - greg2.get(Calendar.YEAR) > 1) {
					greg2.add(Calendar.YEAR, 1);
					difference += 365;
					if (yearType == 0)	difference++;
					yearType = (yearType + 1) % 4;
				} else {
					greg2.add(Calendar.DAY_OF_MONTH, 1);
					difference++;
				}
			}
		}
		
		return difference;
	}
	
	public static String datum(int startDate, int numberOfDaysToShift) {
		return datum(shiftDate(startDate, numberOfDaysToShift));
	}
	
	public static String datum(int myFormat) { 
		int d1 = (myFormat % 100) / 10;
		int d2 = myFormat % 10;
		int mo1 = (myFormat % 10000) / 1000;
		int mo2 = ((myFormat % 10000) / 100) % 10;
		int y = myFormat / 10000;
		
		return d1 + "" + d2 + "." + mo1 + "" + mo2 + "." + y;
	}
	
	public static String uhrzeit(int myFormat) {
		int h1 = (myFormat / 100) / 10;
		int h2 = (myFormat / 100) % 10;
		int mi1 = (myFormat % 100) / 10;
		int mi2 = (myFormat % 100) % 10;
		
		return h1 + "" + h2 + ":" + mi1 + "" + mi2;
	}
}
