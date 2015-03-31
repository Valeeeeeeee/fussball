package model;

import static util.Utilities.*;

import java.io.File;

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
	
	private Liga liga;
	private Turnier turnier;
	private Gruppe gruppe;
	private KORunde startKORunde;
	
	private String kaderFileName;
	private int numberOfPlayers;
	private Spieler[] kader;
	private int[] numberOfPlayersByPosition;
	private final static int GOALKEEPERS = 0;
	private final static int DEFENDERS = 1;
	private final static int MIDFIELDERS = 2;
	private final static int ATTACKERS = 3;

	private boolean playsInLeague = false;
	private boolean playsInGroup = false;

	public Mannschaft(Start start, int id, Liga liga, String mannschaftsDaten) {
		this.id = id;
		this.start = start;
		this.liga = liga;
		this.playsInLeague = true;
		this.playsInGroup = false;
		
		initializeArrays();
		parseString(mannschaftsDaten);
		loadKader();
	}

	public Mannschaft(Start start, int id, Turnier turnier, Gruppe gruppe, String mannschaftsDaten) {
		this.id = id;
		this.start = start;
		this.turnier = turnier;
		this.gruppe = gruppe;
		this.playsInLeague = false;
		this.playsInGroup = true;
		initializeArrays();
		parseString(mannschaftsDaten);
	}
	
	public Mannschaft(Start start, int id, Turnier turnier, KORunde koRunde, String mannschaftsDaten) {
		this.id = id;
		this.start = start;
		this.turnier = turnier;
		this.startKORunde = koRunde;
		this.playsInLeague = false;
		this.playsInGroup = false;
		initializeArrays();
		parseString(mannschaftsDaten);
	}

	private void initializeArrays() {
		int numberOfMatchdays = 0;
		if (playsInLeague)		numberOfMatchdays = liga.getNumberOfMatchdays();
		else if (playsInGroup)	numberOfMatchdays = gruppe.getNumberOfMatchdays();
		else					numberOfMatchdays = turnier.getNumberOfKORounds() * (turnier.koPhaseHasSecondLeg() ? 2 : 1);
		daten = new int[numberOfMatchdays][4];
		homeaway = new boolean[numberOfMatchdays];
		spiele = new Spiel[numberOfMatchdays];
		ergebnisse = new Ergebnis[numberOfMatchdays];
	}
	
	private void loadKader() {
		if (playsInGroup || !playsInLeague)	return;
		if (playsInLeague)		kaderFileName = liga.getWorkspace() + "Kader" + File.separator;
		else if (playsInGroup)	kaderFileName = gruppe.getWorkspace() + "Kader" + File.separator;
		(new File(kaderFileName)).mkdirs(); // if directory does not exist, creates directory
		kaderFileName += this.name + ".txt";
		kaderFileName = removeUmlaute(kaderFileName);
		
		String[] spieler = ausDatei(kaderFileName);
		numberOfPlayers = spieler.length;
		numberOfPlayersByPosition = new int[4];
		kader = new Spieler[numberOfPlayers];
		for (int i = 0; i < kader.length; i++) {
			kader[i] = new Spieler(spieler[i], this);
			numberOfPlayersByPosition[kader[i].getPosition().getID()]++;
		}
	}
	
	public void saveKader() {
		if (playsInGroup || !playsInLeague)	return;
		String[] players = new String[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			players[i] = this.kader[i].toString();
		}
		inDatei(kaderFileName, players);
	}
	
	public int getNumberOfGoalkeepers() {
		return this.numberOfPlayersByPosition[GOALKEEPERS];
	}
	
	public int getNumberOfDefenders() {
		return this.numberOfPlayersByPosition[DEFENDERS];
	}
	
	public int getNumberOfMidfielders() {
		return this.numberOfPlayersByPosition[MIDFIELDERS];
	}
	
	public int getNumberOfAttackers() {
		return this.numberOfPlayersByPosition[ATTACKERS];
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
			if (index == 9)	return 3 * anzG + anzU - deductedPoints;
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

	public int get(int index) {
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

	public void set(int index, int value) {
		if (index == 0)	this.platz = value;
		if (index == 2)	this.anzahl_sp = value;
		if (index == 3)	this.anzahl_g = value;
		if (index == 4)	this.anzahl_u = value;
		if (index == 5)	this.anzahl_v = value;
		if (index == 6)	this.anzahl_tplus = value;
		if (index == 7)	this.anzahl_tminus = value;
		if (index == 8)	this.tdiff = value;
		if (index == 9)	this.punkte = value;
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
	
	public int getNumberOfPlayers() {
		return this.numberOfPlayers;
	}
	
	public Spieler[] getKader() {
		return this.kader;
	}
	
	public Spieler getSpieler(int squadNumber) {
		for (Spieler spieler : kader) {
			if (spieler.getSquadNumber() == squadNumber)	return spieler;
		}
		
		return null;
	}
	
	public String getDateAndTime(int matchday) {
		if (playsInLeague)		return liga.getDateOfTeam(matchday, id);
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
				setGegner(matchday, HOME, spiel.away());
			} else if (this.id == spiel.away()) {
				setGegner(matchday, AWAY, spiel.home());
			} else {
				message("This match came to the wrong team.");
				spiele[matchday] = null;
			}
		}
	}
	
	// TODO make private: people shall use setMatch
	/**
	 * This method is deprecated. Use setMatch instead.
	 * @param matchday
	 * @param homeoraway
	 * @param opponent
	 */
	@Deprecated
	public void setGegner(int matchday, int homeoraway, int opponent) {
//		JOptionPane.showMessageDialog(null, this.name + " empfaengt Infos zum Spiel am " + (matchday + 1) + ". Spieltag, Gegner ist " + opponent
//				+ ", daheim ist " + homeoraway + ", spielnummer ist " + spielnummer);

		if (homeoraway == HOME)	homeaway[matchday] = true;
		else					homeaway[matchday] = false;

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

	public int compareWith(Mannschaft vergleich) {
		if (this.punkte == vergleich.punkte) {
			if (this.tdiff == vergleich.tdiff) {
				if (this.anzahl_tplus == vergleich.anzahl_tplus) {
					return 0;
				}
				else if (this.anzahl_tplus > vergleich.anzahl_tplus)	return 1;
				else if (this.anzahl_tplus < vergleich.anzahl_tplus)	return 2;
			}
			else if (this.tdiff > vergleich.tdiff)	return 1;
			else if (this.tdiff < vergleich.tdiff)	return 2;
		} 
		else if (this.punkte > vergleich.punkte)	return 1;
		else if (this.punkte < vergleich.punkte)	return 2;
		
		return -1;
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





