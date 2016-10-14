package analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.Mannschaft;
import model.Spieler;

public class SaisonPerformance {
	private Spieler player;
	private HashMap<String, SpielPerformance> matchPerformances;
	
	public SaisonPerformance(Spieler player) {
		this.player = player;
		matchPerformances = new HashMap<>();
	}
	
	public Spieler getPlayer() {
		return player;
	}
	
	public SpielPerformance getMatchPerformance(int matchday) {
		if (!matchPerformances.containsKey(Mannschaft.getKey(matchday)))	return null;
		return matchPerformances.get(Mannschaft.getKey(matchday));
	}
	
	public ArrayList<SpielPerformance> asSortedList() {
		ArrayList<SpielPerformance> sortedMatchPerformances = new ArrayList<>();
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			SpielPerformance matchPerformance = matchPerformances.get(iter.next());
			if (!matchPerformance.hasData())	continue;
			
			int index = 0;
			for (SpielPerformance sorted : sortedMatchPerformances) {
				if (sorted.getMatchday() < matchPerformance.getMatchday())	index++;
			}
			
			sortedMatchPerformances.add(index, matchPerformance);
		}
		return sortedMatchPerformances;
	}
	
	public void addMatchPerformance(int matchday, SpielPerformance matchPerformance) {
		if (matchPerformance == null)	matchPerformances.remove(Mannschaft.getKey(matchday));
		else							matchPerformances.put(Mannschaft.getKey(matchday), matchPerformance);
	}
	
	public boolean hasData() {
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasData())	return true;
		}
		return false;
	}
	
	public int matchesPlayed() {
		int matchesPlayed = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasPlayed())	matchesPlayed++;
		}
		
		return matchesPlayed;
	}
	
	public int matchesStarted() {
		int matchesStarted = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasStarted())	matchesStarted++;
		}
		
		return matchesStarted;
	}
	
	public int matchesSubbedOn() {
		int matchesSubbedOn = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasBeenSubbedOn())	matchesSubbedOn++;
		}
		
		return matchesSubbedOn;
	}
	
	public int matchesSubbedOff() {
		int matchesSubbedOff = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasBeenSubbedOff())	matchesSubbedOff++;
		}
		
		return matchesSubbedOff;
	}
	
	public int minutesPlayed() {
		int minutesPlayed = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			minutesPlayed += matchPerformances.get(iter.next()).numberOfMinutesPlayed();
		}
		
		return minutesPlayed;
	}
	
	public int goalsScored() {
		int goalsScored = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			goalsScored += matchPerformances.get(iter.next()).numberOfGoals();
		}
		
		return goalsScored;
	}
	
	public int goalsAssisted() {
		int goalsAssisted = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			goalsAssisted += matchPerformances.get(iter.next()).numberOfAssists();
		}
		
		return goalsAssisted;
	}
	
	public int booked() {
		int booked = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasBeenBooked())	booked++;
		}
		
		return booked;
	}
	
	public int bookedTwice() {
		int bookedTwice = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasBeenBookedTwice())	bookedTwice++;
		}
		
		return bookedTwice;
	}
	
	public int sentOffStraight() {
		int sentOffStraight = 0;
		Iterator<String> iter = matchPerformances.keySet().iterator();
		while (iter.hasNext()) {
			if (matchPerformances.get(iter.next()).hasBeenSentOffStraight())	sentOffStraight++;
		}
		
		return sentOffStraight;
	}
}
