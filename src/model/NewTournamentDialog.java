package model;

import static util.Utilities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class NewTournamentDialog extends JFrame {
	private static final long serialVersionUID = -4797487798345998331L;

	private Color background = new Color(78, 235, 78);
	private Color backgroundRemove = new Color(235, 31, 31);
	private Color foregroundRemove = new Color(255, 255, 255);
	private Font fontWettbewerb = new Font("Dialog", 0, 25);
	
	private String[] possibleKORounds = new String[] {"1. Runde", "2. Runde", "3. Runde", "Zwischenrunde", "Sechzehntelfinale", 
														"Achtelfinale", "Viertelfinale", "Halbfinale", "Spiel um Platz 3", "Finale"};
	private String[] possibleKORoundsShort = new String[] {"1R", "2R", "3R", "ZR", "SF", "AF", "VF", "HF", "P3", "FI"};
	private String[] posNumOfTeamsPerGroup;
	private String[] posNumOfTeamsPerQGroup;
	private String[] prOptions = new String[] {"alle", "keine", "die besten"};
	private String[] gruppenXte = new String[] {"Gruppenersten", "Gruppenzweiten", "Gruppendritten"};
	
	private final int minNumberOfQGroups = 1;
	private final int maxNumberOfQGroups = 12;
	private final int minNumberOfTeamsPerGroup = 2;
	private final int defaultNumberOfTeamsPerGroup = 4;
	private final int maxNumberOfTeamsPerGroup = 6;
	private final int minNumberOfGroups = 1;
	private final int maxNumberOfGroups = 12;
	private final int minNumberOfKORounds = 1;
	private final int maxNumberOfKORounds = 6;

	private static final int GAPPNL = 10;
	private static final int OFFSETX = 30;
	private static final int OFFSETY = 30;
	
	// Bounds
	private Dimension dim;
	private Rectangle REC_WETTBLBL = new Rectangle(30, 30, 380, 40);
	private Rectangle REC_GO;
	private Rectangle REC_CANCEL;

	// Allgemeine Informationen
	private Rectangle REC_INFOPNL = new Rectangle(OFFSETX, OFFSETY, 380, 65);

	private Rectangle REC_NAMELBL = new Rectangle(5, 5, 50, 25);
	private Rectangle REC_NAMETF = new Rectangle(60, 5, 180, 25);
	private Rectangle REC_SHNLBL = new Rectangle(265, 5, 65, 25);
	private Rectangle REC_SHNTF = new Rectangle(335, 5, 40, 25);
	private Rectangle REC_STDLBL = new Rectangle(5, 35, 50, 25);
	private Rectangle REC_STDTF = new Rectangle(60, 35, 90, 25);
	private Rectangle REC_FIDLBL = new Rectangle(245, 35, 35, 25);
	private Rectangle REC_FIDTF = new Rectangle(285, 35, 90, 25);
	
	// Qualification
	private Rectangle REC_QPNL;
	private Rectangle REC_QLBL = new Rectangle(10, 5, 90, 25);
	private Rectangle REC_QYES = new Rectangle(105, 5, 45, 25);
	private Rectangle REC_QNO = new Rectangle(145, 5, 60, 25);
	private Rectangle REC_QSTDLBL = new Rectangle(20, 35, 50, 25);
	private Rectangle REC_QSTDTF = new Rectangle(75, 35, 110, 25);
	private Rectangle REC_QFIDLBL = new Rectangle(205, 35, 40, 25);
	private Rectangle REC_QFIDTF = new Rectangle(255, 35, 110, 25);
	
	private Rectangle REC_QGRPPNL;
	private Rectangle REC_QGRPLBL = new Rectangle(15, 5, 90, 25);
	private Rectangle REC_QGRPYES = new Rectangle(110, 5, 45, 25);
	private Rectangle REC_QGRPNO = new Rectangle(150, 5, 60, 25);
	private Rectangle REC_NOFQGRPLBL = new Rectangle(215, 5, 75, 25);
	private Rectangle REC_BTNCHNOFQGRP = new Rectangle(295, 5, 80, 25);
	private Rectangle REC_BTNADDQGRP = new Rectangle(30, 35, 110, 25);
	private Rectangle REC_BTNSAVEQGRP = new Rectangle(270, 35, 100, 25);
	private Rectangle REC_NOTQGRPLBL = new Rectangle(200, 70, 170, 25);
	private Rectangle REC_SAMENOTQGRPLBL = new Rectangle(200, 100, 80, 25);
	private Rectangle REC_SAMENOTQGRPCB = new Rectangle(280, 100, 60, 25);
	private Rectangle REC_DIFFNOTQGRPLBL = new Rectangle(200, 130, 130, 25);
	private Rectangle REC_DETQGRPCB = new Rectangle(90, 45, 120, 25);
	private Rectangle REC_QGRP2LEGLBL = new Rectangle(25, 75, 70, 25);
	private Rectangle REC_QGRP2LEGYES = new Rectangle(100, 75, 45, 25);
	private Rectangle REC_QGRP2LEGNO = new Rectangle(140, 75, 60, 25);
	
	private int[] rmvQGroups = new int[] {20, 70, 0, 25, 20, 20};
	private int[] qGroups = new int[] {50, 70, 0, 25, 60, 20};
	private int[] nOfTQGrps = new int[] {120, 69, 0, 25, 60, 22};
	
	private Rectangle REC_QKOPNL;
	private Rectangle REC_QKOLBL = new Rectangle(15, 5, 70, 25);
	private Rectangle REC_QKOYES = new Rectangle(110, 5, 45, 25);
	private Rectangle REC_QKONO = new Rectangle(150, 5, 60, 25);
	
	// Group stage
	private Rectangle REC_GRPPNL;
	private Rectangle REC_GRPLBL = new Rectangle(10, 5, 90, 25);
	private Rectangle REC_GRPYES = new Rectangle(105, 5, 45, 25);
	private Rectangle REC_GRPNO = new Rectangle(145, 5, 60, 25);
	private Rectangle REC_NOFGRPLBL = new Rectangle(215, 5, 75, 25);
	private Rectangle REC_BTNCHNOFGRP = new Rectangle(295, 5, 80, 25);
	private Rectangle REC_BTNADDGRP = new Rectangle(30, 35, 110, 25);
	private Rectangle REC_BTNSAVEGRP = new Rectangle(270, 35, 100, 25);
	private Rectangle REC_NOTGRPLBL = new Rectangle(200, 70, 170, 25);
	private Rectangle REC_SAMENOTGRPLBL = new Rectangle(200, 100, 80, 25);
	private Rectangle REC_SAMENOTGRPCB = new Rectangle(280, 100, 60, 25);
	private Rectangle REC_DIFFNOTGRPLBL = new Rectangle(200, 130, 130, 25);
	private Rectangle REC_DETGRPCB = new Rectangle(90, 45, 120, 25);
	private Rectangle REC_GRP2LEGLBL = new Rectangle(25, 75, 70, 25);
	private Rectangle REC_GRP2LEGYES = new Rectangle(100, 75, 45, 25);
	private Rectangle REC_GRP2LEGNO = new Rectangle(140, 75, 60, 25);
	
	private int[] rmvGroups = new int[] {20, 70, 0, 25, 20, 20};
	private int[] groups = new int[] {50, 70, 0, 25, 60, 20};
	private int[] nOfTGrps = new int[] {120, 69, 0, 25, 60, 22};
	
	// Knockout stage
	private Rectangle REC_KOPNL;
	private Rectangle REC_KOLBL = new Rectangle(10, 5, 70, 25);
	private Rectangle REC_KOYES = new Rectangle(85, 5, 45, 25);
	private Rectangle REC_KONO = new Rectangle(125, 5, 60, 25);
	private Rectangle REC_NOFKOLBL = new Rectangle(200, 5, 85, 25);
	private Rectangle REC_BTNCHNOFKO = new Rectangle(295, 5, 80, 25);
	private Rectangle REC_DETKOLBL = new Rectangle(20, 45, 65, 25);
	private Rectangle REC_DETKOCB = new Rectangle(90, 45, 160, 25);
	private Rectangle REC_KO2LEGLBL = new Rectangle(25, 75, 70, 25);
	private Rectangle REC_KO2LEGYES = new Rectangle(100, 75, 45, 25);
	private Rectangle REC_KO2LEGNO = new Rectangle(140, 75, 60, 25);
	private Rectangle REC_NOPRLBL = new Rectangle(75, 165, 200, 25);
	private Rectangle REC_NOTPQVALLBL = new Rectangle(20, 105, 25, 25);
	private Rectangle REC_NOTPQLBL = new Rectangle(50, 105, 180, 25);
	private Rectangle REC_NOTPQMORELBL = new Rectangle(310, 105, 50, 25);
	private Rectangle REC_NOTPRVALLBL = new Rectangle(20, 135, 25, 25);
	private Rectangle REC_NOTPRLBL = new Rectangle(50, 135, 190, 25);
	private Rectangle REC_NOTPRMORELBL = new Rectangle(310, 135, 50, 25);
	private Rectangle REC_NOTOCVALLBL = new Rectangle(20, 165, 25, 25);
	private Rectangle REC_NOTOCLBL = new Rectangle(50, 165, 220, 25);
	private Rectangle REC_NOTOCMORELBL = new Rectangle(310, 165, 50, 25);
	
	private int heightOfDisplacement;
	private int[] koRounds = new int[] {30, 40, 0, 25, 160, 20};
	private int[] cbs = new int[] {50, 170, 0, 30, 115, 25};
	private int[] grpXth = new int[] {175, 170, 0, 30, 60, 25};
	private int[] lbls = new int[] {245, 170, 0, 30, 105, 25};
	
	// View
	private JLabel jLblWettbewerb;
	private JButton go;
	private JButton cancel;
	private JLabel jLblDetailsFor;
	private JScrollPane jSPTeams;
	private JList<String> jLTeams;
	private DefaultListModel<String> jLTeamsModel;
	private JButton jBtnEditTeam;

	// Allgemeine Informationen
	private JPanel infoPnl;
	private JLabel nameLbl;
	private JTextField nameTF;
	private JLabel startDateLbl;
	private JLabel finalDateLbl;
	private JTextField startDateTF;
	private JTextField finalDateTF;
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
	
	private JPanel qGroupStagePnl;
	private JLabel qGroupStageLbl;
	private JRadioButton qGroupStageYesRB;
	private JRadioButton qGroupStageNoRB;
	private ButtonGroup qGroupStageRBGrp;
	private JButton jBtnChangeQGroups;
	private JButton jBtnAddQGroup;
	private ArrayList<JLabel> jLblsQGroups;
	private ArrayList<JLabel> jLblsRemoveQGroup;
	private JButton jBtnSaveQGroups;
	private JLabel jLblNumberOfTeamsQGroups;
	private JRadioButton jRBSameNumberOfTeamsQGroups;
	private JComboBox<String> jCBSameNumberOfTeamsQGroups;
	private JRadioButton jRBDifferentNumberOfTeamsQGroups;
	private ArrayList<JComboBox<String>> jCBsNumberOfTeamsQGroups;
	private ButtonGroup numberOfTeamsQGroupsRBGrp;
	private JLabel numOfQGroupsLbl;
	private JLabel qGroupSecondLegLbl;
	private JRadioButton qGroupSecondLegYesRB;
	private JRadioButton qGroupSecondLegNoRB;
	private ButtonGroup qGroupSecondLegRBGrp;
	private JComboBox<String> detailsQGroupCB;
	
	private JPanel qKoStagePnl;
	private JLabel qKoStageLbl;
	private JRadioButton qKoStageYesRB;
	private JRadioButton qKoStageNoRB;
	private ButtonGroup qKoStageRBGrp;
	
	// Group stage
	private JPanel groupStagePnl;
	private JLabel groupStageLbl;
	private JRadioButton groupStageYesRB;
	private JRadioButton groupStageNoRB;
	private ButtonGroup groupStageRBGrp;
	private JButton jBtnChangeGroups;
	private JButton jBtnAddGroup;
	private ArrayList<JLabel> jLblsGroups;
	private ArrayList<JLabel> jLblsRemoveGroup;
	private JButton jBtnSaveGroups;
	private JLabel jLblNumberOfTeams;
	private JRadioButton jRBSameNumberOfTeams;
	private JComboBox<String> jCBSameNumberOfTeams;
	private JRadioButton jRBDifferentNumberOfTeams;
	private ArrayList<JComboBox<String>> jCBsNumberOfTeams;
	private ButtonGroup numberOfTeamsRBGrp;
	private JLabel numOfGroupsLbl;
	private JLabel groupSecondLegLbl;
	private JRadioButton groupSecondLegYesRB;
	private JRadioButton groupSecondLegNoRB;
	private ButtonGroup groupSecondLegRBGrp;
	private JComboBox<String> detailsGroupCB;

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
	private JComboBox<String> detailsKOCB;
	private JLabel koSecondLegLbl;
	private JRadioButton koSecondLegYesRB;
	private JRadioButton koSecondLegNoRB;
	private ButtonGroup koSecondLegRBGrp;
	private JButton jBtnAddTeam;
	private JButton jBtnDeleteTeam;
	private JLabel jLblPreviousRound;
	private JComboBox<String> jCBPreviousRound;
	private JComboBox<String>[] jCBsPlatzierungen;
	private JComboBox<String>[] jCBsBestGroupXth;
	private JLabel[] jLblsPlatzierungen;
	private JLabel jLblNoPreviousRound;
	private JLabel jLblTeamsPQVal;
	private JLabel jLblTeamsPQ;
	private JLabel jLblTeamsPQMore;
	private JLabel jLblTeamsPRVal;
	private JLabel jLblTeamsPR;
	private JLabel jLblTeamsPRMore;
	private JLabel jLblTeamsOCVal;
	private JLabel jLblTeamsOC;
	private JLabel jLblTeamsOCMore;

	// Model
	private String name;
	private String shortName;
	private int season;
	private int stDate;
	private int fiDate;
	private boolean isSTSS;
	private boolean hasQ;
	private boolean hasQGrp;
	private boolean hasQKO;
	private boolean hasGrp;
	private boolean hasKO;
	private boolean has3pl;
	private int numberOfQGroups;
	private int numberOfGroups;
	private int nOKO;
	
	private ArrayList<String> qConfig;
	private ArrayList<String> grpConfig;
	private ArrayList<String> koConfig;
	private String[][] teamsQG;
	private String[][] teamsQKO;
	private String[][] teamsGrp;
	private String[][] teamsKO;
	
	private int step = 0;
	
	private ArrayList<Integer> numberOfTeamsInQGroup;
	private ArrayList<ArrayList<String>> teamsNamesQGroupStage;
	private int currentQGroup;
	
	private ArrayList<Integer> numberOfTeamsInGroup;
	private ArrayList<ArrayList<String>> teamsNamesGroupStage;
	private int currentGroup;
	
	private boolean[] isKORoundSelected;
	private ArrayList<Integer> selectedKORounds;
	private ArrayList<Integer> possiblePreviousKORounds;
	private int[] previousKORound;
	private ArrayList<int[]> teamsComingFrom;
	private int currentKORound;
	private int showingMoreFrom;
	
	private ArrayList<ArrayList<String>> grpTeamsModelArr;
	private DefaultListModel<String> grpTeamsModel;
	
	private ArrayList<ArrayList<String>> koTeamsModelArr;
	private ArrayList<Boolean> hasKORoundSecondLeg;
	private ArrayList<ArrayList<String>> allKORoundsTeams;
	private ArrayList<String> currentKORoundTeamsPQ;
	private ArrayList<String> currentKORoundTeamsPR;
	private ArrayList<String> currentKORoundTeamsOC;
	private DefaultListModel<String> koTeamsModel;


	public NewTournamentDialog() {
		super();
		
		initGUI();
		updateGUI();
	}

	private void initGUI() {
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		grpTeamsModelArr = new ArrayList<>();
		koTeamsModelArr = new ArrayList<>();
		jLTeamsModel = new DefaultListModel<>();
		

		{
			jLblWettbewerb = new JLabel();
			getContentPane().add(jLblWettbewerb);
			jLblWettbewerb.setBounds(REC_WETTBLBL);
			jLblWettbewerb.setFont(fontWettbewerb);
			alignCenter(jLblWettbewerb);
			jLblWettbewerb.setVisible(false);
		}
		
		{
			go = new JButton();
			getContentPane().add(go);
			go.setText("weiter");
			go.setFocusable(false);
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
			cancel.setFocusable(false);
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelActionPerformed();
				}
			});
		}
		
		{
			jLblDetailsFor = new JLabel();
			jLblDetailsFor.setBounds(REC_DETKOLBL);
			jLblDetailsFor.setText("Details zu");
			jLblDetailsFor.setVisible(false);
		}
		{
			jSPTeams = new JScrollPane();
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
			jBtnEditTeam.setText("Bearbeiten");
			jBtnEditTeam.setVisible(false);
			jBtnEditTeam.setFocusable(false);
			jBtnEditTeam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnEditTeamActionPerformed();
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
			nameLbl.setText("Name");
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
			startDateLbl.setText("Beginn");
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
			finalDateLbl.setText("Ende");
		}
		{
			finalDateTF = new JTextField();
			infoPnl.add(finalDateTF);
			finalDateTF.setBounds(REC_FIDTF);
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
		jLblsQGroups = new ArrayList<>();
		jLblsRemoveQGroup = new ArrayList<>();
		jCBsNumberOfTeamsQGroups = new ArrayList<>();
		numberOfTeamsInQGroup = new ArrayList<>();
		teamsNamesQGroupStage = new ArrayList<>();
		posNumOfTeamsPerQGroup = new String[maxNumberOfTeamsPerGroup - minNumberOfTeamsPerGroup + 1];
		for (int i = 0; i < posNumOfTeamsPerQGroup.length; i++) {
			posNumOfTeamsPerQGroup[i] = "" + (i + minNumberOfTeamsPerGroup);
		}
		
		{
			qualificationPnl = new JPanel();
			getContentPane().add(qualificationPnl);
			qualificationPnl.setLayout(null);
			qualificationPnl.setOpaque(true);
			qualificationPnl.setBackground(background);
			qualificationPnl.setVisible(false);
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
			qualificationYesRB.setFocusable(false);
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
			qualificationNoRB.setFocusable(false);
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
		// Qualification - Group Stage
		{
			qGroupStagePnl = new JPanel();
			qualificationPnl.add(qGroupStagePnl);
			qGroupStagePnl.setLayout(null);
			qGroupStagePnl.setOpaque(true);
			qGroupStagePnl.setBackground(background);
			qGroupStagePnl.setVisible(false);
		}
		{
			qGroupStageLbl = new JLabel();
			qGroupStagePnl.add(qGroupStageLbl);
			qGroupStageLbl.setBounds(REC_QGRPLBL);
			qGroupStageLbl.setText("Gruppenphase");
		}
		{
			qGroupStageYesRB = new JRadioButton("ja");
			qGroupStagePnl.add(qGroupStageYesRB);
			qGroupStageYesRB.setBounds(REC_QGRPYES);
			qGroupStageYesRB.setActionCommand("true");
			qGroupStageYesRB.setOpaque(false);
			qGroupStageYesRB.setFocusable(false);
			qGroupStageYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					qGroupStageRBchanged(true);
				}
			});
		}
		{
			qGroupStageNoRB = new JRadioButton("nein");
			qGroupStagePnl.add(qGroupStageNoRB);
			qGroupStageNoRB.setBounds(REC_QGRPNO);
			qGroupStageNoRB.setActionCommand("false");
			qGroupStageNoRB.setOpaque(false);
			qGroupStageNoRB.setFocusable(false);
			qGroupStageNoRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					qGroupStageRBchanged(false);
				}
			});
		}
		{
			qGroupStageRBGrp = new ButtonGroup();
			qGroupStageRBGrp.add(qGroupStageYesRB);
			qGroupStageRBGrp.add(qGroupStageNoRB);
		}
		// Anzahl Gruppen
		{
			numOfQGroupsLbl = new JLabel();
			qGroupStagePnl.add(numOfQGroupsLbl);
			numOfQGroupsLbl.setBounds(REC_NOFQGRPLBL);
		}
		{
			jBtnChangeQGroups = new JButton();
			qGroupStagePnl.add(jBtnChangeQGroups);
			jBtnChangeQGroups.setText("Ändern");
			jBtnChangeQGroups.setBounds(REC_BTNCHNOFQGRP);
			jBtnChangeQGroups.setVisible(false);
			jBtnChangeQGroups.setFocusable(false);
			jBtnChangeQGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnChangeQGroupsActionPerformed();
				}
			});
		}
		{
			jBtnAddQGroup = new JButton();
			qGroupStagePnl.add(jBtnAddQGroup);
			jBtnAddQGroup.setText("Hinzufügen");
			jBtnAddQGroup.setBounds(REC_BTNADDQGRP);
			jBtnAddQGroup.setFocusable(false);
			jBtnAddQGroup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnAddQGroupActionPerformed();
				}
			});
		}
		{
			jBtnSaveQGroups = new JButton();
			qGroupStagePnl.add(jBtnSaveQGroups);
			jBtnSaveQGroups.setText("Speichern");
			jBtnSaveQGroups.setBounds(REC_BTNSAVEQGRP);
			jBtnSaveQGroups.setFocusable(false);
			jBtnSaveQGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnSaveQGroupsActionPerformed();
				}
			});
		}
		{
			jLblNumberOfTeamsQGroups = new JLabel();
			qGroupStagePnl.add(jLblNumberOfTeamsQGroups);
			jLblNumberOfTeamsQGroups.setBounds(REC_NOTQGRPLBL);
			jLblNumberOfTeamsQGroups.setText("Anzahl Teams pro Gruppe:");
		}
		{
			jRBSameNumberOfTeamsQGroups = new JRadioButton("immer");
			qGroupStagePnl.add(jRBSameNumberOfTeamsQGroups);
			jRBSameNumberOfTeamsQGroups.setBounds(REC_SAMENOTQGRPLBL);
			jRBSameNumberOfTeamsQGroups.setOpaque(false);
			jRBSameNumberOfTeamsQGroups.setFocusable(false);
			jRBSameNumberOfTeamsQGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					useSameNumberOfTeamsQGroups(true);
				}
			});
		}
		{
			jCBSameNumberOfTeamsQGroups = new JComboBox<>();
			qGroupStagePnl.add(jCBSameNumberOfTeamsQGroups);
			jCBSameNumberOfTeamsQGroups.setBounds(REC_SAMENOTQGRPCB);
			jCBSameNumberOfTeamsQGroups.setFocusable(false);
			jCBSameNumberOfTeamsQGroups.setModel(new DefaultComboBoxModel<>(posNumOfTeamsPerQGroup));
		}
		{
			jRBDifferentNumberOfTeamsQGroups = new JRadioButton("unterschiedlich");
			qGroupStagePnl.add(jRBDifferentNumberOfTeamsQGroups);
			jRBDifferentNumberOfTeamsQGroups.setBounds(REC_DIFFNOTQGRPLBL);
			jRBDifferentNumberOfTeamsQGroups.setOpaque(false);
			jRBDifferentNumberOfTeamsQGroups.setFocusable(false);
			jRBDifferentNumberOfTeamsQGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					useSameNumberOfTeamsQGroups(false);
				}
			});
		}
		{
			numberOfTeamsQGroupsRBGrp = new ButtonGroup();
			numberOfTeamsQGroupsRBGrp.add(jRBSameNumberOfTeamsQGroups);
			numberOfTeamsQGroupsRBGrp.add(jRBDifferentNumberOfTeamsQGroups);
		}
		// second leg
		{
			qGroupSecondLegLbl = new JLabel();
			qGroupStagePnl.add(qGroupSecondLegLbl);
			qGroupSecondLegLbl.setBounds(REC_QGRP2LEGLBL);
			qGroupSecondLegLbl.setText("Rückspiel");
			qGroupSecondLegLbl.setVisible(false);
		}
		{
			qGroupSecondLegYesRB = new JRadioButton("ja");
			qGroupStagePnl.add(qGroupSecondLegYesRB);
			qGroupSecondLegYesRB.setBounds(REC_QGRP2LEGYES);
			qGroupSecondLegYesRB.setActionCommand("true");
			qGroupSecondLegYesRB.setOpaque(false);
			qGroupSecondLegYesRB.setFocusable(false);
			qGroupSecondLegYesRB.setVisible(false);
		}
		{
			qGroupSecondLegNoRB = new JRadioButton("nein");
			qGroupStagePnl.add(qGroupSecondLegNoRB);
			qGroupSecondLegNoRB.setBounds(REC_QGRP2LEGNO);
			qGroupSecondLegNoRB.setActionCommand("false");
			qGroupSecondLegNoRB.setOpaque(false);
			qGroupSecondLegNoRB.setFocusable(false);
			qGroupSecondLegNoRB.setVisible(false);
		}
		{
			qGroupSecondLegRBGrp = new ButtonGroup();
			qGroupSecondLegRBGrp.add(qGroupSecondLegYesRB);
			qGroupSecondLegRBGrp.add(qGroupSecondLegNoRB);
		}
		// Teamnamen
		{
			detailsQGroupCB = new JComboBox<>();
			qGroupStagePnl.add(detailsQGroupCB);
			detailsQGroupCB.setBounds(REC_DETQGRPCB);
			detailsQGroupCB.setVisible(false);
			detailsQGroupCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					detailsQGroupCBItemStateChanged(evt);
				}
			});
		}
		// Qualification - KO Stage
		{
			qKoStagePnl = new JPanel();
			qualificationPnl.add(qKoStagePnl);
			qKoStagePnl.setLayout(null);
			qKoStagePnl.setOpaque(true);
			qKoStagePnl.setBackground(background);
			qKoStagePnl.setVisible(false);
		}
		{
			qKoStageLbl = new JLabel();
			qKoStagePnl.add(qKoStageLbl);
			qKoStageLbl.setBounds(REC_QKOLBL);
			qKoStageLbl.setText("KO-Phase");
		}
		{
			qKoStageYesRB = new JRadioButton("ja");
			qKoStagePnl.add(qKoStageYesRB);
			qKoStageYesRB.setBounds(REC_QKOYES);
			qKoStageYesRB.setActionCommand("true");
			qKoStageYesRB.setOpaque(false);
			qKoStageYesRB.setFocusable(false);
			qKoStageYesRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					qKoStageRBchanged(true);
				}
			});
		}
		{
			qKoStageNoRB = new JRadioButton("nein");
			qKoStagePnl.add(qKoStageNoRB);
			qKoStageNoRB.setBounds(REC_QKONO);
			qKoStageNoRB.setActionCommand("false");
			qKoStageNoRB.setOpaque(false);
			qKoStageNoRB.setFocusable(false);
			qKoStageNoRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					qKoStageRBchanged(false);
				}
			});
		}
		{
			qKoStageRBGrp = new ButtonGroup();
			qKoStageRBGrp.add(qKoStageYesRB);
			qKoStageRBGrp.add(qKoStageNoRB);
		}
	}

	private void buildGroupStage() {
		jLblsGroups = new ArrayList<>();
		jLblsRemoveGroup = new ArrayList<>();
		jCBsNumberOfTeams = new ArrayList<>();
		numberOfTeamsInGroup = new ArrayList<>();
		teamsNamesGroupStage = new ArrayList<>();
		posNumOfTeamsPerGroup = new String[maxNumberOfTeamsPerGroup - minNumberOfTeamsPerGroup + 1];
		for (int i = 0; i < posNumOfTeamsPerGroup.length; i++) {
			posNumOfTeamsPerGroup[i] = "" + (i + minNumberOfTeamsPerGroup);
		}
		{
			groupStagePnl = new JPanel();
			getContentPane().add(groupStagePnl);
			groupStagePnl.setLayout(null);
			groupStagePnl.setOpaque(true);
			groupStagePnl.setBackground(background);
			groupStagePnl.setVisible(false);
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
			groupStageYesRB.setFocusable(false);
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
			groupStageNoRB.setFocusable(false);
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
			numOfGroupsLbl = new JLabel();
			groupStagePnl.add(numOfGroupsLbl);
			numOfGroupsLbl.setBounds(REC_NOFGRPLBL);
		}
		{
			jBtnChangeGroups = new JButton();
			groupStagePnl.add(jBtnChangeGroups);
			jBtnChangeGroups.setText("Ändern");
			jBtnChangeGroups.setBounds(REC_BTNCHNOFGRP);
			jBtnChangeGroups.setVisible(false);
			jBtnChangeGroups.setFocusable(false);
			jBtnChangeGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnChangeGroupsActionPerformed();
				}
			});
		}
		{
			jBtnAddGroup = new JButton();
			groupStagePnl.add(jBtnAddGroup);
			jBtnAddGroup.setText("Hinzufügen");
			jBtnAddGroup.setBounds(REC_BTNADDGRP);
			jBtnAddGroup.setFocusable(false);
			jBtnAddGroup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnAddGroupActionPerformed();
				}
			});
		}
		{
			jBtnSaveGroups = new JButton();
			groupStagePnl.add(jBtnSaveGroups);
			jBtnSaveGroups.setText("Speichern");
			jBtnSaveGroups.setBounds(REC_BTNSAVEGRP);
			jBtnSaveGroups.setFocusable(false);
			jBtnSaveGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnSaveGroupsActionPerformed();
				}
			});
		}
		{
			jLblNumberOfTeams = new JLabel();
			groupStagePnl.add(jLblNumberOfTeams);
			jLblNumberOfTeams.setBounds(REC_NOTGRPLBL);
			jLblNumberOfTeams.setText("Anzahl Teams pro Gruppe:");
		}
		{
			jRBSameNumberOfTeams = new JRadioButton("immer");
			groupStagePnl.add(jRBSameNumberOfTeams);
			jRBSameNumberOfTeams.setBounds(REC_SAMENOTGRPLBL);
			jRBSameNumberOfTeams.setOpaque(false);
			jRBSameNumberOfTeams.setFocusable(false);
			jRBSameNumberOfTeams.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					useSameNumberOfTeams(true);
				}
			});
		}
		{
			jCBSameNumberOfTeams = new JComboBox<>();
			groupStagePnl.add(jCBSameNumberOfTeams);
			jCBSameNumberOfTeams.setBounds(REC_SAMENOTGRPCB);
			jCBSameNumberOfTeams.setFocusable(false);
			jCBSameNumberOfTeams.setModel(new DefaultComboBoxModel<>(posNumOfTeamsPerGroup));
		}
		{
			jRBDifferentNumberOfTeams = new JRadioButton("unterschiedlich");
			groupStagePnl.add(jRBDifferentNumberOfTeams);
			jRBDifferentNumberOfTeams.setBounds(REC_DIFFNOTGRPLBL);
			jRBDifferentNumberOfTeams.setOpaque(false);
			jRBDifferentNumberOfTeams.setFocusable(false);
			jRBDifferentNumberOfTeams.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					useSameNumberOfTeams(false);
				}
			});
		}
		{
			numberOfTeamsRBGrp = new ButtonGroup();
			numberOfTeamsRBGrp.add(jRBSameNumberOfTeams);
			numberOfTeamsRBGrp.add(jRBDifferentNumberOfTeams);
		}
		// second leg
		{
			groupSecondLegLbl = new JLabel();
			groupStagePnl.add(groupSecondLegLbl);
			groupSecondLegLbl.setBounds(REC_GRP2LEGLBL);
			groupSecondLegLbl.setText("Rückspiel");
			groupSecondLegLbl.setVisible(false);
		}
		{
			groupSecondLegYesRB = new JRadioButton("ja");
			groupStagePnl.add(groupSecondLegYesRB);
			groupSecondLegYesRB.setBounds(REC_GRP2LEGYES);
			groupSecondLegYesRB.setActionCommand("true");
			groupSecondLegYesRB.setOpaque(false);
			groupSecondLegYesRB.setFocusable(false);
			groupSecondLegYesRB.setVisible(false);
		}
		{
			groupSecondLegNoRB = new JRadioButton("nein");
			groupStagePnl.add(groupSecondLegNoRB);
			groupSecondLegNoRB.setBounds(REC_GRP2LEGNO);
			groupSecondLegNoRB.setActionCommand("false");
			groupSecondLegNoRB.setOpaque(false);
			groupSecondLegNoRB.setFocusable(false);
			groupSecondLegNoRB.setVisible(false);
		}
		{
			groupSecondLegRBGrp = new ButtonGroup();
			groupSecondLegRBGrp.add(groupSecondLegYesRB);
			groupSecondLegRBGrp.add(groupSecondLegNoRB);
		}
		// Teamnamen
		{
			detailsGroupCB = new JComboBox<>();
			groupStagePnl.add(detailsGroupCB);
			detailsGroupCB.setBounds(REC_DETGRPCB);
			detailsGroupCB.setVisible(false);
			detailsGroupCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					detailsGroupCBItemStateChanged(evt);
				}
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	private void buildKOStage() {
		jChBKORunden = new JCheckBox[possibleKORounds.length];
		isKORoundSelected = new boolean[possibleKORounds.length];
		previousKORound = new int[possibleKORounds.length];
		jCBsPlatzierungen = new JComboBox[3];
		jCBsBestGroupXth = new JComboBox[3];
		jLblsPlatzierungen = new JLabel[3];
		selectedKORounds = new ArrayList<>();
		possiblePreviousKORounds = new ArrayList<>();
		hasKORoundSecondLeg = new ArrayList<>();
		allKORoundsTeams = new ArrayList<>();
		currentKORoundTeamsPQ = new ArrayList<>();
		currentKORoundTeamsPR = new ArrayList<>();
		currentKORoundTeamsOC = new ArrayList<>();
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
			koStagePnl.setVisible(false);
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
			koStageYesRB.setFocusable(false);
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
			koStageNoRB.setFocusable(false);
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
			jBtnChangeKORounds.setText("Ändern");
			jBtnChangeKORounds.setBounds(REC_BTNCHNOFKO);
			jBtnChangeKORounds.setVisible(false);
			jBtnChangeKORounds.setFocusable(false);
			jBtnChangeKORounds.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnChangeKORoundsActionPerformed();
				}
			});
		}
		
		for (int i = 0; i < jChBKORunden.length; i++) {
			jChBKORunden[i] = new JCheckBox(possibleKORounds[i]);
			koStagePnl.add(jChBKORunden[i]);
			jChBKORunden[i].setBounds(koRounds[STARTX], koRounds[STARTY] + i * koRounds[GAPY], koRounds[SIZEX], koRounds[SIZEY]);
			jChBKORunden[i].setOpaque(false);
			jChBKORunden[i].setFocusable(false);
		}
		{
			jBtnSaveKORounds = new JButton();
			koStagePnl.add(jBtnSaveKORounds);
			jBtnSaveKORounds.setText("Speichern");
			jBtnSaveKORounds.setBounds(30, 300, 100, 25);
			jBtnSaveKORounds.setFocusable(false);
			jBtnSaveKORounds.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnSaveKORoundsActionPerformed();
				}
			});
		}
		
		// second leg
		{
			koSecondLegLbl = new JLabel();
			koStagePnl.add(koSecondLegLbl);
			koSecondLegLbl.setBounds(REC_KO2LEGLBL);
			koSecondLegLbl.setText("Rückspiel");
			koSecondLegLbl.setVisible(false);
		}
		{
			koSecondLegYesRB = new JRadioButton("ja");
			koStagePnl.add(koSecondLegYesRB);
			koSecondLegYesRB.setBounds(REC_KO2LEGYES);
			koSecondLegYesRB.setActionCommand("true");
			koSecondLegYesRB.setOpaque(false);
			koSecondLegYesRB.setFocusable(false);
			koSecondLegYesRB.setVisible(false);
		}
		{
			koSecondLegNoRB = new JRadioButton("nein");
			koStagePnl.add(koSecondLegNoRB);
			koSecondLegNoRB.setBounds(REC_KO2LEGNO);
			koSecondLegNoRB.setActionCommand("false");
			koSecondLegNoRB.setOpaque(false);
			koSecondLegNoRB.setFocusable(false);
			koSecondLegNoRB.setVisible(false);
		}
		{
			koSecondLegRBGrp = new ButtonGroup();
			koSecondLegRBGrp.add(koSecondLegYesRB);
			koSecondLegRBGrp.add(koSecondLegNoRB);
		}
		// Anzahl KO-Runden
		
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
		{
			jBtnAddTeam = new JButton();
			koStagePnl.add(jBtnAddTeam);
			jBtnAddTeam.setText("Hinzufügen");
			jBtnAddTeam.setVisible(false);
			jBtnAddTeam.setFocusable(false);
			jBtnAddTeam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnAddTeamActionPerformed();
				}
			});
		}
		{
			jBtnDeleteTeam = new JButton();
			koStagePnl.add(jBtnDeleteTeam);
			jBtnDeleteTeam.setText("Entfernen");
			jBtnDeleteTeam.setVisible(false);
			jBtnDeleteTeam.setFocusable(false);
			jBtnDeleteTeam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnDeleteTeamActionPerformed();
				}
			});
		}
		{
			jLblPreviousRound = new JLabel();
			koStagePnl.add(jLblPreviousRound);
			alignCenter(jLblPreviousRound);
			jLblPreviousRound.setText("Vorherige Runde:");
			jLblPreviousRound.setVisible(false);
		}
		{
			jCBPreviousRound = new JComboBox<>();
			koStagePnl.add(jCBPreviousRound);
			jCBPreviousRound.setVisible(false);
		}
		
		for (int i = 0; i < jLblsPlatzierungen.length; i++) {
			final int x = i;
			jCBsPlatzierungen[i] = new JComboBox<>();
			koStagePnl.add(jCBsPlatzierungen[i]);
			jCBsPlatzierungen[i].setBounds(cbs[STARTX], cbs[STARTY] + i * cbs[GAPY], cbs[SIZEX], cbs[SIZEY]);
			jCBsPlatzierungen[i].setModel(new DefaultComboBoxModel<>(prOptions));
			jCBsPlatzierungen[i].setVisible(false);
			jCBsPlatzierungen[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					jCBsPlatzierungenItemStateChanged(x, evt);
				}
			});
			
			jCBsBestGroupXth[i] = new JComboBox<>();
			koStagePnl.add(jCBsBestGroupXth[i]);
			jCBsBestGroupXth[i].setBounds(grpXth[STARTX], grpXth[STARTY] + i * grpXth[GAPY], grpXth[SIZEX], grpXth[SIZEY]);
			jCBsBestGroupXth[i].setVisible(false);
			
			jLblsPlatzierungen[i] = new JLabel();
			koStagePnl.add(jLblsPlatzierungen[i]);
			jLblsPlatzierungen[i].setText(gruppenXte[i]);
			jLblsPlatzierungen[i].setBounds(lbls[STARTX], lbls[STARTY] + i * lbls[GAPY], lbls[SIZEX], lbls[SIZEY]);
			jLblsPlatzierungen[i].setVisible(false);
		}
		{
			jLblNoPreviousRound = new JLabel();
			koStagePnl.add(jLblNoPreviousRound);
			jLblNoPreviousRound.setBounds(REC_NOPRLBL);
			alignCenter(jLblNoPreviousRound);
			jLblNoPreviousRound.setText("Es gibt keine vorherige Runde!");
			jLblNoPreviousRound.setVisible(false);
		}
		{
			jLblTeamsPQVal = new JLabel();
			koStagePnl.add(jLblTeamsPQVal);
			jLblTeamsPQVal.setBounds(REC_NOTPQVALLBL);
			alignCenter(jLblTeamsPQVal);
			jLblTeamsPQVal.setVisible(false);
		}
		{
			jLblTeamsPQ = new JLabel();
			koStagePnl.add(jLblTeamsPQ);
			jLblTeamsPQ.setBounds(REC_NOTPQLBL);
			jLblTeamsPQ.setText("Teams qualifiziert");
			jLblTeamsPQ.setVisible(false);
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
			alignCenter(jLblTeamsPRVal);
			jLblTeamsPRVal.setVisible(false);
		}
		{
			jLblTeamsPR = new JLabel();
			koStagePnl.add(jLblTeamsPR);
			jLblTeamsPR.setBounds(REC_NOTPRLBL);
			jLblTeamsPR.setText("Teams aus vorherigen Runden");
			jLblTeamsPR.setVisible(false);
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
			alignCenter(jLblTeamsOCVal);
			jLblTeamsOCVal.setVisible(false);
		}
		{
			jLblTeamsOC = new JLabel();
			koStagePnl.add(jLblTeamsOC);
			jLblTeamsOC.setBounds(REC_NOTOCLBL);
			jLblTeamsOC.setText("Teams aus anderen Wettbewerben");
			jLblTeamsOC.setVisible(false);
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
	}
	
	private void enterPresetValues() {
		nameTF.setText("Pseudo Turnier");
		shortNameTF.setText("PT");
		startDateTF.setText("12.06.2015");
		finalDateTF.setText("04.07.2016");
		
		qualificationNoRB.setSelected(true);
		qGroupStageNoRB.setSelected(true);
		jRBSameNumberOfTeamsQGroups.setSelected(true);
		jCBSameNumberOfTeamsQGroups.setSelectedIndex(4 - minNumberOfTeamsPerGroup);
		qGroupSecondLegNoRB.setSelected(true);
		qKoStageYesRB.setSelected(true);
		
		groupStageYesRB.setSelected(true);
		jRBSameNumberOfTeams.setSelected(true);
		jCBSameNumberOfTeams.setSelectedIndex(4 - minNumberOfTeamsPerGroup);
		groupSecondLegNoRB.setSelected(true);
		
		koStageYesRB.setSelected(true);
		koSecondLegNoRB.setSelected(true);
		jChBKORunden[5].setSelected(true);
		jChBKORunden[6].setSelected(true);
		jChBKORunden[7].setSelected(true);
		jChBKORunden[9].setSelected(true);
		jCBsPlatzierungen[2].setSelectedIndex(1);
	}
	
	private void updateGUI() {
		hasQ = Boolean.parseBoolean(qualificationRBGrp.getSelection().getActionCommand());
		hasQGrp = Boolean.parseBoolean(qGroupStageRBGrp.getSelection().getActionCommand());
		hasQKO = Boolean.parseBoolean(qKoStageRBGrp.getSelection().getActionCommand());
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		
		int heightQG = hasQGrp ? 375 : 35;
		REC_QGRPPNL = new Rectangle(0, 70, 380, heightQG);
		qGroupStagePnl.setBounds(REC_QGRPPNL);

		// TODO
		int heightQK = hasQKO ? 250 : 35;
		REC_QKOPNL = new Rectangle(0, 70 + heightQG, 380, heightQK);
		qKoStagePnl.setBounds(REC_QKOPNL);
		
		int heightQ = hasQ ? 70 + heightQG + heightQK : 50;
		REC_QPNL = new Rectangle(OFFSETX, OFFSETY + REC_WETTBLBL.height + GAPPNL, 380, heightQ);
		qualificationPnl.setBounds(REC_QPNL);
		
		int heightG = hasGrp ? 400 : 50;
		REC_GRPPNL = new Rectangle(OFFSETX, OFFSETY + REC_WETTBLBL.height + GAPPNL, 380, heightG);
		groupStagePnl.setBounds(REC_GRPPNL);
		
		int heightK = hasKO ? 480 : 50;
		REC_KOPNL = new Rectangle(OFFSETX, OFFSETY + REC_WETTBLBL.height + GAPPNL, 380, heightK);
		koStagePnl.setBounds(REC_KOPNL);
		
		int startyGo = 0;
		if (step == 0)	startyGo = REC_INFOPNL.y + REC_INFOPNL.height;
		else if (step == 1)	startyGo = REC_QPNL.y + REC_QPNL.height;
		else if (step == 2)	startyGo = REC_GRPPNL.y + REC_GRPPNL.height;
		else if (step == 3)	startyGo = REC_KOPNL.y + REC_KOPNL.height;
		REC_CANCEL = new Rectangle(200, startyGo + GAPPNL, 100, 30);
		cancel.setBounds(REC_CANCEL);
		
		REC_GO = new Rectangle(310, startyGo + GAPPNL, 70, 30);
		go.setBounds(REC_GO);
		
		dim = new Dimension(380 + 2 * OFFSETX + getWindowDecorationWidth(), REC_GO.y + REC_GO.height + OFFSETY + getWindowDecorationHeight());
		setSize(this.dim);
		setLocationRelativeTo(null);
	}
	
	private void qualificationRBchanged(boolean hasQ) {
		log("Qualification has been " + (hasQ ? "enabled." : "disabled."));
		this.hasQ = hasQ;
		
		showQualificationConfiguration(true);
		updateGUI();
	}
	
	private void qGroupStageRBchanged(boolean hasQGrp) {
		log("Qualification group stage has been " + (hasQGrp ? "enabled." : "disabled."));
		this.hasQGrp = hasQGrp;
		
		showQGroupStageConfiguration(true);
		updateGUI();
	}
	
	private void qKoStageRBchanged(boolean hasQKO) {
		log("Qualification knockout stage has been " + (hasQKO ? "enabled." : "disabled."));
		this.hasQKO = hasQKO;
		
//		if (showingMoreFrom != 0)	jLblMoreClicked(showingMoreFrom);
		showQKORoundConfiguration(true);
		updateGUI();
	}
	
	private void groupStageRBchanged(boolean hasGrp) {
		log("Group stage has been " + (hasGrp ? "enabled." : "disabled."));
		this.hasGrp = hasGrp;
		
		showGroupStageConfiguration(true);
		updateGUI();
	}
	
	private void koStageRBchanged(boolean hasKO) {
		log("Knockout stage has been " + (hasKO ? "enabled." : "disabled."));
		this.hasKO = hasKO;
		
		if (showingMoreFrom != 0)	jLblMoreClicked(showingMoreFrom);
		showKORoundConfiguration(true);
		updateGUI();
	}
	
	private void showQualificationConfiguration(boolean show) {
		qStartDateLbl.setVisible(hasQ && show);
		qStartDateTF.setVisible(hasQ && show);
		qFinalDateLbl.setVisible(hasQ && show);
		qFinalDateTF.setVisible(hasQ && show);
		
		qGroupStagePnl.setVisible(hasQ && show);
		qGroupStageLbl.setVisible(hasQ && show);
		qGroupStageYesRB.setVisible(hasQ && show);
		qGroupStageNoRB.setVisible(hasQ && show);
		
		qKoStagePnl.setVisible(hasQ && show);
		qKoStageLbl.setVisible(hasQ && show);
		qKoStageYesRB.setVisible(hasQ && show);
		qKoStageNoRB.setVisible(hasQ && show);
	}
	
	private void showQGroupStageConfiguration(boolean show) {
		for (int i = 0; i < jLblsQGroups.size(); i++) {
			jLblsQGroups.get(i).setVisible(hasQGrp && show);
			jLblsRemoveQGroup.get(i).setVisible(hasQGrp && show);
			jCBsNumberOfTeamsQGroups.get(i).setVisible(hasQGrp && show && jRBDifferentNumberOfTeamsQGroups.isSelected());
		}
		jBtnAddQGroup.setVisible(hasQGrp && show);
		jBtnSaveQGroups.setVisible(hasQGrp && show);
		jLblNumberOfTeamsQGroups.setVisible(hasQGrp && show);
		jRBSameNumberOfTeamsQGroups.setVisible(hasQGrp && show);
		jCBSameNumberOfTeamsQGroups.setVisible(hasQGrp && show);
		jRBDifferentNumberOfTeamsQGroups.setVisible(hasQGrp && show);
		
		jBtnChangeQGroups.setVisible(hasQGrp && !show);
		numOfQGroupsLbl.setVisible(hasQGrp && !show);
		qGroupSecondLegLbl.setVisible(hasQGrp && !show);
		qGroupSecondLegYesRB.setVisible(hasQGrp && !show);
		qGroupSecondLegNoRB.setVisible(hasQGrp && !show);
		jLblDetailsFor.setVisible(hasQGrp && !show);
		detailsQGroupCB.setVisible(hasQGrp && !show);
		jSPTeams.setVisible(hasQGrp && !show);
		jBtnEditTeam.setVisible(hasQGrp && !show);
	}
	
	private void showQKORoundConfiguration(boolean show) {
		// TODO
		
	}
	
	private void jBtnChangeQGroupsActionPerformed() {
		saveDataForQGroup(currentQGroup);
		showQGroupStageConfiguration(true);
	}
	
	private void saveDataForQGroup(int index) {
		teamsNamesQGroupStage.get(index).clear();
		for (int i = 0; i < jLTeamsModel.getSize(); i++) {
			teamsNamesQGroupStage.get(index).add(jLTeamsModel.getElementAt(i));
		}
	}
	
	private void showDataForQGroup(int index) {
		jLTeamsModel.clear();
		for (int i = 0; i < teamsNamesQGroupStage.get(index).size(); i++) {
			jLTeamsModel.addElement(teamsNamesQGroupStage.get(index).get(i));
		}
		currentQGroup = index;
	}
	
	private void setDetailsQGroupCBModel() {
		String[] groupsNames = new String[numberOfQGroups];
		for (int i = 0; i < groupsNames.length; i++) {
			groupsNames[i] = "Gruppe " + alphabet[i];
		}
		detailsQGroupCB.setModel(new DefaultComboBoxModel<>(groupsNames));
	}
	
	private void detailsQGroupCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			saveDataForQGroup(currentQGroup);
			int index = detailsQGroupCB.getSelectedIndex();
			showDataForQGroup(index);
		}
	}
	
	private void useSameNumberOfTeamsQGroups(boolean useSame) {
		for (int i = 0; i < jCBsNumberOfTeamsQGroups.size(); i++) {
			jCBsNumberOfTeamsQGroups.get(i).setVisible(!useSame);
		}
	}
	
	private void jBtnAddQGroupActionPerformed() {
		if (numberOfQGroups == maxNumberOfQGroups) {
			message("Es kann nicht mehr als " + maxNumberOfQGroups + " Qualifikations-Gruppen geben.");
			return;
		}
		numberOfTeamsInQGroup.add(defaultNumberOfTeamsPerGroup);
		{
			JLabel label = new JLabel();
			qGroupStagePnl.add(label);
			label.setText("Gruppe " + alphabet[numberOfQGroups]);
			label.setBounds(qGroups[STARTX], qGroups[STARTY] + numberOfQGroups * qGroups[GAPY], qGroups[SIZEX], qGroups[SIZEY]);
			jLblsQGroups.add(label);
			
			label = new JLabel();
			qGroupStagePnl.add(label);
			label.setText("X");
			label.setBounds(rmvQGroups[STARTX], rmvQGroups[STARTY] + numberOfQGroups * rmvQGroups[GAPY], rmvQGroups[SIZEX], rmvQGroups[SIZEY]);
			alignCenter(label);
			label.setBackground(backgroundRemove);
			label.setForeground(foregroundRemove);
			label.setOpaque(true);
			label.setCursor(handCursor);
			final int index = numberOfQGroups;
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					removeQGroup(index);
				}
			});
			jLblsRemoveQGroup.add(label);
			
			final JComboBox<String> comboBox = new JComboBox<>();
			qGroupStagePnl.add(comboBox);
			comboBox.setBounds(nOfTQGrps[STARTX], nOfTQGrps[STARTY] + numberOfQGroups * nOfTQGrps[GAPY], nOfTQGrps[SIZEX], nOfTQGrps[SIZEY]);
			comboBox.setModel(new DefaultComboBoxModel<>(posNumOfTeamsPerQGroup));
			comboBox.setSelectedIndex(defaultNumberOfTeamsPerGroup - minNumberOfTeamsPerGroup);
			comboBox.setVisible(jRBDifferentNumberOfTeamsQGroups.isSelected());
			jCBsNumberOfTeamsQGroups.add(comboBox);
			
			ArrayList<String> list = new ArrayList<>();
			for (int i = 0; i < numberOfTeamsInQGroup.get(teamsNamesQGroupStage.size()); i++) {
				list.add("Mannschaft " + (i + 1));
			}
			teamsNamesQGroupStage.add(list);
		}
		numberOfQGroups++;
	}
	
	private void removeQGroup(int index) {
		if (numberOfQGroups == minNumberOfQGroups) {
			message("Es muss mindestens " + minNumberOfQGroups + " Gruppe(n) geben.");
			return;
		}
		boolean hasData = false;
		for (int i = 0; i < teamsNamesQGroupStage.get(index).size() && !hasData; i++) {
			if (!teamsNamesQGroupStage.get(index).get(i).equals("Mannschaft " + (i + 1)))	hasData = true;
		}
		if (hasData) {
			int cont = yesNoDialog("Es wurden für die gelöschte Gruppe " + alphabet[index] + " bereits Daten bereitgestellt. Diese gehen hierbei verloren. Trotzdem fortfahren?");
			if (cont != JOptionPane.YES_OPTION)	return;
		}
		numberOfQGroups--;
		numberOfTeamsInQGroup.remove(index);
		teamsNamesQGroupStage.remove(index);
		jCBsNumberOfTeamsQGroups.remove(index).setVisible(false);
		for (int i = index; i < jCBsNumberOfTeamsQGroups.size(); i++) {
			Point loc = jCBsNumberOfTeamsQGroups.get(i).getLocation();
			jCBsNumberOfTeamsQGroups.get(i).setLocation(loc.x, loc.y - 25);
		}
		jLblsQGroups.remove(numberOfQGroups).setVisible(false);
		jLblsRemoveQGroup.remove(numberOfQGroups).setVisible(false);
	}
	
	private void jBtnSaveQGroupsActionPerformed() {
		if (numberOfQGroups == 0) {
			message("Es muss mindestens eine Qualifikations-Gruppe geben!");
			return;
		}
		
		int[] numbersOfTeamsPerGroup = new int[numberOfQGroups];
		boolean sameNumberOfTeams = jRBSameNumberOfTeamsQGroups.isSelected();
		
		for (int i = 0; i < numberOfQGroups; i++) {
			numbersOfTeamsPerGroup[i] = (sameNumberOfTeams ? jCBSameNumberOfTeamsQGroups.getSelectedIndex() : jCBsNumberOfTeamsQGroups.get(i).getSelectedIndex()) + minNumberOfTeamsPerGroup;
		}
		
		for (int i = 0; i < numberOfQGroups; i++) {
			boolean hasData = false;
			
			if (teamsNamesQGroupStage.get(i).size() > numbersOfTeamsPerGroup[i]) {
				for (int j = numbersOfTeamsPerGroup[i]; j < teamsNamesQGroupStage.get(i).size() && !hasData; j++) {
					if (!teamsNamesQGroupStage.get(i).get(j).equals("Mannschaft " + (j + 1)))	hasData = true;
				}
			}
			
			if (hasData) {
				int cont = yesNoDialog("Es wurden für die geänderte Gruppe " + alphabet[i] + " bereits Daten bereitgestellt. Diese gehen hierbei verloren. Trotzdem fortfahren?");
				if (cont != JOptionPane.YES_OPTION)	return;
			}
		}
		
		for (int i = 0; i < numberOfQGroups; i++) {
			while (teamsNamesQGroupStage.get(i).size() > numbersOfTeamsPerGroup[i]) {
				teamsNamesQGroupStage.get(i).remove(numbersOfTeamsPerGroup[i]);
			}
			while (teamsNamesQGroupStage.get(i).size() < numbersOfTeamsPerGroup[i]) {
				teamsNamesQGroupStage.get(i).add("Mannschaft " + (teamsNamesQGroupStage.get(i).size() + 1));
			}
		}
		
		numOfQGroupsLbl.setText(numberOfQGroups + " Gruppen");
		
		jSPTeams.setBounds(20, 105, 230, 90);
		jBtnEditTeam.setBounds(260, 110, 110, 25);
		
		setDetailsQGroupCBModel();
		showQGroupStageConfiguration(false);
		showDataForQGroup(0);
	}
	
	// Group stage
	
	private void setDetailsGroupCBModel() {
		String[] groupsNames = new String[numberOfGroups];
		for (int i = 0; i < groupsNames.length; i++) {
			groupsNames[i] = "Gruppe " + alphabet[i];
		}
		detailsGroupCB.setModel(new DefaultComboBoxModel<>(groupsNames));
	}
	
	private void detailsGroupCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			saveDataForGroup(currentGroup);
			int index = detailsGroupCB.getSelectedIndex();
			showDataForGroup(index);
		}
	}
	
	private void jBtnChangeGroupsActionPerformed() {
		saveDataForGroup(currentGroup);
		showGroupStageConfiguration(true);
	}
	
	private void useSameNumberOfTeams(boolean useSame) {
		for (int i = 0; i < jCBsNumberOfTeams.size(); i++) {
			jCBsNumberOfTeams.get(i).setVisible(!useSame);
		}
	}
	
	private void jBtnAddGroupActionPerformed() {
		if (numberOfGroups == maxNumberOfGroups) {
			message("Es kann nicht mehr als " + maxNumberOfGroups + " Gruppen geben.");
			return;
		}
		numberOfTeamsInGroup.add(defaultNumberOfTeamsPerGroup);
		{
			JLabel label = new JLabel();
			groupStagePnl.add(label);
			label.setText("Gruppe " + alphabet[numberOfGroups]);
			label.setBounds(groups[STARTX], groups[STARTY] + numberOfGroups * groups[GAPY], groups[SIZEX], groups[SIZEY]);
			jLblsGroups.add(label);
			
			label = new JLabel();
			groupStagePnl.add(label);
			label.setText("X");
			label.setBounds(rmvGroups[STARTX], rmvGroups[STARTY] + numberOfGroups * rmvGroups[GAPY], rmvGroups[SIZEX], rmvGroups[SIZEY]);
			alignCenter(label);
			label.setBackground(backgroundRemove);
			label.setForeground(foregroundRemove);
			label.setOpaque(true);
			label.setCursor(handCursor);
			final int index = numberOfGroups;
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					removeGroup(index);
				}
			});
			jLblsRemoveGroup.add(label);
			
			final JComboBox<String> comboBox = new JComboBox<>();
			groupStagePnl.add(comboBox);
			comboBox.setBounds(nOfTGrps[STARTX], nOfTGrps[STARTY] + numberOfGroups * nOfTGrps[GAPY], nOfTGrps[SIZEX], nOfTGrps[SIZEY]);
			comboBox.setModel(new DefaultComboBoxModel<>(posNumOfTeamsPerGroup));
			comboBox.setSelectedIndex(defaultNumberOfTeamsPerGroup - minNumberOfTeamsPerGroup);
			comboBox.setVisible(jRBDifferentNumberOfTeams.isSelected());
			jCBsNumberOfTeams.add(comboBox);
			
			ArrayList<String> list = new ArrayList<>();
			for (int i = 0; i < numberOfTeamsInGroup.get(teamsNamesGroupStage.size()); i++) {
				list.add("Mannschaft " + (i + 1));
			}
			teamsNamesGroupStage.add(list);
		}
		numberOfGroups++;
	}
	
	private void removeGroup(int index) {
		if (numberOfGroups == minNumberOfGroups) {
			message("Es muss mindestens " + minNumberOfGroups + " Gruppe(n) geben.");
			return;
		}
		boolean hasData = false;
		for (int i = 0; i < teamsNamesGroupStage.get(index).size() && !hasData; i++) {
			if (!teamsNamesGroupStage.get(index).get(i).equals("Mannschaft " + (i + 1)))	hasData = true;
		}
		if (hasData) {
			int cont = yesNoDialog("Es wurden für die gelöschte Gruppe " + alphabet[index] + " bereits Daten bereitgestellt. Diese gehen hierbei verloren. Trotzdem fortfahren?");
			if (cont != JOptionPane.YES_OPTION)	return;
		}
		numberOfGroups--;
		numberOfTeamsInGroup.remove(index);
		teamsNamesGroupStage.remove(index);
		jCBsNumberOfTeams.remove(index).setVisible(false);
		for (int i = index; i < jCBsNumberOfTeams.size(); i++) {
			Point loc = jCBsNumberOfTeams.get(i).getLocation();
			jCBsNumberOfTeams.get(i).setLocation(loc.x, loc.y - 25);
		}
		jLblsGroups.remove(numberOfGroups).setVisible(false);
		jLblsRemoveGroup.remove(numberOfGroups).setVisible(false);
	}
	
	private void jBtnSaveGroupsActionPerformed() {
		if (numberOfGroups == 0) {
			message("Es muss mindestens eine Gruppe geben!");
			return;
		}
		
		int[] numbersOfTeamsPerGroup = new int[numberOfGroups];
		boolean sameNumberOfTeams = jRBSameNumberOfTeams.isSelected();
		
		for (int i = 0; i < numberOfGroups; i++) {
			numbersOfTeamsPerGroup[i] = (sameNumberOfTeams ? jCBSameNumberOfTeams.getSelectedIndex() : jCBsNumberOfTeams.get(i).getSelectedIndex()) + minNumberOfTeamsPerGroup;
		}
		
		for (int i = 0; i < numberOfGroups; i++) {
			boolean hasData = false;
			
			if (teamsNamesGroupStage.get(i).size() > numbersOfTeamsPerGroup[i]) {
				for (int j = numbersOfTeamsPerGroup[i]; j < teamsNamesGroupStage.get(i).size() && !hasData; j++) {
					if (!teamsNamesGroupStage.get(i).get(j).equals("Mannschaft " + (j + 1)))	hasData = true;
				}
			}
			
			if (hasData) {
				int cont = yesNoDialog("Es wurden für die geänderte Gruppe " + alphabet[i] + " bereits Daten bereitgestellt. Diese gehen hierbei verloren. Trotzdem fortfahren?");
				if (cont != JOptionPane.YES_OPTION)	return;
			}
		}
		
		for (int i = 0; i < numberOfGroups; i++) {
			while (teamsNamesGroupStage.get(i).size() > numbersOfTeamsPerGroup[i]) {
				teamsNamesGroupStage.get(i).remove(numbersOfTeamsPerGroup[i]);
			}
			while (teamsNamesGroupStage.get(i).size() < numbersOfTeamsPerGroup[i]) {
				teamsNamesGroupStage.get(i).add("Mannschaft " + (teamsNamesGroupStage.get(i).size() + 1));
			}
		}
		
		numOfGroupsLbl.setText(numberOfGroups + " Gruppen");
		
		jSPTeams.setBounds(20, 105, 230, 90);
		jBtnEditTeam.setBounds(260, 110, 110, 25);
		
		setDetailsGroupCBModel();
		showGroupStageConfiguration(false);
		showDataForGroup(0);
	}
	
	private void showGroupStageConfiguration(boolean show) {
		for (int i = 0; i < jLblsGroups.size(); i++) {
			jLblsGroups.get(i).setVisible(hasGrp && show);
			jLblsRemoveGroup.get(i).setVisible(hasGrp && show);
			jCBsNumberOfTeams.get(i).setVisible(hasGrp && show && jRBDifferentNumberOfTeams.isSelected());
		}
		jBtnAddGroup.setVisible(hasGrp && show);
		jBtnSaveGroups.setVisible(hasGrp && show);
		jLblNumberOfTeams.setVisible(hasGrp && show);
		jRBSameNumberOfTeams.setVisible(hasGrp && show);
		jCBSameNumberOfTeams.setVisible(hasGrp && show);
		jRBDifferentNumberOfTeams.setVisible(hasGrp && show);

		jBtnChangeGroups.setVisible(hasGrp && !show);
		numOfGroupsLbl.setVisible(hasGrp && !show);
		groupSecondLegLbl.setVisible(hasGrp && !show);
		groupSecondLegYesRB.setVisible(hasGrp && !show);
		groupSecondLegNoRB.setVisible(hasGrp && !show);
		jLblDetailsFor.setVisible(hasGrp && !show);
		detailsGroupCB.setVisible(hasGrp && !show);
		jSPTeams.setVisible(hasGrp && !show);
		jBtnEditTeam.setVisible(hasGrp && !show);
	}
	
	private void saveDataForGroup(int index) {
		teamsNamesGroupStage.get(index).clear();
		for (int i = 0; i < jLTeamsModel.getSize(); i++) {
			teamsNamesGroupStage.get(index).add(jLTeamsModel.getElementAt(i));
		}
	}
	
	private void showDataForGroup(int index) {
		jLTeamsModel.clear();
		for (int i = 0; i < teamsNamesGroupStage.get(index).size(); i++) {
			jLTeamsModel.addElement(teamsNamesGroupStage.get(index).get(i));
		}
		currentGroup = index;
	}
	
	private void setDetailsKOCBModel() {
		String[] model = new String[selectedKORounds.size()];
		for (int i = 0; i < model.length; i++) {
			model[i] = possibleKORounds[selectedKORounds.get(i)];
		}
		detailsKOCB.setModel(new DefaultComboBoxModel<>(model));
	}
	
	private void jBtnChangeKORoundsActionPerformed() {
		saveDataForGroup(currentGroup);
		showKORoundConfiguration(true);
	}
	
	private void jBtnSaveKORoundsActionPerformed() {
		if (allKORoundsTeams.size() == 0) {
			for (int i = 0; i < possibleKORounds.length; i++) {
				ArrayList<String> list = new ArrayList<>();
				allKORoundsTeams.add(list);
				hasKORoundSecondLeg.add(false);
			}
		}
		
		// saving provided data -> could be in different order later, checking if data is deleted
		int[][] teamsCFrom = new int[possibleKORounds.length][];
		int index = 0;
		for (int i = 0; i < jChBKORunden.length; i++) {
			if (isKORoundSelected[i]) {
				teamsCFrom[i] = teamsComingFrom.get(index++);
				if (!jChBKORunden[i].isSelected() && (teamsCFrom[i][0] != 0 || teamsCFrom[i][1] != 0 || teamsCFrom[i][2] != 0)) {
					int cont = yesNoDialog("Es wurden für die gelöschte KO-Runde " + possibleKORounds[i] + " bereits Daten bereitgestellt. Diese gehen hierbei verloren. Trotzdem fortfahren?");
					if (cont != JOptionPane.YES_OPTION)	return;
				}
			} else {
				teamsCFrom[i] = new int[] {0, 0, 0};
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
		jLblDetailsFor.setVisible(hasKO && !show);
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
	
	private void logTCF(String where) {
		log("\n");
		log(where);
		for (int i = 0; i < teamsComingFrom.size(); i++) {
			if (teamsComingFrom.get(i) != null) {
				String tcf = "", sep = "";
				for (int j = 0; j < teamsComingFrom.get(i).length; j++) {
					tcf += sep + teamsComingFrom.get(i)[j];
					sep = ", ";
				}
				log(tcf);
			}
		}
		log("\n");
	}
	
	private void saveDataForKORound(int index) {
		jLblMoreClicked(showingMoreFrom);
		ArrayList<String> thisList = allKORoundsTeams.get(selectedKORounds.get(index));
		thisList.clear();
		int[] tcf = teamsComingFrom.get(index);
		
		tcf[0] = currentKORoundTeamsPQ.size();
		for (int i = 0; i < tcf[0]; i++) {
			thisList.add(currentKORoundTeamsPQ.remove(0));
		}
		
		tcf[1] = currentKORoundTeamsPR.size();
		for (int i = 0; i < tcf[1]; i++) {
			thisList.add(currentKORoundTeamsPR.remove(0));
		}
		
		tcf[2] = currentKORoundTeamsOC.size();
		for (int i = 0; i < tcf[2]; i++) {
			thisList.add(currentKORoundTeamsOC.remove(0));
		}
		hasKORoundSecondLeg.set(selectedKORounds.get(index), koSecondLegYesRB.isSelected());
	}
	
	private void showDataForKORound(int index) {
		ArrayList<String> thisList = allKORoundsTeams.get(selectedKORounds.get(index));
		int[] tcf = teamsComingFrom.get(index);
		for (int i = 0; i < tcf[0]; i++) {
			currentKORoundTeamsPQ.add(thisList.remove(0));
		}
		
		for (int i = 0; i < tcf[1]; i++) {
			currentKORoundTeamsPR.add(thisList.remove(0));
		}
		
		for (int i = 0; i < tcf[2]; i++) {
			currentKORoundTeamsOC.add(thisList.remove(0));
		}
		
		jLblTeamsPQVal.setText("" + currentKORoundTeamsPQ.size());
		jLblTeamsPRVal.setText("" + currentKORoundTeamsPR.size());
		jLblTeamsOCVal.setText("" + currentKORoundTeamsOC.size());
		currentKORound = index;
		if (hasKORoundSecondLeg.get(selectedKORounds.get(index)))	koSecondLegYesRB.setSelected(true);
		else														koSecondLegNoRB.setSelected(true);
		possiblePreviousKORounds.clear();
		String[] model = new String[index + 1];
		model[0] = "Bitte waehlen";
		for (int i = 1; i < model.length; i++) {
			model[i] = possibleKORounds[selectedKORounds.get(i - 1)];
			possiblePreviousKORounds.add(selectedKORounds.get(i - 1));
		}
		jCBPreviousRound.setModel(new DefaultComboBoxModel<>(model));
		int prIndex = previousKORound[selectedKORounds.get(index)], cbIndex = 0;
		for (int i = 0; i < possiblePreviousKORounds.size(); i++) {
			if (possiblePreviousKORounds.get(i) == prIndex)	cbIndex = i + 1;
		}
		if (cbIndex == 1 && index != 1)	cbIndex = 0;
		if (index != 0)	jCBPreviousRound.setSelectedIndex(cbIndex);
		jLblMoreClicked(showingMoreFrom);
	}
	
	private void detailsKOCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			saveDataForKORound(currentKORound);
			int index = detailsKOCB.getSelectedIndex();
			showDataForKORound(index);
		}
	}
	
	private void jCBsPlatzierungenItemStateChanged(int index, ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int selIndex = jCBsPlatzierungen[index].getSelectedIndex();
			if (selIndex == 0) {
				jCBsBestGroupXth[index].setVisible(false);
				if (index != 2) {
					jCBsPlatzierungen[index + 1].setVisible(true);
					jLblsPlatzierungen[index + 1].setVisible(true);
					jCBsPlatzierungen[index + 1].setSelectedIndex(1);
				}
			} else if (selIndex == 1) {
				jCBsBestGroupXth[index].setVisible(false);
				for (int i = index + 1; i < 3; i++) {
					jCBsPlatzierungen[i].setVisible(false);
					jCBsBestGroupXth[i].setVisible(false);
					jLblsPlatzierungen[i].setVisible(false);
				}
			} else if (selIndex == 2) {
				jCBsBestGroupXth[index].setVisible(true);
				for (int i = index + 1; i < 3; i++) {
					jCBsPlatzierungen[i].setVisible(false);
					jCBsBestGroupXth[i].setVisible(false);
					jLblsPlatzierungen[i].setVisible(false);
				}
			}
		}
	}
	
	private void savePrequalified() {
		currentKORoundTeamsPQ.clear();
		for (int i = 0; i < jLTeamsModel.getSize(); i++) {
			currentKORoundTeamsPQ.add(jLTeamsModel.getElementAt(i));
		}
		jLblTeamsPQVal.setText("" + currentKORoundTeamsPQ.size());
	}
	
	private void savePreviousRound() {
		if (currentKORound == 0 && hasGrp) {
			currentKORoundTeamsPR.clear();
			if (jCBsPlatzierungen[0].getSelectedIndex() == 0) {
				for (int i = 0; i < numberOfGroups; i++) {
					currentKORoundTeamsPR.add("G" + alphabet[i] + "1");
				}
				if (jCBsPlatzierungen[1].getSelectedIndex() == 0) {
					for (int i = 0; i < numberOfGroups; i++) {
						currentKORoundTeamsPR.add(2 * i + 1, "G" + alphabet[i] + "2");
					}
					if (jCBsPlatzierungen[2].getSelectedIndex() == 0) {
						for (int i = 0; i < numberOfGroups; i++) {
							currentKORoundTeamsPR.add(3 * i + 2, "G" + alphabet[i] + "3");
						}
					} else if (jCBsPlatzierungen[2].getSelectedIndex() == 2) {
						int numberOfBest = jCBsBestGroupXth[2].getSelectedIndex() + 1;
						for (int i = 0; i < numberOfBest; i++) {
							currentKORoundTeamsPR.add("G3" + (i + 1));
						}
					}
				} else if (jCBsPlatzierungen[1].getSelectedIndex() == 2) {
					int numberOfBest = jCBsBestGroupXth[1].getSelectedIndex() + 1;
					for (int i = 0; i < numberOfBest; i++) {
						currentKORoundTeamsPR.add("G2" + (i + 1));
					}
				}
			} else if (jCBsPlatzierungen[0].getSelectedIndex() == 2) {
				int numberOfBest = jCBsBestGroupXth[0].getSelectedIndex() + 1;
				for (int i = 0; i < numberOfBest; i++) {
					currentKORoundTeamsPR.add("G1" + (i + 1));
				}
			}
		} else if (currentKORound != 0) {
			int prIndex = jCBPreviousRound.getSelectedIndex() - 1;
			if (prIndex != -1) {
				previousKORound[selectedKORounds.get(currentKORound)] = possiblePreviousKORounds.get(prIndex);
				log("Vor dem/der " + possibleKORounds[selectedKORounds.get(currentKORound)] + " war das/die " + possibleKORounds[previousKORound[selectedKORounds.get(currentKORound)]]);
			} else {
				previousKORound[selectedKORounds.get(currentKORound)] = 0;
				log("Vor dem/der " + possibleKORounds[selectedKORounds.get(currentKORound)] + " war nichts.");
			}
		}
		log();
		for (int i = 0; i < currentKORoundTeamsPR.size(); i++) {
			log(currentKORoundTeamsPR.get(i));
		}
		log();
		jLblTeamsPRVal.setText("" + currentKORoundTeamsPR.size());
	}
	
	private void saveOtherCompetition() {
		log(" TODO saving other competition");
//		currentKORoundTeamsOC.clear();
//		for (int i = 0; i < jLTeamsModel.getSize(); i++) {
//			currentKORoundTeamsOC.add(jLTeamsModel.getElementAt(i));
//		}
//		jLblTeamsOCVal.setText("" + currentKORoundTeamsOC.size());
	}
	
	private void jLblMoreClicked(int index) {
		if (showingMoreFrom == 1)		savePrequalified();
		else if (showingMoreFrom == 2)	savePreviousRound();
		else if (showingMoreFrom == 3)	saveOtherCompetition();
		
		switch (showingMoreFrom) {
			case 1:
				REC_NOTPRVALLBL.y -= heightOfDisplacement;
				REC_NOTPRLBL.y -= heightOfDisplacement;
				REC_NOTPRMORELBL.y -= heightOfDisplacement;
				jLblTeamsPRVal.setBounds(REC_NOTPRVALLBL);
				jLblTeamsPR.setBounds(REC_NOTPRLBL);
				jLblTeamsPRMore.setBounds(REC_NOTPRMORELBL);
				jLblTeamsPQMore.setText("mehr");
			case 2:
				REC_NOTOCVALLBL.y -= heightOfDisplacement;
				REC_NOTOCLBL.y -= heightOfDisplacement;
				REC_NOTOCMORELBL.y -= heightOfDisplacement;
				jLblTeamsOCVal.setBounds(REC_NOTOCVALLBL);
				jLblTeamsOC.setBounds(REC_NOTOCLBL);
				jLblTeamsOCMore.setBounds(REC_NOTOCMORELBL);
				jLblTeamsPRMore.setText("mehr");
			case 3:
				jLblTeamsOCMore.setText("mehr");
		}
		
		jSPTeams.setVisible(false);
		jBtnAddTeam.setVisible(false);
		jBtnEditTeam.setVisible(false);
		jBtnDeleteTeam.setVisible(false);
		jLblPreviousRound.setVisible(false);
		jCBPreviousRound.setVisible(false);
		jLblNoPreviousRound.setVisible(false);
		
		for (int i = 0; i < 3; i++) {
			jCBsPlatzierungen[i].setVisible(false);
			jCBsBestGroupXth[i].setVisible(false);
			jLblsPlatzierungen[i].setVisible(false);
		}
		
		if (showingMoreFrom == index) {
			showingMoreFrom = 0;
			return;
		}
		
		showingMoreFrom = index;
		
		jLTeamsModel.clear();
		if (showingMoreFrom == 1) {
			heightOfDisplacement = 100;
			jSPTeams.setVisible(true);
			jBtnAddTeam.setVisible(true);
			jBtnEditTeam.setVisible(true);
			jBtnDeleteTeam.setVisible(true);
			jLblTeamsPQMore.setText("weniger");
			for (int i = 0; i < currentKORoundTeamsPQ.size(); i++) {
				jLTeamsModel.addElement(currentKORoundTeamsPQ.get(i));
			}
		} else if (showingMoreFrom == 2) {
			jLblTeamsPRMore.setText("weniger");
			if (currentKORound != 0) {
				heightOfDisplacement = 70;
				jLblPreviousRound.setBounds(60, REC_NOTPRLBL.y + REC_NOTPRLBL.height + 5, 130, 25);
				jLblPreviousRound.setVisible(true);
				jCBPreviousRound.setBounds(60, REC_NOTPRLBL.y + REC_NOTPRLBL.height + 35, 160, 25);
				jCBPreviousRound.setVisible(true);
			} else if (hasGrp) {
				heightOfDisplacement = 100;
				for (int i = 0; i < 3; i++) {
					jCBsPlatzierungen[i].setVisible(true);
					jLblsPlatzierungen[i].setVisible(true);
					if (jCBsPlatzierungen[i].getSelectedIndex() == 2)	jCBsBestGroupXth[i].setVisible(true);
					if (jCBsPlatzierungen[i].getSelectedIndex() != 0)	break;
				}
			} else {
				heightOfDisplacement = 40;
				jLblNoPreviousRound.setVisible(true);
			}
		} else if (showingMoreFrom == 3) {
			heightOfDisplacement = 100;
			jLblTeamsOCMore.setText("weniger");
			for (int i = 0; i < currentKORoundTeamsOC.size(); i++) {
				jLTeamsModel.addElement(currentKORoundTeamsOC.get(i));
			}
		}
		
		switch (showingMoreFrom) {
			case 1:
				REC_NOTPRVALLBL.y += heightOfDisplacement;
				REC_NOTPRLBL.y += heightOfDisplacement;
				REC_NOTPRMORELBL.y += heightOfDisplacement;
				jLblTeamsPRVal.setBounds(REC_NOTPRVALLBL);
				jLblTeamsPR.setBounds(REC_NOTPRLBL);
				jLblTeamsPRMore.setBounds(REC_NOTPRMORELBL);
			case 2:
				REC_NOTOCVALLBL.y += heightOfDisplacement;
				REC_NOTOCLBL.y += heightOfDisplacement;
				REC_NOTOCMORELBL.y += heightOfDisplacement;
				jLblTeamsOCVal.setBounds(REC_NOTOCVALLBL);
				jLblTeamsOC.setBounds(REC_NOTOCLBL);
				jLblTeamsOCMore.setBounds(REC_NOTOCMORELBL);
		}
	}
	
	private void jBtnAddTeamActionPerformed() {
		String newName = null;
		boolean cancel = false;
		while((newName == null || newName.isEmpty()) && !cancel) {
			newName = inputDialog("Name der neuen Mannschaft:");
			if (newName == null || newName.isEmpty()) {
				cancel = yesNoDialog("Sie haben keinen validen Namen eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
			}
		}
		if (!cancel)	jLTeamsModel.addElement(newName);
	}
	
	private void jBtnEditTeamActionPerformed() {
		int index = jLTeams.getSelectedIndex();
		if (index != -1) {
			String newName = null;
			boolean cancel = false;
			while((newName == null || newName.isEmpty()) && !cancel) {
				newName = inputDialog("Neuer Name fuer " + jLTeamsModel.get(index));
				if (newName == null || newName.isEmpty()) {
					cancel = yesNoDialog("Sie haben keinen validen Namen eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
				}
			}
			if (!cancel)	jLTeamsModel.set(index, newName);
		} else {
			message("Sie haben keine Mannschaft ausgewaehlt.");
		}
	}
	
	private void jBtnDeleteTeamActionPerformed() {
		int index = jLTeams.getSelectedIndex();
		if (index != -1) {
			boolean delete = yesNoDialog("Wollen Sie das Team \"" + jLTeamsModel.get(index) + "\" wirklich aus dieser KO-Runde entfernen?") == JOptionPane.YES_OPTION;
			if (delete)	jLTeamsModel.remove(index);
		} else {
			message("Sie haben keine Mannschaft ausgewaehlt.");
		}
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
		teams = new String[][] {{"GA1#PR", "GA2#PR", "GB1#PR", "GB2#PR", "GC1#PR", "GC2#PR", "GB3#PR", "GC3#PR"}, 
									{"VF1#PR", "VF2#PR", "VF3#PR", "VF4#PR"}, {"HF1#PR", "HF2#PR"}, {"HF1#PR", "HF2#PR"}};
		
		return teams;
	}
	
	private boolean validateGeneralInfo() {
		String message = "";
		
		name = nameTF.getText();
		if (name.isEmpty())	message += "Geben Sie bitte einen gueltigen Namen ein.\n";
		
		shortName = shortNameTF.getText();
		if (shortName.isEmpty())	message += "Geben Sie bitte einen gueltigen Kurznamen ein.\n";
		
		stDate = MyDate.getDate(startDateTF.getText());
		if (stDate == 19700101)	message += "Geben Sie bitte ein gueltiges Startdatum ein.\n";
		
		fiDate = MyDate.getDate(finalDateTF.getText());
		if (fiDate == 19700101)	message += "Geben Sie bitte ein gueltiges Enddatum ein.\n";
		else if (stDate > fiDate)	message += "Das Finale kann nicht vor Beginn des Turniers stattfinden.\n";
		
		season = stDate / 10000;
		isSTSS = season != (fiDate / 10000);
		
		if (message.length() > 0) {
			message("Folgende Fehler sind aufgetreten:\n" + message);
			return false;
		}
		
		return true;
	}
	
	private boolean validateQualification() {
		qConfig = new ArrayList<>();
		
		hasQ = Boolean.parseBoolean(qualificationRBGrp.getSelection().getActionCommand());
		
		if (hasQ) {
			// TODO check and save data
			
			
		}
		
		return true;
	}
	
	private boolean validateGroupStage() {
		grpConfig = new ArrayList<>();
		
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		
		if (hasGrp) {
			if (jBtnSaveGroups.isVisible()) {
				message("Zuerst muss die Konfiguration der Gruppen gespeichert werden.");
				return false;
			}
			
			saveDataForGroup(currentGroup);
			
			// Test-Ausgabe aller Teams in den Gruppen
			log("Alle Teams in der Gruppenphase:");
			boolean ignored = false;
			teamsGrp = new String[numberOfGroups][];
			for (int i = 0; i < numberOfGroups; i++) {
				log("Gruppe " + alphabet[i]);
				teamsGrp[i] = new String[teamsNamesGroupStage.get(i).size()];
				for (int j = 0; j < teamsGrp[i].length; j++) {
					teamsGrp[i][j] = teamsNamesGroupStage.get(i).get(j);
					if (!ignored && teamsGrp[i][j].equals("Mannschaft " + (j + 1))) {
						if (yesNoDialog("Es gibt noch Mannschaften, deren Namen nicht angegeben ist. Diese werden beim Speichern durch einen Platzhalter ersetzt.\n"
								+ "Die Namen können auch später noch angegeben werden. Fortfahren?") == JOptionPane.NO_OPTION) {
							return false;
						}
						ignored = true;
					}
					log(teamsGrp[i][j]);
				}
				log("\n");
			}
			
			grpConfig.add("" + numberOfGroups);
			grpConfig.add("" + groupSecondLegRBGrp.getSelection().getActionCommand());
			
			log("Gruppen-Konfiguration:");
			for (int i = 0; i < grpConfig.size(); i++) {
				log(grpConfig.get(i));
			}
			log("\n\n");
		}
		
		return true;
	}
	
	private boolean validateKOStage() {
		koConfig = new ArrayList<>();
		
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		
		if (hasKO) {
			if (jBtnSaveKORounds.isVisible()) {
				message("Zuerst muss die Konfiguration der KO-Runden gespeichert werden.");
				return false;
			}
			
			saveDataForKORound(currentKORound);
			
			boolean[] prevKORounds = new boolean[possibleKORounds.length];
			for (int i = 0; i < selectedKORounds.size(); i++) {
				int koIndex = selectedKORounds.get(i);
				int[] tcf = teamsComingFrom.get(i);
				
				if (i != 0) {
					int prevKOIndex = previousKORound[koIndex];
					if (prevKOIndex == 0) {
						message("Es wurde noch nicht angegeben, aus welcher vorangegangenen KO-Runde die Teams in " + possibleKORounds[koIndex] + " stammen.");
						return false;
					}
					if (prevKORounds[prevKOIndex] && !(isKORoundSelected[8] && prevKOIndex == 7)) {
						message("Es wurden in mehreren KO-Runden dieselbe vorangegangene KO-Runde angegeben. \n" + 
									"Das ist nicht erlaubt, da so nicht ermittelt werden kann, welche Mannschaften in welche KO-Runde einziehen.");
						return false;
					}
					prevKORounds[prevKOIndex] = true;
					int[] prevTCF = teamsComingFrom.get(i - 1);
					int nOfMatches = (prevTCF[0] + prevTCF[1] + prevTCF[2]) / 2;
					ArrayList<String> list = allKORoundsTeams.get(koIndex);
					for (int j = 0; j < tcf[1]; j++) {
						list.remove(tcf[0]);
					}
					for (int j = 0; j < nOfMatches; j++) {
						list.add(tcf[0] + j, possibleKORoundsShort[prevKOIndex] + (j + 1));
					}
					tcf[1] = nOfMatches;
				}
				
				int numberOfTeams = tcf[0] + tcf[1] + tcf[2];
				if (numberOfTeams < 2) {
					message("Es wurden nicht ausreichend Mannschaften in der KO-Runde " + possibleKORounds[koIndex] + " bereitgestellt.(" + numberOfTeams + ")");
					return false;
				}
				if (numberOfTeams % 2 != 0) {
					message("Es wurde eine ungerade Anzahl an Mannschaften in der KO-Runde " + possibleKORounds[koIndex] + " bereitgestellt.");
					return false;
				}
			}
			
			// Test-Ausgabe aller Teams in den KO-Runden
			log("Alle Teams in der KO-Phase:");
			int index = 0;
			for (ArrayList<String> list : allKORoundsTeams) {
				if (isKORoundSelected[index++]) {
					log(possibleKORounds[index - 1] + ":");
					for (String string : list) {
						log(string);
					}
					log("\n");
				}
			}
			
			// TODO check data
			
			
			
			for (int i = 0; i < selectedKORounds.size(); i++) {
				int koIndex = selectedKORounds.get(i);
				String koRep = possibleKORounds[koIndex] + ";";
				koRep += possibleKORoundsShort[koIndex] + ";";
				koRep += (koIndex != 8) + ";";
				koRep += hasKORoundSecondLeg.get(koIndex) + ";";
				koRep += teamsComingFrom.get(i)[0] + ";";
				koRep += teamsComingFrom.get(i)[1] + ";";
				koRep += teamsComingFrom.get(i)[2] + ";";
				koConfig.add(koRep);
			}
			
			has3pl = isKORoundSelected[8];
			log("KO-Runden-Konfiguration:");
			for (int i = 0; i < koConfig.size(); i++) {
				log(koConfig.get(i));
			}
		}
		
		return true;
	}
	
	public void dispose() {
		int cancel = yesNoDialog("Sicher, dass Sie das Fenster schliessen wollen? Dabei gehen die eingegebenen Daten verloren.");
		if (cancel == JOptionPane.YES_OPTION)	super.dispose();
	}
	
	private void cancelActionPerformed() {
		this.setVisible(false);
	}
	
	private void goActionPerformed() {
		if (step == 0) {
			if (!validateGeneralInfo())	return;
			jLblWettbewerb.setText(name + " " + season + (isSTSS ? "/" + (season + 1) : ""));
			jLblWettbewerb.setVisible(true);
			
			infoPnl.setVisible(false);
			qGroupStagePnl.add(jSPTeams);
			qGroupStagePnl.add(jBtnEditTeam);
			qGroupStagePnl.add(jLblDetailsFor);
			for (int i = 0; i < minNumberOfQGroups; i++) {
				jBtnAddQGroupActionPerformed();
			}
			qualificationPnl.setVisible(true);
			step++;
		} else if (step == 1) {
			if (!validateQualification())	return;
			
			
			qualificationPnl.setVisible(false);
			groupStagePnl.add(jSPTeams);
			groupStagePnl.add(jBtnEditTeam);
			groupStagePnl.add(jLblDetailsFor);
			jSPTeams.setVisible(false);
			jBtnEditTeam.setVisible(false);
			jLblDetailsFor.setVisible(false);
			for (int i = 0; i < minNumberOfGroups; i++) {
				jBtnAddGroupActionPerformed();
			}
			groupStagePnl.setVisible(true);
			step++;
		} else if (step == 2) {
			if (!validateGroupStage())	return;
			
			
			koStagePnl.add(jSPTeams);
			koStagePnl.add(jBtnEditTeam);
			koStagePnl.add(jLblDetailsFor);
			jSPTeams.setVisible(false);
			jBtnEditTeam.setVisible(false);
			jLblDetailsFor.setVisible(false);
			int offsety = REC_NOTPQVALLBL.y + REC_NOTPQVALLBL.height;
			jSPTeams.setBounds(20, offsety + 5, 230, 90);
			jBtnAddTeam.setBounds(260, offsety + 10, 110, 25);
			jBtnEditTeam.setBounds(260, offsety + 40, 110, 25);
			jBtnDeleteTeam.setBounds(260, offsety + 70, 110, 25);
			if (hasGrp) {
				String[] bestGroupXth = new String[numberOfGroups - 1];
				for (int i = 0; i < bestGroupXth.length; i++) {
					bestGroupXth[i] = "" + (i + 1);
				}
				for (int i = 0; i < jCBsBestGroupXth.length; i++) {
					jCBsBestGroupXth[i].setModel(new DefaultComboBoxModel<>(bestGroupXth));
				}
			}
			
			
			groupStagePnl.setVisible(false);
			koStagePnl.setVisible(true);
			step++;
		} else if (step == 3) {
			if (!validateKOStage())	return;
			
			
			
			boolean outOfUse = true;
			if (outOfUse) {
				message("Nicht aktuell. Erst refactorn.");
				cancelActionPerformed();
				return;
			}
			
			teamsGrp = getTeamsGrp();
			teamsKO = getTeamsKO();

			Start.getInstance().addNewTournament(name, shortName, season, isSTSS, stDate, fiDate, hasQ, hasGrp, hasKO, has3pl, qConfig, teamsQG, teamsQKO, grpConfig, teamsGrp, koConfig, teamsKO);
			this.setVisible(false);
		}
		updateGUI();
	}
}
