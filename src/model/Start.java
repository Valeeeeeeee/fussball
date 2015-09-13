package model;

import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*; 

import static util.Utilities.*;

public class Start extends JFrame {
	private static final long serialVersionUID = -3201913070768333811L;
	
	/**
	 * The width of the frame.
	 */
	public final int WIDTH = 1440;
	/**
	 * The height of the frame.
	 */
	public final int HEIGHT = 874;
	
	private static int today;
	
	public String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	private String config;
	private ArrayList<String> configurationFromFile;
	
	private String[] koRFull = new String[] {"1. Runde", "2. Runde", "Achtelfinale", "Viertelfinale", "Halbfinale", "Spiel um Platz 3", "Finale"};
	private String[] koRShort = new String[] {"1R", "2R", "AF", "VF", "HF", "P3", "FI"};
	private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	private int anzahlLigen;
	private int anzahlTurniere;
	
	private ArrayList<Liga> ligen;
	private Liga aktuelleLiga;
	private LigaSaison aktuelleLSaison;
	
	private ArrayList<Turnier> turniere;
	private Turnier aktuellesTurnier;
	private TurnierSaison aktuelleTSaison;
	private Gruppe aktuelleGruppe;
	private KORunde aktuelleKORunde;
	
	private boolean addingNewSeason;
	
	private boolean isCurrentlyALeague = false;
	private boolean isCurrentlyInQualification = false;
	private boolean isCurrentlyInGroupStage = false;
	private boolean isCurrentlyInMatchdayView = false;
	private boolean isCurrentlyInOverviewMode = false;
	
	private int start_btnsstartx = 315;
	private int start_btnsstarty = 120;
	private static final int SIZEX_BTNS = 400;
	private static final int SIZEY_BTNS = 90;
	
	private Rectangle REC_BTNZURUECK = new Rectangle(10, 10, 100, 30);
	
	private Rectangle REC_CBSAISONS = new Rectangle(655, 100, 130, 25);
	private Rectangle REC_BTNNEUESAISON = new Rectangle(800, 100, 120, 25);
	private Rectangle REC_BTNSPIELTAGE = new Rectangle(520, 150, 400, 100);
	private Rectangle REC_BTNTABELLE = new Rectangle(520, 270, 400, 100);
	private Rectangle REC_BTNSTATISTIK = new Rectangle(520, 390, 400, 100);
	private Rectangle REC_BTNOPTIONEN = new Rectangle(520, 510, 400, 100);
	
	private Rectangle REC_BEENDEN = new Rectangle(20, 20, 100, 40);
	private Rectangle REC_ADDLEAG = new Rectangle(520, 60, 180, 40);
	private Rectangle REC_ADDTOUR = new Rectangle(740, 60, 180, 40);
	
	// Homescreen
	private JPanel Homescreen;
	private JButton[] jBtnsLigen;
	private JButton[] jBtnsTurniere;
	private JButton jBtnAddLeague;
	private JButton jBtnAddTournament;
	private JButton jBtnBeenden;
	
	// Liga - Homescreen
	private JPanel LigaHomescreen;
	private JLabel jLblWettbewerb;
	private JComboBox<String> jCBSaisonauswahl;
	private JButton jBtnNeueSaison;
	private JButton jBtnSpieltage;
	private JButton jBtnTabelle;
	private JButton jBtnStatistik;
	private JButton jBtnOptionen;
	
	// Turnier - Homescreen
	private JPanel TurnierHomescreen;
	private JButton jBtnQualifikation;
	private JButton jBtnGruppenphase;
	private JButton jBtnKORunde;
	
	private JPanel QualifikationHomescreen;
	private JButton[] qualificationButtons;
	
	private JPanel GruppenphaseHomescreen;
	private JButton[] groupStageButtons;
	
	private JPanel KORundeHomescreen;
	private JButton[] KORoundsButtons;
	
	
	private JButton jBtnZurueck;
	
	private Spieltag aktuellerSpieltag;
	private Tabelle aktuelleTabelle;
	private LigaStatistik aktuelleStatistik;
	private Uebersicht uebersicht;
//	public Uebersicht aktuelleUebersicht;
	
	// Optionen-Panel
	public JPanel optionen;
	private JButton correctNames;
	private JLabel defaultStarttag;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Start inst = new Start();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public Start() {
		super();
		
		checkOS();
		today = MyDate.newMyDate();
		
		loadConfiguration();
		initGUI();
//		message("Den Spieltag fuer zu viele Spiele mit ScrollPane ausstatten. --> DFB-Pokal");
//		message("Fuer die Bounds ein Rectangle[win/mac][alle lbls, cbs, ...] mit den Bounds-Werten. \n"
//					+ "Zum Aendern int macorwin", "Bounds", JOptionPane.INFORMATION_MESSAGE);
		
//		jBtnTurnierePressed(1);
//		jBtnAlleGruppenPressed();
//		jBtnZurueckActionPerformed();
//		jBtnBeendenActionPerformed();
		
//		jBtnLigenPressed(0);
//		jCBSaisonauswahl.setSelectedIndex(1);
//		jBtnSpieltageActionPerformed();
		
//		jBtnTabelleActionPerformed();
//		uebersichtAnzeigen("VfB Stuttgart");
		
//		jBtnAddLeagueActionPerformed();
//		jBtnAddTournamentActionPerformed();
//		jBtnBeendenActionPerformed();
		
		
		testSomethingBeforeIntroducingItIntoTheRealCode();
		
		log("\nProgramm erfolgreich gestartet ...\n\n");
	}
	
	private void testSomethingBeforeIntroducingItIntoTheRealCode() {
		// TODO do some testing
		
		
		log("\n\n");
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			
			
			buildHomescreen();
			buildLigaHomescreen();
			buildTurnierHomescreen();
			buildOptionenPanel();
			
			
			{
				jBtnZurueck = new JButton();
				getContentPane().add(jBtnZurueck);
				jBtnZurueck.setBounds(REC_BTNZURUECK);
				jBtnZurueck.setText("zurueck");
				jBtnZurueck.setVisible(false);
				jBtnZurueck.setFocusable(false);
				jBtnZurueck.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnZurueckActionPerformed();
					}
				});
			}
			
			pack();
			setSize(WIDTH, HEIGHT);
			setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buildHomescreen() {
		{
			Homescreen = new JPanel();
			getContentPane().add(Homescreen);
			Homescreen.setLayout(null);
			Homescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
		}
		
		buildLeaguesButtons();
		
		{ 
			jBtnAddLeague = new JButton(); 
			Homescreen.add(jBtnAddLeague);
			jBtnAddLeague.setBounds(REC_ADDLEAG);
			jBtnAddLeague.setText("Add league");
			jBtnAddLeague.setFocusable(false);
			jBtnAddLeague.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnAddLeagueActionPerformed();
				}
			});
		}
		{ 
			jBtnAddTournament = new JButton(); 
			Homescreen.add(jBtnAddTournament);
			jBtnAddTournament.setBounds(REC_ADDTOUR);
			jBtnAddTournament.setText("Add tournament");
			jBtnAddTournament.setFocusable(false);
			jBtnAddTournament.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnAddTournamentActionPerformed();
				}
			});
		}
		{ 
			jBtnBeenden = new JButton(); 
			Homescreen.add(jBtnBeenden);
			jBtnBeenden.setBounds(REC_BEENDEN);
			jBtnBeenden.setText("Beenden");
			jBtnBeenden.setFocusable(false);
			jBtnBeenden.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnBeendenActionPerformed();
				}
			});
		}
	}
	
	private void buildLeaguesButtons() {
		if (jBtnsLigen != null) {
			for (int i = 0; i < jBtnsLigen.length; i++)	jBtnsLigen[i].setVisible(false);
		}
		
		jBtnsLigen = new JButton[anzahlLigen];
		jBtnsTurniere = new JButton[anzahlTurniere];
		
		for (int i = 0; i < anzahlLigen; i++) {
			final int x = i;
			jBtnsLigen[i] = new JButton();
			Homescreen.add(jBtnsLigen[i]);
			jBtnsLigen[i].setBounds(start_btnsstartx, start_btnsstarty + i * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
			jBtnsLigen[i].setText(ligen.get(i).getName());
			jBtnsLigen[i].setFocusable(false);
			jBtnsLigen[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnLigenPressed(x);
				}
			});
		}
		for (int i = 0; i < anzahlTurniere; i++) {
			final int x = i;
			jBtnsTurniere[i] = new JButton();
			Homescreen.add(jBtnsTurniere[i]);
			jBtnsTurniere[i].setBounds(start_btnsstartx + SIZEX_BTNS + 10, start_btnsstarty + i * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
			jBtnsTurniere[i].setText(turniere.get(i).getName());
			jBtnsTurniere[i].setFocusable(false);
			jBtnsTurniere[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnTurnierePressed(x);
				}
			});
		}
	}
	
	private void buildLigaHomescreen() {
		{
			LigaHomescreen = new JPanel();
			getContentPane().add(LigaHomescreen);
			LigaHomescreen.setLayout(null);
			LigaHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
			LigaHomescreen.setVisible(false);
		}
		{
			jLblWettbewerb = new JLabel();
			LigaHomescreen.add(jLblWettbewerb);
			jLblWettbewerb.setBounds(520, 100, 110, 25);
			jLblWettbewerb.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			
			jCBSaisonauswahl = new JComboBox<>();
			jCBSaisonauswahl.setBounds(REC_CBSAISONS);
			jCBSaisonauswahl.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					jCBSaisonauswahlItemStateChanged(evt);
				}
			});
		}
		{
			jBtnNeueSaison = new JButton();
			LigaHomescreen.add(jBtnNeueSaison);
			jBtnNeueSaison.setBounds(REC_BTNNEUESAISON);
			jBtnNeueSaison.setText("Neue Saison");
			jBtnNeueSaison.setFocusable(false);
			jBtnNeueSaison.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnNeueSaisonActionPerformed();
				}
			});
		}
		{
			jBtnSpieltage = new JButton();
			LigaHomescreen.add(jBtnSpieltage);
			jBtnSpieltage.setBounds(REC_BTNSPIELTAGE);
			jBtnSpieltage.setText("Spieltage");
			jBtnSpieltage.setFocusable(false);
			jBtnSpieltage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnSpieltageActionPerformed();
				}
			});
		}
		{ 
			jBtnTabelle = new JButton();
			LigaHomescreen.add(jBtnTabelle);
			jBtnTabelle.setBounds(REC_BTNTABELLE);
			jBtnTabelle.setText("Tabelle");
			jBtnTabelle.setFocusable(false);
			jBtnTabelle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnTabelleActionPerformed();
				}
			});
		}
		{ 
			jBtnStatistik = new JButton();
			LigaHomescreen.add(jBtnStatistik);
			jBtnStatistik.setBounds(REC_BTNSTATISTIK);
			jBtnStatistik.setText("Statistik");
			jBtnStatistik.setFocusable(false);
			jBtnStatistik.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnStatistikActionPerformed();
				}
			});
		}
		{
			jBtnOptionen = new JButton();
			LigaHomescreen.add(jBtnOptionen);
			jBtnOptionen.setBounds(REC_BTNOPTIONEN);
			jBtnOptionen.setText("Optionen");
			jBtnOptionen.setFocusable(false);
			jBtnOptionen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnOptionenActionPerformed();
				}
			});
		}
	}
	
	private void buildTurnierHomescreen() {
		{
			TurnierHomescreen = new JPanel();
			getContentPane().add(TurnierHomescreen);
			TurnierHomescreen.setLayout(null);
			TurnierHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
			TurnierHomescreen.setVisible(false);
		}
		{ 
			jBtnQualifikation = new JButton();
			TurnierHomescreen.add(jBtnQualifikation);
			jBtnQualifikation.setBounds(520, 200, SIZEX_BTNS, SIZEY_BTNS);
			jBtnQualifikation.setText("Qualifikation");
			jBtnQualifikation.setFocusable(false);
			jBtnQualifikation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnQualifikationActionPerformed();
				}
			});
		}
		{ 
			jBtnGruppenphase = new JButton();
			TurnierHomescreen.add(jBtnGruppenphase);
			jBtnGruppenphase.setBounds(520, 350, SIZEX_BTNS, SIZEY_BTNS);
			jBtnGruppenphase.setText("Gruppenphase");
			jBtnGruppenphase.setFocusable(false);
			jBtnGruppenphase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnGruppenphaseActionPerformed();
				}
			});
		}
		{ 
			jBtnKORunde = new JButton();
			TurnierHomescreen.add(jBtnKORunde);
			jBtnKORunde.setBounds(520, 500, SIZEX_BTNS, SIZEY_BTNS);
			jBtnKORunde.setText("K.O.-Runde");
			jBtnKORunde.setFocusable(false);
			jBtnKORunde.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnKORundeActionPerformed();
				}
			});
		}
		{
			QualifikationHomescreen = new JPanel();
			getContentPane().add(QualifikationHomescreen);
			QualifikationHomescreen.setLayout(null);
			QualifikationHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
			QualifikationHomescreen.setVisible(false);
		}
		{
			GruppenphaseHomescreen = new JPanel();
			getContentPane().add(GruppenphaseHomescreen);
			GruppenphaseHomescreen.setLayout(null);
			GruppenphaseHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
			GruppenphaseHomescreen.setVisible(false);
		}
		{
			KORundeHomescreen = new JPanel();
			getContentPane().add(KORundeHomescreen);
			KORundeHomescreen.setLayout(null);
			KORundeHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
			KORundeHomescreen.setVisible(false);
		}
	}

	private void buildOptionenPanel() {
		{
			optionen = new JPanel();
			getContentPane().add(optionen);
			optionen.setLayout(null);
			optionen.setBounds(470, 0, 500, this.HEIGHT);
			optionen.setBackground(new Color(255, 255, 0));
			optionen.setVisible(false);
			
		}
		{
			correctNames = new JButton();
			optionen.add(correctNames);
			correctNames.setBounds(20, 80, 210, 30);
			correctNames.setText("Namen verbessern");
			correctNames.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					teamsVerbessern();
				}
			});
		}
		{
			defaultStarttag = new JLabel();
			optionen.add(defaultStarttag);
			defaultStarttag.setBounds(20, 50, 105, 20);
			defaultStarttag.setText("Standard-Starttag:");
			defaultStarttag.setToolTipText("An diesem Wochentag beginnt ueblicherweise ein Spieltag.");
			defaultStarttag.setVisible(false);
		}
	}
	
	public char[] getAlphabet() {
		return alphabet;
	}
	
	public String[] getKoRFull() {
		return koRFull;
	}

	public String[] getKoRShort() {
		return koRShort;
	}

	public String getWorkspace() {
		return workspace;
	}
	
	public boolean addingNewSeason() {
		return addingNewSeason;
	}
	
	public boolean isCurrentlyInQualification() {
		return isCurrentlyInQualification;
	}
	
	public void jBtnLigenPressed(int index) {
		Homescreen.setVisible(false);
		jBtnZurueck.setVisible(true);
		
		if (index < anzahlLigen) {
			// The pressed button leads to a league
			isCurrentlyALeague = true;
			LigaHomescreen.setVisible(true);
			LigaHomescreen.add(jBtnZurueck);
			
			aktuelleLiga = ligen.get(index);
			jLblWettbewerb.setText(aktuelleLiga.getName());
			
			// befuellt die ComboBox mit den verfuegbaren Saisons
			jCBSaisonauswahl.setModel(new DefaultComboBoxModel<>(aktuelleLiga.getAllSeasons()));
			jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
			LigaHomescreen.add(jCBSaisonauswahl);
			
			if (jCBSaisonauswahl.getModel().getSize() - 1 == 0) {
				// dann passiert nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
				aktuelleLiga.laden(0);
				aktuelleLSaison = aktuelleLiga.getAktuelleSaison();
				ligaspezifischesachenladen();
			}
		}
	}
	
	public void jBtnTurnierePressed(int index) {
		Homescreen.setVisible(false);
		jBtnZurueck.setVisible(true);
		
		if (index < anzahlTurniere) {
			// The pressed button leads to a tournament
			isCurrentlyALeague = false;
			TurnierHomescreen.setVisible(true);
			TurnierHomescreen.add(jBtnZurueck);
			
			aktuellesTurnier = turniere.get(index);
			// TODO check if this is visible
			jLblWettbewerb.setText(aktuellesTurnier.getName());
			
			// befuellt die ComboBox mit den verfuegbaren Saisons
			jCBSaisonauswahl.setModel(new DefaultComboBoxModel<>(aktuellesTurnier.getAllSeasons()));
			jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
			TurnierHomescreen.add(jCBSaisonauswahl);
			
			if (jCBSaisonauswahl.getModel().getSize() - 1 == 0) {
//				// dann passierte oben nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
				aktuellesTurnier.laden(0);
				aktuelleTSaison = aktuellesTurnier.getAktuelleSaison();
				turnierspezifischeSachenLaden();
			}
			
			if (!aktuelleTSaison.hasQualification() && !aktuelleTSaison.hasGroupStage()) {
				jBtnKORundeActionPerformed();
				KORundeHomescreen.add(jCBSaisonauswahl);
			} else if (!aktuelleTSaison.hasQualification() && !aktuelleTSaison.hasKOStage()) {
				jBtnGruppenphaseActionPerformed();
				GruppenphaseHomescreen.add(jCBSaisonauswahl);
			}
		}
	}
	
	public void jBtnQualifikationActionPerformed() {
		isCurrentlyInQualification = true;
		isCurrentlyInGroupStage = false;
		
		TurnierHomescreen.setVisible(false);
		QualifikationHomescreen.setVisible(true);
		QualifikationHomescreen.add(jBtnZurueck);
	}
	
	public void jBtnGruppenphaseActionPerformed() {
		isCurrentlyInQualification = false;
		isCurrentlyInGroupStage = true;
		
		TurnierHomescreen.setVisible(false);
		GruppenphaseHomescreen.setVisible(true);
		GruppenphaseHomescreen.add(jBtnZurueck);
	}
	
	public void jBtnKORundeActionPerformed() {
		isCurrentlyInQualification = false;
		isCurrentlyInGroupStage = false;
		
		TurnierHomescreen.setVisible(false);
		KORundeHomescreen.setVisible(true);
		KORundeHomescreen.add(jBtnZurueck);
	}
	
	public void jBtnAlleGruppenPressed() {
		isCurrentlyInOverviewMode = true;
		if (isCurrentlyInQualification) {
			QualifikationHomescreen.setVisible(false);
			aktuellerSpieltag = aktuelleTSaison.getQSpieltag();
		} else {
			GruppenphaseHomescreen.setVisible(false);
			aktuellerSpieltag = aktuelleTSaison.getSpieltag();
		}
		
		getContentPane().add(aktuellerSpieltag);
		aktuellerSpieltag.add(jBtnZurueck);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.spieltagAnzeigen();
	}
	
	public void jBtnGruppePressed(int index) {
		if (isCurrentlyInQualification) {
			aktuelleGruppe = aktuelleTSaison.getQGruppen()[index];
			QualifikationHomescreen.setVisible(false);
		} else {
			aktuelleGruppe = aktuelleTSaison.getGruppen()[index];
			GruppenphaseHomescreen.setVisible(false);
		}
		
		jLblWettbewerb.setText(aktuelleGruppe.getName());
		LigaHomescreen.setVisible(true);
		LigaHomescreen.add(jBtnZurueck);
		
		aktuellerSpieltag = aktuelleGruppe.getSpieltag();
		getContentPane().add(aktuellerSpieltag);
		aktuelleTabelle = aktuelleGruppe.getTabelle();
		getContentPane().add(aktuelleTabelle);
		{
			uebersicht = new Uebersicht(this, aktuelleGruppe);
			getContentPane().add(uebersicht);
			uebersicht.setLocation((this.WIDTH - uebersicht.getSize().width) / 2, 5);
			uebersicht.setVisible(false);
		}
	}
	
	public void jBtnKORundePressed(int index) {
		if (isCurrentlyInQualification) {
			aktuelleKORunde = aktuelleTSaison.getQKORunden()[index];
			QualifikationHomescreen.setVisible(false);
		} else {
			aktuelleKORunde = aktuelleTSaison.getKORunden()[index];
			KORundeHomescreen.setVisible(false);
		}
		
		aktuellerSpieltag = aktuelleKORunde.getSpieltag();
		getContentPane().add(aktuellerSpieltag);
		aktuellerSpieltag.add(jBtnZurueck);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.spieltagAnzeigen();
	}
	
	private void jCBSaisonauswahlItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = jCBSaisonauswahl.getSelectedIndex();
			if (isCurrentlyALeague) {
				aktuelleLiga.speichern();
				aktuelleLiga.laden(index);
				aktuelleLSaison = aktuelleLiga.getAktuelleSaison();
				ligaspezifischesachenladen();
			} else {
				aktuellesTurnier.speichern();
				aktuellesTurnier.laden(index);
				aktuelleTSaison = aktuellesTurnier.getAktuelleSaison();
				turnierspezifischeSachenLaden();
			}
		}
	}
	
	private void ligaspezifischesachenladen() {
		{
			uebersicht = new Uebersicht(this, aktuelleLSaison);
			getContentPane().add(uebersicht);
			uebersicht.setLocation((this.WIDTH - uebersicht.getSize().width) / 2, 5);
			uebersicht.setVisible(false);
		}
		
		aktuellerSpieltag = aktuelleLSaison.getSpieltag();
		getContentPane().add(aktuellerSpieltag);
		aktuelleTabelle = aktuelleLSaison.getTabelle();
		getContentPane().add(aktuelleTabelle);
		aktuelleStatistik = aktuelleLSaison.getLigaStatistik();
		getContentPane().add(aktuelleStatistik);
	}
	
	private void turnierspezifischeSachenLaden() {
		// falls sie bereits existieren entfernen (wird benoetigt, falls ein Turnier komplett neugestartet wird)
		// (im Gegensatz zur Liga muessen die Buttons jedes Mal neu geladen werden, da jedes Turnier unterschiedlich viele Gruppen/KO-Phasen hat)
		if (aktuelleTSaison.hasQualification()) {
			jBtnQualifikation.setVisible(true);
			try {
				for (int i = 0; i < qualificationButtons.length; i++) {
					QualifikationHomescreen.remove(qualificationButtons[i]);
				}
			} catch(NullPointerException npe) {}
			
			int numberOfButtons = aktuelleTSaison.hasQGroupStage() ? aktuelleTSaison.getNumberOfQGroups() + 1 : 0;
			numberOfButtons += aktuelleTSaison.getNumberOfQKORounds();
			qualificationButtons = new JButton[numberOfButtons];
			int ctr;
			for (ctr = 0; ctr < aktuelleTSaison.getNumberOfQGroups(); ctr++) {
				final int x = ctr;
				qualificationButtons[ctr] = new JButton();
				QualifikationHomescreen.add(qualificationButtons[ctr]);
				qualificationButtons[ctr].setBounds(295 + (ctr % 2) * (SIZEX_BTNS + 50), 150 + (ctr / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				qualificationButtons[ctr].setText(aktuelleTSaison.getQGruppen()[ctr].getName());
				qualificationButtons[ctr].setFocusable(false);
				qualificationButtons[ctr].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnGruppePressed(x);
					}
				});
			}
			if (aktuelleTSaison.getNumberOfQGroups() > 0) {
				qualificationButtons[ctr] = new JButton();
				QualifikationHomescreen.add(qualificationButtons[ctr]);
				qualificationButtons[ctr].setBounds(295 + (ctr % 2) * (SIZEX_BTNS + 50), 150 + (ctr / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				qualificationButtons[ctr].setText("Alle Gruppen");
				qualificationButtons[ctr].setFocusable(false);
				qualificationButtons[ctr].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnAlleGruppenPressed();
					}
				});
				ctr++;
			}
			for (int i = 0; i < aktuelleTSaison.getNumberOfQKORounds(); i++) {
				final int x = i, position = i + ctr;
				qualificationButtons[position] = new JButton();
				QualifikationHomescreen.add(qualificationButtons[position]);
				qualificationButtons[position].setBounds(295 + (position % 2) * (SIZEX_BTNS + 50), 150 + position / 2 * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				qualificationButtons[position].setText(aktuelleTSaison.getQKORunden()[x].getName());
				qualificationButtons[position].setFocusable(false);
				qualificationButtons[position].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnKORundePressed(x);
					}
				});
			}
		} else {
			jBtnQualifikation.setVisible(false);
		}
		if (aktuelleTSaison.hasGroupStage()) {
			jBtnGruppenphase.setVisible(true);
			try {
				for (int i = 0; i < groupStageButtons.length; i++) {
					GruppenphaseHomescreen.remove(groupStageButtons[i]);
				}
			} catch(NullPointerException npe) {}
			
			groupStageButtons = new JButton[aktuelleTSaison.getNumberOfGroups() + 1];
			for (int i = 0; i < groupStageButtons.length - 1; i++) {
				final int x = i;
				groupStageButtons[i] = new JButton();
				GruppenphaseHomescreen.add(groupStageButtons[i]);
				groupStageButtons[i].setBounds(295 + (i % 2) * (SIZEX_BTNS + 50), 150 + (i / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				groupStageButtons[i].setText(aktuelleTSaison.getGruppen()[i].getName());
				groupStageButtons[i].setFocusable(false);
				groupStageButtons[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnGruppePressed(x);
					}
				});
			}
			final int lastindex = groupStageButtons.length - 1;
			{
				groupStageButtons[lastindex] = new JButton();
				GruppenphaseHomescreen.add(groupStageButtons[lastindex]);
				groupStageButtons[lastindex].setBounds(295 + (lastindex % 2) * (SIZEX_BTNS + 50), 150 + (lastindex / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				groupStageButtons[lastindex].setText("Alle Gruppen");
				groupStageButtons[lastindex].setFocusable(false);
				groupStageButtons[lastindex].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnAlleGruppenPressed();
					}
				});
			}
		} else {
			jBtnGruppenphase.setVisible(false);
		}
		if (aktuelleTSaison.hasKOStage()) {
			jBtnKORunde.setVisible(true);
			try {
				for (int i = 0; i < KORoundsButtons.length; i++) {
					KORundeHomescreen.remove(KORoundsButtons[i]);
				}
			} catch(NullPointerException npe) {}
			
			KORoundsButtons = new JButton[aktuelleTSaison.getNumberOfKORounds()];
			for (int i = 0; i < KORoundsButtons.length; i++) {
				final int x = i;
				KORoundsButtons[i] = new JButton();
				KORundeHomescreen.add(KORoundsButtons[i]);
				KORoundsButtons[i].setBounds(520, 150 + i * (SIZEY_BTNS + 15), SIZEX_BTNS, SIZEY_BTNS);
				KORoundsButtons[i].setText(aktuelleTSaison.getKORunden()[i].getName());
				KORoundsButtons[i].setFocusable(false);
				KORoundsButtons[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnKORundePressed(x);
					}
				});
			}
		} else {
			jBtnKORunde.setVisible(false);
		}
	}
	
	// Action- und ItemListener
	private void jBtnNeueSaisonActionPerformed() {
		addingNewSeason = true;
		
		NeueLigaSaisonDialog nlsd = new NeueLigaSaisonDialog(this);
		nlsd.setLocationRelativeTo(null);
		nlsd.setVisible(true);
		nlsd.setConfigurationFromPreviousSeason(aktuelleLSaison);
		
		LigaHomescreen.setVisible(false);
	}
	
	public void jBtnNeueLigaSaisonFertigActionPerformed(String toString, ArrayList<String> teamsNewSeasonOrder, String dKOTRep) {
		addingNewSeason = false;
		
		if (aktuelleLiga.addSeason(toString, teamsNewSeasonOrder, dKOTRep)) {
			// befuellt die ComboBox mit den verfuegbaren Saisons
			jCBSaisonauswahl.setModel(new DefaultComboBoxModel<>(aktuelleLiga.getAllSeasons()));
			jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
			
			LigaHomescreen.setVisible(true);
		}
	}
	
	public void jBtnSpieltageActionPerformed() {
		if (isCurrentlyALeague) {
			// der Button wurde in einer Liga aufgerufen
			if (aktuelleLSaison.getNumberOfTeams() > 1) {
				isCurrentlyInMatchdayView = true;
				LigaHomescreen.setVisible(false);
				aktuellerSpieltag.add(jBtnZurueck);
				aktuellerSpieltag.setVisible(true);
				aktuellerSpieltag.spieltagAnzeigen();
			} else {
				JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann kein Spieltag angezeigt werden.");
			}
		} else {
			if(aktuelleGruppe.getNumberOfTeams() > 1) {
				isCurrentlyInMatchdayView = true;
				LigaHomescreen.setVisible(false);
				aktuellerSpieltag.add(jBtnZurueck);
				aktuellerSpieltag.setVisible(true);
				aktuellerSpieltag.spieltagAnzeigen();
			} else {
				JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann kein Spieltag angezeigt werden.");
			}
		}
	}
	
	public void jBtnTabelleActionPerformed() {
		if (isCurrentlyALeague) {
			if (aktuelleLSaison.getNumberOfTeams() <= 0) {
				JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann keine Tabelle angezeigt werden.");
				return;
			}
		} else {
			// der Button wurde in einer Gruppe aufgerufen
			if (aktuelleGruppe.getNumberOfTeams() <= 0) {
				JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann keine Tabelle angezeigt werden.");
				return;
			}
		}
		aktuelleTabelle.aktualisieren();
		isCurrentlyInMatchdayView = false;
		LigaHomescreen.setVisible(false);
		aktuelleTabelle.add(jBtnZurueck);
		aktuelleTabelle.setVisible(true);
	}
	
	public void jBtnStatistikActionPerformed() {
		aktuelleStatistik.aktualisieren();
		LigaHomescreen.setVisible(false);
		aktuelleStatistik.add(jBtnZurueck);
		aktuelleStatistik.setVisible(true);
	}
	
	public void jBtnOptionenActionPerformed() {
		isCurrentlyInMatchdayView = false;
		
		LigaHomescreen.setVisible(false);
		optionen.add(jBtnZurueck);
		optionen.setVisible(true);
	}
	
	public void uebersichtAnzeigen(int index) {
		aktuelleTabelle.setVisible(false);
		
		aktuelleTabelle.remove(jBtnZurueck);
		getContentPane().add(jBtnZurueck);
		jBtnZurueck.repaint();
		uebersicht.setMannschaft(index);
		uebersicht.setVisible(true);
	}
	
	public void spieltagAnzeigen(int matchday) {
		jBtnZurueck.setVisible(false);
		uebersicht.setVisible(false);
		aktuellerSpieltag.add(jBtnZurueck);
		jBtnZurueck.setVisible(true);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.spieltagAnzeigen();
		aktuellerSpieltag.showMatchday(matchday);
		isCurrentlyInMatchdayView = true;
	}
	
	public void teamsVerbessern() {
		if (isCurrentlyALeague) {
			for (int i = 0; i < aktuelleLSaison.getNumberOfTeams(); i++) {
				String newName = JOptionPane.showInputDialog("Korrekter Name fuer Mannschaft \"" + aktuelleLSaison.getMannschaften()[i].getName() + "\"");
				if (newName != null && !newName.isEmpty()) {
					aktuelleLSaison.getMannschaften()[i].setName(newName);
				}
			}
		} else {
			for (int i = 0; i < aktuelleGruppe.getNumberOfTeams(); i++) {
				String newName = JOptionPane.showInputDialog("Korrekter Name fuer Mannschaft \"" + aktuelleGruppe.getMannschaften()[i].getName() + "\"");
				if (newName != null && !newName.isEmpty()) {
					aktuelleGruppe.getMannschaften()[i].setName(newName);
				}
			}
		}
	}
	
	public String getTournamentWorkspaceFromShortName(String shortName, int season) {
		int index = 0;
		for (index = 0; index < turniere.size(); index++) {
			if (turniere.get(index).getShortName().equals(shortName))		break;
		}
		if (index != turniere.size()) {
			return this.turniere.get(index).getWorkspace(season);
		}
		return null;
	}
	
	private void jBtnAddLeagueActionPerformed() {
		NewLeagueDialog nld = new NewLeagueDialog(this);
		nld.setLocationRelativeTo(null);
		nld.setVisible(true);

		this.toFront();
		nld.toFront();
	}
	
	private void jBtnAddTournamentActionPerformed() {
//		testAddNewTournament();
		NewTournamentDialog ntd = new NewTournamentDialog(this);
		ntd.setLocationRelativeTo(null);
		ntd.setVisible(true);

		this.toFront();
		ntd.toFront();
	}
	
	public void addNewLeague(String name, int season, boolean isSTSS, int numberOfTeams, int spGgSG, String defKOTsRep, boolean goalD, boolean tKader, String anzahlenRep, ArrayList<String> teamsNames, String KOTsRep) {
		for (Liga liga : ligen) {
			if (liga.getName().equals(name)) {
				message("A league with this name already exists.");
				return;
			}
		}
		
		try {
			File leagueFile = new File(workspace + File.separator + name);
			leagueFile.mkdir();
		} catch (Exception e) {
			error("Error while creating directories: " + e.getMessage());
			message("The league could not be created because of an unexpected file error.");
			return;
		}
		
		String toString = name + ";";
		Liga neueLiga = new Liga(anzahlLigen, this, toString);
		ligen.add(neueLiga);
		anzahlLigen++;
		
		toString = season + ";";
		toString += isSTSS + ";";
		toString += numberOfTeams + ";";
		toString += spGgSG + ";";
		toString += defKOTsRep + ";";
		toString += goalD + ";";
		toString += tKader + ";";
		toString += anzahlenRep + ";";
		
		neueLiga.addSeason(toString, teamsNames, KOTsRep);
		
		for (int i = 0; i < ligen.size(); i++) {
			ligen.get(i).speichern();
		}
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new league.");
	}
	
	public void testAddNewTournament() {
		String name = "UEFA Europameisterschaft";
		String shortName = "EM";
		int season = 2016;
		boolean isSTSS = true;
		int stDate = 20140907;
		int fiDate = 20160710;
		boolean hasQ = true;
		boolean hasGrp = true;
		boolean hasKO = true;
		boolean has3pl = false;
		ArrayList<String> qConfig = new ArrayList<>(Arrays.asList(new String[] {"20140907", "20151117", "9", "true", "1", "Play-offs;PO;true;true;0;8;0"}));
		String[][] teamsQG = new String[][] {new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5"}};
		String[][] teamsQKO = new String[][] {new String[] {"GA3", "GB3", "GC3", "GD3", "GE3", "GF3", "GG3", "GH3"}};
		ArrayList<String> grpConfig = new ArrayList<>(Arrays.asList(new String[] {"6", "false"}));
		String[][] teamsGrp = new String[][] {new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}, new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}, new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}, new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}};
		ArrayList<String> koConfig = new ArrayList<>(Arrays.asList(new String[] {"Achtelfinale;AF;true;false;0;16;0", "Viertelfinale;VF;true;false;0;8;0", 
																					"Halbfinale;HF;true;false;0;4;0", "Finale;FI;true;false;0;2;0"}));
		String[][] teamsKO = new String[][] {new String[] {"GA1", "GA2", "GB1", "GB2", "GC1", "GC2", "GD1", "GD2", "GE1", "GE2", "GF1", "GF2", "GA3", "GB3", "GC3", "GD3"}, 
								new String[] {"AF1", "AF2", "AF3", "AF4", "AF5", "AF6", "AF7", "AF8"}, new String[] {"VF1", "VF2", "VF3", "VF4"}, new String[] {"HF1", "HF2"}};
		addNewTournament(name, shortName, season, isSTSS, stDate, fiDate, hasQ, hasGrp, hasKO, has3pl, qConfig, teamsQG, teamsQKO, grpConfig, teamsGrp, koConfig, teamsKO);
	}
	
	public void addNewTournament(String name, String shortName, int season, boolean isSTSS, int stDate, int fiDate, boolean hasQ, boolean hasGrp, boolean hasKO, boolean has3pl,
									ArrayList<String> qConfig, String[][] teamsQG, String[][] teamsQKO, ArrayList<String> grpConfig, String[][] teamsGrp, ArrayList<String> koConfig, String[][] teamsKO) {
		for (Turnier turnier : turniere) {
			if (turnier.getName().equals(name)) {
				message("A tournament with this name already exists.");
				return;
			}
		}
		
		String toString = "NAME*" + name + ";";
		toString += "SHN*" + shortName + ";";
		
		Turnier neuesTurnier = new Turnier(anzahlTurniere, this, toString);
		turniere.add(neuesTurnier);
		anzahlTurniere++;
		
		toString = season + ";";
		toString += isSTSS + ";";
		toString += stDate + ";";
		toString += fiDate + ";";
		toString += hasQ + ";";
		toString += hasGrp + ";";
		toString += hasKO + ";";
		toString += has3pl + ";";
		
		neuesTurnier.addNewSeason(toString, qConfig, teamsQG, teamsQKO, grpConfig, teamsGrp, koConfig, teamsKO);
		
		for (int i = 0; i < turniere.size(); i++) {
			turniere.get(i).speichern();
		}
		
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new tournament.");
	}
	
	public void addNewTournament(String name, String shortName, int season, int stDate, int fiDate, boolean isSTSS, boolean hasQ, boolean hasGrp, boolean hasKO, boolean grp2leg, boolean ko2leg, boolean has3pl, 
									int nOTeam, int nOGrp, int nOKO, String[][] teamsGrp, String[][] teamsKO) {
		for (Turnier turnier : turniere) {
			if (turnier.getName().equals(name)) {
				message("A tournament with this name already exists.");
				return;
			}
		}
		
		String daten = "";
		
		// Erstellung des config-strings
		
		daten = "NAME*" + name + ";";
		daten += "SHN*" + shortName + ";";
		daten += "ISSTSS*" + isSTSS + ";";
		daten += "STDATE*" + stDate + ";";
		daten += "FIDATE*" + fiDate + ";";
		daten += "NOFTEAMS*" + nOTeam + ";";
		daten += "QUALI*" + hasQ + ";";
		daten += "GRPSTG*" + hasGrp + ";";
		daten += "KOSTG*" + hasKO + ";";
		daten += "NOFGRPS*" + nOGrp + ";";
		daten += "GRPSECLEG*" + grp2leg + ";";
		daten += "KOSECLEG*"+ ko2leg + ";";
		daten += "MATCHF3PL*" + has3pl + ";";
		daten += "S0*" + season + "*S0;";
		
		File leagueFile = new File(workspace + File.separator + name);
		File seasonFile = new File(workspace + File.separator + name + File.separator + season + (isSTSS ? "_" + (season + 1) : ""));
		try {
			leagueFile.mkdir();
			seasonFile.mkdir();
		} catch (Exception e) {
			error("Error while creating season root directory!");
		}
		if (hasQ) {
			
		}
		if (hasGrp) {
			try {
				for (int index = 0; index < nOGrp; index++) {
					// Berechnung der Anzahlen
					int numberOfTeamsPerGroup = (nOTeam + nOGrp - 1) / nOGrp;
					if ((numberOfTeamsPerGroup * nOGrp - nOTeam) > (nOGrp - index - 1))	numberOfTeamsPerGroup--;
					
					int numberOfMatchdays = 2 * ((numberOfTeamsPerGroup + 1) / 2) - 1;
					numberOfMatchdays *= grp2leg ? 2 : 1;
					
					// Erstellung der Mannschaftsnamen sowie des Spielplans und des Ergebnisplans
					String[] teams = new String[numberOfTeamsPerGroup];
					String[] spielplan = new String[numberOfMatchdays];
					String[] ergebnisplan = new String[numberOfMatchdays];
					for (int team = 0; team < numberOfTeamsPerGroup; team++) {
						try {
							teams[team] = teamsGrp[index][team].substring(0);
						} catch (NullPointerException npe) {
							teams[team] = "Mannschaft " + (team + 1);
						}
					}
					
					String falseStr = "";
					for (int i = 0; i < numberOfTeamsPerGroup / 2; i++) {
						falseStr += "f";
					}
					
					for (int i = 0; i < numberOfMatchdays; i++) {
						spielplan[i] = falseStr;
						ergebnisplan[i] = falseStr;
					}

					// Erstellung des Ordners
					File groupFile = new File(seasonFile.getAbsolutePath() + File.separator + "Gruppe " + alphabet[index]);
					groupFile.mkdir();
					
					// Speicherung des Spielplans und des Ergebnisplans
					inDatei(groupFile.getAbsolutePath() + File.separator + "Mannschaften.txt", teams);
					inDatei(groupFile.getAbsolutePath() + File.separator + "Spielplan.txt", spielplan);
					inDatei(groupFile.getAbsolutePath() + File.separator + "Ergebnisse.txt", ergebnisplan);
				}
			} catch (Exception e) {
				error("Error while creating group root directory!");
			}
		}
		if (hasKO) {
			try {
				// Erstellung und Abspeicherung des KOconfig-strings
				int skip3pl = has3pl ? 0 : 1, diff = koRFull.length - nOKO;
				String[] koConfig = new String[nOKO];
				for (int i = 0; i < nOKO - 1; i++) {
					koConfig[i] = koRFull[i + diff - skip3pl] + ";" + koRShort[i + diff - skip3pl] + ";" + (!has3pl || i != nOKO - 2) + ";" + ko2leg + ";";
				}
				koConfig[koConfig.length - 1] = koRFull[koRFull.length - 1] + ";" + koRShort[koRShort.length - 1] + ";" + true + ";" + false + ";";
				
				for (int index = 0; index < nOKO; index++) {
					// Berechnung der Anzahlen
					int numberOfTeamsPerKO = 2;
					for (int i = index; i < nOKO - 1; i++) {
						numberOfTeamsPerKO *= 2;
					}
					if (has3pl && index < nOKO - 1)	numberOfTeamsPerKO /= 2;
					
					int numberOfMatchdays = (ko2leg && index != nOKO - 1) ? 2 : 1;
					
					// Vervollstaendigung der KO-Konfiguration
					int numberOfTeamsPrequalified = 0;
					int numberOfTeamsFromPreviousRound = 0;
					int numberOfTeamsFromOtherCompetition = 0;
					
					// Erstellung der Mannschaftsnamen sowie des Spielplans und des Ergebnisplans
					String[] teams = new String[numberOfTeamsPerKO];
					String[] spielplan = new String[numberOfMatchdays];
					String[] ergebnisplan = new String[numberOfMatchdays];
					for (int team = 0; team < numberOfTeamsPerKO; team++) {
						try {
							String[] split = teamsKO[index][team].split("#");
							teams[team] = split[0];
							if (split[1].equals("PQ"))		numberOfTeamsPrequalified++;
							else if (split[1].equals("PR"))	numberOfTeamsFromPreviousRound++;
							else if (split[1].equals("OC"))	numberOfTeamsFromOtherCompetition++;
						} catch (NullPointerException npe) {
							if (index == 0) {
								// TODO Gruppendritte
								// TODO Fehler finden: war ueberall eine Runde zu spaet(im Finale: VF1:VF2)
								if (hasGrp) {
									teams[team] = "G" + alphabet[team / 2] + (team % 2 + 1);
									numberOfTeamsFromPreviousRound++;
								} else {
									teams[team] = "Mannschaft " + (team + 1);
									numberOfTeamsPrequalified++;
								}
							} else if (index == nOKO - 1) {
								teams[team] = koRShort[index + diff - (has3pl ? 3 : 2)] + (team + 1);
								numberOfTeamsFromPreviousRound++;
							} else {
								teams[team] = koRShort[index + diff - 2] + (team + 1);
								numberOfTeamsFromPreviousRound++;
							}
						}
					}
					
					koConfig[index] += numberOfTeamsPrequalified + ";" + numberOfTeamsFromPreviousRound + ";" + numberOfTeamsFromOtherCompetition + ";";
					
					String falseStr = "";
					for (int i = 0; i < numberOfTeamsPerKO / 2; i++) {
						falseStr += "f";
					}
					
					for (int i = 0; i < numberOfMatchdays; i++) {
						spielplan[i] = falseStr;
						ergebnisplan[i] = falseStr;
					}
					
					// Erstellung des Ordners
					File koFile = new File(seasonFile.getAbsolutePath() + File.separator + koConfig[index].split(";")[0]);
					koFile.mkdir();
					
					// Speicherung des Spielplans und des Ergebnisplans
					inDatei(koFile.getAbsolutePath() + File.separator + "Mannschaften.txt", teams);
					inDatei(koFile.getAbsolutePath() + File.separator + "Spielplan.txt", spielplan);
					inDatei(koFile.getAbsolutePath() + File.separator + "Ergebnisse.txt", ergebnisplan);
				}
				inDatei(seasonFile.getAbsolutePath() + File.separator + "KOconfig.txt", koConfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// Speicherung in config / turniere-Array
		anzahlTurniere++;
		turniere.add(new Turnier(turniere.size(), this, daten));
		
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new tournament.");
	}
	
	public void jBtnBeendenActionPerformed() {
		saveConfiguration();
		System.exit(0);
	}
	
	public void jBtnZurueckActionPerformed() {
		if (isCurrentlyALeague) {
			// is a league
			if (aktuellerSpieltag.isVisible()) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuelleLSaison.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0) {
						JOptionPane.showMessageDialog(null, "huhu");
						return;
					}
				}
			}
			
			if (LigaHomescreen.isVisible()) {
				try {
					aktuelleLiga.speichern();
				} catch (Exception e) {
					e.printStackTrace();
				}
				LigaHomescreen.setVisible(false);
				Homescreen.setVisible(true);
			} else if (uebersicht.isVisible()) {
				jBtnZurueck.setVisible(false);
				getContentPane().remove(jBtnZurueck);
				aktuelleTabelle.add(jBtnZurueck);
				jBtnZurueck.setVisible(true);
				uebersicht.setVisible(false);
				aktuelleTabelle.setVisible(true);
			} else {
				LigaHomescreen.add(jBtnZurueck);
				aktuellerSpieltag.setVisible(false);
				aktuelleTabelle.setVisible(false);
				aktuelleStatistik.setVisible(false);
				optionen.setVisible(false);
				LigaHomescreen.setVisible(true);
			}
		} else {
			// is a tournament
			if (aktuelleGruppe != null && isCurrentlyInMatchdayView) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuelleGruppe.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0) {
						JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
						return;
					}
				}
			}
			
			if (aktuelleKORunde != null) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuelleKORunde.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0) {
						JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
						return;
					}
				}
			}
			
			if (isCurrentlyInOverviewMode) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuelleTSaison.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0) {
						JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
						return;
					}
				}
			}
			
			if (TurnierHomescreen.isVisible()) {
				aktuellesTurnier.speichern();
				TurnierHomescreen.setVisible(false);
				Homescreen.setVisible(true);
			} else if (QualifikationHomescreen.isVisible() || GruppenphaseHomescreen.isVisible() || KORundeHomescreen.isVisible()) {
				QualifikationHomescreen.setVisible(false);
				GruppenphaseHomescreen.setVisible(false);
				KORundeHomescreen.setVisible(false);
				TurnierHomescreen.add(jBtnZurueck);
				TurnierHomescreen.setVisible(true);
				if ((!aktuelleTSaison.hasQualification() && !aktuelleTSaison.hasGroupStage()) || (!aktuelleTSaison.hasQualification() && !aktuelleTSaison.hasKOStage())) {
					jBtnZurueckActionPerformed();
				}
			} else if (LigaHomescreen.isVisible()) {
				LigaHomescreen.setVisible(false);
				if (isCurrentlyInQualification) {
					QualifikationHomescreen.add(jBtnZurueck);
					QualifikationHomescreen.setVisible(true);
				} else {
					GruppenphaseHomescreen.add(jBtnZurueck);
					GruppenphaseHomescreen.setVisible(true);
				}
				
				aktuelleGruppe = null;
			} else if (isCurrentlyInOverviewMode) {
				aktuellerSpieltag.setVisible(false);
				if (isCurrentlyInQualification) {
					QualifikationHomescreen.add(jBtnZurueck);
					QualifikationHomescreen.setVisible(true);
				} else {
					GruppenphaseHomescreen.add(jBtnZurueck);
					GruppenphaseHomescreen.setVisible(true);
				}
				isCurrentlyInOverviewMode = false;
				aktuellerSpieltag = null;
			} else if (uebersicht != null && uebersicht.isVisible()) {
				jBtnZurueck.setVisible(false);
				getContentPane().remove(jBtnZurueck);
				aktuelleTabelle.add(jBtnZurueck);
				jBtnZurueck.setVisible(true);
				uebersicht.setVisible(false);
				aktuelleTabelle.setVisible(true);
			} else if (isCurrentlyInQualification) {
				if (aktuelleTSaison.hasQGroupStage() && aktuelleTabelle != null) {
					aktuelleTabelle.setVisible(false);
					optionen.setVisible(false);
				}
				aktuellerSpieltag.setVisible(false);
				QualifikationHomescreen.add(jBtnZurueck);
				QualifikationHomescreen.setVisible(true);
				
				aktuelleGruppe = null;
				aktuelleKORunde = null;
			} else if (isCurrentlyInGroupStage) {
				aktuellerSpieltag.setVisible(false);
				aktuelleTabelle.setVisible(false);
				optionen.setVisible(false);
				
				LigaHomescreen.add(jBtnZurueck);
				LigaHomescreen.setVisible(true);
			} else {
				aktuellerSpieltag.setVisible(false);
				aktuelleKORunde = null;
				
				KORundeHomescreen.add(jBtnZurueck);
				KORundeHomescreen.setVisible(true);
			}
		}
	}
	
	private void loadConfiguration() {
		config = workspace + File.separator + "config.txt";
		this.configurationFromFile = ausDatei(config);
		
		int counter = 0;
		
		anzahlLigen = Integer.parseInt(configurationFromFile.get(counter));
		ligen = new ArrayList<>();
		counter++;
		
		for (int i = 0; i < anzahlLigen; i++) {
			ligen.add(new Liga(i, this, (String) configurationFromFile.get(counter)));
			counter++;
		}
		
		anzahlTurniere = Integer.parseInt((String) configurationFromFile.get(counter));
		turniere = new ArrayList<>();
		counter++;
		
		for (int i = 0; i < anzahlTurniere; i++) {
			turniere.add(new Turnier(i, this, (String) configurationFromFile.get(counter)));
			counter++;
		}
	}
	
	private void saveConfiguration() {
		this.configurationFromFile = new ArrayList<>();
		
		
		configurationFromFile.add("" + anzahlLigen);
		
		for (int i = 0; i < ligen.size(); i++) {
			configurationFromFile.add(ligen.get(i).toString());
		}
		
		configurationFromFile.add("" + anzahlTurniere);
		
		for (int i = 0; i < turniere.size(); i++) {
			configurationFromFile.add(turniere.get(i).toString());
		}
		
		inDatei(config, configurationFromFile);
	}
	
	public static int today() {
		return today;
	}
	
	public void checkOS() {
		if (new File(workspaceWIN).isDirectory()) {
//			JOptionPane.showMessageDialog(null, "You are running Windows.");
			workspace = workspaceWIN;
		} else if (new File(workspaceMAC).isDirectory()) {
//			JOptionPane.showMessageDialog(null, "You have a Mac.");
			workspace = workspaceMAC;
		} else {
//			JOptionPane.showMessageDialog(null, "You are running neither OS X nor Windows, probably Linux!");
			workspace = null;
		}
	}
}
