package model;

import static util.Utilities.*;

import java.util.ArrayList;

public class Ergebnis {

	private boolean finishedInRegularTime = false;
	private boolean finishedInExtraTime = false;
	private boolean amGruenenTisch = false;
	
	private int[] home = new int[4];
	private int[] away = new int[4];
	
	public static final int REGULAR = 1;
	public static final int EXTRATIME = 2;
	public static final int PENALTIES = 3;

	public Ergebnis(ArrayList<Tor> goals) {
		for (Tor goal : goals) {
			int time = 0;
			if (goal.getMinute().getRegularTime() > 120) {
				time = PENALTIES;
				finishedInRegularTime = false;
				finishedInExtraTime = false;
			} else if (goal.getMinute().getRegularTime() > 90) {
				time = EXTRATIME;
				finishedInRegularTime = false;
				finishedInExtraTime = true;
			} else {
				if (goal.getMinute().getRegularTime() > 45) 	time = REGULAR;
				finishedInRegularTime = true;
			}
			
			while (time <= 3) {
				if (goal.isFirstTeam())	home[time]++;
				else					away[time]++;
				time++;
			}
		}
	}
	
	/**
	 * A result that has been finished in 90 minutes
	 * @param home The number of goals that the left team has scored
	 * @param away The number of goals that the right team has scored
	 */
	public Ergebnis(int homeRegular, int awayRegular) {
		finishedInRegularTime = true;
		home[REGULAR] = homeRegular;
		away[REGULAR] = awayRegular;
		
		// falls Elfmeterschießen dazu kommt
		home[EXTRATIME] = home[REGULAR];
		away[EXTRATIME] = away[REGULAR];
		
		home[PENALTIES] = home[REGULAR];
		away[PENALTIES] = away[REGULAR];
	}
	
	public Ergebnis(String data) {
		try {
			if (data.indexOf("agT") != -1) {
				// >3:0 agT< / >0:2 agT<
				amGruenenTisch = true;
				
				String[] split = data.split(":|\\ ");
				home[REGULAR] = Integer.parseInt(split[0]);
				away[REGULAR] = Integer.parseInt(split[1]);
				
			} else if (data.indexOf("nE") != -1) {
				// Beispiel: >2:1nE (1:1,0:0)<
				
				String[] split = data.split("nE ");
				split[1] = split[1].substring(1, split[1].length() - 1);
				
				// teile[0] = 2:1
				String[] goals = split[0].split(":");
				home[PENALTIES] = Integer.parseInt(goals[0]);
				away[PENALTIES] = Integer.parseInt(goals[1]);
				
				// teile[1] = 1:1,0:0
				goals = split[1].split(",")[0].split(":");
				home[EXTRATIME] = Integer.parseInt(goals[0]);
				away[EXTRATIME] = Integer.parseInt(goals[1]);
				
				goals = split[1].split(",")[1].split(":");
				home[REGULAR] = Integer.parseInt(goals[0]);
				away[REGULAR] = Integer.parseInt(goals[1]);
				
			} else if (data.indexOf("nV") != -1) {
				// Beispiel: >3:2nV (2:2)<
				finishedInExtraTime = true;
				
				String[] split = data.split("nV ");
				split[1] = split[1].substring(1, split[1].length() - 1);
				
				// teile[0] = 3:2
				String[] goals = split[0].split(":");
				home[EXTRATIME] = Integer.parseInt(goals[0]);
				away[EXTRATIME] = Integer.parseInt(goals[1]);
				
				// teile[1] = 2:2
				goals = split[1].split(":");
				home[REGULAR] = Integer.parseInt(goals[0]);
				away[REGULAR] = Integer.parseInt(goals[1]);
				
				// falls Elfmeterschießen dazu kommt
				home[PENALTIES] = home[EXTRATIME];
				away[PENALTIES] = away[EXTRATIME];
				
			} else {
				// Beispiel: >2:1<
				finishedInRegularTime = true;
				
				String[] goals = data.split(":");
				home[REGULAR] = Integer.parseInt(goals[0]);
				away[REGULAR] = Integer.parseInt(goals[1]);
				
				// falls Elfmeterschießen dazu kommt
				home[EXTRATIME] = home[REGULAR];
				away[EXTRATIME] = away[REGULAR];
				
				home[PENALTIES] = home[REGULAR];
				away[PENALTIES] = away[REGULAR];
			}
			if (!validate()) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException iae) {
			home = new int[4];
			away = new int[4];
			log("Der übergebene String war formal korrekt, aber falsch.");
			
		} catch (Exception e) {
			home = new int[4];
			away = new int[4];
			log("Der übergebene String war formal inkorrekt.");
		}
	}
	
	public String getResult() {
		if (amGruenenTisch)	return home(REGULAR) + ":" + away(REGULAR);
		if (finishedInRegularTime)	return home(REGULAR) + ":" + away(REGULAR);
		if (finishedInExtraTime)	return home(EXTRATIME) + ":" + away(EXTRATIME);
		return home(PENALTIES) + ":" + away(PENALTIES);
	}
	
	public String getMore() {
		if (amGruenenTisch)	return "a.g.T.";
		if (finishedInRegularTime)	return "";
		if (finishedInExtraTime)	return "n.V.";
		return "n.E.";
	}
	
	public String getTooltipText() {
		if (amGruenenTisch)	return "am grünen Tisch";
		if (finishedInRegularTime)	return "";
		if (finishedInExtraTime)	return "nach Verlängerung";
		return "nach Elfmeterschießen";
	}
	
	public int home() {
		if (amGruenenTisch) {
			return home(REGULAR);
		}
		if (finishedInRegularTime) {
			return home(REGULAR);
		}
		if (finishedInExtraTime) {
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
		if (finishedInExtraTime) {
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
			if ((home[REGULAR] + away[REGULAR] != 3 && home[REGULAR] + away[REGULAR] != 2) || home[REGULAR] * away[REGULAR] != 0)	return false;
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
		
		if (finishedInExtraTime) {
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
		String toString = home(REGULAR) + ":" + away(REGULAR);
		
		if (amGruenenTisch)	return toString + " agT";
		
		if (finishedInRegularTime)	return toString;
		
		if (finishedInExtraTime) {
			toString = home(EXTRATIME) + ":" + away(EXTRATIME) + "nV (" + toString + ")";
			return toString;
		}
		
		toString = home(PENALTIES) + ":" + away(PENALTIES) + "nE (" + home(EXTRATIME) + ":" + away(EXTRATIME) + "," + toString + ")";
		return toString;
	}
}
