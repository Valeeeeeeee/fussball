package model;

import java.util.ArrayList;

public class Schiedsrichter {

	private String trennZeichen = ";";
	
	private int id;
	
	private String firstName;
	private String lastName;
	private Datum birthDate;
	private String nationality;
	private ArrayList<Spiel> matches = new ArrayList<>();
	
	public Schiedsrichter(int id, String firstName, String lastName, Datum birthDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
	}
	
	public Schiedsrichter(int id, String data) {
		this.id = id;
		fromString(data);
	}
	
	public int getID() {
		return id;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Datum getBirthDate() {
		return birthDate;
	}
	
	public void addMatch(Spiel match) {
		int index = 0;
		for (int i = 0; i < matches.size(); i++) {
			if (matches.get(i).getKickOffTime().isBefore(match.getKickOffTime()))	index++;
		}
		matches.add(index, match);
	}
	
	public void sortMatches() {
		ArrayList<Spiel> copy = new ArrayList<>();
		for (Spiel spiel : matches) {
			copy.add(spiel);
		}
		matches.clear();
		for (Spiel spiel : copy) {
			addMatch(spiel);
		}
	}
	
	private void fromString(String data) {
		String[] dataSplit = data.split(trennZeichen);
		int index = 0;
		
		firstName = dataSplit[index++];
		lastName = dataSplit[index++];
		birthDate = new Datum(dataSplit[index++]);
		nationality = dataSplit[index++];
	}
	
	public String toString() {
		String toString = "";
		
		toString += firstName + trennZeichen;
		toString += lastName + trennZeichen;
		toString += birthDate.comparable() + trennZeichen;
		toString += nationality + trennZeichen;
		
		return toString;
	}
}
