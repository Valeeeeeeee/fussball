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

	private static final int GAPPNL = 10;
	private final int OFFSETX = 30;
	private final int OFFSETY = 30;
	
	// Bounds
	private Dimension dim = new Dimension(440 + 6, 630 + 28);
	private Rectangle REC_GO = new Rectangle(310, 570, 70, 30);
	private Rectangle REC_CANCEL = new Rectangle(200, 570, 100, 30);

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
	
	// Group stage
	private Rectangle REC_GRPPNL;
	private Rectangle REC_GRPLBL = new Rectangle(5, 0, 90, 30);
	private Rectangle REC_GRPYES = new Rectangle(100, 0, 45, 30); // checked
	private Rectangle REC_GRPNO = new Rectangle(140, 0, 60, 30); // checked
	private Rectangle REC_NOFGRPCB = new Rectangle(5, 30, 70, 30);
	private Rectangle REC_NOFGRPLBL = new Rectangle(80, 30, 70, 30);
	private Rectangle REC_GRP2LEGLBL = new Rectangle(205, 30, 70, 30); // checked
	private Rectangle REC_GRP2LEGYES = new Rectangle(280, 30, 45, 30); // checked
	private Rectangle REC_GRP2LEGNO = new Rectangle(320, 30, 60, 30); // checked
	private Rectangle REC_TNGRPCB = new Rectangle(5, 60, 120, 30); // checked

	// Knockout stage
	private Rectangle REC_KOPNL;
	private Rectangle REC_KOLBL = new Rectangle(5, 0, 70, 30); // checked
	private Rectangle REC_KOYES = new Rectangle(80, 0, 45, 30); // checked
	private Rectangle REC_KONO = new Rectangle(120, 0, 60, 30); // checked
	private Rectangle REC_NOFKOCB = new Rectangle(225, 0, 70, 30);
	private Rectangle REC_NOFKOLBL = new Rectangle(300, 0, 75, 30); // checked
	private Rectangle REC_KO2LEGLBL = new Rectangle(205, 30, 70, 30); // checked
	private Rectangle REC_KO2LEGYES = new Rectangle(280, 30, 45, 30); // checked
	private Rectangle REC_KO2LEGNO = new Rectangle(320, 30, 60, 30); // checked
	private Rectangle REC_DETKOCB = new Rectangle(5, 30, 170, 30);
	private Rectangle REC_NOTPQTF = new Rectangle(5, 65, 40, 30);
	private Rectangle REC_NOTPQLBL = new Rectangle(50, 65, 180, 30);
	private Rectangle REC_NOTPRTF = new Rectangle(5, 100, 40, 30);
	private Rectangle REC_NOTPRLBL = new Rectangle(50, 100, 190, 30);
	private Rectangle REC_NOTOCTF = new Rectangle(5, 135, 40, 30);
	private Rectangle REC_NOTOCLBL = new Rectangle(50, 135, 220, 30);

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
	private JComboBox detailsKOCB;
	private JTextField jTFTeamsPQ;
	private JLabel jLblTeamsPQ;
	private JTextField jTFTeamsPR;
	private JLabel jLblTeamsPR;
	private JTextField jTFTeamsOC;
	private JLabel jLblTeamsOC;
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
	private boolean hasQ;
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
		updateGUI();
	}

	private void initGUI() {
		this.setLayout(null);

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
		buildQualification();
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
			hasThirdPlaceYesRB.setSelected(true);
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
			qualificationNoRB.setSelected(true);
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
			groupStageYesRB.setSelected(true);
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
			numOfGroupsCB = new JComboBox();
			groupStagePnl.add(numOfGroupsCB);
			numOfGroupsCB.setBounds(REC_NOFGRPCB);
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
			groupSecondLegNoRB.setSelected(true);
			groupSecondLegNoRB.setOpaque(false);
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
			teamsNamesGrpCB.setBounds(REC_TNGRPCB);
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
			koStageYesRB.setSelected(true);
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
			numOfKORoundsCB = new JComboBox();
			koStagePnl.add(numOfKORoundsCB);
			numOfKORoundsCB.setBounds(REC_NOFKOCB);
			numOfKORoundsCB.setModel(new DefaultComboBoxModel(posNumOfKORounds));
			numOfKORoundsCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (deleteProvidedData())	numOfKORoundsCBItemStateChanged(evt);
				}
			});
		}
		{
			numOfKORoundsLbl = new JLabel();
			koStagePnl.add(numOfKORoundsLbl);
			numOfKORoundsLbl.setBounds(REC_NOFKOLBL);
			numOfKORoundsLbl.setText("KO-Runden");
		}
		// Rueckspiel
		{
			koSecondLegLbl = new JLabel();
			koStagePnl.add(koSecondLegLbl);
			koSecondLegLbl.setBounds(REC_KO2LEGLBL);
			koSecondLegLbl.setText("Rueckspiel");
		}
		{
			koSecondLegYesRB = new JRadioButton("ja");
			koStagePnl.add(koSecondLegYesRB);
			koSecondLegYesRB.setBounds(REC_KO2LEGYES);
			koSecondLegYesRB.setActionCommand("true");
			koSecondLegYesRB.setOpaque(false);
		}
		{
			koSecondLegNoRB = new JRadioButton("nein");
			koStagePnl.add(koSecondLegNoRB);
			koSecondLegNoRB.setBounds(REC_KO2LEGNO);
			koSecondLegNoRB.setActionCommand("false");
			koSecondLegNoRB.setOpaque(false);
			koSecondLegNoRB.setSelected(true);
		}
		{
			koSecondLegRBGrp = new ButtonGroup();
			koSecondLegRBGrp.add(koSecondLegYesRB);
			koSecondLegRBGrp.add(koSecondLegNoRB);
		}
		// Anzahl KO-Runden
		{
			detailsKOCB = new JComboBox();
			koStagePnl.add(detailsKOCB);
			detailsKOCB.setBounds(REC_DETKOCB);
			detailsKOCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					detailsKOCBItemStateChanged(evt);
				}
			});
		}
		{
			jTFTeamsPQ = new JTextField();
			koStagePnl.add(jTFTeamsPQ);
			jTFTeamsPQ.setBounds(REC_NOTPQTF);
		}
		{
			jLblTeamsPQ = new JLabel();
			koStagePnl.add(jLblTeamsPQ);
			jLblTeamsPQ.setBounds(REC_NOTPQLBL);
			jLblTeamsPQ.setText("Teams qualifiziert");
		}
		{
			jTFTeamsPR = new JTextField();
			koStagePnl.add(jTFTeamsPR);
			jTFTeamsPR.setBounds(REC_NOTPRTF);
		}
		{
			jLblTeamsPR = new JLabel();
			koStagePnl.add(jLblTeamsPR);
			jLblTeamsPR.setBounds(REC_NOTPRLBL);
			jLblTeamsPR.setText("Teams aus vorherigen Runden");
		}
		{
			jTFTeamsOC = new JTextField();
			koStagePnl.add(jTFTeamsOC);
			jTFTeamsOC.setBounds(REC_NOTOCTF);
		}
		{
			jLblTeamsOC = new JLabel();
			koStagePnl.add(jLblTeamsOC);
			jLblTeamsOC.setBounds(REC_NOTOCLBL);
			jLblTeamsOC.setText("Teams aus anderen Wettbewerben");
		}
		
//		{
//			kotSP = new JScrollPane();
//			kotPnl.add(kotSP);
//			kotSP.setBounds(REC_KOTSP);
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
//			defKOTLbl.setBounds(REC_DEFKOTLBL);
//			defKOTLbl.setText("Standard:");
//		}
//		{
//			defaultKOTTF = new JTextField();
//			kotPnl.add(defaultKOTTF);
//			defaultKOTTF.setBounds(REC_DEFKOTTF);
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
		
//		nameTF.setText("Champions League");
//		shortNameTF.setText("CL");
//		
//		startDateTF.setText("16.09.2014");
//		finalDateTF.setText("06.06.2015");
//		hasThirdPlaceNoRB.setSelected(true);
//		numberOfTeamsTF.setText("32");
//		
//		groupStageYesRB.setSelected(true);
//		groupStageRBchanged(true);
//		numOfGroupsCB.setSelectedIndex(8 - minNumberOfGroups);
//		groupSecondLegYesRB.setSelected(true);
//		
//		koStageYesRB.setSelected(true);
//		koStageRBchanged(true);
//		numOfKORoundsCB.setSelectedIndex(4 - minNumberOfKORounds);
//		koSecondLegYesRB.setSelected(true);
		
//		nameTF.setText("Copa2");
//		shortNameTF.setText("C2");
//		
//		startDateTF.setText("12.06.2015");
//		finalDateTF.setText("04.07.2015");
//		hasThirdPlaceYesRB.setSelected(true);
//		numberOfTeamsTF.setText("12");
//		
//		groupStageYesRB.setSelected(true);
//		groupStageRBchanged(true);
//		numOfGroupsCB.setSelectedIndex(3 - minNumberOfGroups);
//		groupSecondLegNoRB.setSelected(true);
//		
//		koStageYesRB.setSelected(true);
//		koStageRBchanged(true);
//		numOfKORoundsCB.setSelectedIndex(4 - minNumberOfKORounds);
//		koSecondLegNoRB.setSelected(true);
	}
	
	private void updateGUI() {
		hasQ = Boolean.parseBoolean(qualificationRBGrp.getSelection().getActionCommand());
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		
		int heightQ = hasQ ? 150 : 50;
		REC_QPNL = new Rectangle(OFFSETX, OFFSETY + REC_INFOPNL.height + GAPPNL, 380, heightQ);
		qualificationPnl.setBounds(REC_QPNL);
		
		int heightG = hasGrp ? 150 : 150;
		REC_GRPPNL = new Rectangle(OFFSETX, OFFSETY + REC_INFOPNL.height + REC_QPNL.height + 2 * GAPPNL, 380, heightG);
		groupStagePnl.setBounds(REC_GRPPNL);
		
		int heightK = hasKO ? 180 : 180;
		REC_KOPNL = new Rectangle(OFFSETX, OFFSETY + REC_INFOPNL.height + REC_QPNL.height + REC_GRPPNL.height + 3 * GAPPNL, 380, heightK);
		koStagePnl.setBounds(REC_KOPNL);
		
	}
	
	private void qualificationRBchanged(boolean hasQ) {
		// Alles was mit Gruppeneinstellung zu tun hat disablen
		log("Qualifiaction has been " + (hasQ ? "enabled." : "disabled."));
		
//		numOfGroupsCB.setEnabled(hasQ);
//		numOfGroupsLbl.setEnabled(hasQ);
//		groupSecondLegLbl.setEnabled(hasQ);
//		groupSecondLegYesRB.setEnabled(hasQ);
//		groupSecondLegNoRB.setEnabled(hasQ);
		updateGUI();
	}
	
	private void groupStageRBchanged(boolean hasGrp) {
		// Alles was mit Gruppeneinstellung zu tun hat disablen
		log("Group stage has been " + (hasGrp ? "enabled." : "disabled."));
		
		numOfGroupsCB.setEnabled(hasGrp);
		numOfGroupsLbl.setEnabled(hasGrp);
		groupSecondLegLbl.setEnabled(hasGrp);
		groupSecondLegYesRB.setEnabled(hasGrp);
		groupSecondLegNoRB.setEnabled(hasGrp);
		updateGUI();
	}
	
	private void koStageRBchanged(boolean hasGrp) {
		// Alles was mit KO-Rundeneinstellung zu tun hat disablen
		log("Knockout stage has been " + (hasGrp ? "enabled." : "disabled."));
		
		numOfKORoundsCB.setEnabled(hasGrp);
		numOfKORoundsLbl.setEnabled(hasGrp);
		koSecondLegLbl.setEnabled(hasGrp);
		koSecondLegYesRB.setEnabled(hasGrp);
		koSecondLegNoRB.setEnabled(hasGrp);
		updateGUI();
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
	
	private void setDetailsKOCBModel() {
		String[] koRoundsNames = new String[nOKO];
		String[] allKOFull = start.getKoRFull();
		has3pl = Boolean.parseBoolean(hasThirdPlaceRBGrp.getSelection().getActionCommand());
		int skip3pl = has3pl ? 0 : 1, diff = allKOFull.length - nOKO;
		for (int i = 0; i < nOKO - 1; i++) {
			koRoundsNames[i] = allKOFull[i + diff - skip3pl];
		}
		koRoundsNames[nOKO - 1] = allKOFull[allKOFull.length - 1];
		detailsKOCB.setModel(new DefaultComboBoxModel(koRoundsNames));
	}
	
	private void detailsKOCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			/*int oldNumOfKORounds = nOKO;
			int newNumOfKORounds = numOfKORoundsCB.getSelectedIndex() + minNumberOfKORounds;
			
			if (oldNumOfKORounds > newNumOfKORounds) {
				
			} else if (oldNumOfKORounds < newNumOfKORounds) {
				
			}
			
			nOKO = newNumOfKORounds;*/
			log("details combobox index changed to " + detailsKOCB.getSelectedIndex());
		}
	}
	
	private boolean deleteProvidedData() {
		boolean result = true;
		
		if (fiDate > 3) {
			result = false;
		}
		fiDate++;
		
		return result;
	}
	
	private void numOfKORoundsCBItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int oldNumOfKORounds = nOKO;
			int newNumOfKORounds = numOfKORoundsCB.getSelectedIndex() + minNumberOfKORounds;
			
			if (oldNumOfKORounds > newNumOfKORounds) {
				
			} else if (oldNumOfKORounds < newNumOfKORounds) {
				
			}
			
			nOKO = newNumOfKORounds;
			setDetailsKOCBModel();
		}
	}
	
	private String[][] getTeamsGrp() {
		String[][] teams = null;
		
		
		return teams;
	}
	
	private String[][] getTeamsKO() {
		String[][] teams = null;
		// teamsKO[index][team]
		teams = new String[][] {{"GA1#PQ", "GA2#PR", "GB1#PR", "GB2#PR", "GC1#PR", "GC2#PR", "GB3#PR", "GC3#PR"}, {"VF1#PR", "VF2#PR", "VF3#PR", "VF4#PR"}, {"HF1#PR", "HF2#PR"}, {"HF1#PQ", "HF2#PR"}};
		
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
		qualificationNoRB.setSelected(true);
		hasQ = Boolean.parseBoolean(qualificationRBGrp.getSelection().getActionCommand());
		hasGrp = Boolean.parseBoolean(groupStageRBGrp.getSelection().getActionCommand());
		hasKO = Boolean.parseBoolean(koStageRBGrp.getSelection().getActionCommand());
		if (hasGrp)	grp2leg = Boolean.parseBoolean(groupSecondLegRBGrp.getSelection().getActionCommand());
		if (hasKO)	ko2leg = Boolean.parseBoolean(koSecondLegRBGrp.getSelection().getActionCommand());
		has3pl = Boolean.parseBoolean(hasThirdPlaceRBGrp.getSelection().getActionCommand());
		nOTeam = Integer.parseInt(numberOfTeamsTF.getText());
		teamsGrp = getTeamsGrp();
		teamsKO = getTeamsKO();

		start.addNewTournament(name, shortName, season, stDate, fiDate, isSTSS, hasQ, hasGrp, hasKO, 
				grp2leg, ko2leg, has3pl, nOTeam, nOGrp, nOKO, teamsGrp, teamsKO);
		this.setVisible(false);
	}
}
