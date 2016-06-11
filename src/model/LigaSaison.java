package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;


public class LigaSaison implements Wettbewerb {
	
	private Liga liga;
	private boolean isSummerToSpringSeason;
	private int seasonIndex;
	private int season;
	private boolean goalDifference;
	private boolean teamsHaveKader;
	
	private int numberOfReferees;
	private ArrayList<Schiedsrichter> referees;
	private int numberOfTeams;
	private Mannschaft[] mannschaften;
	private int halbeAnzMSAuf;
	private int numberOfMatchdays;
	private int numberOfMatchesPerMatchday;
	private int numberOfMatchesAgainstSameOpponent;
	private int currentMatchday;
	private int cMatchdaySetForDate = -1;
	private int newestMatchday;
	private int nMatchdaySetForDate = -1;
	private int nMatchdaySetUntilTime = -1;
	
	private int[] anzahl;
	
	private int[][] datesAndTimes;
	
	private int numberOfKickoffTimes;
	private ArrayList<AnstossZeit> kickOffTimes;
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
	
	private String fileReferees;
	private ArrayList<String> refereesFromFile;
	
	private String dateiMannschaften;
	private ArrayList<String> mannschaftenFromFile;
	
	private String dateiSpielplan;
	private ArrayList<String> spielplanFromFile;
	
	private String dateiErgebnisse;
	private ArrayList<String> ergebnisseFromFile;
	
	private String dateiSpieldaten;
	private ArrayList<String> spieldatenFromFile;
	
	public LigaSaison(Liga liga, int seasonIndex, String data) {
		this.liga = liga;
		this.seasonIndex = seasonIndex;
		fromString(data);
		workspace = liga.getWorkspace() + getSeasonFull("_") + File.separator;
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
	
	public int getID() {
		return seasonIndex;
	}
	
	public int getYear() {
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
	
	public ArrayList<Schiedsrichter> getReferees() {
		return referees;
	}
	
	public String[] getAllReferees() {
		String[] allReferees = new String[referees.size() + 1];
		
		allReferees[0] = "Bitte wählen";
		for (int i = 1; i < allReferees.length; i++) {
			allReferees[i] = referees.get(i - 1).getFullName();
		}
		
		return allReferees;
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
		int today = MyDate.verschoben(MyDate.newMyDate(), -1); // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (cMatchdaySetForDate != today) {
			if (today < getDate(0)) {
				currentMatchday = 0;
			} else if (today >= getDate(numberOfMatchdays - 1) && getDate(numberOfMatchdays - 1) != 0) {
				currentMatchday = numberOfMatchdays - 1;
			} else {
				currentMatchday = 0;
				while (today >= getDate(currentMatchday) && getDate(currentMatchday) != 0) {
					currentMatchday++;
				}
				if (currentMatchday != 0 && MyDate.difference(getDate(currentMatchday - 1), today) < MyDate.difference(today, getDate(currentMatchday))) {
					currentMatchday--;
				}
			}
			cMatchdaySetForDate = today;
		}
		
		return currentMatchday;
	}
	
	public int getNewestStartedMatchday() {
		int today = MyDate.newMyDate(), time = MyDate.newMyTime(), nextDate;
		
		if (nMatchdaySetForDate != today || time >= nMatchdaySetUntilTime) {
			nMatchdaySetUntilTime = 2400;
			if (today < getDate(0, 0)) {
				newestMatchday = 0;
			} else if (today >= getDate(numberOfMatchdays - 1, 0) && getDate(numberOfMatchdays - 1, 0) != 0) {
				newestMatchday = numberOfMatchdays - 1;
			} else {
				newestMatchday = 0;
				nextDate = getDate(newestMatchday + 1, 0);
				while (nextDate != 0 && (today > nextDate || (today == nextDate && time >= getTime(newestMatchday + 1, 0)))) {
					newestMatchday++;
					nextDate = getDate(newestMatchday + 1, 0);
				}
				if (today == getDate(newestMatchday + 1, 0)) {
					nMatchdaySetUntilTime = getTime(newestMatchday + 1, 0);
				}
			}
			
			nMatchdaySetForDate = today;
		}
		
		return newestMatchday;
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
			return kickOffTimes.get(getKOTIndex(matchday, match)).getDateAndTime(getDate(matchday));
		else
			return "nicht terminiert";
	}
	
	public int getDate(int matchday) {
		if (matchday > 0 && matchday < numberOfMatchdays)	return datesAndTimes[matchday][0];
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
		return kickOffTimes.get(datesAndTimes[matchday][match + 1]).getDate(datesAndTimes[matchday][0]);
	}
	
	public int getTime(int matchday, int match) {
		return kickOffTimes.get(datesAndTimes[matchday][match + 1]).getTime();
	}
	
	public int addNewKickoffTime(int daysSince, int time) {
		numberOfKickoffTimes++;
		kickOffTimes.add(new AnstossZeit(numberOfKickoffTimes, daysSince, time));
		return numberOfKickoffTimes;
	}
	
	public int getIndexOfKOT(int diff, int timeOfNewKOT) {
		for (AnstossZeit az : kickOffTimes) {
			if (az.matches(diff, timeOfNewKOT))	return az.getIndex();
		}
		return -1;
	}
	
	public void useDefaultKickoffTimes(int matchday) {
		for (int i = 0; i < defaultKickoffTimes.length; i++) {
			setKOTIndex(matchday, i, defaultKickoffTimes[i]);
		}
	}
	
	public int[] getDefaultKickoffTimes() {
		return defaultKickoffTimes;
	}
	
	public ArrayList<AnstossZeit> getKickOffTimes() {
		return kickOffTimes;
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
				if (oldSpiel != null) {
					spieleInNewOrder[j] = new Spiel(this, matchdayNew, getDate(matchdayNew), 0, oldSpiel.away(), oldSpiel.home());
				}
			}
			for (int j = 0; j < spieleInNewOrder.length; j++) {
				for (int k = j + 1; k < spieleInNewOrder.length; k++) {
					if (spieleInNewOrder[k] != null && (spieleInNewOrder[j] == null || spieleInNewOrder[j].home() > spieleInNewOrder[k].home())) {
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
			dates[match] = kickOffTimes.get(getKOTIndex(matchday, match)).getDate(getDate(matchday));
			times[match] = kickOffTimes.get(getKOTIndex(matchday, match)).getTime();
		}
		
		for (int m = 0; m < numberOfMatchesPerMatchday; m++) {
			for (int m2 = m + 1; m2 < numberOfMatchesPerMatchday; m2++) {
				if (times[m] == -1)				hilfsarray[m2]++;
				else if (times[m2] == -1)		hilfsarray[m]++;
				else if (dates[m2] > dates[m])	hilfsarray[m2]++;
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
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			oldSpielplan[match] = getSpiel(matchday, match);
			oldErgebnisplan[match] = getErgebnis(matchday, match);
			oldSpielplanEingetragen[match] = isSpielplanEntered(matchday, match);
			oldErgebnisplanEingetragen[match] = isErgebnisplanEntered(matchday, match);
			oldKOTindices[match] = datesAndTimes[matchday][match + 1];
		}
		
		for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
			setSpiel(matchday, match, oldSpielplan[oldIndicesInNewOrder[match]]);
			setErgebnis(matchday, match, oldErgebnisplan[oldIndicesInNewOrder[match]]);
			setSpielplanEntered(matchday, match, oldSpielplanEingetragen[oldIndicesInNewOrder[match]]);
			setErgebnisplanEntered(matchday, match, oldErgebnisplanEingetragen[oldIndicesInNewOrder[match]]);
			datesAndTimes[matchday][match + 1] = oldKOTindices[oldIndicesInNewOrder[match]];
		}
	}
	
	private void saveNextMatches() {
		ArrayList<Long> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				int date = getDate(i, j), time = getTime(i, j);
				if (isSpielplanEntered(i, j) && date > 0 && (!inThePast(date, time, 145) || !isErgebnisplanEntered(i, j))) {
					long match = 10000L * date + time;
					if (nextMatches.size() < 10 || match < nextMatches.get(9)) {
						int index = nextMatches.size();
						for (int k = 0; k < nextMatches.size() && index == nextMatches.size(); k++) {
							if (match < nextMatches.get(k))	index = k;
						}
						nextMatches.add(index, match);
					}
				}
			}
		}
		
		String fileName = workspace + "nextMatches.txt";
		if (nextMatches.size() > 0) {
			ArrayList<String> nextMatchesString = new ArrayList<>();
			for (int i = 0; i < 10 && i < nextMatches.size(); i++) {
				nextMatchesString.add("" + nextMatches.get(i));
			}
			inDatei(fileName, nextMatchesString);
		} else {
			new File(fileName).delete();
		}
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
	
	private String getDefaultKickoffTimesRepresentation() {
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
	
	public void loadReferees() {
		refereesFromFile = ausDatei(fileReferees, false);
		
		numberOfReferees = refereesFromFile.size();
		referees = new ArrayList<>();
		for (int i = 0; i < numberOfReferees; i++) {
			referees.add(new Schiedsrichter(i + 1, refereesFromFile.get(i)));
		}
	}
	
	public void saveReferees() {
		refereesFromFile.clear();
		
		for (int i = 0; i < referees.size(); i++) {
			refereesFromFile.add(referees.get(i).toString());
		}
		
		if (refereesFromFile.size() > 0)	inDatei(fileReferees, refereesFromFile);
	}
	
	public void mannschaftenLaden() {
		mannschaftenFromFile = ausDatei(dateiMannschaften);
		
		halbeAnzMSAuf = (int) Math.round((double) numberOfTeams / 2);				// liefert die (aufgerundete) Haelfte zurueck
		numberOfMatchesPerMatchday = numberOfTeams / 2;								// liefert die (abgerundete) Haelfte zurueck
		if (numberOfTeams >= 2)		numberOfMatchdays = numberOfMatchesAgainstSameOpponent * (2 * halbeAnzMSAuf - 1);
		else						numberOfMatchdays = 0;
		
		mannschaften = new Mannschaft[numberOfTeams];
		for (int i = 0; i < numberOfTeams; i++) {
			mannschaften[i] = new Mannschaft(i + 1, this, mannschaftenFromFile.get(i + 1));
		}
	}
	
	public void mannschaftenSpeichern() {
		mannschaftenFromFile.clear();
		
		mannschaftenFromFile.add("" + numberOfTeams);
		for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i].save();
			mannschaftenFromFile.add(mannschaften[i].toString());
		}
		
		inDatei(dateiMannschaften, mannschaftenFromFile);
	}
	
	public void spielplanLaden() {
		spielplanFromFile = ausDatei(dateiSpielplan);
		
		try {
			spielplanFromFile = ausDatei(dateiSpielplan); 
			
			// Anstosszeiten / Spieltermine
			String allKickoffTimes = spielplanFromFile.get(0);
			String[] kickoffs = allKickoffTimes.split(";");
			numberOfKickoffTimes = Integer.parseInt(kickoffs[0]);
			kickOffTimes = new ArrayList<>();
			kickOffTimes.add(new AnstossZeit(0, -1, -1));
			for (int counter = 1; counter <= numberOfKickoffTimes; counter++) {
				kickOffTimes.add(new AnstossZeit(counter, kickoffs[counter]));
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
			string = string + kickOffTimes.get(i + 1) + ";";
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
	
	private void spieldatenLaden() {
		try {
			spieldatenFromFile = ausDatei(dateiSpieldaten);
			
			for (int matchday = 0; matchday < numberOfMatchdays && matchday < spieldatenFromFile.size(); matchday++) {
				for (int match = 0; match < numberOfMatchesPerMatchday; match++) {
					String inhalt = spieldatenFromFile.get(matchday * numberOfMatchesPerMatchday + match);
					if (isSpielplanEntered(matchday, match)) {
						getSpiel(matchday, match).setRemainder(inhalt);
					}
				}
			}
		} catch (Exception e) {
			errorMessage("Keine Spieldaten: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void spieldatenSpeichern() {
		spieldatenFromFile.clear();
		
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				spieldatenFromFile.add(getSpiel(i, j) != null ? getSpiel(i, j).fullString() : "null");
			}
		}
		
		inDatei(dateiSpieldaten, spieldatenFromFile);
	}
	
	public void laden() {
		dateiErgebnisse = workspace + "Ergebnisse.txt";
		dateiSpieldaten = workspace + "Spieldaten.txt";
		dateiSpielplan = workspace + "Spielplan.txt";
		dateiMannschaften = workspace + "Mannschaften.txt";
		fileReferees = workspace + "Schiedsrichter.txt";
		
		loadReferees();
		mannschaftenLaden();
		initializeArrays();
		
		spielplanLaden();
		ergebnisseLaden();
		spieldatenLaden();
		
		if (spieltag == null) {
			spieltag = new Spieltag(this);
			spieltag.setLocation((Start.WIDTH - spieltag.getSize().width) / 2, (Start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			spieltag.setVisible(false);
		}
		if (tabelle == null) {
			tabelle = new Tabelle(this);
			tabelle.setLocation((Start.WIDTH - tabelle.getSize().width) / 2, 50);
			tabelle.setVisible(false);
		}
		if (statistik == null) {
			statistik = new LigaStatistik(this);
			statistik.setLocation((Start.WIDTH - statistik.getSize().width) / 2, 50);
			statistik.setVisible(false);
		}
		spieltag.resetCurrentMatchday();
		tabelle.resetCurrentMatchday();
		
		geladen = true;
	}
	
	public void speichern() {
		if (!geladen)	return;
		
		saveNextMatches();
		saveReferees();
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
		cMatchdaySetForDate = -1;
		nMatchdaySetForDate = -1;
		
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += season + ";";
		toString += isSummerToSpringSeason + ";";
		toString += numberOfTeams + ";";
		toString += numberOfMatchesAgainstSameOpponent + ";";
		toString += getDefaultKickoffTimesRepresentation() + ";";
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

class AnstossZeit {
	
	private int index;
	private int daysSince;
	private int time;
	
	public AnstossZeit(int index, int daysSince, int time) {
		this.index = index;
		this.daysSince = daysSince;
		this.time = time;
	}

	public AnstossZeit(int index, String data) {
		this.index = index;
		fromString(data);
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getDateAndTime(int startDate) {
		String datum, uhrzeit = " k. A.";
		
		int date = MyDate.verschoben(startDate, time != -1 || daysSince != -1 ? daysSince : 0);
		datum = MyDate.datum(date);
		if (time != -1)	uhrzeit = MyDate.uhrzeit(time);
		
		return datum + " " + uhrzeit;
	}
	
	public int getDate(int startDate) {
		if (startDate == 0)	return 0;
		return MyDate.verschoben(startDate, daysSince);
	}
	
	public int getDaysSince() {
		return daysSince;
	}
	
	public int getTime() {
		return time;
	}
	
	public boolean matches(int diff, int timeOfNewKOT) {
		if (daysSince != diff)		return false;
		if (time != timeOfNewKOT)	return false;
		return true;
	}
	
	public String toString() {
		return this.daysSince + "," + time;
	}
	
	private void fromString(String data) {
		String[] split = data.split(",");
		int index = 0;
		
		this.daysSince = Integer.parseInt(split[index++]);
		this.time = Integer.parseInt(split[index++]);
	}
}
