package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class Spieltag extends JPanel {
	private static final long serialVersionUID = 533273470193095401L;

	private static final int WIDTH_BORDER = 2;
	
	private Start start;
	private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private Color colorRand = new Color(0, 192, 0);
	private Color colorSelection = new Color(16, 255, 16);
	private Color colorEditing = new Color(255, 255, 0);
	private Color colorEdited = new Color(64, 255, 64);
	private Color colorUp = new Color(224, 255, 224);
	private Color colorDown = new Color(96, 255, 96);
	private Color colorDatum = new Color(255, 191, 31);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private Font fontRR = new Font("Dialog", 1, 18);
	
	private JComboBox<String> jCBSpieltage;
	public JLabel[] jLblsMannschaften;
	public JTextField[] jTFsTore;
	private JLabel[] jLblsZusatzInfos;
	private JButton jBtnBearbeiten;
	private JButton jBtnFertig;
	private JButton jBtnPrevious;
	private JButton jBtnNext;
	private JButton[] jBtnsMatchInfos;
	private JButton jBtnResetMatchday;
	private JButton jBtnEnterRueckrunde;
	private JButton jBtnDefaultKickoff;
	private JLabel[] jLblsSpieltagsdaten;
	private JLabel[] jLblsGruppen;
	
	private JPanel jPnlEnterRueckrunde;
	private JLabel jLblRRBeschreibung;
	private JLabel jLblHinrunde;
	private JLabel[] jLblsHinrunde;
	private JLabel jLblRueckrunde;
	private JLabel[] jLblsRueckrunde;
	private JButton jBtnEnterRueckrundeCancel;
	private JButton jBtnEnterRueckrundeCompleted;

	private int[][] array;
	private Ergebnis[] ergebnisse;
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
	
	private int[] buttonsauswahl;
	private int[] labels;
	private int[] textfields;
	private int[] zusInfLabels;
	private int[] moreOptButtons;
	private int[] groupLabels;

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

	private JPanel jPnlTeamsSelection;
	private JButton[] jBtnsMannschaften;

	private int numberOfTeams;
	private int numberOfMatches;
	private int numberOfMatchdays;
	private int halfCountTeamsRoundUp;

	private boolean belongsToALeague = false;
	private boolean belongsToGroup = false;
	private boolean belongsToKORound = false;
	private boolean isOverview = false;
	private boolean changeTFs = true;
	private boolean isETPossible;
	private Wettbewerb wettbewerb;
	private LigaSaison season;
	private Gruppe gruppe;
	private KORunde koRunde;
	private TurnierSaison tSeason;
	private Gruppe[] tGruppen;
	private Mannschaft[] teams;
	
	// Overview
	private int[] numbersOfTeams;
	private int[] numbersOfMatches;
	private int[] oldOrder;
	private int[] newOrder;

	public Spieltag(Start start, LigaSaison season) {
		super();

		this.start = start;
		this.season = season;
		this.wettbewerb = season;
		this.belongsToALeague = true;
		this.belongsToGroup = false;
		this.belongsToKORound = false;
		this.isOverview = false;
		this.isETPossible = season.isETPossible();

		this.numberOfMatches = season.getNumberOfMatchesPerMatchday();
		this.numberOfTeams = season.getNumberOfTeams();
		this.numberOfMatchdays = season.getNumberOfMatchdays();
		this.halfCountTeamsRoundUp = season.getHalbeAnzMSAuf();

		initGUI();
	}

	public Spieltag(Start start, Gruppe gruppe) {
		super();

		this.start = start;
		this.gruppe = gruppe;
		this.wettbewerb = gruppe;
		this.belongsToALeague = false;
		this.belongsToGroup = true;
		this.belongsToKORound = false;
		this.isOverview = false;
		this.isETPossible = gruppe.isETPossible();

		this.numberOfMatches = gruppe.getNumberOfMatchesPerMatchday();
		this.numberOfTeams = gruppe.getNumberOfTeams();
		this.numberOfMatchdays = gruppe.getNumberOfMatchdays();
		this.halfCountTeamsRoundUp = (this.numberOfTeams % 2 == 0 ? this.numberOfTeams / 2 : this.numberOfTeams / 2 + 1);

		initGUI();
	}
	
	public Spieltag(Start start, KORunde koRunde) {
		super();
		
		this.start = start;
		this.koRunde = koRunde;
		this.wettbewerb = koRunde;
		this.belongsToALeague = false;
		this.belongsToGroup = false;
		this.belongsToKORound = true;
		this.isOverview = false;
		this.isETPossible = koRunde.isETPossible();
		
		this.numberOfMatches = this.koRunde.getNumberOfMatchesPerMatchday();
		this.numberOfTeams = 2 * numberOfMatches;
		this.numberOfMatchdays = this.koRunde.hasSecondLeg() ? 2 : 1;
		this.halfCountTeamsRoundUp = this.numberOfTeams / 2;
		
		initGUI();
	}
	
	public Spieltag(Start start, TurnierSaison tSeason, boolean isQ) {
		super();
		
		this.start = start;
		this.tSeason = tSeason;
		this.tGruppen = isQ ? tSeason.getQGruppen() : tSeason.getGruppen();
		this.belongsToALeague = false;
		this.belongsToGroup = false;
		this.belongsToKORound = false;
		this.isOverview = true;
		this.isETPossible = tSeason.isETPossible();
		
		this.numbersOfTeams = new int[isQ ? tSeason.getNumberOfQGroups() : tSeason.getNumberOfGroups()];
		this.numbersOfMatches = new int[isQ ? tSeason.getNumberOfQGroups() : tSeason.getNumberOfGroups()];
		
		for (Gruppe gruppe : tGruppen) {
			int nOMatchdays = gruppe.getNumberOfMatchdays();
			this.numbersOfTeams[gruppe.getID()] = gruppe.getNumberOfTeams();
			this.numbersOfMatches[gruppe.getID()] = gruppe.getNumberOfMatchesPerMatchday();
			
			this.numberOfMatches += this.numbersOfMatches[gruppe.getID()];
			this.numberOfTeams += this.numbersOfTeams[gruppe.getID()];
			this.numberOfMatchdays = (this.numberOfMatchdays > nOMatchdays ? this.numberOfMatchdays : nOMatchdays);
		}
		this.halfCountTeamsRoundUp = (this.numberOfTeams + 1) / 2;
		
		initGUI();
	}

	private void calculateButtonsauswahlBounds(int maxHeight) {
		buttonsauswahl = new int[6];
		buttonsauswahl[STARTX] = 10;
		buttonsauswahl[STARTY] = 10;
		buttonsauswahl[GAPX] = 5;
		buttonsauswahl[GAPY] = 5;
		buttonsauswahl[SIZEX] = 200;
		buttonsauswahl[SIZEY] = 50;
		
		if ((2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]) > maxHeight) {
			buttonsauswahl[GAPY] = 1;
			buttonsauswahl[SIZEY] = (maxHeight - (halfCountTeamsRoundUp + 1) * buttonsauswahl[GAPY]) / halfCountTeamsRoundUp;
		}
	}
	
	private void calculateBounds() {
		int zusInfWidth = 35;
		int height = 25, gapy = 2;
		if(numberOfMatches > 26) {
			height = 24;
			gapy = -2;
		}
		groupLabels = new int[] {160, 80, 0, gapy, 10, height};
		
		labels = new int[] { groupLabels[STARTX] + groupLabels[SIZEX] + 5, 80, (isETPossible ? zusInfWidth + 145 : 135), gapy, 180, height };

		textfields = new int[] { labels[STARTX] + labels[SIZEX] + 10, labels[STARTY], 
				labels[GAPX] - 2 * 10 - 2 * 40 - 30 - (isETPossible ? zusInfWidth + 15 : 5), labels[GAPY], 40, labels[SIZEY] };
		
		zusInfLabels = new int[] { labels[STARTX] + labels[SIZEX] + 95, labels[STARTY], 0, labels[GAPY], (isETPossible ? zusInfWidth : 0), labels[SIZEY] };
		
		moreOptButtons = new int[] { labels[STARTX] + labels[SIZEX] + labels[GAPX] - 45, labels[STARTY], 0, labels[GAPY], 40, labels[SIZEY] };
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);

			// TODO min und maxheight sinnvoll setzen
			int minimumheight = 450;
			int maximumheight = 800 - 2 * WIDTH_BORDER;
			calculateButtonsauswahlBounds(maximumheight);
			calculateBounds();

			Dimension dim = new Dimension();
			dim.width = 1200;

			int heightOfTeamLabels = labels[STARTY] + numberOfMatches * (labels[SIZEY] + labels[GAPY]) + 20;
			int heightOfTeamSelection = 2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY] + 2 * WIDTH_BORDER;

			dim.height = (heightOfTeamLabels > heightOfTeamSelection ? heightOfTeamLabels : heightOfTeamSelection);

			// correction to minimumheight or maximumheight if out of these bounds
			if (dim.height < minimumheight) {
				dim.height = minimumheight;
			} else if (dim.height > maximumheight) {
				dim.height = maximumheight;
			}

			this.setSize(dim);

			ergebnisse = new Ergebnis[numberOfMatches];
			
			jLblsGruppen = new JLabel[numberOfMatches];
			jLblsZusatzInfos = new JLabel[numberOfMatches];
			jBtnsMatchInfos = new  JButton[numberOfMatches];
			jBtnsMannschaften = new JButton[numberOfTeams];
			jLblsMannschaften = new JLabel[2 * numberOfMatches];
			jTFsTore = new JTextField[2 * numberOfMatches];
			array = new int[numberOfMatches][2];
			jLblsSpieltagsdaten = new JLabel[numberOfMatches];
			jLblsHinrunde = new JLabel[numberOfMatchdays / 2];
			jLblsRueckrunde = new JLabel[numberOfMatchdays / 2];
			matchdaysHinrunde = new ArrayList<>();
			matchdaysRueckrunde = new ArrayList<>();
			
			{
				String[] hilfsarray = new String[numberOfMatchdays];
				for (int i = 0; i < numberOfMatchdays; i++) {
					hilfsarray[i] = (i + 1) + ". Spieltag";
				}
				jCBSpieltage = new JComboBox<>();
				this.add(jCBSpieltage);
				jCBSpieltage.setModel(new DefaultComboBoxModel<>(hilfsarray));
				jCBSpieltage.setBounds(REC_COMBO);
				jCBSpieltage.setFocusable(false);
				jCBSpieltage.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						jCBSpieltageItemStateChanged(evt);
					}
				});
			}
			
			for (int i = 0; i < jLblsSpieltagsdaten.length; i++) {
				final int x = i;
				jLblsSpieltagsdaten[i] = new JLabel();
				this.add(jLblsSpieltagsdaten[i]);
				jLblsSpieltagsdaten[i].setBounds(30, labels[STARTY] + i * (labels[SIZEY] + labels[GAPY]), 120, labels[SIZEY]);
				jLblsSpieltagsdaten[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				jLblsSpieltagsdaten[i].setBackground(colorDatum);
				jLblsSpieltagsdaten[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						datumsLabelClicked(x);
						jLblsMannschaften[x].setBorder(null);
						jLblsMannschaften[x + numberOfMatches].setBorder(null);
						jLblsSpieltagsdaten[x].setBorder(null);
					}
					
					public void mouseEntered(MouseEvent e) {
						if (editedDate == -1) {
							jLblsMannschaften[x].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsMannschaften[x + numberOfMatches].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsSpieltagsdaten[x].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsSpieltagsdaten[x].setOpaque(true);
							repaintImmediately(jLblsSpieltagsdaten[x]);
						}
					}

					public void mouseExited(MouseEvent e) {
						if (editedDate == -1) {
							jLblsMannschaften[x].setBorder(null);
							jLblsMannschaften[x + numberOfMatches].setBorder(null);
							jLblsSpieltagsdaten[x].setBorder(null);
							jLblsSpieltagsdaten[x].setOpaque(false);
							repaintImmediately(jLblsSpieltagsdaten[x]);
						}
					}
				});
			}
			if (isOverview) {
				for (int i = 0; i < jLblsGruppen.length; i++) {
					final int x = i;
					jLblsGruppen[i] = new JLabel();
					this.add(jLblsGruppen[i]);
					jLblsGruppen[i].setBounds(groupLabels[STARTX], groupLabels[STARTY] + i * (labels[SIZEY] + labels[GAPY]), groupLabels[SIZEX], groupLabels[SIZEY]);
					alignRight(jLblsGruppen[i]);
					jLblsGruppen[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
					jLblsGruppen[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							gruppeClicked(x);
						}
					});
				}
			}
			
			for (int i = 0; i < jLblsMannschaften.length; i++) {
				final int x = i;
				int zeile = i % numberOfMatches;
				int spalte = i / numberOfMatches;

				jLblsMannschaften[i] = new JLabel();
				this.add(jLblsMannschaften[i]);
				jLblsMannschaften[i].setBounds(labels[STARTX] + spalte * (labels[SIZEX] + labels[GAPX]), 
						labels[STARTY] + zeile * (labels[SIZEY] + labels[GAPY]), labels[SIZEX], labels[SIZEY]);
				if (spalte == 0)	alignRight(jLblsMannschaften[i]);
				else				alignLeft(jLblsMannschaften[i]);
				jLblsMannschaften[i].setEnabled(false);
				jLblsMannschaften[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				jLblsMannschaften[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (editingMatches) {
							mannschaftClicked(x);
							jLblsMannschaften[x % numberOfMatches].setBorder(null);
							jLblsMannschaften[x % numberOfMatches + numberOfMatches].setBorder(null);
						}
					}
					
					public void mouseEntered(MouseEvent e) {
						if (editingMatches) {
							jLblsMannschaften[x % numberOfMatches].setBorder(BorderFactory.createDashedBorder(getForeground()));
							jLblsMannschaften[x % numberOfMatches + numberOfMatches].setBorder(BorderFactory.createDashedBorder(getForeground()));
						}
					}
					
					public void mouseExited(MouseEvent e) {
						if (editingMatches) {
							jLblsMannschaften[x % numberOfMatches].setBorder(null);
							jLblsMannschaften[x % numberOfMatches + numberOfMatches].setBorder(null);
						}
					}
				});
			}
			
			for (int i = 0; i < jTFsTore.length; i++) {
				final int x = i;
				jTFsTore[i] = new JTextField();
				this.add(jTFsTore[i]);
				jTFsTore[i].setBounds(textfields[STARTX] + (i / numberOfMatches) * (textfields[SIZEX] + textfields[GAPX]), 
						textfields[STARTY] + (i % numberOfMatches) * (textfields[SIZEY] + textfields[GAPY]), textfields[SIZEX], textfields[SIZEY]);
				alignCenter(jTFsTore[i]);
				jTFsTore[i].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if (arg0.getKeyChar() == 8) {
							aValidKeyWasPressed(x, arg0);
						} else if ((jTFsTore[x].getText().length() >= 2 && !jTFsTore[x].getText().equals("-1")) || arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						} else {
							aValidKeyWasPressed(x, arg0);
						}
					}
				});
				jTFsTore[i].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent arg0) {
						jTFsTore[x].selectAll();
					}
				});
			}
			for (int i = 0; i < jBtnsMatchInfos.length; i++) {
				final int x = i;
				jBtnsMatchInfos[i] = new JButton();
				this.add(jBtnsMatchInfos[i]);
				jBtnsMatchInfos[i].setBounds(moreOptButtons[STARTX], moreOptButtons[STARTY] + i * (moreOptButtons[SIZEY] + moreOptButtons[GAPY]), 
						moreOptButtons[SIZEX], moreOptButtons[SIZEY]);
				jBtnsMatchInfos[i].setText("+");
				jBtnsMatchInfos[i].setToolTipText("Reset this result.");
				jBtnsMatchInfos[i].setFocusable(false);
				jBtnsMatchInfos[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnsMatchInfosClicked(x);
					}
				});
			}
			for (int i = 0; i < jLblsZusatzInfos.length; i++) {
				jLblsZusatzInfos[i] = new JLabel();
				this.add(jLblsZusatzInfos[i]);
				jLblsZusatzInfos[i].setBounds(zusInfLabels[STARTX], zusInfLabels[STARTY] + i * (zusInfLabels[SIZEY] + zusInfLabels[GAPY]), zusInfLabels[SIZEX], zusInfLabels[SIZEY]);
			}
			{
				jBtnBearbeiten = new JButton();
				this.add(jBtnBearbeiten);
				jBtnBearbeiten.setBounds(REC_EDITSAVE);
				jBtnBearbeiten.setText("Spielplan bearbeiten");
				jBtnBearbeiten.setFocusable(false);
				jBtnBearbeiten.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnBearbeitenActionPerformed();
					}
				});
			}
			{
				jBtnFertig = new JButton();
				this.add(jBtnFertig);
				jBtnFertig.setBounds(REC_EDITSAVE);
				jBtnFertig.setText("Speichern");
				jBtnFertig.setFocusable(false);
				jBtnFertig.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnFertigActionPerformed();
					}
				});
				jBtnFertig.setVisible(false);
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
				jBtnEnterRueckrunde = new JButton();
				this.add(jBtnEnterRueckrunde);
				jBtnEnterRueckrunde.setBounds(REC_BTNRRUNDE);
				jBtnEnterRueckrunde.setText("Rueckrunde");
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
			REC_LBLRRDESCR = new Rectangle(INDENT, INDENT, 530, 20);
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
				jLblRRBeschreibung = new JLabel();
				jPnlEnterRueckrunde.add(jLblRRBeschreibung);
				jLblRRBeschreibung.setBounds(REC_LBLRRDESCR);
				jLblRRBeschreibung.setText("Klicken die auf die Spieltage in der Reihenfolge, wie sie in der Rueckrunde gespielt werden.");
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
				jLblRueckrunde.setText("Rueckrunde");
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
				jBtnDefaultKickoff.setText("Standardanstosszeiten");
				jBtnDefaultKickoff.setFocusable(false);
				jBtnDefaultKickoff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnDefaultKickoffActionPerformed();
					}
				});
			}
			
			{
				jPnlTeamsSelection = new JPanel();
				this.add(jPnlTeamsSelection);
				jPnlTeamsSelection.setLayout(null);
				jPnlTeamsSelection.setSize(2 * (buttonsauswahl[STARTX] + buttonsauswahl[SIZEX]) + buttonsauswahl[GAPX], 2 * buttonsauswahl[STARTY]
						+ halfCountTeamsRoundUp * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]);
				jPnlTeamsSelection.setLocation(this.getSize().width - jPnlTeamsSelection.getSize().width - WIDTH_BORDER, (this.getSize().height - jPnlTeamsSelection.getSize().height) / 2);
				jPnlTeamsSelection.setVisible(false);
				jPnlTeamsSelection.setOpaque(true);
				jPnlTeamsSelection.setBackground(colorSelection);
			}

			for (int i = 0; i < numberOfTeams; i++) {
				final int x = i;
				int xfactor = i % 2, yfactor = i / 2;
				jBtnsMannschaften[i] = new JButton();
				jPnlTeamsSelection.add(jBtnsMannschaften[i]);
				jBtnsMannschaften[i].setBounds(buttonsauswahl[STARTX] + xfactor * (buttonsauswahl[SIZEX] + buttonsauswahl[GAPX]),
								buttonsauswahl[STARTY] + yfactor * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]), buttonsauswahl[SIZEX], buttonsauswahl[SIZEY]);
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
		
		
		// Hintergrund
		for (int i = WIDTH_BORDER; i < this.getHeight() - WIDTH_BORDER; i++) {
			g.setColor(new Color(colorUp.getRed() + ((colorDown.getRed() - colorUp.getRed()) * i / (this.getHeight() - WIDTH_BORDER)), 
									colorUp.getGreen() + ((colorDown.getGreen() - colorUp.getGreen()) * i / (this.getHeight() - WIDTH_BORDER)), 
									colorUp.getBlue() + ((colorDown.getBlue() - colorUp.getBlue()) * i / (this.getHeight() - WIDTH_BORDER))));
			g.drawLine(WIDTH_BORDER, i, this.getWidth() - WIDTH_BORDER - 1, i);
		}
	}

	public int getNumberOfMatches() {
		return this.numberOfMatches;
	}
	
	public int getCurrentMatchday() {
		return this.currentMatchday;
	}

	public void resetCurrentMatchday() {
		this.currentMatchday = -1;
	}
	
	public int getEditedMatchday() {
		return this.editedMatchday;
	}
	
	public Ergebnis getErgebnis(int match) {
		return ergebnisse[match];
	}

	public Ergebnis getErgebnis(int groupID, int match) {
		for (int i = 0; i < groupID; i++) {
			match += this.numbersOfMatches[i];
		}
		return ergebnisse[match];
	}
	
	private void setErgebnis(int match, Ergebnis result) {
		ergebnisse[match] = result;
		
		if (changeTFs) {
			int index = newOrder != null ? newOrder[match] : match;
			jLblsZusatzInfos[index].setText("");
			
			if (result != null) {
				jTFsTore[index].setText("" + result.home());
				jTFsTore[index + numberOfMatches].setText("" + result.away());
				jLblsZusatzInfos[index].setText(result.getMore());
				jLblsZusatzInfos[index].setToolTipText(result.getTooltipText());
			} else {
				jTFsTore[index].setText("-1");
				jTFsTore[index + numberOfMatches].setText("-1");
			}
		}
	}
	
	public void showMatchday(int matchday) {
		jCBSpieltage.setSelectedIndex(matchday);
	}
	
	public void ensureNoOpenedMatchInfos() {
		while (openedMatchInfos.size() > 0) {
			openedMatchInfos.get(0).goActionPerformed();
		}
	}

	private void jCBSpieltageItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			ensureNoOpenedMatchInfos();
			if (belongsToALeague) {
				season.ergebnisseSichern();
				spieltagAnzeigen();
			} else if (belongsToGroup) {
				gruppe.ergebnisseSichern();
				spieltagAnzeigen();
			} else if (belongsToKORound) {
				koRunde.ergebnisseSichern();
				spieltagAnzeigen();
			} else {
				tSeason.ergebnisseSichern();
				spieltagAnzeigen();
			}
		}
	}
	
	private void aValidKeyWasPressed(int indexOfTF, KeyEvent arg0) {
		String selText = jTFsTore[indexOfTF].getSelectedText();
		String otherGoals = jTFsTore[(indexOfTF + numberOfMatches) % (2 * numberOfMatches)].getText();
		String newContent = "";
		
		if (arg0.getKeyChar() == 8) {
			if (jTFsTore[indexOfTF].getText().length() == 0 || jTFsTore[indexOfTF].getText().equals("-")) {
				jTFsTore[indexOfTF].setText("-1");
				jTFsTore[indexOfTF].selectAll();
			}
			newContent = jTFsTore[indexOfTF].getText();
		} else {
			if (selText != null) 	newContent = jTFsTore[indexOfTF].getText().replaceAll(selText, "");
			else					newContent = jTFsTore[indexOfTF].getText();
			newContent += arg0.getKeyChar();
		}
		
		int[] goals = new int[2];
		goals[indexOfTF / numberOfMatches] = Integer.parseInt(newContent);
		goals[1 - indexOfTF / numberOfMatches] = Integer.parseInt(otherGoals);
		
		Ergebnis result;
		if (goals[0] == -1 || goals[1] == -1)	result = null;
		else									result = new Ergebnis(goals[0], goals[1]);
		changeTFs = false;
		if (isOverview)	setErgebnis(oldOrder[indexOfTF % numberOfMatches], result);
		else			setErgebnis(indexOfTF % numberOfMatches, result);
		changeTFs = true;
	}
	
	private void setLabelsEnabled(boolean bool) {
		for (JLabel lbl : jLblsMannschaften) {
			lbl.setEnabled(bool);
			lbl.setOpaque(false);
		}
	}
	
	private void disableTFs() {
		for (JTextField tf : jTFsTore)	tf.setEditable(false);
	}
	
	private void setTFsEditableFromRepresentation() {
		String representation;
		if (belongsToALeague)		representation = season.getSpielplanRepresentation(currentMatchday);
		else if (belongsToGroup)	representation = gruppe.getSpielplanRepresentation(currentMatchday);
		else if (belongsToKORound)	representation = koRunde.getSpielplanRepresentation(currentMatchday);
		else {
			representation = "";
			for (Gruppe gruppe : tGruppen) {
				representation += gruppe.getSpielplanRepresentation(currentMatchday);
			}
		}
		
		for (int match = 0; match < this.numberOfMatches; match++) {
			if (representation.charAt(match) == 't') {
				jTFsTore[match].setEditable(true);
				jTFsTore[match + numberOfMatches].setEditable(true);
			} else if (representation.charAt(match) == 'f') {
				jTFsTore[match].setEditable(false);
				jTFsTore[match + numberOfMatches].setEditable(false);
			}
		}
		for (int match = 0; match < this.numberOfMatches; match++) {
			if (jTFsTore[match].isEditable()) {
				jTFsTore[match].selectAll();
				break;
			}
		}
	}
	
	private void spieltagsdatenBefuellenGroupOrder() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		int groupID = 0, matchID = 0;
		for (int i = 0; i < numberOfMatches; i++) {
			dateandtimeofmatches[i] = tGruppen[groupID].getDateAndTime(currentMatchday, matchID);
			jLblsSpieltagsdaten[i].setText(dateandtimeofmatches[i]);
			
			matchID++;
			if (matchID == numbersOfMatches[groupID]) {
				matchID = 0;
				groupID++;
			}
		}
		for (int i = jLblsSpieltagsdaten.length - 1; i > 0; i--) {
			if (jLblsSpieltagsdaten[i].getText().equals(jLblsSpieltagsdaten[i - 1].getText())) {
				jLblsSpieltagsdaten[i].setText("");
			}
		}
	}

	private void spieltagsdatenBefuellen() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		int groupID = 0, matchID = 0;
		for (int i = 0; i < numberOfMatches; i++) {
			if (belongsToALeague)		dateandtimeofmatches[i] = season.getDateAndTime(currentMatchday, i);
			else if (belongsToGroup)	dateandtimeofmatches[i] = gruppe.getDateAndTime(currentMatchday, i);
			else if (belongsToKORound)	dateandtimeofmatches[i] = koRunde.getDateAndTime(currentMatchday, i);
			else {
				dateandtimeofmatches[i] = tGruppen[groupID].getDateAndTime(currentMatchday, matchID);
				
				matchID++;
				if (matchID == numbersOfMatches[groupID]) {
					matchID = 0;
					groupID++;
				}
			}
			if (isOverview)	jLblsSpieltagsdaten[newOrder[i]].setText(dateandtimeofmatches[i]);
			else			jLblsSpieltagsdaten[i].setText(dateandtimeofmatches[i]);
		}
		for (int i = jLblsSpieltagsdaten.length - 1; i > 0; i--) {
			if (jLblsSpieltagsdaten[i].getText().equals(jLblsSpieltagsdaten[i - 1].getText())) {
				jLblsSpieltagsdaten[i].setText("");
			}
		}
	}

	private void setMannschaftenButtonsNames() {
		int groupID = 0, teamID = 0;
		for (int i = 0; i < jBtnsMannschaften.length; i++) {
			if (belongsToALeague)		jBtnsMannschaften[i].setText(season.getMannschaften()[i].getName());
			else if (belongsToGroup)	jBtnsMannschaften[i].setText(gruppe.getMannschaften()[i].getName());
			else if (belongsToKORound)	{
				try {
					jBtnsMannschaften[i].setText(teams[i].getName());
				} catch (NullPointerException npe) {
					jBtnsMannschaften[i].setText(koRunde.getTeamsOrigin(i));
				}
			} else {
				jBtnsMannschaften[i].setText(tGruppen[groupID].getMannschaften()[teamID].getName());
				
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
	private void jBtnBearbeitenActionPerformed() {
		editingMatches = true;
		editedMatchday = jCBSpieltage.getSelectedIndex();
		
		setMannschaftenButtonsNames();
		
		if (isOverview) {
			setMatchesInGroupOrder();
			spieltagsdatenBefuellenGroupOrder();
		}
		
		Spiel spiel;
		for (int i = 0; i < array.length; i++) {
			if (belongsToALeague)		spiel = season.getSpiel(editedMatchday, i);
			else if (belongsToGroup)	spiel = gruppe.getSpiel(editedMatchday, i);
			else if (belongsToKORound)	spiel = koRunde.getSpiel(editedMatchday, i);
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
				
				spiel = tGruppen[groupID].getSpiel(editedMatchday, matchID);
			}
			
			if (spiel != null) {
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
					
					array[i][0] = spiel.home() + teamsDiff;
					array[i][1] = spiel.away() + teamsDiff;
				} else {
					array[i][0] = spiel.home();
					array[i][1] = spiel.away();
				}
			} else {
				array[i][0] = -1;
				array[i][1] = -1;
			}
		}
		
		for (int i = 0; i < jBtnsMannschaften.length; i++) {
			jBtnsMannschaften[i].setEnabled(true);
		}
		setLabelsEnabled(true);
		disableTFs();
		jBtnBearbeiten.setVisible(false);
		jBtnFertig.setVisible(true);
		jCBSpieltage.setEnabled(false);
		jBtnPrevious.setEnabled(false);
		jBtnNext.setEnabled(false);
		jBtnResetMatchday.setVisible(false);
		jBtnEnterRueckrunde.setVisible(false);
		if (jBtnDefaultKickoff != null)	jBtnDefaultKickoff.setVisible(false);
	}

	/**
	 * Optimized for <code>Gruppe</code> and <code>KORunde</code>, setMatch is not called for KORunde
	 * @return if the content was saved: 0 if yes, 1 if not
	 */
	public int jBtnFertigActionPerformed() {
		int fehlerart = -1;
		// 1. Fehlerfall: es befinden sich noch ungesetzte Felder im Array
		for (int i = 0; i < array.length; i++) {
			if (array[i][0] == -1) {
				fehlerart = 1;
				break;
			} else if (array[i][1] == -1) {
				fehlerart = 1;
				break;
			}
		}
		
		int saveanyway = 0;
		if (fehlerart == 1) {
			saveanyway = JOptionPane.showConfirmDialog(null, "Es fehlen noch Spiele. \nTrotzdem fortfahren?", "Warnung", JOptionPane.YES_NO_OPTION);
		}
		
		if (saveanyway == 0) {
			if (belongsToKORound)	koRunde.setCheckTeamsFromPreviousRound(false);
			int groupID = 0, matchID = 0, offset = 0, home, away;
			for (int match = 0; match < array.length; match++) {
				if (isOverview)	wettbewerb = tGruppen[groupID];
				Spiel spiel = null, vergleich;
				
				vergleich = wettbewerb.getSpiel(editedMatchday, matchID);
				
				if ((home = array[match][0]) != -1 && (away = array[match][1]) != -1) {
					spiel = new Spiel(wettbewerb, editedMatchday, wettbewerb.getDate(editedMatchday, matchID), 
								wettbewerb.getTime(editedMatchday, matchID), home - offset, away - offset);
				}
				
				if (spiel != null && vergleich != null && spiel.sameAs(vergleich))	spiel = vergleich;
				
				wettbewerb.setSpiel(editedMatchday, matchID, spiel);
				
				matchID++;
				if (isOverview && matchID == numbersOfMatches[groupID]) {
					offset += 2 * numbersOfMatches[groupID];
					matchID = 0;
					groupID++;
				}
			}
			if (belongsToKORound)	koRunde.setCheckTeamsFromPreviousRound(true);
			
			editedMatchday = -1;
			
			setLabelsEnabled(false);
			setTFsEditableFromRepresentation();
			jBtnFertig.setVisible(false);
			jPnlTeamsSelection.setVisible(false);
			
			jBtnBearbeiten.setVisible(true);
			jCBSpieltage.setEnabled(true);
			jBtnPrevious.setEnabled(true);
			jBtnNext.setEnabled(true);
			jBtnResetMatchday.setVisible(true);
			jBtnEnterRueckrunde.setVisible(true);
			if (jBtnDefaultKickoff != null)	jBtnDefaultKickoff.setVisible(true);
			editingMatches = false;
			spieltagAnzeigen();
		}
		return saveanyway;
	}
	
	private void previousMatchday() {
		jCBSpieltage.setSelectedIndex(currentMatchday - 1);
	}
	
	private void nextMatchday() {
		jCBSpieltage.setSelectedIndex(currentMatchday + 1);
	}
	
	private void resetMatchdayActionPerformed() {
		if (yesNoDialog("Do you really want to reset this matchday? This is irrevocable.") == JOptionPane.NO_OPTION) return;
		
		if (belongsToALeague) {
			for (int match = 0; match < season.getNumberOfMatchesPerMatchday(); match++) {
				season.setSpiel(currentMatchday, match, null);
			}
			
			for (int team = 0; team < season.getNumberOfTeams(); team++) {
				season.getMannschaften()[team].resetMatch(currentMatchday);
			}
			
			season.ergebnisseSichern();
			spieltagAnzeigen();
		} else if (belongsToGroup) {
			for (int match = 0; match < gruppe.getNumberOfMatchesPerMatchday(); match++) {
				gruppe.setSpiel(currentMatchday, match, null);
			}
			
			for (int team = 0; team < gruppe.getNumberOfTeams(); team++) {
				gruppe.getMannschaften()[team].resetMatch(currentMatchday);
			}
			
			gruppe.ergebnisseSichern();
			spieltagAnzeigen();
		} else if (belongsToKORound) {
			for (int match = 0; match < koRunde.getNumberOfMatchesPerMatchday(); match++) {
				koRunde.setSpiel(currentMatchday, match, null);
			}
			
			koRunde.setSpielplanFullyEntered(currentMatchday, false);
			koRunde.ergebnisseSichern();
			spieltagAnzeigen();
		} else {
			for (Gruppe gruppe : tGruppen) {
				for (int match = 0; match < gruppe.getNumberOfMatchesPerMatchday(); match++) {
					gruppe.setSpiel(currentMatchday, match, null);
				}
				
				for (int team = 0; team < gruppe.getNumberOfTeams(); team++) {
					gruppe.getMannschaften()[team].resetMatch(currentMatchday);
				}
			}
			
			tSeason.ergebnisseSichern();
			spieltagAnzeigen();
		}
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
		season.setRueckrundeToOrder(rueckrundeOrder);
		spieltagAnzeigen();
	}
	
	private void showRueckrundePanel(boolean showRRPanel) {
		jPnlEnterRueckrunde.setVisible(showRRPanel);
		
		for (JTextField tf : jTFsTore)			tf.setVisible(!showRRPanel);
		for (JLabel lbl : jLblsMannschaften)	lbl.setVisible(!showRRPanel);
		for (JLabel lbl : jLblsSpieltagsdaten)	lbl.setVisible(!showRRPanel);
		for (JButton btn : jBtnsMatchInfos)	btn.setVisible(!showRRPanel);
		
		jBtnBearbeiten.setVisible(!showRRPanel);
		jBtnResetMatchday.setVisible(!showRRPanel);
		jBtnEnterRueckrunde.setVisible(!showRRPanel);
		if (jBtnDefaultKickoff != null)	jBtnDefaultKickoff.setVisible(!showRRPanel);
	}

	private void jBtnDefaultKickoffActionPerformed() {
		if (belongsToALeague) {
			season.useDefaultKickoffTimes(currentMatchday);
		} else if (belongsToGroup) {
			message("Nicht vorgesehen fuer Gruppe");
		} else if (belongsToKORound) {
			message("Nicht vorgesehen fuer KORunde");
		} else {
			message("Nicht vorgesehen fuer Gruppenuebersicht");
		}
		spieltagsdatenBefuellen();
	}

	private void changeOrderToChronological(int matchday) {
		if (belongsToALeague)		season.changeOrderToChronological(matchday);
		else if (belongsToGroup)	gruppe.changeOrderToChronological(matchday);
		else if (belongsToKORound)	koRunde.changeOrderToChronological(matchday);
		else if (isOverview) {
			oldOrder = tSeason.getChronologicalOrder(matchday); // beinhaltet die alten Indizes in der neuen Reihenfolge
			newOrder = new int[oldOrder.length]; // beinhaltet die neuen Indizes in der alten Reihenfolge
			for (int i = 0; i < oldOrder.length; i++) {
				newOrder[oldOrder[i]] = i; // if enabled
//				oldOrder[i] = i; // if disabled
//				newOrder[i] = i; // if disabled
			}
		}
	}

	public void spieltagAnzeigen() {
		if (currentMatchday == -1) {
			if (belongsToALeague)		currentMatchday = season.getCurrentMatchday();
			else if (belongsToGroup)	currentMatchday = gruppe.getCurrentMatchday();
			else if (belongsToKORound)	currentMatchday = koRunde.getCurrentMatchday();
			else 						currentMatchday = tSeason.getCurrentMatchday();
			
			// damit nicht bei setSelectedIndex die default-Inhalte von tore in das ergebnis-Array kopiert werden muss das Befuellen davor erfolgen
			for (int match = 0; match < numberOfMatches; match++) {
				try {
					if (belongsToALeague) {
						setErgebnis(match, new Ergebnis(season.getErgebnis(currentMatchday, match).toString()));
					} else if (belongsToGroup) {
						setErgebnis(match, new Ergebnis(gruppe.getErgebnis(currentMatchday, match).toString()));
					} else if (belongsToKORound) {
						setErgebnis(match, new Ergebnis(koRunde.getErgebnis(currentMatchday, match).toString()));
					} else {
						int groupID = 0, matchID = match;
						while (matchID > 0) {
							matchID -= numbersOfMatches[groupID];
							groupID++;
						}
						if (matchID < 0) {
							groupID--;
							matchID += numbersOfMatches[groupID];
						}
						setErgebnis(match, new Ergebnis(tGruppen[groupID].getErgebnis(currentMatchday, matchID).toString()));
					}
				} catch (NullPointerException e) {
					setErgebnis(match, null);
				}
			}
			
			if (currentMatchday == jCBSpieltage.getSelectedIndex()) {
				// dann gibt es keinen ItemStateChange und die Methode wird nicht aufgerufen
				spieltagAnzeigen();
			}
			
			jCBSpieltage.setSelectedIndex(currentMatchday);
		} else {
			currentMatchday = jCBSpieltage.getSelectedIndex();
			
			if (currentMatchday - 1 < 0)	jBtnPrevious.setEnabled(false);
			else							jBtnPrevious.setEnabled(true);
			if (currentMatchday + 1 == this.numberOfMatchdays)		jBtnNext.setEnabled(false);
			else													jBtnNext.setEnabled(true);
			
			changeOrderToChronological(currentMatchday);
			
			spieltagsdatenBefuellen();
			
			ergebnisse = new Ergebnis[numberOfMatches];
			
			fillTeamsLabelsAndGoalsTFs(currentMatchday);
			setTFsEditableFromRepresentation();
			
			jBtnEnterRueckrunde.setVisible(false);
			if (belongsToALeague && currentMatchday * 2 == numberOfMatchdays)	jBtnEnterRueckrunde.setVisible(true);
		}	
	}
	
	private void fillTeamsLabelsAndGoalsTFs(int matchday) {
		// fill with dummy text
		for (int i = 0; i < this.jLblsMannschaften.length; i++) {
			this.jLblsMannschaften[i].setText("n. a.");
			this.jTFsTore[i].setText("-1");
		}
		if (belongsToALeague) {
			for (int match = 0; match < numberOfMatches; match++) {
				if (season.isSpielplanEntered(matchday, match)) {
					Spiel spiel = season.getSpiel(matchday, match);
					this.jLblsMannschaften[match].setText(season.getMannschaften()[spiel.home() - 1].getName());
					this.jLblsMannschaften[match + numberOfMatches].setText(season.getMannschaften()[spiel.away() - 1].getName());
				}
				if (season.isErgebnisplanEntered(matchday, match)) {
					setErgebnis(match, new Ergebnis(season.getErgebnis(matchday, match).toString()));
				}
			}
		} else if (belongsToGroup) {
			for (int match = 0; match < numberOfMatches; match++) {
				if (gruppe.isSpielplanEntered(matchday, match)) {
					Spiel spiel = gruppe.getSpiel(matchday, match);
					this.jLblsMannschaften[match].setText(gruppe.getMannschaften()[spiel.home() - 1].getName());
					this.jLblsMannschaften[match + numberOfMatches].setText(gruppe.getMannschaften()[spiel.away() - 1].getName());
				}
				if (gruppe.isErgebnisplanEntered(matchday, match)) {
					setErgebnis(match, new Ergebnis(gruppe.getErgebnis(matchday, match).toString()));
				}
			}
		} else if (belongsToKORound) {
			teams = koRunde.getMannschaften();
			for (int match = 0; match < numberOfMatches; match++) {
				if (koRunde.isSpielplanEntered(matchday, match)) {
					Spiel spiel = koRunde.getSpiel(matchday, match);
					try {
						this.jLblsMannschaften[match].setText(teams[spiel.home() - 1].getName());
						this.jLblsMannschaften[match + numberOfMatches].setText(teams[spiel.away() - 1].getName());
					} catch (NullPointerException npe) {
						this.jLblsMannschaften[match].setText(koRunde.getTeamsOrigin(spiel.home() - 1));
						this.jLblsMannschaften[match + numberOfMatches].setText(koRunde.getTeamsOrigin(spiel.away() - 1));
					}
					
				}
				if (koRunde.isErgebnisplanEntered(matchday, match)) {
					setErgebnis(match, new Ergebnis(koRunde.getErgebnis(matchday, match).toString()));
				}
			}
		} else {
			int groupID = 0, matchID = 0;
			for (int match = 0; match < numberOfMatches; match++) {
				Gruppe gruppe = tGruppen[groupID];
				jLblsGruppen[newOrder[match]].setText(("" + alphabet[groupID]).toUpperCase());
				
				if (gruppe.isSpielplanEntered(matchday, matchID)) {
					Spiel spiel = gruppe.getSpiel(matchday, matchID);
					this.jLblsMannschaften[newOrder[match]].setText(gruppe.getMannschaften()[spiel.home() - 1].getName());
					this.jLblsMannschaften[newOrder[match] + numberOfMatches].setText(gruppe.getMannschaften()[spiel.away() - 1].getName());
				}
				if (gruppe.isErgebnisplanEntered(matchday, matchID)) {
					setErgebnis(match, new Ergebnis(gruppe.getErgebnis(matchday, matchID).toString()));
					this.jTFsTore[newOrder[match]].setText("" + ergebnisse[match].home());
					this.jTFsTore[newOrder[match] + numberOfMatches].setText("" + ergebnisse[match].away());
				}
				
				matchID++;
				if (matchID == numbersOfMatches[groupID]) {
					matchID = 0;
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
		
		fillTeamsLabelsAndGoalsTFs(currentMatchday);
	}
	
	public void gruppeClicked(int index) {
		int oldIndex = oldOrder[index];
		int groupID = 0;
		while (oldIndex > 0) {
			oldIndex -= numbersOfMatches[groupID];
			groupID++;
		}
		if (oldIndex < 0) {
			groupID--;
		}
		start.jBtnZurueckActionPerformed();
		start.jBtnGruppePressed(groupID);
		start.jBtnSpieltageActionPerformed();
	}
	
	public void datumsLabelClicked(int index) {
		if (editedDate != -1) {
			message("Das Datum eines anderen Spiels wird bereits gesetzt.");
			return;
		}
		editedDate = index;
		jLblsSpieltagsdaten[editedDate].setOpaque(true);
		repaintImmediately(jLblsSpieltagsdaten[editedDate]);
		if (isOverview)	editedDate = oldOrder[editedDate];
		
		if (belongsToALeague) {
			MyDateChooser mdc = new MyDateChooser(season, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndKOTindex(season.getDate(currentMatchday), season.getKOTIndex(currentMatchday, editedDate));
			mdc.setMatch(season, currentMatchday, editedDate);
			
			start.toFront();
			mdc.toFront();
		} else if (belongsToGroup) {
			MyDateChooser mdc = new MyDateChooser(gruppe, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(gruppe.getDate(currentMatchday, editedDate), gruppe.getTime(currentMatchday, editedDate));
			mdc.setMatch(gruppe, currentMatchday, editedDate);

			start.toFront();
			mdc.toFront();
		} else if (belongsToKORound) {
			MyDateChooser mdc = new MyDateChooser(koRunde, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(koRunde.getDate(currentMatchday, editedDate), koRunde.getTime(currentMatchday, editedDate));
			mdc.setMatch(koRunde, currentMatchday, editedDate);

			start.toFront();
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
			Gruppe gruppe = tGruppen[groupID];
			
			MyDateChooser mdc = new MyDateChooser(gruppe, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(gruppe.getDate(currentMatchday, matchID), gruppe.getTime(currentMatchday, matchID));
			mdc.setMatch(gruppe, currentMatchday, matchID);

			start.toFront();
			mdc.toFront();
		}
	}
	
	public void dateEnteredLeagueStyle(int startDate, int KOTindex) {
		season.setDate(currentMatchday, startDate);
		season.setKOTIndex(currentMatchday, editedDate, KOTindex);
		dateChooserClosed();
		spieltagsdatenBefuellen();
	}
	
	public void dateEnteredTournamentStyle(int myDate, int myTime) {
		if (belongsToGroup) {
			gruppe.setDate(currentMatchday, editedDate, myDate);
			gruppe.setTime(currentMatchday, editedDate, myTime);
		} else if (belongsToKORound) {
			koRunde.setDate(currentMatchday, editedDate, myDate);
			koRunde.setTime(currentMatchday, editedDate, myTime);
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
			Gruppe gruppe = tGruppen[groupID];
			
			gruppe.setDate(currentMatchday, matchID, myDate);
			gruppe.setTime(currentMatchday, matchID, myTime);
		}
		dateChooserClosed();
		spieltagsdatenBefuellen();
	}
	
	public void dateChooserClosed() {
		if (editedDate == -1)	return;
		if (isOverview)	editedDate = newOrder[editedDate];
		jLblsSpieltagsdaten[editedDate].setOpaque(false);
		repaintImmediately(jLblsSpieltagsdaten[editedDate]);
		editedDate = -1;
	}
	
	private void jBtnsMatchInfosClicked(int index) {
		int offset = 0;
		int matchID = index;
		
		if (isOverview) {
			for (Gruppe gruppe : tGruppen) {
				int nOMatches = gruppe.getNumberOfMatchesPerMatchday();
				if ((offset += nOMatches) > oldOrder[index]) {
					matchID = oldOrder[index] - offset + nOMatches;
					wettbewerb = gruppe;
					break;
				}
			}
		}
		
		if (!wettbewerb.isSpielplanEntered(currentMatchday, matchID)) {
			
			return;
		}
		
		SpielInformationen spielInformationen = new SpielInformationen(this, index, wettbewerb.getSpiel(currentMatchday, matchID), ergebnisse[index]);
		spielInformationen.setLocationRelativeTo(null);
		spielInformationen.setVisible(true);
		openedMatchInfos.add(spielInformationen);

		start.toFront();
		spielInformationen.toFront();
	}
	
	public void saveMatchInfos(SpielInformationen matchInfo, Ergebnis ergebnis, int editedResult) {
		setErgebnis(editedResult, ergebnis);
		openedMatchInfos.remove(matchInfo);
	}
	
	public void mannschaftClicked(int index) {
		jLblsMannschaften[index].setOpaque(true);
		jLblsMannschaften[index].setBackground(colorEditing);
		jLblsMannschaften[index].paintImmediately(0, 0, jLblsMannschaften[index].getWidth(), jLblsMannschaften[index].getHeight());
		for (int i = 0; i < jLblsMannschaften.length; i++) {
			if (i != index) {
				jLblsMannschaften[i].setBackground(colorEdited);
				jLblsMannschaften[i].paintImmediately(0, 0, jLblsMannschaften[i].getWidth(), jLblsMannschaften[i].getHeight());
			}
		}
		jPnlTeamsSelection.setVisible(true);
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
	
	public void mannschaftenButtonClicked(int index) {
		if (belongsToALeague)		jLblsMannschaften[editedLabel].setText(season.getMannschaften()[index].getName());
		else if (belongsToGroup)	jLblsMannschaften[editedLabel].setText(gruppe.getMannschaften()[index].getName());
		else if (belongsToKORound) {
			try {
				jLblsMannschaften[editedLabel].setText(teams[index].getName());
			} catch (NullPointerException npe) {
				jLblsMannschaften[editedLabel].setText(koRunde.getTeamsOrigin(index));
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
			
			jLblsMannschaften[editedLabel].setText(tGruppen[groupID].getMannschaften()[teamID].getName());
		}
		int oldButton = array[editedLabel % numberOfMatches][editedLabel / numberOfMatches] - 1;
		if (oldButton >= 0)	jBtnsMannschaften[oldButton].setEnabled(true);
		
		array[editedLabel % numberOfMatches][editedLabel / numberOfMatches] = index + 1;
		jBtnsMannschaften[index].setEnabled(false);
		jLblsMannschaften[editedLabel].setBackground(colorEdited);
		editedLabel = -1;
		editedGroupID = -1;
		jPnlTeamsSelection.setVisible(false);
	}
}
