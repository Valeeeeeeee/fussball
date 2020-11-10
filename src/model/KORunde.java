package model;

import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class KORunde implements Wettbewerb {

	private boolean belongsToALeague;
	private LigaSaison lSeason;
	private TurnierSaison tSeason;
	private int id;
	private boolean isQ;
	private String name;
	private String shortName;
	
	private int numberOfTeams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int numberOfMatchdays;
	private int currentMatchday;
	private Datum cMatchdaySetForDate = MIN_DATE;
	private int newestMatchday;
	private Datum nMatchdaySetForDate = MIN_DATE;
	private Uhrzeit nMatchdaySetUntilTime = TIME_UNDEFINED;
	private Mannschaft[] teams;
	private int numberOfTeamsPrequalified;
	private int numberOfTeamsFromPreviousRound;
	private int numberOfTeamsFromOtherCompetition;
	private boolean checkTeamsFromPreviousRound = true;
	
	private boolean hasSecondLeg;
	private boolean isETPossible = true;
	private boolean is4thSubPossible = false;
	private boolean goalDifference = true;
	private boolean fairplay = false;
	private boolean teamsHaveKader;
	
	private boolean teamsAreWinners;
	private String[] teamsOrigins;
	
	private Spiel[][] matches;
	private boolean[][] matchesSet;
	
	private Ergebnis[][] results;
	private boolean[][] resultsSet;
	
	private String workspace;
	
	private String fileTeams;
	private ArrayList<String> teamsFromFile;
	
	private String fileMatches;
	private ArrayList<String> matchesFromFile;
	
	private String fileResults;
	private ArrayList<String> resultsFromFile;
	
	private String fileMatchData;
	private ArrayList<String> matchDataFromFile;
	
	private Datum startDate;
	private Datum finalDate;
	private int[][] daysSinceFirstDay;
	private Uhrzeit[][] startTime;
	
	private Spieltag spieltag;
	
	public KORunde(LigaSaison season, String data) {
		belongsToALeague = true;
		this.lSeason = season;
		this.id = 0;
		this.isQ = false;
		
		startDate = season.getStartDate();
		finalDate = season.getFinalDate();
		teamsHaveKader = season.teamsHaveKader();
		is4thSubPossible = false;
		
		fromString(data);
		
		load();
	}
	
	public KORunde(TurnierSaison season, int id, boolean isQ, String data) {
		belongsToALeague = false;
		this.tSeason = season;
		this.id = id;
		this.isQ = isQ;
		
		startDate = season.getStartDate(isQ);
		finalDate = season.getFinalDate(isQ);
		teamsHaveKader = season.teamsHaveKader(isQ);
		is4thSubPossible = season.isFourthSubPossible();
		
		fromString(data);
		
		load();
	}
	
	public int getID() {
		return id;
	}
	
	public boolean belongsToALeague() {
		return belongsToALeague;
	}
	
	public int getNumberOfMatchdaysBeforePlayoff() {
		return belongsToALeague ? lSeason.getNumberOfMatchdays() : 0;
	}
	
	public int getYear() {
		return belongsToALeague ? lSeason.getYear() : tSeason.getYear();
	}
	
	public boolean isQualification() {
		return isQ;
	}
	
	public String getDescription() {
		return name;
	}
	
	public boolean isSTSS() {
		return belongsToALeague ? lSeason.isSTSS() : tSeason.isSTSS();
	}
	
	public boolean isClubCompetition() {
		return belongsToALeague ? lSeason.isClubCompetition() : tSeason.isClubCompetition();
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public String getMatchdayDescription(int matchday) {
		return (belongsToALeague ? lSeason.getDescription() : tSeason.getDescription()) + ", " + name + (numberOfMatchdays != 1 ? ", " + (matchday == 0 ? "Hinspiel" : "Rückspiel") : "");
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public Dauer getDuration() {
		return new Dauer(startDate, finalDate);
	}
	
	public Datum getStartDate() {
		return startDate;
	}
	
	public Datum getFinalDate() {
		return finalDate;
	}
	
	public Spieltag getSpieltag() {
		return spieltag;
	}
	
	public boolean isExtraTimePossible() {
		return isETPossible;
	}
	
	public int getNumberOfRegularSubstitutions(Datum date) {
		return belongsToALeague ? lSeason.getNumberOfRegularSubstitutions(date) : tSeason.getNumberOfRegularSubstitutions(date);
	}
	
	public boolean isFourthSubstitutionPossible() {
		return is4thSubPossible;
	}
	
	public boolean useGoalDifference() {
		return goalDifference;
	}
	
	public boolean useFairplayRule() {
		return fairplay;
	}
	
	public String[] getMatchdays() {
		if (belongsToALeague)	return lSeason.getMatchdays();
		
		String[] matchdays = new String[numberOfMatchdays];
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdays[i] = (i + 1) + ". Spieltag";
		}
		return matchdays;
	}
	
	public void setCheckTeamsFromPreviousRound(boolean check) {
		checkTeamsFromPreviousRound = check;
	}
	
	public ArrayList<Schiedsrichter> getReferees() {
		return belongsToALeague ? lSeason.getReferees() : tSeason.getReferees();
	}
	
	public String[] getAllReferees() {
		return belongsToALeague ? lSeason.getAllReferees() : tSeason.getAllReferees();
	}

	public Mannschaft[] getTeams() {
		if (checkTeamsFromPreviousRound)	refreshTeams();
		return teams;
	}
	
	public Mannschaft getTeamWithName(String teamName) {
		for (Mannschaft team : teams) {
			if (team != null && team.getName().equals(teamName))	return team;	
		}
		return null;
	}
	
	public ArrayList<String[]> getAllMatches(Mannschaft team) {
		return belongsToALeague ? lSeason.getAllMatches(team) : tSeason.getAllMatches(team);
	}
	
	public ArrayList<String[]> getMatches(Mannschaft team) {
		ArrayList<String[]> allMatches = new ArrayList<>();
		
		boolean foundTeam = false;
		for (int i = 0; i < teams.length && !foundTeam; i++) {
			foundTeam = team.equals(teams[i]);
		}
		if (foundTeam) {
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String md = numberOfMatchdays == 1 ? shortName : matchday + 1 + "";
				String date = getDateOfTeam(matchday, team);
				String goalsH = "-", goalsA = "-";
				String sunx = RESULT_NOT_SET;
				boolean allMatchesSet = true, teamFound = false;
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday && !teamFound; matchID++) {
					if (isMatchSet(matchday, matchID)) {
						Spiel match = getMatch(matchday, matchID);
						if (teamFound = team.equals(match.getHomeTeam())) {
							if (isResultSet(matchday, matchID) && !getResult(matchday, matchID).isCancelled()) {
								goalsH = "" + getResult(matchday, matchID).home();
								goalsA = "" + getResult(matchday, matchID).away();
								sunx = getSUN(getResult(matchday, matchID).home(), getResult(matchday, matchID).away());
							}
							allMatches.add(new String[] {md, date, team.getName(), goalsH, goalsA, match.getAwayTeam().getName(), sunx});
						} else if (teamFound = team.equals(match.getAwayTeam())) {
							if (isResultSet(matchday, matchID) && !getResult(matchday, matchID).isCancelled()) {
								goalsH = "" + getResult(matchday, matchID).home();
								goalsA = "" + getResult(matchday, matchID).away();
								sunx = getSUN(getResult(matchday, matchID).away(), getResult(matchday, matchID).home());
							}
							allMatches.add(new String[] {md, date, match.getHomeTeam().getName(), goalsH, goalsA, team.getName(), sunx});
						}
					}
					else	allMatchesSet = false;
				}
				if (!teamFound) {
					if (allMatchesSet)	allMatches.add(new String[] {md, date, SPIELFREI, goalsH, goalsA, SPIELFREI, sunx});
					else				allMatches.add(new String[] {md, date, MATCH_NOT_SET, goalsH, goalsA, MATCH_NOT_SET, sunx});
				}
			}
		}
		
		return allMatches;
	}
	
	public String getDateOfTeam(int matchday, Mannschaft team) {
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isMatchSet(matchday, matchID)) {
				if (getMatch(matchday, matchID).getHomeTeam().equals(team) || getMatch(matchday, matchID).getAwayTeam().equals(team))
					return getDateAndTime(matchday, matchID);
			}
		}
		
		return "n.a.";
	}
	
	public String getTeamsOrigin(int team) {
		return teamsOrigins[team];
	}
	
	public Mannschaft getInvariantTeam(int index) {
		if (index >= 0 && index < numberOfTeamsPrequalified)										return teams[index];
		if (index < numberOfTeams && index + numberOfTeamsFromOtherCompetition >= numberOfTeams)	return teams[index];
		else																						return null;
	}
	
	private void refreshTeams() {
		String[] partOfOrigins = new String[numberOfTeamsFromPreviousRound];
		for (int i = 0; i < partOfOrigins.length; i++) {
			partOfOrigins[i] = teamsOrigins[numberOfTeamsPrequalified + i];
		}
		Mannschaft[] prevRoundTeams;
		if (belongsToALeague)	prevRoundTeams = lSeason.getTeamsInOrderOfOrigins(partOfOrigins);
		else					prevRoundTeams = tSeason.getTeamsInOrderOfOrigins(partOfOrigins, teamsAreWinners, id, isQ);
		
		for (int i = 0; i < numberOfTeamsFromPreviousRound; i++) {
			teams[i + numberOfTeamsPrequalified] = prevRoundTeams[i];
		}
	}
	
	public int getNumberOfTeams() {
		return numberOfTeams;
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return numberOfMatchesPerMatchday;
	}
	
	public boolean hasSecondLeg() {
		return hasSecondLeg;
	}
	
	public boolean teamsAreWinners() {
		return teamsAreWinners;
	}
	
	public int getNumberOfMatchdays() {
		return numberOfMatchdays;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return numberOfMatchesAgainstSameOpponent;
	}
	
	public boolean teamsHaveKader() {
		return teamsHaveKader;
	}
	
	public int getCurrentMatchday() {
		if (numberOfMatchdays == 2) {
			if (!today.equals(cMatchdaySetForDate)) {
				if (getDate(0, 0).equals(startDate)) {
					currentMatchday = 0;
				} else if (!today.isAfter(getDate(0, 0))) {
					currentMatchday = 0;
				} else if (!today.isBefore(getDate(1, 0)) && !isNoMatchSet(1)) {
					currentMatchday = 1;
				} else {
					currentMatchday = 1;
					
					if (getDate(0, 0).daysUntil(today) < today.daysUntil(getDate(1, 0))) {
						currentMatchday--;
					}
				}
				cMatchdaySetForDate = today;
			}
		} else if (numberOfMatchdays == 1) {
			currentMatchday = 0;
		}
		
		return currentMatchday;
	}
	
	public int getNewestStartedMatchday() {
		if (numberOfMatchdays == 2) {
			Uhrzeit time = new Uhrzeit();
			
			if (!today.equals(nMatchdaySetForDate) || !time.isBefore(nMatchdaySetUntilTime)) {
				nMatchdaySetUntilTime = END_OF_DAY;
				if (!today.isAfter(getDate(0, 0))) {
					newestMatchday = 0;
				} else if (!today.isBefore(getDate(numberOfMatchdays - 1, 0))) {
					newestMatchday = numberOfMatchdays - 1;
				} else {
					newestMatchday = 0;
					while (today.isAfter(getDate(newestMatchday + 1, 0)) || (today.equals(getDate(newestMatchday + 1, 0)) && !time.isBefore(getTime(newestMatchday + 1, 0)))) {
						newestMatchday++;
					}
					if (today.equals(getDate(newestMatchday + 1, 0))) {
						nMatchdaySetUntilTime = getTime(newestMatchday + 1, 0);
					}
				}
				
				nMatchdaySetForDate = today;
			}
		} else if (numberOfMatchdays == 1) {
			newestMatchday = 0;
		}
		
		return newestMatchday;
	}
	
	public String getDateAndTime(int matchday, int matchID) {
		if (matchday >= 0 && matchday < numberOfMatchdays && matchID >= 0 && matchID < numberOfMatchesPerMatchday && getDate(matchday, matchID) != MAX_DATE)
			return getDate(matchday, matchID).withDividers() + " " + getTime(matchday, matchID).withDividers();
		else 
			return "nicht terminiert";
	}
	
	public Datum getDate(int matchday, int matchID) {
		if (daysSinceFirstDay[matchday][matchID] == UNDEFINED)	return MAX_DATE;
		return new Datum(startDate, daysSinceFirstDay[matchday][matchID]);
	}
	
	public Uhrzeit getTime(int matchday, int matchID) {
		return startTime[matchday][matchID];
	}
	
	public void setDate(int matchday, int matchID, Datum myDate) {
		if (myDate == MAX_DATE)	daysSinceFirstDay[matchday][matchID] = UNDEFINED;
		else					daysSinceFirstDay[matchday][matchID] = startDate.daysUntil(myDate);
	}
	
	public void setTime(int matchday, int matchID, Uhrzeit myTime) {
		startTime[matchday][matchID] = myTime;
	}
	
	// Spielplan eingetragen
	
	public boolean isNoMatchSet(int matchday) {
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isMatchSet(matchday, matchID)) 	return false;
		}
		return true;
	}
	
	public boolean isMatchSet(int matchday, int matchID) {
		return matchesSet[matchday][matchID];
	}
		
	public void setMatchSet(int matchday, int matchID, boolean isSet) {
		matchesSet[matchday][matchID] = isSet;
	}
	
	public String getMatchesSetRepresentation(int matchday) {
		String representation = "";
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isMatchSet(matchday, matchID))	representation += "t";
			else								representation += "f";
		}
		
		return representation;
	}
	
	public void setMatchesSetFromRepresentation(int matchday, String representation) {
		if (representation.equals("true")) {
			representation = "";
			for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++)	representation += "t";
		} else if (representation.equals("false")) {
			representation = "";
			for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++)	representation += "f";
		}
		
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (representation.charAt(matchID) == 't')		setMatchSet(matchday, matchID, true);
			else if (representation.charAt(matchID) == 'f')	setMatchSet(matchday, matchID, false);
		}
	}
	
	// Ergebnisplan eingetragen
	
	public boolean isNoResultSet(int matchday) {
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isResultSet(matchday, matchID)) 	return false;
		}
		return true;
	}
	
	public boolean isResultSet(int matchday, int matchID) {
		return resultsSet[matchday][matchID];
	}
	
	public void setResultSet(int matchday, int matchID, boolean isSet) {
		resultsSet[matchday][matchID] = isSet;
	}
	
	public String getResultsSetRepresentation(int matchday) {
		String representation = "";
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isResultSet(matchday, matchID))	representation += "t";
			else								representation += "f";
		}
		
		return representation;
	}
	
	public void setResultsSetFromRepresentation(int matchday, String representation) {
		if (representation.equals("true")) {
			representation = "";
			for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++)	representation += "t";
		} else if (representation.equals("false")) {
			representation = "";
			for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++)	representation += "f";
		}
		
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (representation.charAt(matchID) == 't')		setResultSet(matchday, matchID, true);
			else if (representation.charAt(matchID) == 'f')	setResultSet(matchday, matchID, false);
		}
	}
	
	// Ergebnisplan
	
	public Ergebnis getResult(int matchday, int matchID) {
		return results[matchday][matchID];
	}
	
	public void setResult(int matchday, int matchID, Ergebnis result) {
		if (result != null)	setResultSet(matchday, matchID, true);
		else					setResultSet(matchday, matchID, false);
		results[matchday][matchID] = result;
		if (isMatchSet(matchday, matchID))	getMatch(matchday, matchID).setResult(result);
	}
	
	// Spielplan
	
	public Spiel getMatch(int matchday, int matchID) {
		return matches[matchday][matchID];
	}
	
	public void setMatch(int matchday, int matchID, Spiel match) {
		if (match != null)	setMatchSet(matchday, matchID, true);
		else				setMatchSet(matchday, matchID, false);
		matches[matchday][matchID] = match;
	}
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[numberOfMatchesPerMatchday];
		Datum[] dates = new Datum[numberOfMatchesPerMatchday];
		Uhrzeit[] times = new Uhrzeit[numberOfMatchesPerMatchday];
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			dates[matchID] = getDate(matchday, matchID);
			times[matchID] = getTime(matchday, matchID);
		}
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (dates[m2].isAfter(dates[m]))		hilfsarray[m2]++;
				else if (dates[m2].isBefore(dates[m]))	hilfsarray[m]++;
				else if (times[m2].isAfter(times[m]))	hilfsarray[m2]++;
				else if (times[m2].isBefore(times[m]))	hilfsarray[m]++;
				else {
					Spiel sp1 = getMatch(matchday, m), sp2 = getMatch(matchday, m2);
					if (sp1 != null && sp2 != null && sp1.home() > sp2.home())	hilfsarray[m]++;
					else	hilfsarray[m2]++;
				}
			}
		}
		
		for (int i = 0; i < hilfsarray.length; i++) {
			newOrder[hilfsarray[i]] = i;
		}
		
		changeOrderOfMatches(matchday, newOrder);
	}
	
	public void changeOrderOfMatches(int matchday, int[] oldIndicesInNewOrder) {
		// check the correctness of the parameter array
		if (oldIndicesInNewOrder.length != numberOfMatchesPerMatchday) {
			errorMessage("The parameter array does not have the correct length.");
			return;
		}
		
		boolean[] checked = new boolean[oldIndicesInNewOrder.length];
		try {
			for (int i = 0; i < checked.length; i++) {
				checked[oldIndicesInNewOrder[i]] = !checked[oldIndicesInNewOrder[i]];
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			errorMessage("Array contains irregular match indices.");
			return;
		}
		
		for (int i = 0; i < checked.length; i++) {
			if (!checked[i]) {
				errorMessage("Irregular distribution of indices, not every index has appeared.");
				return;
			}
		}
		
		// duplicate old arrays and set new ones
		Spiel[] oldMatches = new Spiel[numberOfMatchesPerMatchday];
		Ergebnis[] oldResults = new Ergebnis[numberOfMatchesPerMatchday];
		Datum[] oldDates = new Datum[numberOfMatchesPerMatchday];
		Uhrzeit[] oldStartTimes = new Uhrzeit[numberOfMatchesPerMatchday];
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			oldMatches[matchID] = getMatch(matchday, matchID);
			oldResults[matchID] = getResult(matchday, matchID);
			oldDates[matchID] = getDate(matchday, matchID);
			oldStartTimes[matchID] = getTime(matchday, matchID);
		}
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			setMatch(matchday, matchID, oldMatches[oldIndicesInNewOrder[matchID]]);
			setResult(matchday, matchID, oldResults[oldIndicesInNewOrder[matchID]]);
			setDate(matchday, matchID, oldDates[oldIndicesInNewOrder[matchID]]);
			setTime(matchday, matchID, oldStartTimes[oldIndicesInNewOrder[matchID]]);
		}
	}
	
	public void getResultsFromSpieltag() {
		int matchday = spieltag.getCurrentMatchday();
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isMatchSet(matchday, matchID)) {
				Ergebnis result = spieltag.getResult(matchID);
				
				setResult(matchday, matchID, result);
//				TODO aktuell werden fuer KO-Phasen Matches keine Ergebnisse an die Mannschaften weitergeleitet
//				mannschaften[spiele[matchday][match][0] - 1].setResult(matchday, result);
//				mannschaften[spiele[matchday][match][1] - 1].setResult(matchday, result);
			}
		}
	}
	
	/**
	 * Returns a String representing the origin of the team winning this match/these matches.
	 * @param match The index of the match, beginning from 1
	 * @return
	 */
	public String getOriginOfWinnerOf(int matchID) {
		int index;
		return ((index = getIndexOf(matchID, true)) == 0 ? null : teamsOrigins[index - 1]);
	}
	
	/**
	 * Returns a String representing the origin of the team losing this match/these matches.
	 * @param match The index of the match, beginning from 1
	 * @return
	 */
	public String getOriginOfLoserOf(int matchID) {
		int index;
		return ((index = getIndexOf(matchID, false)) == 0 ? null : teamsOrigins[index - 1]);
	}
	
	public int getIndexOf(int matchID, boolean isWinnerRequested) {
		if (!isResultSet(0, matchID - 1)) {
			return 0;
		}
		
		// find out involved teams
		int teamHomeFirstLeg = getMatch(0, matchID - 1).home();
		int teamAwayFirstLeg = getMatch(0, matchID - 1).away();
		Ergebnis firstLeg = getResult(0, matchID - 1);
		
		if (hasSecondLeg) {
			// first and second leg don't have to be in the same position on the plan and most likely they aren't!!
			
			// get index of second leg match
			int index = UNDEFINED;
			for (int i = 0; i < numberOfMatchesPerMatchday && index == UNDEFINED; i++) {
				if (getMatch(1, i) != null && (getMatch(1, i).home() == teamHomeFirstLeg || getMatch(1, i).home() == teamAwayFirstLeg)) {
					index = i + 1;
				}
			}
			
			if (index != UNDEFINED && isResultSet(numberOfMatchdays - 1, index - 1)) {
				Ergebnis secondLeg = getResult(1, index - 1);
				if (firstLeg.home() + secondLeg.away() > firstLeg.away() + secondLeg.home()) {
					return isWinnerRequested ? teamHomeFirstLeg : teamAwayFirstLeg;
				} else if (firstLeg.home() + secondLeg.away() < firstLeg.away() + secondLeg.home()) {
					return isWinnerRequested ? teamAwayFirstLeg : teamHomeFirstLeg;
				} else {
					// looking for a winner through the away-goal rule (e.g. 1:2 and 2:3)
					if (firstLeg.away() > secondLeg.away()) {
						return isWinnerRequested ? teamAwayFirstLeg : teamHomeFirstLeg;
					} else if (firstLeg.away() < secondLeg.away()) {
						return isWinnerRequested ? teamHomeFirstLeg : teamAwayFirstLeg;
					}
				}
			}
		} else {
			if (firstLeg.home() > firstLeg.away()) {
				return isWinnerRequested ? teamHomeFirstLeg : teamAwayFirstLeg;
			} else if (firstLeg.home() < firstLeg.away()) {
				return isWinnerRequested ? teamAwayFirstLeg : teamHomeFirstLeg;
			}
		}
		
		// if there is no second leg / result is tied / other problem
		return 0;
	}

	public String[] getRanks() {
		String[] ranks = new String[2 * numberOfMatchesPerMatchday];
		String competition = belongsToALeague ? lSeason.getLeague().getShortName() + lSeason.getYear() : tSeason.getTournament().getShortName() + tSeason.getYear();
		
		for (int i = 0; i < numberOfMatchesPerMatchday; i++) {
			String matchID = getShortName() + ((i + 1) / 10) + ((i + 1) % 10);
			ranks[2 * i] = matchID + "W" + ": ";
			ranks[2 * i + 1] = matchID + "L" + ": ";
			int index = getIndexOf(i + 1, true);
			if (index != 0)	ranks[2 * i] += teams[index - 1].getName();
			else			ranks[2 * i] += competition + matchID + "W";
			
			index = getIndexOf(i + 1, false);
			if (index != 0)	ranks[2 * i + 1] += teams[index - 1].getName();
			else			ranks[2 * i + 1] += competition + matchID + "L";
		}
		
		return ranks;
	}
	
	public ArrayList<Long> getNextMatches() {
		ArrayList<Long> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				if (isResultSet(i, j) && getResult(i, j).isCancelled())	continue;
				Datum date = getDate(i, j);
				Uhrzeit time = getTime(i, j);
				if (isMatchSet(i, j) && (!inThePast(date, time, 105) || !isResultSet(i, j))) {
					long dateAndTime = 10000L * date.comparable() + time.comparable();
					if (nextMatches.size() < Fussball.numberOfMissingResults || dateAndTime < nextMatches.get(Fussball.numberOfMissingResults - 1)) {
						int index = nextMatches.size();
						for (int k = 0; k < nextMatches.size() && index == nextMatches.size(); k++) {
							if (dateAndTime < nextMatches.get(k))	index = k;
						}
						nextMatches.add(index, dateAndTime);
					}
				}
			}
		}
		
		return nextMatches;
	}
	
	private void load() {
		String isQuali = isQ ? "Qualifikation" + File.separator : "";
		workspace = (belongsToALeague ? lSeason.getWorkspace() : tSeason.getWorkspace()) + isQuali + name + File.separator;
		
		fileResults = workspace + "Ergebnisse.txt";
		fileTeams = workspace + "Mannschaften.txt";
		fileMatchData = workspace + "Spieldaten.txt";
		fileMatches = workspace + "Spielplan.txt";
		
		loadTeams();
		initializeArrays();
		
		setCheckTeamsFromPreviousRound(false);
		loadMatches();
		setCheckTeamsFromPreviousRound(true);
		loadResults();
		loadMatchData();
		
		if (spieltag == null) {
			spieltag = new Spieltag(this);
			spieltag.setLocation((Fussball.WIDTH - spieltag.getSize().width) / 2, (Fussball.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
		}
	}
	
	public void save() {
		saveMatches();
		saveResults();
		saveMatchData();
		saveTeams();
	}
	
	private void initializeArrays() {
		daysSinceFirstDay = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		startTime = new Uhrzeit[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		matches = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		matchesSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		results = new Ergebnis[numberOfMatchdays][numberOfMatchesPerMatchday];
		resultsSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
	}
	
	private void loadTeams() {
		teamsFromFile = ausDatei(fileTeams);
		
		numberOfTeams = teamsFromFile.size();
		teams = new Mannschaft[numberOfTeams];
		numberOfMatchesAgainstSameOpponent = hasSecondLeg ? 2 : 1;
		numberOfMatchdays = hasSecondLeg ? 2 : 1;
		numberOfMatchesPerMatchday = numberOfTeams / 2;
		
		teamsOrigins = new String[numberOfTeams];
		for (int i = 0; i < teamsOrigins.length; i++) {
			teamsOrigins[i] = teamsFromFile.get(i);
		}
		
		for (int i = 0; i < numberOfTeamsPrequalified; i++) {
			teams[i] = new Mannschaft(i, this, teamsOrigins[i]);
		}
		
		// testGNOTFOC();
		
		for (int i = numberOfTeams - numberOfTeamsFromOtherCompetition; i < numberOfTeams; i++) {
			if (belongsToALeague)	teams[i] = lSeason.getTeamFromOtherCompetition(i, this, teamsOrigins[i]);
			else					teams[i] = tSeason.getTeamFromOtherCompetition(i, this, teamsOrigins[i]);
		}
		
		refreshTeams();
	}
	
	public void saveTeams() {
		teamsFromFile = new ArrayList<>();
		for (int i = 0; i < numberOfTeams; i++) {
			if (teams[i] != null)	teams[i].save();
			teamsFromFile.add(teamsOrigins[i]); //.toString());
		}
		inDatei(fileTeams, teamsFromFile);
	}
	
	private void loadMatches() {
		try {
			matchesFromFile = ausDatei(fileMatches);
			
			for (int matchday = 0; matchday < matchesFromFile.size(); matchday++) {
				String[] split = matchesFromFile.get(matchday).split(";");
				
				setMatchesSetFromRepresentation(matchday, split[0]);
				
				int matchID = 0;
				if (!isNoMatchSet(matchday)) {
					// Spieltagsdaten
					String[] koTimes = split[1].split(":");
					for (matchID = 0; matchID < koTimes.length; matchID++) {
						if (koTimes[matchID].equals(TO_BE_DATED)) {
							daysSinceFirstDay[matchday][matchID] = UNDEFINED;
							setTime(matchday, matchID, TIME_UNDEFINED);
						} else {
							String[] dateAndTime = koTimes[matchID].split(",");
							daysSinceFirstDay[matchday][matchID] = Integer.parseInt(dateAndTime[0]);
							setTime(matchday, matchID, new Uhrzeit(dateAndTime[1]));
						}
					}
					
					// Herkunften der Mannschaften
					for (matchID = 0; matchID + 2 < split.length; matchID++) {
						Spiel match = null;
						if (isMatchSet(matchday, matchID)) {
							match = new Spiel(this, matchday, getDate(matchday, matchID), getTime(matchday, matchID), split[matchID + 2]);
						}
						
						setMatch(matchday, matchID, match);
					}
				}
				
				while(matchID < numberOfMatchesPerMatchday) {
					setMatch(matchday, matchID, null);
					daysSinceFirstDay[matchday][matchID] = UNDEFINED;
					setTime(matchday, matchID, TIME_UNDEFINED);
					matchID++;
				}
			}
		} catch (Exception e) {
			errorMessage(name + " - Kein Spielplan: " + e.getClass());
			e.printStackTrace();
		}
	}
	
	private void saveMatches() {
		matchesFromFile = new ArrayList<>();
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			String row = getMatchesSetRepresentation(matchday) + ";";
			if (!isNoMatchSet(matchday)) {
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					if (daysSinceFirstDay[matchday][matchID] == UNDEFINED && getTime(matchday, matchID).equals(TIME_UNDEFINED))	row += TO_BE_DATED;
					else	row += daysSinceFirstDay[matchday][matchID] + "," + getTime(matchday, matchID).comparable();
					if (matchID + 1 < numberOfMatchesPerMatchday)	row += ":";
					else											row += ";";
				}
				
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					row += getMatch(matchday, matchID) + ";";
				}
			}
			matchesFromFile.add(row);
		}
		
		inDatei(fileMatches, matchesFromFile);
	}
	
	private void loadResults() {
		try {
			resultsFromFile = ausDatei(fileResults);
			
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String[] split = resultsFromFile.get(matchday).split(";");
				
				setResultsSetFromRepresentation(matchday, split[0]);
				
				int matchID = 0;
				if (!isNoMatchSet(matchday) && !isNoResultSet(matchday)) {
					for (matchID = 0; (matchID + 1) < split.length; matchID++) {
						if (isMatchSet(matchday, matchID)) {
							Ergebnis result = null;
							if (isResultSet(matchday, matchID))	result = new Ergebnis(split[matchID + 1]);
							
							setResult(matchday, matchID, result);
						}
					}
				}
				
				while (matchID < numberOfMatchesPerMatchday) {
					setResult(matchday, matchID, null);
					matchID++;
				}
			}
		} catch (Exception e) {
			errorMessage("Kein Ergebnisplan: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveResults() {
		resultsFromFile = new ArrayList<>();
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			String row = getResultsSetRepresentation(matchday) + ";";
			if (!isNoResultSet(matchday)) {
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					row += getResult(matchday, matchID) + ";";
				}
			}
			resultsFromFile.add(row);
		}
		
		inDatei(fileResults, resultsFromFile);
	}
	
	private void loadMatchData() {
		if (!teamsHaveKader)	return;
		try {
			matchDataFromFile = ausDatei(fileMatchData);
			
			for (int matchday = 0; matchday < numberOfMatchdays && matchday < matchDataFromFile.size(); matchday++) {
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					String matchData = matchDataFromFile.get(matchday * numberOfMatchesPerMatchday + matchID);
					if (isMatchSet(matchday, matchID)) {
						getMatch(matchday, matchID).setMatchData(matchData);
					}
				}
			}
		} catch (Exception e) {
			errorMessage("Keine Spieldaten: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveMatchData() {
		matchDataFromFile.clear();
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
				matchDataFromFile.add(getMatch(matchday, matchID) != null ? getMatch(matchday, matchID).fullString() : "null");
			}
		}
		
		inDatei(fileMatchData, matchDataFromFile);
	}
	
	public String toString() {
		String toString = name + ";";
		toString += shortName + ";";
		toString += teamsAreWinners + ";";
		toString += hasSecondLeg + ";";
		toString += numberOfTeamsPrequalified + ";";
		toString += numberOfTeamsFromPreviousRound + ";";
		toString += numberOfTeamsFromOtherCompetition + ";";
		
		return toString;
	}
	
	private void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		name = split[index++];
		shortName = split[index++];
		teamsAreWinners = Boolean.parseBoolean(split[index++]);
		hasSecondLeg = Boolean.parseBoolean(split[index++]);
		numberOfTeamsPrequalified = Integer.parseInt(split[index++]);
		numberOfTeamsFromPreviousRound = Integer.parseInt(split[index++]);
		numberOfTeamsFromOtherCompetition = Integer.parseInt(split[index++]);
	}
}
