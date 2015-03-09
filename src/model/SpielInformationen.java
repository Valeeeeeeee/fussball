package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static util.Utilities.*;

public class SpielInformationen extends JFrame {
	private static final long serialVersionUID = 7503825008840407522L;
	
//	private final Dimension dim = new Dimension(250, 190);
	private final Dimension dim = new Dimension(700, 500);
	
	private Spiel spiel;
	
	private JPanel jPnlSpielInformationen;
	
	private JLabel jLblHomeTeamName;
	private JLabel jLblResult;
	private JLabel jLblAwayTeamName;
	
	private JButton jBtnLineupHome;
	private JButton jBtnLineupAway;
	private JButton jBtnGoalHome;
	private JButton jBtnGoalAway;
	
	private JPanel jPnlLineupSelection;
	private JLabel[] jLblsLineupSelectionPlayers;
	private JButton jBtnLineupSelectionCompleted;
	private JLabel[] jLblsLineupHome;
	private JLabel[] jLblsLineupAway;
	
	private JPanel jPnlTorEingabe;
	private JLabel jLblMinute;
	private JTextField jTFMinute;
	private JLabel jLblScorer;
	private JLabel jLblAssistgeber;
	
	private Rectangle REC_PNLSPINFO = new Rectangle(0, 0, 700, 500);
	private Rectangle REC_LBLHOMENAME = new Rectangle(40, 30, 265, 40);
	private Rectangle REC_LBLRESULT = new Rectangle(310, 30, 80, 40);
	private Rectangle REC_LBLAWAYNAME = new Rectangle(395, 30, 265, 40);
	
	private Rectangle REC_BTNLINEUPHOME = new Rectangle(40, 80, 190, 30);
	private Rectangle REC_BTNLINEUPAWAY = new Rectangle(470, 80, 190, 30);
	private Rectangle REC_BTNGOALHOME = new Rectangle(230, 80, 70, 30);
	private Rectangle REC_BTNGOALAWAY = new Rectangle(400, 80, 70, 30);
	
	private Rectangle REC_PNLTOREINGABE = new Rectangle(150, 120, 200, 100);
	private Rectangle REC_LBLMINUTE = new Rectangle(70, 10, 70, 20);
	private Rectangle REC_TFMINUTE = new Rectangle(10, 10, 50, 20);
	private Rectangle REC_LBLSCORER = new Rectangle(10, 40, 100, 20);
	private Rectangle REC_LBLASSIST = new Rectangle(10, 70, 100, 20);
	
	private Point LOC_PNLLINEUPHOMESEL = new Point(30, 120);
	private Point LOC_PNLLINEUPAWAYSEL = new Point(390, 120);
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
	private Spieler[] kaderHome;
	private Spieler[] kaderAway;
	private boolean[] playerSelected;
	private boolean enteringHomeTeamLineup;
	private boolean enteringHomeTeamGoal;
	private int[] lineupHome;
	private int[] lineupAway;
	
	private boolean isETpossible = false;
	private boolean isFinishedAfterRT = false;
	private boolean isFinishedAfterET = false;
	
	private JButton go;
	private JButton afterET;
	private JButton afterPS;
	private JLabel[] descrLbls;
	private JTextField[][] goalsTFs;
	
	private Rectangle RECGO = new Rectangle(395, 330, 60, 30);
	private Rectangle RECAET = new Rectangle(395, 370, 50, 30);
	private Rectangle RECAPS = new Rectangle(395, 410, 50, 30);
	
	private int[] descr = new int[] {250, 333, 0, 16, 55, 24};
	private int[] goals = new int[] {315, 330, 10, 10, 30, 30};
	
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
			jBtnLineupHome = new JButton();
			jPnlSpielInformationen.add(jBtnLineupHome);
			jBtnLineupHome.setBounds(REC_BTNLINEUPHOME);
			jBtnLineupHome.setText("Aufstellung eingeben");
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
			jBtnLineupAway.setText("Aufstellung eingeben");
			jBtnLineupAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewLineup(false);
				}
			});
		}
		{
			jBtnGoalHome = new JButton();
			jPnlSpielInformationen.add(jBtnGoalHome);
			jBtnGoalHome.setBounds(REC_BTNGOALHOME);
			jBtnGoalHome.setText("Tor");
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
			jBtnGoalAway.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enterNewGoal(false);
				}
			});
		}
		
		for (int i = 0; i < 11; i++) {
			jLblsLineupHome[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsLineupHome[i]);
			jLblsLineupHome[i].setBounds(130, 130 + i * (20 + 5), 100, 20);
			jLblsLineupHome[i].setOpaque(true);
			jLblsLineupHome[i].setVisible(false);
			
			jLblsLineupAway[i] = new JLabel();
			jPnlSpielInformationen.add(jLblsLineupAway[i]);
			jLblsLineupAway[i].setBounds(470, 130 + i * (20 + 5), 100, 20);
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
			jPnlTorEingabe = new JPanel();
			jPnlSpielInformationen.add(jPnlTorEingabe);
			jPnlTorEingabe.setBounds(REC_PNLTOREINGABE);
			jPnlTorEingabe.setLayout(null);
			jPnlTorEingabe.setBackground(lineupSelColor);
			jPnlTorEingabe.setVisible(false);
		}
		{
			jLblMinute = new JLabel();
			jPnlTorEingabe.add(jLblMinute);
			jLblMinute.setBounds(REC_LBLMINUTE);
			jLblMinute.setText(". Minute");
		}
		{
			jTFMinute = new JTextField();
			jPnlTorEingabe.add(jTFMinute);
			jTFMinute.setBounds(REC_TFMINUTE);
			jTFMinute.setText("");
		}
		{
			jLblScorer = new JLabel();
			jPnlTorEingabe.add(jLblScorer);
			jLblScorer.setBounds(REC_LBLSCORER);
			jLblScorer.setText("Torschuetze");
		}
		{
			jLblAssistgeber = new JLabel();
			jPnlTorEingabe.add(jLblAssistgeber);
			jLblAssistgeber.setBounds(REC_LBLASSIST);
			jLblAssistgeber.setText("Vorbereiter");
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
	
	private void enterNewGoal(boolean isHomeTeam) {
		boolean hasLineup = false;
		if ((isHomeTeam && lineupHome == null) || (!isHomeTeam && lineupAway == null))	hasLineup = true;
		String[] lineup = new String[11];
		this.enteringHomeTeamGoal = isHomeTeam;
		if (!hasLineup) {
			for (int i = 0; i < lineup.length; i++) {
				if (enteringHomeTeamGoal)	lineup[i] = spiel.getHomeTeam().getSpieler(lineupHome[i]).getLastName();
				else						lineup[i] = spiel.getAwayTeam().getSpieler(lineupAway[i]).getLastName();
			}
		}
		
		jPnlTorEingabe.setVisible(true);
	}
	
	private void enterNewLineup(boolean isHomeTeam) {
		Spieler[] kader;
		this.enteringHomeTeamLineup = isHomeTeam;
		if (enteringHomeTeamLineup)	kader = kaderHome = spiel.getHomeTeam().getKader();
		else						kader = kaderAway = spiel.getAwayTeam().getKader();
		
		playerSelected = new boolean[kader.length];
		
		// create labels
		jLblsLineupSelectionPlayers = new JLabel[kader.length];
		for (int i = 0; i < jLblsLineupSelectionPlayers.length; i++) {
			final int x = i;
			jLblsLineupSelectionPlayers[i] = new JLabel();
			jPnlLineupSelection.add(jLblsLineupSelectionPlayers[i]);
			jLblsLineupSelectionPlayers[i].setSize(boundsLSP[WIDTH], boundsLSP[HEIGHT]);
			jLblsLineupSelectionPlayers[i].setLocation(boundsLSP[STARTX] + (i / playersPerColumn) * (boundsLSP[WIDTH] + boundsLSP[GAPX]), 
														boundsLSP[STARTY] + (i % playersPerColumn) * (boundsLSP[HEIGHT] + boundsLSP[GAPY]));
			jLblsLineupSelectionPlayers[i].setText(kader[i].getSquadNumber() + " " + kader[i].getLastName());
			jLblsLineupSelectionPlayers[i].setBackground(playerSelectedColor);
			jLblsLineupSelectionPlayers[i].setCursor(handCursor);
			jLblsLineupSelectionPlayers[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					playerSelected(x);
				}
			});
		}
		
		if (enteringHomeTeamLineup) {
			jPnlLineupSelection.setLocation(LOC_PNLLINEUPHOMESEL);
			if (lineupHome == null) {
				lineupHome = new int[11];
			} else {
				// colorise previously selected players
				for (int i = 0; i < lineupHome.length; i++) {
					for (int j = 0; j < kader.length; j++) {
						if (lineupHome[i] == kader[j].getSquadNumber()) {
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
					for (int j = 0; j < kader.length; j++) {
						if (lineupAway[i] == kader[j].getSquadNumber()) {
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
				if (enteringHomeTeamLineup) {
					lineupHome[counter] = kaderHome[i].getSquadNumber();
					jLblsLineupHome[counter].setText(kaderHome[i].getLastName());
					jLblsLineupHome[counter++].setVisible(true);
				} else {
					lineupAway[counter] = kaderAway[i].getSquadNumber();
					jLblsLineupAway[counter].setText(kaderAway[i].getLastName());
					jLblsLineupAway[counter++].setVisible(true);
				}
			}
		}
		
		spiel.setLineupHome(lineupHome);
		spiel.setLineupAway(lineupAway);
		spiel.toString();
		
		if (jLblsLineupSelectionPlayers != null) {
			for (JLabel label : jLblsLineupSelectionPlayers) {
				label.setVisible(false);
			}
		}
		jPnlLineupSelection.setVisible(false);
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
		String resRT = goalsTFs[1][0].getText() + ":" + goalsTFs[1][1].getText();
		String resET = goalsTFs[2][0].getText() + ":" + goalsTFs[2][1].getText();
		String resPS = goalsTFs[3][0].getText() + ":" + goalsTFs[3][1].getText();
		
		Ergebnis ergebnis = null;
		
		if (isFinishedAfterRT) {
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
		
		log("I returned " + ergebnis);
		
		this.setVisible(false);
		
		spieltag.moreOptions(ergebnis);
	}
	
}




