package model;

import static util.Utilities.message;
import static util.Utilities.UNDEFINED;

public class Aufstellung {
	
	public static final int numberOfPlayersInLineUp = 11;
	
	private int countPlayers = 0; 
	
	private Mannschaft team;
	private Spiel match;
	private TeamAffiliation[] players;
	
	public Aufstellung(Mannschaft team, Spiel match) {
		this.team = team;
		this.match = match;
		players = new TeamAffiliation[numberOfPlayersInLineUp];
	}
	
	public Aufstellung(Mannschaft team, Spiel match, String data) {
		this.team = team;
		this.match = match;
		fromString(data);
	}
	
	public TeamAffiliation[] getPlayers() {
		return players;
	}
	
	public void addPlayer(TeamAffiliation player) {
		if (countPlayers == numberOfPlayersInLineUp) {
			message("Es sind bereits " + countPlayers + " Spieler in der Aufstellung.");
			return;
		}
		players[countPlayers++] = player;
	}
	
	public boolean contains(TeamAffiliation player) {
		for (int i = 0; i < countPlayers; i++) {
			if (players[i].equals(player))	return true;
		}
		return false;
	}
	
	public int getIndex(TeamAffiliation player) {
		for (int i = 0; i < countPlayers; i++) {
			if (players[i].equals(player))	return i;
		}
		return UNDEFINED;
	}
	
	private void fromString(String data) {
		String[] squadNumbersSplit = data.split(",");
		int[] squadNumbers = new int[numberOfPlayersInLineUp];
		for (int i = 0; i < numberOfPlayersInLineUp; i++) {
			squadNumbers[i] = Integer.parseInt(squadNumbersSplit[i]);
		}
		
		players = team.getLineup(squadNumbers, match.getKickOffTime().getDate());
		for (int i = 0; i < numberOfPlayersInLineUp; i++) {
			if (players[i] != null)	countPlayers++;
		}
	}
	
	public String toString() {
		String toString = "", sep = "";
		
		for (int i = 0; i < players.length; i++) {
			toString += sep + players[i].getSquadNumber();
			sep = ",";
		}
		
		return toString;
	}
}
