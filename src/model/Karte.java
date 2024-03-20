package model;

import static util.Utilities.NO_PLAYER_FOR_SHIRTNUMBER;
import static util.Utilities.log;
import static util.Utilities.message;

public class Karte {
	private String id;
	private Spiel match;
	private boolean firstTeam;
	private boolean isYellowCard;
	private boolean isSecondBooking;
	private boolean onTheBench;
	private boolean afterTheMatch;
	private Minute minute;
	private TeamAffiliation bookedPlayer;
	
	public Karte(Spiel match, boolean firstTeam, Minute minute, boolean isYellowCard, boolean isSecondBooking, boolean onTheBench, boolean afterTheMatch, TeamAffiliation bookedPlayer) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.isYellowCard = isYellowCard;
		this.isSecondBooking = isSecondBooking;
		this.onTheBench = onTheBench;
		this.afterTheMatch = afterTheMatch;
		this.bookedPlayer = bookedPlayer;
		
		id = match.toString() + "-h" + firstTeam + "-m" + minute + "-y" + isYellowCard + "-s" + isSecondBooking + "-p" + bookedPlayer.getSquadNumber()
					+ (onTheBench ? "-b" : "") + (afterTheMatch ? "-am" : "");
		log("Booking for " + match.getTeam(firstTeam).getName() + " in the " + minute + ". minute: " + 
				bookedPlayer.getPlayer().getPopularOrLastName() + (onTheBench ? " (on the bench)" : "") + (afterTheMatch ? " (after the match)" : ""));
	}
	
	public Karte(Spiel match, String data) {
		this.match = match;
		parseString(data);
	}
	
	public String getID() {
		return id;
	}

	public Spiel getMatch() {
		return match;
	}
	
	public boolean isFirstTeam() {
		return firstTeam;
	}
	
	public boolean isYellowCard() {
		return isYellowCard;
	}
	
	public boolean isSecondBooking() {
		return isSecondBooking;
	}
	
	public void setSecondBooking(boolean isSecond) {
		isSecondBooking = isSecond;
	}
	
	public boolean isOnTheBench() {
		return onTheBench;
	}
	
	public void setOnTheBench(boolean onTheBench) {
		this.onTheBench = onTheBench;
	}
	
	public boolean isAfterTheMatch() {
		return afterTheMatch;
	}
	
	public Minute getMinute() {
		return minute;
	}
	
	public TeamAffiliation getBookedPlayer() {
		return bookedPlayer;
	}

	public boolean isBookedPlayer(TeamAffiliation player) {
		return bookedPlayer.equals(player);
	}
	
	private void parseString(String data) {
		onTheBench = data.indexOf("-b") != -1;
		afterTheMatch = data.indexOf("-am") != -1;
		data = data.replace("-b", "").replace("-am", "");
		firstTeam = Boolean.parseBoolean(data.substring(0, data.indexOf("-m")));
		minute = Minute.parse(data.substring(data.indexOf("-m") + 2, data.indexOf("-y")));
		isYellowCard = Boolean.parseBoolean(data.substring(data.indexOf("-y") + 2, data.indexOf("-s")));
		isSecondBooking = Boolean.parseBoolean(data.substring(data.indexOf("-s") + 2, data.indexOf("-p")));
		int squadNumber = Integer.parseInt(data.substring(data.indexOf("-p") + 2));
		bookedPlayer = match.getTeam(firstTeam).getAffiliation(squadNumber, match.getKickOffTime().getDate());
		if (bookedPlayer == null) {
			message("Fehler beim Parsen der Karten des Teams " + match.getTeam(firstTeam).getName() + " im Spiel gegen " + match.getTeam(!firstTeam).getName());
			log(String.format(NO_PLAYER_FOR_SHIRTNUMBER, squadNumber, match.getTeam(firstTeam).getName()));
		}
		
		id = match.toString() + "-h" + data;
	}
	
	public String toString() {
		return firstTeam + "-m" + minute.toString() + "-y" + isYellowCard + "-s" + isSecondBooking + "-p" + 
				bookedPlayer.getSquadNumber() + (onTheBench ? "-b" : "") + (afterTheMatch ? "-am" : "");
	}
}
