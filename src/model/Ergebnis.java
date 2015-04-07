package model;

import static util.Utilities.*;

public class Ergebnis {

	private boolean finishedInRegularTime = false;
	private boolean finishedInOvertime = false;
	private boolean amGruenenTisch = false;
	
	private int[] home = new int[4];
	private int[] away = new int[4];
	
	public static final int REGULAR = 1;
	public static final int EXTRATIME = 2;
	public static final int PENALTIES = 3;

	public Ergebnis(Ergebnis ergebnis, Tor tor) {
		finishedInRegularTime = ergebnis.finishedInRegularTime;
		finishedInOvertime = ergebnis.finishedInOvertime;
		amGruenenTisch = ergebnis.amGruenenTisch;
		home = ergebnis.home;
		away = ergebnis.away;
		
		// TODO not unconditionally
		if (tor.isFirstTeam())	home[REGULAR]++;
		else					away[REGULAR]++;
	}
	
	/**
	 * A result that has been finished in 90 minutes
	 * @param home The number of goals that the left team has scored
	 * @param away The number of goals that the right team has scored
	 */
	public Ergebnis(int home, int away) {
		finishedInRegularTime = true;
		this.home[REGULAR] = home;
		this.away[REGULAR] = away;
	}
	
	public Ergebnis(String daten) {
		try {
			if (daten.indexOf("agT") != -1) {
				// >3:0 agT<
				amGruenenTisch = true;
				
				String[] teile = daten.split(":|\\ ");
				home[REGULAR] = Integer.parseInt(teile[0]);
				away[REGULAR] = Integer.parseInt(teile[1]);
				
			} else if (daten.indexOf("nE") != -1) {
				// Beispiel: >2:1nE (1:1,0:0)<
				
				String[] teile = daten.split("nE ");
				teile[1] = teile[1].substring(1, teile[1].length() - 1);
				
				// teile[0] = 2:1
				String[] tore = teile[0].split(":");
				home[PENALTIES] = Integer.parseInt(tore[0]);
				away[PENALTIES] = Integer.parseInt(tore[1]);
				
				// teile[1] = 1:1,0:0
				tore = teile[1].split(",")[0].split(":");
				home[EXTRATIME] = Integer.parseInt(tore[0]);
				away[EXTRATIME] = Integer.parseInt(tore[1]);
				
				tore = teile[1].split(",")[1].split(":");
				home[REGULAR] = Integer.parseInt(tore[0]);
				away[REGULAR] = Integer.parseInt(tore[1]);
				
			} else if (daten.indexOf("nV") != -1) {
				// Beispiel: >3:2nV (2:2)<
				finishedInOvertime = true;
				
				String[] teile = daten.split("nV ");
				teile[1] = teile[1].substring(1, teile[1].length() - 1);
				
				// teile[0] = 3:2
				String[] tore = teile[0].split(":");
				home[EXTRATIME] = Integer.parseInt(tore[0]);
				away[EXTRATIME] = Integer.parseInt(tore[1]);
				
				// teile[1] = 2:2
				tore = teile[1].split(":");
				home[REGULAR] = Integer.parseInt(tore[0]);
				away[REGULAR] = Integer.parseInt(tore[1]);
				
			} else {
				// Beispiel: >2:1<
				finishedInRegularTime = true;
				
				String[] tore = daten.split(":");
				home[REGULAR] = Integer.parseInt(tore[0]);
				away[REGULAR] = Integer.parseInt(tore[1]);
			}
			if (!validate()) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException iae) {
			home = new int[4];
			away = new int[4];
			log("Der uebergebene String war formal korrekt, aber falsch.");
			
		} catch (Exception e) {
			home = new int[4];
			away = new int[4];
			log("Der uebergebene String war formal inkorrekt.");
		}
	}
	

	public int home() {
		if (amGruenenTisch) {
			return home(REGULAR);
		}
		if (finishedInRegularTime) {
			return home(REGULAR);
		}
		if (finishedInOvertime) {
			return home(EXTRATIME);
		}
		return home(PENALTIES);
	}
	
	public int home(int i) {
		return home[i];
	}
	
	public int away() {
		if (amGruenenTisch) {
			return away(REGULAR);
		}
		if (finishedInRegularTime) {
			return away(REGULAR);
		}
		if (finishedInOvertime) {
			return away(EXTRATIME);
		}
		return away(PENALTIES);
	}
	
	public int away(int i) {
		return away[i];
	}
	
	private boolean validate() {
		// checks if penalties and overtime values are higher than the previous ones
		
		// Glossary:
//		HT			half time				45min
//		RT			regular time			90min
//		ET			extra time				120min
//		PS			penalty shootout		120min + PS
//		on agg.		on aggregate			two matches together
		
		
//		TODO check if match was finished after ET, scores must vary from RT scores
//			--> if they are equal a PS would be required
//		e.g. 3:1nV (2:0)	correct
//		e.g. 2:0nV (2:0)	error		(ET with 2:0 after RT means first match was 2:0, so after ET on agg. 2:2 with equal away goals --> PS)
		
//		TODO check if standings after RT/ET are not at equality:
//			--> if regular result and overtime result don't vary: correct, else error
//		e.g. 6:5nE (1:3,1:3) correct	(ET with 1:3 after RT means first match was 3:1, so after ET as well --> penalty shootout)
//		e.g. 6:5nE (2:4,1:3) error		(ET with 1:3 after RT means first match was 3:1, so after ET on agg. 5:5 but more away goals --> no PS)
		
//		TODO check if the match is decided through PS:
//			--> if the leading team at RT and ET varies: error
//		e.g. 7:6nE (4:2,1:2) error		(ET with 1:2 after RT means first match was 2:1, so after ET on agg. 6:3 --> no PS)
//		e.g. 7:5nE (3:3,2:2) correct	(might also be incorrect, if first match was 2:2, but most likely in a tournament with no second leg --> PS)
		
//		TODO optional: check if PS result is possible:
//		e.g. 11:4nE (3:3,1:1) error		(8:1 iE not possible)
//		e.g. 7:4nE (2:2, 0:0) error		(5:2 iE not possible)
//		e.g. 6:3nE (2:2, 0:0) correct	(4:1 iE possible)
		
		// if standings after RT are lower than at HT
		if (home[REGULAR] < home[0] || away[REGULAR] < away[0]) {
			log("Falsches Ergebnis.");
			return false;
		}
		
		// if the match was decided agT and the result is 3:0 or 0:3
		if (amGruenenTisch) {
			if (home[REGULAR] + away[REGULAR] != 3 || home[REGULAR] * away[REGULAR] != 0)	return false;
			return true;
		}
		
		if (finishedInRegularTime) {
			return true;
		}
		
		// if standings after ET are lower than after RT
		if (home[EXTRATIME] < home[REGULAR] || away[EXTRATIME] < away[REGULAR]) {
			log("Falsches Ergebnis.");
			return false;
		}
		
		if (finishedInOvertime) {
			return true;
		}
		
		// if standings after PS are lower than after ET
		if (home[PENALTIES] < home[EXTRATIME] || away[PENALTIES] < away[EXTRATIME]) {
			log("Falsches Ergebnis.");
			return false;
		}
		
		return true;
	}
	
	public String toString() {
		String data = home(REGULAR) + ":" + away(REGULAR);
		
		if (amGruenenTisch)	return data + " agT";
		
		if (finishedInRegularTime)	return data;
		
		if (finishedInOvertime) {
			data = home(EXTRATIME) + ":" + away(EXTRATIME) + "nV (" + data + ")";
			return data;
		}
		
		data = home(PENALTIES) + ":" + away(PENALTIES) + "nE (" + home(EXTRATIME) + ":" + away(EXTRATIME) + "," + data + ")";
		return data;
	}
	
}
