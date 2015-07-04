package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;

public class LigaSaison implements Wettbewerb {
	
	private Start start;
	private Liga liga;
	private boolean isSummerToSpringSeason;
	private int seasonIndex;
	private int season;
	private boolean goalDifference;
	private boolean teamsHaveKader;
	
	private Mannschaft[] mannschaften;
	private int numberOfTeams;
	private int halbeanzMSAuf;
	private int numberOfMatchdays;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	
	private int[] anzahl;
	
	private int[][] datesAndTimes;
	
	private int numberOfKickoffTimes;
	private int[] kickoffTimes;
	private int[] daysSinceDST;
	private int defaultStarttag;
	private int[] defaultKickoffTimes;
	
	private Spiel[][] spielplan;
	private boolean[][] spielplanEingetragen;
	
	private Ergebnis[][] ergebnisplan;
	private boolean[][] ergebnisplanEingetragen;

	private boolean geladen;
	private String workspace;
	
	private String dateiMannschaften;
	private ArrayList<String> mannschaftenFromFile;
	
	private String dateiSpielplan;
	private ArrayList<String> spielplanFromFile;
	
	private String dateiErgebnisse;
	private ArrayList<String> ergebnisseFromFile;
	
	private String dateiSpieldaten;
	private ArrayList<String> spieldatenFromFile;
	
	public LigaSaison(Start start, Liga liga, int seasonIndex, String data) {
		this.start = start;
		this.liga = liga;
		this.seasonIndex = seasonIndex;
		fromString(data);
	}
	
	public Liga getLiga() {
		return liga;
	}
	
	public String getName() {
		return liga.getName();
	}
	
	public int getSeasonIndex() {
		return seasonIndex;
	}
	
	public String getSeasonFull(String trennzeichen) {
		return season + (isSummerToSpringSeason ? trennzeichen + (season + 1) : "");
	}
	
	public int getAnzahl(int index) {
		return anzahl[index];
	}
	
	public int getNumberOfTeams() {
		return numberOfTeams;
	}
	
	public int getNumberOfMatchdays() {
		return numberOfMatchdays;
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return numberOfMatchesPerMatchday;
	}
	
	public int getNumberOfMatchesAgainstSameOpponent() {
		return numberOfMatchesAgainstSameOpponent;
	}
	
	public boolean useGoalDifference() {
		return goalDifference;
	}
	
	public boolean teamsHaveKader() {
		return teamsHaveKader;
	}
	
	public boolean isETPossible() {
		return false;
	}
	
	public Mannschaft[] getMannschaften() {
		return mannschaften;
	}
	
	public String getMatchdayDescription(int matchday) {
		return liga.getName() + " " + getSeasonFull("/") + ", " + (matchday + 1) + ". Spieltag";
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
	
	// Date / Time
	
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
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
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
	
	private String getAnzahlRepresentation() {
		String representation = "", sep = "";
		
		for (int i = 0; i < anzahl.length; i++) {
			representation += sep + anzahl[i];
			sep = ",";
		}
		
		return representation;
	}
	
	private int[] getAnzahlFromString(String anzahlen) {
		String[] anzSplit = anzahlen.split(",");
		int[] anzahl = new int[anzSplit.length];
		
		for (int i = 0; i < anzahl.length; i++) {
			anzahl[i] = Integer.parseInt(anzSplit[i]);
		}
		
		return anzahl;
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
	
	private void initializeArrays() {
		// Alle Array werden initialisiert
		this.datesAndTimes = new int[this.numberOfMatchdays][this.numberOfMatchesPerMatchday + 1];
		this.spielplan = new Spiel[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
		this.ergebnisplan = new Ergebnis[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
		this.spielplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
		this.ergebnisplanEingetragen = new boolean[this.numberOfMatchdays][this.numberOfMatchesPerMatchday];
	}
	
	public void mannschaftenLaden() {
		mannschaftenFromFile = ausDatei(dateiMannschaften);
		
		halbeanzMSAuf = (int) Math.round((double) numberOfTeams / 2);				// liefert die (aufgerundete) Haelfte zurueck
		numberOfMatchesPerMatchday = numberOfTeams / 2;								// liefert die (abgerundete) Haelfte zurueck
		if (numberOfTeams >= 2)		numberOfMatchdays = numberOfMatchesAgainstSameOpponent * (2 * halbeanzMSAuf - 1);
		else						numberOfMatchdays = 0;
		
		this.mannschaften = new Mannschaft[numberOfTeams];
		for (int i = 0; i < numberOfTeams; i++) {
			this.mannschaften[i] = new Mannschaft(this.start, i + 1, this, mannschaftenFromFile.get(i + 1));
			log(this.mannschaften[i]);
		}
	}
	
	public void mannschaftenSpeichern() {
//		mannschaftenFromFile.clear();
		
//		inDatei(dateiMannschaften, mannschaftenFromFile);
	}
	
	public void spielplanLaden() {
		spielplanFromFile = ausDatei(dateiSpielplan);
		
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
	
	public void spielplanSpeichern() {
		spielplanFromFile.clear();
		
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
		
		inDatei(dateiSpielplan, spielplanFromFile);
	}
	
	public void ergebnisseLaden() {
		ergebnisseFromFile = ausDatei(dateiErgebnisse);
		
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
	
	public void ergebnisseSpeichern() {
		ergebnisseFromFile.clear();
		
		for (int i = 0; i < this.numberOfMatchdays; i++) {
            String string = this.getErgebnisplanRepresentation(i) + ";";
            if (!this.isErgebnisplanFullyEmpty(i)) {
				for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
                	string += getErgebnis(i, j) + ";";
				}
            }
            
            this.ergebnisseFromFile.add(string);
        }
        
		inDatei(dateiErgebnisse, ergebnisseFromFile);
	}
	
	public void spieldatenLaden() {
		spieldatenFromFile = ausDatei(dateiSpieldaten);
		
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
	
	public void spieldatenSpeichern() {
		spieldatenFromFile.clear();
		
		for (int i = 0; i < this.numberOfMatchdays; i++) {
			String string = "";
			for (int j = 0; j < this.numberOfMatchesPerMatchday; j++) {
				if (getSpiel(i, j) != null)	string += getSpiel(i, j).fullString() + ";";
				else						string += "null;";
			}
			this.spieldatenFromFile.add(string);
		}
		
		inDatei(dateiSpieldaten, spieldatenFromFile);
	}
	
	public void laden() {
		workspace = liga.getWorkspace() + getSeasonFull("_") + File.separator;
		
		dateiErgebnisse = workspace + "Ergebnisse.txt";
		dateiSpieldaten = workspace + "Spieldaten.txt";
		dateiSpielplan = workspace + "Spielplan.txt";
		dateiMannschaften = workspace + "Mannschaften.txt";
		
		mannschaftenLaden();
		initializeArrays();
		
		spielplanLaden();
		for (int i = 0; i < numberOfMatchdays; i++) {
			log((i + 1) + ". Spieltag");
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				Spiel spiel = getSpiel(i, j);
				if (spiel != null) {
					log(spiel.getHomeTeam().getName() + " vs. " + spiel.getAwayTeam().getName());
				}
			}
		}
		log("\n\n\n");
		ergebnisseLaden();
		spieldatenLaden();
		
		geladen = true;
	}
	
	public void speichern() {
		if (!geladen)	return;
		
		mannschaftenSpeichern();
		spielplanSpeichern();
		ergebnisseSpeichern();
		spieldatenSpeichern();
		
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += season + ";";
		toString += isSummerToSpringSeason + ";";
		toString += numberOfTeams + ";";
		toString += numberOfMatchesAgainstSameOpponent + ";";
		
//		toString += defaultStarttag +";";
		toString += getDefaultKickoffTimes() + ";";
		
		toString += goalDifference + ";";
		toString += teamsHaveKader + ";";
		toString += getAnzahlRepresentation() + ";";
		
		return toString;
	}
	
	private void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		season = Integer.parseInt(split[index++]);
		isSummerToSpringSeason = Boolean.parseBoolean(split[index++]);
		numberOfTeams = Integer.parseInt(split[index++]);
		numberOfMatchesAgainstSameOpponent = Integer.parseInt(split[index++]);
		initDefaultKickoffTimes(split[index++]);
		goalDifference = Boolean.parseBoolean(split[index++]);
		teamsHaveKader = Boolean.parseBoolean(split[index++]);
		anzahl = getAnzahlFromString(split[index++]);
	}
}
