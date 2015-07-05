package model;

import java.io.*;
import java.util.ArrayList;

import static util.Utilities.*;

public class Liga {
	private int id = -1;
    private Start start;
	private String name;
	
	private boolean isSummerToSpringSeason;
	private ArrayList<Integer> seasons;
	private ArrayList<LigaSaison> saisons;
	private int aktuelleSaison;
	
	private int numberOfTeams;
	private int numberOfMatchesAgainstSameOpponent;
	
	private int ANZAHL_CL;
	private int ANZAHL_CLQ;
	private int ANZAHL_EL;
	private int ANZAHL_REL;
	private int ANZAHL_ABS;
	private boolean goalDifference;
	
	private Mannschaft[] mannschaften;
	private boolean teamsHaveKader;
	
	private String workspace;
	
	private String dateiSaisonsDaten;
	private ArrayList<String> saisonsDatenFromFile;
    
    private int defaultStarttag;
    private int[] defaultKickoffTimes;
    
    
	public Liga(int id, Start start, String daten) {
		this.start = start;
		
		this.id = id;
		fromString(daten);
		saisonsLaden();
//		this.mannschaften = new Mannschaft[0];
	}
	
	public boolean addSeason(int season, ArrayList<Mannschaft> teams) {
		boolean outOfUse = true;
		if (outOfUse) {
			message("Add season must be refactored, variables used are not correctly accessed (old implementation required configuration to be identical)");
			// TODO refactor
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

	private String teamsToString() {
		String alles = "";
		
		for (int i = 0; i < numberOfTeams; i++) {
			alles = alles + mannschaften[i].toString() + "\n";
		}
		
		return alles;
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
            hilfsarray[i] = this.seasons.get(i) + (this.isSummerToSpringSeason ? "/" + (this.seasons.get(i) + 1) : "");
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
	
	private void initDefaultKickoffTimes(String DKTAsString) {
		// kommt als 0,1,1,1,1,1,2,3,4
		String[] DKTValues = DKTAsString.split(",");
		defaultKickoffTimes = new int[DKTValues.length];
		for (int i = 0; i < DKTValues.length; i++) {
			defaultKickoffTimes[i] = Integer.parseInt(DKTValues[i]);
		}
	}
	
	private String getDefaultKickoffTimes() {
		String dktimes = "";
		if (defaultKickoffTimes.length >= 1) {
			dktimes += defaultKickoffTimes[0];
			for (int i = 1; i < defaultKickoffTimes.length; i++) {
				dktimes += "," + defaultKickoffTimes[i];
			}
		}
		
		return dktimes;
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
		rueckgabe = rueckgabe + "D_ST*" + this.defaultStarttag +";";
		rueckgabe = rueckgabe + "DKT*" + getDefaultKickoffTimes() + ";";
		rueckgabe = rueckgabe + "ISSTSS*" + this.isSummerToSpringSeason + ";";
		rueckgabe = rueckgabe + "A_MS*" + this.numberOfTeams + ";";
		rueckgabe = rueckgabe + "A_SGDG*" + this.numberOfMatchesAgainstSameOpponent + ";";
		rueckgabe = rueckgabe + "A_CL*" + this.ANZAHL_CL + ";";
		rueckgabe = rueckgabe + "A_CLQ*" + this.ANZAHL_CLQ + ";";
		rueckgabe = rueckgabe + "A_EL*" + this.ANZAHL_EL + ";";
		rueckgabe = rueckgabe + "A_REL*" + this.ANZAHL_REL + ";";
		rueckgabe = rueckgabe + "A_ABS*" + this.ANZAHL_ABS + ";";
		rueckgabe = rueckgabe + "GLDIF*" + this.goalDifference + ";";
		rueckgabe = rueckgabe + "KADER*" + this.teamsHaveKader + ";";
		rueckgabe += getSeasonsRepresentation() + ";";
		
		return rueckgabe;
	}
	
	private void fromString(String daten) {
		this.name = daten.substring(daten.indexOf("NAME*") + 5, daten.indexOf(";D_ST*"));
		this.defaultStarttag = Integer.parseInt(daten.substring(daten.indexOf("D_ST*") + 5, daten.indexOf(";DKT*")));
		initDefaultKickoffTimes(daten.substring(daten.indexOf("DKT*") + 4, daten.indexOf(";ISSTSS*")));
		this.isSummerToSpringSeason = Boolean.parseBoolean(daten.substring(daten.indexOf("ISSTSS*") + 7, daten.indexOf(";A_MS*")));
		this.numberOfTeams = Integer.parseInt(daten.substring(daten.indexOf("A_MS*") + 5, daten.indexOf(";A_SGDG*")));
		this.numberOfMatchesAgainstSameOpponent = Integer.parseInt(daten.substring(daten.indexOf("A_SGDG*") + 7, daten.indexOf(";A_CL*")));
		this.ANZAHL_CL = Integer.parseInt(daten.substring(daten.indexOf("A_CL*") + 5, daten.indexOf(";A_CLQ*")));
		this.ANZAHL_CLQ = Integer.parseInt(daten.substring(daten.indexOf("A_CLQ*") + 6, daten.indexOf(";A_EL*")));
		this.ANZAHL_EL = Integer.parseInt(daten.substring(daten.indexOf("A_EL*") + 5, daten.indexOf(";A_REL*")));
		this.ANZAHL_REL = Integer.parseInt(daten.substring(daten.indexOf("A_REL*") + 6, daten.indexOf(";A_ABS*")));
		this.ANZAHL_ABS = Integer.parseInt(daten.substring(daten.indexOf("A_ABS*") + 6, daten.indexOf(";GLDIF*")));
		this.goalDifference = Boolean.parseBoolean(daten.substring(daten.indexOf("GLDIF*") + 6, daten.indexOf(";KADER*")));
		this.teamsHaveKader = Boolean.parseBoolean(daten.substring(daten.indexOf("KADER*") + 6, daten.indexOf(";S")));
		
		this.seasons = getSeasonsFromRepresentation(daten.substring(daten.indexOf(";S") + 1));
	}
}
