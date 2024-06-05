package dto.statistics;

import java.util.ArrayList;

import model.TeamBilanz;
import model.KORunde;
import model.Mannschaft;
import model.Ranking;
import model.SpielSerien;
import model.Tabellenart;
import model.TeamFairplayBilanz;
import model.Wettbewerb;

public class TeamSaisonStatistikDTO {
	
	private boolean hasStatistics;
	
	private TeamBilanz teamRecord;
	
	private TeamFairplayBilanz teamFairplayRecord;
	
	private ArrayList<TeamSaisonStatistikBegegnungenDTO> matchups;
	
	private SpielSerien longestSeries;
	
	private TeamSaisonStatistikDTO() {
		matchups = new ArrayList<>();
	}
	
	public boolean hasStatistics() {
		return hasStatistics;
	}
	
	public TeamBilanz getTeamRecord() {
		return teamRecord;
	}
	
	public TeamFairplayBilanz getTeamFairplayRecord() {
		return teamFairplayRecord;
	}
	
	public ArrayList<TeamSaisonStatistikBegegnungenDTO> getMatchups() {
		return matchups;
	}
	
	public SpielSerien getLongestSeries() {
		return longestSeries;
	}
	
	public static TeamSaisonStatistikDTO of(Mannschaft team) {
		TeamSaisonStatistikDTO teamSeasonStatisticsDto = new TeamSaisonStatistikDTO();
		
		Wettbewerb competition = team.getCompetition();
		
		teamSeasonStatisticsDto.hasStatistics = !(competition instanceof KORunde);
		if (teamSeasonStatisticsDto.hasStatistics) {
			teamSeasonStatisticsDto.teamRecord = team.getTeamRecord(competition.getCurrentMatchday(), Tabellenart.COMPLETE);
			teamSeasonStatisticsDto.teamFairplayRecord = team.getTeamFairplayRecord();
			teamSeasonStatisticsDto.longestSeries = team.getLongestSeries();
			
			Ranking ranking = new Ranking(competition, competition.getCurrentMatchday(), Tabellenart.COMPLETE);
			ranking.applyCriteria();
			ArrayList<Mannschaft> teamsInRankingOrder = ranking.getTeamsInRankingOrder();
			for (Mannschaft rankedTeam : teamsInRankingOrder) {
				teamSeasonStatisticsDto.matchups.add(team.getAllResults(rankedTeam));
			}
		}
		
		return teamSeasonStatisticsDto;
	}
}
