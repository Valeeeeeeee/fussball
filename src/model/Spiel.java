package model;

import static util.Utilities.*;

import java.util.ArrayList;

import analyse.SpielPerformance;

public class Spiel {
	
	private int matchday;
	private int date;
	private int time;
	private int homeTeamIndex;
	private int awayTeamIndex;
	
	private Wettbewerb competition;
	private Mannschaft homeTeam;
	private Mannschaft awayTeam;
	private int[] lineupHome;
	private int[] lineupAway;
	
	private Schiedsrichter referee;
	private Ergebnis result;
	private ArrayList<Tor> goals = new ArrayList<>();
	private ArrayList<Wechsel> substitutionsHome = new ArrayList<>();
	private ArrayList<Wechsel> substitutionsAway = new ArrayList<>();
	private ArrayList<Karte> bookings = new ArrayList<>();
	
	public Spiel(Wettbewerb competition, int matchday, int date, int time, int homeTeamIndex, int awayTeamIndex) {
		this.competition = competition;
		this.matchday = matchday;
		this.date = date;
		this.time = time;
		
		this.homeTeamIndex = homeTeamIndex;
		homeTeam = competition.getTeams()[homeTeamIndex - 1];
		this.awayTeamIndex = awayTeamIndex;
		awayTeam = competition.getTeams()[awayTeamIndex - 1];
	}
	
	public Spiel(Wettbewerb competition, int matchday, int date, int time, String data) {
		this.competition = competition;
		this.matchday = matchday;
		this.date = date;
		this.time = time;
		parseString(data);
	}
	
	public Wettbewerb getCompetition() {
		return competition;
	}
	
	public String getDescription() {
		return competition.getMatchdayDescription(matchday);
	}
	
	public String getDateAndTime() {
		return MyDate.datum(date) + " " + MyDate.uhrzeit(time);
	}
	
	public int getMatchday() {
		return matchday;
	}
	
	public int getDate() {
		return date;
	}
	
	public int getTime() {
		return time;
	}
	
	public int home() {
		return homeTeamIndex;
	}
	
	public Mannschaft getHomeTeam() {
		return homeTeam;
	}
	
	public int away() {
		return awayTeamIndex;
	}
	
	public Mannschaft getAwayTeam() {
		return awayTeam;
	}
	
	public Ergebnis getResult() {
		return result;
	}
	
	public Mannschaft getTeam(boolean firstTeam) {
		return firstTeam ? homeTeam : awayTeam;
	}
	
	public void setResult(Ergebnis result) {
		this.result = result;
	}
	
	public int[] getLineupHome() {
		return lineupHome;
	}
	
	public void setLineupHome(int[] lineupHome) {
		this.lineupHome = lineupHome;
	}
	
	public int[] getLineupAway() {
		return lineupAway;
	}
	
	public void setLineupAway(int[] lineupAway) {
		this.lineupAway = lineupAway;
	}
	
	public ArrayList<Tor> getGoals() {
		return goals;
	}
	
	public ArrayList<Wechsel> getSubstitutions(boolean firstTeam) {
		return firstTeam ? substitutionsHome : substitutionsAway;
	}
	
	public ArrayList<Karte> getBookings() {
		return bookings;
	}
	
	public void addGoal(Tor goal) {
		if (goal != null) {
			int index = 0;
			for (int i = 0; i < goals.size(); i++) {
				if (!goals.get(i).getMinute().isAfter(goal.getMinute()))	index++;
			}
			goals.add(index, goal);
			result = new Ergebnis(goals);
		}
	}
	
	public void removeGoal(Tor goal) {
		if (goal != null) {
			goals.remove(goal);
			result = new Ergebnis(goals);
		}
	}
	
	public int addSubstitution(Wechsel substitution) {
		int index = 0;
		if (substitution != null) {
			if (substitution.isFirstTeam()) {
				for (int i = 0; i < substitutionsHome.size(); i++) {
					if (!substitutionsHome.get(i).getMinute().isAfter(substitution.getMinute()))	index++;
				}
				substitutionsHome.add(index, substitution);
			} else {
				for (int i = 0; i < substitutionsAway.size(); i++) {
					if (!substitutionsAway.get(i).getMinute().isAfter(substitution.getMinute()))	index++;
				}
				substitutionsAway.add(index, substitution);
			}
		}
		return index;
	}
	
	public void removeSubstitution(Wechsel substitution) {
		(substitution.isFirstTeam() ? substitutionsHome : substitutionsAway).remove(substitution);
	}
	
	public void addBooking(Karte booking) {
		if (booking != null) {
			int index = 0;
			for (int i = 0; i < bookings.size(); i++) {
				if (!bookings.get(i).getMinute().isAfter(booking.getMinute()))	index++;
			}
			bookings.add(index, booking);
		}
	}
	
	public void removeBooking(Karte booking) {
		if (booking != null)	bookings.remove(booking);
	}
	
	public Schiedsrichter getReferee() {
		return referee;
	}
	
	public void changeSquadNumberInLineup(boolean firstTeam, int oldSquadNumber, int newSquadNumber) {
		int[] lineup = firstTeam ? lineupHome : lineupAway;
		if (lineup == null)	return;
		for (int i = 0; i < lineup.length; i++) {
			if (lineup[i] == oldSquadNumber)	lineup[i] = newSquadNumber;
		}
	}
	
	public void setReferee(int refereeID) {
		setReferee(refereeID == 0 ? null : competition.getReferees().get(refereeID - 1));
	}
	
	public void setReferee(Schiedsrichter referee) {
		this.referee = referee;
		referee.addMatch(this);
	}
	
	public SpielPerformance getMatchPerformance(boolean firstTeam, int squadNumber) {
		if (result == null)	return null;
		SpielPerformance matchPerformance = new SpielPerformance(this, getTeam(!firstTeam).getName(), result.fromPerspective(firstTeam));
		int[] lineup = firstTeam ? lineupHome : lineupAway;
		ArrayList<Wechsel> substitutions = firstTeam ? substitutionsHome : substitutionsAway;
		if (lineup == null)	return null;
		
		for (int sqNumber : lineup) {
			if (sqNumber == squadNumber)	matchPerformance.started();
		}
		for (Wechsel sub : substitutions) {
			if (sub.isPlayerOff(squadNumber))		matchPerformance.subbedOff(sub.getMinute());
			else if (sub.isPlayerOn(squadNumber))	matchPerformance.subbedOn(sub.getMinute());
		}
		for (Tor goal : goals) {
			if (goal.isFirstTeam() != firstTeam)	continue;
			if (goal.isScorer(squadNumber))			matchPerformance.goalScored();
			else if (goal.isAssister(squadNumber))	matchPerformance.goalAssisted();
		}
		for (Karte booking : bookings) {
			if (booking.isFirstTeam() != firstTeam)	continue;
			if (booking.isBookedPlayer(squadNumber))	matchPerformance.booked(booking);
		}
		
		return matchPerformance;
	}
	
	public void setMatchData(String matchData) {
		String[] matchDataSplit = matchData.split("=");
		if (!matchDataSplit[0].equals(homeTeamIndex + ":" + awayTeamIndex))	return;
		if (matchDataSplit.length > 1) {
			parseMatchData(matchDataSplit[1]);
			if (matchDataSplit.length == 4) {
				lineupHome = parseLineup(matchDataSplit[2], true);
				lineupAway = parseLineup(matchDataSplit[3], false);
			}
		}
	}
	
	private String getMatchData() {
		String matchData = "";
		
		if (lineupHome != null || lineupAway != null) {
			matchData += "={" + matchDataToString() + "}";
			matchData += "={" + lineupToString(lineupHome, substitutionsHome) + "}={" + lineupToString(lineupAway, substitutionsAway) + "}";
		} else if (result != null) {
			matchData += "={" + matchDataToString() + "}";
		} else if (referee != null) {
			matchData += "={" + referee.getID() + "}";
		}
		
		return matchData;
	}
	
	private String matchDataToString() {
		String matchData = "";
		
		if (referee != null)					matchData += referee.getID();
		if (referee != null && result != null)	matchData += "_";
		if (result != null)						matchData += result;
		if (!competition.teamsHaveKader())	return matchData;
		
		for (Tor goal : goals) {
			matchData += "#" + goal;
		}
		
		if (bookings.size() > 0)	matchData += "^";
		
		for (Karte booking : bookings) {
			matchData += "#" + booking;
		}
		
		return matchData;
	}
	
	private String lineupToString(int[] lineup, ArrayList<Wechsel> substitutions) {
		String lineupString = "";
		
		if (lineup != null) {
			for (int i = 0; i < lineup.length; i++) {
				lineupString += lineup[i];
				if (i < lineup.length - 1)	lineupString += ",";
			}
			for (Wechsel substitution : substitutions) {
				lineupString += "#" + substitution.toString();
			}
		} else {
			lineupString = "null";
		}
		
		return lineupString;
	}
	
	private void parseMatchData(String matchData) {
		matchData = matchData.replace("{", "").replace("}", "");
		if (matchData.indexOf("_") != -1) {
			setReferee(competition.getReferees().get(Integer.parseInt(matchData.substring(0, matchData.indexOf("_"))) - 1));
		}
		matchData = matchData.substring(matchData.indexOf("_") + 1);
		if (matchData.indexOf(":") == -1) {
			setReferee(competition.getReferees().get(Integer.parseInt(matchData) - 1));
			return;
		}
		String[] matchDataSplit = matchData.split("\\^");
		String allGoals = matchDataSplit[0];
		String[] goalsSplit = allGoals.split("#");
		if(!goalsSplit[0].equals("null"))	result = new Ergebnis(goalsSplit[0]);
		if (goalsSplit.length > 1) {
			for (int i = 1; i < goalsSplit.length; i++) {
				goals.add(new Tor(this, goalsSplit[i]));
			}
		}
		if (matchDataSplit.length > 1) {
			String allBookings = matchDataSplit[1];
			String[] bookingsSplit = allBookings.split("#");
			for (int i = 0; i < bookingsSplit.length; i++) {
				if (bookingsSplit[i].length() > 0)	bookings.add(new Karte(this, bookingsSplit[i]));
			}
		}
	}
	
	private int[] parseLineup(String lineupString, boolean firstTeam) {
		int[] lineup = null;
		
		if (!lineupString.equals("{null}")) {
			String[] hashSplit = lineupString.replace("{", "").replace("}", "").split("#");
			String[] lineupSplit = hashSplit[0].split(",");
			lineup = new int[lineupSplit.length];
			for (int i = 0; i < lineupSplit.length; i++) {
				lineup[i] = Integer.parseInt(lineupSplit[i]);
			}
			for (int i = 1; i < hashSplit.length; i++) {
				Wechsel substitution = new Wechsel(this, firstTeam, hashSplit[i]);
				addSubstitution(substitution);
			}
		}
		
		return checkLineup(lineup);
	}
	
	private int[] checkLineup(int[] lineup) {
		boolean isValid = true;
		// TODO check validity
		return isValid ? lineup : null;
	}
	
	private void parseString(String data) {
		try {
			homeTeamIndex = Integer.parseInt(data.split(":")[0]);
			awayTeamIndex = Integer.parseInt(data.split(":")[1]);
			
			if (homeTeamIndex == awayTeamIndex || homeTeamIndex == -1 || awayTeamIndex == -1) {
				throw new IllegalArgumentException();
			}
			
			homeTeam = competition.getTeams()[homeTeamIndex - 1];
			awayTeam = competition.getTeams()[awayTeamIndex - 1];
		} catch (IllegalArgumentException iae) {
			log("The given match was formally correct, but impossible.");
			log(iae.getMessage());
		} catch (Exception e) {
			log("The given match was formally incorrect.");
			e.printStackTrace();
		}
	}
	
	public String fullString() {
		String fullString = homeTeamIndex + ":" + awayTeamIndex + getMatchData();
		
		return fullString;
	}
	
	public String toString() {
		String toString = homeTeamIndex + ":" + awayTeamIndex;
		
		return toString;
	}
	
	public boolean sameAs(Spiel other) {
		if (this.homeTeamIndex != other.homeTeamIndex)	return false;
		if (this.awayTeamIndex != other.awayTeamIndex)	return false;
		return true;
	}
}
