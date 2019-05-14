package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class Spieltag extends JPanel {
	private static final long serialVersionUID = 533273470193095401L;

	private static final int WIDTH_BORDER = 2;
	
	private static final String TEAM_NOT_SET = "n. a.";
	private static final String GOAL_NOT_SET = "-1";
	
	private Color colorRand = new Color(0, 192, 0);
	private Color colorSelection = new Color(255, 255, 255);
	private Color colorEditing = new Color(255, 255, 0);
	private Color colorEdited = new Color(64, 255, 64);
	private Color colorMatches = new Color(224, 255, 224);
	private Color colorDatum = new Color(255, 191, 31);
	private Font fontRR = new Font("Dialog", 1, 18);
	
	private JComboBox<String> jCBMatchdays;
	private JScrollPane jSPMatches;
	private JPanel jPnlMatches;
	private JLabel[] jLblsTeams;
	private JTextField[] jTFsGoals;
	private JLabel[] jLblsAdditionalInfos;
	private JButton jBtnEdit;
	private JButton jBtnDone;
	private JButton jBtnPrevious;
	private JButton jBtnNext;
	private JButton[] jBtnsMatchInfos;
	private JButton jBtnResetMatchday;
	private JButton jBtnSecondLeg;
	private JButton jBtnEnterRueckrunde;
	private JButton jBtnDefaultKickoff;
	private JLabel[] jLblsDates;
	private JLabel[] jLblsGroups;
	
	private JPanel jPnlEnterRueckrunde;
	private JLabel jLblRRDescription;
	private JLabel jLblHinrunde;
	private JLabel[] jLblsHinrunde;
	private JLabel jLblRueckrunde;
	private JLabel[] jLblsRueckrunde;
	private JButton jBtnEnterRueckrundeCancel;
	private JButton jBtnEnterRueckrundeCompleted;

	private int[][] array;
	private Ergebnis[] results;
	private int editedDate = -1;
	private int editedLabel = -1;
	private int editedGroupID = -1;
	private int editedMatchday = -1;
	private int currentMatchday = -1;
	private boolean editingMatches;
	private ArrayList<SpielInformationen> openedMatchInfos = new ArrayList<>();
	
	private ArrayList<Integer> matchdaysHinrunde;
	private ArrayList<Integer> matchdaysRueckrunde;
	
	private int[] rrLabels = new int[] {20, 80, 50, 70, 40, 30};
	
	private int scrollBarWidth;
	private int matchesWidth;
	private int matchesHeight;
	
	private int[] btnsSelection;
	private int[] lblsDates;
	private int[] lblsGroup;
	private int[] lblsTeams;
	private int[] tfsGoals;
	private int[] lblsAddInfo;
	private int[] btnsMoreOpt;

	private Rectangle REC_PREV = new Rectangle(200, 10, 50, 30);
	private Rectangle REC_COMBO = new Rectangle(255, 10, 200, 30);
	private Rectangle REC_NEXT = new Rectangle(460, 10, 50, 30);
	private Rectangle REC_EDITSAVE = new Rectangle(500, 45, 160, 30);
	
	private Rectangle REC_RESETMD = new Rectangle(250, 45, 80, 30);
	private Rectangle REC_DEFKOT = new Rectangle(30, 45, 170, 30);
	private Rectangle REC_BTNRRUNDE = new Rectangle(340, 45, 120, 30);
	
	private static final int INDENT = 20;
	private Rectangle REC_PNLRRUNDE;
	private Rectangle REC_LBLRRDESCR;
	private Rectangle REC_LBLHRUNDE;
	private Rectangle REC_LBLRRUNDE;
	private Rectangle REC_BTNRRCANCEL;
	private Rectangle REC_BTNRRCOMPLETE;

	private JScrollPane jSPTeamsSelection;
	private JPanel jPnlTeamsSelection;
	private JButton[] jBtnsMannschaften;

	private int numberOfTeams;
	private int numberOfMatches;
	private int numberOfMatchdaysBeforePlayoff;
	private int numberOfMatchdays;
	private int numberOfMatchdaysIncludingPlayoff;
	private int halfCountTeamsRoundUp;

	private boolean belongsToALeague = false;
	private boolean belongsToGroup = false;
	private boolean belongsToKORound = false;
	private boolean hasPlayoffs = false;
	private boolean isOverview = false;
	private boolean changeTFs = true;
	private boolean isETPossible;
	private boolean nothingToSave = true;
	private Wettbewerb competition;
	private LigaSaison lSeason;
	private Gruppe group;
	private KORunde koRound;
	private TurnierSaison tSeason;
	private Gruppe[] allGroups;
	private Mannschaft[] teams;
	
	// Overview
	private int[] numbersOfTeams;
	private int[] numbersOfMatches;
	private int[] oldOrder;
	private int[] newOrder;

	public Spieltag(LigaSaison lSeason) {
		super();

		this.lSeason = lSeason;
		competition = lSeason;
		belongsToALeague = true;
		belongsToGroup = false;
		belongsToKORound = false;
		hasPlayoffs = lSeason.hasPlayoffs();
		isOverview = false;
		isETPossible = lSeason.isExtraTimePossible();

		numberOfMatches = lSeason.getNumberOfMatchesPerMatchday();
		numberOfTeams = lSeason.getNumberOfTeams();
		numberOfMatchdays = lSeason.getNumberOfMatchdays();
		numberOfMatchdaysBeforePlayoff = numberOfMatchdays;
		numberOfMatchdaysIncludingPlayoff = lSeason.getNumberOfMatchdaysIncludingPlayoff();
		halfCountTeamsRoundUp = lSeason.getHalfNOfTeamsUp();

		initGUI();
	}

	public Spieltag(Gruppe group) {
		super();

		this.group = group;
		competition = group;
		belongsToALeague = false;
		belongsToGroup = true;
		belongsToKORound = false;
		isOverview = false;
		isETPossible = group.isExtraTimePossible();

		numberOfMatches = group.getNumberOfMatchesPerMatchday();
		numberOfTeams = group.getNumberOfTeams();
		numberOfMatchdays = group.getNumberOfMatchdays();
		numberOfMatchdaysIncludingPlayoff = numberOfMatchdays;
		halfCountTeamsRoundUp = (numberOfTeams % 2 == 0 ? numberOfTeams / 2 : numberOfTeams / 2 + 1);

		initGUI();
	}
	
	public Spieltag(KORunde koRound) {
		super();
		
		this.koRound = koRound;
		competition = koRound;
		belongsToALeague = false;
		belongsToGroup = false;
		belongsToKORound = true;
		hasPlayoffs = koRound.belongsToALeague();
		isOverview = false;
		isETPossible = koRound.isExtraTimePossible();
		
		numberOfMatches = koRound.getNumberOfMatchesPerMatchday();
		numberOfTeams = 2 * numberOfMatches;
		numberOfMatchdays = koRound.hasSecondLeg() ? 2 : 1;
		numberOfMatchdaysBeforePlayoff = koRound.getNumberOfMatchdaysBeforePlayoff();
		numberOfMatchdaysIncludingPlayoff = numberOfMatchdays + numberOfMatchdaysBeforePlayoff;
		halfCountTeamsRoundUp = numberOfTeams / 2;
		
		initGUI();
	}
	
	public Spieltag(TurnierSaison tSeason, boolean isQ) {
		super();
		
		this.tSeason = tSeason;
		allGroups = isQ ? tSeason.getQGroups() : tSeason.getGroups();
		belongsToALeague = false;
		belongsToGroup = false;
		belongsToKORound = false;
		isOverview = true;
		isETPossible = tSeason.isETPossible();
		
		numbersOfTeams = new int[isQ ? tSeason.getNumberOfQGroups() : tSeason.getNumberOfGroups()];
		numbersOfMatches = new int[isQ ? tSeason.getNumberOfQGroups() : tSeason.getNumberOfGroups()];
		
		for (Gruppe group : allGroups) {
			int nOMatchdays = group.getNumberOfMatchdays();
			numbersOfTeams[group.getID()] = group.getNumberOfTeams();
			numbersOfMatches[group.getID()] = group.getNumberOfMatchesPerMatchday();
			
			numberOfMatches += numbersOfMatches[group.getID()];
			numberOfTeams += numbersOfTeams[group.getID()];
			numberOfMatchdays = (numberOfMatchdays > nOMatchdays ? numberOfMatchdays : nOMatchdays);
			numberOfMatchdaysIncludingPlayoff = numberOfMatchdays;
		}
		halfCountTeamsRoundUp = (numberOfTeams + 1) / 2;
		
		initGUI();
	}

	private void calculateButtonsauswahlBounds(int maxHeight) {
		btnsSelection = new int[6];
		btnsSelection[STARTX] = 10;
		btnsSelection[STARTY] = 10;
		btnsSelection[GAPX] = 5;
		btnsSelection[GAPY] = 5;
		btnsSelection[SIZEX] = 200;
		btnsSelection[SIZEY] = 50;
		
		if (2 * btnsSelection[STARTY] + halfCountTeamsRoundUp * (btnsSelection[SIZEY] + btnsSelection[GAPY]) > maxHeight) {
			btnsSelection[STARTX] = 5;
			btnsSelection[STARTY] = 5;
			btnsSelection[GAPY] = 1;
			int buttons = maxHeight - 2 * btnsSelection[STARTY] - (halfCountTeamsRoundUp - 1) * btnsSelection[GAPY];
			btnsSelection[SIZEY] = buttons / halfCountTeamsRoundUp;
			btnsSelection[SIZEY] = Math.max(20, btnsSelection[SIZEY]);
			
			if (halfCountTeamsRoundUp * (btnsSelection[SIZEY] + btnsSelection[GAPY]) > maxHeight) {
				scrollBarWidth = 20;
			}
		}
	}
	
	private void calculateBounds() {
		int zusInfWidth = 35, height = 25, gapy = 2;
		lblsDates = new int[] { 5, 5, 0, gapy, 120, height };
		
		lblsGroup = new int[] { 135, 5, 0, gapy, 10, height };
		
		lblsTeams = new int[] { lblsGroup[STARTX] + lblsGroup[SIZEX] + 5, lblsGroup[STARTY], (isETPossible ? zusInfWidth + 145 : 135), gapy, 180, height };

		tfsGoals = new int[] { lblsTeams[STARTX] + lblsTeams[SIZEX] + 10, lblsTeams[STARTY], 0, gapy, 40, height };
		
		lblsAddInfo = new int[] { lblsTeams[STARTX] + lblsTeams[SIZEX] + 95, lblsTeams[STARTY], 0, gapy, (isETPossible ? zusInfWidth : 0), height };
		
		btnsMoreOpt = new int[] { lblsTeams[STARTX] + lblsTeams[SIZEX] + lblsTeams[GAPX] - 45, lblsTeams[STARTY], 0, gapy, 40, height };
		
		matchesWidth = 650 + (isETPossible ? zusInfWidth + 10 : 0);
		matchesHeight = numberOfMatches * (height + gapy) - gapy + 2 * 5;
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);

			// TODO min und maxheight sinnvoll setzen
			int minimumHeight = 450;
			int maximumHeight = 840;
			calculateButtonsauswahlBounds(maximumHeight - 2 * WIDTH_BORDER);
			calculateBounds();

			Dimension dim = new Dimension();
			dim.width = 1200;

			int heightOfTeamLabels = lblsTeams[STARTY] + numberOfMatches * (lblsTeams[SIZEY] + lblsTeams[GAPY]) + 20;
			int heightOfTeamSelection = 2 * btnsSelection[STARTY] + halfCountTeamsRoundUp * (btnsSelection[SIZEY] + btnsSelection[GAPY]) - btnsSelection[GAPY] + 2 * WIDTH_BORDER;

			dim.height = (heightOfTeamLabels > heightOfTeamSelection ? heightOfTeamLabels : heightOfTeamSelection);

			// correction to minimumheight or maximumheight if out of these bounds
			if (dim.height < minimumHeight) {
				dim.height = minimumHeight;
			} else if (dim.height > maximumHeight) {
				dim.height = maximumHeight;
			}

			setSize(dim);

			results = new Ergebnis[numberOfMatches];
			
			jLblsGroups = new JLabel[numberOfMatches];
			jLblsAdditionalInfos = new JLabel[numberOfMatches];
			jBtnsMatchInfos = new  JButton[numberOfMatches];
			jBtnsMannschaften = new JButton[numberOfTeams];
			jLblsTeams = new JLabel[2 * numberOfMatches];
			jTFsGoals = new JTextField[2 * numberOfMatches];
			array = new int[numberOfMatches][2];
			jLblsDates = new JLabel[numberOfMatches];
			jLblsHinrunde = new JLabel[numberOfMatchdays / 2];
			jLblsRueckrunde = new JLabel[numberOfMatchdays / 2];
			matchdaysHinrunde = new ArrayList<>();
			matchdaysRueckrunde = new ArrayList<>();
			
			{
				jCBMatchdays = new JComboBox<>();
				this.add(jCBMatchdays);
				jCBMatchdays.setBounds(REC_COMBO);
				jCBMatchdays.setFocusable(false);
				jCBMatchdays.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						jCBMatchdaysItemStateChanged(evt);
					}
				});
			}
			
			{
				jPnlMatches = new JPanel();
				jPnlMatches.setLayout(null);
				jPnlMatches.setPreferredSize(new Dimension(matchesWidth, matchesHeight));
				jPnlMatches.setOpaque(true);
				jPnlMatches.setBackground(colorMatches);
			}
			{
				jSPMatches = new JScrollPane();
				this.add(jSPMatches);
				jSPMatches.setSize(matchesWidth + 20, Math.min(matchesHeight, maximumHeight - 2 * WIDTH_BORDER - 80));
				jSPMatches.setLocation(25, 80);
				jSPMatches.getVerticalScrollBar().setUnitIncrement(20);
				jSPMatches.setBorder(null);
				jSPMatches.setViewportView(jPnlMatches);
			}
			
			for (int i = 0; i < jLblsDates.length; i++) {
				final int x = i;
				jLblsDates[i] = new JLabel();
				jPnlMatches.add(jLblsDates[i]);
				jLblsDates[i].setBounds(lblsDates[STARTX], lblsDates[STARTY] + i * (lblsDates[SIZEY] + lblsDates[GAPY]), lblsDates[SIZEX], lblsDates[SIZEY]);
				jLblsDates[i].setCursor(handCursor);
				jLblsDates[i].setBackground(colorDatum);
				jLblsDates[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						datumsLabelClicked(x);
						jLblsTeams[x].setBorder(null);
						jLblsTeams[x + numberOfMatches].setBorder(null);
						jLblsDates[x].setBorder(null);
					}
					
					public void mouseEntered(MouseEvent e) {
						if (editedDate == -1) {
							jLblsTeams[x].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsTeams[x + numberOfMatches].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsDates[x].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsDates[x].setOpaque(true);
							repaintImmediately(jLblsDates[x]);
						}
					}

					public void mouseExited(MouseEvent e) {
						if (editedDate == -1) {
							jLblsTeams[x].setBorder(null);
							jLblsTeams[x + numberOfMatches].setBorder(null);
							jLblsDates[x].setBorder(null);
							jLblsDates[x].setOpaque(false);
							repaintImmediately(jLblsDates[x]);
						}
					}
				});
			}
			if (isOverview) {
				for (int i = 0; i < jLblsGroups.length; i++) {
					final int x = i;
					jLblsGroups[i] = new JLabel();
					jPnlMatches.add(jLblsGroups[i]);
					jLblsGroups[i].setBounds(lblsGroup[STARTX], lblsGroup[STARTY] + i * (lblsTeams[SIZEY] + lblsTeams[GAPY]), lblsGroup[SIZEX], lblsGroup[SIZEY]);
					alignRight(jLblsGroups[i]);
					jLblsGroups[i].setCursor(handCursor);
					jLblsGroups[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							groupClicked(x);
						}
					});
				}
			}
			
			for (int i = 0; i < jLblsTeams.length; i++) {
				final int x = i, row = i % numberOfMatches, column = i / numberOfMatches;

				jLblsTeams[i] = new JLabel();
				jPnlMatches.add(jLblsTeams[i]);
				jLblsTeams[i].setBounds(lblsTeams[STARTX] + column * (lblsTeams[SIZEX] + lblsTeams[GAPX]), 
						lblsTeams[STARTY] + row * (lblsTeams[SIZEY] + lblsTeams[GAPY]), lblsTeams[SIZEX], lblsTeams[SIZEY]);
				if (column == 0)	alignRight(jLblsTeams[i]);
				else				alignLeft(jLblsTeams[i]);
				jLblsTeams[i].setCursor(handCursor);
				jLblsTeams[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (editingMatches) {
							mannschaftClicked(x);
							jLblsTeams[x % numberOfMatches].setBorder(null);
							jLblsTeams[x % numberOfMatches + numberOfMatches].setBorder(null);
						} else if (!jLblsTeams[x].getText().equals(TEAM_NOT_SET)) {
							saveResults();
							Fussball.getInstance().uebersichtAnzeigen(jLblsTeams[x].getText());
						}
					}
					
					public void mouseEntered(MouseEvent e) {
						if (editingMatches) {
							jLblsTeams[x % numberOfMatches].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsTeams[x % numberOfMatches + numberOfMatches].setBorder(BorderFactory.createDashedBorder(getForeground()));
						}
					}
					
					public void mouseExited(MouseEvent e) {
						if (editingMatches) {
							jLblsTeams[x % numberOfMatches].setBorder(null);
							jLblsTeams[x % numberOfMatches + numberOfMatches].setBorder(null);
						}
					}
				});
			}
			
			for (int i = 0; i < jTFsGoals.length; i++) {
				final int x = i;
				jTFsGoals[i] = new JTextField();
				jPnlMatches.add(jTFsGoals[i]);
				jTFsGoals[i].setBounds(tfsGoals[STARTX] + (i / numberOfMatches) * (tfsGoals[SIZEX] + tfsGoals[GAPX]), 
						tfsGoals[STARTY] + (i % numberOfMatches) * (tfsGoals[SIZEY] + tfsGoals[GAPY]), tfsGoals[SIZEX], tfsGoals[SIZEY]);
				alignCenter(jTFsGoals[i]);
				jTFsGoals[i].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if (arg0.getKeyChar() == 8) {
							aValidKeyWasPressed(x, arg0);
						} else if ((jTFsGoals[x].getText().length() >= 2 && !jTFsGoals[x].getText().equals(GOAL_NOT_SET)) || arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						} else {
							aValidKeyWasPressed(x, arg0);
						}
					}
				});
				jTFsGoals[i].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent arg0) {
						jTFsGoals[x].selectAll();
					}
				});
			}
			for (int i = 0; i < jBtnsMatchInfos.length; i++) {
				final int x = i;
				jBtnsMatchInfos[i] = new JButton();
				jPnlMatches.add(jBtnsMatchInfos[i]);
				jBtnsMatchInfos[i].setBounds(btnsMoreOpt[STARTX], btnsMoreOpt[STARTY] + i * (btnsMoreOpt[SIZEY] + btnsMoreOpt[GAPY]), 
						btnsMoreOpt[SIZEX], btnsMoreOpt[SIZEY]);
				jBtnsMatchInfos[i].setText("+");
				jBtnsMatchInfos[i].setToolTipText("Reset this result.");
				jBtnsMatchInfos[i].setFocusable(false);
				jBtnsMatchInfos[i].setMargin(new Insets(2, 6, 2, 6));
				jBtnsMatchInfos[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnsMatchInfosClicked(x);
					}
				});
			}
			for (int i = 0; i < jLblsAdditionalInfos.length; i++) {
				jLblsAdditionalInfos[i] = new JLabel();
				jPnlMatches.add(jLblsAdditionalInfos[i]);
				jLblsAdditionalInfos[i].setBounds(lblsAddInfo[STARTX], lblsAddInfo[STARTY] + i * (lblsAddInfo[SIZEY] + lblsAddInfo[GAPY]), lblsAddInfo[SIZEX], lblsAddInfo[SIZEY]);
			}
			{
				jBtnEdit = new JButton();
				this.add(jBtnEdit);
				jBtnEdit.setBounds(REC_EDITSAVE);
				jBtnEdit.setText("Spielplan bearbeiten");
				jBtnEdit.setFocusable(false);
				jBtnEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnEditActionPerformed();
					}
				});
			}
			{
				jBtnDone = new JButton();
				this.add(jBtnDone);
				jBtnDone.setBounds(REC_EDITSAVE);
				jBtnDone.setText("Speichern");
				jBtnDone.setFocusable(false);
				jBtnDone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnDoneActionPerformed();
					}
				});
				jBtnDone.setVisible(false);
			}
			{
				jBtnPrevious = new JButton();
				this.add(jBtnPrevious);
				jBtnPrevious.setBounds(REC_PREV);
				jBtnPrevious.setText("<<");
				jBtnPrevious.setFocusable(false);
				jBtnPrevious.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						previousMatchday();
					}
				});
			}
			{
				jBtnNext = new JButton();
				this.add(jBtnNext);
				jBtnNext.setBounds(REC_NEXT);
				jBtnNext.setText(">>");
				jBtnNext.setFocusable(false);
				jBtnNext.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						nextMatchday();
					}
				});
			}
			{
				jBtnResetMatchday = new JButton();
				this.add(jBtnResetMatchday);
				jBtnResetMatchday.setBounds(REC_RESETMD);
				jBtnResetMatchday.setText("Reset");
				jBtnResetMatchday.setFocusable(false);
				jBtnResetMatchday.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						resetMatchdayActionPerformed();
					}
				});
			}
			{
				jBtnSecondLeg = new JButton();
				this.add(jBtnSecondLeg);
				jBtnSecondLeg.setBounds(REC_BTNRRUNDE);
				jBtnSecondLeg.setText("Rückspiele");
				jBtnSecondLeg.setFocusable(false);
				jBtnSecondLeg.setVisible(false);
				jBtnSecondLeg.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnSecondLegActionPerformed();
					}
				});
			}
			{
				jBtnEnterRueckrunde = new JButton();
				this.add(jBtnEnterRueckrunde);
				jBtnEnterRueckrunde.setBounds(REC_BTNRRUNDE);
				jBtnEnterRueckrunde.setText("Rückrunde");
				jBtnEnterRueckrunde.setFocusable(false);
				jBtnEnterRueckrunde.setVisible(false);
				jBtnEnterRueckrunde.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnEnterRueckrundeActionPerformed();
					}
				});
			}
			int width = (numberOfMatchdays / 2 - 1) * rrLabels[GAPX] + rrLabels[SIZEX] + 2 * INDENT;
			int height = rrLabels[STARTY] + rrLabels[GAPY] + rrLabels[SIZEY] + INDENT;
			REC_PNLRRUNDE = new Rectangle(50, 60, width, height);
			REC_LBLRRDESCR = new Rectangle(INDENT, INDENT, 570, 20);
			REC_LBLHRUNDE = new Rectangle(INDENT, rrLabels[STARTY] - 25, 60, 20);
			REC_LBLRRUNDE = new Rectangle(INDENT, rrLabels[STARTY] - 25 + rrLabels[GAPY], 80, 20);
			REC_BTNRRCANCEL = new Rectangle(width - 2 * (120 + INDENT), INDENT, 120, 25);
			REC_BTNRRCOMPLETE = new Rectangle(width - 120 - INDENT, INDENT, 120, 25);
			{
				jPnlEnterRueckrunde = new JPanel();
				this.add(jPnlEnterRueckrunde);
				jPnlEnterRueckrunde.setLayout(null);
				jPnlEnterRueckrunde.setBackground(colorSelection);
				jPnlEnterRueckrunde.setBounds(REC_PNLRRUNDE);
				jPnlEnterRueckrunde.setVisible(false);
			}
			{
				jLblRRDescription = new JLabel();
				jPnlEnterRueckrunde.add(jLblRRDescription);
				jLblRRDescription.setBounds(REC_LBLRRDESCR);
				jLblRRDescription.setText("Klicken die auf die Spieltage in der Reihenfolge, wie sie in der Rückrunde gespielt werden.");
			}
			{
				jLblHinrunde = new JLabel();
				jPnlEnterRueckrunde.add(jLblHinrunde);
				jLblHinrunde.setBounds(REC_LBLHRUNDE);
				jLblHinrunde.setText("Hinrunde");
			}
			{
				jLblRueckrunde = new JLabel();
				jPnlEnterRueckrunde.add(jLblRueckrunde);
				jLblRueckrunde.setBounds(REC_LBLRRUNDE);
				jLblRueckrunde.setText("Rückrunde");
			}
			for (int i = 0; i < numberOfMatchdays / 2; i++) {
				matchdaysHinrunde.add(i + 1);
				
				final int x = i;
				jLblsHinrunde[i] = new JLabel();
				jPnlEnterRueckrunde.add(jLblsHinrunde[i]);
				jLblsHinrunde[i].setBounds(rrLabels[STARTX] + i * rrLabels[GAPX], rrLabels[STARTY], rrLabels[SIZEX], rrLabels[SIZEY]);
				alignCenter(jLblsHinrunde[i]);
				jLblsHinrunde[i].setFont(fontRR);
				jLblsHinrunde[i].setText("" + (i + 1));
				jLblsHinrunde[i].setCursor(handCursor);
				jLblsHinrunde[i].setOpaque(true);
				jLblsHinrunde[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						putHinrundeMDToNextFreePosition(x);
					}
				});
				
				jLblsRueckrunde[i] = new JLabel();
				jPnlEnterRueckrunde.add(jLblsRueckrunde[i]);
				jLblsRueckrunde[i].setBounds(rrLabels[STARTX] + i * rrLabels[GAPX], rrLabels[STARTY] + rrLabels[GAPY], rrLabels[SIZEX], rrLabels[SIZEY]);
				alignCenter(jLblsRueckrunde[i]);
				jLblsRueckrunde[i].setFont(fontRR);
				jLblsRueckrunde[i].setText("");
				jLblsRueckrunde[i].setCursor(handCursor);
				jLblsRueckrunde[i].setOpaque(true);
				jLblsRueckrunde[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						putRueckrundeMDToNextFreePosition(x);
					}
				});
			}
			{
				jBtnEnterRueckrundeCancel = new JButton();
				jPnlEnterRueckrunde.add(jBtnEnterRueckrundeCancel);
				jBtnEnterRueckrundeCancel.setBounds(REC_BTNRRCANCEL);
				jBtnEnterRueckrundeCancel.setText("abbrechen");
				jBtnEnterRueckrundeCancel.setFocusable(false);
				jBtnEnterRueckrundeCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnEnterRueckrundeCancelActionPerformed();
					}
				});
			}
			{
				jBtnEnterRueckrundeCompleted = new JButton();
				jPnlEnterRueckrunde.add(jBtnEnterRueckrundeCompleted);
				jBtnEnterRueckrundeCompleted.setBounds(REC_BTNRRCOMPLETE);
				jBtnEnterRueckrundeCompleted.setText("fertig");
				jBtnEnterRueckrundeCompleted.setFocusable(false);
				jBtnEnterRueckrundeCompleted.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnEnterRueckrundeCompletedActionPerformed();
					}
				});
			}
			if (belongsToALeague) {
				jBtnDefaultKickoff = new JButton();
				this.add(jBtnDefaultKickoff);
				jBtnDefaultKickoff.setBounds(REC_DEFKOT);
				jBtnDefaultKickoff.setText("Standardanstoßzeiten");
				jBtnDefaultKickoff.setFocusable(false);
				jBtnDefaultKickoff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnDefaultKickoffActionPerformed();
					}
				});
			}
			
			width = 2 * (btnsSelection[STARTX] + btnsSelection[SIZEX]) + btnsSelection[GAPX];
			height = 2 * btnsSelection[STARTY] + halfCountTeamsRoundUp * (btnsSelection[SIZEY] + btnsSelection[GAPY]) - btnsSelection[GAPY];
			{
				jPnlTeamsSelection = new JPanel();
				jPnlTeamsSelection.setLayout(null);
				jPnlTeamsSelection.setPreferredSize(new Dimension(width, height));
				jPnlTeamsSelection.setOpaque(true);
				jPnlTeamsSelection.setBackground(colorSelection);
			}
			{
				jSPTeamsSelection = new JScrollPane();
				this.add(jSPTeamsSelection);
				jSPTeamsSelection.setSize(width + scrollBarWidth, Math.min(maximumHeight - 2 * WIDTH_BORDER, height));
				jSPTeamsSelection.setLocation(this.getSize().width - jSPTeamsSelection.getSize().width - WIDTH_BORDER, (this.getSize().height - jSPTeamsSelection.getSize().height) / 2);
				jSPTeamsSelection.getVerticalScrollBar().setUnitIncrement(20);
				jSPTeamsSelection.setBorder(null);
				jSPTeamsSelection.setViewportView(jPnlTeamsSelection);
				jSPTeamsSelection.setVisible(false);
			}

			for (int i = 0; i < numberOfTeams; i++) {
				final int x = i;
				int xFactor = i % 2, yFactor = i / 2;
				jBtnsMannschaften[i] = new JButton();
				jPnlTeamsSelection.add(jBtnsMannschaften[i]);
				jBtnsMannschaften[i].setBounds(btnsSelection[STARTX] + xFactor * (btnsSelection[SIZEX] + btnsSelection[GAPX]),
								btnsSelection[STARTY] + yFactor * (btnsSelection[SIZEY] + btnsSelection[GAPY]), btnsSelection[SIZEX], btnsSelection[SIZEY]);
				jBtnsMannschaften[i].setFocusable(false);
				jBtnsMannschaften[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mannschaftenButtonClicked(x);
					}
				});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(colorRand);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.clearRect(WIDTH_BORDER, WIDTH_BORDER, getWidth() - 2 * WIDTH_BORDER, getHeight() - 2 * WIDTH_BORDER);
	}
	
	public void matchdays() {
		jCBMatchdays.setModel(new DefaultComboBoxModel<>(competition.getMatchdays()));
	}

	public int getNumberOfMatches() {
		return numberOfMatches;
	}
	
	public int getCurrentMatchday() {
		return currentMatchday;
	}

	public void resetCurrentMatchday() {
		currentMatchday = -1;
	}
	
	public int getEditedMatchday() {
		return editedMatchday;
	}
	
	public Ergebnis getResult(int match) {
		return results[match];
	}

	public Ergebnis getResult(int groupID, int match) {
		for (int i = 0; i < groupID; i++) {
			match += numbersOfMatches[i];
		}
		return results[match];
	}
	
	private void setResult(int match, Ergebnis result) {
		results[match] = result;
		
		if (changeTFs) {
			int index = newOrder != null ? newOrder[match] : match;
			jLblsAdditionalInfos[index].setText("");
			
			if (result != null) {
				jTFsGoals[index].setText("" + result.home());
				jTFsGoals[index + numberOfMatches].setText("" + result.away());
				jLblsAdditionalInfos[index].setText(result.getMore());
				jLblsAdditionalInfos[index].setToolTipText(result.getTooltipText());
			} else {
				jTFsGoals[index].setText(GOAL_NOT_SET);
				jTFsGoals[index + numberOfMatches].setText(GOAL_NOT_SET);
			}
		}
	}
	
	public void showMatchday(int matchday) {
		matchdays();
		currentMatchday = matchday;
		jCBMatchdays.setSelectedIndex(matchday);
	}
	
	public void ensureNoOpenedMatchInfos() {
		while (openedMatchInfos.size() > 0) {
			openedMatchInfos.get(0).jBtnGoActionPerformed();
		}
	}
	
	private void saveResults() {
		if (nothingToSave)	return;
		if (belongsToALeague)		lSeason.getResultsFromSpieltag();
		else if (belongsToGroup)	group.getResultsFromSpieltag();
		else if (belongsToKORound)	koRound.getResultsFromSpieltag();
		else						tSeason.getResultsFromSpieltag();
	}

	private void jCBMatchdaysItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			ensureNoOpenedMatchInfos();
			saveResults();
			showMatchday();
		}
	}
	
	private void aValidKeyWasPressed(int indexOfTF, KeyEvent arg0) {
		String selText = jTFsGoals[indexOfTF].getSelectedText();
		String otherGoals = jTFsGoals[(indexOfTF + numberOfMatches) % (2 * numberOfMatches)].getText();
		String newContent = "";
		
		if (arg0.getKeyChar() == 8) {
			if (jTFsGoals[indexOfTF].getText().length() == 0 || jTFsGoals[indexOfTF].getText().equals("-")) {
				jTFsGoals[indexOfTF].setText(GOAL_NOT_SET);
				jTFsGoals[indexOfTF].selectAll();
			}
			newContent = jTFsGoals[indexOfTF].getText();
		} else {
			if (selText != null) 	newContent = jTFsGoals[indexOfTF].getText().replaceAll(selText, "");
			else					newContent = jTFsGoals[indexOfTF].getText();
			newContent += arg0.getKeyChar();
		}
		
		int[] goals = new int[2];
		goals[indexOfTF / numberOfMatches] = Integer.parseInt(newContent);
		goals[1 - indexOfTF / numberOfMatches] = Integer.parseInt(otherGoals);
		
		Ergebnis result;
		if (goals[0] == -1 || goals[1] == -1)	result = null;
		else									result = new Ergebnis(goals[0], goals[1]);
		changeTFs = false;
		if (isOverview)	setResult(oldOrder[indexOfTF % numberOfMatches], result);
		else			setResult(indexOfTF % numberOfMatches, result);
		changeTFs = true;
	}
	
	private void setLabelsOpaque(boolean enabled) {
		for (JLabel lbl : jLblsTeams) {
			lbl.setOpaque(false);
		}
	}
	
	private void setButtonsEnabled(boolean enabled) {
		for (JButton btn : jBtnsMatchInfos) {
			btn.setEnabled(enabled);
		}
	}
	
	private void disableTFs() {
		for (JTextField tf : jTFsGoals)	tf.setEditable(false);
	}
	
	private void setTFsEditableFromRepresentation() {
		String representation;
		if (belongsToALeague)		representation = lSeason.getMatchesSetRepresentation(currentMatchday);
		else if (belongsToGroup)	representation = group.getMatchesSetRepresentation(currentMatchday);
		else if (belongsToKORound)	representation = koRound.getMatchesSetRepresentation(currentMatchday);
		else {
			String groupOrder = "";
			for (Gruppe group : allGroups) {
				groupOrder += group.getMatchesSetRepresentation(currentMatchday);
			}
			char[] groupsChars = groupOrder.toCharArray();
			representation = "";
			for (int i = 0; i < numberOfMatches; i++) {
				representation += groupsChars[oldOrder[i]];
			}
		}
		
		for (int match = 0; match < numberOfMatches; match++) {
			if (representation.charAt(match) == 't') {
				jTFsGoals[match].setEditable(true);
				jTFsGoals[match + numberOfMatches].setEditable(true);
			} else if (representation.charAt(match) == 'f') {
				jTFsGoals[match].setEditable(false);
				jTFsGoals[match + numberOfMatches].setEditable(false);
			}
		}
		for (int match = 0; match < numberOfMatches; match++) {
			if (jTFsGoals[match].isEditable()) {
				jTFsGoals[match].selectAll();
				break;
			}
		}
	}
	
	private void fillDatesGroupOrder() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		int groupID = 0, matchID = 0;
		for (int i = 0; i < numberOfMatches; i++) {
			dateandtimeofmatches[i] = allGroups[groupID].getDateAndTime(currentMatchday, matchID);
			jLblsDates[i].setText(dateandtimeofmatches[i]);
			
			matchID++;
			if (matchID == numbersOfMatches[groupID]) {
				matchID = 0;
				groupID++;
			}
		}
		for (int i = jLblsDates.length - 1; i > 0; i--) {
			if (jLblsDates[i].getText().equals(jLblsDates[i - 1].getText())) {
				jLblsDates[i].setText("");
			}
		}
	}

	private void fillDates() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		int groupID = 0, matchID = 0;
		for (int i = 0; i < numberOfMatches; i++) {
			if (belongsToALeague)		dateandtimeofmatches[i] = lSeason.getDateAndTime(currentMatchday, i);
			else if (belongsToGroup)	dateandtimeofmatches[i] = group.getDateAndTime(currentMatchday, i);
			else if (belongsToKORound)	dateandtimeofmatches[i] = koRound.getDateAndTime(currentMatchday, i);
			else {
				dateandtimeofmatches[i] = allGroups[groupID].getDateAndTime(currentMatchday, matchID);
				
				matchID++;
				if (matchID == numbersOfMatches[groupID]) {
					matchID = 0;
					groupID++;
				}
			}
			if (isOverview)	jLblsDates[newOrder[i]].setText(dateandtimeofmatches[i]);
			else			jLblsDates[i].setText(dateandtimeofmatches[i]);
		}
		for (int i = jLblsDates.length - 1; i > 0; i--) {
			if (jLblsDates[i].getText().equals(jLblsDates[i - 1].getText())) {
				jLblsDates[i].setText("");
			}
		}
	}

	private void setMannschaftenButtonsNames() {
		int groupID = 0, teamID = 0;
		for (int i = 0; i < jBtnsMannschaften.length; i++) {
			if (belongsToALeague)		jBtnsMannschaften[i].setText(lSeason.getTeams()[i].getName());
			else if (belongsToGroup)	jBtnsMannschaften[i].setText(group.getTeams()[i].getName());
			else if (belongsToKORound) {
				try {
					jBtnsMannschaften[i].setText(teams[i].getName());
				} catch (NullPointerException npe) {
					jBtnsMannschaften[i].setText(koRound.getTeamsOrigin(i));
				}
			} else {
				jBtnsMannschaften[i].setText(allGroups[groupID].getTeams()[teamID].getName());
				
				teamID++;
				if (teamID == numbersOfTeams[groupID]) {
					teamID = 0;
					groupID++;
				}
			}
		}
	}

	/**
	 * Optimized for <code>Gruppe</code> and <code>KORunde</code>
	 */
	private void jBtnEditActionPerformed() {
		editingMatches = true;
		editedMatchday = currentMatchday;
		
		setMannschaftenButtonsNames();
		
		if (isOverview) {
			setMatchesInGroupOrder();
			fillDatesGroupOrder();
		}
		
		Spiel match;
		for (int i = 0; i < array.length; i++) {
			if (belongsToALeague)		match = lSeason.getMatch(editedMatchday, i);
			else if (belongsToGroup)	match = group.getMatch(editedMatchday, i);
			else if (belongsToKORound)	match = koRound.getMatch(editedMatchday, i);
			else {
				int groupID = 0, matchID = i;
				while (matchID > 0) {
					matchID -= numbersOfMatches[groupID];
					groupID++;
				}
				if (matchID < 0) {
					groupID--;
					matchID += numbersOfMatches[groupID];
				}
				
				match = allGroups[groupID].getMatch(editedMatchday, matchID);
			}
			
			if (match != null) {
				if (isOverview) {
					int groupID = 0, matchID = i, teamsDiff = 0;
					while (matchID > 0) {
						matchID -= numbersOfMatches[groupID];
						teamsDiff += numbersOfTeams[groupID];
						groupID++;
					}
					if (matchID < 0) {
						groupID--;
						matchID += numbersOfMatches[groupID];
						teamsDiff -= numbersOfTeams[groupID];
					}
					
					array[i][0] = match.home() + teamsDiff;
					array[i][1] = match.away() + teamsDiff;
				} else {
					array[i][0] = match.home();
					array[i][1] = match.away();
				}
			} else {
				array[i][0] = -1;
				array[i][1] = -1;
			}
		}
		
		for (int i = 0; i < jBtnsMannschaften.length; i++) {
			jBtnsMannschaften[i].setEnabled(true);
		}
		setLabelsOpaque(true);
		setButtonsEnabled(false);
		disableTFs();
		jBtnEdit.setVisible(false);
		jBtnDone.setVisible(true);
		jCBMatchdays.setEnabled(false);
		jBtnPrevious.setEnabled(false);
		jBtnNext.setEnabled(false);
		jBtnResetMatchday.setVisible(false);
		jBtnSecondLeg.setVisible(false);
		jBtnEnterRueckrunde.setVisible(false);
		if (jBtnDefaultKickoff != null)	jBtnDefaultKickoff.setVisible(false);
		clickNextEmptySpot();
	}

	/**
	 * Optimized for <code>Gruppe</code> and <code>KORunde</code>, setMatch is not called for KORunde
	 * @return if the content was saved: 0 if yes, 1 if not
	 */
	public int jBtnDoneActionPerformed() {
		int error = -1;
		// 1. Fehlerfall: es befinden sich noch ungesetzte Felder im Array
		for (int i = 0; i < array.length; i++) {
			if (array[i][0] == -1 || array[i][1] == -1) {
				error = 1;
				break;
			}
		}
		
		int saveAnyway = 0;
		if (error == 1) {
			saveAnyway = JOptionPane.showConfirmDialog(null, "Es fehlen noch Spiele. \nTrotzdem fortfahren?", "Warnung", JOptionPane.YES_NO_OPTION);
		}
		
		if (saveAnyway == 0) {
			if (belongsToKORound)	koRound.setCheckTeamsFromPreviousRound(false);
			int groupID = 0, matchIDAll = 0, offset = 0, home, away;
			for (int matchID = 0; matchID < array.length; matchID++) {
				if (isOverview)	competition = allGroups[groupID];
				Spiel match = null, other;
				
				other = competition.getMatch(editedMatchday, matchIDAll);
				
				if ((home = array[matchID][0]) != -1 && (away = array[matchID][1]) != -1) {
					match = new Spiel(competition, editedMatchday, competition.getDate(editedMatchday, matchIDAll), 
								competition.getTime(editedMatchday, matchIDAll), home - offset, away - offset);
				}
				
				if (match != null && other != null && match.sameAs(other))	match = other;
				
				competition.setMatch(editedMatchday, matchIDAll, match);
				
				matchIDAll++;
				if (isOverview && matchIDAll == numbersOfMatches[groupID]) {
					offset += numbersOfTeams[groupID];
					matchIDAll = 0;
					groupID++;
				}
			}
			if (belongsToKORound)	koRound.setCheckTeamsFromPreviousRound(true);
			
			editedMatchday = -1;
			
			setLabelsOpaque(false);
			setButtonsEnabled(true);
			setTFsEditableFromRepresentation();
			jBtnDone.setVisible(false);
			jSPTeamsSelection.setVisible(false);
			
			jBtnEdit.setVisible(true);
			jCBMatchdays.setEnabled(true);
			jBtnPrevious.setEnabled(true);
			jBtnNext.setEnabled(true);
			jBtnResetMatchday.setVisible(true);
			if (jBtnDefaultKickoff != null)	jBtnDefaultKickoff.setVisible(true);
			editingMatches = false;
			showMatchday();
		}
		return saveAnyway;
	}
	
	private void previousMatchday() {
		jCBMatchdays.setSelectedIndex(jCBMatchdays.getSelectedIndex() - 1);
	}
	
	private void nextMatchday() {
		jCBMatchdays.setSelectedIndex(jCBMatchdays.getSelectedIndex() + 1);
	}
	
	private void resetMatchdayActionPerformed() {
		if (yesNoDialog("Do you really want to reset this matchday? This is irrevocable.") == JOptionPane.NO_OPTION) return;
		
		if (belongsToALeague) {
			for (int match = 0; match < lSeason.getNumberOfMatchesPerMatchday(); match++) {
				lSeason.setMatch(currentMatchday, match, null);
			}
			
			for (int team = 0; team < lSeason.getNumberOfTeams(); team++) {
				lSeason.getTeams()[team].resetMatch(currentMatchday);
			}
			
			lSeason.getResultsFromSpieltag();
			showMatchday();
		} else if (belongsToGroup) {
			for (int match = 0; match < group.getNumberOfMatchesPerMatchday(); match++) {
				group.setMatch(currentMatchday, match, null);
			}
			
			for (int team = 0; team < group.getNumberOfTeams(); team++) {
				group.getTeams()[team].resetMatch(currentMatchday);
			}
			
			group.getResultsFromSpieltag();
			showMatchday();
		} else if (belongsToKORound) {
			for (int match = 0; match < koRound.getNumberOfMatchesPerMatchday(); match++) {
				koRound.setMatch(currentMatchday, match, null);
			}
			
			koRound.getResultsFromSpieltag();
			showMatchday();
		} else {
			for (Gruppe group : allGroups) {
				for (int match = 0; match < group.getNumberOfMatchesPerMatchday(); match++) {
					group.setMatch(currentMatchday, match, null);
				}
				
				for (int team = 0; team < group.getNumberOfTeams(); team++) {
					group.getTeams()[team].resetMatch(currentMatchday);
				}
			}
			
			tSeason.getResultsFromSpieltag();
			showMatchday();
		}
	}
	
	private void jBtnSecondLegActionPerformed() {
		boolean hasData = false;
		for (int matchID = 0; matchID < koRound.getNumberOfMatchesPerMatchday() && !hasData; matchID++) {
			hasData = koRound.getMatch(1, matchID) != null;
		}
		if (hasData && yesNoDialog("Willst du die gespeicherten Daten überschreiben?") != JOptionPane.YES_OPTION)	return;
		
		ArrayList<Spiel> secondLegs = new ArrayList<>();
		for (int matchID = 0; matchID < koRound.getNumberOfMatchesPerMatchday(); matchID++) {
			Spiel match = koRound.getMatch(0, matchID), secondLeg = null;
			if (match != null)	secondLeg = new Spiel(koRound, 1, MAX_DATE, TIME_UNDEFINED, match.away(), match.home());
			
			int index = 0;
			if (secondLeg != null) {
				for (index = 0; index < secondLegs.size(); index++) {
					if (secondLegs.get(index) == null || secondLeg.home() < secondLegs.get(index).home())	break;
				}
			}
			secondLegs.add(index, secondLeg);
		}
		
		for (int matchID = 0; matchID < koRound.getNumberOfMatchesPerMatchday(); matchID++) {
			koRound.setMatch(1, matchID, secondLegs.get(matchID));
		}
		showMatchday();
	}
	
	private void jBtnEnterRueckrundeActionPerformed() {
		showRueckrundePanel(true);
	}
	
	private void putHinrundeMDToNextFreePosition(int index) {
		jLblsRueckrunde[matchdaysRueckrunde.size()].setText("" + matchdaysHinrunde.get(index));
		matchdaysRueckrunde.add(matchdaysHinrunde.remove(index));
		jLblsHinrunde[matchdaysHinrunde.size()].setVisible(false);
		for (int i = index; i < matchdaysHinrunde.size(); i++) {
			jLblsHinrunde[i].setText("" + matchdaysHinrunde.get(i));
		}
		jLblsHinrunde[matchdaysHinrunde.size()].setText("");
	}
	
	private void putRueckrundeMDToNextFreePosition(int index) {
		if (index >= matchdaysRueckrunde.size())	return;
		jLblsHinrunde[matchdaysHinrunde.size()].setVisible(true);
		jLblsHinrunde[matchdaysHinrunde.size()].setText("" + matchdaysRueckrunde.get(index));
		matchdaysHinrunde.add(matchdaysRueckrunde.remove(index));
		for (int i = index; i < matchdaysRueckrunde.size(); i++) {
			jLblsRueckrunde[i].setText("" + matchdaysRueckrunde.get(i));
		}
		jLblsRueckrunde[matchdaysRueckrunde.size()].setText("");
	}
	
	private void jBtnEnterRueckrundeCancelActionPerformed() {
		showRueckrundePanel(false);
	}
	
	private void jBtnEnterRueckrundeCompletedActionPerformed() {
		if (matchdaysHinrunde.size() > 0) {
			message("Es fehlen noch Spieltage aus der Hinrunde.");
			return;
		}
		
		int[] rueckrundeOrder = new int[matchdaysRueckrunde.size()];
		for (int i = 0; i < rueckrundeOrder.length; i++) {
			rueckrundeOrder[i] = matchdaysRueckrunde.remove(0);
		}
		
		showRueckrundePanel(false);
		lSeason.setRueckrundeToOrder(rueckrundeOrder);
		showMatchday();
	}
	
	private void showRueckrundePanel(boolean showRRPanel) {
		jPnlEnterRueckrunde.setVisible(showRRPanel);
		jSPMatches.setVisible(!showRRPanel);
		
		jBtnEdit.setVisible(!showRRPanel);
		jBtnResetMatchday.setVisible(!showRRPanel);
		jBtnEnterRueckrunde.setVisible(!showRRPanel);
		if (jBtnDefaultKickoff != null)	jBtnDefaultKickoff.setVisible(!showRRPanel);
	}

	private void jBtnDefaultKickoffActionPerformed() {
		if (belongsToALeague) {
			lSeason.useDefaultKickoffTimes(currentMatchday);
		} else if (belongsToGroup) {
			message("Nicht vorgesehen für Gruppe");
		} else if (belongsToKORound) {
			message("Nicht vorgesehen für KORunde");
		} else {
			message("Nicht vorgesehen für Gruppenübersicht");
		}
		fillDates();
	}

	private void changeOrderToChronological() {
		if (belongsToALeague)		lSeason.changeOrderToChronological(currentMatchday);
		else if (belongsToGroup)	group.changeOrderToChronological(currentMatchday);
		else if (belongsToKORound)	koRound.changeOrderToChronological(currentMatchday);
		else if (isOverview) {
			oldOrder = tSeason.getChronologicalOrder(currentMatchday); // beinhaltet die alten Indizes in der neuen Reihenfolge
			newOrder = new int[oldOrder.length]; // beinhaltet die neuen Indizes in der alten Reihenfolge
			for (int i = 0; i < oldOrder.length; i++) {
				newOrder[oldOrder[i]] = i;
			}
		}
	}

	public void showMatchday() {
		if (currentMatchday == -1) {
			matchdays();
			if (belongsToALeague)		currentMatchday = lSeason.getCurrentMatchday();
			else if (belongsToGroup)	currentMatchday = group.getCurrentMatchday();
			else if (belongsToKORound)	currentMatchday = koRound.getCurrentMatchday();
			else 						currentMatchday = tSeason.getCurrentMatchday();
			
			if (currentMatchday == jCBMatchdays.getSelectedIndex()) {
				// dann gibt es keinen ItemStateChange und die Methode wird nicht aufgerufen
				showMatchday();
			}
			
			jCBMatchdays.setSelectedIndex(currentMatchday);
		} else {
			currentMatchday = jCBMatchdays.getSelectedIndex();
			if (hasPlayoffs) {
				if (currentMatchday >= numberOfMatchdaysBeforePlayoff && belongsToALeague) {
					nothingToSave = true;
					Fussball.getInstance().switchToPlayoff(currentMatchday);
					return;
				} else if (currentMatchday < numberOfMatchdaysBeforePlayoff && belongsToKORound) {
					nothingToSave = true;
					Fussball.getInstance().switchToMainSeason(currentMatchday);
					return;
				}
			}
			
			jBtnPrevious.setEnabled(currentMatchday > 0);
			jBtnNext.setEnabled(currentMatchday + 1 < numberOfMatchdaysIncludingPlayoff);
			
			if (hasPlayoffs && belongsToKORound)	currentMatchday -= numberOfMatchdaysBeforePlayoff;
			
			changeOrderToChronological();
			
			fillDates();
			
			results = new Ergebnis[numberOfMatches];
			
			fillTeamsLabelsAndGoalsTFs();
			setTFsEditableFromRepresentation();
			
			jBtnEnterRueckrunde.setVisible(belongsToALeague && currentMatchday * 2 == numberOfMatchdays);
			jBtnSecondLeg.setVisible(belongsToKORound && currentMatchday == 1);
			nothingToSave = false;
		}	
	}
	
	private void fillTeamsLabelsAndGoalsTFs() {
		// fill with dummy text
		for (int i = 0; i < jLblsTeams.length; i++) {
			jLblsTeams[i].setText(TEAM_NOT_SET);
			jTFsGoals[i].setText(GOAL_NOT_SET);
		}
		if (belongsToALeague) {
			for (int matchID = 0; matchID < numberOfMatches; matchID++) {
				if (lSeason.isMatchSet(currentMatchday, matchID)) {
					Spiel match = lSeason.getMatch(currentMatchday, matchID);
					jLblsTeams[matchID].setText(lSeason.getTeams()[match.home() - 1].getName());
					jLblsTeams[matchID + numberOfMatches].setText(lSeason.getTeams()[match.away() - 1].getName());
				}
				if (lSeason.isResultSet(currentMatchday, matchID)) {
					setResult(matchID, new Ergebnis(lSeason.getResult(currentMatchday, matchID).toString()));
				}
			}
		} else if (belongsToGroup) {
			for (int matchID = 0; matchID < numberOfMatches; matchID++) {
				if (group.isMatchSet(currentMatchday, matchID)) {
					Spiel match = group.getMatch(currentMatchday, matchID);
					jLblsTeams[matchID].setText(group.getTeams()[match.home() - 1].getName());
					jLblsTeams[matchID + numberOfMatches].setText(group.getTeams()[match.away() - 1].getName());
				}
				if (group.isResultSet(currentMatchday, matchID)) {
					setResult(matchID, new Ergebnis(group.getResult(currentMatchday, matchID).toString()));
				}
			}
		} else if (belongsToKORound) {
			teams = koRound.getTeams();
			for (int matchID = 0; matchID < numberOfMatches; matchID++) {
				if (koRound.isMatchSet(currentMatchday, matchID)) {
					Spiel match = koRound.getMatch(currentMatchday, matchID);
					try {
						jLblsTeams[matchID].setText(teams[match.home() - 1].getName());
						jLblsTeams[matchID + numberOfMatches].setText(teams[match.away() - 1].getName());
					} catch (NullPointerException npe) {
						jLblsTeams[matchID].setText(koRound.getTeamsOrigin(match.home() - 1));
						jLblsTeams[matchID + numberOfMatches].setText(koRound.getTeamsOrigin(match.away() - 1));
					}
					
				}
				if (koRound.isResultSet(currentMatchday, matchID)) {
					setResult(matchID, new Ergebnis(koRound.getResult(currentMatchday, matchID).toString()));
				}
			}
		} else {
			int groupID = 0, matchIDAll = 0;
			for (int matchID = 0; matchID < numberOfMatches; matchID++) {
				Gruppe group = allGroups[groupID];
				jLblsGroups[newOrder[matchID]].setText(("" + alphabet[groupID]).toUpperCase());
				
				if (group.isMatchSet(currentMatchday, matchIDAll)) {
					Spiel match = group.getMatch(currentMatchday, matchIDAll);
					jLblsTeams[newOrder[matchID]].setText(group.getTeams()[match.home() - 1].getName());
					jLblsTeams[newOrder[matchID] + numberOfMatches].setText(group.getTeams()[match.away() - 1].getName());
				}
				if (group.isResultSet(currentMatchday, matchIDAll)) {
					setResult(matchID, new Ergebnis(group.getResult(currentMatchday, matchIDAll).toString()));
					jTFsGoals[newOrder[matchID]].setText("" + results[matchID].home());
					jTFsGoals[newOrder[matchID] + numberOfMatches].setText("" + results[matchID].away());
				}
				
				matchIDAll++;
				if (matchIDAll == numbersOfMatches[groupID]) {
					matchIDAll = 0;
					groupID++;
				}
			}
		}
	}
	
	private void setMatchesInGroupOrder() {
		newOrder = new int[numberOfMatches];
		for (int i = 0; i < newOrder.length; i++) {
			newOrder[i] = i;
		}
		
		fillTeamsLabelsAndGoalsTFs();
	}
	
	public void groupClicked(int index) {
		int oldIndex = oldOrder[index];
		int groupID = 0;
		while (oldIndex > 0) {
			oldIndex -= numbersOfMatches[groupID];
			groupID++;
		}
		if (oldIndex < 0) {
			groupID--;
		}
		Fussball.getInstance().showGroup(groupID);
	}
	
	public void datumsLabelClicked(int index) {
		if (editedDate != -1) {
			message("Das Datum eines anderen Spiels wird bereits gesetzt.");
			return;
		}
		editedDate = index;
		jLblsDates[editedDate].setOpaque(true);
		repaintImmediately(jLblsDates[editedDate]);
		if (isOverview)	editedDate = oldOrder[editedDate];
		
		if (belongsToALeague) {
			MyDateChooser mdc = new MyDateChooser(lSeason, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndKOTIndex(lSeason.getDate(currentMatchday), lSeason.getKOTIndex(currentMatchday, editedDate));
			mdc.setMatch(lSeason, currentMatchday, editedDate);
			
			Fussball.getInstance().toFront();
			mdc.toFront();
		} else if (belongsToGroup) {
			MyDateChooser mdc = new MyDateChooser(group, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(group.getDate(currentMatchday, editedDate), group.getTime(currentMatchday, editedDate));
			mdc.setMatch(group, currentMatchday, editedDate);

			Fussball.getInstance().toFront();
			mdc.toFront();
		} else if (belongsToKORound) {
			MyDateChooser mdc = new MyDateChooser(koRound, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(koRound.getDate(currentMatchday, editedDate), koRound.getTime(currentMatchday, editedDate));
			mdc.setMatch(koRound, currentMatchday, editedDate);

			Fussball.getInstance().toFront();
			mdc.toFront();
		} else {
			// Bestimmung der Gruppe
			int groupID = 0, matchID = oldOrder[index];
			while (matchID > 0) {
				matchID -= numbersOfMatches[groupID];
				groupID++;
			}
			if (matchID < 0) {
				groupID--;
				matchID += numbersOfMatches[groupID];
			}
			Gruppe group = allGroups[groupID];
			
			MyDateChooser mdc = new MyDateChooser(group, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(group.getDate(currentMatchday, matchID), group.getTime(currentMatchday, matchID));
			mdc.setMatch(group, currentMatchday, matchID);

			Fussball.getInstance().toFront();
			mdc.toFront();
		}
	}
	
	public void dateEnteredLeagueStyle(Datum startDate, int KOTindex) {
		lSeason.setDate(currentMatchday, startDate);
		lSeason.setKOTIndex(currentMatchday, editedDate, KOTindex);
		dateChooserClosed();
		fillDates();
	}
	
	public void dateEnteredTournamentStyle(Datum date, Uhrzeit time) {
		if (belongsToGroup) {
			group.setDate(currentMatchday, editedDate, date);
			group.setTime(currentMatchday, editedDate, time);
		} else if (belongsToKORound) {
			koRound.setDate(currentMatchday, editedDate, date);
			koRound.setTime(currentMatchday, editedDate, time);
		} else {
			// Bestimmung der Gruppe
			int groupID = 0, matchID = editedDate;
			while (matchID > 0) {
				matchID -= numbersOfMatches[groupID];
				groupID++;
			}
			if (matchID < 0) {
				groupID--;
				matchID += numbersOfMatches[groupID];
			}
			
			Gruppe group = allGroups[groupID];
			group.setDate(currentMatchday, matchID, date);
			group.setTime(currentMatchday, matchID, time);
		}
		dateChooserClosed();
		fillDates();
	}
	
	public void dateChooserClosed() {
		if (editedDate == -1)	return;
		if (isOverview)	editedDate = newOrder[editedDate];
		jLblsDates[editedDate].setOpaque(false);
		repaintImmediately(jLblsDates[editedDate]);
		editedDate = -1;
	}
	
	private void jBtnsMatchInfosClicked(int index) {
		int offset = 0;
		int matchID = index;
		
		if (isOverview) {
			for (Gruppe group : allGroups) {
				int nOMatches = group.getNumberOfMatchesPerMatchday();
				if ((offset += nOMatches) > oldOrder[index]) {
					matchID = oldOrder[index] - offset + nOMatches;
					competition = group;
					break;
				}
			}
		}
		
		if (!competition.isMatchSet(currentMatchday, matchID)) {
			return;
		}
		
		SpielInformationen matchInformation = new SpielInformationen(this, index, competition.getMatch(currentMatchday, matchID), results[index]);
		matchInformation.setLocationRelativeTo(null);
		matchInformation.setVisible(true);
		openedMatchInfos.add(matchInformation);

		Fussball.getInstance().toFront();
		matchInformation.toFront();
	}
	
	public void saveMatchInfos(SpielInformationen matchInfo, Ergebnis result, int editedResult) {
		if (isOverview)	setResult(oldOrder[editedResult], result);
		else			setResult(editedResult, result);
		openedMatchInfos.remove(matchInfo);
	}
	
	public void mannschaftClicked(int index) {
		jLblsTeams[index].setOpaque(true);
		jLblsTeams[index].setBackground(colorEditing);
		jLblsTeams[index].paintImmediately(0, 0, jLblsTeams[index].getWidth(), jLblsTeams[index].getHeight());
		for (int i = 0; i < jLblsTeams.length; i++) {
			if (i != index) {
				jLblsTeams[i].setBackground(colorEdited);
				jLblsTeams[i].paintImmediately(0, 0, jLblsTeams[i].getWidth(), jLblsTeams[i].getHeight());
			}
		}
		jSPTeamsSelection.setVisible(true);
		editedLabel = index;
		if (isOverview) {
			int hlf = editedLabel % numberOfMatches;
			editedGroupID = 0;
			while (hlf > 0) {
				hlf -= numbersOfMatches[editedGroupID];
				editedGroupID++;
			}
			if (hlf < 0) {
				editedGroupID--;
			}
		}
	}
	
	private void clickNextEmptySpot() {
		int index = -1;
		for (int row = 0; row < array.length && index == -1; row++) {
			for (int column = 0; column < array[row].length; column++) {
				if (array[row][column] == -1) {
					index = column * numberOfMatches + row;
					break;
				}
			}
		}
		if (index != -1)	mannschaftClicked(index);
	}
	
	public void mannschaftenButtonClicked(int index) {
		if (belongsToALeague)		jLblsTeams[editedLabel].setText(lSeason.getTeams()[index].getName());
		else if (belongsToGroup)	jLblsTeams[editedLabel].setText(group.getTeams()[index].getName());
		else if (belongsToKORound) {
			try {
				jLblsTeams[editedLabel].setText(teams[index].getName());
			} catch (NullPointerException npe) {
				jLblsTeams[editedLabel].setText(koRound.getTeamsOrigin(index));
			}
		} else {
			int groupID = 0, teamID = index;
			while (teamID > 0) {
				teamID -= numbersOfTeams[groupID];
				groupID++;
			}
			if (teamID < 0) {
				groupID--;
				teamID += numbersOfTeams[groupID];
			}
			if (editedGroupID != groupID) {
				message("You can't put a team from group " + (alphabet[groupID] + "").toUpperCase()
						+ " in the label of group " + (alphabet[editedGroupID] + "").toUpperCase());
				return;
			}
			
			jLblsTeams[editedLabel].setText(allGroups[groupID].getTeams()[teamID].getName());
		}
		int oldButton = array[editedLabel % numberOfMatches][editedLabel / numberOfMatches] - 1;
		if (oldButton >= 0)	jBtnsMannschaften[oldButton].setEnabled(true);
		
		array[editedLabel % numberOfMatches][editedLabel / numberOfMatches] = index + 1;
		jBtnsMannschaften[index].setEnabled(false);
		jLblsTeams[editedLabel].setBackground(colorEdited);
		editedLabel = -1;
		editedGroupID = -1;
		jSPTeamsSelection.setVisible(false);
		clickNextEmptySpot();
		repaintImmediately(jPnlTeamsSelection);
	}
}
