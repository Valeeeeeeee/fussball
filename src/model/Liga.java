package model;

import java.io.*;
import java.util.ArrayList;

import static util.Utilities.*;

public class Liga {
	private int id = -1;
	private String name;
	private String shortName;
	private boolean isClubCompetition;
	
	private ArrayList<LigaSaison> seasons;
	private int currentSeasonIndex;
	
	private String workspace;
	
	private String fileSeasonsData;
	private ArrayList<String> seasonsDataFromFile;
	
	
	public Liga(int id, String data) {
		this.id = id;
		fromString(data);
		loadSeasons();
	}
	
	public boolean addSeason(String toString, ArrayList<String> teams, String KOTRepresentation) {
		LigaSaison newSeason = new LigaSaison(this, seasons.size(), toString);
		for (int i = 0; i < seasons.size(); i++) {
			if (seasons.get(i).getYear() == newSeason.getYear()) {
				message("Eine Saison mit diesem Startjahr existiert bereits.");
				return false;
			}
		}
		seasons.add(newSeason);
		
		String folder = workspace + newSeason.getSeasonFull("_") + File.separator;
		(new File(folder)).mkdirs();
		String folderPlayoffs = workspace + newSeason.getSeasonFull("_") + File.separator + "Relegation" + File.separator;
		if (newSeason.hasPlayoffs()) {
			(new File(folderPlayoffs)).mkdirs();
		}
		
		String fileResults = folder + "Ergebnisse.txt";
		String fileMatchData = folder + "Spieldaten.txt";
		String fileMatches = folder + "Spielplan.txt";
		String fileTeams = folder + "Mannschaften.txt";
		ArrayList<String> results = new ArrayList<>();
		ArrayList<String> matchData = new ArrayList<>();
		ArrayList<String> matches = new ArrayList<>();

		int numberOfMatchdays = newSeason.getNumberOfMatchesAgainstSameOpponent() * (2 * ((teams.size() + 1) / 2) - 1);
		int numberOfMatchesPerMatchday = teams.size() / 2;
		
		String allF = "";
		for (int i = 0; i < numberOfMatchesPerMatchday; i++) {
			allF += "f";
		}
		
		matches.add(KOTRepresentation);
		for (int i = 0; i < numberOfMatchdays; i++) {
			results.add(allF);
			matches.add(allF);
			for (int j = 0; j < numberOfMatchesPerMatchday; j++) {
				matchData.add("null");
			}
		}
		
		teams.add(0, "" + teams.size());
		
		inDatei(fileResults, results);
		inDatei(fileMatchData, matchData);
		inDatei(fileMatches, matches);
		inDatei(fileTeams, teams);
		
		return true;
	}
	
	public int[] checkMissingResults() {
		int countNotScheduled = 0, countCompleted = 0, countStillRunning = 0;
		long now = 10000L * today.comparable() + new Uhrzeit().comparable();
		for (LigaSaison season : seasons) {
			String fileName = season.getWorkspace() + "nextMatches.txt";
			ArrayList<String> nextMatchesString = ausDatei(fileName, false);
			if (nextMatchesString.size() > 0) {
				for (int i = 0; i < nextMatchesString.size(); i++) {
					long match = Long.parseLong(nextMatchesString.get(i));
					if (match % 10000 == 9999) {
						countNotScheduled++;
					} else if (match <= now) {
						boolean hourPassed = match % 100 >= now % 100;
						int dayDiff = new Datum((int) (match / 10000)).daysUntil(new Datum((int) (now / 10000)));
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
	
	public String getWorkspace(int season) {
		int seasonIndex = 0;
		for (seasonIndex = 0; seasonIndex < seasons.size(); seasonIndex++) {
			if (seasons.get(seasonIndex).getYear() == season)	break;
		}
		if (seasonIndex >= seasons.size())	return null;
		return workspace + seasons.get(seasonIndex).getSeasonFull("_") + File.separator;
	}

	public String getWorkspace() {
		return workspace;
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
	
	public LigaSaison getCurrentSeason() {
		return seasons.get(currentSeasonIndex);
	}
	
	public int getID() {
		return id;
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
	
	private void loadSeasons() {
		workspace = Fussball.getInstance().getWorkspace() + File.separator + name + File.separator;
		
		fileSeasonsData = workspace + "SaisonsConfig.txt";
		seasonsDataFromFile = ausDatei(fileSeasonsData);
		
		seasons = new ArrayList<>();
		for (int i = 0; i < seasonsDataFromFile.size(); i++) {
			seasons.add(new LigaSaison(this, i, seasonsDataFromFile.get(i)));
		}
	}
	
	private void saveSeasons() {
		seasonsDataFromFile.clear();
		for (int i = 0; i < seasons.size(); i++) {
			seasons.get(i).save();
			seasonsDataFromFile.add(seasons.get(i).toString());
		}
		
		inDatei(fileSeasonsData, seasonsDataFromFile);
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
