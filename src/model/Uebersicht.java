package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import analyse.SaisonPerformance;
import util.MyDocumentListener;

import static util.Utilities.*;

public class Uebersicht extends JPanel {
	private static final long serialVersionUID = 5249833173928569240L;
	
	private static final int totalWidth = 1150;
	private static final int marginX = 5;
	private static final int widthMatches = 585;
	private static final int marginMiddle = 5;
	private static final int widthStatistics = 550;
	private static final int marginY = 5;
	
	private static final int startXStatistics = 595;
	
	private Rectangle REC_BTNBACK = new Rectangle(5, 5, 100, 25);
	private Rectangle REC_LBLSORTBYDATE = new Rectangle(140, 10, 145, 15);
	private Rectangle REC_LBLONLYHOME = new Rectangle(310, 10, 105, 15);
	private Rectangle REC_LBLONLYAWAY = new Rectangle(440, 10, 130, 15);
	
	// Informationen
	private Rectangle REC_INFPNL = new Rectangle(startXStatistics, 35, widthStatistics, 80);
	private Rectangle REC_LBLNAME = new Rectangle(90, 10, 370, 30);
	private Rectangle REC_LBLFOUDATE = new Rectangle(200, 40, 150, 30);
	
	// Statistiken
	private Rectangle REC_STATSPNL = new Rectangle(startXStatistics, 120, widthStatistics, 115);
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
	
	private int[] results = new int[] {15, 130, 0, 25, 190, 20};
	private int[] resultsV = new int[] {210, 130, 30, 25, 25, 20};
	private Rectangle REC_LBLSERIEN = new Rectangle(360, 130, 150, 25);
	private int[] series = new int[] {375, 155, 0, 25, 110, 20};
	private int[] seriesV = new int[] {500, 155, 0, 25, 20, 20};
	
	private int[] bndsSuggestions = new int[] {175, 70, 0, 30, 200, 20};
	
	private Rectangle REC_TABLEPNL = new Rectangle(startXStatistics, 240, widthStatistics, 290);
	private Rectangle REC_LBLNOTABLE = new Rectangle(25, 35, 350, 25);
	
	private Rectangle REC_LBLAVERAGEAGE = new Rectangle(20, 135, 125, 20);
	private Rectangle REC_LBLAVERAGEAGEVAL = new Rectangle(20, 160, 80, 20);
	private Rectangle REC_LBLNOKADER = new Rectangle(25, 35, 370, 25);
	private Rectangle REC_BTNADDAFFILIATION = new Rectangle(55, 5, 170, 25);
	private Rectangle REC_BTNADDPLAYER = new Rectangle(265, 5, 120, 25);
	private Rectangle REC_BTNSAVEAFFILIATION = new Rectangle(415, 5, 110, 25);
	private Rectangle REC_LBLKADERMORELESS = new Rectangle(445, 5, 80, 25);
	private Rectangle REC_LBLPLAYERS = new Rectangle(280, 45, 80, 20);
	private Rectangle REC_LBLUSEDPLAYERS = new Rectangle(280, 70, 140, 20);
	
	private Rectangle REC_LBLPLAYER = new Rectangle(25, 40, 80, 20);
	private Rectangle REC_TFSEARCH = new Rectangle(175, 40, 200, 20);
	private Rectangle REC_LBLPOSITION = new Rectangle(25, 70, 200, 20);
	private Rectangle REC_CBPOSITION = new Rectangle(175, 65, 200, 30);
	private Rectangle REC_LBLSQUADNUMBER = new Rectangle(25, 100, 200, 20);
	private Rectangle REC_TFSQUADNUMBER = new Rectangle(175, 100, 40, 20);
	private Rectangle REC_LBLDURATION = new Rectangle(25, 130, 200, 20);
	private Rectangle REC_CHBENTIRESEASON = new Rectangle(175, 130, 200, 20);
	private Rectangle REC_LBLFROM = new Rectangle(45, 165, 200, 20);
	private Rectangle REC_ATCLUBSINCEDAY = new Rectangle(85, 160, 70, 30);
	private Rectangle REC_ATCLUBSINCEMONTH = new Rectangle(165, 160, 70, 30);
	private Rectangle REC_ATCLUBSINCEYEAR = new Rectangle(245, 160, 85, 30);
	private Rectangle REC_LBLTO = new Rectangle(45, 205, 200, 20);
	private Rectangle REC_ATCLUBUNTILDAY = new Rectangle(85, 200, 70, 30);
	private Rectangle REC_ATCLUBUNTILMONTH = new Rectangle(165, 200, 70, 30);
	private Rectangle REC_ATCLUBUNTILYEAR = new Rectangle(245, 200, 85, 30);
	
	private Color colorBackground = new Color(255, 128, 128);
	private Color colorGrey = new Color(128, 128, 128);
	private Color colorHighlighted = Color.yellow;
	private Color colorOptions = new Color(0, 192, 255);
	private Font fontMainCategory = new Font("Lucida Grande", Font.BOLD, 13);
	private String[] tableHeaders = {"Pl.", "Verein", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
	private String[] kaderHeaders = {"", "", "", "Sp.", "T", "V", "G", "GR", "R"};
	private String[] kaderTooltips = {"", "", "", "Spiele", "Tore", "Vorlagen", "Gelbe Karten", "Gelb-rote Karten", "Rote Karten"};
	private String[] positions = {"Tor", "Abwehr", "Mittelfeld", "Sturm"};
	private String[] positionsPlayer = {"Torhüter", "Verteidiger", "Mittelfeldspieler", "Stürmer"};
	
	private static final int NUMBER_OF_SERIES = 9;
	private String[] seriesStrings = new String[] {"gewonnen", "unentschieden", "verloren", "unbesiegt", "sieglos", "mit Tor", "ohne Tor", "mit Gegentor", "ohne Gegentor"};
	
	private JButton jBtnBack;
	
	private JPanel jPnlMatches;
	private JLabel jLblShowOnlyHome;
	private JLabel jLblShowOnlyAway;
	private JLabel jLblSortByDate;
	private ArrayList<JLabel> jLblsMainCategory;
	private ArrayList<JLabel> jLblsSubCategory;
	private ArrayList<JLabel[]> jLblsAllMatches;
	
	private JPanel jPnlInformation;
	private JLabel jLblTeamName;
	private JLabel jLblFoundingDate;
	
	private JPanel jPnlStatistics;
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
	private JLabel[] jLblsSeries;
	private JLabel[] jLblsSeriesValues;
	private JLabel[] jLblsResultsTeams;
	private JLabel[][] jLblsResultsValues;
	
	private JPanel jPnlTableExcerpt;
	private JLabel jLblNoTableExcerpt;
	private JLabel[] jLblsTableHeader;
	private JLabel[][] jLblsTableExcerpt;
	private JLabel jLblBackground;
	private Tabellenverlauf rankingHistory;
	
	private JScrollPane jSPKader;
	private JPanel jPnlKader;
	private JLabel[] jLblsKaderHeader;
	private JLabel[][] jLblsKader;
	private JLabel[] jLblsPositions;
	private JLabel[] jLblsPositionVal;
	private JLabel[] jLblsPosition;
	private JLabel jLblAverageAge;
	private JLabel jLblAverageAgeVal;
	private JLabel jLblNoKader;
	private JLabel jLblNumberOfPlayers;
	private JLabel jLblNumberOfUsedPlayers;
	private JButton jBtnAddAffiliation;
	private JButton jBtnAddPlayer;
	private JLabel jLblKaderMoreLess;
	
	private JLabel jLblPlayer;
	private JTextField jTFSearchPlayer;
	private JLabel jLblSelectedPlayer;
	private ArrayList<JLabel> jLblsSuggestions;
	private JLabel jLblPosition;
	private JComboBox<String> jCBPosition;
	private JLabel jLblSquadNumber;
	private JTextField jTFSquadNumber;
	private JLabel jLblDuration;
	private JCheckBox jChBEntireSeason;
	private JLabel jLblFrom;
	private JLabel jLblTo;
	private JComboBox<String> jCBAtClubSinceDay;
	private JComboBox<String> jCBAtClubSinceMonth;
	private JComboBox<String> jCBAtClubSinceYear;
	private JComboBox<String> jCBAtClubUntilDay;
	private JComboBox<String> jCBAtClubUntilMonth;
	private JComboBox<String> jCBAtClubUntilYear;
	private JButton jBtnSaveAffiliation;

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
	private int widthMainCat = 250;
	private int widthSubCat = 250;
	private int[] diffsX = {0, 25, 150, 344, 360, 365, 390};
	private int[] widthes = {20, 120, 185, 16, 5, 16, 185};
	private int height = 15;
	private int gapy = 5;
	private int middleGapY = 15;
	
	private int teStartX = 10;
	private int teStartY = 5;
	private int[] teDiffsX = {0, 35, 255, 290, 320, 350, 385, 420, 460, 500};
	private int[] teWidthes = {25, 210, 25, 25, 25, 25, 30, 30, 30, 30};
	private int teHeight = 15;
	private int teGapY = 5;
	
	private int kaderSTARTX = 20;
	private int kaderSTARTY = 40;
	private int[] kaderWIDTHES = {20, 200, 75, 25, 25, 25, 25, 25, 25};
	private int kaderHEIGHT = 15;
	private int[] kaderGAPX = {5, 5, 25, 5, 0, 0, 0, 0, 0};
	private int kaderGAPY = 3;
	private int kaderWIDTH = 531; // scroll bar width: 19
	
	private int standardHeightKader = 190;
	private int standardHeightKaderNoPlayers = 100;
	
	private boolean hasFoundingDate;
	
	private int teamID;
	private Mannschaft team;
	private Mannschaft[] teams;
	private ArrayList<Integer> matchdayOrder = new ArrayList<>();
	
	private Dauer seasonDuration;
	private Datum date;
	
	private boolean noStats;
	private boolean noTable;
	
	private boolean canHaveKader;
	private ArrayList<TeamAffiliation> eligiblePlayers;
	private ArrayList<Spieler> ineligiblePlayers;
	private int numberOfMatchdays;
	private int numberOfEligiblePlayers;
	private int numberOfIneligiblePlayers;
	private int numberOfPositions;
	private boolean showingMoreStats;
	private boolean showingMoreKader;
	
	private int maximumSuggestions = 10;
	private ArrayList<Spieler> suggestions;
	private Spieler selectedPlayer;
	private boolean entireSeason;

	public Uebersicht(LigaSaison season) {
		super();
		hasFoundingDate = true;
		initGUI();
	}
	
	public Uebersicht(TurnierSaison season) {
		super();
		hasFoundingDate = false;
		initGUI();
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);
			
			numberOfPositions = 4;
			
			jLblsMainCategory = new ArrayList<>();
			jLblsSubCategory = new ArrayList<>();
			jLblsAllMatches = new ArrayList<>();
			jLblsSeries = new JLabel[NUMBER_OF_SERIES];
			jLblsSeriesValues = new JLabel[NUMBER_OF_SERIES];
			jLblsTableHeader = new JLabel[10];
			jLblsTableExcerpt = new JLabel[5][10];
			jLblsKaderHeader = new JLabel[NUMBEROFFIELDSKAD];
			jLblsPositionVal = new JLabel[numberOfPositions];
			jLblsPosition = new JLabel[numberOfPositions];
			
			{
				playerInformation = new SpielerInformationen(this);
				playerInformation.setLocationRelativeTo(null);
			}
			
			{
				jBtnBack = new JButton();
				this.add(jBtnBack);
				jBtnBack.setBounds(REC_BTNBACK);
				jBtnBack.setText("zurück");
				jBtnBack.setFocusable(false);
				jBtnBack.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Fussball.getInstance().jBtnBackActionPerformed();
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
				jLblSortByDate.setBackground(colorOptions);
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
				jLblShowOnlyHome.setBackground(colorOptions);
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
				jLblShowOnlyAway.setBackground(colorOptions);
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
				jPnlMatches.setOpaque(true);
				jPnlMatches.setBackground(colorBackground);
			}
			
			{
				jPnlInformation = new JPanel();
				this.add(jPnlInformation);
				jPnlInformation.setLayout(null);
				jPnlInformation.setBounds(REC_INFPNL);
				jPnlInformation.setOpaque(true);
				jPnlInformation.setBackground(colorBackground);
			}
			{
				jLblTeamName = new JLabel();
				jPnlInformation.add(jLblTeamName);
				jLblTeamName.setBounds(REC_LBLNAME);
				jLblTeamName.setFont(new Font("Dialog", 0, 24));
				alignCenter(jLblTeamName);
			}
			if (hasFoundingDate) {
				jLblFoundingDate = new JLabel();
				jPnlInformation.add(jLblFoundingDate);
				jLblFoundingDate.setBounds(REC_LBLFOUDATE);
				jLblFoundingDate.setFont(new Font("Dialog", 0, 12));
				alignCenter(jLblFoundingDate);
			}
			{
				jPnlStatistics = new JPanel();
				this.add(jPnlStatistics);
				jPnlStatistics.setLayout(null);
				jPnlStatistics.setOpaque(true);
				jPnlStatistics.setBackground(colorBackground);
				jPnlStatistics.setBounds(REC_STATSPNL);
			}
			{
				jLblNoStatistics = new JLabel();
				jPnlStatistics.add(jLblNoStatistics);
				jLblNoStatistics.setBounds(REC_LBLNOSTATS);
				jLblNoStatistics.setText("Für diesen Verein können keine Statistiken angezeigt werden.");
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
			{
				jPnlTableExcerpt = new JPanel();
				this.add(jPnlTableExcerpt);
				jPnlTableExcerpt.setLayout(null);
				jPnlTableExcerpt.setOpaque(true);
				jPnlTableExcerpt.setBackground(colorBackground);
				jPnlTableExcerpt.setBounds(REC_TABLEPNL);
			}
			{
				jLblNoTableExcerpt = new JLabel();
				jPnlTableExcerpt.add(jLblNoTableExcerpt);
				jLblNoTableExcerpt.setBounds(REC_LBLNOTABLE);
				jLblNoTableExcerpt.setText("Für diesen Verein kann keine Tabelle angezeigt werden.");
			}
			for (int i = 0; i < 10; i++) {
				jLblsTableHeader[i] = new JLabel();
				jPnlTableExcerpt.add(jLblsTableHeader[i]);
				if (i == 1)	alignLeft(jLblsTableHeader[i]);
				else		alignCenter(jLblsTableHeader[i]);
				jLblsTableHeader[i].setBounds(teStartX + teDiffsX[i], teStartY, teWidthes[i], teHeight);
				jLblsTableHeader[i].setText(tableHeaders[i]);
			}
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 10; j++) {
					jLblsTableExcerpt[i][j] = new JLabel();
					jPnlTableExcerpt.add(jLblsTableExcerpt[i][j]);
					if (j == 1)	alignLeft(jLblsTableExcerpt[i][j]);
					else		alignCenter(jLblsTableExcerpt[i][j]);
					jLblsTableExcerpt[i][j].setBounds(teStartX + teDiffsX[j], teStartY + (i + 1) * (teHeight + teGapY), teWidthes[j], teHeight);
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
				jPnlKader.setBackground(colorBackground);
			}
			int diff = 0;
			for (int i = 0; i < NUMBEROFFIELDSKAD; i++) {
				jLblsKaderHeader[i] = new JLabel();
				jPnlKader.add(jLblsKaderHeader[i]);
				jLblsKaderHeader[i].setBounds(20 + kaderSTARTX + diff, kaderSTARTY, kaderWIDTHES[i], kaderHEIGHT);
				diff += kaderWIDTHES[i] + kaderGAPX[i];
				jLblsKaderHeader[i].setVisible(false);
				alignCenter(jLblsKaderHeader[i]);
				jLblsKaderHeader[i].setText(kaderHeaders[i]);
				jLblsKaderHeader[i].setToolTipText(kaderTooltips[i]);
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
				jLblNoKader = new JLabel();
				jPnlKader.add(jLblNoKader);
				jLblNoKader.setBounds(REC_LBLNOKADER);
				jLblNoKader.setText("Für diesen Verein wurden keine Spielerdaten bereitgestellt.");
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
				jBtnAddAffiliation = new JButton();
				jPnlKader.add(jBtnAddAffiliation);
				jBtnAddAffiliation.setText("Bestehender Spieler");
				jBtnAddAffiliation.setBounds(REC_BTNADDAFFILIATION);
				jBtnAddAffiliation.setFocusable(false);
				jBtnAddAffiliation.setVisible(false);
				jBtnAddAffiliation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						addAffiliation();
					}
				});
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
				jBtnSaveAffiliation = new JButton();
				jPnlKader.add(jBtnSaveAffiliation);
				jBtnSaveAffiliation.setText("Speichern");
				jBtnSaveAffiliation.setBounds(REC_BTNSAVEAFFILIATION);
				jBtnSaveAffiliation.setFocusable(false);
				jBtnSaveAffiliation.setVisible(false);
				jBtnSaveAffiliation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						saveAffiliation();
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
			String[] positionen = new String[Position.values().length];
			for (int i = 0; i < positionen.length; i++) {
				positionen[i] = Position.values()[i].getName();
			}
			String[] monate = new String[12];
			for (int i = 1; i <= monate.length; i++) {
				monate[i - 1] = (i / 10) + "" + (i % 10) + ".";
			}
			{
				jLblPlayer = new JLabel();
				jPnlKader.add(jLblPlayer);
				jLblPlayer.setBounds(REC_LBLPLAYER);
				jLblPlayer.setText("Spieler");
				jLblPlayer.setVisible(false);
			}
			{
				jTFSearchPlayer = new JTextField();
				jPnlKader.add(jTFSearchPlayer);
				jTFSearchPlayer.setBounds(REC_TFSEARCH);
				jTFSearchPlayer.setVisible(false);
				jTFSearchPlayer.getDocument().addDocumentListener(new MyDocumentListener() {
					public void onAllUpdates(DocumentEvent e) {
						searchSuggestion();
					}
				});
			}
			{
				jLblSelectedPlayer = new JLabel();
				jPnlKader.add(jLblSelectedPlayer);
				jLblSelectedPlayer.setBounds(REC_TFSEARCH);
				jLblSelectedPlayer.setVisible(false);
			}
			{
				jLblPosition = new JLabel();
				jPnlKader.add(jLblPosition);
				jLblPosition.setBounds(REC_LBLPOSITION);
				jLblPosition.setText("Position");
				jLblPosition.setVisible(false);
			}
			{
				jCBPosition = new JComboBox<>();
				jPnlKader.add(jCBPosition);
				jCBPosition.setBounds(REC_CBPOSITION);
				jCBPosition.setModel(new DefaultComboBoxModel<>(positionen));
				jCBPosition.setVisible(false);
			}
			{
				jLblSquadNumber = new JLabel();
				jPnlKader.add(jLblSquadNumber);
				jLblSquadNumber.setBounds(REC_LBLSQUADNUMBER);
				jLblSquadNumber.setText("Rückennummer");
				jLblSquadNumber.setVisible(false);
			}
			{
				jTFSquadNumber = new JTextField();
				jPnlKader.add(jTFSquadNumber);
				jTFSquadNumber.setBounds(REC_TFSQUADNUMBER);
				jTFSquadNumber.setVisible(false);
			}
			{
				jLblDuration = new JLabel();
				jPnlKader.add(jLblDuration);
				jLblDuration.setBounds(REC_LBLDURATION);
				jLblDuration.setText("Dauer");
				jLblDuration.setVisible(false);
			}
			{
				jChBEntireSeason = new JCheckBox();
				jPnlKader.add(jChBEntireSeason);
				jChBEntireSeason.setBounds(REC_CHBENTIRESEASON);
				jChBEntireSeason.setText("gesamte Saison");
				jChBEntireSeason.setFocusable(false);
				jChBEntireSeason.setVisible(false);
				jChBEntireSeason.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						toggleEntireSeason();
					}
				});
			}
			{
				jLblFrom = new JLabel();
				jPnlKader.add(jLblFrom);
				jLblFrom.setBounds(REC_LBLFROM);
				jLblFrom.setText("von:");
				jLblFrom.setVisible(false);
			}
			{
				jCBAtClubSinceDay = new JComboBox<>();
				jPnlKader.add(jCBAtClubSinceDay);
				jCBAtClubSinceDay.setBounds(REC_ATCLUBSINCEDAY);
				jCBAtClubSinceDay.setVisible(false);
			}
			{
				jCBAtClubSinceMonth = new JComboBox<>();
				jPnlKader.add(jCBAtClubSinceMonth);
				jCBAtClubSinceMonth.setBounds(REC_ATCLUBSINCEMONTH);
				jCBAtClubSinceMonth.setModel(new DefaultComboBoxModel<>(monate));
				jCBAtClubSinceMonth.setVisible(false);
				jCBAtClubSinceMonth.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							refreshSinceDayModel();
						}
					}
				});
			}
			{
				jCBAtClubSinceYear = new JComboBox<>();
				jPnlKader.add(jCBAtClubSinceYear);
				jCBAtClubSinceYear.setBounds(REC_ATCLUBSINCEYEAR);
				jCBAtClubSinceYear.setVisible(false);
				jCBAtClubSinceYear.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							refreshSinceDayModel();
						}
					}
				});
			}
			{
				jLblTo = new JLabel();
				jPnlKader.add(jLblTo);
				jLblTo.setBounds(REC_LBLTO);
				jLblTo.setText("bis:");
				jLblTo.setVisible(false);
			}
			{
				jCBAtClubUntilDay = new JComboBox<>();
				jPnlKader.add(jCBAtClubUntilDay);
				jCBAtClubUntilDay.setBounds(REC_ATCLUBUNTILDAY);
				jCBAtClubUntilDay.setVisible(false);
			}
			{
				jCBAtClubUntilMonth = new JComboBox<>();
				jPnlKader.add(jCBAtClubUntilMonth);
				jCBAtClubUntilMonth.setBounds(REC_ATCLUBUNTILMONTH);
				jCBAtClubUntilMonth.setModel(new DefaultComboBoxModel<>(monate));
				jCBAtClubUntilMonth.setVisible(false);
				jCBAtClubUntilMonth.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							refreshUntilDayModel();
						}
					}
				});
			}
			{
				jCBAtClubUntilYear = new JComboBox<>();
				jPnlKader.add(jCBAtClubUntilYear);
				jCBAtClubUntilYear.setBounds(REC_ATCLUBUNTILYEAR);
				jCBAtClubUntilYear.setVisible(false);
				jCBAtClubUntilYear.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							refreshUntilDayModel();
						}
					}
				});
			}
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
			for (int i = 0; i < jLblsPositions.length; i++) {
				jLblsPositions[i].setVisible(false);
				jPnlKader.remove(jLblsPositions[i]);
			}
			repaintImmediately(jPnlKader);
		}
		
		eligiblePlayers = team.getEligiblePlayers(date, true);
		numberOfEligiblePlayers = eligiblePlayers.size();
		ineligiblePlayers = team.getIneligiblePlayers(date, true);
		numberOfIneligiblePlayers = ineligiblePlayers.size();
		int[] nOfPlayersByPosition = new int[numberOfPositions];
		
		jLblsKader = new JLabel[numberOfEligiblePlayers + numberOfIneligiblePlayers][NUMBEROFFIELDSKAD];
		jLblsPositions = new JLabel[numberOfEligiblePlayers == 0 ? 0 : numberOfPositions];
		
		for (int i = 0; i < jLblsPositions.length; i++) {
			jLblsPositions[i] = new JLabel();
			jPnlKader.add(jLblsPositions[i]);
			jLblsPositions[i].setText(positions[i]);
			jLblsPositions[i].setFont(getFont().deriveFont(Font.BOLD));
			jLblsPositions[i].setVisible(showingMoreKader);
		}
		
		int countSinceLastER = 0;
		int posID = 0;
		int sumOfAges = 0;
		for (int i = 0; i < numberOfEligiblePlayers; i++) {
			if (countSinceLastER == 0) {
				jLblsPositions[posID].setBounds(kaderSTARTX, kaderSTARTY + (i + posID) * (kaderHEIGHT + kaderGAPY), 70, kaderHEIGHT);
				posID++;
			}
			
			int diff = 0;
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				final int x = i;
				jLblsKader[i][j] = new JLabel();
				jPnlKader.add(jLblsKader[i][j]);
				jLblsKader[i][j].setBounds(20 + kaderSTARTX + diff, kaderSTARTY + (i + posID) * (kaderHEIGHT + kaderGAPY), kaderWIDTHES[j], kaderHEIGHT);
				diff += kaderWIDTHES[j] + kaderGAPX[j];
				jLblsKader[i][j].setVisible(showingMoreKader);
				jLblsKader[i][j].setCursor(handCursor);
				jLblsKader[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						showPlayerInformation(x);
					}
				});
			}
			Spieler player = eligiblePlayers.get(i).getPlayer();
			sumOfAges += player.getAge();
			SaisonPerformance seasonPerformance = player.getSeasonPerformance();
			jLblsKader[i][SQUADNUMBER].setText("" + eligiblePlayers.get(i).getSquadNumber());
			alignCenter(jLblsKader[i][SQUADNUMBER]);
			jLblsKader[i][NAMES].setText(player.getFullNameShort());
			jLblsKader[i][BIRTHDATE].setText(player.getBirthDate().withDividers());
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
			
			nOfPlayersByPosition[posID - 1]++;
			countSinceLastER++;
			if (posID == 1 && countSinceLastER == team.getCurrentNumberOf(Position.TOR) || 
					posID == 2 && countSinceLastER == team.getCurrentNumberOf(Position.ABWEHR) || 
					posID == 3 && countSinceLastER == team.getCurrentNumberOf(Position.MITTELFELD)) {
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
						showPlayerInformation(x);
					}
				});
			}
			Spieler player = ineligiblePlayers.get(i);
			SaisonPerformance seasonPerformance = player.getSeasonPerformance();
			jLblsKader[index][SQUADNUMBER].setText("");
			alignCenter(jLblsKader[index][SQUADNUMBER]);
			jLblsKader[index][NAMES].setText(player.getFullNameShort());
			jLblsKader[index][BIRTHDATE].setText(player.getBirthDate().withDividers());
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
		jLblNoKader.setVisible(!showingMoreKader && !hasPlayers);
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
	
	private void showPlayerInformation(int playerIndex) {
		Spieler player = null;
		if (playerIndex < numberOfEligiblePlayers)	player = eligiblePlayers.get(playerIndex).getPlayer();
		else										player = ineligiblePlayers.get(playerIndex - numberOfEligiblePlayers);
		
		playerInformation.setPlayer(player, team);
		playerInformation.setVisible(true);
	}
	
	public void determineSize() {
		int minimumheight = 730;
		int maximumheight = 840;
		Dimension dim = new Dimension();
		dim.width = totalWidth;
		dim.height = marginY + 30 + jPnlMatches.getSize().height + marginY;
		if (dim.height < minimumheight) {
			dim.height = minimumheight;
		} else if (dim.height > maximumheight) {
			dim.height = maximumheight;
		}
		setSize(dim);
		jSPKader.setBounds(marginX + widthMatches + marginMiddle, 535, widthStatistics, dim.height - (540));
		jPnlStatistics.setBounds(startXStatistics, 120, widthStatistics, showingMoreStats ? getHeight() - 125 : 115);
	}
	
	public void setMannschaft(Mannschaft team) {
		if (showingMoreKader)	showMoreLessFromKader();
		teamID = team.getId();
		this.team = team;
		teams = team.getCompetition().getTeams();
		seasonDuration = team.getCompetition().getDuration();
		playerInformation.setSeasonDuration(seasonDuration);
		date = team.getTodayWithinSeasonBounds();
		numberOfMatchdays = team.getCompetition().getNumberOfMatchdays();
		matchdayOrder.clear();
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdayOrder.add(i);
		}
		canHaveKader = team.getCompetition().teamsHaveKader();
		jLblTeamName.setText(team.getName());
		if (hasFoundingDate) {
			jLblFoundingDate.setText("Gegründet: " + (!team.getFoundingDate().equals(UNIX_EPOCH.withDividers()) ? team.getFoundingDate() : "n. a."));
			jLblFoundingDate.setVisible(true);
		}
		showKader();
		noStats = team.getCompetition() instanceof KORunde;
		noTable = team.getCompetition() instanceof KORunde;
		
		String[] possibleYears = new String[seasonDuration.getToDate().getYear() - seasonDuration.getFromDate().getYear() + 1];
		for (int i = 0; i < possibleYears.length; i++) {
			possibleYears[i] = seasonDuration.getFromDate().getYear() + i + "";
		}
		jCBAtClubSinceYear.setModel(new DefaultComboBoxModel<>(possibleYears));
		jCBAtClubUntilYear.setModel(new DefaultComboBoxModel<>(possibleYears));
		
		fillLabels();
		showStatistics();
		showTableExcerpt();
		determineSize();
	}
	
	private void showOnlyHomeMatches() {
		for (int i = 0; i < numberOfMatchdays; i++) {
			if (!jLblsAllMatches.get(i)[TEAMHOME].getText().equals(team.getName())) {
				for (int j = 0; j < jLblsAllMatches.get(i).length; j++) {
					jLblsAllMatches.get(i)[j].setOpaque(true);
					jLblsAllMatches.get(i)[j].setBackground(colorGrey);
					repaintImmediately(jLblsAllMatches.get(i)[j]);
				}
			}
		}
	}
	
	private void showOnlyAwayMatches() {
		for (int i = 0; i < numberOfMatchdays; i++) {
			if (!jLblsAllMatches.get(i)[TEAMAWAY].getText().equals(team.getName())) {
				for (int j = 0; j < jLblsAllMatches.get(i).length; j++) {
					jLblsAllMatches.get(i)[j].setOpaque(true);
					jLblsAllMatches.get(i)[j].setBackground(colorGrey);
					repaintImmediately(jLblsAllMatches.get(i)[j]);
				}
			}
		}
	}
	
	private void showAllMatches() {
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < jLblsAllMatches.get(i).length; j++) {
				jLblsAllMatches.get(i)[j].setOpaque(false);
			}
			jLblsAllMatches.get(i)[TEAMHOME].setBackground(colorHighlighted);
			jLblsAllMatches.get(i)[TEAMAWAY].setBackground(colorHighlighted);
		}
		fillLabels();
		for (int i = 0; i < numberOfMatchdays; i++) {
			for (int j = 0; j < jLblsAllMatches.get(i).length; j++) {
				repaintImmediately(jLblsAllMatches.get(i)[j]);
			}
		}
	}
	
	private void sortByDate() {
		// make matchday order chronological
		ArrayList<Datum> dates = new ArrayList<>();
		matchdayOrder.clear();
		for (int i = 0; i < numberOfMatchdays; i++) {
			Datum date = Datum.parse(team.getDateAndTime(i).split(" ")[0]);
			int index = 0;
			for (Datum sorted : dates) {
				if (date == MIN_DATE || date.isAfter(sorted))	index++;
			}
			dates.add(index, date);
			matchdayOrder.add(index, i);
		}
		fillLabels();
	}
	
	private void sortByMatchday() {
		// reset matchday order
		matchdayOrder.clear();
		for (int i = 0; i < numberOfMatchdays; i++) {
			matchdayOrder.add(i);
		}
		fillLabels();
	}
	
	private void showTeam(int tableIndex, boolean home) {
		String jump = jLblsAllMatches.get(tableIndex)[home ? TEAMHOME : TEAMAWAY].getText();
		if (!jump.equals(MATCH_NOT_SET) && !jump.equals(SPIELFREI) && !jump.equals(team.getName())) {
			Fussball.getInstance().uebersichtAnzeigen(jump);
		}
	}
	
	private void showMatchday(int matchday) {
		Fussball.getInstance().showMatchday(matchday);
	}
	
	private void showMoreLessStatistics() {
		showingMoreStats = !showingMoreStats;
		
		jLblStatisticsMoreLess.setText(showingMoreStats ? "< weniger" : "mehr dazu >");
		jPnlStatistics.setBounds(startXStatistics, 120, widthStatistics, showingMoreStats ? getHeight() - 125 : 115);
		jPnlTableExcerpt.setVisible(!showingMoreStats);
		jSPKader.setVisible(!showingMoreStats);
	}
	
	private void showMoreLessFromKader() {
		showingMoreKader = !showingMoreKader;
		boolean hasPlayers = numberOfEligiblePlayers > 0;
		for (int i = 0; i < jLblsPositions.length; i++) {
			jLblsPositions[i].setVisible(showingMoreKader);
			jLblsPositionVal[i].setVisible(!showingMoreKader);
			jLblsPosition[i].setVisible(!showingMoreKader);
		}
		
		for (int i = 3; i < NUMBEROFFIELDSKAD; i++) {
			jLblsKaderHeader[i].setVisible(showingMoreKader && hasPlayers);
		}
		
		for (int i = 0; i < jLblsKader.length; i++) {
			for (int j = 0; j < NUMBEROFFIELDSKAD; j++) {
				jLblsKader[i][j].setVisible(showingMoreKader);
			}
		}
		
		jLblNoKader.setVisible(!hasPlayers);
		jLblAverageAge.setVisible(!showingMoreKader && hasPlayers);
		jLblAverageAgeVal.setVisible(!showingMoreKader && hasPlayers);
		jLblNumberOfPlayers.setVisible(!showingMoreKader && hasPlayers);
		jLblNumberOfUsedPlayers.setVisible(!showingMoreKader && hasPlayers);
		
		jLblKaderMoreLess.setText(showingMoreKader ? "< weniger" : "mehr dazu >");
		jBtnAddAffiliation.setVisible(showingMoreKader);
		jBtnAddPlayer.setVisible(showingMoreKader);
		int numberOfPlayers = numberOfEligiblePlayers + 4;
		if (numberOfIneligiblePlayers > 0)	numberOfPlayers += numberOfIneligiblePlayers + 1;
		int height = showingMoreKader ? kaderSTARTY + numberOfPlayers * (kaderHEIGHT + kaderGAPY) + 5 : standardHeightKader;
		jPnlKader.setPreferredSize(new Dimension(kaderWIDTH, height));
		jSPKader.setViewportView(jPnlKader);
		jSPKader.setBounds(marginX + widthMatches + marginMiddle, showingMoreKader ? 120 : 535, widthStatistics, getHeight() - (showingMoreKader ? 125 : 540));
		jPnlStatistics.setVisible(!showingMoreKader);
		jPnlTableExcerpt.setVisible(!showingMoreKader);
	}
	
	private void addAffiliation() {
		for (int i = 0; i < jLblsPositions.length; i++) {
			jLblsPositions[i].setVisible(false);
		}
		for (int i = 0; i < jLblsKaderHeader.length; i++) {
			jLblsKaderHeader[i].setVisible(false);
		}
		for (int i = 0; i < jLblsKader.length; i++) {
			for (int j = 0; j < jLblsKader[i].length; j++) {
				jLblsKader[i][j].setVisible(false);
			}
		}
		jBtnAddAffiliation.setEnabled(false);
		jBtnAddPlayer.setEnabled(false);
		jLblNoKader.setVisible(false);
		jLblKaderMoreLess.setVisible(false);
		
		jLblPlayer.setVisible(true);
		jTFSearchPlayer.setVisible(true);
		jBtnSaveAffiliation.setVisible(true);
		jBtnSaveAffiliation.setEnabled(false);
		
		suggestions = new ArrayList<>();
		jLblsSuggestions = new ArrayList<>();
		jTFSearchPlayer.setText("");
		jTFSearchPlayer.requestFocus();
	}
	
	private void searchSuggestion() {
		for (int i = 0; i < jLblsSuggestions.size(); i++) {
			jLblsSuggestions.get(i).setVisible(false);
		}
		suggestions.clear();
		jLblsSuggestions.clear();
		
		String search = jTFSearchPlayer.getText();
		if (search.length() < 4)	return;
		suggestions = Fussball.getMatchingPlayers(search);
		
		for (int i = 0; i < suggestions.size() && i < maximumSuggestions; i++) {
			final int x = i;
			JLabel label = new JLabel();
			jPnlKader.add(label);
			label.setCursor(handCursor);
			label.setBounds(bndsSuggestions[STARTX], bndsSuggestions[STARTY] + i * bndsSuggestions[GAPY], bndsSuggestions[SIZEX], bndsSuggestions[SIZEY]);
			label.setText(suggestions.get(i).getFullNameShort());
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					selectPlayer(x);
				}
			});
			jLblsSuggestions.add(label);
		}
		if (suggestions.size() > maximumSuggestions) {
			JLabel label = new JLabel();
			jPnlKader.add(label);
			label.setCursor(handCursor);
			label.setBounds(bndsSuggestions[STARTX], bndsSuggestions[STARTY] + maximumSuggestions * bndsSuggestions[GAPY], bndsSuggestions[SIZEX], bndsSuggestions[SIZEY]);
			label.setText(" und " + (suggestions.size() - maximumSuggestions) + " weitere");
			jLblsSuggestions.add(label);
		}
	}
	
	private void selectPlayer(int index) {
		if (index < 0 || index >= suggestions.size())	return;
		
		for (int i = 0; i < jLblsSuggestions.size(); i++) {
			jLblsSuggestions.get(i).setVisible(false);
		}
		jTFSearchPlayer.setVisible(false);
		
		selectedPlayer = suggestions.get(index);
		jLblSelectedPlayer.setText(selectedPlayer.getFullNameShort());
		jCBPosition.setSelectedIndex(2);
		jTFSquadNumber.setText("" + team.getNextFreeSquadNumber());
		
		jCBAtClubSinceYear.setSelectedItem("" + seasonDuration.getFromDate().getYear());
		jCBAtClubSinceMonth.setSelectedIndex(seasonDuration.getFromDate().getMonth() - 1);
		jCBAtClubSinceDay.setSelectedIndex(seasonDuration.getFromDate().getDay() - 1);
		jCBAtClubUntilYear.setSelectedItem("" + seasonDuration.getToDate().getYear());
		jCBAtClubUntilMonth.setSelectedIndex(seasonDuration.getToDate().getMonth() - 1);
		jCBAtClubUntilDay.setSelectedIndex(seasonDuration.getToDate().getDay() - 1);
		
		jBtnSaveAffiliation.setEnabled(true);
		jLblSelectedPlayer.setVisible(true);
		jLblPosition.setVisible(true);
		jCBPosition.setVisible(true);
		jLblSquadNumber.setVisible(true);
		jTFSquadNumber.setVisible(true);
		jLblDuration.setVisible(true);
		jChBEntireSeason.setVisible(true);
		jChBEntireSeason.setSelected(true);
		entireSeason = false;
		toggleEntireSeason();
		jCBPosition.requestFocus();
	}
	
	private void toggleEntireSeason() {
		entireSeason = !entireSeason;
		jLblFrom.setVisible(!entireSeason);
		jCBAtClubSinceDay.setVisible(!entireSeason);
		jCBAtClubSinceMonth.setVisible(!entireSeason);
		jCBAtClubSinceYear.setVisible(!entireSeason);
		jLblTo.setVisible(!entireSeason);
		jCBAtClubUntilDay.setVisible(!entireSeason);
		jCBAtClubUntilMonth.setVisible(!entireSeason);
		jCBAtClubUntilYear.setVisible(!entireSeason);
	}
	
	private void refreshSinceDayModel() {
		int month = jCBAtClubSinceMonth.getSelectedIndex() + 1, year = Integer.parseInt((String) jCBAtClubSinceYear.getSelectedItem());
		int daysInMonth = numberOfDaysInMonth(month, year);
		String[] days = new String[daysInMonth];
		for (int i = 1; i <= days.length; i++) {
			days[i - 1] = (i / 10) + "" + (i % 10) + ".";
		}
		int day = jCBAtClubSinceDay.getSelectedIndex();
		jCBAtClubSinceDay.setModel(new DefaultComboBoxModel<>(days));
		jCBAtClubSinceDay.setSelectedIndex(Math.min(day, days.length - 1));
	}
	
	private void refreshUntilDayModel() {
		int month = jCBAtClubUntilMonth.getSelectedIndex() + 1, year = Integer.parseInt((String) jCBAtClubUntilYear.getSelectedItem());
		int daysInMonth = numberOfDaysInMonth(month, year);
		String[] days = new String[daysInMonth];
		for (int i = 1; i <= days.length; i++) {
			days[i - 1] = (i / 10) + "" + (i % 10) + ".";
		}
		int day = jCBAtClubUntilDay.getSelectedIndex();
		jCBAtClubUntilDay.setModel(new DefaultComboBoxModel<>(days));
		jCBAtClubUntilDay.setSelectedIndex(Math.min(day, days.length - 1));
	}
	
	private void saveAffiliation() {
		Position position = Position.getPositionFromString((String) jCBPosition.getSelectedItem());
		int squadNumber = 0;
		try {
			squadNumber = Integer.parseInt(jTFSquadNumber.getText());
		} catch(NumberFormatException nfe) {
			message("Bitte eine gültige Rückennummer angeben.");
			return;
		}
		Dauer duration = null;
		if (!entireSeason) {
			Datum firstDate = new Datum(jCBAtClubSinceDay.getSelectedIndex() + 1, jCBAtClubSinceMonth.getSelectedIndex() + 1, Integer.parseInt((String) jCBAtClubSinceYear.getSelectedItem()));
			Datum lastDate = new Datum(jCBAtClubUntilDay.getSelectedIndex() + 1, jCBAtClubUntilMonth.getSelectedIndex() + 1, Integer.parseInt((String) jCBAtClubUntilYear.getSelectedItem()));
			if (!seasonDuration.includes(firstDate) || !seasonDuration.includes(lastDate)) {
				message("Die Daten müssen im Bereich der Dauer der Saison (" + seasonDuration.withDividers() + ") liegen!");
				return;
			}
			duration = new Dauer(firstDate, lastDate);
		} else {
			duration = seasonDuration;
		}
		for (Spieler player : team.getPlayers()) {
			if (player.hasUsedSquadNumber(squadNumber, team, duration)) {
				message("Diese Rückennummer kann nicht verwendet werden, da sie bereits einem anderen Spieler zugeteilt ist.");
				return;
			}
		}
		
		TeamAffiliation affiliation = new TeamAffiliation(team, selectedPlayer, position, squadNumber, duration);
		if (selectedPlayer.hasConflictingTeamAffiliation(affiliation, duration)) {
			message("Der Spieler steht im genannten Zeitraum bei einem anderen Verein unter Vertrag!");
			return;
		}
		
		selectedPlayer.addTeamAffiliation(affiliation);
		team.addAffiliation(affiliation);
		team.playerUpdated();
		
		jLblPlayer.setVisible(false);
		jLblSelectedPlayer.setVisible(false);
		jLblPosition.setVisible(false);
		jCBPosition.setVisible(false);
		jLblSquadNumber.setVisible(false);
		jTFSquadNumber.setVisible(false);
		jLblDuration.setVisible(false);
		jChBEntireSeason.setVisible(false);
		jLblFrom.setVisible(false);
		jCBAtClubSinceDay.setVisible(false);
		jCBAtClubSinceMonth.setVisible(false);
		jCBAtClubSinceYear.setVisible(false);
		jLblTo.setVisible(false);
		jCBAtClubUntilDay.setVisible(false);
		jCBAtClubUntilMonth.setVisible(false);
		jCBAtClubUntilYear.setVisible(false);
		jBtnSaveAffiliation.setVisible(false);
		
		jBtnAddAffiliation.setEnabled(true);
		jBtnAddPlayer.setEnabled(true);
		jLblKaderMoreLess.setVisible(true);
		
		showKader();
	}
	
	private void addPlayer() {
		playerInformation.addPlayer(team);
		playerInformation.setVisible(true);
	}
	
	private void fillLabels() {
		String name = team.getName();
		ArrayList<String[]> allMatches = team.getCompetition().getAllMatches(team);
		
		for (int i = 0; i < jLblsAllMatches.size(); i++) {
			for (int j = 0; j < NUMBEROFFIELDSSPPL; j++) {
				jLblsAllMatches.get(i)[j].setVisible(false);
			}
		}
		for (int i = 0; i < jLblsMainCategory.size(); i++) {
			jLblsMainCategory.get(i).setVisible(false);
		}
		for (int i = 0; i < jLblsSubCategory.size(); i++) {
			jLblsSubCategory.get(i).setVisible(false);
		}
		jLblsAllMatches.clear();
		jLblsMainCategory.clear();
		jLblsSubCategory.clear();
		
		if (team.getCompetition() instanceof LigaSaison) {
			int countMatches = 0;
			for (int i = 0; i < matchdayOrder.size(); i++) {
				String[] match = allMatches.get(matchdayOrder.get(i));
				final int x = countMatches;
				JLabel[] labels = new JLabel[NUMBEROFFIELDSSPPL];
				jLblsAllMatches.add(labels);
				for (int j = 0; j < NUMBEROFFIELDSSPPL; j++) {
					labels[j] = new JLabel();
					jPnlMatches.add(labels[j]);
					labels[j].setBounds(marginMatches + diffsX[j], marginMatches + countMatches * (height + gapy) + (countMatches / (numberOfMatchdays / 2)) * middleGapY, widthes[j], height);
				}
				
				alignCenter(labels[MATCHDAY]);
				alignCenter(labels[DATE]);
				alignRight(labels[TEAMHOME]);
				alignCenter(labels[GOALSHOME]);
				alignCenter(labels[TRENNZEICHEN]);
				alignCenter(labels[GOALSAWAY]);
				alignLeft(labels[TEAMAWAY]);
				
				labels[MATCHDAY].setText(match[0]);
				labels[DATE].setText(match[1]);
				labels[TEAMHOME].setText(match[2]);
				labels[GOALSHOME].setText(match[3]);
				labels[TRENNZEICHEN].setText(":");
				labels[GOALSAWAY].setText(match[4]);
				labels[TEAMAWAY].setText(match[5]);
				
				labels[MATCHDAY].setCursor(handCursor);
				labels[MATCHDAY].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showMatchday(x);
					}
				});
				labels[TEAMHOME].setCursor(handCursor);
				labels[TEAMHOME].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showTeam(x, true);
					}
				});
				labels[TEAMAWAY].setCursor(handCursor);
				labels[TEAMAWAY].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						showTeam(x, false);
					}
				});
				
				labels[TEAMHOME].setBackground(colorHighlighted);
				labels[TEAMAWAY].setBackground(colorHighlighted);
				labels[TEAMHOME].setOpaque(name.equals(match[2]));
				labels[TEAMAWAY].setOpaque(name.equals(match[5]));
				
				if (match[6] != RESULT_NOT_SET) {
					Color color = (match[6] == DRAW ? Color.white : (match[6] == WIN ? Color.green : Color.red));
					labels[GOALSHOME].setBackground(color);
					labels[TRENNZEICHEN].setBackground(color);
					labels[GOALSAWAY].setBackground(color);
					labels[GOALSHOME].setOpaque(true);
					labels[TRENNZEICHEN].setOpaque(true);
					labels[GOALSAWAY].setOpaque(true);
				}
				
				countMatches++;
			}
			
			jPnlMatches.setBounds(marginX, marginY + 30, widthMatches, 2 * marginMatches + countMatches * height + (countMatches - 1) * gapy + middleGapY);
		} else {
			int countRows = 0, countMatches = 0;
			for (String[] match : allMatches) {
				if (match[0].equals(MAIN_CATEGORY)) {
					if (countRows != 0)	countRows++;
					
					JLabel label = new JLabel();
					jPnlMatches.add(label);
					label.setBounds(marginMatches, marginMatches + countRows * (height + gapy), widthMainCat, height);
					label.setText(match[1]);
					label.setFont(fontMainCategory);
					jLblsMainCategory.add(label);
					
					countRows++;
				} else if (match[0].equals(SUB_CATEGORY)) {
					JLabel label = new JLabel();
					jPnlMatches.add(label);
					label.setBounds(marginMatches, marginMatches + countRows * (height + gapy), widthSubCat, height);
					label.setText(match[1]);
					jLblsSubCategory.add(label);
					
					countRows++;
				} else {
					final int x = countMatches;
					JLabel[] labels = new JLabel[NUMBEROFFIELDSSPPL];
					jLblsAllMatches.add(labels);
					for (int i = 0; i < NUMBEROFFIELDSSPPL; i++) {
						labels[i] = new JLabel();
						jPnlMatches.add(labels[i]);
						labels[i].setBounds(marginMatches + diffsX[i], marginMatches + countRows * (height + gapy), widthes[i], height);
					}
					
					alignCenter(labels[MATCHDAY]);
					alignCenter(labels[DATE]);
					alignRight(labels[TEAMHOME]);
					alignCenter(labels[GOALSHOME]);
					alignCenter(labels[TRENNZEICHEN]);
					alignCenter(labels[GOALSAWAY]);
					alignLeft(labels[TEAMAWAY]);
					
					labels[MATCHDAY].setText(match[0]);
					labels[DATE].setText(match[1]);
					labels[TEAMHOME].setText(match[2]);
					labels[GOALSHOME].setText(match[3]);
					labels[TRENNZEICHEN].setText(":");
					labels[GOALSAWAY].setText(match[4]);
					labels[TEAMAWAY].setText(match[5]);
					labels[TEAMHOME].setCursor(handCursor);
					labels[TEAMHOME].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							showTeam(x, true);
						}
					});
					labels[TEAMAWAY].setCursor(handCursor);
					labels[TEAMAWAY].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							showTeam(x, false);
						}
					});
					
					labels[TEAMHOME].setBackground(colorHighlighted);
					labels[TEAMAWAY].setBackground(colorHighlighted);
					labels[TEAMHOME].setOpaque(name.equals(match[2]));
					labels[TEAMAWAY].setOpaque(name.equals(match[5]));
					
					if (match[6] != RESULT_NOT_SET) {
						Color color = (match[6] == DRAW ? Color.white : (match[6] == WIN ? Color.green : Color.red));
						labels[GOALSHOME].setBackground(color);
						labels[TRENNZEICHEN].setBackground(color);
						labels[GOALSAWAY].setBackground(color);
						labels[GOALSHOME].setOpaque(true);
						labels[TRENNZEICHEN].setOpaque(true);
						labels[GOALSAWAY].setOpaque(true);
					}
					
					countRows++;
					countMatches++;
				}
				
			}
			jPnlMatches.setBounds(marginX, marginY + 30, widthMatches, 2 * marginMatches + countRows * height + (countRows - 1) * gapy);
		}
	}
	
	public void showStatistics() {
		if (noStats && showingMoreStats)	showMoreLessStatistics();
		jLblNoStatistics.setVisible(noStats);
		jLblMatchesPlayedVal.setVisible(!noStats);
		jLblMatchesPlayed.setVisible(!noStats);
		jLblMatchesWonVal.setVisible(!noStats);
		jLblMatchesWon.setVisible(!noStats);
		jLblMatchesDrawnVal.setVisible(!noStats);
		jLblMatchesDrawn.setVisible(!noStats);
		jLblMatchesLostVal.setVisible(!noStats);
		jLblMatchesLost.setVisible(!noStats);
		jLblGoalsScoredVal.setVisible(!noStats);
		jLblGoalsScored.setVisible(!noStats);
		jLblGoalsConcededVal.setVisible(!noStats);
		jLblGoalsConceded.setVisible(!noStats);
		jLblBookedVal.setVisible(!noStats);
		jLblBooked.setVisible(!noStats);
		jLblBookedTwiceVal.setVisible(!noStats);
		jLblBookedTwice.setVisible(!noStats);
		jLblRedCardsVal.setVisible(!noStats);
		jLblRedCards.setVisible(!noStats);
		jLblStatisticsMoreLess.setVisible(!noStats);
		jLblSeries.setVisible(!noStats);
		for (int i = 0; i < NUMBER_OF_SERIES; i++) {
			jLblsSeries[i].setVisible(!noStats);
			jLblsSeriesValues[i].setVisible(!noStats);
		}
		
		if (jLblsResultsTeams != null) {
			for (int i = 0; i < jLblsResultsTeams.length; i++) {
				jLblsResultsTeams[i].setVisible(false);
				for (int j = 0; j < jLblsResultsValues[i].length; j++) {
					jLblsResultsValues[i][j].setVisible(false);
				}
			}
		}
		if (!noStats) {
			jLblsResultsTeams = new JLabel[teams.length];
			jLblsResultsValues = new JLabel[teams.length][team.getCompetition().getNumberOfMatchesAgainstSameOpponent()];
			for (int i = 0; i < jLblsResultsTeams.length; i++) {
				jLblsResultsTeams[i] = new JLabel();
				jPnlStatistics.add(jLblsResultsTeams[i]);
				jLblsResultsTeams[i].setBounds(results[STARTX], results[STARTY] + i * results[GAPY], results[SIZEX], results[SIZEY]);
				jLblsResultsTeams[i].setVisible(!noStats);
				
				for (int j = 0; j < jLblsResultsValues[i].length; j++) {
					jLblsResultsValues[i][j] = new JLabel();
					jPnlStatistics.add(jLblsResultsValues[i][j]);
					jLblsResultsValues[i][j].setBounds(resultsV[STARTX] + j * resultsV[GAPX], resultsV[STARTY] + i * resultsV[GAPY], resultsV[SIZEX], resultsV[SIZEY]);
					alignCenter(jLblsResultsValues[i][j]);
					jLblsResultsValues[i][j].setOpaque(true);
					jLblsResultsValues[i][j].setVisible(!noStats);
				}
			}
		}
		
		if (!noStats) {
			int matchday = team.getCompetition().getNewestStartedMatchday();
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
					else if (points == -2)	jLblsResultsValues[place][j].setBackground(Color.gray);
					else					jLblsResultsValues[place][j].setBackground(null);
					jLblsResultsValues[place][j].setText(split[1]);
				}
			}
		}
	}
	
	public void showTableExcerpt() {
		jLblNoTableExcerpt.setVisible(noTable);
		if (rankingHistory != null)	rankingHistory.setVisible(!noTable);
		jLblBackground.setVisible(!noTable);
		for (int i = 0; i < 10; i++) {
			jLblsTableHeader[i].setVisible(!noTable);
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				jLblsTableExcerpt[i][j].setVisible(!noTable);
			}
		}
		
		if (!noTable) {
			int newestMatchday = team.getCompetition().getNewestStartedMatchday();
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
			rankingHistory = new Tabellenverlauf(teams.length, places, team.getCompetition());
			jPnlTableExcerpt.add(rankingHistory);
			rankingHistory.setLocation(10, 130);
		}
	}
}
