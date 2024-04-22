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
	private boolean teamsHaveKader;
	private boolean hasLeagueSplit;
	private boolean hasPlayoffs;
	
	private HashMap<Dauer, Integer> numberOfSubstitutions;
	
	private ArrayList<RankingCriterion> rankingCriteria;
	
	private int numberOfReferees;
	private ArrayList<Schiedsrichter> referees;
	private int numberOfTeams;
	private ArrayList<Mannschaft> teams;
	private int halfNOfTeamsUp;
	private int numberOfRegularMatchdays;
	private int numberOfMatchdaysBeforeSplit;
	private int numberOfMatchdaysAfterSplit;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int numberOfMatchesAgainstSameOpponentAfterSplit;
	private int numberOfSplitGroups;
	private ArrayList<ArrayList<Mannschaft>> splitGroups;
	private int currentMatchday;
	private Datum cMatchdaySetForDate;
	private int newestMatchday;
	private Datum nMatchdaySetForDate;
	private Uhrzeit nMatchdaySetUntilTime = TIME_UNDEFINED;
	
	private KORunde playoffs;
	private HashMap<String, Mannschaft> teamsFromOtherCompetition = new HashMap<>();
	
	private int[] numberOf;
	
	private Datum[] dates;
	private int[][] rkotIndices;
	
	private int numberOfRelativeKickoffTimes;
	private ArrayList<RelativeAnstossZeit> relativeKickOffTimes;
	private int[] defaultRelativeKickoffTimes;
	
	private Spiel[][] matches;
	private boolean[][] matchesSet;
	
	private Spieltag spieltag;
	private Tabelle tabelle;
	private LigaStatistik statistik;
	
	private boolean hasResultChanges;
	
	private boolean geladen;
	private String workspace;
	
	private String fileReferees;
	private ArrayList<String> refereesFromFile;
	
	private String fileRankingCriteria;
	private ArrayList<String> rankingCriteriaFromFile;
	
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
		splitGroups = new ArrayList<>();
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
	
	public boolean hasLeagueSplit() {
		return hasLeagueSplit;
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
	
	public int getNumberOfJointMatchdays() {
		return numberOfMatchdaysBeforeSplit;
	}
	
	public int getNumberOfRegularMatchdays() {
		return numberOfRegularMatchdays;
	}
	
	public int getNumberOfMatchdaysIncludingPlayoff() {
		return numberOfRegularMatchdays + (hasPlayoffs ? playoffs.getNumberOfRegularMatchdays() : 0);
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return numberOfMatchesPerMatchday;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return numberOfMatchesAgainstSameOpponent;
	}
	
	public int getNumberOfMatchesAgainstSameOpponentAfterSplit() {
		return numberOfMatchesAgainstSameOpponentAfterSplit;
	}
	
	public ArrayList<RankingCriterion> getRankingCriteria() {
		return rankingCriteria;
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
	
	public Tabelle getTable() {
		return tabelle;
	}
	
	public String[] getMatchdays() {
		if (hasPlayoffs) {
			String[] matchdays = new String[numberOfRegularMatchdays + playoffs.getNumberOfRegularMatchdays()];
			for (int i = 0; i < numberOfRegularMatchdays; i++) {
				matchdays[i] = (i + 1) + ". Spieltag";
			}
			if (playoffs.hasSecondLeg()) {
				matchdays[numberOfRegularMatchdays] = playoffs.getDescription() + " Hinspiel";
				matchdays[numberOfRegularMatchdays + 1] = playoffs.getDescription() + " Rückspiel";
			} else {
				matchdays[numberOfRegularMatchdays] = playoffs.getDescription();
			}
			return matchdays;
		} else {
			String[] matchdays = new String[numberOfRegularMatchdays];
			for (int i = 0; i < numberOfRegularMatchdays; i++) {
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
	
	public ArrayList<Mannschaft> getTeams() {
		return teams;
	}
	
	public ArrayList<String[]> getAllMatches(Mannschaft team) {
		ArrayList<String[]> allMatches = new ArrayList<>();
		
		for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
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
		return teams.get(index - 1);
	}
	
	public int getNumberOfSplitGroups() {
		return numberOfSplitGroups;
	}
	
	public ArrayList<Mannschaft> getTeamsInSplitGroup(int index) {
		calculateSplitGroups();
		return splitGroups.get(index);
	}
	
	public int getSplitGroup(Mannschaft team, int matchday) {
		calculateSplitGroups();
		if (matchday + 1 >= numberOfMatchdaysBeforeSplit) {
			int sg = 0;
			for (ArrayList<Mannschaft> splitGroup : splitGroups) {
				sg++;
				if (splitGroup.contains(team)) {
					return -sg;
				}
			}
		}
		
		return 0;
	}
	
	private void calculateSplitGroups() {
		if (!hasLeagueSplit)	return;
		if (!hasResultChanges)	return;
		hasResultChanges = false;
		splitGroups.clear();
		
		boolean allResultsSetUntilSplit = true;
		for (int matchday = 0; matchday < numberOfMatchdaysBeforeSplit; matchday++) {
			allResultsSetUntilSplit = allResultsSetUntilSplit && !isAnyResultNotSet(matchday);
		}
		if (allResultsSetUntilSplit) {
			Ranking ranking = new Ranking(this, numberOfMatchdaysBeforeSplit - 1, Tabellenart.COMPLETE);
			ranking.applyCriteria();
			
			int sg = 1;
			ArrayList<Integer> lastPlacesInSplitGroups = new ArrayList<>();
			while (sg <= numberOfSplitGroups) {
				lastPlacesInSplitGroups.add(sg * numberOfTeams / numberOfSplitGroups);
				splitGroups.add(new ArrayList<>());
				sg++;
			}
			
			for (Mannschaft team : teams) {
				int place = 1 + team.get(teams, 0, numberOfMatchdaysBeforeSplit - 1, Tabellenart.COMPLETE);
				sg = 0;
				while (sg < numberOfSplitGroups) {
					if (place <= lastPlacesInSplitGroups.get(sg)) {
						splitGroups.get(sg).add(team);
						break;
					}
					sg++;
				}
			}
			
		} else {
			splitGroups.add(teams);
		}
	}
	
	public String getMatchdayDescription(int matchday) {
		return league.getName() + " " + getSeasonFull("/") + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public int getCurrentMatchday() {
		Datum yesterday = new Datum(today, -1); // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (!yesterday.equals(cMatchdaySetForDate)) {
			if (yesterday.isBefore(getDate(0))) {
				currentMatchday = 0;
			} else if (!yesterday.isBefore(getDate(numberOfRegularMatchdays - 1))) {
				currentMatchday = numberOfRegularMatchdays - 1;
			} else {
				currentMatchday = 0;
				while (yesterday.isAfter(getDate(currentMatchday))) {
					if (yesterday.isAfter(getDate(currentMatchday + 1))) {
						currentMatchday++;
					} else if (getDate(currentMatchday).daysUntil(today) >= today.daysUntil(getDate(currentMatchday + 1))) {
						currentMatchday++;
					} else {
						break;
					}
				}
			}
			cMatchdaySetForDate = yesterday;
		}
		
		return currentMatchday;
	}
	
	public int getNewestStartedMatchday() {
		Uhrzeit time = new Uhrzeit();
		Zeitpunkt now = new Zeitpunkt(today, time);
		
		if (!today.equals(nMatchdaySetForDate) || !time.isBefore(nMatchdaySetUntilTime)) {
			nMatchdaySetUntilTime = END_OF_DAY;
			if (today.isBefore(getDate(0, 0))) {
				newestMatchday = 0;
			} else if (!today.isBefore(getDate(numberOfRegularMatchdays - 1, 0))) {
				newestMatchday = numberOfRegularMatchdays - 1;
			} else {
				newestMatchday = 0;
				AnstossZeit nextKickOffTime;
				while (now.isAfter(nextKickOffTime = getKickOffTime(newestMatchday + 1, 0))) {
					newestMatchday++;
				}
				if (today.equals(nextKickOffTime.getDate())) {
					nMatchdaySetUntilTime = nextKickOffTime.getTime();
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
		return getKickOffTime(matchday, matchIndex).toDisplay();
	}
	
	public Datum getDate(int matchday) {
		return dates[matchday];
	}
	
	public int getRKOTIndex(int matchday, int matchIndex) {
		return rkotIndices[matchday][matchIndex];
	}
	
	public void setDate(int matchday, Datum date) {
		dates[matchday] = date;
	}
	
	public void setRKOTIndex(int matchday, int matchIndex, int index) {
		rkotIndices[matchday][matchIndex] = index;
	}
	
	public AnstossZeit getKickOffTime(int matchday, int matchIndex) {
		return relativeKickOffTimes.get(getRKOTIndex(matchday, matchIndex)).getKickOffTime(dates[matchday]);
	}
	
	public Datum getDate(int matchday, int matchIndex) {
		return getKickOffTime(matchday, matchIndex).getDate();
	}
	
	public int addNewRelativeKickoffTime(int daysSince, Uhrzeit time) {
		numberOfRelativeKickoffTimes++;
		relativeKickOffTimes.add(RelativeAnstossZeit.of(numberOfRelativeKickoffTimes, daysSince, time));
		return numberOfRelativeKickoffTimes;
	}
	
	public int getIndexOfKOT(int daysSince, Uhrzeit time) {
		for (RelativeAnstossZeit az : relativeKickOffTimes) {
			if (az.matches(daysSince, time))	return az.getIndex();
		}
		return UNDEFINED;
	}
	
	public void useDefaultKickoffTimes(int matchday) {
		ArrayList<Integer> unsetMatches = new ArrayList<>(), unsetKOTs = new ArrayList<>();
		for (int i = 0; i < defaultRelativeKickoffTimes.length; i++) {
			unsetKOTs.add(defaultRelativeKickoffTimes[i]);
		}
		
		for (int i = 0; i < defaultRelativeKickoffTimes.length; i++) {
			if (unsetKOTs.contains(getRKOTIndex(matchday, i)))	unsetKOTs.remove(Integer.valueOf(getRKOTIndex(matchday, i)));
			else												unsetMatches.add(i);
		}
		
		for (int i = 0; i < unsetKOTs.size(); i++) {
			setRKOTIndex(matchday, unsetMatches.get(i), unsetKOTs.get(i));
		}
	}
	
	public int[] getDefaultKickoffTimes() {
		return defaultRelativeKickoffTimes;
	}
	
	public ArrayList<RelativeAnstossZeit> getRelativeKickOffTimes() {
		return relativeKickOffTimes;
	}
	
	public int getNumberOfRelativeKickoffTimes() {
		return numberOfRelativeKickoffTimes;
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
	
	public boolean allResultsSet() {
		for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
			if (isAnyResultNotSet(matchday)) 	return false;
		}
		return true;
	}
	
	public boolean isAnyResultNotSet(int matchday) {
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (!isResultSet(matchday, matchIndex)) 	return true;
		}
		return false;
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
	
	public void resultChanged() {
		hasResultChanges = true;
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
				setRKOTIndex(matchday, matchIndex, 0);
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
		if (2 * orderOfReverseFixtures.length != numberOfRegularMatchdays) {
			errorMessage("The given reverse fixtures order does not match the expected number of matchdays.");
			return;
		}
		
		boolean[] isFirstFixture = new boolean[numberOfRegularMatchdays];
		for (int i = 0; i < orderOfReverseFixtures.length; i++) {
			if (isFirstFixture[orderOfReverseFixtures[i] - 1] == true) {
				errorMessage("Some matchday appears to be twice in the reverse fixtures order.");
				return;
			}
			isFirstFixture[orderOfReverseFixtures[i] - 1] = true;
		}
		
		for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
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
		AnstossZeit[] kickOffTimes = new AnstossZeit[numberOfMatchesPerMatchday];
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			kickOffTimes[matchIndex] = getKickOffTime(matchday, matchIndex);
		}
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (kickOffTimes[m].getTime().isUndefined() && !kickOffTimes[m2].getTime().isUndefined())		hilfsarray[m]++;
				else if (kickOffTimes[m2].getTime().isUndefined() && !kickOffTimes[m].getTime().isUndefined())	hilfsarray[m2]++;
				else if (kickOffTimes[m2].isAfter(kickOffTimes[m]))												hilfsarray[m2]++;
				else if (kickOffTimes[m2].isBefore(kickOffTimes[m]))											hilfsarray[m]++;
				else {
					Spiel sp1 = getMatch(matchday, m), sp2 = getMatch(matchday, m2);
					if (sp2 != null && sp2.isInOrderBefore(sp1))	hilfsarray[m]++;
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
			oldKOTindices[matchIndex] = rkotIndices[matchday][matchIndex];
		}
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			setMatch(matchday, matchIndex, oldMatches[oldIndicesInNewOrder[matchIndex]]);
			setResult(matchday, matchIndex, oldResults[oldIndicesInNewOrder[matchIndex]]);
			rkotIndices[matchday][matchIndex] = oldKOTindices[oldIndicesInNewOrder[matchIndex]];
		}
	}
	
	private void saveNextMatches() {
		ArrayList<AnstossZeit> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfRegularMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				if (isResultSet(i, j) && getResult(i, j).isCancelled())	continue;
				AnstossZeit kickOffTime = getKickOffTime(i, j);
				if (isMatchSet(i, j) && kickOffTime.hasDate() && (!inThePast(kickOffTime.plusMinutes(105)) || !isResultSet(i, j))) {
					if (nextMatches.size() < Fussball.numberOfMissingResults || kickOffTime.isBefore(nextMatches.get(Fussball.numberOfMissingResults - 1))) {
						addInOrder(nextMatches, kickOffTime);
					}
				}
			}
		}
		
		if (hasPlayoffs) {
			ArrayList<AnstossZeit> nextMatchesPO = playoffs.getNextMatches();
			for (int i = 0; i < nextMatchesPO.size(); i++) {
				addInOrder(nextMatches, nextMatchesPO.get(i));
			}
		}
		
		String fileName = workspace + "nextMatches.txt";
		if (nextMatches.size() > 0) {
			ArrayList<String> nextMatchesString = new ArrayList<>();
			for (int i = 0; i < Fussball.numberOfMissingResults && i < nextMatches.size(); i++) {
				nextMatchesString.add("" + nextMatches.get(i).comparable());
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
		defaultRelativeKickoffTimes = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			defaultRelativeKickoffTimes[i] = Integer.parseInt(split[i]);
		}
	}
	
	private String getDefaultKickoffTimesRepresentation() {
		String dktimes = "";
		if (defaultRelativeKickoffTimes.length >= 1) {
			dktimes += defaultRelativeKickoffTimes[0];
			for (int i = 1; i < defaultRelativeKickoffTimes.length; i++) {
				dktimes += "," + defaultRelativeKickoffTimes[i];
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
		tabelle.calculate(numberOfRegularMatchdays - 1, Tabellenart.COMPLETE);
		return getTeamOnPlace(teamOrigin.getPlaceIndex());
	}
	
	private void initializeArrays() {
		dates = new Datum[numberOfRegularMatchdays];
		rkotIndices = new int[numberOfRegularMatchdays][numberOfMatchesPerMatchday];
		
		matches = new Spiel[numberOfRegularMatchdays][numberOfMatchesPerMatchday];
		matchesSet = new boolean[numberOfRegularMatchdays][numberOfMatchesPerMatchday];
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
		if (numberOfTeams >= 2) {
			numberOfMatchdaysBeforeSplit = numberOfMatchesAgainstSameOpponent * (2 * halfNOfTeamsUp - 1);
			numberOfMatchdaysAfterSplit = (hasLeagueSplit ? numberOfMatchesAgainstSameOpponentAfterSplit * (halfNOfTeamsUp - 1) : 0);
			numberOfRegularMatchdays = numberOfMatchdaysBeforeSplit + numberOfMatchdaysAfterSplit;
		}
		else						numberOfRegularMatchdays = 0;
		
		teams = new ArrayList<Mannschaft>();
		for (int i = 0; i < numberOfTeams; i++) {
			teams.add(new Mannschaft(i + 1, this, teamsFromFile.get(i + 1)));
		}
	}
	
	public void saveTeams() {
		teamsFromFile.clear();
		
		teamsFromFile.add("" + numberOfTeams);
		for (int i = 0; i < teams.size(); i++) {
			teams.get(i).save();
			teamsFromFile.add(teams.get(i).toString());
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
			numberOfRelativeKickoffTimes = Integer.parseInt(split[0]);
			relativeKickOffTimes = new ArrayList<>();
			relativeKickOffTimes.add(RelativeAnstossZeit.of(0, 0, TIME_UNDEFINED));
			for (int counter = 1; counter <= numberOfRelativeKickoffTimes; counter++) {
				relativeKickOffTimes.add(RelativeAnstossZeit.of(counter, split[counter]));
			}
			
			for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
				split = matchesFromFile.get(matchday + 1).split(";");
				
				setMatchesSetFromRepresentation(matchday, split[0]);
				
				int matchIndex = 0;
				if (!isNoMatchSet(matchday)) {
					// Daten und Uhrzeiten
					String[] koTimes = split[1].split(":");
					setDate(matchday, koTimes[0].equals("0") ? DATE_UNDEFINED : new Datum(koTimes[0]));
					for (matchIndex = 0; (matchIndex + 1) < koTimes.length; matchIndex++) {
						setRKOTIndex(matchday, matchIndex, Integer.parseInt(koTimes[matchIndex + 1]));
					}
					
					// Spielplan
					for (matchIndex = 0; (matchIndex + 2) < split.length; matchIndex++) {
						Spiel match = null;
						
						if (isMatchSet(matchday, matchIndex)) {
							match = new Spiel(this, matchday, getKickOffTime(matchday, matchIndex), split[matchIndex + 2]);
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
		for (int i = 0; i < defaultRelativeKickoffTimes.length; i++) {
			if (defaultRelativeKickoffTimes[i] > lastDefault)	lastDefault = defaultRelativeKickoffTimes[i];
		}
		
		ArrayList<RelativeAnstossZeit> newKOTs = new ArrayList<>();
		for (int i = 0; i <= lastDefault; i++) {
			newKOTs.add(relativeKickOffTimes.get(i));
		}
		
		ArrayList<ArrayList<Integer>> allOccurrences = new ArrayList<>();
		for (int i = lastDefault + 1; i <= numberOfRelativeKickoffTimes; i++) {
			allOccurrences.add(getAllOccurrencesOfKOT(i));
		}
		
		int[] newOrder = new int[numberOfRelativeKickoffTimes - lastDefault];
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
					newKOTs.add(relativeKickOffTimes.get(i + lastDefault + 1));
					if (i == order)	continue;
					for (int j = 0; j < allOccurrences.get(i).size(); j++) {
						int matchday = allOccurrences.get(i).get(j) / 100 - 1, matchIndex = allOccurrences.get(i).get(j) % 100 - 1;
						setRKOTIndex(matchday, matchIndex, order + lastDefault + 1);
					}
				}
			}
		}
		
		relativeKickOffTimes = newKOTs;
	}
	
	private ArrayList<Integer> getAllOccurrencesOfKOT(int kotIndex) {
		ArrayList<Integer> occurrences = new ArrayList<>();
		for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
				if (rkotIndices[matchday][matchIndex] == kotIndex)	occurrences.add(100 * (matchday + 1) + (matchIndex + 1));
			}
		}
		return occurrences;
	}
	
	public void saveMatches() {
		matchesFromFile.clear();
		
		String row = numberOfRelativeKickoffTimes + ";";
		for (int i = 1; i <= numberOfRelativeKickoffTimes; i++) {
			row = row + relativeKickOffTimes.get(i) + ";";
		}
		matchesFromFile.add(row);
		
		for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
			row = getMatchesSetRepresentation(matchday) + ";";
			if (!isNoMatchSet(matchday)) {
				Datum date = getDate(matchday);
				row += date.equals(DATE_UNDEFINED) ? 0 : date.comparable();
				for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
					row += ":" + getRKOTIndex(matchday, matchIndex);
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
			
			for (int matchday = 0; matchday < numberOfRegularMatchdays && matchday < matchDataFromFile.size(); matchday++) {
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
		
		for (int matchday = 0; matchday < numberOfRegularMatchdays; matchday++) {
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
				matchDataFromFile.add(getMatch(matchday, matchIndex) != null ? getMatch(matchday, matchIndex).fullString() : "null");
			}
		}
		
		writeFile(fileMatchData, matchDataFromFile);
	}
	
	public Optional<Mannschaft> getTeamOnPlace(int place) {
		if (place < 1 || place > teams.size())	return Optional.empty();
		if (!allResultsSet()) {
			return Optional.empty();
		}
		
		for (Mannschaft team : teams) {
			if (team.get(teams, 0, numberOfRegularMatchdays - 1, Tabellenart.COMPLETE) == place - 1)		return Optional.of(team);
		}
		
		return Optional.empty();
	}
	
	public String[] getRanks() {
		tabelle.calculate(numberOfRegularMatchdays - 1, Tabellenart.COMPLETE);
		
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
	
	private void loadRankingCriteria() {
		rankingCriteriaFromFile = readFile(fileRankingCriteria);
		rankingCriteria = new ArrayList<>();
		
		for (int i = 0; i < rankingCriteriaFromFile.size(); i++) {
			rankingCriteria.add(RankingCriterion.parse(rankingCriteriaFromFile.get(i)));
		}
	}
	
	public void load() {
		fileMatchData = workspace + "Spieldaten.txt";
		fileMatches = workspace + "Spielplan.txt";
		fileTeams = workspace + "Mannschaften.txt";
		fileReferees = workspace + "Schiedsrichter.txt";
		fileKOconfig = workspace + "KOconfig.txt";
		fileRankingCriteria = workspace + "RankingKriterien.txt";
		
		loadRankingCriteria();
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
		
		calculateSplitGroups();
		
		tabelle.calculate(numberOfRegularMatchdays - 1, Tabellenart.COMPLETE);
		
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
		toString += teamsHaveKader + ";";
		toString += getAnzahlRepresentation() + ";";
		toString += hasPlayoffs + ";";
		toString += hasLeagueSplit + ";";
		
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
		teamsHaveKader = Boolean.parseBoolean(split[index++]);
		numberOf = getAnzahlFromString(split[index++]);
		hasPlayoffs = Boolean.parseBoolean(split[index++]);
		hasLeagueSplit = Boolean.parseBoolean(split[index++]);
		numberOfMatchesAgainstSameOpponentAfterSplit = hasLeagueSplit ? 1 : 0;
		numberOfSplitGroups = hasLeagueSplit ? 2 : 1;
		
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
