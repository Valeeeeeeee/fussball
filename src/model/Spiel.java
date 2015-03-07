package model;

import static util.Utilities.*;

import java.util.ArrayList;

public class Spiel {
	
	private int homeTeamIndex;
	private int awayTeamIndex;
	
	private Wettbewerb wettbewerb;
//	private Liga liga;
//	private Gruppe gruppe;
//	private KORunde koRunde;
	private Mannschaft homeTeam;
	private Mannschaft awayTeam;
	private int[] lineupHome;
	private int[] lineupAway;
	
	private String schiedsrichter;
	private Ergebnis ergebnis;
	private ArrayList<Tor> tore = new ArrayList<>();
	
	public Spiel(Wettbewerb wettbewerb, int homeTeamIndex, int awayTeamIndex) {
		this.wettbewerb = wettbewerb;
//		if (wettbewerb instanceof Liga)			this.liga = (Liga) wettbewerb;
//		else if (wettbewerb instanceof Gruppe)	this.gruppe = (Gruppe) wettbewerb;
//		else if (wettbewerb instanceof KORunde)	this.koRunde = (KORunde) wettbewerb;
		
		this.homeTeamIndex = homeTeamIndex;
		this.homeTeam = wettbewerb.getMannschaften()[homeTeamIndex - 1];
		this.awayTeamIndex = awayTeamIndex;
		this.awayTeam = wettbewerb.getMannschaften()[awayTeamIndex - 1];
	}
	
	public Spiel(Wettbewerb wettbewerb, String daten) {
		try {
			if (daten.indexOf("+") != -1) {
				String data = daten.substring(daten.indexOf("+"));
				daten = daten.substring(0, daten.indexOf("+"));
				log("besteht aus >" + daten + "< und >" + data + "<");
			}
			
			this.homeTeamIndex = Integer.parseInt(daten.split(":")[0]);
			this.awayTeamIndex = Integer.parseInt(daten.split(":")[1]);
			
			if (this.homeTeamIndex == this.awayTeamIndex || this.homeTeamIndex == -1 || this.awayTeamIndex == -1) {
				throw new IllegalArgumentException();
			}
			
			this.wettbewerb = wettbewerb;
//			if (wettbewerb instanceof Liga)			this.liga = (Liga) wettbewerb;
//			else if (wettbewerb instanceof Gruppe)	this.gruppe = (Gruppe) wettbewerb;
//			else if (wettbewerb instanceof KORunde)	this.koRunde = (KORunde) wettbewerb;

			this.homeTeam = wettbewerb.getMannschaften()[homeTeamIndex - 1];
			this.awayTeam = wettbewerb.getMannschaften()[awayTeamIndex - 1];
			
		} catch (IllegalArgumentException iae) {
			log("The given match was formally correct, but impossible.");
		} catch (Exception e) {
			log("The given match was formally incorrect.");
		}
	}
	
	public Wettbewerb getWettbewerb() {
		return this.wettbewerb;
	}
	
	public int home() {
		return this.homeTeamIndex;
	}
	
	public Mannschaft getHomeTeam() {
		return this.homeTeam;
	}
	
	public int away() {
		return this.awayTeamIndex;
	}
	
	public Mannschaft getAwayTeam() {
		return this.awayTeam;
	}
	
	public Ergebnis getErgebnis() {
		return this.ergebnis;
	}
	
	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}
	
	public int[] getLineupHome() {
		return this.lineupHome;
	}
	
	public void setLineupHome(int[] lineupHome) {
		this.lineupHome = lineupHome;
	}
	
	public int[] getLineupAway() {
		return this.lineupAway;
	}
	
	public void setLineupAway(int[] lineupAway) {
		this.lineupAway = lineupAway;
	}
	
	public ArrayList<Tor> getTore() {
		return this.tore;
	}
	
	public String getSchiedsrichter() {
		return this.schiedsrichter;
	}
	
	public void setSchiedsrichter(String schiedsrichter) {
		this.schiedsrichter = schiedsrichter;
	}
	
	public String lineupToString(int[] lineup) {
		String lineupString = "";
		
		if (lineup != null) {
			for (int i = 0; i < lineup.length; i++) {
				lineupString += lineup[i];
				if (i < lineup.length - 1)	lineupString += ",";
			}
		} else {
			lineupString = "null";
		}
		
		return lineupString;
	}
	
	public String toString() {
		String toString = this.homeTeamIndex + ":" + this.awayTeamIndex;
		
		log("toString(): " + toString + "+{" + lineupToString(lineupHome) + "}+{" + lineupToString(lineupAway) + "}");
		
		return toString;
	}
}
