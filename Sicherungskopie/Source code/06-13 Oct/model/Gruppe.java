package model;
import java.io.File;

import static util.Utilities.*;

public class Gruppe {
	private int id;
	private Start start;
	
	private int numberOfTeams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchdays;
	private Mannschaft[] mannschaften;
	private Turnier turnier;
	
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
	private String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	private String dateiMannschaft;
	private String dateiSpielplan;
	private String dateiErgebnisse;
	private String[] teamsFromFile;
	private String[] spielplanFromFile;
	private String[] ergebnisseFromFile;
	
	private int startDate;
	private int finalDate;
	private int[][] datesAndTimes;
    private int[][] daysSinceFirstDay;
    private int[][] startTime;
	
	private Spieltag spieltag;
	private Tabelle tabelle;
	
	public Gruppe(Start start, int id, Turnier turnier) {
		checkOS();
		
		this.id = id;
		
		this.start = start;
		
		this.turnier = turnier;
		this.startDate = turnier.getStartDate();
		this.finalDate = turnier.getFinalDate();
		
		System.out.println("\nGruppe " + (this.id + 1) + ":");
		
		this.laden();
		
		for (int i = 1; i <= mannschaften.length; i++) {
			try {
				System.out.println(i + ". " + getTeamOnPlace(i).getName());
			} catch (NullPointerException npe) {
				System.out.println("  Mannschaft: " + mannschaften[i - 1].getName());
			}
		}
		
	}
	
	public int getID() {
		return this.id;
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
	
	public String getDateOf(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.numberOfMatchdays && spiel >= 0 && spiel < this.numberOfMatchesPerMatchday)
			return MyDate.datum(getDate(matchday, spiel)) + " " + MyDate.uhrzeit(getTime(matchday, spiel));
		else 
			return "nicht terminiert";
	}
	
	public int getDate(int matchday, int match) {
		return MyDate.verschoben(startDate, daysSinceFirstDay[matchday][match]);
	}
	
	public String getDateOfTeam(int matchday, int id) {
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			if (isSpielplanEntered(matchday, match)) {
				if (getSpiel(matchday, match).home() == id || getSpiel(matchday, match).away() == id)	return getDateOf(matchday, match);
			}
		}
		
		return "n.a.";
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
			if (ms.get(0) == place - 1)		return ms;
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
	
	public Mannschaft getTeamwithName(String teamsName) {
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
			if (matchday != 0 && MyDate.difference(getDate(matchday - 1, 0), today) < MyDate.difference(today, getDate(matchday, 0))) {
				matchday--;
			}
		}
		
		return matchday;
	}
	
	@SuppressWarnings("unused")
	private void testIsSpielplanEntered() {
		log("\n\nTest-Ausgabe des spielplanEingetragen-Arrays:\n");
		for (int i = 0; i < numberOfMatchdays; i++) {
			log((i + 1) + ". matchday is" + (!isSpielplanFullyEmpty(i) ? "" : " not") + " fully empty.");
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
				mannschaften[getSpiel(matchday, match).home() - 1].setResult(matchday, result);
				mannschaften[getSpiel(matchday, match).away() - 1].setResult(matchday, result);
			}
		}
	}
	
	public void laden() {
		String saison;
		if (turnier.isSTSS())	saison = turnier.getAktuelleSaison() + "_" + (turnier.getAktuelleSaison() + 1);
		else					saison = "" + turnier.getAktuelleSaison();
		
		String root = workspace + File.separator + turnier.getName() + File.separator + saison + File.separator + "Gruppe " + (this.id + 1);
		
		dateiErgebnisse = root + File.separator + "Ergebnisse.txt";
		dateiSpielplan = root + File.separator + "Spielplan.txt";
		dateiMannschaft = root + File.separator + "Mannschaften.txt";
		
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
		
		numberOfTeams = this.teamsFromFile.length;
		numberOfMatchesPerMatchday = numberOfTeams / 2;
		numberOfMatchdays = 2 * ((numberOfTeams + 1) / 2) - 1;
		numberOfMatchdays *= turnier.groupHasSecondLeg() ? 2 : 1;
		mannschaften = new Mannschaft[numberOfTeams];
    	
    	for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i] = new Mannschaft(this.start, i + 1, this.turnier, this); // ((String) jListConfigModel.getElementAt(i)).split(";"));
			mannschaften[i].setName(teamsFromFile[i]);
		}
	}
	
	public void mannschaftenSpeichern() {
		this.teamsFromFile = new String[this.numberOfTeams];
		for (int i = 0; i < this.numberOfTeams; i++) {
			this.teamsFromFile[i] = this.mannschaften[i].getName(); //.toString());
		}
		inDatei(this.dateiMannschaft, this.teamsFromFile);
	}
	
	private void initializeArrays() {
    	// Alle Array werden initialisiert
		
		this.datesAndTimes = new int[numberOfMatchdays][numberOfMatchesPerMatchday + 1];
        
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
		    
	        for (int counter = 0; counter < this.numberOfMatchdays; counter++) {
	            String[] inhalte = this.spielplanFromFile[counter].split(";");
	            
	            this.setSpielplanEnteredFromRepresentation(counter, inhalte[0]);
	            
	            int match = 0;
	            if (!this.isSpielplanFullyEmpty(counter)) {
	            	String[] uhrzeiten = inhalte[1].split(":");
            		for (match = 0; match < uhrzeiten.length; match++) {
            			String[] spieldaten = uhrzeiten[match].split(",");
            			daysSinceFirstDay[counter][match] = Integer.parseInt(spieldaten[0]);
            			startTime[counter][match] = Integer.parseInt(spieldaten[1]);
            		}
	            	
	            	for (match = 0; (match + 2) < inhalte.length; match++) {
	            		Spiel spiel = null;
	            		
	            		if (isSpielplanEntered(counter, match)) {
	            			spiel = new Spiel(inhalte[match + 2]);
	            			
	                    	this.mannschaften[spiel.home() - 1].setMatch(counter, spiel);
		                    this.mannschaften[spiel.away() - 1].setMatch(counter, spiel);
	            		}
	            		
	                    setSpiel(counter, match, spiel);
					}
	            }
	            
	            while(match < this.numberOfMatchesPerMatchday) {
	                setSpiel(counter, match, null);
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

