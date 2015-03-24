package model;

public class Tor {

	private String id;
	private String toString;
	private Spiel spiel;
	private boolean firstTeam;
	private int minute;
	private Spieler scorer;
	private Spieler assistgeber;
	
	public Tor(Spiel spiel, boolean firstTeam, int minute, Spieler scorer, Spieler assistgeber) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.scorer = scorer;
		this.assistgeber = assistgeber;
		
		this.toString = firstTeam + "m" + minute + "s" + scorer.getSquadNumber() + "a" + assistgeber.getSquadNumber();
		this.id = spiel.home() + "v" + spiel.away() + "h" + toString;
	}
	
	public Tor(Spiel spiel, String daten) {
		this.spiel = spiel;
		parseString(daten);
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public boolean isScoredByFirstTeam() {
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
		firstTeam = Boolean.parseBoolean(daten.substring(0, daten.indexOf("m")));
		minute = Integer.parseInt(daten.substring(daten.indexOf("m") + 1, daten.indexOf("s")));
	}
	
	public String toString() {
		return toString;
	}
}
