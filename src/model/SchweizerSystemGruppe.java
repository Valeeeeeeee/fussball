package model;

import static util.Utilities.twoDigit;

public class SchweizerSystemGruppe extends Gruppe {

	private int fixedNumberOfMatchdays;
	
	public SchweizerSystemGruppe(TurnierSaison season, int id, boolean isQ, int numberOfMatchdaysSSGroupStage) {
		super(season, id, isQ);
		name = "Gruppenphase";
		numberOfMatchesAgainstSameOpponent = 0;
		
		fixedNumberOfMatchdays = numberOfMatchdaysSSGroupStage;
		
		shortRankPrefix = String.format("%sP", isQ ? "Q" :  "");
		longRankPrefix = String.format("%s%d%s", season.getTournament().getShortName(), season.getYear(), shortRankPrefix);
		
		load();
	}
	
	protected void determineNumberOfMatchdays() {
		numberOfMatchdays = fixedNumberOfMatchdays;
	}
	
	protected String getShortRankId(int rank) {
		return String.format("%s%s", shortRankPrefix, twoDigit(rank));
	}
	
	protected String getLongRankId(int rank) {
		return String.format("%s%s", longRankPrefix, twoDigit(rank));
	}
}
