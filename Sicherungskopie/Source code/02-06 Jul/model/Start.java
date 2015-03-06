package model;

import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 

import javax.swing.*; 

import static util.Utilities.*;

@SuppressWarnings({"rawtypes","unchecked"}) 
public class Start extends JFrame {
    private static final long serialVersionUID = -3201913070768333811L;
    
    String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
    
    String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
    
    String config = workspaceMAC + File.separator + "config.txt";
    String[] configurationFromFile;
    
    
    
    int anzahl_ligen;
    int anzahl_turniere;
    
	static Liga[] ligen;
	static int aktuelle_liga = -1;
	
	static Turnier[] turniere;
	static int aktuelles_turnier = -1;
	int aktuelle_gruppe = -1;
	
	private boolean isCurrentlyALeague = false;
    private boolean isCurrentlyInGroupStage = false;
    private boolean isCurrentlyInMatchdayView = false;
    
	int start_btnsstartx = 520;
	int start_btnsstarty = 120;
    int start_btnswidth = 400;
    int start_btnsheight = 100;
    int m_btnsstartx= 30;
    int m_btnsstarty = 30;
    int m_btnsgapx = 20;
    int m_btnsgapy = 20;
    int m_btnswidth = 200;
    int m_btnsheight = 60;
    
    // Homescreen
    public JPanel Homescreen;
    public JButton[] jBtnsLigen;
    
    // Liga - Homescreen
    public JPanel LigaHomescreen;
    public JComboBox jCBSaisonauswahl;
    public JButton jBtnSpieltage;
    public JButton jBtnTabelle;
    public JButton jBtnOptionen;
    public JButton jBtnBeenden;
    
    // Turnier - Homescreen
    public JPanel TurnierHomescreen;
    public JButton jBtnGruppenphase;
    public JButton jBtnKORunde;
    
    public JPanel GruppenphaseHomescreen;
    public JButton[] groupStageButtons;
    
    public JPanel KORundeHomescreen;
    
    
    public static JButton jBtnZurueck;
    
    private Spieltag aktuellerSpieltag;
    private Tabelle aktuelleTabelle;
    public static Uebersicht uebersicht;
//    public Uebersicht aktuelleUebersicht;
    public JPanel optionen;
    
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
        loadConfiguration();
        initGUI();
//      JOptionPane.showMessageDialog(null, "Datumsmethoden nach MyDate auslagern", "", JOptionPane.ERROR_MESSAGE);
//        JOptionPane.showMessageDialog(null, "Die Klasse Mannschaft erweitern", "", JOptionPane.ERROR_MESSAGE);
//        JOptionPane.showMessageDialog(null, "Für die Bounds ein Rectangle[win/mac][alle lbls, cbs, ...] mit den Bounds-Werten. \n"
//        									+ "Zum Ändern int macorwin", "Bounds", JOptionPane.INFORMATION_MESSAGE);
        
//        jBtnLigenPressed(3); // TODO
//        jBtnZurueckActionPerformed();
//        jBtnBeendenActionPerformed();
        
//        jBtnTabelleActionPerformed();
//        uebersicht_anzeigen("VfB Stuttgart");
    }
    
    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(null);
            
            
            buildHomescreen();
            buildLigaHomescreen();
            buildTurnierHomescreen();
            
            
            {
                jBtnZurueck = new JButton();
                getContentPane().add(jBtnZurueck);
                jBtnZurueck.setBounds(20, 10, 150, 30);
                jBtnZurueck.setText("zurück");
                jBtnZurueck.setVisible(false);
                jBtnZurueck.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jBtnZurueckActionPerformed();
                    }
                });
            }
            
            pack();
            setSize(1440, 874);
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
        	Homescreen.setBounds(0, 0, 1440, 874);
        }
        
        for (int i = 0; i < anzahl_ligen; i++) {
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
        for (int i = 0; i < anzahl_turniere; i++) {
        	final int x = i + anzahl_ligen;
        	jBtnsLigen[i + anzahl_ligen] = new JButton();
        	Homescreen.add(jBtnsLigen[i + anzahl_ligen]);
        	jBtnsLigen[i + anzahl_ligen].setBounds(start_btnsstartx, start_btnsstarty + (i + anzahl_ligen) * (start_btnsheight + 10), start_btnswidth, start_btnsheight);
        	jBtnsLigen[i + anzahl_ligen].setText(turniere[i].getName());
        	jBtnsLigen[i + anzahl_ligen].setFocusable(false);
        	jBtnsLigen[i + anzahl_ligen].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnLigenPressed(x);
				}
			});
        }
        { 
            jBtnBeenden = new JButton(); 
            Homescreen.add(jBtnBeenden);
            jBtnBeenden.setBounds(520, 600, start_btnswidth, start_btnsheight);
            jBtnBeenden.setText("Beenden");
            jBtnBeenden.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    jBtnBeendenActionPerformed();
                }
            });
        }
    }
    
    public void buildLigaHomescreen() {
    	{
        	LigaHomescreen = new JPanel();
        	getContentPane().add(LigaHomescreen);
        	LigaHomescreen.setLayout(null);
        	LigaHomescreen.setBounds(0, 0, 1440, 874);
        	LigaHomescreen.setVisible(false);
        }
        {
            
            jCBSaisonauswahl = new JComboBox();
            LigaHomescreen.add(jCBSaisonauswahl);
            jCBSaisonauswahl.setBounds(670, 100, 100, 25);
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
        	TurnierHomescreen.setBounds(0, 0, 1440, 874);
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
        	GruppenphaseHomescreen.setBounds(0, 0, 1440, 874);
        	GruppenphaseHomescreen.setVisible(false);
        	GruppenphaseHomescreen.setOpaque(true);
        	GruppenphaseHomescreen.setBackground(Color.blue);
        }
    	{
    		KORundeHomescreen = new JPanel();
        	getContentPane().add(KORundeHomescreen);
        	KORundeHomescreen.setLayout(null);
        	KORundeHomescreen.setBounds(0, 0, 1440, 874);
        	KORundeHomescreen.setVisible(false);
        	KORundeHomescreen.setOpaque(true);
        	KORundeHomescreen.setBackground(Color.yellow);
        }
    }
    
    public void jBtnLigenPressed(int index) {
    	
		Homescreen.setVisible(false);
		jBtnZurueck.setVisible(true);
		
		
    	if (index < anzahl_ligen) {
    		// The pressed button leads to a league
    		isCurrentlyALeague = true;
    		LigaHomescreen.setVisible(true);
    		LigaHomescreen.add(jBtnZurueck);
    		
        	aktuelle_liga = index;
//    		ligen[aktuelle_liga].laden(ligen[aktuelle_liga].saisons.length - 1);
    		
    		// befuellt die ComboBox mit den verfügbaren Saisons
    		String[] hilfsarray = new String[ligen[aktuelle_liga].saisons.length];
            for (int i = 0; i < ligen[aktuelle_liga].saisons.length; i++) {
                hilfsarray[i] = "" + ligen[aktuelle_liga].saisons[i];
            }
            ComboBoxModel jCBSaisonauswahlModel = new DefaultComboBoxModel(hilfsarray);
            jCBSaisonauswahl.setModel(jCBSaisonauswahlModel);
    		jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
    		
    		aktuellerSpieltag = ligen[aktuelle_liga].getSpieltag();
    		aktuelleTabelle = ligen[aktuelle_liga].getTable();
//    		aktuelleUebersicht = ligen[aktuelle_liga].getMannschaften()[0].getOverview(); // damit es nicht null ist
		} else {
			// The pressed button leads to a tournament
    		isCurrentlyALeague = false;
			TurnierHomescreen.setVisible(true);
			TurnierHomescreen.add(jBtnZurueck);
			
			aktuelles_turnier = index - anzahl_ligen;
			turniere[aktuelles_turnier].laden();
		}
		
    }
    
    public void jBtnGruppenphaseActionPerformed() {
    	// TODO
    	isCurrentlyInGroupStage = true;
    	
    	TurnierHomescreen.setVisible(false);
    	GruppenphaseHomescreen.setVisible(true);
    	GruppenphaseHomescreen.add(jBtnZurueck);
    	
    	groupStageButtons = new JButton[turniere[aktuelles_turnier].getNumberOfGroups()];
    	
    	for (int i = 0; i < groupStageButtons.length; i++) {
    		final int x = i;
    		groupStageButtons[i] = new JButton();
			GruppenphaseHomescreen.add(groupStageButtons[i]);
			groupStageButtons[i].setBounds(520, 100 + i * (70 + 15), start_btnswidth, 70);
			groupStageButtons[i].setText("Gruppe " + (i + 1));
			groupStageButtons[i].setFocusable(false);
			groupStageButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	jBtnGruppePressed(x);
                }
            });
		}
    }
    
    public void jBtnKORundeActionPerformed() {
    	// TODO
    	isCurrentlyInGroupStage = false;
    	
    	TurnierHomescreen.setVisible(false);
    	KORundeHomescreen.setVisible(true);
    	KORundeHomescreen.add(jBtnZurueck);
    }
    
    public void jBtnGruppePressed(int index) {
    	aktuelle_gruppe = index;
    	
    	GruppenphaseHomescreen.setVisible(false);
    	LigaHomescreen.setVisible(true);
    	LigaHomescreen.add(jBtnZurueck);
    }
    
    private void jCBSaisonauswahlItemStateChanged(ItemEvent evt) {
    	if (evt.getStateChange() == ItemEvent.SELECTED) {
    		int index = jCBSaisonauswahl.getSelectedIndex();
            try {
            	ligen[aktuelle_liga].speichern();
            } catch (Exception e) {
            	
            }
            ligen[aktuelle_liga].laden(index);
            ligaspezifischesachenladen();
    	}
	}
    
    public void jCBDefStarttagItemStateChanged(ItemEvent evt) {
    	if (evt.getStateChange() == ItemEvent.SELECTED) {
    		// TODO default starttag ändern
    	}
	}
    
    private void ligaspezifischesachenladen() {
//        {
//            Tabelle = new Tabelle(ligen[aktuelle_liga]);
//            getContentPane().add(Tabelle);
//            Tabelle.setLocation((1440 - Tabelle.getSize().width) / 2, 50);
//            Tabelle.setVisible(false);
//        }
        {
        	uebersicht = new Uebersicht(this, ligen[aktuelle_liga]);
            getContentPane().add(uebersicht);
            uebersicht.setLocation((1440 - uebersicht.getSize().width) / 2, 5);
            uebersicht.setVisible(false);
        }
        {
        	optionen = new JPanel();
            getContentPane().add(optionen);
            optionen.setLayout(null);
            optionen.setBounds(470, 0, 500, 874);
            optionen.setBackground(new Color(255, 255, 0));
            optionen.setVisible(false);
            {
            	JButton correctname = new JButton();
            	optionen.add(correctname);
            	correctname.setBounds(20, 80, 210, 30);
            	correctname.setText("Namen verbessern");
            	correctname.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						teamsVerbessern();
					}
				});
            }
            {
            	JButton addteam = new JButton();
            	optionen.add(addteam);
            	addteam.setBounds(20, 120, 210, 30);
            	addteam.setText("Mannschaft hinzufügen");
            	addteam.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						mannschaftHinzufuegen();
					}
				});
            }
            {
            	JLabel defaultstarttag = new JLabel();
            	optionen.add(defaultstarttag);
            	defaultstarttag.setBounds(20, 50, 105, 20);
            	defaultstarttag.setText("Standard-Starttag:");
            	defaultstarttag.setToolTipText("An diesem Wochentag beginnt üblicherweise ein Spieltag.");
            }
            {
            	String[] wochentage = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
            	ComboBoxModel jCBDefStarttagModel = new DefaultComboBoxModel(wochentage);
            	JComboBox jCBDefStarttag = new JComboBox();
            	optionen.add(jCBDefStarttag);
    	        jCBDefStarttag.setModel(jCBDefStarttagModel);
    	        jCBDefStarttag.setBounds(130, 50, 100, 20);
    	        jCBDefStarttag.addItemListener(new ItemListener() {
    	            public void itemStateChanged(ItemEvent evt) {
    	                jCBDefStarttagItemStateChanged(evt);
    	            }
    	        });
            }
        }
    }
    

	@SuppressWarnings("unused")
	private static void testLigaAusgabeSpieltagHeimspielGegnerPosition() {
    	System.out.println("Um den Test auszuführen, müssen daten[][] und homeaway[] public sein!");
    	
        for (Mannschaft ms : ligen[aktuelle_liga].getMannschaften()) {
        	System.out.println("\nNeue Mannschaft: " + ms.getString(1));
        	for (int i = 0; i < ligen[aktuelle_liga].getAnzahlSpieltage(); i++) {
        		if (ligen[aktuelle_liga].spieltageEingetragen[i]) {
//        			System.out.println("Spieltag = " + i + "\t\tHeimspiel = " + ms.homeaway[i] + 
//            				"\t\tGegner = " + ms.daten[i][Mannschaft.OPPONENT] + "\t\tPosition auf dem Spielplan: " + ms.positiononplan[i]);
        		}
        	}
        }
    }
    
    @SuppressWarnings("unused")
	private static void testLigaAusgabeSpielplanUndErgebnisse() {
    	for (int i = 0; i < ligen[aktuelle_liga].getAnzahlSpieltage(); i++) {
        	if (ligen[aktuelle_liga].spieltageEingetragen[i]) {
        		System.out.println("\n\nNeuer Spieltag");
            	for (int j = 0; j < ligen[aktuelle_liga].getAnzahlSpiele(); j++) {
            		System.out.println(	ligen[aktuelle_liga].getMannschaften()[ligen[aktuelle_liga].spieltage[i][j][0] - 1].getName() + " " + 
            							ligen[aktuelle_liga].ergebnis[i][j][0] + ":" + 
            							ligen[aktuelle_liga].ergebnis[i][j][1] + " " + 
            							ligen[aktuelle_liga].getMannschaften()[ligen[aktuelle_liga].spieltage[i][j][1] - 1].getName());
              	}
        	}
        }
    }
    
    // Action- und ItemListener
    public void jBtnSpieltageActionPerformed() {
    	if (isCurrentlyALeague) {
    		// der Button wurde in einer Liga aufgerufen
    		if (ligen[aktuelle_liga].getAnzahlTeams() > 1) {
        		aktuellerSpieltag = ligen[aktuelle_liga].getSpieltag();
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
    		if(turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getNumberOfTeams() > 1) {
    			aktuellerSpieltag = turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getSpieltag();
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
    		
    		aktuelleTabelle = ligen[aktuelle_liga].getTable();
    		if (ligen[aktuelle_liga].getAnzahlTeams() <= 0) {
        		JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann keine Tabelle angezeigt werden.");
        		return;
        	}
    		
    	} else {
    		// der Button wurde in einem Turnier aufgerufen
    		aktuelleTabelle = turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getTabelle();
    		
    		if (turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getNumberOfTeams() <= 0) {
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
    	if (!isCurrentlyALeague) {
    		return; // TODO
    	}
    	
    	isCurrentlyInMatchdayView = false;
    	
    	LigaHomescreen.setVisible(false);
    	optionen.add(jBtnZurueck);
        optionen.setVisible(true);
    }

    /**
     * To be replaced
     * @param name
     */
    public void uebersichtAnzeigen(String name) {
        aktuelleTabelle.setVisible(false);
        
    	int index = ligen[aktuelle_liga].getIndexOfMannschaft(name);
    	uebersicht.add(jBtnZurueck);
    	uebersicht.setVisible(false);
    	uebersicht.setMannschaft(index);
    	uebersicht.labelsbefuellen();
    	uebersicht.setVisible(true);
    }
    
    public void uebersichtAnzeigen(Uebersicht uebersicht) {
//    	aktuelleUebersicht = uebersicht;
//    	JOptionPane.showMessageDialog(null, "Die angezeigte Übersicht hat die Maße: " + aktuelleUebersicht.getSize().width + "x" + aktuelleUebersicht.getSize().height);
//    	getContentPane().add(aktuelleUebersicht);
//    	
//    	Component[] allcomps = getContentPane().getComponents();
//    	for (int i = 0; i < allcomps.length; i++) {
//			if (allcomps[i].isVisible()) {
//				JOptionPane.showMessageDialog(null, allcomps[i].toString() + "\nis visible!");
//			} else {
//				System.out.println(allcomps[i].toString() + "\nis not visible!");
//			}
//		}
    }
    
    public void teamsVerbessern() {
    	if (isCurrentlyALeague) {
    		for (int i = 0; i < ligen[aktuelle_liga].getAnzahlTeams(); i++) {
        		ligen[aktuelle_liga].getMannschaften()[i].setName(JOptionPane.showInputDialog("Korrekter Name für Mannschaft \""
        															+ ligen[aktuelle_liga].getMannschaften()[i].getName() + "\""));
            }
    	} else {
    		for (int i = 0; i < ligen[aktuelle_liga].getAnzahlTeams(); i++) {
        		turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getMannschaften()[i].setName(JOptionPane.showInputDialog("Korrekter Name für Mannschaft \""
        															+ turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getMannschaften()[i].getName() + "\""));
            }
    	}
    }
    
    public void mannschaftHinzufuegen() {
    	if (!isCurrentlyALeague) {
    		JOptionPane.showMessageDialog(null, "Das Hinzufügen von Mannschaften ist aktuell bei Turnieren nicht möglich.");
    		return;
    	}
    	
    	if (JOptionPane.showConfirmDialog(null, "Das Hinzufügen einer Mannschaft löscht alle zuvor eingegebenen Daten. \nMöchtest du trotzdem fortfahren?") == JOptionPane.NO_OPTION) {
    		return;
    	}
    	
    	String name = JOptionPane.showInputDialog("Du willst also eine neue Mannschaft hinzufuegen! Verrätst du mir den Namen?");
    	int cancel = 1;
    	if (name == null || name.isEmpty()) {
    		cancel = JOptionPane.showConfirmDialog(null, "Du hast nichts eingegeben. Möchtest du abbrechen?", "Fehler", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    		while (cancel != JOptionPane.YES_OPTION) {
    			name = JOptionPane.showInputDialog("Bitte gib den Namen der Mannschaft ein!");
        		cancel = JOptionPane.showConfirmDialog(null, "Du hast wieder nichts eingegeben. Möchtest du wirklich nicht abbrechen?", "Fehler", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        	}
    	}
    	
    	if (name != null && !name.isEmpty()) {
        	ligen[aktuelle_liga].addNewTeam(name, true);
        	
        	// Optionen-Panel ausblenden
        	optionen.setVisible(false);
        	
        	// Tabelle, Uebersicht, Mannschaftenbuttons, Spieltage ... erneuern
        	ligaspezifischesachenladen();
		}
    }
    
    public void jBtnBeendenActionPerformed() {
        saveConfiguration();
        System.exit(0);
    }
    
    public void jBtnZurueckActionPerformed() {
    	if (isCurrentlyALeague) {
    		// is a league
            if (aktuellerSpieltag.isVisible()) {
            	if (aktuellerSpieltag.edited_matchday == -1) {
                	ligen[aktuelle_liga].ergebnisse_sichern();
                } else {
                    if (aktuellerSpieltag.fertigActionPerformed() != 0){
                    	JOptionPane.showMessageDialog(null, "huhu");
                    	return;
                    }
                }
            }
            
            if (LigaHomescreen.isVisible()) {
            	ligen[aktuelle_liga].speichern();
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
            	System.out.println("\n\nTest!!\n\n");
                LigaHomescreen.setVisible(true);
            }
		} else {
			// is a tournament
			if (aktuelle_gruppe != -1 && isCurrentlyInMatchdayView) {
				Spieltag spieltag = turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getSpieltag();
				spieltag.setVisible(false);
				
				if (spieltag.edited_matchday == -1) {
                	turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].ergebnisseSichern();
                } else {
                    if (spieltag.fertigActionPerformed() != 0){
                    	JOptionPane.showMessageDialog(null, "huhu");
                    	return;
                    }
                }
			}
			
			if (TurnierHomescreen.isVisible()) {
				turniere[aktuelles_turnier].speichern();
				TurnierHomescreen.setVisible(false);
				Homescreen.setVisible(true);
			} else if (GruppenphaseHomescreen.isVisible() || KORundeHomescreen.isVisible()) {
				GruppenphaseHomescreen.setVisible(false);
				KORundeHomescreen.setVisible(false);
				TurnierHomescreen.add(jBtnZurueck);
				TurnierHomescreen.setVisible(true);
			} else if (LigaHomescreen.isVisible()) {
				LigaHomescreen.setVisible(false);
				if (isCurrentlyInGroupStage) {
					GruppenphaseHomescreen.setVisible(true);
					GruppenphaseHomescreen.add(jBtnZurueck);
				} else {
					KORundeHomescreen.setVisible(true);
					KORundeHomescreen.add(jBtnZurueck);
				}
				
				aktuelle_gruppe = -1;
			} else {
				turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getSpieltag().setVisible(false);
				turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getTabelle().setVisible(false);
				
				LigaHomescreen.add(jBtnZurueck);
				LigaHomescreen.setVisible(true);
			}
		}
    }
    
    
    
    private void loadConfiguration() {
        this.configurationFromFile = ausDatei(config);
        
        int counter = 0;
        
        anzahl_ligen = Integer.parseInt(configurationFromFile[counter]);
        ligen = new Liga[anzahl_ligen];
        counter++;
        
        for (int i = 0; i < anzahl_ligen; i++) {
        	ligen[i] = new Liga(i, this, (String) configurationFromFile[counter]);
            counter++;
        }
        
        anzahl_turniere = Integer.parseInt((String) configurationFromFile[counter]);
        turniere = new Turnier[anzahl_turniere];
        counter++;
        
        for (int i = 0; i < anzahl_turniere; i++) {
        	turniere[i] = new Turnier(this, i, (String) configurationFromFile[counter]);
            counter++;
        }
        
        jBtnsLigen = new JButton[anzahl_ligen + anzahl_turniere];
    }
    
    private void saveConfiguration() {
    	this.configurationFromFile = new String[anzahl_ligen + anzahl_turniere + 2];
    	
    	int counter = 0;
    	
		configurationFromFile[counter] = "" + anzahl_ligen;
		counter++;
		
    	for (int i = 0; i < ligen.length; i++) {
    		configurationFromFile[counter] = ligen[i].toString();
    		counter++;
    	}
    	
		configurationFromFile[counter] = "" + anzahl_turniere;
		counter++;
		
    	for (int i = 0; i < turniere.length; i++) {
    		configurationFromFile[counter] = turniere[i].toString();
    		counter++;
    	}
    	
    	
    	inDatei(config, configurationFromFile);
    }
    
}