package model;
import java.util.Calendar;
import java.util.GregorianCalendar;


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
		int h2 = myformat / 10;
		int mi1 = (myformat % 100) / 10;
		int mi2 = ((myformat % 100) / 100) % 10;
		
		return h1 + "" + h2 + ":" + mi1 + "" + mi2;
	}
	
	public static String uhrzeit(String ligaformat) {
		
		
		return ligaformat;
	}
	
}
