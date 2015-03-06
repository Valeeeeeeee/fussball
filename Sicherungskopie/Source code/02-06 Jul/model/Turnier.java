package model;
import java.io.File;

import static util.Utilities.*;

public class Turnier {
	
	private int id;
	private Start start;
	
	private String name;
	
	private Mannschaft[] mannschaften;
	private Gruppe[] gruppen;
	
	private int numberOfTeams;
	private int numberOfGroups;
	private int numberOfTeamsPerGroup;
	private int numberOfKORounds;
	
	String workspace;
	String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
	
	
	String dateiErgebnisse;
	String dateiSpielplan;
	String dateiTeams;
	
	String[] teamsFromFile;
	String[] spielplanFromFile;
	String[] ergebnisseFromFile;
	
	/**
	 * Indizes der Mannschaften [KORunde][spiel] zB: "AF1:AF2"
	 */
	private String[][] spielplan;
	private Mannschaft[][][] spielplanMannschaften;
	private boolean[] spielplanEingetragen;
	/**
	 * [KORunde][spielIndex][vorne/hinten]
	 */
	private int[][][] ergebnisse;
	private boolean[] ergebnisseEingetragen;
	
	char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
	String[] KOrounds = new String[] {"AF", "VF", "HF", "P3", "FI"};
	
	private int startDate;
	private int finalDate;
    private int[][] daysSinceFirstDay;
    private int[][] startTime;
	
	public Turnier(Start start, int id, String daten) {
		checkOS();
		this.start = start;
		fromString(daten);
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNumberOfGroups() {
		return this.numberOfGroups;
	}
	
	public Gruppe[] getGruppen() {
		return this.gruppen;
	}
	
	public int getStartDate() {
		return this.startDate;
	}
	
	public int getFinalDate() {
		return this.finalDate;
	}
	
	public int getNumberOfKORounds() {
		return this.numberOfKORounds;
	}
	
	private void testGetMatchInRound() {
		
		Mannschaft ms1;
		Mannschaft ms2;

		log("\n\n\ntestGetMatchInRound");
		log("==============================");
		String[] matches = new String[] {"VF1", "VF2", "VF3", "VF4", "HF1", "HF2", "FI1"};
//		String[] matches = new String[] {"P31", "AF6", "VF4", "HF1", "HF2", "AF4"};
		for (int i = 0; i < matches.length; i++) {
			ms1 = null;
			ms2 = null;
			logWONL(matches[i] + ": ");
			log(getMatchInRound(matches[i].substring(0, 2), Integer.parseInt(matches[i].substring(2, 3))));
			try {
				ms1 = getMannschaftAt(matches[i], true);
				logWONL(ms1.getName() + "  :  ");
			} catch (Exception e) {
				error("Exception at match " + matches[i] + ": " + "Die gesuchte Mannschaft kann aktuell nicht angezeigt werden!");
			}
			try {
				ms2 = getMannschaftAt(matches[i], false);
				log(ms2.getName());
			} catch (Exception e) {
				error("Exception at match " + matches[i] + ": " + "Die gesuchte Mannschaft kann aktuell nicht angezeigt werden!");
			}
			log("\n\n");
		}
		
		log("\n\nLosers of the following rounds");
		log("==============================");
		
		matches = new String[] {"AF1", "AF2", "AF3", "AF4", "AF5", "AF6", "AF7", "AF8", "VF2"};
		for (int i = 0; i < matches.length; i++) {
			logWONL(matches[i] + ": ");
			logWONL(mannschaftFromString(searchForWinnerOf(matches[i].substring(0, 2), Integer.parseInt(matches[i].substring(2, 3)))).getName());
			logWONL(" defeated ");
			log(mannschaftFromString(searchForLoserOf(matches[i].substring(0, 2), Integer.parseInt(matches[i].substring(2, 3)))).getName());
		}
		
	}
	
	/**
	 * 
	 * @param matchIndex 1 bis X
	 * @param round String representing the round (AF, VF, HF, P3, FI)
	 * @return
	 */
	public String getMatchInRound(String round, int matchIndex) {
		int roundIndex = -1;
		for (int i = 0; i < KOrounds.length; i++) {
			if (round.equals(KOrounds[i]))	roundIndex = i;
		}

		if (roundIndex == -1)	return null;
		if (spielplan[roundIndex].length < matchIndex)		return null;

		return spielplan[roundIndex][matchIndex - 1];
	}
	
	/**
	 * 
	 * @param round Das Spiel, das die gesuchte Mannschaft gewonnen haben muss
	 * @return
	 */
	public String searchForWinnerOf(String round, int indexOfMatch) {
		
		// Bestimmung der Runde
		int roundIndex = -1;
		for (int i = 0; i < KOrounds.length; i++) {
			if (round.equals(KOrounds[i]))	roundIndex = i;
		}
//		log("Searching for the winner of round  " + round + ", indexOfMatch =  "+ indexOfMatch + ", index of round ==  " + roundIndex + "...");
		
		if (roundIndex == -1)		return round + indexOfMatch;
		
		// Abfrage, ob das Spiel bereits einen Gewinner hat
		if (ergebnisseEingetragen[roundIndex]) {
//			log("For this round results have been entered: " + ergebnisse[roundIndex][indexOfMatch - 1][0] + ":" + ergebnisse[roundIndex][indexOfMatch - 1][1]);
			String winner;
			if (ergebnisse[roundIndex][indexOfMatch - 1][0] > ergebnisse[roundIndex][indexOfMatch - 1][1]) {
				winner = spielplan[roundIndex][indexOfMatch - 1].split(":")[0];
			} else {
				winner = spielplan[roundIndex][indexOfMatch - 1].split(":")[1];
			}
			return searchForWinnerOf(winner.substring(0, 2), Integer.parseInt(winner.substring(2)));
		}
		
		error("    Results have not been entered yet for this match.");
		return round + indexOfMatch;
	}
	
	public String searchForLoserOf(String round, int indexOfMatch) {
		
		// Bestimmung der Runde
		int roundIndex = -1;
		for (int i = 0; i < KOrounds.length; i++) {
			if (round.equals(KOrounds[i]))	roundIndex = i;
		}
//		log("Searching for the loser of round  " + round + ", indexOfMatch =  "+ indexOfMatch + ", index of round ==  " + roundIndex + "...");
		
		if (roundIndex == -1)		return round + indexOfMatch;
		
		// Abfrage, ob das Spiel bereits einen Gewinner hat
		if (ergebnisseEingetragen[roundIndex]) {
//			log("For this round results have been entered: " + ergebnisse[roundIndex][indexOfMatch - 1][0] + ":" + ergebnisse[roundIndex][indexOfMatch - 1][1]);
			String loser;
			if (ergebnisse[roundIndex][indexOfMatch - 1][0] < ergebnisse[roundIndex][indexOfMatch - 1][1]) {
				loser = spielplan[roundIndex][indexOfMatch - 1].split(":")[0];
			} else {
				loser = spielplan[roundIndex][indexOfMatch - 1].split(":")[1];
			}
			return searchForWinnerOf(loser.substring(0, 2), Integer.parseInt(loser.substring(2)));
		}
		error("    Results have not been entered yet for this match.");
		
		return round + indexOfMatch;
	}
	
	public Mannschaft mannschaftFromString(String teamsorigin) {
		Mannschaft mannschaft = null;
		
		if (teamsorigin.charAt(0) == 'G') {
//			log("\nSuche nach Gruppenindex...");
			
			// Bestimmen des Indexes der Gruppe
			int groupindex;
			for (groupindex = 0; groupindex < alphabet.length; groupindex++) {
				if (teamsorigin.charAt(1) == alphabet[groupindex]) {
					break;
				}
			}
			
			if (groupindex == alphabet.length) {
				error("    ungültiger Gruppenindex:  " + groupindex + " für Buchstabe  " + teamsorigin.charAt(1));
				return null;
			}
//			log("gültiger Gruppenindex:  " + groupindex + " für Buchstabe  " + teamsorigin.charAt(1));
			
			int placeindex = Integer.parseInt("" + teamsorigin.charAt(2));
//			log("Platzindex:  " + placeindex);
			
			mannschaft = gruppen[groupindex].getTeamOnPlace(placeindex);
			
			teamsorigin = mannschaft.getName();
		}
		
		return mannschaft;
	}
	
	public Mannschaft getMannschaftAt(String matchname, boolean vorderes) {
		
		// String mit dem Match, zB für matchname = "FI1" sollte match = "HF1:HF2" zurückkommen
		String thisround = matchname.substring(0, 2);
		int thisindex = Integer.parseInt(matchname.substring(2, 3));
		
		String match = getMatchInRound(thisround, thisindex);
//		log("Das Match   " + matchname + "   wird bestritten von den Gewinnern von   " + match);
		String teamsorigin = match.split(":")[vorderes ? 0 : 1];
//		log("Und ich will wissen wer  " + teamsorigin + "  ist.\n");
		
		String beforeround = teamsorigin.substring(0, 2);
		int beforeindex = Integer.parseInt(teamsorigin.substring(2, 3));
		teamsorigin = searchForWinnerOf(beforeround, beforeindex);

//		log("\nErgebnis: " + teamsorigin);
		
		return mannschaftFromString(teamsorigin);
	}
	
	public void laden() {
		dateiErgebnisse = workspace + File.separator + name + File.separator + "Ergebnisse.txt";
		dateiSpielplan = workspace + File.separator + name + File.separator + "Spielplan.txt";
    	dateiTeams = workspace + File.separator + name + File.separator + "Mannschaften.txt";
		
    	mannschaftenLaden();
    	spielplanLaden();
    	ergebnisseLaden();
    	
    	testGetMatchInRound();
	}
	
	public void speichern() {
		for (Gruppe gruppe : gruppen) {
			gruppe.speichern();
		}
		// TODO KORunde ...
	}
	
	public void mannschaftenLaden() {
    	this.teamsFromFile = ausDatei(dateiTeams);
		
    	this.numberOfTeams = Integer.parseInt(teamsFromFile[0]);
		this.numberOfTeamsPerGroup = this.numberOfTeams / this.numberOfGroups;
    	
    	gruppen = new Gruppe[this.numberOfGroups];
    	for (int i = 0; i < gruppen.length; i++) {
    		gruppen[i] = new Gruppe(this.start, i, this);
    	}
    	log("\n\n\n\n\n\n\n");
    	
    	mannschaften = new Mannschaft[numberOfTeams];
    	for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i] = gruppen[i / numberOfTeamsPerGroup].getMannschaften()[i % numberOfTeamsPerGroup];
		}
	}
	
	public void spielplanLaden() {
    	this.spielplanFromFile = ausDatei(dateiSpielplan);
    	
    	this.numberOfKORounds = this.spielplanFromFile.length;
    	String[] spielplanzeilen = new String[numberOfKORounds];
    	this.spielplanEingetragen = new boolean[numberOfKORounds];
    	
        this.daysSinceFirstDay = new int[numberOfKORounds][0];
        this.startTime = new int[numberOfKORounds][0];
        this.spielplan = new String[numberOfKORounds][0];
    	
    	log("Dies ist der KO-Runden-Spielplan:");
    	for (int zeile = 0; zeile < spielplanFromFile.length; zeile++) {
    		spielplanzeilen[zeile] = spielplanFromFile[zeile];
    		log(spielplanzeilen[zeile]);
    		
    		
        	String[] spielplanzellen = spielplanzeilen[zeile].split(";");
        	spielplanEingetragen[zeile] = Boolean.parseBoolean(spielplanzellen[0]);
        	log(spielplanEingetragen[zeile]);
        	
        	if (spielplanEingetragen[zeile]) {
        		// Spieltagsdaten
        		String[] spiele = spielplanzellen[1].split(":");
        		int[] days = new int[spiele.length];
        		int[] time = new int[spiele.length];
        		String[] matchstrings = new String[spiele.length];
        		
        		for (int match = 0; match < spiele.length; match++) {
        			String datenUndZeiten[] = spiele[match].split(",");

        			days[match] = Integer.parseInt(datenUndZeiten[0]);
        			time[match] = Integer.parseInt(datenUndZeiten[1]);
        			
        			log(days[match] + " , " + time[match]);
        		}
        		daysSinceFirstDay[zeile] = days;
        		startTime[zeile] = time;
        		
        		// Mannschaften bzw. vorangegangenes Spiel
        		for (int match = 0; (match + 2) < spielplanzellen.length; match++) {
        			matchstrings[match] = spielplanzellen[match + 2];
        			log(matchstrings[match]);
        		}
        		spielplan[zeile] = matchstrings;
        	}
        	log();
    	}
	}
	
	public void ergebnisseLaden() {
		this.ergebnisseFromFile = ausDatei(dateiErgebnisse);
    	
    	String[] ergebnisplanzeilen = new String[this.numberOfKORounds];
    	this.ergebnisseEingetragen = new boolean[this.numberOfKORounds];
        this.ergebnisse = new int[this.numberOfKORounds][0][2];
    	
    	log("Dies ist der KO-Runden-Ergebnisplan:");
    	for (int zeile = 0; zeile < ergebnisseFromFile.length; zeile++) {
    		ergebnisplanzeilen[zeile] = ergebnisseFromFile[zeile];
    		log(ergebnisplanzeilen[zeile]);
    		
        	String[] ergebnisplanzellen = ergebnisplanzeilen[zeile].split(";");
        	ergebnisseEingetragen[zeile] = Boolean.parseBoolean(ergebnisplanzellen[0]);
        	log(ergebnisseEingetragen[zeile]);
        	
        	if (ergebnisseEingetragen[zeile]) {
        		// Ergebnisdaten
        		String[] matchresultstrings = new String[ergebnisplanzellen.length - 1];
        		int[][] matchresultints = new int[ergebnisplanzellen.length - 1][2];
        		
//        		// Mannschaften bzw. vorangegangenes Spiel
        		for (int match = 0; (match + 1) < ergebnisplanzellen.length; match++) {
        			matchresultstrings[match] = ergebnisplanzellen[match + 1];
        			matchresultints[match][0] = Integer.parseInt(matchresultstrings[match].split(":")[0]);
        			matchresultints[match][1] = Integer.parseInt(matchresultstrings[match].split(":")[1]);
        			log(matchresultints[match][0] + " : " + matchresultints[match][1]);
        		}
        		ergebnisse[zeile] = matchresultints;
        	}
    		log();
    	}
	}
	
	private void fromString(String daten) {
		this.name = "FIFA Weltmeisterschaft 2014";
		this.numberOfGroups = 8;
		this.startDate = 20140612;
		this.finalDate = 20140713;
		// TODO
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
		return alles;
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