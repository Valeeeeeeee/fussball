package model;

public class AnstossZeit {
	
	private int index;
	private int daysSince;
	private int time;
	
	public AnstossZeit(int index, int daysSince, int time) {
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
	
	public String getDateAndTime(int startDate) {
		String datum, uhrzeit = " k. A.";
		
		int date = MyDate.shiftDate(startDate, time != -1 || daysSince != -1 ? daysSince : 0);
		datum = MyDate.datum(date);
		if (time != -1)	uhrzeit = MyDate.uhrzeit(time);
		
		return datum + " " + uhrzeit;
	}
	
	public int getDate(int startDate) {
		if (startDate == 0)	return 0;
		return MyDate.shiftDate(startDate, daysSince);
	}
	
	public int getDaysSince() {
		return daysSince;
	}
	
	public int getTime() {
		return time;
	}
	
	public boolean matches(int diff, int timeOfNewKOT) {
		if (daysSince != diff)		return false;
		if (time != timeOfNewKOT)	return false;
		return true;
	}
	
	public String toString() {
		return daysSince + "," + time;
	}
	
	private void fromString(String data) {
		String[] split = data.split(",");
		int index = 0;
		
		daysSince = Integer.parseInt(split[index++]);
		time = Integer.parseInt(split[index++]);
	}
}
