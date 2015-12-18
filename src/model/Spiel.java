package model;

import static util.Utilities.*;

import java.util.ArrayList;

public class Spiel {
	
	private int matchday;
	private int date;
	private int time;
	private int homeTeamIndex;
	private int awayTeamIndex;
	
	private Wettbewerb wettbewerb;
	private Mannschaft homeTeam;
	private Mannschaft awayTeam;
	private int[] lineupHome;
	private int[] lineupAway;
	
	private Schiedsrichter referee;
	private Ergebnis ergebnis;
	private ArrayList<Tor> goals = new ArrayList<>();
	private ArrayList<Wechsel> substitutionsHome = new ArrayList<>();
	private ArrayList<Wechsel> substitutionsAway = new ArrayList<>();
	private ArrayList<Karte> bookings = new ArrayList<>();
	
	public Spiel(Wettbewerb wettbewerb, int matchday, int date, int time, int homeTeamIndex, int awayTeamIndex) {
		this.wettbewerb = wettbewerb;
		this.matchday = matchday;
		this.date = date;
		this.time = time;
		
		this.homeTeamIndex = homeTeamIndex;
		this.homeTeam = wettbewerb.getMannschaften()[homeTeamIndex - 1];
		this.awayTeamIndex = awayTeamIndex;
		this.awayTeam = wettbewerb.getMannschaften()[awayTeamIndex - 1];
	}
	
	public Spiel(Wettbewerb wettbewerb, int matchday, int date, int time, String daten) {
		this.wettbewerb = wettbewerb;
		this.matchday = matchday;
		this.date = date;
		this.time = time;
		parseString(daten);
	}
	
	public Wettbewerb getWettbewerb() {
		return this.wettbewerb;
	}
	
	public String getDescription() {
		return wettbewerb.getMatchdayDescription(matchday);
	}
	
	public String getDateAndTime() {
		return MyDate.datum(date) + " " + MyDate.uhrzeit(time);
	}
	
	public int getMatchday() {
		return matchday;
	}
	
	public int getDate() {
		return this.date;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public int home() {
		return this.homeTeamIndex;
	}
	
	public Mannschaft getHomeTeam() {
		return this.homeTeam;
	}
	
	public int away() {
		return this.awayTeamIndex;
	}
	
	public Mannschaft getAwayTeam() {
		return this.awayTeam;
	}
	
	public Ergebnis getErgebnis() {
		return this.ergebnis;
	}
	
	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}
	
	public int[] getLineupHome() {
		return this.lineupHome;
	}
	
	public void setLineupHome(int[] lineupHome) {
		this.lineupHome = lineupHome;
	}
	
	public int[] getLineupAway() {
		return this.lineupAway;
	}
	
	public void setLineupAway(int[] lineupAway) {
		this.lineupAway = lineupAway;
	}
	
	public ArrayList<Tor> getTore() {
		return this.goals;
	}
	
	public ArrayList<Wechsel> getSubstitutions(boolean firstTeam) {
		return firstTeam ? substitutionsHome : substitutionsAway;
	}
	
	public ArrayList<Karte> getBookings() {
		return this.bookings;
	}
	
	public void addGoal(Tor goal) {
		if (goal != null) {
			int index = 0;
			for (int i = 0; i < goals.size(); i++) {
				if (goals.get(i).getMinute() <= goal.getMinute())	index++;
			}
			goals.add(index, goal);
			ergebnis = new Ergebnis(goals);
		}
	}
	
	public void removeGoal(Tor goal) {
		if (goal != null) {
			goals.remove(goal);
			ergebnis = new Ergebnis(goals);
		}
	}
	
	public int addSubstitution(Wechsel substitution) {
		int index = 0;
		if (substitution != null) {
			if (substitution.isFirstTeam()) {
				for (int i = 0; i < substitutionsHome.size(); i++) {
					if (substitutionsHome.get(i).getMinute() <= substitution.getMinute())	index++;
				}
				substitutionsHome.add(index, substitution);
			} else {
				for (int i = 0; i < substitutionsAway.size(); i++) {
					if (substitutionsAway.get(i).getMinute() <= substitution.getMinute())	index++;
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
				if (bookings.get(i).getMinute() <= booking.getMinute())	index++;
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
	
	public void setSchiedsrichter(int refereeID) {
		setSchiedsrichter(refereeID == 0 ? null : wettbewerb.getReferees().get(refereeID - 1));
	}
	
	public void setSchiedsrichter(Schiedsrichter referee) {
		this.referee = referee;
	}
	
	public void setRemainder(String matchData) {
		String[] matchDataSplit = matchData.split("\\+");
		if (!matchDataSplit[0].equals(homeTeamIndex + ":" + awayTeamIndex))	return;
		if (matchDataSplit.length > 1) {
			parseMatchData(matchDataSplit[1]);
			if (matchDataSplit.length == 4) {
				lineupHome = parseLineup(matchDataSplit[2], true);
				lineupAway = parseLineup(matchDataSplit[3], false);
			}
		}
	}
	
	private String getRemainder() {
		String remainder = "";
		
		if (lineupHome != null || lineupAway != null) {
			remainder += "+{" + matchDataToString() + "}";
			remainder += "+{" + lineupToString(lineupHome, substitutionsHome) + "}+{" + lineupToString(lineupAway, substitutionsAway) + "}";
		} else if (ergebnis != null) {
			remainder += "+{" + matchDataToString() + "}";
		} else if (referee != null) {
			remainder += "+{" + referee.getID() + "}";
		}
		
		return remainder;
	}
	
	private String matchDataToString() {
		String matchData = "";
		
		if (referee != null)						matchData += referee.getID();
		if (referee != null && ergebnis != null)	matchData += "_";
		if (ergebnis != null)						matchData += ergebnis;
		
		for (Tor tor : goals) {
			matchData += "#" + tor;
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
			for (Wechsel wechsel : substitutions) {
				lineupString += "#" + wechsel.toString();
			}
		} else {
			lineupString = "null";
		}
		
		return lineupString;
	}
	
	private void parseMatchData(String matchData) {
		matchData = matchData.replace("{", "").replace("}", "");
		if (matchData.indexOf("_") != -1) {
			referee = wettbewerb.getReferees().get(Integer.parseInt(matchData.substring(0, matchData.indexOf("_"))) - 1);
		}
		matchData = matchData.substring(matchData.indexOf("_") + 1);
		if (matchData.indexOf(":") == -1) {
			referee = wettbewerb.getReferees().get(Integer.parseInt(matchData) - 1);
			return;
		}
		String[] matchDataSplit = matchData.split("\\^");
		String allGoals = matchDataSplit[0];
		String[] goalsSplit = allGoals.split("#");
		if(!goalsSplit[0].equals("null"))	this.ergebnis = new Ergebnis(goalsSplit[0]);
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
				Wechsel wechsel = new Wechsel(this, firstTeam, hashSplit[i]);
				addSubstitution(wechsel);
			}
		}
		
		return checkLineup(lineup);
	}
	
	private int[] checkLineup(int[] lineup) {
		boolean isValid = true;
		// TODO check validity
		return isValid ? lineup : null;
	}
	
	private void parseString(String daten) {
		try {
			this.homeTeamIndex = Integer.parseInt(daten.split(":")[0]);
			this.awayTeamIndex = Integer.parseInt(daten.split(":")[1]);
			
			if (this.homeTeamIndex == this.awayTeamIndex || this.homeTeamIndex == -1 || this.awayTeamIndex == -1) {
				throw new IllegalArgumentException();
			}
			
			this.homeTeam = wettbewerb.getMannschaften()[homeTeamIndex - 1];
			this.awayTeam = wettbewerb.getMannschaften()[awayTeamIndex - 1];
		} catch (IllegalArgumentException iae) {
			log("The given match was formally correct, but impossible.");
			log(iae.getMessage());
		} catch (Exception e) {
			log("The given match was formally incorrect.");
			e.printStackTrace();
		}
	}
	
	public String fullString() {
		String fullString = this.homeTeamIndex + ":" + this.awayTeamIndex + getRemainder();
		
		return fullString;
	}
	
	public String toString() {
		String toString = this.homeTeamIndex + ":" + this.awayTeamIndex;
		
		return toString;
	}
	
	public boolean sameAs(Spiel other) {
		if (this.homeTeamIndex != other.homeTeamIndex)	return false;
		if (this.awayTeamIndex != other.awayTeamIndex)	return false;
		return true;
	}
}
