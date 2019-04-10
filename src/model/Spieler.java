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
	private Datum birthDate;
	private int age;
	private String nationality;
	
	private Position position;
	private Mannschaft team;
	private int squadNumber;
	
	private Datum firstDate = MIN_DATE;
	private Datum lastDate = MAX_DATE;
	private Datum secondFDate = MAX_DATE;
	
	private SaisonPerformance seasonPerformance;
	
	public Spieler(String data, Mannschaft team) {
		fromString(data, team);
		seasonPerformance = new SaisonPerformance(this);
	}
	
	public Spieler(String firstName, String lastName, String popularName, Datum birthDate, String nationality, Position position, Mannschaft team, int squadNumber) {
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
		firstName = firstNameFile;
		if (firstNameFile.contains("'") && firstNameFile.indexOf("'") < firstNameFile.lastIndexOf("'")) {
			firstName = firstName.replace("'", "");
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

	public Datum getBirthDate() {
		return birthDate;
	}

	public int getAge() {
		if (age == 0)	age = birthDate.daysUntil(today);
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
	
	public Datum getFirstDate() {
		return firstDate;
	}
	
	public Datum getLastDate() {
		return lastDate;
	}
	
	public Datum getSecondFirstDate() {
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
	
	public boolean isEligible(Datum date) {
		if (date.isBefore(firstDate))		return false;
		if (!date.isBefore(secondFDate))	return true;
		if (date.isAfter(lastDate))			return false;
		return true;
	}
	
	public boolean playedAtTheSameTimeAs(Datum otherFirstDate, Datum otherLastDate, Datum otherSecondFDate) {
		boolean this2FD = secondFDate != MAX_DATE, other2FD = otherSecondFDate != MAX_DATE;
		if (this2FD && other2FD || firstDate.equals(otherFirstDate) || lastDate.equals(otherLastDate))	return true;
		int fDate = firstDate.comparable(), lDate = lastDate.comparable();
		int oFDate = otherFirstDate.comparable(), oLDate = otherLastDate.comparable();
		if (other2FD)	return lDate >= oFDate && (oLDate >= fDate || lDate >= otherSecondFDate.comparable());
		if (this2FD)	return oLDate >= fDate && (lDate >= oFDate || oLDate >= secondFDate.comparable());
		return (oFDate <= lDate && fDate <= oLDate);
	}
	
	public void updateInfo(String firstName, String lastName, String popularName, Datum birthDate, String nationality, String position, int squadNumber, Datum firstDate, Datum lastDate, Datum secondFDate) {
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
		toString += birthDate.comparable() + trennZeichen;
		toString += nationality + trennZeichen;
		toString += position.getName() + trennZeichen;
		toString += squadNumber;
		if (firstDate != MIN_DATE || lastDate != MAX_DATE) {
			toString += trennZeichen + (firstDate != MIN_DATE ? firstDate.comparable() : "") + "-" + (lastDate != MAX_DATE ? lastDate.comparable() : "");
			if (secondFDate != MAX_DATE)	toString += "," + secondFDate.comparable() + "-";
		}
		return toString;
	}
	
	public void fromString(String data, Mannschaft team) {
		String[] split = data.split(trennZeichen);
		int index = 0;
		
		setFirstName(split[index++]);
		setLastName(split[index++]);
		popularName = (split[index++].equals("null") ? null : split[index - 1]);
		birthDate = new Datum(split[index++]);
		nationality = split[index++];
		position = Position.getPositionFromString(split[index++]);
		this.team = team;
		squadNumber = Integer.parseInt(split[index++]);
		if (split.length >= 8) {
			String[] allDates = split[index++].split(",");
			String[] dates = allDates[0].split("\\-");
			if (dates[0] != null && !dates[0].isEmpty())	firstDate = new Datum(dates[0]);
			if (dates.length == 2 && dates[1] != null)		lastDate = new Datum(dates[1]);
			if (allDates.length > 1)						secondFDate = new Datum(allDates[1].substring(0, allDates[1].indexOf("-")));
		}
	}
}
