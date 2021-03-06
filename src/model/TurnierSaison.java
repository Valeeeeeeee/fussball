package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static util.Utilities.*;

public class TurnierSaison {
	
	private Turnier tournament;
	private boolean isSummerToSpringSeason;
	private int seasonIndex;
	private int year;
	private Datum startDate;
	private Datum finalDate;
	private Datum qStartDate;
	private Datum qFinalDate;
	
	private boolean hasQualification;
	private boolean hasGroupStage;
	private boolean hasKOStage;
	private boolean hasSecondLegGroupStage;
	private boolean goalDifferenceGroupStage;
	private boolean fairplayGroupStage;
	private boolean matchForThirdPlace;
	private boolean teamsHaveKader;
	private boolean qTeamsHaveKader;
	private boolean isFourthSubPossible;
	
	private HashMap<Dauer, Integer> numberOfSubstitutions;
	
	private Map<String, Mannschaft> teamsFromOtherCompetition = new HashMap<>();
	
	private Spieltag overview;
	private Gruppe[] groups;
	private KORunde[] koRounds;
	
	private int numberOfReferees;
	private ArrayList<Schiedsrichter> referees;
	
	private boolean hasQGroupStage;
	private boolean hasQKOStage;
	private boolean hasSecondLegQGroupStage;
	private boolean goalDifferenceQGroupStage;
	private boolean fairplayQGroupStage;
	private int untilRankBestQGroupXths = 5;
	
	private Spieltag qOverview;
	private Gruppe[] qGroups;
	private KORunde[] qKORounds;
	
	private int numberOfQGroups;
	private int numberOfQKORounds;
	
	private int numberOfGroups;
	private int numberOfKORounds;
	
	private boolean geladen;
	private String workspace;
	
	private String fileQualificationData;
	private ArrayList<String> qualificationDataFromFile;
	
	private String fileGroupsData;
	private ArrayList<String> groupsDataFromFile;
	
	private String fileKORoundsData;
	private ArrayList<String> koRoundsDataFromFile;
	
	private String fileReferees;
	private ArrayList<String> refereesFromFile;
	
	public TurnierSaison(Turnier tournament, int seasonIndex, String data) {
		this.tournament = tournament;
		this.seasonIndex = seasonIndex;
		fromString(data);
		workspace = tournament.getWorkspace() + getSeasonFull("_") + File.separator;
	}
	
	public Turnier getTournament() {
		return tournament;
	}
	
	public int getID() {
		return seasonIndex;
	}
	
	public int getYear() {
		return year;
	}
	
	public boolean isSTSS() {
		return isSummerToSpringSeason;
	}
	
	public boolean isClubCompetition() {
		return tournament.isClubCompetition();
	}
	
	public String getSeasonFull(String trennzeichen) {
		return year + (isSummerToSpringSeason ? trennzeichen + (year + 1) : "");
	}
	
	public String getDescription() {
		return tournament.getName() + " " + getSeasonFull("/");
	}
	
	public Datum getStartDate(boolean isQ) {
		return isQ ? qStartDate : startDate;
	}
	
	public Datum getFinalDate(boolean isQ) {
		return isQ ? qFinalDate : finalDate;
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

	public boolean hasSecondLegQGroupStage() {
		return hasSecondLegQGroupStage;
	}
	
	public boolean hasMatchForThirdPlace() {
		return matchForThirdPlace;
	}
	
	public boolean teamsHaveKader(boolean isQ) {
		return isQ ? qTeamsHaveKader : teamsHaveKader;
	}
	
	public int getNumberOfRegularSubstitutions(Datum date) {
		if (!numberOfSubstitutions.isEmpty()) {
			Set<Dauer> durations = numberOfSubstitutions.keySet();
			for (Dauer duration : durations) {
				if (duration.includes(date))	return numberOfSubstitutions.get(duration);
			}
		}
		return Fussball.numberOfRegularSubstitutions;
	}
	
	public boolean isFourthSubPossible() {
		return isFourthSubPossible;
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
	
	public Gruppe[] getGroups() {
		return groups;
	}
	
	public KORunde[] getKORounds() {
		return koRounds;
	}
	
	public Gruppe[] getQGroups() {
		return qGroups;
	}
	
	public KORunde[] getQKORounds() {
		return qKORounds;
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	// Saison-spezifische Methoden
	
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
	
	public int getCurrentMatchday(boolean isQualification) {
		int matchday = 0, altMD = -1;
		if (isQualification) {
			matchday = qGroups[0].getOverviewMatchday();
			for (int i = 1; i < numberOfQGroups && altMD == -1; i++) {
				if (matchday != qGroups[i].getOverviewMatchday()) altMD = qGroups[i].getOverviewMatchday();	
			}
			if (altMD != -1) {
				// always use the earlier matchday to remember setting the result
				matchday = matchday < altMD ? matchday : altMD;
			}
		} else {
			matchday = groups[0].getOverviewMatchday();
			for (int i = 1; i < numberOfGroups && altMD == -1; i++) {
				if (matchday != groups[i].getOverviewMatchday()) altMD = groups[i].getOverviewMatchday();	
			}
			if (altMD != -1) {
				// always use the earlier matchday to remember setting the result
				matchday = matchday < altMD ? matchday : altMD;
			}
		}
		
		return matchday;
	}
	
	public int[] getChronologicalOrder(boolean isQualification, int matchday) {
		int numberOfMatches = 0;
		if (isQualification) {
			for (int i = 0; i < numberOfQGroups; i++) {
				qGroups[i].changeOrderToChronological(matchday);
				numberOfMatches += qGroups[i].getNumberOfMatchesPerMatchday();
			}
		} else {
			for (int i = 0; i < numberOfGroups; i++) {
				groups[i].changeOrderToChronological(matchday);
				numberOfMatches += groups[i].getNumberOfMatchesPerMatchday();
			}
		}
		
		int[] newOrder = new int[numberOfMatches];
		int[] hilfsarray = new int[numberOfMatches];
		Datum[] dates = new Datum[numberOfMatches];
		Uhrzeit[] times = new Uhrzeit[numberOfMatches];
		
		int matchID = 0;
		for (Gruppe group : isQualification ? qGroups : groups) {
			for (int match = 0; match < group.getNumberOfMatchesPerMatchday(); match++) {
				dates[matchID] = group.getDate(matchday, match);
				times[matchID] = group.getTime(matchday, match);
				matchID++;
			}
		}
		
		for (int m = 0; m < numberOfMatches; m++) {
			for (int m2 = m + 1; m2 < numberOfMatches; m2++) {
				if (dates[m2].isAfter(dates[m]))										hilfsarray[m2]++;
				else if (dates[m2].equals(dates[m]) && !times[m2].isBefore(times[m]))	hilfsarray[m2]++;
				else																	hilfsarray[m]++;
			}
		}
		
		for (int i = 0; i < hilfsarray.length; i++) {
			newOrder[hilfsarray[i]] = i;
		}
		
		return newOrder;
	}
	
	public String[] getOverviewMatchdays(boolean isQ) {
		int numberOfMatchdays = 0;
		for (Gruppe group : (isQ ? qGroups : groups)) {
			if (numberOfMatchdays < group.getNumberOfMatchdays())	numberOfMatchdays = group.getNumberOfMatchdays();
		}
		
		String[] matchdays = new String[numberOfMatchdays];
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdays[i] = (i + 1) + ". Spieltag";
		}
		return matchdays;
	}
	
	public ArrayList<String[]> getAllMatches(Mannschaft team) {
		ArrayList<String[]> allMatches = new ArrayList<>();
		
		for (int i = 0; i < numberOfQGroups; i++) {
			ArrayList<String[]> matches = qGroups[i].getMatches(team);
			if (matches.size() > 0) {
				allMatches.add(new String[] {SUB_CATEGORY, "Gruppenphase"});
				for (int j = 0; j < matches.size(); j++) {
					allMatches.add(matches.get(j));
				}
				break;
			}
		}
		for (int i = 0; i < numberOfQKORounds; i++) {
			ArrayList<String[]> matches = qKORounds[i].getMatches(team);
			if (matches.size() > 0) {
				allMatches.add(new String[] {SUB_CATEGORY, qKORounds[i].getDescription()});
				for (int j = 0; j < matches.size(); j++) {
					allMatches.add(matches.get(j));
				}
			}
		}
		if (allMatches.size() > 0)	allMatches.add(0, new String[] {MAIN_CATEGORY, "Qualifikation"});
		for (int i = 0; i < numberOfGroups; i++) {
			ArrayList<String[]> matches = groups[i].getMatches(team);
			if (matches.size() > 0) {
				allMatches.add(new String[] {MAIN_CATEGORY, "Gruppenphase"});
				for (int j = 0; j < matches.size(); j++) {
					allMatches.add(matches.get(j));
				}
				break;
			}
		}
		for (int i = 0; i < numberOfKORounds; i++) {
			ArrayList<String[]> matches = koRounds[i].getMatches(team);
			if (matches.size() > 0) {
				allMatches.add(new String[] {MAIN_CATEGORY, koRounds[i].getDescription()});
				for (int j = 0; j < matches.size(); j++) {
					allMatches.add(matches.get(j));
				}
			}
		}
		
		return allMatches;
	}
	
	public Mannschaft getTeamWithName(String teamName) {
		Mannschaft team = null;
		if (hasGroupStage) {
			for (Gruppe gruppe : groups) {
				team = gruppe.getTeamWithName(teamName);
				if (team != null)	return team;
			}
		}
		if (hasKOStage) {
			for (KORunde koRound : koRounds) {
				team = koRound.getTeamWithName(teamName);
				if (team != null)	return team;
			}
		}
		if (hasQualification) {
			if (hasQGroupStage) {
				for (Gruppe gruppe : qGroups) {
					team = gruppe.getTeamWithName(teamName);
					if (team != null)	return team;
				}
			}
			if (hasQKOStage) {
				for (KORunde koRound : qKORounds) {
					team = koRound.getTeamWithName(teamName);
					if (team != null)	return team;
				}
			}
		}
		
		return team;
	}
	
	public Mannschaft getTeamFromOtherCompetition(String origin) {
		if (teamsFromOtherCompetition.containsKey(origin)) {
			return teamsFromOtherCompetition.get(origin);
		}
		return null;
	}
	
	public Mannschaft getTeamFromOtherCompetition(int id, Wettbewerb competition, String origin) {
		Mannschaft team = null;
		
		if (teamsFromOtherCompetition.containsKey(origin)) {
			return teamsFromOtherCompetition.get(origin);
		}
		team = new Mannschaft(id, competition, getNameOfTeamFromOtherCompetition(origin));
		teamsFromOtherCompetition.put(origin, team);
		
		return team;
	}
	
	private String getNameOfTeamFromOtherCompetition(String origin) {
		String fileName = Fussball.getInstance().getTournamentWorkspaceFromShortName(origin.substring(0, 2), Integer.parseInt(origin.substring(2,6)));
		
		ArrayList<String> teams = readFile(fileName + "allRanks.txt");
		for (String team : teams) {
			if (origin.substring(6).equals(team.split(": ")[0])) {
				return team.split(": ")[1];
			}
		}
		
		return origin;
	}
	
	public Mannschaft[] getTeamsInOrderOfOrigins(String[] origins, boolean teamsAreWinners, int KORound, boolean isQ) {
		Mannschaft[] orderOfOrigins = new Mannschaft[origins.length];
		
		if (isQ) {
			if (hasQGroupStage) {
				String[] groupOrigins = getGroupStageOriginsOfTeams(origins, teamsAreWinners, KORound, isQ);
				
				for (int i = 0; i < orderOfOrigins.length; i++) {
					orderOfOrigins[i] = getTeamFromGroupstageOrigin(groupOrigins[i], isQ);
				}
			} else {
				for (int i = 0; i < orderOfOrigins.length; i++) {
					orderOfOrigins[i] = getDeepestOrigin(origins[i], teamsAreWinners, KORound, isQ);
				}
			}
		} else {
			if (hasGroupStage) {
				String[] groupOrigins = getGroupStageOriginsOfTeams(origins, teamsAreWinners, KORound, isQ);
				
				for (int i = 0; i < orderOfOrigins.length; i++) {
					orderOfOrigins[i] = getTeamFromGroupstageOrigin(groupOrigins[i], isQ);
				}
			} else {
				for (int i = 0; i < orderOfOrigins.length; i++) {
					orderOfOrigins[i] = getDeepestOrigin(origins[i], teamsAreWinners, KORound, isQ);
				}
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
	private String[] getGroupStageOriginsOfTeams(String[] origins, boolean teamsAreWinners, int KORound, boolean isQ) {
		if (KORound < 0 || (isQ && KORound >= numberOfQKORounds) || (!isQ && KORound >= numberOfKORounds)) {
			log("just returned null because KORound = " + KORound + " which is less than 0 or greater than or equal to " + (isQ ? numberOfQKORounds : numberOfKORounds));
			return null;
		}
		
		if (KORound == 0)	return origins; // Rekursionsabbruch
		
		int skipARound = 0;
		// in case of a match for the third place, there is a gap of two rounds between the final and the semifinals
		if (!isQ && matchForThirdPlace && KORound == numberOfKORounds - 1)	skipARound = 1;
		int koIndex = KORound - 1 - skipARound;
		
		String[] olderOrigins = new String[origins.length];
		for (int i = 0; i < olderOrigins.length; i++) {
			if (origins[i] == null) {
				olderOrigins[i] = null;
			} else if (teamsAreWinners) {
				if (isQ)	olderOrigins[i] = qKORounds[koIndex].getOriginOfWinnerOf(Integer.parseInt(origins[i].substring(2)));
				else		olderOrigins[i] = koRounds[koIndex].getOriginOfWinnerOf(Integer.parseInt(origins[i].substring(2)));
			} else {
				if (isQ)	olderOrigins[i] = qKORounds[koIndex].getOriginOfLoserOf(Integer.parseInt(origins[i].substring(2)));
				else		olderOrigins[i] = koRounds[koIndex].getOriginOfLoserOf(Integer.parseInt(origins[i].substring(2)));
			}
		}
		
		return getGroupStageOriginsOfTeams(olderOrigins, true, KORound - 1 - skipARound, isQ);
	}
	
	/**
	 * This method returns the team with the given origin.
	 * @param teamsorigin origin of the team in the format 'GG1'
	 * @return
	 */
	public Mannschaft getTeamFromGroupstageOrigin(String teamsorigin, boolean isQ) {
		Mannschaft mannschaft = null;
		
		if (teamsorigin != null && teamsorigin.length() > 8) {
			// aus anderem Wettbewerb
			mannschaft = getTeamFromOtherCompetition(teamsorigin);
		} else if (teamsorigin != null && teamsorigin.charAt(0) == 'G') {
			// Bestimmen des Indexes der Gruppe
			int groupindex;
			for (groupindex = 0; groupindex < alphabet.length; groupindex++) {
				if (teamsorigin.charAt(1) == alphabet[groupindex])	break;
			}
			
			if (groupindex == alphabet.length) {
				// check for best x-th-placed team
				int nOfGroups = isQ ? numberOfQGroups : numberOfGroups;
				Gruppe[] grps = isQ ? qGroups : groups;
				int xBest = (int) teamsorigin.charAt(2) - 48;
				int placeindex = (int) teamsorigin.charAt(1) - 48;
				int untilRank = isQ ? untilRankBestQGroupXths : Integer.MAX_VALUE;
				ArrayList<Mannschaft> groupXth = new ArrayList<>();
				ArrayList<Integer> order = new ArrayList<>();
				for (int i = 0; i < nOfGroups; i++) {
					groupXth.add(getTeamFromGroupstageOrigin(i, placeindex, isQ));
					order.add(1);
					untilRank = Math.min(untilRank, grps[i].getNumberOfTeams());
				}
				
				int[] points = new int[nOfGroups], tdiff = new int[nOfGroups], tplus = new int[nOfGroups];
				for (int i = 0; i < nOfGroups; i++) {
					if (groupXth.get(i) == null)	continue;
					points[i] = groupXth.get(i).get(9, 0, grps[i].getNumberOfMatchdays() - 1, untilRank);
					tdiff[i] = groupXth.get(i).get(8, 0, grps[i].getNumberOfMatchdays() - 1, untilRank);
					tplus[i] = groupXth.get(i).get(6, 0, grps[i].getNumberOfMatchdays() - 1, untilRank);
				}
				
				for (int i = 0; i < nOfGroups - 1; i++) {
					for (int j = i + 1; j < nOfGroups; j++) {
						if (groupXth.get(i) != null && groupXth.get(j) != null) {
							if (points[i] < points[j])		order.set(i, order.get(i) + 1);
							else if (points[j] < points[i])	order.set(j, order.get(j) + 1);
							else {
								if (tdiff[i] < tdiff[j])		order.set(i, order.get(i) + 1);
								else if (tdiff[j] < tdiff[i])	order.set(j, order.get(j) + 1);
								else {
									if (tplus[i] < tplus[j])		order.set(i, order.get(i) + 1);
									else if (tplus[j] < tplus[i])	order.set(j, order.get(j) + 1);
									else {
										// TODO implement tie-breaker
										order.set(j, order.get(i) + 1);
									}
								}
							}
						} else if (groupXth.get(i) != null) {
							order.set(j, order.get(j) + 1);
						} else if (groupXth.get(j) != null) {
							order.set(i, order.get(i) + 1);
						}
					}
				}
				
				for (int i = 0; i < order.size(); i++) {
					if (order.get(i) == xBest) {
						return groupXth.get(i);
					}
				}
				
				groupindex = (0 < placeindex && placeindex < 10 ? Integer.MAX_VALUE : -1);
			}
			
			if (groupindex == -1) {
				error("\tungültiger Gruppenindex:  " + groupindex + " für Zeichen  " + teamsorigin.charAt(1));
				return null;
			}
			
			int placeindex = (int) teamsorigin.charAt(2) - 48;
			
			mannschaft = getTeamFromGroupstageOrigin(groupindex, placeindex, isQ);
		}
		
		return mannschaft;
	}
	
	private Mannschaft getTeamFromGroupstageOrigin(int groupindex, int placeindex, boolean isQ) {
		Mannschaft team = null;
		
		if (isQ && groupindex >= 0 && groupindex < numberOfQGroups) {
			team = qGroups[groupindex].getTeamOnPlace(placeindex);
		} else if (!isQ && groupindex >= 0 && groupindex < numberOfGroups) {
			team = groups[groupindex].getTeamOnPlace(placeindex);
		}
		
		return team;
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
				for (koIndex = 0; koIndex < qKORounds.length && !foundKO; koIndex++) {
					if (qKORounds[koIndex].getShortName().equals(origKOround))	foundKO = true;
				}
			} else {
				for (koIndex = 0; koIndex < koRounds.length && !foundKO; koIndex++) {
					if (koRounds[koIndex].getShortName().equals(origKOround))	foundKO = true;
				}
			}
			
			if (foundKO) {
				int teamIndex = 0;
				if (isQ) {
					if (teamsAreWinners)	teamIndex = qKORounds[koIndex - 1].getIndexOf(matchIndex, true);
					else					teamIndex = qKORounds[koIndex - 1].getIndexOf(matchIndex, false);
					deepestOrigin = qKORounds[koIndex - 1].getInvariantTeam(teamIndex - 1);
				} else {
					if (teamsAreWinners)	teamIndex = koRounds[koIndex - 1].getIndexOf(matchIndex, true);
					else					teamIndex = koRounds[koIndex - 1].getIndexOf(matchIndex, false);
					deepestOrigin = koRounds[koIndex - 1].getInvariantTeam(teamIndex - 1);
				}
				
				if (deepestOrigin != null) {
					teamFound = true;
				} else {
					if (isQ) {
						if (teamsAreWinners)	origin = qKORounds[koIndex - 1].getOriginOfWinnerOf(matchIndex);
						else					origin = qKORounds[koIndex - 1].getOriginOfLoserOf(matchIndex);
					} else {
						if (teamsAreWinners)	origin = koRounds[koIndex - 1].getOriginOfWinnerOf(matchIndex);
						else					origin = koRounds[koIndex - 1].getOriginOfLoserOf(matchIndex);
					}
					teamsAreWinners = true;
				}
			}
		}
		
		return deepestOrigin;
	}
	
	private void testGetGroupStageOriginsOfTeams() {
		log("\n\n");
		for (int i = 0; i < koRounds.length; i++) {
			log(koRounds[i].getDescription() + "\n-------------");
			koRounds[i].setCheckTeamsFromPreviousRound(false);
			Mannschaft[] teams = koRounds[i].getTeams();
			koRounds[i].setCheckTeamsFromPreviousRound(true);
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
	
	// Laden / Speichern
	
	public void loadReferees() {
		refereesFromFile = readFile(fileReferees, false);
		
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
		
		if (refereesFromFile.size() > 0)	writeFile(fileReferees, refereesFromFile);
	}
	
	public void loadQualification() {
		if (!hasQualification)	return;
		qualificationDataFromFile = readFile(fileQualificationData);
		int index = 0;
		
		qStartDate = new Datum(qualificationDataFromFile.get(index++));
		qFinalDate = new Datum(qualificationDataFromFile.get(index++));
		qTeamsHaveKader = Boolean.parseBoolean(qualificationDataFromFile.get(index++));
		numberOfQGroups = Integer.parseInt(qualificationDataFromFile.get(index++));
		if (numberOfQGroups > 0) {
			hasQGroupStage = true;
			hasSecondLegQGroupStage = Boolean.parseBoolean(qualificationDataFromFile.get(index++));
			goalDifferenceQGroupStage = Boolean.parseBoolean(qualificationDataFromFile.get(index++));
			fairplayQGroupStage = Boolean.parseBoolean(qualificationDataFromFile.get(index++));
			untilRankBestQGroupXths = Integer.parseInt(qualificationDataFromFile.get(index++));
			qGroups = new Gruppe[numberOfQGroups];
			for (int i = 0; i < numberOfQGroups; i++)	qGroups[i] = new Gruppe(this, i, true, goalDifferenceQGroupStage, fairplayQGroupStage);
			{
				qOverview = new Spieltag(this, true);
				qOverview.setLocation((Fussball.WIDTH - qOverview.getSize().width) / 2, (Fussball.HEIGHT - 28 - qOverview.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
				qOverview.setVisible(false);
			}
		}
		
		numberOfQKORounds = Integer.parseInt(qualificationDataFromFile.get(index++));
		if (numberOfQKORounds > 0) {
			hasQKOStage = true;
			qKORounds = new KORunde[numberOfQKORounds];
			for (int i = 0; i < numberOfQKORounds; i++)	qKORounds[i] = new KORunde(this, i, true, qualificationDataFromFile.get(index++));
		}
	}
	
	public void saveQualification() {
		if (!hasQualification)	return;
		
		qualificationDataFromFile.clear();
		
		qualificationDataFromFile.add("" + qStartDate.comparable());
		qualificationDataFromFile.add("" + qFinalDate.comparable());
		qualificationDataFromFile.add("" + qTeamsHaveKader);
		qualificationDataFromFile.add("" + numberOfQGroups);
		if (hasQGroupStage) {
			qualificationDataFromFile.add("" + hasSecondLegQGroupStage);
			qualificationDataFromFile.add("" + goalDifferenceQGroupStage);
			qualificationDataFromFile.add("" + fairplayQGroupStage);
			qualificationDataFromFile.add("" + untilRankBestQGroupXths);
			for (Gruppe group : qGroups)	group.save();
		}
		
		qualificationDataFromFile.add("" + numberOfQKORounds);
		if (hasQKOStage) {
			for (int i = 0; i < numberOfQKORounds; i++) {
				qKORounds[i].save();
				qualificationDataFromFile.add("" + qKORounds[i].toString());
			}
		}
		
		writeFile(fileQualificationData, qualificationDataFromFile);
	}
	
	public void loadGroups() {
		if (!hasGroupStage)	return;
		groupsDataFromFile = readFile(fileGroupsData);
		int index = 0;
		
		numberOfGroups = Integer.parseInt(groupsDataFromFile.get(index++));
		hasSecondLegGroupStage = Boolean.parseBoolean(groupsDataFromFile.get(index++));
		goalDifferenceGroupStage = Boolean.parseBoolean(groupsDataFromFile.get(index++));
		fairplayGroupStage = Boolean.parseBoolean(groupsDataFromFile.get(index++));
		groups = new Gruppe[numberOfGroups];
		for (int i = 0; i < groups.length; i++)	groups[i] = new Gruppe(this, i, false, goalDifferenceGroupStage, fairplayGroupStage);
		{
			overview = new Spieltag(this, false);
			overview.setLocation((Fussball.WIDTH - overview.getSize().width) / 2, (Fussball.HEIGHT - 28 - overview.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
			overview.setVisible(false);
		}
	}
	
	public void saveGroups() {
		if (!hasGroupStage)	return;
		
		for (Gruppe group : groups)	group.save();
		
		groupsDataFromFile.clear();
		groupsDataFromFile.add("" + numberOfGroups);
		groupsDataFromFile.add("" + hasSecondLegGroupStage);
		groupsDataFromFile.add("" + goalDifferenceGroupStage);
		groupsDataFromFile.add("" + fairplayGroupStage);
		
		writeFile(fileGroupsData, groupsDataFromFile);
	}
	
	public void loadKORounds() {
		if (!hasKOStage)	return;
		koRoundsDataFromFile = readFile(fileKORoundsData);
		numberOfKORounds = koRoundsDataFromFile.size();
		
		koRounds = new KORunde[numberOfKORounds];
		for (int i = 0; i < koRounds.length; i++)	koRounds[i] = new KORunde(this, i, false, koRoundsDataFromFile.get(i));
	}
	
	public void saveKORounds() {
		if (!hasKOStage)	return;
		
		koRoundsDataFromFile.clear();
		for (int i = 0; i < koRounds.length; i++) {
			koRounds[i].save();
			koRoundsDataFromFile.add(koRounds[i].toString());
		}
		
		writeFile(fileKORoundsData, koRoundsDataFromFile);
	}
	
	private void saveNextMatches() {
		ArrayList<ArrayList<Long>> allNextMatches = new ArrayList<>();
		ArrayList<Long> nextMatches = new ArrayList<>();
		for (int i = 0; i < numberOfQGroups; i++) {
			allNextMatches.add(qGroups[i].getNextMatches());
		}
		for (int i = 0; i < numberOfQKORounds; i++) {
			allNextMatches.add(qKORounds[i].getNextMatches());
		}
		for (int i = 0; i < numberOfGroups; i++) {
			allNextMatches.add(groups[i].getNextMatches());
		}
		for (int i = 0; i < numberOfKORounds; i++) {
			allNextMatches.add(koRounds[i].getNextMatches());
		}
		for (int i = 0; i < allNextMatches.size(); i++) {
			ArrayList<Long> list = allNextMatches.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (nextMatches.size() >= Fussball.numberOfMissingResults && list.get(j) > nextMatches.get(Fussball.numberOfMissingResults - 1))	break;
				int index = nextMatches.size();
				for (int k = 0; k < nextMatches.size() && index == nextMatches.size(); k++) {
					if (list.get(j) < nextMatches.get(k))	index = k;
				}
				nextMatches.add(index, list.get(j));
			}
		}
		
		String fileName = workspace + "nextMatches.txt";
		if (nextMatches.size() > 0) {
			ArrayList<String> nextMatchesString = new ArrayList<>();
			for (int i = 0; i < Fussball.numberOfMissingResults && i < nextMatches.size(); i++) {
				nextMatchesString.add("" + nextMatches.get(i));
			}
			writeFile(fileName, nextMatchesString);
		} else {
			new File(fileName).delete();
		}
	}
	
	private void saveRanks() {
		ArrayList<String> allRanks = new ArrayList<>();
		
		if (hasQGroupStage) {
			for (Gruppe group : qGroups) {
				String[] ranks = group.getRanks();
				for (int i = 0; i < ranks.length; i++) {
					allRanks.add(ranks[i]);
				}
			}
		}
		if (hasQKOStage) {
			for (KORunde koRound : qKORounds) {
				String[] ranks = koRound.getRanks();
				for (int i = 0; i < ranks.length; i++) {
					allRanks.add(ranks[i]);
				}
			}
		}
		if (hasGroupStage) {
			for (Gruppe group : groups) {
				String[] ranks = group.getRanks();
				for (int i = 0; i < ranks.length; i++) {
					allRanks.add(ranks[i]);
				}
			}
		}
		if (hasKOStage) {
			for (KORunde koRound : koRounds) {
				String[] ranks = koRound.getRanks();
				for (int i = 0; i < ranks.length; i++) {
					allRanks.add(ranks[i]);
				}
			}
		}
		
		writeFile(workspace + "allRanks.txt", allRanks);
	}
	
	public void load() {
		fileQualificationData = workspace + "QualiConfig.txt";
		fileGroupsData = workspace + "GruppenConfig.txt";
		fileKORoundsData = workspace + "KOconfig.txt";
		fileReferees = workspace + "Schiedsrichter.txt";
		
		loadReferees();
		loadQualification();
		loadGroups();
		loadKORounds();
		geladen = true;
		
		boolean debug = false;
		if (debug)	testGetGroupStageOriginsOfTeams();
	}
	
	public void save() {
		if (!geladen)	return;
		saveNextMatches();
		saveRanks();
		
		saveQualification();
		saveGroups();
		saveKORounds();
		saveReferees();
		geladen = false;
	}
	
	public String toString() {
		String toString = "";
		
		toString += year + ";";
		toString += isSummerToSpringSeason + ";";
		toString += startDate.comparable() + ";";
		toString += finalDate.comparable() + ";";
		toString += hasQualification + ";";
		toString += hasGroupStage + ";";
		toString += hasKOStage + ";";
		toString += matchForThirdPlace + ";";
		toString += teamsHaveKader + ";";
		toString += isFourthSubPossible + ";";
		
		if (!numberOfSubstitutions.isEmpty()) {
			ArrayList<String> subs = new ArrayList<>();
			Set<Dauer> durations = numberOfSubstitutions.keySet();
			for (Dauer duration : durations) {
				addAscending(subs, duration.toString() + ":" + numberOfSubstitutions.get(duration));
			}
			String addData = "", sep = "";
			for (int i = 0; i < subs.size(); i++) {
				addData += sep + subs.get(i);
				sep = ",";
			}
			toString += addData;
		}
		
		return toString;
	}
	
	public void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		year = Integer.parseInt(split[index++]);
		isSummerToSpringSeason = Boolean.parseBoolean(split[index++]);
		startDate = new Datum(split[index++]);
		finalDate = new Datum(split[index++]);
		hasQualification = Boolean.parseBoolean(split[index++]);
		hasGroupStage = Boolean.parseBoolean(split[index++]);
		hasKOStage = Boolean.parseBoolean(split[index++]);
		matchForThirdPlace = Boolean.parseBoolean(split[index++]);
		teamsHaveKader = Boolean.parseBoolean(split[index++]);
		isFourthSubPossible = Boolean.parseBoolean(split[index++]);
		
		numberOfSubstitutions = new HashMap<>();
		if (split.length > index) {
			String[] addData = split[index].split(",");
			for (int i = 0; i < addData.length; i++) {
				String[] addDataSplit = addData[i].split(":");
				numberOfSubstitutions.put(new Dauer(addDataSplit[0]), Integer.parseInt(addDataSplit[1]));
			}
		}
	}
}
