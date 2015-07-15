package model;

import static util.Utilities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class NewTournamentDialog extends JFrame {
	private static final long serialVersionUID = -4797487798345998331L;

	private Start start;

	private Color background = new Color(78, 235, 78);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	
	private String[] possibleKORounds = new String[] {"1. Runde", "2. Runde", "3. Runde", "Zwischenrunde", "Sechzehntelfinale", "Achtelfinale", "Viertelfinale", "Halbfinale", "Spiel um Platz 3", "Finale"};
	private String[] possibleKORoundsShort = new String[] {"1R", "2R", "3R", "ZR", "SF", "AF", "VF", "HF", "P3", "FI"};
	
	private final int minNumberOfGroups = 1;
	private final int maxNumberOfGroups = 12;
	private final int minNumberOfKORounds = 1;
	private final int maxNumberOfKORounds = 6;

	private static final int GAPPNL = 10;
	private static final int OFFSETX = 30;
	private static final int OFFSETY = 30;
	
	// Bounds
	private Dimension dim;
	private Rectangle REC_GO;
	private Rectangle REC_CANCEL;

	// Allgemeine Informationen
	private Rectangle REC_INFOPNL = new Rectangle(OFFSETX, OFFSETY, 380, 120);

	private Rectangle REC_NAMELBL = new Rectangle(5, 0, 120, 30);
	private Rectangle REC_NAMETF = new Rectangle(145, 0, 235, 30);
	private Rectangle REC_STDLBL = new Rectangle(5, 30, 90, 30); // checked
	private Rectangle REC_STDTF = new Rectangle(145, 30, 120, 30); // checked
	private Rectangle REC_FIDLBL = new Rectangle(5, 60, 75, 30); // checked
	private Rectangle REC_FIDTF = new Rectangle(145, 60, 120, 30); // checked
	private Rectangle REC_H3PLBL = new Rectangle(270, 30, 100, 30); // checked
	private Rectangle REC_H3PYESRB = new Rectangle(270, 60, 45, 30); // checked
	private Rectangle REC_H3PNORB = new Rectangle(320, 60, 60, 30); // checked
	private Rectangle REC_NOFTLBL = new Rectangle(5, 90, 135, 30);
	private Rectangle REC_NOFTTF = new Rectangle(145, 90, 50, 30);
	private Rectangle REC_SHNLBL = new Rectangle(270, 90, 65, 30);
	private Rectangle REC_SHNTF = new Rectangle(340, 90, 40, 30);

	// Qualification
	private Rectangle REC_QPNL;
	private Rectangle REC_QLBL = new Rectangle(5, 0, 90, 30);
	private Rectangle REC_QYES = new Rectangle(100, 0, 45, 30); // checked
	private Rectangle REC_QNO = new Rectangle(140, 0, 60, 30); // checked
	private Rectangle REC_QSTDLBL = new Rectangle(5, 30, 50, 30); // checked
	private Rectangle REC_QSTDTF = new Rectangle(60, 30, 120, 30); // checked
	private Rectangle REC_QFIDLBL = new Rectangle(200, 30, 40, 30); // checked
	private Rectangle REC_QFIDTF = new Rectangle(250, 30, 120, 30); // checked
	
	// Group stage
	private Rectangle REC_GRPPNL;
	private Rectangle REC_GRPLBL = new Rectangle(5, 5, 90, 25);
	private Rectangle REC_GRPYES = new Rectangle(100, 5, 45, 25); // checked
	private Rectangle REC_GRPNO = new Rectangle(140, 5, 60, 25); // checked
	private Rectangle REC_NOFGRPCB = new Rectangle(230, 5, 70, 25);
	private Rectangle REC_NOFGRPLBL = new Rectangle(305, 5, 70, 25);
	private Rectangle REC_GRP2LEGLBL = new Rectangle(205, 30, 70, 30); // checked
	private Rectangle REC_GRP2LEGYES = new Rectangle(280, 30, 45, 30); // checked
	private Rectangle REC_GRP2LEGNO = new Rectangle(320, 30, 60, 30); // checked
	private Rectangle REC_TNGRPCB = new Rectangle(5, 65, 120, 25); // checked

	// Knockout stage
	private Rectangle REC_KOPNL;
	private Rectangle REC_KOLBL = new Rectangle(5, 5, 70, 25); // checked
	private Rectangle REC_KOYES = new Rectangle(80, 5, 45, 25); // checked
	private Rectangle REC_KONO = new Rectangle(120, 5, 60, 25); // checked
	private Rectangle REC_NOFKOLBL = new Rectangle(200, 5, 75, 25); // checked
	private Rectangle REC_BTNCHNOFKO = new Rectangle(290, 5, 85, 25);
	private Rectangle REC_KO2LEGLBL = new Rectangle(205, 30, 70, 30); // checked
	private Rectangle REC_KO2LEGYES = new Rectangle(280, 30, 45, 30); // checked
	private Rectangle REC_KO2LEGNO = new Rectangle(320, 30, 60, 30); // checked
	private Rectangle REC_DETKOCB = new Rectangle(5, 35, 170, 25);
	private Rectangle REC_NOTPQVALLBL = new Rectangle(20, 65, 25, 25);
	private Rectangle REC_NOTPQLBL = new Rectangle(50, 65, 180, 25);
	private Rectangle REC_NOTPQMORELBL = new Rectangle(310, 65, 50, 25);
	private Rectangle REC_NOTPRVALLBL = new Rectangle(20, 95, 25, 25);
	private Rectangle REC_NOTPRLBL = new Rectangle(50, 95, 190, 25);
	private Rectangle REC_NOTPRMORELBL = new Rectangle(310, 95, 50, 25);
	private Rectangle REC_NOTOCVALLBL = new Rectangle(20, 125, 25, 25);
	private Rectangle REC_NOTOCLBL = new Rectangle(50, 125, 220, 25);
	private Rectangle REC_NOTOCMORELBL = new Rectangle(310, 125, 50, 25);

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

	// Qualification
	private JPanel qualificationPnl;
	private JLabel qualificationLbl;
	private JRadioButton qualificationYesRB;
	private JRadioButton qualificationNoRB;
	private ButtonGroup qualificationRBGrp;
	private JLabel qStartDateLbl;
	private JLabel qFinalDateLbl;
	private JTextField qStartDateTF;
	private JTextField qFinalDateTF;
	
	// Group stage
	private JPanel groupStagePnl;
	private JLabel groupStageLbl;
	private JRadioButton groupStageYesRB;
	private JRadioButton groupStageNoRB;
	private ButtonGroup groupStageRBGrp;
	private JComboBox<String> numOfGroupsCB;
	private JLabel numOfGroupsLbl;
	private JLabel groupSecondLegLbl;
	private JRadioButton groupSecondLegYesRB;// not used
	private JRadioButton groupSecondLegNoRB;// not used
	private ButtonGroup groupSecondLegRBGrp;// not used
	private JComboBox<String> teamsNamesGrpCB;

	// Knockout stage
	private JPanel koStagePnl;
	private JLabel koStageLbl;
	private JRadioButton koStageYesRB;
	private JRadioButton koStageNoRB;
	private ButtonGroup koStageRBGrp;
	private JLabel numOfKORoundsLbl;
	private JButton jBtnChangeKORounds;
	private JCheckBox[] jChBKORunden;
	private JButton jBtnSaveKORounds;
	private JLabel koSecondLegLbl;
	private JRadioButton koSecondLegYesRB;// not used
	private JRadioButton koSecondLegNoRB;// not used
	private ButtonGroup koSecondLegRBGrp;// not used
	private JComboBox<String> detailsKOCB;
	private JScrollPane jSPTeams;
	private JList<String> jLTeams;
	private DefaultListModel<String> jLTeamsModel;
	private JButton jBtnEditTeam;
	private JButton jBtnDeleteTeam;
	private JLabel jLblTeamsPQVal;
	private JLabel jLblTeamsPQ;
	private JLabel jLblTeamsPQMore;
	private JLabel jLblTeamsPRVal;
	private JLabel jLblTeamsPR;
	private JLabel jLblTeamsPRMore;
	private JLabel jLblTeamsOCVal;
	private JLabel jLblTeamsOC;
	private JLabel jLblTeamsOCMore;
//	private JScrollPane koStageSP;
//	private JList koStageL;
//	private DefaultListModel koStageModel = new DefaultListModel();

	// Model
	private String name;
	private String shortName;
	private int season;
	private int stDate;
	private int fiDate;
	private boolean isSTSS;
	private boolean hasQ;
	private boolean hasGrp;
	private boolean hasKO;
	private boolean has3pl;
	private int nOTeam;
	private int nOGrp;
	private int nOKO;
	private String[][] teamsGrp;
	private String[][] teamsKO;
	
	
	private boolean[] isKORoundSelected;
	private ArrayList<Integer> selectedKORounds;
	private ArrayList<int[]> teamsComingFrom;
	private int showingMoreFrom;
	
	private ArrayList<ArrayList<String>> grpTeamsModelArr;
	private DefaultListModel<String> grpTeamsModel;
	
	private ArrayList<ArrayList<String>> koTeamsModelArr;
	private DefaultListModel<String> koTeamsModel;


	public NewTournamentDialog(Start start) {
		super();
		
		this.start = start;

		initGUI();
		updateGUI();
	}

	private void initGUI() {
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		grpTeamsModelArr = new ArrayList<>();
		koTeamsModelArr = new ArrayList<>();
		
		{
			go = new JButton();
			getContentPane().add(go);
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
			cancel.setText("abbrechen");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelActionPerformed();
				}
			});
		}

		buildGeneralInfo();
		buildQualification();
		buildGroupStage();
		buildKOStage();
		
		boolean enterPresetValues = true;
		if (enterPresetValues)	enterPresetValues();

		setTitle("Neues Turnier erstellen");
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
		// Name
		{
			nameLbl = new JLabel();
			infoPnl.add(nameLbl);
			nameLbl.setBounds(REC_NAMELBL);
			nameLbl.setText("Name des Turniers");
		}
		{
			nameTF = new JTextField();
			infoPnl.add(nameTF);
			nameTF.setBounds(REC_NAMETF);
		}
		// Start- und Enddatum
		{
			startDateLbl = new JLabel();
			infoPnl.add(startDateLbl);
			startDateLbl.setBounds(REC_STDLBL);
			startDateLbl.setText("Turnierbeginn");
		}
		{
			startDateTF = new JTextField();
			infoPnl.add(startDateTF);
			startDateTF.setBounds(REC_STDTF);
		}
		{
			finalDateLbl = new JLabel();
			infoPnl.add(finalDateLbl);
			finalDateLbl.setBounds(REC_FIDLBL);
			finalDateLbl.setText("Turnierende");
		}
		{
			finalDateTF = new JTextField();
			infoPnl.add(finalDateTF);
			finalDateTF.setBounds(REC_FIDTF);
		}
		// Spiel um Platz 3
		{
			hasThirdPlaceLbl = new JLabel();
			infoPnl.add(hasThirdPlaceLbl);
			hasThirdPlaceLbl.setBounds(REC_H3PLBL);
			hasThirdPlaceLbl.setText("Spiel um Platz 3");
		}
		{
			hasThirdPlaceYesRB = new JRadioButton("ja");
			infoPnl.add(hasThirdPlaceYesRB);
			hasThirdPlaceYesRB.setBounds(REC_H3PYESRB);
			hasThirdPlaceYesRB.setActionCommand("true");
			hasThirdPlaceYesRB.setOpaque(false);
		}
		{
			hasThirdPlaceNoRB = new JRadioButton("nein");
			infoPnl.add(hasThirdPlaceNoRB);
			hasThirdPlaceNoRB.setBounds(REC_H3PNORB);
			hasThirdPlaceNoRB.setActionCommand("false");
			hasThirdPlaceNoRB.setOpaque(false);
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
			numberOfTeamsLbl.setBounds(REC_NOFTLBL);
			numberOfTeamsLbl.setText("Anzahl Mannschaften");
		}
		{
			numberOfTeamsTF = new JTextField();
			infoPnl.add(numberOfTeamsTF);
			numberOfTeamsTF.setBounds(REC_NOFTTF);
		}
		// Kurzname
		{
			shortNameLbl = new JLabel();
			infoPnl.add(shortNameLbl);
			shortNameLbl.setBounds(REC_SHNLBL);
			shortNameLbl.setText("Kurzname");
		}
		{
			shortNameTF = new JTextField();
			infoPnl.add(shortNameTF);
			shortNameTF.setBounds(REC_SHNTF);
		}
	}
	
	private void buildQualification() {
		{
			qualificationPnl = new JPanel();
			getContentPane().add(qualificationPnl);
			qualificationPnl.setLayout(null);
			qualificationPnl.setOpaque(true);
			qualificationPnl.setBackground(background);
		}
		{
			qualificationLbl = new JLabel();
			qualificationPnl.add(qualificationLbl);
			qualificationLbl.setBounds(REC_QLBL);
			qualificationLbl.setText("Qualifikation");
		}
		{
			qualificationYesRB = new JRadioButton("ja");
			qualificationPnl.add(qualificationYesRB);
			qualificationYesRB.setBounds(REC_QYES);
			qualificationYesRB.setActionCommand("true");
			qualificationYesRB.setOpaque(false);
			qualificationYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					qualificationRBchanged(true);
				}
			});
		}
		{
			qualificationNoRB = new JRadioButton("nein");
			qualificationPnl.add(qualificationNoRB);
			qualificationNoRB.setBounds(REC_QNO);
			qualificationNoRB.setActionCommand("false");
			qualificationNoRB.setOpaque(false);
			qualificationNoRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					qualificationRBchanged(false);
				}
			});
		}
		{
			qualificationRBGrp = new ButtonGroup();
			qualificationRBGrp.add(qualificationYesRB);
			qualificationRBGrp.add(qualificationNoRB);
		}
		{
			qStartDateLbl = new JLabel();
			qualificationPnl.add(qStartDateLbl);
			qStartDateLbl.setBounds(REC_QSTDLBL);
			qStartDateLbl.setText("Beginn");
			qStartDateLbl.setVisible(false);
		}
		{
			qStartDateTF = new JTextField();
			qualificationPnl.add(qStartDateTF);
			qStartDateTF.setBounds(REC_QSTDTF);
			qStartDateTF.setVisible(false);
		}
		{
			qFinalDateLbl = new JLabel();
			qualificationPnl.add(qFinalDateLbl);
			qFinalDateLbl.setBounds(REC_QFIDLBL);
			qFinalDateLbl.setText("Ende");
			qFinalDateLbl.setVisible(false);
		}
		{
			qFinalDateTF = new JTextField();
			qualificationPnl.add(qFinalDateTF);
			qFinalDateTF.setBounds(REC_QFIDTF);
			qFinalDateTF.setVisible(false);
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
			groupStagePnl.setOpaque(true);
			groupStagePnl.setBackground(background);
		}
		// gibt es eine Gruppenphase
		{
			groupStageLbl = new JLabel();
			groupStagePnl.add(groupStageLbl);
			groupStageLbl.setBounds(REC_GRPLBL);
			groupStageLbl.setText("Gruppenphase");
		}
		{
			groupStageYesRB = new JRadioButton("ja");
			groupStagePnl.add(groupStageYesRB);
			groupStageYesRB.setBounds(REC_GRPYES);
			groupStageYesRB.setActionCommand("true");
			groupStageYesRB.setOpaque(false);
			groupStageYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					groupStageRBchanged(true);
				}
			});
		}
		{
			groupStageNoRB = new JRadioButton("nein");
			groupStagePnl.add(groupStageNoRB);
			groupStageNoRB.setBounds(REC_GRPNO);
			groupStageNoRB.setActionCommand("false");
			groupStageNoRB.setOpaque(false);
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
			numOfGroupsCB = new JComboBox<>();
			groupStagePnl.add(numOfGroupsCB);
			numOfGroupsCB.setBounds(REC_NOFGRPCB);
			numOfGroupsCB.setModel(new DefaultComboBoxModel<>(posNumOfGroups));
			numOfGroupsCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					numOfGroupsCBItemStateChanged(evt);
				}
			});
		}
		{
			numOfGroupsLbl = new JLabel();
			groupStagePnl.add(numOfGroupsLbl);
			numOfGroupsLbl.setBounds(REC_NOFGRPLBL);
			numOfGroupsLbl.setText("Gruppen");
		}
		// Rueckspiel
		{
			groupSecondLegLbl = new JLabel();
			groupStagePnl.add(groupSecondLegLbl);
			groupSecondLegLbl.setBounds(REC_GRP2LEGLBL);
			groupSecondLegLbl.setText("Rueckspiel");
		}
		{
			groupSecondLegYesRB = new JRadioButton("ja");
			groupStagePnl.add(groupSecondLegYesRB);
			groupSecondLegYesRB.setBounds(REC_GRP2LEGYES);
			groupSecondLegYesRB.setActionCommand("true");
			groupSecondLegYesRB.setOpaque(false);
		}
		{
			groupSecondLegNoRB = new JRadioButton("nein");
			groupStagePnl.add(groupSecondLegNoRB);
			groupSecondLegNoRB.setBounds(REC_GRP2LEGNO);
			groupSecondLegNoRB.setActionCommand("false");
			groupSecondLegNoRB.setOpaque(false);
		}
		{
			groupSecondLegRBGrp = new ButtonGroup();
			groupSecondLegRBGrp.add(groupSecondLegYesRB);
			groupSecondLegRBGrp.add(groupSecondLegNoRB);
		}
		// Teamnamen
		{
			teamsNamesGrpCB = new JComboBox<>();
			groupStagePnl.add(teamsNamesGrpCB);
			teamsNamesGrpCB.setBounds(REC_TNGRPCB);
			teamsNamesGrpCB.setModel(new DefaultComboBoxModel<>(posNumOfGroups));
			teamsNamesGrpCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					teamsNamesGrpCBItemStateChanged(evt);
				}
			});
		}
	}
	
	private void buildKOStage() {
		jChBKORunden = new JCheckBox[possibleKORounds.length];
		isKORoundSelected = new boolean[possibleKORounds.length];
		selectedKORounds = new ArrayList<>();
		String[] posNumOfKORounds = new String[maxNumberOfKORounds - minNumberOfKORounds + 1];
		for (int i = 0; i < posNumOfKORounds.length; i++) {
			posNumOfKORounds[i] = "" + (i + 1);
		}
		{
			koStagePnl = new JPanel();
			getContentPane().add(koStagePnl);
			koStagePnl.setLayout(null);
			koStagePnl.setOpaque(true);
			koStagePnl.setBackground(background);
		}
		// gibt es eine KO Phase
		{
			koStageLbl = new JLabel();
			koStagePnl.add(koStageLbl);
			koStageLbl.setBounds(REC_KOLBL);
			koStageLbl.setText("KO-Phase");
		}
		{
			koStageYesRB = new JRadioButton("ja");
			koStagePnl.add(koStageYesRB);
			koStageYesRB.setBounds(REC_KOYES);
			koStageYesRB.setActionCommand("true");
			koStageYesRB.setOpaque(false);
			koStageYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					koStageRBchanged(true);
				}
			});
		}
		{
			koStageNoRB = new JRadioButton("nein");
			koStagePnl.add(koStageNoRB);
			koStageNoRB.setBounds(REC_KONO);
			koStageNoRB.setActionCommand("false");
			koStageNoRB.setOpaque(false);
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
			numOfKORoundsLbl = new JLabel();
			koStagePnl.add(numOfKORoundsLbl);
			numOfKORoundsLbl.setBounds(REC_NOFKOLBL);
			numOfKORoundsLbl.setVisible(false);
		}
		{
			jBtnChangeKORounds = new JButton();
			koStagePnl.add(jBtnChangeKORounds);
			jBtnChangeKORounds.setText("Aendern");
			jBtnChangeKORounds.setBounds(REC_BTNCHNOFKO);
			jBtnChangeKORounds.setVisible(false);
			jBtnChangeKORounds.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnChangeKORoundsActionPerformed();
				}
			});
		}
		
		for (int i = 0; i < jChBKORunden.length; i++) {
			jChBKORunden[i] = new JCheckBox(possibleKORounds[i]);
			koStagePnl.add(jChBKORunden[i]);
			jChBKORunden[i].setBounds(30, 40 + i * 25, 140, 20);
			jChBKORunden[i].setOpaque(false);
		}
		{
			jBtnSaveKORounds = new JButton();
			koStagePnl.add(jBtnSaveKORounds);
			jBtnSaveKORounds.setText("Speichern");
			jBtnSaveKORounds.setBounds(30, 300, 100, 25);
			jBtnSaveKORounds.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnSaveKORoundsActionPerformed();
				}
			});
		}
		
		// Rueckspiel
		{
			koSecondLegLbl = new JLabel();
			koStagePnl.add(koSecondLegLbl);
			koSecondLegLbl.setBounds(REC_KO2LEGLBL);
			koSecondLegLbl.setText("Rueckspiel");
			koSecondLegLbl.setVisible(false);
		}
		{
			koSecondLegYesRB = new JRadioButton("ja");
			koStagePnl.add(koSecondLegYesRB);
			koSecondLegYesRB.setBounds(REC_KO2LEGYES);
			koSecondLegYesRB.setActionCommand("true");
			koSecondLegYesRB.setOpaque(false);
			koSecondLegYesRB.setVisible(false);
		}
		{
			koSecondLegNoRB = new JRadioButton("nein");
			koStagePnl.add(koSecondLegNoRB);
			koSecondLegNoRB.setBounds(REC_KO2LEGNO);
			koSecondLegNoRB.setActionCommand("false");
			koSecondLegNoRB.setOpaque(false);
			koSecondLegNoRB.setVisible(false);
		}
		{
			koSecondLegRBGrp = new ButtonGroup();
			koSecondLegRBGrp.add(koSecondLegYesRB);
			koSecondLegRBGrp.add(koSecondLegNoRB);
		}
		// Anzahl KO-Runden
		// TODO Label davor mit "Details fuer "
		{
			detailsKOCB = new JComboBox<>();
			koStagePnl.add(detailsKOCB);
			detailsKOCB.setBounds(REC_DETKOCB);
			detailsKOCB.setVisible(false);
			detailsKOCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					detailsKOCBItemStateChanged(evt);
				}
			});
		}
		jLTeamsModel = new DefaultListModel<>();
		jLTeamsModel.addElement("Mannschaft 1");
		jLTeamsModel.addElement("Mannschaft 2");
		{
			jSPTeams = new JScrollPane();
			koStagePnl.add(jSPTeams);
			jSPTeams.setVisible(false);
			{
				jLTeams = new JList<>();
			    jSPTeams.setViewportView(jLTeams);
			    jLTeams.setModel(jLTeamsModel);
			    jLTeams.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			jBtnEditTeam = new JButton();
			koStagePnl.add(jBtnEditTeam);
			jBtnEditTeam.setText("Bearbeiten");
			jBtnEditTeam.setVisible(false);
			jBtnEditTeam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnEditTeamActionPerformed();
				}
			});
		}
		{
			jBtnDeleteTeam = new JButton();
			koStagePnl.add(jBtnDeleteTeam);
			jBtnDeleteTeam.setText("Entfernen");
			jBtnDeleteTeam.setVisible(false);
			jBtnDeleteTeam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnDeleteTeamActionPerformed();
				}
			});
		}
		// TODO Labels mit 'mehr': beim draufdruecken liste mit scrollpane anzeigen
		{
			jLblTeamsPQVal = new JLabel();
			koStagePnl.add(jLblTeamsPQVal);
			jLblTeamsPQVal.setBounds(REC_NOTPQVALLBL);
			jLblTeamsPQVal.setHorizontalAlignment(SwingConstants.CENTER);
			jLblTeamsPQVal.setVisible(false);
		}
		{
			jLblTeamsPQ = new JLabel();
			koStagePnl.add(jLblTeamsPQ);
			jLblTeamsPQ.setBounds(REC_NOTPQLBL);
			jLblTeamsPQ.setText("Teams qualifiziert");
			jLblTeamsPQ.setVisible(false);
			jLblTeamsPQ.setOpaque(true);
		}
		{
			jLblTeamsPQMore = new JLabel();
			koStagePnl.add(jLblTeamsPQMore);
			jLblTeamsPQMore.setBounds(REC_NOTPQMORELBL);
			jLblTeamsPQMore.setText("mehr");
			jLblTeamsPQMore.setCursor(handCursor);
			jLblTeamsPQMore.setVisible(false);
			jLblTeamsPQMore.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jLblMoreClicked(1);
				}
			});
		}
		{
			jLblTeamsPRVal = new JLabel();
			koStagePnl.add(jLblTeamsPRVal);
			jLblTeamsPRVal.setBounds(REC_NOTPRVALLBL);
			jLblTeamsPRVal.setHorizontalAlignment(SwingConstants.CENTER);
			jLblTeamsPRVal.setVisible(false);
		}
		{
			jLblTeamsPR = new JLabel();
			koStagePnl.add(jLblTeamsPR);
			jLblTeamsPR.setBounds(REC_NOTPRLBL);
			jLblTeamsPR.setText("Teams aus vorherigen Runden");
			jLblTeamsPR.setVisible(false);
			jLblTeamsPR.setOpaque(true);
		}
		{
			jLblTeamsPRMore = new JLabel();
			koStagePnl.add(jLblTeamsPRMore);
			jLblTeamsPRMore.setBounds(REC_NOTPRMORELBL);
			jLblTeamsPRMore.setText("mehr");
			jLblTeamsPRMore.setCursor(handCursor);
			jLblTeamsPRMore.setVisible(false);
			jLblTeamsPRMore.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jLblMoreClicked(2);
				}
			});
		}
		{
			jLblTeamsOCVal = new JLabel();
			koStagePnl.add(jLblTeamsOCVal);
			jLblTeamsOCVal.setBounds(REC_NOTOCVALLBL);
			jLblTeamsOCVal.setHorizontalAlignment(SwingConstants.CENTER);
			jLblTeamsOCVal.setVisible(false);
		}
		{
			jLblTeamsOC = new JLabel();
			koStagePnl.add(jLblTeamsOC);
			jLblTeamsOC.setBounds(REC_NOTOCLBL);
			jLblTeamsOC.setText("Teams aus anderen Wettbewerben");
			jLblTeamsOC.setVisible(false);
			jLblTeamsOC.setOpaque(true);
		}
		{
			jLblTeamsOCMore = new JLabel();
			koStagePnl.add(jLblTeamsOCMore);
			jLblTeamsOCMore.setBounds(REC_NOTOCMORELBL);
			jLblTeamsOCMore.setText("mehr");
			jLblTeamsOCMore.setCursor(handCursor);
			jLblTeamsOCMore.setVisible(false);
			jLblTeamsOCMore.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jLblMoreClicked(3);
				}
			});
		}
		
//		{
//			koStageSP = new JScrollPane();
//			koStagePnl.add(koStageSP);
//			koStageSP.setBounds(REC_KOSTAGESP);
//			{
//				koStageL = new JList();
//				koStageSP.setViewportView(koStageL);
//				koStageL.setModel(koStageModel);
//				koStageL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			}
//		}
	}
	
	private void refreshTeamsNamesGrpCBModel() {
		String[] gruppenNamen = new String[nOGrp];
		for (int i = 0; i < gruppenNamen.length; i++) {
			gruppenNamen[i] = "Gruppe " + start.getAlphabet()[i];
		}
		teamsNamesGrpCB.setModel(new DefaultComboBoxModel<>(gruppenNamen));
	}

	
	private void enterPresetValues() {
//		nameTF.setText("Copa2");
//		shortNameTF.setText("C2");
//		startDateTF.setText("12.06.2015");
//		finalDateTF.setText("04.07.2015");
		hasThirdPlaceYesRB.setSelected(true);
//		numberOfTeamsTF.setText("12");
		
		qualificationNoRB.setSelected(true);
		
		groupStageYesRB.setSelected(true);
//		groupStageRBchanged(true);
//		numOfGroupsCB.setSelectedIndex(3 - minNumberOfGroups);
		groupSecondLegNoRB.setSelected(true);
		
		koStageYesRB.setSelected(true);
//		koStageRBchanged(true);
//		numOfKORoundsCB.setSelectedIndex(4 - minNumberOfKORounds);
		koSecondLegNoRB.setSelected(true);
		jChBKORunden[5].setSelected(true);
		jChBKORunden[6].setSelected(true);
		jChBKORunden[7].setSelected(true);
		jChBKORunden[9].setSelected(true);
	}
	
	private void updateGUI() {
		hasQ = Boolean.parseBoolean(qualificationRBGrp.getSelection().getActionCommand());
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		
		int heightQ = hasQ ? 150 : 50;
		REC_QPNL = new Rectangle(OFFSETX, OFFSETY + REC_INFOPNL.height + GAPPNL, 380, heightQ);
		qualificationPnl.setBounds(REC_QPNL);
		
		int heightG = hasGrp ? 150 : 50;
		REC_GRPPNL = new Rectangle(OFFSETX * 2 + 380, OFFSETY, 380, heightG);
		groupStagePnl.setBounds(REC_GRPPNL);
		
		int heightK = hasKO ? 480 : 50;
		REC_KOPNL = new Rectangle(OFFSETX * 2 + 380, REC_GRPPNL.y + REC_GRPPNL.height + GAPPNL, 380, heightK);
		koStagePnl.setBounds(REC_KOPNL);
		
		REC_CANCEL = new Rectangle(610, REC_KOPNL.y + REC_KOPNL.height + GAPPNL, 100, 30);
		cancel.setBounds(REC_CANCEL);
		
		REC_GO = new Rectangle(720, REC_KOPNL.y + REC_KOPNL.height + GAPPNL, 70, 30);
		go.setBounds(REC_GO);
		
		dim = new Dimension(2 * 380 + 3 * OFFSETX + 6, Math.max(REC_QPNL.y + REC_QPNL.height, REC_GO.y + REC_GO.height) + OFFSETY + 28);
		setSize(this.dim);
		setLocationRelativeTo(null);
	}
	
	private void qualificationRBchanged(boolean hasQ) {
		// Alles was mit Qualifikationseinstellung zu tun hat disablen
		log("Qualification has been " + (hasQ ? "enabled." : "disabled."));
		
		qStartDateLbl.setVisible(hasQ);
		qStartDateTF.setVisible(hasQ);
		qFinalDateLbl.setVisible(hasQ);
		qFinalDateTF.setVisible(hasQ);
		updateGUI();
	}
	
	private void groupStageRBchanged(boolean hasGrp) {
		// Alles was mit Gruppeneinstellung zu tun hat disablen
		log("Group stage has been " + (hasGrp ? "enabled." : "disabled."));
		
		numOfGroupsCB.setVisible(hasGrp);
		numOfGroupsLbl.setVisible(hasGrp);
		groupSecondLegLbl.setVisible(hasGrp);
		groupSecondLegYesRB.setVisible(hasGrp);
		groupSecondLegNoRB.setVisible(hasGrp);
		teamsNamesGrpCB.setVisible(hasGrp);
		updateGUI();
	}
	
	private void koStageRBchanged(boolean hasKO) {
		// Alles was mit KO-Rundeneinstellung zu tun hat disablen
		log("Knockout stage has been " + (hasKO ? "enabled." : "disabled."));
		this.hasKO = hasKO;
		
		showKORoundConfiguration(true);
		updateGUI();
	}
	
	private void numOfGroupsCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int oldNumOfGroups = nOGrp;
			int newNumOfGroups = numOfGroupsCB.getSelectedIndex() + minNumberOfGroups;
			
			if (oldNumOfGroups > newNumOfGroups) { // wird weniger
				for (int i = newNumOfGroups; i < oldNumOfGroups; i++) {
					grpTeamsModelArr.remove(newNumOfGroups);
				}
			} else { // gleich viel oder mehr
				for (int i = oldNumOfGroups; i < newNumOfGroups; i++) {
					grpTeamsModelArr.add(new ArrayList<String>());
				}
			}
			
			nOGrp = newNumOfGroups;
			refreshTeamsNamesGrpCBModel();
		}
	}
	
	private void teamsNamesGrpCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = teamsNamesGrpCB.getSelectedIndex();
			if (grpTeamsModel != null)	grpTeamsModel.clear();
			else						grpTeamsModel = new DefaultListModel<>();
			
			ArrayList<String> list = grpTeamsModelArr.get(index);
			for (int i = 0; i < list.size(); i++) {
				grpTeamsModel.addElement(list.get(i));
			}
		}
	}
	
	private void setDetailsKOCBModel() {
		String[] model = new String[selectedKORounds.size()];
		for (int i = 0; i < model.length; i++) {
			model[i] = possibleKORounds[selectedKORounds.get(i)];
		}
		detailsKOCB.setModel(new DefaultComboBoxModel<>(model));
	}
	
	private void jBtnChangeKORoundsActionPerformed() {
		showKORoundConfiguration(true);
	}
	
	private void jBtnSaveKORoundsActionPerformed() {
		// saving provided data -> could be in different order later, checking if data is deleted
		int[][] teamsCFrom = new int[possibleKORounds.length][];
		int index = 0;
		for (int i = 0; i < jChBKORunden.length; i++) {
			if (isKORoundSelected[i]) {
				teamsCFrom[i] = teamsComingFrom.get(index++);
				if (!jChBKORunden[i].isSelected() && (teamsCFrom[i][0] != 0 || teamsCFrom[i][1] != 0 || teamsCFrom[i][2] != 0)) {
					int cont = yesNoDialog("Es wurden fuer die geloeschte KO-Runde " + possibleKORounds[i] + " bereits Daten bereitgestellt. Diese gehen hierbei verloren. Trotzdem fortfahren?");
					if (cont != JOptionPane.YES_OPTION)	return;
				}
			} else {
				teamsCFrom[i] = new int[] {0, 0, 0};
			}
			if (selectedKORounds.size() == 0) {
				teamsCFrom[5] = new int[] {0, 16, 0};
				teamsCFrom[6] = new int[] {0, 8, 0};
				teamsCFrom[7] = new int[] {0, 4, 0};
				teamsCFrom[9] = new int[] {0, 2, 0};
			}
		}
		
		selectedKORounds.clear();
		for (int i = 0; i < jChBKORunden.length; i++) {
			isKORoundSelected[i] = jChBKORunden[i].isSelected();
			if (isKORoundSelected[i])	selectedKORounds.add(i);
		}
		
		int numberOfKORounds = selectedKORounds.size();
		
		if (numberOfKORounds == 0) {
			message("Es muss mindestens eine KO-Runde geben!");
			return;
		}
		
		// copying data back in new order
		if (teamsComingFrom == null)	teamsComingFrom = new ArrayList<>();
		teamsComingFrom.clear();
		for (int i = 0; i < isKORoundSelected.length; i++) {
			if (isKORoundSelected[i]) {
				teamsComingFrom.add(teamsCFrom[i]);
			}
		}
		
		numOfKORoundsLbl.setText(numberOfKORounds + " KO-Runden");
		
		for (int i = 0; i < numberOfKORounds; i++) {
			for (int j = 0; j < 3; j++) {
			}
		}
		setDetailsKOCBModel();
		
		showKORoundConfiguration(false);
		showDataForKORound(0);
	}
	
	private void showKORoundConfiguration(boolean show) {
		for (int i = 0; i < jChBKORunden.length; i++) {
			jChBKORunden[i].setVisible(hasKO && show);
		}
		jBtnSaveKORounds.setVisible(hasKO && show);

		jBtnChangeKORounds.setVisible(hasKO && !show);
		numOfKORoundsLbl.setVisible(hasKO && !show);
		koSecondLegLbl.setVisible(hasKO && !show);
		koSecondLegYesRB.setVisible(hasKO && !show);
		koSecondLegNoRB.setVisible(hasKO && !show);
		detailsKOCB.setVisible(hasKO && !show);
		jLblTeamsPQVal.setVisible(hasKO && !show);
		jLblTeamsPQ.setVisible(hasKO && !show);
		jLblTeamsPQMore.setVisible(hasKO && !show);
		jLblTeamsPRVal.setVisible(hasKO && !show);
		jLblTeamsPR.setVisible(hasKO && !show);
		jLblTeamsPRMore.setVisible(hasKO && !show);
		jLblTeamsOCVal.setVisible(hasKO && !show);
		jLblTeamsOC.setVisible(hasKO && !show);
		jLblTeamsOCMore.setVisible(hasKO && !show);
	}
	
	private void showDataForKORound(int index) {
		jLblTeamsPQVal.setText("" + teamsComingFrom.get(index)[0]);
		jLblTeamsPRVal.setText("" + teamsComingFrom.get(index)[1]);
		jLblTeamsOCVal.setText("" + teamsComingFrom.get(index)[2]);
	}
	
	private void detailsKOCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = detailsKOCB.getSelectedIndex();
			showDataForKORound(index);
		}
	}
	
	private void jLblMoreClicked(int index) {
		int heightOfScrollPane = 100;
		switch (showingMoreFrom) {
			case 1:
				REC_NOTPRVALLBL.y -= heightOfScrollPane;
				REC_NOTPRLBL.y -= heightOfScrollPane;
				REC_NOTPRMORELBL.y -= heightOfScrollPane;
				jLblTeamsPRVal.setBounds(REC_NOTPRVALLBL);
				jLblTeamsPR.setBounds(REC_NOTPRLBL);
				jLblTeamsPRMore.setBounds(REC_NOTPRMORELBL);
				jLblTeamsPQMore.setText("mehr");
			case 2:
				REC_NOTOCVALLBL.y -= heightOfScrollPane;
				REC_NOTOCLBL.y -= heightOfScrollPane;
				REC_NOTOCMORELBL.y -= heightOfScrollPane;
				jLblTeamsOCVal.setBounds(REC_NOTOCVALLBL);
				jLblTeamsOC.setBounds(REC_NOTOCLBL);
				jLblTeamsOCMore.setBounds(REC_NOTOCMORELBL);
				jLblTeamsPRMore.setText("mehr");
			case 3:
				jLblTeamsOCMore.setText("mehr");
		}
		if (showingMoreFrom == index) {
			jSPTeams.setVisible(false);
			jBtnEditTeam.setVisible(false);
			jBtnDeleteTeam.setVisible(false);
			showingMoreFrom = 0;
			return;
		}
		showingMoreFrom = index;
		switch (showingMoreFrom) {
			case 1:
				REC_NOTPRVALLBL.y += heightOfScrollPane;
				REC_NOTPRLBL.y += heightOfScrollPane;
				REC_NOTPRMORELBL.y += heightOfScrollPane;
				jLblTeamsPRVal.setBounds(REC_NOTPRVALLBL);
				jLblTeamsPR.setBounds(REC_NOTPRLBL);
				jLblTeamsPRMore.setBounds(REC_NOTPRMORELBL);
			case 2:
				REC_NOTOCVALLBL.y += heightOfScrollPane;
				REC_NOTOCLBL.y += heightOfScrollPane;
				REC_NOTOCMORELBL.y += heightOfScrollPane;
				jLblTeamsOCVal.setBounds(REC_NOTOCVALLBL);
				jLblTeamsOC.setBounds(REC_NOTOCLBL);
				jLblTeamsOCMore.setBounds(REC_NOTOCMORELBL);
		}
		
		if (showingMoreFrom == 1) {
			jSPTeams.setBounds(20, 95, 240, 90);
			jBtnEditTeam.setBounds(270, 100, 100, 25);
			jBtnDeleteTeam.setBounds(270, 130, 100, 25);
			jLblTeamsPQMore.setText("weniger");
		} else if (showingMoreFrom == 2) {
			jSPTeams.setBounds(20, 125, 240, 90);
			jBtnEditTeam.setBounds(270, 130, 100, 25);
			jBtnDeleteTeam.setBounds(270, 160, 100, 25);
			jLblTeamsPRMore.setText("weniger");
		} else if (showingMoreFrom == 3) {
			jSPTeams.setBounds(20, 155, 240, 90);
			jBtnEditTeam.setBounds(270, 160, 100, 25);
			jBtnDeleteTeam.setBounds(270, 190, 100, 25);
			jLblTeamsOCMore.setText("weniger");
		}
		jSPTeams.setVisible(true);
		jBtnEditTeam.setVisible(true);
		jBtnDeleteTeam.setVisible(true);
	}
	
	private void jBtnEditTeamActionPerformed() {
		
	}
	
	private void jBtnDeleteTeamActionPerformed() {
		
	}
	
	private String[][] getTeamsGrp() {
		String[][] teams = null;
		
		for (int i = 0; i < grpTeamsModelArr.size(); i++) {
			ArrayList<String> defaultListModel = grpTeamsModelArr.get(i);
			for (int j = 0; j < defaultListModel.size(); j++) {
				log("Gruppe " + i + ": " + defaultListModel.get(j));
			}
		}
		
		return teams;
	}
	
	private String[][] getTeamsKO() {
		String[][] teams = null;
		// In Start wird der Wert von teamsKO[index][team] abgefragt
		teams = new String[][] {{"GA1#PR", "GA2#PR", "GB1#PR", "GB2#PR", "GC1#PR", "GC2#PR", "GB3#PR", "GC3#PR"}, {"VF1#PR", "VF2#PR", "VF3#PR", "VF4#PR"}, {"HF1#PR", "HF2#PR"}, {"HF1#PR", "HF2#PR"}};
		
		return teams;
	}
	
	public void dispose() {
		int cancel = yesNoDialog("Sicher, dass Sie das Fenster schliessen wollen? Dabei gehen die eingegebenen Daten verloren.");
		if (cancel == JOptionPane.YES_OPTION)	super.dispose();
	}
	
	private void cancelActionPerformed() {
		this.setVisible(false);
	}
	
	private void goActionPerformed() {
		boolean outOfUse = true;
		if (outOfUse) {
			message("Nicht aktuell. Erst refactorn.");
			cancelActionPerformed();
			return;
		}

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
		qualificationNoRB.setSelected(true);
		hasQ = Boolean.parseBoolean(qualificationRBGrp.getSelection().getActionCommand());
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		has3pl = Boolean.parseBoolean(hasThirdPlaceRBGrp.getSelection().getActionCommand());
		nOTeam = Integer.parseInt(numberOfTeamsTF.getText());
		teamsGrp = getTeamsGrp();
		teamsKO = getTeamsKO();

//		start.addNewTournament(name, shortName, season, isSTSS, stDate, fiDate, hasQ, hasGrp, hasKO, has3pl, qConfig, teamsQG, teamsQKO, grpConfig, teamsGrp, koConfig, teamsKO);
		this.setVisible(false);
	}
}
