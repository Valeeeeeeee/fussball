package model;

import static util.Utilities.*;

public class Ergebnis {

	private boolean finishedInRegularTime = false;
	private boolean finishedInOvertime = false;
	
	private int[] home = new int[4];
	private int[] away = new int[4];
	
	final int REGULAR = 1;
	final int OVERTIME = 2;
	final int PENALTIES = 3;

	public Ergebnis() {
		
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
			if (daten.indexOf("nE") != -1) {
				// Beispiel: >2:1nE (1:1,0:0)<
				
				String[] teile = daten.split("nE ");
				teile[1] = teile[1].substring(1, teile[1].length() - 1);
				
				// teile[0] = 2:1
				String[] tore = teile[0].split(":");
				home[PENALTIES] = Integer.parseInt(tore[0]);
				away[PENALTIES] = Integer.parseInt(tore[1]);
				
				// teile[1] = 1:1,0:0
				tore = teile[1].split(",")[0].split(":");
				home[OVERTIME] = Integer.parseInt(tore[0]);
				away[OVERTIME] = Integer.parseInt(tore[1]);
				
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
				home[OVERTIME] = Integer.parseInt(tore[0]);
				away[OVERTIME] = Integer.parseInt(tore[1]);
				
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
		} catch (Exception e) {
			home = new int[4];
			away = new int[4];
			log("Der uebergebene String war inkorrekt.");
		}
	}
	

	public int home() {
		if (finishedInRegularTime) {
			return home(REGULAR);
		}
		if (finishedInOvertime) {
			return home(OVERTIME);
		}
		return home(PENALTIES);
	}
	
	public int home(int i) {
		return home[i];
	}
	
	public int away() {
		if (finishedInRegularTime) {
			return away(REGULAR);
		}
		if (finishedInOvertime) {
			return away(OVERTIME);
		}
		return away(PENALTIES);
	}
	
	public int away(int i) {
		return away[i];
	}
	
	
	public String toString() {
		String data = home(REGULAR) + ":" + away(REGULAR);
		
		if (finishedInRegularTime)	return data;
		
		if (finishedInOvertime) {
			data = home(OVERTIME) + ":" + away(OVERTIME) + "nV (" + data + ")";
			return data;
		}
		
		data = home(PENALTIES) + ":" + away(PENALTIES) + "nE (" + home(OVERTIME) + ":" + away(OVERTIME) + "," + data + ")";
		return data;
	}
	
}
