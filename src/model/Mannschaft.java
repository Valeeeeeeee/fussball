package model;

import static util.Utilities.*;

import java.io.File;
import java.util.ArrayList;

public class Mannschaft {
	private int id;
	private Start start;
	private String name;
	private String gruendungsdatum;
	
	private int platz = -1;
	private int anzahl_sp;
	private int anzahl_g;
	private int anzahl_u;
	private int anzahl_v;
	private int anzahl_tplus;
	private int anzahl_tminus;
	private int tdiff;
	private int punkte;
	private int valuesCorrectAsOfMatchday = -1;
	private int deductedPoints = 0;
	
	private int[][] daten;
	private Spiel[] spiele;
	private Ergebnis[] ergebnisse;
	public final static int OPPONENT = 0;
	public final static int GOALS = 1;
	public final static int CGOALS = 2;
	public final static int POINTS = 3;
	
	private boolean[] homeaway;
	public final static int HOME = 4;
	public final static int AWAY = 5;
	
	private Wettbewerb wettbewerb;
	private LigaSaison lSeason;
	private TurnierSaison tSeason;
	private Gruppe gruppe;
	private KORunde startKORunde;
	
	private String kaderFileName;
	private int numberOfPlayers;
	private ArrayList<Spieler> kader = new ArrayList<>();
	private int[] numberOfPlayersByPosition;
	private ArrayList<Spieler> eligiblePlayers = new ArrayList<Spieler>();
	private int lastUpdatedForDate = -1;
	private int[] currentNumberOfPlayersByPosition;

	private boolean playsInLeague = false;
	private boolean playsInGroup = false;

	public Mannschaft(Start start, int id, LigaSaison lSeason, String mannschaftsDaten) {
		this.id = id;
		this.start = start;
		this.lSeason = lSeason;
		this.wettbewerb = lSeason;
		this.playsInLeague = true;
		this.playsInGroup = false;
		
		initializeArrays();
		parseString(mannschaftsDaten);
		if (!start.addingNewSeason())	loadKader();
	}

	public Mannschaft(Start start, int id, TurnierSaison tSeason, Gruppe gruppe, String mannschaftsDaten) {
		this.id = id;
		this.start = start;
		this.tSeason = tSeason;
		this.gruppe = gruppe;
		this.wettbewerb = gruppe;
		this.playsInLeague = false;
		this.playsInGroup = true;
		initializeArrays();
		parseString(mannschaftsDaten);
	}
	
	public Mannschaft(Start start, int id, TurnierSaison tSeason, KORunde koRunde, String mannschaftsDaten) {
		this.id = id;
		this.start = start;
		this.tSeason = tSeason;
		this.startKORunde = koRunde;
		this.wettbewerb = koRunde;
		this.playsInLeague = false;
		this.playsInGroup = false;
		initializeArrays();
		parseString(mannschaftsDaten);
	}

	private void initializeArrays() {
		int numberOfMatchdays = 0;
		if (playsInLeague)		numberOfMatchdays = lSeason.getNumberOfMatchdays();
		else if (playsInGroup)	numberOfMatchdays = gruppe.getNumberOfMatchdays();
		else					numberOfMatchdays = tSeason.getNumberOfKORounds() * (tSeason.hasSecondLegKOStage() ? 2 : 1);
		daten = new int[numberOfMatchdays][4];
		homeaway = new boolean[numberOfMatchdays];
		spiele = new Spiel[numberOfMatchdays];
		ergebnisse = new Ergebnis[numberOfMatchdays];
	}
	
	private void loadKader() {
		if (!wettbewerb.teamsHaveKader())	return;
		if (playsInLeague)		kaderFileName = lSeason.getWorkspace() + "Kader" + File.separator;
		else if (playsInGroup)	kaderFileName = gruppe.getWorkspace() + "Kader" + File.separator;
		(new File(kaderFileName)).mkdirs(); // if directory does not exist, creates directory
		kaderFileName += this.name + ".txt";
		kaderFileName = removeUmlaute(kaderFileName);
		
		ArrayList<String> spieler = ausDatei(kaderFileName);
		numberOfPlayers = spieler.size();
		numberOfPlayersByPosition = new int[4];
		kader.clear();
		for (int i = 0; i < numberOfPlayers; i++) {
			kader.add(new Spieler(spieler.get(i), this));
			numberOfPlayersByPosition[kader.get(i).getPosition().getID()]++;
		}
	}
	
	public void saveKader() {
		if (!wettbewerb.teamsHaveKader())	return;
		ArrayList<String> players = new ArrayList<>();
		for (int i = 0; i < numberOfPlayers; i++) {
			players.add(this.kader.get(i).toString());
		}
		inDatei(kaderFileName, players);
	}
	
	public int getCurrentNumberOf(Position position) {
		return this.currentNumberOfPlayersByPosition[position.getID()];
	}
	
	private void setValuesForMatchday(int untilMatchday) {
		if (valuesCorrectAsOfMatchday == untilMatchday)	return;
		
		anzahl_g = anzahl_u = anzahl_v = anzahl_tplus = anzahl_tminus = 0;
		for (int matchday = 0; matchday <= untilMatchday; matchday++) {
			if (daten[matchday][3] == 3)		anzahl_g++;
			else if (daten[matchday][3] == 1)	anzahl_u++;
			else if (daten[matchday][1] < daten[matchday][2])	anzahl_v++;
			
			anzahl_tplus += daten[matchday][1];
			anzahl_tminus += daten[matchday][2];
		}
		
		anzahl_sp = anzahl_g + anzahl_u + anzahl_v;
		punkte = 3 * anzahl_g + anzahl_u + deductedPoints;
		tdiff = anzahl_tplus - anzahl_tminus;
		
		valuesCorrectAsOfMatchday = untilMatchday;
	}
	
	public int get(int index, int firstMatchday, int lastMatchday) {
		if (index == 9 || (index >= 2 && index <= 5)) {
			int anzG = 0, anzU = 0, anzV = 0;
			for (int matchday = firstMatchday; matchday <= lastMatchday; matchday++) {
				if (daten[matchday][3] == 3)		anzG++;
				else if (daten[matchday][3] == 1)	anzU++;
				else if (daten[matchday][1] < daten[matchday][2])	anzV++;
			}
			
			if (index == 2)	return anzG + anzU + anzV;
			if (index == 3)	return anzG;
			if (index == 4)	return anzU;
			if (index == 5)	return anzV;
			if (index == 9)	return 3 * anzG + anzU + deductedPoints;
		}
		if (index >= 6 && index <= 8) {
			int anzT = 0, anzGT = 0;
			for (int matchday = firstMatchday; matchday <= lastMatchday; matchday++) {
				anzT += daten[matchday][1];
				anzGT += daten[matchday][2];
			}
			
			if (index == 6)	return anzT;
			if (index == 7)	return anzGT;
			if (index == 8)	return anzT - anzGT;
		}
		
		return -1;
	}
	
	public String getString(int index) {
		if (index == 0)	return "" + (this.platz + 1);
		if (index == 1)	return this.name;
		if (index == 2)	return "" + this.anzahl_sp;
		if (index == 3)	return "" + this.anzahl_g;
		if (index == 4)	return "" + this.anzahl_u;
		if (index == 5)	return "" + this.anzahl_v;
		if (index == 6)	return "" + this.anzahl_tplus;
		if (index == 7)	return "" + this.anzahl_tminus;
		if (index == 8)	return "" + this.tdiff;
		if (index == 9)	return "" + this.punkte;
		return null;
	}

	public int get(int index, int untilMatchday) {
		setValuesForMatchday(untilMatchday);
		if (index == 0)	return this.platz;
		if (index == 2)	return this.anzahl_sp;
		if (index == 3)	return this.anzahl_g;
		if (index == 4)	return this.anzahl_u;
		if (index == 5)	return this.anzahl_v;
		if (index == 6)	return this.anzahl_tplus;
		if (index == 7)	return this.anzahl_tminus;
		if (index == 8)	return this.tdiff;
		if (index == 9)	return this.punkte;
		return -1;
	}
	
	/**
	 * 1: Siegesserie <br/>
	 * 2: Unentschiedenserie <br/>
	 * 3: Niederlagenserie <br/>
	 * 4: Unbesiegt-Serie <br/>
	 * 5: Sieglos-Serie <br/>
	 * 6: Tor-Serie <br/>
	 * 7: Torlos-Serie <br/>
	 * 8: Gegentor-Serie <br/>
	 * 9: Gegentorlos-Serie <br/>
	 * @param index
	 * @return
	 */
	public int getSeries(int index) {
		int currentDuration = 0, longestDuration = 0;
		
		boolean reset;
		for (int matchday = 0; matchday < daten.length; matchday++) {
			if (daten[matchday][0] != 0) {
				if (daten[matchday][3] == 0 && daten[matchday][2] == 0)	continue;
				reset = false;
				
				switch (index) {
					case 1:		if (daten[matchday][3] != 3)	reset = true;	break;
					case 2:		if (daten[matchday][3] != 1)	reset = true;	break;
					case 3:		if (daten[matchday][3] != 0)	reset = true;	break;
					case 4:		if (daten[matchday][3] == 0)	reset = true;	break;
					case 5:		if (daten[matchday][3] == 3)	reset = true;	break;
					case 6:		if (daten[matchday][1] == 0)	reset = true;	break;
					case 7:		if (daten[matchday][1] > 0)		reset = true;	break;
					case 8:		if (daten[matchday][2] == 0)	reset = true;	break;
					case 9:		if (daten[matchday][2] > 0)		reset = true;	break;
				}
				if (reset) {
					longestDuration = Math.max(longestDuration, currentDuration);
					currentDuration = 0;
				} else {
					currentDuration++;
				}
			}
		}
		
		longestDuration = Math.max(longestDuration, currentDuration);
		
		return longestDuration;
	}

	public int getPlace() {
		return this.platz;
	}

	public void setPlace(int value) {
		this.platz = value;
	}
	
	public boolean isSpielEntered(int matchday) {
		if (spiele[matchday] != null)	return true;
		return false;
	}
	
	public boolean isErgebnisEntered(int matchday) {
		if (ergebnisse[matchday] != null)	return true;
		return false;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getGruendungsdatum() {
		return this.gruendungsdatum;
	}

	public void setGruendungsdatum(String value) {
		this.gruendungsdatum = value;
	}

	public int getId() {
		return this.id;
	}
	
	public int getDeductedPoints() {
		return this.deductedPoints;
	}
	
	public void setDeductedPoints(int deductedPoints) {
		this.deductedPoints = deductedPoints;
		
		// force update of values
		int untilMatchday = valuesCorrectAsOfMatchday;
		valuesCorrectAsOfMatchday = -1;
		setValuesForMatchday(untilMatchday);
	}
	
	private void updateEligiblePlayers(int date) {
		if (lastUpdatedForDate == date)	return;
		
		currentNumberOfPlayersByPosition = new int[4];
		eligiblePlayers.clear();
		
		for (Spieler spieler : kader) {
			if (spieler.isEligible(date)) {
				eligiblePlayers.add(spieler);
				currentNumberOfPlayersByPosition[spieler.getPosition().getID()]++;
			}
		}
		
		lastUpdatedForDate = date;
	}
	
	public int getCurrentNumberOfPlayers(int date) {
		updateEligiblePlayers(date);
		return eligiblePlayers.size();
	}
	
	public ArrayList<Spieler> getEligiblePlayers(int date) {
		updateEligiblePlayers(date);
		return eligiblePlayers;
	}
	
	public Spieler getSpieler(int squadNumber, int date) {
		for (Spieler spieler : kader) {
			if (spieler.getSquadNumber() == squadNumber && spieler.isEligible(date))	return spieler;
		}
		
		return null;
	}
	
	public String getDateAndTime(int matchday) {
		if (playsInLeague)		return lSeason.getDateOfTeam(matchday, id);
		else if (playsInGroup)	return gruppe.getDateOfTeam(matchday, id);
		else					return "21.06.2014 22:00";
	}
	
	public void resetMatch(int matchday) {
		this.setResult(matchday, null);
		this.resetGegner(matchday);
	}

	public void resetGegner(int matchday) {
		homeaway[matchday] = false;
		this.daten[matchday][OPPONENT] = 0;
	}
	
	public void setMatch(int matchday, Spiel spiel) {
		spiele[matchday] = spiel;
		if (spiel != null) {
			if (this.id == spiel.home()) {
				setGegner(matchday, true, spiel.away());
			} else if (this.id == spiel.away()) {
				setGegner(matchday, false, spiel.home());
			} else {
				message("This match came to the wrong team.");
				spiele[matchday] = null;
			}
		}
	}
	
	private void setGegner(int matchday, boolean homeoraway, int opponent) {
		this.homeaway[matchday] = homeoraway;
		this.daten[matchday][OPPONENT] = opponent;
	}

	public void setResult(int matchday, Ergebnis result) {
		ergebnisse[matchday] = result;
		if (result == null) {
			setResult(matchday, -1, -1);
			return;
		}
		
		if (homeaway[matchday]) {
			setResult(matchday, result.home(), result.away());
		} else {
			setResult(matchday, result.away(), result.home());
		}
	}

	private void setResult(int matchday, int tore, int gegentore) {
		if (0 > matchday || matchday >= daten.length) {
			message("Ergebnis konnte nicht gesetzt werden, da matchday die Grenzen verlaesst.");
			return;
		}
		
//		log(this.name + " empfaengt Infos zum Spiel am " + (matchday + 1) + ". Spieltag, Ergebnis ist " + tore + "-" + gegentore);

		// "Entfernen" des alten Ergebnisses, wenn als Parameter -1:-1 kommt, wird das Ergebnis zurueckgesetzt
		int oldtore = this.daten[matchday][GOALS];
		int oldgegentore = this.daten[matchday][CGOALS];
		int oldpoints = this.daten[matchday][POINTS];

		if (oldpoints > 0 || oldtore < oldgegentore) { // ansonsten steht kein Ergebnis drin (0:0 mit 0 Punkten)
			this.anzahl_tplus -= oldtore;
			this.anzahl_tminus -= oldgegentore;

			if (oldpoints == 3)			this.anzahl_g--;
			else if (oldpoints == 1)	this.anzahl_u--;
			else if (oldpoints == 0)	this.anzahl_v--;
			
			this.daten[matchday][GOALS] = 0;
			this.daten[matchday][CGOALS] = 0;
			this.daten[matchday][POINTS] = 0;
		}
		
		if (tore != -1 && gegentore != -1) {
			// Speichern des neuen Ergebnisses
			this.daten[matchday][GOALS] = tore;
			this.daten[matchday][CGOALS] = gegentore;

			this.anzahl_tplus += tore;
			this.anzahl_tminus += gegentore;

			int points;
			if (tore > gegentore) {
				points = 3;
				this.anzahl_g++;
			} else if (tore == gegentore) {
				points = 1;
				this.anzahl_u++;
			} else {
				points = 0;
				this.anzahl_v++;
			}

			this.daten[matchday][POINTS] = points;
		}
		
		this.anzahl_sp = this.anzahl_g + this.anzahl_u + this.anzahl_v;
		this.punkte = 3 * this.anzahl_g + this.anzahl_u + this.deductedPoints;
		this.tdiff = this.anzahl_tplus - this.anzahl_tminus;
	}
	
	/**
	 * Returns an array containing: <br>
	 * <ol>
	 * 	<li> an integer representing home or away
	 * 	<li> an integer representing the opponent
	 * 	<li> the number of goals scored
	 * 	<li> the number of goals conceded
	 * </ol>
	 * @param matchday The matchday of which the match information are required.
	 * @return an array with the mentioned content
	 */
	public int[] getMatch(int matchday) {
		int[] match = new int[4];

		if (homeaway[matchday])	match[0] = HOME;
		else					match[0] = AWAY;
		match[1] = daten[matchday][OPPONENT];
		match[2] = daten[matchday][GOALS];
		match[3] = daten[matchday][CGOALS];

		return match;
	}

	public void compareWithOtherTeams(Mannschaft[] otherTeams, int untilMatchday) {
		this.setValuesForMatchday(untilMatchday);
		ArrayList<Integer> teamsSamePoints = new ArrayList<>();
		this.platz = 0;
		
		for (Mannschaft vergleich : otherTeams) {
			if (this.id == vergleich.id)	continue;
			vergleich.setValuesForMatchday(untilMatchday);
			
			if (this.punkte == vergleich.punkte)	teamsSamePoints.add(vergleich.id);
			else if (this.punkte < vergleich.punkte)	this.platz++;
		}
		
		if (wettbewerb.useGoalDifference()) {
			for (Integer id : teamsSamePoints) {
				if (this.tdiff == otherTeams[id - 1].tdiff) {
					if (this.anzahl_tplus < otherTeams[id - 1].anzahl_tplus)	this.platz++;
				}
				else if (this.tdiff < otherTeams[id - 1].tdiff)	this.platz++;
			}
		} else {
			teamsSamePoints.add(0, this.id);
			int[] points = new int[teamsSamePoints.size() + 1];
			int[] pointsOpp = new int[teamsSamePoints.size() + 1];
			int[] goals = new int[teamsSamePoints.size() + 1];
			int[] goalsOpp = new int[teamsSamePoints.size() + 1];
			
			for (int i = 0; i < teamsSamePoints.size(); i++) {
				Mannschaft team1 = otherTeams[teamsSamePoints.get(i) - 1];
				for (int j = 0; j < teamsSamePoints.size(); j++) {
					if (i == j)	continue;
					Mannschaft team2 = otherTeams[teamsSamePoints.get(j) - 1];
					for (int k = 0; k < daten.length; k++) {
						if (team1.daten[k][OPPONENT] == team2.id) {
							goals[i] += team1.daten[k][GOALS];
							goalsOpp[i] += team1.daten[k][CGOALS];
							points[i] += team1.daten[k][POINTS];
							pointsOpp[i] += (team1.daten[k][POINTS] == 1 ? 1 : 3 - team1.daten[k][POINTS]);
						}
					}
				}
			}
			for (int i = 1; i < teamsSamePoints.size(); i++) {
				if (points[0] == points[i]) {
					if (goals[0] - goalsOpp[0] == goals[i] - goalsOpp[i]) {
						if (goals[0] < goals[i])	this.platz++;
					} else if (goals[0] - goalsOpp[0] < goals[i] - goalsOpp[i])	this.platz++;
				}
				else if (points[0] < points[i])	this.platz++;
			}
		}
	}

	private void parseString(String data) {
		String[] daten = data.split(";");
		
		this.name = daten[0];
		if (daten.length > 1) {
			this.gruendungsdatum = !daten[1].equals("null") ? daten[1] : null;
			if (daten.length > 2) {
				this.deductedPoints = Integer.parseInt(daten[2]);
				log(this.name + ": This team has been deducted " + this.deductedPoints + " points.");
			}
		}
	}

	public String toString() {
		String data = this.name;
		if (playsInLeague) {
			data += ";" + this.gruendungsdatum;
			if (deductedPoints != 0) {
				data += ";" + this.deductedPoints;
			}
		} else if (playsInGroup) {
			if (deductedPoints != 0) {
				data += ";null;" + this.deductedPoints;
			}
		}

		return data;
	}
}
