package model;

import static util.Utilities.*;

public class Spiel {
	
	private int homeTeamIndex;
	private int awayTeamIndex;
	
	private String schiedsrichter;
	private Ergebnis ergebnis;
	
	public Spiel(int homeTeamIndex, int awayTeamIndex) {
		this.homeTeamIndex = homeTeamIndex;
		this.awayTeamIndex = awayTeamIndex;
	}
	
	public Spiel(String daten) {
		try {
			this.homeTeamIndex = Integer.parseInt(daten.split(":")[0]);
			this.awayTeamIndex = Integer.parseInt(daten.split(":")[1]);
			
			if (this.homeTeamIndex == this.awayTeamIndex || this.homeTeamIndex == -1 || this.awayTeamIndex == -1) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException iae) {
			log("The given match was formally correct, but impossible.");
		} catch (Exception e) {
			log("The given match was formally incorrect.");
		}
	}
	
	public int home() {
		return this.homeTeamIndex;
	}
	
	public int away() {
		return this.awayTeamIndex;
	}
	
	public Ergebnis getErgebnis() {
		return this.ergebnis;
	}
	
	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}
	
	public String getSchiedsrichter() {
		return this.schiedsrichter;
	}
	
	public void setSchiedsrichter(String schiedsrichter) {
		this.schiedsrichter = schiedsrichter;
	}
	
	public String toString() {
		return this.homeTeamIndex + ":" + this.awayTeamIndex;
	}
}
