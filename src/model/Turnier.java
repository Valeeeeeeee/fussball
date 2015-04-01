package model;

import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class Turnier {
	private Start start;
	private int id;
	private String name;
	private String shortName;
	
	private int startDate;
	private int finalDate;
	
	private boolean isETPossible = false;
	private boolean isSummerToSpringSeason;
	private boolean hasGroupStage;
	private boolean hasKOStage;
	private boolean groupPhaseSecondLeg;
	private boolean koPhaseSecondLeg;
	private boolean matchForThirdPlace;
	
	private int[] seasons;
	private int aktuelleSaison;
	
	private Spieltag overview;
	
	private Gruppe[] gruppen;
	private KORunde[] koRunden;
	
	private int numberOfTeams;
	private int numberOfGroups;
	private int numberOfKORounds;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	private String dateiKORundenDaten;
	private String[] koRundenDatenFromFile;
	
	private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	public Turnier(int id, Start start, String daten) {
		checkOS();
		this.start = start;
		this.id = id;
		fromString(daten);
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getWorkspace(int season) {
		int seasonIndex = 0;
		for (seasonIndex = 0; seasonIndex < seasons.length; seasonIndex++) {
			if (seasons[seasonIndex] == season)	break;
		}
		return workspace + File.separator + name + File.separator + getSeason(seasonIndex) + File.separator;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	public boolean isSTSS() {
		return this.isSummerToSpringSeason;
	}
	
	public boolean isETPossible() {
		return this.isETPossible;
	}
	
	public int getNumberOfGroups() {
		return this.numberOfGroups;
	}
	
	public Gruppe[] getGruppen() {
		return this.gruppen;
	}
	
	public KORunde[] getKORunden() {
		return this.koRunden;
	}
	
	public int getStartDate() {
		return this.startDate;
	}
	
	public int getFinalDate() {
		return this.finalDate;
	}
	
	public boolean hasGroupStage() {
		return this.hasGroupStage;
	}
	
	public boolean hasKOStage() {
		return this.hasKOStage;
	}
	
	public boolean groupHasSecondLeg() {
		return this.groupPhaseSecondLeg;
	}
	
	public boolean koPhaseHasSecondLeg() {
		return this.koPhaseSecondLeg;
	}
	
	public int getNumberOfKORounds() {
		return this.numberOfKORounds;
	}
	
	/**
	 * This method returns all available seasons in the format "2014/2015" if isSTSS, otherwise "2014".
	 * @return a String array containing all available seasons
	 */
	public String[] getAllSeasons() {
		String[] hilfsarray = new String[this.seasons.length];
        for (int i = 0; i < this.seasons.length; i++) {
            hilfsarray[i] = this.seasons[i] + (this.isSummerToSpringSeason ? "/" + (this.seasons[i] + 1) : "");
        }
        return hilfsarray;
	}
	
	public String getSeasonsRepresentation() {
		String representation = "";
		for (int i = 0; i < this.seasons.length; i++) {
			String trenn = "S" + i;
			representation += trenn + "*" + this.seasons[i] + "*" + trenn + ",";
		}
		return representation.substring(0, representation.length() - 1);
	}
	
	public int[] getSeasonsFromRepresentation(String representation) {
		String[] seasonsReps = representation.split(",");
		int[] seasons = new int[seasonsReps.length];
		
		for (int i = 0; i < seasons.length; i++) {
			String rep = seasonsReps[i];
			seasons[i] = Integer.parseInt(rep.substring(rep.indexOf("*") + 1, rep.lastIndexOf("*")));
		}
		
		return seasons;
	}
	
	public String getSeason(int seasonIndex) {
		
		return this.seasons[seasonIndex] + (this.isSummerToSpringSeason ? "_" + (this.seasons[seasonIndex] + 1) : "");
	}
	
	public int getAktuelleSaison() {
		return this.seasons[this.aktuelleSaison];
	}
	
	public Mannschaft[] getMannschaftenInOrderOfOrigins(String[] origins, boolean teamsAreWinners, int KORound) {
		Mannschaft[] teamsInOrder = new Mannschaft[origins.length];
		
		if (hasGroupStage) {
			String[] groupOrigins = getGroupStageOriginsOfTeams(origins, teamsAreWinners, KORound);
			
			for (int i = 0; i < teamsInOrder.length; i++) {
				teamsInOrder[i] = getTeamFromGroupstageOrigin(groupOrigins[i]);
			}
		} else {
			for (int i = 0; i < teamsInOrder.length; i++) {
				teamsInOrder[i] = getDeepestOrigin(origins[i], teamsAreWinners, KORound);
			}
		}
		
		return teamsInOrder;
	}
	
	public Mannschaft getDeepestOrigin(String origin, boolean teamsAreWinners, int KORound) {
		Mannschaft deepestOrigin = null;
		boolean teamFound = false;
		int koIndex = -1;
		
		while (origin != null && !teamFound) {
			String origKOround = origin.substring(0, 2);
			int matchIndex = Integer.parseInt(origin.substring(2));
			
			boolean foundKO = false;
			for (koIndex = 0; koIndex < koRunden.length && !foundKO; koIndex++) {
				if (koRunden[koIndex].getShortName().equals(origKOround))	foundKO = true;
			}
			
			if (foundKO) {
				int teamIndex = 0;
				if (teamsAreWinners)	teamIndex = koRunden[koIndex - 1].getIndexOf(matchIndex, true);
				else					teamIndex = koRunden[koIndex - 1].getIndexOf(matchIndex, false);
				
				deepestOrigin = koRunden[koIndex - 1].getPrequalifiedTeam(teamIndex - 1);
				if (deepestOrigin != null) {
					teamFound = true;
				} else {
					if (teamsAreWinners)	origin = koRunden[koIndex - 1].getOriginOfWinnerOf(matchIndex);
					else					origin = koRunden[koIndex - 1].getOriginOfLoserOf(matchIndex);
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
	
	public Mannschaft getTeamFromDeepestOrigin(String teamsorigin) {
		Mannschaft mannschaft = null;
		
		
		String identifier;
		int index;
		while (true) {
			break;
		}
		int koIndex;
		for (koIndex = 0; koIndex < koRunden.length; koIndex++) {
			if (teamsorigin.charAt(1) == alphabet[koIndex])	break;
		}
		
		// TODO this is bullshit
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
////			log("gueltiger Gruppenindex:  " + groupindex + " fuer Buchstabe  " + teamsorigin.charAt(1));
//			
//			int placeindex = Integer.parseInt("" + teamsorigin.charAt(2));
//			
//			mannschaft = getTeamFromGroupstageOrigin(groupindex, placeindex);
//		}
		
		return mannschaft;
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
//			log("gueltiger Gruppenindex:  " + groupindex + " fuer Buchstabe  " + teamsorigin.charAt(1));
			
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
	
	public Spieltag getSpieltag() {
		return this.overview;
	}
	
	public int getCurrentMatchday() {
		int matchday = gruppen[0].getCurrentMatchday(), altMD = -1;
		for (int i = 1; i < numberOfGroups && altMD == -1; i++) {
			if (matchday != gruppen[i].getCurrentMatchday()) altMD = gruppen[i].getCurrentMatchday();	
		}
		if (altMD != -1) {
			log("\n\nGet the current matchday properly!!\n\n"); // TODO
			matchday = matchday < altMD ? matchday : altMD;
		}
		return matchday;
	}
	
	public int[] getChronologicalOrder(int matchday) {
		int numberOfMatches = 0;
		for (int i = 0; i < numberOfGroups; i++) {
			numberOfMatches += gruppen[i].getNumberOfMatchesPerMatchday();
		}
		int[] newOrder = new int[numberOfMatches];
		int[] hilfsarray = new int[numberOfMatches];
		int[] dates = new int[numberOfMatches];
		int[] times = new int[numberOfMatches];
		
		int matchID = 0;
		for (Gruppe gruppe : gruppen) {
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
	
	public void ergebnisseSichern() {
		// when in overview mode
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
	
	private void saveRanks() {
		ArrayList<String> allRanks = new ArrayList<>();
		for(Gruppe gruppe : gruppen) {
			String[] ranks = gruppe.getRanks();
			for (int i = 0; i < ranks.length; i++) {
				allRanks.add(ranks[i]);
			}
		}
		inDatei(workspace + File.separator + name + File.separator + getSeason(aktuelleSaison) + File.separator + "allRanks.txt", allRanks);
	}
	
	public void laden(int index) {
		aktuelleSaison = index;
		
		dateiKORundenDaten = workspace + File.separator + name + File.separator + getSeason(aktuelleSaison) + File.separator + "KOconfig.txt";
		
		if (hasGroupStage)	gruppenLaden();
    	if (hasKOStage) {
    		koRundenLaden();
        	testGetGroupStageOriginsOfTeams();
    	}
	}
	
	public void speichern() {
		if (hasGroupStage) {
			for (Gruppe gruppe : gruppen)		gruppe.speichern();
			saveRanks();
		}
		if (hasKOStage) {
			for (KORunde koRunde : koRunden)	koRunde.speichern();
			
			koRundenDatenFromFile = new String[numberOfKORounds];
			for (int i = 0; i < numberOfKORounds; i++) {
				koRundenDatenFromFile[i] = koRunden[i].toString();
			}
			
			inDatei(dateiKORundenDaten, koRundenDatenFromFile);
		}
	}
	
	private void gruppenLaden() {
		gruppen = new Gruppe[this.numberOfGroups];
    	for (int i = 0; i < gruppen.length; i++)	gruppen[i] = new Gruppe(this.start, i, this);
    	{
    		overview = new Spieltag(this.start, this);
    		overview.setLocation((this.start.WIDTH - overview.getSize().width) / 2, (this.start.HEIGHT - 28 - overview.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
    		overview.setVisible(false);
    	}
	}
	
	private void koRundenLaden() {
    	this.koRundenDatenFromFile = ausDatei(dateiKORundenDaten);
    	this.numberOfKORounds = koRundenDatenFromFile.length;
    	
		koRunden = new KORunde[this.numberOfKORounds];
    	for (int i = 0; i < koRunden.length; i++)	koRunden[i] = new KORunde(this.start, this, i, koRundenDatenFromFile[i]);
	}
	
	private void fromString(String daten) {
		String[] alleDaten = daten.split(";");
		
		this.name = alleDaten[0].substring(5);
		this.shortName = alleDaten[1].substring(4);
		this.isSummerToSpringSeason = Boolean.parseBoolean(alleDaten[2].substring(7));
		this.startDate = Integer.parseInt(alleDaten[3].substring(7));
		this.finalDate = Integer.parseInt(alleDaten[4].substring(7));
		this.numberOfTeams = Integer.parseInt(alleDaten[5].substring(9));
		this.hasGroupStage = Boolean.parseBoolean(alleDaten[6].substring(7));
		this.hasKOStage = Boolean.parseBoolean(alleDaten[7].substring(6));
		this.numberOfGroups = Integer.parseInt(alleDaten[8].substring(8));
		this.groupPhaseSecondLeg = Boolean.parseBoolean(alleDaten[9].substring(10));
		this.koPhaseSecondLeg = Boolean.parseBoolean(alleDaten[10].substring(9));
		this.matchForThirdPlace = Boolean.parseBoolean(alleDaten[11].substring(10));
		this.seasons = getSeasonsFromRepresentation(alleDaten[12]);
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
		alles += "SHN*" + this.shortName + ";";
		alles += "ISSTSS*" + this.isSummerToSpringSeason + ";";
		alles += "STDATE*" + this.startDate + ";";
		alles += "FIDATE*" + this.finalDate + ";";
		alles += "NOFTEAMS*" + this.numberOfTeams + ";";
		alles += "GRPSTG*" + this.hasGroupStage + ";";
		alles += "KOSTG*" + this.hasKOStage + ";";
		alles += "NOFGRPS*" + this.numberOfGroups + ";";
		alles += "GRPSECLEG*" + this.groupPhaseSecondLeg + ";";
		alles += "KOSECLEG*"+ this.koPhaseSecondLeg + ";";
		alles += "MATCHF3PL*" + this.matchForThirdPlace + ";";
		alles += getSeasonsRepresentation() + ";";
		return alles;
	}
	
	public void checkOS() {
		if (new File(workspaceWIN).isDirectory()) {
//			message("You are running Windows.");
			workspace = workspaceWIN;
		} else if (new File(workspaceMAC).isDirectory()) {
//			message("You have a Mac.");
			workspace = workspaceMAC;
		} else {
//			message("You are running neither OS X nor Windows, probably Linux!");
			workspace = null;
		}
	}
}




