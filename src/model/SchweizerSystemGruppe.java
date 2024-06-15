package model;

import static util.Utilities.alphabet;

public class SchweizerSystemGruppe extends Gruppe {

	private int fixedNumberOfMatchdays;
	
	public SchweizerSystemGruppe(TurnierSaison season, int id, boolean isQ, int numberOfMatchdaysSSGroupStage) {
		super(season, id, isQ);
		name = "Gruppenphase";
		numberOfMatchesAgainstSameOpponent = 0;
		
		fixedNumberOfMatchdays = numberOfMatchdaysSSGroupStage;
		
		load();
	}
	
	protected void determineNumberOfMatchdays() {
		numberOfMatchdays = fixedNumberOfMatchdays;
	}
	
	protected String getRankId(int rank) {
		return String.format("%sP%d", isQ ? "Q" :  "", rank);
	}
}
