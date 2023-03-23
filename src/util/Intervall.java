package util;

public class Intervall {
	
	private int lowerBound;
	private int upperBound;
	
	public Intervall(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public int lower() {
		return lowerBound;
	}
	
	public int upper() {
		return upperBound;
	}
}
