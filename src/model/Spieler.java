package model;

import static util.Utilities.*;

import java.util.ArrayList;

import analyse.SaisonPerformance;
import analyse.SpielPerformance;

public class Spieler {

	private static final int[] factorsFN = {3, -1, 4, -1, 2, 1, -2, 3, -1};
	private static final int[] factorsLN = {2, -1, 3, 1, -1, 1, -1, 3, -1};
	
	private String trennZeichen = ";";
	
	private int id;
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
	
	private ArrayList<TeamAffiliation> teamAffiliations;
	private ArrayList<TeamAffiliation> nationalTeamAffiliations;
	
	private Spieler() {
		teamAffiliations = new ArrayList<>();
		nationalTeamAffiliations = new ArrayList<>();
	}
	
	public Spieler(String data) {
		this();
		fromString(data);
	}
	
	public Spieler(String firstName, String lastName, String popularName, Datum birthDate, String nationality) {
		this();
		setFirstName(firstName);
		setLastName(lastName);
		this.popularName = popularName;
		this.birthDate = birthDate;
		this.nationality = nationality;
		id = generateID();
	}
	
	private int generateID() {
		int birthday = this.birthDate.comparable();
		
		int sumFN = firstName.length(), sumLN = lastName.length();
		
		for (int i = 0; i < firstName.length(); i++) {
			int c = firstName.charAt(i);
			sumFN += c * factorsFN[i % 9];
		}
		for (int i = 0; i < lastName.length(); i++) {
			int c = lastName.charAt(i);
			sumLN += c * factorsLN[i % 9];
		}
		
		int id = 24000000 + (9999 - (birthday % 10000)) * sumFN + ((birthday / 10000 - 1900) * sumLN);
		while(Fussball.getPlayer(id) != null) {
			id++;
		}
		return id;
	}
	
	public int getID() {
		return id;
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
		if (firstName.indexOf("'") != firstName.lastIndexOf("'")) {
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
		String[] lastNameSplit = lastNameFile.split(" ");
		this.lastNameFile = lastNameFile;
		lastName = lastNameFile;
		
		if (lastName.indexOf("'") != lastName.lastIndexOf("'")) {
			lastName = lastName.replace("'", "");
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
	
	public boolean hasUsedSquadNumber(int squadNumber, Mannschaft team, Dauer duration) {
		ArrayList<TeamAffiliation> allAffiliations = getAllAffiliations(team, duration);
		for (TeamAffiliation teamAffiliation : allAffiliations) {
			if (teamAffiliation.getSquadNumber() == squadNumber)	return true;
		}
		return false;
	}
	
	public boolean isEligible(Mannschaft team, Datum date) {
		for (TeamAffiliation affiliation : teamAffiliations) {
			if (affiliation.getDuration().includes(date) && affiliation.getTeam().equals(team))	return true;
		}
		return false;
	}
	
	public void addTeamAffiliation(TeamAffiliation teamAffiliation) {
		if (teamAffiliation == null)	return;
		
		if (teamAffiliation.getTeam().isClub()) {
			for (TeamAffiliation affiliation : teamAffiliations) {
				if (affiliation.equals(teamAffiliation))	return;
				if (teamAffiliation.overlaps(affiliation)) {
					String message = "Der Spieler " + getFullNameShort() + " ist zu einem Zeitpunkt Teil mehrerer Teams:\n";
					message += affiliation.getDuration().withDividers() + ": " + affiliation.getTeam().getName() + "\n";
					message += teamAffiliation.getDuration().withDividers() + ": " + teamAffiliation.getTeam().getName();
					message(message);
					return;
				}
			}
			teamAffiliations.add(teamAffiliation);
		} else {
			for (TeamAffiliation affiliation : nationalTeamAffiliations) {
				if (affiliation.equals(teamAffiliation))	return;
				if (teamAffiliation.overlaps(affiliation)) {
					String message = "Der Spieler " + getFullNameShort() + " ist zu einem Zeitpunkt Teil mehrerer Nationalteams:\n";
					message += affiliation.getDuration().withDividers() + ": " + affiliation.getTeam().getName() + "\n";
					message += teamAffiliation.getDuration().withDividers() + ": " + teamAffiliation.getTeam().getName();
					message(message);
					return;
				}
			}
			nationalTeamAffiliations.add(teamAffiliation);
		}
	}
	
	public ArrayList<TeamAffiliation> getAllAffiliations(Mannschaft team, Dauer duration) {
		ArrayList<TeamAffiliation> affiliations = new ArrayList<>();
		
		for (TeamAffiliation affiliation : (team.isClub() ? teamAffiliations : nationalTeamAffiliations)) {
			if (affiliation.getDuration().overlaps(duration) && affiliation.getTeam().equals(team)) {
				affiliations.add(affiliation);
			}
		}
		
		return affiliations;
	}
	
	public boolean hasConflictingTeamAffiliation(TeamAffiliation changedTeamAffiliation, Dauer duration) {
		for (TeamAffiliation affiliation : (changedTeamAffiliation.getTeam().isClub() ? teamAffiliations : nationalTeamAffiliations)) {
			if (affiliation == changedTeamAffiliation)	continue;
			if (affiliation.getDuration().overlaps(duration))	return true;
		}
		return false;
	}
	
	public void updateInfo(String firstName, String lastName, String popularName, Datum birthDate, String nationality) {
		boolean updateID = !firstName.equals(firstNameFile) || !lastName.equals(lastNameFile) || !birthDate.equals(this.birthDate);
		setFirstName(firstName);
		setLastName(lastName);
		this.popularName = popularName;
		this.birthDate = birthDate;
		this.nationality = nationality;
		if (updateID) {
			id = generateID();
		}
	}
	
	public boolean inOrderBefore(Spieler other) {
		String myName = removeUmlaute(popularName != null ? popularName : lastNameShort).toLowerCase();
		String otherName = removeUmlaute(other.popularName != null ? other.popularName : other.lastNameShort).toLowerCase();
		if (myName.equals(otherName))	return firstName.compareTo(other.firstName) < 0;
		return myName.compareTo(otherName) < 0;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Spieler))	return false;
		Spieler player = (Spieler) other;
		return id == player.id;
	}
	
	public String toString() {
		String toString = id + trennZeichen;
		toString += firstNameFile + trennZeichen;
		toString += lastNameFile + trennZeichen;
		toString += popularName + trennZeichen;
		toString += birthDate.comparable() + trennZeichen;
		toString += nationality + trennZeichen;
		return toString;
	}
	
	private void fromString(String data) {
		String[] split = data.split(trennZeichen);
		int index = 0;
		id = Integer.parseInt(split[index++]);
		setFirstName(split[index++]);
		setLastName(split[index++]);
		popularName = (split[index++].equals("null") ? null : split[index - 1]);
		birthDate = new Datum(split[index++]);
		nationality = split[index++];
	}
}
