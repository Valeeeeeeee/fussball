package model;

import java.io.File;

import javax.swing.JOptionPane;

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
	
	private boolean hasSecondLeg;
	
	/**
	 * [Spieltag][Spiel]
	 */
	public String[][] spielplan;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisse;
	
	public boolean[] spielplanEingetragen;
	public boolean[] ergebnisseEingetragen;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	
	private String dateiErgebnisse;
	private String dateiSpielplanString;
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
	
	public KORunde(Start start, Turnier turnier, int id, String daten) {
		checkOS();
		
		this.start = start;
		this.turnier = turnier;
		this.id = id;
		
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
	
	public Mannschaft[] getMannschaftenIn(int KOround) {
		// TODO Implementierung
		return turnier.getMannschaftenIn(id);
	}
	
	public String getMannschaftsNameAt(int KOround, int matchday, int spiel, int backOrFront) {
		return turnier.getMannschaftsNameAt(KOround, matchday, spiel, backOrFront);
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return this.numberOfMatchesPerMatchday;
	}
	
	public boolean hasSecondLeg() {
		return this.hasSecondLeg;
	}
	
	public int getCurrentMatchday() {
		return turnier.getCurrentMatchday();
		
//		if (this.numberOfMatchdays > 1) {
//			return 0; // TODO getCurrentMatchday
//		} else if (this.numberOfMatchdays == 1) {
//			return 0;
//		} else {
//			return -1;
//		}
	}
	
	public String getDateOf(int matchday, int spiel) {
		return MyDate.datum(turnier.getStartDate(), this.daysSinceFirstDay[matchday][spiel]) + " " + MyDate.uhrzeit(startTime[matchday][spiel]);
	}
	
	public String getDateOf(int KOround, int matchday, int spiel) {
		return turnier.getDateOf(KOround, matchday, spiel);
	}
	
	public Ergebnis getErgebnis(int matchday, int match) {
		return ergebnisse[matchday][match];
	}
	
	public void setErgebnis(int matchday, int match, int goalsLeft, int goalsRight) {
		ergebnisse[matchday][match] = new Ergebnis(goalsLeft, goalsRight);
	}
	
	public void setErgebnis(int matchday, int match, Ergebnis ergebnis) {
		ergebnisse[matchday][match] = ergebnis;
	}
	
	public void ergebnisseSichern() {
		int matchday = spieltag.currentMatchday;
		
		int counter = 0;
		for (int match = 0; match < spieltag.getNumberOfMatches(); match++) {
			Ergebnis result;
			if (!spieltag.tore[match].getText().equals("-1") && !spieltag.tore[match + numberOfMatchesPerMatchday].getText().equals("-1")) {
				result = new Ergebnis(spieltag.tore[match].getText() + ":" + spieltag.tore[match + numberOfMatchesPerMatchday].getText());
				counter += 2;
			} else {
				result = null;
			}
			
			setErgebnis(matchday, match, result);
//			TODO aktuell werden für KO-Phasen Matches keine Ergebnisse an die Mannschaften weitergeleitet
//			mannschaften[spiele[matchday][match][0] - 1].setResult(matchday, result);
//			mannschaften[spiele[matchday][match][1] - 1].setResult(matchday, result);
		}
		
		ergebnisseEingetragen[matchday] = false;
		
		if (spielplanEingetragen[matchday]) {
			if (counter == mannschaften.length)		ergebnisseEingetragen[matchday] = true;
		}
	}
	
	public void laden() {
		dateiErgebnisse = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "Ergebnisse.txt";
		dateiSpielplanString = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "SpielplanString.txt";
		
		// TODO Mit "AF1" als "Mannschaft" und im Spielplan mit Indizes
		dateiMannschaft = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "Mannschaften.txt";
		dateiSpielplan = workspace + File.separator + turnier.getName() + File.separator + name + File.separator + "Spielplan.txt";
		
//    	mannschaftenLaden();
    	
//    	initializeArrays();
    	
    	spielplanLaden();
		ergebnisseLaden();
		
		{
            spieltag = new Spieltag(this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
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
		spielplanFromFile = ausDatei(dateiSpielplanString);
		
		this.countteams = Integer.parseInt(spielplanFromFile[0]);
		
		this.numberOfMatchdays = this.hasSecondLeg ? 2 : 1;
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
		inDatei(dateiSpielplanString, spielplanFromFile);
	}
	
	private void ergebnisseLaden() {
		ergebnisseFromFile = ausDatei(dateiErgebnisse);
		
		String[] ergebnisplanzeilen = new String[this.numberOfMatchdays];
    	this.ergebnisseEingetragen = new boolean[this.numberOfMatchdays];
    	this.ergebnisse = new Ergebnis[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
    	
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
        			setErgebnis(spieltag, match, new Ergebnis(matchresultstring));
        			
        			log(getErgebnis(spieltag, match).home() + " : " + getErgebnis(spieltag, match).away());
        		}
        	}
    		log();
    	}
		
	}
	
	private void ergebnisseSpeichern() {
		inDatei(dateiErgebnisse, ergebnisseFromFile);
	}
	
	public String toString() {
		String alles = name + ";";
		
		return alles;
	}
	
	private void fromString(String daten) {
		// TODO fromString
		this.name = daten.split(";")[0];
		this.hasSecondLeg = false;
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
