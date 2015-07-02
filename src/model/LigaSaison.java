package model;

import static util.Utilities.ausDatei;
import static util.Utilities.inDatei;

import java.io.File;
import java.util.ArrayList;

public class LigaSaison {
	
	private Start start;
	private Liga liga;
	private boolean isSummerToSpringSeason;
	private int seasonIndex;
	private int season;
	private boolean goalDifference;
	private boolean teamsHaveKader;
	
	private int numberOfTeams;
	private int numberOfMatchdays;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	
	private int[] anzahl;
	
	private int[][] datesAndTimes;
	
	private Spiel[][] spielplan;
	private boolean[][] spielplanEingetragen;
	
	private Ergebnis[][] ergebnisplan;
	private boolean[][] ergebnisplanEingetragen;

	private boolean geladen;
	private String workspace;
	
	private String dateiMannschaften;
	private ArrayList<String> mannschaftenFromFile;
	
	private String dateiSpielplan;
	private ArrayList<String> spielplanFromFile;
	
	private String dateiErgebnisse;
	private ArrayList<String> ergebnisseFromFile;
	
	private String dateiSpieldaten;
	private ArrayList<String> spieldatenFromFile;
	
	public LigaSaison(Start start, Liga liga, int seasonIndex, String data) {
		this.start = start;
		this.liga = liga;
		this.seasonIndex = seasonIndex;
		fromString(data);
	}
	
	public int getSeasonIndex() {
		return seasonIndex;
	}
	
	public String getSeasonFull(String trennzeichen) {
		return season + (isSummerToSpringSeason ? trennzeichen + (season + 1) : "");
	}
	
	public int getAnzahl(int index) {
		return anzahl[index];
	}
	
	private void initializeArrays() {
    	// Alle Array werden initialisiert
		this.datesAndTimes = new int[this.numberOfMatchdays][this.numberOfMatchesPerMatchday + 1];
        this.spielplan = new Spiel[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.ergebnisplan = new Ergebnis[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.spielplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.ergebnisplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
    }
	
	private String getAnzahlRepresentation() {
		String representation = "", sep = "";
		
		for (int i = 0; i < anzahl.length; i++) {
			representation += sep + anzahl[i];
			sep = ",";
		}
		
		return representation;
	}
	
	private int[] getAnzahlFromString(String anzahlen) {
		String[] anzSplit = anzahlen.split(",");
		int[] anzahl = new int[anzSplit.length];
		
		for (int i = 0; i < anzahl.length; i++) {
			anzahl[i] = Integer.parseInt(anzSplit[i]);
		}
		
		return anzahl;
	}
	
	public void mannschaftenLaden() {
//		mannschaftenFromFile = ausDatei(dateiMannschaften);
	}
	
	public void mannschaftenSpeichern() {
//		mannschaftenFromFile.clear();
		
//		inDatei(dateiMannschaften, mannschaftenFromFile);
	}
	
	public void spielplanLaden() {
//		spielplanFromFile = ausDatei(dateiSpielplan);
	}
	
	public void spielplanSpeichern() {
//		spielplanFromFile.clear();
		
//		inDatei(dateiSpielplan, spielplanFromFile);
	}
	
	public void ergebnisseLaden() {
//		ergebnisseFromFile = ausDatei(dateiErgebnisse);
	}
	
	public void ergebnisseSpeichern() {
//		ergebnisseFromFile.clear();
		
//		inDatei(dateiErgebnisse, ergebnisseFromFile);
	}
	
	public void spieldatenLaden() {
//		spieldatenFromFile = ausDatei(dateiSpieldaten);
	}
	
	public void spieldatenSpeichern() {
//		spieldatenFromFile.clear();
		
//		inDatei(dateiSpieldaten, spieldatenFromFile);
	}
	
	public void laden() {
		workspace = liga.getWorkspace() + getSeasonFull("_") + File.separator;
		
		dateiErgebnisse = workspace + "Ergebnisse.txt";
        dateiSpieldaten = workspace + "Spieldaten.txt";
        dateiSpielplan = workspace + "Spielplan.txt";
    	dateiMannschaften = workspace + "Mannschaften.txt";
    	
		mannschaftenLaden();
		initializeArrays();
		
		spielplanLaden();
		ergebnisseLaden();
		spieldatenLaden();
		
		geladen = true;
	}
	
	public void speichern() {
		if (!geladen)	return;
		
		mannschaftenSpeichern();
		spielplanSpeichern();
		ergebnisseSpeichern();
		spieldatenSpeichern();
		
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += season + ";";
		toString += isSummerToSpringSeason + ";";
		toString += numberOfTeams + ";";
		toString += numberOfMatchesAgainstSameOpponent + ";";
		
//		toString += defaultStarttag +";";
//		toString += getDefaultKickoffTimes() + ";";
		
		toString += goalDifference + ";";
		toString += teamsHaveKader + ";";
		toString += getAnzahlRepresentation() + ";";
		
		return toString;
	}
	
	private void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		season = Integer.parseInt(split[index++]);
		isSummerToSpringSeason = Boolean.parseBoolean(split[index++]);
		numberOfTeams = Integer.parseInt(split[index++]);
		numberOfMatchesAgainstSameOpponent = Integer.parseInt(split[index++]);
		goalDifference = Boolean.parseBoolean(split[index++]);
		teamsHaveKader = Boolean.parseBoolean(split[index++]);
		anzahl = getAnzahlFromString(split[index++]);
	}
}
