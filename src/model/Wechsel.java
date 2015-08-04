package model;

import static util.Utilities.log;

public class Wechsel {
	
	private Spiel spiel;
	private boolean firstTeam;
	private int minute;
	private Spieler ausgewechselterSpieler;
	private Spieler eingewechselterSpieler;
	
	private String toString;
	
	public Wechsel(Spiel spiel, boolean firstTeam, int minute, Spieler ausgSpieler, Spieler eingSpieler) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.ausgewechselterSpieler = ausgSpieler;
		this.eingewechselterSpieler = eingSpieler;
		
		toString = minute + ":" + ausgewechselterSpieler.getSquadNumber() + ">>" + eingewechselterSpieler.getSquadNumber();
		log("Substitution by " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
				", in the " + minute + ". minute " + ausgewechselterSpieler.getPseudonymOrLN() + 
				" leaves the pitch and is replaced by " + eingewechselterSpieler.getPseudonymOrLN());
	}
	
	public Wechsel(Spiel spiel, boolean firstTeam, String daten) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
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

	public Spieler getAusgewechselterSpieler() {
		return ausgewechselterSpieler;
	}

	public Spieler getEingewechselterSpieler() {
		return eingewechselterSpieler;
	}

	private void parseString(String daten) {
		minute = Integer.parseInt(daten.substring(0, daten.indexOf(":")));
		int sqAusg = Integer.parseInt(daten.substring(daten.indexOf(":") + 1, daten.indexOf(">>")));
		ausgewechselterSpieler = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(sqAusg, spiel.getDate());
		int sqEing = Integer.parseInt(daten.substring(daten.indexOf(">>") + 2));
		eingewechselterSpieler = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(sqEing, spiel.getDate());
		
		toString = minute + ":" + ausgewechselterSpieler.getSquadNumber() + ">>" + eingewechselterSpieler.getSquadNumber();
	}
	
	public String toString() {
		return toString;
	}
}
