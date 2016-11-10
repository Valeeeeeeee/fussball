package analyse;

import model.Karte;
import model.Minute;
import model.Position;
import model.Spiel;
import model.Spieler;
import model.Tor;

public class SpielPerformance {
	
	private static final int bonusGoal = 40;
	private static final int bonusAssist = 10;
	private static final int bonusNoGoalConcededGK = 30;
	private static final int bonusNoGoalConcededDF = 20;
	private static final int malusOwnGoal = 20;
	private static final int malusBooked = 10;
	private static final int malusBookedTwice = 30;
	private static final int malusSentOffStraight = 50;
	
	private int minutesFullMatch = 90;
	
	private Spieler player;
	private Spiel match;
	private int matchday;
	private boolean home;
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
	private int numberOfOwnGoals;
	
	private boolean booked;
	private Minute minuteBooked;
	private boolean bookedTwice;
	private Minute minuteBookedTwice;
	private boolean sentOffStraight;
	private Minute minuteSentOffStraight;
	
	private int numberOfScoredGoalsFullMatch;
	private int numberOfScoredGoalsWhileOnPitch;
	private int numberOfConcededGoalsFullMatch;
	private int numberOfConcededGoalsWhileOnPitch;
	
	public SpielPerformance(Spieler player, Spiel match, boolean home, String opponentName, String result) {
		this.player = player;
		this.match = match;
		this.matchday = match.getMatchday();
		this.home = home;
		this.opponentName = opponentName;
		this.result = result;
	}
	
	public Spiel getMatch() {
		return match;
	}
	
	public int getMatchday() {
		return matchday;
	}
	
	public boolean atHome() {
		return home;
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
	
	public int numberOfOwnGoals() {
		return numberOfOwnGoals;
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
	
	public int numberOfScoredGoalsFullMatch() {
		return numberOfScoredGoalsFullMatch;
	}
	
	public int numberOfScoredGoalsWhileOnPitch() {
		return numberOfScoredGoalsWhileOnPitch;
	}
	
	public int numberOfConcededGoalsFullMatch() {
		return numberOfConcededGoalsFullMatch;
	}
	
	public int numberOfConcededGoalsWhileOnPitch() {
		return numberOfConcededGoalsWhileOnPitch;
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
	
	public void goal(Tor goal) {
		boolean forOwnTeam = goal.isFirstTeam() == home;
		if (forOwnTeam) {
			numberOfScoredGoalsFullMatch++;
			if (onPitch(goal.getMinute())) {
				numberOfScoredGoalsWhileOnPitch++;
				if (goal.isOwnGoal())	return;
				if (goal.isScorer(player.getSquadNumber()))	goalScored();
				if (goal.isAssister(player.getSquadNumber()))	goalAssisted();
			}
		} else {
			numberOfConcededGoalsFullMatch++;
			if (onPitch(goal.getMinute())) {
				numberOfConcededGoalsWhileOnPitch++;
				if (goal.isOwnGoal() && goal.isScorer(player.getSquadNumber()))	numberOfOwnGoals++;
			}
		}
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
	
	private boolean onPitch(Minute minute) {
		if (!played)	return false;
		if (subbedOn && minuteSubOn.isAfter(minute))	return false;
		if (subbedOff && minuteSubOff.isBefore(minute))	return false;
		if (bookedTwice && minuteBookedTwice.isBefore(minute))	return false;
		if (sentOffStraight && minuteSentOffStraight.isBefore(minute))	return false;
		return true;
	}
	
	public double getImpact() {
		double impact = (180 - numberOfMinutesPlayed) * (numberOfScoredGoalsWhileOnPitch - numberOfConcededGoalsWhileOnPitch);
		impact += bonusGoal * numberOfGoals + bonusAssist * numberOfAssists - malusOwnGoal * numberOfOwnGoals;
		if (bookedTwice)	impact -= malusBookedTwice;
		else if (booked)	impact -= malusBooked;
		if (sentOffStraight)	impact -= malusSentOffStraight;
		if (numberOfConcededGoalsWhileOnPitch == 0) {
			if (player.getPosition() == Position.ABWEHR)	impact += (bonusNoGoalConcededDF * numberOfMinutesPlayed / minutesFullMatch);
			else if (player.getPosition() == Position.TOR)	impact += (bonusNoGoalConcededGK * numberOfMinutesPlayed / minutesFullMatch);
		}
		return 0.1 * impact;
	}
}
