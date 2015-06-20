package model;

import java.io.*;
import java.util.ArrayList;

import static util.Utilities.*;

public class Liga implements Wettbewerb {
	private int id = -1;
    private Start start;
	private String name;
	
	private boolean isETPossible = false;
	private boolean isSummerToSpringSeason;
	private ArrayList<Integer> saisons;
	private int aktuelleSaison;
	
	private int numberOfTeams;
	private int halbeanzMSAuf;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int numberOfMatchdays;
	
	private int ANZAHL_CL;
	private int ANZAHL_CLQ;
	private int ANZAHL_EL;
	private int ANZAHL_REL;
	private int ANZAHL_ABS;
	private boolean goalDifference; // TODO in toString
	
	private Mannschaft[] mannschaften;
	private boolean teamsHaveKader;
	
	private String workspace;
    
    private String dateiTeams;
    private ArrayList<String> teamsFromFile;
    
	private String dateiSpielplan;
	private ArrayList<String> spielplanFromFile;
    private boolean[][] spielplanEingetragen;
    /**
     * [spieltag][spiel]
     */
    private Spiel[][] spielplan;
    
    private String dateiErgebnisse;
    private ArrayList<String> ergebnisseFromFile;
    private boolean[][] ergebnisplanEingetragen;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisplan;
    
	private String dateiSpieldaten;
	private ArrayList<String> spieldatenFromFile;
	
    private int[][] datesAndTimes;
    
    private int numberOfKickoffTimes;
    int[] kickoffTimes;
    int[] daysSinceDST;
    private int defaultStarttag;
    int[] defaultKickoffTimes;
    
    private Spieltag spieltag;
    private Tabelle tabelle;
    private LigaStatistik statistik;
    
	public Liga(int id, Start start, String daten) {
		this.start = start;
		checkOS();
		
		this.id = id;
		fromString(daten);
		this.mannschaften = new Mannschaft[0];
	}
	
	public Liga(int id, Start start, String name, int anz_MS, int nOMASO, int anzCL, int anzCLQ, int anzEL, int anzREL, int anzABS) {
		this.start = start;
		checkOS();
		
		this.id = id;
		this.name = name;
		this.mannschaften = new Mannschaft[0];
		
		this.numberOfMatchesAgainstSameOpponent = nOMASO;
		this.ANZAHL_CL = anzCL;
		this.ANZAHL_CLQ = anzCLQ;
		this.ANZAHL_EL = anzEL;
		this.ANZAHL_REL = anzREL;
		this.ANZAHL_ABS = anzABS;
	}
	
	public Spieltag getSpieltag() {
		return this.spieltag;
	}
	
	public Tabelle getTabelle() {
		return this.tabelle;
	}
	
	public LigaStatistik getLigaStatistik() {
		return this.statistik;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return this.numberOfMatchesAgainstSameOpponent;
	}
	
	public boolean teamsHaveKader() {
		return this.teamsHaveKader;
	}
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = MyDate.verschoben(MyDate.newMyDate(), -1); // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (today < getDate(0)) {
			matchday = 0;
		} else if (today > getDate(this.numberOfMatchdays - 1) && getDate(this.numberOfMatchdays - 1) != 0) {
			matchday = this.numberOfMatchdays - 1;
		} else {
			matchday = 0;
			while (today > getDate(matchday) && getDate(matchday) != 0) {
				matchday++;
			}
			if (matchday != 0 && MyDate.difference(getDate(matchday - 1), today) < MyDate.difference(today, getDate(matchday))) {
				matchday--;
			}
		}
		
		return matchday;
	}
	
	public boolean addSeason(int season, ArrayList<Mannschaft> teams) {
		try {
			speichern();
		} catch (Exception e) {}
		
		int index = 0;
		for (int i = 0; i < saisons.size(); i++) {
			if (season == saisons.get(i)) {
				message("A season for that year already exists.");
				return false;
			} else if (season > saisons.get(i))	index++;
		}
		saisons.add(index, season);
		aktuelleSaison = index;
		
		String saison = "" + saisons.get(aktuelleSaison);
		if (this.isSummerToSpringSeason)	saison += "_" + (saisons.get(aktuelleSaison) + 1);
		
        String folder = workspace + File.separator + name + File.separator + saison + File.separator;
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
        
        int numberOfDKOT = defaultKickoffTimes[numberOfMatchesPerMatchday - 1] + 1;
        String defaultKickOffTimes = numberOfDKOT + ";";
        for (int i = 0; i < numberOfDKOT; i++) {
        	defaultKickOffTimes += daysSinceDST[i] + "," + kickoffTimes[i] + ";";
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
	
	public void addNewKickoffTime(int tageseitfreitag, int kickofftime) {
		int[] oldtageseitfr = daysSinceDST;
		int[] oldKickoffTimes = kickoffTimes;
		daysSinceDST = new int[oldtageseitfr.length + 1];
		kickoffTimes = new int[oldKickoffTimes.length + 1];
		
		for (int i = 0; i < oldtageseitfr.length; i++) {
			daysSinceDST[i] = oldtageseitfr[i];
			kickoffTimes[i] = oldKickoffTimes[i];
		}
		
		daysSinceDST[daysSinceDST.length - 1] = tageseitfreitag;
		kickoffTimes[kickoffTimes.length - 1] = kickofftime;
		this.numberOfKickoffTimes++;
	}
	
	public void useDefaultKickoffTimes(int matchday) {
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			setKOTIndex(matchday, i, defaultKickoffTimes[i]);
		}
	}
	
	/**
	 * This is the new implementation, that starts with the matches and does not go through the teams. It therefore renounces on the positiononplan array.
	 */
	public void ergebnisseSichern() {
		int matchday = spieltag.getCurrentMatchday();
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (isSpielplanEntered(matchday, match)) {
				Ergebnis result = spieltag.getErgebnis(match);
				
				setErgebnis(matchday, match, result);
				mannschaften[getSpiel(matchday, match).home() - 1].setResult(matchday, result);
				mannschaften[getSpiel(matchday, match).away() - 1].setResult(matchday, result);
			}
		}
	}
	
	public void laden(int index) {
		String saison;
		aktuelleSaison = index;
		if (this.isSummerToSpringSeason)	saison = saisons.get(aktuelleSaison) + "_" + (saisons.get(aktuelleSaison) + 1);
		else								saison = "" + saisons.get(aktuelleSaison);
		
        dateiErgebnisse = workspace + File.separator + name + File.separator + saison + File.separator + "Ergebnisse.txt";
        dateiSpieldaten = workspace + File.separator + name + File.separator + saison + File.separator + "Spieldaten.txt";
        dateiSpielplan = workspace + File.separator + name + File.separator + saison + File.separator + "Spielplan.txt";
    	dateiTeams = workspace + File.separator + name + File.separator + saison + File.separator + "Mannschaften.txt";
    	
    	File file = new File(workspace + File.separator + name + File.separator + saison);
    	
    	try {
    		file.mkdir();
    	} catch (Exception e) {
    		System.out.println("Error while creating directory!");
    	}
    	
    	mannschaftenLaden();
        
        arraygroessen_initialisieren();
        
        spielplanLaden();
        ergebnisplanLaden();
        spieldatenLaden();
        
        if (spieltag == null) {
            spieltag = new Spieltag(start, this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        } else {
        	spieltag.resetCurrentMatchday();
        }
        if (tabelle == null) {
            tabelle = new Tabelle(start, this);
            tabelle.setLocation((start.WIDTH - tabelle.getSize().width) / 2, 50);
            tabelle.setVisible(false);
        }
        if (statistik == null) {
        	statistik = new LigaStatistik(this);
        	statistik.setLocation((start.WIDTH - statistik.getSize().width) / 2, 50);
        	statistik.setVisible(false);
        }
        tabelle.resetCurrentMatchday();
    }
	
	public void speichern() throws Exception {
		this.spielplanSchreiben();
		this.ergebnisplanSchreiben();
		this.spieldatenSchreiben();
		this.mannschaftenSchreiben();
	}
	
	public Mannschaft[] getMannschaften() {
		return this.mannschaften;
	}

    public int getIndexOfMannschaft(String name) {
    	for (Mannschaft ms : this.mannschaften) {
    		if (ms.getName().equals(name)) {
    			return ms.getId();
    		}
    	}
    	return -1;
    }
    
    public Mannschaft getTeamwithName(String teamsName) {
    	return mannschaften[getIndexOfMannschaft(teamsName) - 1];
    }
    
	public String teamsToString() {
		String alles = "";
		
		for (int i = 0; i < numberOfTeams; i++) {
			alles = alles + mannschaften[i].toString() + "\n";
		}
		
		return alles;
	}
	
	public boolean isETPossible() {
		return this.isETPossible;
	}
	
	public boolean isSTSS() {
		return this.isSummerToSpringSeason;
	}
	
	public boolean useGoalDifference() {
		return this.goalDifference;
	}
	
	public String getWorkspace() {
		return this.workspace + File.separator + name + File.separator + getAktuelleSaison() + (isSTSS() ? "_" + (getAktuelleSaison() + 1) : "") + File.separator;
	}
	
	/**
	 * This method returns all available seasons in the format "2014/2015" if isSTSS, otherwise "2014".
	 * @return a String array containing all available seasons
	 */
	public String[] getAllSeasons() {
		String[] hilfsarray = new String[this.saisons.size()];
        for (int i = 0; i < this.saisons.size(); i++) {
            hilfsarray[i] = this.saisons.get(i) + (this.isSummerToSpringSeason ? "/" + (this.saisons.get(i) + 1) : "");
        }
        return hilfsarray;
	}
	
	public String getSeasonsRepresentation() {
		String representation = "";
		for (int i = 0; i < this.saisons.size(); i++) {
			String trenn = "S" + i;
			representation += trenn + "*" + this.saisons.get(i) + "*" + trenn + ",";
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
	
	public int getAktuelleSaison() {
		return this.saisons.get(this.aktuelleSaison);
	}
	
	public int getDefaultStarttag() {
		return this.defaultStarttag;
	}
	
	public int getNumberOfTeams() {
		return this.numberOfTeams;
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return this.numberOfMatchesPerMatchday;
	}
	
	public int getHalbeAnzMSAuf() {
		return this.halbeanzMSAuf;
	}
	
	public int getNumberOfMatchdays() {
		return this.numberOfMatchdays;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getMatchdayDescription(int matchday) {
		return name + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public int getAnzahlCL() {
		return this.ANZAHL_CL;
	}
	
	public int getAnzahlCLQ() {
		return this.ANZAHL_CLQ;
	}
	
	public int getAnzahlEL() {
		return this.ANZAHL_EL;
	}
	
	public int getAnzahlREL() {
		return this.ANZAHL_REL;
	}
	
	public int getAnzahlABS() {
		return this.ANZAHL_ABS;
	}
	
	@SuppressWarnings("unused")
	private void testIsSpielplanEntered() {
		log("\n\nTest-Ausgabe des spielplanEingetragen-Arrays:\n");
		for (int i = 0; i < numberOfMatchdays; i++) {
			log((i + 1) + ". matchday is" + (isSpielplanFullyEmpty(i) ? "" : " not") + " fully empty.");
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				log("   " + (j + 1) + ". match is" + (isSpielplanEntered(i, j) ? "" : " not") + " entered");
			}
			log();
		}
	}
	
	@SuppressWarnings("unused")
	private void testIsErgebnisplanEntered() {
		log("\n\nTest-Ausgabe des ergebnisplanEingetragen-Arrays:\n");
		for (int i = 0; i < numberOfMatchdays; i++) {
			log((i + 1) + ". matchday is" + (isErgebnisplanFullyEmpty(i) ? "" : " not") + " fully empty.");
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				log("   " + (j + 1) + ". match is" + (isErgebnisplanEntered(i, j) ? "" : " not") + " entered");
			}
			log();
		}
	}
	
	// Spielplan eingetragen
	
	public boolean isSpielplanFullyEmpty(int matchday) {
		for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
			if (isSpielplanEntered(matchday, match)) 	return false;
		}
		return true;
	}
	
	public boolean isSpielplanEntered(int matchday, int match) {
		return spielplanEingetragen[matchday][match];
	}
	
	public void setSpielplanEntered(int matchday, int match, boolean isEntered) {
		spielplanEingetragen[matchday][match] = isEntered;
	}
	
	public String getSpielplanRepresentation(int matchday) {
		String representation = "";
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (isSpielplanEntered(matchday, match))	representation += "t";
			else										representation += "f";
		}
		
		return representation;
	}
	
	public void setSpielplanEnteredFromRepresentation(int matchday, String representation) {
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (representation.charAt(match) == 't')		setSpielplanEntered(matchday, match, true);
			else if (representation.charAt(match) == 'f')	setSpielplanEntered(matchday, match, false);
		}
	}
	
	// Ergebnisplan eingetragen
	
	public boolean isErgebnisplanFullyEmpty(int matchday) {
		for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
			if (isErgebnisplanEntered(matchday, match)) 	return false;
		}
		return true;
	}
	
	public boolean isErgebnisplanEntered(int matchday, int match) {
		return ergebnisplanEingetragen[matchday][match];
	}
	
	public void setErgebnisplanEntered(int matchday, int match, boolean isEntered) {
		ergebnisplanEingetragen[matchday][match] = isEntered;
	}
	
	public String getErgebnisplanRepresentation(int matchday) {
		String representation = "";
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (isErgebnisplanEntered(matchday, match))	representation += "t";
			else										representation += "f";
		}
		
		return representation;
	}
	
	public void setErgebnisplanEnteredFromRepresentation(int matchday, String representation) {
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (representation.charAt(match) == 't')		setErgebnisplanEntered(matchday, match, true);
			else if (representation.charAt(match) == 'f')	setErgebnisplanEntered(matchday, match, false);
		}
	}
	
	// Ergebnisplan
	
	public Ergebnis getErgebnis(int matchday, int match) {
		return ergebnisplan[matchday][match];
	}
	
	public void setErgebnis(int matchday, int match, Ergebnis ergebnis) {
		if (ergebnis != null)	setErgebnisplanEntered(matchday, match, true);
		else					setErgebnisplanEntered(matchday, match, false);
		ergebnisplan[matchday][match] = ergebnis;
		if (isSpielplanEntered(matchday, match))	getSpiel(matchday, match).setErgebnis(ergebnis);
	}
	
	// Spielplan
	
	public Spiel getSpiel(int matchday, int match) {
		return spielplan[matchday][match];
	}
	
	public void setSpiel(int matchday, int match, Spiel spiel) {
		if (spiel != null) {
			setSpielplanEntered(matchday, match, true);
	    	this.mannschaften[spiel.home() - 1].setMatch(matchday, spiel);
	        this.mannschaften[spiel.away() - 1].setMatch(matchday, spiel);
		}
		else				setSpielplanEntered(matchday, match, false);
		spielplan[matchday][match] = spiel;
	}
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[this.numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[this.numberOfMatchesPerMatchday];
		int[] dates = new int[this.numberOfMatchesPerMatchday];
		int[] times = new int[this.numberOfMatchesPerMatchday];
		
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++) {
			dates[match] = MyDate.verschoben(this.getDate(matchday), this.daysSinceDST[this.getKOTIndex(matchday, match)]);
			times[match] = this.kickoffTimes[this.getKOTIndex(matchday, match)];
		}
		
		for (int m = 0; m < this.numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < this.numberOfMatchesPerMatchday; m2++) {
				if (dates[m2] > dates[m])									hilfsarray[m2]++;
				else if (dates[m2] == dates[m] && times[m2] >= times[m])	hilfsarray[m2]++;
				else														hilfsarray[m]++;
			}
		}
		
		for (int i = 0; i < hilfsarray.length; i++) {
			newOrder[hilfsarray[i]] = i;
		}
		
		changeOrderOfMatches(matchday, newOrder);
	}
	
	public void changeOrderOfMatches(int matchday, int[] oldIndicesInNewOrder) {
		// check the correctness of the parameter array
		if (oldIndicesInNewOrder.length != this.numberOfMatchesPerMatchday) {
			errorMessage("The parameter array does not have the correct length.");
			return;
		}
		
		boolean[] checked = new boolean[oldIndicesInNewOrder.length];
		try {
			for (int i = 0; i < checked.length; i++) {
				checked[oldIndicesInNewOrder[i]] = !checked[oldIndicesInNewOrder[i]];
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			errorMessage("Array contains irregular match indices.");
			return;
		}
		
		for (int i = 0; i < checked.length; i++) {
			if (!checked[i]) {
				errorMessage("Irregular distribution of indices, not every index has appeared.");
				return;
			}
		}
		
		// duplicate old arrays and set new ones
		Spiel[] oldSpielplan = new Spiel[this.numberOfMatchesPerMatchday];
		Ergebnis[] oldErgebnisplan = new Ergebnis[this.numberOfMatchesPerMatchday];
		boolean[] oldSpielplanEingetragen = new boolean[this.numberOfMatchesPerMatchday];
		boolean[] oldErgebnisplanEingetragen = new boolean[this.numberOfMatchesPerMatchday];
		int[] oldKOTindices = new int[this.numberOfMatchesPerMatchday];
		
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldSpielplan[match] = getSpiel(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldErgebnisplan[match] = getErgebnis(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldSpielplanEingetragen[match] = isSpielplanEntered(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldErgebnisplanEingetragen[match] = isErgebnisplanEntered(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldKOTindices[match] = datesAndTimes[matchday][match + 1];
		
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setSpiel(matchday, match, oldSpielplan[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setErgebnis(matchday, match, oldErgebnisplan[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setSpielplanEntered(matchday, match, oldSpielplanEingetragen[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setErgebnisplanEntered(matchday, match, oldErgebnisplanEingetragen[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	datesAndTimes[matchday][match + 1] = oldKOTindices[oldIndicesInNewOrder[match]];
	}
	
	public void setRueckrundeToOrder(int[] orderOfRueckRunde) {
		if (2 * orderOfRueckRunde.length != this.numberOfMatchdays) {
			errorMessage("Your Rueckrunde does not match the expected number of matchdays.");
			return;
		}
		boolean[] inHinrunde = new boolean[this.numberOfMatchdays];
		for (int i = 0; i < orderOfRueckRunde.length; i++) {
			if (inHinrunde[orderOfRueckRunde[i] - 1] == true) {
				errorMessage("Some matchday appears to be twice in the Rueckrunde.");
				return;
			}
			inHinrunde[orderOfRueckRunde[i] - 1] = true;
		}
		
		int matchdayOld, matchdayNew = 0;
		for (int i = 0; i < orderOfRueckRunde.length; i++) {
			Spiel[] spieleInNewOrder = new Spiel[this.numberOfMatchesPerMatchday];
			matchdayOld = orderOfRueckRunde[i] - 1;
			while (inHinrunde[matchdayNew]) {
				matchdayNew++;
			}
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				Spiel oldSpiel = getSpiel(matchdayOld, j);
				spieleInNewOrder[j] = new Spiel(this, matchdayNew, this.datesAndTimes[matchdayNew][0], 0, oldSpiel.away(), oldSpiel.home());
			}
			for (int j = 0; j < spieleInNewOrder.length; j++) {
				for (int k = j + 1; k < spieleInNewOrder.length; k++) {
					if (spieleInNewOrder[j].home() > spieleInNewOrder[k].home()) {
						Spiel zwischen = spieleInNewOrder[j];
						spieleInNewOrder[j] = spieleInNewOrder[k];
						spieleInNewOrder[k] = zwischen;
					}
				}
			}
			for (int j = 0; j < spieleInNewOrder.length; j++) {
				setSpiel(matchdayNew, j, spieleInNewOrder[j]);
			}
			matchdayNew++;
		}
		
	}
	
	public String getDateOfTeam(int matchday, int id) {
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (isSpielplanEntered(matchday, match)) {
				if (getSpiel(matchday, match).home() == id || getSpiel(matchday, match).away() == id)
					return getDateAndTime(matchday, match);
			}
		}
		
		return "n.a.";
	}
	
	public String getDateAndTime(int matchday, int match) {
		if (matchday >= 0 && matchday < numberOfMatchdays && match >= 0 && match < numberOfMatchesPerMatchday && getDate(matchday) != 0)
			return MyDate.datum(getDate(matchday, match)) + " " + MyDate.uhrzeit(getTime(matchday, match));
		else
			return "nicht terminiert";
	}
	
	public int getDate(int matchday) {
		if (matchday >= 0 && matchday < this.numberOfMatchdays)	return datesAndTimes[matchday][0];
		else													return this.datesAndTimes[0][0];
	}
	
	public int getKOTIndex(int matchday, int match) {
		return datesAndTimes[matchday][match + 1];
	}
	
	public void setDate(int matchday, int date) {
		datesAndTimes[matchday][0] = date;
	}
	
	public void setKOTIndex(int matchday, int match, int index) {
		datesAndTimes[matchday][match + 1] = index;
	}
	
	public int getDate(int matchday, int match) {
		return MyDate.verschoben(datesAndTimes[matchday][0], daysSinceDST[datesAndTimes[matchday][match + 1]]);
	}
	
	public int getTime(int matchday, int match) {
		return this.kickoffTimes[this.getKOTIndex(matchday, match)];
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
	
	private void arraygroessen_initialisieren() {
    	// Alle Array werden initialisiert
		this.datesAndTimes = new int[this.numberOfMatchdays][this.numberOfMatchesPerMatchday + 1];
        this.spielplan = new Spiel[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.ergebnisplan = new Ergebnis[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.spielplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.ergebnisplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
    }
	
    private void mannschaftenLaden() {
    	this.teamsFromFile = ausDatei(this.dateiTeams);
    	
		halbeanzMSAuf = (int) Math.round((double) numberOfTeams / 2);				// liefert die (aufgerundete) Haelfte zurueck
		numberOfMatchesPerMatchday = numberOfTeams / 2;								// liefert die (abgerundete) Haelfte zurueck
		if (numberOfTeams >= 2)		numberOfMatchdays = numberOfMatchesAgainstSameOpponent * (2 * halbeanzMSAuf - 1);
		else						numberOfMatchdays = 0;
		
		this.mannschaften = new Mannschaft[numberOfTeams];
		for (int i = 0; i < numberOfTeams; i++) {
			this.mannschaften[i] = new Mannschaft(this.start, i + 1, this, teamsFromFile.get(i + 1));
		}
    }
    
    private void mannschaftenSchreiben() {
    	this.teamsFromFile = new ArrayList<>();
    	
    	this.teamsFromFile.add("" + this.numberOfTeams);
		for (int i = 0; i < this.numberOfTeams; i++) {
			this.mannschaften[i].saveKader();
			this.teamsFromFile.add(this.mannschaften[i].toString());
		}
		inDatei(this.dateiTeams, this.teamsFromFile);
    }
    
	private void spielplanLaden() {
		try {
			this.spielplanFromFile = ausDatei(this.dateiSpielplan); 
		    
	        int counter = 0;
	        
	        // Anstosszeiten / Spieltermine
	        String allKickoffTimes = this.spielplanFromFile.get(0);
	        String[] kickoffs = allKickoffTimes.split(";");
	        this.numberOfKickoffTimes = Integer.parseInt(kickoffs[0]);
	        this.daysSinceDST = new int[this.numberOfKickoffTimes];
	        this.kickoffTimes = new int[this.numberOfKickoffTimes];
	        for (counter = 0; counter < this.numberOfKickoffTimes; counter++) {
	        	String[] kickoffDetails = kickoffs[counter + 1].split(",");
	        	daysSinceDST[counter] = Integer.parseInt(kickoffDetails[0]);
	        	kickoffTimes[counter] = Integer.parseInt(kickoffDetails[1]);
	        }
	        
	        for (int matchday = 0; matchday < this.numberOfMatchdays; matchday++) {
	        	String[] inhalte = this.spielplanFromFile.get(matchday + 1).split(";");
	        	
	            this.setSpielplanEnteredFromRepresentation(matchday, inhalte[0]);
	            
	            int match = 0;
	            if (!this.isSpielplanFullyEmpty(matchday)) {
	            	// Daten und Uhrzeiten
	            	String[] uhrzeiten = inhalte[1].split(":");
	            	setDate(matchday, Integer.parseInt(uhrzeiten[0]));
	            	for (match = 0; (match + 1) < uhrzeiten.length; match++) {
	            		setKOTIndex(matchday, match, Integer.parseInt(uhrzeiten[match + 1]));
            		}
	            	
	            	// Spielplan
	            	for (match = 0; (match + 2) < inhalte.length; match++) {
	            		Spiel spiel = null;
	            		
	            		if (isSpielplanEntered(matchday, match)) {
	            			spiel = new Spiel(this, matchday, getDate(matchday, match), getTime(matchday, match), inhalte[match + 2]);
	            		}
	            		
	                    setSpiel(matchday, match, spiel);
					}
	            }
	            
	            while(match < this.numberOfMatchesPerMatchday) {
                    setSpiel(matchday, match, null);
                    match++;
	            }
	        }
		} catch (Exception e) {
			errorMessage("Kein Spielplan: " + e.getMessage());
			e.printStackTrace();
		}
    }
	
	private void spielplanSchreiben() throws NullPointerException {
		if (this.dateiSpielplan == null) {
			throw new NullPointerException();
		}
		
        this.spielplanFromFile = new ArrayList<>();
        
		String string = this.numberOfKickoffTimes + ";";
		for (int i = 0; i < this.numberOfKickoffTimes; i++) {
			string = string + this.daysSinceDST[i] + "," + this.kickoffTimes[i] + ";";
        }
		this.spielplanFromFile.add(string);
		
        for (int matchday = 0; matchday < this.numberOfMatchdays; matchday++) {
            string = this.getSpielplanRepresentation(matchday) + ";";
            if (!this.isSpielplanFullyEmpty(matchday)) {
            	string += getDate(matchday);
            	for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
            		string += ":" + getKOTIndex(matchday, j);
            	}
            	string += ";";
                for (int match = 0; match < this.numberOfMatchesPerMatchday; match++) {
                    string += getSpiel(matchday, match) + ";";
                }
            }
            
            this.spielplanFromFile.add(string);
        }
        
        inDatei(this.dateiSpielplan, this.spielplanFromFile);
    }
	
	private void ergebnisplanLaden() {
		try {
			this.ergebnisseFromFile = ausDatei(this.dateiErgebnisse);
	        int counter;
	        
	        for (counter = 0; counter < this.numberOfMatchdays; counter++) {
	        	String inhalte[] = this.ergebnisseFromFile.get(counter).split(";");
	            this.setErgebnisplanEnteredFromRepresentation(counter, inhalte[0]);

	            int match = 0;
	            if (!this.isSpielplanFullyEmpty(counter) && !this.isErgebnisplanFullyEmpty(counter)) {
	            	for (match = 0; (match + 1) < inhalte.length; match++) {
	        			if (isSpielplanEntered(counter, match)) {
	        				Ergebnis ergebnis;
		        			if (isErgebnisplanEntered(counter, match))	ergebnis = new Ergebnis(inhalte[match + 1]);
		        			else										ergebnis = null;
		        			
		        			setErgebnis(counter, match, ergebnis);
	        			
	        				this.mannschaften[getSpiel(counter, match).home() - 1].setResult(counter, ergebnis);
		                    this.mannschaften[getSpiel(counter, match).away() - 1].setResult(counter, ergebnis);
	        			}
		            }
	            }
	            while (match < this.numberOfMatchesPerMatchday) {
	            	setErgebnis(counter, match, null);
	            	match++;
	            }
	        }
		} catch (Exception e) {
			errorMessage("Kein Ergebnisseplan: " + e.getMessage());
			e.printStackTrace();
		}
    }
	
	private void ergebnisplanSchreiben() throws NullPointerException {
		if (this.dateiErgebnisse == null) {
			throw new NullPointerException();
		}
		
        this.ergebnisseFromFile = new ArrayList<>();
        for (int i = 0; i < this.numberOfMatchdays; i++) {
            String string = this.getErgebnisplanRepresentation(i) + ";";
            if (!this.isErgebnisplanFullyEmpty(i)) {
				for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
                	string += getErgebnis(i, j) + ";";
				}
            }
            
            this.ergebnisseFromFile.add(string);
        }
        
        inDatei(this.dateiErgebnisse, this.ergebnisseFromFile);
    }
	
	private void spieldatenLaden() {
		try {
			this.spieldatenFromFile = ausDatei(this.dateiSpieldaten);
			int matchday;
			
			for (matchday = 0; matchday < this.numberOfMatchdays && matchday < spieldatenFromFile.size(); matchday++) {
				String inhalte[] = this.spieldatenFromFile.get(matchday).split(";");
				int match = 0;
				for (match = 0; match < inhalte.length; match++) {
					if (isSpielplanEntered(matchday, match)) {
						getSpiel(matchday, match).setRemainder(inhalte[match]);
					}
				}
			}
		} catch (Exception e) {
			errorMessage("Keine Spieldaten: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void spieldatenSchreiben() throws NullPointerException {
		if (this.dateiSpieldaten == null) {
			throw new NullPointerException();
		}
		
		this.spieldatenFromFile = new ArrayList<>();
		for (int i = 0; i < this.numberOfMatchdays; i++) {
			String string = "";
			for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
				if (getSpiel(i, j) != null)	string += getSpiel(i, j).fullString() + ";";
				else						string += "null;";
			}
			this.spieldatenFromFile.add(string);
		}
		
		inDatei(this.dateiSpieldaten, this.spieldatenFromFile);
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
		
		this.saisons = getSeasonsFromRepresentation(daten.substring(daten.indexOf(";S") + 1));
	}
	
	public void checkOS() {
		workspace = start.getWorkspace();
	}
}
