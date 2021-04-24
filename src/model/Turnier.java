package model;

import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class Turnier {
	private int id;
	private String name;
	private String shortName;
	private boolean isClubCompetition;
	
	private int currentSeasonIndex;
	private ArrayList<TurnierSaison> seasons;
	
	private String workspace;
	
	private String fileSeasonsData;
	private ArrayList<String> seasonsDataFromFile;
	
	
	public Turnier(int id, String data) {
		this.id = id;
		fromString(data);
		loadSeasons();
	}
	
	public int getID() {
		return id;
	}
	
	public String getWorkspace(int season) {
		int seasonIndex = 0;
		for (seasonIndex = 0; seasonIndex < seasons.size(); seasonIndex++) {
			if (seasons.get(seasonIndex).getYear() == season)	break;
		}
		return workspace + seasons.get(seasonIndex).getSeasonFull("_") + File.separator;
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public boolean isClubCompetition() {
		return isClubCompetition;
	}
	
	/**
	 * This method returns all available seasons in the format "2014/2015" if isSTSS, otherwise "2014".
	 * @return a String array containing all available seasons
	 */
	public String[] getAllSeasons() {
		String[] allSeasons = new String[seasons.size()];
		for (int i = 0; i < seasons.size(); i++) {
			allSeasons[i] = seasons.get(i).getSeasonFull("/");
		}
		return allSeasons;
	}
	
	public int getCurrentSeasonYear() {
		return seasons.get(currentSeasonIndex).getYear();
	}
	
	public TurnierSaison getCurrentSeason() {
		return seasons.get(currentSeasonIndex);
	}
	
	public boolean addNewSeason(String toString, ArrayList<String> qConfig, String[][] teamsQG, String[][] teamsQKO, ArrayList<String> grpConfig, String[][] teamsGrp, ArrayList<String> koConfig, String[][] teamsKO) {
		TurnierSaison newSeason = new TurnierSaison(this, seasons.size(), toString);
		for (int i = 0; i < seasons.size(); i++) {
			if (seasons.get(i).getYear() == newSeason.getYear()) {
				message("Eine Saison mit diesem Startjahr existiert bereits.");
				return false;
			}
		}
		seasons.add(newSeason);
		
		String folder = workspace + newSeason.getSeasonFull("_") + File.separator;
		(new File(folder)).mkdirs();
		
		if (newSeason.hasQualification()) {
			writeFile(folder + "QualiConfig.txt", qConfig);
			String qFolder = folder + "Qualifikation" + File.separator;
			(new File(qFolder)).mkdirs();
			// remove startDate and finalDate
			qConfig.remove(0);
			qConfig.remove(0);
			
			// Gruppen
			int nOfQGrps = Integer.parseInt(qConfig.remove(0));
			int nOfMASO = 0;
			if (nOfQGrps > 0)	nOfMASO = Boolean.parseBoolean(qConfig.remove(0)) ? 2 : 1;
			for (int i = 0; i < nOfQGrps; i++) {
				String grpFolder = qFolder + "Gruppe " + alphabet[i] + File.separator;
				(new File(grpFolder)).mkdirs();
				
				ArrayList<String> teams = new ArrayList<>();
				ArrayList<String> matches = new ArrayList<>();
				ArrayList<String> results = new ArrayList<>();
				
				int nOfTeams = teamsQG[i].length;
				int nOfMtchs = nOfTeams / 2;
				int nOfMtdys = (2 * ((nOfTeams + 1) / 2) - 1) * nOfMASO;
				String allF = "";
				for (int j = 0; j < nOfMtchs; j++) {
					allF += "f";
				}
				for (int j = 0; j < nOfMtdys; j++) {
					matches.add(allF + ";");
					results.add(allF + ";");
				}
				for (int j = 0; j < nOfTeams; j++) {
					teams.add(teamsQG[i][j]);
				}
				writeFile(grpFolder + "Mannschaften.txt", teams);
				writeFile(grpFolder + "Spielplan.txt", matches);
				writeFile(grpFolder + "Ergebnisse.txt", results);
			}
			
			// KO-Runden
			int nOfQKORds = Integer.parseInt(qConfig.remove(0));
			for (int i = 0; i < nOfQKORds; i++) {
				String config = qConfig.remove(0);
				String[] split = config.split(";");
				
				String koFolder = qFolder + split[0] + File.separator;
				(new File(koFolder)).mkdirs();
				
				ArrayList<String> teams = new ArrayList<>();
				ArrayList<String> matches = new ArrayList<>();
				ArrayList<String> results = new ArrayList<>();
				
				int nOfTeams = teamsQKO[i].length;
				int nOfMtchs = nOfTeams / 2;
				int nOfMtdys = Boolean.parseBoolean(split[3]) ? 2 : 1;
				String allF = "";
				for (int j = 0; j < nOfMtchs; j++) {
					allF += "f";
				}
				for (int j = 0; j < nOfMtdys; j++) {
					matches.add(allF + ";");
					results.add(allF + ";");
				}
				for (int j = 0; j < nOfTeams; j++) {
					teams.add(teamsQKO[i][j]);
				}
				
				writeFile(koFolder + "Mannschaften.txt", teams);
				writeFile(koFolder + "Spielplan.txt", matches);
				writeFile(koFolder + "Ergebnisse.txt", results);
			}
		}
		if (newSeason.hasGroupStage()) {
			writeFile(folder + "GruppenConfig.txt", grpConfig);
			
			int nOfGrps = Integer.parseInt(grpConfig.remove(0));
			int nOfMASO = Boolean.parseBoolean(grpConfig.remove(0)) ? 2 : 1;
			for (int i = 0; i < nOfGrps; i++) {
				String grpFolder = folder + "Gruppe " + alphabet[i] + File.separator;
				(new File(grpFolder)).mkdirs();
				
				ArrayList<String> teams = new ArrayList<>();
				ArrayList<String> matches = new ArrayList<>();
				ArrayList<String> results = new ArrayList<>();
				
				int nOfTeams = teamsGrp[i].length;
				int nOfMtchs = nOfTeams / 2;
				int nOfMtdys = (2 * ((nOfTeams + 1) / 2) - 1) * nOfMASO;
				String allF = "";
				for (int j = 0; j < nOfMtchs; j++) {
					allF += "f";
				}
				for (int j = 0; j < nOfMtdys; j++) {
					matches.add(allF + ";");
					results.add(allF + ";");
				}
				for (int j = 0; j < nOfTeams; j++) {
					teams.add(teamsGrp[i][j]);
				}
				writeFile(grpFolder + "Mannschaften.txt", teams);
				writeFile(grpFolder + "Spielplan.txt", matches);
				writeFile(grpFolder + "Ergebnisse.txt", results);
			}
		}
		if (newSeason.hasKOStage()) {
			writeFile(folder + "KOconfig.txt", koConfig);
			
			int nOfKORds = koConfig.size();
			for (int i = 0; i < nOfKORds; i++) {
				String config = koConfig.remove(0);
				String[] split = config.split(";");
				
				String koFolder = folder + split[0] + File.separator;
				(new File(koFolder)).mkdirs();
				
				ArrayList<String> teams = new ArrayList<>();
				ArrayList<String> matches = new ArrayList<>();
				ArrayList<String> results = new ArrayList<>();
				
				int nOfTeams = teamsKO[i].length;
				int nOfMtchs = nOfTeams / 2;
				int nOfMtdys = Boolean.parseBoolean(split[3]) ? 2 : 1;
				String allF = "";
				for (int j = 0; j < nOfMtchs; j++) {
					allF += "f";
				}
				for (int j = 0; j < nOfMtdys; j++) {
					matches.add(allF + ";");
					results.add(allF + ";");
				}
				for (int j = 0; j < nOfTeams; j++) {
					teams.add(teamsKO[i][j]);
				}
				
				writeFile(koFolder + "Mannschaften.txt", teams);
				writeFile(koFolder + "Spielplan.txt", matches);
				writeFile(koFolder + "Ergebnisse.txt", results);
			}
		}
		
		return true;
	}
	
	private void loadSeasons() {
		workspace = Fussball.getInstance().getWorkspace() + File.separator + name + File.separator;
		
		fileSeasonsData = workspace + "SaisonsConfig.txt";
		seasonsDataFromFile = readFile(fileSeasonsData);
		
		seasons = new ArrayList<>();
		for (int i = 0; i < seasonsDataFromFile.size(); i++) {
			seasons.add(new TurnierSaison(this, i, seasonsDataFromFile.get(i)));
		}
	}
	
	private void saveSeasons() {
		seasonsDataFromFile.clear();
		for (int i = 0; i < seasons.size(); i++) {
			seasons.get(i).save();
			seasonsDataFromFile.add(seasons.get(i).toString());
		}
		
		writeFile(fileSeasonsData, seasonsDataFromFile);
	}
	
	public int[] checkMissingResults() {
		Datum today = new Datum();
		int countNotScheduled = 0, countCompleted = 0, countStillRunning = 0;
		for (TurnierSaison season : seasons) {
			String fileName = season.getWorkspace() + "nextMatches.txt";
			ArrayList<String> nextMatchesString = readFile(fileName, false);
			if (nextMatchesString.size() > 0) {
				long now = 10000L * today.comparable() + new Uhrzeit().comparable();
				for (int i = 0; i < nextMatchesString.size(); i++) {
					long match = Long.parseLong(nextMatchesString.get(i));
					if (match % 10000 == 9999) {
						countNotScheduled++;
					} else if (match < now) {
						boolean hourPassed = match % 100 >= now % 100;
						int dayDiff = new Datum((int) match / 10000).daysUntil(new Datum((int) now / 10000));
						long diff = (now % 10000) - (match % 10000) + dayDiff * 2400 - (hourPassed ? 40 : 0);
						diff = (diff / 100) * 60 + diff % 100;
						if (diff < 105)	countStillRunning++;
						else			countCompleted++;
					}
				}
			}
		}
		
		return new int[] {countNotScheduled, countCompleted, countStillRunning};
	}
	
	public void load(int index) {
		currentSeasonIndex = index;
		seasons.get(currentSeasonIndex).load();
	}
	
	public void save() {
		saveSeasons();
	}
	
	private void fromString(String data) {
		String[] split = data.split(";");
		int index = 0;
		
		name = split[index++];
		shortName = split[index++];
		isClubCompetition = Boolean.parseBoolean(split[index++]);
	}
	
	public String toString() {
		String toString = "";
		
		toString += name + ";";
		toString += shortName + ";";
		toString += isClubCompetition + ";";
		
		return toString;
	}
}
