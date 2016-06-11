package model;

import static util.Utilities.log;

public class Karte {
	private String id;
	private Spiel spiel;
	private boolean firstTeam;
	private boolean isYellowCard;
	private boolean isSecondBooking;
	private boolean onTheBench;
	private int minute;
	private Spieler bookedPlayer;
	
	public Karte(Spiel spiel, boolean firstTeam, int minute, boolean isYellowCard, boolean isSecondBooking, boolean onTheBench, Spieler bookedPlayer) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.isYellowCard = isYellowCard;
		this.isSecondBooking = isSecondBooking;
		this.onTheBench = onTheBench;
		this.bookedPlayer = bookedPlayer;
		
		this.id = spiel.home() + "v" + spiel.away() + "-h" + firstTeam + "-m" + minute + "-y" + isYellowCard + "-s" + isSecondBooking + "-p" + bookedPlayer.getSquadNumber()
					+ (onTheBench ? "-b" : "");
		log("Booking for " + (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getName() + 
				" in the " + minute + ". minute: " + bookedPlayer.getPseudonymOrLN() + (onTheBench ? " (on the bench)" : ""));
	}
	
	public Karte(Spiel spiel, String data) {
		this.spiel = spiel;
		parseString(data);
	}
	
	public String getID() {
		return id;
	}

	public Spiel getSpiel() {
		return spiel;
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
	
	public int getMinute() {
		return minute;
	}

	public Spieler getBookedPlayer() {
		return bookedPlayer;
	}
	
	private void parseString(String data) {
		onTheBench = data.indexOf("-b") != -1;
		firstTeam = Boolean.parseBoolean(data.substring(0, data.indexOf("-m")));
		minute = Integer.parseInt(data.substring(data.indexOf("-m") + 2, data.indexOf("-y")));
		isYellowCard = Boolean.parseBoolean(data.substring(data.indexOf("-y") + 2, data.indexOf("-s")));
		isSecondBooking = Boolean.parseBoolean(data.substring(data.indexOf("-s") + 2, data.indexOf("-p")));
		int squadNumber = Integer.parseInt(data.substring(data.indexOf("-p") + 2).replace("-b", ""));
		bookedPlayer = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(squadNumber, spiel.getDate());
		
		id = spiel.home() + "v" + spiel.away() + "-h" + data;
	}
	
	public String toString() {
		return firstTeam + "-m" + minute + "-y" + isYellowCard + "-s" + isSecondBooking + "-p" + bookedPlayer.getSquadNumber() + (onTheBench ? "-b" : "");
	}
}
