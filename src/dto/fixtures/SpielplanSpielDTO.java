package dto.fixtures;

import java.util.Optional;

import model.Ergebnis;
import model.Mannschaft;
import model.Spiel;
import model.SubjektivesErgebnis;
import model.Wettbewerb;

import static util.Utilities.*;

public class SpielplanSpielDTO extends SpielplanZeileDTO {
	
	private Wettbewerb competition;
	private String matchday;
	private String dateAndTime;
	private String homeTeamName;
	private String awayTeamName;
	private Optional<Ergebnis> result;
	
	public Wettbewerb getCompetition() {
		return competition;
	}
	
	public String getMatchday() {
		return matchday;
	}
	
	public String getDateAndTime() {
		return dateAndTime;
	}
	
	public String getHomeTeamName() {
		return homeTeamName;
	}
	
	public String getAwayTeamName() {
		return awayTeamName;
	}
	
	public String getResult() {
		return result.map(r -> r.home() + " : " + r.away()).orElse("- : -");
	}
	
	public String getResultToolTip() {
		return result.filter(r -> r.isFinishedInRegularTime()).map(Ergebnis::toString).orElse("");
	}
	
	public SubjektivesErgebnis getSubjectiveResult(Mannschaft team) {
		return result.map(r -> r.getSubjectiveResult(team.getName().equals(homeTeamName))).orElse(SubjektivesErgebnis.NOT_SET);
	}
	
	public static SpielplanSpielDTO fromMatch(String matchday, String dateAndTime, Spiel match) {
		SpielplanSpielDTO matchDto = new SpielplanSpielDTO();
		
		matchDto.competition = match.getCompetition();
		matchDto.matchday = matchday;
		matchDto.dateAndTime = dateAndTime;
		matchDto.homeTeamName = Optional.ofNullable(match.getHomeTeam()).map(Mannschaft::getName).orElse(NOT_AVAILABLE);
		matchDto.awayTeamName = Optional.ofNullable(match.getAwayTeam()).map(Mannschaft::getName).orElse(NOT_AVAILABLE);
		matchDto.result = Optional.ofNullable(match.getResult());
		
		return matchDto;
	}
	
	public static SpielplanSpielDTO noMatchSet(Wettbewerb competition, String matchday, String dateAndTime) {
		SpielplanSpielDTO matchDto = new SpielplanSpielDTO();
		
		matchDto.competition = competition;
		matchDto.matchday = matchday;
		matchDto.dateAndTime = dateAndTime;
		matchDto.homeTeamName = NOT_AVAILABLE;
		matchDto.awayTeamName = NOT_AVAILABLE;
		matchDto.result = Optional.empty();
		
		return matchDto;
	}

	public static SpielplanSpielDTO noMatch(Wettbewerb competition, String matchday, String dateAndTime) {
		SpielplanSpielDTO matchDto = new SpielplanSpielDTO();
		
		matchDto.competition = competition;
		matchDto.matchday = matchday;
		matchDto.dateAndTime = dateAndTime;
		matchDto.homeTeamName = SPIELFREI;
		matchDto.awayTeamName = SPIELFREI;
		matchDto.result = Optional.empty();
		
		return matchDto;
	}
}
