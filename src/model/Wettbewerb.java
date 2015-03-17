package model;

public interface Wettbewerb {

	public String getMatchdayDescription(int matchday);
	public boolean isSpielplanEntered(int matchday, int match);
	public Spiel getSpiel(int matchday, int match);
	public Mannschaft[] getMannschaften();
	public boolean isETPossible();
	
}
