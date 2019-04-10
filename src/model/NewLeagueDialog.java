package model;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class NewLeagueDialog extends JFrame {
	private static final long serialVersionUID = -4797487798345998331L;
	
	private final int minNumOfTeams = 2;
	private final int maxNumOfTeams = 24;
	private Color background = new Color(78, 235, 78);
	
	// Bounds
	private Dimension dim = new Dimension(410 + 6, 590 + 28);
	private Rectangle REC_GO = new Rectangle(320, 540, 70, 30);
	private Rectangle REC_CANCEL = new Rectangle(210, 540, 100, 30);
	
	
	// Allgemeine Informationen
	private Rectangle REC_INFOPNL = new Rectangle(20, 20, 370, 120);
	
	private Rectangle REC_NAMELBL = new Rectangle(5, 1, 90, 28);
	private Rectangle REC_SEASLBL = new Rectangle(5, 31, 100, 28);
	private Rectangle REC_CALYEARLBL = new Rectangle(175, 31, 80, 28);
	private Rectangle REC_SGDGLBL = new Rectangle(5, 61, 260, 28);
	private Rectangle REC_GDIFFLBL = new Rectangle(5, 91, 80, 28);
	private Rectangle REC_KADERLBL = new Rectangle(220, 91, 45, 28);
	
	private Rectangle REC_NAMETF = new Rectangle(105, 1, 259, 28);
	private Rectangle REC_SEASTF = new Rectangle(105, 31, 60, 28);
	private Rectangle REC_CALYEARYRB = new Rectangle(260, 31, 45, 28);
	private Rectangle REC_CALYEARNRB = new Rectangle(305, 31, 60, 28);
	private Rectangle REC_SGDGTF = new Rectangle(270, 61, 60, 28);
	private Rectangle REC_GDIFFYRB = new Rectangle(90, 91, 45, 28);
	private Rectangle REC_GDIFFNRB = new Rectangle(135, 91, 60, 28);
	private Rectangle REC_KADERYRB = new Rectangle(260, 91, 45, 28);
	private Rectangle REC_KADERNRB = new Rectangle(305, 91, 60, 28);
	
	
	// Mannschaften
	private Rectangle REC_TEAMPNL = new Rectangle(20, 150, 190, 380);
	private Rectangle REC_NOTEAMCB = new Rectangle(5, 6, 70, 28);
	private Rectangle REC_TEAMLBL = new Rectangle(80, 5, 90, 30);
	private Rectangle REC_TEAMSP = new Rectangle(5, 35, 180, 310);
	private Rectangle REC_EDITBTN = new Rectangle(5, 346, 180, 28);
	
	
	// Kick off times
	private Rectangle REC_KOTPNL = new Rectangle(220, 310, 170, 220);
	private Rectangle REC_KOTLBL = new Rectangle(5, 0, 90, 30);
	private Rectangle REC_KOTSP = new Rectangle(5, 30, 160, 105);
	private Rectangle REC_AKOTBTN = new Rectangle(10, 140, 45, 30);
	private Rectangle REC_EKOTBTN = new Rectangle(60, 140, 95, 30);
	private Rectangle REC_DEFKOTLBL = new Rectangle(5, 165, 90, 30);
	private Rectangle REC_DEFKOTTF = new Rectangle(1, 190, 168, 28);
	
	
	// Anzahlen
	private Rectangle REC_ANZPNL = new Rectangle(220, 150, 170, 150);
	private int[] anzLbls = new int[] {5, 0, 0, 0, 100, 30};
	private int[] anzTFs = new int[] {139, 1, 0, 2, 30, 28};
	
	// View
	private JButton go;
	private JButton cancel;
	
	
	// Allgemeine Informationen
	private JPanel infoPnl;
	private JLabel nameLbl;
	private JTextField nameTF;
	private JLabel seasonLbl;
	private JTextField seasonTF;
	private JLabel calendarYearLbl;
	private JRadioButton calendarYearYesRB;
	private JRadioButton calendarYearNoRB;
	private ButtonGroup calendarYearRBGrp;
	private JLabel sameOpponentLbl;
	private JTextField sameOpponentTF;
	private JLabel goalDifferenceLbl;
	private JRadioButton goalDifferenceYesRB;
	private JRadioButton goalDifferenceNoRB;
	private ButtonGroup goalDifferenceRBGrp;
	private JLabel teamsHaveKaderLbl;
	private JRadioButton teamsHaveKaderYesRB;
	private JRadioButton teamsHaveKaderNoRB;
	private ButtonGroup teamsHaveKaderRBGrp;
	
	
	// Mannschaften
	private JPanel teamsPnl;
	private JComboBox<String> numOfTeamsCB;
	private JLabel teamsLbl;
	private JScrollPane teamsSP;
	private JList<String> teamsL;
	private DefaultListModel<String> teamsModel;
	private JButton editTeamBtn;
	
	
	// Anzahlen
	private JPanel anzahlenPnl;
	private JLabel[] anzahlenLbls;
	private JTextField[] anzahlenTFs;
	
	
	// Kick off times
	private JPanel kotPnl;
	private JLabel kotLbl;
	private JScrollPane kotSP;
	private JList<String> kotL;
	private DefaultListModel<String> kotModel = new DefaultListModel<>();
	private JButton addKOTBtn;
	private JButton editKOTBtn;
	private JLabel defKOTLbl;
	private JTextField defaultKOTTF;
	
	
	// Model
	private String name;
	private int season;
	private int numberOfTeams;
	private int spGgSGegner;
	private ArrayList<String> teamsNames;
	private String anzahlenRep;
	private boolean isSTSS;
	private boolean goalDifference;
	private boolean teamsHaveKader;
	private String KOTsRep;
	private String defKOTsRep;
	
	
	public NewLeagueDialog() {
		super();
		
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		{
			go = new JButton();
			getContentPane().add(go);
			go.setBounds(REC_GO);
			go.setText("weiter");
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goActionPerformed();
				}
			});
		}
		{
			cancel = new JButton();
			getContentPane().add(cancel);
			cancel.setBounds(REC_CANCEL);
			cancel.setText("abbrechen");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelActionPerformed();
				}
			});
		}
		
		
		buildGeneralInfo();
		buildTeams();
		buildAmounts();
		buildKOT();
		
		enterPresetValues();
		
		setTitle("Neue Liga erstellen");
		setSize(dim);
		setResizable(false);
	}
	
	private void buildGeneralInfo() {
		{
			infoPnl = new JPanel();
			getContentPane().add(infoPnl);
			infoPnl.setLayout(null);
			infoPnl.setBounds(REC_INFOPNL);
			infoPnl.setOpaque(true);
			infoPnl.setBackground(background);
		}
		{
			nameLbl = new JLabel();
			infoPnl.add(nameLbl);
			nameLbl.setBounds(REC_NAMELBL);
			nameLbl.setText("Name der Liga");
		}
		{
			nameTF = new JTextField();
			infoPnl.add(nameTF);
			nameTF.setBounds(REC_NAMETF);
		}
		{
			seasonLbl = new JLabel();
			infoPnl.add(seasonLbl);
			seasonLbl.setBounds(REC_SEASLBL);
			seasonLbl.setText("Saison beginnt");
		}
		{
			seasonTF = new JTextField();
			infoPnl.add(seasonTF);
			seasonTF.setBounds(REC_SEASTF);
		}
		{
			calendarYearLbl = new JLabel();
			infoPnl.add(calendarYearLbl);
			calendarYearLbl.setBounds(REC_CALYEARLBL);
			calendarYearLbl.setText("Kalenderjahr");
		}
		{
			calendarYearYesRB = new JRadioButton("ja");
			infoPnl.add(calendarYearYesRB);
			calendarYearYesRB.setBounds(REC_CALYEARYRB);
			calendarYearYesRB.setActionCommand("false");
			calendarYearYesRB.setOpaque(false);
		}
		{
			calendarYearNoRB = new JRadioButton("nein");
			infoPnl.add(calendarYearNoRB);
			calendarYearNoRB.setBounds(REC_CALYEARNRB);
			calendarYearNoRB.setActionCommand("true");
			calendarYearNoRB.setOpaque(false);
		}
		{
			calendarYearRBGrp = new ButtonGroup();
			calendarYearRBGrp.add(calendarYearYesRB);
			calendarYearRBGrp.add(calendarYearNoRB);
		}
		{
			sameOpponentLbl = new JLabel();
			infoPnl.add(sameOpponentLbl);
			sameOpponentLbl.setBounds(REC_SGDGLBL);
			sameOpponentLbl.setText("Anzahl Spiele gegen denselben Gegner:");
		}
		{
			sameOpponentTF = new JTextField();
			infoPnl.add(sameOpponentTF);
			sameOpponentTF.setBounds(REC_SGDGTF);
		}
		{
			goalDifferenceLbl = new JLabel();
			infoPnl.add(goalDifferenceLbl);
			goalDifferenceLbl.setBounds(REC_GDIFFLBL);
			goalDifferenceLbl.setText("Tordifferenz");
		}
		{
			goalDifferenceYesRB = new JRadioButton("ja");
			infoPnl.add(goalDifferenceYesRB);
			goalDifferenceYesRB.setBounds(REC_GDIFFYRB);
			goalDifferenceYesRB.setActionCommand("true");
			goalDifferenceYesRB.setOpaque(false);
		}
		{
			goalDifferenceNoRB = new JRadioButton("nein");
			infoPnl.add(goalDifferenceNoRB);
			goalDifferenceNoRB.setBounds(REC_GDIFFNRB);
			goalDifferenceNoRB.setActionCommand("false");
			goalDifferenceNoRB.setOpaque(false);
		}
		{
			goalDifferenceRBGrp = new ButtonGroup();
			goalDifferenceRBGrp.add(goalDifferenceYesRB);
			goalDifferenceRBGrp.add(goalDifferenceNoRB);
		}
		{
			teamsHaveKaderLbl = new JLabel();
			infoPnl.add(teamsHaveKaderLbl);
			teamsHaveKaderLbl.setBounds(REC_KADERLBL);
			teamsHaveKaderLbl.setText("Kader");
		}
		{
			teamsHaveKaderYesRB = new JRadioButton("ja");
			infoPnl.add(teamsHaveKaderYesRB);
			teamsHaveKaderYesRB.setBounds(REC_KADERYRB);
			teamsHaveKaderYesRB.setActionCommand("true");
			teamsHaveKaderYesRB.setOpaque(false);
		}
		{
			teamsHaveKaderNoRB = new JRadioButton("nein");
			infoPnl.add(teamsHaveKaderNoRB);
			teamsHaveKaderNoRB.setBounds(REC_KADERNRB);
			teamsHaveKaderNoRB.setActionCommand("false");
			teamsHaveKaderNoRB.setOpaque(false);
		}
		{
			teamsHaveKaderRBGrp = new ButtonGroup();
			teamsHaveKaderRBGrp.add(teamsHaveKaderYesRB);
			teamsHaveKaderRBGrp.add(teamsHaveKaderNoRB);
		}
	}
	
	private void buildTeams() {
		String[] posNumOfTeams = new String[maxNumOfTeams - minNumOfTeams + 1];
		for (int i = 0; i < posNumOfTeams.length; i++) {
			posNumOfTeams[i] = "" + (i + minNumOfTeams);
		}
		{
			teamsPnl = new JPanel();
			getContentPane().add(teamsPnl);
			teamsPnl.setLayout(null);
			teamsPnl.setBounds(REC_TEAMPNL);
			teamsPnl.setOpaque(true);
			teamsPnl.setBackground(background);
		}
		{
			numOfTeamsCB = new JComboBox<>();
			teamsPnl.add(numOfTeamsCB);
			numOfTeamsCB.setBounds(REC_NOTEAMCB);
			numOfTeamsCB.setModel(new DefaultComboBoxModel<>(posNumOfTeams));
			numOfTeamsCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					numOfTeamsCBItemStateChanged(evt);
				}
			});
		}
		{
			teamsLbl = new JLabel();
			teamsPnl.add(teamsLbl);
			teamsLbl.setBounds(REC_TEAMLBL);
			teamsLbl.setText("Mannschaften");
		}
		teamsModel = new DefaultListModel<>();
		teamsModel.addElement("Mannschaft 1");
		teamsModel.addElement("Mannschaft 2");
		{
			teamsSP = new JScrollPane();
			teamsPnl.add(teamsSP);
			teamsSP.setBounds(REC_TEAMSP);
			{
				teamsL = new JList<>();
				teamsSP.setViewportView(teamsL);
				teamsL.setModel(teamsModel);
				teamsL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			editTeamBtn = new JButton();
			teamsPnl.add(editTeamBtn);
			editTeamBtn.setBounds(REC_EDITBTN);
			editTeamBtn.setText("Name bearbeiten");
			editTeamBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editTeamBtnActionPerformed();
				}
			});
		}
	}
	
	private void buildAmounts() {
		String[] anzLblsContent = new String[] {"CL / Aufst.", "CLQ / Auf.-Rel.", "EL", "Ab.-Relegation", "Absteiger"};
		anzahlenLbls = new JLabel[anzLblsContent.length];
		anzahlenTFs = new JTextField[anzLblsContent.length];
		{
			anzahlenPnl = new JPanel();
			getContentPane().add(anzahlenPnl);
			anzahlenPnl.setLayout(null);
			anzahlenPnl.setBounds(REC_ANZPNL);
			anzahlenPnl.setOpaque(true);
			anzahlenPnl.setBackground(background);
		}
		for (int i = 0; i < anzahlenLbls.length; i++) {
			anzahlenLbls[i] = new JLabel();
			anzahlenPnl.add(anzahlenLbls[i]);
			anzahlenLbls[i].setBounds(anzLbls[STARTX], anzLbls[STARTY] + i * (anzLbls[SIZEY] + anzLbls[GAPY]), anzLbls[SIZEX], anzLbls[SIZEY]);
			anzahlenLbls[i].setText(anzLblsContent[i]);
		}
		for (int i = 0; i < anzahlenTFs.length; i++) {
			anzahlenTFs[i] = new JTextField();
			anzahlenPnl.add(anzahlenTFs[i]);
			anzahlenTFs[i].setBounds(anzTFs[STARTX], anzTFs[STARTY] + i * (anzTFs[SIZEY] + anzTFs[GAPY]), anzTFs[SIZEX], anzTFs[SIZEY]);
		}
	}
	
	private void buildKOT() {
		{
			kotPnl = new JPanel();
			getContentPane().add(kotPnl);
			kotPnl.setLayout(null);
			kotPnl.setBounds(REC_KOTPNL);
			kotPnl.setOpaque(true);
			kotPnl.setBackground(background);
		}
		{
			kotLbl = new JLabel();
			kotPnl.add(kotLbl);
			kotLbl.setBounds(REC_KOTLBL);
			kotLbl.setText("Anstoßzeiten");
		}
		{
			kotSP = new JScrollPane();
			kotPnl.add(kotSP);
			kotSP.setBounds(REC_KOTSP);
			{
				kotL = new JList<>();
				kotSP.setViewportView(kotL);
				kotL.setModel(kotModel);
				kotL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			addKOTBtn = new JButton();
			kotPnl.add(addKOTBtn);
			addKOTBtn.setBounds(REC_AKOTBTN);
			addKOTBtn.setText("+");
			addKOTBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addKOTBtnActionPerformed();
				}
			});
		}
		{
			editKOTBtn = new JButton();
			kotPnl.add(editKOTBtn);
			editKOTBtn.setBounds(REC_EKOTBTN);
			editKOTBtn.setText("bearbeiten");
			editKOTBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editKOTBtnActionPerformed();
				}
			});
		}
		{
			defKOTLbl = new JLabel();
			kotPnl.add(defKOTLbl);
			defKOTLbl.setBounds(REC_DEFKOTLBL);
			defKOTLbl.setText("Standard:");
		}
		{
			defaultKOTTF = new JTextField();
			kotPnl.add(defaultKOTTF);
			defaultKOTTF.setBounds(REC_DEFKOTTF);
		}
	}
	
	private void enterPresetValues() {
		seasonTF.setText("" + Fussball.today().getYear());
		calendarYearNoRB.setSelected(true);
		goalDifferenceYesRB.setSelected(true);
		teamsHaveKaderNoRB.setSelected(true);
		sameOpponentTF.setText("2");
		numOfTeamsCB.setSelectedIndex(18 - minNumOfTeams);
	}
	
	private void editTeamBtnActionPerformed() {
		int index = teamsL.getSelectedIndex();
		if (index != -1) {
			String newName = null;
			boolean cancel = false;
			while((newName == null || newName.isEmpty()) && !cancel) {
				newName = inputDialog("Neuer Name für " + teamsModel.get(index));
				if (newName == null || newName.isEmpty()) {
					cancel = yesNoDialog("Sie haben keinen validen Namen eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
				}
			}
			if (!cancel)	teamsModel.set(index, newName);
		} else {
			message("Sie haben keine Mannschaft ausgewählt.");
		}
	}
	
	private void addKOTBtnActionPerformed() {
		String newKOT = null;
		boolean cancel = false, valid = false;
		while(!valid && !cancel) {
			newKOT = inputDialog("Neue Anstoßzeit:");
			valid = validateKOT(newKOT);
			
			if (!valid) {
				cancel = yesNoDialog("Sie haben keine valide Anstoßzeit eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
			}
		}
		if (!cancel)	kotModel.addElement(newKOT);
	}
	
	private void editKOTBtnActionPerformed() {
		int index = kotL.getSelectedIndex();
		if (index != -1) {
			String newKOT = null;
			boolean cancel = false, valid = false;
			while(!valid && !cancel) {
				newKOT = inputDialog("Verbesserte Anstoßzeit von " + kotModel.get(index));
				valid = validateKOT(newKOT);
				
				if (!valid) {
					cancel = yesNoDialog("Sie haben keine valide Anstoßzeit eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
				}
			}
			if (!cancel)	kotModel.set(index, newKOT);
		} else {
			message("Sie haben keine Anstoßzeit ausgewählt.");
		}
	}
	
	private boolean validateKOT(String kot) {
		// TODO invalid?
		
		return true;
	}
	
	private void numOfTeamsCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int oldNumOfTeams = teamsModel.getSize();
			int newNumOfTeams = numOfTeamsCB.getSelectedIndex() + minNumOfTeams;
			
			if (oldNumOfTeams < newNumOfTeams) {
				for (int i = oldNumOfTeams; i < newNumOfTeams; i++) {
					teamsModel.addElement("Mannschaft " + (i + 1));
				}
			} else {
				for (int i = oldNumOfTeams; i > newNumOfTeams; i--) {
					teamsModel.remove(i - 1);
				}
			}
		}
	}
	
	private String getAnzahlen() {
		String anzahlen = "", sep = "";
		
		for (int i = 0; i < anzahlenTFs.length; i++) {
			anzahlen += sep + Integer.parseInt(anzahlenTFs[i].getText());
			sep = ",";
		}
		
		return anzahlen;
	}
	
	private ArrayList<String> getTeamsNames() {
		ArrayList<String> teamsNames = new ArrayList<>();
		
		for (int i = 0; i < teamsModel.size(); i++) {
			teamsNames.add(teamsModel.getElementAt(i).toString() + ";01.01.1970;");
		}
		
		return teamsNames;
	}
	
	private String getKOTsRep() {
		String kotRepresentation = kotModel.getSize() + ";";
		
		for (int i = 0; i < kotModel.getSize(); i++) {
			kotRepresentation += kotModel.getElementAt(i).toString() + ";";
		}
		
		return kotRepresentation;
	}
	
	private String getDefaultKOTsRep() {
		int[] defaultKOTs = new int[numberOfTeams / 2];
		String allZero = "0";
		for (int i = 1; i < numberOfTeams / 2; i++) {
			allZero += ",0";
		}
		String[] defKOTs = defaultKOTTF.getText().split(",");
		
		if (defaultKOTs.length != defKOTs.length)	return allZero;
		
		for (int i = 0; i < defKOTs.length; i++) {
			try {
				defaultKOTs[i] = Integer.parseInt(defKOTs[i]);
				if (defaultKOTs[i] >= kotModel.getSize())	return allZero;
			} catch(NumberFormatException nfe) {
				return allZero;
			}
		}
		
		return defaultKOTTF.getText();
	}
	
	public void dispose() {
		int cancel = yesNoDialog("Sicher, dass Sie das Fenster schließen wollen? Dabei gehen die eingegebenen Daten verloren.");
		if (cancel == JOptionPane.YES_OPTION)	super.dispose();
	}
	
	private void cancelActionPerformed() {
		this.setVisible(false);
	}
	
	private void goActionPerformed() {
		name = nameTF.getText();
		season = Integer.parseInt(seasonTF.getText());
		numberOfTeams = numOfTeamsCB.getSelectedIndex() + minNumOfTeams;
		spGgSGegner = Integer.parseInt(sameOpponentTF.getText());
		teamsNames = getTeamsNames();
		anzahlenRep = getAnzahlen();
		isSTSS = Boolean.parseBoolean(calendarYearRBGrp.getSelection().getActionCommand());
		goalDifference = Boolean.parseBoolean(goalDifferenceRBGrp.getSelection().getActionCommand());
		teamsHaveKader = Boolean.parseBoolean(teamsHaveKaderRBGrp.getSelection().getActionCommand());
		KOTsRep = getKOTsRep();
		defKOTsRep = getDefaultKOTsRep();
		
		Fussball.getInstance().addNewLeague(name, season, isSTSS, numberOfTeams, spGgSGegner, defKOTsRep, goalDifference, teamsHaveKader, anzahlenRep, teamsNames, KOTsRep);
		
		this.setVisible(false);
		Fussball.getInstance().toFront();
	}
}
