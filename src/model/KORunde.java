package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import model.tournament.*;

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
	private ArrayList<Mannschaft> teams;
	private boolean checkTeamsFromPreviousRound = true;
	
	private ArrayList<RankingCriterion> rankingCriteria;
	
	private boolean hasSecondLeg;
	private boolean isETPossible = true;
	private boolean is4thSubPossible = false;
	private boolean teamsHaveKader;
	
	private KOOrigin[] teamsOrigins;
	
	private Spiel[][] matches;
	private boolean[][] matchesSet;
	
	private boolean hasResultChanges;
	
	private String workspace;
	
	private String fileRankingCriteria;
	private ArrayList<String> rankingCriteriaFromFile;
	
	private String fileTeams;
	private ArrayList<String> teamsFromFile;
	
	private String fileMatches;
	private ArrayList<String> matchesFromFile;
	
	private String fileMatchData;
	private ArrayList<String> matchDataFromFile;
	
	private Datum startDate;
	private Datum finalDate;
	private RelativeAnstossZeit[][] relativeKickOffTimes;
	
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
		return belongsToALeague ? lSeason.getNumberOfRegularMatchdays() : 0;
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
	
	public ArrayList<RankingCriterion> getRankingCriteria() {
		return rankingCriteria;
	}
	
	public Tabelle getTable() {
		return null;
	}
	
	public String[] getMatchdays() {
		if (belongsToALeague)	return lSeason.getMatchdays();
		
		String[] matchdays = new String[numberOfMatchdays];
		if (hasSecondLeg) {
			matchdays[0] = "Hinspiel" + (numberOfMatchesPerMatchday > 1 ? "e" : "");
			matchdays[1] = "Rückspiel" + (numberOfMatchesPerMatchday > 1 ? "e" : "");
		} else {
			matchdays[0] = "Spieltag";
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

	public ArrayList<Mannschaft> getTeams() {
		if (checkTeamsFromPreviousRound)	refreshTeams();
		return teams;
	}

	public Mannschaft getTeamFromId(int id) {
		if (checkTeamsFromPreviousRound)	refreshTeams();
		return teams.get(id - 1);
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
		for (int i = 0; i < teams.size() && !foundTeam; i++) {
			foundTeam = team.equals(teams.get(i));
		}
		if (foundTeam) {
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String md = numberOfMatchdays == 1 ? shortName : matchday + 1 + "";
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
		}
		
		return allMatches;
	}
	
	public String getDateOfTeam(int matchday, Mannschaft team) {
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (isMatchSet(matchday, matchIndex)) {
				if (team.equals(getMatch(matchday, matchIndex).getHomeTeam()) || team.equals(getMatch(matchday, matchIndex).getAwayTeam()))
					return getDateAndTime(matchday, matchIndex);
			}
		}
		
		return "n.a.";
	}
	
	public KOOrigin getTeamsOrigin(int team) {
		return teamsOrigins[team];
	}
	
	public Optional<Mannschaft> getInvariantTeam(int index) {
		if (teamsOrigins[index].getKOOriginType().isFromPreviousRound())	return Optional.empty();
		return Optional.of(teams.get(index));
	}
	
	private void refreshTeams() {
		for (int i = 0; i < teamsOrigins.length; i++) {
			if (teamsOrigins[i].getKOOriginType().isFromPreviousRound()) {
				if (belongsToALeague)	teams.set(i, lSeason.getTeamFromOrigin(teamsOrigins[i]).orElse(null));
				else					teams.set(i, tSeason.getTeamFromOrigin(teamsOrigins[i]).orElse(null));
			}
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
	
	public int getNumberOfJointMatchdays() {
		return numberOfMatchdays;
	}
	
	public int getNumberOfRegularMatchdays() {
		return numberOfMatchdays;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return numberOfMatchesAgainstSameOpponent;
	}
	
	public int getNumberOfMatchesAgainstSameOpponentAfterSplit() {
		return 0;
	}
	
	public boolean teamsHaveKader() {
		return teamsHaveKader;
	}
	
	public int getCurrentMatchday() {
		if (numberOfMatchesPerMatchday == 0)	return 0;
		if (numberOfMatchdays == 2) {
			if (!today.equals(cMatchdaySetForDate)) {
				if (getDate(0, 0).equals(startDate)) {
					currentMatchday = 0;
				} else if (!today.isAfter(getDate(0, 0))) {
					currentMatchday = 0;
				} else if (!today.isBefore(getDate(1, 0)) && !isNoMatchSet(1)) {
					currentMatchday = 1;
				} else {
					currentMatchday = 0;
					if (getDate(0, 0).daysUntil(today) >= today.daysUntil(getDate(1, 0))) {
						currentMatchday++;
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
			Zeitpunkt now = new Zeitpunkt(today, time);
			
			if (!today.equals(nMatchdaySetForDate) || !time.isBefore(nMatchdaySetUntilTime)) {
				nMatchdaySetUntilTime = END_OF_DAY;
				if (!today.isAfter(getDate(0, 0))) {
					newestMatchday = 0;
				} else if (!today.isBefore(getDate(numberOfMatchdays - 1, 0))) {
					newestMatchday = numberOfMatchdays - 1;
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
		} else if (numberOfMatchdays == 1) {
			newestMatchday = 0;
		}
		
		return newestMatchday;
	}
	
	public String getDateAndTime(int matchday, int matchIndex) {
		return getKickOffTime(matchday, matchIndex).toDisplay();
	}
	
	public RelativeAnstossZeit getRelativeKickOffTime(int matchday, int matchIndex) {
		return relativeKickOffTimes[matchday][matchIndex];
	}
	
	public void setRelativeKickOffTime(int matchday, int matchIndex, RelativeAnstossZeit kickOffTime) {
		relativeKickOffTimes[matchday][matchIndex] = kickOffTime;
	}
	
	public AnstossZeit getKickOffTime(int matchday, int matchIndex) {
		return Optional.ofNullable(getRelativeKickOffTime(matchday, matchIndex))
				.map(rkot -> rkot.getKickOffTime(startDate)).orElse(KICK_OFF_TIME_UNDEFINED);
	}
	
	public Datum getDate(int matchday, int matchIndex) {
		return getKickOffTime(matchday, matchIndex).getDate();
	}
	
	// Spielplan eingetragen
	
	public boolean isNoMatchSet(int matchday) {
		for (int matchIndx = 0; matchIndx < numberOfMatchesPerMatchday; matchIndx++) {
			if (isMatchSet(matchday, matchIndx)) 	return false;
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
		if (representation.equals("true")) {
			representation = "";
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++)	representation += "t";
		} else if (representation.equals("false")) {
			representation = "";
			for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++)	representation += "f";
		}
		
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (representation.charAt(matchIndex) == 't')		setMatchSet(matchday, matchIndex, true);
			else if (representation.charAt(matchIndex) == 'f')	setMatchSet(matchday, matchIndex, false);
		}
	}
	
	// Ergebnisplan eingetragen
	
	public boolean allResultsSet() {
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			if (isNoResultSet(matchday)) 	return false;
		}
		return true;
	}
	
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
			if (teams.get(match.home() - 1) != null)	teams.get(match.home() - 1).setMatch(key, match);
			if (teams.get(match.away() - 1) != null)	teams.get(match.away() - 1).setMatch(key, match);
		} else {
			if (isMatchSet(matchday, matchIndex)) {
				Spiel previousMatch = getMatch(matchday, matchIndex);
				if (teams.get(previousMatch.home() - 1) != null)	teams.get(previousMatch.home() - 1).resetMatch(key);
				if (teams.get(previousMatch.away() - 1) != null)	teams.get(previousMatch.away() - 1).resetMatch(key);
				setRelativeKickOffTime(matchday, matchIndex, null);
			}
		}
		matches[matchday][matchIndex] = match;
		setMatchSet(matchday, matchIndex, match != null);
	}
	
	public String getKey(int matchday) {
		return shortName + twoDigit(matchday);
	}
	
	public void resetMatchday(int matchday) {
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			setMatch(matchday, match, null);
		}
	}
	
	public void createSecondLegs() {
		if (numberOfMatchdays == 1)	return;
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			if (!isMatchSet(0, matchIndex)) {
				message("Es sind nicht alle Hinspiele eingegeben.");
				return;
			}
		}
		
		boolean hasData = false;
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday && !hasData; matchIndex++) {
			hasData = isMatchSet(1, matchIndex);
		}
		if (hasData && yesNoDialog("Willst du die gespeicherten Daten überschreiben?") != JOptionPane.YES_OPTION)	return;
		
		ArrayList<Spiel> secondLegs = new ArrayList<>();
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			addInOrder(secondLegs, getMatch(0, matchIndex).getReverseFixture(1));
		}
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			setMatch(1, matchIndex, secondLegs.get(matchIndex));
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
				if (kickOffTimes[m2].isAfter(kickOffTimes[m]))			hilfsarray[m2]++;
				else if (kickOffTimes[m2].isBefore(kickOffTimes[m]))	hilfsarray[m]++;
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
		RelativeAnstossZeit[] oldKickOffTimes = new RelativeAnstossZeit[numberOfMatchesPerMatchday];
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			oldMatches[matchIndex] = getMatch(matchday, matchIndex);
			oldResults[matchIndex] = getResult(matchday, matchIndex);
			oldKickOffTimes[matchIndex] = getRelativeKickOffTime(matchday, matchIndex);
		}
		
		for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
			setMatch(matchday, matchIndex, oldMatches[oldIndicesInNewOrder[matchIndex]]);
			setResult(matchday, matchIndex, oldResults[oldIndicesInNewOrder[matchIndex]]);
			setRelativeKickOffTime(matchday, matchIndex, oldKickOffTimes[oldIndicesInNewOrder[matchIndex]]);
		}
	}
	
	private int getIndexOfReverseTie(int matchID) {
		if (!hasSecondLeg)	return UNDEFINED;
		
		int matchIndex = matchID - 1;
		Spiel firstLeg = getMatch(0, matchIndex);
		
		for (int i = 0; i < numberOfMatchesPerMatchday; i++) {
			Spiel match = getMatch(1, i);
			if (match != null && match.away() == firstLeg.home() && match.home() == firstLeg.away()) {
				return i;
			}
		}
		
		return UNDEFINED;
	}
	
	public boolean isTieDecided(int matchID) {
		int matchIndex = matchID - 1;
		if (!isResultSet(0, matchIndex)) {
			return false;
		}
		
		if (!hasSecondLeg)	return true;
		
		int reverseTie = getIndexOfReverseTie(matchID);
		
		return isResultSet(numberOfMatchdays - 1, reverseTie);
	}
	
	public Optional<KOOrigin> getOriginsOfTie(int matchID) {
		int matchIndex = matchID - 1;
		if (!isMatchSet(0, matchIndex))	return Optional.empty();
		
		Spiel match = getMatch(0, matchIndex);
		KOOrigin originFirstTeam = teamsOrigins[match.home() - 1];
		KOOrigin originSecondTeam = teamsOrigins[match.away() - 1];
		
		return Optional.of(new KOOriginTwoOrigins(originFirstTeam, originSecondTeam));
	}
	
	/**
	 * Returns the optional KOOrigin of the team winning this tie.
	 * @param matchID The id of the match, beginning from 1
	 * @return
	 */
	public Optional<KOOrigin> getOriginOfWinnerOfTie(int matchID) {
		return Optional.of(getTeamIDOf(matchID, true)).filter(id -> id != UNDEFINED).map(id -> teamsOrigins[id - 1]);
	}
	
	/**
	 * Returns the optional KOOrigin of the team losing this tie.
	 * @param matchID The id of the match, beginning from 1
	 * @return
	 */
	public Optional<KOOrigin> getOriginOfLoserOfTie(int matchID) {
		return Optional.of(getTeamIDOf(matchID, false)).filter(id -> id != UNDEFINED).map(id -> teamsOrigins[id - 1]);
	}
	
	private int getTeamIDOf(int matchID, boolean isWinnerRequested) {
		int matchIndex = matchID - 1;
		if (!isResultSet(0, matchIndex)) {
			return UNDEFINED;
		}
		
		// find out involved teams
		int teamHomeFirstLeg = getMatch(0, matchIndex).home();
		int teamAwayFirstLeg = getMatch(0, matchIndex).away();
		Ergebnis firstLeg = getResult(0, matchIndex);
		
		if (hasSecondLeg) {
			// first and second leg don't have to be in the same position on the plan and most likely they aren't!!
			
			// get index of second leg match
			int index = getIndexOfReverseTie(matchID);
			
			if (index != UNDEFINED && isResultSet(numberOfMatchdays - 1, index)) {
				Ergebnis secondLeg = getResult(1, index);
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
		return UNDEFINED;
	}

	public String[] getRanks() {
		String[] ranks = new String[2 * numberOfMatchesPerMatchday];
		String competition = belongsToALeague ? lSeason.getLeague().getShortName() + lSeason.getYear() : tSeason.getTournament().getShortName() + tSeason.getYear();
		
		int pos = 0;
		for (int i = 0; i < numberOfMatchesPerMatchday; i++) {
			String matchID = getShortName() + twoDigit(i + 1);
			String key = matchID + "W";
			ranks[pos] = key + ": ";
			int id = getTeamIDOf(i + 1, true);
			if (id != UNDEFINED)	ranks[pos++] += getTeamFromId(id).getName();
			else {
				if (isMatchSet(0, i)) {
					Spiel match = getMatch(0, i);
					ranks[pos++] += Optional.ofNullable(match.getHomeTeam()).map(Mannschaft :: getName).filter(n -> !n.contains(VERSUS)).orElse(getTeamsOrigin(match.home() - 1).getOrigin())
							+ VERSUS + Optional.ofNullable(match.getAwayTeam()).map(Mannschaft :: getName).filter(n -> !n.contains(VERSUS)).orElse(getTeamsOrigin(match.away() - 1).getOrigin());
				}
				else	ranks[pos++] += competition + key;
			}
			
			key = matchID + "L";
			ranks[pos] = key + ": ";
			id = getTeamIDOf(i + 1, false);
			if (id != UNDEFINED)	ranks[pos++] += getTeamFromId(id).getName();
			else {
				if (isMatchSet(0, i)) {
					Spiel match = getMatch(0, i);
					ranks[pos++] += Optional.ofNullable(match.getHomeTeam()).map(Mannschaft :: getName).filter(n -> !n.contains(VERSUS)).orElse(getTeamsOrigin(match.home() - 1).getOrigin())
							+ VERSUS + Optional.ofNullable(match.getAwayTeam()).map(Mannschaft :: getName).filter(n -> !n.contains(VERSUS)).orElse(getTeamsOrigin(match.away() - 1).getOrigin());
				}
				else	ranks[pos++] += competition + key;
			}
		}
		
		return ranks;
	}
	
	public ArrayList<AnstossZeit> getNextMatches() {
		ArrayList<AnstossZeit> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				if (isResultSet(i, j) && getResult(i, j).isCancelled())	continue;
				AnstossZeit kickOffTime = getKickOffTime(i, j);
				if (isMatchSet(i, j) && (!inThePast(kickOffTime.plusMinutes(105)) || !isResultSet(i, j))) {
					if (nextMatches.size() < Fussball.numberOfMissingResults || kickOffTime.isBefore(nextMatches.get(Fussball.numberOfMissingResults - 1))) {
						addInOrder(nextMatches, kickOffTime);
					}
				}
			}
		}
		
		return nextMatches;
	}
	
	private void load() {
		String isQuali = isQ ? "Qualifikation" + File.separator : "";
		workspace = (belongsToALeague ? lSeason.getWorkspace() : tSeason.getWorkspace()) + isQuali + name + File.separator;
		
		fileTeams = workspace + "Mannschaften.txt";
		fileMatchData = workspace + "Spieldaten.txt";
		fileMatches = workspace + "Spielplan.txt";
		fileRankingCriteria = workspace + "RankingKriterien.txt";
		
		loadRankingCriteria();
		
		loadTeams();
		initializeArrays();
		
		setCheckTeamsFromPreviousRound(false);
		loadMatches();
		setCheckTeamsFromPreviousRound(true);
		loadMatchData();
		
		if (spieltag == null) {
			spieltag = new Spieltag(this);
			spieltag.setLocation((Fussball.WIDTH - spieltag.getSize().width) / 2, (Fussball.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
		}
	}
	
	public void save() {
		saveMatches();
		saveMatchData();
		saveTeams();
	}
	
	private void initializeArrays() {
		relativeKickOffTimes = new RelativeAnstossZeit[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		matches = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		matchesSet = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
	}
	
	private void loadRankingCriteria() {
		rankingCriteriaFromFile = readFile(fileRankingCriteria);
		rankingCriteria = new ArrayList<>();
		
		for (int i = 0; i < rankingCriteriaFromFile.size(); i++) {
			rankingCriteria.add(RankingCriterion.parse(rankingCriteriaFromFile.get(i)));
		}
	}
	
	private void loadTeams() {
		teamsFromFile = readFile(fileTeams);
		
		numberOfTeams = teamsFromFile.size();
		teams = new ArrayList<Mannschaft>(numberOfTeams);
		numberOfMatchesAgainstSameOpponent = hasSecondLeg ? 2 : 1;
		numberOfMatchdays = hasSecondLeg ? 2 : 1;
		numberOfMatchesPerMatchday = numberOfTeams / 2;
		
		teamsOrigins = new KOOrigin[numberOfTeams];
		for (int i = 0; i < teamsOrigins.length; i++) {
			String origin = teamsFromFile.get(i);
			KOOriginType type = KOOriginType.getTypeFromOrigin(origin); 
			origin = origin.replace(type.getPrefix(), "");
			
			switch (type) {
				case PREQUALIFIED:
					teamsOrigins[i] = new KOOriginPrequalified(origin, this, i);
					teams.add(new Mannschaft(i, this, origin));
					break;
				case PREVIOUS_GROUP_STAGE:
					teamsOrigins[i] = new KOOriginPreviousGroupStage(origin, isQ);
					teams.add(null);
					break;
				case PREVIOUS_KNOCKOUT_ROUND:
					teamsOrigins[i] = new KOOriginPreviousKnockoutRound(origin, isQ);
					teams.add(null);
					break;
				case PREVIOUS_LEAGUE:
					teamsOrigins[i] = new KOOriginPreviousLeague(origin);
					teams.add(null);
					break;
				case OTHER_COMPETITION:
					teamsOrigins[i] = new KOOriginOtherCompetition(origin, this, i);
					if (belongsToALeague)	teams.add(lSeason.getTeamFromOtherCompetition(i, this, teamsOrigins[i]));
					else					teams.add(tSeason.getTeamFromOtherCompetition(i, this, teamsOrigins[i]));
					break;
				default:
					break;
			}
		}
		
		refreshTeams();
	}
	
	public void saveTeams() {
		teamsFromFile = new ArrayList<>();
		for (int i = 0; i < numberOfTeams; i++) {
			if (teams.get(i) != null)	teams.get(i).save();
			teamsFromFile.add(teamsOrigins[i].toString());
		}
		writeFile(fileTeams, teamsFromFile);
	}
	
	private void loadMatches() {
		try {
			matchesFromFile = readFile(fileMatches);
			
			for (int matchday = 0; matchday < matchesFromFile.size(); matchday++) {
				String[] split = matchesFromFile.get(matchday).split(";");
				
				setMatchesSetFromRepresentation(matchday, split[0]);
				
				int matchIndex = 0;
				if (!isNoMatchSet(matchday)) {
					// Spieltagsdaten
					String[] koTimes = split[1].split(":");
					for (matchIndex = 0; matchIndex < koTimes.length; matchIndex++) {
						if (koTimes[matchIndex].equals(TO_BE_DATED)) {
							setRelativeKickOffTime(matchday, matchIndex, null);
						} else {
							setRelativeKickOffTime(matchday, matchIndex, new RelativeAnstossZeit(0, koTimes[matchIndex]));
						}
					}
					
					// Herkunften der Mannschaften
					for (matchIndex = 0; matchIndex + 2 < split.length; matchIndex++) {
						Spiel match = null;
						if (isMatchSet(matchday, matchIndex)) {
							match = new Spiel(this, matchday, getKickOffTime(matchday, matchIndex), split[matchIndex + 2]);
						}
						
						setMatch(matchday, matchIndex, match);
					}
				}
				
				while(matchIndex < numberOfMatchesPerMatchday) {
					setMatch(matchday, matchIndex, null);
					setRelativeKickOffTime(matchday, matchIndex, null);
					matchIndex++;
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
			if (numberOfMatchesPerMatchday == 0)	continue;
			String row = getMatchesSetRepresentation(matchday) + ";";
			if (!isNoMatchSet(matchday)) {
				for (int matchIndex = 0; matchIndex < numberOfMatchesPerMatchday; matchIndex++) {
					if (getRelativeKickOffTime(matchday, matchIndex) == null)	row += TO_BE_DATED;
					else	row += getRelativeKickOffTime(matchday, matchIndex).toString();
					if (matchIndex + 1 < numberOfMatchesPerMatchday)	row += ":";
					else												row += ";";
				}
				
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
	
	public String toString() {
		String toString = name + ";";
		toString += shortName + ";";
		toString += hasSecondLeg + ";";
		
		return toString;
	}
	
	private void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		name = split[index++];
		shortName = split[index++];
		hasSecondLeg = Boolean.parseBoolean(split[index++]);
	}
}
