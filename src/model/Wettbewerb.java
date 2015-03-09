package model;

public interface Wettbewerb {

	public Spiel getSpiel(int matchday, int match);
	public Mannschaft[] getMannschaften();
	public boolean isETPossible();
	
}


