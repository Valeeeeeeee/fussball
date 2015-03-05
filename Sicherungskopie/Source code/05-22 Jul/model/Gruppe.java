package model;
import java.io.File;

import javax.swing.*;

import static util.Utilities.*;

public class Gruppe {
	private int id;
	private Start start;
	
	private int countteams;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchdays;
	private Mannschaft[] mannschaften;
	private Turnier turnier;
	
	/**
	 * [Spieltag][Spiel][Mannschaft]
	 */
	public int[][][] spiele;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisseNeu;
	
	public boolean[] spieleEingetragen;
	public boolean[] ergebnisseEingetragen;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	private String dateiMannschaft;
	private String dateiSpielplan;
	private String dateiErgebnisse;
	private String[] teamsFromFile;
	private String[] spielplanFromFile;
	private String[] ergebnisseFromFile;
	
	private int startDate;
	private int finalDate;
	public int[][] datesAndTimes;
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

		{
            spieltag = new Spieltag(this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
		{
            tabelle = new Tabelle(start, this);
            tabelle.setLocation((1440 - tabelle.getSize().width) / 2, 50);
            tabelle.setVisible(false);
        }
		
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
	
	public int getNumberOfMatchesPerMatchday() {
		return this.numberOfMatchesPerMatchday;
	}
	
	public int getNumberOfTeams() {
		return this.countteams;
	}
	
	public int getNumberOfMatchdays() {
		return this.numberOfMatchdays;	// TODO only for tournaments with no second leg
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
	
	/**
	 * This method returns the team that finished the group stage on this place
	 * @param place A place between 1 and the number of teams
	 * @return The Mannschaft that finished the group stage on that place, null if not finished or out of bounds
	 */
	public Mannschaft getTeamOnPlace(int place) {
		if (place < 1 || place > mannschaften.length)	return null;
		for (int i = 0; i < ergebnisseEingetragen.length; i++) {
			if (!ergebnisseEingetragen[i])	return null;
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
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = MyDate.newMyDate() - 1; // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (today < this.datesAndTimes[0][0]) {
			matchday = 0;
		} else if (today > this.datesAndTimes[this.getNumberOfMatchdays() - 1][0]) {
			matchday = this.getNumberOfMatchdays() - 1;
		} else {
			matchday = 0;
			while (today > this.datesAndTimes[matchday][0]) {
				matchday++;
			}
			if (matchday != 0 && (today - this.datesAndTimes[matchday - 1][0]) < (this.datesAndTimes[matchday][0] - today)) {
				matchday--;
			}
		}
		
		return matchday;
	}
	
	public String getDateOf(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.getNumberOfMatchdays() && spiel >= 0 && spiel < this.getNumberOfMatchdays())
			return MyDate.datum(startDate, this.daysSinceFirstDay[matchday][spiel]) + " " + MyDate.uhrzeit(this.startTime[matchday][spiel]);
		else 
			return "nicht terminiert";
	}
	
	public Ergebnis getErgebnis(int matchday, int match) {
		return ergebnisseNeu[matchday][match];
	}
	
	public void setErgebnisNew(int matchday, int match, int goalsLeft, int goalsRight) {
		ergebnisseNeu[matchday][match] = new Ergebnis(goalsLeft, goalsRight);
	}
	
	public void setErgebnisNew(int matchday, int match, Ergebnis ergebnis) {
		ergebnisseNeu[matchday][match] = ergebnis;
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
			
			setErgebnisNew(matchday, match, result);
			mannschaften[spiele[matchday][match][0] - 1].setResult(matchday, result);
			mannschaften[spiele[matchday][match][1] - 1].setResult(matchday, result);
		}
		
		ergebnisseEingetragen[matchday] = false;
		
		if (spieleEingetragen[matchday]) {
			int spielfrei = 0;
			for (int i = 0; i < mannschaften.length; i++) {
				// falls die Anzahl an Mannschaften ungerade ist: hat die spielfreie Mannschaft (Gegner == 0) auch positiononplan == 0
				if (mannschaften[i].getMatch(matchday)[1] == 0) {
					spielfrei++; // eine spielfreie Mannschaft ist auch korrekt eingetragen
				}
			}
			
			if (spielfrei > 1) {
				// dann ist der Spieltag nicht oder nicht vollständig eingetragen
				return;
			}
			
			if (counter + spielfrei == mannschaften.length)		ergebnisseEingetragen[matchday] = true;
		}
	}
	
	public void laden() {
		dateiErgebnisse = workspace + File.separator + turnier.getName() + File.separator + "Gruppe " + (this.id + 1) + File.separator + "Ergebnisse.txt";
		dateiSpielplan = workspace + File.separator + turnier.getName() + File.separator + "Gruppe " + (this.id + 1) + File.separator + "Spielplan.txt";
		dateiMannschaft = workspace + File.separator + turnier.getName() + File.separator + "Gruppe " + (this.id + 1) + File.separator + "Mannschaften.txt";
		
    	mannschaftenLaden();
    	
    	initializeArrays();
    	
    	spielplanLaden();
		ergebnisseLaden();
	}
	
	public void speichern() {
		this.spielplanSpeichern();
		this.ergebnisseSpeichern();
		this.mannschaftenSpeichern();
	}
	
	public void mannschaftenLaden() {
		this.teamsFromFile = ausDatei(this.dateiMannschaft);
		
		countteams = Integer.parseInt(teamsFromFile[0]);
		numberOfMatchesPerMatchday = countteams / 2;
		numberOfMatchdays = turnier.groupHasSecondLeg() ? 2 * (countteams - 1) : countteams - 1;
		mannschaften = new Mannschaft[countteams];
    	
    	for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i] = new Mannschaft(this.start, i + 1, this.turnier, this); // ((String) jListConfigModel.getElementAt(i)).split(";"));
			mannschaften[i].setName(teamsFromFile[i + 1]);
		}
	}
	
	public void mannschaftenSpeichern() {
		this.teamsFromFile = new String[this.countteams + 1];
    	this.teamsFromFile[0] = "" + this.countteams;
		for (int i = 0; i < this.countteams; i++) {
			this.teamsFromFile[i + 1] = this.mannschaften[i].getName(); //.toString());
		}
		inDatei(this.dateiMannschaft, this.teamsFromFile);
	}
	
	private void initializeArrays() {
    	// Alle Array werden initialisiert
		
		this.datesAndTimes = new int[numberOfMatchdays][numberOfMatchesPerMatchday + 1];
        
        this.spiele = new int[numberOfMatchdays][numberOfMatchesPerMatchday][2];
		this.ergebnisseNeu = new Ergebnis[numberOfMatchdays][numberOfMatchesPerMatchday];
		this.daysSinceFirstDay = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		this.startTime = new int[numberOfMatchdays][numberOfMatchesPerMatchday];
		
		this.spieleEingetragen = new boolean[numberOfMatchdays];
		this.ergebnisseEingetragen = new boolean[numberOfMatchdays];
    }
	
	private void spielplanLaden() {
		try {
			this.spielplanFromFile = ausDatei(this.dateiSpielplan); 
		    
	        int counter;
	        for (counter = 0; counter < this.numberOfMatchdays; counter++) {
	            String[] inhalte = this.spielplanFromFile[counter].split(";");
	            
	            this.spieleEingetragen[counter] = Boolean.parseBoolean(inhalte[0]);
	            
	            int match = 0;
	            if (this.spieleEingetragen[counter]) {
	            	String[] uhrzeiten = inhalte[1].split(":");
            		for (match = 0; match < uhrzeiten.length; match++) {
            			String[] spieldaten = uhrzeiten[match].split(",");
            			daysSinceFirstDay[counter][match] = Integer.parseInt(spieldaten[0]);
            			startTime[counter][match] = Integer.parseInt(spieldaten[1]);
            		}
	            	
	            	for (match = 0; (match + 2) < inhalte.length; match++) {
	            		String[] teams = inhalte[match + 2].split(":");
	                    int teamHOME = Integer.parseInt(teams[0]);
	                    int teamAWAY = Integer.parseInt(teams[1]);
	                    this.spiele[counter][match][0] = teamHOME;
	                    this.spiele[counter][match][1] = teamAWAY;

	                    this.mannschaften[teamHOME - 1].setGegner(counter, Mannschaft.HOME, teamAWAY, match);
	                    this.mannschaften[teamAWAY - 1].setGegner(counter, Mannschaft.AWAY, teamHOME, match);
	            	}
	            }
	            
	            for (int k = match; k < this.numberOfMatchesPerMatchday; k++) {
	            	this.spiele[counter][k][0] = -1;
	            	this.spiele[counter][k][1] = -1;
	            }
	        }
	        
	        while(counter < this.numberOfMatchdays) {
	            for (int k = 0; k < this.numberOfMatchesPerMatchday; k++) {
	            	this.spiele[counter][k][0] = -1;
	            	this.spiele[counter][k][1] = -1;
	            }
	            counter++;
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Kein Spielplan", "", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }

	private void spielplanSpeichern() {
		this.spielplanFromFile = new String[this.numberOfMatchdays];
		
		for (int spieltag = 0; spieltag < numberOfMatchdays; spieltag++) {
			String element = spieleEingetragen[spieltag] + ";";
			if (spieleEingetragen[spieltag]) {
				for (int match = 0; match < daysSinceFirstDay[spieltag].length; match++) {
					element += daysSinceFirstDay[spieltag][match] + "," + startTime[spieltag][match];
					if ((match + 1) < daysSinceFirstDay[spieltag].length)	element += ":";
					else													element += ";";
				}
				
				for (int match = 0; match < spiele[spieltag].length; match++) {
					element += spiele[spieltag][match][0] + ":" + spiele[spieltag][match][1] + ";";
				}
			}
			this.spielplanFromFile[spieltag] = element;
		}
		
		inDatei(this.dateiSpielplan, this.spielplanFromFile);
	}
	
	private void ergebnisseLaden() {
		try {
			this.ergebnisseFromFile = ausDatei(this.dateiErgebnisse);
	        
			int counter;
	        for (counter = 0; counter < this.numberOfMatchdays; counter++) {
	            String[] inhalte = this.ergebnisseFromFile[counter].split(";");
	            
	            this.ergebnisseEingetragen[counter] = Boolean.parseBoolean(inhalte[0]);
	            
	            int match = 0;
	            if (this.spieleEingetragen[counter] && this.ergebnisseEingetragen[counter]) {
	            	for (match = 0; (match + 1) < inhalte.length; match++) {
	                    Ergebnis ergebnis = new Ergebnis(inhalte[match + 1]);
	                    setErgebnisNew(counter, match, ergebnis);
	                    
	                    this.mannschaften[this.spiele[counter][match][0] - 1].setResult(counter, ergebnis);
	                    this.mannschaften[this.spiele[counter][match][1] - 1].setResult(counter, ergebnis);
	            	}
	            }
	            
	            while (match < this.numberOfMatchesPerMatchday) {
                    setErgebnisNew(counter, match, null);
                    match++;
	            }
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Kein Ergebnisseplan", "", JOptionPane.ERROR_MESSAGE);
		}
    }

	private void ergebnisseSpeichern() {
		this.ergebnisseFromFile = new String[numberOfMatchdays];
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			String element = ergebnisseEingetragen[i] + ";";
			if (ergebnisseEingetragen[i]) {
				for (int j = 0; j < ergebnisseNeu[i].length; j++) {
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