package model;

public class Tor {

	private String id;
	private Spiel spiel;
	private int minute;
	private Spieler scorer;
	private Spieler assistgeber;
	
	public Tor(Spiel spiel, int minute, Spieler scorer, Spieler assistgeber) {
		this.spiel = spiel;
		this.minute = minute;
		this.scorer = scorer;
		this.assistgeber = assistgeber;
		
		this.id = spiel.home() + "v" + spiel.away() + "m" + minute + "s" + scorer.getSquadNumber() + "a" + assistgeber.getSquadNumber();
	}

	public Spiel getSpiel() {
		return spiel;
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
	
}


