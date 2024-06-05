package dto.statistics;

import java.awt.Color;
import java.util.Optional;

import model.Ergebnis;

public class TeamSaisonStatistikSpielDTO {
	
	private int matchday;
	
	private boolean home;
	
	private Optional<Ergebnis> result;
	
	private TeamSaisonStatistikSpielDTO() {
		
	}
	
	public int getMatchday() {
		return matchday;
	}
	
	public String getResult() {
		return result.map(r -> r.fromPerspective(home)).orElse(matchday == -1 ? "--" : "(" + matchday + ")");
	}
	
	public Color getBackground() {
		return result.map(r -> r.getSubjectiveResult(home).getColor()).orElse(matchday == -1 ? Color.gray : null);
	}
	
	public static TeamSaisonStatistikSpielDTO of(int matchday, boolean home, Ergebnis result) {
		TeamSaisonStatistikSpielDTO match = new TeamSaisonStatistikSpielDTO();
		
		match.matchday = matchday;
		match.home = home;
		match.result = Optional.ofNullable(result);
		
		return match;
	}
}
