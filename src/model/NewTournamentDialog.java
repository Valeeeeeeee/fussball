package model;

import static util.Utilities.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NewTournamentDialog extends JFrame {
	private static final long serialVersionUID = -4797487798345998331L;

	private Start start;

	private Color background = new Color(78, 235, 78);
	
	private final int minNumberOfGroups = 1;
	private final int maxNumberOfGroups = 12;
	private final int minNumberOfKORounds = 1;
	private final int maxNumberOfKORounds = 6;

	private final int GAPPNL = 10;
	private final int OFFSETX = 30;
	private final int OFFSETY = 30;
	
	// Bounds
	private Dimension dim = new Dimension(410, 600);
	private Rectangle RECGO = new Rectangle(310, 530, 70, 30);
	private Rectangle RECCANCEL = new Rectangle(220, 530, 90, 30);

	// Allgemeine Informationen
	private Rectangle RECINFOPNL = new Rectangle(OFFSETX, OFFSETY, 350, 120);

	private Rectangle RECNAMELBL = new Rectangle(5, 0, 120, 30);
	private Rectangle RECNAMETF = new Rectangle(145, 0, 205, 30);
	private Rectangle RECSTDLBL = new Rectangle(5, 30, 90, 30); // checked
	private Rectangle RECSTDTF = new Rectangle(145, 30, 90, 30); // checked
	private Rectangle RECFIDLBL = new Rectangle(5, 60, 75, 30); // checked
	private Rectangle RECFIDTF = new Rectangle(145, 60, 90, 30); // checked
	private Rectangle RECH3PLBL = new Rectangle(240, 30, 100, 30); // checked
	private Rectangle RECH3PYESRB = new Rectangle(240, 60, 45, 30); // checked
	private Rectangle RECH3PNORB = new Rectangle(290, 60, 60, 30); // checked
	private Rectangle RECNOFTLBL = new Rectangle(5, 90, 135, 30);
	private Rectangle RECNOFTTF = new Rectangle(145, 90, 50, 30);
	private Rectangle RECSHNLBL = new Rectangle(240, 90, 65, 30);
	private Rectangle RECSHNTF = new Rectangle(310, 90, 40, 30);

	// Group stage
	private Rectangle RECGRPPNL = new Rectangle(OFFSETX, OFFSETY + RECINFOPNL.height + GAPPNL, 350, 150);
	private Rectangle RECGRPLBL = new Rectangle(5, 0, 90, 30);
	private Rectangle RECGRPYES = new Rectangle(100, 0, 45, 30); // checked
	private Rectangle RECGRPNO = new Rectangle(140, 0, 60, 30); // checked
	private Rectangle RECNOFGRPCB = new Rectangle(5, 30, 70, 30);
	private Rectangle RECNOFGRPLBL = new Rectangle(80, 30, 70, 30);
	private Rectangle RECGRP2LEGLBL = new Rectangle(175, 30, 70, 30); // checked
	private Rectangle RECGRP2LEGYES = new Rectangle(250, 30, 45, 30); // checked
	private Rectangle RECGRP2LEGNO = new Rectangle(290, 30, 60, 30); // checked
	private Rectangle RECTNGRPCB = new Rectangle(5, 60, 120, 30); // checked

	// Knockout stage
	private Rectangle RECKOPNL = new Rectangle(OFFSETX, OFFSETY + RECINFOPNL.height + RECGRPPNL.height + 2 * GAPPNL, 350, 180);
	private Rectangle RECKOLBL = new Rectangle(5, 0, 90, 30);
	private Rectangle RECKOYES = new Rectangle(100, 0, 45, 30); // checked
	private Rectangle RECKONO = new Rectangle(140, 0, 60, 30); // checked
	private Rectangle RECNOFKOCB = new Rectangle(5, 30, 70, 30);
	private Rectangle RECNOFKOLBL = new Rectangle(80, 30, 80, 30);
	private Rectangle RECKO2LEGLBL = new Rectangle(175, 30, 70, 30); // checked
	private Rectangle RECKO2LEGYES = new Rectangle(250, 30, 45, 30); // checked
	private Rectangle RECKO2LEGNO = new Rectangle(290, 30, 60, 30); // checked

	// View
	private JButton go;
	private JButton cancel;

	// Allgemeine Informationen
	private JPanel infoPnl;
	private JLabel nameLbl;
	private JTextField nameTF;
	private JLabel startDateLbl;
	private JLabel finalDateLbl;
	private JTextField startDateTF;
	private JTextField finalDateTF;
	private JLabel hasThirdPlaceLbl;
	private JRadioButton hasThirdPlaceYesRB;
	private JRadioButton hasThirdPlaceNoRB;
	private ButtonGroup hasThirdPlaceRBGrp;
	private JLabel numberOfTeamsLbl;
	private JTextField numberOfTeamsTF;
	private JLabel shortNameLbl;
	private JTextField shortNameTF;

	// Group stage
	private JPanel groupStagePnl;
	private JLabel groupStageLbl;
	private JRadioButton groupStageYesRB;
	private JRadioButton groupStageNoRB;
	private ButtonGroup groupStageRBGrp;
	private JComboBox numOfGroupsCB;
	private JLabel numOfGroupsLbl;
	private JLabel groupSecondLegLbl;
	private JRadioButton groupSecondLegYesRB;
	private JRadioButton groupSecondLegNoRB;
	private ButtonGroup groupSecondLegRBGrp;
	private JComboBox teamsNamesGrpCB;

	// Knockout stage
	private JPanel koStagePnl;
	private JLabel koStageLbl;
	private JRadioButton koStageYesRB;
	private JRadioButton koStageNoRB;
	private ButtonGroup koStageRBGrp;
	private JComboBox numOfKORoundsCB;
	private JLabel numOfKORoundsLbl;
	private JLabel koSecondLegLbl;
	private JRadioButton koSecondLegYesRB;
	private JRadioButton koSecondLegNoRB;
	private ButtonGroup koSecondLegRBGrp;
//	private JScrollPane kotSP;
//	private JList kotL;
//	private DefaultListModel kotModel = new DefaultListModel();

	// Model
	private String name;
	private String shortName;
	private int season;
	private int stDate;
	private int fiDate;
	private boolean isSTSS;
	private boolean hasGrp;
	private boolean hasKO;
	private boolean grp2leg;
	private boolean ko2leg;
	private boolean has3pl;
	private int nOTeam;
	private int nOGrp;
	private int nOKO;
	private String[][] teamsGrp;
	private String[][] teamsKO;
	
	
	private DefaultListModel[] grpTeamsModelArr;
	private DefaultListModel grpTeamsModel;


	public NewTournamentDialog(Start start) {
		super();
		
		this.start = start;

		initGUI();
	}

	private void initGUI() {
		this.setLayout(null);

		{
			go = new JButton();
			getContentPane().add(go);
			go.setBounds(RECGO);
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
			cancel.setBounds(RECCANCEL);
			cancel.setText("abbrechen");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelActionPerformed();
				}
			});
		}
//
		buildGeneralInfo();
		buildGroupStage();
		buildKOStage();
		
		boolean enterPresetValues = false;
		if (enterPresetValues)	enterPresetValues();

		setTitle("Neues Turnier erstellen");
		setSize(this.dim);
		setResizable(false);
	}
	
	private void buildGeneralInfo() {
		{
			infoPnl = new JPanel();
			getContentPane().add(infoPnl);
			infoPnl.setLayout(null);
			infoPnl.setBounds(RECINFOPNL);
			infoPnl.setOpaque(true);
			infoPnl.setBackground(background);
		}
		// Name
		{
			nameLbl = new JLabel();
			infoPnl.add(nameLbl);
			nameLbl.setBounds(RECNAMELBL);
			nameLbl.setText("Name des Turniers");
		}
		{
			nameTF = new JTextField();
			infoPnl.add(nameTF);
			nameTF.setBounds(RECNAMETF);
		}
		// Start- und Enddatum
		{
			startDateLbl = new JLabel();
			infoPnl.add(startDateLbl);
			startDateLbl.setBounds(RECSTDLBL);
			startDateLbl.setText("Turnierbeginn");
		}
		{
			startDateTF = new JTextField();
			infoPnl.add(startDateTF);
			startDateTF.setBounds(RECSTDTF);
		}
		{
			finalDateLbl = new JLabel();
			infoPnl.add(finalDateLbl);
			finalDateLbl.setBounds(RECFIDLBL);
			finalDateLbl.setText("Turnierende");
		}
		{
			finalDateTF = new JTextField();
			infoPnl.add(finalDateTF);
			finalDateTF.setBounds(RECFIDTF);
		}
		// Spiel um Platz 3
		{
			hasThirdPlaceLbl = new JLabel();
			infoPnl.add(hasThirdPlaceLbl);
			hasThirdPlaceLbl.setBounds(RECH3PLBL);
			hasThirdPlaceLbl.setText("Spiel um Platz 3");
		}
		{
			hasThirdPlaceYesRB = new JRadioButton("ja");
			infoPnl.add(hasThirdPlaceYesRB);
			hasThirdPlaceYesRB.setBounds(RECH3PYESRB);
			hasThirdPlaceYesRB.setActionCommand("true");
		}
		{
			hasThirdPlaceNoRB = new JRadioButton("nein");
			infoPnl.add(hasThirdPlaceNoRB);
			hasThirdPlaceNoRB.setBounds(RECH3PNORB);
			hasThirdPlaceNoRB.setActionCommand("false");
		}
		{
			hasThirdPlaceRBGrp = new ButtonGroup();
			hasThirdPlaceRBGrp.add(hasThirdPlaceYesRB);
			hasThirdPlaceRBGrp.add(hasThirdPlaceNoRB);
		}
		// Anzahl teilnehmende Mannschaften
		{
			numberOfTeamsLbl = new JLabel();
			infoPnl.add(numberOfTeamsLbl);
			numberOfTeamsLbl.setBounds(RECNOFTLBL);
			numberOfTeamsLbl.setText("Anzahl Mannschaften");
		}
		{
			numberOfTeamsTF = new JTextField();
			infoPnl.add(numberOfTeamsTF);
			numberOfTeamsTF.setBounds(RECNOFTTF);
		}
		// Kurzname
		{
			shortNameLbl = new JLabel();
			infoPnl.add(shortNameLbl);
			shortNameLbl.setBounds(RECSHNLBL);
			shortNameLbl.setText("Kurzname");
		}
		{
			shortNameTF = new JTextField();
			infoPnl.add(shortNameTF);
			shortNameTF.setBounds(RECSHNTF);
		}
	}

	private void buildGroupStage() {
		String[] posNumOfGroups = new String[maxNumberOfGroups - minNumberOfGroups + 1];
		for (int i = 0; i < posNumOfGroups.length; i++) {
			posNumOfGroups[i] = "" + (i + 1);
		}
		{
			groupStagePnl = new JPanel();
			getContentPane().add(groupStagePnl);
			groupStagePnl.setLayout(null);
			groupStagePnl.setBounds(RECGRPPNL);
			groupStagePnl.setOpaque(true);
			groupStagePnl.setBackground(background);
		}
		// gibt es eine Gruppenphase
		{
			groupStageLbl = new JLabel();
			groupStagePnl.add(groupStageLbl);
			groupStageLbl.setBounds(RECGRPLBL);
			groupStageLbl.setText("Gruppenphase");
		}
		{
			groupStageYesRB = new JRadioButton("ja");
			groupStagePnl.add(groupStageYesRB);
			groupStageYesRB.setBounds(RECGRPYES);
			groupStageYesRB.setActionCommand("true");
			groupStageYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					groupStageRBchanged(true);
				}
			});
		}
		{
			groupStageNoRB = new JRadioButton("nein");
			groupStagePnl.add(groupStageNoRB);
			groupStageNoRB.setBounds(RECGRPNO);
			groupStageNoRB.setActionCommand("false");
			groupStageNoRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					groupStageRBchanged(false);
				}
			});
		}
		{
			groupStageRBGrp = new ButtonGroup();
			groupStageRBGrp.add(groupStageYesRB);
			groupStageRBGrp.add(groupStageNoRB);
		}
		// Anzahl Gruppen
		{
			numOfGroupsCB = new JComboBox();
			groupStagePnl.add(numOfGroupsCB);
			numOfGroupsCB.setBounds(RECNOFGRPCB);
			numOfGroupsCB.setModel(new DefaultComboBoxModel(posNumOfGroups));
			numOfGroupsCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					numOfGroupsCBItemStateChanged(evt);
				}
			});
		}
		{
			numOfGroupsLbl = new JLabel();
			groupStagePnl.add(numOfGroupsLbl);
			numOfGroupsLbl.setBounds(RECNOFGRPLBL);
			numOfGroupsLbl.setText("Gruppen");
		}
		// Rueckspiel
		{
			groupSecondLegLbl = new JLabel();
			groupStagePnl.add(groupSecondLegLbl);
			groupSecondLegLbl.setBounds(RECGRP2LEGLBL);
			groupSecondLegLbl.setText("Rueckspiel");
		}
		{
			groupSecondLegYesRB = new JRadioButton("ja");
			groupStagePnl.add(groupSecondLegYesRB);
			groupSecondLegYesRB.setBounds(RECGRP2LEGYES);
			groupSecondLegYesRB.setActionCommand("true");
		}
		{
			groupSecondLegNoRB = new JRadioButton("nein");
			groupStagePnl.add(groupSecondLegNoRB);
			groupSecondLegNoRB.setBounds(RECGRP2LEGNO);
			groupSecondLegNoRB.setActionCommand("false");
		}
		{
			groupSecondLegRBGrp = new ButtonGroup();
			groupSecondLegRBGrp.add(groupSecondLegYesRB);
			groupSecondLegRBGrp.add(groupSecondLegNoRB);
		}
		// Teamnamen
		{
			teamsNamesGrpCB = new JComboBox();
			groupStagePnl.add(teamsNamesGrpCB);
			teamsNamesGrpCB.setBounds(RECTNGRPCB);
			teamsNamesGrpCB.setModel(new DefaultComboBoxModel(posNumOfGroups));
			teamsNamesGrpCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					teamsNamesGrpCBItemStateChanged(evt);
				}
			});
		}
	}
	
	private void buildKOStage() {
		String[] posNumOfKORounds = new String[maxNumberOfKORounds - minNumberOfKORounds + 1];
		for (int i = 0; i < posNumOfKORounds.length; i++) {
			posNumOfKORounds[i] = "" + (i + 1);
		}
		{
			koStagePnl = new JPanel();
			getContentPane().add(koStagePnl);
			koStagePnl.setLayout(null);
			koStagePnl.setBounds(RECKOPNL);
			koStagePnl.setOpaque(true);
			koStagePnl.setBackground(background);
		}
		// gibt es eine KO Phase
		{
			koStageLbl = new JLabel();
			koStagePnl.add(koStageLbl);
			koStageLbl.setBounds(RECKOLBL);
			koStageLbl.setText("KO-Phase");
		}
		{
			koStageYesRB = new JRadioButton("ja");
			koStagePnl.add(koStageYesRB);
			koStageYesRB.setBounds(RECKOYES);
			koStageYesRB.setActionCommand("true");
			koStageYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					koStageRBchanged(true);
				}
			});
		}
		{
			koStageNoRB = new JRadioButton("nein");
			koStagePnl.add(koStageNoRB);
			koStageNoRB.setBounds(RECKONO);
			koStageNoRB.setActionCommand("false");
			koStageNoRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					koStageRBchanged(false);
				}
			});
		}
		{
			koStageRBGrp = new ButtonGroup();
			koStageRBGrp.add(koStageYesRB);
			koStageRBGrp.add(koStageNoRB);
		}
		// Anzahl KO-Runden
		{
			numOfKORoundsCB = new JComboBox();
			koStagePnl.add(numOfKORoundsCB);
			numOfKORoundsCB.setBounds(RECNOFKOCB);
			numOfKORoundsCB.setModel(new DefaultComboBoxModel(posNumOfKORounds));
			numOfKORoundsCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					numOfKORoundsCBItemStateChanged(evt);
				}
			});
		}
		{
			numOfKORoundsLbl = new JLabel();
			koStagePnl.add(numOfKORoundsLbl);
			numOfKORoundsLbl.setBounds(RECNOFKOLBL);
			numOfKORoundsLbl.setText("KO-Runden");
		}
		// Rueckspiel
		{
			koSecondLegLbl = new JLabel();
			koStagePnl.add(koSecondLegLbl);
			koSecondLegLbl.setBounds(RECKO2LEGLBL);
			koSecondLegLbl.setText("Rueckspiel");
		}
		{
			koSecondLegYesRB = new JRadioButton("ja");
			koStagePnl.add(koSecondLegYesRB);
			koSecondLegYesRB.setBounds(RECKO2LEGYES);
			koSecondLegYesRB.setActionCommand("true");
		}
		{
			koSecondLegNoRB = new JRadioButton("nein");
			koStagePnl.add(koSecondLegNoRB);
			koSecondLegNoRB.setBounds(RECKO2LEGNO);
			koSecondLegNoRB.setActionCommand("false");
		}
		{
			koSecondLegRBGrp = new ButtonGroup();
			koSecondLegRBGrp.add(koSecondLegYesRB);
			koSecondLegRBGrp.add(koSecondLegNoRB);
		}
//		{
//			kotSP = new JScrollPane();
//			kotPnl.add(kotSP);
//			kotSP.setBounds(RECKOTSP);
//			{
//				kotL = new JList();
//				kotSP.setViewportView(kotL);
//				kotL.setModel(kotModel);
//				kotL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			}
//		}
//		{
//			defKOTLbl = new JLabel();
//			kotPnl.add(defKOTLbl);
//			defKOTLbl.setBounds(RECDEFKOTLBL);
//			defKOTLbl.setText("Standard:");
//		}
//		{
//			defaultKOTTF = new JTextField();
//			kotPnl.add(defaultKOTTF);
//			defaultKOTTF.setBounds(RECDEFKOTTF);
//		}
	}
	
	private void refreshTeamsNamesGrpCBModel() {
		String[] gruppenNamen = new String[nOGrp];
		for (int i = 0; i < gruppenNamen.length; i++) {
			gruppenNamen[i] = "Gruppe " + (i + 1);
		}
		teamsNamesGrpCB.setModel(new DefaultComboBoxModel(gruppenNamen));
	}

	
	private void enterPresetValues() {
		// TODO remove these preentered values
		
		nameTF.setText("Champions League");
		shortNameTF.setText("CL");
		
		startDateTF.setText("16.09.2014");
		finalDateTF.setText("06.06.2015");
		hasThirdPlaceNoRB.setSelected(true);
		numberOfTeamsTF.setText("32");
		
		groupStageYesRB.setSelected(true);
		groupStageRBchanged(true);
		numOfGroupsCB.setSelectedIndex(8 - minNumberOfGroups);
		groupSecondLegYesRB.setSelected(true);
		
		koStageYesRB.setSelected(true);
		koStageRBchanged(true);
		numOfKORoundsCB.setSelectedIndex(4 - minNumberOfKORounds);
		koSecondLegYesRB.setSelected(true);
	}
	
	private void groupStageRBchanged(boolean hasGrp) {
		// Alles was mit Gruppeneinstellung zu tun hat disablen
		log("Group stage has been " + (hasGrp ? "enabled." : "disabled."));
		
		numOfGroupsCB.setEnabled(hasGrp);
		numOfGroupsLbl.setEnabled(hasGrp);
		groupSecondLegLbl.setEnabled(hasGrp);
		groupSecondLegYesRB.setEnabled(hasGrp);
		groupSecondLegNoRB.setEnabled(hasGrp);
	}
	
	private void koStageRBchanged(boolean hasGrp) {
		// Alles was mit KO-Rundeneinstellung zu tun hat disablen
		log("Knockout stage has been " + (hasGrp ? "enabled." : "disabled."));
		
		numOfKORoundsCB.setEnabled(hasGrp);
		numOfKORoundsLbl.setEnabled(hasGrp);
		koSecondLegLbl.setEnabled(hasGrp);
		koSecondLegYesRB.setEnabled(hasGrp);
		koSecondLegNoRB.setEnabled(hasGrp);
	}
	
	private void numOfGroupsCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int oldNumOfGroups = nOGrp;
			int newNumOfGroups = numOfGroupsCB.getSelectedIndex() + minNumberOfGroups;
			
			if (oldNumOfGroups > newNumOfGroups) { // wird weniger
				DefaultListModel[] oldDLMA = grpTeamsModelArr;
				grpTeamsModelArr = new DefaultListModel[newNumOfGroups];
				for (int i = 0; i < newNumOfGroups; i++) {
					grpTeamsModelArr[i] = oldDLMA[i];
				}
			} else { // gleich viel oder mehr
				DefaultListModel[] oldDLMA = grpTeamsModelArr;
				grpTeamsModelArr = new DefaultListModel[newNumOfGroups];
				for (int i = 0; i < oldNumOfGroups; i++) {
					grpTeamsModelArr[i] = oldDLMA[i];
				}
				for (int i = oldNumOfGroups; i < newNumOfGroups; i++) {
					grpTeamsModelArr[i] = new DefaultListModel();
				}
			}
			
			nOGrp = newNumOfGroups;
			refreshTeamsNamesGrpCBModel();
		}
	}
	
	private void teamsNamesGrpCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			
		}
	}
	
	private void numOfKORoundsCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int oldNumOfKORounds = nOKO;
			int newNumOfKORounds = numOfKORoundsCB.getSelectedIndex() + minNumberOfKORounds;
			
			if (oldNumOfKORounds > newNumOfKORounds) {
				
			} else if (oldNumOfKORounds < newNumOfKORounds) {
				
			}
			
			nOKO = newNumOfKORounds;
		}
	}
	
	private String[][] getTeamsGrp() {
		String[][] teams = null;
		
		
		return teams;
	}
	
	private String[][] getTeamsKO() {
		String[][] teams = null;
		
		
		return teams;
	}
	
	private void cancelActionPerformed() {
		this.setVisible(false);
	}
	
	private void goActionPerformed() {
		// TODO kopieren aus der Methode in Start

		
		log("REQUIREMENT --- This should be done correctly. ");

		name = nameTF.getText();
		shortName = shortNameTF.getText();
		stDate = MyDate.getDate(startDateTF.getText());
		fiDate = MyDate.getDate(finalDateTF.getText());
		if (stDate == 19700101 || fiDate == 19700101) {
			message("Geben Sie bitte ein gueltiges Start- und Enddatum ein.");
			return;
		}
		season = stDate / 10000;
		isSTSS = season != (fiDate / 10000);
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		if (hasGrp)	grp2leg = Boolean.parseBoolean(groupSecondLegRBGrp.getSelection().getActionCommand());
		if (hasKO)	ko2leg = Boolean.parseBoolean(koSecondLegRBGrp.getSelection().getActionCommand());
		has3pl = Boolean.parseBoolean(hasThirdPlaceRBGrp.getSelection().getActionCommand());
		nOTeam = Integer.parseInt(numberOfTeamsTF.getText());
		teamsGrp = getTeamsGrp();
		teamsKO = getTeamsKO();

		start.addNewTournament(name, shortName, season, stDate, fiDate, isSTSS, hasGrp, hasKO, 
				grp2leg, ko2leg, has3pl, nOTeam, nOGrp, nOKO, teamsGrp, teamsKO);
	}
}
