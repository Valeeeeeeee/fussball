package model;

import java.io.File;

import static util.Utilities.*;

public class KORunde implements Wettbewerb {

	private Start start;
	private Turnier turnier;
	private int id;
	private String name;
	private String shortName;
	
	private int numberOfTeams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchdays;
	private Mannschaft[] mannschaften;
	private int numberOfTeamsPrequalified;
	private int numberOfTeamsFromPreviousRound;
	private int numberOfTeamsFromOtherCompetition;
	private boolean checkTeamsFromPreviousRound = true;
	
	private boolean hasSecondLeg;
	private boolean isETPossible = true;
	
	private boolean teamsAreWinners;
	private String[] teamsOrigins;
	
    /**
     * [spieltag][spiel]
     */
	private Spiel[][] spielplan;
    /**
	 * [spieltag][spiel]
	 */
	private Ergebnis[][] ergebnisplan;
	
	private boolean[][] spielplanEingetragen;
	private boolean[][] ergebnisplanEingetragen;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
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
    
    private int startDate;
    private int finalDate;
    
    private Spieltag spieltag;
	
	public KORunde(Start start, Turnier turnier, int id, String daten) {
		checkOS();
		
		this.start = start;
		this.turnier = turnier;
		this.id = id;
		
		this.startDate = turnier.getStartDate();
		this.finalDate = turnier.getFinalDate();
		
		fromString(daten);
		
		laden();
	}

	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getMatchdayDescription(int matchday) {
		return turnier.getName() + ", " + name + (numberOfMatchdays != 1 ? ", " + (matchday == 0 ? "Hinspiel" : "Rueckspiel") : "");
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	public int getStartDate() {
		return this.startDate;
	}
	
	public int getFinalDate() {
		return this.finalDate;
	}
	
	public Spieltag getSpieltag() {
		return this.spieltag;
	}
	
	public boolean isETPossible() {
		return this.isETPossible;
	}
	
	public void setCheckTeamsFromPreviousRound(boolean checkTeamsFromPreviousRound) {
		this.checkTeamsFromPreviousRound = checkTeamsFromPreviousRound;
	}

	public Mannschaft[] getMannschaften() {
		if (checkTeamsFromPreviousRound)	mannschaftenAktualisieren();
		return mannschaften;
	}
	
	public String getTeamsOrigin(int team) {
		return this.teamsOrigins[team];
	}
	
	public Mannschaft getPrequalifiedTeam(int index) {
		if (index >= 0 && index < this.numberOfTeamsPrequalified)	return this.mannschaften[index];
		else														return null;
	}
	
	private void mannschaftenAktualisieren() {
		String[] partOfOrigins = new String[numberOfTeamsFromPreviousRound];
		for (int i = 0; i < partOfOrigins.length; i++) {
			partOfOrigins[i] = teamsOrigins[numberOfTeamsPrequalified + i];
		}
		Mannschaft[] prevRoundTeams = turnier.getMannschaftenInOrderOfOrigins(partOfOrigins, teamsAreWinners, id);
		
		for (int i = 0; i < numberOfTeamsFromPreviousRound; i++) {
			mannschaften[i + numberOfTeamsPrequalified] = prevRoundTeams[i];
			log((i + numberOfTeamsPrequalified) + ": " + mannschaften[i + numberOfTeamsPrequalified]);
		}
		
		
	}
	
	private void testGNOTFOC() {
		String[] otherComp = new String[] {"CL2014GA3"};
		
		log("Test:");
		for (int i = 0; i < otherComp.length; i++) {
			getNameOfTeamFromOtherCompetition(otherComp[i]);
		}
		log();
	}
	
	private String getNameOfTeamFromOtherCompetition(String origin) {
		// TODO get the name from file
		
		String fileName = start.getTournamentWorkspaceFromShortName(origin.substring(0, 2), Integer.parseInt(origin.substring(2,6)));
		log(fileName);
//		String[] teams = ausDatei(fileName);
		
		return origin;
	}
	
	public int getNumberOfTeams() {
		return this.numberOfTeams;
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return this.numberOfMatchesPerMatchday;
	}
	
	public boolean hasSecondLeg() {
		return this.hasSecondLeg;
	}
	
	public boolean teamsAreWinners() {
		return this.teamsAreWinners;
	}
	
	public int getCurrentMatchday() {
		int matchday = -1;
		if (this.numberOfMatchdays == 2) {
			int today = MyDate.newMyDate();
			
			if (today < getDate(0, 0)) {
				matchday = 0;
			} else if (today > getDate(1, 0) && !isSpielplanFullyEmpty(1)) {
				matchday = 1;
			} else {
				matchday = 1;
				
				if (MyDate.difference(getDate(0, 0), today) < MyDate.difference(today, getDate(1, 0))) {
					matchday--;
				}
			}
		} else if (this.numberOfMatchdays == 1) {
			matchday = 0;
		}
		
		return matchday;
	}
	
	public String getDateOf(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.numberOfMatchdays && spiel >= 0 && spiel < this.numberOfMatchesPerMatchday)
			return MyDate.datum(getDate(matchday, spiel)) + " " + MyDate.uhrzeit(getTime(matchday, spiel));
		else 
			return "nicht terminiert";
	}
	
	public int getDate(int matchday, int match) {
		return MyDate.verschoben(this.startDate, this.daysSinceFirstDay[matchday][match]);
	}
	
	public int getTime(int matchday, int match) {
		return this.startTime[matchday][match];
	}
	
	public void setDate(int matchday, int match, int myDate) {
		this.daysSinceFirstDay[matchday][match] = MyDate.difference(startDate, myDate);
	}
	
	public void setTime(int matchday, int match, int myTime) {
		this.startTime[matchday][match] = myTime;
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
	
	public void setSpielplanFullyEntered(int matchday, boolean isEntered) {
		for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
			setSpielplanEntered(matchday, match, isEntered);
		}
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
		if (representation.equals("true")) {
			representation = "";
			for (int match = 0; match < numberOfMatchesPerMatchday; match++)	representation += "t";
		} else if (representation.equals("false")) {
			representation = "";
			for (int match = 0; match < numberOfMatchesPerMatchday; match++)	representation += "f";
		}
		
		if (representation.length() != numberOfMatchesPerMatchday)	return;
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (representation.charAt(match) == 't')		setSpielplanEntered(matchday, match, true);
			else if (representation.charAt(match) == 'f')	setSpielplanEntered(matchday, match, false);
		}
	}
	
	// Ergebnisplan eingetragen
	
	private boolean isErgebnisplanFullyEntered(int matchday) {
		for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
			if (!isErgebnisplanEntered(matchday, match)) 	return false;
		}
		return true;
	}
	
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
		if (representation.equals("true")) {
			representation = "";
			for (int match = 0; match < numberOfMatchesPerMatchday; match++)	representation += "t";
		} else if (representation.equals("false")) {
			representation = "";
			for (int match = 0; match < numberOfMatchesPerMatchday; match++)	representation += "f";
		}
		
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
		if (spiel != null)	setSpielplanEntered(matchday, match, true);
		else				setSpielplanEntered(matchday, match, false);
		spielplan[matchday][match] = spiel;
	}
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[this.numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[this.numberOfMatchesPerMatchday];
		
		for (int m = 0; m < this.numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < this.numberOfMatchesPerMatchday; m2++) {
				if (getDate(matchday, m2) > getDate(matchday, m))															hilfsarray[m2]++;
				else if (getDate(matchday, m2) == getDate(matchday, m) && getTime(matchday, m2) >= getTime(matchday, m))	hilfsarray[m2]++;
				else																										hilfsarray[m]++;
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
		int[] oldDaysSinceFirstDay = new int[this.numberOfMatchesPerMatchday];
		int[] oldStartTime = new int[this.numberOfMatchesPerMatchday];
		
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldSpielplan[match] = getSpiel(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldErgebnisplan[match] = getErgebnis(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldSpielplanEingetragen[match] = isSpielplanEntered(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldErgebnisplanEingetragen[match] = isErgebnisplanEntered(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldDaysSinceFirstDay[match] = getDate(matchday, match);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	oldStartTime[match] = getTime(matchday, match);
		
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setSpiel(matchday, match, oldSpielplan[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setErgebnis(matchday, match, oldErgebnisplan[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setSpielplanEntered(matchday, match, oldSpielplanEingetragen[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setErgebnisplanEntered(matchday, match, oldErgebnisplanEingetragen[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setDate(matchday, match, oldDaysSinceFirstDay[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < this.numberOfMatchesPerMatchday; match++)	setTime(matchday, match, oldStartTime[oldIndicesInNewOrder[match]]);
	}
	
	public void ergebnisseSichern() {
		int matchday = spieltag.getCurrentMatchday();
		
		for (int match = 0; match < spieltag.getNumberOfMatches(); match++) {
			if (isSpielplanEntered(matchday, match)) {
				Ergebnis result = spieltag.getErgebnis(match);
				
				setErgebnis(matchday, match, result);
//				TODO aktuell werden fuer KO-Phasen Matches keine Ergebnisse an die Mannschaften weitergeleitet
//				mannschaften[spiele[matchday][match][0] - 1].setResult(matchday, result);
//				mannschaften[spiele[matchday][match][1] - 1].setResult(matchday, result);
			}
		}
	}
	
	/**
	 * Returns a String representing the origin of the team winning this match/these matches.
	 * @param match The index of the match, beginning from 1
	 * @return
	 */
	public String getOriginOfWinnerOf(int match) {
		int index;
		return ((index = getIndexOf(match, true)) == 0 ? null : teamsOrigins[index - 1]);
	}
	
	/**
	 * Returns a String representing the origin of the team losing this match/these matches.
	 * @param match The index of the match, beginning from 1
	 * @return
	 */
	public String getOriginOfLoserOf(int match) {
		int index;
		return ((index = getIndexOf(match, false)) == 0 ? null : teamsOrigins[index - 1]);
	}
	
	public int getIndexOf(int match, boolean isWinnerRequested) {
		if (!isErgebnisplanEntered(0, match - 1)) {
			return 0;
		}
		
		// find out involved teams
		int teamHomeFirstLeg = getSpiel(0, match - 1).home();
		int teamAwayFirstLeg = getSpiel(0, match - 1).away();
		Ergebnis firstLeg = getErgebnis(0, match - 1);
		
		if (hasSecondLeg) {
			// first and second leg don't have to be in the same position on the plan and most likely they aren't!!
			
			// get index of second leg match
			int index = -1;
			for (int i = 0; i < numberOfMatchesPerMatchday && index == -1; i++) {
				if (getSpiel(1, i) != null && (getSpiel(1, i).home() == teamHomeFirstLeg || getSpiel(1, i).home() == teamAwayFirstLeg)) {
					index = i + 1;
				}
			}
			
			if (index != -1 && isErgebnisplanEntered(numberOfMatchdays - 1, index - 1)) {
				Ergebnis secondLeg = getErgebnis(1, index - 1);
				if (firstLeg.home() + secondLeg.away() > firstLeg.away() + secondLeg.home()) {
					return isWinnerRequested ? teamHomeFirstLeg : teamAwayFirstLeg;
				} else if (firstLeg.home() + secondLeg.away() < firstLeg.away() + secondLeg.home()) {
					return isWinnerRequested ? teamAwayFirstLeg : teamHomeFirstLeg;
				} else {
					// looking for a winner through the away-goal rule (e.g. 1:2 and 2:3)
					if (firstLeg.away() > secondLeg.away()) {
						return isWinnerRequested ? teamAwayFirstLeg : teamHomeFirstLeg;
					} else if (firstLeg.away() < secondLeg.away()) {
						return isWinnerRequested ? teamHomeFirstLeg : teamAwayFirstLeg;
					}
				}
			}
		} else {
			if (firstLeg.home() > firstLeg.away()) {
				return isWinnerRequested ? teamHomeFirstLeg : teamAwayFirstLeg;
			} else if (firstLeg.home() < firstLeg.away()) {
				return isWinnerRequested ? teamAwayFirstLeg : teamHomeFirstLeg;
			}
		}
		
		// if there is no second leg / result is tied / other problem
		return 0;
	}
	
	private void laden() {
		String saison;
		if (turnier.isSTSS())	saison = turnier.getAktuelleSaison() + "_" + (turnier.getAktuelleSaison() + 1);
		else					saison = "" + turnier.getAktuelleSaison();
		
		String root = workspace + File.separator + turnier.getName() + File.separator + saison + File.separator + this.name;
		
		dateiErgebnisse = root + File.separator + "Ergebnisse.txt";
		dateiMannschaft = root + File.separator + "Mannschaften.txt";
		dateiSpielplan = root + File.separator + "Spielplan.txt";
		
    	mannschaftenLaden();
    	
    	initializeArrays();
    	
    	setCheckTeamsFromPreviousRound(false);
    	spielplanLaden();
    	setCheckTeamsFromPreviousRound(true);
		ergebnisseLaden();
		
		{
            spieltag = new Spieltag(this.start, this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
		
//		testIsSpielplanEntered();
//		testIsErgebnisplanEntered();
	}
	
	public void speichern() {
		this.spielplanSpeichern();
		this.ergebnisseSpeichern();
		this.mannschaftenSpeichern();
	}
	
	private void initializeArrays() {
        this.daysSinceFirstDay = new int[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        this.startTime = new int[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
        
        this.spielplan = new Spiel[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
		this.spielplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
		
    	this.ergebnisplan = new Ergebnis[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
    	this.ergebnisplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
	}
	
	private void mannschaftenLaden() {
		teamsFromFile = ausDatei(dateiMannschaft);
		
		this.numberOfTeams = teamsFromFile.length;
		mannschaften = new Mannschaft[numberOfTeams];
		this.numberOfMatchdays = this.hasSecondLeg ? 2 : 1;
		this.numberOfMatchesPerMatchday = numberOfTeams / 2;
		
		teamsOrigins = new String[this.numberOfTeams];
		for (int i = 0; i < teamsOrigins.length; i++) {
			teamsOrigins[i] = teamsFromFile[i];
		}
		
		for (int i = 0; i < numberOfTeamsPrequalified; i++) {
			mannschaften[i] = new Mannschaft(this.start, i, this.turnier, this);
			mannschaften[i].setName(teamsOrigins[i]);
		}
		
		testGNOTFOC();
		
		for (int i = numberOfTeams - numberOfTeamsFromOtherCompetition; i < numberOfTeamsFromOtherCompetition; i++) {
			mannschaften[i] = new Mannschaft(this.start, i, this.turnier, this);
			mannschaften[i].setName(getNameOfTeamFromOtherCompetition(teamsOrigins[i]));
		}
		
		mannschaftenAktualisieren();
	}
	
	public void mannschaftenSpeichern() {
		this.teamsFromFile = new String[this.numberOfTeams];
		for (int i = 0; i < this.numberOfTeams; i++) {
			this.teamsFromFile[i] = this.teamsOrigins[i]; //.toString());
		}
		inDatei(this.dateiMannschaft, this.teamsFromFile);
	}
	
	private void spielplanLaden() {
		try {
			spielplanFromFile = ausDatei(dateiSpielplan);
			
	    	for (int spieltag = 0; spieltag < spielplanFromFile.length; spieltag++) {
	        	String[] inhalte = spielplanFromFile[spieltag].split(";");
	        	
	        	this.setSpielplanEnteredFromRepresentation(spieltag, inhalte[0]);
	        	
	        	int match = 0;
	        	if (!this.isSpielplanFullyEmpty(spieltag)) {
	        		// Spieltagsdaten
	        		String[] uhrzeiten = inhalte[1].split(":");
	        		for (match = 0; match < uhrzeiten.length; match++) {
	        			String spieldaten[] = uhrzeiten[match].split(",");
	        			daysSinceFirstDay[spieltag][match] = Integer.parseInt(spieldaten[0]);
	        			startTime[spieltag][match] = Integer.parseInt(spieldaten[1]);
	        		}
	        		
	        		// Herkunften der Mannschaften
	        		for (match = 0; (match + 2) < inhalte.length; match++) {
	        			Spiel spiel = null;
	        			if (isSpielplanEntered(spieltag, match))	spiel = new Spiel(this, spieltag, inhalte[match + 2]);
	        			
	        			setSpiel(spieltag, match, spiel);
	        		}
	        	}
	            
	            while(match < this.numberOfMatchesPerMatchday) {
	                setSpiel(spieltag, match, null);
	                match++;
	            }
	    	}
		} catch (Exception e) {
			errorMessage("Kein Spielplan");
			e.printStackTrace();
		}
	}
	
	private void spielplanSpeichern() {
		this.spielplanFromFile = new String[this.numberOfMatchdays];
		
		for (int spieltag = 0; spieltag < numberOfMatchdays; spieltag++) {
			String element = getSpielplanRepresentation(spieltag) + ";";
			if (!isSpielplanFullyEmpty(spieltag)) {
				for (int match = 0; match < daysSinceFirstDay[spieltag].length; match++) {
					element += daysSinceFirstDay[spieltag][match] + "," + startTime[spieltag][match];
					if ((match + 1) < daysSinceFirstDay[spieltag].length)	element += ":";
					else													element += ";";
				}
				
				for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
					element += getSpiel(spieltag, match) + ";";
				}
			}
			this.spielplanFromFile[spieltag] = element;
		}
		
		inDatei(this.dateiSpielplan, this.spielplanFromFile);
	}
	
	private void ergebnisseLaden() {
		try {
			this.ergebnisseFromFile = ausDatei(this.dateiErgebnisse);
	        
	        for (int counter = 0; counter < this.numberOfMatchdays; counter++) {
	            String[] inhalte = this.ergebnisseFromFile[counter].split(";");
	            
	            this.setErgebnisplanEnteredFromRepresentation(counter, inhalte[0]);
	            
	            int match = 0;
	            if (!this.isSpielplanFullyEmpty(counter) && !this.isErgebnisplanFullyEmpty(counter)) {
	            	for (match = 0; (match + 1) < inhalte.length; match++) {
	        			if (isSpielplanEntered(counter, match)) {
		            		Ergebnis ergebnis = null;
		        			if (isErgebnisplanEntered(counter, match))	ergebnis = new Ergebnis(inhalte[match + 1]);
		        			
		        			setErgebnis(counter, match, ergebnis);
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
	
	private void ergebnisseSpeichern() {
		this.ergebnisseFromFile = new String[numberOfMatchdays];
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			String element = getErgebnisplanRepresentation(i) + ";";
			if (!isErgebnisplanFullyEmpty(i)) {
				for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
					element += getErgebnis(i, j) + ";";
				}
			}
			this.ergebnisseFromFile[i] = element;
		}
		
		inDatei(this.dateiErgebnisse, this.ergebnisseFromFile);
	}
	
	public String toString() {
		String alles = this.name + ";";
		alles += this.shortName + ";";
		alles += this.teamsAreWinners + ";";
		alles += this.hasSecondLeg + ";";
		alles += this.numberOfTeamsPrequalified + ";";
		alles += this.numberOfTeamsFromPreviousRound + ";";
		alles += this.numberOfTeamsFromOtherCompetition + ";";
		
		return alles;
	}
	
	private void fromString(String daten) {
		// TODO fromString
		String[] dataSplit = daten.split(";");
		this.name = dataSplit[0];
		this.shortName = dataSplit[1];
		this.teamsAreWinners = Boolean.parseBoolean(dataSplit[2]);
		this.hasSecondLeg = Boolean.parseBoolean(dataSplit[3]);
		this.numberOfTeamsPrequalified = Integer.parseInt(dataSplit[4]);
		this.numberOfTeamsFromPreviousRound = Integer.parseInt(dataSplit[5]);
		this.numberOfTeamsFromOtherCompetition = Integer.parseInt(dataSplit[6]);
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
