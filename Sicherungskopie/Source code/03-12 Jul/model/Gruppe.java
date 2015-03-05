package model;
import java.io.File;

import javax.swing.*;

import static util.Utilities.*;

public class Gruppe {
	private int id;
	private Start start;
	
	int countteams;
	int numberOfMatchesPerMatchday;
	int numberOfMatchdays;
	public Mannschaft[] mannschaften;
	private Turnier turnier;
	
	/**
	 * [Spieltag][Spiel][Mannschaft]
	 */
	public int[][][] spiele;
	public int[][][] ergebnisse;
	
	public boolean[] spieleEingetragen;
	public boolean[] ergebnisseEingetragen;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
	
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

		System.out.println();
		
		{
            spieltag = new Spieltag(this);
            spieltag.setLocation((1440 - spieltag.getSize().width) / 2, (874 - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
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

	public void ergebnisseSichern() {
		for (int i = 0; i < spieltag.tore.length; i++) {
			ergebnisse[spieltag.current_matchday][i % numberOfMatchesPerMatchday][i / numberOfMatchesPerMatchday] = Integer.parseInt(spieltag.tore[i].getText());
		}
		int counter = 0;
		for (int i = 0; i < mannschaften.length; i++) {
			// falls die Anzahl an Mannschaften ungerade ist: hat die spielfreie Mannschaft (Gegner == 0) auch positiononplan == 0 und darf nicht gezählt werden
			if (mannschaften[i].getMatch(spieltag.current_matchday)[1] != 0) { 
				int pos = mannschaften[i].positiononplan[spieltag.current_matchday];
				int tore = Integer.parseInt(spieltag.tore[pos].getText());
				int gegentore = Integer.parseInt(spieltag.tore[(pos + numberOfMatchesPerMatchday) % spieltag.tore.length].getText());
				mannschaften[i].setResult(spieltag.current_matchday, tore, gegentore);
				
				if(tore >= 0 && gegentore >= 0) {
					counter++; // zählt diejenigen Spiele die komplett mit Ergebnis eingetragen sind
				}
			} else {
				counter++; // eine spielfreie Mannschaft ist auch korrekt eingetragen
			}
		}
		if (counter == mannschaften.length)		ergebnisseEingetragen[spieltag.current_matchday] = true;
		else									ergebnisseEingetragen[spieltag.current_matchday] = false;
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
		numberOfMatchdays = countteams - 1;
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
		this.ergebnisse = new int[numberOfMatchdays][numberOfMatchesPerMatchday][2];
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
	            		System.out.println(spieldaten[0] + " + " + spieldaten[1]);
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
	            		String[] tore = inhalte[match + 1].split(":");
	                    int toreHOME = Integer.parseInt(tore[0]);
	                    int toreAWAY = Integer.parseInt(tore[1]);
	                    int teamHOME = this.spiele[counter][match][0];
	                    int teamAWAY = this.spiele[counter][match][1];
	                    
	                    this.ergebnisse[counter][match][0] = toreHOME;
	                    this.ergebnisse[counter][match][1] = toreAWAY;
	                    this.mannschaften[teamHOME - 1].setResult(counter, toreHOME, toreAWAY);
	                    this.mannschaften[teamAWAY - 1].setResult(counter, toreAWAY, toreHOME);
	            	}
	            }
	            
	            for (int k = match; k < this.numberOfMatchesPerMatchday; k++) {
	            	this.ergebnisse[counter][k][0] = -1;
	            	this.ergebnisse[counter][k][1] = -1;
	            }
	        }
	        
	        while(counter < this.numberOfMatchdays) {
	            for (int k = 0; k < this.numberOfMatchesPerMatchday; k++) {
	            	this.ergebnisse[counter][k][0] = -1;
	            	this.ergebnisse[counter][k][1] = -1;
	            }
	            counter++;
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
				for (int j = 0; j < ergebnisse[i].length; j++) {
					element += ergebnisse[i][j][0] + ":" + ergebnisse[i][j][1] + ";";
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