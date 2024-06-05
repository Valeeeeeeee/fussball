package dto.statistics;

import java.util.ArrayList;

import model.Mannschaft;

public class TeamSaisonStatistikBegegnungenDTO {
	
	private Mannschaft opponent;
	
	private ArrayList<TeamSaisonStatistikSpielDTO> matches;
	
	private TeamSaisonStatistikBegegnungenDTO() {
		
	}
	
	public String getNameOfOpponent() {
		return opponent.getName();
	}
	
	public ArrayList<TeamSaisonStatistikSpielDTO> getMatches() {
		return matches;
	}
	
	public static TeamSaisonStatistikBegegnungenDTO of(Mannschaft team, Mannschaft opponent, ArrayList<TeamSaisonStatistikSpielDTO> matches) {
		TeamSaisonStatistikBegegnungenDTO matchup = new TeamSaisonStatistikBegegnungenDTO();
		
		matchup.opponent = opponent;
		matchup.matches = matches;
		
		return matchup;
	}
}
