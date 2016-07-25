package model;

import static util.Utilities.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class NeueLigaSaisonDialog extends JFrame {
	private static final long serialVersionUID = -2949488695441610088L;
	
	private int WIDTH = 600 + 6;
	private int HEIGHT = 800 + 28;
	
	private Color colorBackground = new Color(16, 255, 16);
	private Color colorHighlighted = new Color(255, 255, 0);
	private static final int minNumberOfTeams = 2;
	private static final int maxNumberOfTeams = 24;
	
	private boolean teamChangeMode;
	private int newSeasonTeamIndex = -1;
	private ArrayList<Mannschaft> newSeasonTeamsOrder;
	private ArrayList<Mannschaft> oldSeasonTeamsOrder;
	private int numberOfTeams;
	private int numberOfTeamsOld;
	
	private boolean changingConfiguration;
	private int season;
	private boolean isSummerToSpringSeason;
	private int numberOfMatchesAgainstSameOpponent;
	private int[] defaultKickoffTimes;
	private ArrayList<AnstossZeit> kickOffTimes;
	private boolean goalDifference;
	private boolean teamsHaveKader;
	private int[] anzahl;
	
	private Rectangle REC_LBLSAISON = new Rectangle(25, 15, 85, 25);
	private Rectangle REC_TFSAISON = new Rectangle(90, 15, 50, 25);
	
	private Rectangle REC_LBLCONFIG = new Rectangle(200, 15, 160, 25);
	private Rectangle REC_CALYEARLBL = new Rectangle(25, 45, 80, 25);
	private Rectangle REC_CALYEARYRB = new Rectangle(110, 45, 45, 25);
	private Rectangle REC_CALYEARNRB = new Rectangle(155, 45, 60, 25);
	private Rectangle REC_SGDGLBL = new Rectangle(25, 75, 260, 25);
	private Rectangle REC_SGDGTF = new Rectangle(290, 75, 40, 25);
	private Rectangle REC_GDIFFLBL = new Rectangle(25, 105, 80, 25);
	private Rectangle REC_GDIFFYRB = new Rectangle(110, 105, 45, 25);
	private Rectangle REC_GDIFFNRB = new Rectangle(155, 105, 60, 25);
	private Rectangle REC_KADERLBL = new Rectangle(25, 135, 45, 25);
	private Rectangle REC_KADERYRB = new Rectangle(110, 135, 45, 25);
	private Rectangle REC_KADERNRB = new Rectangle(155, 135, 60, 25);
	
	private Rectangle REC_CBNOFTEAMS = new Rectangle(30, 45, 75, 25);
	private Rectangle REC_LBLNOFTEAMS = new Rectangle(110, 45, 90, 25);
	private Rectangle REC_LBLBEARBEITEN = new Rectangle(220, 45, 70, 25);
	private Rectangle REC_BTNFERTIG = new Rectangle(500, 750, 80, 30);
	
	private int[] labels = new int[] {30, 80, 300, 25, 200, 20};
	
	private JPanel jPnlBackground;
	private JLabel jLblSaison;
	private JTextField jTFSaison;
	private JLabel jLblConfiguration;
	private JLabel jLblCalendarYear;
	private JRadioButton jRBCalendarYearYes;
	private JRadioButton jRBCalendarYearNo;
	private ButtonGroup calendarYearRBGrp;
	private JLabel jLblSameOpponent;
	private JTextField jTFSameOpponent;
	private JLabel jLblGoalDifference;
	private JRadioButton jRBGoalDifferenceYes;
	private JRadioButton jRBGoalDifferenceNo;
	private ButtonGroup goalDifferenceRBGrp;
	private JLabel jLblTeamsHaveKader;
	private JRadioButton jRBTeamsHaveKaderYes;
	private JRadioButton jRBTeamsHaveKaderNo;
	private ButtonGroup teamsHaveKaderRBGrp;
	
	private JComboBox<String> jCBNumberOfTeams;
	private JLabel jLblNumberOfTeams;
	private JLabel jLblBearbeiten;
	private JLabel[] jLblsMannschaftenNeueSaison;
	private JLabel[] jLblsMannschaftenAlteSaison;
	private JLabel jLblName;
	private JTextField jTFName;
	private JLabel jLblDatum;
	private JTextField jTFDatum;
	private JButton jBtnChangeTeamCompleted;
	private JButton jBtnFertig;
	
	public NeueLigaSaisonDialog() {
		super();
		
		initGUI();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		newSeasonTeamsOrder = new ArrayList<>();
		oldSeasonTeamsOrder = new ArrayList<>();
		
		String[] posNumOfTeams = new String[maxNumberOfTeams - minNumberOfTeams + 1];
    	for (int i = 0; i < posNumOfTeams.length; i++) {
			posNumOfTeams[i] = "" + (i + minNumberOfTeams);
		}
    	
    	{
    		jPnlBackground = new JPanel();
    		getContentPane().add(jPnlBackground);
    		jPnlBackground.setLayout(null);
    		jPnlBackground.setSize(WIDTH, HEIGHT);
    		jPnlBackground.setBackground(colorBackground);
    	}
		{
			jLblSaison = new JLabel();
			jPnlBackground.add(jLblSaison);
			jLblSaison.setBounds(REC_LBLSAISON);
		}
		{
			jTFSaison = new JTextField();
			jPnlBackground.add(jTFSaison);
			jTFSaison.setBounds(REC_TFSAISON);
			jTFSaison.setOpaque(true);
			jTFSaison.setVisible(false);
		}
		// Configuration
		{
			jLblConfiguration = new JLabel();
			jPnlBackground.add(jLblConfiguration);
			jLblConfiguration.setBounds(REC_LBLCONFIG);
			jLblConfiguration.setText("Konfiguration ändern");
			jLblConfiguration.setCursor(handCursor);
			jLblConfiguration.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					changeOrSaveConfiguration();
				}
			});
		}
		{
			jLblCalendarYear = new JLabel();
			jPnlBackground.add(jLblCalendarYear);
			jLblCalendarYear.setBounds(REC_CALYEARLBL);
			jLblCalendarYear.setText("Kalenderjahr");
			jLblCalendarYear.setVisible(false);
		}
		{
			jRBCalendarYearYes = new JRadioButton("ja");
			jPnlBackground.add(jRBCalendarYearYes);
			jRBCalendarYearYes.setBounds(REC_CALYEARYRB);
			jRBCalendarYearYes.setActionCommand("false");
			jRBCalendarYearYes.setOpaque(false);
			jRBCalendarYearYes.setVisible(false);
		}
		{
			jRBCalendarYearNo = new JRadioButton("nein");
			jPnlBackground.add(jRBCalendarYearNo);
			jRBCalendarYearNo.setBounds(REC_CALYEARNRB);
			jRBCalendarYearNo.setActionCommand("true");
			jRBCalendarYearNo.setOpaque(false);
			jRBCalendarYearNo.setVisible(false);
		}
		{
			calendarYearRBGrp = new ButtonGroup();
			calendarYearRBGrp.add(jRBCalendarYearYes);
			calendarYearRBGrp.add(jRBCalendarYearNo);
		}
		{
			jLblSameOpponent = new JLabel();
			jPnlBackground.add(jLblSameOpponent);
			jLblSameOpponent.setBounds(REC_SGDGLBL);
			jLblSameOpponent.setText("Anzahl Spiele gegen denselben Gegner:");
			jLblSameOpponent.setVisible(false);
		}
		{
			jTFSameOpponent = new JTextField();
			jPnlBackground.add(jTFSameOpponent);
			jTFSameOpponent.setBounds(REC_SGDGTF);
			jTFSameOpponent.setVisible(false);
		}
		{
			jLblGoalDifference = new JLabel();
			jPnlBackground.add(jLblGoalDifference);
			jLblGoalDifference.setBounds(REC_GDIFFLBL);
			jLblGoalDifference.setText("Tordifferenz");
			jLblGoalDifference.setVisible(false);
		}
		{
			jRBGoalDifferenceYes = new JRadioButton("ja");
			jPnlBackground.add(jRBGoalDifferenceYes);
			jRBGoalDifferenceYes.setBounds(REC_GDIFFYRB);
			jRBGoalDifferenceYes.setActionCommand("true");
			jRBGoalDifferenceYes.setOpaque(false);
			jRBGoalDifferenceYes.setVisible(false);
		}
		{
			jRBGoalDifferenceNo = new JRadioButton("nein");
			jPnlBackground.add(jRBGoalDifferenceNo);
			jRBGoalDifferenceNo.setBounds(REC_GDIFFNRB);
			jRBGoalDifferenceNo.setActionCommand("false");
			jRBGoalDifferenceNo.setOpaque(false);
			jRBGoalDifferenceNo.setVisible(false);
		}
		{
			goalDifferenceRBGrp = new ButtonGroup();
			goalDifferenceRBGrp.add(jRBGoalDifferenceYes);
			goalDifferenceRBGrp.add(jRBGoalDifferenceNo);
		}
		{
			jLblTeamsHaveKader = new JLabel();
			jPnlBackground.add(jLblTeamsHaveKader);
			jLblTeamsHaveKader.setBounds(REC_KADERLBL);
			jLblTeamsHaveKader.setText("Kader");
			jLblTeamsHaveKader.setVisible(false);
		}
		{
			jRBTeamsHaveKaderYes = new JRadioButton("ja");
			jPnlBackground.add(jRBTeamsHaveKaderYes);
			jRBTeamsHaveKaderYes.setBounds(REC_KADERYRB);
			jRBTeamsHaveKaderYes.setActionCommand("true");
			jRBTeamsHaveKaderYes.setOpaque(false);
			jRBTeamsHaveKaderYes.setVisible(false);
		}
		{
			jRBTeamsHaveKaderNo = new JRadioButton("nein");
			jPnlBackground.add(jRBTeamsHaveKaderNo);
			jRBTeamsHaveKaderNo.setBounds(REC_KADERNRB);
			jRBTeamsHaveKaderNo.setActionCommand("false");
			jRBTeamsHaveKaderNo.setOpaque(false);
			jRBTeamsHaveKaderNo.setVisible(false);
		}
		{
			teamsHaveKaderRBGrp = new ButtonGroup();
			teamsHaveKaderRBGrp.add(jRBTeamsHaveKaderYes);
			teamsHaveKaderRBGrp.add(jRBTeamsHaveKaderNo);
		}
		{
			// TODO
		}
		// Data
		{
			jCBNumberOfTeams = new JComboBox<>();
			jPnlBackground.add(jCBNumberOfTeams);
			jCBNumberOfTeams.setBounds(REC_CBNOFTEAMS);
			jCBNumberOfTeams.setModel(new DefaultComboBoxModel<>(posNumOfTeams));
			jCBNumberOfTeams.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                	jCBNumberOfTeamsItemStateChanged(evt);
                }
            });
		}
		{
			jLblNumberOfTeams = new JLabel();
			jPnlBackground.add(jLblNumberOfTeams);
			jLblNumberOfTeams.setBounds(REC_LBLNOFTEAMS);
			jLblNumberOfTeams.setText("Mannschaften");
			jLblNumberOfTeams.setCursor(handCursor);
			jLblNumberOfTeams.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					changeMode();
				}
			});
		}
		{
			jLblBearbeiten = new JLabel();
			jPnlBackground.add(jLblBearbeiten);
			jLblBearbeiten.setBounds(REC_LBLBEARBEITEN);
			jLblBearbeiten.setText("Bearbeiten");
			jLblBearbeiten.setCursor(handCursor);
			jLblBearbeiten.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					changeMode();
				}
			});
		}
		
		jLblsMannschaftenNeueSaison = new JLabel[maxNumberOfTeams];
		jLblsMannschaftenAlteSaison = new JLabel[maxNumberOfTeams];
		for (int i = 0; i < jLblsMannschaftenNeueSaison.length; i++) {
			final int x = i;
			jLblsMannschaftenNeueSaison[i] = new JLabel();
			jPnlBackground.add(jLblsMannschaftenNeueSaison[i]);
			jLblsMannschaftenNeueSaison[i].setBounds(labels[STARTX], labels[STARTY] + i * labels[GAPY], labels[SIZEX], labels[SIZEY]);
			jLblsMannschaftenNeueSaison[i].setText("n/a");
			jLblsMannschaftenNeueSaison[i].setVisible(false);
			jLblsMannschaftenNeueSaison[i].setCursor(handCursor);
			jLblsMannschaftenNeueSaison[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					putNewTeamToNextFreePosition(x);
				}
			});
			
			jLblsMannschaftenAlteSaison[i] = new JLabel();
			jPnlBackground.add(jLblsMannschaftenAlteSaison[i]);
			jLblsMannschaftenAlteSaison[i].setBounds(labels[STARTX] + labels[GAPX], labels[STARTY] + i * labels[GAPY], labels[SIZEX], labels[SIZEY]);
			jLblsMannschaftenAlteSaison[i].setOpaque(false);
			jLblsMannschaftenAlteSaison[i].setVisible(false);
			jLblsMannschaftenAlteSaison[i].setCursor(handCursor);
			jLblsMannschaftenAlteSaison[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					putOldTeamToNextFreePosition(x);
				}
			});
		}
		
		{
			jLblName = new JLabel();
			jPnlBackground.add(jLblName);
			jLblName.setText("Vereinsname");
			jLblName.setVisible(false);
		}
		{
			jTFName = new JTextField();
			jPnlBackground.add(jTFName);
			jTFName.setVisible(false);
		}
		{
			jLblDatum = new JLabel();
			jPnlBackground.add(jLblDatum);
			jLblDatum.setText("Gründungsdatum");
			jLblDatum.setVisible(false);
		}
		{
			jTFDatum = new JTextField();
			jPnlBackground.add(jTFDatum);
			jTFDatum.setVisible(false);
			jTFDatum.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent evt) {
					jTFDatum.selectAll();
				}
			});
		}
		{
			jBtnChangeTeamCompleted = new JButton();
			jPnlBackground.add(jBtnChangeTeamCompleted);
			jBtnChangeTeamCompleted.setText("Fertig");
			jBtnChangeTeamCompleted.setVisible(false);
			jBtnChangeTeamCompleted.setFocusable(false);
			jBtnChangeTeamCompleted.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnChangeTeamCompletedActionPerformed();
				}
			});
		}
		{
			jBtnFertig = new JButton();
			jPnlBackground.add(jBtnFertig);
			jBtnFertig.setBounds(REC_BTNFERTIG);
			jBtnFertig.setText("Fertig");
			jBtnFertig.setFocusable(false);
			jBtnFertig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnFertigActionPerformed();
				}
			});
		}
		
		setTitle("Neue Liga-Saison erstellen");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
	}
	
	private void changeOrSaveConfiguration() {
		if (teamChangeMode)	changeMode();
		if (changingConfiguration) {
			season = Integer.parseInt(jTFSaison.getText());
			isSummerToSpringSeason = jRBCalendarYearNo.isSelected();
			numberOfMatchesAgainstSameOpponent = Integer.parseInt(jTFSameOpponent.getText());
			goalDifference = jRBGoalDifferenceYes.isSelected();
			teamsHaveKader = jRBTeamsHaveKaderYes.isSelected();
			jLblConfiguration.setText("Konfiguration ändern");
			jLblSaison.setText("Saison " + season);
			
		} else {
			jLblConfiguration.setText("Konfiguration speichern");
			jLblSaison.setText("Saison");
		}
		
		changingConfiguration = !changingConfiguration;
		jCBNumberOfTeams.setVisible(!changingConfiguration);
		jLblNumberOfTeams.setVisible(!changingConfiguration);
		jLblBearbeiten.setVisible(!changingConfiguration);
		for (int i = 0; i < jLblsMannschaftenAlteSaison.length; i++) {
			jLblsMannschaftenAlteSaison[i].setVisible(!changingConfiguration && !jLblsMannschaftenAlteSaison[i].getText().equals(""));
			jLblsMannschaftenNeueSaison[i].setVisible(!changingConfiguration && !jLblsMannschaftenNeueSaison[i].getText().equals("n/a"));
		}
		
		jTFSaison.setVisible(changingConfiguration);
		jLblCalendarYear.setVisible(changingConfiguration);
		jRBCalendarYearYes.setVisible(changingConfiguration);
		jRBCalendarYearNo.setVisible(changingConfiguration);
		jLblSameOpponent.setVisible(changingConfiguration);
		jTFSameOpponent.setVisible(changingConfiguration);
		jLblGoalDifference.setVisible(changingConfiguration);
		jRBGoalDifferenceYes.setVisible(changingConfiguration);
		jRBGoalDifferenceNo.setVisible(changingConfiguration);
		jLblTeamsHaveKader.setVisible(changingConfiguration);
		jRBTeamsHaveKaderYes.setVisible(changingConfiguration);
		jRBTeamsHaveKaderNo.setVisible(changingConfiguration);
	}
	
	private void jCBNumberOfTeamsItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			numberOfTeams = jCBNumberOfTeams.getSelectedIndex() + minNumberOfTeams;
			
			int starty = labels[STARTY] + numberOfTeams * labels[GAPY];
			Rectangle REC_LBLNAME = new Rectangle(30, starty + 10, 80, 25);
			Rectangle REC_TFNAME = new Rectangle(160, starty + 10, 100, 25);
			Rectangle REC_LBLDATUM = new Rectangle(30, starty + 40, 120, 25);
			Rectangle REC_TFDATUM = new Rectangle(160, starty + 40, 100, 25);
			Rectangle REC_BTNCTFERTIG = new Rectangle(180, starty + 75, 80, 25);
			
			jLblName.setBounds(REC_LBLNAME);
			jTFName.setBounds(REC_TFNAME);
			jLblDatum.setBounds(REC_LBLDATUM);
			jTFDatum.setBounds(REC_TFDATUM);
			jBtnChangeTeamCompleted.setBounds(REC_BTNCTFERTIG);
		}
	}
	
	private void putNewTeamToNextFreePosition(int index) {
		if (teamChangeMode) {
			changeTeam(index);
			return;
		}
		jLblsMannschaftenAlteSaison[oldSeasonTeamsOrder.size()].setVisible(true);
		jLblsMannschaftenAlteSaison[oldSeasonTeamsOrder.size()].setText(newSeasonTeamsOrder.get(index).getName());
		oldSeasonTeamsOrder.add(newSeasonTeamsOrder.remove(index));
		jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setVisible(false);
		for (int i = index; i < newSeasonTeamsOrder.size(); i++) {
			jLblsMannschaftenNeueSaison[i].setText(newSeasonTeamsOrder.get(i).getName());
		}
		jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setText("n/a");
	}
	
	private void putOldTeamToNextFreePosition(int index) {
		if (oldSeasonTeamsOrder.size() <= anzahl[4]) {
			if(yesNoDialog("Laut Konfiguration müssen " + anzahl[4] + " Mannschaften absteigen. Trotzdem fortfahren?") == JOptionPane.NO_OPTION)	return;
		}
		jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setVisible(true);
		jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setText(oldSeasonTeamsOrder.get(index).getName());
		newSeasonTeamsOrder.add(oldSeasonTeamsOrder.remove(index));
		jLblsMannschaftenAlteSaison[oldSeasonTeamsOrder.size()].setVisible(false);
		for (int i = index; i < oldSeasonTeamsOrder.size(); i++) {
			jLblsMannschaftenAlteSaison[i].setText(oldSeasonTeamsOrder.get(i).getName());
		}
		jLblsMannschaftenAlteSaison[oldSeasonTeamsOrder.size()].setText("");
	}
	
	private void changeMode() {
		teamChangeMode = !teamChangeMode;
		if (teamChangeMode) {
			jLblBearbeiten.setText("Fertig");
			for (int i = 0; i < numberOfTeams; i++) {
				jLblsMannschaftenNeueSaison[i].setVisible(true);
			}
		} else {
			jLblBearbeiten.setText("Bearbeiten");
			for (int i = newSeasonTeamsOrder.size(); i < jLblsMannschaftenNeueSaison.length; i++) {
				jLblsMannschaftenNeueSaison[i].setVisible(false);
			}
			
			if (newSeasonTeamIndex != -1) {
				jLblsMannschaftenNeueSaison[newSeasonTeamIndex].setOpaque(false);
				repaintImmediately(jLblsMannschaftenNeueSaison[newSeasonTeamIndex]);
				newSeasonTeamIndex = -1;
				
				jLblName.setVisible(false);
				jTFName.setVisible(false);
				jLblDatum.setVisible(false);
				jTFDatum.setVisible(false);
				jBtnChangeTeamCompleted.setVisible(false);
			}
		}
	}
	
	private void changeTeam(int index) {
		if (newSeasonTeamIndex != -1) {
			jLblsMannschaftenNeueSaison[newSeasonTeamIndex].setOpaque(false);
			repaintImmediately(jLblsMannschaftenNeueSaison[newSeasonTeamIndex]);
		}
		
		newSeasonTeamIndex = index;
		jLblsMannschaftenNeueSaison[newSeasonTeamIndex].setOpaque(true);
		jLblsMannschaftenNeueSaison[newSeasonTeamIndex].setBackground(colorHighlighted);
		repaintImmediately(jLblsMannschaftenNeueSaison[newSeasonTeamIndex]);
		
		if (newSeasonTeamIndex < newSeasonTeamsOrder.size()) {
			jTFName.setText(newSeasonTeamsOrder.get(newSeasonTeamIndex).getName());
			jTFDatum.setText(newSeasonTeamsOrder.get(newSeasonTeamIndex).getFoundingDate());
		} else {
			jTFName.setText("");
			jTFDatum.setText("01.01.1970");
		}
		
		jLblName.setVisible(true);
		jTFName.setVisible(true);
		jLblDatum.setVisible(true);
		jTFDatum.setVisible(true);
		jBtnChangeTeamCompleted.setVisible(true);
		jTFName.requestFocus();
	}
	
	private void jBtnChangeTeamCompletedActionPerformed() {
		String name = jTFName.getText();
		if (name.isEmpty()) {
			message("Bitte Vereinsnamen angeben!");
			return;
		}
		
		String grDatum = jTFDatum.getText();
		if (name.isEmpty()) {
			message("Bitte Gründungsdatum angeben!");
			return;
		}
		int datum = MyDate.getDate(grDatum);
		grDatum = MyDate.datum(datum);
		
		if (newSeasonTeamIndex < newSeasonTeamsOrder.size()) {
			newSeasonTeamsOrder.get(newSeasonTeamIndex).setName(name);
			newSeasonTeamsOrder.get(newSeasonTeamIndex).setFoundingDate(grDatum);
		} else {
			Mannschaft mannschaft = new Mannschaft(newSeasonTeamIndex, null, name + ";" + grDatum);
			jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setVisible(true);
			jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setText(mannschaft.getName());
			newSeasonTeamsOrder.add(mannschaft);
		}
		
		jLblsMannschaftenNeueSaison[newSeasonTeamIndex].setOpaque(false);
		repaintImmediately(jLblsMannschaftenNeueSaison[newSeasonTeamIndex]);
		newSeasonTeamIndex = -1;
		
		jLblName.setVisible(false);
		jTFName.setVisible(false);
		jLblDatum.setVisible(false);
		jTFDatum.setVisible(false);
		jBtnChangeTeamCompleted.setVisible(false);
	}
	
	public void setConfigurationFromPreviousSeason(LigaSaison lSeason) {
		season = (lSeason.getYear() + 1);
		isSummerToSpringSeason = lSeason.isSTSS();
		setOldMannschaften(lSeason.getTeams());
		numberOfMatchesAgainstSameOpponent = lSeason.getNumberOfMatchesAgainstSameOpponent();
		setKickoffTimes(lSeason);
		goalDifference = lSeason.useGoalDifference();
		teamsHaveKader = lSeason.teamsHaveKader();
		setAnzahlen(lSeason);
		
		// TODO create more textfields, radiobuttons etc. to enable changing the config
		jLblSaison.setText("Saison " + season);
		jTFSaison.setText("" + season);
		(isSummerToSpringSeason ? jRBCalendarYearNo : jRBCalendarYearYes).setSelected(true);
		jTFSameOpponent.setText("" + numberOfMatchesAgainstSameOpponent);
		(goalDifference ? jRBGoalDifferenceYes : jRBGoalDifferenceNo).setSelected(true);
		(teamsHaveKader ? jRBTeamsHaveKaderYes : jRBTeamsHaveKaderNo).setSelected(true);
	}
	
	private void setOldMannschaften(Mannschaft[] mannschaften) {
		numberOfTeams = numberOfTeamsOld = mannschaften.length;
		jCBNumberOfTeams.setSelectedIndex(numberOfTeams - minNumberOfTeams);
		for (int i = 0; i < numberOfTeamsOld; i++) {
			oldSeasonTeamsOrder.add(mannschaften[i]);
			jLblsMannschaftenAlteSaison[i].setText(mannschaften[i].getName());
			jLblsMannschaftenAlteSaison[i].setVisible(true);
		}
	}
	
	private void setKickoffTimes(LigaSaison lSeason) {
		defaultKickoffTimes = lSeason.getDefaultKickoffTimes();
		kickOffTimes = new ArrayList<>();
		for (AnstossZeit kot : lSeason.getKickOffTimes()) {
			kickOffTimes.add(kot);
		}
		kickOffTimes.remove(0);
		int lastKOT = defaultKickoffTimes[defaultKickoffTimes.length - 1];
		while (lastKOT + 1 < kickOffTimes.size()) {
			kickOffTimes.remove(lastKOT);
		}
	}
	
	private void setAnzahlen(LigaSaison lSeason) {
		anzahl = new int[5];
		for (int i = 0; i < anzahl.length; i++) {
			anzahl[i] = lSeason.getAnzahl(i);
		}
	}
	
	private String getDefaultKickoffTimes() {
		String dktimes = "";
		if (defaultKickoffTimes.length >= 1) {
			dktimes += defaultKickoffTimes[0];
			for (int i = 1; i < defaultKickoffTimes.length; i++) {
				dktimes += "," + defaultKickoffTimes[i];
			}
		}
		
		return dktimes;
	}
	
	private String getAnzahlRepresentation() {
		String representation = "", sep = "";
		
		for (int i = 0; i < anzahl.length; i++) {
			representation += sep + anzahl[i];
			sep = ",";
		}
		
		return representation;
	}
	
	public void jBtnFertigActionPerformed() {
		if (numberOfTeams != newSeasonTeamsOrder.size()) {
			message("Bitte erst " + numberOfTeams + " Mannschaften angeben.");
			return;
		}
		
		String toString = "";
		
		toString += season + ";";
		toString += isSummerToSpringSeason + ";";
		toString += numberOfTeams + ";";
		toString += numberOfMatchesAgainstSameOpponent + ";";
		toString += getDefaultKickoffTimes() + ";";
		toString += goalDifference + ";";
		toString += teamsHaveKader + ";";
		toString += getAnzahlRepresentation() + ";";
		
		ArrayList<String> teamsNames = new ArrayList<>();
		for (int i = 0; i < newSeasonTeamsOrder.size(); i++) {
			teamsNames.add(newSeasonTeamsOrder.get(i).toString());
		}
		
		int numberOfDKOT = kickOffTimes.size();
		String defaultKickOffTimes = numberOfDKOT + ";";
		for (int i = 0; i < numberOfDKOT; i++) {
			defaultKickOffTimes += kickOffTimes.get(i) + ";";
		}
		Start.getInstance().jBtnNewLeagueSeasonDoneActionPerformed(toString, teamsNames, defaultKickOffTimes);
		setVisible(false);
	}
}
