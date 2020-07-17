package model;
import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class Gruppe implements Wettbewerb {
	private boolean debug = false;
	private int id;
	private boolean isQ;
	private String name;
	
	private int numberOfTeams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int numberOfMatchdays;
	private int currentMatchday;
	private Datum cMatchdaySetForDate = MIN_DATE;
	private boolean cMatchdaySetForOverview;
	private int newestMatchday;
	private Datum nMatchdaySetForDate = MIN_DATE;
	private Uhrzeit nMatchdaySetUntilTime = TIME_UNDEFINED;
	private Mannschaft[] teams;
	private TurnierSaison season;
	private boolean goalDifference;
	private boolean fairplay;
	private boolean teamsHaveKader;
	
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
	private Tabelle tabelle;
	
	public Gruppe(TurnierSaison season, int id, boolean isQ, boolean goalDifference, boolean fairplay) {
		this.id = id;
		this.isQ = isQ;
		name = "Gruppe " + alphabet[id];
		
		this.season = season;
		startDate = season.getStartDate(isQ);
		finalDate = season.getFinalDate(isQ);
		this.goalDifference = goalDifference;
		this.fairplay = fairplay;
		teamsHaveKader = season.teamsHaveKader(isQ);
		
		load();
		
		if (debug)	testAusgabePlatzierungen();
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public int getID() {
		return id;
	}
	
	public int getYear() {
		return season.getYear();
	}
	
	public boolean isQualification() {
		return isQ;
	}
	
	public String getMatchdayDescription(int matchday) {
		String isQuali = isQ ? ", Qualifikation" : "";
		return season.getDescription() + isQuali + ", " + name + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public String getDescription() {
		return name;
	}
	
	public boolean isSTSS() {
		return season.isSTSS();
	}
	
	public boolean isClubCompetition() {
		return season.isClubCompetition();
	}
	
	public String getTournamentName() {
		return season.getTournament().getName();
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return numberOfMatchesPerMatchday;
	}
	
	public int getNumberOfTeams() {
		return numberOfTeams;
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
	
	public Dauer getDuration() {
		return new Dauer(startDate, finalDate);
	}
	
	public Datum getStartDate() {
		return startDate;
	}
	
	public Datum getFinalDate() {
		return finalDate;
	}
	
	public String[] getMatchdays() {
		String[] matchdays = new String[numberOfMatchdays];
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdays[i] = (i + 1) + ". Spieltag";
		}
		return matchdays;
	}
	
	public Spieltag getSpieltag() {
		return spieltag;
	}
	
	public Tabelle getTabelle() {
		return tabelle;
	}
	
	public ArrayList<Schiedsrichter> getReferees() {
		return season.getReferees();
	}
	
	public String[] getAllReferees() {
		return season.getAllReferees();
	}
	
	public Mannschaft[] getTeams() {
		return teams;
	}
	
	public boolean isExtraTimePossible() {
		return false;
	}
	
	public int getNumberOfRegularSubstitutions(Datum date) {
		return season.getNumberOfRegularSubstitutions(date);
	}
	
	public boolean isFourthSubstitutionPossible() {
		return false;
	}
	
	public boolean useGoalDifference() {
		return goalDifference;
	}
	
	public boolean useFairplayRule() {
		return fairplay;
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
	
	public Mannschaft getTeamOnPlace(int place) {
		if (place < 1 || place > teams.length)	return null;
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int matchID = 0; matchID < getNumberOfMatchesPerMatchday(); matchID++) {
				if (!isResultSet(matchday, matchID)) 	return null;
			}
		}
		
		for (Mannschaft ms : teams) {
			ms.compareWithOtherTeams(teams, numberOfMatchdays - 1, Tabellenart.COMPLETE);
		}
		for (Mannschaft ms : teams) {
			if (ms.get(0, numberOfMatchdays - 1, Tabellenart.COMPLETE) == place - 1)		return ms;
		}
		
		return null;
	}
	
	public int getIndexOfMannschaft(String name) {
		for (Mannschaft ms : teams) {
			if (ms.getName().equals(name)) {
				return ms.getId();
			}
		}
		return UNDEFINED;
	}
	
	public ArrayList<String[]> getAllMatches(Mannschaft team) {
		return season.getAllMatches(team);
	}
	
	public ArrayList<String[]> getMatches(Mannschaft team) {
		ArrayList<String[]> allMatches = new ArrayList<>();
		
		boolean foundTeam = false;
		for (int i = 0; i < teams.length && !foundTeam; i++) {
			foundTeam = team.equals(teams[i]);
		}
		if (foundTeam) {
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String md = matchday + 1 + "";
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
	
	public Mannschaft getTeamWithName(String teamsName) {
		int index = getIndexOfMannschaft(teamsName);
		if (index == UNDEFINED)	return null;
		return teams[index - 1];
	}
	
	public int getCurrentMatchday() {
		if (!cMatchdaySetForDate.equals(today) || cMatchdaySetForOverview) {
			if (today.isBefore(getDate(0, 0))) {
				currentMatchday = 0;
			} else if (!today.isBefore(getDate(numberOfMatchdays - 1, 0))) {
				currentMatchday = numberOfMatchdays - 1;
			} else {
				currentMatchday = 0;
				while (!today.isBefore(getDate(currentMatchday, 0))) {
					currentMatchday++;
				}
				if (currentMatchday != 0 && getDate(currentMatchday - 1, 0).daysUntil(today) <= today.daysUntil(getDate(currentMatchday, 0))) {
					currentMatchday--;
				}
			}
			cMatchdaySetForDate = today;
			cMatchdaySetForOverview = false;
		}
		
		return currentMatchday;
	}
	
	public int getOverviewMatchday() {
		if (!today.equals(cMatchdaySetForDate) || !cMatchdaySetForOverview) {
			if (today.isBefore(getDate(0, 0))) {
				currentMatchday = 0;
			} else if (!today.isBefore(getDate(numberOfMatchdays - 1, 0)) && !getDate(numberOfMatchdays - 1, 0).equals(startDate)) {
				currentMatchday = numberOfMatchdays - 1;
			} else {
				currentMatchday = 0;
				while (currentMatchday < numberOfMatchdays - 1) {
					boolean allResultsSet = true, allPast = true;
					for (int matchID = 0; matchID < numberOfMatchesPerMatchday && allResultsSet && allPast; matchID++) {
						allResultsSet = allResultsSet && isResultSet(currentMatchday, matchID);
						allPast = allPast && !getDate(currentMatchday, matchID).isAfter(today);
					}
					if (allResultsSet && allPast)	currentMatchday++;
					else							break;
				}
			}
			cMatchdaySetForDate = today;
			cMatchdaySetForOverview = true;
		}
		return currentMatchday;
	}
	
	public int getNewestStartedMatchday() {
		Uhrzeit time = new Uhrzeit();
		
		if (!today.equals(nMatchdaySetForDate) || !time.isBefore(nMatchdaySetUntilTime)) {
			nMatchdaySetUntilTime = END_OF_DAY;
			if (today.isBefore(getDate(0, 0))) {
				newestMatchday = 0;
			} else if (!today.isBefore(getDate(numberOfMatchdays - 1, 0)) && !getDate(numberOfMatchdays - 1, 0).equals(startDate)) {
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
		
		return newestMatchday;
	}
	
	private void testAusgabePlatzierungen() {
		log("\nGruppe " + (id + 1) + ":");
		
		for (int i = 1; i <= teams.length; i++) {
			try {
				log(i + ". " + getTeamOnPlace(i).getName());
			} catch (NullPointerException npe) {
				log("  Mannschaft: " + teams[i - 1].getName());
			}
		}
	}
	
	// Spielplan eingetragen
	
	public boolean isNoMatchSet(int matchday) {
		for (int matchID = 0; matchID < getNumberOfMatchesPerMatchday(); matchID++) {
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
		for (int matchID = 0; matchID < getNumberOfMatchesPerMatchday(); matchID++) {
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
		if (match != null) {
			setMatchSet(matchday, matchID, true);
			teams[match.home() - 1].setMatch(matchday, match);
			teams[match.away() - 1].setMatch(matchday, match);
		}
		else				setMatchSet(matchday, matchID, false);
		matches[matchday][matchID] = match;
	}
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[numberOfMatchesPerMatchday];
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (getDate(matchday, m2).isAfter(getDate(matchday, m)))																hilfsarray[m2]++;
				else if (getDate(matchday, m2).equals(getDate(matchday, m)) && !getTime(matchday, m2).isBefore(getTime(matchday, m)))	hilfsarray[m2]++;
				else																													hilfsarray[m]++;
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
				teams[getMatch(matchday, matchID).home() - 1].setResult(matchday, result);
				teams[getMatch(matchday, matchID).away() - 1].setResult(matchday, result);
			}
		}
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
	
	public void load() {
		String isQuali = isQ ? "Qualifikation" + File.separator : "";
		workspace = season.getWorkspace() + isQuali + name + File.separator;
		
		fileResults = workspace + "Ergebnisse.txt";
		fileMatchData = workspace + "Spieldaten.txt";
		fileMatches = workspace + "Spielplan.txt";
		fileTeams = workspace + "Mannschaften.txt";
		
		loadTeams();
		initializeArrays();
		
		loadMatches();
		loadResults();
		loadMatchData();

		if (spieltag == null) {
			spieltag = new Spieltag(this);
			spieltag.setLocation((Fussball.WIDTH - spieltag.getSize().width) / 2, (Fussball.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
		}
		if (tabelle == null) {
			tabelle = new Tabelle(this);
			tabelle.setLocation((1440 - tabelle.getSize().width) / 2, 50);
			tabelle.setVisible(false);
		}
	}
	
	public void save() {
		saveMatches();
		saveResults();
		saveMatchData();
		saveTeams();
	}
	
	public void loadTeams() {
		teamsFromFile = ausDatei(fileTeams);
		
		numberOfTeams = teamsFromFile.size();
		numberOfMatchesPerMatchday = numberOfTeams / 2;
		numberOfMatchesAgainstSameOpponent = (isQ ? season.hasSecondLegQGroupStage() : season.hasSecondLegGroupStage()) ? 2 : 1;
		numberOfMatchdays = 2 * ((numberOfTeams + 1) / 2) - 1;
		numberOfMatchdays *= numberOfMatchesAgainstSameOpponent;
		teams = new Mannschaft[numberOfTeams];
		
		for (int i = 0; i < teams.length; i++) {
			teams[i] = new Mannschaft(i + 1, this, teamsFromFile.get(i));
		}
	}
	
	public void saveTeams() {
		teamsFromFile = new ArrayList<>();
		for (int i = 0; i < numberOfTeams; i++) {
			teams[i].save();
			teamsFromFile.add(teams[i].toString());
		}
		inDatei(fileTeams, teamsFromFile);
	}
	
	public String[] getRanks() {
		String[] ranks = new String[numberOfTeams];
		
		for (int i = 0; i < ranks.length; i++) {
			String id = (isQ ? "Q" :  "") + "G" + alphabet[this.id] + (i + 1);
			try {
				ranks[i] = id + ": " + getTeamOnPlace(i + 1).getName();
			} catch (NullPointerException npe) {
				ranks[i] = id + ": " + season.getTournament().getShortName() + season.getYear() + id;
			}
		}
		
		return ranks;
	}
	
	private void initializeArrays() {
		// Alle Array werden initialisiert
		
		matches = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		results = new Ergebnis[numberOfMatchdays][numberOfMatchesPerMatchday];
		daysSinceFirstDay = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		startTime = new Uhrzeit[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		matchesSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
		resultsSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
	}
	
	private void loadMatches() {
		try {
			matchesFromFile = ausDatei(fileMatches); 
			
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String[] split = matchesFromFile.get(matchday).split(";");
				
				setMatchesSetFromRepresentation(matchday, split[0]);
				
				int matchID = 0;
				if (!isNoMatchSet(matchday)) {
					String[] koTimes = split[1].split(":");
					for (matchID = 0; matchID < koTimes.length; matchID++) {
						String[] dateAndTime = koTimes[matchID].split(",");
						daysSinceFirstDay[matchday][matchID] = Integer.parseInt(dateAndTime[0]);
						setTime(matchday, matchID, new Uhrzeit(dateAndTime[1]));
					}
					
					for (matchID = 0; (matchID + 2) < split.length; matchID++) {
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
			errorMessage("Kein Spielplan: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveMatches() {
		matchesFromFile = new ArrayList<>();
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			String row = getMatchesSetRepresentation(matchday) + ";";
			if (!isNoMatchSet(matchday)) {
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					row += daysSinceFirstDay[matchday][matchID] + "," + getTime(matchday, matchID).comparable();
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
							
							teams[getMatch(matchday, matchID).home() - 1].setResult(matchday, result);
							teams[getMatch(matchday, matchID).away() - 1].setResult(matchday, result);
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
		if (!teamsHaveKader)	return;
		matchDataFromFile.clear();
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
				matchDataFromFile.add(getMatch(matchday, matchID) != null ? getMatch(matchday, matchID).fullString() : "null");
			}
		}
		
		inDatei(fileMatchData, matchDataFromFile);
	}
}
