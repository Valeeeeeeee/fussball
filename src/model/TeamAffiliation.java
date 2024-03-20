package model;

import java.util.ArrayList;

import analyse.SaisonPerformance;
import analyse.SpielPerformance;

public class TeamAffiliation {
	
	private String trennZeichen = ";";
	
	private Mannschaft team;
	private Spieler player;
	private Position position;
	private int squadNumber;
	private Dauer duration;
	
	private SaisonPerformance seasonPerformance;
	
	public TeamAffiliation(Mannschaft team, Spieler player, Position position, int squadNumber, Dauer duration) {
		this.team = team;
		this.player = player;
		this.position = position;
		this.squadNumber = squadNumber;
		this.duration = duration;
		
		seasonPerformance = new SaisonPerformance(this);
	}
	
	public TeamAffiliation(Mannschaft team, String affiliation) {
		this.team = team;
		fromString(affiliation);
		seasonPerformance = new SaisonPerformance(this);
	}

	public Mannschaft getTeam() {
		return team;
	}

	public Spieler getPlayer() {
		return player;
	}

	public Position getPosition() {
		return position;
	}
	
	public int getSquadNumber() {
		return squadNumber;
	}
	
	public Dauer getDuration() {
		return duration;
	}
	
	public SaisonPerformance getSeasonPerformance() {
		return seasonPerformance;
	}
	
	public double getAverageImpact() {
		double sumOfImpacts = 0;
		int count = 0;
		
		ArrayList<SpielPerformance> performances = seasonPerformance.asSortedList();
		for (SpielPerformance mp : performances) {
			if (!mp.hasData())	continue;
			sumOfImpacts += mp.getImpact();
			count++;
		}
		
		return sumOfImpacts / count;
	}
	
	public void changeValues(Dauer duration, int squadNumber, Position position) {
		this.position = position;
		this.squadNumber = squadNumber;
		this.duration = duration;
	}
	
	public boolean inOrderBefore(TeamAffiliation other) {
		if (position.getID() < other.position.getID())	return true;
		if (position.getID() > other.position.getID())	return false;
		return player.inOrderBefore(other.player);
	}
	
	public boolean overlaps(TeamAffiliation other) {
		return duration.overlaps(other.duration);
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof TeamAffiliation))	return false;
		TeamAffiliation affiliation = (TeamAffiliation) other;
		
		if (!team.equals(affiliation.team))	return false;
		if (!player.equals(affiliation.player))	return false;
		if (position != affiliation.position)	return false;
		if (squadNumber != affiliation.squadNumber)	return false;
		if (!duration.equals(affiliation.duration))	return false;
		
		return true;
	}
	
	private void fromString(String affiliation) {
		int index = 0;
		String[] split = affiliation.split(trennZeichen);
		int playerID = Integer.parseInt(split[index++]);
		player = Fussball.getPlayer(playerID);
		position = Position.getPositionFromString(split[index++]);
		squadNumber = Integer.parseInt(split[index++]);
		duration = new Dauer(split[index++]);
	}
	
	public String toString() {
		String toString = player.getID() + trennZeichen;
		toString += position.getName() + trennZeichen;
		toString += squadNumber + trennZeichen;
		toString += duration.toString();
		return toString;
	}
}
