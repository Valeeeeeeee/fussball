package model;

import java.util.ArrayList;

public interface Wettbewerb {

	public int getID();
	public int getYear();
	public String getDescription();
	public boolean isSTSS();
	public String getWorkspace();
	public int getCurrentMatchday();
	public int getNewestStartedMatchday();
	public int getNumberOfMatchdays();
	public int getNumberOfMatchesAgainstSameOpponent();
	public String getMatchdayDescription(int matchday);
	
	public boolean isMatchSet(int matchday, int matchID);
	public Spiel getMatch(int matchday, int matchID);
	public void setMatch(int matchday, int matchID, Spiel match);
	
	public boolean isResultSet(int matchday, int matchID);
	public Ergebnis getResult(int matchday, int matchID);
	public void setResult(int matchday, int matchID, Ergebnis result);
	
	public Datum getDate(int matchday, int matchID);
	public Uhrzeit getTime(int matchday, int matchID);
	
	public ArrayList<Schiedsrichter> getReferees();
	public String[] getAllReferees();
	
	public Mannschaft[] getTeams();
	
	public boolean isETPossible();
	
	/** 
	 * When two teams have the same amount of points:<br />
	 * If true, the goal difference comes next.<br />
	 * If false, all matches between these two teams are compared.
	 * */
	public boolean useGoalDifference();
	public boolean teamsHaveKader();
	
}
