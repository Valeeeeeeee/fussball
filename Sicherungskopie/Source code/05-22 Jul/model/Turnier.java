package model;

import java.io.File;

import static util.Utilities.*;

public class Turnier {
	
	private Start start;
	private int id;
	private String name;
	
	private int startDate;
	private int finalDate;
	
	private boolean hasGroupStage;
	private boolean groupPhaseSecondLeg;
	private boolean koPhaseSecondLeg;
	
	private Mannschaft[] mannschaften;
	private Gruppe[] gruppen;
	private KORunde[] koRunden;
	
	private int numberOfTeams;
	private int numberOfGroups;
	private int numberOfTeamsPerGroup;
	private int numberOfKORounds;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	private String dateiSpielplan;
	private String dateiKORundenDaten;
	
	private String[] koRundenDatenFromFile;
	private String[] spielplanFromFile;
	
	/**
	 * Indizes der Mannschaften [KORunde][spiel] Inhalt zB: "AF1:AF2"
	 */
	private String[][] spielplan;
	private boolean[] spielplanEingetragen;

	/**
	 * [KORunde][spielIndex]
	 */
    private int[][] daysSinceFirstDay;
    
	/**
	 * [KORunde][spielIndex]
	 */
    private int[][] startTime;
	
	char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
	String[] KOrounds = new String[] {"AF", "VF", "HF", "P3", "FI"};
	String[] KOroundsFullnames = new String[] {"Achtelfinale", "Viertelfinale", "Halbfinale", "Spiel um Platz 3", "Finale"};
	
	public Turnier(Start start, int id, String daten) {
		checkOS();
		this.start = start;
		this.id = id;
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
	
	public KORunde[] getKORunden() {
		return this.koRunden;
	}
	
	public int getStartDate() {
		return this.startDate;
	}
	
	public int getFinalDate() {
		return this.finalDate;
	}
	
	public boolean groupHasSecondLeg() {
		return this.groupPhaseSecondLeg;
	}
	
	public boolean koPhaseHasSecondLeg() {
		return this.koPhaseSecondLeg;
	}
	
	public int getNumberOfKORounds() {
		return this.numberOfKORounds;
	}
	
	public Spieltag getSpieltag(int koRoundIndex) {
//		return this.KOSpieltage[koRoundIndex]; TODO
		return this.koRunden[koRoundIndex].getSpieltag();
	}
	
	public int getNumberOfMatchdays(int KOround) {
		return 1;
	}
	
	public int getNumberOfMatches(int KOround) {
		return spielplan[KOround].length;
	}
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = MyDate.newMyDate() - startDate; // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
//		if (today < this.daysSinceFirstDay[KOround][0]) {
//			matchday = 0;
//		} else if (today > this.daysSinceFirstDay[this.getNumberOfMatchdays(KOround) - 1][0]) {
//			matchday = this.getNumberOfMatchdays(KOround) - 1;
//		} else {
//			matchday = 0;
//			while (today > this.datesAndTimes[matchday][0]) {
//				matchday++;
//			}
//			if (matchday != 0 && (today - this.datesAndTimes[matchday - 1][0]) < (this.datesAndTimes[matchday][0] - today)) {
//				matchday--;
//			}
//		}
		
		matchday = 0;
		
		return matchday;
	}
	
	public String getDateOf(int KOround, int matchday, int spiel) {
//		return MyDate.datum(this.startDate, this.daysSinceFirstDay[KOround][spiel]) + " " + MyDate.uhrzeit(startTime[KOround][spiel]);
		return koRunden[KOround].getDateOf(matchday, spiel);
	}
	
	public Mannschaft[] getMannschaftenIn(int KOround) {
		if (0 > KOround || KOround >= spielplan.length) {
			return null;
		}
		
		Mannschaft[] mannschaften = new Mannschaft[spielplan[KOround].length * 2];
		
		int numbOfMatches = mannschaften.length / 2;
		String teamsorigin;
		
		for (int i = 0; i < mannschaften.length; i++) {
			teamsorigin = getTeamsorigin(KOround, (i % numbOfMatches) + 1, (i < numbOfMatches ? true : false));
			mannschaften[i] = mannschaftFromString(teamsorigin);
		}
		
		return mannschaften;
	}

	
	private void testGetMannschaftenIn() {
		Mannschaft[] teams;
		log("\n\n");
		for (int round = 0; round < KOrounds.length; round++) {
			teams = getMannschaftenIn(round);
			
			log(KOroundsFullnames[round] + " (" + teams.length + " Teams)");
			log("----------------");
			
			for (int i = 0; i < teams.length; i++) {
				try {
					log(teams[i].getName() + "      " + teams[i].toString());
				} catch (NullPointerException e) {
					log("  null pointer");
				}
			}
			
			log("\n");
		}
	}
	
	/**
	 * 
	 * @param matchname A String representing the match, e.g. VF2
	 * @param front if the "home" team is meant or not
	 * @return a String that represents the team like 'GC1' if found, else null
	 */
	public String getTeamsorigin(String matchname, boolean front) {
		
		String thisround = matchname.substring(0, 2);
		int thisindex = Integer.parseInt(matchname.substring(2, 3));
		
		return getTeamsorigin(thisround, thisindex, front);
	}
	
	/**
	 * 
	 * @param round the short name of the elimination round
	 * @param match a number from 1 to as many matches as there are in the round
	 * @param front if the "home" team is meant or not
	 * @return a String that represents the team like 'GC1' if found, else null
	 */
	public String getTeamsorigin(String round, int match, boolean front) {
		int roundIndex = -1;
		
		for (int i = 0; i < KOrounds.length && roundIndex == -1; i++) {
			if (KOrounds[i].equals(round)) {
				roundIndex = i;
			}
		}
		
		if (roundIndex == -1)	return round + match;
		
		if (0 >= match || match > spielplan[roundIndex].length)	return null;
		
		return getTeamsorigin(roundIndex, match, front);
	}
	
	/**
	 * 
	 * @param round the index of the elimination round
	 * @param match a number from 1 to as many matches as there are in the round
	 * @param front if the "home" team is meant or not
	 * @return a String that represents the team like 'GC1' if found, else null
	 */
	private String getTeamsorigin(int round, int match, boolean front) {
		String teamsorigin = null;
		
		String lastMatch = spielplan[round][match - 1].split(":")[front ? 0 : 1];
		String lastRound = lastMatch.substring(0, 2);
		int lastMatchIndex = Integer.parseInt(lastMatch.substring(2, 3));
		
		if (round == 3) {
			teamsorigin = searchForLoserOf(lastRound, lastMatchIndex);
		} else {
			teamsorigin = searchForWinnerOf(lastRound, lastMatchIndex);
		}
		
//		log("teamsorigin = " + teamsorigin + "     lastMatch = " + lastRound + lastMatchIndex);
		
		return teamsorigin;
	}
	
	private void testGetMatchInRound() {
		
		Mannschaft ms1;
		Mannschaft ms2;

		log("\n\n\ntestGetMatchInRound");
		log("==============================");
		String[] matches = new String[] {"VF1", "VF2", "VF3", "VF4", "HF1", "HF2", "P31", "FI1"};
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
				error("\nException at match " + matches[i] + ": " + "Die gesuchte Mannschaft kann aktuell nicht angezeigt werden!");
			}
			try {
				ms2 = getMannschaftAt(matches[i], false);
				log(ms2.getName());
			} catch (Exception e) {
				error("\nException at match " + matches[i] + ": " + "Die gesuchte Mannschaft kann aktuell nicht angezeigt werden!\n");
			}
			log("\n");
		}
		
		log("\n\nWinners and losers of the following rounds");
		log("==============================");
		
		matches = new String[] {"AF1", "AF2", "AF3", "AF4", "AF5", "AF6", "AF7", "AF8", "VF1", "VF2", "VF3", "VF4", "HF1", "HF2"/*, "P31", "FI1"*/};
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
	 * @param round Die Runde des Spiel, das die gesuchte Mannschaft gewonnen haben muss
	 * @param indexOfMatch Der Index des Spiels in der Runde
	 * @return Den String der die Siegermannschaft repräsentiert<br>
	 * 			idealerweise 'Gxy' mit x zwischen A und zB H, y 1 oder 2
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
		if (koRunden[roundIndex].ergebnisseEingetragen[0]) {
//			log("For this round results have been entered: " + ergebnisse[roundIndex][indexOfMatch - 1][0] + ":" + ergebnisse[roundIndex][indexOfMatch - 1][1]);
			String winner;
			if (koRunden[roundIndex].getErgebnis(0, indexOfMatch - 1).home() > koRunden[roundIndex].getErgebnis(0, indexOfMatch - 1).away()) {
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
		if (koRunden[roundIndex].ergebnisseEingetragen[0]) {
//			log("For this round results have been entered: " + ergebnisse[roundIndex][indexOfMatch - 1][0] + ":" + ergebnisse[roundIndex][indexOfMatch - 1][1]);
			String loser;
			if (koRunden[roundIndex].getErgebnis(0, indexOfMatch - 1).home() < koRunden[roundIndex].getErgebnis(0, indexOfMatch - 1).away()) {
				loser = spielplan[roundIndex][indexOfMatch - 1].split(":")[0];
			} else {
				loser = spielplan[roundIndex][indexOfMatch - 1].split(":")[1];
			}
			return searchForWinnerOf(loser.substring(0, 2), Integer.parseInt(loser.substring(2)));
		}
		error("    Results have not been entered yet for this match.");
		
		return round + indexOfMatch;
	}
	
	
	/**
	 * This method returns the team with the given origin.
	 * @param teamsorigin origin of the team in the format 'GG1'
	 * @return
	 */
	public Mannschaft mannschaftFromString(String teamsorigin) {
		Mannschaft mannschaft = null;
		
		if (teamsorigin != null && teamsorigin.charAt(0) == 'G') {
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
			
			mannschaft = gruppen[groupindex].getTeamOnPlace(placeindex);
		}
		
		return mannschaft;
	}
	
	public Mannschaft getMannschaftAt(String matchname, boolean front) {
		String teamsorigin = getTeamsorigin(matchname, front);
		
//		log("\nErgebnis: " + teamsorigin);
		
		return mannschaftFromString(teamsorigin);
	}
	
	public String getMannschaftsNameAt(int KOround, int matchday, int spiel, int backOrFront) {
		
		Mannschaft team = mannschaftFromString(getTeamsorigin(KOround, spiel + 1, backOrFront == 0));
		if (team != null)	return team.getName();
		
		String teamsorigin = getTeamsorigin(KOround, spiel, backOrFront == 0);
		if (teamsorigin != null)	return teamsorigin;
		
		return null;
	}
	
	public void laden() {
		dateiSpielplan = workspace + File.separator + name + File.separator + "Spielplan.txt";
		dateiKORundenDaten = workspace + File.separator + name + File.separator + "KOconfig.txt";
		
    	spielplanLaden();
    	mannschaftenLaden();
    	
    	testGetMatchInRound();
    	testGetMannschaftenIn();
	}
	
	public void speichern() {
		for (Gruppe gruppe : gruppen) {
			gruppe.speichern();
		}
		
		for (KORunde koRunde : koRunden) {
			koRunde.speichern();
		}
		inDatei(dateiKORundenDaten, koRundenDatenFromFile);
	}
	
	public void mannschaftenLaden() {
    	this.koRundenDatenFromFile = ausDatei(dateiKORundenDaten);
		
    	if (hasGroupStage) {
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
    	
    	koRunden = new KORunde[KOrounds.length];
    	for (int i = 0; i < koRunden.length; i++) {
    		koRunden[i] = new KORunde(this.start, this, i, koRundenDatenFromFile[i]);
    	}
    	log("\n\n\n\n\n\n\n");
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
	
	private void fromString(String daten) {
		String[] alledaten = daten.split(";");
		// TODO
		
		this.name = "FIFA Weltmeisterschaft 2014";
		this.startDate = 20140612;
		this.finalDate = 20140713;
		this.numberOfTeams = 32;
		this.hasGroupStage = true;
		this.numberOfGroups = 8;
		this.groupPhaseSecondLeg = false;
		this.koPhaseSecondLeg = false;
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
		alles += "STDATE*" + this.startDate + ";";
		alles += "FIDATE*" + this.finalDate + ";";
		alles += "NOFTEAMS*" + this.numberOfTeams + ";";
		alles += "GRPSTG*" + this.hasGroupStage + ";";
		alles += "NOFGRPS*" + this.numberOfGroups + ";";
		alles += "GRPSECLEG*" + this.groupPhaseSecondLeg + ";";
		alles += "KOSECLEG*"+ this.koPhaseSecondLeg + ";";
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