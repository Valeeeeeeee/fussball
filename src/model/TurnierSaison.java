package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import dto.fixtures.SpielplanHauptKategorieDTO;
import dto.fixtures.SpielplanUnterKategorieDTO;
import dto.fixtures.SpielplanZeileDTO;
import model.tournament.KOOrigin;
import model.tournament.KOOriginOtherCompetition;
import model.tournament.KOOriginPrequalified;
import model.tournament.KOOriginPreviousGroupStage;
import model.tournament.KOOriginPreviousKnockoutRound;
import model.tournament.KOOriginTwoOrigins;
import model.tournament.KOOriginType;

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
	private int untilRankBestQGroupXths = 5;
	
	private Spieltag qOverview;
	private Gruppe[] qGroups;
	private KORunde[] qKORounds;
	
	private int numberOfQGroups;
	private boolean isRoundRobinQGroupStage;
	private int numberOfLegsRRQGroupStage;
	private int numberOfMatchdaysSSQGroupStage;
	private int numberOfQKORounds;
	
	private int numberOfGroups;
	private boolean isRoundRobinGroupStage;
	private int numberOfLegsRRGroupStage;
	private int numberOfMatchdaysSSGroupStage;
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
		AnstossZeit[] kickOffTimes = new AnstossZeit[numberOfMatches];
		
		int counter = 0;
		for (Gruppe group : isQualification ? qGroups : groups) {
			for (int matchIndex = 0; matchIndex < group.getNumberOfMatchesPerMatchday(); matchIndex++) {
				kickOffTimes[counter] = group.getKickOffTime(matchday, matchIndex);
				counter++;
			}
		}
		
		for (int m = 0; m < numberOfMatches; m++) {
			for (int m2 = m + 1; m2 < numberOfMatches; m2++) {
				if (kickOffTimes[m].isAfter(kickOffTimes[m2]))	hilfsarray[m]++;
				else											hilfsarray[m2]++;
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
			if (numberOfMatchdays < group.getNumberOfRegularMatchdays())	numberOfMatchdays = group.getNumberOfRegularMatchdays();
		}
		
		String[] matchdays = new String[numberOfMatchdays];
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdays[i] = (i + 1) + ". Spieltag";
		}
		return matchdays;
	}
	
	public ArrayList<SpielplanZeileDTO> getAllMatches(Mannschaft team, boolean chronologicalOrder) {
		ArrayList<SpielplanZeileDTO> allMatches = new ArrayList<>();
		
		for (int i = 0; i < numberOfQGroups; i++) {
			ArrayList<SpielplanZeileDTO> matches = qGroups[i].getMatches(team, chronologicalOrder);
			if (matches.size() > 0) {
				allMatches.add(SpielplanUnterKategorieDTO.of("Gruppenphase"));
				allMatches.addAll(matches);
				break;
			}
		}
		for (int i = 0; i < numberOfQKORounds; i++) {
			ArrayList<SpielplanZeileDTO> matches = qKORounds[i].getMatches(team, chronologicalOrder);
			if (matches.size() > 0) {
				allMatches.add(SpielplanUnterKategorieDTO.of(qKORounds[i].getDescription()));
				allMatches.addAll(matches);
			}
		}
		if (allMatches.size() > 0)	allMatches.add(0, SpielplanHauptKategorieDTO.of("Qualifikation"));
		for (int i = 0; i < numberOfGroups; i++) {
			ArrayList<SpielplanZeileDTO> matches = groups[i].getMatches(team, chronologicalOrder);
			if (matches.size() > 0) {
				allMatches.add(SpielplanHauptKategorieDTO.of("Gruppenphase"));
				allMatches.addAll(matches);
				break;
			}
		}
		for (int i = 0; i < numberOfKORounds; i++) {
			ArrayList<SpielplanZeileDTO> matches = koRounds[i].getMatches(team, chronologicalOrder);
			if (matches.size() > 0) {
				allMatches.add(SpielplanHauptKategorieDTO.of(koRounds[i].getDescription()));
				allMatches.addAll(matches);
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
	
	public Mannschaft getTeamFromOtherCompetition(int id, Wettbewerb competition, KOOrigin origin) {
		Mannschaft team = null;
		
		if (teamsFromOtherCompetition.containsKey(origin.getOrigin())) {
			return teamsFromOtherCompetition.get(origin.getOrigin());
		}
		team = new Mannschaft(id, competition, getNameOfTeamFromOtherCompetition(origin));
		teamsFromOtherCompetition.put(origin.getOrigin(), team);
		
		return team;
	}
	
	private String getNameOfTeamFromOtherCompetition(KOOrigin origin) {
		String teamOrigin = origin.getOrigin();
		String fileName = Fussball.getInstance().getTournamentWorkspaceFromShortName(teamOrigin.substring(0, 4), Integer.parseInt(teamOrigin.substring(4, 8)));
		
		ArrayList<String> teams = readFile(fileName + "allRanks.txt");
		for (String team : teams) {
			if (teamOrigin.substring(8).equals(team.split(": ")[0])) {
				return team.split(": ")[1];
			}
		}
		
		return teamOrigin;
	}
	
	public Optional<Mannschaft> getTeamFromOrigin(KOOrigin origin) {
		if (origin == null)	return Optional.empty();
		
		switch (origin.getKOOriginType()) {
			case PREQUALIFIED:
				KOOriginPrequalified pqOrigin = (KOOriginPrequalified) origin;
				KORunde knockoutRound = pqOrigin.getKnockoutRound();
				int teamIndex = pqOrigin.getTeamIndex();
				return knockoutRound.getInvariantTeam(teamIndex);
			case OTHER_COMPETITION:
				KOOriginOtherCompetition ocOrigin = (KOOriginOtherCompetition) origin;
				knockoutRound = ocOrigin.getKnockoutRound();
				teamIndex = ocOrigin.getTeamIndex();
				return knockoutRound.getInvariantTeam(teamIndex);
			case PREVIOUS_SWISS_SYSTEM_GROUP_STAGE:
			case PREVIOUS_ROUND_ROBIN_GROUP_STAGE:
				KOOriginPreviousGroupStage pgsOrigin = (KOOriginPreviousGroupStage) origin;
				boolean isQ = pgsOrigin.isQualification();
				String group = pgsOrigin.getPreviousGroupStageIndex();
				int placeIndex = pgsOrigin.getPlaceIndex();
				if (pgsOrigin.isRoundRobinGroup()) {
					if (pgsOrigin.isGroupXth())	return getTeamFromRoundRobinGroupXthOrigin(isQ, Integer.parseInt(group), placeIndex);
					else						return getTeamFromRoundRobinGroupStageOrigin(isQ, group.charAt(0), placeIndex);
				} else {
					return getTeamFromSwissSystemGroupStageOrigin(isQ, placeIndex);
				}
			case PREVIOUS_KNOCKOUT_ROUND:
				KOOriginPreviousKnockoutRound pkoOrigin = (KOOriginPreviousKnockoutRound) origin;
				isQ = pkoOrigin.isQualification();
				String shortNameKORound = pkoOrigin.getPreviousKnockoutRound();
				int matchIndex = pkoOrigin.getMatchIndex();
				boolean teamIsWinner = pkoOrigin.teamIsWinner();
				return getDeeperOrigin(isQ, shortNameKORound, matchIndex, teamIsWinner).map(this::getTeamFromOrigin).orElse(Optional.empty());
			case TWO_ORIGINS:
				KOOriginTwoOrigins twoOrigins = (KOOriginTwoOrigins) origin;
				KOOrigin originFirstTeam = twoOrigins.getOriginFirstTeam();
				KOOrigin originSecondTeam = twoOrigins.getOriginSecondTeam();
				
				String o1 = getTeamFromOrigin(originFirstTeam).map(Mannschaft::getName).filter(o -> !o.contains(VERSUS)).orElse(originFirstTeam.toDisplay());
				String o2 = getTeamFromOrigin(originSecondTeam).map(Mannschaft::getName).filter(o -> !o.contains(VERSUS)).orElse(originSecondTeam.toDisplay());
				
				if (isEqualButNotPrequalified(o1, originFirstTeam) && isEqualButNotPrequalified(o2, originSecondTeam)) {
					return Optional.empty();
				}
				return Optional.of(new Mannschaft(0, null, o1 + VERSUS + o2));
			default:
				break;
		}
		
		return Optional.empty();
	}
	
	private static boolean isEqualButNotPrequalified(String origin, KOOrigin koOrigin) {
		return origin.equals(koOrigin.toDisplay()) && koOrigin.getKOOriginType() != KOOriginType.PREQUALIFIED;
	}
	
	private Optional<Mannschaft> getTeamFromRoundRobinGroupXthOrigin(boolean isQ, int placeInGroup, int xthBest) {
		int nOfGroups = isQ ? numberOfQGroups : numberOfGroups;
		Gruppe[] grps = isQ ? qGroups : groups;
		int untilRank = isQ ? untilRankBestQGroupXths : Integer.MAX_VALUE;
		ArrayList<Optional<Mannschaft>> groupXth = new ArrayList<>();
		for (int i = 0; i < nOfGroups; i++) {
			groupXth.add(getTeamFromRoundRobinGroupStageOrigin(isQ, alphabet[i], placeInGroup));
			untilRank = Math.min(untilRank, grps[i].getNumberOfTeams());
		}
		
		int[] points = new int[nOfGroups], goalDiff = new int[nOfGroups], goalsFor = new int[nOfGroups];
		for (int i = 0; i < nOfGroups; i++) {
			if (!groupXth.get(i).isPresent())	continue;
			points[i] = groupXth.get(i).get().get(9, 0, grps[i].getNumberOfRegularMatchdays() - 1, untilRank);
			goalDiff[i] = groupXth.get(i).get().get(8, 0, grps[i].getNumberOfRegularMatchdays() - 1, untilRank);
			goalsFor[i] = groupXth.get(i).get().get(6, 0, grps[i].getNumberOfRegularMatchdays() - 1, untilRank);
		}
		
		ArrayList<Integer> order = getOrder(groupXth, points, goalDiff, goalsFor);
		
		for (int i = 0; i < order.size(); i++) {
			if (order.get(i) == xthBest) {
				return groupXth.get(i);
			}
		}
		
		return Optional.empty();
	}
	
	private ArrayList<Integer> getOrder(ArrayList<Optional<Mannschaft>> groupXth, int[] points, int[] goalDiff, int[] goalsFor) {
		ArrayList<Integer> order = new ArrayList<>();
		
		int nOfGroups = groupXth.size();
		
		for (int i = 0; i < nOfGroups; i++) {
			order.add(1);
		}
		
		for (int i = 0; i < nOfGroups - 1; i++) {
			for (int j = i + 1; j < nOfGroups; j++) {
				if (groupXth.get(i).isPresent() && groupXth.get(j).isPresent()) {
					if (points[i] < points[j])		order.set(i, order.get(i) + 1);
					else if (points[j] < points[i])	order.set(j, order.get(j) + 1);
					else {
						if (goalDiff[i] < goalDiff[j])		order.set(i, order.get(i) + 1);
						else if (goalDiff[j] < goalDiff[i])	order.set(j, order.get(j) + 1);
						else {
							if (goalsFor[i] < goalsFor[j])		order.set(i, order.get(i) + 1);
							else if (goalsFor[j] < goalsFor[i])	order.set(j, order.get(j) + 1);
							else {
								// TODO implement tie-breaker
								order.set(j, order.get(j) + 1);
							}
						}
					}
				} else if (groupXth.get(i).isPresent()) {
					order.set(j, order.get(j) + 1);
				} else if (groupXth.get(j).isPresent()) {
					order.set(i, order.get(i) + 1);
				}
			}
		}
		
		return order;
	}
	
	private Optional<Mannschaft> getTeamFromSwissSystemGroupStageOrigin(boolean isQ, int place) {
		return getSwissSystemGroup(isQ).map(g -> g.getTeamOnPlace(place).orElse(null));
	}
	
	private Optional<Mannschaft> getTeamFromRoundRobinGroupStageOrigin(boolean isQ, char group, int place) {
		return getRoundRobinGroup(isQ, group).map(g -> g.getTeamOnPlace(place).orElse(null));
	}
	
	private Optional<Gruppe> getSwissSystemGroup(boolean isQ) {
		return Stream.of(isQ ? qGroups : groups).findAny();
	}
	
	private Optional<Gruppe> getRoundRobinGroup(boolean isQ, char group) {
		return Stream.of(isQ ? qGroups : groups).filter(g -> group == alphabet[g.getID()]).findAny();
	}
	
	private Optional<KOOrigin> getDeeperOrigin(boolean isQ, String shortName, int matchIndex, boolean teamIsWinner) {
		Optional<KORunde> koRound = getKORound(isQ, shortName);
		if (teamIsWinner) {
			Optional<KOOrigin> opt = koRound.map(k -> k.getOriginOfWinnerOfTie(matchIndex).orElse(null));
			if (opt.isPresent())	return opt;
			return koRound.map(k -> k.getOriginsOfTie(matchIndex).orElse(null));
		} else {
			Optional<KOOrigin> opt = koRound.map(k -> k.getOriginOfLoserOfTie(matchIndex).orElse(null));
			if (opt.isPresent())	return opt;
			return koRound.map(k -> k.getOriginsOfTie(matchIndex).orElse(null));
		}
	}
	
	private Optional<KORunde> getKORound(boolean isQ, String shortName) {
		return Stream.of(isQ ? qKORounds : koRounds).filter(k -> shortName.equals(k.getShortName())).findAny();
	}
	
	private void testGetGroupStageOriginsOfTeams() {
		log("\n\n");
		for (int i = 0; i < koRounds.length; i++) {
			log(koRounds[i].getDescription() + "\n-------------");
			koRounds[i].setCheckTeamsFromPreviousRound(false);
			ArrayList<Mannschaft> teams = koRounds[i].getTeams();
			koRounds[i].setCheckTeamsFromPreviousRound(true);
			for (int j = 0; j < teams.size(); j++) {
				Mannschaft team = teams.get(j);
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
			
			qGroups = new Gruppe[numberOfQGroups];
			isRoundRobinQGroupStage = Boolean.parseBoolean(qualificationDataFromFile.get(index++));
			if (isRoundRobinQGroupStage) {
				numberOfLegsRRQGroupStage = Integer.parseInt(qualificationDataFromFile.get(index++));
				untilRankBestQGroupXths = Integer.parseInt(qualificationDataFromFile.get(index++));
				for (int i = 0; i < numberOfQGroups; i++)	qGroups[i] = new RoundRobinGruppe(this, i, true, numberOfLegsRRQGroupStage);
			} else {
				numberOfMatchdaysSSQGroupStage = Integer.parseInt(qualificationDataFromFile.get(index++));
				groups[0] = new SchweizerSystemGruppe(this, 0, true, numberOfMatchdaysSSQGroupStage);
			}
			if (numberOfQGroups > 1) {
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
			qualificationDataFromFile.add("" + isRoundRobinQGroupStage);
			if (isRoundRobinQGroupStage) {
				qualificationDataFromFile.add("" + numberOfLegsRRQGroupStage);
				qualificationDataFromFile.add("" + untilRankBestQGroupXths);
			} else {
				qualificationDataFromFile.add("" + numberOfMatchdaysSSQGroupStage);
			}
			
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
		groups = new Gruppe[numberOfGroups];
		isRoundRobinGroupStage = Boolean.parseBoolean(groupsDataFromFile.get(index++));
		if (isRoundRobinGroupStage) {
			numberOfLegsRRGroupStage = Integer.parseInt(groupsDataFromFile.get(index++));
			for (int i = 0; i < groups.length; i++)	groups[i] = new RoundRobinGruppe(this, i, false, numberOfLegsRRGroupStage);
		} else {
			numberOfMatchdaysSSGroupStage = Integer.parseInt(groupsDataFromFile.get(index++));
			groups[0] = new SchweizerSystemGruppe(this, 0, false, numberOfMatchdaysSSGroupStage);
		}
		if (numberOfGroups > 1) {
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
		groupsDataFromFile.add("" + isRoundRobinGroupStage);
		if (isRoundRobinGroupStage) {
			groupsDataFromFile.add("" + numberOfLegsRRGroupStage);
		} else {
			groupsDataFromFile.add("" + numberOfMatchdaysSSGroupStage);
		}
		
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
		ArrayList<ArrayList<AnstossZeit>> allNextMatches = new ArrayList<>();
		ArrayList<AnstossZeit> nextMatches = new ArrayList<>();
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
			ArrayList<AnstossZeit> list = allNextMatches.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (nextMatches.size() >= Fussball.numberOfMissingResults && !list.get(j).isBefore(nextMatches.get(Fussball.numberOfMissingResults - 1)))	break;
				int index = nextMatches.size();
				for (int k = 0; k < nextMatches.size() && index == nextMatches.size(); k++) {
					if (list.get(j).isBefore(nextMatches.get(k)))	index = k;
				}
				nextMatches.add(index, list.get(j));
			}
		}
		
		String fileName = workspace + "nextMatches.txt";
		if (nextMatches.size() > 0) {
			ArrayList<String> nextMatchesString = new ArrayList<>();
			for (int i = 0; i < Fussball.numberOfMissingResults && i < nextMatches.size(); i++) {
				nextMatchesString.add("" + nextMatches.get(i).comparable());
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
