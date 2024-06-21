package model;

import static util.Utilities.alphabet;

public class RoundRobinGruppe extends Gruppe {

	public RoundRobinGruppe(TurnierSaison season, int id, boolean isQ, int numberOfMatchesAgainstSameOpponent) {
		super(season, id, isQ);
		name = "Gruppe " + alphabet[id];
		this.numberOfMatchesAgainstSameOpponent = numberOfMatchesAgainstSameOpponent;
		
		shortRankPrefix = String.format("%sG%c", isQ ? "Q" :  "", alphabet[this.id]);
		longRankPrefix = String.format("%s%d%s", season.getTournament().getShortName(), season.getYear(), shortRankPrefix);
		
		load();
	}
	
	protected void determineNumberOfMatchdays() {
		numberOfMatchdays = 2 * ((numberOfTeams + 1) / 2) - 1;
		numberOfMatchdays *= numberOfMatchesAgainstSameOpponent;
	}
	
	protected String getShortRankId(int rank) {
		return String.format("%s%d", shortRankPrefix, rank);
	}
	
	protected String getLongRankId(int rank) {
		return String.format("%s%d", longRankPrefix, rank);
	}
}
