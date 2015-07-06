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
	private int halbeAnzMSAuf;
	private int numberOfMatchdays;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	
	private int[] anzahl;
	
	private int[][] datesAndTimes;
	
	private int numberOfKickoffTimes;
	private ArrayList<Integer> kickoffTimes;
	private ArrayList<Integer> daysSinceST;
	private int[] defaultKickoffTimes;
	
	private Spiel[][] spielplan;
	private boolean[][] spielplanEingetragen;
	
	private Ergebnis[][] ergebnisplan;
	private boolean[][] ergebnisplanEingetragen;
	
	private Spieltag spieltag;
	private Tabelle tabelle;
	private LigaStatistik statistik;
	
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
		return liga.getName() + " " + getSeasonFull("/");
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public int getSeasonIndex() {
		return seasonIndex;
	}
	
	public int getSeason() {
		return season;
	}
	
	public boolean isSTSS() {
		return isSummerToSpringSeason;
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
	
	public int getHalbeAnzMSAuf() {
		return halbeAnzMSAuf;
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
	
	public Spieltag getSpieltag() {
		return spieltag;
	}
	
	public Tabelle getTabelle() {
		return tabelle;
	}
	
	public LigaStatistik getLigaStatistik() {
		return statistik;
	}
	
	public Mannschaft[] getMannschaften() {
		return mannschaften;
	}

    public int getIndexOfMannschaft(String name) {
    	for (Mannschaft ms : mannschaften) {
    		if (ms.getName().equals(name)) {
    			return ms.getId();
    		}
    	}
    	return -1;
    }
	
	public Mannschaft getTeamWithName(String teamsName) {
    	return mannschaften[getIndexOfMannschaft(teamsName) - 1];
    }
	
	public String getMatchdayDescription(int matchday) {
		return liga.getName() + " " + getSeasonFull("/") + ", " + (matchday + 1) + ". Spieltag";
	}
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = MyDate.verschoben(MyDate.newMyDate(), -1); // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (today < getDate(0)) {
			matchday = 0;
		} else if (today > getDate(numberOfMatchdays - 1) && getDate(numberOfMatchdays - 1) != 0) {
			matchday = numberOfMatchdays - 1;
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
		if (matchday >= 0 && matchday < numberOfMatchdays)	return datesAndTimes[matchday][0];
		else												return datesAndTimes[0][0];
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
		return MyDate.verschoben(datesAndTimes[matchday][0], daysSinceST.get(datesAndTimes[matchday][match + 1]));
	}
	
	public int getTime(int matchday, int match) {
		return kickoffTimes.get(getKOTIndex(matchday, match));
	}
	
	public void addNewKickoffTime(int tageseitstarttag, int kickofftime) {
		daysSinceST.add(tageseitstarttag);
		kickoffTimes.add(kickofftime);
		numberOfKickoffTimes++;
	}
	
	public int getIndexOfKOT(int diff, int timeOfNewKOT) {
		for (int i = 0; i < daysSinceST.size(); i++) {
			if (daysSinceST.get(i) == diff) {
				if (kickoffTimes.get(i) == timeOfNewKOT) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public void useDefaultKickoffTimes(int matchday) {
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			setKOTIndex(matchday, i, defaultKickoffTimes[i]);
		}
	}
	
	// TODO improve this by passing on the kot (create data type) instead of granting separate direct access to the arrays
	
	public int getDefaultKickoffTime(int index) {
		return defaultKickoffTimes[index];
	}
	
	public int getDaysSinceST(int index) {
		return daysSinceST.get(index);
	}
	
	public int getKickoffTimes(int index) {
		return kickoffTimes.get(index);
	}
	
	public int getNumberOfKickoffTimes() {
		return numberOfKickoffTimes;
	}
	
	// Spielplan eingetragen
	
	public boolean isSpielplanFullyEmpty(int matchday) {
		for (int match = 0; match < getNumberOfMatchesPerMatchday(); match++) {
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
			mannschaften[spiel.home() - 1].setMatch(matchday, spiel);
			mannschaften[spiel.away() - 1].setMatch(matchday, spiel);
		}
		else				setSpielplanEntered(matchday, match, false);
		spielplan[matchday][match] = spiel;
	}
	
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
	
	public void setRueckrundeToOrder(int[] orderOfRueckRunde) {
		if (2 * orderOfRueckRunde.length != numberOfMatchdays) {
			errorMessage("Your Rueckrunde does not match the expected number of matchdays.");
			return;
		}
		boolean[] inHinrunde = new boolean[numberOfMatchdays];
		for (int i = 0; i < orderOfRueckRunde.length; i++) {
			if (inHinrunde[orderOfRueckRunde[i] - 1] == true) {
				errorMessage("Some matchday appears to be twice in the Rueckrunde.");
				return;
			}
			inHinrunde[orderOfRueckRunde[i] - 1] = true;
		}
		
		int matchdayOld, matchdayNew = 0;
		for (int i = 0; i < orderOfRueckRunde.length; i++) {
			Spiel[] spieleInNewOrder = new Spiel[numberOfMatchesPerMatchday];
			matchdayOld = orderOfRueckRunde[i] - 1;
			while (inHinrunde[matchdayNew]) {
				matchdayNew++;
			}
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				Spiel oldSpiel = getSpiel(matchdayOld, j);
				spieleInNewOrder[j] = new Spiel(this, matchdayNew, datesAndTimes[matchdayNew][0], 0, oldSpiel.away(), oldSpiel.home());
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
	
	public void changeOrderToChronological(int matchday) {
		int[] newOrder = new int[numberOfMatchesPerMatchday];
		int[] hilfsarray = new int[numberOfMatchesPerMatchday];
		int[] dates = new int[numberOfMatchesPerMatchday];
		int[] times = new int[numberOfMatchesPerMatchday];
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			dates[match] = MyDate.verschoben(getDate(matchday), daysSinceST.get(getKOTIndex(matchday, match)));
			times[match] = kickoffTimes.get(getKOTIndex(matchday, match));
		}
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (dates[m2] > dates[m])		hilfsarray[m2]++;
				else if (dates[m2] < dates[m])	hilfsarray[m]++;
				else if (times[m2] > times[m])	hilfsarray[m2]++;
				else if (times[m2] < times[m])	hilfsarray[m]++;
				else {
					Spiel sp1 = getSpiel(matchday, m), sp2 = getSpiel(matchday, m2);
					if (sp1 != null && sp2 != null && sp1.getHomeTeam().getId() > sp2.getHomeTeam().getId())	hilfsarray[m]++;
					else	hilfsarray[m2]++;
				}
			}
		}
		
		for (int i = 0; i < hilfsarray.length; i++) {
			newOrder[hilfsarray[i]] = i;
		}
		
		changeOrderOfMatches(matchday, newOrder);
	}
	
	public void changeOrderOfMatches(int matchday, int[] oldIndicesInNewOrder) {
		// check the correctness of the parameter array
		if (oldIndicesInNewOrder.length != numberOfMatchesPerMatchday) {
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
		Spiel[] oldSpielplan = new Spiel[numberOfMatchesPerMatchday];
		Ergebnis[] oldErgebnisplan = new Ergebnis[numberOfMatchesPerMatchday];
		boolean[] oldSpielplanEingetragen = new boolean[numberOfMatchesPerMatchday];
		boolean[] oldErgebnisplanEingetragen = new boolean[numberOfMatchesPerMatchday];
		int[] oldKOTindices = new int[numberOfMatchesPerMatchday];
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	oldSpielplan[match] = getSpiel(matchday, match);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	oldErgebnisplan[match] = getErgebnis(matchday, match);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	oldSpielplanEingetragen[match] = isSpielplanEntered(matchday, match);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	oldErgebnisplanEingetragen[match] = isErgebnisplanEntered(matchday, match);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	oldKOTindices[match] = datesAndTimes[matchday][match + 1];
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	setSpiel(matchday, match, oldSpielplan[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	setErgebnis(matchday, match, oldErgebnisplan[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	setSpielplanEntered(matchday, match, oldSpielplanEingetragen[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	setErgebnisplanEntered(matchday, match, oldErgebnisplanEingetragen[oldIndicesInNewOrder[match]]);
		for (int match = 0; match < numberOfMatchesPerMatchday; match++)	datesAndTimes[matchday][match + 1] = oldKOTindices[oldIndicesInNewOrder[match]];
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
	
	public String getDefaultKickoffTimes() {
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
		datesAndTimes = new int[numberOfMatchdays][numberOfMatchesPerMatchday + 1];
		spielplan = new Spiel[numberOfMatchdays][numberOfMatchesPerMatchday];
		ergebnisplan = new Ergebnis[numberOfMatchdays][numberOfMatchesPerMatchday];
		spielplanEingetragen = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
		ergebnisplanEingetragen = new boolean[numberOfMatchdays][numberOfMatchesPerMatchday];
	}
	
	public void mannschaftenLaden() {
		mannschaftenFromFile = ausDatei(dateiMannschaften);
		
		halbeAnzMSAuf = (int) Math.round((double) numberOfTeams / 2);				// liefert die (aufgerundete) Haelfte zurueck
		numberOfMatchesPerMatchday = numberOfTeams / 2;								// liefert die (abgerundete) Haelfte zurueck
		if (numberOfTeams >= 2)		numberOfMatchdays = numberOfMatchesAgainstSameOpponent * (2 * halbeAnzMSAuf - 1);
		else						numberOfMatchdays = 0;
		
		mannschaften = new Mannschaft[numberOfTeams];
		for (int i = 0; i < numberOfTeams; i++) {
			mannschaften[i] = new Mannschaft(start, i + 1, this, mannschaftenFromFile.get(i + 1));
		}
	}
	
	public void mannschaftenSpeichern() {
		mannschaftenFromFile.clear();
		
		mannschaftenFromFile.add("" + numberOfTeams);
		for (int i = 0; i < mannschaften.length; i++) {
			mannschaftenFromFile.add(mannschaften[i].toString());
		}
		
		inDatei(dateiMannschaften, mannschaftenFromFile);
	}
	
	public void spielplanLaden() {
		spielplanFromFile = ausDatei(dateiSpielplan);
		
		try {
			spielplanFromFile = ausDatei(dateiSpielplan); 
			
			int counter = 0;
			
			// Anstosszeiten / Spieltermine
			String allKickoffTimes = spielplanFromFile.get(0);
			String[] kickoffs = allKickoffTimes.split(";");
			numberOfKickoffTimes = Integer.parseInt(kickoffs[0]);
			daysSinceST = new ArrayList<>();
			kickoffTimes = new ArrayList<>();
			for (counter = 0; counter < numberOfKickoffTimes; counter++) {
				String[] kickoffDetails = kickoffs[counter + 1].split(",");
				daysSinceST.add(Integer.parseInt(kickoffDetails[0]));
				kickoffTimes.add(Integer.parseInt(kickoffDetails[1]));
			}
			
			for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
				String[] inhalte = spielplanFromFile.get(matchday + 1).split(";");
				
				setSpielplanEnteredFromRepresentation(matchday, inhalte[0]);
				
				int match = 0;
				if (!isSpielplanFullyEmpty(matchday)) {
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
				
				while(match < numberOfMatchesPerMatchday) {
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
		
		String string = numberOfKickoffTimes + ";";
		for (int i = 0; i < numberOfKickoffTimes; i++) {
			string = string + daysSinceST.get(i) + "," + kickoffTimes.get(i) + ";";
		}
		spielplanFromFile.add(string);
		
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			string = getSpielplanRepresentation(matchday) + ";";
			if (!isSpielplanFullyEmpty(matchday)) {
				string += getDate(matchday);
				for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
					string += ":" + getKOTIndex(matchday, j);
				}
				string += ";";
				for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
					string += getSpiel(matchday, match) + ";";
				}
			}
			
			spielplanFromFile.add(string);
		}
		
		inDatei(dateiSpielplan, spielplanFromFile);
	}
	
	public void ergebnisseLaden() {
		ergebnisseFromFile = ausDatei(dateiErgebnisse);
		
		try {
			ergebnisseFromFile = ausDatei(dateiErgebnisse);
			int counter;
			
			for (counter = 0; counter < numberOfMatchdays; counter++) {
				String inhalte[] = ergebnisseFromFile.get(counter).split(";");
				setErgebnisplanEnteredFromRepresentation(counter, inhalte[0]);

				int match = 0;
				if (!isSpielplanFullyEmpty(counter) && !isErgebnisplanFullyEmpty(counter)) {
					for (match = 0; (match + 1) < inhalte.length; match++) {
						if (isSpielplanEntered(counter, match)) {
							Ergebnis ergebnis;
							if (isErgebnisplanEntered(counter, match))	ergebnis = new Ergebnis(inhalte[match + 1]);
							else										ergebnis = null;
							
							setErgebnis(counter, match, ergebnis);
						
							mannschaften[getSpiel(counter, match).home() - 1].setResult(counter, ergebnis);
							mannschaften[getSpiel(counter, match).away() - 1].setResult(counter, ergebnis);
						}
					}
				}
				while (match < numberOfMatchesPerMatchday) {
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
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			String string = getErgebnisplanRepresentation(i) + ";";
			if (!isErgebnisplanFullyEmpty(i)) {
				for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
					string += getErgebnis(i, j) + ";";
				}
			}
			
			ergebnisseFromFile.add(string);
		}
		
		inDatei(dateiErgebnisse, ergebnisseFromFile);
	}
	
	public void spieldatenLaden() {
		spieldatenFromFile = ausDatei(dateiSpieldaten);
		
		try {
			spieldatenFromFile = ausDatei(dateiSpieldaten);
			int matchday;
			
			for (matchday = 0; matchday < numberOfMatchdays && matchday < spieldatenFromFile.size(); matchday++) {
				String inhalte[] = spieldatenFromFile.get(matchday).split(";");
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
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			String string = "";
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				if (getSpiel(i, j) != null)	string += getSpiel(i, j).fullString() + ";";
				else						string += "null;";
			}
			spieldatenFromFile.add(string);
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
		ergebnisseLaden();
		spieldatenLaden();
		
		if (spieltag == null) {
			spieltag = new Spieltag(start, this);
			spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
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
		spieltag.resetCurrentMatchday();
		tabelle.resetCurrentMatchday();
		
		geladen = true;
	}
	
	public void speichern() {
		if (!geladen)	return;
		
		mannschaftenSpeichern();
		spielplanSpeichern();
		ergebnisseSpeichern();
		spieldatenSpeichern();
		
		spieltag.setVisible(false);
		spieltag = null;
		tabelle.setVisible(false);
		tabelle = null;
		statistik.setVisible(false);
		statistik = null;
		
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += season + ";";
		toString += isSummerToSpringSeason + ";";
		toString += numberOfTeams + ";";
		toString += numberOfMatchesAgainstSameOpponent + ";";
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
