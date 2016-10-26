package model;

import static util.Utilities.*;

import java.util.ArrayList;

import analyse.SaisonPerformance;
import analyse.SpielPerformance;

public class Spieler {

	private String trennZeichen = ";";
	
	private String firstName;
	private String firstNameShort;
	private String firstNameFile;
	private String lastName;
	private String lastNameShort;
	private String lastNameFile;
	private String distinctName;
	private String popularName;
	private int birthDate;
	private int age;
	private String nationality;
	
	private Position position;
	private Mannschaft team;
	private int squadNumber;
	
	private int firstDate = -1;
	private int lastDate = -1;
	private int secondFDate = -1;
	
	private SaisonPerformance seasonPerformance;
	
	public Spieler(String data, Mannschaft team) {
		fromString(data, team);
		seasonPerformance = new SaisonPerformance(this);
	}
	
	public Spieler(String firstName, String lastName, String popularName, int birthDate, String nationality, Position position, Mannschaft team, int squadNumber) {
		setFirstName(firstName);
		setLastName(lastName);
		this.popularName = popularName;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.position = position;
		this.team = team;
		this.squadNumber = squadNumber;
		seasonPerformance = new SaisonPerformance(this);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getFirstNameShort() {
		return firstNameShort;
	}
	
	public String getFirstNameFile() {
		return firstNameFile;
	}
	
	private void setFirstName(String firstNameFile) {
		this.firstNameFile = firstNameFile;
		firstName = firstNameFile.replace("'", "");
		if (firstNameFile.contains("'") && firstNameFile.indexOf("'") < firstNameFile.lastIndexOf("'")) {
			firstNameShort = firstNameFile.substring(firstNameFile.indexOf("'") + 1, firstNameFile.lastIndexOf("'"));
		} else {
			firstNameShort = firstNameFile.split(" ")[0];
		}
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getLastNameShort() {
		return lastNameShort;
	}
	
	public String getLastNameFile() {
		return lastNameFile;
	}
	
	private void setLastName(String lastNameFile) {
		this.lastNameFile = lastNameFile;
		lastName = lastNameFile.replace("'", "");
		
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
	
	public String getPopularOrLastName() {
		if (popularName != null)	return popularName;
		if (distinctName != null)	return distinctName;
		return lastNameShort;
	}
	
	public void setDistinctionLevel(int level) {
		boolean fullFirstName = level >= firstName.length();
		distinctName = (fullFirstName ? firstName : firstName.substring(0, level) + ".") + " " + lastNameShort;
	}
	
	public void resetDistinctName() {
		distinctName = null;
	}
	
	public String getPopularName() {
		return popularName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getFullNameShort() {
		return popularName != null ? popularName : firstNameShort + " " + lastNameShort;
	}

	public int getBirthDate() {
		return birthDate;
	}

	public int getAge() {
		if (age == 0)	age = MyDate.difference(birthDate, Start.today());
		return age;
	}

	public String getNationality() {
		return nationality;
	}
	
	public Position getPosition() {
		return position;
	}

	public Mannschaft getTeam() {
		return team;
	}

	public int getSquadNumber() {
		return squadNumber;
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
		if (other2FD)	return lDate >= oFDate && (oLDate >= fDate || lDate >= otherSecondFDate);
		if (this2FD)	return oLDate >= fDate && (lDate >= oFDate || oLDate >= secondFDate);
		return (oFDate <= lDate && fDate <= oLDate);
	}
	
	public void updateInfo(String firstName, String lastName, String popularName, int birthDate, String nationality, String position, int squadNumber, int firstDate, int lastDate, int secondFDate) {
		team.changeSquadNumber(this, squadNumber);
		setFirstName(firstName);
		setLastName(lastName);
		this.popularName = popularName;
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
		String myName = removeUmlaute(popularName != null ? popularName : lastNameShort).toLowerCase();
		String otherName = removeUmlaute(other.popularName != null ? other.popularName : other.lastNameShort).toLowerCase();
		if (myName.equals(otherName))	return firstName.compareTo(other.firstName) < 0;
		return myName.compareTo(otherName) < 0;
	}
	
	public String toString() {
		String toString = firstNameFile + trennZeichen;
		toString += lastNameFile + trennZeichen;
		toString += popularName + trennZeichen;
		toString += birthDate + trennZeichen;
		toString += nationality + trennZeichen;
		toString += position.getName() + trennZeichen;
		toString += squadNumber;
		if (firstDate + lastDate != -2) {
			toString += trennZeichen + (firstDate != -1 ? firstDate : "") + "-" + (lastDate != -1 ? lastDate : "");
			if (secondFDate != -1)	toString += "," + secondFDate + "-";
		}
		return toString;
	}
	
	public void fromString(String data, Mannschaft team) {
		String[] split = data.split(trennZeichen);
		int index = 0;
		
		setFirstName(split[index++]);
		setLastName(split[index++]);
		popularName = (split[index++].equals("null") ? null : split[index - 1]);
		birthDate = Integer.parseInt(split[index++]);
		nationality = split[index++];
		position = Position.getPositionFromString(split[index++]);
		this.team = team;
		squadNumber = Integer.parseInt(split[index++]);
		if (split.length >= 8) {
			String[] allDates = split[index++].split(",");
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
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public static Position getPositionFromString(String string) {
		for (Position position : values()) {
			if (position.getName().equals(string))	return position;
		}
		return null;
	}
}
