package model;

import java.util.stream.Stream;

public enum RankingCriterion {
	
	ALL_GAMES_MORE_POINTS(true, true),
	ALL_GAMES_BETTER_GOAL_DIFFERENCE(true, true),
	ALL_GAMES_MORE_GOALS_SCORED(true, true),
	ALL_GAMES_MORE_AWAY_GOALS_SCORED(false, true),
	ALL_GAMES_MORE_WINS(false, true),
	ALL_GAMES_MORE_AWAY_WINS(false, true),
	/* DIRECT COMPARISON */
	DIRECT_COMPARISON_MORE_POINTS(false, false),
	DIRECT_COMPARISON_BETTER_GOAL_DIFFERENCE(false, false),
	DIRECT_COMPARISON_MORE_GOALS_SCORED(false, false),
	DIRECT_COMPARISON_MORE_AWAY_GOALS_SCORED(false, false),
	
	FAIRPLAY_Y1YR3R3(false, true),
	FAIRPLAY_Y1YR3R4(false, true),
	PLAY_OFF(false, false),
	LOT_DRAWN(false, false);
	
	private boolean evaluateBeforeEnd;
	
	private boolean includeAllGames;
	
	RankingCriterion(boolean evaluateBeforeEnd, boolean includeAllGames) {
		this.evaluateBeforeEnd = evaluateBeforeEnd;
		this.includeAllGames = includeAllGames;
	}
	
	public boolean evaluateBeforeEnd() {
		return evaluateBeforeEnd;
	}
	
	public boolean includeAllGames() {
		return includeAllGames;
	}
	
	public static RankingCriterion parse(String value) {
		return Stream.of(values()).filter(v -> v.toString().equals(value)).findFirst().orElse(null);
	}
}
