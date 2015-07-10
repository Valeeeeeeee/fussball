package model;
import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class Gruppe implements Wettbewerb {
	private boolean debug = false;
	private int id;
	private boolean isQ;
	private String name;
	private Start start;
	
	private int numberOfTeams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int numberOfMatchdays;
	private Mannschaft[] mannschaften;
	private Turnier turnier;
	private TurnierSaison season;
	private boolean isETPossible = false;
	private boolean goalDifference = true;
	private boolean teamsHaveKader = false;
	
	/**
	 * [Spieltag][Spiel]
	 */
	private Spiel[][] spielplan;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisplan;
	
	private boolean[][] spielplanEingetragen;
	private boolean[][] ergebnisplanEingetragen;
	
	private String workspace;
	
	private String dateiMannschaft;
	private String dateiSpielplan;
	private String dateiErgebnisse;
	private ArrayList<String> teamsFromFile;
	private ArrayList<String> spielplanFromFile;
	private ArrayList<String> ergebnisseFromFile;
	
	private int startDate;
	private int finalDate;
    private int[][] daysSinceFirstDay;
    private int[][] startTime;
	
	private Spieltag spieltag;
	private Tabelle tabelle;
	
	public Gruppe(Start start, TurnierSaison season, int id, boolean isQ) {
		this.start = start;
		
		this.id = id;
		this.isQ = isQ;
		name = "Gruppe " + start.getAlphabet()[id];
		
		this.season = season;
		this.turnier = season.getTurnier();
		this.startDate = season.getStartDate();
		this.finalDate = season.getFinalDate();
		
		this.laden();
		
		if (debug)	testAusgabePlatzierungen();
	}
	
	public String getWorkspace() {
		return this.workspace;
	}
	
	public int getID() {
		return this.id;
	}
	
	public boolean isQualification() {
		return isQ;
	}
	
	public String getMatchdayDescription(int matchday) {
		String isQuali = isQ ? ", Qualifikation" : "";
		return season.getDescription() + isQuali + ", " + name + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getTournamentName() {
		return turnier.getName();
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return this.numberOfMatchesPerMatchday;
	}
	
	public int getNumberOfTeams() {
		return this.numberOfTeams;
	}
	
	public int getNumberOfMatchdays() {
		return this.numberOfMatchdays;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return numberOfMatchesAgainstSameOpponent;
	}
	
	public boolean teamsHaveKader() {
		return this.teamsHaveKader;
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
	
	public Tabelle getTabelle() {
		return this.tabelle;
	}
	
	public Mannschaft[] getMannschaften() {
		return this.mannschaften;
	}
	
	public boolean isETPossible() {
		return this.isETPossible;
	}
	
	public boolean useGoalDifference() {
		return this.goalDifference;
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
	
	public String getDateAndTime(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.numberOfMatchdays && spiel >= 0 && spiel < this.numberOfMatchesPerMatchday)
			return MyDate.datum(getDate(matchday, spiel)) + " " + MyDate.uhrzeit(getTime(matchday, spiel));
		else 
			return "nicht terminiert";
	}
	
	public int getDate(int matchday, int match) {
		return MyDate.verschoben(startDate, daysSinceFirstDay[matchday][match]);
	}
	
	public int getTime(int matchday, int match) {
		return startTime[matchday][match];
	}
	
	public void setDate(int matchday, int match, int myDate) {
		daysSinceFirstDay[matchday][match] = MyDate.difference(startDate, myDate);
	}
	
	public void setTime(int matchday, int match, int myTime) {
		startTime[matchday][match] = myTime;
	}
	
	/**
	 * This method returns the team that finished the group stage on this place
	 * @param place A place between 1 and the number of teams
	 * @return The Mannschaft that finished the group stage on that place, null if not finished or out of bounds
	 */
	public Mannschaft getTeamOnPlace(int place) {
		if (place < 1 || place > mannschaften.length)	return null;
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int match = 0; match < this.getNumberOfMatchesPerMatchday(); match++) {
				if (!isErgebnisplanEntered(matchday, match)) 	return null;
			}
		}
		
		tabelle.aktualisieren();
		for (Mannschaft ms : mannschaften) {
			if (ms.get(0, numberOfMatchdays - 1, Tabellenart.COMPLETE) == place - 1)		return ms;
		}
		
		return null;
	}
	
	public int getIndexOfMannschaft(String name) {
    	for (Mannschaft ms : this.mannschaften) {
    		if (ms.getName().equals(name)) {
    			return ms.getId();
    		}
    	}
    	return -1;
    }
	
	public Mannschaft getTeamWithName(String teamsName) {
    	return mannschaften[getIndexOfMannschaft(teamsName) - 1];
    }
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = MyDate.verschoben(MyDate.newMyDate(), -1) ; // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (today < getDate(0, 0)) {
			matchday = 0;
		} else if (today > getDate(this.getNumberOfMatchdays() - 1, 0) && !this.isSpielplanFullyEmpty(this.getNumberOfMatchdays() - 1)) {
			matchday = this.getNumberOfMatchdays() - 1;
		} else {
			matchday = 0;
			while (today > getDate(matchday, 0) && !this.isSpielplanFullyEmpty(matchday)) {
				matchday++;
			}
			if (matchday != 0 && MyDate.difference(getDate(matchday - 1, 0), today) <= MyDate.difference(today, getDate(matchday, 0))) {
				matchday--;
			}
		}
		
		return matchday;
	}
	
	private void testAusgabePlatzierungen() {
		log("\nGruppe " + (this.id + 1) + ":");
		
		for (int i = 1; i <= mannschaften.length; i++) {
			try {
				log(i + ". " + getTeamOnPlace(i).getName());
			} catch (NullPointerException npe) {
				log("  Mannschaft: " + mannschaften[i - 1].getName());
			}
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
				mannschaften[getSpiel(matchday, match).home() - 1].setResult(matchday, result);
				mannschaften[getSpiel(matchday, match).away() - 1].setResult(matchday, result);
			}
		}
	}
	
	public void laden() {
		String isQuali = isQ ? "Qualifikation" + File.separator : "";
		workspace = season.getWorkspace() + isQuali + name + File.separator;
		
		dateiErgebnisse = workspace + "Ergebnisse.txt";
		dateiSpielplan = workspace + "Spielplan.txt";
		dateiMannschaft = workspace + "Mannschaften.txt";
		
    	mannschaftenLaden();
    	
    	initializeArrays();
    	
    	spielplanLaden();
		ergebnisseLaden();

		{
            spieltag = new Spieltag(this.start, this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
		{
            tabelle = new Tabelle(start, this);
            tabelle.setLocation((1440 - tabelle.getSize().width) / 2, 50);
            tabelle.setVisible(false);
        }
		
//		testIsSpielplanEntered();
//		testIsErgebnisplanEntered();
	}
	
	public void speichern() {
		this.spielplanSpeichern();
		this.ergebnisseSpeichern();
		this.mannschaftenSpeichern();
	}
	
	public void mannschaftenLaden() {
		this.teamsFromFile = ausDatei(this.dateiMannschaft);
		
		numberOfTeams = this.teamsFromFile.size();
		numberOfMatchesPerMatchday = numberOfTeams / 2;
		numberOfMatchesAgainstSameOpponent = (isQ ? season.hasSecondLegQGroupStage() : season.hasSecondLegGroupStage()) ? 2 : 1;
		numberOfMatchdays = 2 * ((numberOfTeams + 1) / 2) - 1;
		numberOfMatchdays *= numberOfMatchesAgainstSameOpponent;
		mannschaften = new Mannschaft[numberOfTeams];
    	
    	for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i] = new Mannschaft(start, i + 1, season, this, teamsFromFile.get(i));
		}
	}
	
	public void mannschaftenSpeichern() {
		this.teamsFromFile = new ArrayList<>();
		for (int i = 0; i < this.numberOfTeams; i++) {
			this.teamsFromFile.add(this.mannschaften[i].toString());
		}
		inDatei(this.dateiMannschaft, this.teamsFromFile);
	}
	
	public String[] getRanks() {
		String[] ranks = new String[this.numberOfTeams];
		
		for (int i = 0; i < ranks.length; i++) {
			String id = "G" + alphabet[this.id] + (i + 1);
			try {
				ranks[i] = id + ": " + getTeamOnPlace(i + 1).getName();
			} catch (NullPointerException npe) {
				ranks[i] = id + ": " + turnier.getShortName() + turnier.getCurrentSeason() + id;
			}
		}
		
		return ranks;
	}
	
	private void initializeArrays() {
    	// Alle Array werden initialisiert
		
        this.spielplan = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		this.ergebnisplan = new Ergebnis[numberOfMatchdays][numberOfMatchesPerMatchday];
		this.daysSinceFirstDay = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		this.startTime = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		this.spielplanEingetragen = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
		this.ergebnisplanEingetragen = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
    }
	
	private void spielplanLaden() {
		try {
			this.spielplanFromFile = ausDatei(this.dateiSpielplan); 
		    
	        for (int matchday = 0; matchday < this.numberOfMatchdays; matchday++) {
	            String[] inhalte = this.spielplanFromFile.get(matchday).split(";");
	            
	            this.setSpielplanEnteredFromRepresentation(matchday, inhalte[0]);
	            
	            int match = 0;
	            if (!this.isSpielplanFullyEmpty(matchday)) {
	            	String[] uhrzeiten = inhalte[1].split(":");
            		for (match = 0; match < uhrzeiten.length; match++) {
            			String[] spieldaten = uhrzeiten[match].split(",");
            			daysSinceFirstDay[matchday][match] = Integer.parseInt(spieldaten[0]);
            			startTime[matchday][match] = Integer.parseInt(spieldaten[1]);
            		}
	            	
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
			errorMessage("Kein Spielplan");
			e.printStackTrace();
		}
    }

	private void spielplanSpeichern() {
		this.spielplanFromFile = new ArrayList<>();
		
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
			this.spielplanFromFile.add(element);
		}
		
		inDatei(this.dateiSpielplan, this.spielplanFromFile);
	}
	
	private void ergebnisseLaden() {
		try {
			this.ergebnisseFromFile = ausDatei(this.dateiErgebnisse);
	        
	        for (int matchday = 0; matchday < this.numberOfMatchdays; matchday++) {
	            String[] inhalte = this.ergebnisseFromFile.get(matchday).split(";");
	            this.setErgebnisplanEnteredFromRepresentation(matchday, inhalte[0]);
	            
	            int match = 0;
	            if (!this.isSpielplanFullyEmpty(matchday) && !this.isErgebnisplanFullyEmpty(matchday)) {
	            	for (match = 0; (match + 1) < inhalte.length; match++) {
	        			if (isSpielplanEntered(matchday, match)) {
		            		Ergebnis ergebnis;
		        			if (isErgebnisplanEntered(matchday, match))	ergebnis = new Ergebnis(inhalte[match + 1]);
		        			else										ergebnis = null;
		        			
		        			setErgebnis(matchday, match, ergebnis);
		        			
	        				this.mannschaften[getSpiel(matchday, match).home() - 1].setResult(matchday, ergebnis);
		                    this.mannschaften[getSpiel(matchday, match).away() - 1].setResult(matchday, ergebnis);
	        			}
		            }
	            }
	            
	            while (match < this.numberOfMatchesPerMatchday) {
                    setErgebnis(matchday, match, null);
                    match++;
	            }
	        }
		} catch (Exception e) {
			errorMessage("Kein Ergebnisseplan");
			e.printStackTrace();
		}
    }

	private void ergebnisseSpeichern() {
		this.ergebnisseFromFile = new ArrayList<>();
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			String element = getErgebnisplanRepresentation(i) + ";";
			if (!isErgebnisplanFullyEmpty(i)) {
				for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
					element += getErgebnis(i, j) + ";";
				}
			}
			this.ergebnisseFromFile.add(element);
		}
		
		inDatei(this.dateiErgebnisse, this.ergebnisseFromFile);
	}
	
}
