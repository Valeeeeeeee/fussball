package model;

import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 

import javax.swing.*; 

import static util.Utilities.*;

@SuppressWarnings({"rawtypes","unchecked"}) 
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
    
    String workspace;
    String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
    String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
    
    String config;
    String[] configurationFromFile;
    
	private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
    int anzahlLigen;
    int anzahlTurniere;
    
	private Liga[] ligen;
	private Liga aktuelleLiga;
	
	private Turnier[] turniere;
	private Turnier aktuellesTurnier;
	private Gruppe aktuelleGruppe;
	private KORunde aktuelleKORunde;
	
	private boolean isCurrentlyALeague = false;
    private boolean isCurrentlyInGroupStage = false;
    private boolean isCurrentlyInMatchdayView = false;
    
	int start_btnsstartx = 520;
	int start_btnsstarty = 120;
    int start_btnswidth = 400;
    int start_btnsheight = 80;
    
    Rectangle REC_BEENDEN = new Rectangle(20, 20, 100, 40);
    Rectangle REC_ADDLEAG = new Rectangle(520, 60, 180, 40);
    Rectangle REC_ADDTOUR = new Rectangle(740, 60, 180, 40);
    
    // Homescreen
    public JPanel Homescreen;
    public JButton[] jBtnsLigen;
    private JButton addLeague;
    private JButton addTournament;
    private JButton jBtnBeenden;
    
    // Liga - Homescreen
    private JPanel LigaHomescreen;
    private JLabel jLblWettbewerb;
    private JComboBox jCBSaisonauswahl;
    private JButton jBtnSpieltage;
    private JButton jBtnTabelle;
    private JButton jBtnOptionen;
    
    
    // Turnier - Homescreen
    public JPanel TurnierHomescreen;
    public JButton jBtnGruppenphase;
    public JButton jBtnKORunde;
    
    public JPanel GruppenphaseHomescreen;
    public JButton[] groupStageButtons;
    
    public JPanel KORundeHomescreen;
    public JButton[] KORoundsButtons;
    
    
    public static JButton jBtnZurueck;
    
    private Spieltag aktuellerSpieltag;
    private Tabelle aktuelleTabelle;
    public Uebersicht uebersicht;
//    public Uebersicht aktuelleUebersicht;
    
    // Optionen-Panel
    public JPanel optionen;
    JButton correctNames;
    JButton addTeam;
    JLabel defaultStarttag;
    JComboBox jCBDefStarttag;
    
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
		
        loadConfiguration();
        initGUI();
//        message("Die Klasse Mannschaft erweitern", "", JOptionPane.ERROR_MESSAGE);
//        message("Fuer die Bounds ein Rectangle[win/mac][alle lbls, cbs, ...] mit den Bounds-Werten. \n"
//        			+ "Zum Aendern int macorwin", "Bounds", JOptionPane.INFORMATION_MESSAGE);
        
        jBtnLigenPressed(7);
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
        
        
        
        
        log("\nProgramm erfolgreich gestartet ...\n\n");
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
                jBtnZurueck.setBounds(20, 10, 150, 30);
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
    
    public void buildHomescreen() {
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
    
    public void buildLeaguesButtons() {
    	if (jBtnsLigen != null) {
    		for (int i = 0; i < jBtnsLigen.length; i++)	jBtnsLigen[i].setVisible(false);
    	}
    	
    	jBtnsLigen = new JButton[anzahlLigen + anzahlTurniere];
    	
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
        	final int x = i + anzahlLigen;
        	jBtnsLigen[i + anzahlLigen] = new JButton();
        	Homescreen.add(jBtnsLigen[i + anzahlLigen]);
        	jBtnsLigen[i + anzahlLigen].setBounds(start_btnsstartx, start_btnsstarty + (i + anzahlLigen) * (start_btnsheight + 10), start_btnswidth, start_btnsheight);
        	jBtnsLigen[i + anzahlLigen].setText(turniere[i].getName());
        	jBtnsLigen[i + anzahlLigen].setFocusable(false);
        	jBtnsLigen[i + anzahlLigen].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnLigenPressed(x);
				}
			});
        }
    }
    
    public void buildLigaHomescreen() {
    	{
        	LigaHomescreen = new JPanel();
        	getContentPane().add(LigaHomescreen);
        	LigaHomescreen.setLayout(null);
        	LigaHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
        	LigaHomescreen.setVisible(false);
        	LigaHomescreen.setOpaque(true);
        	LigaHomescreen.setBackground(Color.green);
        }
    	{
    		jLblWettbewerb = new JLabel();
    		LigaHomescreen.add(jLblWettbewerb);
    		jLblWettbewerb.setBounds(540, 100, 200, 25);
    		jLblWettbewerb.setHorizontalAlignment(SwingConstants.CENTER);
    	}
        {
            
            jCBSaisonauswahl = new JComboBox();
            LigaHomescreen.add(jCBSaisonauswahl);
            jCBSaisonauswahl.setBounds(780, 100, 130, 25);
            jCBSaisonauswahl.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                	jCBSaisonauswahlItemStateChanged(evt);
                }
            });
        }
        {
            jBtnSpieltage = new JButton();
            LigaHomescreen.add(jBtnSpieltage);
            jBtnSpieltage.setBounds(520, 150, start_btnswidth, start_btnsheight);
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
            jBtnTabelle.setBounds(520, 300, start_btnswidth, start_btnsheight);
            jBtnTabelle.setText("Tabelle");
            jBtnTabelle.setFocusable(false);
            jBtnTabelle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    jBtnTabelleActionPerformed();
                }
            });
        }
        {
            jBtnOptionen = new JButton();
            LigaHomescreen.add(jBtnOptionen);
            jBtnOptionen.setBounds(520, 450, start_btnswidth, start_btnsheight);
            jBtnOptionen.setText("Optionen");
            jBtnOptionen.setFocusable(false);
            jBtnOptionen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	jBtnOptionenActionPerformed();
                }
            });
        }
    }
    
    public void buildTurnierHomescreen() {
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
        	GruppenphaseHomescreen.setOpaque(true);
        	GruppenphaseHomescreen.setBackground(Color.blue);
        }
    	{
    		KORundeHomescreen = new JPanel();
        	getContentPane().add(KORundeHomescreen);
        	KORundeHomescreen.setLayout(null);
        	KORundeHomescreen.setBounds(0, 0, this.WIDTH, this.HEIGHT);
        	KORundeHomescreen.setVisible(false);
        	KORundeHomescreen.setOpaque(true);
        	KORundeHomescreen.setBackground(Color.yellow);
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
        	addTeam = new JButton();
        	optionen.add(addTeam);
        	addTeam.setBounds(20, 120, 210, 30);
        	addTeam.setText("Mannschaft hinzufuegen");
        	addTeam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mannschaftHinzufuegen();
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
        	jCBDefStarttag = new JComboBox();
        	optionen.add(jCBDefStarttag);
	        jCBDefStarttag.setModel(new DefaultComboBoxModel(wochentage));
	        jCBDefStarttag.setBounds(130, 50, 100, 20);
	        jCBDefStarttag.setVisible(false);
	        jCBDefStarttag.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBDefStarttagItemStateChanged(evt);
	            }
	        });
        }
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
            ComboBoxModel jCBSaisonauswahlModel = new DefaultComboBoxModel(aktuelleLiga.getAllSeasons());
            jCBSaisonauswahl.setModel(jCBSaisonauswahlModel);
    		jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
    		
    		if (jCBSaisonauswahl.getModel().getSize() - 1 == 0) {
        		// dann passiert nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
    			aktuelleLiga.laden(0);
                ligaspezifischesachenladen();
    		}
    		
			aktuellerSpieltag = aktuelleLiga.getSpieltag();
    		aktuelleTabelle = aktuelleLiga.getTable();
		} else {
			// The pressed button leads to a tournament
    		isCurrentlyALeague = false;
			TurnierHomescreen.setVisible(true);
			TurnierHomescreen.add(jBtnZurueck);
			
			aktuellesTurnier = turniere[index - anzahlLigen];
			
			// befuellt die ComboBox mit den verfuegbaren Saisons
            ComboBoxModel jCBSaisonauswahlModel = new DefaultComboBoxModel(aktuellesTurnier.getAllSeasons());
            jCBSaisonauswahl.setModel(jCBSaisonauswahlModel);
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
    	// TODO
    	isCurrentlyInGroupStage = true;
    	
    	TurnierHomescreen.setVisible(false);
    	GruppenphaseHomescreen.setVisible(true);
    	GruppenphaseHomescreen.add(jBtnZurueck);
    }
    
    public void jBtnKORundeActionPerformed() {
    	// TODO
    	isCurrentlyInGroupStage = false;
    	
    	TurnierHomescreen.setVisible(false);
    	KORundeHomescreen.setVisible(true);
    	KORundeHomescreen.add(jBtnZurueck);
    }
    
    public void jBtnAlleGruppenPressed() {
    	GruppenphaseHomescreen.setVisible(false);
    	
    	{
            Spieltag spieltag = new Spieltag(this, aktuellesTurnier);
            spieltag.setLocation((this.WIDTH - spieltag.getSize().width) / 2, (this.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
    }
    
    public void jBtnGruppePressed(int index) {
    	aktuelleGruppe = aktuellesTurnier.getGruppen()[index];
    	
		jLblWettbewerb.setText("Gruppe " + (index + 1));
    	
    	GruppenphaseHomescreen.setVisible(false);
    	LigaHomescreen.setVisible(true);
    	LigaHomescreen.add(jBtnZurueck);
    	
    	aktuellerSpieltag = aktuelleGruppe.getSpieltag();
    	aktuelleTabelle = aktuelleGruppe.getTabelle();
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
				groupStageButtons[i].setBounds(520, 100 + i * (65 + 10), start_btnswidth, 65);
				groupStageButtons[i].setText("Gruppe " + alphabet[i]);
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
				groupStageButtons[lastindex].setBounds(520, 100 + lastindex * (65 + 10), start_btnswidth, 65);
				groupStageButtons[lastindex].setText("Alle Gruppen");
				groupStageButtons[lastindex].setFocusable(false);
				groupStageButtons[lastindex].addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent evt) {
	                	jBtnAlleGruppenPressed();
	                }
	            });
	    	}
		}
		if (aktuellesTurnier.hasGroupStage()) {
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
				KORoundsButtons[i].setBounds(520, 100 + i * (110 + 25), start_btnswidth, 110);
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
    
    @SuppressWarnings("unused")
	private void testLigaAusgabeSpielplanUndErgebnisse() {
    	for (int i = 0; i < aktuelleLiga.getNumberOfMatchdays(); i++) {
    		System.out.println("\n\nNeuer Spieltag");
        	for (int j = 0; j < aktuelleLiga.getNumberOfMatchesPerMatchday(); j++) {
        		if (aktuelleLiga.isSpielplanEntered(i, j)) {
        			log(aktuelleLiga.getMannschaften()[aktuelleLiga.getSpiel(i, j).home() - 1].getName() + " " + 
                			aktuelleLiga.getErgebnis(i, j) + " " + 
                			aktuelleLiga.getMannschaften()[aktuelleLiga.getSpiel(i, j).away() - 1].getName());
        		}
          	}
        }
    }
    
    // Action- und ItemListener
    public void jBtnSpieltageActionPerformed() {
    	if (isCurrentlyALeague) {
    		// der Button wurde in einer Liga aufgerufen
    		if (aktuelleLiga.getNumberOfTeams() > 1) {
    			isCurrentlyInMatchdayView = true;
        		LigaHomescreen.setVisible(false);
        		getContentPane().add(aktuellerSpieltag);
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
    			getContentPane().add(aktuellerSpieltag);
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
		getContentPane().add(aktuelleTabelle);
		aktuelleTabelle.aktualisieren();
		isCurrentlyInMatchdayView = false;
		LigaHomescreen.setVisible(false);
		aktuelleTabelle.add(jBtnZurueck);
		aktuelleTabelle.setVisible(true);
    }
    
    public void jBtnOptionenActionPerformed() {
    	isCurrentlyInMatchdayView = false;
    	
    	LigaHomescreen.setVisible(false);
    	optionen.add(jBtnZurueck);
        optionen.setVisible(true);
    }
    
    public void uebersichtAnzeigen(String name) {
        aktuelleTabelle.setVisible(false);
        if (isCurrentlyALeague) {
        	int index = aktuelleLiga.getIndexOfMannschaft(name);
        	uebersicht.add(jBtnZurueck);
        	uebersicht.setMannschaft(index);
        	uebersicht.labelsBefuellen();
        	uebersicht.setVisible(true);
        } else {
        	int index = -1, groupindex = -1;
        	for (Gruppe gruppe : aktuellesTurnier.getGruppen()) {
        		index = gruppe.getIndexOfMannschaft(name);
        		if (index != -1) {
        			groupindex = gruppe.getID();
        			break;
        		}
        	}
        	
        	uebersicht.add(jBtnZurueck);
        	uebersicht.setMannschaft(index);
        	uebersicht.labelsBefuellen();
        	uebersicht.setVisible(true);
        }
    	
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
    
    public void mannschaftHinzufuegen() {
    	if (!isCurrentlyALeague) {
    		JOptionPane.showMessageDialog(null, "Das Hinzufuegen von Mannschaften ist aktuell bei Turnieren nicht moeglich.");
    		return;
    	}
    	
    	if (JOptionPane.showConfirmDialog(null, "Das Hinzufuegen einer Mannschaft loescht alle zuvor eingegebenen Daten. \nMoechtest du trotzdem fortfahren?") == JOptionPane.NO_OPTION) {
    		return;
    	}
    	
    	String name = JOptionPane.showInputDialog("Du willst also eine neue Mannschaft hinzufuegen! Verraetst du mir den Namen?");
    	int cancel = 1;
    	if (name == null || name.isEmpty()) {
    		cancel = JOptionPane.showConfirmDialog(null, "Du hast nichts eingegeben. Moechtest du abbrechen?", "Fehler", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    		while (cancel != JOptionPane.YES_OPTION) {
    			name = JOptionPane.showInputDialog("Bitte gib den Namen der Mannschaft ein!");
        		cancel = JOptionPane.showConfirmDialog(null, "Du hast wieder nichts eingegeben. Moechtest du wirklich nicht abbrechen?", "Fehler", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        	}
    	}
    	
    	if (name != null && !name.isEmpty()) {
        	try {
				aktuelleLiga.addNewTeam(name, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        	// Optionen-Panel ausblenden
        	optionen.setVisible(false);
        	
        	// Tabelle, Uebersicht, Mannschaftenbuttons, Spieltage ... erneuern
        	ligaspezifischesachenladen();
		}
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
    
    public void addNewLeague(String name, int season, int numberOfTeams, String[] teamsNames, int[] anzahlen, boolean isSTSS, int defaultST, String KOTs, int[] defKOTs) {
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
    	int numberOfMatchdays = 4 * (int) Math.round((double) numberOfTeams / 2) - 2;
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
    
    public void addNewTournament(String name, int season, int stDate, int fiDate, boolean isSTSS, boolean hasGrp, boolean hasKO, boolean grp2leg, boolean ko2leg, boolean has3pl, 
    								int nOTeam, int nOGrp, int nOKO, String[][] teamsGrp, String[][] teamsKO) {
    	for (Turnier turnier : turniere) {
			if (turnier.getName().equals(name)) {
				message("A tournament with this name already exists.");
				return;
			}
		}
    	
    	String daten = "";
    	
    	String[] koRFull = new String[] {"Sechzehntelfinale", "Achtelfinale", "Viertelfinale", "Halbfinale", "Spiel um Platz 3", "Finale"};
    	String[] koRShort = new String[] {"SF", "AF", "VF", "HF", "P3", "FI"};
    	char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
    						'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    	
    	// Erstellung des config-strings
    	
    	daten = "NAME*" + name + ";";
    	daten += "ISSTSS*" + isSTSS + ";";
		daten += "STDATE*" + stDate + ";";
		daten += "FIDATE*" + fiDate + ";";
		daten += "NOFTEAMS*" + nOTeam + ";";
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
    				File groupFile = new File(seasonFile.getAbsolutePath() + File.separator + "Gruppe " + (index + 1));
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
    			inDatei(seasonFile.getAbsolutePath() + File.separator + "KOconfig.txt", koConfig);
    			
        		for (int index = 0; index < nOKO; index++) {
        			// Berechnung der Anzahlen
    				int numberOfTeamsPerKO = 2;
    				for (int i = index; i < nOKO - 1; i++) {
						numberOfTeamsPerKO *= 2;
					}
    				if (has3pl && index < nOKO - 1)	numberOfTeamsPerKO /= 2;
    				
    				int numberOfMatchdays = (ko2leg && index != nOKO - 1) ? 2 : 1;
    				
    				// Erstellung der Mannschaftsnamen sowie des Spielplans und des Ergebnisplans
    				String[] teams = new String[numberOfTeamsPerKO];
    				String[] spielplan = new String[numberOfMatchdays];
    				String[] ergebnisplan = new String[numberOfMatchdays];
    				for (int team = 0; team < numberOfTeamsPerKO; team++) {
    					try {
    						teams[team] = teamsGrp[index][team].substring(0);
    					} catch (NullPointerException npe) {
    						if (index == 0) {
    							if (hasGrp) {
    								teams[team] = "G" + alphabet[team / 2] + (team % 2 + 1);
    							} else {
    								teams[team] = "Mannschaft " + (team + 1);
    							}
    						} else if (index == nOKO - 1) {
    							teams[team] = koRShort[index + diff - (has3pl ? 3 : 2)] + (team + 1);
    						} else {
    							teams[team] = koRShort[index + diff - 2] + (team + 1);
    						}
    					}
    				}
        			
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
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	
    	// Speicherung in config / turniere-Array
    	anzahlTurniere++;
		Turnier[] oldTurniere = turniere;
		
		turniere = new Turnier[anzahlTurniere];
		for (int i = 0; i < oldTurniere.length; i++) {
			turniere[i] = oldTurniere[i];
		}
		turniere[turniere.length - 1]  = new Turnier(turniere.length - 1, this, daten);
		
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
                    if (aktuellerSpieltag.fertigActionPerformed() != 0) {
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
            	optionen.setVisible(false);
                LigaHomescreen.setVisible(true);
            }
		} else {
			// is a tournament
			if (aktuelleGruppe != null && isCurrentlyInMatchdayView) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
                	aktuelleGruppe.ergebnisseSichern();
                } else {
                    if (aktuellerSpieltag.fertigActionPerformed() != 0){
                    	JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten, zurueck gehen war nicht moeglich.");
                    	return;
                    }
                }
			}
			
			if (aktuelleKORunde != null) {
				if (aktuellerSpieltag.getEditedMatchday() == -1) {
					aktuelleKORunde.ergebnisseSichern();
				} else {
					if (aktuellerSpieltag.fertigActionPerformed() != 0){
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
				if (isCurrentlyInGroupStage) {
					GruppenphaseHomescreen.setVisible(true);
					GruppenphaseHomescreen.add(jBtnZurueck);
				} else {
					KORundeHomescreen.setVisible(true);
					KORundeHomescreen.add(jBtnZurueck);
				}
				
				aktuelleGruppe = null;
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
        
        anzahlLigen = Integer.parseInt(configurationFromFile[counter]);
        ligen = new Liga[anzahlLigen];
        counter++;
        
        for (int i = 0; i < anzahlLigen; i++) {
        	ligen[i] = new Liga(i, this, (String) configurationFromFile[counter]);
            counter++;
        }
        
        anzahlTurniere = Integer.parseInt((String) configurationFromFile[counter]);
        turniere = new Turnier[anzahlTurniere];
        counter++;
        
        for (int i = 0; i < anzahlTurniere; i++) {
        	turniere[i] = new Turnier(i, this, (String) configurationFromFile[counter]);
            counter++;
        }
    }
    
    private void saveConfiguration() {
    	this.configurationFromFile = new String[anzahlLigen + anzahlTurniere + 2];
    	
    	int counter = 0;
    	
		configurationFromFile[counter] = "" + anzahlLigen;
		counter++;
		
    	for (int i = 0; i < ligen.length; i++) {
    		configurationFromFile[counter] = ligen[i].toString();
    		counter++;
    	}
    	
		configurationFromFile[counter] = "" + anzahlTurniere;
		counter++;
		
    	for (int i = 0; i < turniere.length; i++) {
    		configurationFromFile[counter] = turniere[i].toString();
    		counter++;
    	}
    	
    	
    	inDatei(config, configurationFromFile);
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

