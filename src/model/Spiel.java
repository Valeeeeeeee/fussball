package model;

import static util.Utilities.*;

import java.util.ArrayList;

public class Spiel {
	
	private int matchday;
	private int homeTeamIndex;
	private int awayTeamIndex;
	
	private Wettbewerb wettbewerb;
	private Mannschaft homeTeam;
	private Mannschaft awayTeam;
	private int[] lineupHome;
	private int[] lineupAway;
	
	private String schiedsrichter;
	private Ergebnis ergebnis;
	private ArrayList<Tor> tore = new ArrayList<>();
	
	public Spiel(Wettbewerb wettbewerb, int matchday, int homeTeamIndex, int awayTeamIndex) {
		this.wettbewerb = wettbewerb;
		
		this.matchday = matchday;
		this.homeTeamIndex = homeTeamIndex;
		this.homeTeam = wettbewerb.getMannschaften()[homeTeamIndex - 1];
		this.awayTeamIndex = awayTeamIndex;
		this.awayTeam = wettbewerb.getMannschaften()[awayTeamIndex - 1];
	}
	
	public Spiel(Wettbewerb wettbewerb, int matchday, String daten) {
		this.wettbewerb = wettbewerb;
		this.matchday = matchday;
		parseString(daten);
	}
	
	public Wettbewerb getWettbewerb() {
		return this.wettbewerb;
	}
	
	public String getDescription() {
		return wettbewerb.getMatchdayDescription(matchday);
	}
	
	public int home() {
		return this.homeTeamIndex;
	}
	
	public Mannschaft getHomeTeam() {
		return this.homeTeam;
	}
	
	public int away() {
		return this.awayTeamIndex;
	}
	
	public Mannschaft getAwayTeam() {
		return this.awayTeam;
	}
	
	public Ergebnis getErgebnis() {
		return this.ergebnis;
	}
	
	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}
	
	public int[] getLineupHome() {
		return this.lineupHome;
	}
	
	public void setLineupHome(int[] lineupHome) {
		this.lineupHome = lineupHome;
	}
	
	public int[] getLineupAway() {
		return this.lineupAway;
	}
	
	public void setLineupAway(int[] lineupAway) {
		this.lineupAway = lineupAway;
	}
	
	public ArrayList<Tor> getTore() {
		return this.tore;
	}
	
	public void addGoal(Tor tor) {
		if (tor != null) {
			this.tore.add(tor);
			ergebnis = new Ergebnis(ergebnis != null ? ergebnis : new Ergebnis("0:0"), tor);
		}
	}
	
	public String getSchiedsrichter() {
		return this.schiedsrichter;
	}
	
	public void setSchiedsrichter(String schiedsrichter) {
		this.schiedsrichter = schiedsrichter;
	}
	
	private String getRemainder() {
		String remainder = "";
		
		if (ergebnis != null) {
			remainder = "+{" + matchDataToString() + "}";
		}
		
		if (lineupHome != null || lineupAway != null) {
			remainder += "+{" + lineupToString(lineupHome) + "}+{" + lineupToString(lineupAway) + "}";
		}
		
		return remainder;
	}
	
	private String matchDataToString() {
		String matchData = "" + ergebnis;
		
		for (Tor tor : tore) {
			matchData += "#" + tor;
		}
		
		return matchData;
	}
	
	private String lineupToString(int[] lineup) {
		String lineupString = "";
		
		if (lineup != null) {
			for (int i = 0; i < lineup.length; i++) {
				lineupString += lineup[i];
				if (i < lineup.length - 1)	lineupString += ",";
			}
		} else {
			lineupString = "null";
		}
		
		return lineupString;
	}
	
	private void parseMatchData(String matchData) {
		matchData = matchData.replace("{", "").replace("}", "");
		String[] matchDataSplit = matchData.split("#");
		this.ergebnis = new Ergebnis(matchDataSplit[0]);
		if (matchDataSplit.length > 1) {
			for (int i = 1; i < matchDataSplit.length; i++) {
				tore.add(new Tor(this, matchDataSplit[i]));
			}
		}
	}
	
	private int[] parseLineup(String lineupString) {
		int[] lineup = null;
		
		if (!lineupString.equals("null")) {
			String[] lineupSplit = lineupString.replace("{", "").replace("}", "").split(",");
			lineup = new int[lineupSplit.length];
			for (int i = 0; i < lineupSplit.length; i++) {
				lineup[i] = Integer.parseInt(lineupSplit[i]);
			}
		}
		
		return checkLineup(lineup);
	}
	
	private int[] checkLineup(int[] lineup) {
		boolean isValid = true;
		
		return isValid ? lineup : null;
	}
	
	private void parseString(String daten) {
		try {
			String[] datenSplit = daten.split("\\+");
			String spieldaten = datenSplit[0];
			
			this.homeTeamIndex = Integer.parseInt(spieldaten.split(":")[0]);
			this.awayTeamIndex = Integer.parseInt(spieldaten.split(":")[1]);
			
			if (this.homeTeamIndex == this.awayTeamIndex || this.homeTeamIndex == -1 || this.awayTeamIndex == -1) {
				throw new IllegalArgumentException();
			}
			
			this.homeTeam = wettbewerb.getMannschaften()[homeTeamIndex - 1];
			this.awayTeam = wettbewerb.getMannschaften()[awayTeamIndex - 1];
			
			if (datenSplit.length > 1) {
				parseMatchData(datenSplit[1]);
				if (datenSplit.length == 4) {
					lineupHome = parseLineup(datenSplit[2]);
					lineupAway = parseLineup(datenSplit[3]);
				}
			}
			
		} catch (IllegalArgumentException iae) {
			log("The given match was formally correct, but impossible.");
			log(iae.getMessage());
		} catch (Exception e) {
			log("The given match was formally incorrect.");
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String toString = this.homeTeamIndex + ":" + this.awayTeamIndex + getRemainder();
		
		return toString;
	}
}
