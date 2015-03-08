package model;

import java.io.*;

import static util.Utilities.*;

public class Liga implements Wettbewerb {
	private int id = -1;
    private Start start;
	private String name;
	
	private boolean isETPossible = false;
	private boolean isSummerToSpringSeason;
	private int[] saisons;
	private int aktuelleSaison;
	
	private int numberOfTeams;
	private int halbeanzMSAuf;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent = 2;
	private int numberOfMatchdays;
	
	private int ANZAHL_CL;
	private int ANZAHL_CLQ;
	private int ANZAHL_EL;
	private int ANZAHL_REL;
	private int ANZAHL_ABS;
	
	private Mannschaft[] mannschaften;
	
	private String workspace;
    private String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
    private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
    
    private String dateiTeams;
    private String[] teamsFromFile;
    
	private String dateiSpielplan;
	private String[] spielplanFromFile;
    private boolean[][] spielplanEingetragen;
    /**
     * [spieltag][spiel]
     */
    private Spiel[][] spielplan;
    
    private String dateiErgebnisse;
    private String[] ergebnisseFromFile;
    private boolean[][] ergebnisplanEingetragen;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisplan;
    
    private int[][] datesAndTimes;
    
    private int numberOfKickoffTimes;
    int[] kickoffTimes;
    int[] daysSinceDST;
    private int defaultStarttag;
    int[] defaultKickoffTimes;
    
    private Spieltag spieltag;
    private Tabelle tabelle;
    
    
	public Liga(int id, Start start, String daten) {
		checkOS();
		
		this.id = id;
		this.start = start;
		fromString(daten);
		this.mannschaften = new Mannschaft[0];
	}
	
	public Liga(int id, Start start, String name, int anz_MS, int nOMASO, int anzCL, int anzCLQ, int anzEL, int anzREL, int anzABS) {
		checkOS();
		
		this.id = id;
		this.start = start;
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
	
	public Tabelle getTable() {
		return this.tabelle;
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
		if (this.isSummerToSpringSeason)	saison = saisons[aktuelleSaison] + "_" + (saisons[aktuelleSaison] + 1);
		else								saison = "" + saisons[aktuelleSaison];
		
        dateiErgebnisse = workspace + File.separator + name + File.separator + saison + File.separator + "Ergebnisse.txt";
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
        
        if (spieltag == null) {
            spieltag = new Spieltag(this.start, this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        } else {
        	spieltag.resetCurrentMatchday();
        }
        if (tabelle == null) {
            tabelle = new Tabelle(start, this);
            tabelle.setLocation((1440 - tabelle.getSize().width) / 2, 50);
            tabelle.setVisible(false);
        }
        
//        testIsSpielplanEntered();
//        testIsErgebnisplanEntered();
    }
	
	public void speichern() throws Exception {
		this.spielplanSchreiben();
		this.ergebnisplanSchreiben();
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
	
	public String getWorkspace() {
		return this.workspace + File.separator + name + File.separator + getAktuelleSaison() + (isSTSS() ? "_" + (getAktuelleSaison() + 1) : "") + File.separator;
	}
	
	/**
	 * This method returns all available seasons in the format "2014/2015" if isSTSS, otherwise "2014".
	 * @return a String array containing all available seasons
	 */
	public String[] getAllSeasons() {
		String[] hilfsarray = new String[this.saisons.length];
        for (int i = 0; i < this.saisons.length; i++) {
            hilfsarray[i] = this.saisons[i] + (this.isSummerToSpringSeason ? "/" + (this.saisons[i] + 1) : "");
        }
        return hilfsarray;
	}
	
	public String getSeasonsRepresentation() {
		String representation = "";
		for (int i = 0; i < this.saisons.length; i++) {
			String trenn = "S" + i;
			representation += trenn + "*" + this.saisons[i] + "*" + trenn + ",";
		}
		return representation.substring(0, representation.length() - 1);
	}
	
	public int[] getSeasonsFromRepresentation(String representation) {
		String[] seasonsReps = representation.split(",");
		int[] seasons = new int[seasonsReps.length];
		
		for (int i = 0; i < seasons.length; i++) {
			String rep = seasonsReps[i];
			seasons[i] = Integer.parseInt(rep.substring(rep.indexOf("*") + 1, rep.lastIndexOf("*")));
		}
		
		return seasons;
	}
	
	public int getAktuelleSaison() {
		return this.saisons[this.aktuelleSaison];
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
	
	public String getDateOfTeam(int matchday, int id) {
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (isSpielplanEntered(matchday, match)) {
				if (getSpiel(matchday, match).home() == id || getSpiel(matchday, match).away() == id)	return getDateOf(matchday, match);
			}
		}
		
		return "n.a.";
	}
	
	public String getDateOf(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.numberOfMatchdays && spiel >= 0 && spiel < this.numberOfMatchesPerMatchday && getDate(matchday) != 0)
			return MyDate.datum(this.getDate(matchday), this.daysSinceDST[this.getKOTIndex(matchday, spiel)])
				+ " " + MyDate.uhrzeit(this.kickoffTimes[this.getKOTIndex(matchday, spiel)]);
		else
			return "nicht terminiert";
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
		getSpiel(matchday, match).setErgebnis(ergebnis);
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
				spieleInNewOrder[j] = new Spiel(this, oldSpiel.away(), oldSpiel.home());
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
			this.mannschaften[i] = new Mannschaft(this.start, i + 1, this, teamsFromFile[i + 1].split(";"));
		}
    }
    
    private void mannschaftenSchreiben() {
    	this.teamsFromFile = new String[this.numberOfTeams + 1];
    	
    	this.teamsFromFile[0] = "" + this.numberOfTeams;
		for (int i = 0; i < this.numberOfTeams; i++) {
			this.mannschaften[i].saveKader();
			this.teamsFromFile[i + 1] = this.mannschaften[i].toString();
		}
		inDatei(this.dateiTeams, this.teamsFromFile);
    }
    
	private void spielplanLaden() {
		try {
			this.spielplanFromFile = ausDatei(this.dateiSpielplan); 
		    
	        int counter = 0;
	        
	        // Anstosszeiten / Spieltermine
	        String allKickoffTimes = this.spielplanFromFile[0];
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
	        	String[] inhalte = this.spielplanFromFile[matchday + 1].split(";");
	        	
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
	            			spiel = new Spiel(this, inhalte[match + 2]);
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
			errorMessage("Kein Spielplan");
			e.printStackTrace();
		}
    }
	
	private void spielplanSchreiben() throws NullPointerException {
		if (this.dateiSpielplan == null) {
			throw new NullPointerException();
		}
		
        this.spielplanFromFile = new String[this.numberOfMatchdays + 1];
        
		String string = this.numberOfKickoffTimes + ";";
		for (int i = 0; i < this.numberOfKickoffTimes; i++) {
			string = string + this.daysSinceDST[i] + "," + this.kickoffTimes[i] + ";";
        }
		this.spielplanFromFile[0] = string;
		
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
            
            this.spielplanFromFile[matchday + 1] = string;
        }
        
        inDatei(this.dateiSpielplan, this.spielplanFromFile);
    }
	
	private void ergebnisplanLaden() {
		try {
			this.ergebnisseFromFile = ausDatei(this.dateiErgebnisse);
	        int counter;
	        
	        for (counter = 0; counter < this.numberOfMatchdays; counter++) {
	        	String inhalte[] = this.ergebnisseFromFile[counter].split(";");
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
			errorMessage("Kein Ergebnisseplan");
			e.printStackTrace();
		}
    }
	
	private void ergebnisplanSchreiben() throws NullPointerException {
		if (this.dateiErgebnisse == null) {
			throw new NullPointerException();
		}
		
        this.ergebnisseFromFile = new String[this.numberOfMatchdays];
        for (int i = 0; i < this.numberOfMatchdays; i++) {
            String string = this.getErgebnisplanRepresentation(i) + ";";
            if (!this.isErgebnisplanFullyEmpty(i)) {
				for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
                	string += getErgebnis(i, j) + ";";
				}
            }
            
            this.ergebnisseFromFile[i] = string;
        }
        
        inDatei(this.dateiErgebnisse, this.ergebnisseFromFile);
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
		rueckgabe = rueckgabe + "A_SAI*" + this.saisons.length + ";";
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
		this.ANZAHL_ABS = Integer.parseInt(daten.substring(daten.indexOf("A_ABS*") + 6, daten.indexOf(";A_SAI*")));
		
		this.saisons = getSeasonsFromRepresentation(daten.substring(daten.indexOf(";S") + 1));
	}
	
	public void checkOS() {
		if (new File(workspaceWIN).isDirectory()) {
//			message("You are running Windows.");
			workspace = workspaceWIN;
		} else if (new File(workspaceMAC).isDirectory()) {
//			message("You have a Mac.");
			workspace = workspaceMAC;
		} else {
//			message("You are running neither OS X nor Windows, probably Linux!");
			workspace = null;
		}
	}
}




