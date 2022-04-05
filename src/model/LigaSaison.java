package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import model.tournament.KOOrigin;
import model.tournament.KOOriginPreviousLeague;


public class LigaSaison implements Wettbewerb {
	
	private Liga league;
	private boolean isSummerToSpringSeason;
	private Datum startDate;
	private Datum finalDate;
	private int seasonIndex;
	private int year;
	private boolean goalDifference;
	private boolean fairplay = false;
	private boolean teamsHaveKader;
	private boolean hasPlayoffs;
	
	private HashMap<Dauer, Integer> numberOfSubstitutions;
	
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
	private Uhrzeit nMatchdaySetUntilTime = TIME_UNDEFINED;
	
	private KORunde playoffs;
	private HashMap<String, Mannschaft> teamsFromOtherCompetition = new HashMap<>();
	
	private int[] numberOf;
	
	private Datum[] dates;
	private int[][] kotIndices;
	
	private int numberOfKickoffTimes;
	private ArrayList<AnstossZeit> kickOffTimes;
	private int[] defaultKickoffTimes;
	
	private Spiel[][] matches;
	private boolean[][] matchesSet;
	
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
	
	private String fileMatchData;
	private ArrayList<String> matchDataFromFile;
	
	private String fileKOconfig;
	private ArrayList<String> koConfigFromFile;
	
	public LigaSaison(Liga league, int seasonIndex, String data) {
		this.league = league;
		this.seasonIndex = seasonIndex;
		fromString(data);
		workspace = league.getWorkspace() + getSeasonFull("_") + File.separator;
		startDate = isSummerToSpringSeason ? new Datum(1, 7, year) : new Datum(1, 1, year);
		finalDate = isSummerToSpringSeason ? new Datum(30, 6, year + 1) : new Datum(31, 12, year);
	}
	
	public Liga getLeague() {
		return league;
	}
	
	public String getDescription() {
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
	
	public boolean isClubCompetition() {
		return league.isClubCompetition();
	}
	
	public boolean hasPlayoffs() {
		return hasPlayoffs;
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
	
	public int getNumberOfMatchdaysIncludingPlayoff() {
		return numberOfMatchdays + (hasPlayoffs ? playoffs.getNumberOfMatchdays() : 0);
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
	
	public boolean useFairplayRule() {
		return fairplay;
	}
	
	public boolean teamsHaveKader() {
		return teamsHaveKader;
	}
	
	public boolean isExtraTimePossible() {
		return false;
	}
	
	public int getNumberOfRegularSubstitutions(Datum date) {
		if (!numberOfSubstitutions.isEmpty()) {
			Set<Dauer> durations = numberOfSubstitutions.keySet();
			for (Dauer duration : durations) {
				if (duration.includes(date))	return numberOfSubstitutions.get(duration);
			}
		}
		return Fussball.numberOfRegularSubstitutions;
	}
	
	public boolean isFourthSubstitutionPossible() {
		return false;
	}
	
	public String[] getMatchdays() {
		if (hasPlayoffs) {
			String[] matchdays = new String[numberOfMatchdays + playoffs.getNumberOfMatchdays()];
			for (int i = 0; i < numberOfMatchdays; i++) {
				matchdays[i] = (i + 1) + ". Spieltag";
			}
			if (playoffs.hasSecondLeg()) {
				matchdays[numberOfMatchdays] = playoffs.getDescription() + " Hinspiel";
				matchdays[numberOfMatchdays + 1] = playoffs.getDescription() + " Rückspiel";
			} else {
				matchdays[numberOfMatchdays] = playoffs.getDescription();
			}
			return matchdays;
		} else {
			String[] matchdays = new String[numberOfMatchdays];
			for (int i = 0; i < numberOfMatchdays; i++) {
				matchdays[i] = (i + 1) + ". Spieltag";
			}
			return matchdays;
		}
	}
	
	public Spieltag getSpieltag() {
		return spieltag;
	}
	
	public Spieltag getPlayoffSpieltag() {
		return hasPlayoffs ? playoffs.getSpieltag() : null;
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
	
	public ArrayList<String[]> getAllMatches(Mannschaft team) {
		ArrayList<String[]> allMatches = new ArrayList<>();
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			String md = matchday + 1 + "";
			String date = getDateOfTeam(matchday, team);
			String goalsH = "-", goalsA = "-";
			String sunx = RESULT_NOT_SET;
			boolean allMatchesSet = true, teamFound = false;
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday && !teamFound; matchIndex++) {
				if (isMatchSet(matchday, matchIndex)) {
					Spiel match = getMatch(matchday, matchIndex);
					Ergebnis result = getResult(matchday, matchIndex);
					if (teamFound = team.equals(match.getHomeTeam())) {
						if (isResultSet(matchday, matchIndex) && !result.isCancelled()) {
							goalsH = "" + result.home();
							goalsA = "" + result.away();
							sunx = getSUN(result.home(), result.away());
						}
						String otherTeamName = match.getAwayTeam() == null ? MATCH_NOT_SET : match.getAwayTeam().getName();
						allMatches.add(new String[] {md, date, team.getName(), goalsH, goalsA, otherTeamName, sunx});
					} else if (teamFound = team.equals(match.getAwayTeam())) {
						if (isResultSet(matchday, matchIndex) && !result.isCancelled()) {
							goalsH = "" + result.home();
							goalsA = "" + result.away();
							sunx = getSUN(result.away(), result.home());
						}
						String otherTeamName = match.getHomeTeam() == null ? MATCH_NOT_SET : match.getHomeTeam().getName();
						allMatches.add(new String[] {md, date, otherTeamName, goalsH, goalsA, team.getName(), sunx});
					}
				}
				else	allMatchesSet = false;
			}
			if (!teamFound) {
				if (allMatchesSet)	allMatches.add(new String[] {md, date, SPIELFREI, goalsH, goalsA, SPIELFREI, sunx});
				else				allMatches.add(new String[] {md, date, MATCH_NOT_SET, goalsH, goalsA, MATCH_NOT_SET, sunx});
			}
		}
		
		return allMatches;
	}

	public int getIndexOfMannschaft(String name) {
		for (Mannschaft ms : teams) {
			if (ms.getName().equals(name)) {
				return ms.getId();
			}
		}
		return UNDEFINED;
	}
	
	public Mannschaft getTeamWithName(String teamsName) {
		int index = getIndexOfMannschaft(teamsName);
		if (index == UNDEFINED)	return null;
		return teams[index - 1];
	}
	
	public String getMatchdayDescription(int matchday) {
		return league.getName() + " " + getSeasonFull("/") + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public int getCurrentMatchday() {
		Datum yesterday = new Datum(today, UNDEFINED); // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (!yesterday.equals(cMatchdaySetForDate)) {
			if (yesterday.isBefore(getDate(0))) {
				currentMatchday = 0;
			} else if (!yesterday.isBefore(getDate(numberOfMatchdays - 1))) {
				currentMatchday = numberOfMatchdays - 1;
			} else {
				currentMatchday = 0;
				while (!yesterday.isBefore(getDate(currentMatchday))) {
					currentMatchday++;
				}
				if (currentMatchday != 0 && getDate(currentMatchday - 1).daysUntil(yesterday) < yesterday.daysUntil(getDate(currentMatchday))) {
					currentMatchday--;
				}
			}
			cMatchdaySetForDate = yesterday;
		}
		
		return currentMatchday;
	}
	
	public int getNewestStartedMatchday() {
		Datum nextDate;
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
	
	public String getDateOfTeam(int matchday, Mannschaft team) {
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (isMatchSet(matchday, matchIndex)) {
				if (team.equals(getMatch(matchday, matchIndex).getHomeTeam()) || team.equals(getMatch(matchday, matchIndex).getAwayTeam()))
					return getDateAndTime(matchday, matchIndex);
			}
		}
		
		return "n.a.";
	}
	
	public String getDateAndTime(int matchday, int matchIndex) {
		if (matchday >= 0 && matchday < numberOfMatchdays && matchIndex >= 0 && matchIndex < numberOfMatchesPerMatchday && !getDate(matchday).equals(DATE_UNDEFINED))
			return kickOffTimes.get(getKOTIndex(matchday, matchIndex)).getDateAndTime(getDate(matchday));
		else
			return "nicht terminiert";
	}
	
	public Datum getDate(int matchday) {
		return dates[matchday];
	}
	
	public int getKOTIndex(int matchday, int matchIndex) {
		return kotIndices[matchday][matchIndex];
	}
	
	public void setDate(int matchday, Datum date) {
		dates[matchday] = date;
	}
	
	public void setKOTIndex(int matchday, int matchIndex, int index) {
		kotIndices[matchday][matchIndex] = index;
	}
	
	public Datum getDate(int matchday, int matchIndex) {
		return kickOffTimes.get(kotIndices[matchday][matchIndex]).getDate(dates[matchday]);
	}
	
	public Uhrzeit getTime(int matchday, int matchIndex) {
		return kickOffTimes.get(kotIndices[matchday][matchIndex]).getTime();
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
		return UNDEFINED;
	}
	
	public void useDefaultKickoffTimes(int matchday) {
		ArrayList<Integer> unsetMatches = new ArrayList<>(), unsetKOTs = new ArrayList<>();
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			unsetKOTs.add(defaultKickoffTimes[i]);
		}
		
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			if (unsetKOTs.contains(getKOTIndex(matchday, i)))	unsetKOTs.remove(new Integer(getKOTIndex(matchday, i)));
			else												unsetMatches.add(i);
		}
		
		for (int i = 0; i < unsetKOTs.size(); i++) {
			setKOTIndex(matchday, unsetMatches.get(i), unsetKOTs.get(i));
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
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (isMatchSet(matchday, matchIndex)) 	return false;
		}
		return true;
	}
	
	public boolean isMatchSet(int matchday, int matchIndex) {
		return matchesSet[matchday][matchIndex];
	}
	
	public void setMatchSet(int matchday, int matchIndex, boolean isSet) {
		matchesSet[matchday][matchIndex] = isSet;
	}
	
	public String getMatchesSetRepresentation(int matchday) {
		String representation = "";
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (isMatchSet(matchday, matchIndex))	representation += "t";
			else									representation += "f";
		}
		
		return representation;
	}
	
	public void setMatchesSetFromRepresentation(int matchday, String representation) {
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (representation.charAt(matchIndex) == 't')		setMatchSet(matchday, matchIndex, true);
			else if (representation.charAt(matchIndex) == 'f')	setMatchSet(matchday, matchIndex, false);
		}
	}
	
	// Ergebnisplan eingetragen
	
	public boolean isNoResultSet(int matchday) {
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (isResultSet(matchday, matchIndex)) 	return false;
		}
		return true;
	}
	
	public boolean isResultSet(int matchday, int matchIndex) {
		return isMatchSet(matchday, matchIndex) && getMatch(matchday, matchIndex).hasResult();
	}
	
	public String getResultsSetRepresentation(int matchday) {
		String representation = "";
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (isResultSet(matchday, matchIndex))	representation += "t";
			else									representation += "f";
		}
		
		return representation;
	}
	
	// Ergebnisplan
	
	public Ergebnis getResult(int matchday, int matchIndex) {
		if (isMatchSet(matchday, matchIndex))	return getMatch(matchday, matchIndex).getResult();
		return null;
	}
	
	public void setResult(int matchday, int matchIndex, Ergebnis result) {
		if (isMatchSet(matchday, matchIndex))	getMatch(matchday, matchIndex).setResult(result);
	}
	
	// Spielplan
	
	public Spiel getMatch(int matchday, int matchIndex) {
		return matches[matchday][matchIndex];
	}
	
	public void setMatch(int matchday, int matchIndex, Spiel match) {
		String key = getKey(matchday);
		if (match != null) {
			match.getHomeTeam().setMatch(key, match);
			match.getAwayTeam().setMatch(key, match);
		} else {
			if (isMatchSet(matchday, matchIndex)) {
				Spiel previousMatch = getMatch(matchday, matchIndex);
				previousMatch.getHomeTeam().resetMatch(key);
				previousMatch.getAwayTeam().resetMatch(key);
				setKOTIndex(matchday, matchIndex, 0);
			}
		}
		matches[matchday][matchIndex] = match;
		setMatchSet(matchday, matchIndex, match != null);
	}
	
	public String getKey(int matchday) {
		return "RR" + twoDigit(matchday);
	}
	
	public void resetMatchday(int matchday) {
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			setMatch(matchday, match, null);
		}
	}
	
	public void createReverseFixtures(int[] orderOfReverseFixtures) {
		if (2 * orderOfReverseFixtures.length != numberOfMatchdays) {
			errorMessage("The given reverse fixtures order does not match the expected number of matchdays.");
			return;
		}
		
		boolean[] isFirstFixture = new boolean[numberOfMatchdays];
		for (int i = 0; i < orderOfReverseFixtures.length; i++) {
			if (isFirstFixture[orderOfReverseFixtures[i] - 1] == true) {
				errorMessage("Some matchday appears to be twice in the reverse fixtures order.");
				return;
			}
			isFirstFixture[orderOfReverseFixtures[i] - 1] = true;
		}
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			if (isFirstFixture[matchday]) {
				for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
					if (!isMatchSet(matchday, matchIndex)) {
						errorMessage("You can't create reverse fixtures when some of the first fixtures are still missing.");
						return;
					}
				}
			}
		}
		
		int matchdayOld, matchdayNew = 0;
		for (int i = 0; i < orderOfReverseFixtures.length; i++) {
			ArrayList<Spiel> reverseFixtures = new ArrayList<>();
			matchdayOld = orderOfReverseFixtures[i] - 1;
			while (isFirstFixture[matchdayNew]) {
				matchdayNew++;
			}
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				addInOrder(reverseFixtures, getMatch(matchdayOld, j).getReverseFixture(matchdayNew));
			}
			for (int j = 0; j < reverseFixtures.size(); j++) {
				setMatch(matchdayNew, j, reverseFixtures.get(j));
			}
			matchdayNew++;
		}
	}
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[numberOfMatchesPerMatchday];
		Datum[] dates = new Datum[numberOfMatchesPerMatchday];
		Uhrzeit[] times = new Uhrzeit[numberOfMatchesPerMatchday];
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			dates[matchIndex] = kickOffTimes.get(getKOTIndex(matchday, matchIndex)).getDate(getDate(matchday));
			times[matchIndex] = kickOffTimes.get(getKOTIndex(matchday, matchIndex)).getTime();
		}
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (times[m].isUndefined() && !times[m2].isUndefined())			hilfsarray[m]++;
				else if (times[m2].isUndefined() && !times[m].isUndefined())	hilfsarray[m2]++;
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
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			oldMatches[matchIndex] = getMatch(matchday, matchIndex);
			oldResults[matchIndex] = getResult(matchday, matchIndex);
			oldKOTindices[matchIndex] = kotIndices[matchday][matchIndex];
		}
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			setMatch(matchday, matchIndex, oldMatches[oldIndicesInNewOrder[matchIndex]]);
			setResult(matchday, matchIndex, oldResults[oldIndicesInNewOrder[matchIndex]]);
			kotIndices[matchday][matchIndex] = oldKOTindices[oldIndicesInNewOrder[matchIndex]];
		}
	}
	
	private void saveNextMatches() {
		ArrayList<Long> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				if (isResultSet(i, j) && getResult(i, j).isCancelled())	continue;
				Datum date = getDate(i, j);
				Uhrzeit time = getTime(i, j);
				if (isMatchSet(i, j) && date != null && (!inThePast(date, time, 105) || !isResultSet(i, j))) {
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
		
		if (hasPlayoffs) {
			ArrayList<Long> nextMatchesPO = playoffs.getNextMatches();
			for (int i = 0; i < nextMatchesPO.size(); i++) {
				int index = nextMatches.size();
				for (int j = 0; j < nextMatches.size() && index == nextMatches.size(); j++) {
					if (nextMatchesPO.get(i) < nextMatches.get(j))	index = j;
				}
				nextMatches.add(index, nextMatchesPO.get(i));
			}
		}
		
		String fileName = workspace + "nextMatches.txt";
		if (nextMatches.size() > 0) {
			ArrayList<String> nextMatchesString = new ArrayList<>();
			for (int i = 0; i < Fussball.numberOfMissingResults && i < nextMatches.size(); i++) {
				nextMatchesString.add("" + nextMatches.get(i));
			}
			writeFile(fileName, nextMatchesString);
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
	
	public Mannschaft getTeamFromOtherCompetition(int id, Wettbewerb competition, KOOrigin origin) {
		Mannschaft team = null;
		
		if (teamsFromOtherCompetition.containsKey(origin.getOrigin())) {
			return teamsFromOtherCompetition.get(origin.getOrigin());
		}
		team = new Mannschaft(id, competition, getNameOfTeamFromOtherCompetition(origin));
		teamsFromOtherCompetition.put(origin.getOrigin(), team);
		
		return team;
	}
	
	private String getNameOfTeamFromOtherCompetition(KOOrigin origin) {
		String teamOrigin = origin.getOrigin();
		String fileName = Fussball.getInstance().getLeagueWorkspaceFromShortName(teamOrigin.substring(0, 4), Integer.parseInt(teamOrigin.substring(4, 8)));
		if (fileName == null)	return teamOrigin;
		
		ArrayList<String> teams = readFile(fileName + "allRanks.txt");
		for (String team : teams) {
			if (teamOrigin.substring(8).equals(team.split(": ")[0])) {
				return team.split(": ")[1];
			}
		}
		
		return teamOrigin;
	}
	
	public Optional<Mannschaft> getTeamFromOrigin(KOOrigin origin) {
		KOOriginPreviousLeague teamOrigin = (KOOriginPreviousLeague) origin;
		return getTeamOnPlace(teamOrigin.getPlaceIndex());
	}
	
	private void initializeArrays() {
		dates = new Datum[numberOfMatchdays];
		kotIndices = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		matches = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		matchesSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
	}
	
	public void loadReferees() {
		refereesFromFile = readFile(fileReferees, false);
		
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
		
		if (refereesFromFile.size() > 0)	writeFile(fileReferees, refereesFromFile);
	}
	
	public void loadTeams() {
		teamsFromFile = readFile(fileTeams);
		
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
		
		writeFile(fileTeams, teamsFromFile);
	}
	
	public void loadMatches() {
		matchesFromFile = readFile(fileMatches);
		
		try {
			matchesFromFile = readFile(fileMatches); 
			
			// Anstoßzeiten / Spieltermine
			String allKickoffTimes = matchesFromFile.get(0);
			String[] split = allKickoffTimes.split(";");
			numberOfKickoffTimes = Integer.parseInt(split[0]);
			kickOffTimes = new ArrayList<>();
			kickOffTimes.add(new AnstossZeit(0, UNDEFINED, TIME_UNDEFINED));
			for (int counter = 1; counter <= numberOfKickoffTimes; counter++) {
				kickOffTimes.add(new AnstossZeit(counter, split[counter]));
			}
			
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				split = matchesFromFile.get(matchday + 1).split(";");
				
				setMatchesSetFromRepresentation(matchday, split[0]);
				
				int matchIndex = 0;
				if (!isNoMatchSet(matchday)) {
					// Daten und Uhrzeiten
					String[] koTimes = split[1].split(":");
					setDate(matchday, koTimes[0].equals("0") ? DATE_UNDEFINED : new Datum(koTimes[0]));
					for (matchIndex = 0; (matchIndex + 1) < koTimes.length; matchIndex++) {
						setKOTIndex(matchday, matchIndex, Integer.parseInt(koTimes[matchIndex + 1]));
					}
					
					// Spielplan
					for (matchIndex = 0; (matchIndex + 2) < split.length; matchIndex++) {
						Spiel match = null;
						
						if (isMatchSet(matchday, matchIndex)) {
							match = new Spiel(this, matchday, getDate(matchday, matchIndex), getTime(matchday, matchIndex), split[matchIndex + 2]);
						}
						
						setMatch(matchday, matchIndex, match);
					}
				}
				else	setDate(matchday, DATE_UNDEFINED);
				
				while(matchIndex < numberOfMatchesPerMatchday) {
					setMatch(matchday, matchIndex, null);
					matchIndex++;
				}
			}
		} catch (Exception e) {
			errorMessage("Kein Spielplan: " + e.getMessage());
			e.printStackTrace();
		}
		
		checkAndCorrectOrderOfKOTs();
	}
	
	private void checkAndCorrectOrderOfKOTs() {
		int lastDefault = 0;
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			if (defaultKickoffTimes[i] > lastDefault)	lastDefault = defaultKickoffTimes[i];
		}
		
		ArrayList<AnstossZeit> newKOTs = new ArrayList<>();
		for (int i = 0; i <= lastDefault; i++) {
			newKOTs.add(kickOffTimes.get(i));
		}
		
		ArrayList<ArrayList<Integer>> allOccurrences = new ArrayList<>();
		for (int i = lastDefault + 1; i <= numberOfKickoffTimes; i++) {
			allOccurrences.add(getAllOccurrencesOfKOT(i));
		}
		
		int[] newOrder = new int[numberOfKickoffTimes - lastDefault];
		for (int i = 0; i < allOccurrences.size(); i++) {
			for (int j = i + 1; j < allOccurrences.size(); j++) {
				if (allOccurrences.get(j).size() == 0)			newOrder[j]++;
				else if (allOccurrences.get(i).size() == 0)		newOrder[i]++;
				else if (allOccurrences.get(i).get(0) > allOccurrences.get(j).get(0))	newOrder[i]++;
				else																	newOrder[j]++;
			}
		}
		for (int order = 0; order < newOrder.length; order++) {
			for (int i = 0; i < newOrder.length; i++) {
				if (newOrder[i] == order) {
					newKOTs.add(kickOffTimes.get(i + lastDefault + 1));
					if (i == order)	continue;
					for (int j = 0; j < allOccurrences.get(i).size(); j++) {
						int matchday = allOccurrences.get(i).get(j) / 100 - 1, matchIndex = allOccurrences.get(i).get(j) % 100 - 1;
						setKOTIndex(matchday, matchIndex, order + lastDefault + 1);
					}
				}
			}
		}
		
		kickOffTimes = newKOTs;
	}
	
	private ArrayList<Integer> getAllOccurrencesOfKOT(int kotIndex) {
		ArrayList<Integer> occurrences = new ArrayList<>();
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
				if (kotIndices[matchday][matchIndex] == kotIndex)	occurrences.add(100 * (matchday + 1) + (matchIndex + 1));
			}
		}
		return occurrences;
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
				row += date.equals(DATE_UNDEFINED) ? 0 : date.comparable();
				for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
					row += ":" + getKOTIndex(matchday, matchIndex);
				}
				row += ";";
				for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
					row += getMatch(matchday, matchIndex) + ";";
				}
			}
			
			matchesFromFile.add(row);
		}
		
		writeFile(fileMatches, matchesFromFile);
	}
	
	private void loadMatchData() {
		try {
			matchDataFromFile = readFile(fileMatchData);
			
			for (int matchday = 0; matchday < numberOfMatchdays && matchday < matchDataFromFile.size(); matchday++) {
				for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
					String matchData = matchDataFromFile.get(matchday * numberOfMatchesPerMatchday + matchIndex);
					if (isMatchSet(matchday, matchIndex)) {
						getMatch(matchday, matchIndex).setMatchData(matchData);
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
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
				matchDataFromFile.add(getMatch(matchday, matchIndex) != null ? getMatch(matchday, matchIndex).fullString() : "null");
			}
		}
		
		writeFile(fileMatchData, matchDataFromFile);
	}
	
	public Optional<Mannschaft> getTeamOnPlace(int place) {
		if (place < 1 || place > teams.length)	return Optional.empty();
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int matchIndex = 0; matchIndex < getNumberOfMatchesPerMatchday(); matchIndex++) {
				if (!isResultSet(matchday, matchIndex)) 	return Optional.empty();
			}
		}
		
		for (Mannschaft team : teams) {
			team.compareWithOtherTeams(teams, numberOfMatchdays - 1, Tabellenart.COMPLETE);
		}
		for (Mannschaft team : teams) {
			if (team.get(0, numberOfMatchdays - 1, Tabellenart.COMPLETE) == place - 1)		return Optional.of(team);
		}
		
		return Optional.empty();
	}
	
	public String[] getRanks() {
		String[] ranks = new String[numberOfTeams];
		
		for (int i = 0; i < ranks.length; i++) {
			String id = "P" + (i + 1);
			ranks[i] = id + ": " + getTeamOnPlace(i + 1).map(Mannschaft::getName).orElse(league.getShortName() + year + id);
		}
		
		return ranks;
	}
	
	private void saveRanks() {
		ArrayList<String> allRanks = new ArrayList<>();
		
		String[] ranks = getRanks();
		for (int i = 0; i < ranks.length; i++) {
			allRanks.add(ranks[i]);
		}
		
		if (hasPlayoffs) {
			ranks = playoffs.getRanks();
			for (int i = 0; i < ranks.length; i++) {
				allRanks.add(ranks[i]);
			}
		}
		
		writeFile(workspace + "allRanks.txt", allRanks);
	}
	
	private void loadPlayoffs() {
		if (!hasPlayoffs)	return;
		
		koConfigFromFile = readFile(fileKOconfig);
		playoffs = new KORunde(this, koConfigFromFile.get(0));
	}
	
	private void savePlayoffs() {
		if (!hasPlayoffs)	return;
		
		koConfigFromFile.clear();
		playoffs.save();
		koConfigFromFile.add(playoffs.toString());
		
		writeFile(fileKOconfig, koConfigFromFile);
	}
	
	public void load() {
		fileMatchData = workspace + "Spieldaten.txt";
		fileMatches = workspace + "Spielplan.txt";
		fileTeams = workspace + "Mannschaften.txt";
		fileReferees = workspace + "Schiedsrichter.txt";
		fileKOconfig = workspace + "KOconfig.txt";
		
		loadReferees();
		loadTeams();
		initializeArrays();
		
		if (tabelle == null) {
			tabelle = new Tabelle(this);
			tabelle.setLocation((Fussball.WIDTH - tabelle.getSize().width) / 2, 50);
			tabelle.setVisible(false);
		}
		
		loadMatches();
		loadMatchData();
		loadPlayoffs();
		
		if (spieltag == null) {
			spieltag = new Spieltag(this);
			spieltag.setLocation((Fussball.WIDTH - spieltag.getSize().width) / 2, (Fussball.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
		}
		if (statistik == null) {
			statistik = new LigaStatistik(this);
			statistik.setLocation((Fussball.WIDTH - statistik.getSize().width) / 2, 50);
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
		saveRanks();
		saveReferees();
		saveTeams();
		saveMatches();
		saveMatchData();
		savePlayoffs();
		
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
		toString += hasPlayoffs + ";";
		
		if (!numberOfSubstitutions.isEmpty()) {
			ArrayList<String> subs = new ArrayList<>();
			Set<Dauer> durations = numberOfSubstitutions.keySet();
			for (Dauer duration : durations) {
				addAscending(subs, duration.toString() + ":" + numberOfSubstitutions.get(duration));
			}
			String addData = "", sep = "";
			for (int i = 0; i < subs.size(); i++) {
				addData += sep + subs.get(i);
				sep = ",";
			}
			toString += addData;
		}
		
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
		hasPlayoffs = Boolean.parseBoolean(split[index++]);
		
		numberOfSubstitutions = new HashMap<>();
		if (split.length > index) {
			String[] addData = split[index].split(",");
			for (int i = 0; i < addData.length; i++) {
				String[] addDataSplit = addData[i].split(":");
				numberOfSubstitutions.put(new Dauer(addDataSplit[0]), Integer.parseInt(addDataSplit[1]));
			}
		}
	}
}
