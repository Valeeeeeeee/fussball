package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import analyse.SpielPerformance;

public class Mannschaft {
	
	private String trennZeichen = ";";
	
	private int id;
	private String name;
	private String nameForFileSearch;
	private String foundingDate;
	private boolean isClub;
	
	private int place = -1;
	private int numOfMatches;
	private int numOfWins;
	private int numOfDraws;
	private int numOfLosses;
	private int numOfGoals;
	private int numOfCGoals;
	private int numOfAwayGoals;
	private int goalDiff;
	private int points;
	private ArrayList<Mannschaft> valuesCorrectAsOfOpponents;
	private int valuesCorrectAsOfMatchday = -1;
	private Tabellenart valuesCorrectAsOf;
	private int deductedPoints = 0;
	
	private int numberOfMatchdays;
	private int[][] data;
	private int[] matchdayOrder;
	private HashMap<String, Spiel> matches;
	private HashMap<String, Ergebnis> results;
	
	public final static int OPPONENT = 0;
	public final static int GOALS = 1;
	public final static int CGOALS = 2;
	public final static int POINTS = 3;
	
	private boolean[] homeaway;
	public final static int HOME = 4;
	public final static int AWAY = 5;
	
	public static final int NUMBEROFPERFORMANCEDATA = 10;
	public static final int MATCHES_PLAYED = 0;
	public static final int MATCHES_STARTED = 1;
	public static final int MATCHES_SUB_ON = 2;
	public static final int MATCHES_SUB_OFF = 3;
	public static final int MINUTES_PLAYED = 4;
	public static final int GOALS_SCORED = 5;
	public static final int GOALS_ASSISTED = 6;
	public static final int BOOKED = 7;
	public static final int BOOKED_TWICE = 8;
	public static final int RED_CARDS = 9;
	
	private Wettbewerb competition;
	private LigaSaison lSeason;
	private Gruppe group;
	
	private String kaderFileName;
	private int numberOfTeamAffiliations;
	private ArrayList<Spieler> kader = new ArrayList<>();
	private ArrayList<TeamAffiliation> teamAffiliations = new ArrayList<>();
	private int[] numberOfPlayersByPosition;
	private ArrayList<TeamAffiliation> eligiblePlayers = new ArrayList<TeamAffiliation>();
	private ArrayList<TeamAffiliation> ineligiblePlayers = new ArrayList<TeamAffiliation>();
	private Datum lastUpdatedEligibleForDate;
	private Datum lastUpdatedIneligibleForDate;
	private int[] currentNumberOfPlayersByPosition;

	private boolean playsInLeague = false;
	private boolean playsInGroup = false;
	private boolean playsInKORound = false;

	public Mannschaft(int id, Wettbewerb competition, String mannschaftsDaten) {
		this.id = id;
		this.competition = competition;
		parseString(mannschaftsDaten);
		
		matches = new HashMap<>();
		results = new HashMap<>();
		
		if (competition != null) {
			isClub = competition.isClubCompetition();
			if (competition instanceof LigaSaison)		lSeason = (LigaSaison) competition;
			else if (competition instanceof Gruppe)		group = (Gruppe) competition;
			playsInLeague = lSeason != null;
			playsInGroup = group != null;
			playsInKORound = competition instanceof KORunde;
			
			initializeArrays();
			loadKader();
		}
	}
	
	private void initializeArrays() {
		numberOfMatchdays = 0;
		if (playsInLeague)		numberOfMatchdays = lSeason.getNumberOfRegularMatchdays();
		else if (playsInGroup)	numberOfMatchdays = group.getNumberOfRegularMatchdays();
		data = new int[numberOfMatchdays][4];
		homeaway = new boolean[numberOfMatchdays];
	}
	
	public Wettbewerb getCompetition() {
		return competition;
	}
	
	public String getPhotoDirectory() {
		return competition.getWorkspace() + "Bilder" + File.separator + nameForFileSearch + File.separator;
	}
	
	public int getNumberOfPlayers(boolean onlyEligible, boolean forceUpdate) {
		if (onlyEligible) {
			updateEligiblePlayers(today, forceUpdate);
			return eligiblePlayers.size();
		}
		return kader.size();
	}
	
	public int getNumberOfUsedPlayers() {
		int numberOfUsedPlayers = 0;
		
		for (int i = 0; i < teamAffiliations.size(); i++) {
			if (teamAffiliations.get(i).getSeasonPerformance().matchesPlayed() > 0)	numberOfUsedPlayers++;
		}
		
		return numberOfUsedPlayers;
	}
	
	public ArrayList<Spieler> getPlayers() {
		return kader;
	}
	
	private void sortAffiliations() {
		ArrayList<TeamAffiliation> unsorted = new ArrayList<>();
		for (TeamAffiliation teamAffiliation : teamAffiliations) {
			unsorted.add(teamAffiliation);
		}
		numberOfPlayersByPosition = new int[4];
		teamAffiliations.clear();
		int index;
		for (int i = 0; i < unsorted.size(); i++) {
			TeamAffiliation teamAffiliation = unsorted.get(i);
			index = 0;
			for (int j = 0; j < teamAffiliations.size(); j++) {
				if (teamAffiliation.inOrderBefore(teamAffiliations.get(j)))	break;
				index++;
			}
			teamAffiliations.add(index, teamAffiliation);
		}
		
		kader.clear();
		for (int i = 0; i < numberOfTeamAffiliations; i++) {
			if (kader.contains(teamAffiliations.get(i).getPlayer()))	continue;
			kader.add(teamAffiliations.get(i).getPlayer());
			numberOfPlayersByPosition[teamAffiliations.get(i).getPosition().getID()]++;
		}
	}
	
	public int getNextFreeSquadNumber() {
		int squadNumber = 1;
		boolean collision;
		
		for (int i = 0; i < 100; i++) {
			collision = false;
			for (int j = 0; j < teamAffiliations.size() && !collision; j++) {
				collision = teamAffiliations.get(j).getSquadNumber() == squadNumber;
			}
			if (!collision)	return squadNumber;
			squadNumber++;
		}
		
		return squadNumber;
	}
	
	public boolean checkForDuplicate(String firstName, String lastName, Datum birthDate) {
		for (Spieler player : kader) {
			if (player.getFirstName().equals(firstName) && player.getLastName().equals(lastName) && player.getBirthDate().equals(birthDate))	return true;
		}
		return false;
	}
	
	public void addAffiliation(TeamAffiliation affiliation) {
		teamAffiliations.add(affiliation);
		numberOfTeamAffiliations++;
		sortAffiliations();
		distinguishNames();
		updateEligiblePlayers(today, true);
		updateIneligiblePlayers(today, true);
	}
	
	public void playerUpdated() {
		sortAffiliations();
		distinguishNames();
	}
	
	public Datum getTodayWithinSeasonBounds() {
		Datum today = new Datum();
		if (today.isBefore(competition.getStartDate()))	return competition.getStartDate();
		if (today.isAfter(competition.getFinalDate()))	return competition.getFinalDate();
		return today;
	}
	
	private void loadKader() {
		if (!proceedWithKaderFileName())	return;
		
		ArrayList<String> playersFromFile = readFile(kaderFileName);
		numberOfTeamAffiliations = 0;
		teamAffiliations.clear();
		
		for (int i = 0; i < playersFromFile.size(); i++) {
			TeamAffiliation teamAffiliation = new TeamAffiliation(this, playersFromFile.get(i));
			teamAffiliation.getPlayer().addTeamAffiliation(teamAffiliation);
			teamAffiliations.add(teamAffiliation);
			numberOfTeamAffiliations++;
		}
		sortAffiliations();
		distinguishNames();
	}
	
	private boolean proceedWithKaderFileName() {
		if (name.contains("Mannschaft "))	return false;
		if (competition == null)			return false;
		if (!competition.teamsHaveKader())	return false;
		if (playsInKORound)					return false;
		determineKaderFileName();
		
		return true;
	}
	
	private void determineKaderFileName() {
		kaderFileName = competition.getWorkspace() + "Kader" + File.separator;
		new File(kaderFileName).mkdirs();
		kaderFileName += nameForFileSearch + ".txt";
	}
	
	public void save() {
		saveKader();
	}
	
	private void saveKader() {
		if (!proceedWithKaderFileName())	return;
		
		ArrayList<String> players = new ArrayList<>();
		for (int i = 0; i < numberOfTeamAffiliations; i++) {
			players.add(teamAffiliations.get(i).toString());
		}
		writeFile(kaderFileName, players);
	}
	
	private void distinguishNames() {
		for (Spieler player : kader) {
			player.resetDistinctName();
		}
		
		boolean ensuredDN = false;
		int distinctionLevel = 0;
		while (!ensuredDN) {
			distinctionLevel++;
			boolean[] doubleNames = new boolean[kader.size()];
			for (int i = 0; i < kader.size(); i++) {
				Spieler player = kader.get(i);
				for (int j = 0; j < kader.size() && !doubleNames[i]; j++) {
					if (i == j)	continue;
					if (player.getPopularOrLastName().equals(kader.get(j).getPopularOrLastName())) {
						doubleNames[i] = true;
						doubleNames[j] = true;
					}
				}
			}
			ensuredDN = true;
			for (int i = 0; i < doubleNames.length; i++) {
				if (doubleNames[i]) {
					kader.get(i).setDistinctionLevel(distinctionLevel);
					ensuredDN = false;
				}
			}
		}
	}
	
	public int getCurrentNumberOf(Position position) {
		return currentNumberOfPlayersByPosition[position.getID()];
	}
	
	public void retrieveMatchPerformances() {
		Iterator<String> iter = matches.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Spiel match = matches.get(key);
			if (match == null)	continue;
			if (!inThePast(match.getKickOffTime()))	continue;
			for (TeamAffiliation affiliation : teamAffiliations) {
				if (!affiliation.getDuration().includes(match.getKickOffTime().getDate()))	continue;
				SpielPerformance matchPerformance = match.getMatchPerformance(affiliation);
				affiliation.getSeasonPerformance().addMatchPerformance(key, matchPerformance);
			}
		}
	}
	
	public int[] getFairplayData() {
		int booked = 0, bookedTwice = 0, redCards = 0;
		
		Iterator<String> iter = matches.keySet().iterator();
		while (iter.hasNext()) {
			Spiel match = matches.get(iter.next());
			if (match != null) {
				ArrayList<Karte> bookings = match.getBookings();
				for (Karte booking : bookings) {
					if (booking.isFirstTeam() == homeaway[match.getMatchday()]) {
						if (booking.isSecondBooking()) {
							booked--;
							bookedTwice++;
						}
						else if (booking.isYellowCard())	booked++;
						else {
							redCards++;
						}
					}
				}
			}
		}
		
		return new int[] {booked, bookedTwice, redCards};
	}
	
	private void setMatchdayOrder() {
		if (matchdayOrder != null)	return;
		
		int[] array = new int[numberOfMatchdays];
		AnstossZeit[] kickOffTimes = new AnstossZeit[numberOfMatchdays];
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			if (matches.containsKey(competition.getKey(i)))	kickOffTimes[i] = matches.get(competition.getKey(i)).getKickOffTime();
		}
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = i + 1; j < numberOfMatchdays; j++) {
				if (kickOffTimes[i] == null || kickOffTimes[j] == null)	array[j]++;
				else if (kickOffTimes[j].isBefore(kickOffTimes[i]))		array[i]++;
				else													array[j]++;
			}
		}
		
		matchdayOrder = new int[numberOfMatchdays];
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdayOrder[array[i]] = i;
		}
	}
	
	public int getFairplayValue(RankingCriterion criterion) {
		int value = 0;
		
		Iterator<String> iter = matches.keySet().iterator();
		while (iter.hasNext()) {
			Spiel match = matches.get(iter.next());
			for (Karte booking : match.getBookings()) {
				if (booking.isFirstTeam() == homeaway[match.getMatchday()]) {
					switch (criterion) {
						case FAIRPLAY_Y1YR3R3:
							if (booking.isSecondBooking())		value -= 2;
							else if (booking.isYellowCard())	value--;
							else								value -= 3;
						case FAIRPLAY_Y1YR3R4:
						default:
							if (booking.isSecondBooking())		value -= 2;
							else if (booking.isYellowCard())	value--;
							else								value -= 4;
					}
				}
			}
		}
		
		return value;
	}
	
	private void setValuesForMatchday(ArrayList<Mannschaft> includedOpponents, int untilMatchday, Tabellenart tableType) {
		if (valuesCorrectAsOfMatchday == untilMatchday && valuesCorrectAsOf == tableType && containsSameElements(valuesCorrectAsOfOpponents, includedOpponents))	return;
		
		numOfWins = numOfDraws = numOfLosses = numOfGoals = numOfCGoals = numOfAwayGoals = 0;
		for (int matchday = 0; matchday <= untilMatchday; matchday++) {
			final int opponent = data[matchday][OPPONENT];
			if (homeaway[matchday] && tableType == Tabellenart.AWAY)	continue;
			if (!homeaway[matchday] && tableType == Tabellenart.HOME)	continue;
			if (!includedOpponents.stream().anyMatch(m -> m.getId() == opponent))	continue;
			if (data[matchday][POINTS] == 3)			numOfWins++;
			else if (data[matchday][POINTS] == 1)	numOfDraws++;
			else if (data[matchday][GOALS] < data[matchday][CGOALS])	numOfLosses++;
			
			numOfGoals += data[matchday][GOALS];
			numOfCGoals += data[matchday][CGOALS];
			if (!homeaway[matchday])	numOfAwayGoals += data[matchday][GOALS];
		}
		
		numOfMatches = numOfWins + numOfDraws + numOfLosses;
		points = 3 * numOfWins + numOfDraws + deductedPoints;
		goalDiff = numOfGoals - numOfCGoals;
		
		valuesCorrectAsOfOpponents = includedOpponents;
		valuesCorrectAsOfMatchday = untilMatchday;
		valuesCorrectAsOf = tableType;
	}
	
	public int get(int index, int firstMatchday, int lastMatchday) {
		return get(index, firstMatchday, lastMatchday, competition.getTeams().size() - 1);
	}
	
	public int get(int index, int firstMatchday, int lastMatchday, int includingRank) {
		ArrayList<Integer> excludedTeams = new ArrayList<>();
		if (competition instanceof Gruppe) {
			Gruppe group = (Gruppe) competition;
			while (includingRank < group.getTeams().size()) {
				includingRank++;
				Mannschaft team = group.getTeamOnPlace(includingRank);
				if (team != null)	excludedTeams.add(team.getId());
			}
		}
		
		if (index == 9 || (index >= 2 && index <= 5)) {
			int nOfW = 0, nOfD = 0, nOfL = 0;
			for (int matchday = firstMatchday; matchday <= lastMatchday; matchday++) {
				if (isElementOf(data[matchday][0], excludedTeams))	continue;
				if (data[matchday][3] == 3)			nOfW++;
				else if (data[matchday][3] == 1)	nOfD++;
				else if (data[matchday][1] < data[matchday][2])	nOfL++;
			}
			
			if (index == 2)	return nOfW + nOfD + nOfL;
			if (index == 3)	return nOfW;
			if (index == 4)	return nOfD;
			if (index == 5)	return nOfL;
			if (index == 9)	return 3 * nOfW + nOfD + deductedPoints;
		}
		if (index >= 6 && index <= 8) {
			int nOfG = 0, nOfCG = 0;
			for (int matchday = firstMatchday; matchday <= lastMatchday; matchday++) {
				if (isElementOf(data[matchday][0], excludedTeams))	continue;
				nOfG += data[matchday][1];
				nOfCG += data[matchday][2];
			}
			
			if (index == 6)	return nOfG;
			if (index == 7)	return nOfCG;
			if (index == 8)	return nOfG - nOfCG;
		}
		
		return -1;
	}
	
	public String getString(int index) {
		if (index == 0)	return "" + (place + 1);
		if (index == 1)	return name;
		if (index == 2)	return "" + numOfMatches;
		if (index == 3)	return "" + numOfWins;
		if (index == 4)	return "" + numOfDraws;
		if (index == 5)	return "" + numOfLosses;
		if (index == 6)	return "" + numOfGoals;
		if (index == 7)	return "" + numOfCGoals;
		if (index == 8)	return "" + goalDiff;
		if (index == 9)	return "" + points;
		return null;
	}

	public int get(ArrayList<Mannschaft> includedOpponents, int index, int untilMatchday, Tabellenart tableType) {
		setValuesForMatchday(includedOpponents, untilMatchday, tableType);
		if (index == 0)	return place;
		if (index == 2)	return numOfMatches;
		if (index == 3)	return numOfWins;
		if (index == 4)	return numOfDraws;
		if (index == 5)	return numOfLosses;
		if (index == 6)	return numOfGoals;
		if (index == 7)	return numOfCGoals;
		if (index == 8)	return goalDiff;
		if (index == 9)	return points;
		
		return -1;
	}
	
	/**
	 * 1: Siegesserie <br/>
	 * 2: Unentschiedenserie <br/>
	 * 3: Niederlagenserie <br/>
	 * 4: Unbesiegt-Serie <br/>
	 * 5: Sieglos-Serie <br/>
	 * 6: Tor-Serie <br/>
	 * 7: Torlos-Serie <br/>
	 * 8: Gegentor-Serie <br/>
	 * 9: Gegentorlos-Serie <br/>
	 * @param index
	 * @return
	 */
	public int getSeries(int index) {
		int currentDuration = 0, longestDuration = 0;
		setMatchdayOrder();
		
		boolean reset;
		for (int i = 0; i < data.length; i++) {
			int matchday = matchdayOrder[i];
			if (data[matchday][0] != 0) {
				if (data[matchday][3] == 0 && data[matchday][2] == 0)	continue;
				reset = false;
				
				switch (index) {
					case 1:		if (data[matchday][3] != 3)	reset = true;	break;
					case 2:		if (data[matchday][3] != 1)	reset = true;	break;
					case 3:		if (data[matchday][3] != 0)	reset = true;	break;
					case 4:		if (data[matchday][3] == 0)	reset = true;	break;
					case 5:		if (data[matchday][3] == 3)	reset = true;	break;
					case 6:		if (data[matchday][1] == 0)	reset = true;	break;
					case 7:		if (data[matchday][1] > 0)	reset = true;	break;
					case 8:		if (data[matchday][2] == 0)	reset = true;	break;
					case 9:		if (data[matchday][2] > 0)	reset = true;	break;
				}
				if (reset) {
					longestDuration = Math.max(longestDuration, currentDuration);
					currentDuration = 0;
				} else {
					currentDuration++;
				}
			}
		}
		
		longestDuration = Math.max(longestDuration, currentDuration);
		
		return longestDuration;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}
	
	public boolean isMatchSet(String key) {
		if (matches.containsKey(key))	return true;
		return false;
	}
	
	public boolean isResultSet(String key) {
		if (results.containsKey(key))	return true;
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
		nameForFileSearch = removeUmlaute(name);
	}

	public String getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(String foundingDate) {
		this.foundingDate = foundingDate;
	}
	
	public boolean isClub() {
		return isClub;
	}

	public int getId() {
		return id;
	}
	
	public int getDeductedPoints() {
		return deductedPoints;
	}
	
	public void setDeductedPoints(int deductedPoints) {
		this.deductedPoints = deductedPoints;
		
		// force update of values
		int untilMatchday = valuesCorrectAsOfMatchday;
		valuesCorrectAsOfMatchday = -1;
		setValuesForMatchday(valuesCorrectAsOfOpponents, untilMatchday, valuesCorrectAsOf);
	}
	
	private void updateEligiblePlayers(Datum date, boolean forceUpdate) {
		if (!forceUpdate && date.equals(lastUpdatedEligibleForDate))	return;
		
		currentNumberOfPlayersByPosition = new int[4];
		eligiblePlayers.clear();
		
		for (TeamAffiliation affiliation : teamAffiliations) {
			if (affiliation.getDuration().includes(date)) {
				eligiblePlayers.add(affiliation);
				currentNumberOfPlayersByPosition[affiliation.getPosition().getID()]++;
				continue;
			}
		}
		
		lastUpdatedEligibleForDate = date;
	}
	
	private void updateIneligiblePlayers(Datum date, boolean forceUpdate) {
		if (!forceUpdate && date.equals(lastUpdatedIneligibleForDate))	return;
		
		ineligiblePlayers.clear();
		
		for (TeamAffiliation affiliation : teamAffiliations) {
			if (!affiliation.getDuration().includes(date)) {
				ineligiblePlayers.add(affiliation);
			}
		}
		
		lastUpdatedIneligibleForDate = date;
	}
	
	public int getCurrentNumberOfPlayers(Datum date, boolean forceUpdate) {
		updateEligiblePlayers(date, forceUpdate);
		return eligiblePlayers.size();
	}
	
	public ArrayList<TeamAffiliation> getEligiblePlayers(Datum date, boolean forceUpdate) {
		updateEligiblePlayers(date, forceUpdate);
		return eligiblePlayers;
	}
	
	public ArrayList<TeamAffiliation> getIneligiblePlayers(Datum date, boolean forceUpdate) {
		updateIneligiblePlayers(date, forceUpdate);
		return ineligiblePlayers;
	}
	
	public TeamAffiliation getAffiliation(int squadNumber, Datum date) {
		for (TeamAffiliation teamAffiliation : teamAffiliations) {
			if (teamAffiliation.getSquadNumber() == squadNumber && teamAffiliation.getDuration().includes(date))	return teamAffiliation;
		}
		
		return null;
	}

	public TeamAffiliation[] getLineup(int[] unordered, Datum date) {
		ArrayList<TeamAffiliation> eligiblePlayers = getEligiblePlayers(date, false);
		
		int counter = 0;
		TeamAffiliation[] ordered = new TeamAffiliation[Aufstellung.numberOfPlayersInLineUp];
		boolean[] sqFound = new boolean[11];
		for (TeamAffiliation affiliation : eligiblePlayers) {
			for (int i = 0; i < unordered.length; i++) {
				if (unordered[i] == affiliation.getSquadNumber()) {
					if (sqFound[i]) {
						Spieler player = affiliation.getPlayer();
						message("Mehrere Spieler mit derselben Rückennummer " + unordered[i] + ", u.a.: " + player.getFullNameShort());
					}
					if (counter == 11)	message("Mehr als 11 Spieler gefunden: (s.o.)");
					ordered[counter++] = affiliation;
					sqFound[i] = true;
				}
			}
		}
		if (counter != 11) {
			String numbers = "", sep = "";
			for (int i = 0; i < sqFound.length; i++) {
				if (!sqFound[i]) {
					numbers += sep + unordered[i];
					sep = ", ";
				}
			}
			message(name + " (" + date.withDividers() + ")" + " - Kein Spieler gefunden für diese Rückennummer(n): " + numbers);
		}
		
		return ordered;
	}
	
	public String[] getResultsAgainst(Mannschaft opponent) {
		int numberOfJointMatchdays = competition.getNumberOfJointMatchdays();
		int matchesVsSameOpponent = competition.getNumberOfMatchesAgainstSameOpponent();
		int matchesVsSameOpponentAfterSplit = competition.getNumberOfMatchesAgainstSameOpponentAfterSplit();
		boolean evenNumber = matchesVsSameOpponent % 2 == 0;
		int halfNumberOfMatchesASO = matchesVsSameOpponent / 2;
		String[] resultsOpponent = new String[matchesVsSameOpponent + matchesVsSameOpponentAfterSplit];
		
		if (opponent == this) {
			for (int i = 0; i < resultsOpponent.length; i++) {
				resultsOpponent[i] = "-3;n/a";
			}
		} else {
			for (int i = 0; i < resultsOpponent.length; i++) {
				resultsOpponent[i] = "-2;--";
			}
			int counterH = 0, counterA = 0;
			for (int i = 0; i < numberOfMatchdays; i++) {
				if (data[i][OPPONENT] == opponent.getId()) {
					String result = data[i][GOALS] + ":" + data[i][CGOALS];
					if (isResultSet(competition.getKey(i))) {
						result = data[i][POINTS] + ";" + result;
					} else	result = "-1;(" + (i + 1) + ")";
					
					if (i >= numberOfJointMatchdays || !evenNumber && (matchesVsSameOpponent * i >= (matchesVsSameOpponent - 1) * numberOfJointMatchdays)) {
						resultsOpponent[counterH + counterA] = result;
						if (homeaway[i])	counterH++;
						else				counterA++;
					}
					else if (homeaway[i])	resultsOpponent[counterH++] = result;
					else					resultsOpponent[halfNumberOfMatchesASO + counterA++] = result;
				}
			}
		}
		
		return resultsOpponent;
	}
	
	public void resetMatch(String key) {
		setMatch(key, null);
		setResult(key, null);
	}

	public void resetOpponent(int matchday) {
		homeaway[matchday] = false;
		data[matchday][OPPONENT] = 0;
	}
	
	public void setMatch(String key, Spiel match) {
		if (match != null) {
			matches.put(key, match);
		} else {
			matches.remove(key);
		}
		
		if (key.contains("RR")) {
			int matchday = Integer.parseInt(key.replace("RR", ""));
			
			if (match == null) {
				resetOpponent(matchday);
			} else if (id == match.home()) {
				setOpponent(matchday, true, match.away());
			} else if (id == match.away()) {
				setOpponent(matchday, false, match.home());
			}
		}
	}
	
	private void setOpponent(int matchday, boolean homeoraway, int opponent) {
		homeaway[matchday] = homeoraway;
		data[matchday][OPPONENT] = opponent;
	}

	public void setResult(String key, Ergebnis result) {
		if (result != null) {
			results.put(key, result);
		} else {
			results.remove(key);
		}
		
		if (key.contains("RR")) {
			int matchday = Integer.parseInt(key.replace("RR", ""));
			
			if (result == null) {
				setResult(matchday, -1, -1);
				return;
			}
			
			if (homeaway[matchday]) {
				setResult(matchday, result.home(), result.away());
			} else {
				setResult(matchday, result.away(), result.home());
			}
		}
	}

	private void setResult(int matchday, int goals, int cGoals) {
		if (0 > matchday || matchday >= data.length) {
			log("Ergebnis konnte nicht gesetzt werden, da matchday die Grenzen verlässt.");
			return;
		}
		
		// "Entfernen" des alten Ergebnisses, wenn als Parameter -1:-1 kommt, wird das Ergebnis zurückgesetzt
		int oldGoals = data[matchday][GOALS];
		int oldCGoals = data[matchday][CGOALS];
		int oldPoints = data[matchday][POINTS];

		if (oldPoints > 0 || oldGoals < oldCGoals) { // ansonsten steht kein Ergebnis drin (0:0 mit 0 Punkten)
			data[matchday][GOALS] = 0;
			data[matchday][CGOALS] = 0;
			data[matchday][POINTS] = 0;
		}
		
		if (goals != -1 && cGoals != -1) {
			// Speichern des neuen Ergebnisses
			data[matchday][GOALS] = goals;
			data[matchday][CGOALS] = cGoals;

			int points;
			if (goals > cGoals) {
				points = 3;
			} else if (goals == cGoals) {
				points = 1;
			} else {
				points = 0;
			}

			data[matchday][POINTS] = points;
		}
		
		valuesCorrectAsOfMatchday = -1;
	}
	
	public int getValueForCriterion(ArrayList<Mannschaft> includedOpponents, int untilMatchday, Tabellenart tableType, RankingCriterion criterion) {
		setValuesForMatchday(includedOpponents, untilMatchday, tableType);
		
		switch (criterion) {
			case ALL_GAMES_MORE_POINTS:
			case DIRECT_COMPARISON_MORE_POINTS:
				return points;
			case ALL_GAMES_BETTER_GOAL_DIFFERENCE:
			case DIRECT_COMPARISON_BETTER_GOAL_DIFFERENCE:
				return goalDiff;
			case ALL_GAMES_MORE_GOALS_SCORED:
			case DIRECT_COMPARISON_MORE_GOALS_SCORED:
				return numOfGoals;
			case ALL_GAMES_MORE_AWAY_GOALS_SCORED:
			case DIRECT_COMPARISON_MORE_AWAY_GOALS_SCORED:
				return numOfAwayGoals;
			case FAIRPLAY_Y1YR3R3:
			case FAIRPLAY_Y1YR3R4:
				return getFairplayValue(criterion);
			case SPLIT_GROUP:
				return lSeason.getSplitGroup(this, untilMatchday);
			default:
				return -1;
		}
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Mannschaft))	return false;
		Mannschaft team = (Mannschaft) object;
		if (!name.equals(team.name))	return false;
		return true;
	}

	private void parseString(String data) {
		String[] split = data.split(trennZeichen);
		int index = 0;
		
		setName(split[index++]);
		if (split.length > 1) {
			foundingDate = !split[index++].equals("null") ? split[index - 1] : null;
			if (split.length > 2) {
				deductedPoints = Integer.parseInt(split[index++]);
				log(name + ": This team has been deducted " + deductedPoints + " points.");
			}
		}
	}

	public String toString() {
		String toString = name;
		if (playsInLeague) {
			toString += trennZeichen + foundingDate;
			if (deductedPoints != 0) {
				toString += trennZeichen + deductedPoints;
			}
		} else if (playsInGroup) {
			if (deductedPoints != 0) {
				toString += trennZeichen + "null" + trennZeichen + deductedPoints;
			}
		} else if (foundingDate != null) {
			toString += trennZeichen + foundingDate;
		}

		return toString;
	}
}
