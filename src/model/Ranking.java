package model;

import java.util.ArrayList;

public class Ranking {
	
	private Wettbewerb competition;
	
	private ArrayList<Mannschaft> teams;
	
	private ArrayList<RankingCriterion> rankingCriteria;
	
	private int untilMatchday;
	
	private Tabellenart tableType;
	
	private SubRanking subRanking;
	
	public Ranking(Wettbewerb competition, int untilMatchday, Tabellenart tableType) {
		this.competition = competition;
		teams = competition.getTeams();
		rankingCriteria = competition.getRankingCriteria();
		this.untilMatchday = untilMatchday;
		this.tableType = tableType;
		
		subRanking = new SubRanking(0, teams, untilMatchday, tableType);
		for (Mannschaft ms : teams) {
			subRanking.addIdAtValue(ms.getId());
		}
	}
	
	public void applyCriteria() {
		boolean competitionIsOver = untilMatchday + 1 == competition.getNumberOfRegularMatchdays() && competition.allResultsSet();
		
		for (RankingCriterion rankingCriterion : rankingCriteria) {
			if (!rankingCriterion.evaluateBeforeEnd() && !competitionIsOver)	continue;
			
			subRanking.discriminate(rankingCriterion, null);
		}
		
		subRanking.setPlaces(new RankingPosition());
		
		for (Mannschaft ms : teams) {
			ms.getValueForCriterion(teams, untilMatchday, tableType, rankingCriteria.get(0));
		}
	}
	
	public ArrayList<Mannschaft> getTeamsInRankingOrder() {
		return subRanking.getTeamsInRankingOrder();
	}
}
