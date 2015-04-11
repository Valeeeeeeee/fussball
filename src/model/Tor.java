package model;

import static util.Utilities.*;

public class Tor {

	private String id;
	private String toString;
	private Spiel spiel;
	private boolean firstTeam;
	private int minute;
	private Spieler scorer;
	private Spieler assistgeber;
	
	public Tor(Spiel spiel, boolean firstTeam, int minute) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		
		this.toString = firstTeam + "-m" + minute + "-s-a";
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
		log("New goal for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
			" in the " + minute + ". minute");
	}
	
	public Tor(Spiel spiel, boolean firstTeam, int minute, Spieler scorer) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.scorer = scorer;
		
		this.toString = firstTeam + "-m" + minute + "-s" + scorer.getSquadNumber() + "-a";
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
		log("New goal for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPseudonym());
	}
	
	public Tor(Spiel spiel, boolean firstTeam, int minute, Spieler scorer, Spieler assistgeber) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.scorer = scorer;
		this.assistgeber = assistgeber;
		
		this.toString = firstTeam + "-m" + minute + "-s" + scorer.getSquadNumber() + "-a" + assistgeber.getSquadNumber();
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
		log("New goal for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPseudonym() + 
			" and assisted by " + assistgeber.getPseudonym());
	}
	
	public Tor(Spiel spiel, String daten) {
		this.spiel = spiel;
		parseString(daten);
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public boolean isFirstTeam() {
		return firstTeam;
	}
	
	public int getMinute() {
		return minute;
	}

	public Spieler getScorer() {
		return scorer;
	}

	public Spieler getAssistgeber() {
		return assistgeber;
	}
	
	private void parseString(String daten) {
		firstTeam = Boolean.parseBoolean(daten.substring(0, daten.indexOf("-m")));
		minute = Integer.parseInt(daten.substring(daten.indexOf("-m") + 2, daten.indexOf("-s")));
		String substring;
		if ((substring = daten.substring(daten.indexOf("-s") + 2, daten.indexOf("-a"))).length() > 0) {
			int sqScorer = Integer.parseInt(substring);
			scorer = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(sqScorer, spiel.getDate());
		}
		if ((substring = daten.substring(daten.indexOf("-a") + 2)).length() > 0) {
			int sqAssist = Integer.parseInt(substring);
			assistgeber = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(sqAssist, spiel.getDate());
		}
		
		toString = daten;
	}
	
	public String toString() {
		return toString;
	}
}
