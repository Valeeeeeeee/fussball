package model;

import static util.Utilities.*;

public class Spieler {

	private String trennZeichen = ";";
	
	private String firstName;
	private String lastName;
	
	private int birthDate;
	
	private String nationality;
	
	private Position position;
	
	private Mannschaft team;
	private int squadNumber;
	
	public Spieler(String data, Mannschaft team) {
		fromString(data, team);
	}
	
	public Spieler(String firstName, String lastName, int birthDate, String nationality, Position position, Mannschaft team, int squadNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.position = position;
		this.team = team;
		this.squadNumber = squadNumber;
	}
	
	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public int getBirthDate() {
		return this.birthDate;
	}

	public String getNationality() {
		return this.nationality;
	}
	
	public Position getPosition() {
		return this.position;
	}

	public Mannschaft getTeam() {
		return this.team;
	}

	public int getSquadNumber() {
		return this.squadNumber;
	}

	public String toString() {
		String stringRep = this.firstName + trennZeichen;
		stringRep += this.lastName + trennZeichen;
		stringRep += this.birthDate + trennZeichen;
		stringRep += this.nationality + trennZeichen;
		stringRep += this.position.getName() + trennZeichen;
		stringRep += this.team.getName() + trennZeichen;
		stringRep += this.squadNumber;
		return stringRep;
	}
	
	public void fromString(String data, Mannschaft team) {
		String[] dataSplit = data.split(trennZeichen);
		
		this.firstName = dataSplit[0];
		this.lastName = dataSplit[1];
		this.birthDate = Integer.parseInt(dataSplit[2]);
		this.nationality = dataSplit[3];
		this.position = Position.getPositionFromString(dataSplit[4]);
		this.team = team;
		this.squadNumber = Integer.parseInt(dataSplit[6]);
	}
}

enum Position {
	TOR(0, "Tor"), ABWEHR(1, "Abwehr"), MITTELFELD(2, "Mittelfeld"), STURM(3, "Sturm");
	
	private int id;
	private String name;
	
	private Position (int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static Position getPositionFromString(String string) {
		for (Position position : values()) {
			if (position.getName().equals(string))	return position;
		}
		return null;
	}
}



