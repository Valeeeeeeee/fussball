package model;

import java.io.File;

import static util.Utilities.*;

public class KORunde {

	private Start start;
	private Turnier turnier;
	private int id;
	private String name;
	private String shortName;
	
	private int countteams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchdays;
	public Mannschaft[] mannschaften;
	
	private boolean secondLeg;
	
	/**
	 * [Spieltag][Spiel]
	 */
	public String[][] spielplan;
	/**
	 * [Spieltag][spielIndex][vorne/hinten]
	 */
//	private int[][][] ergebnisse;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisseNeu;
	
	public boolean[] spielplanEingetragen;
	public boolean[] ergebnisseEingetragen;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
	
	
	private String dateiErgebnisse;
	private String dateiSpielplan;
	private String dateiMannschaft;
	
	private String[] teamsFromFile;
	private String[] spielplanFromFile;
	private String[] ergebnisseFromFile;
	
	/**
	 * [Spieltag][spielIndex]
	 */
    private int[][] daysSinceFirstDay;
    /**
	 * [Spieltag][spielIndex]
	 */
    private int[][] startTime;
    
    private Spieltag spieltag;
	
	public KORunde(Start start, Turnier turnier, int id, String name, String daten) {
		checkOS();
		
		this.start = start;
		this.turnier = turnier;
		this.id = id;
		this.name = name;
		
		fromString(daten);
		
		laden();
	}

	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Spieltag getSpieltag() {
		return this.spieltag;
	}
	
	public int getErgebnis(int matchday, int match, int backOrFront) {
		return ergebnisse[matchday][match][backOrFront];
	}
	
	public void setErgebnis(int matchday, int match, int backOrFront, int goals) {
		ergebnisse[matchday][match][backOrFront] = goals;
	}
	
	public void setErgebnisNew(int matchday, int match, int goalsLeft, int goalsRight) {
		ergebnisse[matchday][match][0] = goalsLeft;
		ergebnisse[matchday][match][1] = goalsRight;
	}
	
	public void laden() {
		dateiErgebnisse = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "Ergebnisse.txt";
		dateiSpielplan = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "Spielplan.txt";
		dateiMannschaft = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "Mannschaften.txt";
		
//    	mannschaftenLaden();
    	
//    	initializeArrays();
    	
    	spielplanLaden();
		ergebnisseLaden();
	}
	
	public void speichern() {
		this.spielplanSpeichern();
		this.ergebnisseSpeichern();
		this.mannschaftenSpeichern();
	}
	
	private void initializeArrays() {
		
	}
	
	private void mannschaftenLaden() {
		teamsFromFile = ausDatei(dateiMannschaft);
		
	}
	
	private void mannschaftenSpeichern() {
		inDatei(dateiMannschaft, teamsFromFile);
	}
	
	private void spielplanLaden() {
		spielplanFromFile = ausDatei(dateiSpielplan);
		
		this.countteams = Integer.parseInt(spielplanFromFile[0]);
		
		this.numberOfMatchdays = this.spielplanFromFile.length - 1;
		this.numberOfMatchesPerMatchday = countteams / 2;
    	String[] spielplanzeilen = new String[numberOfMatchdays];
    	this.spielplanEingetragen = new boolean[numberOfMatchdays];
    	
    	
        this.daysSinceFirstDay = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
        this.startTime = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
        this.spielplan = new String[numberOfMatchdays][numberOfMatchesPerMatchday];
    	
    	log("\nDies ist der " + this.name + "-Spielplan:");
    	for (int spieltag = 0; spieltag < (spielplanFromFile.length - 1); spieltag++) {
    		spielplanzeilen[spieltag] = spielplanFromFile[spieltag + 1];
    		log(spielplanzeilen[spieltag]);
    		
    		
        	String[] spielplanzellen = spielplanzeilen[spieltag].split(";");
        	spielplanEingetragen[spieltag] = Boolean.parseBoolean(spielplanzellen[0]);
        	log(spielplanEingetragen[spieltag]);
        	
        	if (spielplanEingetragen[spieltag]) {
        		// Spieltagsdaten
        		String[] spiele = spielplanzellen[1].split(":");
        		
        		for (int match = 0; match < spiele.length; match++) {
        			String datenUndZeiten[] = spiele[match].split(",");

        			daysSinceFirstDay[spieltag][match] = Integer.parseInt(datenUndZeiten[0]);
        			startTime[spieltag][match] = Integer.parseInt(datenUndZeiten[1]);
        			
        			log(daysSinceFirstDay[spieltag][match] + " , " + startTime[spieltag][match]);
        		}
        		
        		// Mannschaften bzw. vorangegangenes Spiel
        		for (int match = 0; (match + 2) < spielplanzellen.length; match++) {
        			spielplan[spieltag][match] = spielplanzellen[match + 2];
        			log(spielplan[spieltag][match]);
        		}
        	}
        	log();
    	}
	}
	
	private void spielplanSpeichern() {
		inDatei(dateiSpielplan, spielplanFromFile);
	}
	
	private void ergebnisseLaden() {
		ergebnisseFromFile = ausDatei(dateiErgebnisse);
		
		String[] ergebnisplanzeilen = new String[this.numberOfMatchdays];
    	this.ergebnisseEingetragen = new boolean[this.numberOfMatchdays];
    	this.ergebnisse = new int[this.numberOfMatchdays][this.numberOfMatchesPerMatchday][2];
    	
    	log("Dies ist der " + this.name + "-Ergebnisplan:");
    	for (int spieltag = 0; spieltag < ergebnisseFromFile.length; spieltag++) {
    		ergebnisplanzeilen[spieltag] = ergebnisseFromFile[spieltag];
    		log(ergebnisplanzeilen[spieltag]);
    		
        	String[] ergebnisplanzellen = ergebnisplanzeilen[spieltag].split(";");
        	ergebnisseEingetragen[spieltag] = Boolean.parseBoolean(ergebnisplanzellen[0]);
        	log(ergebnisseEingetragen[spieltag]);
        	
        	if (ergebnisseEingetragen[spieltag]) {
        		String matchresultstring;
        		
        		// Mannschaften bzw. vorangegangenes Spiel
        		for (int match = 0; (match + 1) < ergebnisplanzellen.length; match++) {
        			matchresultstring = ergebnisplanzellen[match + 1];
        			setErgebnis(spieltag, match, 0, Integer.parseInt(matchresultstring.split(":")[0]));
        			setErgebnis(spieltag, match, 1, Integer.parseInt(matchresultstring.split(":")[1]));
        			log(getErgebnis(spieltag, match, 0) + " : " + getErgebnis(spieltag, match, 1));
        		}
        	}
    		log();
    	}
		
	}
	
	private void ergebnisseSpeichern() {
		inDatei(dateiErgebnisse, ergebnisseFromFile);
	}
	
	public String toString() {
		String alles = "";
		
		return alles;
	}
	
	private void fromString(String daten) {
		
	}
	
	public void checkOS() {
		if (new File(workspaceWIN).isDirectory()) {
//			JOptionPane.showMessageDialog(null, "You are running Windows.");
			workspace = workspaceWIN;
		} else if (new File(workspaceMAC).isDirectory()) {
//			JOptionPane.showMessageDialog(null, "You have a Mac.");
			workspace = workspaceMAC;
		} else {
//			JOptionPane.showMessageDialog(null, "You are running neither OS X nor Windows, probably Linux!");
			workspace = null;
		}
	}
}
