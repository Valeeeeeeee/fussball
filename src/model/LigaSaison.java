package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;


public class LigaSaison implements Wettbewerb {
	
	private Liga league;
	private boolean isSummerToSpringSeason;
	private int seasonIndex;
	private int year;
	private boolean goalDifference;
	private boolean teamsHaveKader;
	
	private int numberOfReferees;
	private ArrayList<Schiedsrichter> referees;
	private int numberOfTeams;
	private Mannschaft[] teams;
	private int halfNOfTeamsUp;
	private int numberOfMatchdays;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int currentMatchday;
	private Datum cMatchdaySetForDate;
	private int newestMatchday;
	private Datum nMatchdaySetForDate;
	private Uhrzeit nMatchdaySetUntilTime = UNDEFINED;
	
	private int[] numberOf;
	
	private Datum[] dates;
	private int[][] kotIndices;
	
	private int numberOfKickoffTimes;
	private ArrayList<AnstossZeit> kickOffTimes;
	private int[] defaultKickoffTimes;
	
	private Spiel[][] matches;
	private boolean[][] matchesSet;
	
	private Ergebnis[][] results;
	private boolean[][] resultsSet;
	
	private Spieltag spieltag;
	private Tabelle tabelle;
	private LigaStatistik statistik;
	
	private boolean geladen;
	private String workspace;
	
	private String fileReferees;
	private ArrayList<String> refereesFromFile;
	
	private String fileTeams;
	private ArrayList<String> teamsFromFile;
	
	private String fileMatches;
	private ArrayList<String> matchesFromFile;
	
	private String fileResults;
	private ArrayList<String> resultsFromFile;
	
	private String fileMatchData;
	private ArrayList<String> matchDataFromFile;
	
	public LigaSaison(Liga league, int seasonIndex, String data) {
		this.league = league;
		this.seasonIndex = seasonIndex;
		fromString(data);
		workspace = league.getWorkspace() + getSeasonFull("_") + File.separator;
	}
	
	public Liga getLeague() {
		return league;
	}
	
	public String getName() {
		return league.getName() + " " + getSeasonFull("/");
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public int getID() {
		return seasonIndex;
	}
	
	public int getYear() {
		return year;
	}
	
	public boolean isSTSS() {
		return isSummerToSpringSeason;
	}
	
	public String getSeasonFull(String trennzeichen) {
		return year + (isSummerToSpringSeason ? trennzeichen + (year + 1) : "");
	}
	
	public int getNumberOf(int index) {
		return numberOf[index];
	}
	
	public int getNumberOfTeams() {
		return numberOfTeams;
	}
	
	public int getHalfNOfTeamsUp() {
		return halfNOfTeamsUp;
	}
	
	public int getNumberOfMatchdays() {
		return numberOfMatchdays;
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return numberOfMatchesPerMatchday;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return numberOfMatchesAgainstSameOpponent;
	}
	
	public boolean useGoalDifference() {
		return goalDifference;
	}
	
	public boolean teamsHaveKader() {
		return teamsHaveKader;
	}
	
	public boolean isETPossible() {
		return false;
	}
	
	public Spieltag getSpieltag() {
		return spieltag;
	}
	
	public Tabelle getTabelle() {
		return tabelle;
	}
	
	public LigaStatistik getStatistik() {
		return statistik;
	}
	
	public ArrayList<Schiedsrichter> getReferees() {
		return referees;
	}
	
	public String[] getAllReferees() {
		String[] allReferees = new String[referees.size() + 1];
		
		allReferees[0] = "Bitte wählen";
		for (int i = 1; i < allReferees.length; i++) {
			allReferees[i] = referees.get(i - 1).getFullName();
		}
		
		return allReferees;
	}
	
	public Mannschaft[] getTeams() {
		return teams;
	}

	public int getIndexOfMannschaft(String name) {
		for (Mannschaft ms : teams) {
			if (ms.getName().equals(name)) {
				return ms.getId();
			}
		}
		return -1;
	}
	
	public Mannschaft getTeamWithName(String teamsName) {
		return teams[getIndexOfMannschaft(teamsName) - 1];
	}
	
	public String getMatchdayDescription(int matchday) {
		return league.getName() + " " + getSeasonFull("/") + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public int getCurrentMatchday() {
		Datum today = new Datum(Start.today(), -1); // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (!today.equals(cMatchdaySetForDate)) {
			if (today.isBefore(getDate(0))) {
				currentMatchday = 0;
			} else if (!today.isBefore(getDate(numberOfMatchdays - 1))) {
				currentMatchday = numberOfMatchdays - 1;
			} else {
				currentMatchday = 0;
				while (!today.isBefore(getDate(currentMatchday))) {
					currentMatchday++;
				}
				if (currentMatchday != 0 && getDate(currentMatchday - 1).daysUntil(today) < today.daysUntil(getDate(currentMatchday))) {
					currentMatchday--;
				}
			}
			cMatchdaySetForDate = today;
		}
		
		return currentMatchday;
	}
	
	public int getNewestStartedMatchday() {
		Datum today = Start.today(), nextDate;
		Uhrzeit time = new Uhrzeit();
		
		if (!today.equals(nMatchdaySetForDate) || !time.isBefore(nMatchdaySetUntilTime)) {
			nMatchdaySetUntilTime = END_OF_DAY;
			if (today.isBefore(getDate(0, 0))) {
				newestMatchday = 0;
			} else if (!today.isBefore(getDate(numberOfMatchdays - 1, 0))) {
				newestMatchday = numberOfMatchdays - 1;
			} else {
				newestMatchday = 0;
				nextDate = getDate(newestMatchday + 1, 0);
				while (today.isAfter(nextDate) || (today.equals(nextDate) && !time.isBefore(getTime(newestMatchday + 1, 0)))) {
					newestMatchday++;
					nextDate = getDate(newestMatchday + 1, 0);
				}
				if (today.equals(getDate(newestMatchday + 1, 0))) {
					nMatchdaySetUntilTime = getTime(newestMatchday + 1, 0);
				}
			}
			
			nMatchdaySetForDate = today;
		}
		
		return newestMatchday;
	}
	
	// Date / Time
	
	public String getDateOfTeam(int matchday, int id) {
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			if (isMatchSet(matchday, matchID)) {
				if (getMatch(matchday, matchID).home() == id || getMatch(matchday, matchID).away() == id)
					return getDateAndTime(matchday, matchID);
			}
		}
		
		return "n.a.";
	}
	
	public String getDateAndTime(int matchday, int matchID) {
		if (matchday >= 0 && matchday < numberOfMatchdays && matchID >= 0 && matchID < numberOfMatchesPerMatchday && getDate(matchday) != MAX_DATE)
			return kickOffTimes.get(getKOTIndex(matchday, matchID)).getDateAndTime(getDate(matchday));
		else
			return "nicht terminiert";
	}
	
	public Datum getDate(int matchday) {
		return dates[matchday];
	}
	
	public int getKOTIndex(int matchday, int matchID) {
		return kotIndices[matchday][matchID];
	}
	
	public void setDate(int matchday, Datum date) {
		dates[matchday] = date;
	}
	
	public void setKOTIndex(int matchday, int matchID, int index) {
		kotIndices[matchday][matchID] = index;
	}
	
	public Datum getDate(int matchday, int matchID) {
		// TODO: frühere 0 ist jetzt MAX_DATE
		return kickOffTimes.get(kotIndices[matchday][matchID]).getDate(dates[matchday]);
	}
	
	public Uhrzeit getTime(int matchday, int matchID) {
		return kickOffTimes.get(kotIndices[matchday][matchID]).getTime();
	}
	
	public int addNewKickoffTime(int daysSince, Uhrzeit time) {
		numberOfKickoffTimes++;
		kickOffTimes.add(new AnstossZeit(numberOfKickoffTimes, daysSince, time));
		return numberOfKickoffTimes;
	}
	
	public int getIndexOfKOT(int diff, Uhrzeit timeOfNewKOT) {
		for (AnstossZeit az : kickOffTimes) {
			if (az.matches(diff, timeOfNewKOT))	return az.getIndex();
		}
		return -1;
	}
	
	public void useDefaultKickoffTimes(int matchday) {
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			setKOTIndex(matchday, i, defaultKickoffTimes[i]);
		}
	}
	
	public int[] getDefaultKickoffTimes() {
		return defaultKickoffTimes;
	}
	
	public ArrayList<AnstossZeit> getKickOffTimes() {
		return kickOffTimes;
	}
	
	public int getNumberOfKickoffTimes() {
		return numberOfKickoffTimes;
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
		else	setMatchSet(matchday, matchID, false);
		matches[matchday][matchID] = match;
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
	
	public void setRueckrundeToOrder(int[] orderOfRueckRunde) {
		if (2 * orderOfRueckRunde.length != numberOfMatchdays) {
			errorMessage("Your Rueckrunde does not match the expected number of matchdays.");
			return;
		}
		boolean[] inHinrunde = new boolean[numberOfMatchdays];
		for (int i = 0; i < orderOfRueckRunde.length; i++) {
			if (inHinrunde[orderOfRueckRunde[i] - 1] == true) {
				errorMessage("Some matchday appears to be twice in the Rueckrunde.");
				return;
			}
			inHinrunde[orderOfRueckRunde[i] - 1] = true;
		}
		
		int matchdayOld, matchdayNew = 0;
		Uhrzeit time = MIDNIGHT;
		for (int i = 0; i < orderOfRueckRunde.length; i++) {
			Spiel[] matchesInNewOrder = new Spiel[numberOfMatchesPerMatchday];
			matchdayOld = orderOfRueckRunde[i] - 1;
			while (inHinrunde[matchdayNew]) {
				matchdayNew++;
			}
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				Spiel oldMatch = getMatch(matchdayOld, j);
				if (oldMatch != null) {
					matchesInNewOrder[j] = new Spiel(this, matchdayNew, getDate(matchdayNew), time, oldMatch.away(), oldMatch.home());
				}
			}
			for (int j = 0; j < matchesInNewOrder.length; j++) {
				for (int k = j + 1; k < matchesInNewOrder.length; k++) {
					if (matchesInNewOrder[k] != null && (matchesInNewOrder[j] == null || matchesInNewOrder[j].home() > matchesInNewOrder[k].home())) {
						Spiel temp = matchesInNewOrder[j];
						matchesInNewOrder[j] = matchesInNewOrder[k];
						matchesInNewOrder[k] = temp;
					}
				}
			}
			for (int j = 0; j < matchesInNewOrder.length; j++) {
				setMatch(matchdayNew, j, matchesInNewOrder[j]);
			}
			matchdayNew++;
		}
	}
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[numberOfMatchesPerMatchday];
		Datum[] dates = new Datum[numberOfMatchesPerMatchday];
		Uhrzeit[] times = new Uhrzeit[numberOfMatchesPerMatchday];
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			dates[matchID] = kickOffTimes.get(getKOTIndex(matchday, matchID)).getDate(getDate(matchday));
			times[matchID] = kickOffTimes.get(getKOTIndex(matchday, matchID)).getTime();
		}
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (times[m].isUndefined() && !times[m2].isUndefined())			hilfsarray[m2]++;
				else if (times[m2].isUndefined() && !times[m].isUndefined())	hilfsarray[m]++;
				else if (dates[m2].isAfter(dates[m]))							hilfsarray[m2]++;
				else if (dates[m2].isBefore(dates[m]))							hilfsarray[m]++;
				else if (times[m2].isAfter(times[m]))							hilfsarray[m2]++;
				else if (times[m2].isBefore(times[m]))							hilfsarray[m]++;
				else {
					Spiel sp1 = getMatch(matchday, m), sp2 = getMatch(matchday, m2);
					if (sp1 != null && sp2 != null && sp1.getHomeTeam().getId() > sp2.getHomeTeam().getId())	hilfsarray[m]++;
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
		int[] oldKOTindices = new int[numberOfMatchesPerMatchday];
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			oldMatches[matchID] = getMatch(matchday, matchID);
			oldResults[matchID] = getResult(matchday, matchID);
			oldKOTindices[matchID] = kotIndices[matchday][matchID];
		}
		
		for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
			setMatch(matchday, matchID, oldMatches[oldIndicesInNewOrder[matchID]]);
			setResult(matchday, matchID, oldResults[oldIndicesInNewOrder[matchID]]);
			kotIndices[matchday][matchID] = oldKOTindices[oldIndicesInNewOrder[matchID]];
		}
	}
	
	private void saveNextMatches() {
		ArrayList<Long> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				Datum date = getDate(i, j);
				Uhrzeit time = getTime(i, j);
				if (isMatchSet(i, j) && date != null && (!inThePast(date, time, 105) || !isResultSet(i, j))) {
					long dateAndTime = 10000L * date.comparable() + time.comparable();
					if (nextMatches.size() < 10 || dateAndTime < nextMatches.get(9)) {
						int index = nextMatches.size();
						for (int k = 0; k < nextMatches.size() && index == nextMatches.size(); k++) {
							if (dateAndTime < nextMatches.get(k))	index = k;
						}
						nextMatches.add(index, dateAndTime);
					}
				}
			}
		}
		
		String fileName = workspace + "nextMatches.txt";
		if (nextMatches.size() > 0) {
			ArrayList<String> nextMatchesString = new ArrayList<>();
			for (int i = 0; i < 10 && i < nextMatches.size(); i++) {
				nextMatchesString.add("" + nextMatches.get(i));
			}
			inDatei(fileName, nextMatchesString);
		} else {
			new File(fileName).delete();
		}
	}
	
	private String getAnzahlRepresentation() {
		String representation = "", sep = "";
		
		for (int i = 0; i < numberOf.length; i++) {
			representation += sep + numberOf[i];
			sep = ",";
		}
		
		return representation;
	}
	
	private int[] getAnzahlFromString(String anzahlen) {
		String[] split = anzahlen.split(",");
		int[] anzahl = new int[split.length];
		
		for (int i = 0; i < anzahl.length; i++) {
			anzahl[i] = Integer.parseInt(split[i]);
		}
		
		return anzahl;
	}
	
	private void initDefaultKickoffTimes(String DKTAsString) {
		// kommt als 0,1,1,1,1,1,2,3,4
		String[] split = DKTAsString.split(",");
		defaultKickoffTimes = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			defaultKickoffTimes[i] = Integer.parseInt(split[i]);
		}
	}
	
	private String getDefaultKickoffTimesRepresentation() {
		String dktimes = "";
		if (defaultKickoffTimes.length >= 1) {
			dktimes += defaultKickoffTimes[0];
			for (int i = 1; i < defaultKickoffTimes.length; i++) {
				dktimes += "," + defaultKickoffTimes[i];
			}
		}
		
		return dktimes;
	}
	
	private void initializeArrays() {
		// Alle Array werden initialisiert
		dates = new Datum[numberOfMatchdays];
		kotIndices = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		matches = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		results = new Ergebnis[numberOfMatchdays][numberOfMatchesPerMatchday];
		matchesSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
		resultsSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
	}
	
	public void loadReferees() {
		refereesFromFile = ausDatei(fileReferees, false);
		
		numberOfReferees = refereesFromFile.size();
		referees = new ArrayList<>();
		for (int i = 0; i < numberOfReferees; i++) {
			referees.add(new Schiedsrichter(i + 1, refereesFromFile.get(i)));
		}
	}
	
	public void saveReferees() {
		refereesFromFile.clear();
		
		for (int i = 0; i < referees.size(); i++) {
			refereesFromFile.add(referees.get(i).toString());
		}
		
		if (refereesFromFile.size() > 0)	inDatei(fileReferees, refereesFromFile);
	}
	
	public void loadTeams() {
		teamsFromFile = ausDatei(fileTeams);
		
		halfNOfTeamsUp = (1 + numberOfTeams) / 2;
		numberOfMatchesPerMatchday = numberOfTeams / 2;
		if (numberOfTeams >= 2)		numberOfMatchdays = numberOfMatchesAgainstSameOpponent * (2 * halfNOfTeamsUp - 1);
		else						numberOfMatchdays = 0;
		
		teams = new Mannschaft[numberOfTeams];
		for (int i = 0; i < numberOfTeams; i++) {
			teams[i] = new Mannschaft(i + 1, this, teamsFromFile.get(i + 1));
		}
	}
	
	public void saveTeams() {
		teamsFromFile.clear();
		
		teamsFromFile.add("" + numberOfTeams);
		for (int i = 0; i < teams.length; i++) {
			teams[i].save();
			teamsFromFile.add(teams[i].toString());
		}
		
		inDatei(fileTeams, teamsFromFile);
	}
	
	public void loadMatches() {
		matchesFromFile = ausDatei(fileMatches);
		
		try {
			matchesFromFile = ausDatei(fileMatches); 
			
			// Anstoßzeiten / Spieltermine
			String allKickoffTimes = matchesFromFile.get(0);
			String[] split = allKickoffTimes.split(";");
			numberOfKickoffTimes = Integer.parseInt(split[0]);
			kickOffTimes = new ArrayList<>();
			kickOffTimes.add(new AnstossZeit(0, -1, UNDEFINED));
			for (int counter = 1; counter <= numberOfKickoffTimes; counter++) {
				kickOffTimes.add(new AnstossZeit(counter, split[counter]));
			}
			
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				split = matchesFromFile.get(matchday + 1).split(";");
				
				setMatchesSetFromRepresentation(matchday, split[0]);
				
				int matchID = 0;
				if (!isNoMatchSet(matchday)) {
					// Daten und Uhrzeiten
					String[] koTimes = split[1].split(":");
					setDate(matchday, koTimes[0].equals("0") ? MAX_DATE : new Datum(koTimes[0]));
					for (matchID = 0; (matchID + 1) < koTimes.length; matchID++) {
						setKOTIndex(matchday, matchID, Integer.parseInt(koTimes[matchID + 1]));
					}
					
					// Spielplan
					for (matchID = 0; (matchID + 2) < split.length; matchID++) {
						Spiel match = null;
						
						if (isMatchSet(matchday, matchID)) {
							match = new Spiel(this, matchday, getDate(matchday, matchID), getTime(matchday, matchID), split[matchID + 2]);
						}
						
						setMatch(matchday, matchID, match);
					}
				}
				else	setDate(matchday, MAX_DATE);
				
				while(matchID < numberOfMatchesPerMatchday) {
					setMatch(matchday, matchID, null);
					matchID++;
				}
			}
		} catch (Exception e) {
			errorMessage("Kein Spielplan: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void saveMatches() {
		matchesFromFile.clear();
		
		String row = numberOfKickoffTimes + ";";
		for (int i = 0; i < numberOfKickoffTimes; i++) {
			row = row + kickOffTimes.get(i + 1) + ";";
		}
		matchesFromFile.add(row);
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			row = getMatchesSetRepresentation(matchday) + ";";
			if (!isNoMatchSet(matchday)) {
				Datum date = getDate(matchday);
				row += date == MAX_DATE ? 0 : date.comparable();
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					row += ":" + getKOTIndex(matchday, matchID);
				}
				row += ";";
				for (int matchID = 0; matchID < numberOfMatchesPerMatchday; matchID++) {
					row += getMatch(matchday, matchID) + ";";
				}
			}
			
			matchesFromFile.add(row);
		}
		
		inDatei(fileMatches, matchesFromFile);
	}
	
	public void loadResults() {
		resultsFromFile = ausDatei(fileResults);
		
		try {
			resultsFromFile = ausDatei(fileResults);
			
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String[] split = resultsFromFile.get(matchday).split(";");
				setResultsSetFromRepresentation(matchday, split[0]);

				int matchID = 0;
				if (!isNoMatchSet(matchday) && !isNoResultSet(matchday)) {
					for (matchID = 0; matchID + 1 < split.length; matchID++) {
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
					setResult(matchday, matchID++, null);
				}
			}
		} catch (Exception e) {
			errorMessage("Kein Ergebnisplan: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void saveResults() {
		resultsFromFile.clear();
		
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
	
	public void load() {
		fileResults = workspace + "Ergebnisse.txt";
		fileMatchData = workspace + "Spieldaten.txt";
		fileMatches = workspace + "Spielplan.txt";
		fileTeams = workspace + "Mannschaften.txt";
		fileReferees = workspace + "Schiedsrichter.txt";
		
		loadReferees();
		loadTeams();
		initializeArrays();
		
		loadMatches();
		loadResults();
		loadMatchData();
		
		if (spieltag == null) {
			spieltag = new Spieltag(this);
			spieltag.setLocation((Start.WIDTH - spieltag.getSize().width) / 2, (Start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
		}
		if (tabelle == null) {
			tabelle = new Tabelle(this);
			tabelle.setLocation((Start.WIDTH - tabelle.getSize().width) / 2, 50);
			tabelle.setVisible(false);
		}
		if (statistik == null) {
			statistik = new LigaStatistik(this);
			statistik.setLocation((Start.WIDTH - statistik.getSize().width) / 2, 50);
			statistik.setVisible(false);
		}
		spieltag.resetCurrentMatchday();
		tabelle.resetCurrentMatchday();
		
		cMatchdaySetForDate = MIN_DATE;
		nMatchdaySetForDate = MIN_DATE;
		
		geladen = true;
	}
	
	public void save() {
		if (!geladen)	return;
		
		saveNextMatches();
		saveReferees();
		saveTeams();
		saveMatches();
		saveResults();
		saveMatchData();
		
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += year + ";";
		toString += isSummerToSpringSeason + ";";
		toString += numberOfTeams + ";";
		toString += numberOfMatchesAgainstSameOpponent + ";";
		toString += getDefaultKickoffTimesRepresentation() + ";";
		toString += goalDifference + ";";
		toString += teamsHaveKader + ";";
		toString += getAnzahlRepresentation() + ";";
		
		return toString;
	}
	
	private void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		year = Integer.parseInt(split[index++]);
		isSummerToSpringSeason = Boolean.parseBoolean(split[index++]);
		numberOfTeams = Integer.parseInt(split[index++]);
		numberOfMatchesAgainstSameOpponent = Integer.parseInt(split[index++]);
		initDefaultKickoffTimes(split[index++]);
		goalDifference = Boolean.parseBoolean(split[index++]);
		teamsHaveKader = Boolean.parseBoolean(split[index++]);
		numberOf = getAnzahlFromString(split[index++]);
	}
}
