package model;

import static util.Utilities.alphabet;;

public class RoundRobinGruppe extends Gruppe {

	public RoundRobinGruppe(TurnierSaison season, int id, boolean isQ, int numberOfMatchesAgainstSameOpponent) {
		super(season, id, isQ);
		name = "Gruppe " + alphabet[id];
		this.numberOfMatchesAgainstSameOpponent = numberOfMatchesAgainstSameOpponent;
		
		load();
	}
	
	protected void determineNumberOfMatchdays() {
		numberOfMatchdays = 2 * ((numberOfTeams + 1) / 2) - 1;
		numberOfMatchdays *= numberOfMatchesAgainstSameOpponent;
	}
	
	protected String getRankId(int rank) {
		return String.format("%sG%c%d", isQ ? "Q" :  "", alphabet[this.id], rank);
	}
}
