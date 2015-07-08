package model;

import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class TurnierSaison {
	
	private Start start;
	private Turnier turnier;
	private boolean isSummerToSpringSeason;
	private int seasonIndex;
	private int season;
	private int startDate;
	private int finalDate;
	
	private boolean hasQualification;
	private boolean hasGroupStage;
	private boolean hasKOStage;
	private boolean hasSecondLegGroupStage;
	private boolean hasSecondLegKOStage;
	private boolean matchForThirdPlace;
	
	private Spieltag overview;
	private Gruppe[] gruppen;
	private KORunde[] koRunden;
	
	private boolean hasQGroupStage;
	private boolean hasQKOStage;
	
	private Spieltag qOverview;
	private Gruppe[] qGruppen;
	private KORunde[] qKORunden;
	
	private int numberOfQGroups;
	private int numberOfQKORounds;
	
	private int numberOfGroups;
	private int numberOfKORounds;
	
	private boolean geladen;
	private String workspace;
	
	private String dateiQualifikationDaten;
	private ArrayList<String> qualifikationDatenFromFile;
	
	private String dateiGruppenDaten;
	private ArrayList<String> gruppenDatenFromFile;
	
	private String dateiKORundenDaten;
	private ArrayList<String> koRundenDatenFromFile;
	
	public TurnierSaison(Start start, Turnier turnier, int seasonIndex, String data) {
		this.start = start;
		this.turnier = turnier;
		this.seasonIndex = seasonIndex;
		fromString(data);
	}
	
	public Turnier getTurnier() {
		return turnier;
	}
	
	public int getSeasonIndex() {
		return seasonIndex;
	}
	
	public int getSeason() {
		return season;
	}
	
	public String getSeasonFull(String trennzeichen) {
		return season + (isSummerToSpringSeason ? trennzeichen + (season + 1) : "");
	}
	
	public String getDescription() {
		return turnier.getName() + " " + getSeasonFull("/");
	}
	
	public int getStartDate() {
		return startDate;
	}
	
	public int getFinalDate() {
		return finalDate;
	}
	
	public boolean hasQualification() {
		return hasQualification;
	}
	
	public boolean hasGroupStage() {
		return hasGroupStage;
	}
	
	public boolean hasKOStage() {
		return hasKOStage;
	}
	
	public int getNumberOfGroups() {
		return numberOfGroups;
	}
	
	public int getNumberOfKORounds() {
		return numberOfKORounds;
	}
	
	public boolean hasQGroupStage() {
		return hasQGroupStage;
	}
	
	public boolean hasQKOStage() {
		return hasQKOStage;
	}
	
	public int getNumberOfQGroups() {
		return numberOfQGroups;
	}
	
	public int getNumberOfQKORounds() {
		return numberOfQKORounds;
	}
	
	public boolean hasSecondLegGroupStage() {
		return hasSecondLegGroupStage;
	}

	public boolean hasSecondLegKOStage() {
		return hasSecondLegKOStage;
	}
	
	public boolean hasMatchForThirdPlace() {
		return matchForThirdPlace;
	}
	
	public boolean isETPossible() {
		return false;
	}

	public Spieltag getSpieltag() {
		return overview;
	}
	
	public Spieltag getQSpieltag() {
		return qOverview;
	}
	
	public Gruppe[] getGruppen() {
		return gruppen;
	}
	
	public KORunde[] getKORunden() {
		return koRunden;
	}
	
	public Gruppe[] getQGruppen() {
		return qGruppen;
	}
	
	public KORunde[] getQKORunden() {
		return qKORunden;
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	// Saison-spezifische Methoden
	
	public int getCurrentMatchday() {
		int matchday = 0, altMD = -1;
		if (start.isCurrentlyInQualification()) {
			matchday = qGruppen[0].getCurrentMatchday();
			for (int i = 1; i < numberOfQGroups && altMD == -1; i++) {
				if (matchday != qGruppen[i].getCurrentMatchday()) altMD = qGruppen[i].getCurrentMatchday();	
			}
			if (altMD != -1) {
				// always use the earlier matchday to remember setting the result
				matchday = matchday < altMD ? matchday : altMD;
			}
		} else {
			matchday = gruppen[0].getCurrentMatchday();
			for (int i = 1; i < numberOfGroups && altMD == -1; i++) {
				if (matchday != gruppen[i].getCurrentMatchday()) altMD = gruppen[i].getCurrentMatchday();	
			}
			if (altMD != -1) {
				// always use the earlier matchday to remember setting the result
				matchday = matchday < altMD ? matchday : altMD;
			}
		}
		
		return matchday;
	}
	
	public void ergebnisseSichern() {
		// when in overview mode
		if (start.isCurrentlyInQualification()) {
			int matchday = qOverview.getCurrentMatchday();
			
			for (int groupID = 0; groupID < qGruppen.length; groupID++) {
				Gruppe gruppe = qGruppen[groupID];
				for (int match = 0; match < gruppe.getNumberOfMatchesPerMatchday(); match++) {
					if (gruppe.isSpielplanEntered(matchday, match)) {
						Ergebnis result = qOverview.getErgebnis(groupID, match);
						
						gruppe.setErgebnis(matchday, match, result);
						gruppe.getMannschaften()[gruppe.getSpiel(matchday, match).home() - 1].setResult(matchday, result);
						gruppe.getMannschaften()[gruppe.getSpiel(matchday, match).away() - 1].setResult(matchday, result);
					}
				}
			}
		} else {
			int matchday = overview.getCurrentMatchday();
			
			for (int groupID = 0; groupID < gruppen.length; groupID++) {
				Gruppe gruppe = gruppen[groupID];
				for (int match = 0; match < gruppe.getNumberOfMatchesPerMatchday(); match++) {
					if (gruppe.isSpielplanEntered(matchday, match)) {
						Ergebnis result = overview.getErgebnis(groupID, match);
						
						gruppe.setErgebnis(matchday, match, result);
						gruppe.getMannschaften()[gruppe.getSpiel(matchday, match).home() - 1].setResult(matchday, result);
						gruppe.getMannschaften()[gruppe.getSpiel(matchday, match).away() - 1].setResult(matchday, result);
					}
				}
			}
		}
	}
	
	public int[] getChronologicalOrder(int matchday) {
		int numberOfMatches = 0;
		if (start.isCurrentlyInQualification()) {
			for (int i = 0; i < numberOfQGroups; i++) {
				numberOfMatches += qGruppen[i].getNumberOfMatchesPerMatchday();
			}
		} else {
			for (int i = 0; i < numberOfGroups; i++) {
				numberOfMatches += gruppen[i].getNumberOfMatchesPerMatchday();
			}
		}
		
		int[] newOrder = new int[numberOfMatches];
		int[] hilfsarray = new int[numberOfMatches];
		int[] dates = new int[numberOfMatches];
		int[] times = new int[numberOfMatches];
		
		int matchID = 0;
		for (Gruppe gruppe : start.isCurrentlyInQualification() ? qGruppen : gruppen) {
			for (int match = 0; match < gruppe.getNumberOfMatchesPerMatchday(); match++) {
				dates[matchID] = gruppe.getDate(matchday, match);
				times[matchID] = gruppe.getTime(matchday, match);
				matchID++;
			}
		}
		
		for (int m = 0; m < numberOfMatches; m++) {
			for (int m2 = m + 1; m2 < numberOfMatches; m2++) {
				if (dates[m2] > dates[m])									hilfsarray[m2]++;
				else if (dates[m2] == dates[m] && times[m2] >= times[m])	hilfsarray[m2]++;
				else														hilfsarray[m]++;
			}
		}
		
		for (int i = 0; i < hilfsarray.length; i++) {
			newOrder[hilfsarray[i]] = i;
		}
		
		return newOrder;
	}
	
	public Mannschaft[] getMannschaftenInOrderOfOrigins(String[] origins, boolean teamsAreWinners, int KORound, boolean isQ) {
		Mannschaft[] orderOfOrigins = new Mannschaft[origins.length];
		
		if (isQ) {
			for (int i = 0; i < orderOfOrigins.length; i++) {
				orderOfOrigins[i] = getDeepestOrigin(origins[i], teamsAreWinners, KORound, isQ);
			}
		} else if (hasGroupStage) {
			String[] groupOrigins = getGroupStageOriginsOfTeams(origins, teamsAreWinners, KORound);
			
			for (int i = 0; i < orderOfOrigins.length; i++) {
				orderOfOrigins[i] = getTeamFromGroupstageOrigin(groupOrigins[i]);
			}
		} else {
			for (int i = 0; i < orderOfOrigins.length; i++) {
				orderOfOrigins[i] = getDeepestOrigin(origins[i], teamsAreWinners, KORound, isQ);
			}
		}
		
		return orderOfOrigins;
	}
	
	/**
	 * Returns, if possible, the group stage representations of the teams with the given later origins. 
	 * The teams in origins must be all from the same KORunde, the one whose winners advance to the KORunde with the id KOround.
	 * @param origins
	 * @param teamsAreWinners
	 * @param KORound
	 * @return
	 */
	private String[] getGroupStageOriginsOfTeams(String[] origins, boolean teamsAreWinners, int KORound) {
		if (KORound < 0 || KORound >= numberOfKORounds) {
			log("just returned null because KORound = " + KORound + " which is less than 0 or greater than or equal to " + numberOfKORounds);
			return null;
		}
		
		if (KORound == 0)	return origins; // Rekursionsabbruch
		
		int skipARound = 0;
		// in case of a match for the third place, there is a gap of two rounds between the final and the semifinals
		if (matchForThirdPlace && KORound == numberOfKORounds - 1)	skipARound = 1;
		int koIndex = KORound - 1 - skipARound;
		
		String[] olderOrigins = new String[origins.length];
		for (int i = 0; i < olderOrigins.length; i++) {
			if (origins[i] == null) {
				olderOrigins[i] = null;
			} else if (teamsAreWinners) {
				olderOrigins[i] = koRunden[koIndex].getOriginOfWinnerOf(Integer.parseInt(origins[i].substring(2, 3)));
			} else {
				olderOrigins[i] = koRunden[koIndex].getOriginOfLoserOf(Integer.parseInt(origins[i].substring(2, 3)));
			}
		}
		
		return getGroupStageOriginsOfTeams(olderOrigins, true, KORound - 1 - skipARound);
	}
	
	/**
	 * This method returns the team with the given origin.
	 * @param teamsorigin origin of the team in the format 'GG1'
	 * @return
	 */
	public Mannschaft getTeamFromGroupstageOrigin(String teamsorigin) {
		Mannschaft mannschaft = null;
		
		if (teamsorigin != null && teamsorigin.charAt(0) == 'G') {
			// Bestimmen des Indexes der Gruppe
			int groupindex;
			for (groupindex = 0; groupindex < alphabet.length; groupindex++) {
				if (teamsorigin.charAt(1) == alphabet[groupindex])	break;
			}
			
			if (groupindex == alphabet.length) {
				error("    ungueltiger Gruppenindex:  " + groupindex + " fuer Buchstabe  " + teamsorigin.charAt(1));
				return null;
			}
			
			int placeindex = Integer.parseInt("" + teamsorigin.charAt(2));
			
			mannschaft = getTeamFromGroupstageOrigin(groupindex, placeindex);
		}
		
		return mannschaft;
	}
	
	private Mannschaft getTeamFromGroupstageOrigin(int groupindex, int placeindex) {
		Mannschaft mannschaft = null;
		
		if (groupindex >= 0 && groupindex < numberOfGroups) {
			mannschaft = gruppen[groupindex].getTeamOnPlace(placeindex);
		}
		
		return mannschaft;
	}
	
	public Mannschaft getDeepestOrigin(String origin, boolean teamsAreWinners, int KORound, boolean isQ) {
		Mannschaft deepestOrigin = null;
		boolean teamFound = false;
		int koIndex = -1;
		
		while (origin != null && !teamFound) {
			String origKOround = origin.substring(0, 2);
			int matchIndex = Integer.parseInt(origin.substring(2));
			
			boolean foundKO = false;
			if (isQ) {
				for (koIndex = 0; koIndex < qKORunden.length && !foundKO; koIndex++) {
					if (qKORunden[koIndex].getShortName().equals(origKOround))	foundKO = true;
				}
			} else {
				for (koIndex = 0; koIndex < koRunden.length && !foundKO; koIndex++) {
					if (koRunden[koIndex].getShortName().equals(origKOround))	foundKO = true;
				}
			}
			
			if (foundKO) {
				int teamIndex = 0;
				if (isQ) {
					if (teamsAreWinners)	teamIndex = qKORunden[koIndex - 1].getIndexOf(matchIndex, true);
					else					teamIndex = qKORunden[koIndex - 1].getIndexOf(matchIndex, false);
					deepestOrigin = qKORunden[koIndex - 1].getPrequalifiedTeam(teamIndex - 1);
				} else {
					if (teamsAreWinners)	teamIndex = koRunden[koIndex - 1].getIndexOf(matchIndex, true);
					else					teamIndex = koRunden[koIndex - 1].getIndexOf(matchIndex, false);
					deepestOrigin = koRunden[koIndex - 1].getPrequalifiedTeam(teamIndex - 1);
				}
				
				if (deepestOrigin != null) {
					teamFound = true;
				} else {
					if (isQ) {
						if (teamsAreWinners)	origin = qKORunden[koIndex - 1].getOriginOfWinnerOf(matchIndex);
						else					origin = qKORunden[koIndex - 1].getOriginOfLoserOf(matchIndex);
					} else {
						if (teamsAreWinners)	origin = koRunden[koIndex - 1].getOriginOfWinnerOf(matchIndex);
						else					origin = koRunden[koIndex - 1].getOriginOfLoserOf(matchIndex);
					}
					teamsAreWinners = true;
				}
			}
		}
		
		return deepestOrigin;
	}
	
	private void testGetGroupStageOriginsOfTeams() {
		log("\n\n");
		for (int i = 0; i < koRunden.length; i++) {
			log(koRunden[i].getName() + "\n-------------");
			koRunden[i].setCheckTeamsFromPreviousRound(false);
			Mannschaft[] teams = koRunden[i].getMannschaften();
			koRunden[i].setCheckTeamsFromPreviousRound(true);
			for (int j = 0; j < teams.length; j++) {
				Mannschaft team = teams[j];
				logWONL("Team " + (j + 1) + ": ");
				if (team != null) {
					log(team.getName());
				} else {
					log("n/a");
				}
			}
			log();
		}
	}
	
	@SuppressWarnings("unused")
	private Mannschaft getTeamFromDeepestOrigin(String teamsorigin) {
		Mannschaft mannschaft = null;
		// TODO find out what was supposed to happen here
		
		String identifier;
		int index;
		while (true) {
			break;
		}
		int koIndex;
		for (koIndex = 0; koIndex < koRunden.length; koIndex++) {
			if (teamsorigin.charAt(1) == alphabet[koIndex])	break;
		}
		
		// ("this is bullshit")
//		mannschaft = new Mannschaft(start, 0, this, koRunden[1]);
		
//		if (teamsorigin != null && teamsorigin.charAt(0) == 'G') {
//			// Bestimmen des Indexes der Gruppe
//			int groupindex;
//			for (groupindex = 0; groupindex < alphabet.length; groupindex++) {
//				if (teamsorigin.charAt(1) == alphabet[groupindex])	break;
//			}
//			
//			if (groupindex == alphabet.length) {
//				error("    ungueltiger Gruppenindex:  " + groupindex + " fuer Buchstabe  " + teamsorigin.charAt(1));
//				return null;
//			}
//			
//			int placeindex = Integer.parseInt("" + teamsorigin.charAt(2));
//			
//			mannschaft = getTeamFromGroupstageOrigin(groupindex, placeindex);
//		}
		
		return mannschaft;
	}
	
	// Laden / Speichern
	
	public void qualifikationLaden() {
		if (!hasQualification)	return;
		qualifikationDatenFromFile = ausDatei(dateiQualifikationDaten);
		
		numberOfQGroups = Integer.parseInt(qualifikationDatenFromFile.remove(0));
		if (numberOfQGroups > 0) {
			hasQGroupStage = true;
			qGruppen = new Gruppe[numberOfQGroups];
			for (int i = 0; i < numberOfQGroups; i++) {
				qGruppen[i] = new Gruppe(start, this, i, true);
			}
			{
				qOverview = new Spieltag(this.start, this, true);
				qOverview.setLocation((this.start.WIDTH - qOverview.getSize().width) / 2, (this.start.HEIGHT - 28 - qOverview.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
				qOverview.setVisible(false);
	    	}
		}
		
		numberOfQKORounds = Integer.parseInt(qualifikationDatenFromFile.remove(0));
		if (numberOfQKORounds > 0) {
			hasQKOStage = true;
			qKORunden = new KORunde[numberOfQKORounds];
			for (int i = 0; i < numberOfQKORounds; i++) {
				qKORunden[i] = new KORunde(start, this, 0, true, qualifikationDatenFromFile.remove(0));
			}
		}
	}
	
	public void qualifikationSpeichern() {
		if (!hasQualification)	return;
		
		qualifikationDatenFromFile.clear();
		
		qualifikationDatenFromFile.add("" + numberOfQGroups);
		for (int i = 0; i < numberOfQGroups; i++) {
			qGruppen[i].speichern();
		}
		
		qualifikationDatenFromFile.add("" + numberOfQKORounds);
		for (int i = 0; i < numberOfQKORounds; i++) {
			qKORunden[i].speichern();
			qualifikationDatenFromFile.add("" + qKORunden[i].toString());
		}
		
		inDatei(dateiQualifikationDaten, qualifikationDatenFromFile);
	}
	
	public void gruppenLaden() {
		if (!hasGroupStage)	return;
		gruppenDatenFromFile = ausDatei(dateiGruppenDaten);
		
		numberOfGroups = Integer.parseInt(gruppenDatenFromFile.get(0));
		gruppen = new Gruppe[numberOfGroups];
		for (int i = 0; i < gruppen.length; i++)	gruppen[i] = new Gruppe(this.start, this, i, false);
		{
    		overview = new Spieltag(this.start, this, false);
    		overview.setLocation((this.start.WIDTH - overview.getSize().width) / 2, (this.start.HEIGHT - 28 - overview.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
    		overview.setVisible(false);
    	}
	}
	
	public void gruppenSpeichern() {
		if (!hasGroupStage)	return;
		
		for (Gruppe gruppe : gruppen)	gruppe.speichern();
		
		gruppenDatenFromFile.clear();
		gruppenDatenFromFile.add("" + numberOfGroups);
		
		saveRanks();
		
		inDatei(dateiGruppenDaten, gruppenDatenFromFile);
	}
	
	public void koRundenLaden() {
		if (!hasKOStage)	return;
		koRundenDatenFromFile = ausDatei(dateiKORundenDaten);
		this.numberOfKORounds = koRundenDatenFromFile.size();
		
		koRunden = new KORunde[numberOfKORounds];
    	for (int i = 0; i < koRunden.length; i++)	koRunden[i] = new KORunde(start, this, i, false, koRundenDatenFromFile.get(i));
	}
	
	public void koRundenSpeichern() {
		if (!hasKOStage)	return;
		
		for (KORunde koRunde : koRunden)	koRunde.speichern();
		
		koRundenDatenFromFile.clear();
		for (int i = 0; i < koRunden.length; i++) {
			koRundenDatenFromFile.add(koRunden[i].toString());
		}
		
		inDatei(dateiKORundenDaten, koRundenDatenFromFile);
	}
	
	private void saveRanks() {
		ArrayList<String> allRanks = new ArrayList<>();
		for(Gruppe gruppe : gruppen) {
			String[] ranks = gruppe.getRanks();
			for (int i = 0; i < ranks.length; i++) {
				allRanks.add(ranks[i]);
			}
		}
		inDatei(workspace + "allRanks.txt", allRanks);
	}
	
	public void laden() {
		workspace = turnier.getWorkspace() + getSeasonFull("_") + File.separator;
		dateiQualifikationDaten = workspace + "QualiConfig.txt";
		dateiGruppenDaten = workspace + "GruppenConfig.txt";
		dateiKORundenDaten = workspace + "KOconfig.txt";
		
		qualifikationLaden();
		gruppenLaden();
		koRundenLaden();
		geladen = true;
		
		boolean debug = false;
		if (debug)	testGetGroupStageOriginsOfTeams();
	}
	
	public void speichern() {
		if (!geladen)	return;
		qualifikationSpeichern();
		gruppenSpeichern();
		koRundenSpeichern();
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += season + ";";
		toString += isSummerToSpringSeason + ";";
		toString += startDate + ";";
		toString += finalDate + ";";
		toString += hasQualification + ";";
		toString += hasGroupStage + ";";
		toString += hasKOStage + ";";
		toString += hasSecondLegGroupStage + ";";
		toString += hasSecondLegKOStage + ";";
		toString += matchForThirdPlace + ";";
		
		return toString;
	}
	
	public void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		season = Integer.parseInt(split[index++]);
		isSummerToSpringSeason = Boolean.parseBoolean(split[index++]);
		startDate = Integer.parseInt(split[index++]);
		finalDate = Integer.parseInt(split[index++]);
		hasQualification = Boolean.parseBoolean(split[index++]);
		hasGroupStage = Boolean.parseBoolean(split[index++]);
		hasKOStage = Boolean.parseBoolean(split[index++]);
		hasSecondLegGroupStage = Boolean.parseBoolean(split[index++]);
		hasSecondLegKOStage = Boolean.parseBoolean(split[index++]);
		matchForThirdPlace = Boolean.parseBoolean(split[index++]);
	}
}
