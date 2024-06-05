package model;

public class TeamFairplayBilanz {

	private int numberOfYellowCards;
	private int numberOfSecondYellowCards;
	private int numberOfRedCards;
	
	private TeamFairplayBilanz(int numberOfYellowCards, int numberOfSecondYellowCards, int numberOfRedCards) {
		this.numberOfYellowCards = numberOfYellowCards;
		this.numberOfSecondYellowCards = numberOfSecondYellowCards;
		this.numberOfRedCards = numberOfRedCards;
	}
	
	public int getNumberOfYellowCards() {
		return numberOfYellowCards;
	}
	
	public int getNumberOfSecondYellowCards() {
		return numberOfSecondYellowCards;
	}
	
	public int getNumberOfRedCards() {
		return numberOfRedCards;
	}
	
	public static TeamFairplayBilanz of(int numberOfYellowCards, int numberOfSecondYellowCards, int numberOfRedCards) {
		return new TeamFairplayBilanz(numberOfYellowCards, numberOfSecondYellowCards, numberOfRedCards);
	}
}
