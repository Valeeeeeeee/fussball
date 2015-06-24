package model;

import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 
import java.util.ArrayList;

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
	
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	
	private int anzahlLigen;
	private int anzahlTurniere;
    
	private Liga[] ligen;
	private Liga aktuelleLiga;
	
	private ArrayList<Turnier> turniere;
	private Turnier aktuellesTurnier;
	private Gruppe aktuelleGruppe;
	private KORunde aktuelleKORunde;
	
	private boolean addingNewSeason;
	private boolean teamChangeMode;
	private int newSeasonTeamIndex = -1;
	private ArrayList<Mannschaft> newSeasonTeamsOrder;
	private ArrayList<Mannschaft> oldSeasonTeamsOrder;
	
	private boolean isCurrentlyALeague = false;
    private boolean isCurrentlyInGroupStage = false;
    private boolean isCurrentlyInMatchdayView = false;
    private boolean isCurrentlyInOverviewMode = false;
    
    private int start_btnsstartx = 315;
    private int start_btnsstarty = 120;
    private int start_btnswidth = 400;
    private int start_btnsheight = 70;
    
    private Rectangle REC_BTNZURUECK = new Rectangle(10, 10, 100, 30);
    
    private Rectangle REC_CBSAISONS = new Rectangle(650, 100, 130, 25);
    private Rectangle REC_BTNNEUESAISON = new Rectangle(800, 100, 120, 25);
    private Rectangle REC_BTNSPIELTAGE = new Rectangle(520, 150, 400, 100);
    private Rectangle REC_BTNTABELLE = new Rectangle(520, 270, 400, 100);
    private Rectangle REC_BTNSTATISTIK = new Rectangle(520, 390, 400, 100);
    private Rectangle REC_BTNOPTIONEN = new Rectangle(520, 510, 400, 100);
    
    private Rectangle REC_BEENDEN = new Rectangle(20, 20, 100, 40);
    private Rectangle REC_ADDLEAG = new Rectangle(520, 60, 180, 40);
    private Rectangle REC_ADDTOUR = new Rectangle(740, 60, 180, 40);
    
    private Rectangle REC_LBLSAISON = new Rectangle(30, 30, 50, 25);
    private Rectangle REC_TFSAISON = new Rectangle(90, 30, 50, 25);
    private Rectangle REC_LBLBEARBEITEN = new Rectangle(170, 30, 70, 25);
    private Rectangle REC_BTNNSFERTIG = new Rectangle(400, 750, 80, 30);
    
    // Homescreen
    private JPanel Homescreen;
    private JButton[] jBtnsLigen;
    private JButton[] jBtnsTurniere;
    private JButton addLeague;
    private JButton addTournament;
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
    
    // Liga - neue Saison
    private JPanel LigaNeueSaison;
    private JLabel jLblSaison;
    private JTextField jTFSaison;
    private JLabel jLblBearbeiten;
    private JLabel[] jLblsMannschaftenNeueSaison;
    private JLabel[] jLblsMannschaftenAlteSaison;
    private JLabel jLblName;
    private JTextField jTFName;
    private JLabel jLblDatum;
    private JTextField jTFDatum;
    private JButton jBtnChangeTeamCompleted;
    private JButton jBtnLigaNeueSaisonFertig;
    
    // Turnier - Homescreen
    private JPanel TurnierHomescreen;
    private JButton jBtnGruppenphase;
    private JButton jBtnKORunde;
    
    private JPanel GruppenphaseHomescreen;
    private JButton[] groupStageButtons;
    
    private JPanel KORundeHomescreen;
    private JButton[] KORoundsButtons;
    
    
    private JButton jBtnZurueck;
    
    private Spieltag aktuellerSpieltag;
    private Tabelle aktuelleTabelle;
    private LigaStatistik aktuelleStatistik;
    private Uebersicht uebersicht;
//    public Uebersicht aktuelleUebersicht;
    
    // Optionen-Panel
    public JPanel optionen;
    private JButton correctNames;
    private JLabel defaultStarttag;
    private JComboBox<String> jCBDefStarttag;
    
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
//        message("Den Spieltag fuer zu viele Spiele mit ScrollPane ausstatten. --> DFB-Pokal");
//        message("Fuer die Bounds ein Rectangle[win/mac][alle lbls, cbs, ...] mit den Bounds-Werten. \n"
//        			+ "Zum Aendern int macorwin", "Bounds", JOptionPane.INFORMATION_MESSAGE);
        
//        jBtnTurnierePressed(1);
//        jBtnAlleGruppenPressed();
//        jBtnZurueckActionPerformed();
//        jBtnBeendenActionPerformed();
        
//        jBtnLigenPressed(0);
//        jCBSaisonauswahl.setSelectedIndex(1);
//        jBtnSpieltageActionPerformed();
        
//        jBtnTabelleActionPerformed();
//        uebersichtAnzeigen("VfB Stuttgart");
        
//        addLeagueActionPerformed();
//        addTournamentActionPerformed();
//        jBtnBeendenActionPerformed();
        
        
        testSomethingBeforeIntroducingItIntoTheRealCode();
        
        log("\nProgramm erfolgreich gestartet ...\n\n");
    }
    
    private void testSomethingBeforeIntroducingItIntoTheRealCode() {
    	// TODO do some testing
    	
    	// Heim-/Auswaertstabelle
    	
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
            addLeague = new JButton(); 
            Homescreen.add(addLeague);
            addLeague.setBounds(REC_ADDLEAG);
            addLeague.setText("Add league");
            addLeague.setFocusable(false);
            addLeague.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	addLeagueActionPerformed();
                }
            });
        }
        { 
            addTournament = new JButton(); 
            Homescreen.add(addTournament);
            addTournament.setBounds(REC_ADDTOUR);
            addTournament.setText("Add tournament");
            addTournament.setFocusable(false);
            addTournament.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	addTournamentActionPerformed();
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
        	jBtnsLigen[i].setBounds(start_btnsstartx, start_btnsstarty + i * (start_btnsheight + 10), start_btnswidth, start_btnsheight);
        	jBtnsLigen[i].setText(ligen[i].getName());
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
        	jBtnsTurniere[i].setBounds(start_btnsstartx + start_btnswidth + 10, start_btnsstarty + i * (start_btnsheight + 10), start_btnswidth, start_btnsheight);
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
            LigaHomescreen.add(jCBSaisonauswahl);
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
        	jBtnGruppenphase = new JButton();
        	TurnierHomescreen.add(jBtnGruppenphase);
            jBtnGruppenphase.setBounds(520, 300, start_btnswidth, start_btnsheight);
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
        	jBtnKORunde.setBounds(520, 450, start_btnswidth, start_btnsheight);
        	jBtnKORunde.setText("K.O.-Runde");
        	jBtnKORunde.setFocusable(false);
        	jBtnKORunde.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	jBtnKORundeActionPerformed();
                }
            });
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
        {
        	String[] wochentage = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
        	jCBDefStarttag = new JComboBox<>();
        	optionen.add(jCBDefStarttag);
	        jCBDefStarttag.setModel(new DefaultComboBoxModel<>(wochentage));
	        jCBDefStarttag.setBounds(130, 50, 100, 20);
	        jCBDefStarttag.setVisible(false);
	        jCBDefStarttag.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBDefStarttagItemStateChanged(evt);
	            }
	        });
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
    
    public void jBtnLigenPressed(int index) {
		Homescreen.setVisible(false);
		jBtnZurueck.setVisible(true);
		
    	if (index < anzahlLigen) {
    		// The pressed button leads to a league
    		isCurrentlyALeague = true;
    		LigaHomescreen.setVisible(true);
    		LigaHomescreen.add(jBtnZurueck);
    		
        	aktuelleLiga = ligen[index];
    		jLblWettbewerb.setText(aktuelleLiga.getName());
    		
    		// befuellt die ComboBox mit den verfuegbaren Saisons
            jCBSaisonauswahl.setModel(new DefaultComboBoxModel<>(aktuelleLiga.getAllSeasons()));
    		jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
    		
    		if (jCBSaisonauswahl.getModel().getSize() - 1 == 0) {
        		// dann passiert nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
    			aktuelleLiga.laden(0);
                ligaspezifischesachenladen();
    		}
    		
			aktuellerSpieltag = aktuelleLiga.getSpieltag();
			getContentPane().add(aktuellerSpieltag);
    		aktuelleTabelle = aktuelleLiga.getTabelle();
    		getContentPane().add(aktuelleTabelle);
    		aktuelleStatistik = aktuelleLiga.getLigaStatistik();
    		getContentPane().add(aktuelleStatistik);
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
			
			// befuellt die ComboBox mit den verfuegbaren Saisons
            jCBSaisonauswahl.setModel(new DefaultComboBoxModel<>(aktuellesTurnier.getAllSeasons()));
    		jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
			
    		if (jCBSaisonauswahl.getModel().getSize() - 1 == 0) {
//        		// dann passierte oben nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
                aktuellesTurnier.laden(0);
    			turnierspezifischeSachenLaden();
    		}
    		
    		if (!aktuellesTurnier.hasGroupStage()) {
    			jBtnKORundeActionPerformed();
    		} else if (!aktuellesTurnier.hasKOStage()) {
    			jBtnGruppenphaseActionPerformed();
    		}
		}
    }
    
    public void jBtnGruppenphaseActionPerformed() {
    	isCurrentlyInGroupStage = true;
    	
    	TurnierHomescreen.setVisible(false);
    	GruppenphaseHomescreen.setVisible(true);
    	GruppenphaseHomescreen.add(jBtnZurueck);
    }
    
    public void jBtnKORundeActionPerformed() {
    	isCurrentlyInGroupStage = false;
    	
    	TurnierHomescreen.setVisible(false);
    	KORundeHomescreen.setVisible(true);
    	KORundeHomescreen.add(jBtnZurueck);
    }
    
    public void jBtnAlleGruppenPressed() {
    	GruppenphaseHomescreen.setVisible(false);
    	isCurrentlyInOverviewMode = true;
    	
    	aktuellerSpieltag = aktuellesTurnier.getSpieltag();
    	getContentPane().add(aktuellerSpieltag);
    	aktuellerSpieltag.add(jBtnZurueck);
    	aktuellerSpieltag.setVisible(true);
    	aktuellerSpieltag.spieltagAnzeigen();
    }
    
    public void jBtnGruppePressed(int index) {
    	aktuelleGruppe = aktuellesTurnier.getGruppen()[index];
    	
		jLblWettbewerb.setText("Gruppe " + (index + 1));
    	
    	GruppenphaseHomescreen.setVisible(false);
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
    	aktuelleKORunde = aktuellesTurnier.getKORunden()[index];
    	
    	KORundeHomescreen.setVisible(false);
    	
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
                try {
                	aktuelleLiga.speichern();
                } catch (Exception e) {}
                aktuelleLiga.laden(index);
                ligaspezifischesachenladen();
    		} else {
                try {
                	aktuellesTurnier.speichern();
                } catch (Exception e) {}
                aktuellesTurnier.laden(index);
                ligaspezifischesachenladen();
    		}
    	}
	}
    
    public void jCBDefStarttagItemStateChanged(ItemEvent evt) {
    	if (evt.getStateChange() == ItemEvent.SELECTED) {
    		// TODO default starttag aendern
    	}
	}
    
    private void ligaspezifischesachenladen() {
        {
        	uebersicht = new Uebersicht(this, aktuelleLiga);
            getContentPane().add(uebersicht);
            uebersicht.setLocation((this.WIDTH - uebersicht.getSize().width) / 2, 5);
            uebersicht.setVisible(false);
        }
    }
    
    private void turnierspezifischeSachenLaden() {
    	// falls sie bereits existieren (wird benoetigt, falls ein Turnier komplett neugestartet wird)
		// (im Gegensatz zur Liga muessen die Buttons jedes Mal neu geladen werden, da jedes Turnier unterschiedlich viele Gruppen/KO-Phasen hat)
		if (aktuellesTurnier.hasGroupStage()) {
			try {
				for (int i = 0; i < groupStageButtons.length; i++) {
        			GruppenphaseHomescreen.remove(groupStageButtons[i]);
    			}
	    	} catch(NullPointerException npe) {}
			
			groupStageButtons = new JButton[aktuellesTurnier.getNumberOfGroups() + 1];
	    	for (int i = 0; i < groupStageButtons.length - 1; i++) {
	    		final int x = i;
	    		groupStageButtons[i] = new JButton();
				GruppenphaseHomescreen.add(groupStageButtons[i]);
				groupStageButtons[i].setBounds(520, 50 + i * (60 + 10), start_btnswidth, 60);
				groupStageButtons[i].setText(aktuellesTurnier.getGruppen()[i].getName());
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
				groupStageButtons[lastindex].setBounds(520, 50 + lastindex * (60 + 10), start_btnswidth, 60);
				groupStageButtons[lastindex].setText("Alle Gruppen");
				groupStageButtons[lastindex].setFocusable(false);
				groupStageButtons[lastindex].addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent evt) {
	                	jBtnAlleGruppenPressed();
	                }
	            });
	    	}
		}
		if (aktuellesTurnier.hasKOStage()) {
			try {
				for (int i = 0; i < KORoundsButtons.length; i++) {
        			KORundeHomescreen.remove(KORoundsButtons[i]);
    			}
	    	} catch(NullPointerException npe) {}
			
			KORoundsButtons = new JButton[aktuellesTurnier.getNumberOfKORounds()];
	    	for (int i = 0; i < KORoundsButtons.length; i++) {
	    		final int x = i;
	    		KORoundsButtons[i] = new JButton();
	    		KORundeHomescreen.add(KORoundsButtons[i]);
				KORoundsButtons[i].setBounds(520, 100 + i * (100 + 15), start_btnswidth, 100);
				KORoundsButtons[i].setText(aktuellesTurnier.getKORunden()[i].getName());
				KORoundsButtons[i].setFocusable(false);
				KORoundsButtons[i].addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent evt) {
	                	jBtnKORundePressed(x);
	                }
	            });
			}
		}
    }
    
    // Action- und ItemListener
    private void jBtnNeueSaisonActionPerformed() {
    	addingNewSeason = true;
    	newSeasonTeamsOrder = new ArrayList<>();
    	oldSeasonTeamsOrder = new ArrayList<>();
    	LigaHomescreen.setVisible(false);
    	{
    		LigaNeueSaison = new JPanel();
        	getContentPane().add(LigaNeueSaison);
        	LigaNeueSaison.setLayout(null);
        	LigaNeueSaison.setBounds(470, 20, 500, 800);
        	LigaNeueSaison.setBackground(Color.red);
        }
    	{
    		jLblSaison = new JLabel();
    		LigaNeueSaison.add(jLblSaison);
    		jLblSaison.setBounds(REC_LBLSAISON);
    		jLblSaison.setText("Saison");
    	}
    	{
    		jTFSaison = new JTextField();
    		LigaNeueSaison.add(jTFSaison);
    		jTFSaison.setBounds(REC_TFSAISON);
    		jTFSaison.setText("" + (aktuelleLiga.getAktuelleSaison() + 1));
    		jTFSaison.setOpaque(true);
    	}
    	{
    		jLblBearbeiten = new JLabel();
    		LigaNeueSaison.add(jLblBearbeiten);
    		jLblBearbeiten.setBounds(REC_LBLBEARBEITEN);
    		jLblBearbeiten.setText("Bearbeiten");
    		jLblBearbeiten.setCursor(handCursor);
    		jLblBearbeiten.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent evt) {
	            	changeMode();
	            }
	        });
    	}
    	
    	Mannschaft[] mannschaften = aktuelleLiga.getMannschaften();
    	for (Mannschaft mannschaft : mannschaften) {
    		oldSeasonTeamsOrder.add(mannschaft);
		}
    	jLblsMannschaftenNeueSaison = new JLabel[mannschaften.length];
        jLblsMannschaftenAlteSaison = new JLabel[mannschaften.length];
        for (int i = 0; i < jLblsMannschaftenNeueSaison.length; i++) {
    		final int x = i;
    		jLblsMannschaftenNeueSaison[i] = new JLabel();
    		LigaNeueSaison.add(jLblsMannschaftenNeueSaison[i]);
    		jLblsMannschaftenNeueSaison[i].setBounds(30, 70 + i * 30, 180, 20);
    		jLblsMannschaftenNeueSaison[i].setText("n/a");
    		jLblsMannschaftenNeueSaison[i].setVisible(false);
    		jLblsMannschaftenNeueSaison[i].setCursor(handCursor);
    		jLblsMannschaftenNeueSaison[i].addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent evt) {
	            	putNewTeamToNextFreePosition(x);
	            }
	        });
    		
    		jLblsMannschaftenAlteSaison[i] = new JLabel();
    		LigaNeueSaison.add(jLblsMannschaftenAlteSaison[i]);
    		jLblsMannschaftenAlteSaison[i].setBounds(280, 70 + i * 30, 180, 20);
    		jLblsMannschaftenAlteSaison[i].setText(mannschaften[i].getName());
    		jLblsMannschaftenAlteSaison[i].setOpaque(false);
    		jLblsMannschaftenAlteSaison[i].setCursor(handCursor);
    		jLblsMannschaftenAlteSaison[i].addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent evt) {
	            	putOldTeamToNextFreePosition(x);
	            }
	        });
		}
        int starty = 70 + mannschaften.length * 30;
        Rectangle REC_LBLNAME = new Rectangle(30, starty + 15, 80, 25);
        Rectangle REC_TFNAME = new Rectangle(150, starty + 15, 100, 25);
        Rectangle REC_LBLDATUM = new Rectangle(30, starty + 50, 110, 25);
        Rectangle REC_TFDATUM = new Rectangle(150, starty + 50, 100, 25);
        Rectangle REC_BTNCTFERTIG = new Rectangle(30, starty + 85, 80, 25);
        
    	{
    		jLblName = new JLabel();
    		LigaNeueSaison.add(jLblName);
    		jLblName.setBounds(REC_LBLNAME);
    		jLblName.setText("Vereinsname");
    		jLblName.setVisible(false);
    	}
    	{
    		jTFName = new JTextField();
    		LigaNeueSaison.add(jTFName);
    		jTFName.setBounds(REC_TFNAME);
    		jTFName.setVisible(false);
    	}
    	{
    		jLblDatum = new JLabel();
    		LigaNeueSaison.add(jLblDatum);
    		jLblDatum.setBounds(REC_LBLDATUM);
    		jLblDatum.setText("Gruendungsdatum");
    		jLblDatum.setVisible(false);
    	}
    	{
    		jTFDatum = new JTextField();
    		LigaNeueSaison.add(jTFDatum);
    		jTFDatum.setBounds(REC_TFDATUM);
    		jTFDatum.setVisible(false);
    		jTFDatum.addFocusListener(new FocusAdapter() {
    			public void focusGained(FocusEvent evt) {
    				jTFDatum.selectAll();
    			}
			});
    	}
        {
            jBtnChangeTeamCompleted = new JButton();
            LigaNeueSaison.add(jBtnChangeTeamCompleted);
            jBtnChangeTeamCompleted.setBounds(REC_BTNCTFERTIG);
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
            jBtnLigaNeueSaisonFertig = new JButton();
            LigaNeueSaison.add(jBtnLigaNeueSaisonFertig);
            jBtnLigaNeueSaisonFertig.setBounds(REC_BTNNSFERTIG);
            jBtnLigaNeueSaisonFertig.setText("Fertig");
            jBtnLigaNeueSaisonFertig.setFocusable(false);
            jBtnLigaNeueSaisonFertig.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    jBtnLigaNeueSaisonFertigActionPerformed();
                }
            });
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
    	if (oldSeasonTeamsOrder.size() <= aktuelleLiga.getAnzahlABS()) {
			if(yesNoDialog("Laut Konfiguration muessen " + aktuelleLiga.getAnzahlABS()
					+ " Mannschaften absteigen. Trotzdem fortfahren?") == JOptionPane.NO_OPTION)	return;
		}
    	jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setVisible(true);
    	jLblsMannschaftenNeueSaison[newSeasonTeamsOrder.size()].setText(oldSeasonTeamsOrder.get(index).getName());
    	newSeasonTeamsOrder.add(oldSeasonTeamsOrder.remove(index));
    	jLblsMannschaftenAlteSaison[oldSeasonTeamsOrder.size()].setVisible(false);
    	for (int i = index; i < oldSeasonTeamsOrder.size(); i++) {
    		jLblsMannschaftenAlteSaison[i].setText(oldSeasonTeamsOrder.get(i).getName());
		}
    	jLblsMannschaftenAlteSaison[oldSeasonTeamsOrder.size()].setText("n/a");
    }
    
    private void changeMode() {
    	teamChangeMode = !teamChangeMode;
		if (teamChangeMode) {
			jLblBearbeiten.setText("Fertig");
			for (JLabel label : jLblsMannschaftenNeueSaison) {
				label.setVisible(true);
			}
		} else {
			jLblBearbeiten.setText("Bearbeiten");
			for (int i = newSeasonTeamsOrder.size(); i < jLblsMannschaftenNeueSaison.length; i++) {
				jLblsMannschaftenNeueSaison[i].setVisible(false);
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
    	jLblsMannschaftenNeueSaison[newSeasonTeamIndex].setBackground(Color.green);
		repaintImmediately(jLblsMannschaftenNeueSaison[newSeasonTeamIndex]);
    	
    	if (newSeasonTeamIndex < newSeasonTeamsOrder.size()) {
    		jTFName.setText(newSeasonTeamsOrder.get(newSeasonTeamIndex).getName());
    		jTFDatum.setText(newSeasonTeamsOrder.get(newSeasonTeamIndex).getGruendungsdatum());
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
    		message("Bitte Gruendungsdatum angeben!");
    		return;
    	}
    	int datum = MyDate.getDate(grDatum);
		grDatum = MyDate.datum(datum);
		
		if (newSeasonTeamIndex < newSeasonTeamsOrder.size()) {
			newSeasonTeamsOrder.get(newSeasonTeamIndex).setName(name);
			newSeasonTeamsOrder.get(newSeasonTeamIndex).setGruendungsdatum(grDatum);
		} else {
			Mannschaft mannschaft = new Mannschaft(this, newSeasonTeamIndex, aktuelleLiga, name + ";" + grDatum);
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
    
    private void jBtnLigaNeueSaisonFertigActionPerformed() {
    	int season;
    	try {
    		season = Integer.parseInt(jTFSaison.getText());
    	} catch(NumberFormatException nfe) {
    		message("Bitte geben Sie eine gueltige Zahl an.");
    		return;
    	}
    	
    	if (newSeasonTeamsOrder.size() != aktuelleLiga.getNumberOfTeams()) {
    		message("Bitte fuer die neue Saison " + aktuelleLiga.getNumberOfTeams() + " Mannschaften angeben.");
    		return;
    	}
    	
    	addingNewSeason = false;
    	
    	if (aktuelleLiga.addSeason(season, newSeasonTeamsOrder)) {
    		// befuellt die ComboBox mit den verfuegbaren Saisons
            jCBSaisonauswahl.setModel(new DefaultComboBoxModel<>(aktuelleLiga.getAllSeasons()));
    		jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
    		
    		LigaNeueSaison.setVisible(false);
        	LigaHomescreen.setVisible(true);
    	}
    }
    
    public void jBtnSpieltageActionPerformed() {
    	if (isCurrentlyALeague) {
    		// der Button wurde in einer Liga aufgerufen
    		if (aktuelleLiga.getNumberOfTeams() > 1) {
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
    		if (aktuelleLiga.getNumberOfTeams() <= 0) {
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
        
        uebersicht.add(jBtnZurueck);
    	uebersicht.setMannschaft(index);
    	uebersicht.labelsBefuellen();
    	uebersicht.setVisible(true);
    }
    
    public void spieltagAnzeigen(int matchday) {
    	uebersicht.setVisible(false);
		aktuellerSpieltag.add(jBtnZurueck);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.spieltagAnzeigen();
    	aktuellerSpieltag.showMatchday(matchday);
		isCurrentlyInMatchdayView = true;
    }
    
    public void teamsVerbessern() {
    	if (isCurrentlyALeague) {
    		for (int i = 0; i < aktuelleLiga.getNumberOfTeams(); i++) {
    			String newName = JOptionPane.showInputDialog("Korrekter Name fuer Mannschaft \"" + aktuelleLiga.getMannschaften()[i].getName() + "\"");
        		if (newName != null && !newName.isEmpty()) {
        			aktuelleLiga.getMannschaften()[i].setName(newName);
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
    
    private void addLeagueActionPerformed() {
    	NewLeagueDialog nld = new NewLeagueDialog(this);
    	nld.setLocationRelativeTo(null);
    	nld.setVisible(true);

		this.toFront();
		nld.toFront();
    }
    
    private void addTournamentActionPerformed() {
    	NewTournamentDialog ntd = new NewTournamentDialog(this);
    	ntd.setLocationRelativeTo(null);
    	ntd.setVisible(true);

		this.toFront();
		ntd.toFront();
    }
    
    public void addNewLeague(String name, int season, int numberOfTeams, int spGgSG, String[] teamsNames, int[] anzahlen, boolean isSTSS, int defaultST, String KOTs, int[] defKOTs) {
    	for (Liga liga : ligen) {
			if (liga.getName().equals(name)) {
				message("A league with this name already exists.");
				return;
			}
		}
    	
    	
    	// Erstellung des config-Strings
    	if (teamsNames == null) {
    		teamsNames = new String[numberOfTeams + 1];
    		teamsNames[0] = "" + numberOfTeams;
    		for (int i = 1; i < teamsNames.length; i++)		teamsNames[i] = "Team " + i;
    	}
    	
    	String defaultKOTs = "" + defKOTs[0];
    	for (int i = 1; i < defKOTs.length; i++)	defaultKOTs += "," + defKOTs[i];
    	
    	String daten = "NAME*" + name + ";";
		daten = daten + "D_ST*" + defaultST +";";
		daten = daten + "DKT*" + defaultKOTs + ";";
		daten = daten + "ISSTSS*" + isSTSS + ";";
		daten = daten + "A_MS*" + numberOfTeams + ";";
		daten = daten + "A_SGDG*" + spGgSG + ";";
		daten = daten + "A_CL*" + anzahlen[0] + ";";
		daten = daten + "A_CLQ*" + anzahlen[1] + ";";
		daten = daten + "A_EL*" + anzahlen[2] + ";";
		daten = daten + "A_REL*" + anzahlen[3] + ";";
		daten = daten + "A_ABS*" + anzahlen[4] + ";";
		daten = daten + "A_SAI*" + 1 + ";";
		daten = daten + "S0*" + season + "*S0;";
		
		// Erstellung der Ordner
		String saison;
		if (isSTSS)	saison = season + "_" + (season + 1);
		else		saison = "" + season;
		
    	File leagueFile = new File(workspace + File.separator + name);
    	File seasonFile = new File(workspace + File.separator + name + File.separator + saison);
    	
    	try {
    		leagueFile.mkdir();
    		seasonFile.mkdir();
    	} catch (Exception e) {
    		error("Error while creating directories!");
    	}
    	
    	// Erstellen und Abspeichern des Spiel- und Ergebnisplans
    	int numberOfMatchdays = spGgSG * (2 * (int) Math.round((double) numberOfTeams / 2) - 1);
    	int numberOfMatches = numberOfTeams / 2;
    	
    	String[] spielplan = new String[numberOfMatchdays + 1];
    	String[] ergebnisplan = new String[numberOfMatchdays];
    	
    	String representation = "";
    	for (int i = 0; i < numberOfMatches; i++) {
			representation += "f";
		}
    	
    	spielplan[0] = KOTs;
    	for (int i = 0; i < numberOfMatchdays; i++) {
			spielplan[i + 1] = representation + ";";
			ergebnisplan[i] = representation + ";";
		}
    	
    	inDatei(seasonFile.getAbsolutePath() + File.separator + "Mannschaften.txt", teamsNames);
    	inDatei(seasonFile.getAbsolutePath() + File.separator + "Spielplan.txt", spielplan);
    	inDatei(seasonFile.getAbsolutePath() + File.separator + "Ergebnisse.txt", ergebnisplan);
    	
    	anzahlLigen++;
		Liga[] oldLigen = ligen;
		
		ligen = new Liga[anzahlLigen];
		for (int i = 0; i < oldLigen.length; i++) {
			ligen[i] = oldLigen[i];
		}
		ligen[ligen.length - 1] = new Liga(ligen.length - 1, this, daten);
		
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new league.");
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
                	aktuelleLiga.ergebnisseSichern();
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
            	aktuelleTabelle.add(jBtnZurueck);
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
                    if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0){
                    	JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
                    	return;
                    }
                }
			}
			
			if (aktuelleKORunde != null) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuelleKORunde.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0){
                    	JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
                    	return;
                    }
				}
			}
			
			if (isCurrentlyInOverviewMode) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuellesTurnier.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.jBtnFertigActionPerformed() != 0){
                    	JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
                    	return;
                    }
				}
			}
			
			if (TurnierHomescreen.isVisible()) {
				aktuellesTurnier.speichern();
				TurnierHomescreen.setVisible(false);
				Homescreen.setVisible(true);
			} else if (GruppenphaseHomescreen.isVisible() || KORundeHomescreen.isVisible()) {
				GruppenphaseHomescreen.setVisible(false);
				KORundeHomescreen.setVisible(false);
				TurnierHomescreen.add(jBtnZurueck);
				TurnierHomescreen.setVisible(true);
				if (!aktuellesTurnier.hasGroupStage() || !aktuellesTurnier.hasKOStage()) {
					jBtnZurueckActionPerformed();
				}
			} else if (LigaHomescreen.isVisible()) {
				LigaHomescreen.setVisible(false);
				GruppenphaseHomescreen.add(jBtnZurueck);
				GruppenphaseHomescreen.setVisible(true);
				
				aktuelleGruppe = null;
			} else if (isCurrentlyInOverviewMode) {
				aktuellerSpieltag.setVisible(false);
				GruppenphaseHomescreen.add(jBtnZurueck);
				GruppenphaseHomescreen.setVisible(true);
				isCurrentlyInOverviewMode = false;
				aktuellerSpieltag = null;
			} else if (uebersicht != null && uebersicht.isVisible()) {
            	aktuelleTabelle.add(jBtnZurueck);
            	uebersicht.setVisible(false);
            	aktuelleTabelle.setVisible(true);
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
        ligen = new Liga[anzahlLigen];
        counter++;
        
        for (int i = 0; i < anzahlLigen; i++) {
        	ligen[i] = new Liga(i, this, (String) configurationFromFile.get(counter));
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
		
    	for (int i = 0; i < ligen.length; i++) {
    		configurationFromFile.add(ligen[i].toString());
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
