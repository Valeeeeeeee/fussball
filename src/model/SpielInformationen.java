package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class SpielInformationen extends JFrame {
	private static final long serialVersionUID = 7503825008840407522L;
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 550;
	
	private int matchIndex;
	private Spiel spiel;
	
	private JPanel jPnlSpielInformationen;
	
	private JLabel jLblWettbewerb;
	private JLabel jLblDatum;
	
	private JLabel jLblHomeTeamName;
	private JLabel jLblResult;
	private JLabel jLblZusatz;
	private JLabel jLblAwayTeamName;
	
	private JButton jBtnStartGame;
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
	private JButton jBtnLineupSelectionCancel;
	private JButton jBtnLineupSelectionCompleted;
	
	private JLabel[] jLblsPlayersHome;
	private JLabel[] jLblsPlayersAway;
	private JLabel[] jLblsBookingsHome;
	private JLabel[] jLblsBookingsAway;
	private JLabel[] jLblsSubsOffMinutesHome;
	private JLabel[] jLblsSubsOffMinutesAway;
	private JLabel[] jLblsSubsOnMinutesHome;
	private JLabel[] jLblsSubsOnMinutesAway;
	private ArrayList<JLabel> jLblsGoals = new ArrayList<>();
	
	private JPanel jPnlEingabe;
	private JButton jBtnEingabeCancelled;
	private JButton jBtnEingabeCompleted;
	private JLabel jLblMinute;
	private JTextField jTFMinute;
	private JCheckBox jChBLeft;
	private JCheckBox jChBRight;
	private ButtonGroup buttonGroupDetails;
	private JLabel jLblOben;
	private JComboBox<String> jCBOben;
	private JLabel jLblUnten;
	private JComboBox<String> jCBUnten;
	
	private JPanel jPnlPenalties;
	private JLabel jLblPenalties;
	private ArrayList<JLabel> jLblsPenaltiesHome;
	private ArrayList<JLabel> jLblsPenaltiesAway;
	private JButton jBtnPenaltiesCompleted;
	
	// Obere Labels
	private Rectangle REC_PNLSPINFO = new Rectangle(0, 0, 800, 550);
	private Rectangle REC_LBLWETTBW = new Rectangle(200, 10, 400, 20);
	private Rectangle REC_LBLDATUM = new Rectangle(340, 35, 120, 25);
	private Rectangle REC_LBLHOMENAME = new Rectangle(25, 60, 330, 40);
	private Rectangle REC_LBLRESULT = new Rectangle(360, 60, 80, 40);
	private Rectangle REC_LBLZUSATZ = new Rectangle(370, 90, 60, 20);
	private Rectangle REC_LBLAWAYNAME = new Rectangle(445, 60, 330, 40);
	private Rectangle REC_BTNAGTHOME = new Rectangle(260, 35, 70, 25);
	private Rectangle REC_BTNAGTAWAY = new Rectangle(470, 35, 70, 25);
	
	// Untere Button-Reihe
	private Rectangle REC_BTNSTARTGAME = new Rectangle(350, 110, 100, 30);
	private Rectangle REC_BTNLINEUPHOME = new Rectangle(30, 110, 110, 30);
	private Rectangle REC_BTNLINEUPAWAY = new Rectangle(660, 110, 110, 30);
	private Rectangle REC_BTNSUBSTITUTIONHOME = new Rectangle(145, 110, 90, 30);
	private Rectangle REC_BTNSUBSTITUTIONAWAY = new Rectangle(565, 110, 90, 30);
	private Rectangle REC_BTNGOALHOME = new Rectangle(315, 110, 60, 30);
	private Rectangle REC_BTNGOALAWAY = new Rectangle(425, 110, 60, 30);
	private Rectangle REC_BTNBOOKINGHOME = new Rectangle(240, 110, 70, 30);
	private Rectangle REC_BTNBOOKINGAWAY = new Rectangle(490, 110, 70, 30);
	private Rectangle REC_BTNPENALTIES = new Rectangle(325, 170, 150, 25);
	
	// Labels Aufstellung, Wechsel, Tore
	private int[] subMinsLbls = new int[] {90, 160, 580, 25, 40, 20};
	private int[] luLbls = new int[] {135, 160, 405, 25, 125, 20};
	private int[] bLbls = new int[] {265, 160, 256, 25, 14, 20};
	private int[] gLbls = new int[] {305, 160, 30, 25, 160, 20};
	
	// Toreingabe
	private Point LOC_PNLEINGABEHOME = new Point(120, 150);
	private Point LOC_PNLEINGABEAWAY = new Point(410, 150);
	private Dimension DIM_PNLEINGABE = new Dimension(270, 130);
	private Rectangle REC_BTNTOREINGCANCL = new Rectangle(145, 5, 40, 30);
	private Rectangle REC_BTNTOREINGCOMPL = new Rectangle(190, 5, 70, 30);
	private Rectangle REC_LBLMINUTE = new Rectangle(50, 10, 70, 20);
	private Rectangle REC_TFMINUTE = new Rectangle(10, 10, 40, 20);
	private Rectangle REC_CHBLEFT = new Rectangle(20, 40, 105, 20);
	private Rectangle REC_CHBRIGHT = new Rectangle(155, 40, 95, 20);
	private Rectangle REC_LBLOBEN = new Rectangle(10, 70, 95, 20);
	private Rectangle REC_CBOBEN = new Rectangle(105, 67, 155, 26);
	private Rectangle REC_LBLUNTEN = new Rectangle(10, 100, 95, 20);
	private Rectangle REC_CBUNTEN = new Rectangle(105, 97, 155, 26);
	
	// Lineup selection
	private Point LOC_PNLLINEUPHOMESEL = new Point(30, 150);
	private Point LOC_PNLLINEUPAWAYSEL = new Point(490, 150);
	private Dimension DIM_PNLLINEUPSEL = new Dimension(280, 370);
	private Rectangle REC_BTNLUSCANCEL = new Rectangle(100, 330, 90, 30);
	private Rectangle REC_BTNLUSCOMPL = new Rectangle(200, 330, 70, 30);
	
	// Penalties
	private Rectangle REC_PNLPENALTIES = new Rectangle(170, 120, 460, 120);
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
	private Color ausgSpielerColor = new Color(224, 0, 0);
	private Color eingSpielerColor = new Color(0, 224, 0);
	private Color colorYellowCard = new Color(224, 224, 0);
	private Color colorRedCard = new Color(255, 0, 0);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private Font fontSecondBooking = new Font("Calibri", Font.BOLD, 15);
	private Font fontTeamNames = new Font("Verdana", Font.PLAIN, 24);
	private static final int PENALTY_NO = 0;
	private static final int PENALTY_IN = 1;
	private static final int PENALTY_OUT = 2;
	
	private static final int numberOfPlayersInLineUp = 11;
	private static final int maximumNumberOfSubstitutions = 3;

	private int[] boundsLSP = new int[] {5, 5, 5, 1, 130, 20};
	private int playersPerColumn = 15;
	
	private Spieltag spieltag;
	private Ergebnis ergebnis;
	private ArrayList<Spieler> kaderHome;
	private ArrayList<Spieler> kaderAway;
	private ArrayList<Spieler> eligiblePlayersListUpper  = new ArrayList<>();
	private ArrayList<Spieler> eligiblePlayersListLower  = new ArrayList<>();
	private boolean[] playerSelected;
	private boolean editingFirstTeam;
	private boolean enteringLineup;
	private boolean enteringGoal;
	private boolean enteringSubstitution;
	private boolean enteringBooking;
	private int goalDetails;
	private int[] lineupHome;
	private int[] lineupAway;
	private ArrayList<Wechsel> substitutionsHome;
	private ArrayList<Wechsel> substitutionsAway;
	private ArrayList<Tor> goals;
	private ArrayList<Karte> bookings;
	private int changedElement = -1;
	private boolean repaint;
	private ArrayList<Integer> penaltiesHome;
	private ArrayList<Integer> penaltiesAway;
	private int latestPenalty;
	
	private boolean isETpossible = false;
	
	private JButton go;

	private Rectangle RECGO = new Rectangle(700, 10, 90, 40);
	
	public SpielInformationen(Spieltag spieltag, int matchIndex, Spiel spiel, Ergebnis ergebnis) {
		super();
		
		this.spieltag = spieltag;
		this.matchIndex = matchIndex;
		this.spiel = spiel;
		this.goals = spiel.getTore();
		this.substitutionsHome = spiel.getSubstitutions(true);
		this.substitutionsAway = spiel.getSubstitutions(false);
		this.bookings = spiel.getBookings();
		this.ergebnis = ergebnis;
		this.isETpossible = spiel.getWettbewerb().isETPossible();
		
		initGUI();
		displayGivenValues();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		int maxNumOfPlayers = numberOfPlayersInLineUp + maximumNumberOfSubstitutions;
		jLblsPlayersHome = new JLabel[maxNumOfPlayers];
		jLblsPlayersAway = new JLabel[maxNumOfPlayers];
		jLblsBookingsHome = new JLabel[maxNumOfPlayers];
		jLblsBookingsAway = new JLabel[maxNumOfPlayers];
		jLblsSubsOffMinutesHome = new JLabel[maxNumOfPlayers];
		jLblsSubsOffMinutesAway = new JLabel[maxNumOfPlayers];
		jLblsSubsOnMinutesHome = new JLabel[maximumNumberOfSubstitutions];
		jLblsSubsOnMinutesAway = new JLabel[maximumNumberOfSubstitutions];
		jLblsPenaltiesHome = new ArrayList<>();
		jLblsPenaltiesAway = new ArrayList<>();
		penaltiesHome = new ArrayList<>();
		penaltiesAway = new ArrayList<>();
		
		{
			jPnlSpielInformationen = new JPanel();
			getContentPane().add(jPnlSpielInformationen);
			jPnlSpielInformationen.setBounds(REC_PNLSPINFO);
			jPnlSpielInformationen.setLayout(null);
			jPnlSpielInformationen.setBackground(background);
		}
		{
			jLblWettbewerb = new JLabel();
			jPnlSpielInformationen.add(jLblWettbewerb);
			jLblWettbewerb.setBounds(REC_LBLWETTBW);
			jLblWettbewerb.setText(spiel.getDescription());
			alignCenter(jLblWettbewerb);
		}
		{
			jLblDatum = new JLabel();
			jPnlSpielInformationen.add(jLblDatum);
			jLblDatum.setBounds(REC_LBLDATUM);
			jLblDatum.setText(spiel.getDateAndTime());
			alignCenter(jLblDatum);
		}
		{
			jLblHomeTeamName = new JLabel();
			jPnlSpielInformationen.add(jLblHomeTeamName);
			jLblHomeTeamName.setBounds(REC_LBLHOMENAME);
			jLblHomeTeamName.setFont(fontTeamNames);
			jLblHomeTeamName.setText(spiel.getHomeTeam().getName());
			alignRight(jLblHomeTeamName);
		}
		{
			jLblResult = new JLabel();
			jPnlSpielInformationen.add(jLblResult);
			jLblResult.setBounds(REC_LBLRESULT);
			jLblResult.setFont(fontTeamNames);
			jLblResult.setText(ergebnis != null ? ergebnis.getResult() : "-:-");
			alignCenter(jLblResult);
		}
		{
			jLblZusatz = new JLabel();
			jPnlSpielInformationen.add(jLblZusatz);
			jLblZusatz.setBounds(REC_LBLZUSATZ);
			jLblZusatz.setText(ergebnis != null ? ergebnis.getMore() : "");
			alignCenter(jLblZusatz);
		}
		{
			jLblAwayTeamName = new JLabel();
			jPnlSpielInformationen.add(jLblAwayTeamName);
			jLblAwayTeamName.setBounds(REC_LBLAWAYNAME);
			jLblAwayTeamName.setFont(fontTeamNames);
			jLblAwayTeamName.setText(spiel.getAwayTeam().getName());
			alignLeft(jLblAwayTeamName);
		}
		
		
		{
			jBtnStartGame = new JButton();
			jPnlSpielInformationen.add(jBtnStartGame);
			jBtnStartGame.setBounds(REC_BTNSTARTGAME);
			jBtnStartGame.setText("Anpfiff");
			jBtnStartGame.setFocusable(false);
			jBtnStartGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startGame();
				}
			});
		}
		{
			jBtnLineupHome = new JButton();
			jPnlSpielInformationen.add(jBtnLineupHome);
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
			jPnlSpielInformationen.add(jBtnLineupAway);
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
			jPnlSpielInformationen.add(jBtnSubstitutionHome);
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
			jPnlSpielInformationen.add(jBtnSubstitutionAway);
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
			jPnlSpielInformationen.add(jBtnGoalHome);
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
			jPnlSpielInformationen.add(jBtnGoalAway);
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
			jPnlSpielInformationen.add(jBtnBookingHome);
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
			jPnlSpielInformationen.add(jBtnBookingAway);
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
			jPnlSpielInformationen.add(jBtnAGTHome);
			jBtnAGTHome.setBounds(REC_BTNAGTHOME);
			jBtnAGTHome.setText("a.g.T.");
			jBtnAGTHome.setFocusable(false);
			jBtnAGTHome.setToolTipText("Sieg am gruenen Tisch");
			jBtnAGTHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAmGruenenTisch(true);
				}
			});
		}
		{
			jBtnAGTAway = new JButton();
			jPnlSpielInformationen.add(jBtnAGTAway);
			jBtnAGTAway.setBounds(REC_BTNAGTAWAY);
			jBtnAGTAway.setText("a.g.T.");
			jBtnAGTAway.setFocusable(false);
			jBtnAGTAway.setToolTipText("Sieg am gruenen Tisch");
			jBtnAGTAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAmGruenenTisch(false);
				}
			});
		}
		{
			jBtnPenaltyShootout = new JButton();
			jPnlSpielInformationen.add(jBtnPenaltyShootout);
			jBtnPenaltyShootout.setBounds(REC_BTNPENALTIES);
			jBtnPenaltyShootout.setText("Elfmeterschiessen");
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
			jPnlSpielInformationen.add(jPnlPenalties);
			jPnlPenalties.setBounds(REC_PNLPENALTIES);
			jPnlPenalties.setLayout(null);
			jPnlPenalties.setBackground(penaltiesBGColor);
			jPnlPenalties.setVisible(false);
		}
		{
			jLblPenalties = new JLabel();
			jPnlPenalties.add(jLblPenalties);
			jLblPenalties.setBounds(REC_LBLPENALTIES);
			jLblPenalties.setText("Elfmeterschiessen");
		}
		addPLabels(5);
		{
			jBtnPenaltiesCompleted = new JButton();
			jPnlPenalties.add(jBtnPenaltiesCompleted);
			jBtnPenaltiesCompleted.setBounds(REC_BTNPENALTIESCOMPL);
			jBtnPenaltiesCompleted.setText("Fertig");
			jBtnPenaltiesCompleted.setFocusable(false);
			jBtnPenaltiesCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					finishPenaltyShootout();
				}
			});
		}
		for (int i = 0; i < maxNumOfPlayers; i++) {
			jLblsPlayersHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsPlayersHome[i]);
			jLblsPlayersHome[i].setLocation(luLbls[STARTX], luLbls[STARTY] + i * luLbls[GAPY]);
			jLblsPlayersHome[i].setSize(luLbls[SIZEX], luLbls[SIZEY]);
			alignRight(jLblsPlayersHome[i]);
			jLblsPlayersHome[i].setVisible(false);
			
			jLblsPlayersAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsPlayersAway[i]);
			jLblsPlayersAway[i].setLocation(luLbls[STARTX] + luLbls[GAPX], luLbls[STARTY] + i * luLbls[GAPY]);
			jLblsPlayersAway[i].setSize(luLbls[SIZEX], luLbls[SIZEY]);
			alignLeft(jLblsPlayersAway[i]);
			jLblsPlayersAway[i].setVisible(false);
			
			jLblsBookingsHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsBookingsHome[i]);
			jLblsBookingsHome[i].setLocation(bLbls[STARTX], bLbls[STARTY] + i * bLbls[GAPY]);
			jLblsBookingsHome[i].setSize(bLbls[SIZEX], bLbls[SIZEY]);
			
			jLblsBookingsAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsBookingsAway[i]);
			jLblsBookingsAway[i].setLocation(bLbls[STARTX] + bLbls[GAPX], bLbls[STARTY] + i * bLbls[GAPY]);
			jLblsBookingsAway[i].setSize(bLbls[SIZEX], bLbls[SIZEY]);
		}
		for (int i = 0; i < maxNumOfPlayers; i++) {
			jLblsSubsOffMinutesHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsSubsOffMinutesHome[i]);
			jLblsSubsOffMinutesHome[i].setLocation(subMinsLbls[STARTX] - 45, subMinsLbls[STARTY] + i * subMinsLbls[GAPY]);
			jLblsSubsOffMinutesHome[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignLeft(jLblsSubsOffMinutesHome[i]);
			jLblsSubsOffMinutesHome[i].setCursor(handCursor);
			jLblsSubsOffMinutesHome[i].setVisible(false);
			jLblsSubsOffMinutesHome[i].setForeground(ausgSpielerColor);
			
			jLblsSubsOffMinutesAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsSubsOffMinutesAway[i]);
			jLblsSubsOffMinutesAway[i].setLocation(subMinsLbls[STARTX] + subMinsLbls[GAPX] + 45, subMinsLbls[STARTY] + i * subMinsLbls[GAPY]);
			jLblsSubsOffMinutesAway[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignRight(jLblsSubsOffMinutesAway[i]);
			jLblsSubsOffMinutesAway[i].setCursor(handCursor);
			jLblsSubsOffMinutesAway[i].setVisible(false);
			jLblsSubsOffMinutesAway[i].setForeground(ausgSpielerColor);
		}
		for (int i = 0; i < 3; i++) {
			jLblsSubsOnMinutesHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsSubsOnMinutesHome[i]);
			jLblsSubsOnMinutesHome[i].setLocation(subMinsLbls[STARTX], subMinsLbls[STARTY] + (numberOfPlayersInLineUp + i) * subMinsLbls[GAPY]);
			jLblsSubsOnMinutesHome[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignLeft(jLblsSubsOnMinutesHome[i]);
			jLblsSubsOnMinutesHome[i].setCursor(handCursor);
			jLblsSubsOnMinutesHome[i].setVisible(false);
			jLblsSubsOnMinutesHome[i].setForeground(eingSpielerColor);
			
			jLblsSubsOnMinutesAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsSubsOnMinutesAway[i]);
			jLblsSubsOnMinutesAway[i].setLocation(subMinsLbls[STARTX] + subMinsLbls[GAPX], subMinsLbls[STARTY] + (numberOfPlayersInLineUp + i) * subMinsLbls[GAPY]);
			jLblsSubsOnMinutesAway[i].setSize(subMinsLbls[SIZEX], subMinsLbls[SIZEY]);
			alignRight(jLblsSubsOnMinutesAway[i]);
			jLblsSubsOnMinutesAway[i].setCursor(handCursor);
			jLblsSubsOnMinutesAway[i].setVisible(false);
			jLblsSubsOnMinutesAway[i].setForeground(eingSpielerColor);
		}
		
		{
			jPnlLineupSelection = new JPanel();
			jPnlSpielInformationen.add(jPnlLineupSelection);
			jPnlLineupSelection.setSize(DIM_PNLLINEUPSEL);
			jPnlLineupSelection.setLayout(null);
			jPnlLineupSelection.setBackground(lineupSelColor);
			jPnlLineupSelection.setVisible(false);
		}
		{
			jBtnLineupSelectionCompleted = new JButton();
			jPnlLineupSelection.add(jBtnLineupSelectionCompleted);
			jBtnLineupSelectionCompleted.setBounds(REC_BTNLUSCOMPL);
			jBtnLineupSelectionCompleted.setText("fertig");
			jBtnLineupSelectionCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnLineupSelectionCompletedActionPerformed();
				}
			});
		}
		{
			jBtnLineupSelectionCancel = new JButton();
			jPnlLineupSelection.add(jBtnLineupSelectionCancel);
			jBtnLineupSelectionCancel.setBounds(REC_BTNLUSCANCEL);
			jBtnLineupSelectionCancel.setText("abbrechen");
			jBtnLineupSelectionCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnLineupSelectionCancelActionPerformed();
				}
			});
		}
		
		{
			jPnlEingabe = new JPanel();
			jPnlSpielInformationen.add(jPnlEingabe);
			jPnlEingabe.setSize(DIM_PNLEINGABE);
			jPnlEingabe.setLayout(null);
			jPnlEingabe.setBackground(lineupSelColor);
			jPnlEingabe.setVisible(false);
		}
		{
			jBtnEingabeCancelled = new JButton();
			jPnlEingabe.add(jBtnEingabeCancelled);
			jBtnEingabeCancelled.setBounds(REC_BTNTOREINGCANCL);
			jBtnEingabeCancelled.setText("X");
			jBtnEingabeCancelled.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnEingabeCancelledActionPerformed();
				}
			});
		}
		{
			jBtnEingabeCompleted = new JButton();
			jPnlEingabe.add(jBtnEingabeCompleted);
			jBtnEingabeCompleted.setBounds(REC_BTNTOREINGCOMPL);
			jBtnEingabeCompleted.setText("fertig");
			jBtnEingabeCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (enteringSubstitution)	jBtnWechseleingabeCompletedActionPerformed();
					else if (enteringGoal)		jBtnToreingabeCompletedActionPerformed();
					else if (enteringBooking)	jBtnKarteneingabeCompletedActionPerformed();
				}
			});
		}
		{
			jLblMinute = new JLabel();
			jPnlEingabe.add(jLblMinute);
			jLblMinute.setBounds(REC_LBLMINUTE);
			jLblMinute.setText(". Minute");
		}
		{
			jTFMinute = new JTextField();
			jPnlEingabe.add(jTFMinute);
			jTFMinute.setBounds(REC_TFMINUTE);
			jTFMinute.setText("");
			jTFMinute.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent ke) {
					if (ke.getKeyChar() <= 47 || ke.getKeyChar() >= 58) {
						ke.consume();
					}
				}
			});
		}
		{
			jChBLeft = new JCheckBox();
			jPnlEingabe.add(jChBLeft);
			jChBLeft.setBounds(REC_CHBLEFT);
			jChBLeft.setOpaque(false);
			jChBLeft.setVisible(false);
			jChBLeft.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBLeftSelectionChanged();
				}
			});
		}
		{
			jChBRight = new JCheckBox();
			jPnlEingabe.add(jChBRight);
			jChBRight.setBounds(REC_CHBRIGHT);
			jChBRight.setOpaque(false);
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
			jLblOben = new JLabel();
			jPnlEingabe.add(jLblOben);
			jLblOben.setBounds(REC_LBLOBEN);
		}
		{
			jCBOben = new JComboBox<>();
			jPnlEingabe.add(jCBOben);
	        jCBOben.setBounds(REC_CBOBEN);
	        jCBOben.setFocusable(false);
		}
		{
			jLblUnten = new JLabel();
			jPnlEingabe.add(jLblUnten);
			jLblUnten.setBounds(REC_LBLUNTEN);
		}
		{
			jCBUnten = new JComboBox<>();
			jPnlEingabe.add(jCBUnten);
			jCBUnten.setBounds(REC_CBUNTEN);
	        jCBUnten.setFocusable(false);
		}
		
		
		{
			go = new JButton();
			jPnlSpielInformationen.add(go);
			go.setBounds(RECGO);
			go.setText("fertig");
			go.setFocusable(false);
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
			        goActionPerformed();
				}
			});
		}
		
		setSize(WIDTH + getWindowDecorationWidth(), HEIGHT + getWindowDecorationHeight());
		setResizable(false);
	}
	
	private void displayGivenValues() {
		lineupHome = spiel.getLineupHome();
		lineupAway = spiel.getLineupAway();
		
		for (int i = 0; i < numberOfPlayersInLineUp; i++) {
			if (lineupHome != null) {
				jLblsPlayersHome[i].setText(spiel.getHomeTeam().getSpieler(lineupHome[i], spiel.getDate()).getPseudonymOrLN());
				jLblsPlayersHome[i].setVisible(true);
			}
			if (lineupAway != null) {
				jLblsPlayersAway[i].setText(spiel.getAwayTeam().getSpieler(lineupAway[i], spiel.getDate()).getPseudonymOrLN());
				jLblsPlayersAway[i].setVisible(true);
			}
		}
		
		createPseudoGoals();
		paintGoals();
		paintSubstitutions(true);
		paintSubstitutions(false);
		paintBookings();
		
		if (goals.size() > 0 || substitutionsHome.size() > 0 || substitutionsAway.size() > 0 || 
				inThePast(spiel.getDate(), spiel.getTime()))	startGame();
	}
	
	private void createPseudoGoals() {
		// for matches without lineup, otherwise when modified later, all current goals would be lost
		if (ergebnis != null) {
			if (ergebnis.home(1) + ergebnis.away(1) != goals.size() && ergebnis.home(2) + ergebnis.away(2) != goals.size()) {
				int home = 0, away = 0;
				for (Tor tor : goals) {
					if (tor.isFirstTeam())	home++;
					else					away++;
				}
				for (int i = home; i < ergebnis.home(1); i++) {
					spiel.addGoal(new Tor(spiel, true, false, false, 1));
				}
				for (int i = away; i < ergebnis.away(1); i++) {
					spiel.addGoal(new Tor(spiel, false, false, false, 1));
				}
				for (int i = ergebnis.home(1); i < ergebnis.home(2); i++) {
					spiel.addGoal(new Tor(spiel, true, false, false, 91));
				}
				for (int i = ergebnis.away(1); i < ergebnis.away(2); i++) {
					spiel.addGoal(new Tor(spiel, false, false, false, 91));
				}
			}
			if (ergebnis.home(3) > ergebnis.home(2) || ergebnis.away(3) > ergebnis.away(2)) {
				int firstT = ergebnis.home(3) - ergebnis.home(2), secondT = ergebnis.away(3) - ergebnis.away(2);
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
		for (Tor tor : goals) {
			displayGoal(tor);
		}
		repaint = false;
	}
	
	private void paintSubstitutions(boolean firstTeam) {
		int index = 0;
		if (firstTeam) {
			for (Wechsel wechsel : substitutionsHome) {
				displaySubstitution(wechsel, index++);
			}
		} else {
			for (Wechsel wechsel : substitutionsAway) {
				displaySubstitution(wechsel, index++);
			}
		}
		repaint = false;
	}
	
	private void paintBookings() {
		for (Karte booking: bookings) {
			displayBooking(booking);
		}
	}
	
	private void displayGoal(Tor tor) {
		String zusatz = tor.isPenalty() ? " (11m)" : (tor.isOwnGoal() ? " (ET)" : "");
		String scorer = (tor.getScorer() != null ? tor.getScorer().getPseudonymOrLN() : "n/a"), minute = tor.getMinute() + "'";
		final int i = jLblsGoals.size();
		JLabel jLblNewGoal = new JLabel();
		jPnlSpielInformationen.add(jLblNewGoal);
		jLblNewGoal.setLocation(gLbls[STARTX] + (tor.isFirstTeam() ? 0 : gLbls[GAPX]), gLbls[STARTY] + i * gLbls[GAPY]);
		jLblNewGoal.setSize(gLbls[SIZEX], gLbls[SIZEY]);
		if (tor.isFirstTeam())	alignLeft(jLblNewGoal);
		else					alignRight(jLblNewGoal);
		jLblNewGoal.setText(tor.isFirstTeam() ? minute + " " + scorer + zusatz : scorer + zusatz + " " + minute);
		jLblNewGoal.setCursor(handCursor);
		jLblNewGoal.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeGoal(i);
			}
		});
		jLblsGoals.add(jLblNewGoal);
		Rectangle bds = jBtnPenaltyShootout.getBounds();
		jBtnPenaltyShootout.setBounds(bds.x, bds.y + gLbls[GAPY], bds.width, bds.height);
	}
	
	private void displaySubstitution(Wechsel wechsel, final int index) {
		int squadNumberOff = wechsel.getAusgewechselterSpieler().getSquadNumber();
		String subOn = wechsel.getEingewechselterSpieler().getPseudonymOrLN();
		String minute = wechsel.getMinute() + "'";
		final boolean firstTeam = wechsel.isFirstTeam();
		JLabel[] labels = firstTeam ? jLblsPlayersHome : jLblsPlayersAway;
		JLabel[] offMinutes = firstTeam ? jLblsSubsOffMinutesHome : jLblsSubsOffMinutesAway;
		JLabel[] onMinutes = firstTeam ? jLblsSubsOnMinutesHome : jLblsSubsOnMinutesAway;
		labels[numberOfPlayersInLineUp + index].setText(subOn);
		labels[numberOfPlayersInLineUp + index].setVisible(true);
		
		String arrowup = arrowUp();
		onMinutes[index].setText(firstTeam ? arrowup + " " + minute : minute + " " + arrowup);
		onMinutes[index].setVisible(true);
		onMinutes[index].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeSubstitution(firstTeam, index);
			}
		});
		
		int playerPos = -1;
		for (int j = 0; j < numberOfPlayersInLineUp && playerPos == -1; j++) {
			if (firstTeam && lineupHome[j] == squadNumberOff)		playerPos = j;
			else if (!firstTeam && lineupAway[j] == squadNumberOff)	playerPos = j;
		}
		if (playerPos == -1) {
			for (int j = 0; j < numberOfPlayersInLineUp && playerPos == -1; j++) {
				if (spiel.getSubstitutions(firstTeam).get(j).getEingewechselterSpieler().getSquadNumber() == squadNumberOff)	playerPos = numberOfPlayersInLineUp + j;
			}
		}
		String arrowdown = arrowDown();
		offMinutes[playerPos].setText(firstTeam ? arrowdown + " " + minute : minute + " " + arrowdown);
		offMinutes[playerPos].setVisible(true);
		offMinutes[playerPos].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeSubstitution(firstTeam, index);
			}
		});
	}
	
	private void displayBooking(Karte booking) {
		int squadNumber = booking.getBookedPlayer().getSquadNumber();
		boolean found = false, yellow = booking.isYellowCard(), second = booking.isSecondBooking();
		JLabel label = null;
		if (booking.isFirstTeam()) {
			for (int i = 0; i < lineupHome.length && !found; i++) {
				if (lineupHome[i] == squadNumber) {
					label = jLblsBookingsHome[i];
					found = true;
				}
			}
			if (!found) {
				int index = 0;
				for (Wechsel wechsel : substitutionsHome) {
					if (wechsel.getEingewechselterSpieler().getSquadNumber() == squadNumber) {
						label = jLblsBookingsHome[numberOfPlayersInLineUp + index];
						break;
					}
					index++;
				}
			}
		} else {
			for (int i = 0; i < lineupAway.length && !found; i++) {
				if (lineupAway[i] == squadNumber) {
					label = jLblsBookingsAway[i];
					found = true;
				}
			}
			if (!found) {
				int index = 0;
				for (Wechsel wechsel : substitutionsAway) {
					if (wechsel.getEingewechselterSpieler().getSquadNumber() == squadNumber) {
						label = jLblsBookingsAway[numberOfPlayersInLineUp + index];
						break;
					}
					index++;
				}
			}
		}

		if (yellow)	label.setBackground(colorYellowCard);
		else		label.setBackground(colorRedCard);
		alignCenter(label);
		label.setForeground(colorRedCard);
		label.setFont(fontSecondBooking);
		label.setText(second ? "2" : "");
		label.setOpaque(true);
	}
	
	private void changeGoal(int index) {
		changedElement = index;
		Tor tor = goals.get(index);
		this.repaint = true;
		this.enteringGoal = true;
		editingFirstTeam = tor.isFirstTeam();
		
		log("You want to change the goal of " + (tor.getScorer() == null ? "n/a" : tor.getScorer().getPseudonymOrLN()) + "(" + tor.getMinute() + ")");
		
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblOben.setText("Torschütze");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
		jLblUnten.setText("Vorbereiter");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
		
		
		jChBLeft.setText("Elfmeter");
		jChBRight.setText("Eigentor");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		if (tor.isPenalty()) {
			jChBLeft.setSelected(true);
			goalDetails = 1;
		} else if (tor.isOwnGoal()) {
			jChBRight.setSelected(true);
			goalDetails = 2;
		}
		if (tor.getScorer() != null)		jCBOben.setSelectedItem(tor.getScorer().getPseudonymOrLN());
		if (tor.getAssistgeber() != null)	jCBUnten.setSelectedItem(tor.getAssistgeber().getPseudonymOrLN());
		jTFMinute.setText("" + tor.getMinute());
		
		if (editingFirstTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void changeSubstitution(boolean firstTeam, int index) {
		if (enteringGoal || enteringSubstitution)	return;
		
		changedElement = index;
		Wechsel wechsel = (firstTeam ? substitutionsHome : substitutionsAway).get(index);
		this.repaint = true;
		this.enteringSubstitution = true;
		this.editingFirstTeam = firstTeam;
		
		log("You want to change the substitution " + wechsel.getAusgewechselterSpieler().getPseudonymOrLN() + "(" + wechsel.getMinute() + ". " + 
				wechsel.getEingewechselterSpieler().getPseudonymOrLN() + ")");
		
		int playerPos = -1, squadNumber = wechsel.getAusgewechselterSpieler().getSquadNumber();
		for (int j = 0; j < numberOfPlayersInLineUp && playerPos == -1; j++) {
			if (firstTeam && lineupHome[j] == squadNumber)			playerPos = j;
			else if (!firstTeam && lineupAway[j] == squadNumber)	playerPos = j;
		}
		if (playerPos == -1) {
			for (int j = 0; j < numberOfPlayersInLineUp && playerPos == -1; j++) {
				if (spiel.getSubstitutions(firstTeam).get(j).getEingewechselterSpieler().getSquadNumber() == squadNumber)	playerPos = numberOfPlayersInLineUp + j;
			}
		}
		JLabel[] minutes = firstTeam ? jLblsSubsOffMinutesHome : jLblsSubsOffMinutesAway;
		minutes[playerPos].setText("");
		
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblOben.setText("ausgewechselt");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(true)));
		jLblUnten.setText("eingewechselt");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(false)));
		
		jCBOben.setSelectedItem(wechsel.getAusgewechselterSpieler().getPseudonymOrLN());
		jCBUnten.setSelectedItem(wechsel.getEingewechselterSpieler().getPseudonymOrLN());
		jTFMinute.setText("" + wechsel.getMinute());
		
		if (editingFirstTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void setAmGruenenTisch(boolean isHomeTeam) {
		ergebnis = new Ergebnis((isHomeTeam ? "3:0" : "0:3") + " agT");
		setErgebnis();
	}
	
	private void setErgebnis() {
		spiel.setErgebnis(ergebnis);
		jLblResult.setText(ergebnis.getResult());
		jLblZusatz.setText(ergebnis.getMore());
		jLblZusatz.setToolTipText(ergebnis.getTooltipText());
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
			message("Ein Elfmeterschiessen kann nicht unentschieden enden.");
			return;
		}
		
		String regular = ergebnis.home(1) + ":" + ergebnis.away(1);
		String extra = ergebnis.home(2) + ":" + ergebnis.away(2);
		ergebnis = new Ergebnis((penH + ergebnis.home(2)) + ":" + (penA + ergebnis.away(2)) + "nE (" + extra + "," + regular + ")");
		setErgebnis();
		
		showPenalties(false);
	}
	
	private void startGame() {
		if (ergebnis == null)	ergebnis = new Ergebnis("0:0");
		setErgebnis();
		
		jBtnStartGame.setVisible(false);
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
				label.setVisible(value && label.getText() != "");
			}
			for (JLabel label : jLblsBookingsHome) {
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
				label.setVisible(value && label.getText() != "");
			}
			for (JLabel label : jLblsBookingsAway) {
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
	
	private void jBtnEingabeCancelledActionPerformed() {
		enteringSubstitution = false;
		enteringBooking = false;
		enteringGoal = false;
		repaint = false;
		changedElement = -1;
		
		jPnlEingabe.setVisible(false);
		jTFMinute.setText("");
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
	}
	
	private void enterNewSubstitution(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		if ((firstTeam && lineupHome == null) || (!firstTeam && lineupAway == null))	return;
		
		if (spiel.getSubstitutions(firstTeam).size() == 3) {
			message("You have already submitted all three possible substitutions for this team.");
			return;
		}
		
		enteringSubstitution = true;
		editingFirstTeam = firstTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblOben.setText("ausgewechselt");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(true)));
		jCBOben.setSelectedIndex(jCBOben.getModel().getSize() / 2);
		jLblUnten.setText("eingewechselt");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(false)));
		jCBUnten.setSelectedIndex(jCBUnten.getModel().getSize() / 2);
		
		if (editingFirstTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private String[] getEligiblePlayersSub(boolean subOff) {
		String[] eligiblePlayers;
		
		Mannschaft team = editingFirstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
		ArrayList<Wechsel> substitutions = spiel.getSubstitutions(editingFirstTeam);
		int[] lineup = editingFirstTeam ? lineupHome : lineupAway;
		
		if (subOff) {
			eligiblePlayersListUpper.clear();
			for (int i = 0; i < lineup.length; i++) {
				eligiblePlayersListUpper.add(team.getSpieler(lineup[i], spiel.getDate()));
			}
			for (Wechsel wechsel : substitutions) {
				eligiblePlayersListUpper.remove(wechsel.getAusgewechselterSpieler());
				eligiblePlayersListUpper.add(wechsel.getEingewechselterSpieler());
			}
			eligiblePlayers = new String[eligiblePlayersListUpper.size()];
			for (int i = 0; i < eligiblePlayers.length; i++) {
				eligiblePlayers[i] = eligiblePlayersListUpper.get(i).getPseudonymOrLN();
			}
		} else {
			eligiblePlayersListLower = cloneList(team.getEligiblePlayers(spiel.getDate()));
			for (int i = 0; i < lineup.length; i++) {
				eligiblePlayersListLower.remove(team.getSpieler(lineup[i], spiel.getDate()));
			}
			for (Wechsel wechsel : substitutions) {
				eligiblePlayersListLower.remove(wechsel.getEingewechselterSpieler());
			}
			eligiblePlayers = new String[eligiblePlayersListLower.size()];
			for (int i = 0; i < eligiblePlayers.length; i++) {
				eligiblePlayers[i] = eligiblePlayersListLower.get(i).getPseudonymOrLN();
			}
		}
		
		return eligiblePlayers;
	}
	
	private void jBtnWechseleingabeCompletedActionPerformed() {
		if (!enteringSubstitution)	return;
		
		if (jTFMinute.getText().length() == 0) {
			message("You have to enter a minute before you can save.");
			return;
		}
		
		int minute = Integer.parseInt(jTFMinute.getText());
		if (minute > 120) {
			message("Ein Wechsel kann nicht nach der 120. Minute erfolgen.");
			return;
		} else if (!isETpossible && minute > 90) {
			message("In diesem Spiel kann es keine Verlaengerung geben.");
			return;
		}
		if (changedElement != -1)	(editingFirstTeam ? substitutionsHome : substitutionsAway).get(changedElement);
		changedElement = -1;
		for (Wechsel wechsel : spiel.getSubstitutions(editingFirstTeam)) {
			if (wechsel.getMinute() > minute) {
				repaint = true;
			}
		}
		Spieler ausgSpieler = eligiblePlayersListUpper.get(jCBOben.getSelectedIndex());
		Spieler eingSpieler = eligiblePlayersListLower.get(jCBUnten.getSelectedIndex());
		
		Wechsel substitution = new Wechsel(spiel, editingFirstTeam, minute, ausgSpieler, eingSpieler);
		int index = spiel.addSubstitution(substitution);
		if (repaint)	paintSubstitutions(editingFirstTeam);
		else			displaySubstitution(substitution, index);
		enteringSubstitution = false;
		
		jPnlEingabe.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
	}
	
	private void enterNewGoal(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		this.enteringGoal = true;
		this.editingFirstTeam = firstTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jChBLeft.setText("Elfmeter");
		jChBRight.setText("Eigentor");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		jLblOben.setText("Torschuetze");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
		jLblUnten.setText("Vorbereiter");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
		
		if (editingFirstTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void jChBLeftSelectionChanged() {
		if (enteringGoal) {
			if (goalDetails == 1) {
				buttonGroupDetails.clearSelection();
				goalDetails = 0;
			} else {
				if (goalDetails == 2) {
					jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
					jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
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
			jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
			jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(false)));
		}
	}
	
	private String[] getEligiblePlayers(boolean upper) {
		boolean ownGoal = jChBRight.isSelected();
		String[] eligiblePlayers;
		if (upper) {
			boolean firstTeam = editingFirstTeam ^ ownGoal;
			if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
				Mannschaft scoringTeam = firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
				int[] scoringLineup = firstTeam ? lineupHome : lineupAway;
				
				eligiblePlayersListUpper.clear();
				ArrayList<Wechsel> substitutions = spiel.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + numberOfPlayersInLineUp + substitutions.size()];
				for (int i = 0; i < numberOfPlayersInLineUp; i++) {
					eligiblePlayersListUpper.add(scoringTeam.getSpieler(scoringLineup[i], spiel.getDate()));
					eligiblePlayers[1 + i] = eligiblePlayersListUpper.get(i).getPseudonymOrLN();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListUpper.add(substitutions.get(i).getEingewechselterSpieler());
					eligiblePlayers[1 + numberOfPlayersInLineUp + i] = eligiblePlayersListUpper.get(numberOfPlayersInLineUp + i).getPseudonymOrLN();
				}
			} else {
				eligiblePlayers = new String[1];
			}
		} else {
			boolean firstTeam = editingFirstTeam;
			if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
				Mannschaft assistingTeam = firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
				int[] assistingLineup = firstTeam ? lineupHome : lineupAway;
				
				eligiblePlayersListLower.clear();
				ArrayList<Wechsel> substitutions = spiel.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + numberOfPlayersInLineUp + substitutions.size()];
				for (int i = 0; i < numberOfPlayersInLineUp; i++) {
					eligiblePlayersListLower.add(assistingTeam.getSpieler(assistingLineup[i], spiel.getDate()));
					eligiblePlayers[1 + i] = eligiblePlayersListLower.get(i).getPseudonymOrLN();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListLower.add(substitutions.get(i).getEingewechselterSpieler());
					eligiblePlayers[1 + numberOfPlayersInLineUp + i] = eligiblePlayersListLower.get(numberOfPlayersInLineUp + i).getPseudonymOrLN();
				}
			} else {
				eligiblePlayers = new String[1];
			}
		}
		
		eligiblePlayers[0] = "Bitte waehlen";
		
		return eligiblePlayers;
	}
	
	private void jBtnToreingabeCompletedActionPerformed() {
		if (!enteringGoal)	return;
		
		if (jTFMinute.getText().length() == 0) {
			message("You have to enter a minute before you can save.");
			return;
		}
		
		int minute = Integer.parseInt(jTFMinute.getText());
		if (minute > 120) {
			message("Ein Tor kann nicht nach der 120. Minute fallen.\nBenutzen Sie bitte fuer Elfmeterschiessen die dafuer vorgesehene Eingabemaske.");
			return;
		} else if (!isETpossible && minute > 90) {
			message("In diesem Spiel kann es keine Verlaengerung geben.");
			return;
		}
		for (Tor tor : goals) {
			if (tor.getMinute() > minute) {
				repaint = true;
			}
		}
		
		boolean penalty = jChBLeft.isSelected();
		boolean ownGoal = jChBRight.isSelected();
		int index;
		Spieler scorer = null;
		if ((index = jCBOben.getSelectedIndex()) != 0) {
			scorer = eligiblePlayersListUpper.get(index - 1);
			for (Wechsel wechsel : spiel.getSubstitutions(editingFirstTeam)) {
				if (wechsel.getAusgewechselterSpieler() == scorer && minute > wechsel.getMinute() || 
						wechsel.getEingewechselterSpieler() == scorer && minute < wechsel.getMinute()) {
					message("The player " + scorer.getPseudonymOrLN() + " was not on the pitch in the given minute.");
					return;
				}
			}
		}
		Spieler assistgeber = null;
		if ((index = jCBUnten.getSelectedIndex()) != 0) {
			assistgeber = eligiblePlayersListLower.get(index - 1);
			for (Wechsel wechsel : spiel.getSubstitutions(editingFirstTeam)) {
				if (wechsel.getAusgewechselterSpieler() == assistgeber && minute > wechsel.getMinute() || 
						wechsel.getEingewechselterSpieler() == assistgeber && minute < wechsel.getMinute()) {
					message("The player " + assistgeber.getPseudonymOrLN() + " was not on the pitch in the given minute.");
					return;
				}
			}
		}
		
		if (changedElement != -1)	goals.remove(changedElement);
		changedElement = -1;
		
		Tor tor = null;
		if (scorer == null)				tor = new Tor(spiel, editingFirstTeam, penalty, ownGoal, minute);
		else if (assistgeber == null)	tor = new Tor(spiel, editingFirstTeam, penalty, ownGoal, minute, scorer);
		else							tor = new Tor(spiel, editingFirstTeam, penalty, ownGoal, minute, scorer, assistgeber);
		spiel.addGoal(tor);
		ergebnis = spiel.getErgebnis();
		setErgebnis();
		if (repaint)	paintGoals();
		else			displayGoal(tor);
		enteringGoal = false;
		
		buttonGroupDetails.clearSelection();
		goalDetails = 0;
		jChBLeft.setVisible(false);
		jChBRight.setVisible(false);
		jPnlEingabe.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
	}
	
	private void enterNewBooking(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		
		this.enteringBooking = true;
		this.editingFirstTeam = firstTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jLblOben.setText("Spieler");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayers(true)));
		jLblUnten.setVisible(false);
		jCBUnten.setVisible(false);
		
		jChBLeft.setText("gelbe Karte");
		jChBRight.setText("rote Karte");
		jChBLeft.setVisible(true);
		jChBRight.setVisible(true);
		jChBLeft.setSelected(true);
		
		if (editingFirstTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void jBtnKarteneingabeCompletedActionPerformed() {
		if (!enteringBooking)	return;
		
		if (jTFMinute.getText().length() == 0) {
			message("Bitte eine Minute angeben.");
			return;
		}
		
		if (!jChBLeft.isSelected() && !jChBRight.isSelected()) {
			message("Bitte angeben, um welche Karte es sich handelt.");
			return;
		}
		
		if (jCBOben.getSelectedIndex() == 0) {
			message("Bitte einen Spieler auswaehlen.");
			return;
		}
		
		int minute = Integer.parseInt(jTFMinute.getText());
		if (minute > 120) {
			message("Ein Spieler kann nicht nach der 120. Minute verwarnt werden. Bitte eine korrekte Minute angeben.");
			return;
		} else if (!isETpossible && minute > 90) {
			message("In diesem Spiel kann es keine Verlaengerung geben. Bitte eine korrekte Minute angeben.");
			return;
		}
		
		boolean yellowCard = jChBLeft.isSelected();
		boolean isSecondBooking = false;
		
		int index = jCBOben.getSelectedIndex();
		Spieler bookedPlayer = eligiblePlayersListUpper.get(index - 1);
		for (Wechsel wechsel : spiel.getSubstitutions(editingFirstTeam)) {
			if (wechsel.getAusgewechselterSpieler() == bookedPlayer && minute > wechsel.getMinute() || 
					wechsel.getEingewechselterSpieler() == bookedPlayer && minute < wechsel.getMinute()) {
				message("Der Spieler " + bookedPlayer.getPseudonymOrLN() + " war in der angegebenen Minute noch nicht auf dem Spielfeld.");
				return;
			}
		}
		for (Karte booking : bookings) {
			if (booking.getBookedPlayer() == bookedPlayer) {
				if (!isSecondBooking && booking.isYellowCard()) {
					isSecondBooking = true;
				} else {
					message("Der Spieler " + bookedPlayer.getPseudonymOrLN() + " wurde bereits vom Platz gestellt.");
					return;
				}
			}
		}
		
		Karte booking = new Karte(spiel, editingFirstTeam, minute, yellowCard, isSecondBooking, bookedPlayer);
		spiel.addBooking(booking);
		displayBooking(booking);
		enteringBooking = false;
		
		buttonGroupDetails.clearSelection();
		jChBLeft.setVisible(false);
		jChBRight.setVisible(false);
		
		jLblUnten.setVisible(true);
		jCBUnten.setVisible(true);
		jPnlEingabe.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
	}
	
	private void enterNewLineup(boolean firstTeam) {
		if (enteringLineup || enteringGoal || enteringSubstitution || enteringBooking)	return;
		ArrayList<Spieler> kader;
		this.enteringLineup = true;
		this.editingFirstTeam = firstTeam;
		int[] lineup = editingFirstTeam ? lineupHome : lineupAway;
		if (editingFirstTeam)	kader = kaderHome = spiel.getHomeTeam().getEligiblePlayers(spiel.getDate());
		else					kader = kaderAway = spiel.getAwayTeam().getEligiblePlayers(spiel.getDate());
		
		// hide lineup labels
		setLabelsVisible(false);
		
		playerSelected = new boolean[kader.size()];
		
		// create lineup selection labels
		jLblsLineupSelectionPlayers = new JLabel[kader.size()];
		for (int i = 0; i < jLblsLineupSelectionPlayers.length; i++) {
			final int x = i;
			jLblsLineupSelectionPlayers[i] = new JLabel();
			jPnlLineupSelection.add(jLblsLineupSelectionPlayers[i]);
			jLblsLineupSelectionPlayers[i].setSize(boundsLSP[SIZEX], boundsLSP[SIZEY]);
			jLblsLineupSelectionPlayers[i].setLocation(boundsLSP[STARTX] + (i / playersPerColumn) * (boundsLSP[SIZEX] + boundsLSP[GAPX]), 
														boundsLSP[STARTY] + (i % playersPerColumn) * (boundsLSP[SIZEY] + boundsLSP[GAPY]));
			jLblsLineupSelectionPlayers[i].setText(kader.get(i).getSquadNumber() + " " + kader.get(i).getPseudonymOrLN());
			jLblsLineupSelectionPlayers[i].setBackground(playerSelectedColor);
			jLblsLineupSelectionPlayers[i].setCursor(handCursor);
			jLblsLineupSelectionPlayers[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					playerSelected(x);
				}
			});
		}
		
		if (lineup == null) {
			lineup = new int[numberOfPlayersInLineUp];
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
		
		if (editingFirstTeam)	jPnlLineupSelection.setLocation(LOC_PNLLINEUPHOMESEL);
		else					jPnlLineupSelection.setLocation(LOC_PNLLINEUPAWAYSEL);
		jPnlLineupSelection.setVisible(true);
	}
	
	private void jBtnLineupSelectionCancelActionPerformed() {
		if (!enteringLineup)	return;
		
		// hiding lineup selection labels -> will be replaced next time
		for (JLabel label : jLblsLineupSelectionPlayers) {
			label.setVisible(false);
		}
		
		// show hidden lineup labels
		setLabelsVisible(true);
		
		jPnlLineupSelection.setVisible(false);
		enteringLineup = false;
	}
	
	private void playerSelected(int index) {
		jLblsLineupSelectionPlayers[index].setOpaque(playerSelected[index] = !playerSelected[index]);
		repaintImmediately(jLblsLineupSelectionPlayers[index]);
	}
	
	private void jBtnLineupSelectionCompletedActionPerformed() {
		if (!enteringLineup)	return;
		
		int numberOfPlayers = 0, counter = 0;
		// count number of players
		for (int i = 0; i < playerSelected.length; i++) {
			if(playerSelected[i])	numberOfPlayers++;
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
					jLblsPlayersHome[counter].setText(kaderHome.get(i).getPseudonymOrLN());
					jLblsPlayersHome[counter++].setVisible(true);
				} else {
					lineupAway[counter] = kaderAway.get(i).getSquadNumber();
					jLblsPlayersAway[counter].setText(kaderAway.get(i).getPseudonymOrLN());
					jLblsPlayersAway[counter++].setVisible(true);
				}
			}
		}
		for (Karte booking : bookings) {
			int sqNumber = booking.getBookedPlayer().getSquadNumber();
			boolean found = false;
			if (booking.isFirstTeam()) {
				for (int i = 0; i < lineupHome.length; i++) {
					if (lineupHome[i] == sqNumber)	found = true;
				}
				for (Wechsel wechsel : substitutionsHome) {
					if (wechsel.getEingewechselterSpieler().getSquadNumber() == sqNumber)	found = true;
				}
			} else {
				for (int i = 0; i < lineupAway.length; i++) {
					if (lineupAway[i] == sqNumber)	found = true;
				}
				for (Wechsel wechsel : substitutionsAway) {
					if (wechsel.getEingewechselterSpieler().getSquadNumber() == sqNumber)	found = true;
				}
			}
			if (!found) {
				bookings.remove(booking);
			}
		}
		
		spiel.setLineupHome(lineupHome);
		spiel.setLineupAway(lineupAway);
		spiel.toString();
		
		// hiding lineup selection labels -> will be replaced next time
		for (JLabel label : jLblsLineupSelectionPlayers) {
			label.setVisible(false);
		}
		
		// show hidden lineup labels
		setLabelsVisible(true);
		
		jPnlLineupSelection.setVisible(false);
		enteringLineup = false;
	}
	
	public void goActionPerformed() {
		this.setVisible(false);
		spieltag.saveMatchInfos(this, ergebnis, matchIndex);
	}
}
