package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static util.Utilities.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Spieltag extends JPanel {
	private static final long serialVersionUID = 533273470193095401L;

	private Start start;
	private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	private JComboBox jCBSpieltage;
	public JLabel[] mannschaften;
	public JTextField[] tore;
	private JLabel[] zusatzInfos;
	private JButton bearbeiten;
	private JButton fertig;
	private JButton previous;
	private JButton next;
	private JButton[] moreOptions;
	private JButton jBtnResetMatchday;
	private JButton jBtnEnterRueckrunde;
	private JButton defaultKickoff;
	private JLabel[] spieltagsdaten;
	private JLabel[] gruppenLbls;
	private SpielInformationen spielInformationen;

	private int[][] array;
	private Ergebnis[] ergebnisse;
	private int editedDate = -1;
	private int editedResult;
	private int editedLabel = -1;
	private int editedGroupID = -1;
	private int editedMatchday = -1;
	private int currentMatchday = -1;
	
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

	private JPanel teamsSelection;
	private JButton[] mannschaftenbtns;

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
	private Liga liga;
	private Gruppe gruppe;
	private KORunde koRunde;
	private Turnier turnier;
	private Mannschaft[] teams;
	
	// Overview
	private int[] numbersOfTeams;
	private int[] numbersOfMatches;
	private int[] oldOrder;
	private int[] newOrder;

	public Spieltag(Start start, Liga liga) {
		super();

		this.start = start;
		this.liga = liga;
		this.wettbewerb = liga;
		this.belongsToALeague = true;
		this.belongsToGroup = false;
		this.belongsToKORound = false;
		this.isOverview = false;
		this.isETPossible = liga.isETPossible();

		this.numberOfMatches = liga.getNumberOfMatchesPerMatchday();
		this.numberOfTeams = liga.getNumberOfTeams();
		this.numberOfMatchdays = liga.getNumberOfMatchdays();
		this.halfCountTeamsRoundUp = liga.getHalbeAnzMSAuf();

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
	
	// in case of failure delete this constructor and the variable isOverview
	public Spieltag(Start start, Turnier turnier) {
		super();
		
		this.start = start;
		this.turnier = turnier;
		this.belongsToALeague = false;
		this.belongsToGroup = false;
		this.belongsToKORound = false;
		this.isOverview = true;
		this.isETPossible = turnier.isETPossible();
		
		this.numbersOfTeams = new int[turnier.getNumberOfGroups()];
		this.numbersOfMatches = new int[turnier.getNumberOfGroups()];
		
		for (Gruppe gruppe : turnier.getGruppen()) {
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
		buttonsauswahl[GAPX] = 10;
		buttonsauswahl[GAPY] = 0;
		buttonsauswahl[SIZEX] = 200;
		buttonsauswahl[SIZEY] = 50;
		
		if ((2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]) > maxHeight) {
			buttonsauswahl[SIZEY] = (maxHeight - (halfCountTeamsRoundUp + 1) * buttonsauswahl[GAPY]) / halfCountTeamsRoundUp;
//			message("The calculated value of height is " + buttonsauswahl[SIZEY] + " with hAnzAuf = "
//					+ halfCountTeamsRoundUp, "\"height\" was calculated");
		}
	}
	
	private void calculateBounds() {
		int height = 25, gapy = 2;
		if(numberOfMatches > 26) {
			height = 24;
			gapy = -2;
		}
		groupLabels = new int[] {160, 80, 0, gapy, 10, height};
		
		labels = new int[] { groupLabels[STARTX] + groupLabels[SIZEX] + 5, 80, (isETPossible ? 175 : 135), gapy, 180, height };

		textfields = new int[] { labels[STARTX] + labels[SIZEX] + 10, labels[STARTY], 
				labels[GAPX] - 2 * 10 - 2 * 40 - 30 - (isETPossible ? 45 : 5), labels[GAPY], 40, labels[SIZEY] };
		
		zusInfLabels = new int[] { labels[STARTX] + labels[SIZEX] + 95, labels[STARTY], 0, labels[GAPY], (isETPossible ? 30 : 0), labels[SIZEY] };
		
		moreOptButtons = new int[] { labels[STARTX] + labels[SIZEX] + labels[GAPX] - 45, labels[STARTY], 0, labels[GAPY], 40, labels[SIZEY] };
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);

			// TODO min und maxheight sinnvoll setzen
			int minimumheight = 450;
			int maximumheight = 800;
			calculateButtonsauswahlBounds(maximumheight);
			calculateBounds();

			Dimension dim = new Dimension();
			dim.width = 1200;

			int heightOfTeamLabels = labels[STARTY] + numberOfMatches * (labels[SIZEY] + labels[GAPY]) + 20;
			int heightOfTeamSelection = 2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY])
					- buttonsauswahl[GAPY];

			dim.height = (heightOfTeamLabels > heightOfTeamSelection ? heightOfTeamLabels : heightOfTeamSelection);
//			message("labelsHeight = " + heightOfTeamLabels + " oder buttonsHeight = " + heightOfTeamSelection + " macht: " + dim.height);

			// correction to minimumheight or maximumheight if out of these bounds
			if (dim.height < minimumheight) {
				dim.height = minimumheight;
			} else if (dim.height > maximumheight) {
				dim.height = maximumheight;
			}

			this.setSize(dim);
//			message("This is dim: " + dim);

			ergebnisse = new Ergebnis[numberOfMatches];
			
			gruppenLbls = new JLabel[numberOfMatches];
			zusatzInfos = new JLabel[numberOfMatches];
			moreOptions = new  JButton[numberOfMatches];
			mannschaftenbtns = new JButton[numberOfTeams];
			mannschaften = new JLabel[2 * numberOfMatches];
			tore = new JTextField[2 * numberOfMatches];
			array = new int[numberOfMatches][2];
			spieltagsdaten = new JLabel[numberOfMatches];
			
			{
				String[] hilfsarray = new String[numberOfMatchdays];
				for (int i = 0; i < numberOfMatchdays; i++) {
					hilfsarray[i] = (i + 1) + ". Spieltag";
				}
				jCBSpieltage = new JComboBox();
				this.add(jCBSpieltage);
				jCBSpieltage.setModel(new DefaultComboBoxModel(hilfsarray));
				jCBSpieltage.setBounds(REC_COMBO);
				jCBSpieltage.setFocusable(false);
				jCBSpieltage.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						jCBSpieltageItemStateChanged(evt);
					}
				});
			}
			
			for (int i = 0; i < spieltagsdaten.length; i++) {
				final int x = i;
				spieltagsdaten[i] = new JLabel();
				this.add(spieltagsdaten[i]);
				spieltagsdaten[i].setBounds(30, labels[STARTY] + i * (labels[SIZEY] + labels[GAPY]), 120, labels[SIZEY]);
				spieltagsdaten[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				spieltagsdaten[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						datumsLabelClicked(x);
					}
				});
			}
			if (isOverview) {
				for (int i = 0; i < gruppenLbls.length; i++) {
					final int x = i;
					gruppenLbls[i] = new JLabel();
					this.add(gruppenLbls[i]);
					gruppenLbls[i].setBounds(groupLabels[STARTX], groupLabels[STARTY] + i * (labels[SIZEY] + labels[GAPY]), groupLabels[SIZEX], groupLabels[SIZEY]);
					gruppenLbls[i].setHorizontalAlignment(SwingConstants.RIGHT);
					gruppenLbls[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
//					gruppenLbls[i].setOpaque(true);
					gruppenLbls[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							gruppeClicked(x);
						}
					});
				}
			}
			
			for (int i = 0; i < mannschaften.length; i++) {
				final int x = i;
				int zeile = i % numberOfMatches;
				int spalte = i / numberOfMatches;

				mannschaften[i] = new JLabel();
				this.add(mannschaften[i]);
				mannschaften[i].setBounds(labels[STARTX] + spalte * (labels[SIZEX] + labels[GAPX]), 
						labels[STARTY] + zeile * (labels[SIZEY] + labels[GAPY]), labels[SIZEX], labels[SIZEY]);
				if (spalte == 0)	mannschaften[i].setHorizontalAlignment(SwingConstants.RIGHT);
				else				mannschaften[i].setHorizontalAlignment(SwingConstants.LEFT);
				mannschaften[i].setEnabled(false);
				mannschaften[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
//				mannschaften[i].setOpaque(true);
				mannschaften[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (mannschaften[x].isEnabled()) {
							mannschaftClicked(x);
						}
					}
				});
			}
			
			for (int i = 0; i < tore.length; i++) {
				final int x = i;
				tore[i] = new JTextField();
				this.add(tore[i]);
				tore[i].setBounds(textfields[STARTX] + (i / numberOfMatches) * (textfields[SIZEX] + textfields[GAPX]), 
						textfields[STARTY] + (i % numberOfMatches) * (textfields[SIZEY] + textfields[GAPY]), textfields[SIZEX], textfields[SIZEY]);
				tore[i].setHorizontalAlignment(SwingConstants.CENTER);
				tore[i].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if (arg0.getKeyChar() == 8) {
							aValidKeyWasPressed(x, arg0);
						} else if ((tore[x].getText().length() >= 2 && !tore[x].getText().equals("-1")) || arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						} else {
							aValidKeyWasPressed(x, arg0);
						}
					}
				});
				tore[i].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent arg0) {
						tore[x].selectAll();
					}
				});
			}
			for (int i = 0; i < zusatzInfos.length; i++) {
				zusatzInfos[i] = new JLabel();
				this.add(zusatzInfos[i]);
				zusatzInfos[i].setText("");
				zusatzInfos[i].setBounds(zusInfLabels[STARTX], zusInfLabels[STARTY] + i * (zusInfLabels[SIZEY] + zusInfLabels[GAPY]), zusInfLabels[SIZEX], zusInfLabels[SIZEY]);
			}
			{
				bearbeiten = new JButton();
				this.add(bearbeiten);
				bearbeiten.setBounds(REC_EDITSAVE);
				bearbeiten.setText("Spielplan bearbeiten");
				bearbeiten.setFocusable(false);
				bearbeiten.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						bearbeitenActionPerformed();
					}
				});
			}
			{
				fertig = new JButton();
				this.add(fertig);
				fertig.setBounds(REC_EDITSAVE);
				fertig.setText("Speichern");
				fertig.setFocusable(false);
				fertig.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fertigActionPerformed();
					}
				});
				fertig.setVisible(false);
			}
			{
				previous = new JButton();
				this.add(previous);
				previous.setBounds(REC_PREV);
				previous.setText("<<");
				previous.setFocusable(false);
				previous.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						previousMatchdayActionPerformed();
					}
				});
			}
			{
				next = new JButton();
				this.add(next);
				next.setBounds(REC_NEXT);
				next.setText(">>");
				next.setFocusable(false);
				next.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						nextMatchdayActionPerformed();
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
			for (int i = 0; i < moreOptions.length; i++) {
				final int x = i;
				moreOptions[i] = new JButton();
				this.add(moreOptions[i]);
				moreOptions[i].setBounds(moreOptButtons[STARTX], moreOptButtons[STARTY] + i * (moreOptButtons[SIZEY] + moreOptButtons[GAPY]), 
						moreOptButtons[SIZEX], moreOptButtons[SIZEY]);
				moreOptions[i].setText("+");
				moreOptions[i].setToolTipText("Reset this result.");
				moreOptions[i].setFocusable(false);
				moreOptions[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						moreOptionsClicked(x);
					}
				});
			}
			if (belongsToALeague) {
				defaultKickoff = new JButton();
				this.add(defaultKickoff);
				defaultKickoff.setBounds(REC_DEFKOT);
				defaultKickoff.setText("Standardanstosszeiten");
				defaultKickoff.setFocusable(false);
				defaultKickoff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						defaultKickoffActionPerformed();
					}
				});
			}
			
			{
				teamsSelection = new JPanel();
				this.add(teamsSelection);
				teamsSelection.setLayout(null);
				teamsSelection.setSize(2 * (buttonsauswahl[STARTX] + buttonsauswahl[SIZEX]) + buttonsauswahl[GAPX], 2 * buttonsauswahl[STARTY]
						+ halfCountTeamsRoundUp * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]);
				teamsSelection.setLocation(730, (this.getSize().height - teamsSelection.getSize().height) / 2);
				teamsSelection.setVisible(false);
				teamsSelection.setOpaque(true);
				teamsSelection.setBackground(Color.red);
			}

			for (int i = 0; i < numberOfTeams; i++) {
				final int x = i; // for the ActionListener
				int xfactor = i % 2, yfactor = i / 2;
//				int xfactor = i / halfCountTeamsRoundUp, yfactor = i % halfCountTeamsRoundUp;
				mannschaftenbtns[i] = new JButton();
				teamsSelection.add(mannschaftenbtns[i]);
				mannschaftenbtns[i].setBounds(buttonsauswahl[STARTX] + xfactor * (buttonsauswahl[SIZEX] + buttonsauswahl[GAPX]),
								buttonsauswahl[STARTY] + yfactor * (buttonsauswahl[SIZEY] + buttonsauswahl[GAPY]), buttonsauswahl[SIZEX], buttonsauswahl[SIZEY]);
				mannschaftenbtns[i].addActionListener(new ActionListener() {
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
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.clearRect(2, 2, getWidth() - 4, getHeight() - 4);
		
		Color colUp = new Color(243, 255, 243);
		Color colDown = new Color(128, 255, 128);
		
		// Hintergrund
		for (int i = 2; i < this.getHeight() - 2; i++) {
			g.setColor(new Color(colUp.getRed() + ((colDown.getRed() - colUp.getRed()) * i / (this.getHeight() - 2)), 
									colUp.getGreen() + ((colDown.getGreen() - colUp.getGreen()) * i / (this.getHeight() - 2)), 
									colUp.getBlue() + ((colDown.getBlue() - colUp.getBlue()) * i / (this.getHeight() - 2))));
			g.drawLine(2, i, this.getWidth() - 3, i);
		}

		// 10/50/100 Raster
//		Color[] colors = new Color[] {Color.lightGray, Color.white, Color.black};
//		int[] schritte = new int[] {10, 50, 100};
//		
//		g.setColor(Color.lightGray);
//		for (int j = 0; j < colors.length; j++) {
//			g.setColor(colors[j]);
//			for (int i = 1; i * schritte[j] < getWidth(); i++) {
//				g.drawLine(i * schritte[j], 0, i * schritte[j], this.getHeight());
//			}
//			for (int i = 0; i * schritte[j] < getHeight(); i++) {
//				g.drawLine(0, i * schritte[j], this.getWidth(), i * schritte[j]);
//			}
//		}
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
			zusatzInfos[index].setText("");
			
			if (result != null) {
				tore[index].setText("" + result.home());
				tore[index + numberOfMatches].setText("" + result.away());
				if (result.toString().indexOf("n") != -1) {
					if (result.toString().indexOf("nE") != -1)			zusatzInfos[index].setText("n. E.");
					else if (result.toString().indexOf("nV") != -1)		zusatzInfos[index].setText("n. V.");
				}
			} else {
				tore[index].setText("-1");
				tore[index + numberOfMatches].setText("-1");
			}
		}
	}
	
	public void showMatchday(int matchday) {
		jCBSpieltage.setSelectedIndex(matchday);
	}

	private void jCBSpieltageItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			if (belongsToALeague) {
				liga.ergebnisseSichern();
				spieltagAnzeigen();
			} else if (belongsToGroup) {
				gruppe.ergebnisseSichern();
				spieltagAnzeigen();
			} else if (belongsToKORound) {
				koRunde.ergebnisseSichern();
				spieltagAnzeigen();
			} else {
				turnier.ergebnisseSichern();
				spieltagAnzeigen();
			}
		}
	}
	
	private void aValidKeyWasPressed(int indexOfTF, KeyEvent arg0) {
		String selText = tore[indexOfTF].getSelectedText();
		String otherGoals = tore[(indexOfTF + numberOfMatches) % (2 * numberOfMatches)].getText();
		String newContent = "";
		
		if (arg0.getKeyChar() == 8) {
			if (tore[indexOfTF].getText().length() == 0 || tore[indexOfTF].getText().equals("-")) {
				tore[indexOfTF].setText("-1");
				tore[indexOfTF].selectAll();
			}
			newContent = tore[indexOfTF].getText();
		} else {
			if (selText != null) 	newContent = tore[indexOfTF].getText().replaceAll(selText, "");
			else					newContent = tore[indexOfTF].getText();
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
		for (JLabel lbl : mannschaften) {
			lbl.setEnabled(bool);
			lbl.setOpaque(false);
		}
	}
	
	private void disableTFs() {
		for (JTextField tf : tore)	tf.setEditable(false);
	}
	
	private void setTFsEditableFromRepresentation() {
		String representation;
		if (belongsToALeague)		representation = liga.getSpielplanRepresentation(currentMatchday);
		else if (belongsToGroup)	representation = gruppe.getSpielplanRepresentation(currentMatchday);
		else if (belongsToKORound)	representation = koRunde.getSpielplanRepresentation(currentMatchday);
		else {
			representation = "";
			for (Gruppe gruppe : turnier.getGruppen()) {
				representation += gruppe.getSpielplanRepresentation(currentMatchday);
			}
		}
		
		for (int match = 0; match < this.numberOfMatches; match++) {
			if (representation.charAt(match) == 't') {
				tore[match].setEditable(true);
				tore[match + numberOfMatches].setEditable(true);
			} else if (representation.charAt(match) == 'f') {
				tore[match].setEditable(false);
				tore[match + numberOfMatches].setEditable(false);
			}
		}
		for (int match = 0; match < this.numberOfMatches; match++) {
			if (tore[match].isEditable()) {
				tore[match].selectAll();
				break;
			}
		}
	}
	
	private void spieltagsdatenBefuellenGroupOrder() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		int groupID = 0, matchID = 0;
		for (int i = 0; i < numberOfMatches; i++) {
			dateandtimeofmatches[i] = turnier.getGruppen()[groupID].getDateAndTime(currentMatchday, matchID);
			spieltagsdaten[i].setText(dateandtimeofmatches[i]);
			
			matchID++;
			if (matchID == numbersOfMatches[groupID]) {
				matchID = 0;
				groupID++;
			}
		}
		for (int i = spieltagsdaten.length - 1; i > 0; i--) {
			if (spieltagsdaten[i].getText().equals(spieltagsdaten[i - 1].getText())) {
				spieltagsdaten[i].setText("");
			}
		}
	}

	private void spieltagsdatenBefuellen() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		int groupID = 0, matchID = 0;
		for (int i = 0; i < numberOfMatches; i++) {
			if (belongsToALeague)		dateandtimeofmatches[i] = liga.getDateAndTime(currentMatchday, i);
			else if (belongsToGroup)	dateandtimeofmatches[i] = gruppe.getDateAndTime(currentMatchday, i);
			else if (belongsToKORound)	dateandtimeofmatches[i] = koRunde.getDateAndTime(currentMatchday, i);
			else {
				dateandtimeofmatches[i] = turnier.getGruppen()[groupID].getDateAndTime(currentMatchday, matchID);
				
				matchID++;
				if (matchID == numbersOfMatches[groupID]) {
					matchID = 0;
					groupID++;
				}
			}
			if (isOverview)	spieltagsdaten[newOrder[i]].setText(dateandtimeofmatches[i]);
			else			spieltagsdaten[i].setText(dateandtimeofmatches[i]);
		}
		for (int i = spieltagsdaten.length - 1; i > 0; i--) {
			if (spieltagsdaten[i].getText().equals(spieltagsdaten[i - 1].getText())) {
				spieltagsdaten[i].setText("");
			}
		}
	}

	private void setMannschaftenButtonsNames() {
		int groupID = 0, teamID = 0;
		for (int i = 0; i < mannschaftenbtns.length; i++) {
			if (belongsToALeague)		mannschaftenbtns[i].setText(liga.getMannschaften()[i].getName());
			else if (belongsToGroup)	mannschaftenbtns[i].setText(gruppe.getMannschaften()[i].getName());
			else if (belongsToKORound)	{
				try {
					mannschaftenbtns[i].setText(teams[i].getName());
				} catch (NullPointerException npe) {
					mannschaftenbtns[i].setText(koRunde.getTeamsOrigin(i));
				}
			} else {
				mannschaftenbtns[i].setText(turnier.getGruppen()[groupID].getMannschaften()[teamID].getName());
				
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
	private void bearbeitenActionPerformed() {
		editedMatchday = jCBSpieltage.getSelectedIndex();
		
		setMannschaftenButtonsNames();
		
		if (isOverview) {
			setMatchesInGroupOrder();
			spieltagsdatenBefuellenGroupOrder();
		}
		
		Spiel spiel;
		for (int i = 0; i < array.length; i++) {
			if (belongsToALeague)		spiel = liga.getSpiel(editedMatchday, i);
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
				
				spiel = turnier.getGruppen()[groupID].getSpiel(editedMatchday, matchID);
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
		
		setLabelsEnabled(true);
		disableTFs();
		bearbeiten.setVisible(false);
		fertig.setVisible(true);
		jCBSpieltage.setEnabled(false);
		previous.setEnabled(false);
		next.setEnabled(false);
		jBtnResetMatchday.setVisible(false);
		if (defaultKickoff != null)	defaultKickoff.setVisible(false);
	}

	/**
	 * Optimized for <code>Gruppe</code> and <code>KORunde</code>, setMatch is not called for KORunde
	 * @return if the content was saved: 0 if yes, 1 if not
	 */
	public int fertigActionPerformed() {
		int fehlerart = -1;
		// 1. Fehlerfall: es gibt doppelte Vorkommnisse im Array
		for (int i = 0; i < 2 * array.length; i++) {
			int r = array[i % array.length][i / array.length];
			for (int j = 0; j < 2 * array.length; j++) {
				if (i != j) {
					int s = array[j % array.length][j / array.length];
					if (r == s) {
						fehlerart = 2;
						break;
					}
				}
			}
			if (fehlerart == 2)		break;
		}
		// 2. Fehlerfall: es befinden sich noch ungesetzte Felder im Array
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
		if (fehlerart == 2) {
			JOptionPane.showMessageDialog(null, "Some teams appear twice. Remove the mistake before you can save.", "Fehler", JOptionPane.ERROR_MESSAGE);
			saveanyway = 1;
		} else if (fehlerart == 1) {
			saveanyway = JOptionPane.showConfirmDialog(null, "The matchday is incomplete. \nContinue anyway?", "Warning", JOptionPane.YES_NO_OPTION);
		}
		
		if (saveanyway == 0) {
			if (belongsToKORound)	koRunde.setCheckTeamsFromPreviousRound(false);
			int groupID = 0, matchID = 0, offset = 0, home, away;
			for (int match = 0; match < array.length; match++) {
				if (isOverview)	wettbewerb = turnier.getGruppen()[groupID];
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
			fertig.setVisible(false);
			teamsSelection.setVisible(false);
			
			bearbeiten.setVisible(true);
			jCBSpieltage.setEnabled(true);
			previous.setEnabled(true);
			next.setEnabled(true);
			jBtnResetMatchday.setVisible(true);
			if (defaultKickoff != null)	defaultKickoff.setVisible(true);
			spieltagAnzeigen();
		}
		return saveanyway;
	}
	
	private void previousMatchdayActionPerformed() {
		jCBSpieltage.setSelectedIndex(currentMatchday - 1);
	}
	
	private void nextMatchdayActionPerformed() {
		jCBSpieltage.setSelectedIndex(currentMatchday + 1);
	}
	
	private void resetMatchdayActionPerformed() {
		if (yesNoDialog("Do you really want to reset this matchday? This is irrevocable.") == JOptionPane.NO_OPTION) return;
		
		if (belongsToALeague) {
			for (int match = 0; match < liga.getNumberOfMatchesPerMatchday(); match++) {
				liga.setSpiel(currentMatchday, match, null);
			}
			
			for (int team = 0; team < liga.getNumberOfTeams(); team++) {
				liga.getMannschaften()[team].resetMatch(currentMatchday);
			}
			
			liga.ergebnisseSichern();
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
			for (Gruppe gruppe : turnier.getGruppen()) {
				for (int match = 0; match < gruppe.getNumberOfMatchesPerMatchday(); match++) {
					gruppe.setSpiel(currentMatchday, match, null);
				}
				
				for (int team = 0; team < gruppe.getNumberOfTeams(); team++) {
					gruppe.getMannschaften()[team].resetMatch(currentMatchday);
				}
			}
			
			turnier.ergebnisseSichern();
			spieltagAnzeigen();
		}
	}
	
	private void jBtnEnterRueckrundeActionPerformed() {
		String standard = "1";
		for (int i = 2; i <= (numberOfMatchdays / 2); i++) {
			standard += "," + i;
		}
		String eingabe = inputDialog("Please enter the order of the hinrunde matchdays matching the rueckrunde order.", standard);
		String[] eingabeSplit = eingabe.split(",");
		if (eingabeSplit.length * 2 != numberOfMatchdays) {
			message("You have to submit " + (numberOfMatchdays / 2) + " values separated by comma.");
			return;
		}
		
		int[] rueckrundeOrder = new int[eingabeSplit.length];
		try {
			for (int i = 0; i < rueckrundeOrder.length; i++) {
				rueckrundeOrder[i] = Integer.parseInt(eingabeSplit[i]);
			}
		} catch (NumberFormatException nfe) {
			message("You have to enter numbers!");
			return;
		}
		
		boolean[] checks = new boolean[rueckrundeOrder.length];
		try {
			for (int i = 0; i < rueckrundeOrder.length; i++) {
				if (!checks[rueckrundeOrder[i] - 1])	checks[rueckrundeOrder[i] - 1] = true;
				else {
					message("You have to submit distinct values.");
					return;
				}
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			message("You have to submit values between 0 and " + (numberOfMatchdays / 2) + ".");
			return;
		}
		
		liga.setRueckrundeToOrder(rueckrundeOrder);
		spieltagAnzeigen();
	}

	private void defaultKickoffActionPerformed() {
		if (belongsToALeague) {
			liga.useDefaultKickoffTimes(currentMatchday);
		} else if (belongsToGroup) {
			message("Nicht vorgesehen für Gruppe");
		} else if (belongsToKORound) {
			message("Nicht vorgesehen für KORunde");
		} else {
			message("Nicht vorgesehen für Gruppenuebersicht");
		}
		spieltagsdatenBefuellen();
	}

	private void changeOrderToChronological(int matchday) {
		if (belongsToALeague)		liga.changeOrderToChronological(matchday);
		else if (belongsToGroup)	gruppe.changeOrderToChronological(matchday);
		else if (belongsToKORound)	koRunde.changeOrderToChronological(matchday);
		else if (isOverview) {
			oldOrder = turnier.getChronologicalOrder(matchday); // beinhaltet die alten Indizes in der neuen Reihenfolge
			newOrder = new int[oldOrder.length]; // beinhaltet die neuen Indizes in der alten Reihenfolge
			for (int i = 0; i < oldOrder.length; i++) {
				newOrder[oldOrder[i]] = i; // if enabled
//				log((i + 1) + ". Spiel:   alterIndex " + oldOrder[i] + "\t oder: " + (oldOrder[i] + 1) + ". Spiel:   alterIndex " + i);
//				oldOrder[i] = i; // if disabled
//				newOrder[i] = i; // if disabled
			}
		}
	}

	public void spieltagAnzeigen() {
		if (currentMatchday == -1) {
			if (belongsToALeague)		currentMatchday = liga.getCurrentMatchday();
			else if (belongsToGroup)	currentMatchday = gruppe.getCurrentMatchday();
			else if (belongsToKORound)	currentMatchday = koRunde.getCurrentMatchday();
			else 						currentMatchday = turnier.getCurrentMatchday();
			
			// damit nicht bei setSelectedIndex die default-Inhalte von tore in das ergebnis-Array kopiert werden muss das Befuellen davor erfolgen
			for (int match = 0; match < numberOfMatches; match++) {
				try {
					if (belongsToALeague) {
						setErgebnis(match, new Ergebnis(liga.getErgebnis(currentMatchday, match).toString()));
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
						setErgebnis(match, new Ergebnis(turnier.getGruppen()[groupID].getErgebnis(currentMatchday, matchID).toString()));
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
			
			if (currentMatchday - 1 < 0)	previous.setEnabled(false);
			else							previous.setEnabled(true);
			if (currentMatchday + 1 == this.numberOfMatchdays)		next.setEnabled(false);
			else													next.setEnabled(true);
			
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
		for (int i = 0; i < this.mannschaften.length; i++) {
			this.mannschaften[i].setText("n. a.");
			this.tore[i].setText("-1");
		}
		if (belongsToALeague) {
			for (int match = 0; match < numberOfMatches; match++) {
				if (liga.isSpielplanEntered(matchday, match)) {
					Spiel spiel = liga.getSpiel(matchday, match);
					this.mannschaften[match].setText(liga.getMannschaften()[spiel.home() - 1].getName());
					this.mannschaften[match + numberOfMatches].setText(liga.getMannschaften()[spiel.away() - 1].getName());
				}
				if (liga.isErgebnisplanEntered(matchday, match)) {
					setErgebnis(match, new Ergebnis(liga.getErgebnis(matchday, match).toString()));
				}
			}
		} else if (belongsToGroup) {
			for (int match = 0; match < numberOfMatches; match++) {
				if (gruppe.isSpielplanEntered(matchday, match)) {
					Spiel spiel = gruppe.getSpiel(matchday, match);
					this.mannschaften[match].setText(gruppe.getMannschaften()[spiel.home() - 1].getName());
					this.mannschaften[match + numberOfMatches].setText(gruppe.getMannschaften()[spiel.away() - 1].getName());
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
						this.mannschaften[match].setText(teams[spiel.home() - 1].getName());
						this.mannschaften[match + numberOfMatches].setText(teams[spiel.away() - 1].getName());
					} catch (NullPointerException npe) {
						this.mannschaften[match].setText(koRunde.getTeamsOrigin(spiel.home() - 1));
						this.mannschaften[match + numberOfMatches].setText(koRunde.getTeamsOrigin(spiel.away() - 1));
					}
					
				}
				if (koRunde.isErgebnisplanEntered(matchday, match)) {
					setErgebnis(match, new Ergebnis(koRunde.getErgebnis(matchday, match).toString()));
				}
			}
		} else {
			int groupID = 0, matchID = 0;
			for (int match = 0; match < numberOfMatches; match++) {
				Gruppe gruppe = turnier.getGruppen()[groupID];
				gruppenLbls[newOrder[match]].setText(("" + alphabet[groupID]).toUpperCase());
				
				if (gruppe.isSpielplanEntered(matchday, matchID)) {
					Spiel spiel = gruppe.getSpiel(matchday, matchID);
					this.mannschaften[newOrder[match]].setText(gruppe.getMannschaften()[spiel.home() - 1].getName());
					this.mannschaften[newOrder[match] + numberOfMatches].setText(gruppe.getMannschaften()[spiel.away() - 1].getName());
				}
				if (gruppe.isErgebnisplanEntered(matchday, matchID)) {
					setErgebnis(match, new Ergebnis(gruppe.getErgebnis(matchday, matchID).toString()));
					this.tore[newOrder[match]].setText("" + ergebnisse[match].home());
					this.tore[newOrder[match] + numberOfMatches].setText("" + ergebnisse[match].away());
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
		editedDate = index;
		if (isOverview)	editedDate = oldOrder[editedDate];
		
		if (belongsToALeague) {
			MyDateChooser mdc = new MyDateChooser(liga, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndKOTindex(liga.getDate(currentMatchday), liga.getKOTIndex(currentMatchday, editedDate));
			
			start.toFront();
			mdc.toFront();
		} else if (belongsToGroup) {
			MyDateChooser mdc = new MyDateChooser(gruppe, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(gruppe.getDate(currentMatchday, editedDate), gruppe.getTime(currentMatchday, editedDate));

			start.toFront();
			mdc.toFront();
		} else if (belongsToKORound) {
			MyDateChooser mdc = new MyDateChooser(koRunde, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(koRunde.getDate(currentMatchday, editedDate), koRunde.getTime(currentMatchday, editedDate));

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
			Gruppe gruppe = turnier.getGruppen()[groupID];
			
			MyDateChooser mdc = new MyDateChooser(gruppe, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);
			mdc.setDateAndTime(gruppe.getDate(currentMatchday, matchID), gruppe.getTime(currentMatchday, matchID));

			start.toFront();
			mdc.toFront();
		}
	}
	
	public void dateEnteredLeagueStyle(int startDate, int KOTindex) {
		liga.setDate(currentMatchday, startDate);
		liga.setKOTIndex(currentMatchday, editedDate, KOTindex);
		editedDate = -1;
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
			Gruppe gruppe = turnier.getGruppen()[groupID];
			
			gruppe.setDate(currentMatchday, matchID, myDate);
			gruppe.setTime(currentMatchday, matchID, myTime);
		}
		editedDate = -1;
		spieltagsdatenBefuellen();
	}
	
	private void moreOptionsClicked(int index) {
		
		editedResult = index;
		int offset = 0;
		int matchID = editedResult;
		
		if (isOverview) {
			for (Gruppe gruppe : turnier.getGruppen()) {
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
		
		spielInformationen = new SpielInformationen(this, wettbewerb.getSpiel(currentMatchday, matchID), ergebnisse[editedResult]);
		spielInformationen.setLocationRelativeTo(null);
		spielInformationen.setVisible(true);

		start.toFront();
		spielInformationen.toFront();
	}
	
	public void moreOptions(Ergebnis ergebnis) {
		setErgebnis(editedResult, ergebnis);
		editedResult = -1;
	}
	
	public void mannschaftClicked(int index) {
		mannschaften[index].setOpaque(true);
		mannschaften[index].setBackground(new Color(255, 255, 0));
		mannschaften[index].paintImmediately(0, 0, mannschaften[index].getWidth(), mannschaften[index].getHeight());
		for (int i = 0; i < mannschaften.length; i++) {
			if (i != index) {
				mannschaften[i].setBackground(null);
				mannschaften[i].paintImmediately(0, 0, mannschaften[i].getWidth(), mannschaften[i].getHeight());
			}
		}
		teamsSelection.setVisible(true);
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
		if (belongsToALeague)		mannschaften[editedLabel].setText(liga.getMannschaften()[index].getName());
		else if (belongsToGroup)	mannschaften[editedLabel].setText(gruppe.getMannschaften()[index].getName());
		else if (belongsToKORound) {
			try {
				mannschaften[editedLabel].setText(teams[index].getName());
			} catch (NullPointerException npe) {
				mannschaften[editedLabel].setText(koRunde.getTeamsOrigin(index));
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
			
			mannschaften[editedLabel].setText(turnier.getGruppen()[groupID].getMannschaften()[teamID].getName());
		}
		array[editedLabel % numberOfMatches][editedLabel / numberOfMatches] = index + 1;
		mannschaften[editedLabel].setBackground(null);
		editedLabel = -1;
		editedGroupID = -1;
		teamsSelection.setVisible(false);
	}
}
