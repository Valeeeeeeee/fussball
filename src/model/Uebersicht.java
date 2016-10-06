package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import analyse.SaisonPerformance;

import static util.Utilities.*;

public class Uebersicht extends JPanel {
	private static final long serialVersionUID = 5249833173928569240L;

	private Rectangle REC_BTNBACK = new Rectangle(5, 5, 100, 25);
	private Rectangle REC_LBLSORTBYDATE = new Rectangle(140, 10, 145, 15);
	private Rectangle REC_LBLONLYHOME = new Rectangle(310, 10, 105, 15);
	private Rectangle REC_LBLONLYAWAY = new Rectangle(440, 10, 130, 15);
	
	// Spielplan
	private Rectangle REC_SPPLPNL;
	
	// Informationen
	private Rectangle REC_INFPNL = new Rectangle(595, 35, 550, 80);
	private Rectangle REC_LBLNAME = new Rectangle(90, 10, 370, 30);
	private Rectangle REC_LBLGRDATUM = new Rectangle(200, 40, 150, 30);
	
	// Statistiken
	private Rectangle REC_STATSPNL = new Rectangle(595, 120, 550, 115);
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
	
	private int[] results = new int[] {15, 130, 0, 25, 190, 20};
	private int[] resultsV = new int[] {210, 130, 30, 25, 25, 20};
	private Rectangle REC_LBLSERIEN = new Rectangle(360, 130, 150, 25);
	private int[] series = new int[] {375, 155, 0, 25, 110, 20};
	private int[] seriesV = new int[] {500, 155, 0, 25, 20, 20};
	
	private Rectangle REC_TABLEPNL = new Rectangle(595, 240, 550, 290);
	
	private Rectangle REC_LBLAVERAGEAGE = new Rectangle(20, 135, 125, 20);
	private Rectangle REC_LBLAVERAGEAGEVAL = new Rectangle(20, 160, 80, 20);
	private Rectangle REC_LBLNODATA = new Rectangle(25, 35, 370, 25);
	private Rectangle REC_BTNADDPLAYER = new Rectangle(235, 5, 120, 25);
	private Rectangle REC_LBLKADERMORELESS = new Rectangle(445, 5, 80, 25);
	private Rectangle REC_LBLPLAYERS = new Rectangle(280, 45, 80, 20);
	private Rectangle REC_LBLUSEDPLAYERS = new Rectangle(280, 70, 140, 20);
	
	private Color cbackground = new Color(255, 128, 128);
	private Color cgreyedout = new Color(128, 128, 128);
	private Color coptions = new Color(0, 192, 255);
	private String[] headerStrings = {"Pl.", "Verein", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
	private String[] positions = new String[] {"Tor", "Abwehr", "Mittelfeld", "Sturm"};
	private String[] positionsPlayer = new String[] {"Torhüter", "Verteidiger", "Mittelfeldspieler", "Stürmer"};
	
	private static final int NUMBER_OF_SERIES = 9;
	private String[] seriesStrings = new String[] {"gewonnen", "unentschieden", "verloren", "unbesiegt", "sieglos", "mit Tor", "ohne Tor", "mit Gegentor", "ohne Gegentor"};
	
	private JButton jBtnBack;
	
	private JPanel jPnlMatches;
	private JLabel jLblShowOnlyHome;
	private JLabel jLblShowOnlyAway;
	private JLabel jLblSortByDate;
	private JLabel[][] jLblsMatches;
	
	private JPanel jPnlInformation;
	private JLabel jLblTeamName;
	private JLabel jLblFoundingDate;
	
	private JPanel jPnlStatistics;
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
	private JLabel[] jLblsSeries;
	private JLabel[] jLblsSeriesValues;
	private JLabel[] jLblsResultsTeams;
	private JLabel[][] jLblsResultsValues;
	
	private JPanel jPnlTableExcerpt;
	private JLabel[] jLblsTableHeader;
	private JLabel[][] jLblsTableExcerpt;
	private JLabel jLblBackground;
	private Tabellenverlauf rankingHistory;
	
	private JScrollPane jSPKader;
	private JPanel jPnlKader;
	private JLabel[][] jLblsKader;
	private JLabel[] jLblsKaderDescr;
	private JLabel[] jLblsPositionVal;
	private JLabel[] jLblsPosition;
	private JLabel jLblAverageAge;
	private JLabel jLblAverageAgeVal;
	private JLabel jLblNoData;
	private JLabel jLblNumberOfPlayers;
	private JLabel jLblNumberOfUsedPlayers;
	private JButton jBtnAddPlayer;
	private JLabel jLblKaderMoreLess;

	private SpielerInformationen playerInformation;
	
	private static final int MATCHDAY = 0;
	private static final int DATE = 1;
	private static final int TEAMHOME = 2;
	private static final int GOALSHOME = 3;
	private static final int TRENNZEICHEN = 4;
	private static final int GOALSAWAY = 5;
	private static final int TEAMAWAY = 6;
	private static final int NUMBEROFFIELDSSPPL = 7;
	
	private static final int SQUADNUMBER = 0;
	private static final int NAMES = 1;
	private static final int BIRTHDATE = 2;
	private static final int MATCHES = 3;
	private static final int GOALS = 4;
	private static final int ASSISTS = 5;
	private static final int BOOKINGS = 6;
	private static final int BOOKEDTWICE = 7;
	private static final int REDCARDS = 8;
	private static final int NUMBEROFFIELDSKAD = 9;
	
	/** The left and right margin for matches */
	private int marginMatches = 5;
	private int marginX = 5;
	private int marginY = 5;
	private int[] widthes = {20, 120, 185, 16, 5, 16, 185};
	private int height = 15;
	private int[] gapx = {5, 5, 9, 0, 0, 9, 0};
	private int gapy = 5;
	private int middlegapy = 15;
	
	private int teStartX = 10;
	private int teStartY = 5;
	private int[] teWidthes = {25, 210, 25, 25, 25, 25, 30, 30, 30, 30};
	private int teHeight = 15;
	private int[] teGapX = {10, 10, 10, 5, 5, 10, 5, 10, 10, 0};
	private int teGapY = 5;
	
	private int kaderSTARTX = 20;
	private int kaderSTARTY = 20;
	private int[] kaderWIDTHES = {20, 200, 75, 25, 25, 25, 25, 25, 25};
	private int kaderHEIGHT = 15;
	private int[] kaderGAPX = {5, 5, 25, 5, 0, 0, 0, 0, 0};
	private int kaderGAPY = 3;
	private int kaderWIDTH = 531; // scroll bar width: 19
	
	private int standardHeightKader = 190;
	private int standardHeightKaderNoPlayers = 100;
	
	private Wettbewerb competition;
	
	private boolean hasGrDatum;
	
	private int teamID;
	private Mannschaft team;
	private Mannschaft[] teams;
	private int[] opponents;
	private boolean[] homeaway;
	private ArrayList<Integer> matchdayOrder = new ArrayList<>();
	
	private boolean canHaveKader;
	private ArrayList<Spieler> eligiblePlayers;
	private ArrayList<Spieler> ineligiblePlayers;
	private int numberOfMatchdays;
	private int numberOfEligiblePlayers;
	private int numberOfIneligiblePlayers;
	private int numberOfPositions;
	private boolean showingMoreStats;
	private boolean showingMoreKader;

	public Uebersicht(LigaSaison season) {
		super();
		competition = season;
		hasGrDatum = true;
		initGUI();
	}
	
	public Uebersicht(Gruppe group) {
		super();
		competition = group;
		hasGrDatum = false;
		initGUI();
	}
	
	public Uebersicht(Turnier tournament) {
		super();
		hasGrDatum = false;
//		initGUI();
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);
			
			teams = competition.getTeams();
			numberOfMatchdays = competition.getNumberOfMatchdays();
			numberOfPositions = 4;
			if (competition.getNumberOfMatchesAgainstSameOpponent() == 1)	middlegapy = 0;
			
			jLblsMatches = new JLabel[numberOfMatchdays][NUMBEROFFIELDSSPPL];
			opponents = new int[numberOfMatchdays];
			homeaway = new boolean[numberOfMatchdays];
			jLblsSeries = new JLabel[NUMBER_OF_SERIES];
			jLblsSeriesValues = new JLabel[NUMBER_OF_SERIES];
			jLblsResultsTeams = new JLabel[teams.length];
			jLblsResultsValues = new JLabel[teams.length][competition.getNumberOfMatchesAgainstSameOpponent()];
			jLblsTableHeader = new JLabel[10];
			jLblsTableExcerpt = new JLabel[5][10];
			jLblsPositionVal = new JLabel[numberOfPositions];
			jLblsPosition = new JLabel[numberOfPositions];
			
			for (int i = 0; i < numberOfMatchdays; i++) {
				matchdayOrder.add(i);
			}

			int sumofwidthes = 2 * marginMatches;
			for (int i = 0; i < widthes.length; i++) {
				sumofwidthes += widthes[i];
			}
			for (int i = 0; i < gapx.length; i++) {
				sumofwidthes += gapx[i];
			}
			REC_SPPLPNL = new Rectangle(marginX, marginY + 30, sumofwidthes, 2 * marginMatches + numberOfMatchdays * height + (numberOfMatchdays - 1) * gapy + middlegapy);
			
			{
				playerInformation = new SpielerInformationen(this, competition);
				playerInformation.setLocationRelativeTo(null);
			}
			
			{
				jBtnBack = new JButton();
				this.add(jBtnBack);
				jBtnBack.setBounds(REC_BTNBACK);
				jBtnBack.setText("zurück");
				jBtnBack.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Start.getInstance().jBtnBackActionPerformed();
					}
				});
			}
			{
				jLblSortByDate = new JLabel();
				this.add(jLblSortByDate);
				alignCenter(jLblSortByDate);
				jLblSortByDate.setBounds(REC_LBLSORTBYDATE);
				jLblSortByDate.setCursor(handCursor);
				jLblSortByDate.setText("nach Datum sortieren");
				jLblSortByDate.setOpaque(true);
				jLblSortByDate.setBackground(coptions);
				jLblSortByDate.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent evt) {
						sortByDate();
					}
					
					public void mouseExited(MouseEvent evt) {
						sortByMatchday();
					}
				});
			}
			{
				jLblShowOnlyHome = new JLabel();
				this.add(jLblShowOnlyHome);
				alignCenter(jLblShowOnlyHome);
				jLblShowOnlyHome.setBounds(REC_LBLONLYHOME);
				jLblShowOnlyHome.setCursor(handCursor);
				jLblShowOnlyHome.setText("nur Heimspiele");
				jLblShowOnlyHome.setOpaque(true);
				jLblShowOnlyHome.setBackground(coptions);
				jLblShowOnlyHome.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent evt) {
						showOnlyHomeMatches();
					}
					
					public void mouseExited(MouseEvent evt) {
						showAllMatches();
					}
				});
			}
			{
				jLblShowOnlyAway = new JLabel();
				this.add(jLblShowOnlyAway);
				alignCenter(jLblShowOnlyAway);
				jLblShowOnlyAway.setBounds(REC_LBLONLYAWAY);
				jLblShowOnlyAway.setCursor(handCursor);
				jLblShowOnlyAway.setText("nur Auswärtsspiele");
				jLblShowOnlyAway.setOpaque(true);
				jLblShowOnlyAway.setBackground(coptions);
				jLblShowOnlyAway.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent evt) {
						showOnlyAwayMatches();
					}
					
					public void mouseExited(MouseEvent evt) {
						showAllMatches();
					}
				});
			}
			
			{
				jPnlMatches = new JPanel();
				this.add(jPnlMatches);
				jPnlMatches.setLayout(null);
				jPnlMatches.setBounds(REC_SPPLPNL);
				jPnlMatches.setOpaque(true);
				jPnlMatches.setBackground(cbackground);
			}
			
			for (int i = 0; i < jLblsMatches.length; i++) {
				final int x = i;
				int diff = 0;
				
				for (int j = 0; j < jLblsMatches[i].length; j++) {
					jLblsMatches[i][j] = new JLabel();
					jPnlMatches.add(jLblsMatches[i][j]);
					jLblsMatches[i][j].setBounds(marginMatches + diff, marginMatches + i * (height + gapy) + (i / (jLblsMatches.length / 2)) * middlegapy, widthes[j], height);
					
					diff += widthes[j] + gapx[j];
				}
				jLblsMatches[i][TEAMHOME].setCursor(handCursor);
				jLblsMatches[i][TEAMAWAY].setCursor(handCursor);
				jLblsMatches[i][MATCHDAY].setCursor(handCursor);
				
				alignCenter(jLblsMatches[i][MATCHDAY]);
				alignCenter(jLblsMatches[i][DATE]);
				alignRight(jLblsMatches[i][TEAMHOME]);
				alignCenter(jLblsMatches[i][GOALSHOME]);
				alignCenter(jLblsMatches[i][TRENNZEICHEN]);
				alignCenter(jLblsMatches[i][GOALSAWAY]);
				alignLeft(jLblsMatches[i][TEAMAWAY]);
				
				jLblsMatches[i][TEAMHOME].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showTeam(x, true);
					}
				});
				jLblsMatches[i][TEAMAWAY].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showTeam(x, false);
					}
				});
				jLblsMatches[i][MATCHDAY].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showMatchday(x);
					}
				});
			}
			{
				jPnlInformation = new JPanel();
				this.add(jPnlInformation);
				jPnlInformation.setLayout(null);
				jPnlInformation.setBounds(REC_INFPNL);
				jPnlInformation.setOpaque(true);
				jPnlInformation.setBackground(cbackground);
			}
			{
				jLblTeamName = new JLabel();
				jPnlInformation.add(jLblTeamName);
				jLblTeamName.setBounds(REC_LBLNAME);
				jLblTeamName.setFont(new Font("Dialog", 0, 24));
				alignCenter(jLblTeamName);
			}
			if (hasGrDatum) {
				jLblFoundingDate = new JLabel();
				jPnlInformation.add(jLblFoundingDate);
				jLblFoundingDate.setBounds(REC_LBLGRDATUM);
				jLblFoundingDate.setFont(new Font("Dialog", 0, 12));
				alignCenter(jLblFoundingDate);
			}
			{
				jPnlStatistics = new JPanel();
				this.add(jPnlStatistics);
				jPnlStatistics.setLayout(null);
				jPnlStatistics.setOpaque(true);
				jPnlStatistics.setBackground(cbackground);
				jPnlStatistics.setBounds(REC_STATSPNL);
			}
			{
				jLblMatchesPlayedVal = new JLabel();
				jPnlStatistics.add(jLblMatchesPlayedVal);
				jLblMatchesPlayedVal.setBounds(REC_LBLMATCHESVAL);
				alignCenter(jLblMatchesPlayedVal);
			}
			{
				jLblMatchesPlayed = new JLabel();
				jPnlStatistics.add(jLblMatchesPlayed);
				jLblMatchesPlayed.setBounds(REC_LBLMATCHES);
				jLblMatchesPlayed.setText("Spiele");
			}
			{
				jLblMatchesWonVal = new JLabel();
				jPnlStatistics.add(jLblMatchesWonVal);
				jLblMatchesWonVal.setBounds(REC_LBLMATCHESWONVAL);
				alignCenter(jLblMatchesWonVal);
			}
			{
				jLblMatchesWon = new JLabel();
				jPnlStatistics.add(jLblMatchesWon);
				jLblMatchesWon.setBounds(REC_LBLMATCHESWON);
				jLblMatchesWon.setText("gewonnen");
			}
			{
				jLblMatchesDrawnVal = new JLabel();
				jPnlStatistics.add(jLblMatchesDrawnVal);
				jLblMatchesDrawnVal.setBounds(REC_LBLMATCHESDRAWNVAL);
				alignCenter(jLblMatchesDrawnVal);
			}
			{
				jLblMatchesDrawn = new JLabel();
				jPnlStatistics.add(jLblMatchesDrawn);
				jLblMatchesDrawn.setBounds(REC_LBLMATCHESDRAWN);
				jLblMatchesDrawn.setText("unentschieden");
			}
			{
				jLblMatchesLostVal = new JLabel();
				jPnlStatistics.add(jLblMatchesLostVal);
				jLblMatchesLostVal.setBounds(REC_LBLMATCHESLOSTVAL);
				alignCenter(jLblMatchesLostVal);
			}
			{
				jLblMatchesLost = new JLabel();
				jPnlStatistics.add(jLblMatchesLost);
				jLblMatchesLost.setBounds(REC_LBLMATCHESLOST);
				jLblMatchesLost.setText("verloren");
			}
			{
				jLblGoalsScoredVal = new JLabel();
				jPnlStatistics.add(jLblGoalsScoredVal);
				jLblGoalsScoredVal.setBounds(REC_LBLGOALSVAL);
				alignCenter(jLblGoalsScoredVal);
			}
			{
				jLblGoalsScored = new JLabel();
				jPnlStatistics.add(jLblGoalsScored);
				jLblGoalsScored.setBounds(REC_LBLGOALS);
				jLblGoalsScored.setText("Tore");
			}
			{
				jLblGoalsConcededVal = new JLabel();
				jPnlStatistics.add(jLblGoalsConcededVal);
				jLblGoalsConcededVal.setBounds(REC_LBLGOALSCONCVAL);
				alignCenter(jLblGoalsConcededVal);
			}
			{
				jLblGoalsConceded = new JLabel();
				jPnlStatistics.add(jLblGoalsConceded);
				jLblGoalsConceded.setBounds(REC_LBLGOALSCONC);
				jLblGoalsConceded.setText("Gegentore");
			}
			{
				jLblBookedVal = new JLabel();
				jPnlStatistics.add(jLblBookedVal);
				jLblBookedVal.setBounds(REC_LBLBOOKEDVAL);
				alignCenter(jLblBookedVal);
			}
			{
				jLblBooked = new JLabel();
				jPnlStatistics.add(jLblBooked);
				jLblBooked.setBounds(REC_LBLBOOKED);
				jLblBooked.setText("Gelbe Karten");
			}
			{
				jLblBookedTwiceVal = new JLabel();
				jPnlStatistics.add(jLblBookedTwiceVal);
				jLblBookedTwiceVal.setBounds(REC_LBLBOOKEDTWICEVAL);
				alignCenter(jLblBookedTwiceVal);
			}
			{
				jLblBookedTwice = new JLabel();
				jPnlStatistics.add(jLblBookedTwice);
				jLblBookedTwice.setBounds(REC_LBLBOOKEDTWICE);
				jLblBookedTwice.setText("Gelb-Rote Karten");
			}
			{
				jLblRedCardsVal = new JLabel();
				jPnlStatistics.add(jLblRedCardsVal);
				jLblRedCardsVal.setBounds(REC_LBLREDCARDSVAL);
				alignCenter(jLblRedCardsVal);
			}
			{
				jLblRedCards = new JLabel();
				jPnlStatistics.add(jLblRedCards);
				jLblRedCards.setBounds(REC_LBLREDCARDS);
				jLblRedCards.setText("Rote Karten");
			}
			{
				jLblStatisticsMoreLess = new JLabel();
				jPnlStatistics.add(jLblStatisticsMoreLess);
				jLblStatisticsMoreLess.setBounds(REC_LBLSTATSMORELESS);
				alignRight(jLblStatisticsMoreLess);
				jLblStatisticsMoreLess.setText("mehr dazu >");
				jLblStatisticsMoreLess.setCursor(handCursor);
				jLblStatisticsMoreLess.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showMoreLessStatistics();
					}
				});
			}
			{
				jLblSeries = new JLabel();
				jPnlStatistics.add(jLblSeries);
				jLblSeries.setBounds(REC_LBLSERIEN);
				jLblSeries.setText("Meiste Spiele in Folge ...");
			}
			for (int i = 0; i < NUMBER_OF_SERIES; i++) {
				jLblsSeries[i] = new JLabel();
				jPnlStatistics.add(jLblsSeries[i]);
				jLblsSeries[i].setBounds(series[STARTX], series[STARTY] + i * series[GAPY], series[SIZEX], series[SIZEY]);
				jLblsSeries[i].setText("... " + seriesStrings[i]);
				
				jLblsSeriesValues[i] = new JLabel();
				jPnlStatistics.add(jLblsSeriesValues[i]);
				jLblsSeriesValues[i].setBounds(seriesV[STARTX], seriesV[STARTY] + i * seriesV[GAPY], seriesV[SIZEX], seriesV[SIZEY]);
				alignCenter(jLblsSeriesValues[i]);
				jLblsSeriesValues[i].setText("n/a");
			}
			for (int i = 0; i < jLblsResultsTeams.length; i++) {
				jLblsResultsTeams[i] = new JLabel();
				jPnlStatistics.add(jLblsResultsTeams[i]);
				jLblsResultsTeams[i].setBounds(results[STARTX], results[STARTY] + i * results[GAPY], results[SIZEX], results[SIZEY]);
				
				for (int j = 0; j < jLblsResultsValues[i].length; j++) {
					jLblsResultsValues[i][j] = new JLabel();
					jPnlStatistics.add(jLblsResultsValues[i][j]);
					jLblsResultsValues[i][j].setBounds(resultsV[STARTX] + j * resultsV[GAPX], resultsV[STARTY] + i * resultsV[GAPY], resultsV[SIZEX], resultsV[SIZEY]);
					alignCenter(jLblsResultsValues[i][j]);
					jLblsResultsValues[i][j].setOpaque(true);
				}
			}
			{
				jPnlTableExcerpt = new JPanel();
				this.add(jPnlTableExcerpt);
				jPnlTableExcerpt.setLayout(null);
				jPnlTableExcerpt.setOpaque(true);
				jPnlTableExcerpt.setBackground(cbackground);
				jPnlTableExcerpt.setBounds(REC_TABLEPNL);
			}
			sumofwidthes = 0;
			for (int i = 0; i < 10; i++) {
				jLblsTableHeader[i] = new JLabel();
				jPnlTableExcerpt.add(jLblsTableHeader[i]);
				if (i == 1)	alignLeft(jLblsTableHeader[i]);
				else		alignCenter(jLblsTableHeader[i]);
				jLblsTableHeader[i].setBounds(teStartX + sumofwidthes, teStartY, teWidthes[i], teHeight);
				jLblsTableHeader[i].setText(headerStrings[i]);
				sumofwidthes += teWidthes[i] + teGapX[i];
			}
			for (int i = 0; i < 5; i++) {
				sumofwidthes = 0;
				for (int j = 0; j < 10; j++) {
					jLblsTableExcerpt[i][j] = new JLabel();
					jPnlTableExcerpt.add(jLblsTableExcerpt[i][j]);
					if (j == 1)	alignLeft(jLblsTableExcerpt[i][j]);
					else		alignCenter(jLblsTableExcerpt[i][j]);
					jLblsTableExcerpt[i][j].setBounds(teStartX + sumofwidthes, teStartY + (i + 1) * (teHeight + teGapY), teWidthes[j], teHeight);
					sumofwidthes += teWidthes[j] + teGapX[j];
				}
			}
			{
				jLblBackground = new JLabel();
				jPnlTableExcerpt.add(jLblBackground);
				jLblBackground.setOpaque(true);
				jLblBackground.setBackground(colorCategory3);
			}
			{
				jSPKader = new JScrollPane();
				this.add(jSPKader);
				jSPKader.setVisible(true);
				jSPKader.getVerticalScrollBar().setUnitIncrement(20);
				jSPKader.setBorder(null);
			}
			{
				jPnlKader = new JPanel();
				jPnlKader.setLayout(null);
				jPnlKader.setOpaque(true);
				jPnlKader.setBackground(cbackground);
			}
			for (int i = 0; i < numberOfPositions; i++) {
				jLblsPositionVal[i] = new JLabel();
				jPnlKader.add(jLblsPositionVal[i]);
				
				jLblsPosition[i] = new JLabel();
				jPnlKader.add(jLblsPosition[i]);
				jLblsPosition[i].setText(positionsPlayer[i]);
			}
			{
				jLblAverageAge = new JLabel();
				jPnlKader.add(jLblAverageAge);
				jLblAverageAge.setBounds(REC_LBLAVERAGEAGE);
				jLblAverageAge.setText("Durchschnittsalter:");
			}
			{
				jLblAverageAgeVal = new JLabel();
				jPnlKader.add(jLblAverageAgeVal);
				jLblAverageAgeVal.setBounds(REC_LBLAVERAGEAGEVAL);
			}
			{
				jLblNoData = new JLabel();
				jPnlKader.add(jLblNoData);
				jLblNoData.setBounds(REC_LBLNODATA);
				jLblNoData.setText("Für diesen Verein wurden keine Spielerdaten bereitgestellt.");
			}
			{
				jLblNumberOfPlayers = new JLabel();
				jPnlKader.add(jLblNumberOfPlayers);
				jLblNumberOfPlayers.setBounds(REC_LBLPLAYERS);
			}
			{
				jLblNumberOfUsedPlayers = new JLabel();
				jPnlKader.add(jLblNumberOfUsedPlayers);
				jLblNumberOfUsedPlayers.setBounds(REC_LBLUSEDPLAYERS);
			}
			{
				jBtnAddPlayer = new JButton();
				jPnlKader.add(jBtnAddPlayer);
				jBtnAddPlayer.setText("Neuer Spieler");
				jBtnAddPlayer.setBounds(REC_BTNADDPLAYER);
				jBtnAddPlayer.setFocusable(false);
				jBtnAddPlayer.setVisible(false);
				jBtnAddPlayer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						addPlayer();
					}
				});
			}
			{
				jLblKaderMoreLess = new JLabel();
				jPnlKader.add(jLblKaderMoreLess);
				alignRight(jLblKaderMoreLess);
				jLblKaderMoreLess.setText("mehr dazu >");
				jLblKaderMoreLess.setBounds(REC_LBLKADERMORELESS);
				jLblKaderMoreLess.setCursor(handCursor);
				jLblKaderMoreLess.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showMoreLessFromKader();
					}
				});
			}
			
			
			int minimumheight = 730;
			int maximumheight = 840;
			Dimension dim = new Dimension();
			dim.width = marginX + jPnlMatches.getSize().width + 5 + jPnlInformation.getSize().width + marginX;
			dim.height = marginY + 30 + jPnlMatches.getSize().height + marginY;
			if (dim.height < minimumheight) {
				dim.height = minimumheight;
			} else if (dim.height > maximumheight) {
				dim.height = maximumheight;
			}
			setSize(dim);
			jSPKader.setBounds(marginX + REC_SPPLPNL.width + 5, 535, 550, dim.height - (540));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (int i = 0; i < getHeight(); i++) {
			int wert = (int) 128 + (128 * i / getHeight());
			g.setColor(new Color(wert, 0, 0));
			g.drawLine(0, i, getWidth() - 1, i);
		}
	}
	
	public void showKader() {
		team.retrieveMatchPerformances();
		if (jLblsKader != null) {
			for (int i = 0; i < jLblsKader.length; i++) {
				for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
					jLblsKader[i][j].setVisible(false);
					jPnlKader.remove(jLblsKader[i][j]);
				}
			}
			for (int i = 0; i < jLblsKaderDescr.length; i++) {
				jLblsKaderDescr[i].setVisible(false);
				jPnlKader.remove(jLblsKaderDescr[i]);
			}
			repaintImmediately(jPnlKader);
		}
		
		eligiblePlayers = team.getEligiblePlayers(Start.today(), true);
		numberOfEligiblePlayers = eligiblePlayers.size();
		ineligiblePlayers = team.getIneligiblePlayers(Start.today(), true);
		numberOfIneligiblePlayers = ineligiblePlayers.size();
		int[] nOfPlayersByPosition = new int[numberOfPositions];
		
		jLblsKader = new JLabel[numberOfEligiblePlayers + numberOfIneligiblePlayers][NUMBEROFFIELDSKAD];
		jLblsKaderDescr = new JLabel[numberOfEligiblePlayers == 0 ? 0 : numberOfPositions];
		
		for (int i = 0; i < jLblsKaderDescr.length; i++) {
			jLblsKaderDescr[i] = new JLabel();
			jPnlKader.add(jLblsKaderDescr[i]);
			jLblsKaderDescr[i].setText(positions[i]);
			jLblsKaderDescr[i].setFont(getFont().deriveFont(Font.BOLD));
			jLblsKaderDescr[i].setVisible(showingMoreKader);
		}
		
		int countSinceLastER = 0;
		int descrIndex = 0;
		int sumOfAges = 0;
		for (int i = 0; i < numberOfEligiblePlayers; i++) {
			if (countSinceLastER == 0) {
				jLblsKaderDescr[descrIndex].setBounds(kaderSTARTX, kaderSTARTY + (i + descrIndex) * (kaderHEIGHT + kaderGAPY), 70, kaderHEIGHT);
				descrIndex++;
			}
			
			int diff = 0;
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				final int x = i;
				jLblsKader[i][j] = new JLabel();
				jPnlKader.add(jLblsKader[i][j]);
				jLblsKader[i][j].setBounds(20 + kaderSTARTX + diff, kaderSTARTY + (i + descrIndex) * (kaderHEIGHT + kaderGAPY), kaderWIDTHES[j], kaderHEIGHT);
				diff += kaderWIDTHES[j] + kaderGAPX[j];
				jLblsKader[i][j].setVisible(showingMoreKader);
				jLblsKader[i][j].setCursor(handCursor);
				jLblsKader[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						showPlayerPhoto(x);
					}
				});
			}
			Spieler player = eligiblePlayers.get(i);
			sumOfAges += player.getAge();
			SaisonPerformance seasonPerformance = player.getSeasonPerformance();
			jLblsKader[i][SQUADNUMBER].setText("" + player.getSquadNumber());
			alignCenter(jLblsKader[i][SQUADNUMBER]);
			jLblsKader[i][NAMES].setText(player.getFullNameShort());
			jLblsKader[i][BIRTHDATE].setText(MyDate.datum(player.getBirthDate()));
			jLblsKader[i][MATCHES].setText("" + seasonPerformance.matchesPlayed());
			alignCenter(jLblsKader[i][MATCHES]);
			jLblsKader[i][GOALS].setText("" + seasonPerformance.goalsScored());
			alignCenter(jLblsKader[i][GOALS]);
			jLblsKader[i][ASSISTS].setText("" + seasonPerformance.goalsAssisted());
			alignCenter(jLblsKader[i][ASSISTS]);
			jLblsKader[i][BOOKINGS].setText("" + seasonPerformance.booked());
			alignCenter(jLblsKader[i][BOOKINGS]);
			jLblsKader[i][BOOKEDTWICE].setText("" + seasonPerformance.bookedTwice());
			alignCenter(jLblsKader[i][BOOKEDTWICE]);
			jLblsKader[i][REDCARDS].setText("" + seasonPerformance.sentOffStraight());
			alignCenter(jLblsKader[i][REDCARDS]);
			
			nOfPlayersByPosition[descrIndex - 1]++;
			countSinceLastER++;
			if (descrIndex == 1 && countSinceLastER == team.getCurrentNumberOf(Position.TOR) || 
					descrIndex == 2 && countSinceLastER == team.getCurrentNumberOf(Position.ABWEHR) || 
					descrIndex == 3 && countSinceLastER == team.getCurrentNumberOf(Position.MITTELFELD)) {
				countSinceLastER = 0;
			}
		}
		for (int i = 0; i < numberOfIneligiblePlayers; i++) {
			int index = numberOfEligiblePlayers + i;
			int diff = 0;
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				final int x = index;
				jLblsKader[index][j] = new JLabel();
				jPnlKader.add(jLblsKader[index][j]);
				jLblsKader[index][j].setBounds(20 + kaderSTARTX + diff, kaderSTARTY + (index + 5) * (kaderHEIGHT + kaderGAPY), kaderWIDTHES[j], kaderHEIGHT);
				diff += kaderWIDTHES[j] + kaderGAPX[j];
				jLblsKader[index][j].setVisible(showingMoreKader);
				jLblsKader[index][j].setCursor(handCursor);
				jLblsKader[index][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						showPlayerPhoto(x);
					}
				});
			}
			Spieler player = ineligiblePlayers.get(i);
			SaisonPerformance seasonPerformance = player.getSeasonPerformance();
			jLblsKader[index][SQUADNUMBER].setText("" + player.getSquadNumber());
			alignCenter(jLblsKader[index][SQUADNUMBER]);
			jLblsKader[index][NAMES].setText(player.getFullNameShort());
			jLblsKader[index][BIRTHDATE].setText(MyDate.datum(player.getBirthDate()));
			jLblsKader[index][MATCHES].setText("" + seasonPerformance.matchesPlayed());
			alignCenter(jLblsKader[index][MATCHES]);
			jLblsKader[index][GOALS].setText("" + seasonPerformance.goalsScored());
			alignCenter(jLblsKader[index][GOALS]);
			jLblsKader[index][ASSISTS].setText("" + seasonPerformance.goalsAssisted());
			alignCenter(jLblsKader[index][ASSISTS]);
			jLblsKader[index][BOOKINGS].setText("" + seasonPerformance.booked());
			alignCenter(jLblsKader[index][BOOKINGS]);
			jLblsKader[index][BOOKEDTWICE].setText("" + seasonPerformance.bookedTwice());
			alignCenter(jLblsKader[index][BOOKEDTWICE]);
			jLblsKader[index][REDCARDS].setText("" + seasonPerformance.sentOffStraight());
			alignCenter(jLblsKader[index][REDCARDS]);
		}
		
		boolean hasPlayers = numberOfEligiblePlayers > 0;
		jLblKaderMoreLess.setVisible(hasPlayers || canHaveKader);
		jLblNoData.setVisible(!showingMoreKader && !hasPlayers);
		jLblAverageAge.setVisible(!showingMoreKader && hasPlayers);
		jLblAverageAgeVal.setVisible(!showingMoreKader && hasPlayers);
		jLblNumberOfPlayers.setVisible(!showingMoreKader && hasPlayers);
		jLblNumberOfUsedPlayers.setVisible(!showingMoreKader && hasPlayers);
		
		for (int i = 0; i < numberOfPositions; i++) {
			jLblsPositionVal[i].setText("" + nOfPlayersByPosition[i]);
			jLblsPositionVal[i].setBounds(20, 20 + i * 25, 20, 20);
			jLblsPosition[i].setBounds(45, 20 + i * 25, 110, 20);
			jLblsPositionVal[i].setVisible(!showingMoreKader && hasPlayers);
			jLblsPosition[i].setVisible(!showingMoreKader && hasPlayers);
		}
		jLblNumberOfPlayers.setText(team.getNumberOfPlayers(false, false) + " Spieler");
		jLblNumberOfUsedPlayers.setText("davon " + team.getNumberOfUsedPlayers() + " eingesetzt");
		
		jPnlKader.setPreferredSize(new Dimension(kaderWIDTH, hasPlayers ? standardHeightKader : standardHeightKaderNoPlayers));
		if (showingMoreKader) {
			int numberOfPlayers = numberOfEligiblePlayers + 4;
			if (numberOfIneligiblePlayers > 0)	numberOfPlayers += numberOfIneligiblePlayers + 1;
			int height = showingMoreKader ? kaderSTARTY + numberOfPlayers * (kaderHEIGHT + kaderGAPY) : standardHeightKader;
			jPnlKader.setPreferredSize(new Dimension(kaderWIDTH, height));
		}
		jSPKader.setViewportView(jPnlKader);
		if (hasPlayers) {
			jLblAverageAgeVal.setText(String.format("%5.2f Jahre", (double) sumOfAges / numberOfEligiblePlayers / 365.24));
		}
	}
	
	private void showPlayerPhoto(int playerIndex) {
		Spieler player = null;
		if (playerIndex < numberOfEligiblePlayers)	player = eligiblePlayers.get(playerIndex);
		else										player = ineligiblePlayers.get(playerIndex - numberOfEligiblePlayers);
		
		playerInformation.setPlayer(player);
		playerInformation.setVisible(true);
	}
	
	public void setMannschaft(int id) {
		if (showingMoreKader)	showMoreLessFromKader();
		teamID = id;
		team = teams[teamID - 1];
		canHaveKader = team.getCompetition().teamsHaveKader();
		jLblTeamName.setText(team.getName());
		if (hasGrDatum) {
			jLblFoundingDate.setText("Gegründet: " + (!team.getFoundingDate().equals("01.01.1970") ? team.getFoundingDate() : "n. a."));
			jLblFoundingDate.setVisible(true);
		}
		showKader();
		
		labelsBefuellen();
		showStatistics();
		showTableExcerpt();
	}
	
	private void showOnlyHomeMatches() {
		for (int i = 0; i < numberOfMatchdays; i++) {
			if (!jLblsMatches[i][TEAMHOME].getText().equals(team.getName())) {
				for (int j = 0; j < jLblsMatches[i].length; j++) {
					jLblsMatches[i][j].setOpaque(true);
					jLblsMatches[i][j].setBackground(cgreyedout);
					repaintImmediately(jLblsMatches[i][j]);
				}
			}
		}
	}
	
	private void showOnlyAwayMatches() {
		for (int i = 0; i < numberOfMatchdays; i++) {
			if (!jLblsMatches[i][TEAMAWAY].getText().equals(team.getName())) {
				for (int j = 0; j < jLblsMatches[i].length; j++) {
					jLblsMatches[i][j].setOpaque(true);
					jLblsMatches[i][j].setBackground(cgreyedout);
					repaintImmediately(jLblsMatches[i][j]);
				}
			}
		}
	}
	
	private void showAllMatches() {
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < jLblsMatches[i].length; j++) {
				jLblsMatches[i][j].setOpaque(false);
			}
		}
		labelsBefuellen();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < jLblsMatches[i].length; j++) {
				repaintImmediately(jLblsMatches[i][j]);
			}
		}
	}
	
	private void sortByDate() {
		// make matchday order chronological
		ArrayList<Integer> dates = new ArrayList<>();
		matchdayOrder.clear();
		for (int i = 0; i < numberOfMatchdays; i++) {
			String dateString = team.getDateAndTime(i);
			int index = 0, date = MyDate.getDate(dateString.split(" ")[0]);
			for (Integer itg : dates) {
				if (date == MyDate.UNIX_EPOCH || date > itg)	index++;
			}
			dates.add(index, date);
			matchdayOrder.add(index, i);
		}
		labelsBefuellen();
	}
	
	private void sortByMatchday() {
		// reset matchday order
		matchdayOrder.clear();
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdayOrder.add(i);
		}
		labelsBefuellen();
	}
	
	private void showTeam(int tableIndex, boolean home) {
		if ((homeaway[tableIndex] ^ home) && opponents[tableIndex] != 0)	Start.getInstance().uebersichtAnzeigen(opponents[tableIndex]);
	}
	
	private void showMatchday(int matchday) {
		Start.getInstance().showMatchday(matchday);
	}
	
	private void showMoreLessStatistics() {
		showingMoreStats = !showingMoreStats;
		
		jLblStatisticsMoreLess.setText(showingMoreStats ? "< weniger" : "mehr dazu >");
		jPnlStatistics.setBounds(595, 120, 550, showingMoreStats ? getHeight() - 125 : 115);
		jPnlTableExcerpt.setVisible(!showingMoreStats);
		jSPKader.setVisible(!showingMoreStats);
	}
	
	private void showMoreLessFromKader() {
		showingMoreKader = !showingMoreKader;
		for (int i = 0; i < jLblsKaderDescr.length; i++) {
			jLblsKaderDescr[i].setVisible(showingMoreKader);
			jLblsPositionVal[i].setVisible(!showingMoreKader);
			jLblsPosition[i].setVisible(!showingMoreKader);
		}
		for (int i = 0; i < jLblsKader.length; i++) {
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				jLblsKader[i][j].setVisible(showingMoreKader);
			}
		}
		jLblAverageAge.setVisible(!showingMoreKader);
		jLblAverageAgeVal.setVisible(!showingMoreKader);
		jLblNumberOfPlayers.setVisible(!showingMoreKader);
		jLblNumberOfUsedPlayers.setVisible(!showingMoreKader);
		
		jLblKaderMoreLess.setText(showingMoreKader ? "< weniger" : "mehr dazu >");
		jBtnAddPlayer.setVisible(showingMoreKader);
		int numberOfPlayers = numberOfEligiblePlayers + 4;
		if (numberOfIneligiblePlayers > 0)	numberOfPlayers += numberOfIneligiblePlayers + 1;
		int height = showingMoreKader ? kaderSTARTY + numberOfPlayers * (kaderHEIGHT + kaderGAPY) + 5 : standardHeightKader;
		jPnlKader.setPreferredSize(new Dimension(kaderWIDTH, height));
		jSPKader.setViewportView(jPnlKader);
		jSPKader.setBounds(marginX + REC_SPPLPNL.width + 5, showingMoreKader ? 120 : 535, 550, getHeight() - (showingMoreKader ? 125 : 540));
		jPnlStatistics.setVisible(!showingMoreKader);
		jPnlTableExcerpt.setVisible(!showingMoreKader);
	}
	
	private void addPlayer() {
		playerInformation.addPlayer(team);
		playerInformation.setVisible(true);
	}
	
	public void labelsBefuellen() {
		int[] indices = new int[numberOfMatchdays];
		for (int i = 0; i < indices.length; i++) {
			indices[matchdayOrder.get(i)] = i;
		}
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			int[] match = team.getMatch(matchday);
			int index = indices[matchday];
			
			jLblsMatches[index][MATCHDAY].setText("" + (matchday + 1));
			jLblsMatches[index][TRENNZEICHEN].setText(":");
			
			jLblsMatches[index][DATE].setText(team.getDateAndTime(matchday));
			jLblsMatches[index][TEAMHOME].setBackground(Color.yellow);
			jLblsMatches[index][TEAMAWAY].setBackground(Color.yellow);
			
			if (team.isMatchSet(matchday)) {
				if (match[1] != 0) {
					opponents[index] = match[1];
					homeaway[index] = match[0] == Mannschaft.HOME;
					if (match[0] == Mannschaft.HOME) {
						jLblsMatches[index][TEAMHOME].setText(team.getName());
						jLblsMatches[index][TEAMAWAY].setText(teams[match[1] - 1].getName());
						jLblsMatches[index][TEAMHOME].setOpaque(true);
						jLblsMatches[index][TEAMAWAY].setOpaque(false);
					} else if (match[0] == Mannschaft.AWAY){
						jLblsMatches[index][TEAMHOME].setText(teams[match[1] - 1].getName());
						jLblsMatches[index][TEAMAWAY].setText(team.getName());
						jLblsMatches[index][TEAMHOME].setOpaque(false);
						jLblsMatches[index][TEAMAWAY].setOpaque(true);
					}
				} else {
					jLblsMatches[index][TEAMHOME].setText("spielfrei");
					jLblsMatches[index][TEAMAWAY].setText("spielfrei");
				}
			} else {
				jLblsMatches[index][TEAMHOME].setText("n.a.");
				jLblsMatches[index][TEAMAWAY].setText("n.a.");
			}
			
			jLblsMatches[index][TEAMHOME].repaint();
			jLblsMatches[index][TEAMAWAY].repaint();
			
			if (team.isResultSet(matchday)) {
				if (match[0] == Mannschaft.HOME) {
					jLblsMatches[index][GOALSHOME].setText("" + match[2]);
					jLblsMatches[index][GOALSAWAY].setText("" + match[3]);
				} else if (match[0] == Mannschaft.AWAY){
					jLblsMatches[index][GOALSHOME].setText("" + match[3]);
					jLblsMatches[index][GOALSAWAY].setText("" + match[2]);
				}
				Color color = (match[2] == match[3] ? Color.white : (match[2] > match[3] ? Color.green : Color.red));
				jLblsMatches[index][GOALSHOME].setBackground(color);
				jLblsMatches[index][TRENNZEICHEN].setBackground(color);
				jLblsMatches[index][GOALSAWAY].setBackground(color);
				jLblsMatches[index][GOALSHOME].setOpaque(true);
				jLblsMatches[index][TRENNZEICHEN].setOpaque(true);
				jLblsMatches[index][GOALSAWAY].setOpaque(true);
			} else {
				jLblsMatches[index][GOALSHOME].setText("-");
				jLblsMatches[index][GOALSAWAY].setText("-");
				jLblsMatches[index][GOALSHOME].setBackground(null);
				jLblsMatches[index][TRENNZEICHEN].setBackground(null);
				jLblsMatches[index][GOALSAWAY].setBackground(null);
				jLblsMatches[index][GOALSHOME].setOpaque(false);
				jLblsMatches[index][TRENNZEICHEN].setOpaque(false);
				jLblsMatches[index][GOALSAWAY].setOpaque(false);
			}
		}
	}
	
	public void showStatistics() {
		int matchday = competition.getNewestStartedMatchday();
		int[] fairplayData = team.getFairplayData();
		jLblMatchesPlayedVal.setText("" + team.get(2, matchday, Tabellenart.COMPLETE));
		jLblMatchesWonVal.setText("" + team.get(3, matchday, Tabellenart.COMPLETE));
		jLblMatchesDrawnVal.setText("" + team.get(4, matchday, Tabellenart.COMPLETE));
		jLblMatchesLostVal.setText("" + team.get(5, matchday, Tabellenart.COMPLETE));
		jLblGoalsScoredVal.setText("" + team.get(6, matchday, Tabellenart.COMPLETE));
		jLblGoalsConcededVal.setText("" + team.get(7, matchday, Tabellenart.COMPLETE));
		jLblBookedVal.setText("" + fairplayData[0]);
		jLblBookedTwiceVal.setText("" + fairplayData[1]);
		jLblRedCardsVal.setText("" + fairplayData[2]);
		
		for (int i = 0; i < NUMBER_OF_SERIES; i++) {
			int maximum = team.getSeries(i + 1);
			jLblsSeriesValues[i].setText("" + maximum);
		}
		
		for (int i = 0; i < teams.length; i++) {
			jLblsResultsTeams[i].setText("");
		}
		for (int i = 0; i < teams.length; i++) {
			teams[i].compareWithOtherTeams(teams, matchday, Tabellenart.COMPLETE);
			int place = teams[i].getPlace();
			while (!jLblsResultsTeams[place].getText().equals("")) {
				place++;
			}
			jLblsResultsTeams[place].setText(teams[i].getName());
			
			String[] results = team.getResultsAgainst(teams[i]);
			for (int j = 0; j < jLblsResultsValues[place].length; j++) {
				String[] split = results[j].split(";");
				int points = Integer.parseInt(split[0]);
				if (points == 3)		jLblsResultsValues[place][j].setBackground(Color.green);
				else if (points == 1)	jLblsResultsValues[place][j].setBackground(Color.white);
				else if (points == 0)	jLblsResultsValues[place][j].setBackground(Color.red);
				else					jLblsResultsValues[place][j].setBackground(null);
				jLblsResultsValues[place][j].setText(split[1]);
			}
		}
	}
	
	public void showTableExcerpt() {
		int newestMatchday = competition.getNewestStartedMatchday();
		team.compareWithOtherTeams(teams, newestMatchday, Tabellenart.COMPLETE);
		int anzahlMannschaften = teams.length;
		int[] tabelle = new int[anzahlMannschaften];
		
		int nextPlace = 0, thisTeamsPlace = 0;
		for (int i = 0; i < anzahlMannschaften; i++) {
			for (Mannschaft ms : teams) {
				if (ms.getPlace() == i) {
					tabelle[nextPlace] = ms.getId();
					if (ms.getId() == teamID)	thisTeamsPlace = nextPlace;
					nextPlace++;
				}
			}
		}
		int firstShownTeam = thisTeamsPlace - 2, lastShownTeam = thisTeamsPlace + 2;
		int lowDiff, upDiff;
		if ((lowDiff = firstShownTeam - 0) < 0) {
			firstShownTeam -= lowDiff;
			lastShownTeam -= lowDiff;
		}
		if ((upDiff = lastShownTeam - anzahlMannschaften + 1) > 0) {
			firstShownTeam -= upDiff;
			lastShownTeam -= upDiff;
		}
		if (firstShownTeam < 0) firstShownTeam = 0;
		
		int index = 0;
		for (int i = firstShownTeam; i <= lastShownTeam; i++) {
			boolean thisTeam = i == thisTeamsPlace;
			jLblsTableExcerpt[index][0].setText("" + (teams[tabelle[i] - 1].get(0, newestMatchday, Tabellenart.COMPLETE) + 1));
			jLblsTableExcerpt[index][1].setText(teams[tabelle[i] - 1].getName());
			for (int j = 2; j < 10; j++) {
				jLblsTableExcerpt[index][j].setText("" + teams[tabelle[i] - 1].get(j, newestMatchday, Tabellenart.COMPLETE));
			}
			
			if (thisTeam)	jLblBackground.setBounds(teStartX, teStartY + (index + 1) * (teHeight + teGapY), 530, teHeight);
			index++;
		}
		
		int[] places = new int[numberOfMatchdays];
		int matchday = 0;
		while (matchday <= newestMatchday) {
			team.compareWithOtherTeams(teams, matchday, Tabellenart.COMPLETE);
			int place = team.get(0, matchday, Tabellenart.COMPLETE) + 1;
			places[matchday] = place;
			matchday++;
		}
		
		if (rankingHistory != null)	rankingHistory.setVisible(false);
		rankingHistory = new Tabellenverlauf(teams.length, places, competition);
		jPnlTableExcerpt.add(rankingHistory);
		rankingHistory.setLocation(10, 130);
	}
}
