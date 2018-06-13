package model;

import static util.Utilities.*;

import java.awt.Color;
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
	
	private Rectangle REC_LBLSEASON = new Rectangle(25, 15, 85, 25);
	private Rectangle REC_TFSEASON = new Rectangle(90, 15, 50, 25);
	
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
	private Rectangle REC_LBLEDIT = new Rectangle(220, 45, 70, 25);
	private Rectangle REC_BTNDONE = new Rectangle(500, 750, 80, 30);
	
	private int[] labels = new int[] {30, 80, 300, 25, 200, 20};
	
	private JPanel jPnlBackground;
	private JLabel jLblSeason;
	private JTextField jTFSeason;
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
	private JLabel jLblEdit;
	private JLabel[] jLblsTeamsNewSeason;
	private JLabel[] jLblsTeamsOldSeason;
	private JLabel jLblName;
	private JTextField jTFName;
	private JLabel jLblDate;
	private JTextField jTFDate;
	private JButton jBtnChangeTeamCompleted;
	private JButton jBtnDone;
	
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
			jLblSeason = new JLabel();
			jPnlBackground.add(jLblSeason);
			jLblSeason.setBounds(REC_LBLSEASON);
		}
		{
			jTFSeason = new JTextField();
			jPnlBackground.add(jTFSeason);
			jTFSeason.setBounds(REC_TFSEASON);
			jTFSeason.setOpaque(true);
			jTFSeason.setVisible(false);
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
			jLblEdit = new JLabel();
			jPnlBackground.add(jLblEdit);
			jLblEdit.setBounds(REC_LBLEDIT);
			jLblEdit.setText("Bearbeiten");
			jLblEdit.setCursor(handCursor);
			jLblEdit.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					changeMode();
				}
			});
		}
		
		jLblsTeamsNewSeason = new JLabel[maxNumberOfTeams];
		jLblsTeamsOldSeason = new JLabel[maxNumberOfTeams];
		for (int i = 0; i < jLblsTeamsNewSeason.length; i++) {
			final int x = i;
			jLblsTeamsNewSeason[i] = new JLabel();
			jPnlBackground.add(jLblsTeamsNewSeason[i]);
			jLblsTeamsNewSeason[i].setBounds(labels[STARTX], labels[STARTY] + i * labels[GAPY], labels[SIZEX], labels[SIZEY]);
			jLblsTeamsNewSeason[i].setText("n/a");
			jLblsTeamsNewSeason[i].setVisible(false);
			jLblsTeamsNewSeason[i].setCursor(handCursor);
			jLblsTeamsNewSeason[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					putNewTeamToNextFreePosition(x);
				}
			});
			
			jLblsTeamsOldSeason[i] = new JLabel();
			jPnlBackground.add(jLblsTeamsOldSeason[i]);
			jLblsTeamsOldSeason[i].setBounds(labels[STARTX] + labels[GAPX], labels[STARTY] + i * labels[GAPY], labels[SIZEX], labels[SIZEY]);
			jLblsTeamsOldSeason[i].setOpaque(false);
			jLblsTeamsOldSeason[i].setVisible(false);
			jLblsTeamsOldSeason[i].setCursor(handCursor);
			jLblsTeamsOldSeason[i].addMouseListener(new MouseAdapter() {
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
			jLblDate = new JLabel();
			jPnlBackground.add(jLblDate);
			jLblDate.setText("Gründungsdatum");
			jLblDate.setVisible(false);
		}
		{
			jTFDate = new JTextField();
			jPnlBackground.add(jTFDate);
			jTFDate.setVisible(false);
			jTFDate.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent evt) {
					jTFDate.selectAll();
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
			jBtnDone = new JButton();
			jPnlBackground.add(jBtnDone);
			jBtnDone.setBounds(REC_BTNDONE);
			jBtnDone.setText("Fertig");
			jBtnDone.setFocusable(false);
			jBtnDone.addActionListener(new ActionListener() {
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
			season = Integer.parseInt(jTFSeason.getText());
			isSummerToSpringSeason = jRBCalendarYearNo.isSelected();
			numberOfMatchesAgainstSameOpponent = Integer.parseInt(jTFSameOpponent.getText());
			goalDifference = jRBGoalDifferenceYes.isSelected();
			teamsHaveKader = jRBTeamsHaveKaderYes.isSelected();
			jLblConfiguration.setText("Konfiguration ändern");
			jLblSeason.setText("Saison " + season);
			
		} else {
			jLblConfiguration.setText("Konfiguration speichern");
			jLblSeason.setText("Saison");
		}
		
		changingConfiguration = !changingConfiguration;
		jCBNumberOfTeams.setVisible(!changingConfiguration);
		jLblNumberOfTeams.setVisible(!changingConfiguration);
		jLblEdit.setVisible(!changingConfiguration);
		for (int i = 0; i < jLblsTeamsOldSeason.length; i++) {
			jLblsTeamsOldSeason[i].setVisible(!changingConfiguration && !jLblsTeamsOldSeason[i].getText().equals(""));
			jLblsTeamsNewSeason[i].setVisible(!changingConfiguration && !jLblsTeamsNewSeason[i].getText().equals("n/a"));
		}
		
		jTFSeason.setVisible(changingConfiguration);
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
			Rectangle REC_LBLDATE = new Rectangle(30, starty + 40, 120, 25);
			Rectangle REC_TFDATE = new Rectangle(160, starty + 40, 100, 25);
			Rectangle REC_BTNCTCOMPLETE = new Rectangle(180, starty + 75, 80, 25);
			
			jLblName.setBounds(REC_LBLNAME);
			jTFName.setBounds(REC_TFNAME);
			jLblDate.setBounds(REC_LBLDATE);
			jTFDate.setBounds(REC_TFDATE);
			jBtnChangeTeamCompleted.setBounds(REC_BTNCTCOMPLETE);
		}
	}
	
	private void putNewTeamToNextFreePosition(int index) {
		if (teamChangeMode) {
			changeTeam(index);
			return;
		}
		jLblsTeamsOldSeason[oldSeasonTeamsOrder.size()].setVisible(true);
		jLblsTeamsOldSeason[oldSeasonTeamsOrder.size()].setText(newSeasonTeamsOrder.get(index).getName());
		oldSeasonTeamsOrder.add(newSeasonTeamsOrder.remove(index));
		jLblsTeamsNewSeason[newSeasonTeamsOrder.size()].setVisible(false);
		for (int i = index; i < newSeasonTeamsOrder.size(); i++) {
			jLblsTeamsNewSeason[i].setText(newSeasonTeamsOrder.get(i).getName());
		}
		jLblsTeamsNewSeason[newSeasonTeamsOrder.size()].setText("n/a");
	}
	
	private void putOldTeamToNextFreePosition(int index) {
		if (oldSeasonTeamsOrder.size() <= anzahl[4]) {
			if(yesNoDialog("Laut Konfiguration müssen " + anzahl[4] + " Mannschaften absteigen. Trotzdem fortfahren?") == JOptionPane.NO_OPTION)	return;
		}
		jLblsTeamsNewSeason[newSeasonTeamsOrder.size()].setVisible(true);
		jLblsTeamsNewSeason[newSeasonTeamsOrder.size()].setText(oldSeasonTeamsOrder.get(index).getName());
		newSeasonTeamsOrder.add(oldSeasonTeamsOrder.remove(index));
		jLblsTeamsOldSeason[oldSeasonTeamsOrder.size()].setVisible(false);
		for (int i = index; i < oldSeasonTeamsOrder.size(); i++) {
			jLblsTeamsOldSeason[i].setText(oldSeasonTeamsOrder.get(i).getName());
		}
		jLblsTeamsOldSeason[oldSeasonTeamsOrder.size()].setText("");
	}
	
	private void changeMode() {
		teamChangeMode = !teamChangeMode;
		if (teamChangeMode) {
			jLblEdit.setText("Fertig");
			for (int i = 0; i < numberOfTeams; i++) {
				jLblsTeamsNewSeason[i].setVisible(true);
			}
		} else {
			jLblEdit.setText("Bearbeiten");
			for (int i = newSeasonTeamsOrder.size(); i < jLblsTeamsNewSeason.length; i++) {
				jLblsTeamsNewSeason[i].setVisible(false);
			}
			
			if (newSeasonTeamIndex != -1) {
				jLblsTeamsNewSeason[newSeasonTeamIndex].setOpaque(false);
				repaintImmediately(jLblsTeamsNewSeason[newSeasonTeamIndex]);
				newSeasonTeamIndex = -1;
				
				jLblName.setVisible(false);
				jTFName.setVisible(false);
				jLblDate.setVisible(false);
				jTFDate.setVisible(false);
				jBtnChangeTeamCompleted.setVisible(false);
			}
		}
	}
	
	private void changeTeam(int index) {
		if (newSeasonTeamIndex != -1) {
			jLblsTeamsNewSeason[newSeasonTeamIndex].setOpaque(false);
			repaintImmediately(jLblsTeamsNewSeason[newSeasonTeamIndex]);
		}
		
		newSeasonTeamIndex = index;
		jLblsTeamsNewSeason[newSeasonTeamIndex].setOpaque(true);
		jLblsTeamsNewSeason[newSeasonTeamIndex].setBackground(colorHighlighted);
		repaintImmediately(jLblsTeamsNewSeason[newSeasonTeamIndex]);
		
		if (newSeasonTeamIndex < newSeasonTeamsOrder.size()) {
			jTFName.setText(newSeasonTeamsOrder.get(newSeasonTeamIndex).getName());
			jTFDate.setText(newSeasonTeamsOrder.get(newSeasonTeamIndex).getFoundingDate());
		} else {
			jTFName.setText("");
			jTFDate.setText(UNIX_EPOCH.withDividers());
		}
		
		jLblName.setVisible(true);
		jTFName.setVisible(true);
		jLblDate.setVisible(true);
		jTFDate.setVisible(true);
		jBtnChangeTeamCompleted.setVisible(true);
		jTFName.requestFocus();
	}
	
	private void jBtnChangeTeamCompletedActionPerformed() {
		String name = jTFName.getText();
		if (name.isEmpty()) {
			message("Bitte Vereinsnamen angeben!");
			return;
		}
		
		if (name.isEmpty()) {
			message("Bitte Gründungsdatum angeben!");
			return;
		}
		String grDatum = Datum.parse(jTFDate.getText()).withDividers();
		
		if (newSeasonTeamIndex < newSeasonTeamsOrder.size()) {
			newSeasonTeamsOrder.get(newSeasonTeamIndex).setName(name);
			newSeasonTeamsOrder.get(newSeasonTeamIndex).setFoundingDate(grDatum);
		} else {
			Mannschaft mannschaft = new Mannschaft(newSeasonTeamIndex, null, name + ";" + grDatum);
			jLblsTeamsNewSeason[newSeasonTeamsOrder.size()].setVisible(true);
			jLblsTeamsNewSeason[newSeasonTeamsOrder.size()].setText(mannschaft.getName());
			newSeasonTeamsOrder.add(mannschaft);
		}
		
		jLblsTeamsNewSeason[newSeasonTeamIndex].setOpaque(false);
		repaintImmediately(jLblsTeamsNewSeason[newSeasonTeamIndex]);
		newSeasonTeamIndex = -1;
		
		jLblName.setVisible(false);
		jTFName.setVisible(false);
		jLblDate.setVisible(false);
		jTFDate.setVisible(false);
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
		jLblSeason.setText("Saison " + season);
		jTFSeason.setText("" + season);
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
			jLblsTeamsOldSeason[i].setText(mannschaften[i].getName());
			jLblsTeamsOldSeason[i].setVisible(true);
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
		while (lastKOT < kickOffTimes.size()) {
			kickOffTimes.remove(lastKOT);
		}
	}
	
	private void setAnzahlen(LigaSaison lSeason) {
		anzahl = new int[5];
		for (int i = 0; i < anzahl.length; i++) {
			anzahl[i] = lSeason.getNumberOf(i);
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
