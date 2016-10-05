package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Mannschaft {
	private int id;
	private String name;
	private String nameForFileSearch;
	private String foundingDate;
	
	private int place = -1;
	private int numOfMatches;
	private int numOfWins;
	private int numOfDraws;
	private int numOfLosses;
	private int numOfGoals;
	private int numOfCGoals;
	private int goalDiff;
	private int points;
	private int valuesCorrectAsOfMatchday = -1;
	private Tabellenart valuesCorrectAsOf;
	private int deductedPoints = 0;
	
	private int numberOfMatchdays;
	private int[][] data;
	private HashMap<String, Spiel> matches;
	private Ergebnis[] results;
	private int[][] performanceData;
	
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
	private int numberOfPlayers;
	private ArrayList<Spieler> kader = new ArrayList<>();
	private int[] numberOfPlayersByPosition;
	private ArrayList<Spieler> eligiblePlayers = new ArrayList<Spieler>();
	private ArrayList<Spieler> ineligiblePlayers = new ArrayList<Spieler>();
	private int lastUpdatedEligibleForDate = -1;
	private int lastUpdatedIneligibleForDate = -1;
	private int[] currentNumberOfPlayersByPosition;

	private boolean playsInLeague = false;
	private boolean playsInGroup = false;

	public Mannschaft(int id, Wettbewerb competition, String mannschaftsDaten) {
		this.id = id;
		if (competition instanceof LigaSaison)		lSeason = (LigaSaison) competition;
		else if (competition instanceof Gruppe)		group = (Gruppe) competition;
		this.competition = competition;
		playsInLeague = lSeason != null;
		playsInGroup = group != null;
		
		parseString(mannschaftsDaten);
		if (competition != null) {
			initializeArrays();
			loadKader();
		}
	}
	
	private void initializeArrays() {
		numberOfMatchdays = 0;
		if (playsInLeague)		numberOfMatchdays = lSeason.getNumberOfMatchdays();
		else if (playsInGroup)	numberOfMatchdays = group.getNumberOfMatchdays();
		data = new int[numberOfMatchdays][4];
		homeaway = new boolean[numberOfMatchdays];
		matches = new HashMap<>();
		results = new Ergebnis[numberOfMatchdays];
	}
	
	public Wettbewerb getCompetition() {
		return competition;
	}
	
	public String getPhotoDirectory() {
		return competition.getWorkspace() + "Bilder" + File.separator + nameForFileSearch + File.separator;
	}
	
	public int getNumberOfPlayers(boolean onlyEligible, boolean forceUpdate) {
		if (onlyEligible) {
			updateEligiblePlayers(Start.today(), forceUpdate);
			return eligiblePlayers.size();
		}
		return kader.size();
	}
	
	public int getNumberOfUsedPlayers() {
		int numberOfUsedPlayers = 0;
		
		for (int i = 0; i < performanceData.length; i++) {
			if (performanceData[i][0] != 0)	numberOfUsedPlayers++;
		}
		
		return numberOfUsedPlayers;
	}
	
	public ArrayList<Spieler> getPlayers() {
		return kader;
	}
	
	private void sortPlayers() {
		ArrayList<Spieler> unsorted = new ArrayList<>();
		for (Spieler player : kader) {
			unsorted.add(player);
		}
		kader.clear();
		int index;
		for (int i = 0; i < unsorted.size(); i++) {
			Spieler player = unsorted.get(i);
			index = 0;
			for (int j = 0; j < kader.size(); j++) {
				if (player.inOrderBefore(kader.get(j)))	break;
				index++;
			}
			kader.add(index, player);
		}
	}
	
	public int getNextFreeSquadNumber() {
		int squadNumber = 1;
		boolean collision;
		
		for (int i = 0; i < 100; i++) {
			collision = false;
			for (int j = 0; j < kader.size() && !collision; j++) {
				collision = kader.get(j).getSquadNumber() == squadNumber;
			}
			if (!collision)	return squadNumber;
			squadNumber++;
		}
		
		return squadNumber;
	}
	
	public void addPlayer(Spieler player) {
		kader.add(player);
		numberOfPlayersByPosition[player.getPosition().getID()]++;
		numberOfPlayers++;
		sortPlayers();
		distinguishNames();
		updateEligiblePlayers(Start.today(), true);
		updateIneligiblePlayers(Start.today(), true);
	}
	
	public void playerUpdated() {
		sortPlayers();
		distinguishNames();
	}
	
	public void changeSquadNumber(Spieler player, int newSquadNumber) {
		int oldSquadNumber = player.getSquadNumber();
		Iterator<String> iter = matches.keySet().iterator();
		while (iter.hasNext()) {
			Spiel match = matches.get(iter.next());
			if (match == null || !player.isEligible(match.getDate())) continue;
			match.changeSquadNumberInLineup(match.getHomeTeam() == this, oldSquadNumber, newSquadNumber);
		}
	}
	
	private void loadKader() {
		if (!competition.teamsHaveKader())	return;
		if (playsInLeague)		kaderFileName = lSeason.getWorkspace() + "Kader" + File.separator;
		else if (playsInGroup)	kaderFileName = group.getWorkspace() + "Kader" + File.separator;
		(new File(kaderFileName)).mkdirs(); // if directory does not exist, creates directory
		kaderFileName += nameForFileSearch + ".txt";
		
		ArrayList<String> playersFromFile = ausDatei(kaderFileName);
		numberOfPlayers = playersFromFile.size();
		numberOfPlayersByPosition = new int[4];
		kader.clear();
		for (int i = 0; i < numberOfPlayers; i++) {
			kader.add(new Spieler(playersFromFile.get(i), this));
			numberOfPlayersByPosition[kader.get(i).getPosition().getID()]++;
		}
		sortPlayers();
		distinguishNames();
	}
	
	public void save() {
		saveKader();
	}
	
	private void saveKader() {
		if (!competition.teamsHaveKader())	return;
		ArrayList<String> players = new ArrayList<>();
		for (int i = 0; i < numberOfPlayers; i++) {
			players.add(kader.get(i).toString());
		}
		inDatei(kaderFileName, players);
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
					if (player.getPseudonymOrLN().equals(kader.get(j).getPseudonymOrLN())) {
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
	
	public String getResultOfMatch(int matchday) {
		if (matchday < 0 || matchday > numberOfMatchdays || data[matchday][OPPONENT] == 0)	return null;
		return data[matchday][GOALS] + ":" + data[matchday][CGOALS];
	}
	
	public ArrayList<int[]> getPerformanceDataMatchByMatch(Spieler player) {
		int squadNumber = player.getSquadNumber();
		ArrayList<int[]> performanceData = new ArrayList<>();
		
		Iterator<String> iter = matches.keySet().iterator();
		while (iter.hasNext()) {
			Spiel match = matches.get(iter.next());
			if (match != null) {
				if (!inThePast(match.getDate(), match.getTime()))	continue;
				if (!player.isEligible(match.getDate()))	continue;
				boolean played = false, home = homeaway[match.getMatchday()];
				int[] lineup = home ? match.getLineupHome() : match.getLineupAway();
				if (lineup == null)	continue;
				int otherTeam = home ? match.away() : match.home();
				int firstMinute = 91, lastMinute = 91, goalsScored = 0, assists = 0, booked = 0, bookedTwice = 0, redCard = 0;
				ArrayList<Wechsel> substitutions = match.getSubstitutions(home);
				ArrayList<Tor> goals = match.getGoals();
				ArrayList<Karte> bookings = match.getBookings();
				
				for (int j = 0; j < lineup.length; j++) {
					if (lineup[j] == squadNumber) {
						played = true;
						firstMinute = 1;
					}
				}
				for (Wechsel substitution : substitutions) {
					if (substitution.getPlayerOn() == player) {
						played = true;
						firstMinute = substitution.getMinute().getRegularTime();
					} else if (substitution.getPlayerOff() == player) {
						lastMinute = substitution.getMinute().getRegularTime();
					}
				}
				for (Tor goal : goals) {
					if (!goal.isOwnGoal() && home == goal.isFirstTeam() && goal.getScorer() == player) {
						goalsScored++;
					} else if (home == goal.isFirstTeam() && goal.getAssister() != null && goal.getAssister() == player) {
						assists++;
					}
				}
				for (Karte booking : bookings) {
					if (booking.isFirstTeam() == home && booking.getBookedPlayer() == player) {
						if (booking.isSecondBooking())		bookedTwice = booking.getMinute().getRegularTime();
						else if (booking.isYellowCard())	booked = booking.getMinute().getRegularTime();
						else								redCard = booking.getMinute().getRegularTime();
					}
				}
				
				int index = 0;
				for (int i = 0; i < performanceData.size(); i++) {
					if (performanceData.get(i)[0] < match.getMatchday())	index++;
				}
				
				if (played || booked + bookedTwice + redCard != 0)	performanceData.add(index, new int[] {match.getMatchday(), otherTeam, firstMinute, lastMinute, goalsScored, assists, booked, bookedTwice, redCard});
			}
		}
		
		return performanceData;
	}
	
	public int[] getPerformanceData(Spieler player) {
		for (int i = 0; i < kader.size(); i++) {
			if (kader.get(i) == player)	return performanceData[i];
		}
		return null;
	}
	
	public void retrievePerformanceData() {
		performanceData = new int[numberOfPlayers][];
		for (int i = 0; i < performanceData.length; i++) {
			Spieler player = kader.get(i);
			
			int matchesPlayed = 0, matchesStarted = 0, subOn = 0, subOff = 0, minutesPlayed = 0, goalsScored = 0, assists = 0, booked = 0, bookedTwice = 0, redCards = 0;
			int squadNumber = player.getSquadNumber();
			
			Iterator<String> iter = matches.keySet().iterator();
			while (iter.hasNext()) {
				Spiel match = matches.get(iter.next());
				if (match != null) {
					if (!inThePast(match.getDate(), match.getTime()))	continue;
					if (!player.isEligible(match.getDate()))	continue;
					boolean home = homeaway[match.getMatchday()];
					int[] lineup = home ? match.getLineupHome() : match.getLineupAway();
					if (lineup == null)	continue;
					int firstMinute = 90, lastMinute = 90;
					ArrayList<Wechsel> substitutions = match.getSubstitutions(home);
					ArrayList<Tor> goals = match.getGoals();
					ArrayList<Karte> bookings = match.getBookings();
					
					for (int j = 0; j < lineup.length; j++) {
						if (lineup[j] == squadNumber) {
							matchesPlayed++;
							matchesStarted++;
							firstMinute = 0;
						}
					}
					for (Wechsel substitution : substitutions) {
						if (substitution.getPlayerOn() == player) {
							matchesPlayed++;
							subOn++;
							firstMinute = substitution.getMinute().getRegularTime();
							if (firstMinute == 45 || firstMinute == 46 || firstMinute == 90)	firstMinute--;
						} else if (substitution.getPlayerOff() == player) {
							subOff++;
							lastMinute = substitution.getMinute().getRegularTime();
							if (lastMinute == 46)	lastMinute--;
						}
					}
					for (Tor goal : goals) {
						if (!goal.isOwnGoal() && home == goal.isFirstTeam() && goal.getScorer() == player) {
							goalsScored++;
						} else if (home == goal.isFirstTeam() && goal.getAssister() != null && goal.getAssister() == player) {
							assists++;
						}
					}
					for (Karte booking : bookings) {
						if (booking.isFirstTeam() == home && booking.getBookedPlayer() == player) {
							if (booking.isSecondBooking()) {
								booked--;
								bookedTwice++;
								lastMinute = booking.getMinute().getRegularTime();
							}
							else if (booking.isYellowCard())	booked++;
							else {
								redCards++;
								lastMinute = booking.getMinute().getRegularTime();
							}
						}
					}
					
					minutesPlayed += lastMinute - firstMinute;
				}
			}
			
			performanceData[i] = new int[] {matchesPlayed, matchesStarted, subOn, subOff, minutesPlayed, goalsScored, assists, booked, bookedTwice, redCards};
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
	
	private void setValuesForMatchday(int untilMatchday, Tabellenart tableType) {
		if (valuesCorrectAsOfMatchday == untilMatchday && valuesCorrectAsOf == tableType)	return;
		
		numOfWins = numOfDraws = numOfLosses = numOfGoals = numOfCGoals = 0;
		for (int matchday = 0; matchday <= untilMatchday; matchday++) {
			if (homeaway[matchday] && tableType == Tabellenart.AWAY)	continue;
			if (!homeaway[matchday] && tableType == Tabellenart.HOME)	continue;
			if (data[matchday][3] == 3)			numOfWins++;
			else if (data[matchday][3] == 1)	numOfDraws++;
			else if (data[matchday][1] < data[matchday][2])	numOfLosses++;
			
			numOfGoals += data[matchday][1];
			numOfCGoals += data[matchday][2];
		}
		
		numOfMatches = numOfWins + numOfDraws + numOfLosses;
		points = 3 * numOfWins + numOfDraws + deductedPoints;
		goalDiff = numOfGoals - numOfCGoals;
		
		valuesCorrectAsOfMatchday = untilMatchday;
		valuesCorrectAsOf = tableType;
	}
	
	public int get(int index, int firstMatchday, int lastMatchday) {
		return get(index, firstMatchday, lastMatchday, competition.getTeams().length - 1);
	}
	
	public int get(int index, int firstMatchday, int lastMatchday, int includingRank) {
		ArrayList<Integer> excludedTeams = new ArrayList<>();
		if (competition instanceof Gruppe) {
			Gruppe group = (Gruppe) competition;
			while (includingRank < group.getTeams().length) {
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

	public int get(int index, int untilMatchday, Tabellenart tableType) {
		setValuesForMatchday(untilMatchday, tableType);
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
		
		boolean reset;
		for (int matchday = 0; matchday < data.length; matchday++) {
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
	
	public boolean isMatchSet(int matchday) {
		if (matches.containsKey(getKey(matchday)))	return true;
		return false;
	}
	
	public boolean isResultSet(int matchday) {
		if (results[matchday] != null)	return true;
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
		setValuesForMatchday(untilMatchday, valuesCorrectAsOf);
	}
	
	private void updateEligiblePlayers(int date, boolean forceUpdate) {
		if (!forceUpdate && lastUpdatedEligibleForDate == date)	return;
		
		currentNumberOfPlayersByPosition = new int[4];
		eligiblePlayers.clear();
		
		for (Spieler player : kader) {
			if (player.isEligible(date)) {
				eligiblePlayers.add(player);
				currentNumberOfPlayersByPosition[player.getPosition().getID()]++;
			}
		}
		
		lastUpdatedEligibleForDate = date;
	}
	
	private void updateIneligiblePlayers(int date, boolean forceUpdate) {
		if (!forceUpdate && lastUpdatedIneligibleForDate == date)	return;
		
		ineligiblePlayers.clear();
		
		for (Spieler player : kader) {
			if (!player.isEligible(date)) {
				ineligiblePlayers.add(player);
			}
		}
		
		lastUpdatedIneligibleForDate = date;
	}
	
	public int getCurrentNumberOfPlayers(int date, boolean forceUpdate) {
		updateEligiblePlayers(date, forceUpdate);
		return eligiblePlayers.size();
	}
	
	public ArrayList<Spieler> getEligiblePlayers(int date, boolean forceUpdate) {
		updateEligiblePlayers(date, forceUpdate);
		return eligiblePlayers;
	}
	
	public ArrayList<Spieler> getIneligiblePlayers(int date, boolean forceUpdate) {
		updateIneligiblePlayers(date, forceUpdate);
		return ineligiblePlayers;
	}
	
	public Spieler getPlayer(int squadNumber, int date) {
		for (Spieler player : kader) {
			if (player.getSquadNumber() == squadNumber && player.isEligible(date))	return player;
		}
		
		return null;
	}
	
	public String[] getResultsAgainst(Mannschaft opponent) {
		int halfNumberOfMatchesASO = competition.getNumberOfMatchesAgainstSameOpponent() / 2;
		String[] resultsOpponent = new String[competition.getNumberOfMatchesAgainstSameOpponent()];
		for (int i = 0; i < resultsOpponent.length; i++) {
			resultsOpponent[i] = "2;-:-";
		}
		if (opponent == this) {
			for (int i = 0; i < resultsOpponent.length; i++) {
				resultsOpponent[i] = "2;n/a";
			}
		} else {
			int counterH = 0, counterA = 0;
			for (int i = 0; i < numberOfMatchdays; i++) {
				if (data[i][OPPONENT] == opponent.getId()) {
					String result = data[i][GOALS] + ":" + data[i][CGOALS];
					if (results[i] != null) {
						result = data[i][POINTS] + ";" + result;
					} else	result = "2;-:-";
					if (homeaway[i])	resultsOpponent[counterH++] = result;
					else				resultsOpponent[halfNumberOfMatchesASO + counterA++] = result;
				}
			}
		}
		
		return resultsOpponent;
	}
	
	public String getDateAndTime(int matchday) {
		if (playsInLeague)		return lSeason.getDateOfTeam(matchday, id);
		else if (playsInGroup)	return group.getDateOfTeam(matchday, id);
		else					return "01.01.1970 00:00";
	}
	
	public void resetMatch(int matchday) {
		setResult(matchday, null);
		resetOpponent(matchday);
	}

	public void resetOpponent(int matchday) {
		homeaway[matchday] = false;
		data[matchday][OPPONENT] = 0;
	}
	
	public String getKey(int matchday) {
		return "SP" + matchday;
	}
	
	public void setMatch(int matchday, Spiel match) {
		String key = getKey(matchday);
		if (match != null) {
			matches.put(key, match);
			if (id == match.home()) {
				setOpponent(matchday, true, match.away());
			} else if (id == match.away()) {
				setOpponent(matchday, false, match.home());
			} else {
				log("This match came to the wrong team.");
				matches.remove(key);
			}
		} else {
			matches.remove(key);
		}
	}
	
	public void setMatch(String key, Spiel match) {
		if (match != null) {
			matches.put(key, match);
		} else {
			matches.remove(key);
		}
	}
	
	private void setOpponent(int matchday, boolean homeoraway, int opponent) {
		homeaway[matchday] = homeoraway;
		data[matchday][OPPONENT] = opponent;
	}

	public void setResult(int matchday, Ergebnis result) {
		results[matchday] = result;
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
			numOfGoals -= oldGoals;
			numOfCGoals -= oldCGoals;

			if (oldPoints == 3)			numOfWins--;
			else if (oldPoints == 1)	numOfDraws--;
			else if (oldPoints == 0)	numOfLosses--;
			
			data[matchday][GOALS] = 0;
			data[matchday][CGOALS] = 0;
			data[matchday][POINTS] = 0;
		}
		
		if (goals != -1 && cGoals != -1) {
			// Speichern des neuen Ergebnisses
			data[matchday][GOALS] = goals;
			data[matchday][CGOALS] = cGoals;

			numOfGoals += goals;
			numOfCGoals += cGoals;

			int points;
			if (goals > cGoals) {
				points = 3;
				numOfWins++;
			} else if (goals == cGoals) {
				points = 1;
				numOfDraws++;
			} else {
				points = 0;
				numOfLosses++;
			}

			data[matchday][POINTS] = points;
		}
		
		numOfMatches = numOfWins + numOfDraws + numOfLosses;
		points = 3 * numOfWins + numOfDraws + deductedPoints;
		goalDiff = numOfGoals - numOfCGoals;
	}
	
	/**
	 * Returns an array containing: <br>
	 * <ol>
	 * 	<li> an integer representing home or away
	 * 	<li> an integer representing the opponent
	 * 	<li> the number of goals scored
	 * 	<li> the number of goals conceded
	 * </ol>
	 * @param matchday The matchday of which the match information is required.
	 * @return an array with the mentioned content
	 */
	public int[] getMatch(int matchday) {
		int[] match = new int[4];

		if (homeaway[matchday])	match[0] = HOME;
		else					match[0] = AWAY;
		match[1] = data[matchday][OPPONENT];
		match[2] = data[matchday][GOALS];
		match[3] = data[matchday][CGOALS];

		return match;
	}

	public void compareWithOtherTeams(Mannschaft[] otherTeams, int untilMatchday, Tabellenart tableType) {
		setValuesForMatchday(untilMatchday, tableType);
		ArrayList<Integer> teamsSamePoints = new ArrayList<>();
		this.place = 0;
		
		for (Mannschaft other : otherTeams) {
			if (this.id == other.id)	continue;
			other.setValuesForMatchday(untilMatchday, tableType);
			
			if (this.points == other.points)		teamsSamePoints.add(other.id);
			else if (this.points < other.points)	this.place++;
		}
		
		if (untilMatchday + 1 != numberOfMatchdays || competition.useGoalDifference()) {
			for (Integer id : teamsSamePoints) {
				if (this.goalDiff == otherTeams[id - 1].goalDiff) {
					if (this.numOfGoals < otherTeams[id - 1].numOfGoals)	this.place++;
				}
				else if (this.goalDiff < otherTeams[id - 1].goalDiff)	this.place++;
			}
		} else {
			teamsSamePoints.add(0, this.id);
			int[] points = new int[teamsSamePoints.size() + 1];
			int[] pointsOpp = new int[teamsSamePoints.size() + 1];
			int[] goals = new int[teamsSamePoints.size() + 1];
			int[] goalsOpp = new int[teamsSamePoints.size() + 1];
			
			for (int i = 0; i < teamsSamePoints.size(); i++) {
				Mannschaft team1 = otherTeams[teamsSamePoints.get(i) - 1];
				for (int j = 0; j < teamsSamePoints.size(); j++) {
					if (i == j)	continue;
					Mannschaft team2 = otherTeams[teamsSamePoints.get(j) - 1];
					for (int k = 0; k <= untilMatchday; k++) {
						if (team1.data[k][OPPONENT] == team2.id) {
							goals[i] += team1.data[k][GOALS];
							goalsOpp[i] += team1.data[k][CGOALS];
							points[i] += team1.data[k][POINTS];
							pointsOpp[i] += (team1.data[k][POINTS] == 1 ? 1 : 3 - team1.data[k][POINTS]);
						}
					}
				}
			}
			for (int i = 1; i < teamsSamePoints.size(); i++) {
				if (points[0] == points[i]) {
					if (goals[0] - goalsOpp[0] == goals[i] - goalsOpp[i]) {
						if (goals[0] < goals[i])	this.place++;
						else if (goals[0] == goals[i]) {
							// use goal difference anyway
							int otherID = teamsSamePoints.get(i);
							if (this.goalDiff == otherTeams[otherID - 1].goalDiff) {
								if (this.numOfGoals < otherTeams[otherID - 1].numOfGoals)	this.place++;
							}
							else if (this.goalDiff < otherTeams[otherID - 1].goalDiff)	this.place++;
						}
					} else if (goals[0] - goalsOpp[0] < goals[i] - goalsOpp[i])	this.place++;
				}
				else if (points[0] < points[i])	this.place++;
			}
		}
	}

	private void parseString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		name = split[index++];
		nameForFileSearch = removeUmlaute(name);
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
			toString += ";" + foundingDate;
			if (deductedPoints != 0) {
				toString += ";" + deductedPoints;
			}
		} else if (playsInGroup) {
			if (deductedPoints != 0) {
				toString += ";null;" + deductedPoints;
			}
		} else if (foundingDate != null) {
			toString += ";" + foundingDate;
		}

		return toString;
	}
}
