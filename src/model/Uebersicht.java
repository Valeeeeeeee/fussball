package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import analyse.SaisonPerformance;
import dto.fixtures.SpielplanZeileDTO;
import dto.statistics.TeamSaisonStatistikDTO;
import util.Intervall;
import util.MyDocumentListener;
import view.fixtures.Spielplan;
import view.statistics.TeamSaisonStatistik;

import static util.Utilities.*;

public class Uebersicht extends JPanel {
	private static final long serialVersionUID = 5249833173928569240L;
	
	private static final int totalWidth = 1150;
	private static final int marginX = 5;
	private static final int widthFixtures = 585;
	private static final int marginMiddle = 5;
	private static final int widthStatistics = 550;
	private static final int marginY = 5;
	
	private static final int startXStatistics = 595;
	private static final int startYStatistics = 120;
	
	private Rectangle REC_BTNBACK = new Rectangle(5, 5, 100, 25);
	private Rectangle REC_LBLSORTBYDATE = new Rectangle(140, 10, 145, 15);
	private Rectangle REC_LBLONLYHOME = new Rectangle(310, 10, 105, 15);
	private Rectangle REC_LBLONLYAWAY = new Rectangle(440, 10, 130, 15);
	
	// Informationen
	private Rectangle REC_INFPNL = new Rectangle(startXStatistics, 35, widthStatistics, 80);
	private Rectangle REC_LBLNAME = new Rectangle(90, 10, 370, 30);
	private Rectangle REC_LBLFOUDATE = new Rectangle(200, 40, 150, 30);
	
	private int[] bndsSuggestions = new int[] {175, 70, 0, 30, 200, 20};
	
	private Rectangle REC_TABLEPNL = new Rectangle(startXStatistics, 240, widthStatistics, 290);
	private Rectangle REC_LBLNOTABLE = new Rectangle(25, 35, 350, 25);
	
	private Rectangle REC_LBLAVERAGEAGE = new Rectangle(20, 130, 125, 20);
	private Rectangle REC_LBLAVERAGEAGEVAL = new Rectangle(20, 155, 80, 20);
	private Rectangle REC_LBLNOKADER = new Rectangle(25, 35, 370, 25);
	private Rectangle REC_BTNADDAFFILIATION = new Rectangle(55, 5, 170, 25);
	private Rectangle REC_BTNADDPLAYER = new Rectangle(265, 5, 120, 25);
	private Rectangle REC_BTNCANCEL = new Rectangle(415, 5, 110, 25);
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
	private Color colorOptions = new Color(0, 192, 255);
	private String[] tableHeaders = {"Pl.", "Verein", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
	private String[] kaderHeaders = {"", "", "", "Sp.", "T", "V", "G", "GR", "R"};
	private String[] kaderTooltips = {"", "", "", "Spiele", "Tore", "Vorlagen", "Gelbe Karten", "Gelb-rote Karten", "Rote Karten"};
	private String[] positions = {"Tor", "Abwehr", "Mittelfeld", "Sturm"};
	private String[] positionsPlayer = {"Torhüter", "Verteidiger", "Mittelfeldspieler", "Stürmer"};
	
	private JButton jBtnBack;
	
	private JLabel jLblShowOnlyHome;
	private JLabel jLblShowOnlyAway;
	private JLabel jLblSortByDate;
	private Spielplan fixtures;
	
	private JPanel jPnlInformation;
	private JLabel jLblTeamName;
	private JLabel jLblFoundingDate;
	
	private TeamSaisonStatistik teamSeasonStatistics;
	
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
	private JButton jBtnCancelAffiliation;
	private JButton jBtnSaveAffiliation;

	private SpielerInformationen playerInformation;
	
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
	
	private static final int numberOfTeamsInTableExcerpt = 5;
	
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
	
	private int standardHeightKader = 185;
	private int standardHeightKaderNoPlayers = 100;
	
	private boolean hasFoundingDate;
	
	private int teamID;
	private Mannschaft team;
	private ArrayList<Mannschaft> teams;
	private ArrayList<Integer> matchdayOrder = new ArrayList<>();
	
	private Dauer seasonDuration;
	private Datum date;
	
	private boolean noTable;
	
	private boolean canHaveKader;
	private ArrayList<TeamAffiliation> eligiblePlayers;
	private ArrayList<TeamAffiliation> ineligiblePlayers;
	private int numberOfMatchdays;
	private int numberOfEligiblePlayers;
	private int numberOfIneligiblePlayers;
	private int numberOfPositions;
	private boolean showingFixturesInChronologicalOrder;
	private boolean showingMoreStats;
	private boolean showingMoreKader;
	
	private int maximumSuggestions = 15;
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
			for (int i = 0; i < numberOfTeamsInTableExcerpt; i++) {
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
				jBtnCancelAffiliation = new JButton();
				jPnlKader.add(jBtnCancelAffiliation);
				jBtnCancelAffiliation.setText("abbrechen");
				jBtnCancelAffiliation.setBounds(REC_BTNCANCEL);
				jBtnCancelAffiliation.setFocusable(false);
				jBtnCancelAffiliation.setVisible(false);
				jBtnCancelAffiliation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cancelAffiliation();
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
			TeamAffiliation affiliation = eligiblePlayers.get(i);
			Spieler player = affiliation.getPlayer();
			sumOfAges += player.getAge();
			SaisonPerformance seasonPerformance = affiliation.getSeasonPerformance();
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
			TeamAffiliation affiliation = ineligiblePlayers.get(i);
			Spieler player = affiliation.getPlayer();
			SaisonPerformance seasonPerformance = affiliation.getSeasonPerformance();
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
		jLblNoKader.setVisible(!hasPlayers);
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
		else										player = ineligiblePlayers.get(playerIndex - numberOfEligiblePlayers).getPlayer();
		
		playerInformation.setPlayer(player, team);
		playerInformation.setVisible(true);
	}
	
	public void determineSize() {
		int minimumheight = 725;
		int maximumheight = 870;
		Dimension dim = new Dimension();
		dim.width = totalWidth;
		dim.height = marginY + 30 + fixtures.getSize().height + marginY;
		if (dim.height < minimumheight) {
			dim.height = minimumheight;
		} else if (dim.height > maximumheight) {
			dim.height = maximumheight;
		}
		setSize(dim);
		int startY = showingMoreKader ? 120 : 535;
		jSPKader.setBounds(marginX + widthFixtures + marginMiddle, startY, widthStatistics, dim.height - startY - marginY);
		teamSeasonStatistics.setMaxHeight(getHeight() - 125);
	}
	
	public void setMannschaft(Mannschaft team) {
		if (showingMoreKader)	showMoreLessFromKader();
		teamID = team.getId();
		this.team = team;
		teams = team.getCompetition().getTeams();
		seasonDuration = team.getCompetition().getDuration();
		playerInformation.setSeasonDuration(seasonDuration);
		date = team.getTodayWithinSeasonBounds();
		numberOfMatchdays = team.getCompetition().getNumberOfRegularMatchdays();
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
		noTable = team.getCompetition() instanceof KORunde;
		
		String[] possibleYears = new String[seasonDuration.getToDate().getYear() - seasonDuration.getFromDate().getYear() + 1];
		for (int i = 0; i < possibleYears.length; i++) {
			possibleYears[i] = seasonDuration.getFromDate().getYear() + i + "";
		}
		jCBAtClubSinceYear.setModel(new DefaultComboBoxModel<>(possibleYears));
		jCBAtClubUntilYear.setModel(new DefaultComboBoxModel<>(possibleYears));
		
		showingFixturesInChronologicalOrder = false;
		showFixtures();
		showStatistics();
		showTableExcerpt();
		determineSize();
	}
	
	private void showOnlyHomeMatches() {
		fixtures.showOnlyHomeMatches();
	}
	
	private void showOnlyAwayMatches() {
		fixtures.showOnlyAwayMatches();
	}
	
	private void showAllMatches() {
		fixtures.showAllMatches();
	}
	
	private void sortByDate() {
		showingFixturesInChronologicalOrder = true;
		showFixtures();
		determineSize();
	}
	
	private void sortByMatchday() {
		showingFixturesInChronologicalOrder = false;
		showFixtures();
		determineSize();
	}
	
	public void showMoreLessStatistics() {
		showingMoreStats = !showingMoreStats;
		
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
		int startY = showingMoreKader ? 120 : 535;
		jSPKader.setBounds(marginX + widthFixtures + marginMiddle, startY, widthStatistics, getHeight() - startY - marginY);
		teamSeasonStatistics.setVisible(!showingMoreKader);
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
		jBtnCancelAffiliation.setVisible(true);
		
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
			label.setText(suggestions.get(i).getFullNameShort() + " (" + suggestions.get(i).getBirthDate().withDividers() + ")");
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
		
		jBtnCancelAffiliation.setVisible(false);
		jBtnSaveAffiliation.setVisible(true);
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
	
	private void cancelAffiliation() {
		jLblPlayer.setVisible(false);
		jTFSearchPlayer.setVisible(false);
		jBtnCancelAffiliation.setVisible(false);
		
		for (int i = 0; i < jLblsSuggestions.size(); i++) {
			jLblsSuggestions.get(i).setVisible(false);
		}
		
		jBtnAddAffiliation.setEnabled(true);
		jBtnAddPlayer.setEnabled(true);
		jLblKaderMoreLess.setVisible(true);
		
		showKader();
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
	
	private void showFixtures() {
		if (fixtures != null) {
			fixtures.setVisible(false);
			remove(fixtures);
		}
		
		ArrayList<SpielplanZeileDTO> allMatches = team.getCompetition().getAllMatches(team, showingFixturesInChronologicalOrder);
		fixtures = Spielplan.of(allMatches, team);
		add(fixtures);
		fixtures.setLocation(marginX, marginY + 30);
	}
	
	public void showStatistics() {
		if (teamSeasonStatistics != null) {
			teamSeasonStatistics.setVisible(false);
			remove(teamSeasonStatistics);
		}
		
		teamSeasonStatistics = TeamSaisonStatistik.of(TeamSaisonStatistikDTO.of(team), this, showingMoreStats);
		add(teamSeasonStatistics);
		teamSeasonStatistics.setLocation(startXStatistics, startYStatistics);
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
			team.getCompetition().getTable().calculate(newestMatchday, Tabellenart.COMPLETE);
			int anzahlMannschaften = teams.size();
			int[] tabelle = new int[anzahlMannschaften];
			
			int nextPlace = 0, thisTeamsPosition = 0;
			for (int i = 0; i < anzahlMannschaften; i++) {
				for (Mannschaft ms : teams) {
					if (ms.getPlace() == i) {
						tabelle[nextPlace] = ms.getId();
						if (ms.getId() == teamID)	thisTeamsPosition = nextPlace;
						nextPlace++;
					}
				}
			}
			
			Intervall positions = getPositionsToShow(thisTeamsPosition);
			
			int index = 0;
			for (int position = positions.lower(); position <= positions.upper(); position++) {
				boolean thisTeam = position == thisTeamsPosition;
				Mannschaft team = teams.get(tabelle[position] - 1);
				for (int j = 0; j < 10; j++) {
					jLblsTableExcerpt[index][j].setText(team.getString(j));
				}
				
				if (thisTeam)	jLblBackground.setBounds(teStartX, teStartY + (index + 1) * (teHeight + teGapY), 530, teHeight);
				index++;
			}
			while (index < numberOfTeamsInTableExcerpt) {
				for (int j = 0; j < 10; j++) {
					jLblsTableExcerpt[index][j].setText("");
				}
				index++;
			}
			
			int[] positionsTrend = team.getCompetition().getTable().getPositionsOfTeam(teamID, newestMatchday);
			
			if (rankingHistory != null)	rankingHistory.setVisible(false);
			rankingHistory = new Tabellenverlauf(teams.size(), positionsTrend, team.getCompetition());
			jPnlTableExcerpt.add(rankingHistory);
			rankingHistory.setLocation(10, 130);
		}
	}
	
	private Intervall getPositionsToShow(int positionOfTeam) {
		int firstPosition = positionOfTeam - 2, lastPosition = positionOfTeam + 2;
		
		while (firstPosition < 0) {
			firstPosition++;
			lastPosition++;
		}
		while (lastPosition >= teams.size()) {
			lastPosition--;
			firstPosition = Math.max(0, firstPosition - 1);
		}
		
		return new Intervall(firstPosition, lastPosition);
	}
}
