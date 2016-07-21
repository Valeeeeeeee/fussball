package model;

import static util.Utilities.log;

public class Wechsel {
	
	private Spiel match;
	private boolean firstTeam;
	private int minute;
	private Spieler playerOff;
	private Spieler playerOn;
	
	public Wechsel(Spiel match, boolean firstTeam, int minute, Spieler playerOff, Spieler playerOn) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.playerOff = playerOff;
		this.playerOn = playerOn;
		
		log("Substitution by " + (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getName() + 
				", in the " + minute + ". minute " + playerOff.getPseudonymOrLN() + 
				" leaves the pitch and is replaced by " + playerOn.getPseudonymOrLN());
	}
	
	public Wechsel(Spiel match, boolean firstTeam, String data) {
		this.match = match;
		this.firstTeam = firstTeam;
		parseString(data);
	}
	
	public Spiel getMatch() {
		return match;
	}

	public boolean isFirstTeam() {
		return firstTeam;
	}

	public int getMinute() {
		return minute;
	}

	public Spieler getPlayerOff() {
		return playerOff;
	}

	public Spieler getPlayerOn() {
		return playerOn;
	}

	private void parseString(String data) {
		minute = Integer.parseInt(data.substring(0, data.indexOf(":")));
		int sqOff = Integer.parseInt(data.substring(data.indexOf(":") + 1, data.indexOf(">>")));
		playerOff = (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getPlayer(sqOff, match.getDate());
		int sqOn = Integer.parseInt(data.substring(data.indexOf(">>") + 2));
		playerOn = (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getPlayer(sqOn, match.getDate());
	}
	
	public String toString() {
		return minute + ":" + playerOff.getSquadNumber() + ">>" + playerOn.getSquadNumber();
	}
}
