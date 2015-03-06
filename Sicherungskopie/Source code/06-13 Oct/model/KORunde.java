package model;

import java.io.File;

import static util.Utilities.*;

public class KORunde {

	private Start start;
	private Turnier turnier;
	private int id;
	private String name;
	private String shortName;
	
	private int numberOfTeams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchdays;
	private Mannschaft[] mannschaften;
	
	private boolean hasSecondLeg;
	
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
	
	public int getStartDate() {
		return this.startDate;
	}
	
	public int getFinalDate() {
		return this.finalDate;
	}
	
	public Spieltag getSpieltag() {
		return this.spieltag;
	}
	
	public Mannschaft[] getMannschaften() {
		mannschaftenAktualisieren();
		return mannschaften;
	}
	
	public String getTeamsOrigin(int team) {
		return this.teamsOrigins[team];
	}
	
	private void mannschaftenAktualisieren() {
		mannschaften = turnier.getMannschaftenInOrderOfOrigins(teamsOrigins, teamsAreWinners, id);
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
			int today = MyDate.newMyDate() - turnier.getStartDate();
			
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
	
	private boolean isSpielplanFullyEntered(int matchday) {
		for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
			if (!isSpielplanEntered(matchday, match)) 	return false;
		}
		return true;
	}
	
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
	
	private void setErgebnisplanFullyEntered(int matchday, boolean isEntered) {
		for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
			setErgebnisplanEntered(matchday, match, isEntered);
		}
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
		if (!(isErgebnisplanFullyEntered(numberOfMatchdays - 1) && isErgebnisplanFullyEntered(0))) {
			return null;
		}
		
		// find out involved teams
		int teamHomeFirstLeg = getSpiel(0, match - 1).home();
		int teamAwayFirstLeg = getSpiel(0, match - 1).away();
		
		if (hasSecondLeg) {
			// first and second leg don't have to be in the same position on the plan and most likely they aren't!!
			
			// get index of second leg match
			int index = -1;
			for (int i = 0; i < numberOfMatchesPerMatchday && index == -1; i++) {
				if (getSpiel(1, i).home() == teamHomeFirstLeg || getSpiel(1, i).home() == teamAwayFirstLeg) {
					index = i + 1;
				}
			}
			
			if (index != -1) {
				if (getErgebnis(0, match - 1).home() + getErgebnis(0, index - 1).away() > getErgebnis(0, match - 1).away() + getErgebnis(0, index - 1).home()) {
					return teamsOrigins[teamHomeFirstLeg - 1];
				} else if (getErgebnis(0, match - 1).home() + getErgebnis(0, index - 1).away() < getErgebnis(0, match - 1).away() + getErgebnis(0, index - 1).home()) {
					return teamsOrigins[teamAwayFirstLeg - 1];
				} else {
					// looking for a winner through the away-goal rule (e.g. 1:2 and 2:3)
					if (getErgebnis(0, match - 1).home() > getErgebnis(0, index - 1).home()) {
						return teamsOrigins[teamHomeFirstLeg - 1];
					} else if (getErgebnis(0, match - 1).home() < getErgebnis(0, index - 1).home()) {
						return teamsOrigins[teamAwayFirstLeg - 1];
					}
				}
			}
		} else {
			if (getErgebnis(0, match - 1).home() > getErgebnis(0, match - 1).away()) {
				return teamsOrigins[teamHomeFirstLeg - 1];
			} else if (getErgebnis(0, match - 1).home() < getErgebnis(0, match - 1).away()) {
				return teamsOrigins[teamAwayFirstLeg - 1];
			}
		}
		
		// if there is no second leg / result is tied / other problem
		
		return null;
	}
	
	/**
	 * Returns a String representing the origin of the team losing this match/these matches.
	 * @param match The index of the match, beginning from 1
	 * @return
	 */
	public String getOriginOfLoserOf(int match) {
		if (!(isErgebnisplanFullyEntered(numberOfMatchdays - 1) && isErgebnisplanFullyEntered(0))) {
			return null;
		}
		
		// find out involved teams
		int teamHomeFirstLeg = getSpiel(0, match - 1).home();
		int teamAwayFirstLeg = getSpiel(0, match - 1).away();
		
		if (hasSecondLeg) {
			// first and second leg don't have to be in the same position on the plan and most likely they aren't!!
			
			// get index of second leg match
			int index = -1;
			for (int i = 0; i < numberOfMatchesPerMatchday && index == -1; i++) {
				if (getSpiel(1, i).home() == teamHomeFirstLeg || getSpiel(1, i).home() == teamAwayFirstLeg) {
					index = i + 1;
				}
			}
			
			if (index != -1) {
				if (getErgebnis(0, match - 1).home() + getErgebnis(0, index - 1).away() < getErgebnis(0, match - 1).away() + getErgebnis(0, index - 1).home()) {
					return teamsOrigins[teamHomeFirstLeg - 1];
				} else if (getErgebnis(0, match - 1).home() + getErgebnis(0, index - 1).away() > getErgebnis(0, match - 1).away() + getErgebnis(0, index - 1).home()) {
					return teamsOrigins[teamAwayFirstLeg - 1];
				} else {
					// looking for a winner through the away-goal rule (e.g. 1:2 and 2:3)
					if (getErgebnis(0, match - 1).home() < getErgebnis(0, index - 1).home()) {
						return teamsOrigins[teamHomeFirstLeg - 1];
					} else if (getErgebnis(0, match - 1).home() > getErgebnis(0, index - 1).home()) {
						return teamsOrigins[teamAwayFirstLeg - 1];
					}
				}
			}
		} else {
			if (getErgebnis(0, match - 1).home() < getErgebnis(0, match - 1).away()) {
				return teamsOrigins[teamHomeFirstLeg - 1];
			} else if (getErgebnis(0, match - 1).home() > getErgebnis(0, match - 1).away()) {
				return teamsOrigins[teamAwayFirstLeg - 1];
			}
		}
		
		// if there is no second leg / result is tied / other problem
		
		return null;
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
    	
    	spielplanLaden();
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
	        			if (isSpielplanEntered(spieltag, match))	spiel = new Spiel(inhalte[match + 2]);
	        			
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
		String alles = name + ";";
		
		return alles;
	}
	
	private void fromString(String daten) {
		// TODO fromString
		this.name = daten.split(";")[0];
		this.shortName = daten.split(";")[1];
		this.teamsAreWinners = Boolean.parseBoolean(daten.split(";")[2]);
		this.hasSecondLeg = Boolean.parseBoolean(daten.split(";")[3]);
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

