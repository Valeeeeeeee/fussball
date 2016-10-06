package analyse;

import model.Karte;
import model.Minute;
import model.Spiel;

public class SpielPerformance {
	
	private int minutesFullMatch = 90;
	
	private Spiel match;
	private int matchday;
	private String opponentName;
	private String result;
	
	private boolean played;
	private boolean started;
	private boolean subbedOn;
	private Minute minuteSubOn;
	private boolean subbedOff;
	private Minute minuteSubOff;
	private int numberOfMinutesPlayed;
	
	private boolean scored;
	private int numberOfGoals;
	private boolean assisted;
	private int numberOfAssists;
	
	private boolean booked;
	private Minute minuteBooked;
	private boolean bookedTwice;
	private Minute minuteBookedTwice;
	private boolean sentOffStraight;
	private Minute minuteSentOffStraight;
	
	public SpielPerformance(Spiel match, String opponentName, String result) {
		this.match = match;
		this.matchday = match.getMatchday();
		this.opponentName = opponentName;
		this.result = result;
	}
	
	public Spiel getMatch() {
		return match;
	}
	
	public int getMatchday() {
		return matchday;
	}
	
	public String getOpponentName() {
		return opponentName;
	}
	
	public String getResult() {
		return result;
	}
	
	public boolean hasPlayed() {
		return played;
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public boolean hasBeenSubbedOn() {
		return subbedOn;
	}
	
	public Minute minuteSubOn() {
		return minuteSubOn;
	}
	
	public boolean hasBeenSubbedOff() {
		return subbedOff;
	}
	
	public Minute minuteSubOff() {
		return minuteSubOff;
	}
	
	public int numberOfMinutesPlayed() {
		return numberOfMinutesPlayed;
	}
	
	public boolean hasScored() {
		return scored;
	}
	
	public int numberOfGoals() {
		return numberOfGoals;
	}
	
	public boolean hasAssisted() {
		return assisted;
	}
	
	public int numberOfAssists() {
		return numberOfAssists;
	}
	
	public boolean hasBeenBooked() {
		return booked;
	}
	
	public Minute minuteBooked() {
		return minuteBooked;
	}
	
	public boolean hasBeenBookedTwice() {
		return bookedTwice;
	}
	
	public Minute minuteBookedTwice() {
		return minuteBookedTwice;
	}
	
	public boolean hasBeenSentOffStraight() {
		return sentOffStraight;
	}
	
	public Minute minuteSentOffStraight() {
		return minuteSentOffStraight;
	}
	
	public boolean hasData() {
		return played || booked || sentOffStraight;
	}
	
	public void withExtraTime() {
		minutesFullMatch = 120;
	}
	
	public void started() {
		started = true;
		played = true;
		numberOfMinutesPlayed = minutesFullMatch;
	}
	
	public void subbedOn(Minute minute) {
		subbedOn = true;
		played = true;
		minuteSubOn = minute;
		int firstMinute = minuteSubOn.getRegularTime();
		if (firstMinute == 45 || firstMinute == 46 || firstMinute == 90)	firstMinute--;
		numberOfMinutesPlayed = minutesFullMatch - firstMinute;
	}
	
	public void subbedOff(Minute minute) {
		subbedOff = true;
		minuteSubOff = minute;
		int lastMinute = minuteSubOff.getRegularTime();
		if (lastMinute == 46)	lastMinute--;
		numberOfMinutesPlayed -= (minutesFullMatch - lastMinute);
	}
	
	public void goalScored() {
		scored = true;
		numberOfGoals++;
	}
	
	public void goalAssisted() {
		assisted = true;
		numberOfAssists++;
	}
	
	public void booked(Karte booking) {
		if (booking.isYellowCard()) {
			if (booked) {
				bookedTwice = true;
				minuteBookedTwice = booking.getMinute();
			} else {
				booked = true;
				minuteBooked = booking.getMinute();
			}
		} else {
			sentOffStraight = true;
			minuteSentOffStraight = booking.getMinute();
		}
	}
}
