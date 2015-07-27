package model;

import static util.Utilities.*;

public class Tor {

	private String id;
	private String toString;
	private Spiel spiel;
	private boolean firstTeam;
	private boolean penalty;
	private boolean ownGoal;
	private int minute;
	private Spieler scorer;
	private Spieler assistgeber;
	
	public Tor(Spiel spiel, boolean firstTeam, boolean penalty, boolean ownGoal, int minute) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		
		this.toString = firstTeam + "-m" + minute + "-s-a" + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
		log("GOOOAL for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
			" in the " + minute + ". minute");
	}
	
	public Tor(Spiel spiel, boolean firstTeam, boolean penalty, boolean ownGoal, int minute, Spieler scorer) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		this.scorer = scorer;
		
		this.toString = firstTeam + "-m" + minute + "-s" + scorer.getSquadNumber() + "-a" + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
		log("GOOOAL for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPseudonym());
	}
	
	public Tor(Spiel spiel, boolean firstTeam, boolean penalty, boolean ownGoal, int minute, Spieler scorer, Spieler assistgeber) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		this.scorer = scorer;
		this.assistgeber = assistgeber;
		
		this.toString = firstTeam + "-m" + minute + "-s" + scorer.getSquadNumber() + "-a" + assistgeber.getSquadNumber() + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
		log("GOOOAL for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPseudonym() + 
			" and assisted by " + assistgeber.getPseudonym());
	}
	
	public Tor(Spiel spiel, String daten) {
		this.spiel = spiel;
		parseString(daten);
	}
	
	public String getID() {
		return id;
	}

	public Spiel getSpiel() {
		return spiel;
	}
	
	public boolean isOwnGoal() {
		return ownGoal;
	}
	
	public boolean isPenalty() {
		return penalty;
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
		penalty = daten.indexOf("-p") != -1;
		if (penalty)	daten = daten.substring(0, daten.indexOf("-p"));
		ownGoal = daten.indexOf("-og") != -1;
		if (ownGoal)	daten = daten.substring(0, daten.indexOf("-og"));
		minute = Integer.parseInt(daten.substring(daten.indexOf("-m") + 2, daten.indexOf("-s")));
		String substring;
		if ((substring = daten.substring(daten.indexOf("-s") + 2, daten.indexOf("-a"))).length() > 0) {
			int sqScorer = Integer.parseInt(substring);
			scorer = (firstTeam ^ ownGoal ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(sqScorer, spiel.getDate());
		}
		if ((substring = daten.substring(daten.indexOf("-a") + 2)).length() > 0) {
			int sqAssist = Integer.parseInt(substring);
			assistgeber = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(sqAssist, spiel.getDate());
		}
		
		toString = daten + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
	}
	
	public String toString() {
		return toString;
	}
}
