package model;

public interface Wettbewerb {

	public String getMatchdayDescription(int matchday);
	
	public boolean isSpielplanEntered(int matchday, int match);
	public Spiel getSpiel(int matchday, int match);
	public void setSpiel(int matchday, int match, Spiel spiel);
	
	public boolean isErgebnisplanEntered(int matchday, int match);
	public Ergebnis getErgebnis(int matchday, int match);
	public void setErgebnis(int matchday, int match, Ergebnis ergebnis);
	
	public int getDate(int matchday, int match);
	public int getTime(int matchday, int match);
	
	public Mannschaft[] getMannschaften();
	
	public boolean isETPossible();
	
}
