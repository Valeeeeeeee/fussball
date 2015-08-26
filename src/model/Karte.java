package model;

public class Karte {
	private String id;
	private String toString;
	private Spiel spiel;
	private boolean firstTeam;
	private boolean isYellowCard;
	private boolean isSecondBooking;
	private int minute;
	private Spieler bookedPlayer;
	
	public Karte(Spiel spiel, boolean firstTeam, int minute, boolean isYellowCard, boolean isSecondBooking, Spieler bookedPlayer) {
		this.spiel = spiel;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.isYellowCard = isYellowCard;
		this.isSecondBooking = isSecondBooking;
		this.bookedPlayer = bookedPlayer;
		
		this.toString = firstTeam + "-m" + minute + "-y" + isYellowCard + "-s" + isSecondBooking + "-p" + bookedPlayer.getSquadNumber();
		this.id = spiel.home() + "v" + spiel.away() + "-h" + toString;
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
	
	public int getMinute() {
		return minute;
	}

	public Spieler getBookedPlayer() {
		return bookedPlayer;
	}
	
	private void parseString(String data) {
		firstTeam = Boolean.parseBoolean(data.substring(0, data.indexOf("-m")));
		minute = Integer.parseInt(data.substring(data.indexOf("-m") + 2, data.indexOf("-y")));
		isYellowCard = Boolean.parseBoolean(data.substring(data.indexOf("-y") + 2, data.indexOf("-s")));
		isSecondBooking = Boolean.parseBoolean(data.substring(data.indexOf("-s") + 2, data.indexOf("-p")));
		int squadNumber = Integer.parseInt(data.substring(data.indexOf("-p") + 2));
		bookedPlayer = (firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam()).getSpieler(squadNumber, spiel.getDate());;
		
		toString = data;
		id = spiel.home() + "v" + spiel.away() + "-h" + toString;
	}
	
	public String toString() {
		return toString;
	}
}
