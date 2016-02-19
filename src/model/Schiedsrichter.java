package model;

import java.util.ArrayList;

public class Schiedsrichter {

	private String trennZeichen = ";";
	
	private int id;
	
	private String firstName;
	private String lastName;
	private int birthDate;
	private ArrayList<Spiel> matches = new ArrayList<>();
	
	public Schiedsrichter(int id, String firstName, String lastName, int birthDate) {
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
	
	public int getBirthDate() {
		return birthDate;
	}
	
	public void addMatch(Spiel match) {
		int index = 0;
		for (int i = 0; i < matches.size(); i++) {
			if (matches.get(i).getDate() < match.getDate() || match.getDate() == 0)	index++;
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
		birthDate = Integer.parseInt(dataSplit[index++]);
	}
	
	public String toString() {
		String toString = "";
		
		toString += firstName + trennZeichen;
		toString += lastName + trennZeichen;
		toString += birthDate + trennZeichen;
		
		return toString;
	}
}
