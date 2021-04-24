package model;

public class Dauer {
	
	private Datum fromDate;
	private Datum toDate;
	
	public Dauer (Datum fromDate, Datum toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		checkOrder();
	}
	
	public Dauer(String duration) {
		fromString(duration);
	}
	
	public Datum getFromDate() {
		return fromDate;
	}
	
	public Datum getToDate() {
		return toDate;
	}
	
	public int getDuration() {
		return fromDate.daysUntilIncl(toDate);
	}
	
	public boolean includes(Datum date) {
		return !(fromDate.isAfter(date) || toDate.isBefore(date));
	}
	
	public boolean overlaps(Dauer other) {
		return !(toDate.isBefore(other.fromDate) || fromDate.isAfter(other.toDate));
	}
	
	public String withDividers() {
		return fromDate.withDividers() + " - " + toDate.withDividers();
	}
	
	private void checkOrder() {
		if (fromDate.isAfter(toDate)) {
			Datum temp = fromDate;
			this.fromDate = toDate;
			this.toDate = temp;
		}
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Dauer))	return false;
		Dauer dauer = (Dauer) other;
		if (!fromDate.equals(dauer.fromDate))	return false;
		if (!toDate.equals(dauer.toDate))		return false;
		return true;
	}
	
	private void fromString(String duration) {
		String[] split = duration.split("\\-");
		fromDate = new Datum(split[0]);
		toDate = new Datum(split[1]);
		checkOrder();
	}
	
	public String toString() {
		return fromDate.comparable() + "-" + toDate.comparable();
	}
}
