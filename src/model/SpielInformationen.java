package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class SpielInformationen extends JFrame {
	private static final long serialVersionUID = 7503825008840407522L;
	
	private final Dimension dim = new Dimension(700 + 6, 550 + 28);
	
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
	private JButton jBtnAGTHome;
	private JButton jBtnAGTAway;
	private JButton jBtnPenaltyShootout;
	
	private JPanel jPnlLineupSelection;
	private JLabel[] jLblsLineupSelectionPlayers;
	private JButton jBtnLineupSelectionCancel;
	private JButton jBtnLineupSelectionCompleted;
	
	private JLabel[] jLblsLineupHome;
	private JLabel[] jLblsLineupAway;
	private ArrayList<JLabel> jLblsSubstitutionsHome = new ArrayList<>();
	private ArrayList<JLabel> jLblsSubstitutionsAway = new ArrayList<>();
	private ArrayList<JLabel> jLblsGoals = new ArrayList<>();
	
	private JPanel jPnlEingabe;
	private JButton jBtnEingabeCompleted;
	private JLabel jLblMinute;
	private JTextField jTFMinute;
	private JCheckBox jChBPenalty;
	private JCheckBox jChBOwnGoal;
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
	private Rectangle REC_PNLSPINFO = new Rectangle(0, 0, 700, 550);
	private Rectangle REC_LBLWETTBW = new Rectangle(150, 10, 400, 20);
	private Rectangle REC_LBLDATUM = new Rectangle(290, 35, 120, 20);
	private Rectangle REC_LBLHOMENAME = new Rectangle(40, 60, 265, 40);
	private Rectangle REC_LBLRESULT = new Rectangle(310, 60, 80, 40);
	private Rectangle REC_LBLZUSATZ = new Rectangle(320, 90, 60, 20);
	private Rectangle REC_LBLAWAYNAME = new Rectangle(395, 60, 265, 40);
	private Rectangle REC_BTNAGTHOME = new Rectangle(210, 30, 70, 20);
	private Rectangle REC_BTNAGTAWAY = new Rectangle(420, 30, 70, 20);
	
	// Untere Button-Reihe
	private Rectangle REC_BTNSTARTGAME = new Rectangle(300, 110, 100, 30);
	private Rectangle REC_BTNLINEUPHOME = new Rectangle(30, 110, 110, 30);
	private Rectangle REC_BTNLINEUPAWAY = new Rectangle(560, 110, 110, 30);
	private Rectangle REC_BTNSUBSTITUTIONHOME = new Rectangle(150, 110, 90, 30);
	private Rectangle REC_BTNSUBSTITUTIONAWAY = new Rectangle(460, 110, 90, 30);
	private Rectangle REC_BTNGOALHOME = new Rectangle(250, 110, 60, 30);
	private Rectangle REC_BTNGOALAWAY = new Rectangle(390, 110, 60, 30);
	private Rectangle REC_BTNPENALTIES = new Rectangle(280, 150, 150, 20);
	
	// Labels Aufstellung, Wechsel, Tore
	private int[] subLbls = new int[] {25, 160, 540, 5, 110, 20};
	private int[] luLbls = new int[] {145, 160, 300, 5, 110, 20};
	private int[] gLbls = new int[] {265, 160, 40, 5, 130, 20};
	
	// Toreingabe
	private Point LOC_PNLEINGABEHOME = new Point(150, 150);
	private Point LOC_PNLEINGABEAWAY = new Point(310, 150);
	private Dimension DIM_PNLEINGABE = new Dimension(250, 130);
	private Rectangle REC_BTNTOREINGCOMPL = new Rectangle(170, 5, 70, 30);
	private Rectangle REC_LBLMINUTE = new Rectangle(40, 10, 70, 20);
	private Rectangle REC_TFMINUTE = new Rectangle(10, 10, 30, 20);
	private Rectangle REC_CHBPENALTY = new Rectangle(40, 40, 90, 20);
	private Rectangle REC_CHBOWNGOAL = new Rectangle(140, 40, 90, 20);
	private Rectangle REC_LBLOBEN = new Rectangle(10, 70, 95, 20);
	private Rectangle REC_CBOBEN = new Rectangle(105, 67, 135, 26);
	private Rectangle REC_LBLUNTEN = new Rectangle(10, 100, 95, 20);
	private Rectangle REC_CBUNTEN = new Rectangle(105, 97, 135, 26);
	
	// Lineup selection
	private Point LOC_PNLLINEUPHOMESEL = new Point(30, 150);
	private Point LOC_PNLLINEUPAWAYSEL = new Point(390, 150);
	private Dimension DIM_PNLLINEUPSEL = new Dimension(280, 350);
	private Rectangle REC_BTNLUSCANCEL = new Rectangle(120, 310, 70, 30);
	private Rectangle REC_BTNLUSCOMPL = new Rectangle(200, 310, 70, 30);
	
	// Penalties
	private Rectangle REC_PNLPENALTIES = new Rectangle(120, 120, 460, 120);
	private Rectangle REC_LBLPENALTIES = new Rectangle(10, 10, 130, 25);
	private Rectangle REC_BTNPENALTIESCOMPL = new Rectangle(380, 10, 70, 25);
	
	private int[] penaltyH = new int[] {20, 45, 35, 35, 30, 30};
	private int[] penaltyA = new int[] {270, 45, 35, 35, 30, 30};
	
	private Color color = new Color(255, 255, 0);
	private Color penaltiesBGColor = new Color(175, 255, 175);
	private Color penaltiesInColor = new Color(25, 255, 25);
	private Color penaltiesOutColor = new Color(255, 25, 25);
	private Color penaltiesNoColor = new Color(192, 192, 192);
	private Color lineupSelColor = new Color(175, 255, 175);
	private Color playerSelectedColor = new Color(255, 255, 0);
	private Color ausgSpielerColor = new Color(255, 55, 55);
	private Color eingSpielerColor = new Color(55, 255, 55);
	private Font fontTeamNames = new Font("Verdana", Font.PLAIN, 24);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	private int[] boundsLSP = new int[] {5, 5, 5, 1, 120, 20};
	private int playersPerColumn = 15;
	
	private Spieltag spieltag;
	private Ergebnis ergebnis;
	private ArrayList<Spieler> kaderHome;
	private ArrayList<Spieler> kaderAway;
	private ArrayList<Spieler> eligiblePlayersListUpper  = new ArrayList<>();
	private ArrayList<Spieler> eligiblePlayersListLower  = new ArrayList<>();
	private boolean[] playerSelected;
	private boolean editingHomeTeam;
	private boolean enteringLineup;
	private boolean enteringGoal;
	private boolean enteringSubstitution;
	private int goalDetails;
	private int[] lineupHome;
	private int[] lineupAway;
	private ArrayList<Wechsel> substitutionsHome;
	private ArrayList<Wechsel> substitutionsAway;
	private ArrayList<Tor> tore;
	private boolean repaint;
	private ArrayList<Integer> penaltiesHome;
	private ArrayList<Integer> penaltiesAway;
	
	private boolean isETpossible = false;
	
	private JButton go;

	private Rectangle RECGO = new Rectangle(600, 10, 90, 40);
	
	public SpielInformationen(Spieltag spieltag, Spiel spiel, Ergebnis previous) {
		super();
		
		this.spieltag = spieltag;
		this.spiel = spiel;
		this.tore = spiel.getTore();
		this.substitutionsHome = spiel.getSubstitutions(true);
		this.substitutionsAway = spiel.getSubstitutions(false);
		this.ergebnis = previous;
		this.isETpossible = spiel.getWettbewerb().isETPossible();
		
		initGUI();
		displayGivenValues();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		jLblsLineupHome = new JLabel[11];
		jLblsLineupAway = new JLabel[11];
		jLblsPenaltiesHome = new ArrayList<>();
		jLblsPenaltiesAway = new ArrayList<>();
		penaltiesHome = new ArrayList<>();
		penaltiesAway = new ArrayList<>();
		
		{
			jPnlSpielInformationen = new JPanel();
			getContentPane().add(jPnlSpielInformationen);
			jPnlSpielInformationen.setBounds(REC_PNLSPINFO);
			jPnlSpielInformationen.setLayout(null);
			jPnlSpielInformationen.setBackground(color);
		}
		{
			jLblWettbewerb = new JLabel();
			jPnlSpielInformationen.add(jLblWettbewerb);
			jLblWettbewerb.setBounds(REC_LBLWETTBW);
			jLblWettbewerb.setText(spiel.getDescription());
			jLblWettbewerb.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			jLblDatum = new JLabel();
			jPnlSpielInformationen.add(jLblDatum);
			jLblDatum.setBounds(REC_LBLDATUM);
			jLblDatum.setText(spiel.getDateAndTime());
			jLblDatum.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			jLblHomeTeamName = new JLabel();
			jPnlSpielInformationen.add(jLblHomeTeamName);
			jLblHomeTeamName.setBounds(REC_LBLHOMENAME);
			jLblHomeTeamName.setFont(fontTeamNames);
			jLblHomeTeamName.setText(spiel.getHomeTeam().getName());
			jLblHomeTeamName.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		{
			jLblResult = new JLabel();
			jPnlSpielInformationen.add(jLblResult);
			jLblResult.setBounds(REC_LBLRESULT);
			jLblResult.setFont(fontTeamNames);
			jLblResult.setText(ergebnis != null ? ergebnis.getResult() : "-:-");
			jLblResult.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			jLblZusatz = new JLabel();
			jPnlSpielInformationen.add(jLblZusatz);
			jLblZusatz.setBounds(REC_LBLZUSATZ);
			jLblZusatz.setText(ergebnis != null ? ergebnis.getMore() : "");
			jLblZusatz.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			jLblAwayTeamName = new JLabel();
			jPnlSpielInformationen.add(jLblAwayTeamName);
			jLblAwayTeamName.setBounds(REC_LBLAWAYNAME);
			jLblAwayTeamName.setFont(fontTeamNames);
			jLblAwayTeamName.setText(spiel.getAwayTeam().getName());
			jLblAwayTeamName.setHorizontalAlignment(SwingConstants.LEFT);
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
		for (int i = 0; i < 5; i++) {
			final int x = i;
			JLabel label = new JLabel();
			jPnlPenalties.add(label);
			label.setBounds(penaltyH[STARTX] + i * penaltyH[GAPX], penaltyH[STARTY], penaltyH[SIZEX], penaltyH[SIZEY]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
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
			
			label = new JLabel();
			jPnlPenalties.add(label);
			label.setBounds(penaltyA[STARTX] + i * penaltyA[GAPX], penaltyA[STARTY], penaltyA[SIZEX], penaltyA[SIZEY]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
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
		}
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
		for (int i = 0; i < 11; i++) {
			jLblsLineupHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsLineupHome[i]);
			jLblsLineupHome[i].setLocation(luLbls[STARTX], luLbls[STARTY] + i * (luLbls[SIZEY] + luLbls[GAPY]));
			jLblsLineupHome[i].setSize(luLbls[SIZEX], luLbls[SIZEY]);
			jLblsLineupHome[i].setHorizontalAlignment(SwingConstants.RIGHT);
			jLblsLineupHome[i].setVisible(false);
			
			jLblsLineupAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsLineupAway[i]);
			jLblsLineupAway[i].setLocation(luLbls[STARTX] + luLbls[GAPX], luLbls[STARTY] + i * (luLbls[SIZEY] + luLbls[GAPY]));
			jLblsLineupAway[i].setSize(luLbls[SIZEX], luLbls[SIZEY]);
			jLblsLineupAway[i].setHorizontalAlignment(SwingConstants.LEFT);
			jLblsLineupAway[i].setVisible(false);
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
			jBtnEingabeCompleted = new JButton();
			jPnlEingabe.add(jBtnEingabeCompleted);
			jBtnEingabeCompleted.setBounds(REC_BTNTOREINGCOMPL);
			jBtnEingabeCompleted.setText("fertig");
			jBtnEingabeCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (enteringSubstitution)	jBtnWechseleingabeCompletedActionPerformed();	
					else if (enteringGoal)		jBtnToreingabeCompletedActionPerformed();
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
			jChBPenalty = new JCheckBox();
			jPnlEingabe.add(jChBPenalty);
			jChBPenalty.setBounds(REC_CHBPENALTY);
			jChBPenalty.setText("Elfmeter");
			jChBPenalty.setVisible(false);
			jChBPenalty.setOpaque(true);
			jChBPenalty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBPenaltySelectionChanged();
				}
			});
		}
		{
			jChBOwnGoal = new JCheckBox();
			jPnlEingabe.add(jChBOwnGoal);
			jChBOwnGoal.setBounds(REC_CHBOWNGOAL);
			jChBOwnGoal.setText("Eigentor");
			jChBOwnGoal.setVisible(false);
			jChBOwnGoal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jChBOwnGoalSelectionChanged();
				}
			});
		}
		{
			buttonGroupDetails = new ButtonGroup();
			buttonGroupDetails.add(jChBPenalty);
			buttonGroupDetails.add(jChBOwnGoal);
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
		
		setSize(this.dim);
		setResizable(false);
	}
	
	private void displayGivenValues() {
		lineupHome = spiel.getLineupHome();
//		lineupHome = new int[] {1, 32, 24, 20, 21, 16, 18, 44, 26, 33, 7};
		lineupAway = spiel.getLineupAway();
//		lineupAway = new int[] {1, 8, 2, 5, 7, 44, 22, 16, 11, 9, 27};
		
		for (int i = 0; i < 11; i++) {
			if (lineupHome != null) {
				jLblsLineupHome[i].setText(spiel.getHomeTeam().getSpieler(lineupHome[i], spiel.getDate()).getPseudonymOrLN());
				jLblsLineupHome[i].setVisible(true);
			}
			if (lineupAway != null) {
				jLblsLineupAway[i].setText(spiel.getAwayTeam().getSpieler(lineupAway[i], spiel.getDate()).getPseudonymOrLN());
				jLblsLineupAway[i].setVisible(true);
			}
		}
		
		paintGoals();
		paintSubstitutions(true);
		paintSubstitutions(false);
		
		if (tore.size() > 0 || substitutionsHome.size() > 0 || substitutionsAway.size() > 0 || 
				inThePast(spiel.getDate(), spiel.getTime()))	startGame();
	}
	
	private void paintGoals() {
		if (jLblsGoals.size() > 0) {
			for (JLabel label : jLblsGoals) {
				label.setVisible(false);
			}
			jLblsGoals.clear();
		}
		for (Tor tor : tore) {
			displayGoal(tor);
		}
	}
	
	private void paintSubstitutions(boolean firstTeam) {
		if (firstTeam) {
			if (jLblsSubstitutionsHome.size() > 0) {
				for (JLabel label : jLblsSubstitutionsHome) {
					label.setVisible(false);
				}
				jLblsSubstitutionsHome.clear();
			}
			for (Wechsel wechsel : substitutionsHome) {
				displaySubstitution(wechsel);
			}
		} else {
			if (jLblsSubstitutionsAway.size() > 0) {
				for (JLabel label : jLblsSubstitutionsAway) {
					label.setVisible(false);
				}
				jLblsSubstitutionsAway.clear();
			}
			for (Wechsel wechsel : substitutionsAway) {
				displaySubstitution(wechsel);
			}
		}
	}
	
	private void displayGoal(Tor tor) {
		final int i = jLblsGoals.size();
		JLabel jLblNewGoal = new JLabel();
		jPnlSpielInformationen.add(jLblNewGoal);
		jLblNewGoal.setLocation(gLbls[STARTX] + (tor.isFirstTeam() ? 0 : gLbls[GAPX]), gLbls[STARTY] + i * (gLbls[SIZEY] + gLbls[GAPY]));
		jLblNewGoal.setSize(gLbls[SIZEX], gLbls[SIZEY]);
		jLblNewGoal.setHorizontalAlignment(tor.isFirstTeam() ? SwingConstants.LEFT : SwingConstants.RIGHT);
		jLblNewGoal.setText((tor.getScorer() != null ? tor.getScorer().getPseudonymOrLN() : "n/a") + " (" + tor.getMinute() + "')");
		jLblNewGoal.setOpaque(true);
		jLblNewGoal.setCursor(handCursor);
		jLblNewGoal.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeGoal(i);
			}
		});
		jLblsGoals.add(jLblNewGoal);
	}
	
	private void displaySubstitution(Wechsel wechsel) {
		final boolean firstTeam = wechsel.isFirstTeam();
		final int i = (firstTeam ? jLblsSubstitutionsHome : jLblsSubstitutionsAway).size();
		JLabel jLblNewSubOn = new JLabel();
		jPnlSpielInformationen.add(jLblNewSubOn);
		jLblNewSubOn.setLocation(subLbls[STARTX] + (firstTeam ? 0 : subLbls[GAPX]), subLbls[STARTY] + i * (subLbls[SIZEY] + subLbls[GAPY]));
		jLblNewSubOn.setSize(subLbls[SIZEX], subLbls[SIZEY]);
		jLblNewSubOn.setText(wechsel.getEingewechselterSpieler().getPseudonymOrLN() + " (" + wechsel.getMinute() + "')");
		jLblNewSubOn.setOpaque(true);
		jLblNewSubOn.setCursor(handCursor);
		jLblNewSubOn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeSubstitution(firstTeam, i / 2);
			}
		});
		jLblNewSubOn.setBackground(eingSpielerColor);
		(firstTeam ? jLblsSubstitutionsHome : jLblsSubstitutionsAway).add(jLblNewSubOn);
		
		JLabel jLblNewSubOff = new JLabel();
		jPnlSpielInformationen.add(jLblNewSubOff);
		jLblNewSubOff.setLocation(subLbls[STARTX] + (firstTeam ? 0 : subLbls[GAPX]), subLbls[STARTY] + (i + 1) * (subLbls[SIZEY] + subLbls[GAPY]));
		jLblNewSubOff.setSize(subLbls[SIZEX], subLbls[SIZEY]);
		jLblNewSubOff.setText(wechsel.getAusgewechselterSpieler().getPseudonymOrLN() + " (" + wechsel.getMinute() + "')");
		jLblNewSubOff.setOpaque(true);
		jLblNewSubOff.setCursor(handCursor);
		jLblNewSubOff.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				changeSubstitution(firstTeam, i / 2);
			}
		});
		jLblNewSubOff.setBackground(ausgSpielerColor);
		(firstTeam ? jLblsSubstitutionsHome : jLblsSubstitutionsAway).add(jLblNewSubOff);
	}
	
	private void changeGoal(int index) {
		Tor tor = tore.remove(index);
		this.repaint = true;
		this.enteringGoal = true;
		editingHomeTeam = tor.isFirstTeam();
		
		log("You want to change the goal of " + (tor.getScorer() == null ? "n/a" : tor.getScorer().getPseudonymOrLN()) + "(" + tor.getMinute() + ")");
		
		setLabelsVisible(false);
		
		jLblOben.setText("Torschuetze");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(true)));
		jLblUnten.setText("Vorbereiter");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(false)));
		
		
		jChBPenalty.setVisible(true);
		jChBOwnGoal.setVisible(true);
		if (tor.isPenalty()) {
			jChBPenalty.setSelected(true);
			goalDetails = 1;
		} else if (tor.isOwnGoal()) {
			jChBOwnGoal.setSelected(true);
			goalDetails = 2;
		}
		if (tor.getScorer() != null)		jCBOben.setSelectedItem(tor.getScorer().getPseudonymOrLN());
		if (tor.getAssistgeber() != null)	jCBUnten.setSelectedItem(tor.getAssistgeber().getPseudonymOrLN());
		jTFMinute.setText("" + tor.getMinute());
		
		if (editingHomeTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void changeSubstitution(boolean firstTeam, int index) {
		if (enteringGoal || enteringSubstitution)	return;
		
		Wechsel wechsel = (firstTeam ? substitutionsHome : substitutionsAway).remove(index);
		this.repaint = true;
		this.enteringSubstitution = true;
		
		log("You want to change the substitution " + wechsel.getAusgewechselterSpieler().getPseudonymOrLN() + "(" + wechsel.getMinute() + ". " + 
				wechsel.getEingewechselterSpieler().getPseudonymOrLN() + ")");
		
		setLabelsVisible(false);
		
		jLblOben.setText("ausgewechselt");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(true)));
		jLblUnten.setText("eingewechselt");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(false)));
		
		jCBOben.setSelectedItem(wechsel.getAusgewechselterSpieler().getPseudonymOrLN());
		jCBUnten.setSelectedItem(wechsel.getEingewechselterSpieler().getPseudonymOrLN());
		jTFMinute.setText("" + wechsel.getMinute());
		
		if (editingHomeTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
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
		jLblZusatz.setToolTipText(ergebnis.getTooltipext());
	}
	
	private void startPenaltyShootout() {
		jBtnLineupHome.setVisible(false);
		jBtnLineupAway.setVisible(false);
		jBtnSubstitutionHome.setVisible(false);
		jBtnSubstitutionAway.setVisible(false);
		jBtnGoalHome.setVisible(false);
		jBtnGoalAway.setVisible(false);
		jBtnPenaltyShootout.setVisible(false);
		
		jPnlPenalties.setVisible(true);
	}
	
	private void changePenalty(boolean firstTeam, int index) {
		ArrayList<JLabel> list = firstTeam ? jLblsPenaltiesHome : jLblsPenaltiesAway;
		
		// TODO save current state in arraylist, change from green to red, red to green, or, if being the last: from red to grey
		if (firstTeam) {
			list.get(index).setBackground(penaltiesInColor);
		} else {
			list.get(index).setBackground(penaltiesOutColor);
		}
	}
	
	private void finishPenaltyShootout() {
		jPnlPenalties.setVisible(false);
		
		jBtnLineupHome.setVisible(true);
		jBtnLineupAway.setVisible(true);
		jBtnSubstitutionHome.setVisible(true);
		jBtnSubstitutionAway.setVisible(true);
		jBtnGoalHome.setVisible(true);
		jBtnGoalAway.setVisible(true);
		jBtnPenaltyShootout.setVisible(true);
	}
	
	private void startGame() {
		if (ergebnis == null)	ergebnis = new Ergebnis("0:0");
		setErgebnis();
		
		jBtnStartGame.setVisible(false);
		jBtnSubstitutionHome.setVisible(true);
		jBtnGoalHome.setVisible(true);
		jBtnSubstitutionAway.setVisible(true);
		jBtnGoalAway.setVisible(true);
		if (isETpossible)	jBtnPenaltyShootout.setVisible(true);
	}
	
	private void setLabelsVisible(boolean value) {
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(value);
			}
			for (JLabel label : jLblsSubstitutionsHome) {
				label.setVisible(value);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(value);
			}
			for (JLabel label : jLblsSubstitutionsAway) {
				label.setVisible(value);
			}
		}
		for (JLabel label : jLblsGoals) {
			label.setVisible(value);
		}
	}
	
	private void enterNewSubstitution(boolean isHomeTeam) {
		if ((isHomeTeam && lineupHome == null) || (!isHomeTeam && lineupAway == null))	return;
		
		if (spiel.getSubstitutions(isHomeTeam).size() == 3) {
			message("You have already submitted all three possible substitutions for this team.");
			return;
		}
		
		this.enteringSubstitution = true;
		this.editingHomeTeam = isHomeTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		
		jLblOben.setText("ausgewechselt");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(true)));
		jCBOben.setSelectedIndex(jCBOben.getModel().getSize() / 2);
		jLblUnten.setText("eingewechselt");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersSub(false)));
		jCBUnten.setSelectedIndex(jCBUnten.getModel().getSize() / 2);
		
		if (editingHomeTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private String[] getEligiblePlayersSub(boolean subOff) {
		String[] eligiblePlayers;
		
		Mannschaft team = editingHomeTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
		ArrayList<Wechsel> substitutions = spiel.getSubstitutions(editingHomeTeam);
		int[] lineup = editingHomeTeam ? lineupHome : lineupAway;
		
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
		if (minute > 121) {
			message("Ein Spiel kann nicht laenger als 120 Minuten dauern.");
			return;
		} else if (!isETpossible && minute > 90) {
			message("In diesem Spiel kann es keine Verlaengerung geben.");
			return;
		}
		for (Wechsel wechsel : spiel.getSubstitutions(editingHomeTeam)) {
			if (wechsel.getMinute() > minute) {
				this.repaint = true;
			}
		}
		Spieler ausgSpieler = eligiblePlayersListUpper.get(jCBOben.getSelectedIndex());
		Spieler eingSpieler = eligiblePlayersListLower.get(jCBUnten.getSelectedIndex());
		
		Wechsel substitution = new Wechsel(spiel, editingHomeTeam, minute, ausgSpieler, eingSpieler);
		spiel.addSubstitution(substitution);
		if (repaint)	paintSubstitutions(editingHomeTeam);
		else			displaySubstitution(substitution);
		enteringSubstitution = false;
		
		jPnlEingabe.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
	}
	
	private void enterNewGoal(boolean isHomeTeam) {
		this.enteringGoal = true;
		this.editingHomeTeam = isHomeTeam;
		
		// hide lineup labels
		setLabelsVisible(false);
		
		jChBPenalty.setVisible(true);
		jChBOwnGoal.setVisible(true);
		jLblOben.setText("Torschuetze");
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(true)));
		jLblUnten.setText("Vorbereiter");
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(false)));
		
		if (editingHomeTeam)	jPnlEingabe.setLocation(LOC_PNLEINGABEHOME);
		else					jPnlEingabe.setLocation(LOC_PNLEINGABEAWAY);
		jPnlEingabe.setVisible(true);
		jTFMinute.requestFocus();
	}
	
	private void jChBPenaltySelectionChanged() {
		if (goalDetails == 1) {
			buttonGroupDetails.clearSelection();
			goalDetails = 0;
		} else {
			if (goalDetails == 2) {
				jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(true)));
				jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(false)));
			}
			goalDetails = 1;
		}
		log("Selection changed: penalty is " + (jChBPenalty.isSelected() ? "" : "not ") + "selected");
	}
	
	private void jChBOwnGoalSelectionChanged() {
		if (goalDetails == 2) {
			buttonGroupDetails.clearSelection();
			goalDetails = 0;
		} else {
			goalDetails = 2;
		}
		jCBOben.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(true)));
		jCBUnten.setModel(new DefaultComboBoxModel<>(getEligiblePlayersGoal(false)));
		log("Selection changed: own goal is " + (jChBOwnGoal.isSelected() ? "" : "not ") + "selected");
	}
	
	private String[] getEligiblePlayersGoal(boolean scorer) {
		boolean ownGoal = jChBOwnGoal.isSelected();
		String[] eligiblePlayers;
		if (scorer) {
			boolean firstTeam = editingHomeTeam ^ ownGoal;
			if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
				Mannschaft scoringTeam = firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
				int[] scoringLineup = firstTeam ? lineupHome : lineupAway;
				
				eligiblePlayersListUpper.clear();
				ArrayList<Wechsel> substitutions = spiel.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + 11 + substitutions.size()];
				for (int i = 0; i < 11; i++) {
					eligiblePlayersListUpper.add(scoringTeam.getSpieler(scoringLineup[i], spiel.getDate()));
					eligiblePlayers[1 + i] = eligiblePlayersListUpper.get(i).getPseudonymOrLN();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListUpper.add(substitutions.get(i).getEingewechselterSpieler());
					eligiblePlayers[12 + i] = eligiblePlayersListUpper.get(11 + i).getPseudonymOrLN();
				}
			} else {
				eligiblePlayers = new String[1];
			}
		} else {
			boolean firstTeam = editingHomeTeam;
			if ((firstTeam && lineupHome != null) || (!firstTeam && lineupAway != null)) {
				Mannschaft assistingTeam = firstTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
				int[] assistingLineup = firstTeam ? lineupHome : lineupAway;
				
				eligiblePlayersListLower.clear();
				ArrayList<Wechsel> substitutions = spiel.getSubstitutions(firstTeam);
				eligiblePlayers = new String[1 + 11 + substitutions.size()];
				for (int i = 0; i < 11; i++) {
					eligiblePlayersListLower.add(assistingTeam.getSpieler(assistingLineup[i], spiel.getDate()));
					eligiblePlayers[1 + i] = eligiblePlayersListLower.get(i).getPseudonymOrLN();
				}
				for (int i = 0; i < substitutions.size(); i++) {
					eligiblePlayersListLower.add(substitutions.get(i).getEingewechselterSpieler());
					eligiblePlayers[12 + i] = eligiblePlayersListLower.get(11 + i).getPseudonymOrLN();
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
		if (minute > 121) {
			message("Ein Spiel kann nicht laenger als 120 Minuten dauern.");
			return;
		} else if (!isETpossible && minute > 90) {
			message("In diesem Spiel kann es keine Verlaengerung geben.");
			return;
		}
		for (Tor tor : tore) {
			if (tor.getMinute() > minute ) {
				this.repaint = true;
			}
		}
		
		boolean penalty = jChBPenalty.isSelected();
		boolean ownGoal = jChBOwnGoal.isSelected();
		int index;
		Spieler scorer = null;
		if ((index = jCBOben.getSelectedIndex()) != 0) {
			scorer = eligiblePlayersListUpper.get(index - 1);
			for (Wechsel wechsel : spiel.getSubstitutions(editingHomeTeam)) {
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
			for (Wechsel wechsel : spiel.getSubstitutions(editingHomeTeam)) {
				if (wechsel.getAusgewechselterSpieler() == assistgeber && minute > wechsel.getMinute() || 
						wechsel.getEingewechselterSpieler() == assistgeber && minute < wechsel.getMinute()) {
					message("The player " + assistgeber.getPseudonymOrLN() + " was not on the pitch in the given minute.");
					return;
				}
			}
		}
		
		Tor tor = null;
		if (scorer == null)				tor = new Tor(spiel, editingHomeTeam, penalty, ownGoal, minute);
		else if (assistgeber == null)	tor = new Tor(spiel, editingHomeTeam, penalty, ownGoal, minute, scorer);
		else							tor = new Tor(spiel, editingHomeTeam, penalty, ownGoal, minute, scorer, assistgeber);
		spiel.addGoal(tor);
		ergebnis = spiel.getErgebnis();
		setErgebnis();
		if (repaint)	paintGoals();
		else			displayGoal(tor);
		enteringGoal = false;
		
		buttonGroupDetails.clearSelection();
		goalDetails = 0;
		jChBPenalty.setSelected(false);
		jChBPenalty.setVisible(false);
		jChBOwnGoal.setSelected(false);
		jChBOwnGoal.setVisible(false);
		jPnlEingabe.setVisible(false);
		jTFMinute.setText("");
		
		// show hidden lineup labels
		setLabelsVisible(true);
	}
	
	private void enterNewLineup(boolean isHomeTeam) {
		ArrayList<Spieler> kader;
		this.enteringLineup = true;
		this.editingHomeTeam = isHomeTeam;
		int[] lineup = editingHomeTeam ? lineupHome : lineupAway;
		if (editingHomeTeam)	kader = kaderHome = spiel.getHomeTeam().getEligiblePlayers(spiel.getDate());
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
			lineup = new int[11];
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
		
		if (editingHomeTeam)	jPnlLineupSelection.setLocation(LOC_PNLLINEUPHOMESEL);
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
		
		if (numberOfPlayers != 11) {
			message("You must choose eleven players.");
			return;
		}
		
		if (editingHomeTeam && lineupHome == null)	lineupHome = new int[11];
		else if (lineupAway == null)				lineupAway = new int[11];
		
		for (int i = 0; i < playerSelected.length; i++) {
			if (playerSelected[i]) {
				if (editingHomeTeam) {
					lineupHome[counter] = kaderHome.get(i).getSquadNumber();
					jLblsLineupHome[counter].setText(kaderHome.get(i).getPseudonymOrLN());
					jLblsLineupHome[counter++].setVisible(true);
				} else {
					lineupAway[counter] = kaderAway.get(i).getSquadNumber();
					jLblsLineupAway[counter].setText(kaderAway.get(i).getPseudonymOrLN());
					jLblsLineupAway[counter++].setVisible(true);
				}
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
	
	private void goActionPerformed() {
		Ergebnis ergebnis = this.ergebnis;
		
		this.setVisible(false);
		spieltag.moreOptions(ergebnis);
	}
}
