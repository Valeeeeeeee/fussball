package model;

import java.io.*;
import java.util.ArrayList;

import static util.Utilities.*;

public class Liga {
	private int id = -1;
    private Start start;
	private String name;
	
	private ArrayList<Integer> seasons;
	private ArrayList<LigaSaison> saisons;
	private int aktuelleSaison;
	
	private String workspace;
	
	private String dateiSaisonsDaten;
	private ArrayList<String> saisonsDatenFromFile;
    
    
	public Liga(int id, Start start, String daten) {
		this.start = start;
		
		this.id = id;
		fromString(daten);
		saisonsLaden();
	}
	
	public boolean addSeason(int season, ArrayList<Mannschaft> teams) {
		boolean outOfUse = true;
		if (outOfUse) {
			message("Add season must be refactored, variables used are not correctly accessed (old implementation required configuration to be identical)");
			// TODO refactor: create a new type like new league dialog
			// add radiobuttons, textfields etc and fill them with data of previous season, so that if no changes are necessary, user has to do nothing
			// move currently local fields to signature as parameters
			return false;
		}
		
		LigaSaison lastSeason = saisons.get(aktuelleSaison);
		boolean isSummerToSpringSeason = true;
		int numberOfMatchesAgainstSameOpponent = 2;
		int numberOfTeams = teams.size();
		int numberOfMatchesPerMatchday = numberOfTeams / 2;
		int numberOfMatchdays = numberOfMatchesAgainstSameOpponent * (2 * ((numberOfTeams + 1) / 2) - 1);
		
		try {
			speichern();
		} catch (Exception e) {}
		
		int index = 0;
		for (int i = 0; i < seasons.size(); i++) {
			if (season == seasons.get(i)) {
				message("A season for that year already exists.");
				return false;
			} else if (season > seasons.get(i))	index++;
		}
		seasons.add(index, season);
		aktuelleSaison = index;
		
		String saison = "" + seasons.get(aktuelleSaison);
		if (isSummerToSpringSeason)	saison += "_" + (seasons.get(aktuelleSaison) + 1);
		
        String folder = workspace + saison + File.separator;
        (new File(folder)).mkdirs();
        
        String dateiErgebnisse = folder + "Ergebnisse.txt";
        String dateiSpieldaten = folder + "Spieldaten.txt";
        String dateiSpielplan = folder + "Spielplan.txt";
        String dateiTeams = folder + "Mannschaften.txt";
        ArrayList<String> ergebnisplan = new ArrayList<>();
        ArrayList<String> spieldaten = new ArrayList<>();
        ArrayList<String> spielplan = new ArrayList<>();
        ArrayList<String> teamsNames = new ArrayList<>();
        
        String allF = "", allNull = "";
        for (int i = 0; i < numberOfMatchesPerMatchday; i++) {
			allF += "f";
			allNull += "null;";
		}
        
        int numberOfDKOT = lastSeason.getDefaultKickoffTime(numberOfMatchesPerMatchday - 1) + 1;
        String defaultKickOffTimes = numberOfDKOT + ";";
        for (int i = 0; i < numberOfDKOT; i++) {
        	defaultKickOffTimes += lastSeason.getDaysSinceST(i) + "," + lastSeason.getKickoffTimes(i) + ";";
		}
        
        spielplan.add(defaultKickOffTimes);
        for (int i = 0; i < numberOfMatchdays; i++) {
        	ergebnisplan.add(allF);
        	spielplan.add(allF);
        	spieldaten.add(allNull);
		}
        
        teamsNames.add("" + teams.size());
        for (int i = 0; i < teams.size(); i++) {
        	teamsNames.add(teams.get(i).toString());
		}
        
        inDatei(dateiErgebnisse, ergebnisplan);
        inDatei(dateiSpieldaten, spieldaten);
        inDatei(dateiSpielplan, spielplan);
        inDatei(dateiTeams, teamsNames);
        
        return true;
	}
	
	public void laden(int index) {
		aktuelleSaison = index;
		saisons.get(aktuelleSaison).laden();
    }
	
	public void speichern() throws Exception {
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
		String[] hilfsarray = new String[this.seasons.size()];
        for (int i = 0; i < this.seasons.size(); i++) {
            hilfsarray[i] = this.seasons.get(i) + (saisons.get(i).isSTSS() ? "/" + (this.seasons.get(i) + 1) : "");
        }
        return hilfsarray;
	}
	
	public String getSeasonsRepresentation() {
		String representation = "";
		for (int i = 0; i < this.seasons.size(); i++) {
			String trenn = "S" + i;
			representation += trenn + "*" + this.seasons.get(i) + "*" + trenn + ",";
		}
		return representation.substring(0, representation.length() - 1);
	}
	
	public ArrayList<Integer> getSeasonsFromRepresentation(String representation) {
		String[] seasonsReps = representation.split(",");
		
		ArrayList<Integer> seasons = new ArrayList<>();
		
		for (int i = 0; i < seasonsReps.length; i++) {
			String rep = seasonsReps[i];
			seasons.add(Integer.parseInt(rep.substring(rep.indexOf("*") + 1, rep.lastIndexOf("*"))));
		}
		
		return seasons;
	}
	
	public int getAktuelleSeason() {
		return this.seasons.get(this.aktuelleSaison);
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
		workspace = start.getWorkspace() + File.separator + name + File.separator;
		
		// SaisonsConfig.txt
		dateiSaisonsDaten = workspace + "SaisonsConfig.txt";
		saisonsDatenFromFile = ausDatei(dateiSaisonsDaten);
		
		// LigaSaisons erstellen
		saisons = new ArrayList<>();
		for (int i = 0; i < saisonsDatenFromFile.size(); i++) {
			saisons.add(new LigaSaison(start, this, i, saisonsDatenFromFile.get(i)));
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
		String rueckgabe = "NAME*" + this.name + ";";
		rueckgabe += getSeasonsRepresentation() + ";";
		
		return rueckgabe;
	}
	
	private void fromString(String daten) {
		this.name = daten.substring(daten.indexOf("NAME*") + 5, daten.indexOf(";S"));
		this.seasons = getSeasonsFromRepresentation(daten.substring(daten.indexOf(";S") + 1));
	}
}
