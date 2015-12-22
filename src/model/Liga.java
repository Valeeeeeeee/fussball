package model;

import java.io.*;
import java.util.ArrayList;

import static util.Utilities.*;

public class Liga {
	private int id = -1;
	private String name;
	
	private ArrayList<LigaSaison> saisons;
	private int aktuelleSaison;
	
	private String workspace;
	
	private String dateiSaisonsDaten;
	private ArrayList<String> saisonsDatenFromFile;
	
	
	public Liga(int id, String daten) {
		this.id = id;
		fromString(daten);
		saisonsLaden();
	}
	
	public boolean addSeason(String toString, ArrayList<String> teams, String KOTRepresentation) {
		LigaSaison neueSaison = new LigaSaison(this, saisons.size(), toString);
		for (int i = 0; i < saisons.size(); i++) {
			if (saisons.get(i).getYear() == neueSaison.getYear()) {
				message("Eine Saison mit diesem Startjahr existiert bereits.");
				return false;
			}
		}
		saisons.add(neueSaison);
		
		String folder = workspace + neueSaison.getSeasonFull("_") + File.separator;
		(new File(folder)).mkdirs();
		
		String dateiErgebnisse = folder + "Ergebnisse.txt";
		String dateiSpieldaten = folder + "Spieldaten.txt";
		String dateiSpielplan = folder + "Spielplan.txt";
		String dateiTeams = folder + "Mannschaften.txt";
		ArrayList<String> ergebnisplan = new ArrayList<>();
		ArrayList<String> spieldaten = new ArrayList<>();
		ArrayList<String> spielplan = new ArrayList<>();
		ArrayList<String> teamsNames = new ArrayList<>();

		int numberOfMatchdays = neueSaison.getNumberOfMatchesAgainstSameOpponent() * (2 * ((teams.size() + 1) / 2) - 1);
		int numberOfMatchesPerMatchday = teams.size() / 2;
		
		String allF = "", allNull = "";
		for (int i = 0; i < numberOfMatchesPerMatchday; i++) {
			allF += "f";
			allNull += "null;";
		}
		
		spielplan.add(KOTRepresentation);
		for (int i = 0; i < numberOfMatchdays; i++) {
			ergebnisplan.add(allF);
			spielplan.add(allF);
			spieldaten.add(allNull);
		}
		
		teamsNames.add("" + teams.size());
		for (int i = 0; i < teams.size(); i++) {
			teamsNames.add(teams.get(i));
		}
		
		inDatei(dateiErgebnisse, ergebnisplan);
		inDatei(dateiSpieldaten, spieldaten);
		inDatei(dateiSpielplan, spielplan);
		inDatei(dateiTeams, teamsNames);
		
		return true;
	}
	
	public int[] checkMissingResults() {
		int countCompleted = 0, countStillRunning = 0;
		long now = 10000L * MyDate.newMyDate() + MyDate.newMyTime();
		for (LigaSaison season : saisons) {
			String fileName = season.getWorkspace() + "nextMatches.txt";
			ArrayList<String> nextMatchesString = ausDatei(fileName, false);
			if (nextMatchesString.size() > 0) {
				for (int i = 0; i < nextMatchesString.size(); i++) {
					long match = Long.parseLong(nextMatchesString.get(i));
					if (match <= now) {
						boolean hourPassed = match % 100 >= now % 100;
						int dayDiff = MyDate.difference((int) match / 10000, (int) now / 10000);
						long diff = (now % 10000) - (match % 10000) + dayDiff * 2400 - (hourPassed ? 40 : 0);
						diff = (diff / 100) * 60 + diff % 100;
						if (diff < 105)	countStillRunning++;
						else			countCompleted++;
					}
				}
			}
		}
		
		return new int[] {countCompleted, countStillRunning};
	}
	
	public void laden(int index) {
		aktuelleSaison = index;
		saisons.get(aktuelleSaison).laden();
	}
	
	public void speichern() {
		saisonsSpeichern();
	}

	public String getWorkspace() {
		return this.workspace;
	}
	
	/**
	 * This method returns all available seasons in the format "2014/2015" if isSTSS, otherwise "2014".
	 * @return a String array containing all available seasons
	 */
	public String[] getAllSeasons() {
		String[] hilfsarray = new String[this.saisons.size()];
		for (int i = 0; i < this.saisons.size(); i++) {
			hilfsarray[i] = this.saisons.get(i).getSeasonFull("/");
		}
		return hilfsarray;
	}
	
	public int getAktuelleSeason() {
		return this.saisons.get(this.aktuelleSaison).getYear();
	}
	
	public LigaSaison getAktuelleSaison() {
		return this.saisons.get(this.aktuelleSaison);
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	private void saisonsLaden() {
		workspace = Start.getInstance().getWorkspace() + File.separator + name + File.separator;
		
		// SaisonsConfig.txt
		dateiSaisonsDaten = workspace + "SaisonsConfig.txt";
		saisonsDatenFromFile = ausDatei(dateiSaisonsDaten);
		
		// LigaSaisons erstellen
		saisons = new ArrayList<>();
		for (int i = 0; i < saisonsDatenFromFile.size(); i++) {
			saisons.add(new LigaSaison(this, i, saisonsDatenFromFile.get(i)));
		}
	}
	
	private void saisonsSpeichern() {
		saisonsDatenFromFile.clear();
		for (int i = 0; i < saisons.size(); i++) {
			saisons.get(i).speichern();
			saisonsDatenFromFile.add(saisons.get(i).toString());
		}
		
		inDatei(dateiSaisonsDaten, saisonsDatenFromFile);
	}
	
	public String toString() {
		String rueckgabe = this.name;
		return rueckgabe;
	}
	
	private void fromString(String daten) {
		String[] split = daten.split(";");
		int index = 0;
		
		this.name = split[index++];
	}
}
