package model;

public class TeamBilanz {
	
	private int numberOfMatches;
	
	private int numberOfWins;
	private int numberOfDraws;
	private int numberOfLosses;
	
	private int numberOfGoalsScored;
	private int numberOfGoalsConceded;
	
	private int goalDifference;
	
	private int numberOfPoints;
	
	private TeamBilanz(int numberOfWins, int numberOfDraws, int numberOfLosses, int numberOfGoalsScored, int numberOfGoalsConceded) {
		this.numberOfWins = numberOfWins;
		this.numberOfDraws = numberOfDraws;
		this.numberOfLosses = numberOfLosses;
		this.numberOfGoalsScored = numberOfGoalsScored;
		this.numberOfGoalsConceded = numberOfGoalsConceded;
		calculateAggregatedValues();
	}
	
	public int getNumberOfMatches() {
		return numberOfMatches;
	}
	
	public int getNumberOfWins() {
		return numberOfWins;
	}
	
	public int getNumberOfDraws() {
		return numberOfDraws;
	}
	
	public int getNumberOfLosses() {
		return numberOfLosses;
	}
	
	public int getNumberOfGoalsScored() {
		return numberOfGoalsScored;
	}
	
	public int getNumberOfGoalsConceded() {
		return numberOfGoalsConceded;
	}
	
	public int getGoalDifference() {
		return goalDifference;
	}
	
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	
	protected void calculateAggregatedValues() {
		numberOfMatches = numberOfWins + numberOfDraws + numberOfLosses;
		goalDifference = numberOfGoalsScored - numberOfGoalsConceded;
		numberOfPoints = 3 * numberOfWins + numberOfDraws;
	}
	
	public static TeamBilanz of(int numberOfWins, int numberOfDraws, int numberOfLosses, int numberOfGoalsScored, int numberOfGoalsConceded) {
		return new TeamBilanz(numberOfWins, numberOfDraws, numberOfLosses, numberOfGoalsScored, numberOfGoalsConceded);
	}
}
