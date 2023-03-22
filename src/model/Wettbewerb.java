package model;

import java.util.ArrayList;

public interface Wettbewerb {

	public int getID();
	public int getYear();
	public String getDescription();
	public boolean isSTSS();
	public boolean isClubCompetition();
	public String getWorkspace();
	public int getCurrentMatchday();
	public int getNewestStartedMatchday();
	public int getNumberOfMatchdays();
	public int getNumberOfMatchesAgainstSameOpponent();
	public String getMatchdayDescription(int matchday);
	public String[] getMatchdays();
	
	public String getKey(int matchday);
	
	public Dauer getDuration();
	public Datum getStartDate();
	public Datum getFinalDate();
	
	public boolean isMatchSet(int matchday, int matchIndex);
	public Spiel getMatch(int matchday, int matchIndex);
	public void setMatch(int matchday, int matchIndex, Spiel match);
	
	public boolean isResultSet(int matchday, int matchIndex);
	public Ergebnis getResult(int matchday, int matchIndex);
	public void setResult(int matchday, int matchIndex, Ergebnis result);
	
	public boolean allResultsSet();
	
	public AnstossZeit getKickOffTime(int matchday, int matchIndex);
	
	public Tabelle getTable();
	
	public ArrayList<Schiedsrichter> getReferees();
	public String[] getAllReferees();
	
	public ArrayList<Mannschaft> getTeams();
	
	public ArrayList<String[]> getAllMatches(Mannschaft team);
	
	public boolean isExtraTimePossible();
	public int getNumberOfRegularSubstitutions(Datum date);
	public boolean isFourthSubstitutionPossible();
	
	public ArrayList<RankingCriterion> getRankingCriteria();
	
	public boolean teamsHaveKader();
	
}
