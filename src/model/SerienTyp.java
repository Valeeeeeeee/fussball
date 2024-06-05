package model;

public enum SerienTyp {
	WIN("gewonnen"),
	DRAW("unentschieden"),
	LOSS("verloren"),
	UNBEATEN("unbesiegt"),
	WINLESS("sieglos"),
	GOAL_SCORED("mit Tor"),
	NO_GOAL_SCORED("ohne Tor"),
	GOAL_CONCEDED("mit Gegentor"),
	NO_GOAL_CONCEDED("ohne Gegentor");
	
	private String description;
	
	SerienTyp(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
