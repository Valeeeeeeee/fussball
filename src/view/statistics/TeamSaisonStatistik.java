package view.statistics;

import static util.Utilities.*;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dto.statistics.TeamSaisonStatistikBegegnungenDTO;
import dto.statistics.TeamSaisonStatistikDTO;
import dto.statistics.TeamSaisonStatistikSpielDTO;
import model.TeamBilanz;
import model.SerienTyp;
import model.SpielSerien;
import model.TeamFairplayBilanz;
import model.Uebersicht;
import model.Wettbewerb;

public class TeamSaisonStatistik extends JPanel {

	private static final long serialVersionUID = 5967789253217940676L;
	
	private static final int width = 550;
	private static final int standardHeight = 115;
	private int maxHeight = standardHeight;
	
	private int[] results = new int[] {15, 130, 0, 25, 190, 20};
	private int[] resultsV = new int[] {210, 130, 30, 25, 25, 20};
	private int[] series = new int[] {375, 155, 0, 25, 110, 20};
	private int[] seriesV = new int[] {500, 155, 0, 25, 20, 20};
	
	private Rectangle REC_LBLNOSTATS = new Rectangle(25, 35, 390, 25);
	private Rectangle REC_LBLMATCHESVAL = new Rectangle(10, 10, 25, 20);
	private Rectangle REC_LBLMATCHES = new Rectangle(40, 10, 50, 20);
	private Rectangle REC_LBLMATCHESWONVAL = new Rectangle(10, 35, 25, 20);
	private Rectangle REC_LBLMATCHESWON = new Rectangle(40, 35, 70, 20);
	private Rectangle REC_LBLMATCHESDRAWNVAL = new Rectangle(10, 60, 25, 20);
	private Rectangle REC_LBLMATCHESDRAWN = new Rectangle(40, 60, 95, 20);
	private Rectangle REC_LBLMATCHESLOSTVAL = new Rectangle(10, 85, 25, 20);
	private Rectangle REC_LBLMATCHESLOST = new Rectangle(40, 85, 60, 20);
	private Rectangle REC_LBLGOALSVAL = new Rectangle(215, 10, 25, 20);
	private Rectangle REC_LBLGOALS = new Rectangle(245, 10, 40, 20);
	private Rectangle REC_LBLGOALSCONCVAL = new Rectangle(215, 35, 25, 20);
	private Rectangle REC_LBLGOALSCONC = new Rectangle(245, 35, 70, 20);
	private Rectangle REC_LBLBOOKEDVAL = new Rectangle(390, 10, 25, 20);
	private Rectangle REC_LBLBOOKED = new Rectangle(420, 10, 85, 20);
	private Rectangle REC_LBLBOOKEDTWICEVAL = new Rectangle(390, 35, 25, 20);
	private Rectangle REC_LBLBOOKEDTWICE = new Rectangle(420, 35, 110, 20);
	private Rectangle REC_LBLREDCARDSVAL = new Rectangle(390, 60, 25, 20);
	private Rectangle REC_LBLREDCARDS = new Rectangle(420, 60, 80, 20);
	private Rectangle REC_LBLSTATSMORELESS = new Rectangle(445, 90, 80, 20);
	private Rectangle REC_LBLSERIEN = new Rectangle(360, 130, 150, 25);

	private JLabel jLblNoStatistics;
	private JLabel jLblMatchesPlayedVal;
	private JLabel jLblMatchesPlayed;
	private JLabel jLblMatchesWonVal;
	private JLabel jLblMatchesWon;
	private JLabel jLblMatchesDrawnVal;
	private JLabel jLblMatchesDrawn;
	private JLabel jLblMatchesLostVal;
	private JLabel jLblMatchesLost;
	private JLabel jLblGoalsScoredVal;
	private JLabel jLblGoalsScored;
	private JLabel jLblGoalsConcededVal;
	private JLabel jLblGoalsConceded;
	private JLabel jLblBookedVal;
	private JLabel jLblBooked;
	private JLabel jLblBookedTwiceVal;
	private JLabel jLblBookedTwice;
	private JLabel jLblRedCardsVal;
	private JLabel jLblRedCards;
	private JLabel jLblStatisticsMoreLess;
	private JLabel jLblSeries;
	private ArrayList<JLabel> jLblsResultsTeams;
	private ArrayList<ArrayList<JLabel>> jLblsResultsValues;
	private ArrayList<JLabel> jLblsSeries;
	private ArrayList<JLabel> jLblsSeriesValues;
	
	private boolean hasStatistics;
	private boolean showingMoreStatistics;
	
	private Uebersicht overview;

	private TeamSaisonStatistik(Uebersicht overview) {
		super();
		this.overview = overview;
		setLayout(null);
		setBackground(colorOverviewBackground);
		initialise();
	}
	
	private void initialise() {
		{
			jLblNoStatistics = new JLabel();
			add(jLblNoStatistics);
			jLblNoStatistics.setBounds(REC_LBLNOSTATS);
			jLblNoStatistics.setText("Für diesen Verein können keine Statistiken angezeigt werden.");
		}
		{
			jLblMatchesPlayedVal = new JLabel();
			add(jLblMatchesPlayedVal);
			jLblMatchesPlayedVal.setBounds(REC_LBLMATCHESVAL);
			alignCenter(jLblMatchesPlayedVal);
		}
		{
			jLblMatchesPlayed = new JLabel();
			add(jLblMatchesPlayed);
			jLblMatchesPlayed.setBounds(REC_LBLMATCHES);
			jLblMatchesPlayed.setText("Spiele");
		}
		{
			jLblMatchesWonVal = new JLabel();
			add(jLblMatchesWonVal);
			jLblMatchesWonVal.setBounds(REC_LBLMATCHESWONVAL);
			alignCenter(jLblMatchesWonVal);
		}
		{
			jLblMatchesWon = new JLabel();
			add(jLblMatchesWon);
			jLblMatchesWon.setBounds(REC_LBLMATCHESWON);
			jLblMatchesWon.setText("gewonnen");
		}
		{
			jLblMatchesDrawnVal = new JLabel();
			add(jLblMatchesDrawnVal);
			jLblMatchesDrawnVal.setBounds(REC_LBLMATCHESDRAWNVAL);
			alignCenter(jLblMatchesDrawnVal);
		}
		{
			jLblMatchesDrawn = new JLabel();
			add(jLblMatchesDrawn);
			jLblMatchesDrawn.setBounds(REC_LBLMATCHESDRAWN);
			jLblMatchesDrawn.setText("unentschieden");
		}
		{
			jLblMatchesLostVal = new JLabel();
			add(jLblMatchesLostVal);
			jLblMatchesLostVal.setBounds(REC_LBLMATCHESLOSTVAL);
			alignCenter(jLblMatchesLostVal);
		}
		{
			jLblMatchesLost = new JLabel();
			add(jLblMatchesLost);
			jLblMatchesLost.setBounds(REC_LBLMATCHESLOST);
			jLblMatchesLost.setText("verloren");
		}
		{
			jLblGoalsScoredVal = new JLabel();
			add(jLblGoalsScoredVal);
			jLblGoalsScoredVal.setBounds(REC_LBLGOALSVAL);
			alignCenter(jLblGoalsScoredVal);
		}
		{
			jLblGoalsScored = new JLabel();
			add(jLblGoalsScored);
			jLblGoalsScored.setBounds(REC_LBLGOALS);
			jLblGoalsScored.setText("Tore");
		}
		{
			jLblGoalsConcededVal = new JLabel();
			add(jLblGoalsConcededVal);
			jLblGoalsConcededVal.setBounds(REC_LBLGOALSCONCVAL);
			alignCenter(jLblGoalsConcededVal);
		}
		{
			jLblGoalsConceded = new JLabel();
			add(jLblGoalsConceded);
			jLblGoalsConceded.setBounds(REC_LBLGOALSCONC);
			jLblGoalsConceded.setText("Gegentore");
		}
		{
			jLblBookedVal = new JLabel();
			add(jLblBookedVal);
			jLblBookedVal.setBounds(REC_LBLBOOKEDVAL);
			alignCenter(jLblBookedVal);
		}
		{
			jLblBooked = new JLabel();
			add(jLblBooked);
			jLblBooked.setBounds(REC_LBLBOOKED);
			jLblBooked.setText("Gelbe Karten");
		}
		{
			jLblBookedTwiceVal = new JLabel();
			add(jLblBookedTwiceVal);
			jLblBookedTwiceVal.setBounds(REC_LBLBOOKEDTWICEVAL);
			alignCenter(jLblBookedTwiceVal);
		}
		{
			jLblBookedTwice = new JLabel();
			add(jLblBookedTwice);
			jLblBookedTwice.setBounds(REC_LBLBOOKEDTWICE);
			jLblBookedTwice.setText("Gelb-Rote Karten");
		}
		{
			jLblRedCardsVal = new JLabel();
			add(jLblRedCardsVal);
			jLblRedCardsVal.setBounds(REC_LBLREDCARDSVAL);
			alignCenter(jLblRedCardsVal);
		}
		{
			jLblRedCards = new JLabel();
			add(jLblRedCards);
			jLblRedCards.setBounds(REC_LBLREDCARDS);
			jLblRedCards.setText("Rote Karten");
		}
		{
			jLblStatisticsMoreLess = new JLabel();
			add(jLblStatisticsMoreLess);
			jLblStatisticsMoreLess.setBounds(REC_LBLSTATSMORELESS);
			alignRight(jLblStatisticsMoreLess);
			jLblStatisticsMoreLess.setText("mehr dazu >");
			jLblStatisticsMoreLess.setCursor(handCursor);
			jLblStatisticsMoreLess.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					toggleMoreLessStatistics();
				}
			});
		}
		{
			jLblSeries = new JLabel();
			add(jLblSeries);
			jLblSeries.setBounds(REC_LBLSERIEN);
			jLblSeries.setText("Meiste Spiele in Folge ...");
		}
	}
	
	private void showLabels() {
		jLblNoStatistics.setVisible(!hasStatistics);
		jLblMatchesPlayedVal.setVisible(hasStatistics);
		jLblMatchesPlayed.setVisible(hasStatistics);
		jLblMatchesWonVal.setVisible(hasStatistics);
		jLblMatchesWon.setVisible(hasStatistics);
		jLblMatchesDrawnVal.setVisible(hasStatistics);
		jLblMatchesDrawn.setVisible(hasStatistics);
		jLblMatchesLostVal.setVisible(hasStatistics);
		jLblMatchesLost.setVisible(hasStatistics);
		jLblGoalsScoredVal.setVisible(hasStatistics);
		jLblGoalsScored.setVisible(hasStatistics);
		jLblGoalsConcededVal.setVisible(hasStatistics);
		jLblGoalsConceded.setVisible(hasStatistics);
		jLblBookedVal.setVisible(hasStatistics);
		jLblBooked.setVisible(hasStatistics);
		jLblBookedTwiceVal.setVisible(hasStatistics);
		jLblBookedTwice.setVisible(hasStatistics);
		jLblRedCardsVal.setVisible(hasStatistics);
		jLblRedCards.setVisible(hasStatistics);
		jLblStatisticsMoreLess.setVisible(hasStatistics);
	}
	
	private void toggleMoreLessStatistics() {
		if (!hasStatistics)	return;
		showingMoreStatistics = !showingMoreStatistics;
		
		setSize();
		overview.showMoreLessStatistics();
	}
	
	private void setTeamRecordValues(TeamBilanz periodRecord) {
		jLblMatchesPlayedVal.setText(String.valueOf(periodRecord.getNumberOfMatches()));
		jLblMatchesWonVal.setText(String.valueOf(periodRecord.getNumberOfWins()));
		jLblMatchesDrawnVal.setText(String.valueOf(periodRecord.getNumberOfDraws()));
		jLblMatchesLostVal.setText(String.valueOf(periodRecord.getNumberOfLosses()));
		jLblGoalsScoredVal.setText(String.valueOf(periodRecord.getNumberOfGoalsScored()));
		jLblGoalsConcededVal.setText(String.valueOf(periodRecord.getNumberOfGoalsConceded()));
	}
	
	private void setTeamFairplayRecordValues(TeamFairplayBilanz fairplayData) {
		jLblBookedVal.setText(String.valueOf(fairplayData.getNumberOfYellowCards()));
		jLblBookedTwiceVal.setText(String.valueOf(fairplayData.getNumberOfSecondYellowCards()));
		jLblRedCardsVal.setText(String.valueOf(fairplayData.getNumberOfRedCards()));
	}
	
	private void showMatchups(Wettbewerb competition, ArrayList<TeamSaisonStatistikBegegnungenDTO> matchupDtos) {
		jLblsResultsTeams = new ArrayList<>();
		jLblsResultsValues = new ArrayList<>();
		
		int counter = 0;
		for (TeamSaisonStatistikBegegnungenDTO matchupDto : matchupDtos) {
			ArrayList<TeamSaisonStatistikSpielDTO> matchDtos = matchupDto.getMatches();
			if (competition.getNumberOfMatchesAgainstSameOpponent() == 0 && matchDtos.isEmpty())	continue;
			
			JLabel label = new JLabel();
			add(label);
			label.setBounds(results[STARTX], results[STARTY] + counter * results[GAPY], results[SIZEX], results[SIZEY]);
			label.setText(matchupDto.getNameOfOpponent());
			jLblsResultsTeams.add(label);
			
			jLblsResultsValues.add(new ArrayList<>());
			
			int counterMatches = 0;
			for (TeamSaisonStatistikSpielDTO matchDto : matchDtos) {
				label = new JLabel();
				add(label);
				label.setBounds(resultsV[STARTX] + counterMatches++ * resultsV[GAPX], resultsV[STARTY] + counter * resultsV[GAPY], resultsV[SIZEX], resultsV[SIZEY]);
				alignCenter(label);
				label.setOpaque(true);
				label.setBackground(matchDto.getBackground());
				label.setText(matchDto.getResult());
				jLblsResultsValues.get(counter).add(label);
			}
			
			counter++;
		}
	}
	
	private void showLongestSeries(SpielSerien longestSeries) {
		jLblsSeries = new ArrayList<>();
		jLblsSeriesValues = new ArrayList<>();
		
		int counter = 0;
		for (SerienTyp seriesType : SerienTyp.values()) {
			JLabel label = new JLabel();
			add(label);
			label.setBounds(series[STARTX], series[STARTY] + counter * series[GAPY], series[SIZEX], series[SIZEY]);
			label.setText("... " + seriesType.getDescription());
			jLblsSeries.add(label);
			
			label = new JLabel();
			add(label);
			label.setBounds(seriesV[STARTX], seriesV[STARTY] + counter++ * seriesV[GAPY], seriesV[SIZEX], seriesV[SIZEY]);
			alignCenter(label);
			label.setText(String.valueOf(longestSeries.getLongestSeriesOfMatchesWith(seriesType)));
			jLblsSeriesValues.add(label);
		}
	}
	
	public void setMaxHeight(int height) {
		maxHeight = height;
		setSize();
	}
	
	public void setSize() {
		jLblStatisticsMoreLess.setText(showingMoreStatistics ? "< weniger" : "mehr dazu >");
		setSize(width, hasStatistics && showingMoreStatistics ? maxHeight : standardHeight);		
	}
	
	public static TeamSaisonStatistik of(TeamSaisonStatistikDTO teamSeasonStatisticsDto, Uebersicht overview, Wettbewerb competition, boolean showingMoreStatistics) {
		TeamSaisonStatistik teamSeasonStatistics = new TeamSaisonStatistik(overview);
		
		teamSeasonStatistics.hasStatistics = teamSeasonStatisticsDto.hasStatistics();
		if (teamSeasonStatistics.hasStatistics) {
			teamSeasonStatistics.setTeamRecordValues(teamSeasonStatisticsDto.getTeamRecord());
			teamSeasonStatistics.setTeamFairplayRecordValues(teamSeasonStatisticsDto.getTeamFairplayRecord());
			teamSeasonStatistics.showMatchups(competition, teamSeasonStatisticsDto.getMatchups());
			teamSeasonStatistics.showLongestSeries(teamSeasonStatisticsDto.getLongestSeries());
		}
		
		teamSeasonStatistics.showingMoreStatistics = showingMoreStatistics;
		teamSeasonStatistics.setSize();
		teamSeasonStatistics.showLabels();
		
		return teamSeasonStatistics;
	}
}
