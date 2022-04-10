package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class SpielInformationen extends JFrame {
	private static final long serialVersionUID = 7503825008840407522L;
	
	private static final int WIDTH = 950;
	private static final int HEIGHT = 600;
	
	private int matchIndex;
	private Spiel match;
	
	private JPanel jPnlMatchInformation;
	
	private JLabel jLblCompetition;
	private JLabel jLblDate;
	
	private JLabel jLblHomeTeamName;
	private JLabel jLblResult;
	private JLabel jLblZusatz;
	private JLabel jLblAwayTeamName;
	private JLabel jLblReferee;
	private JComboBox<String> jCBReferees;
	
	private JButton jBtnStartMatch;
	private JButton jBtnLineupHome;
	private JButton jBtnLineupAway;
	private JButton jBtnSubstitutionHome;
	private JButton jBtnSubstitutionAway;
	private JButton jBtnGoalHome;
	private JButton jBtnGoalAway;
	private JButton jBtnBookingHome;
	private JButton jBtnBookingAway;
	private JButton jBtnAGTHome;
	private JButton jBtnAGTAway;
	private JButton jBtnPenaltyShootout;
	
	private JPanel jPnlLineupSelection;
	private JLabel[] jLblsLineupSelectionPlayers;
	private JLabel jLblNumberOfSelectedPlayers;
	private JButton jBtnLineupSelectionCancel;
	private JButton jBtnLineupSelectionCompleted;
	
	private JLabel[] jLblsPlayersHome;
	private JLabel[] jLblsPlayersAway;
	private JLabel[] jLblsSquadNumbersHome;
	private JLabel[] jLblsSquadNumbersAway;
	private JLabel[] jLblsBookingsHome;
	private JLabel[] jLblsBookingsAway;
	private JLabel[] jLblsSecBookingsHome;
	private JLabel[] jLblsSecBookingsAway;
	private JLabel[] jLblsSubsOffMinutesHome;
	private JLabel[] jLblsSubsOffMinutesAway;
	private JLabel[] jLblsSubsOnMinutesHome;
	private JLabel[] jLblsSubsOnMinutesAway;
	private ArrayList<JLabel> jLblsGoals = new ArrayList<>();
	
	private JPanel jPnlEntry;
	private JButton jBtnEntryCancelled;
	private JButton jBtnEntryCompleted;
	private JLabel jLblMinute;
	private JTextField jTFMinute;
	private JCheckBox jChBLeft;
	private JCheckBox jChBRight;
	private ButtonGroup buttonGroupDetails;
	private JCheckBox jChBBench;
	private JCheckBox jChBAfterMatch;
	private JLabel jLblTop;
	private JComboBox<String> jCBTop;
	private JLabel jLblBottom;
	private JComboBox<String> jCBBottom;
	
	private JPanel jPnlPenalties;
	private JLabel jLblPenalties;
	private ArrayList<JLabel> jLblsPenaltiesHome;
	private ArrayList<JLabel> jLblsPenaltiesAway;
	private JButton jBtnPenaltiesCompleted;
	
	// Obere Labels
	private Rectangle REC_PNLMATCHINFO = new Rectangle(0, 0, WIDTH, HEIGHT);
	private Rectangle REC_LBLCOMPETITION = new Rectangle(275, 10, 400, 20);
	private Rectangle REC_LBLDATE = new Rectangle(415, 35, 120, 25);
	private Rectangle REC_LBLHOMENAME = new Rectangle(100, 60, 330, 40);
	private Rectangle REC_LBLRESULT = new Rectangle(435, 60, 80, 40);
	private Rectangle REC_LBLZUSATZ = new Rectangle(445, 90, 60, 20);
	private Rectangle REC_LBLAWAYNAME = new Rectangle(520, 60, 330, 40);
	private Rectangle REC_LBLREFEREE = new Rectangle(425, 535, 100, 20);
	private Rectangle REC_CBREFEREES = new Rectangle(375, 560, 200, 25);
	private Rectangle REC_BTNAGTHOME = new Rectangle(335, 35, 70, 25);
	private Rectangle REC_BTNAGTAWAY = new Rectangle(545, 35, 70, 25);
	
	// Untere Button-Reihe
	private Rectangle REC_BTNSTARTMATCH = new Rectangle(425, 105, 100, 35);
	private Rectangle REC_BTNLINEUPHOME = new Rectangle(45, 105, 125, 35);
	private Rectangle REC_BTNLINEUPAWAY = new Rectangle(780, 105, 125, 35);
	private Rectangle REC_BTNSUBSTITUTIONHOME = new Rectangle(175, 105, 105, 35);
	private Rectangle REC_BTNSUBSTITUTIONAWAY = new Rectangle(670, 105, 105, 35);
	private Rectangle REC_BTNGOALHOME = new Rectangle(375, 105, 75, 35);
	private Rectangle REC_BTNGOALAWAY = new Rectangle(500, 105, 75, 35);
	private Rectangle REC_BTNBOOKINGHOME = new Rectangle(285, 105, 85, 35);
	private Rectangle REC_BTNBOOKINGAWAY = new Rectangle(580, 105, 85, 35);
	private Rectangle REC_BTNPENALTIES = new Rectangle(400, 170, 150, 25);
	
	// Labels Aufstellung, Wechsel, Tore, Karten
	private int[] subMinsLbls = new int[] {85, 160, 715, 25, 65, 20};
	private int[] luLbls = new int[] {155, 160, 515, 25, 125, 20};
	private int[] sqNumLbls = new int[] {285, 160, 360, 25, 20, 20};
	private int[] bLbls = new int[] {310, 161, 316, 25, 14, 19};
	private int[] gLbls = new int[] {370, 160, 40, 25, 170, 20};

	private int bDeltaX = 10;
	private int bDeltaY = 2;
	
	// Eingabe
	private Point LOC_PNLENTRYHOME = new Point(175, 150);
	private Point LOC_PNLENTRYAWAY = new Point(485, 150);
	private Dimension DIM_PNLENTRY = new Dimension(290, 130);
	private Rectangle REC_BTNENTRYCANCL = new Rectangle(160, 5, 45, 30);
	private Rectangle REC_BTNENTRYCOMPL = new Rectangle(210, 5, 70, 30);
	private Rectangle REC_LBLMINUTE = new Rectangle(70, 10, 70, 20);
	private Rectangle REC_TFMINUTE = new Rectangle(10, 10, 60, 20);
	private Rectangle REC_CHBLEFT = new Rectangle(30, 40, 105, 20);
	private Rectangle REC_CHBRIGHT = new Rectangle(165, 40, 95, 20);
	private Rectangle REC_CHBBENCH = new Rectangle(20, 70, 110, 20);
	private Rectangle REC_CHBAFTERMATCH = new Rectangle(150, 70, 140, 20);
	private Rectangle REC_LBLTOP = new Rectangle(10, 70, 95, 20);
	private Rectangle REC_CBTOP = new Rectangle(105, 67, 175, 26);
	private Rectangle REC_LBLBOTTOM = new Rectangle(10, 100, 95, 20);
	private Rectangle REC_CBBOTTOM = new Rectangle(105, 97, 175, 26);
	
	// Lineup selection
	private Point LOC_PNLENTERLINEUPHOME = new Point(50, 150);
	private Point LOC_PNLENTERLINEUPAWAY = new Point(490, 150);
	private Dimension DIM_PNLENTERLINEUP = new Dimension(410, 330);
	private Rectangle REC_LBLNOFSELPLAYERS = new Rectangle(30, 295, 160, 20);
	private Rectangle REC_BTNELUCANCEL = new Rectangle(220, 290, 100, 30);
	private Rectangle REC_BTNELUCOMPL = new Rectangle(330, 290, 70, 30);
	
	// Penalties
	private Rectangle REC_PNLPENALTIES = new Rectangle(245, 120, 460, 120);
	private Rectangle REC_LBLPENALTIES = new Rectangle(10, 10, 130, 25);
	private Rectangle REC_BTNPENALTIESCOMPL = new Rectangle(380, 10, 70, 25);
	
	private int[] penaltyH = new int[] {20, 45, 35, 35, 30, 30};
	private int[] penaltyA = new int[] {270, 45, 35, 35, 30, 30};
	
	private Color background = new Color(224, 255, 224);
	private Color penaltiesBGColor = new Color(175, 255, 175);
	private Color penaltiesInColor = new Color(25, 255, 25);
	private Color penaltiesOutColor = new Color(255, 25, 25);
	private Color penaltiesNoColor = new Color(192, 192, 192);
	private Color lineupSelColor = new Color(175, 255, 175);
	private Color playerSelectedColor = new Color(255, 255, 0);
	private Color playerOffColor = new Color(224, 0, 0);
	private Color playerOnColor = new Color(0, 224, 0);
	private Color colorYellowCard = new Color(224, 224, 0);
	private Color colorRedCard = new Color(255, 0, 0);
	private Font fontTeamNames = new Font("Verdana", Font.PLAIN, 24);
	private static final int PENALTY_NO = 0;
	private static final int PENALTY_IN = 1;
	private static final int PENALTY_OUT = 2;
	
	private static final int numberOfPlayersInLineUp = 11;

	private int[] boundsLSP = new int[] {5, 5, 135, 21, 130, 20};
	private int playersPerColumn = 13;
	
	private Spieltag spieltag;
	private Ergebnis result;
	private Datum date;
	private ArrayList<TeamAffiliation> kaderHome;
	private ArrayList<TeamAffiliation> kaderAway;
	private ArrayList<TeamAffiliation> eligiblePlayersListUpper  = new ArrayList<>();
	private ArrayList<TeamAffiliation> eligiblePlayersListLower  = new ArrayList<>();
	private boolean[] playerSelected;
	private int numberOfSelectedPlayers;
	private int squadNumberToSelect;
	private boolean editingFirstTeam;
	private boolean enteringLineup;
	private boolean enteringGoal;
	private boolean enteringSubstitution;
	private boolean enteringBooking;
	private boolean bookedOnTheBench;
	private boolean bookedAfterTheMatch;
	private int goalDetails;
	private int[] lineupHome;
	private int[] lineupAway;
	private int maximumNumberOfSubstitutions;
	private int nOfRTSubsHome;
	private int nOfRTSubsAway;
	private ArrayList<Wechsel> substitutionsHome;
	private ArrayList<Wechsel> substitutionsAway;
	private ArrayList<Tor> goals;
	private ArrayList<Karte> bookings;
	private Tor changedGoal;
	private Wechsel changedSubstitution;
	private Karte changedBooking;
	private boolean repaint;
	private ArrayList<Integer> penaltiesHome;
	private ArrayList<Integer> penaltiesAway;
	private int latestPenalty;
	private int typed = -1;
	
	private boolean isETpossible = false;
	private boolean is4thSubPossible = false;
	private int maximumNumberOfSubstitutionsRT;
	
	private JButton jBtnGo;

	private Rectangle RECGO = new Rectangle(850, 10, 90, 40);
	
	public SpielInformationen(Spieltag spieltag, int matchIndex, Spiel match) {
		super();
		
		this.spieltag = spieltag;
		this.matchIndex = matchIndex;
		this.match = match;
		goals = match.getGoals();
		substitutionsHome = match.getSubstitutions(true);
		substitutionsAway = match.getSubstitutions(false);
		bookings = match.getBookings();
		this.result = match.getResult();
		isETpossible = match.getCompetition().isExtraTimePossible();
		date = match.getKickOffTime().getDate();
		maximumNumberOfSubstitutionsRT = match.getCompetition().getNumberOfRegularSubstitutions(date);
		is4thSubPossible = match.getCompetition().isFourthSubstitutionPossible();
		
		initGUI();
		displayGivenValues();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		maximumNumberOfSubstitutions = maximumNumberOfSubstitutionsRT + (is4thSubPossible ? 1 : 0);
		int maxNumOfPlayers = numberOfPlayersInLineUp + maximumNumberOfSubstitutions;
		jLblsPlayersHome = new JLabel[maxNumOfPlayers];
		jLblsPlayersAway = new JLabel[maxNumOfPlayers];
		jLblsSquadNumbersHome = new JLabel[maxNumOfPlayers];
		jLblsSquadNumbersAway = new JLabel[maxNumOfPlayers];
		jLblsBookingsHome = new JLabel[maxNumOfPlayers];
		jLblsBookingsAway = new JLabel[maxNumOfPlayers];
		jLblsSecBookingsHome = new JLabel[maxNumOfPlayers];
		jLblsSecBookingsAway = new JLabel[maxNumOfPlayers];
		jLblsSubsOffMinutesHome = new JLabel[maxNumOfPlayers];
		jLblsSubsOffMinutesAway = new JLabel[maxNumOfPlayers];
		jLblsSubsOnMinutesHome = new JLabel[maximumNumberOfSubstitutions];
		jLblsSubsOnMinutesAway = new JLabel[maximumNumberOfSubstitutions];
		jLblsPenaltiesHome = new ArrayList<>();
		jLblsPenaltiesAway = new ArrayList<>();
		penaltiesHome = new ArrayList<>();
		penaltiesAway = new ArrayList<>();
		
		{
			jPnlMatchInformation = new JPanel();
			getContentPane().add(jPnlMatchInformation);
			jPnlMatchInformation.setBounds(REC_PNLMATCHINFO);
			jPnlMatchInformation.setLayout(null);
			jPnlMatchInformation.setBackground(background);
		}
		{
			jLblCompetition = new JLabel();
			jPnlMatchInformation.add(jLblCompetition);
			jLblCompetition.setBounds(REC_LBLCOMPETITION);
			jLblCompetition.setText(match.getDescription());
			alignCenter(jLblCompetition);
		}
		{
			jLblDate = new JLabel();
			jPnlMatchInformation.add(jLblDate);
			jLblDate.setBounds(REC_LBLDATE);
			jLblDate.setText(match.getDateAndTime());
			alignCenter(jLblDate);
		}
		{
			jLblHomeTeamName = new JLabel();
			jPnlMatchInformation.add(jLblHomeTeamName);
			jLblHomeTeamName.setBounds(REC_LBLHOMENAME);
			jLblHomeTeamName.setFont(fontTeamNames);
			jLblHomeTeamName.setText(match.getHomeTeam().getName());
			alignRight(jLblHomeTeamName);
		}
		{
			jLblResult = new JLabel();
			jPnlMatchInformation.add(jLblResult);
			jLblResult.setBounds(REC_LBLRESULT);
			jLblResult.setFont(fontTeamNames);
			jLblResult.setText(result != null ? result.getResult() : "-:-");
			alignCenter(jLblResult);
		}
		{
			jLblZusatz = new JLabel();
			jPnlMatchInformation.add(jLblZusatz);
			jLblZusatz.setBounds(REC_LBLZUSATZ);
			jLblZusatz.setText(result != null ? result.getMore() : "");
			alignCenter(jLblZusatz);
		}
		{
			jLblAwayTeamName = new JLabel();
			jPnlMatchInformation.add(jLblAwayTeamName);
			jLblAwayTeamName.setBounds(REC_LBLAWAYNAME);
			jLblAwayTeamName.setFont(fontTeamNames);
			jLblAwayTeamName.setText(match.getAwayTeam().getName());
			alignLeft(jLblAwayTeamName);
		}
		{
			jLblReferee = new JLabel();
			jPnlMatchInformation.add(jLblReferee);
			jLblReferee.setBounds(REC_LBLREFEREE);
			jLblReferee.setText("Schiedsrichter:");
			alignCenter(jLblReferee);
		}
		{
			jCBReferees = new JComboBox<>();
			jPnlMatchInformation.add(jCBReferees);
			jCBReferees.setBounds(REC_CBREFEREES);
			jCBReferees.setFocusable(false);
			jCBReferees.setModel(new DefaultComboBoxModel<>(match.getCompetition().getAllReferees()));
			jCBReferees.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					jCBRefereesItemStateChanged(e);
				}
			});
		}
		
		
		{
			jBtnStartMatch = new JButton();
			jPnlMatchInformation.add(jBtnStartMatch);
			jBtnStartMatch.setBounds(REC_BTNSTARTMATCH);
			jBtnStartMatch.setText("Anpfiff");
			jBtnStartMatch.setFocusable(false);
			jBtnStartMatch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startMatch();
				}
			});
		}
		{
			jBtnLineupHome = new JButton();
			jPnlMatchInformation.add(jBtnLineupHome);
			jBtnLineupHome.setBounds(REC_BTNLINEUPHOME);
			jBtnLineupHome.setText("Aufstellung");
			jBtnLineupHome.setFocusable(false);
			jBtnLineupHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewLineup(true);
				}
			});
		}
		{
			jBtnLineupAway = new JButton();
			jPnlMatchInformation.add(jBtnLineupAway);
			jBtnLineupAway.setBounds(REC_BTNLINEUPAWAY);
			jBtnLineupAway.setText("Aufstellung");
			jBtnLineupAway.setFocusable(false);
			jBtnLineupAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewLineup(false);
				}
			});
		}
		{
			jBtnSubstitutionHome = new JButton();
			jPnlMatchInformation.add(jBtnSubstitutionHome);
			jBtnSubstitutionHome.setBounds(REC_BTNSUBSTITUTIONHOME);
			jBtnSubstitutionHome.setText("Wechsel");
			jBtnSubstitutionHome.setFocusable(false);
			jBtnSubstitutionHome.setVisible(false);
			jBtnSubstitutionHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewSubstitution(true);
				}
			});
		}
		{
			jBtnSubstitutionAway = new JButton();
			jPnlMatchInformation.add(jBtnSubstitutionAway);
			jBtnSubstitutionAway.setBounds(REC_BTNSUBSTITUTIONAWAY);
			jBtnSubstitutionAway.setText("Wechsel");
			jBtnSubstitutionAway.setFocusable(false);
			jBtnSubstitutionAway.setVisible(false);
			jBtnSubstitutionAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewSubstitution(false);
				}
			});
		}
		{
			jBtnGoalHome = new JButton();
			jPnlMatchInformation.add(jBtnGoalHome);
			jBtnGoalHome.setBounds(REC_BTNGOALHOME);
			jBtnGoalHome.setText("Tor");
			jBtnGoalHome.setFocusable(false);
			jBtnGoalHome.setVisible(false);
			jBtnGoalHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewGoal(true);
				}
			});
		}
		{
			jBtnGoalAway = new JButton();
			jPnlMatchInformation.add(jBtnGoalAway);
			jBtnGoalAway.setBounds(REC_BTNGOALAWAY);
			jBtnGoalAway.setText("Tor");
			jBtnGoalAway.setFocusable(false);
			jBtnGoalAway.setVisible(false);
			jBtnGoalAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewGoal(false);
				}
			});
		}
		{
			jBtnBookingHome = new JButton();
			jPnlMatchInformation.add(jBtnBookingHome);
			jBtnBookingHome.setBounds(REC_BTNBOOKINGHOME);
			jBtnBookingHome.setText("Karte");
			jBtnBookingHome.setFocusable(false);
			jBtnBookingHome.setVisible(false);
			jBtnBookingHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewBooking(true);
				}
			});
		}
		{
			jBtnBookingAway = new JButton();
			jPnlMatchInformation.add(jBtnBookingAway);
			jBtnBookingAway.setBounds(REC_BTNBOOKINGAWAY);
			jBtnBookingAway.setText("Karte");
			jBtnBookingAway.setFocusable(false);
			jBtnBookingAway.setVisible(false);
			jBtnBookingAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewBooking(false);
				}
			});
		}
		{
			jBtnAGTHome = new JButton();
			jPnlMatchInformation.add(jBtnAGTHome);
			jBtnAGTHome.setBounds(REC_BTNAGTHOME);
			jBtnAGTHome.setText("a.g.T.");
			jBtnAGTHome.setFocusable(false);
			jBtnAGTHome.setToolTipText("Sieg am grünen Tisch");
			jBtnAGTHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAmGruenenTisch(true);
				}
			});
		}
		{
			jBtnAGTAway = new JButton();
			jPnlMatchInformation.add(jBtnAGTAway);
			jBtnAGTAway.setBounds(REC_BTNAGTAWAY);
			jBtnAGTAway.setText("a.g.T.");
			jBtnAGTAway.setFocusable(false);
			jBtnAGTAway.setToolTipText("Sieg am grünen Tisch");
			jBtnAGTAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAmGruenenTisch(false);
				}
			});
		}
		{
			jBtnPenaltyShootout = new JButton();
			jPnlMatchInformation.add(jBtnPenaltyShootout);
			jBtnPenaltyShootout.setBounds(REC_BTNPENALTIES);
			jBtnPenaltyShootout.setText("Elfmeterschießen");
			jBtnPenaltyShootout.setFocusable(false);
			jBtnPenaltyShootout.setVisible(false);
			jBtnPenaltyShootout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startPenaltyShootout();
				}
			});
		}
		{
			jPnlPenalties = new JPanel();
			jPnlMatchInformation.add(jPnlPenalties);
			jPnlPenalties.setBounds(REC_PNLPENALTIES);
			jPnlPenalties.setLayout(null);
			jPnlPenalties.setBackground(penaltiesBGColor);
			jPnlPenalties.setVisible(false);
		}
		{
			jLblPenalties = new JLabel();
			jPnlPenalties.add(jLblPenalties);
			jLblPenalties.setBounds(REC_LBLPENALTIES);
			jLblPenalties.setText("Elfmeterschießen");
		}
		addPLabels(5);
		{
			jBtnPenaltiesCompleted = new JButton();
			jPnlPenalties.add(jBtnPenaltiesCompleted);
			jBtnPenaltiesCompleted.setBounds(REC_BTNPENALTIESCOMPL);
			jBtnPenaltiesCompleted.setText("fertig");
			jBtnPenaltiesCompleted.setFocusable(false);
			jBtnPenaltiesCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					finishPenaltyShootout();
				}
			});
		}
		for (int i = 0; i < maxNumOfPlayers; i++) {
			jLblsPlayersHome[i] = new JLabel();
			jPnlMatchInformation.add(jLblsPlayersHome[i]);
			jLblsPlayersHome[i].setLocation(luLbls[STARTX], luLbls[STARTY] + i * luLbls[GAPY]);
			jLblsPlayersHome[i].setSize(luLbls[SIZEX], luLbls[SIZEY]);
			alignRight(jLblsPlayersHome[i]);
			jLblsPlayersHome[i].setVisible(false);
			
			jLblsPlayersAway[i] = new JLabel();
			jPnlMatchInformation.add(jLblsPlayersAway[i]);
			jLblsPlayersAway[i].setLocation(luLbls[STARTX] + luLbls[GAPX], luLbls[STARTY] + i * luLbls[GAPY]);
			jLblsPlayersAway[i].setSize(luLbls[SIZEX], luLbls[SIZEY]);
			alignLeft(jLblsPlayersAway[i]);
			jLblsPlayersAway[i].setVisible(false);
			
			jLblsSquadNumbersHome[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSquadNumbersHome[i]);
			jLblsSquadNumbersHome[i].setLocation(sqNumLbls[STARTX], sqNumLbls[STARTY] + i * sqNumLbls[GAPY]);
			jLblsSquadNumbersHome[i].setSize(sqNumLbls[SIZEX], sqNumLbls[SIZEY]);
			alignCenter(jLblsSquadNumbersHome[i]);
			jLblsSquadNumbersHome[i].setVisible(false);
			
			jLblsSquadNumbersAway[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSquadNumbersAway[i]);
			jLblsSquadNumbersAway[i].setLocation(sqNumLbls[STARTX] + sqNumLbls[GAPX], sqNumLbls[STARTY] + i * sqNumLbls[GAPY]);
			jLblsSquadNumbersAway[i].setSize(sqNumLbls[SIZEX], sqNumLbls[SIZEY]);
			alignCenter(jLblsSquadNumbersAway[i]);
			jLblsSquadNumbersAway[i].setVisible(false);
			
			jLblsBookingsHome[i] = new JLabel();
			jPnlMatchInformation.add(jLblsBookingsHome[i]);
			jLblsBookingsHome[i].setLocation(bLbls[STARTX], bLbls[STARTY] + i * bLbls[GAPY]);
			jLblsBookingsHome[i].setSize(bLbls[SIZEX], bLbls[SIZEY]);
			
			jLblsBookingsAway[i] = new JLabel();
			jPnlMatchInformation.add(jLblsBookingsAway[i]);
			jLblsBookingsAway[i].setLocation(bLbls[STARTX] + bLbls[GAPX], bLbls[STARTY] + i * bLbls[GAPY]);
			jLblsBookingsAway[i].setSize(bLbls[SIZEX], bLbls[SIZEY]);
			
			jLblsSecBookingsHome[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSecBookingsHome[i]);
			jLblsSecBookingsHome[i].setLocation(bLbls[STARTX] + bLbls[SIZEX] + 1 - bDeltaX, bLbls[STARTY] + i * bLbls[GAPY] - bDeltaY);
			jLblsSecBookingsHome[i].setSize(bLbls[SIZEX], bLbls[SIZEY]);
			
			jLblsSecBookingsAway[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSecBookingsAway[i]);
			jLblsSecBookingsAway[i].setLocation(bLbls[STARTX] + bLbls[GAPX] - bLbls[SIZEX] - 1 + bDeltaX, bLbls[STARTY] + i * bLbls[GAPY] - bDeltaY);
			jLblsSecBookingsAway[i].setSize(bLbls[SIZEX], bLbls[SIZEY]);
		}
		for (int i = 0; i < maxNumOfPlayers; i++) {
			jLblsSubsOffMinutesHome[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSubsOffMinutesHome[i]);
			jLblsSubsOffMinutesHome[i].setLocation(subMinsLbls[STARTX] - 70, subMinsLbls[STARTY] + i * subMinsLbls[GAPY]);
			jLblsSubsOffMinutesHome[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignLeft(jLblsSubsOffMinutesHome[i]);
			jLblsSubsOffMinutesHome[i].setCursor(handCursor);
			jLblsSubsOffMinutesHome[i].setVisible(false);
			jLblsSubsOffMinutesHome[i].setForeground(playerOffColor);
			
			jLblsSubsOffMinutesAway[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSubsOffMinutesAway[i]);
			jLblsSubsOffMinutesAway[i].setLocation(subMinsLbls[STARTX] + subMinsLbls[GAPX] + 70, subMinsLbls[STARTY] + i * subMinsLbls[GAPY]);
			jLblsSubsOffMinutesAway[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignRight(jLblsSubsOffMinutesAway[i]);
			jLblsSubsOffMinutesAway[i].setCursor(handCursor);
			jLblsSubsOffMinutesAway[i].setVisible(false);
			jLblsSubsOffMinutesAway[i].setForeground(playerOffColor);
		}
		for (int i = 0; i < maximumNumberOfSubstitutions; i++) {
			jLblsSubsOnMinutesHome[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSubsOnMinutesHome[i]);
			jLblsSubsOnMinutesHome[i].setLocation(subMinsLbls[STARTX], subMinsLbls[STARTY] + (numberOfPlayersInLineUp + i) * subMinsLbls[GAPY]);
			jLblsSubsOnMinutesHome[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignLeft(jLblsSubsOnMinutesHome[i]);
			jLblsSubsOnMinutesHome[i].setCursor(handCursor);
			jLblsSubsOnMinutesHome[i].setVisible(false);
			jLblsSubsOnMinutesHome[i].setForeground(playerOnColor);
			
			jLblsSubsOnMinutesAway[i] = new JLabel();
			jPnlMatchInformation.add(jLblsSubsOnMinutesAway[i]);
			jLblsSubsOnMinutesAway[i].setLocation(subMinsLbls[STARTX] + subMinsLbls[GAPX], subMinsLbls[STARTY] + (numberOfPlayersInLineUp + i) * subMinsLbls[GAPY]);
			jLblsSubsOnMinutesAway[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignRight(jLblsSubsOnMinutesAway[i]);
			jLblsSubsOnMinutesAway[i].setCursor(handCursor);
			jLblsSubsOnMinutesAway[i].setVisible(false);
			jLblsSubsOnMinutesAway[i].setForeground(playerOnColor);
		}
		
		{
			jPnlLineupSelection = new JPanel();
			jPnlMatchInformation.add(jPnlLineupSelection);
			jPnlLineupSelection.setSize(DIM_PNLENTERLINEUP);
			jPnlLineupSelection.setLayout(null);
			jPnlLineupSelection.setBackground(lineupSelColor);
			jPnlLineupSelection.setVisible(false);
			jPnlLineupSelection.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() > 47 && e.getKeyChar() < 58) {
						squadNumberDigit(e.getKeyChar() - 48);
					} else if (e.getKeyChar() == 44) {
						squadNumberCompleted();
					}
				}
			});
		}
		{
			jLblNumberOfSelectedPlayers = new JLabel();
			jPnlLineupSelection.add(jLblNumberOfSelectedPlayers);
			jLblNumberOfSelectedPlayers.setBounds(REC_LBLNOFSELPLAYERS);
		}
		{
			jBtnLineupSelectionCompleted = new JButton();
			jPnlLineupSelection.add(jBtnLineupSelectionCompleted);
			jBtnLineupSelectionCompleted.setBounds(REC_BTNELUCOMPL);
			jBtnLineupSelectionCompleted.setText("fertig");
			jBtnLineupSelectionCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnEnterLineupCompletedActionPerformed();
				}
			});
		}
		{
			jBtnLineupSelectionCancel = new JButton();
			jPnlLineupSelection.add(jBtnLineupSelectionCancel);
			jBtnLineupSelectionCancel.setBounds(REC_BTNELUCANCEL);
			jBtnLineupSelectionCancel.setText("abbrechen");
			jBtnLineupSelectionCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnEnterLineupCancelActionPerformed();
				}
			});
		}
		
		{
			jPnlEntry = new JPanel();
			jPnlMatchInformation.add(jPnlEntry);
			jPnlEntry.setSize(DIM_PNLENTRY);
			jPnlEntry.setLayout(null);
			jPnlEntry.setBackground(lineupSelColor);
			jPnlEntry.setVisible(false);
		}
		{
			jBtnEntryCancelled = new JButton();
			jPnlEntry.add(jBtnEntryCancelled);
			jBtnEntryCancelled.setBounds(REC_BTNENTRYCANCL);
			jBtnEntryCancelled.setText("X");
			jBtnEntryCancelled.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnEntryCancelledActionPerformed();
				}
			});
		}
		{
			jBtnEntryCompleted = new JButton();
			jPnlEntry.add(jBtnEntryCompleted);
			jBtnEntryCompleted.setBounds(REC_BTNENTRYCOMPL);
			jBtnEntryCompleted.setText("fertig");
			jBtnEntryCompleted.setFocusable(false);
			jBtnEntryCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (enteringSubstitution)	jBtnEnterSubstitutionCompletedActionPerformed();
					else if (enteringGoal)		jBtnEnterGoalCompletedActionPerformed();
					else if (enteringBooking)	jBtnEnterBookingCompletedActionPerformed();
				}
			});
		}
		{
			jLblMinute = new JLabel();
			jPnlEntry.add(jLblMinute);
			jLblMinute.setBounds(REC_LBLMINUTE);
			jLblMinute.setText(". Minute");
		}
		{
			jTFMinute = new JTextField();
			jPnlEntry.add(jTFMinute);
			jTFMinute.setBounds(REC_TFMINUTE);
			jTFMinute.setText("");
			jTFMinute.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent ke) {
					if (ke.getKeyChar() == 10)	jBtnEntryCompleted.doClick();
					else if (ke.getKeyChar() != 43 && (ke.getKeyChar() <= 47 || ke.getKeyChar() >= 58)) {
						ke.consume();
					}
				}
			});
			jTFMinute.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					jTFMinute.selectAll();
				}
			});
		}
		{
			jChBLeft = new JCheckBox();
			jPnlEntry.add(jChBLeft);
			jChBLeft.setBounds(REC_CHBLEFT);
			jChBLeft.setOpaque(false);
			jChBLeft.setFocusable(false);
			jChBLeft.setVisible(false);
			jChBLeft.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBLeftSelectionChanged();
				}
			});
		}
		{
			jChBRight = new JCheckBox();
			jPnlEntry.add(jChBRight);
			jChBRight.setBounds(REC_CHBRIGHT);
			jChBRight.setOpaque(false);
			jChBRight.setFocusable(false);
			jChBRight.setVisible(false);
			jChBRight.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBRightSelectionChanged();
				}
			});
		}
		{
			buttonGroupDetails = new ButtonGroup();
			buttonGroupDetails.add(jChBLeft);
			buttonGroupDetails.add(jChBRight);
		}
		{
			jChBBench = new JCheckBox();
			jPnlEntry.add(jChBBench);
			jChBBench.setBounds(REC_CHBBENCH);
			jChBBench.setText("auf der Bank");
			jChBBench.setOpaque(false);
			jChBBench.setFocusable(false);
			jChBBench.setVisible(false);
			jChBBench.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBBenchSelectionChanged();
				}
			});
		}

		{
			jChBAfterMatch = new JCheckBox();
			jPnlEntry.add(jChBAfterMatch);
			jChBAfterMatch.setBounds(REC_CHBAFTERMATCH);
			jChBAfterMatch.setText("nach dem Spiel");
			jChBAfterMatch.setOpaque(false);
			jChBAfterMatch.setFocusable(false);
			jChBAfterMatch.setVisible(false);
			jChBAfterMatch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBAfterMatchSelectionChanged();
				}
			});
		}
		{
			jLblTop = new JLabel();
			jPnlEntry.add(jLblTop);
			jLblTop.setBounds(REC_LBLTOP);
		}
		{
			jCBTop = new JComboBox<>();
			jPnlEntry.add(jCBTop);
			jCBTop.setBounds(REC_CBTOP);
			jCBTop.setFocusable(false);
		}
		{
			jLblBottom = new JLabel();
			jPnlEntry.add(jLblBottom);
			jLblBottom.setBounds(REC_LBLBOTTOM);
		}
		{
			jCBBottom = new JComboBox<>();
			jPnlEntry.add(jCBBottom);
			jCBBottom.setBounds(REC_CBBOTTOM);
			jCBBottom.setFocusable(false);
		}
		
		
		{
			jBtnGo = new JButton();
			jPnlMatchInformation.add(jBtnGo);
			jBtnGo.setBounds(RECGO);
			jBtnGo.setText("fertig");
			jBtnGo.setFocusable(false);
			jBtnGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					jBtnGoActionPerformed();
				}
			});
		}
		
		setSize(WIDTH + getWindowDecorationWidth(), HEIGHT + getWindowDecorationHeight());
		setResizable(false);
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				typed(e.getKeyChar());
			}
		});
	}
	
	private void typed(int keyChar) {
		if (typed == -1) {
			if (keyChar == 8)	typed = -1;
			else if (isIn(keyChar, new Integer[] {66, 71, 76, 83, 98, 103, 108, 115}))	typed = keyChar;
			else if (keyChar == 120) {
				jBtnGoActionPerformed();
			}
		} else {
			if (keyChar == 72 || keyChar == 104) {
				if (typed % 32 == 2)	jBtnBookingHome.doClick();
				if (typed % 32 == 7)	jBtnGoalHome.doClick();
				if (typed % 32 == 12)	jBtnLineupHome.doClick();
				if (typed % 32 == 19)	jBtnSubstitutionHome.doClick();
				typed = -1;
			} else if (keyChar == 65 || keyChar == 97) {
				if (typed % 32 == 2)	jBtnBookingAway.doClick();
				if (typed % 32 == 7)	jBtnGoalAway.doClick();
				if (typed % 32 == 12)	jBtnLineupAway.doClick();
				if (typed % 32 == 19)	jBtnSubstitutionAway.doClick();
				typed = -1;
			}
		}
	}
	
	private void displayGivenValues() {
		lineupHome = match.getLineupHome();
		lineupAway = match.getLineupAway();
		
		for (int i = 0; i < numberOfPlayersInLineUp; i++) {
			if (lineupHome != null) {
				jLblsPlayersHome[i].setText(match.getHomeTeam().getAffiliation(lineupHome[i], date).getPlayer().getPopularOrLastName());
				jLblsPlayersHome[i].setVisible(true);
				jLblsSquadNumbersHome[i].setText("" + match.getHomeTeam().getAffiliation(lineupHome[i], date).getSquadNumber());
				jLblsSquadNumbersHome[i].setVisible(true);
			}
			if (lineupAway != null) {
				jLblsPlayersAway[i].setText(match.getAwayTeam().getAffiliation(lineupAway[i], date).getPlayer().getPopularOrLastName());
				jLblsPlayersAway[i].setVisible(true);
				jLblsSquadNumbersAway[i].setText("" + match.getAwayTeam().getAffiliation(lineupAway[i], date).getSquadNumber());
				jLblsSquadNumbersAway[i].setVisible(true);
			}
		}
		
		if (match.getReferee() != null)	jCBReferees.setSelectedIndex(match.getReferee().getID());
		
		createPseudoGoals();
		paintGoals();
		paintSubstitutions(true);
		paintSubstitutions(false);
		paintBookings();
		
		if (goals.size() > 0 || substitutionsHome.size() > 0 || substitutionsAway.size() > 0 || 
				inThePast(match.getKickOffTime()))	startMatch();
	}
	
	private void createPseudoGoals() {
		// for matches without lineup, otherwise when modified later, all current goals would be lost
		if (result != null) {
			if (result.home(1) + result.away(1) != goals.size() && result.home(2) + result.away(2) != goals.size()) {
				int home = 0, away = 0;
				for (Tor goal : goals) {
					if (goal.isFirstTeam())	home++;
					else					away++;
				}
				for (int i = home; i < result.home(1); i++) {
					match.addGoal(new Tor(match, true, false, false, new Minute(1, 0)));
				}
				for (int i = away; i < result.away(1); i++) {
					match.addGoal(new Tor(match, false, false, false, new Minute(1, 0)));
				}
				for (int i = result.home(1); i < result.home(2); i++) {
					match.addGoal(new Tor(match, true, false, false, new Minute(91, 0)));
				}
				for (int i = result.away(1); i < result.away(2); i++) {
					match.addGoal(new Tor(match, false, false, false, new Minute(91, 0)));
				}
			}
			if (result.home(3) > result.home(2) || result.away(3) > result.away(2)) {
				int firstT = result.home(3) - result.home(2), secondT = result.away(3) - result.away(2);
				int difference = firstT > secondT ? firstT - secondT : secondT - firstT;
				int max = firstT > secondT ? firstT : secondT;
				if (max < 5)	max = 6 - difference;
				
				// make sure size of penalties lists is max
				addPLabels(max - penaltiesHome.size());
				removePLabels(max);
				
				for (int i = 0; i < max; i++) {
					penaltiesHome.set(i, i < firstT ? 1 : 2);
					penaltiesAway.set(i, i < secondT ? 1 : 2);
				}
				latestPenalty = max;
			}
		}
	}
	
	private void paintGoals() {
		jBtnPenaltyShootout.setBounds(REC_BTNPENALTIES);
		if (jLblsGoals.size() > 0) {
			for (JLabel label : jLblsGoals) {
				label.setVisible(false);
			}
			jLblsGoals.clear();
		}
		int i = 0;
		for (Tor goal : goals) {
			displayGoal(goal, i++);
		}
		repaint = false;
	}
	
	private void paintSubstitutions(boolean firstTeam) {
		int index = 0;
		if (firstTeam) {
			for (int i = 0; i < numberOfPlayersInLineUp + maximumNumberOfSubstitutions; i++) {
				removeAllMouseListeners(jLblsSubsOffMinutesHome[i]);
				jLblsSubsOffMinutesHome[i].setText("");
				jLblsSubsOffMinutesHome[i].setVisible(false);
			}
			for (int i = 0; i < maximumNumberOfSubstitutions; i++) {
				removeAllMouseListeners(jLblsSubsOnMinutesHome[i]);
				jLblsSubsOnMinutesHome[i].setText("");
				jLblsSubsOnMinutesHome[i].setVisible(false);
				jLblsPlayersHome[numberOfPlayersInLineUp + i].setText("");
				jLblsPlayersHome[numberOfPlayersInLineUp + i].setVisible(false);
				jLblsSquadNumbersHome[numberOfPlayersInLineUp + i].setText("");
				jLblsSquadNumbersHome[numberOfPlayersInLineUp + i].setVisible(false);
			}
			for (Wechsel substitution : substitutionsHome) {
				displaySubstitution(substitution, index++);
			}
		} else {
			for (int i = 0; i < numberOfPlayersInLineUp + maximumNumberOfSubstitutions; i++) {
				removeAllMouseListeners(jLblsSubsOffMinutesAway[i]);
				jLblsSubsOffMinutesAway[i].setText("");
				jLblsSubsOffMinutesAway[i].setVisible(false);
			}
			for (int i = 0; i < maximumNumberOfSubstitutions; i++) {
				removeAllMouseListeners(jLblsSubsOnMinutesAway[i]);
				jLblsSubsOnMinutesAway[i].setText("");
				jLblsSubsOnMinutesAway[i].setVisible(false);
				jLblsPlayersAway[numberOfPlayersInLineUp + i].setText("");
				jLblsPlayersAway[numberOfPlayersInLineUp + i].setVisible(false);
				jLblsSquadNumbersAway[numberOfPlayersInLineUp + i].setText("");
				jLblsSquadNumbersAway[numberOfPlayersInLineUp + i].setVisible(false);
			}
			for (Wechsel substitution : substitutionsAway) {
				displaySubstitution(substitution, index++);
			}
		}
		repaint = false;
	}
	
	private void paintBookings() {
		for (int i = 0; i < numberOfPlayersInLineUp + maximumNumberOfSubstitutions; i++) {
			removeAllMouseListeners(jLblsBookingsHome[i]);
			removeAllMouseListeners(jLblsBookingsAway[i]);
			removeAllMouseListeners(jLblsSecBookingsHome[i]);
			removeAllMouseListeners(jLblsSecBookingsAway[i]);
			jLblsBookingsHome[i].setOpaque(false);
			jLblsBookingsAway[i].setOpaque(false);
			jLblsSecBookingsHome[i].setOpaque(false);
			jLblsSecBookingsAway[i].setOpaque(false);
			jLblsSecBookingsHome[i].setLocation(bLbls[STARTX] + bLbls[SIZEX] + 1 - bDeltaX, bLbls[STARTY] + i * bLbls[GAPY] - bDeltaY);
			jLblsSecBookingsAway[i].setLocation(bLbls[STARTX] + bLbls[GAPX] - bLbls[SIZEX] - 1 + bDeltaX, bLbls[STARTY] + i * bLbls[GAPY] - bDeltaY);
		}
		int i = 0;
		for (Karte booking : bookings) {
			displayBooking(booking, i++);
		}
		repaint = false;
	}
	
	private void displayGoal(Tor goal, final int index) {
		String zusatz = goal.isPenalty() ? " (11m)" : (goal.isOwnGoal() ? " (ET)" : "");
		String scorer = (goal.getScorer() != null ? goal.getScorer().getPlayer().getPopularOrLastName() : "n/a"), minute = goal.getMinute().asString();
		JLabel jLblNewGoal = new JLabel();
		jPnlMatchInformation.add(jLblNewGoal);
		jLblNewGoal.setLocation(gLbls[STARTX] + (goal.isFirstTeam() ? 0 : gLbls[GAPX]), gLbls[STARTY] + index * gLbls[GAPY]);
		jLblNewGoal.setSize(gLbls[SIZEX], gLbls[SIZEY]);
		if (goal.isFirstTeam())	alignLeft(jLblNewGoal);
		else					alignRight(jLblNewGoal);
		jLblNewGoal.setText(goal.isFirstTeam() ? minute + " " + scorer + zusatz : scorer + zusatz + " " + minute);
		jLblNewGoal.setCursor(handCursor);
		jLblNewGoal.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeGoal(index);
			}
		});
		jLblsGoals.add(jLblNewGoal);
		Rectangle bds = jBtnPenaltyShootout.getBounds();
		jBtnPenaltyShootout.setBounds(bds.x, bds.y + gLbls[GAPY], bds.width, bds.height);
	}
	
	private void displaySubstitution(Wechsel substitution, final int index) {
		int squadNumberOff = substitution.getPlayerOff().getSquadNumber();
		String squadNumberOn = "" + substitution.getPlayerOn().getSquadNumber();
		String subOn = substitution.getPlayerOn().getPlayer().getPopularOrLastName();
		String minute = substitution.getMinute().asString();
		final boolean firstTeam = substitution.isFirstTeam();
		JLabel[] names = firstTeam ? jLblsPlayersHome : jLblsPlayersAway;
		JLabel[] numbers = firstTeam ? jLblsSquadNumbersHome : jLblsSquadNumbersAway;
		JLabel[] offMinutes = firstTeam ? jLblsSubsOffMinutesHome : jLblsSubsOffMinutesAway;
		JLabel[] onMinutes = firstTeam ? jLblsSubsOnMinutesHome : jLblsSubsOnMinutesAway;
		names[numberOfPlayersInLineUp + index].setText(subOn);
		names[numberOfPlayersInLineUp + index].setVisible(true);
		numbers[numberOfPlayersInLineUp + index].setText(squadNumberOn);
		numbers[numberOfPlayersInLineUp + index].setVisible(true);
		
		onMinutes[index].setText(firstTeam ? arrowUp() + " " + minute : minute + " " + arrowUp());
		onMinutes[index].setVisible(true);
		onMinutes[index].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeSubstitution(firstTeam, index);
			}
		});
		
		int playerPos = -1;
		for (int i = 0; i < numberOfPlayersInLineUp && playerPos == -1; i++) {
			if (firstTeam && lineupHome[i] == squadNumberOff)		playerPos = i;
			else if (!firstTeam && lineupAway[i] == squadNumberOff)	playerPos = i;
		}
		if (playerPos == -1) {
			for (int i = 0; i < maximumNumberOfSubstitutions && playerPos == -1; i++) {
				if (match.getSubstitutions(firstTeam).get(i).isPlayerOn(squadNumberOff))	playerPos = numberOfPlayersInLineUp + i;
			}
		}
		offMinutes[playerPos].setText(firstTeam ? arrowDown() + " " + minute : minute + " " + arrowDown());
		offMinutes[playerPos].setVisible(true);
		offMinutes[playerPos].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeSubstitution(firstTeam, index);
			}
		});
	}
	
	private void displayBooking(Karte booking, final int index) {
		if (booking.isOnTheBench())	return;
		int squadNumber = booking.getBookedPlayer().getSquadNumber();
		boolean found = false, yellow = booking.isYellowCard(), second = booking.isSecondBooking();
		JLabel label = null;
		if (booking.isFirstTeam()) {
			for (int i = 0; i < lineupHome.length && !found; i++) {
				if (lineupHome[i] == squadNumber) {
					label = (second ? jLblsSecBookingsHome : jLblsBookingsHome)[i];
					found = true;
				}
			}
			if (!found) {
				int i = 0;
				for (Wechsel substitution : substitutionsHome) {
					if (substitution.isPlayerOn(squadNumber)) {
						label = (second ? jLblsSecBookingsHome : jLblsBookingsHome)[numberOfPlayersInLineUp + i];
						break;
					}
					i++;
				}
			}
			if (second && !yellow) {
				Point loc = label.getLocation();
				label.setLocation(loc.x + bDeltaX, loc.y + bDeltaY);
			}
		} else {
			for (int i = 0; i < lineupAway.length && !found; i++) {
				if (lineupAway[i] == squadNumber) {
					label = (second ? jLblsSecBookingsAway : jLblsBookingsAway)[i];
					found = true;
				}
			}
			if (!found) {
				int i = 0;
				for (Wechsel substitution : substitutionsAway) {
					if (substitution.isPlayerOn(squadNumber)) {
						label = (second ? jLblsSecBookingsAway : jLblsBookingsAway)[numberOfPlayersInLineUp + i];
						break;
					}
					i++;
				}
			}
			if (second && !yellow) {
				Point loc = label.getLocation();
				label.setLocation(loc.x - bDeltaX, loc.y + bDeltaY);
			}
		}
		
		if (second || !yellow)	label.setBackground(colorRedCard);
		else					label.setBackground(colorYellowCard);
		alignCenter(label);
		label.setOpaque(true);
		label.setCursor(handCursor);
		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeBooking(index);
			}
		});
	}
	
	private void changeGoal(int index) {
		changedGoal = goals.get(index);
		repaint = true;
		enteringGoal = true;
		editingFirstTeam = changedGoal.isFirstTeam();
		
		log("You want to change the goal of " + (changedGoal.getScorer() == null ? "n/a" : changedGoal.getScorer().getPlayer().getPopularOrLastName()) + "(" + changedGoal.getMinute().asString() + ")");
		
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jChBLeft.setText("Elfmeter");
		jChBRight.setText("Eigentor");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		if (changedGoal.isPenalty()) {
			jChBLeft.setSelected(true);
			goalDetails = 1;
		} else if (changedGoal.isOwnGoal()) {
			jChBRight.setSelected(true);
			goalDetails = 2;
		}
		
		jLblTop.setText("Torschütze");
		jCBTop.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
		jLblBottom.setText("Vorbereiter");
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
		
		if (changedGoal.getScorer() != null)	jCBTop.setSelectedItem(changedGoal.getScorer().getPlayer().getPopularOrLastName());
		if (changedGoal.getAssister() != null)	jCBBottom.setSelectedItem(changedGoal.getAssister().getPlayer().getPopularOrLastName());
		jTFMinute.setText("" + changedGoal.getMinute().toString());
		
		if (editingFirstTeam)	jPnlEntry.setLocation(LOC_PNLENTRYHOME);
		else					jPnlEntry.setLocation(LOC_PNLENTRYAWAY);
		
		jPnlEntry.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void changeSubstitution(boolean firstTeam, int index) {
		if (enteringGoal || enteringSubstitution)	return;
		
		changedSubstitution = (firstTeam ? substitutionsHome : substitutionsAway).get(index);
		repaint = true;
		enteringSubstitution = true;
		editingFirstTeam = firstTeam;
		
		log("You want to change the substitution " + changedSubstitution.getPlayerOff().getPlayer().getPopularOrLastName() + "(" + changedSubstitution.getMinute().toString() + ". " + 
				changedSubstitution.getPlayerOn().getPlayer().getPopularOrLastName() + ")");
		
		int playerPos = -1, squadNumber = changedSubstitution.getPlayerOff().getSquadNumber();
		for (int j = 0; j < numberOfPlayersInLineUp && playerPos == -1; j++) {
			if (firstTeam && lineupHome[j] == squadNumber)			playerPos = j;
			else if (!firstTeam && lineupAway[j] == squadNumber)	playerPos = j;
		}
		if (playerPos == -1) {
			for (int j = 0; j < numberOfPlayersInLineUp && playerPos == -1; j++) {
				if (match.getSubstitutions(firstTeam).get(j).isPlayerOn(squadNumber))	playerPos = numberOfPlayersInLineUp + j;
			}
		}
		JLabel[] minutes = firstTeam ? jLblsSubsOffMinutesHome : jLblsSubsOffMinutesAway;
		minutes[playerPos].setText("");
		
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		// remove the substitution in order to being able to select the players in the combo boxes 
		match.removeSubstitution(changedSubstitution);
		jLblTop.setText("ausgewechselt");
		jCBTop.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(true)));
		jLblBottom.setText("eingewechselt");
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(false)));
		match.addSubstitution(changedSubstitution);
		
		jCBTop.setSelectedItem(changedSubstitution.getPlayerOff().getPlayer().getPopularOrLastName());
		jCBBottom.setSelectedItem(changedSubstitution.getPlayerOn().getPlayer().getPopularOrLastName());
		jTFMinute.setText("" + changedSubstitution.getMinute().toString());
		
		if (editingFirstTeam)	jPnlEntry.setLocation(LOC_PNLENTRYHOME);
		else					jPnlEntry.setLocation(LOC_PNLENTRYAWAY);
		
		jPnlEntry.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void changeBooking(int index) {
		changedBooking = bookings.get(index);
		repaint = true;
		enteringBooking = true;
		editingFirstTeam = changedBooking.isFirstTeam();
		
		log("You want to change the booking of " + changedBooking.getBookedPlayer().getPlayer().getPopularOrLastName() + "(" + changedBooking.getMinute() + ")");
		
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblTop.setVisible(false);
		jCBTop.setVisible(false);
		jChBBench.setVisible(true);
		jChBAfterMatch.setVisible(true);
		bookedOnTheBench = changedBooking.isOnTheBench();
		bookedAfterTheMatch = changedBooking.isAfterTheMatch();
		jLblBottom.setText("Spieler");
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayersBooking()));
		jCBBottom.setSelectedItem(changedBooking.getBookedPlayer().getPlayer().getPopularOrLastName());
		
		jChBLeft.setText("gelbe Karte");
		jChBRight.setText("rote Karte");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		if (changedBooking.isYellowCard())	jChBLeft.setSelected(true);
		else								jChBRight.setSelected(true);
		if (bookedOnTheBench)		jChBBench.setSelected(true);
		if (bookedAfterTheMatch)	jChBAfterMatch.setSelected(true);
		
		jTFMinute.setText("" + changedBooking.getMinute().toString());
		
		if (editingFirstTeam)	jPnlEntry.setLocation(LOC_PNLENTRYHOME);
		else					jPnlEntry.setLocation(LOC_PNLENTRYAWAY);
		
		jPnlEntry.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void setAmGruenenTisch(boolean isHomeTeam) {
		result = new Ergebnis(result.toString() + " agT");
		setResult();
	}
	
	private void setResult() {
		match.setResult(result);
		jLblResult.setText(result.getResult());
		jLblZusatz.setText(result.getMore());
		jLblZusatz.setToolTipText(result.getTooltipText());
	}
	
	private void startPenaltyShootout() {
		showPenalties(true);
		for (int i = 0; i < penaltiesHome.size(); i++) {
			if (penaltiesHome.get(i) == PENALTY_IN)			jLblsPenaltiesHome.get(i).setBackground(penaltiesInColor);
			else if (penaltiesHome.get(i) == PENALTY_OUT)	jLblsPenaltiesHome.get(i).setBackground(penaltiesOutColor);
			if (penaltiesAway.get(i) == PENALTY_IN)			jLblsPenaltiesAway.get(i).setBackground(penaltiesInColor);
			else if (penaltiesAway.get(i) == PENALTY_OUT)	jLblsPenaltiesAway.get(i).setBackground(penaltiesOutColor);
		}
	}
	
	private void showPenalties(boolean show) {
		jBtnLineupHome.setVisible(!show);
		jBtnLineupAway.setVisible(!show);
		jBtnSubstitutionHome.setVisible(!show);
		jBtnSubstitutionAway.setVisible(!show);
		jBtnGoalHome.setVisible(!show);
		jBtnGoalAway.setVisible(!show);
		jBtnBookingHome.setVisible(!show);
		jBtnBookingAway.setVisible(!show);
		jBtnPenaltyShootout.setVisible(!show);
		editingFirstTeam = true;
		setLabelsVisible(!show);
		editingFirstTeam = false;
		setLabelsVisible(!show);
		
		jPnlPenalties.setVisible(show);
	}
	
	private void addPLabels(int nOfLabels) {
		int oldSize = jLblsPenaltiesHome.size();
		for (int i = oldSize; i < oldSize + nOfLabels; i++) {
			final int x = i;
			JLabel label = new JLabel();
			jPnlPenalties.add(label);
			label.setBounds(penaltyH[STARTX] + (i % 5) * penaltyH[GAPX], penaltyH[STARTY] + (i / 5) * penaltyH[GAPY], penaltyH[SIZEX], penaltyH[SIZEY]);
			alignCenter(label);
			label.setText("11");
			label.setCursor(handCursor);
			label.setBackground(penaltiesNoColor);
			label.setOpaque(true);
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					changePenalty(true, x);
				}
			});
			jLblsPenaltiesHome.add(label);
			penaltiesHome.add(0);
			
			label = new JLabel();
			jPnlPenalties.add(label);
			label.setBounds(penaltyA[STARTX] + (i % 5) * penaltyA[GAPX], penaltyA[STARTY] + (i / 5) * penaltyH[GAPY], penaltyA[SIZEX], penaltyA[SIZEY]);
			alignCenter(label);
			label.setText("11");
			label.setCursor(handCursor);
			label.setBackground(penaltiesNoColor);
			label.setOpaque(true);
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					changePenalty(false, x);
				}
			});
			jLblsPenaltiesAway.add(label);
			penaltiesAway.add(0);
		}
	}
	
	private void removePLabels(int firstIndexToRemove) {
		while (firstIndexToRemove < penaltiesHome.size()) {
			jLblsPenaltiesHome.remove(firstIndexToRemove).setVisible(false);
			jLblsPenaltiesAway.remove(firstIndexToRemove).setVisible(false);
			penaltiesHome.remove(firstIndexToRemove);
			penaltiesAway.remove(firstIndexToRemove);
		}
	}
	
	private void changePenalty(boolean firstTeam, int index) {
		if (latestPenalty < index) {
			message("Erst den Elfmeter der anderen Mannschaft eingeben.");
			return;
		}
		ArrayList<JLabel> labelList = firstTeam ? jLblsPenaltiesHome : jLblsPenaltiesAway;
		ArrayList<Integer> penStateList = firstTeam ? penaltiesHome : penaltiesAway;
		
		if (penStateList.get(index) == PENALTY_NO) {
			labelList.get(index).setBackground(penaltiesInColor);
			penStateList.set(index, PENALTY_IN);
		} else if (penStateList.get(index) == PENALTY_IN) {
			labelList.get(index).setBackground(penaltiesOutColor);
			penStateList.set(index, PENALTY_OUT);
		} else if (penStateList.get(index) == PENALTY_OUT && index != latestPenalty) {
			labelList.get(index).setBackground(penaltiesInColor);
			penStateList.set(index, PENALTY_IN);
		} else {
			labelList.get(index).setBackground(penaltiesNoColor);
			penStateList.set(index, PENALTY_NO);
		}
		
		addPLabels(5 - penaltiesHome.size());
		int firstT = 0, secondT = 0;
		for (int i = 0; i < penaltiesHome.size(); i++) {
			if (penaltiesHome.get(i) == PENALTY_IN)	firstT++;
			if (penaltiesAway.get(i) == PENALTY_IN)	secondT++;
			if (penaltiesHome.get(i) != PENALTY_NO && penaltiesAway.get(i) != PENALTY_NO)	latestPenalty = i + 1;
			
			int difference = firstT > secondT ? firstT - secondT : secondT - firstT, remaining = penaltiesHome.size() - i - 1;
			if (i > 3 && difference != 0)	removePLabels(i + 1);
			if (difference > remaining && remaining != 0)	removePLabels(i + 1);
		}
		if (firstT == secondT && latestPenalty == penaltiesHome.size())	addPLabels(1);
	}
	
	private void finishPenaltyShootout() {
		int penH = 0, penA = 0;
		for (int i = 0; i < latestPenalty; i++) {
			if (penaltiesHome.get(i) == PENALTY_IN)	penH++;
			if (penaltiesAway.get(i) == PENALTY_IN)	penA++;
		}
		if (penH == penA) {
			message("Ein Elfmeterschießen kann nicht unentschieden enden.");
			return;
		}
		
		String regular = result.home(1) + ":" + result.away(1);
		String extra = result.home(2) + ":" + result.away(2);
		result = new Ergebnis((penH + result.home(2)) + ":" + (penA + result.away(2)) + "nE (" + extra + "," + regular + ")");
		setResult();
		
		showPenalties(false);
	}
	
	private void jCBRefereesItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			match.setReferee(jCBReferees.getSelectedIndex());
		}
	}
	
	private void startMatch() {
		if (result == null)	result = new Ergebnis("0:0");
		setResult();
		
		jBtnStartMatch.setVisible(false);
		jBtnSubstitutionHome.setVisible(true);
		jBtnSubstitutionAway.setVisible(true);
		jBtnGoalHome.setVisible(true);
		jBtnGoalAway.setVisible(true);
		jBtnBookingHome.setVisible(true);
		jBtnBookingAway.setVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
	}
	
	private void setLabelsVisible(boolean value) {
		if (editingFirstTeam) {
			for (JLabel label : jLblsPlayersHome) {
				label.setVisible(value && !label.getText().equals(""));
			}
			for (JLabel label : jLblsSquadNumbersHome) {
				label.setVisible(value && !label.getText().equals(""));
			}
			for (JLabel label : jLblsBookingsHome) {
				label.setVisible(value);
			}
			for (JLabel label : jLblsSecBookingsHome) {
				label.setVisible(value);
			}
			for (JLabel label : jLblsSubsOffMinutesHome) {
				label.setVisible(value && !label.getText().equals(""));
			}
			for (JLabel label : jLblsSubsOnMinutesHome) {
				label.setVisible(value && !label.getText().equals(""));
			}
		} else {
			for (JLabel label : jLblsPlayersAway) {
				label.setVisible(value && !label.getText().equals(""));
			}
			for (JLabel label : jLblsSquadNumbersAway) {
				label.setVisible(value && !label.getText().equals(""));
			}
			for (JLabel label : jLblsBookingsAway) {
				label.setVisible(value);
			}
			for (JLabel label : jLblsSecBookingsAway) {
				label.setVisible(value);
			}
			for (JLabel label : jLblsSubsOffMinutesAway) {
				label.setVisible(value && !label.getText().equals(""));
			}
			for (JLabel label : jLblsSubsOnMinutesAway) {
				label.setVisible(value && !label.getText().equals(""));
			}
		}
		for (JLabel label : jLblsGoals) {
			label.setVisible(value);
		}
	}
	
	private void jBtnEntryCancelledActionPerformed() {
		enteringSubstitution = false;
		enteringBooking = false;
		enteringGoal = false;
		bookedOnTheBench = false;
		bookedAfterTheMatch = false;
		repaint = false;
		changedGoal = null;
		changedSubstitution = null;
		changedBooking = null;
		
		jPnlEntry.setVisible(false);
		jTFMinute.setText("");
		jLblTop.setVisible(true);
		jCBTop.setVisible(true);
		jChBBench.setSelected(false);
		jChBBench.setVisible(false);
		jChBAfterMatch.setSelected(false);
		jChBAfterMatch.setVisible(false);
		jChBLeft.setVisible(false);
		jChBRight.setVisible(false);
		buttonGroupDetails.clearSelection();
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
		requestFocus();
	}
	
	private void enterNewSubstitution(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		if ((firstTeam && lineupHome == null) || (!firstTeam && lineupAway == null))	return;
		
		if (match.getSubstitutions(firstTeam).size() == maximumNumberOfSubstitutions) {
			message("You have already submitted all possible substitutions for this team.");
			return;
		}
		
		enteringSubstitution = true;
		editingFirstTeam = firstTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblTop.setText("ausgewechselt");
		jCBTop.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(true)));
		jCBTop.setSelectedIndex(jCBTop.getModel().getSize() / 2);
		jLblBottom.setText("eingewechselt");
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(false)));
		jCBBottom.setSelectedIndex(jCBBottom.getModel().getSize() / 2);
		
		if (editingFirstTeam)	jPnlEntry.setLocation(LOC_PNLENTRYHOME);
		else					jPnlEntry.setLocation(LOC_PNLENTRYAWAY);
		jPnlEntry.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private String[] getEligiblePlayersSub(boolean subOff) {
		String[] eligiblePlayers;
		
		Mannschaft team = match.getTeam(editingFirstTeam);
		ArrayList<Wechsel> substitutions = match.getSubstitutions(editingFirstTeam);
		int[] lineup = editingFirstTeam ? lineupHome : lineupAway;
		
		if (subOff) {
			eligiblePlayersListUpper.clear();
			for (int i = 0; i < lineup.length; i++) {
				eligiblePlayersListUpper.add(team.getAffiliation(lineup[i], date));
			}
			for (Wechsel substitution : substitutions) {
				eligiblePlayersListUpper.remove(substitution.getPlayerOff());
				eligiblePlayersListUpper.add(substitution.getPlayerOn());
			}
			eligiblePlayers = new String[eligiblePlayersListUpper.size()];
			for (int i = 0; i < eligiblePlayers.length; i++) {
				eligiblePlayers[i] = eligiblePlayersListUpper.get(i).getPlayer().getPopularOrLastName();
			}
		} else {
			eligiblePlayersListLower = cloneList(team.getEligiblePlayers(date, false));
			for (int i = 0; i < lineup.length; i++) {
				eligiblePlayersListLower.remove(team.getAffiliation(lineup[i], date));
			}
			for (Wechsel substitution : substitutions) {
				eligiblePlayersListLower.remove(substitution.getPlayerOn());
			}
			eligiblePlayers = new String[eligiblePlayersListLower.size()];
			for (int i = 0; i < eligiblePlayers.length; i++) {
				eligiblePlayers[i] = eligiblePlayersListLower.get(i).getPlayer().getPopularOrLastName();
			}
		}
		
		return eligiblePlayers;
	}
	
	private void jBtnEnterSubstitutionCompletedActionPerformed() {
		if (!enteringSubstitution)	return;
		
		if (jTFMinute.getText().isEmpty()) {
			message("Bitte eine Minute angeben.");
			return;
		}
		
		Minute minute;
		try {
			minute = Minute.parse(jTFMinute.getText());
		} catch (Exception e) {
			message("Bitte eine gültige Minute eingeben.");
			return;
		}
		if (minute.getRegularTime() > 120) {
			message("Ein Wechsel kann nicht nach der 120. Minute erfolgen.");
			return;
		} else if (minute.getRegularTime() > 90) {
			if (!isETpossible) {
				message("In diesem Spiel kann es keine Verlängerung geben.");
				return;
			}
		} else {
			if (editingFirstTeam) {
				if (nOfRTSubsHome == maximumNumberOfSubstitutionsRT && (changedSubstitution == null || changedSubstitution.getMinute().getRegularTime() > 90)) {
					message("Es kann nur " + maximumNumberOfSubstitutionsRT + " Wechsel vor Ablauf der regulären Spielzeit geben.");
					return;
				}
				nOfRTSubsHome++;
			} else {
				if (nOfRTSubsAway == maximumNumberOfSubstitutionsRT && (changedSubstitution == null || changedSubstitution.getMinute().getRegularTime() > 90)) {
					message("Es kann nur " + maximumNumberOfSubstitutionsRT + " Wechsel vor Ablauf der regulären Spielzeit geben.");
					return;
				}
				nOfRTSubsAway++;
			}
		}
		if (changedSubstitution != null) {
			if (editingFirstTeam) {
				substitutionsHome.remove(changedSubstitution);
				if (changedSubstitution.getMinute().getRegularTime() <= 90)	nOfRTSubsHome--;
			} else {
				substitutionsAway.remove(changedSubstitution);
				if (changedSubstitution.getMinute().getRegularTime() <= 90)	nOfRTSubsAway--;
			}
			changedSubstitution = null;
		}
		for (Wechsel substitution : match.getSubstitutions(editingFirstTeam)) {
			if (substitution.getMinute().isAfter(minute)) {
				repaint = true;
			}
		}
		TeamAffiliation playerOff = eligiblePlayersListUpper.get(jCBTop.getSelectedIndex());
		TeamAffiliation playerOn = eligiblePlayersListLower.get(jCBBottom.getSelectedIndex());
		
		Wechsel substitution = new Wechsel(match, editingFirstTeam, minute, playerOff, playerOn);
		int index = match.addSubstitution(substitution);
		if (repaint) {
			paintSubstitutions(editingFirstTeam);
			paintBookings();
		}
		else	displaySubstitution(substitution, index);
		enteringSubstitution = false;
		validateMatchDataOnLineupChange();
		
		jPnlEntry.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
		requestFocus();
	}
	
	private void enterNewGoal(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		enteringGoal = true;
		editingFirstTeam = firstTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jChBLeft.setText("Elfmeter");
		jChBRight.setText("Eigentor");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		jLblTop.setText("Torschütze");
		jCBTop.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
		jLblBottom.setText("Vorbereiter");
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
		
		if (editingFirstTeam)	jPnlEntry.setLocation(LOC_PNLENTRYHOME);
		else					jPnlEntry.setLocation(LOC_PNLENTRYAWAY);
		jPnlEntry.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void jChBBenchSelectionChanged() {
		bookedOnTheBench = !bookedOnTheBench;
		String selectedItem = (String) jCBBottom.getSelectedItem();
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayersBooking()));
		jCBBottom.setSelectedItem(selectedItem);
	}
	
	private void jChBAfterMatchSelectionChanged() {
		bookedAfterTheMatch = !bookedAfterTheMatch;
		String selectedItem = (String) jCBBottom.getSelectedItem();
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayersBooking()));
		jCBBottom.setSelectedItem(selectedItem);
	}
	
	private void jChBLeftSelectionChanged() {
		if (enteringGoal) {
			if (goalDetails == 1) {
				buttonGroupDetails.clearSelection();
				goalDetails = 0;
			} else {
				if (goalDetails == 2) {
					jCBTop.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
					jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
				}
				goalDetails = 1;
			}
		}
	}
	
	private void jChBRightSelectionChanged() {
		if (enteringGoal) {
			if (goalDetails == 2) {
				buttonGroupDetails.clearSelection();
				goalDetails = 0;
			} else {
				goalDetails = 2;
			}
			jCBTop.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
			jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
		}
	}
	
	private String[] getEligiblePlayersBooking() {
		String[] eligiblePlayers;
		
		boolean firstTeam = editingFirstTeam;
		if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
			Mannschaft bookedTeam = match.getTeam(firstTeam);
			eligiblePlayersListLower.clear();
			
			if (bookedOnTheBench || bookedAfterTheMatch) {
				ArrayList<TeamAffiliation> eligPlayers = bookedTeam.getEligiblePlayers(date, false);
				eligiblePlayers = new String[1 + eligPlayers.size()];
				int count = 1;
				for (TeamAffiliation affiliation : eligPlayers) {
					eligiblePlayersListLower.add(affiliation);
					eligiblePlayers[count++] = affiliation.getPlayer().getPopularOrLastName();
				}
			} else {
				int[] bookedTeamLineup = firstTeam ? lineupHome : lineupAway;
				ArrayList<Wechsel> substitutions = match.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + numberOfPlayersInLineUp + substitutions.size()];
			
				for (int i = 0; i < numberOfPlayersInLineUp; i++) {
					eligiblePlayersListLower.add(bookedTeam.getAffiliation(bookedTeamLineup[i], date));
					eligiblePlayers[1 + i] = eligiblePlayersListLower.get(i).getPlayer().getPopularOrLastName();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListLower.add(substitutions.get(i).getPlayerOn());
					eligiblePlayers[1 + numberOfPlayersInLineUp + i] = eligiblePlayersListLower.get(numberOfPlayersInLineUp + i).getPlayer().getPopularOrLastName();
				}
			}
		} else {
			eligiblePlayers = new String[1];
		}
		
		eligiblePlayers[0] = "Bitte wählen";
		
		return eligiblePlayers;
	}
	
	private String[] getEligiblePlayers(boolean upper) {
		boolean ownGoal = jChBRight.isSelected();
		String[] eligiblePlayers;
		if (upper) {
			boolean firstTeam = editingFirstTeam ^ ownGoal;
			if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
				Mannschaft scoringTeam = match.getTeam(firstTeam);
				int[] scoringLineup = firstTeam ? lineupHome : lineupAway;
				
				eligiblePlayersListUpper.clear();
				ArrayList<Wechsel> substitutions = match.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + numberOfPlayersInLineUp + substitutions.size()];
				for (int i = 0; i < numberOfPlayersInLineUp; i++) {
					eligiblePlayersListUpper.add(scoringTeam.getAffiliation(scoringLineup[i], date));
					eligiblePlayers[1 + i] = eligiblePlayersListUpper.get(i).getPlayer().getPopularOrLastName();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListUpper.add(substitutions.get(i).getPlayerOn());
					eligiblePlayers[1 + numberOfPlayersInLineUp + i] = eligiblePlayersListUpper.get(numberOfPlayersInLineUp + i).getPlayer().getPopularOrLastName();
				}
			} else {
				eligiblePlayers = new String[1];
			}
		} else {
			boolean firstTeam = editingFirstTeam;
			if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
				Mannschaft assistingTeam = match.getTeam(firstTeam);
				int[] assistingLineup = firstTeam ? lineupHome : lineupAway;
				
				eligiblePlayersListLower.clear();
				ArrayList<Wechsel> substitutions = match.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + numberOfPlayersInLineUp + substitutions.size()];
				for (int i = 0; i < numberOfPlayersInLineUp; i++) {
					eligiblePlayersListLower.add(assistingTeam.getAffiliation(assistingLineup[i], date));
					eligiblePlayers[1 + i] = eligiblePlayersListLower.get(i).getPlayer().getPopularOrLastName();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListLower.add(substitutions.get(i).getPlayerOn());
					eligiblePlayers[1 + numberOfPlayersInLineUp + i] = eligiblePlayersListLower.get(numberOfPlayersInLineUp + i).getPlayer().getPopularOrLastName();
				}
			} else {
				eligiblePlayers = new String[1];
			}
		}
		
		eligiblePlayers[0] = "Bitte wählen";
		
		return eligiblePlayers;
	}
	
	private void jBtnEnterGoalCompletedActionPerformed() {
		if (!enteringGoal)	return;
		
		if (jTFMinute.getText().isEmpty()) {
			message("Bitte eine Minute angeben.");
			return;
		}
		
		Minute minute;
		try {
			minute = Minute.parse(jTFMinute.getText());
		} catch (Exception e) {
			message("Bitte eine gültige Minute eingeben.");
			return;
		}
		if (minute.getRegularTime() > 120) {
			message("Ein Tor kann nicht nach der 120. Minute fallen.\nBenutzen Sie bitte für Elfmeterschießen die dafür vorgesehene Eingabemaske.");
			return;
		} else if (!isETpossible && minute.getRegularTime() > 90) {
			message("In diesem Spiel kann es keine Verlängerung geben.");
			return;
		}
		for (Tor goal : goals) {
			if (goal.getMinute().isAfter(minute)) {
				repaint = true;
			}
		}
		
		boolean penalty = jChBLeft.isSelected();
		boolean ownGoal = jChBRight.isSelected();
		int index;
		TeamAffiliation scorer = null;
		if ((index = jCBTop.getSelectedIndex()) != 0) {
			scorer = eligiblePlayersListUpper.get(index - 1);
			for (Wechsel substitution : match.getSubstitutions(editingFirstTeam)) {
				if (substitution.getPlayerOff() == scorer && minute.isAfter(substitution.getMinute()) || 
						substitution.getPlayerOn() == scorer && minute.isBefore(substitution.getMinute())) {
					message("The player " + scorer.getPlayer().getPopularOrLastName() + " was not on the pitch in the given minute.");
					return;
				}
			}
		}
		TeamAffiliation assister = null;
		if ((index = jCBBottom.getSelectedIndex()) != 0) {
			assister = eligiblePlayersListLower.get(index - 1);
			for (Wechsel substitution : match.getSubstitutions(editingFirstTeam)) {
				if (substitution.getPlayerOff() == assister && minute.isAfter(substitution.getMinute()) || 
						substitution.getPlayerOn() == assister && minute.isBefore(substitution.getMinute())) {
					message("The player " + assister.getPlayer().getPopularOrLastName() + " was not on the pitch in the given minute.");
					return;
				}
			}
		}
		
		if (changedGoal != null)	goals.remove(changedGoal);
		changedGoal = null;
		
		Tor goal = null;
		if (scorer == null)			goal = new Tor(match, editingFirstTeam, penalty, ownGoal, minute);
		else if (assister == null)	goal = new Tor(match, editingFirstTeam, penalty, ownGoal, minute, scorer);
		else						goal = new Tor(match, editingFirstTeam, penalty, ownGoal, minute, scorer, assister);
		match.addGoal(goal);
		result = match.getResult();
		setResult();
		if (repaint)	paintGoals();
		else			displayGoal(goal, goals.size() - 1);
		enteringGoal = false;
		
		buttonGroupDetails.clearSelection();
		goalDetails = 0;
		jChBLeft.setVisible(false);
		jChBRight.setVisible(false);
		jPnlEntry.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
		requestFocus();
	}
	
	private void enterNewBooking(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		
		enteringBooking = true;
		editingFirstTeam = firstTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblTop.setVisible(false);
		jCBTop.setVisible(false);
		jChBBench.setVisible(true);
		jChBAfterMatch.setVisible(true);
		jLblBottom.setText("Spieler");
		jCBBottom.setModel(new DefaultComboBoxModel<>(getEligiblePlayersBooking()));
		
		jChBLeft.setText("gelbe Karte");
		jChBRight.setText("rote Karte");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		jChBLeft.setSelected(true);
		
		if (editingFirstTeam)	jPnlEntry.setLocation(LOC_PNLENTRYHOME);
		else					jPnlEntry.setLocation(LOC_PNLENTRYAWAY);
		jPnlEntry.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void jBtnEnterBookingCompletedActionPerformed() {
		if (!enteringBooking)	return;
		
		if (jTFMinute.getText().isEmpty()) {
			message("Bitte eine Minute angeben.");
			return;
		}
		
		if (!jChBLeft.isSelected() && !jChBRight.isSelected()) {
			message("Bitte angeben, um welche Karte es sich handelt.");
			return;
		}
		
		if (jCBBottom.getSelectedIndex() == 0) {
			message("Bitte einen Spieler auswählen.");
			return;
		}
		
		Minute minute;
		try {
			minute = Minute.parse(jTFMinute.getText());
		} catch (Exception e) {
			message("Bitte eine gültige Minute eingeben.");
			return;
		}
		boolean afterTheMatch = jChBAfterMatch.isSelected();
		if (minute.getRegularTime() > 120) {
			if (minute.getRegularTime() != 121 || !isETpossible || !afterTheMatch) {
				message("Ein Spieler kann nicht nach der 120. Minute verwarnt werden. Bitte eine korrekte Minute angeben.");
				return;
			}
		} else if (!isETpossible && minute.getRegularTime() > 90) {
			if (minute.getRegularTime() != 91 || !afterTheMatch) {
				message("In diesem Spiel kann es keine Verlängerung geben. Bitte eine korrekte Minute angeben.");
				return;
			}
		}
		
		boolean yellowCard = jChBLeft.isSelected();
		boolean onTheBench = jChBBench.isSelected();
		
		int index = jCBBottom.getSelectedIndex();
		TeamAffiliation bookedPlayer = eligiblePlayersListLower.get(index - 1);
		boolean onThePitch = checkPlayerOnPitch(bookedPlayer, minute);
		if (onThePitch && onTheBench) {
			message("Der Spieler " + bookedPlayer.getPlayer().getPopularOrLastName() + " war in der angegebenen Minute auf dem Spielfeld, kann also nicht auf der Bank sitzen.");
			return;
		} else if (!onThePitch && !onTheBench) {
			message("Der Spieler " + bookedPlayer.getPlayer().getPopularOrLastName() + " war in der angegebenen Minute nicht auf dem Spielfeld.");
			return;
		}
		
		if (!addBooking(minute, yellowCard, onTheBench, afterTheMatch, bookedPlayer)) return;
		enteringBooking = false;
		bookedOnTheBench = false;
		bookedAfterTheMatch = false;
		
		buttonGroupDetails.clearSelection();
		jChBLeft.setVisible(false);
		jChBRight.setVisible(false);
		
		jChBBench.setSelected(false);
		jChBBench.setVisible(false);
		jChBAfterMatch.setSelected(false);
		jChBAfterMatch.setVisible(false);
		jLblTop.setVisible(true);
		jCBTop.setVisible(true);
		jPnlEntry.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
		requestFocus();
	}
	
	private boolean checkPlayerOnPitch(TeamAffiliation player, Minute minute) {
		boolean subOn = false;
		for (Wechsel substitution : match.getSubstitutions(editingFirstTeam)) {
			if (substitution.getPlayerOff() == player && minute.isAfter(substitution.getMinute())) {
				return false;
			} else if (substitution.getPlayerOn() == player) {
				if (minute.isBefore(substitution.getMinute()))	return false;
				subOn = true;
			}
		}
		if (subOn)	return true;
		int[] lineup = editingFirstTeam ? lineupHome : lineupAway;
		for (int i = 0; i < lineup.length; i++) {
			if (lineup[i] == player.getSquadNumber())	return true;
		}
		
		return false;
	}
	
	private boolean addBooking(Minute minute, boolean yellowCard, boolean onTheBench, boolean afterTheMatch, TeamAffiliation bookedPlayer) {
		boolean isSecondBooking = false;
		ArrayList<Karte> playersBookings = new ArrayList<>();
		ArrayList<Karte> otherBookings = new ArrayList<>();
		for (Karte booking : bookings) {
			if (changedBooking != null && changedBooking == booking)	continue;
			if (booking.getBookedPlayer() == bookedPlayer)	playersBookings.add(booking);
			else											otherBookings.add(booking);
		}
		
		if (playersBookings.size() >= 2) {
			message("Dem ausgewählten Spieler sind bereits zu viele Karten zugeteilt!");
			return false;
		} else if (playersBookings.size() == 1) {
			Karte previous = playersBookings.get(0);
			boolean prvYellow = previous.isYellowCard();
			boolean prvFirst = !previous.getMinute().isAfter(minute);
			
			if (!yellowCard && !prvFirst) {
				message("Der Spieler kann nicht vom Platz gestellt werden, da er später noch eine Karte sieht.");
				return false;
			} else if (!prvYellow && prvFirst) {
				message("Der Spieler wurde bereits vom Platz gestellt!");
				return false;
			}
			previous.setSecondBooking(!prvFirst);
			isSecondBooking = prvFirst;
			repaint = true;
		}

		for (Karte booking : otherBookings) {
			booking.setSecondBooking(false);
			for (Karte previous : otherBookings) {
				if (previous == booking)	break;
				else if (previous.getBookedPlayer() == booking.getBookedPlayer())	booking.setSecondBooking(true);
			}
		}
		
		Karte booking = new Karte(match, editingFirstTeam, minute, yellowCard, isSecondBooking, onTheBench, afterTheMatch, bookedPlayer);
		if (changedBooking != null)	bookings.remove(changedBooking);
		changedBooking = null;
		match.addBooking(booking);
		if (repaint)	paintBookings();
		else			displayBooking(booking, bookings.size() - 1);
		
		return true;
	}
	
	private void enterNewLineup(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		ArrayList<TeamAffiliation> kader;
		enteringLineup = true;
		editingFirstTeam = firstTeam;
		int[] lineup = editingFirstTeam ? lineupHome : lineupAway;
		if (editingFirstTeam)	kader = kaderHome = match.getHomeTeam().getEligiblePlayers(date, false);
		else					kader = kaderAway = match.getAwayTeam().getEligiblePlayers(date, false);
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		playerSelected = new boolean[kader.size()];
		
		// create lineup selection labels
		jLblsLineupSelectionPlayers = new JLabel[kader.size()];
		for (int i = 0; i < jLblsLineupSelectionPlayers.length; i++) {
			final int x = i;
			jLblsLineupSelectionPlayers[i] = new JLabel();
			jPnlLineupSelection.add(jLblsLineupSelectionPlayers[i]);
			jLblsLineupSelectionPlayers[i].setSize(boundsLSP[SIZEX], boundsLSP[SIZEY]);
			jLblsLineupSelectionPlayers[i].setLocation(boundsLSP[STARTX] + (i / playersPerColumn) * boundsLSP[GAPX], 
														boundsLSP[STARTY] + (i % playersPerColumn) * boundsLSP[GAPY]);
			jLblsLineupSelectionPlayers[i].setText(kader.get(i).getSquadNumber() + " " + kader.get(i).getPlayer().getPopularOrLastName());
			jLblsLineupSelectionPlayers[i].setBackground(playerSelectedColor);
			jLblsLineupSelectionPlayers[i].setCursor(handCursor);
			jLblsLineupSelectionPlayers[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					playerSelected(x);
				}
			});
		}
		
		numberOfSelectedPlayers = 0;
		if (lineup == null) {
			lineup = new int[numberOfPlayersInLineUp];
			jLblNumberOfSelectedPlayers.setText(numberOfSelectedPlayers + " Spieler ausgewählt");
		} else {
			// colorise previously selected players
			for (int i = 0; i < lineup.length; i++) {
				for (int j = 0; j < kader.size(); j++) {
					if (lineup[i] == kader.get(j).getSquadNumber()) {
						playerSelected(j);
						break;
					}
				}
			}
		}
		
		if (editingFirstTeam)	jPnlLineupSelection.setLocation(LOC_PNLENTERLINEUPHOME);
		else					jPnlLineupSelection.setLocation(LOC_PNLENTERLINEUPAWAY);
		jPnlLineupSelection.setVisible(true);
		jPnlLineupSelection.requestFocus();
	}
	
	private void jBtnEnterLineupCancelActionPerformed() {
		if (!enteringLineup)	return;
		
		// hiding lineup selection labels -> will be replaced next time
		for (JLabel label : jLblsLineupSelectionPlayers) {
			label.setVisible(false);
		}
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
		
		jPnlLineupSelection.setVisible(false);
		enteringLineup = false;
		requestFocus();
	}
	
	private void squadNumberDigit(int digit) {
		squadNumberToSelect *= 10;
		squadNumberToSelect += digit;
	}
	
	private void squadNumberCompleted() {
		for (int i = 0; i < jLblsLineupSelectionPlayers.length; i++) {
			if (jLblsLineupSelectionPlayers[i].getText().startsWith(squadNumberToSelect + " ")) {
				playerSelected(i);
				break;
			}
		}
		squadNumberToSelect = 0;
	}
	
	private void playerSelected(int index) {
		numberOfSelectedPlayers += (playerSelected[index] = !playerSelected[index]) ? 1 : -1;
		jLblNumberOfSelectedPlayers.setText(numberOfSelectedPlayers + " Spieler ausgewählt");
		jLblsLineupSelectionPlayers[index].setOpaque(playerSelected[index]);
		repaintImmediately(jLblsLineupSelectionPlayers[index]);
		jPnlLineupSelection.requestFocus();
	}
	
	private void jBtnEnterLineupCompletedActionPerformed() {
		if (!enteringLineup)	return;
		
		int numberOfPlayers = 0, counter = 0;
		// count number of players
		for (int i = 0; i < playerSelected.length; i++) {
			if (playerSelected[i])	numberOfPlayers++;
		}
		
		if (numberOfPlayers != numberOfPlayersInLineUp) {
			message("You must choose eleven players.");
			return;
		}
		
		if (editingFirstTeam && lineupHome == null)	lineupHome = new int[numberOfPlayersInLineUp];
		else if (lineupAway == null)				lineupAway = new int[numberOfPlayersInLineUp];
		
		for (int i = 0; i < playerSelected.length; i++) {
			if (playerSelected[i]) {
				if (editingFirstTeam) {
					lineupHome[counter] = kaderHome.get(i).getSquadNumber();
					jLblsPlayersHome[counter].setText(kaderHome.get(i).getPlayer().getPopularOrLastName());
					jLblsPlayersHome[counter].setVisible(true);
					jLblsSquadNumbersHome[counter].setText("" + lineupHome[counter]);
					jLblsSquadNumbersHome[counter++].setVisible(true);
				} else {
					lineupAway[counter] = kaderAway.get(i).getSquadNumber();
					jLblsPlayersAway[counter].setText(kaderAway.get(i).getPlayer().getPopularOrLastName());
					jLblsPlayersAway[counter].setVisible(true);
					jLblsSquadNumbersAway[counter].setText("" + lineupAway[counter]);
					jLblsSquadNumbersAway[counter++].setVisible(true);
				}
			}
		}
		validateMatchDataOnLineupChange();
		
		match.setLineupHome(lineupHome);
		match.setLineupAway(lineupAway);
		
		// hiding lineup selection labels -> will be replaced next time
		for (JLabel label : jLblsLineupSelectionPlayers) {
			label.setVisible(false);
		}
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
		
		jPnlLineupSelection.setVisible(false);
		enteringLineup = false;
		requestFocus();
	}
	
	private void validateMatchDataOnLineupChange() {
		boolean affectsGoals = false, affectsSubsHome = false, affectsSubsAway = false, affectsBookings = false;
		// Goals
		for (int index = 0; index < goals.size(); index++) {
			Tor goal = goals.get(index);
			boolean scorerOnPitch = false, assisterOnPitch = false;
			if (goal.getScorer() != null) {
				boolean firstTeam = goal.isFirstTeam() ^ goal.isOwnGoal();
				int sqN = goal.getScorer().getSquadNumber();
				if (firstTeam) {
					for (int i = 0; i < lineupHome.length; i++) {
						if (lineupHome[i] == sqN)	scorerOnPitch = true;
					}
					for (Wechsel substitution : substitutionsHome) {
						if (substitution.isPlayerOn(sqN)) scorerOnPitch = true;
					}
				} else {
					for (int i = 0; i < lineupAway.length; i++) {
						if (lineupAway[i] == sqN)	scorerOnPitch = true;
					}
					for (Wechsel substitution : substitutionsAway) {
						if (substitution.isPlayerOn(sqN)) scorerOnPitch = true;
					}
				}
			}
			else	scorerOnPitch = true;
			if (goal.getAssister() != null) {
				boolean firstTeam = goal.isFirstTeam();
				int sqN = goal.getAssister().getSquadNumber();
				if (firstTeam) {
					for (int i = 0; i < lineupHome.length; i++) {
						if (lineupHome[i] == sqN)	assisterOnPitch = true;
					}
					for (Wechsel substitution : substitutionsHome) {
						if (substitution.isPlayerOn(sqN)) assisterOnPitch = true;
					}
				} else {
					for (int i = 0; i < lineupAway.length; i++) {
						if (lineupAway[i] == sqN)	assisterOnPitch = true;
					}
					for (Wechsel substitution : substitutionsAway) {
						if (substitution.isPlayerOn(sqN)) assisterOnPitch = true;
					}
				}
			}
			else	assisterOnPitch = true;
			if (!scorerOnPitch || !assisterOnPitch) {
				match.removeGoal(goal);
				affectsGoals = true;
				index--;
			}
		}
		// Substitutions
		for (int index = 0; index < substitutionsHome.size(); index++) {
			Wechsel substitution = substitutionsHome.get(index);
			int sqNumberOff = substitution.getPlayerOff().getSquadNumber();
			int sqNumberOn = substitution.getPlayerOn().getSquadNumber();
			boolean subOffOnPitch = false, subOnInLineup = false;
			for (int i = 0; i < lineupHome.length; i++) {
				if (lineupHome[i] == sqNumberOff)	subOffOnPitch = true;
				if (lineupHome[i] == sqNumberOn)	subOnInLineup = true;
			}
			for (int i = 0; i < index; i++) {
				if (substitutionsHome.get(i).isPlayerOn(sqNumberOff)) subOffOnPitch = true;
			}
			
			if (!subOffOnPitch || subOnInLineup) {
				match.removeSubstitution(substitution);
				affectsSubsHome = true;
				index--;
			}
		}
		for (int index = 0; index < substitutionsAway.size(); index++) {
			Wechsel substitution = substitutionsAway.get(index);
			int sqNumberOff = substitution.getPlayerOff().getSquadNumber();
			int sqNumberOn = substitution.getPlayerOn().getSquadNumber();
			boolean subOffOnPitch = false, subOnInLineup = false;
			for (int i = 0; i < lineupAway.length; i++) {
				if (lineupAway[i] == sqNumberOff)	subOffOnPitch = true;
				if (lineupAway[i] == sqNumberOn)	subOnInLineup = true;
			}
			for (int i = 0; i < index; i++) {
				if (substitutionsAway.get(i).isPlayerOn(sqNumberOff)) subOffOnPitch = true;
			}
			
			if (!subOffOnPitch || subOnInLineup) {
				match.removeSubstitution(substitution);
				affectsSubsAway = true;
				index--;
			}
		}
		// Bookings
		for (int index = 0; index < bookings.size(); index++) {
			Karte booking = bookings.get(index);
			int sqNumber = booking.getBookedPlayer().getSquadNumber();
			boolean found = false;
			if (booking.isFirstTeam()) {
				for (int i = 0; i < lineupHome.length; i++) {
					if (lineupHome[i] == sqNumber)	found = true;
				}
				for (Wechsel substitution : substitutionsHome) {
					if (substitution.isPlayerOn(sqNumber))	found = true;
				}
			} else {
				for (int i = 0; i < lineupAway.length; i++) {
					if (lineupAway[i] == sqNumber)	found = true;
				}
				for (Wechsel substitution : substitutionsAway) {
					if (substitution.isPlayerOn(sqNumber))	found = true;
				}
			}
			if (!found) {
				match.removeBooking(booking);
				affectsBookings = true;
				index--;
			}
		}
		if (affectsGoals)		paintGoals();
		if (affectsSubsHome)	paintSubstitutions(true);
		if (affectsSubsAway)	paintSubstitutions(false);
		if (affectsBookings)	paintBookings();
		result = match.getResult();
		if (result != null)	setResult();
	}
	
	public void jBtnGoActionPerformed() {
		if (enteringBooking || enteringGoal || enteringLineup || enteringSubstitution) {
			if (yesNoDialog("Es werden gerade Daten verändert, die beim Verlassen des Fensters nicht gespeichert werden. Trotzdem fortfahren?") == JOptionPane.NO_OPTION)	return;
		}
		this.setVisible(false);
		spieltag.saveMatchInfos(this, result, matchIndex);
	}
}
