package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class Uebersicht extends JPanel {
	private static final long serialVersionUID = 5249833173928569240L;

	// Spielplan
	private Rectangle REC_SPPLPNL;
	
	// Informationen
	private Rectangle REC_INFPNL = new Rectangle(580, 20, 500, 80);
	private Rectangle REC_LBLNAME = new Rectangle(65, 10, 370, 30);
	private Rectangle REC_LBLGRDATUM = new Rectangle(175, 40, 150, 30);
	
	// Statistiken
	private Rectangle REC_STATSPNL = new Rectangle(580, 105, 500, 115);
	private Rectangle REC_LBLMATCHESVAL = new Rectangle(10, 10, 25, 20);
	private Rectangle REC_LBLMATCHES = new Rectangle(40, 10, 50, 20);
	private Rectangle REC_LBLMATCHESWONVAL = new Rectangle(10, 35, 25, 20);
	private Rectangle REC_LBLMATCHESWON = new Rectangle(40, 35, 70, 20);
	private Rectangle REC_LBLMATCHESDRAWNVAL = new Rectangle(10, 60, 25, 20);
	private Rectangle REC_LBLMATCHESDRAWN = new Rectangle(40, 60, 95, 20);
	private Rectangle REC_LBLMATCHESLOSTVAL = new Rectangle(10, 85, 25, 20);
	private Rectangle REC_LBLMATCHESLOST = new Rectangle(40, 85, 60, 20);
	private Rectangle REC_LBLGOALSVAL = new Rectangle(190, 10, 25, 20);
	private Rectangle REC_LBLGOALS = new Rectangle(220, 10, 40, 20);
	private Rectangle REC_LBLGOALSCONCVAL = new Rectangle(190, 35, 25, 20);
	private Rectangle REC_LBLGOALSCONC = new Rectangle(220, 35, 70, 20);
	private Rectangle REC_LBLBOOKEDVAL = new Rectangle(340, 10, 25, 20);
	private Rectangle REC_LBLBOOKED = new Rectangle(370, 10, 85, 20);
	private Rectangle REC_LBLBOOKEDTWICEVAL = new Rectangle(340, 35, 25, 20);
	private Rectangle REC_LBLBOOKEDTWICE = new Rectangle(370, 35, 110, 20);
	private Rectangle REC_LBLREDCARDSVAL = new Rectangle(340, 60, 25, 20);
	private Rectangle REC_LBLREDCARDS = new Rectangle(370, 60, 80, 20);
	private Rectangle REC_LBLSTATSMORELESS = new Rectangle(395, 90, 80, 20);
	
	private int[] results = new int[] {15, 130, 0, 25, 190, 20};
	private int[] resultsV = new int[] {210, 130, 30, 25, 25, 20};
	private Rectangle REC_LBLSERIEN = new Rectangle(335, 130, 150, 25);
	private int[] series = new int[] {350, 155, 0, 25, 110, 20};
	private int[] seriesV = new int[] {460, 155, 0, 25, 20, 20};
	
	private Rectangle REC_TABLEPNL = new Rectangle(580, 225, 500, 290);
	
	private Rectangle REC_LBLAVERAGEAGE = new Rectangle(20, 135, 120, 20);
	private Rectangle REC_LBLAVERAGEAGEVAL = new Rectangle(20, 160, 80, 20);
	private Rectangle REC_LBLNODATA = new Rectangle(25, 35, 370, 25);
	private Rectangle REC_LBLKADERMORELESS = new Rectangle(395, 5, 80, 25);
	
	private Color cbackground = new Color(255, 128, 128);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private String[] headerStrings = {"Pl.", "Verein", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
	private String[] positions = new String[] {"Tor", "Abwehr", "Mittelfeld", "Sturm"};
	private String[] positionsPlayer = new String[] {"Torhüter", "Verteidiger", "Mittelfeldspieler", "Stürmer"};
	
	private static final int NUMBER_OF_SERIES = 9;
	private String[] seriesStrings = new String[] {"gewonnen", "unentschieden", "verloren", "unbesiegt", "sieglos", "mit Tor", "ohne Tor", "mit Gegentor", "ohne Gegentor"};
	
	private JPanel spiele;
	private JLabel[][] spieltage;
	
	private JPanel jPnlInformationen;
	private JLabel jLblMannschaftsname;
	private JLabel jLblGruendungsdatum;
	
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
	private JLabel jLblsSerien;
	private JLabel[] jLblsSeries;
	private JLabel[] jLblsSeriesValues;
	private JLabel[] jLblsResultsTeams;
	private JLabel[][] jLblsResultsValues;
	
	private JPanel jPnlTableExcerpt;
	private JLabel[] jLblsTableHeader;
	private JLabel[][] jLblsTableExcerpt;
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
	private JLabel jLblKaderMoreLess;

	private SpielerInformationen spielerInformationen;
	
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
	private static final int NUMBEROFFIELDSKAD = 3;
	
	/** The left and right margin for spiele */
	private int nstartx = 05;
	private int startx = 20;
	private int starty = 20;
	private int[] widthes = {20, 120, 175, 10, 5, 10, 175};
	private int height = 15;
	private int[] gapx = {5, 5, 10, 0, 0, 10, 0};
	private int gapy = 5;
	private int middlegapy = 15;
	
	private int teStartx = 10;
	private int teStarty = 5;
	private int[] teWidthes = {25, 200, 25, 25, 25, 25, 30, 30, 30, 30};
	private int teHeight = 15;
	private int[] teGapx = {10, 5, 5, 0, 0, 5, 0, 5, 5, 0};
	private int teGapy = 5;
	
	private int kaderSTARTX = 20;
	private int kaderSTARTY = 10;
	private int[] kaderWIDTHES = {20, 170, 75};
	private int kaderHEIGHT = 15;
	private int[] kaderGAPX = {5, 5, 0};
	private int kaderGAPY = 3;
	
	private int standardHeightKader = 190;
	private int standardHeightKaderNoPlayers = 100;
	
	private Start start;
	private Wettbewerb wettbewerb;
	
	private boolean hasGrDatum;
	
	private int mannschaftID;
	private Mannschaft mannschaft;
	private Mannschaft[] mannschaften;
	private int[] opponents;
	private boolean[] homeaway;
	
	private int numberOfMatchdays;
	private int numberOfPlayers;
	private int numberOfPositions;
	private boolean showingMore;

	public Uebersicht(Start start, LigaSaison season/*, Mannschaft mannschaft*/) {
		super();
		this.start = start;
		this.wettbewerb = season;
		this.hasGrDatum = true;
//		this.mannschaft = mannschaft;
		initGUI();
	}
	
	public Uebersicht(Start start, Gruppe gruppe/*, Mannschaft mannschaft*/) {
		super();
		this.start = start;
		this.wettbewerb = gruppe;
		this.hasGrDatum = false;
//		this.mannschaft = mannschaft;
		initGUI();
	}
	
	public Uebersicht(Start start, Turnier turnier/*, Mannschaft mannschaft*/) {
		super();
		this.start = start;
		this.hasGrDatum = false;
//		this.mannschaft = mannschaft;
//		initGUI();
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);
			
			mannschaften = wettbewerb.getMannschaften();
			numberOfMatchdays = wettbewerb.getNumberOfMatchdays();
			numberOfPositions = 4;
			if (wettbewerb.getNumberOfMatchesAgainstSameOpponent() == 1)	middlegapy = 0;
			
			spieltage = new JLabel[numberOfMatchdays][NUMBEROFFIELDSSPPL];
			opponents = new int[numberOfMatchdays];
			homeaway = new boolean[numberOfMatchdays];
			jLblsSeries = new JLabel[NUMBER_OF_SERIES];
			jLblsSeriesValues = new JLabel[NUMBER_OF_SERIES];
			jLblsResultsTeams = new JLabel[mannschaften.length];
			jLblsResultsValues = new JLabel[mannschaften.length][wettbewerb.getNumberOfMatchesAgainstSameOpponent()];
			jLblsTableHeader = new JLabel[10];
			jLblsTableExcerpt = new JLabel[5][10];
			jLblsPositionVal = new JLabel[numberOfPositions];
			jLblsPosition = new JLabel[numberOfPositions];
			

			int sumofwidthes = 2 * nstartx;
			for (int i = 0; i < widthes.length; i++) {
				sumofwidthes += widthes[i];
			}
			for (int i = 0; i < gapx.length; i++) {
				sumofwidthes += gapx[i];
			}
			REC_SPPLPNL = new Rectangle(startx, starty, sumofwidthes, 2 * 5 + numberOfMatchdays * height + (numberOfMatchdays - 1) * gapy + middlegapy);
			
			{
				spielerInformationen = new SpielerInformationen(wettbewerb);
				spielerInformationen.setLocationRelativeTo(null);
			}
			
			{
				spiele = new JPanel();
				this.add(spiele);
				spiele.setLayout(null);
				spiele.setBounds(REC_SPPLPNL);
				spiele.setOpaque(true);
				spiele.setBackground(cbackground);
			}
			
			for (int i = 0; i < spieltage.length; i++) {
				final int x = i;
				int diff = 0;
				
				for (int j = 0; j < spieltage[i].length; j++) {
					spieltage[i][j] = new JLabel();
					spiele.add(spieltage[i][j]);
					spieltage[i][j].setBounds(nstartx + diff, 5 + i * (height + gapy) + (i / (spieltage.length / 2)) * middlegapy, widthes[j], height);
					
					diff += widthes[j] + gapx[j];
				}
				spieltage[i][TEAMHOME].setCursor(handCursor);
				spieltage[i][TEAMAWAY].setCursor(handCursor);
				spieltage[i][MATCHDAY].setCursor(handCursor);
				
				spieltage[i][MATCHDAY].setHorizontalAlignment(SwingConstants.CENTER);
				spieltage[i][DATE].setHorizontalAlignment(SwingConstants.CENTER);
				spieltage[i][TEAMHOME].setHorizontalAlignment(SwingConstants.RIGHT);
				spieltage[i][GOALSHOME].setHorizontalAlignment(SwingConstants.RIGHT);
				spieltage[i][TRENNZEICHEN].setHorizontalAlignment(SwingConstants.CENTER);
				spieltage[i][GOALSAWAY].setHorizontalAlignment(SwingConstants.LEFT);
				spieltage[i][TEAMAWAY].setHorizontalAlignment(SwingConstants.LEFT);
				
				spieltage[i][TEAMHOME].setBackground(Color.yellow);
				spieltage[i][TEAMAWAY].setBackground(Color.yellow);
				
				spieltage[i][TEAMHOME].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showTeam(x, true);
					}
				});
				spieltage[i][TEAMAWAY].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showTeam(x, false);
					}
				});
				spieltage[i][MATCHDAY].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						spieltagAnzeigen(x);
					}
				});
			}
			{
				jPnlInformationen = new JPanel();
				this.add(jPnlInformationen);
				jPnlInformationen.setLayout(null);
				jPnlInformationen.setBounds(REC_INFPNL);
				jPnlInformationen.setOpaque(true);
				jPnlInformationen.setBackground(cbackground);
			}
			{
				jLblMannschaftsname = new JLabel();
				jPnlInformationen.add(jLblMannschaftsname);
				jLblMannschaftsname.setBounds(REC_LBLNAME);
				jLblMannschaftsname.setFont(new Font("Dialog", 0, 24));
				jLblMannschaftsname.setHorizontalAlignment(SwingConstants.CENTER);
			}
			if (hasGrDatum) {
				jLblGruendungsdatum = new JLabel();
				jPnlInformationen.add(jLblGruendungsdatum);
				jLblGruendungsdatum.setBounds(REC_LBLGRDATUM);
				jLblGruendungsdatum.setFont(new Font("Dialog", 0, 12));
				jLblGruendungsdatum.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblMatchesPlayedVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblMatchesWonVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblMatchesDrawnVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblMatchesLostVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblGoalsScoredVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblGoalsConcededVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblBookedVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblBookedTwiceVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblRedCardsVal.setHorizontalAlignment(SwingConstants.CENTER);
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
				jLblStatisticsMoreLess.setHorizontalAlignment(SwingConstants.RIGHT);
				jLblStatisticsMoreLess.setText("Mehr dazu >");
				jLblStatisticsMoreLess.setCursor(handCursor);
				jLblStatisticsMoreLess.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showMoreStatistics();
					}
				});
			}
			{
				jLblsSerien = new JLabel();
				jPnlStatistics.add(jLblsSerien);
				jLblsSerien.setBounds(REC_LBLSERIEN);
				jLblsSerien.setText("Meiste Spiele in Folge ...");
			}
			for (int i = 0; i < NUMBER_OF_SERIES; i++) {
				jLblsSeries[i] = new JLabel();
				jPnlStatistics.add(jLblsSeries[i]);
				jLblsSeries[i].setBounds(series[STARTX], series[STARTY] + i * series[GAPY], series[SIZEX], series[SIZEY]);
				jLblsSeries[i].setText("... " + seriesStrings[i]);
				
				jLblsSeriesValues[i] = new JLabel();
				jPnlStatistics.add(jLblsSeriesValues[i]);
				jLblsSeriesValues[i].setBounds(seriesV[STARTX], seriesV[STARTY] + i * seriesV[GAPY], seriesV[SIZEX], seriesV[SIZEY]);
				jLblsSeriesValues[i].setHorizontalAlignment(SwingConstants.CENTER);
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
					jLblsResultsValues[i][j].setHorizontalAlignment(SwingConstants.CENTER);
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
				if (i == 1)	jLblsTableHeader[i].setHorizontalAlignment(SwingConstants.LEFT);
				else		jLblsTableHeader[i].setHorizontalAlignment(SwingConstants.CENTER);
				jLblsTableHeader[i].setBounds(teStartx + sumofwidthes, teStarty, teWidthes[i], teHeight);
				jLblsTableHeader[i].setText(headerStrings[i]);
				sumofwidthes += teWidthes[i] + teGapx[i];
			}
			for (int i = 0; i < 5; i++) {
				sumofwidthes = 0;
				for (int j = 0; j < 10; j++) {
					jLblsTableExcerpt[i][j] = new JLabel();
					jPnlTableExcerpt.add(jLblsTableExcerpt[i][j]);
					if (j == 1)	jLblsTableExcerpt[i][j].setHorizontalAlignment(SwingConstants.LEFT);
					else		jLblsTableExcerpt[i][j].setHorizontalAlignment(SwingConstants.CENTER);
					jLblsTableExcerpt[i][j].setBounds(teStartx + sumofwidthes, teStarty + (i + 1) * (teHeight + teGapy), teWidthes[j], teHeight);
					sumofwidthes += teWidthes[j] + teGapx[j];
				}
			}
			{
				jSPKader = new JScrollPane();
				this.add(jSPKader);
				jSPKader.setVisible(true);
				jSPKader.getVerticalScrollBar().setUnitIncrement(20);
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
				jLblKaderMoreLess = new JLabel();
				jPnlKader.add(jLblKaderMoreLess);
				jLblKaderMoreLess.setHorizontalAlignment(SwingConstants.RIGHT);
				jLblKaderMoreLess.setText("Mehr dazu >");
				jLblKaderMoreLess.setBounds(REC_LBLKADERMORELESS);
				jLblKaderMoreLess.setCursor(handCursor);
				jLblKaderMoreLess.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showMoreLessFromKader();
					}
				});
			}
			
			
			int minimumheight = 645;
			int maximumheight = 840;
			Dimension dim = new Dimension();
			dim.width = startx + spiele.getSize().width + 5 + jPnlInformationen.getSize().width + startx;
			dim.height = starty + spiele.getSize().height + 20;
			if (dim.height < minimumheight) {
				dim.height = minimumheight;
			} else if (dim.height > maximumheight) {
				dim.height = maximumheight;
			}
			this.setSize(dim);
			jSPKader.setBounds(startx + REC_SPPLPNL.width + 5, 520, 481 + 19, dim.height - (540));
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
	
	private void newKader() {
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
		
		ArrayList<Spieler> eligiblePlayers = mannschaft.getEligiblePlayers(Start.today());
		numberOfPlayers = eligiblePlayers.size();
		int[] nOfPlayersByPosition = new int[numberOfPositions];
		
		jLblsKader = new JLabel[numberOfPlayers][NUMBEROFFIELDSKAD];
		jLblsKaderDescr = new JLabel[numberOfPlayers == 0 ? 0 : numberOfPositions];
		
		int countSinceLastER = 0;
		int descrIndex = 0;
		int sumOfAges = 0;
		for (int i = 0; i < numberOfPlayers; i++) {
			if (countSinceLastER == 0) {
				jLblsKaderDescr[descrIndex] = new JLabel();
				jPnlKader.add(jLblsKaderDescr[descrIndex]);
				jLblsKaderDescr[descrIndex].setBounds(kaderSTARTX, kaderSTARTY + (i + descrIndex) * (kaderHEIGHT + kaderGAPY), 70, kaderHEIGHT);
				jLblsKaderDescr[descrIndex].setText(positions[descrIndex]);
				jLblsKaderDescr[descrIndex].setFont(getFont().deriveFont(Font.BOLD));
				jLblsKaderDescr[descrIndex].setVisible(false);
				descrIndex++;
			}
			
			int diff = 0;
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				final int x = i;
				jLblsKader[i][j] = new JLabel();
				jPnlKader.add(jLblsKader[i][j]);
				jLblsKader[i][j].setBounds(20 + kaderSTARTX + diff, kaderSTARTY + (i + descrIndex) * (kaderHEIGHT + kaderGAPY), kaderWIDTHES[j], kaderHEIGHT);
				diff += kaderWIDTHES[j] + kaderGAPX[j];
				jLblsKader[i][j].setVisible(false);
				jLblsKader[i][j].setCursor(handCursor);
				jLblsKader[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						showPlayerPhoto(x);
					}
				});
			}
			Spieler spieler = eligiblePlayers.get(i);
			jLblsKader[i][SQUADNUMBER].setText("" + spieler.getSquadNumber());
			jLblsKader[i][SQUADNUMBER].setHorizontalAlignment(SwingConstants.CENTER);
			jLblsKader[i][NAMES].setText(spieler.getFullNameShort());
			jLblsKader[i][BIRTHDATE].setText(MyDate.datum(spieler.getBirthDate()));
			int age = spieler.getAge();
			sumOfAges += age;
			
			nOfPlayersByPosition[descrIndex - 1]++;
			countSinceLastER++;
			if (descrIndex == 1 && countSinceLastER == mannschaft.getCurrentNumberOf(Position.TOR) || 
					descrIndex == 2 && countSinceLastER == mannschaft.getCurrentNumberOf(Position.ABWEHR) || 
					descrIndex == 3 && countSinceLastER == mannschaft.getCurrentNumberOf(Position.MITTELFELD)) {
				countSinceLastER = 0;
			}
		}
		
		boolean hasPlayers = numberOfPlayers > 0;
		jLblKaderMoreLess.setVisible(hasPlayers);
		jLblNoData.setVisible(!hasPlayers);
		jLblAverageAge.setVisible(hasPlayers);
		jLblAverageAgeVal.setVisible(hasPlayers);
		
		for (int i = 0; i < numberOfPositions; i++) {
			jLblsPositionVal[i].setText("" + nOfPlayersByPosition[i]);
			jLblsPositionVal[i].setBounds(20, 20 + i * 25, 20, 20);
			jLblsPosition[i].setBounds(45, 20 + i * 25, 110, 20);
			jLblsPositionVal[i].setVisible(hasPlayers);
			jLblsPosition[i].setVisible(hasPlayers);
		}
		
		jPnlKader.setPreferredSize(new Dimension(401, hasPlayers ? standardHeightKader : standardHeightKaderNoPlayers));
		jSPKader.setViewportView(jPnlKader);
		if (hasPlayers) {
			jLblAverageAgeVal.setText(String.format("%5.2f Jahre", (double) sumOfAges / numberOfPlayers / 365.24));
		}
	}
	
	private void showPlayerPhoto(int playerIndex) {
		Spieler player = mannschaft.getEligiblePlayers(Start.today()).get(playerIndex);
		String playerName = removeUmlaute(player.getFullNameShort());
		playerName = playerName.toLowerCase().replace(' ', '-');
		String url = "file:///" + mannschaft.getPhotoDirectory() + playerName + ".jpg";
		
		spielerInformationen.setPlayer(player, url);
		spielerInformationen.setVisible(true);
	}
	
	public void setMannschaft(int id) {
		mannschaftID = id;
		mannschaft = mannschaften[mannschaftID - 1];
		jLblMannschaftsname.setText(mannschaft.getName());
		if (hasGrDatum) {
			jLblGruendungsdatum.setText("Gegründet: " + mannschaft.getGruendungsdatum());
			jLblGruendungsdatum.setVisible(true);
		}
		newKader();

		labelsBefuellen();
		showStatistics();
		showTableExcerpt();
	}
	
	private void showTeam(int tableIndex, boolean home) {
		if ((homeaway[tableIndex] ^ home) && opponents[tableIndex] != 0)	start.uebersichtAnzeigen(opponents[tableIndex]);
	}
	
	private void spieltagAnzeigen(int matchday) {
		start.spieltagAnzeigen(matchday);
	}
	
	private void showMoreStatistics() {
		// TODO more statistics
		showingMore = !showingMore;
		
		jLblStatisticsMoreLess.setText(showingMore ? "< Weniger" : "Mehr dazu >");
		jPnlStatistics.setBounds(580, 105, 500, showingMore ? getHeight() - 125 : 115);
		jPnlTableExcerpt.setVisible(!showingMore);
		jSPKader.setVisible(!showingMore);
	}
	
	private void showMoreLessFromKader() {
		showingMore = !showingMore;
		for (int i = 0; i < jLblsKaderDescr.length; i++) {
			jLblsKaderDescr[i].setVisible(showingMore);
			jLblsPositionVal[i].setVisible(!showingMore);
			jLblsPosition[i].setVisible(!showingMore);
		}
		for (int i = 0; i < jLblsKader.length; i++) {
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				jLblsKader[i][j].setVisible(showingMore);
			}
		}
		jLblAverageAge.setVisible(!showingMore);
		jLblAverageAgeVal.setVisible(!showingMore);
		
		jLblKaderMoreLess.setText(showingMore ? "< Weniger" : "Mehr dazu >");
		int height = showingMore ? kaderSTARTY + (numberOfPlayers + 4) * (kaderHEIGHT + kaderGAPY) : standardHeightKader;
		jPnlKader.setPreferredSize(new Dimension(401, height));
		jSPKader.setViewportView(jPnlKader);
		jSPKader.setBounds(startx + REC_SPPLPNL.width + 5, showingMore ? 105 : 520, 481 + 19, getHeight() - (showingMore ? 125 : 540));
		jPnlStatistics.setVisible(!showingMore);
		jPnlTableExcerpt.setVisible(!showingMore);
	}
	
	public void labelsBefuellen() {
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			int[] match = mannschaft.getMatch(matchday);
			
			spieltage[matchday][MATCHDAY].setText("" + (matchday + 1));
			spieltage[matchday][TRENNZEICHEN].setText(":");
			
			spieltage[matchday][DATE].setText(mannschaft.getDateAndTime(matchday));
			
			if (mannschaft.isSpielEntered(matchday)) {
				if (match[1] != 0) {
					opponents[matchday] = match[1];
					homeaway[matchday] = match[0] == Mannschaft.HOME;
					if (match[0] == Mannschaft.HOME) {
						spieltage[matchday][TEAMHOME].setText(mannschaft.getName());
						spieltage[matchday][TEAMAWAY].setText(mannschaften[match[1] - 1].getName());
						spieltage[matchday][TEAMHOME].setOpaque(true);
						spieltage[matchday][TEAMAWAY].setOpaque(false);
					} else if (match[0] == Mannschaft.AWAY){
						spieltage[matchday][TEAMHOME].setText(mannschaften[match[1] - 1].getName());
						spieltage[matchday][TEAMAWAY].setText(mannschaft.getName());
						spieltage[matchday][TEAMHOME].setOpaque(false);
						spieltage[matchday][TEAMAWAY].setOpaque(true);
					}
				} else {
					spieltage[matchday][TEAMHOME].setText("spielfrei");
					spieltage[matchday][TEAMAWAY].setText("spielfrei");
				}
			} else {
				spieltage[matchday][TEAMHOME].setText("n.a.");
				spieltage[matchday][TEAMAWAY].setText("n.a.");
			}
			
			spieltage[matchday][TEAMHOME].repaint();
			spieltage[matchday][TEAMAWAY].repaint();
			
			if (mannschaft.isErgebnisEntered(matchday)) {
				if (match[0] == Mannschaft.HOME) {
					spieltage[matchday][GOALSHOME].setText("" + match[2]);
					spieltage[matchday][GOALSAWAY].setText("" + match[3]);
				} else if (match[0] == Mannschaft.AWAY){
					spieltage[matchday][GOALSHOME].setText("" + match[3]);
					spieltage[matchday][GOALSAWAY].setText("" + match[2]);
				}
				Color color = (match[2] == match[3] ? Color.white : (match[2] > match[3] ? Color.green : Color.red));
				spieltage[matchday][GOALSHOME].setBackground(color);
				spieltage[matchday][TRENNZEICHEN].setBackground(color);
				spieltage[matchday][GOALSAWAY].setBackground(color);
				spieltage[matchday][GOALSHOME].setOpaque(true);
				spieltage[matchday][TRENNZEICHEN].setOpaque(true);
				spieltage[matchday][GOALSAWAY].setOpaque(true);
			} else {
				spieltage[matchday][GOALSHOME].setText("-");
				spieltage[matchday][GOALSAWAY].setText("-");
				spieltage[matchday][GOALSHOME].setBackground(null);
				spieltage[matchday][TRENNZEICHEN].setBackground(null);
				spieltage[matchday][GOALSAWAY].setBackground(null);
				spieltage[matchday][GOALSHOME].setOpaque(false);
				spieltage[matchday][TRENNZEICHEN].setOpaque(false);
				spieltage[matchday][GOALSAWAY].setOpaque(false);
			}
		}
	}
	
	public void showStatistics() {
		int matchday = wettbewerb.getNewestStartedMatchday();
		int[] fairplayData = mannschaft.getFairplayData();
		jLblMatchesPlayedVal.setText("" + mannschaft.get(2, matchday, Tabellenart.COMPLETE));
		jLblMatchesWonVal.setText("" + mannschaft.get(3, matchday, Tabellenart.COMPLETE));
		jLblMatchesDrawnVal.setText("" + mannschaft.get(4, matchday, Tabellenart.COMPLETE));
		jLblMatchesLostVal.setText("" + mannschaft.get(5, matchday, Tabellenart.COMPLETE));
		jLblGoalsScoredVal.setText("" + mannschaft.get(6, matchday, Tabellenart.COMPLETE));
		jLblGoalsConcededVal.setText("" + mannschaft.get(7, matchday, Tabellenart.COMPLETE));
		jLblBookedVal.setText("" + fairplayData[0]);
		jLblBookedTwiceVal.setText("" + fairplayData[1]);
		jLblRedCardsVal.setText("" + fairplayData[2]);
		
		for (int i = 0; i < NUMBER_OF_SERIES; i++) {
			int maximum = mannschaft.getSeries(i + 1);
			jLblsSeriesValues[i].setText("" + maximum);
		}
		
		for (int i = 0; i < mannschaften.length; i++) {
			jLblsResultsTeams[i].setText("");
		}
		for (int i = 0; i < mannschaften.length; i++) {
			int place = mannschaften[i].getPlace();
			while (!jLblsResultsTeams[place].getText().equals("")) {
				place++;
			}
			jLblsResultsTeams[place].setText(mannschaften[i].getName());
			
			String[] results = mannschaft.getResultsAgainst(mannschaften[i]);
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
		int newestMatchday = wettbewerb.getNewestStartedMatchday();
		mannschaft.compareWithOtherTeams(mannschaften, newestMatchday, Tabellenart.COMPLETE);
		int anzahlMannschaften = mannschaften.length;
		int[] tabelle = new int[anzahlMannschaften];
		
		int nextPlace = 0, thisTeamsPlace = 0;
		for (int i = 0; i < anzahlMannschaften; i++) {
			for (Mannschaft ms : mannschaften) {
				if (ms.getPlace() == i) {
					tabelle[nextPlace] = ms.getId();
					if (ms.getId() == mannschaftID)	thisTeamsPlace = nextPlace;
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
			jLblsTableExcerpt[index][0].setText("" + (mannschaften[tabelle[i] - 1].get(0, newestMatchday, Tabellenart.COMPLETE) + 1));
			jLblsTableExcerpt[index][1].setText(mannschaften[tabelle[i] - 1].getName());
			for (int j = 2; j < 10; j++) {
				jLblsTableExcerpt[index][j].setText("" + mannschaften[tabelle[i] - 1].get(j, newestMatchday, Tabellenart.COMPLETE));
			}
			
			for (int j = 0; j < 10; j++) {
				if (thisTeam)	jLblsTableExcerpt[index][j].setBackground(colorCategory3);
				jLblsTableExcerpt[index][j].setOpaque(thisTeam);
				repaintImmediately(jLblsTableExcerpt[index][j]);
			}
			index++;
		}
		
		int[] places = new int[numberOfMatchdays];
		int matchday = 0;
		while (matchday <= newestMatchday) {
			mannschaft.compareWithOtherTeams(mannschaften, matchday, Tabellenart.COMPLETE);
			int place = mannschaft.get(0, matchday, Tabellenart.COMPLETE) + 1;
			places[matchday] = place;
			matchday++;
		}
		
		if (rankingHistory != null)	rankingHistory.setVisible(false);
		rankingHistory = new Tabellenverlauf(wettbewerb.getMannschaften().length, places, wettbewerb);
		jPnlTableExcerpt.add(rankingHistory);
		rankingHistory.setLocation(10, 130);
	}
}
