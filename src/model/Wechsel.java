package model;

import static util.Utilities.log;
import static util.Utilities.message;

public class Wechsel {
	
	private Spiel match;
	private boolean firstTeam;
	private Minute minute;
	private TeamAffiliation playerOff;
	private TeamAffiliation playerOn;
	
	public Wechsel(Spiel match, boolean firstTeam, Minute minute, TeamAffiliation playerOff, TeamAffiliation playerOn) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.minute = minute;
		this.playerOff = playerOff;
		this.playerOn = playerOn;
		
		log("Substitution by " + match.getTeam(firstTeam).getName() + 
				", in the " + minute + ". minute " + playerOff.getPlayer().getPopularOrLastName() + 
				" leaves the pitch and is replaced by " + playerOn.getPlayer().getPopularOrLastName());
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

	public Minute getMinute() {
		return minute;
	}

	public TeamAffiliation getPlayerOff() {
		return playerOff;
	}

	public TeamAffiliation getPlayerOn() {
		return playerOn;
	}

	public boolean isPlayerOff(int squadNumber) {
		return playerOff.getSquadNumber() == squadNumber;
	}

	public boolean isPlayerOn(int squadNumber) {
		return playerOn.getSquadNumber() == squadNumber;
	}

	private void parseString(String data) {
		minute = Minute.parse(data.substring(0, data.indexOf(":")));
		int sqOff = Integer.parseInt(data.substring(data.indexOf(":") + 1, data.indexOf(">>")));
		playerOff = match.getTeam(firstTeam).getAffiliation(sqOff, match.getKickOffTime().getDate());
		int sqOn = Integer.parseInt(data.substring(data.indexOf(">>") + 2));
		playerOn = match.getTeam(firstTeam).getAffiliation(sqOn, match.getKickOffTime().getDate());
		if (playerOff == null || playerOn == null) {
			message("Fehler beim Parsen der Wechsel des Teams " + match.getTeam(firstTeam).getName() + " im Spiel gegen " + match.getTeam(!firstTeam).getName());
			if (playerOff == null)	log("Es konnte der Rückennummer " + sqOff + " kein spielberechtigter Spieler zugeordnet werden.");
			if (playerOn == null)	log("Es konnte der Rückennummer " + sqOn + " kein spielberechtigter Spieler zugeordnet werden.");
		}
	}
	
	public String toString() {
		return minute.toString() + ":" + playerOff.getSquadNumber() + ">>" + playerOn.getSquadNumber();
	}
}
