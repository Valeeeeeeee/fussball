package model;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static util.Utilities.*;

public class MyDate {
	
	public static int newMyDate() {
		int datum = 0;
		Calendar gc = new GregorianCalendar();
		int dd = gc.get(Calendar.DAY_OF_MONTH);
		int mm = gc.get(Calendar.MONTH) + 1;
		int yyyy = gc.get(Calendar.YEAR);
		
		datum = 10000 * yyyy + 100 * mm + dd;
		
		return datum;
	}
	
	public static int newMyTime() {
		int datum = 0;
		Calendar gc = new GregorianCalendar();
		int hh = gc.get(Calendar.HOUR_OF_DAY);
		int mm = gc.get(Calendar.MINUTE);
		
		datum = 100 * hh + mm;
		
		return datum;
	}
	
	public static int getDate(String dateString) {
		try {
			String[] daten = dateString.split("\\.");
			
			int myDate = Integer.parseInt(daten[0]);
			myDate += 100 * Integer.parseInt(daten[1]);
			myDate += 10000 * Integer.parseInt(daten[2]);
			
			return myDate;
		} catch (NumberFormatException nfe) {
			return 19700101;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return 19700101;
		}
	}
	
	public static int verschoben(int startdatum, int verschiebung) {
		int dd = startdatum % 100;
		int mm = (startdatum % 10000) / 100;
		int yyyy = startdatum / 10000;
		
		GregorianCalendar greg = new GregorianCalendar(yyyy, mm - 1, dd);
		greg.add(Calendar.DAY_OF_YEAR, verschiebung);
		
		int datum = 0;
		
		dd = greg.get(Calendar.DAY_OF_MONTH);
		mm = greg.get(Calendar.MONTH) + 1;
		yyyy = greg.get(Calendar.YEAR);
		
		datum = 10000 * yyyy + 100 * mm + dd;
		
		return datum;
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
	
	public static String datum(int startdatum, int verschiebung) {
		return datum(verschoben(startdatum, verschiebung));
	}
	
	public static String datum(int myformat) { 
		int d1 = (myformat % 100) / 10;
		int d2 = myformat % 10;
		int mo1 = (myformat % 10000) / 1000;
		int mo2 = ((myformat % 10000) / 100) % 10;
		int y = myformat / 10000;
		
		return d1 + "" + d2 + "." + mo1 + "" + mo2 + "." + y;
	}
	
	public static String uhrzeit(int myformat) {
		int h1 = (myformat / 100) / 10;
		int h2 = (myformat / 100) % 10;
		int mi1 = (myformat % 100) / 10;
		int mi2 = (myformat % 100) % 10;
		
		return h1 + "" + h2 + ":" + mi1 + "" + mi2;
	}
	
	public static String uhrzeit(String ligaformat) {
		
		
		return ligaformat;
	}
	
}
