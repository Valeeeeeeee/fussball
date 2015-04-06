package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class SpielInformationen extends JFrame {
	private static final long serialVersionUID = 7503825008840407522L;
	
//	private final Dimension dim = new Dimension(250, 190);
	private final Dimension dim = new Dimension(700, 550);
	
	private Spiel spiel;
	
	private JPanel jPnlSpielInformationen;
	
	private JLabel jLblWettbewerb;
	private JLabel jLblDatum;
	
	private JLabel jLblHomeTeamName;
	private JLabel jLblResult;
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
	
	private JPanel jPnlLineupSelection;
	private JLabel[] jLblsLineupSelectionPlayers;
	private JButton jBtnLineupSelectionCompleted;
	private JLabel[] jLblsLineupHome;
	private JLabel[] jLblsLineupAway;
	
	private JPanel jPnlEingabe;
	private JLabel jLblMinute;
	private JTextField jTFMinute;
	private JButton jBtnEingabeCompleted;
	private JLabel jLblOben;
	private JComboBox<String> jCBOben;
	private JLabel jLblUnten;
	private JComboBox<String> jCBUnten;
	
	private Rectangle REC_PNLSPINFO = new Rectangle(0, 0, 700, 550);
	private Rectangle REC_LBLWETTBW = new Rectangle(150, 10, 400, 20);
	private Rectangle REC_LBLDATUM = new Rectangle(290, 35, 120, 20);
	private Rectangle REC_LBLHOMENAME = new Rectangle(40, 60, 265, 40);
	private Rectangle REC_LBLRESULT = new Rectangle(310, 60, 80, 40);
	private Rectangle REC_LBLAWAYNAME = new Rectangle(395, 60, 265, 40);
	private Rectangle REC_BTNAGTHOME = new Rectangle(50, 30, 50, 30); //300, 80, 50, 30);
	private Rectangle REC_BTNAGTAWAY = new Rectangle(600, 30, 50, 30); //350, 80, 50, 30);
	
	private Rectangle REC_BTNSTARTGAME = new Rectangle(300, 110, 100, 30);
	private Rectangle REC_BTNLINEUPHOME = new Rectangle(40, 110, 110, 30);
	private Rectangle REC_BTNLINEUPAWAY = new Rectangle(550, 110, 110, 30);
	private Rectangle REC_BTNSUBSTITUTIONHOME = new Rectangle(150, 110, 90, 30);
	private Rectangle REC_BTNSUBSTITUTIONAWAY = new Rectangle(460, 110, 90, 30);
	private Rectangle REC_BTNGOALHOME = new Rectangle(240, 110, 60, 30);
	private Rectangle REC_BTNGOALAWAY = new Rectangle(400, 110, 60, 30);
	
	// Toreingabe
	private Rectangle REC_PNLTOREINGABE = new Rectangle(150, 150, 200, 100);
	private Rectangle REC_LBLMINUTE = new Rectangle(60, 10, 70, 20);
	private Rectangle REC_TFMINUTE = new Rectangle(10, 10, 50, 20);
	private Rectangle REC_BTNTOREINGCOMPL = new Rectangle(130, 5, 60, 30);
	private Rectangle REC_LBLOBEN = new Rectangle(10, 40, 80, 20);
	private Rectangle REC_CBOBEN = new Rectangle(100, 37, 100, 26);
	private Rectangle REC_LBLUNTEN = new Rectangle(10, 70, 80, 20);
	private Rectangle REC_CBUNTEN = new Rectangle(100, 67, 100, 26);
	
	// Lineup selection
	private Point LOC_PNLLINEUPHOMESEL = new Point(30, 150);
	private Point LOC_PNLLINEUPAWAYSEL = new Point(390, 150);
	private Dimension DIM_PNLLINEUPSEL = new Dimension(280, 350);
	private Rectangle REC_BTNLUSCOMPL = new Rectangle(200, 310, 70, 30);
	
	
	private Color color = new Color(205, 255, 205);
	private Color lineupSelColor = new Color(175, 255, 175);
	private Color playerSelectedColor = new Color(255, 255, 0);
	private Font fontTeamNames = new Font("Verdana", Font.PLAIN, 24);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	private int[] boundsLSP = new int[] {5, 5, 5, 1, 120, 20};
	private int playersPerColumn = 15;
	
	private Spieltag spieltag;
	private Ergebnis ergebnis;
	private ArrayList<Spieler> kaderHome;
	private ArrayList<Spieler> kaderAway;
	private boolean[] playerSelected;
	private boolean editingHomeTeam;
	private boolean enteringLineup;
	private boolean enteringGoal;
	private boolean enteringSubstitution;
	private int[] lineupHome;
	private int[] lineupAway;
	
	private boolean isETpossible = false;
	private boolean amGruenenTisch = false;
	private boolean isFinishedAfterRT = false;
	private boolean isFinishedAfterET = false;
	
	private JButton go;
	private JButton afterET;
	private JButton afterPS;
	private JLabel[] descrLbls;
	private JTextField[][] goalsTFs;
	
	private Rectangle RECGO = new Rectangle(395, 380, 60, 30);
	private Rectangle RECAET = new Rectangle(395, 420, 50, 30);
	private Rectangle RECAPS = new Rectangle(395, 460, 50, 30);
	
	private int[] descr = new int[] {250, 383, 0, 16, 55, 24};
	private int[] goals = new int[] {315, 380, 10, 10, 30, 30};
	
	private static final int STARTX = 0;
	private static final int STARTY = 1;
	private static final int GAPX = 2;
	private static final int GAPY = 3;
	private static final int WIDTH = 4;
	private static final int HEIGHT = 5;
	
	public SpielInformationen(Spieltag spieltag, Spiel spiel, Ergebnis previous) {
		super();
		
		this.spieltag = spieltag;
		this.spiel = spiel;
		this.ergebnis = previous;
		this.isETpossible = spiel.getWettbewerb().isETPossible();
		
		initGUI();
		displayGivenValues();
		setErgebnis(ergebnis);
	}
	
	public SpielInformationen(Spiel spiel) {
		super();
		
		this.spiel = spiel;
		
		initGUI();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		jLblsLineupHome = new JLabel[11];
		jLblsLineupAway = new JLabel[11];
		
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
			jLblWettbewerb.setOpaque(true);
		}
		{
			jLblDatum = new JLabel();
			jPnlSpielInformationen.add(jLblDatum);
			jLblDatum.setBounds(REC_LBLDATUM);
			jLblDatum.setText(spiel.getDateAndTime());
			jLblDatum.setHorizontalAlignment(SwingConstants.CENTER);
			jLblDatum.setOpaque(true);
		}
		{
			jLblHomeTeamName = new JLabel();
			jPnlSpielInformationen.add(jLblHomeTeamName);
			jLblHomeTeamName.setBounds(REC_LBLHOMENAME);
			jLblHomeTeamName.setFont(fontTeamNames);
			jLblHomeTeamName.setText(spiel.getHomeTeam().getName());
			jLblHomeTeamName.setHorizontalAlignment(SwingConstants.RIGHT);
			jLblHomeTeamName.setOpaque(true);
		}
		{
			jLblResult = new JLabel();
			jPnlSpielInformationen.add(jLblResult);
			jLblResult.setBounds(REC_LBLRESULT);
			jLblResult.setFont(fontTeamNames);
			jLblResult.setText(ergebnis != null ? ergebnis.toString() : "-:-");
			jLblResult.setHorizontalAlignment(SwingConstants.CENTER);
			jLblResult.setOpaque(true);
		}
		{
			jLblAwayTeamName = new JLabel();
			jPnlSpielInformationen.add(jLblAwayTeamName);
			jLblAwayTeamName.setBounds(REC_LBLAWAYNAME);
			jLblAwayTeamName.setFont(fontTeamNames);
			jLblAwayTeamName.setText(spiel.getAwayTeam().getName());
			jLblAwayTeamName.setHorizontalAlignment(SwingConstants.LEFT);
			jLblAwayTeamName.setOpaque(true);
		}
		
		
		{
			jBtnStartGame = new JButton();
			jPnlSpielInformationen.add(jBtnStartGame);
			jBtnStartGame.setBounds(REC_BTNSTARTGAME);
			jBtnStartGame.setText("Anpfiff");
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
			jBtnLineupHome.setVisible(false);
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
			jBtnLineupAway.setVisible(false);
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
			jBtnAGTHome.setText("+");
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
			jBtnAGTAway.setText("+");
			jBtnAGTAway.setToolTipText("Sieg am gruenen Tisch");
			jBtnAGTAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAmGruenenTisch(false);
				}
			});
		}
		
		for (int i = 0; i < 11; i++) {
			jLblsLineupHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsLineupHome[i]);
			jLblsLineupHome[i].setBounds(130, 160 + i * (20 + 5), 100, 20);
			jLblsLineupHome[i].setOpaque(true);
			jLblsLineupHome[i].setVisible(false);
			
			jLblsLineupAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsLineupAway[i]);
			jLblsLineupAway[i].setBounds(470, 160 + i * (20 + 5), 100, 20);
			jLblsLineupAway[i].setOpaque(true);
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
			jBtnLineupSelectionCompleted.setText("Fertig");
			jBtnLineupSelectionCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnLineupSelectionCompletedActionPerformed();
				}
			});
		}
		
		{
			jPnlEingabe = new JPanel();
			jPnlSpielInformationen.add(jPnlEingabe);
			jPnlEingabe.setBounds(REC_PNLTOREINGABE);
			jPnlEingabe.setLayout(null);
			jPnlEingabe.setBackground(lineupSelColor);
			jPnlEingabe.setVisible(false);
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
			jLblOben = new JLabel();
			jPnlEingabe.add(jLblOben);
			jLblOben.setBounds(REC_LBLOBEN);
		}
		{
			jCBOben = new JComboBox<>();
			jPnlEingabe.add(jCBOben);
	        jCBOben.setBounds(REC_CBOBEN);
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
		}
		
		
		
		String[] descriptions = new String[] {"45 min", "90 min", "120 min", "n.E."};
		goalsTFs = new JTextField[4][2];
		descrLbls = new JLabel[4];
		
		for (int i = 0; i < goalsTFs.length; i++) {
			for (int j = 0; j < goalsTFs[i].length; j++) {
				final int x = i, y = j;
				goalsTFs[i][j] = new JTextField();
				jPnlSpielInformationen.add(goalsTFs[i][j]);
				goalsTFs[i][j].setBounds(goals[STARTX] + j * (goals[WIDTH] + goals[GAPX]), goals[STARTY] + i * (goals[HEIGHT] + goals[GAPY]), goals[WIDTH], goals[HEIGHT]);
				goalsTFs[i][j].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if ((goalsTFs[x][y].getText().length() >= 2 && !goalsTFs[x][y].getText().equals("-1")) || arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						}
					}
				});
				goalsTFs[i][j].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent e) {
						goalsTFs[x][y].selectAll();
					}
				});
			}
		}
		goalsTFs[0][0].setEnabled(false);
		goalsTFs[0][1].setEnabled(false);
		goalsTFs[1][0].requestFocus();
		
		for (int i = 0; i < descrLbls.length; i++) {
			descrLbls[i] = new JLabel();
			jPnlSpielInformationen.add(descrLbls[i]);
			descrLbls[i].setBounds(descr[STARTX], descr[STARTY] + i * (descr[HEIGHT] + descr[GAPY]), descr[WIDTH], descr[HEIGHT]);
			descrLbls[i].setText(descriptions[i]);
			descrLbls[i].setHorizontalAlignment(SwingConstants.RIGHT);
		}
		
		{
			afterET = new JButton();
			jPnlSpielInformationen.add(afterET);
			afterET.setBounds(RECAET);
			afterET.setText("n.V.");
			afterET.setFocusable(false);
			if (!isETpossible)	afterET.setEnabled(false);
			afterET.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					afterETActionPerformed();
				}
			});
		}
		{
			afterPS = new JButton();
			jPnlSpielInformationen.add(afterPS);
			afterPS.setBounds(RECAPS);
			afterPS.setText("n.E.");
			afterPS.setFocusable(false);
			if (!isETpossible)	afterPS.setEnabled(false);
			afterPS.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					afterPSActionPerformed();
				}
			});
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
		lineupAway = spiel.getLineupAway();
		
		for (int i = 0; i < 11; i++) {
			if (lineupHome != null) {
				jLblsLineupHome[i].setText(spiel.getHomeTeam().getSpieler(lineupHome[i], spiel.getDate()).getPseudonym());
				jLblsLineupHome[i].setVisible(true);
			}
			if (lineupAway != null) {
				jLblsLineupAway[i].setText(spiel.getAwayTeam().getSpieler(lineupAway[i], spiel.getDate()).getPseudonym());
				jLblsLineupAway[i].setVisible(true);
			}
		}
		
		// TODO tore
		
	}
	
	private void setAmGruenenTisch(boolean isHomeTeam) {
		this.amGruenenTisch = true;
		goalsTFs[1][0].setText(isHomeTeam ? "3" : "0");
		goalsTFs[1][1].setText(isHomeTeam ? "0" : "3");
	}
	
	private void startGame() {
		ergebnis = new Ergebnis("0:0");
		spiel.setErgebnis(ergebnis);
		jLblResult.setText(ergebnis.toString());
		
		jBtnStartGame.setVisible(false);
		jBtnAGTHome.setVisible(false);
		jBtnAGTAway.setVisible(false);
		jBtnLineupHome.setVisible(true);
		jBtnSubstitutionHome.setVisible(true);
		jBtnGoalHome.setVisible(true);
		jBtnLineupAway.setVisible(true);
		jBtnSubstitutionAway.setVisible(true);
		jBtnGoalAway.setVisible(true);
	}
	
	private void enterNewSubstitution(boolean isHomeTeam) {
		if ((isHomeTeam && lineupHome == null) || (!isHomeTeam && lineupAway == null))	return;
		this.enteringSubstitution = true;
		this.editingHomeTeam = isHomeTeam;
		
		// hide lineup labels
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(false);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(false);
			}
		}
		
		Mannschaft team = editingHomeTeam ? spiel.getHomeTeam() : spiel.getAwayTeam();
		int[] lineup = editingHomeTeam ? lineupHome : lineupAway;
		String[] lineupString = new String[11];
		for (int i = 0; i < lineup.length; i++) {
			lineupString[i] = team.getSpieler(lineup[i], spiel.getDate()).getPseudonym();
		}
		jLblOben.setText("ausgewechselt");
		jCBOben.setModel(new DefaultComboBoxModel<>(lineupString));
		jLblUnten.setText("eingewechselt");
		jCBUnten.setModel(new DefaultComboBoxModel<>(lineupString));
		
		jPnlEingabe.setVisible(true);
	}
	
	private void jBtnWechseleingabeCompletedActionPerformed() {
		
		
		// show hidden lineup labels
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(true);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(true);
			}
		}
	}
	
	private void enterNewGoal(boolean isHomeTeam) {
		this.enteringGoal = true;
		this.editingHomeTeam = isHomeTeam;
		
		// hide lineup labels
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(false);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(false);
			}
		}
		
		boolean hasLineup = false;
		if ((isHomeTeam && lineupHome != null) || (!isHomeTeam && lineupAway != null))	hasLineup = true;
		if (hasLineup) {
			String[] lineup = new String[11];
			for (int i = 0; i < lineup.length; i++) {
				if (editingHomeTeam)	lineup[i] = spiel.getHomeTeam().getSpieler(lineupHome[i], spiel.getDate()).getPseudonym();
				else					lineup[i] = spiel.getAwayTeam().getSpieler(lineupAway[i], spiel.getDate()).getPseudonym();
			}
			jLblOben.setText("Torschuetze");
			jCBOben.setModel(new DefaultComboBoxModel<>(lineup));
			jLblUnten.setText("Vorbereiter");
			jCBUnten.setModel(new DefaultComboBoxModel<>(lineup));
		}
		
		jPnlEingabe.setVisible(true);
	}
	
	private void jBtnToreingabeCompletedActionPerformed() {
		if (!enteringGoal)	return;
		
		jPnlEingabe.setVisible(false);
		
		int minute = Integer.parseInt(jTFMinute.getText());
		Spieler scorer = null, assistgeber = null;
		if (editingHomeTeam) {
			if (lineupHome != null) {
				scorer = spiel.getHomeTeam().getSpieler(lineupHome[jCBOben.getSelectedIndex()], spiel.getDate());
				assistgeber = spiel.getHomeTeam().getSpieler(lineupHome[jCBUnten.getSelectedIndex()], spiel.getDate());
			}
		} else {
			if (lineupAway != null) {
				scorer = spiel.getAwayTeam().getSpieler(lineupAway[jCBOben.getSelectedIndex()], spiel.getDate());
				assistgeber = spiel.getAwayTeam().getSpieler(lineupAway[jCBUnten.getSelectedIndex()], spiel.getDate());
			}
		}
		
		// show hidden lineup labels
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(true);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(true);
			}
		}
		
		Tor tor = new Tor(spiel, editingHomeTeam, minute, scorer, assistgeber);
		spiel.addGoal(tor);
		enteringGoal = false;
		ergebnis = spiel.getErgebnis();
		jLblResult.setText(ergebnis.toString());
	}
	
	private void enterNewLineup(boolean isHomeTeam) {
		ArrayList<Spieler> kader;
		this.enteringLineup = true;
		this.editingHomeTeam = isHomeTeam;
		if (editingHomeTeam)	kader = kaderHome = spiel.getHomeTeam().getEligiblePlayers(spiel.getDate());
		else					kader = kaderAway = spiel.getAwayTeam().getEligiblePlayers(spiel.getDate());
		
		// hide lineup labels
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(false);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(false);
			}
		}
		
		playerSelected = new boolean[kader.size()];
		
		// create lineup selection labels
		jLblsLineupSelectionPlayers = new JLabel[kader.size()];
		for (int i = 0; i < jLblsLineupSelectionPlayers.length; i++) {
			final int x = i;
			jLblsLineupSelectionPlayers[i] = new JLabel();
			jPnlLineupSelection.add(jLblsLineupSelectionPlayers[i]);
			jLblsLineupSelectionPlayers[i].setSize(boundsLSP[WIDTH], boundsLSP[HEIGHT]);
			jLblsLineupSelectionPlayers[i].setLocation(boundsLSP[STARTX] + (i / playersPerColumn) * (boundsLSP[WIDTH] + boundsLSP[GAPX]), 
														boundsLSP[STARTY] + (i % playersPerColumn) * (boundsLSP[HEIGHT] + boundsLSP[GAPY]));
			jLblsLineupSelectionPlayers[i].setText(kader.get(i).getSquadNumber() + " " + kader.get(i).getPseudonym());
			jLblsLineupSelectionPlayers[i].setBackground(playerSelectedColor);
			jLblsLineupSelectionPlayers[i].setCursor(handCursor);
			jLblsLineupSelectionPlayers[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					playerSelected(x);
				}
			});
		}
		
		if (editingHomeTeam) {
			jPnlLineupSelection.setLocation(LOC_PNLLINEUPHOMESEL);
			if (lineupHome == null) {
				lineupHome = new int[11];
			} else {
				// colorise previously selected players
				for (int i = 0; i < lineupHome.length; i++) {
					for (int j = 0; j < kader.size(); j++) {
						if (lineupHome[i] == kader.get(j).getSquadNumber()) {
							playerSelected(j);
							break;
						}
					}
				}
			}
		} else {
			jPnlLineupSelection.setLocation(LOC_PNLLINEUPAWAYSEL);
			if (lineupAway == null) {
				lineupAway = new int[11];
			} else {
				// colorise previously selected players
				for (int i = 0; i < lineupAway.length; i++) {
					for (int j = 0; j < kader.size(); j++) {
						if (lineupAway[i] == kader.get(j).getSquadNumber()) {
							playerSelected(j);
							break;
						}
					}
				}
			}
		}
		jPnlLineupSelection.setVisible(true);
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
		
		for (int i = 0; i < playerSelected.length; i++) {
			if (playerSelected[i]) {
				if (editingHomeTeam) {
					lineupHome[counter] = kaderHome.get(i).getSquadNumber();
					jLblsLineupHome[counter].setText(kaderHome.get(i).getPseudonym());
					jLblsLineupHome[counter++].setVisible(true);
				} else {
					lineupAway[counter] = kaderAway.get(i).getSquadNumber();
					jLblsLineupAway[counter].setText(kaderAway.get(i).getPseudonym());
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
		if (editingHomeTeam) {
			for (JLabel label : jLblsLineupHome) {
				label.setVisible(true);
			}
		} else {
			for (JLabel label : jLblsLineupAway) {
				label.setVisible(true);
			}
		}
		
		jPnlLineupSelection.setVisible(false);
		enteringLineup = false;
	}
	
	private void afterETActionPerformed() {
		if (isFinishedAfterRT || !isFinishedAfterET)	showTextFields(true, false);
		else											showTextFields(false, false);
	}
	
	private void afterPSActionPerformed() {
		if (isFinishedAfterRT || isFinishedAfterET)	showTextFields(true, true);
		else										showTextFields(true, false);
	}
	
	private void showTextFields(boolean extraTime, boolean penalties) {
		if (!extraTime) {
			isFinishedAfterRT = true;
			isFinishedAfterET = false;
			goalsTFs[2][0].setVisible(false);
			goalsTFs[2][1].setVisible(false);
			goalsTFs[3][0].setVisible(false);
			goalsTFs[3][1].setVisible(false);
			descrLbls[2].setVisible(false);
			descrLbls[3].setVisible(false);
		} else if (!penalties) {
			isFinishedAfterRT = false;
			isFinishedAfterET = true;
			goalsTFs[2][0].setVisible(true);
			goalsTFs[2][1].setVisible(true);
			goalsTFs[3][0].setVisible(false);
			goalsTFs[3][1].setVisible(false);
			descrLbls[2].setVisible(true);
			descrLbls[3].setVisible(false);
		} else {
			isFinishedAfterRT = false;
			isFinishedAfterET = false;
			goalsTFs[2][0].setVisible(true);
			goalsTFs[2][1].setVisible(true);
			goalsTFs[3][0].setVisible(true);
			goalsTFs[3][1].setVisible(true);
			descrLbls[2].setVisible(true);
			descrLbls[3].setVisible(true);
		}
	}
	
	private void setErgebnis(Ergebnis ergebnis) {
		for (int i = 0; i < 4; i++) {
			goalsTFs[i][0].setText("-1");
			goalsTFs[i][1].setText("-1");
		}
		if (ergebnis != null) {
			if (ergebnis.toString().indexOf("n") == -1)	{
				isFinishedAfterRT = true;
				showTextFields(false, false);
			} else if (ergebnis.toString().indexOf("nE") == -1)	{
				isFinishedAfterET = true;
				showTextFields(true, false);
			}
			for (int i = 0; i < 4; i++) {
				goalsTFs[i][0].setText("" + ergebnis.home(i));
				goalsTFs[i][1].setText("" + ergebnis.away(i));
			}
		} else {
			showTextFields(false, false);
		}
	}
	
	private void goActionPerformed() {
		Ergebnis ergebnis = this.ergebnis;
		
		if (this.ergebnis == null) {
			String resRT = (goalsTFs[1][0].getText().length() > 0 ? goalsTFs[1][0].getText() : "-1") + ":"
					+ (goalsTFs[1][1].getText().length() > 0 ? goalsTFs[1][1].getText() : "-1");
			String resET = (goalsTFs[2][0].getText().length() > 0 ? goalsTFs[2][0].getText() : "-1") + ":"
					+ (goalsTFs[2][1].getText().length() > 0 ? goalsTFs[2][1].getText() : "-1");
			String resPS = (goalsTFs[3][0].getText().length() > 0 ? goalsTFs[3][0].getText() : "-1") + ":"
					+ (goalsTFs[3][1].getText().length() > 0 ? goalsTFs[3][1].getText() : "-1");
			
			if (amGruenenTisch) {
				if(resRT.equals("3:0") || resRT.equals("0:3")) {
					ergebnis = new Ergebnis(resRT + " agT");
				}
			} else if (isFinishedAfterRT) {
				if (resRT.indexOf("-1") == -1) {
					ergebnis = new Ergebnis(resRT);
				}
			} else if (isFinishedAfterET) {
				if (resRT.indexOf("-1") == -1 && resET.indexOf("-1") == -1) {
					ergebnis = new Ergebnis(resET + "nV (" + resRT + ")");
				}
			} else {
				if (resRT.indexOf("-1") == -1 && resET.indexOf("-1") == -1 && resPS.indexOf("-1") == -1) {
					ergebnis = new Ergebnis(resPS + "nE (" + resET + "," + resRT + ")");
				}
			}
		}
		
		log("I returned " + ergebnis);
		
		this.setVisible(false);
		spieltag.moreOptions(ergebnis);
	}
}
