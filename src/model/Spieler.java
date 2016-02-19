package model;

import static util.Utilities.*;

public class Spieler {

	private String trennZeichen = ";";
	
	private String firstName;
	private String firstNameShort;
	private String firstNameFile;
	private String lastName;
	private String lastNameShort;
	private String lastNameFile;
	private String distinctName;
	private String pseudonym;
	private int birthDate;
	private int age;
	private String nationality;
	
	private Position position;
	private Mannschaft team;
	private int squadNumber;
	
	private int firstDate = -1;
	private int lastDate = -1;
	private int secondFDate = -1;
	
	public Spieler(String data, Mannschaft team) {
		fromString(data, team);
	}
	
	public Spieler(String firstName, String lastName, String pseudonym, int birthDate, String nationality, Position position, Mannschaft team, int squadNumber) {
		setFirstName(firstName);
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
	
	public String getFirstNameShort() {
		return this.firstNameShort;
	}
	
	public String getFirstNameFile() {
		return this.firstNameFile;
	}
	
	private void setFirstName(String firstNameFile) {
		this.firstNameFile = firstNameFile;
		this.firstName = firstNameFile.replace("'", "");
		if (firstNameFile.contains("'") && firstNameFile.indexOf("'") < firstNameFile.lastIndexOf("'")) {
			firstNameShort = firstNameFile.substring(firstNameFile.indexOf("'") + 1, firstNameFile.lastIndexOf("'"));
		} else {
			firstNameShort = firstNameFile.split(" ")[0];
		}
	}

	public String getLastName() {
		return this.lastName;
	}
	
	public String getLastNameShort() {
		return this.lastNameShort;
	}
	
	public String getLastNameFile() {
		return this.lastNameFile;
	}
	
	private void setLastName(String lastNameFile) {
		this.lastNameFile = lastNameFile;
		this.lastName = lastNameFile.replace("'", "");
		
		String[] lastNameSplit = lastNameFile.split(" ");
		if (lastNameFile.contains("'") && lastNameFile.indexOf("'") < lastNameFile.lastIndexOf("'")) {
			lastNameShort = lastNameFile.substring(lastNameFile.indexOf("'") + 1, lastNameFile.lastIndexOf("'"));
		} else if (lastNameSplit.length > 1) {
			int countUpperCaseLastNames = 0;
			for (int i = 0; i < lastNameSplit.length; i++) {
				if (lastNameSplit[i].charAt(0) != lastNameSplit[i].toLowerCase().charAt(0))	countUpperCaseLastNames++;
			}
			if (countUpperCaseLastNames > 1) {
				lastNameShort = lastNameSplit[0];
			} else {
				lastNameShort = lastNameFile;
			}
		} else {
			lastNameShort = lastNameFile;
		}
	}
	
	public String getPseudonymOrLN() {
		if (this.pseudonym != null)	return this.pseudonym;
		if (this.distinctName != null)	return this.distinctName;
		return this.lastNameShort;
	}
	
	public void setDistictionLevel(int level) {
		boolean fullFirstName = level >= firstName.length();
		this.distinctName = (fullFirstName ? this.firstName : this.firstName.substring(0, level) + ".") + " " + this.lastNameShort;
	}
	
	public void resetDistinctName() {
		this.distinctName = null;
	}
	
	public String getPseudonym() {
		return this.pseudonym;
	}
	
	public String getFullName() {
		return this.firstName + " " + lastName;
	}
	
	public String getFullNameShort() {
		return this.pseudonym != null ? this.pseudonym : this.firstNameShort + " " + lastNameShort;
	}

	public int getBirthDate() {
		return this.birthDate;
	}

	public int getAge() {
		if (this.age == 0)	this.age = MyDate.difference(birthDate, Start.today());
		return this.age;
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
	
	public int getFirstDate() {
		return firstDate;
	}
	
	public int getLastDate() {
		return lastDate;
	}
	
	public int getSecondFirstDate() {
		return secondFDate;
	}
	
	public boolean isEligible(int date) {
		if (date == 0)									return false;
		if (date < firstDate)							return false;
		if (secondFDate != -1 && date >= secondFDate)	return true;
		if (lastDate != -1 && date > lastDate)			return false;
		
		return true;
	}
	
	public boolean playedAtTheSameTimeAs(int otherFirstDate, int otherLastDate, int otherSecondFDate) {
		boolean this2FD = secondFDate != -1, other2FD = otherSecondFDate != -1;
		if (this2FD && other2FD || firstDate == otherFirstDate || lastDate == otherLastDate)	return true;
		int fDate = firstDate == -1 ? 101 : firstDate, lDate = lastDate == -1 ? 99991231 : lastDate;
		int oFDate = otherFirstDate == -1 ? 101 : otherFirstDate, oLDate = otherLastDate == -1 ? 99991231 : otherLastDate;
		if (other2FD)		return lDate >= oFDate && (oLDate >= fDate || lDate >= otherSecondFDate);
		else if (this2FD)	return oLDate >= fDate && (lDate >= oFDate || oLDate >= secondFDate);
		else				return (oFDate <= lDate && fDate <= oLDate);
	}
	
	public void updateInfo(String firstName, String lastName, String pseudonym, int birthDate, String nationality, String position, int squadNumber, int firstDate, int lastDate, int secondFDate) {
		team.changeSquadNumber(this, squadNumber);
		setFirstName(firstName);
		setLastName(lastName);
		this.pseudonym = pseudonym;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.position = Position.getPositionFromString(position);
		this.squadNumber = squadNumber;
		this.firstDate = firstDate;
		this.lastDate = lastDate;
		this.secondFDate = secondFDate;
		team.playerUpdated();
	}
	
	public boolean inOrderBefore(Spieler other) {
		if (position.getID() < other.position.getID())	return true;
		if (position.getID() > other.position.getID())	return false;
		String myName = removeUmlaute(pseudonym != null ? pseudonym : lastName).toLowerCase();
		String otherName = removeUmlaute(other.pseudonym != null ? other.pseudonym : other.lastName).toLowerCase();
		if (myName.equals(otherName))	return firstName.compareTo(other.firstName) < 0;
		else return						myName.compareTo(otherName) < 0;
	}
	
	public String toString() {
		String stringRep = this.firstNameFile + trennZeichen;
		stringRep += this.lastNameFile + trennZeichen;
		stringRep += this.pseudonym + trennZeichen;
		stringRep += this.birthDate + trennZeichen;
		stringRep += this.nationality + trennZeichen;
		stringRep += this.position.getName() + trennZeichen;
		stringRep += this.squadNumber;
		if (this.firstDate + this.lastDate != -2) {
			stringRep += trennZeichen + (this.firstDate != -1 ? this.firstDate : "") + "-" + (this.lastDate != -1 ? this.lastDate : "");
			if (secondFDate != -1)	stringRep += "," + this.secondFDate + "-";
		}
		return stringRep;
	}
	
	public void fromString(String data, Mannschaft team) {
		String[] dataSplit = data.split(trennZeichen);
		
		setFirstName(dataSplit[0]);
		setLastName(dataSplit[1]);
		this.pseudonym = (dataSplit[2].equals("null") ? null : dataSplit[2]);
		this.birthDate = Integer.parseInt(dataSplit[3]);
		this.nationality = dataSplit[4];
		this.position = Position.getPositionFromString(dataSplit[5]);
		this.team = team;
		this.squadNumber = Integer.parseInt(dataSplit[6]);
		if (dataSplit.length >= 8) {
			String[] allDates = dataSplit[7].split(",");
			String[] dates = allDates[0].split("\\-");
			if (dates[0] != null && !dates[0].isEmpty())	firstDate = Integer.parseInt(dates[0]);
			if (dates.length == 2 && dates[1] != null)		lastDate = Integer.parseInt(dates[1]);
			if (allDates.length > 1)						secondFDate = Integer.parseInt(allDates[1].substring(0, allDates[1].indexOf("-")));
		}
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
