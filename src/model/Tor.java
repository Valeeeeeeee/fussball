package model;

import static util.Utilities.*;

public class Tor {

	private String id;
	private Spiel match;
	private boolean firstTeam;
	private boolean penalty;
	private boolean ownGoal;
	private Minute minute;
	private TeamAffiliation scorer;
	private TeamAffiliation assister;
	
	public Tor(Spiel match, boolean firstTeam, boolean penalty, boolean ownGoal, Minute minute) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		
		id = match.toString() + "-h" + firstTeam + "-m" + minute.toString() + "-s-a" + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		log("GOOOAL for " + match.getTeam(firstTeam).getName() + 
			" in the " + minute + ". minute");
	}
	
	public Tor(Spiel match, boolean firstTeam, boolean penalty, boolean ownGoal, Minute minute, TeamAffiliation scorer) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		this.scorer = scorer;
		
		id = match.toString() + "-h" + firstTeam + "-m" + minute.toString() + "-s" + scorer.getSquadNumber() + "-a" + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		log("GOOOAL for " + match.getTeam(firstTeam).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPlayer().getPopularOrLastName());
	}
	
	public Tor(Spiel match, boolean firstTeam, boolean penalty, boolean ownGoal, Minute minute, TeamAffiliation scorer, TeamAffiliation assister) {
		this.match = match;
		this.firstTeam = firstTeam;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
		this.minute = minute;
		this.scorer = scorer;
		this.assister = assister;
		
		id = match.toString() + "-h" + firstTeam + "-m" + minute.toString() + "-s" + scorer.getSquadNumber() + "-a" + assister.getSquadNumber()
					+ (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
		log("GOOOAL for " + match.getTeam(firstTeam).getName() + 
			" in the " + minute + ". minute scored by " + scorer.getPlayer().getPopularOrLastName() + 
			" and assisted by " + assister.getPlayer().getPopularOrLastName());
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
	
	public TeamAffiliation getScorer() {
		return scorer;
	}

	public TeamAffiliation getAssister() {
		return assister;
	}

	public boolean isScorer(TeamAffiliation player) {
		if (scorer == null)	return false;
		return scorer.equals(player);
	}

	public boolean isAssister(TeamAffiliation player) {
		if (assister == null)	return false;
		return assister.equals(player);
	}
	
	private void parseString(String data) {
		penalty = data.indexOf("-p") != -1;
		ownGoal = data.indexOf("-og") != -1;
		data = data.replace("-p", "").replace("-og", "");
		firstTeam = Boolean.parseBoolean(data.substring(0, data.indexOf("-m")));
		minute = Minute.parse(data.substring(data.indexOf("-m") + 2, data.indexOf("-s")));
		String substring;
		if ((substring = data.substring(data.indexOf("-s") + 2, data.indexOf("-a"))).length() > 0) {
			int sqScorer = Integer.parseInt(substring);
			scorer = match.getTeam(firstTeam ^ ownGoal).getAffiliation(sqScorer, match.getKickOffTime().getDate());
			if (scorer == null) {
				message("Fehler beim Parsen der Tore des Teams " + match.getTeam(firstTeam).getName() + " im Spiel gegen " + match.getTeam(!firstTeam).getName());
				log(String.format(NO_PLAYER_FOR_SHIRTNUMBER, sqScorer, match.getTeam(firstTeam).getName()));
			}
		}
		if ((substring = data.substring(data.indexOf("-a") + 2)).length() > 0) {
			int sqAssist = Integer.parseInt(substring);
			assister = match.getTeam(firstTeam).getAffiliation(sqAssist, match.getKickOffTime().getDate());
			if (assister == null) {
				message("Fehler beim Parsen der Tore des Teams " + match.getTeam(firstTeam).getName() + " im Spiel gegen " + match.getTeam(!firstTeam).getName());
				log(String.format(NO_PLAYER_FOR_SHIRTNUMBER, sqAssist, match.getTeam(firstTeam).getName()));
			}
		}
		
		id = match.toString() + "-h" + data + (ownGoal ? "-og" : "") + (penalty ? "-p" : "");
	}
	
	public String toString() {
		return firstTeam + "-m" + minute.toString() + "-s" + (scorer != null ? scorer.getSquadNumber() : "")
					+ "-a" + (assister != null ? assister.getSquadNumber() : "") + (ownGoal ? "-og" : (penalty ? "-p" : ""));
	}
}
