package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static util.Utilities.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NewLeagueDialog extends JFrame {
	private static final long serialVersionUID = -4797487798345998331L;
	
	private Start start;
	
	private String[] wochentage = new String[] {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
	private final int minNumOfTeams = 2;
	private final int maxNumOfTeams = 24;
	private Color bg = new Color(128, 255, 128);
	
	// Bounds
	private Dimension dim = new Dimension(390, 555);
	private Rectangle RECGO = new Rectangle(300, 495, 70,30);
	private Rectangle RECCANCEL = new Rectangle(200, 495, 90,30);
	
	
	// Allgemeine Informationen
	private Rectangle RECINFOPNL = new Rectangle(20, 20, 350, 90);
	
	private Rectangle RECNAMELBL = new Rectangle(5, 0, 100, 30);
	private Rectangle RECSEASLBL = new Rectangle(5, 30, 100, 30);
	private Rectangle RECDEFSTLBL = new Rectangle(190, 30, 60, 30);
	private Rectangle RECSTSSLBL = new Rectangle(5, 60, 100, 30);
	
	private Rectangle RECNAMETF = new Rectangle(110, 0, 240, 30);
	private Rectangle RECSEASTF = new Rectangle(110, 30, 60, 30);
	private Rectangle RECDEFSTCB = new Rectangle(250, 30, 100, 30);
	private Rectangle REC1YRB = new Rectangle(110, 60, 80, 30);
	private Rectangle REC2YRB = new Rectangle(200, 60, 95, 30);
	
	
	// Mannschaften
	private Rectangle RECTEAMPNL = new Rectangle(20, 120, 170, 370);
	private Rectangle RECNOTEAMCB = new Rectangle(0, 0, 70, 30);
	private Rectangle RECTEAMLBL = new Rectangle(70, 0, 90, 30);
	private Rectangle RECTEAMSP = new Rectangle(5, 30, 160, 310);
	private Rectangle RECEDITBTN = new Rectangle(5, 340, 160, 30);
	
	
	// Kick off times
	private Rectangle RECKOTPNL = new Rectangle(200, 280, 170, 210);
	private Rectangle RECKOTLBL = new Rectangle(5, 0, 90, 30);
	private Rectangle RECKOTSP = new Rectangle(5, 30, 160, 110);
	private Rectangle RECAKOTBTN = new Rectangle(5, 140, 40, 30);
	private Rectangle RECEKOTBTN = new Rectangle(50, 140, 90, 30);
	private Rectangle RECDEFKOTLBL = new Rectangle(5, 160, 90, 30);
	private Rectangle RECDEFKOTTF = new Rectangle(0, 180, 170, 30);
	
	
	// Anzahlen
	private Rectangle RECANZPNL = new Rectangle(200, 120, 170, 150);
	private int[] anzLbls = new int[] {5, 0, 0, 0, 100, 30};
	private int[] anzTFs = new int[] {140, 0, 0, 0, 30, 30};
	
	private final int STARTX = 0;
	private final int STARTY = 1;
	private final int GAPX = 2;
	private final int GAPY = 3;
	private final int WIDTH = 4;
	private final int HEIGHT = 5;
	
	// View
	private JButton go;
	private JButton cancel;
	
	
	// Allgemeine Informationen
	private JPanel infoPnl;
	private JLabel nameLbl;
	private JTextField nameTF;
	private JLabel seasonLbl;
	private JTextField seasonTF;
	private JLabel defaultSTLbl;
	private JComboBox defaultSTCB;
	private JLabel isSTSSLbl;
	private JRadioButton oneYearRB;
	private JRadioButton twoYearsRB;
	private ButtonGroup yearsRBGrp;
	
	
	// Mannschaften
	private JPanel teamsPnl;
	private JComboBox numOfTeamsCB;
	private JLabel teamsLbl;
	private JScrollPane teamsSP;
	private JList teamsL;
	private DefaultListModel teamsModel;
	private JButton editTeamBtn;
	
	
	// Anzahlen
	private JPanel anzahlenPnl;
	private JLabel[] anzahlenLbls;
	private JTextField[] anzahlenTFs;
	
	
	// Kick off times
	private JPanel kotPnl;
	private JLabel kotLbl;
	private JScrollPane kotSP;
	private JList kotL;
	private DefaultListModel kotModel = new DefaultListModel();
	private JButton addKOTBtn;
	private JButton editKOTBtn;
	private JLabel defKOTLbl;
	private JTextField defaultKOTTF;
	
	
	// Model
	private String name;
	private int season;
	private int numberOfTeams;
	private String[] teamsNames;
	private int[] anzahlen;
	private boolean isSTSS;
	private int defaultST;
	private String KOTs;
	private int[] defKOTs;
	
	
	public NewLeagueDialog(Start start) {
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
		
		
		buildGeneralInfo();
		buildTeams();
		buildAmounts();
		buildKOT();
		
//		enterPresetValues();
		
		setTitle("Neue Liga erstellen");
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
			infoPnl.setBackground(bg);
		}
		{
			nameLbl = new JLabel();
			infoPnl.add(nameLbl);
			nameLbl.setBounds(RECNAMELBL);
			nameLbl.setText("Name der Liga");
		}
		{
			nameTF = new JTextField();
			infoPnl.add(nameTF);
			nameTF.setBounds(RECNAMETF);
		}
		{
			seasonLbl = new JLabel();
			infoPnl.add(seasonLbl);
			seasonLbl.setBounds(RECSEASLBL);
			seasonLbl.setText("Saison beginnt");
		}
		{
			seasonTF = new JTextField();
			infoPnl.add(seasonTF);
			seasonTF.setBounds(RECSEASTF);
		}
		{
			defaultSTLbl = new JLabel();
			infoPnl.add(defaultSTLbl);
			defaultSTLbl.setBounds(RECDEFSTLBL);
			defaultSTLbl.setText("Starttag");
		}
		{
			defaultSTCB = new JComboBox();
			infoPnl.add(defaultSTCB);
	        defaultSTCB.setBounds(RECDEFSTCB);
	        defaultSTCB.setModel(new DefaultComboBoxModel(wochentage));
		}
		{
			isSTSSLbl = new JLabel();
			infoPnl.add(isSTSSLbl);
			isSTSSLbl.setBounds(RECSTSSLBL);
			isSTSSLbl.setText("Saison dauert");
		}
		{
			oneYearRB = new JRadioButton("ein Jahr");
			infoPnl.add(oneYearRB);
			oneYearRB.setBounds(REC1YRB);
			oneYearRB.setActionCommand("false");
		}
		{
			twoYearsRB = new JRadioButton("zwei Jahre");
			infoPnl.add(twoYearsRB);
			twoYearsRB.setBounds(REC2YRB);
			twoYearsRB.setActionCommand("true");
		}
		{
			yearsRBGrp = new ButtonGroup();
			yearsRBGrp.add(oneYearRB);
			yearsRBGrp.add(twoYearsRB);
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
			teamsPnl.setBounds(RECTEAMPNL);
			teamsPnl.setOpaque(true);
			teamsPnl.setBackground(bg);
		}
		{
			numOfTeamsCB = new JComboBox();
			teamsPnl.add(numOfTeamsCB);
			numOfTeamsCB.setBounds(RECNOTEAMCB);
			numOfTeamsCB.setModel(new DefaultComboBoxModel(posNumOfTeams));
			numOfTeamsCB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                	numOfTeamsCBItemStateChanged(evt);
                }
            });
		}
		{
			teamsLbl = new JLabel();
			teamsPnl.add(teamsLbl);
			teamsLbl.setBounds(RECTEAMLBL);
			teamsLbl.setText("Mannschaften");
		}
		teamsModel = new DefaultListModel();
		{
			teamsSP = new JScrollPane();
			teamsPnl.add(teamsSP);
			teamsSP.setBounds(RECTEAMSP);
			{
				teamsL = new JList();
			    teamsSP.setViewportView(teamsL);
			    teamsL.setModel(teamsModel);
			    teamsL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			editTeamBtn = new JButton();
			teamsPnl.add(editTeamBtn);
			editTeamBtn.setBounds(RECEDITBTN);
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
			anzahlenPnl.setBounds(RECANZPNL);
			anzahlenPnl.setOpaque(true);
			anzahlenPnl.setBackground(bg);
		}
		for (int i = 0; i < anzahlenLbls.length; i++) {
			anzahlenLbls[i] = new JLabel();
			anzahlenPnl.add(anzahlenLbls[i]);
			anzahlenLbls[i].setBounds(anzLbls[STARTX], anzLbls[STARTY] + i * (anzLbls[HEIGHT] + anzLbls[GAPY]), anzLbls[WIDTH], anzLbls[HEIGHT]);
			anzahlenLbls[i].setText(anzLblsContent[i]);
		}
		for (int i = 0; i < anzahlenTFs.length; i++) {
			anzahlenTFs[i] = new JTextField();
			anzahlenPnl.add(anzahlenTFs[i]);
			anzahlenTFs[i].setBounds(anzTFs[STARTX], anzTFs[STARTY] + i * (anzTFs[HEIGHT] + anzTFs[GAPY]), anzTFs[WIDTH], anzTFs[HEIGHT]);
		}
	}
	
	private void buildKOT() {
		{
			kotPnl = new JPanel();
			getContentPane().add(kotPnl);
			kotPnl.setLayout(null);
			kotPnl.setBounds(RECKOTPNL);
			kotPnl.setOpaque(true);
			kotPnl.setBackground(bg);
		}
		{
			kotLbl = new JLabel();
			kotPnl.add(kotLbl);
			kotLbl.setBounds(RECKOTLBL);
			kotLbl.setText("Anstosszeiten");
		}
		{
			kotSP = new JScrollPane();
			kotPnl.add(kotSP);
			kotSP.setBounds(RECKOTSP);
			{
				kotL = new JList();
				kotSP.setViewportView(kotL);
				kotL.setModel(kotModel);
				kotL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			addKOTBtn = new JButton();
			kotPnl.add(addKOTBtn);
			addKOTBtn.setBounds(RECAKOTBTN);
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
			editKOTBtn.setBounds(RECEKOTBTN);
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
			defKOTLbl.setBounds(RECDEFKOTLBL);
			defKOTLbl.setText("Standard:");
		}
		{
			defaultKOTTF = new JTextField();
			kotPnl.add(defaultKOTTF);
			defaultKOTTF.setBounds(RECDEFKOTTF);
		}
	}
	
	private void enterPresetValues() {
		// TODO remove these preentered values
    	int[] preDefAnzahlen = new int[] {2, 1, 0, 1, 2};
		
		nameTF.setText("2. Bundesliga");
		seasonTF.setText("2014");
        defaultSTCB.setSelectedIndex(4);
		twoYearsRB.setSelected(true);
		numOfTeamsCB.setSelectedIndex(18 - minNumOfTeams);
		for (int i = 0; i < anzahlenTFs.length; i++) {
			anzahlenTFs[i].setText("" + preDefAnzahlen[i]);
		}
		kotModel.addElement("0,1830");
		kotModel.addElement("1,1300");
		kotModel.addElement("2,1330");
		kotModel.addElement("3,2015");
		defaultKOTTF.setText("0,0,0,1,1,2,2,2,3");
	}
	
	private void editTeamBtnActionPerformed() {
		int index = teamsL.getSelectedIndex();
		if (index != -1) {
			String newName = null;
			boolean cancel = false;
			while((newName == null || newName.isEmpty()) && !cancel) {
				newName = inputDialog("Neuer Name fuer " + teamsModel.get(index));
				if (newName == null || newName.isEmpty()) {
					cancel = yesNoDialog("Sie haben keinen validen Namen eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
				}
			}
			if (!cancel)	teamsModel.set(index, newName);
		} else {
			message("Sie haben keine Mannschaft ausgewaehlt.");
		}
	}
	
	private void addKOTBtnActionPerformed() {
		String newKOT = null;
		boolean cancel = false, valid = false;
		while(!valid && !cancel) {
			newKOT = inputDialog("Neue Anstosszeit:");
			valid = validateKOT(newKOT);
			
			if (!valid) {
				cancel = yesNoDialog("Sie haben keine valide Anstosszeit eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
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
				newKOT = inputDialog("Verbesserte Anstosszeit von " + kotModel.get(index));
				valid = validateKOT(newKOT);
				
				if (!valid) {
					cancel = yesNoDialog("Sie haben keine valide Anstosszeit eingegeben. Wollen Sie abbrechen?") == JOptionPane.YES_OPTION;
				}
			}
			if (!cancel)	kotModel.set(index, newKOT);
		} else {
			message("Sie haben keine Anstosszeit ausgewaehlt.");
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
	
	private int[] getAnzahlen() {
		int[] anzahlen = new int[anzahlenTFs.length];
		
		for (int i = 0; i < anzahlen.length; i++) {
			anzahlen[i] = Integer.parseInt(anzahlenTFs[i].getText());
		}
		
		return anzahlen;
	}
	
	private String[] getTeamsNames() {
		String[] teamsNames = new String[numberOfTeams + 1];
		
		teamsNames[0] = "" + numberOfTeams;
		for (int i = 0; i < teamsModel.size(); i++) {
			teamsNames[i + 1] = teamsModel.getElementAt(i).toString() + ";01.01.1970;";
		}
		
		return teamsNames;
	}
	
	private String getKOTs() {
		String kotRepresentation = kotModel.getSize() + ";";
		
		for (int i = 0; i < kotModel.getSize(); i++) {
			kotRepresentation += kotModel.getElementAt(i).toString() + ";";
		}
		
		return kotRepresentation;
	}
	
	private int[] getDefaultKOTs() {
		int[] defaultKOTs = new int[numberOfTeams / 2];
		String[] defKOTs = defaultKOTTF.getText().split(",");
		
		if (defaultKOTs.length != defKOTs.length)	return null;
		
		for (int i = 0; i < defKOTs.length; i++) {
			defaultKOTs[i] = Integer.parseInt(defKOTs[i]);
		}
		
		return defaultKOTs;
	}
	
	private void cancelActionPerformed() {
		this.setVisible(false);
	}
	
	private void goActionPerformed() {
		// TODO kopieren aus der Methode in Start
		

    	log("REQUIREMENT --- This should be done correctly. ");
		
    	name = nameTF.getText();
    	season = Integer.parseInt(seasonTF.getText());
    	numberOfTeams = numOfTeamsCB.getSelectedIndex() + minNumOfTeams;
    	teamsNames = getTeamsNames();
    	anzahlen = getAnzahlen();
    	isSTSS = Boolean.parseBoolean(yearsRBGrp.getSelection().getActionCommand());
    	defaultST = defaultSTCB.getSelectedIndex();
    	KOTs = getKOTs();
    	defKOTs = getDefaultKOTs();
    	
		start.addNewLeague(name, season, numberOfTeams, teamsNames, anzahlen, isSTSS, defaultST, KOTs, defKOTs);
		
		this.setVisible(false);
		start.toFront();
	}
}



