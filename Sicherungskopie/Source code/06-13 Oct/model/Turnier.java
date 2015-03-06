package model;

import java.io.File;

import static util.Utilities.*;

public class Turnier {
	private Start start;
	private int id;
	private String name;
	
	private int startDate;
	private int finalDate;
	
	private boolean isSummerToSpringSeason;
	private boolean hasGroupStage;
	private boolean hasKOStage;
	private boolean groupPhaseSecondLeg;
	private boolean koPhaseSecondLeg;
	private boolean matchForThirdPlace;
	
	private int[] seasons;
	private int aktuelleSaison;
	
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
	
	public String getName() {
		return this.name;
	}
	
	public boolean isSTSS() {
		return this.isSummerToSpringSeason;
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
	
	public int getAktuelleSaison() {
		return this.seasons[this.aktuelleSaison];
	}
	
	public Mannschaft[] getMannschaftenInOrderOfOrigins(String[] origins, boolean teamsAreWinners, int KORound) {
		Mannschaft[] teamsInOrder = new Mannschaft[origins.length];
		
		String[] deepestOrigins = getGroupStageOriginsOfTeams(origins, teamsAreWinners, KORound);
		
		for (int i = 0; i < teamsInOrder.length; i++) {
			teamsInOrder[i] = getTeamFromGroupstageOrigin(deepestOrigins[i]);
		}
		
		return teamsInOrder;
	}
	
	private void testGetGroupStageOriginsOfTeams() {
		log("\n\n");
		for (int i = 0; i < koRunden.length; i++) {
			log(koRunden[i].getName() + "\n-------------");
			Mannschaft[] teams = koRunden[i].getMannschaften();
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
		
		String[] olderOrigins = new String[origins.length];
		for (int i = 0; i < olderOrigins.length; i++) {
			if (origins[i] == null) {
				olderOrigins[i] = null;
			} else if (teamsAreWinners) {
				olderOrigins[i] = koRunden[KORound - 1 - skipARound].getOriginOfWinnerOf(Integer.parseInt(origins[i].substring(2, 3)));
			} else {
				olderOrigins[i] = koRunden[KORound - 1 - skipARound].getOriginOfLoserOf(Integer.parseInt(origins[i].substring(2, 3)));
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
	
	public void laden(int index) {
		String saison;
		if (isSummerToSpringSeason)		saison = seasons[aktuelleSaison] + "_" + (seasons[aktuelleSaison] + 1);
		else							saison = "" + seasons[aktuelleSaison];
		aktuelleSaison = index;
		
		dateiKORundenDaten = workspace + File.separator + name + File.separator + saison + File.separator + "KOconfig.txt";
		
		if (hasGroupStage)	gruppenLaden();
    	koRundenLaden();
    	
    	testGetGroupStageOriginsOfTeams();
	}
	
	public void speichern() {
		for (Gruppe gruppe : gruppen)		gruppe.speichern();
		for (KORunde koRunde : koRunden)	koRunde.speichern();
		
		inDatei(dateiKORundenDaten, koRundenDatenFromFile);
	}
	
	private void gruppenLaden() {
		gruppen = new Gruppe[this.numberOfGroups];
    	for (int i = 0; i < gruppen.length; i++)	gruppen[i] = new Gruppe(this.start, i, this);
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
		this.isSummerToSpringSeason = Boolean.parseBoolean(alleDaten[1].substring(7));
		this.startDate = Integer.parseInt(alleDaten[2].substring(7));
		this.finalDate = Integer.parseInt(alleDaten[3].substring(7));
		this.numberOfTeams = Integer.parseInt(alleDaten[4].substring(9));
		this.hasGroupStage = Boolean.parseBoolean(alleDaten[5].substring(7));
		this.hasKOStage = Boolean.parseBoolean(alleDaten[6].substring(6));
		this.numberOfGroups = Integer.parseInt(alleDaten[7].substring(8));
		this.groupPhaseSecondLeg = Boolean.parseBoolean(alleDaten[8].substring(10));
		this.koPhaseSecondLeg = Boolean.parseBoolean(alleDaten[9].substring(9));
		this.matchForThirdPlace = Boolean.parseBoolean(alleDaten[10].substring(10));
		this.seasons = getSeasonsFromRepresentation(alleDaten[11]);
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
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