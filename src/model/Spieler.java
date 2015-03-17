package model;

import static util.Utilities.*;

public class Spieler {

	private String trennZeichen = ";";
	
	private String firstName;
	private String lastName;
	private String lastNameShort;
	private String pseudonym;
	
	private int birthDate;
	
	private String nationality;
	
	private Position position;
	
	private Mannschaft team;
	private int squadNumber;
	
	public Spieler(String data, Mannschaft team) {
		fromString(data, team);
	}
	
	public Spieler(String firstName, String lastName, String pseudonym, int birthDate, String nationality, Position position, Mannschaft team, int squadNumber) {
		this.firstName = firstName;
		setLastName(lastName);
		this.pseudonym = pseudonym;
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
	
	public String getLastNameShort() {
		return this.lastNameShort;
	}
	
	private void setLastName(String lastName) {
		this.lastName = lastName;
		
		String[] lastNameSplit = lastName.split(" ");
		if (lastNameSplit.length > 1) {
			int countUpperCaseLastNames = 0;
			for (int i = 0; i < lastNameSplit.length; i++) {
				if (lastNameSplit[i].charAt(0) != lastNameSplit[i].toLowerCase().charAt(0))	countUpperCaseLastNames++;
			}
			if (countUpperCaseLastNames > 1) {
				lastNameShort = lastNameSplit[0];
			} else {
				lastNameShort = lastName;
			}
		} else {
			lastNameShort = lastName;
		}
	}
	
	public String getPseudonym() {
		return this.pseudonym != null ? this.pseudonym : this.lastNameShort;
	}
	
	public String getFullName() {
		return this.firstName + " " + lastName;
	}
	
	public String getFullNameShort() {
		return this.firstName + " " + lastNameShort;
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
		stringRep += this.pseudonym + trennZeichen;
		stringRep += this.birthDate + trennZeichen;
		stringRep += this.nationality + trennZeichen;
		stringRep += this.position.getName() + trennZeichen;
		stringRep += this.squadNumber;
		return stringRep;
	}
	
	public void fromString(String data, Mannschaft team) {
		String[] dataSplit = data.split(trennZeichen);
		
		this.firstName = dataSplit[0];
		setLastName(dataSplit[1]);
		this.pseudonym = (dataSplit[2].equals("null") ? null : dataSplit[2]);
		this.birthDate = Integer.parseInt(dataSplit[3]);
		this.nationality = dataSplit[4];
		this.position = Position.getPositionFromString(dataSplit[5]);
		this.team = team;
		this.squadNumber = Integer.parseInt(dataSplit[dataSplit.length - 1]);
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
