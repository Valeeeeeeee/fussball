package model;

import javax.swing.JOptionPane;
import static util.Utilities.*;

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
	
	private int[][] daten;
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

	private boolean playsInALeague = false;

	public Mannschaft(Start start, int i, Liga liga) {
		id = i;
		this.start = start;
		this.liga = liga;
		this.playsInALeague = true;
		initializeArrays();
	}

	public Mannschaft(Start start, int i, Liga liga, String[] mannschaftsDaten) {
		id = i;
		this.start = start;
		this.liga = liga;
		this.playsInALeague = true;
		initializeArrays();

		this.name = mannschaftsDaten[0];
		this.gruendungsdatum = mannschaftsDaten[1];
	}

	public Mannschaft(Start start, int i, Turnier turnier, Gruppe gruppe) {
		id = i;
		this.start = start;
		this.turnier = turnier;
		this.gruppe = gruppe;
		this.playsInALeague = false;
		initializeArrays();
	}

	public Mannschaft(Start start, int i, Turnier turnier, Gruppe gruppe, String[] mannschaftsDaten) {
		id = i;
		this.start = start;
		this.turnier = turnier;
		this.gruppe = gruppe;
		this.playsInALeague = false;
		initializeArrays();

		this.name = mannschaftsDaten[0];
		this.gruendungsdatum = mannschaftsDaten[1];
	}

	private void initializeArrays() {
		if (playsInALeague) {
			daten = new int[liga.getAnzahlSpieltage()][4];
			homeaway = new boolean[liga.getAnzahlSpieltage()];
		} else {
			daten = new int[gruppe.getNumberOfMatchdays()][4];
			homeaway = new boolean[gruppe.getNumberOfMatchdays()];
		}
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
		else			return null;
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
		else			return -1;
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

	public void setGegner(int matchday, int homeoraway, int opponent, int spielnummer) {
//		JOptionPane.showMessageDialog(null, this.name + " empfängt Infos zum Spiel am " + (matchday + 1) + ". Spieltag, Gegner ist " + opponent
//				+ ", daheim ist " + homeoraway + ", spielnummer ist " + spielnummer);

		if (homeoraway == HOME)	homeaway[matchday] = true;
		else					homeaway[matchday] = false;

		this.daten[matchday][OPPONENT] = opponent;
	}

	public void setResult(int matchday, Ergebnis result) {
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

	public void setResult(int matchday, int tore, int gegentore) {
		if (0 > matchday || matchday >= daten.length) {
			JOptionPane.showMessageDialog(null, "Ergebnis konnte nicht gesetzt werden, da matchday die Grenzen verlässt.");
			return;
		}
		
		// JOptionPane.showMessageDialog(null, this.name + " empfängt Infos zum Spiel am " + (matchday + 1) + ". Spieltag, Ergebnis ist " + tore + "-" + gegentore);
		log(this.name + " empfängt Infos zum Spiel am " + (matchday + 1) + ". Spieltag, Ergebnis ist " + tore + "-" + gegentore);

		// "Entfernen" des alten Ergebnisses, wenn -1:-1 kommt, wird das Ergebnis zurueckgesetzt
		int oldtore = this.daten[matchday][GOALS];
		int oldgegentore = this.daten[matchday][CGOALS];
		int oldpoints = this.daten[matchday][POINTS];

		if (oldpoints > 0 || oldtore < oldgegentore) { // ansonsten steht kein Ergebnis drin (0:0 mit 0 Punkten)
			this.anzahl_tplus -= oldtore;
			this.anzahl_tminus -= oldgegentore;

			if (oldpoints == 3)			this.anzahl_g--;
			else if (oldpoints == 1)	this.anzahl_u--;
			else if (oldpoints == 0)	this.anzahl_v--;

		}
		
		if (tore != -1 && gegentore != -1) {
			// Speichern des neuen Ergebnisses
			this.daten[matchday][GOALS] = tore;
			this.daten[matchday][CGOALS] = gegentore;

			this.anzahl_tplus += tore;
			this.anzahl_tminus += gegentore;
			this.tdiff = this.anzahl_tplus - this.anzahl_tminus;

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
			this.anzahl_sp = this.anzahl_g + this.anzahl_u + this.anzahl_v;

			this.daten[matchday][POINTS] = points;
			this.punkte = 3 * this.anzahl_g + this.anzahl_u;
		}
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

	public void fromString(String data) {
		String[] daten = data.split(";");
		
		this.name = data.substring(0, data.indexOf(";"));
		this.gruendungsdatum = data.substring(data.indexOf(";") + 1, data.length() - 1);
		
		this.name = daten[0];
		this.gruendungsdatum = daten[1];
	}

	public String toString() {
		String data = this.name + ";";
		data += this.gruendungsdatum + ";";

		return data;
	}

}