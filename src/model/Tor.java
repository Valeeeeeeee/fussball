package model;

import static util.Utilities.*;

public class Tor {

	private String id;
	private Spiel match;
	private boolean firstTeam;
	private boolean penalty;
	private boolean ownGoal;
	private Minute minute;
	private Spieler scorer;
	private Spieler assister;
	
	public Tor(Spiel match, boolean firstTeam, boolean penalty, boolean ownGoal, Minute minute) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		
		id = match.home() + "v" + match.away() + "-h" + firstTeam + "-m" + minute.toString() + "-s-a" + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		log("GOOOAL for " + (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getName() + 
			" in the " + minute + ". minute");
	}
	
	public Tor(Spiel match, boolean firstTeam, boolean penalty, boolean ownGoal, Minute minute, Spieler scorer) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		this.scorer = scorer;
		
		id = match.home() + "v" + match.away() + "-h" + firstTeam + "-m" + minute.toString() + "-s" + scorer.getSquadNumber() + "-a" + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		log("GOOOAL for " + (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPseudonymOrLN());
	}
	
	public Tor(Spiel match, boolean firstTeam, boolean penalty, boolean ownGoal, Minute minute, Spieler scorer, Spieler assister) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		this.scorer = scorer;
		this.assister = assister;
		
		id = match.home() + "v" + match.away() + "-h" + firstTeam + "-m" + minute.toString() + "-s" + scorer.getSquadNumber() + "-a" + assister.getSquadNumber()
					+ (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		log("GOOOAL for " + (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPseudonymOrLN() + 
			" and assisted by " + assister.getPseudonymOrLN());
	}
	
	public Tor(Spiel match, String data) {
		this.match = match;
		parseString(data);
	}
	
	public String getID() {
		return id;
	}

	public Spiel getMatch() {
		return match;
	}
	
	public boolean isOwnGoal() {
		return ownGoal;
	}
	
	public boolean isPenalty() {
		return penalty;
	}

	public boolean isFirstTeam() {
		return firstTeam;
	}
	
	public Minute getMinute() {
		return minute;
	}

	public Spieler getScorer() {
		return scorer;
	}

	public Spieler getAssister() {
		return assister;
	}
	
	private void parseString(String data) {
		firstTeam = Boolean.parseBoolean(data.substring(0, data.indexOf("-m")));
		penalty = data.indexOf("-p") != -1;
		if (penalty)	data = data.substring(0, data.indexOf("-p"));
		ownGoal = data.indexOf("-og") != -1;
		if (ownGoal)	data = data.substring(0, data.indexOf("-og"));
		minute = Minute.parse(data.substring(data.indexOf("-m") + 2, data.indexOf("-s")));
		String substring;
		if ((substring = data.substring(data.indexOf("-s") + 2, data.indexOf("-a"))).length() > 0) {
			int sqScorer = Integer.parseInt(substring);
			scorer = (firstTeam ^ ownGoal ? match.getHomeTeam() : match.getAwayTeam()).getPlayer(sqScorer, match.getDate());
		}
		if ((substring = data.substring(data.indexOf("-a") + 2)).length() > 0) {
			int sqAssist = Integer.parseInt(substring);
			assister = (firstTeam ? match.getHomeTeam() : match.getAwayTeam()).getPlayer(sqAssist, match.getDate());
		}
		
		id = match.home() + "v" + match.away() + "-h" + data + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
	}
	
	public String toString() {
		return firstTeam + "-m" + minute.toString() + "-s" + (scorer != null ? scorer.getSquadNumber() : "")
					+ "-a" + (assister != null ? assister.getSquadNumber() : "") + (ownGoal ? "-og" : (penalty ? "-p" : ""));
	}
}
