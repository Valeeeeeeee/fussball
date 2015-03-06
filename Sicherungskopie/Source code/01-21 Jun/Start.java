
import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 

import javax.swing.*; 

@SuppressWarnings({"rawtypes","unchecked"}) 
public class Start extends JFrame {
    private static final long serialVersionUID = -3201913070768333811L;
    
    String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
    
    String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
    
    String config = workspaceMAC + File.separator + "config.txt";
    DefaultListModel jListConfigModel = new DefaultListModel();
    
    
    
    int anzahl_ligen;
    int anzahl_turniere;
    
	static Liga[] ligen;
	static int aktuelle_liga = -1;
	
	static Turnier[] turniere;
	static int aktuelles_turnier = -1;
	int aktuelle_gruppe = -1;
	
	private boolean isCurrentlyALeague = false;
    
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
    
    public static Spieltag Spieltage;
    public static Tabelle Tabelle;
    public static Uebersicht uebersicht;
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
        load_config();
        initGUI();
//        JOptionPane.showMessageDialog(null, "Die Klasse Mannschaft erweitern", "", JOptionPane.ERROR_MESSAGE);
//        JOptionPane.showMessageDialog(null, "Für die Bounds ein Rectangle[win/mac][alle lbls, cbs, ...] mit den Bounds-Werten. \n"
//        									+ "Zum Ändern int macorwin", "Bounds", JOptionPane.INFORMATION_MESSAGE);
        
//        jBtnLigenPressed(1); // TODO
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
		
		
    	if (index <= 2) {
    		// The pressed button leads to a league
    		isCurrentlyALeague = true;
    		LigaHomescreen.setVisible(true);
    		LigaHomescreen.add(jBtnZurueck);
    		
        	aktuelle_liga = index;
    		ligen[aktuelle_liga].laden(ligen[aktuelle_liga].saisons.length - 1);
    		
    		// befuellt die ComboBox mit den verfügbaren Saisons
    		String[] hilfsarray = new String[ligen[aktuelle_liga].saisons.length];
            for (int i = 0; i < ligen[aktuelle_liga].saisons.length; i++) {
                hilfsarray[i] = "" + ligen[aktuelle_liga].saisons[i];
            }
            ComboBoxModel jCBSaisonauswahlModel = new DefaultComboBoxModel(hilfsarray);
            jCBSaisonauswahl.setModel(jCBSaisonauswahlModel);
    		jCBSaisonauswahl.setSelectedIndex(jCBSaisonauswahl.getModel().getSize() - 1);
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
                	jBtnGruppeGedrueckt(x);
                }
            });
		}
    }
    
    public void jBtnKORundeActionPerformed() {
    	// TODO
    	TurnierHomescreen.setVisible(false);
    	KORundeHomescreen.setVisible(true);
    	KORundeHomescreen.add(jBtnZurueck);
    }
    
    public void jBtnGruppeGedrueckt(int index) {
    	aktuelle_gruppe = index;
    	
    	GruppenphaseHomescreen.setVisible(false);
    	LigaHomescreen.setVisible(true);
    	
//    	jBtnSpieltage.setVisible(true);
//    	jBtnTabelle.setVisible(true);
//    	jBtnOptionen.setVisible(true);
    }
    
    private void jCBSaisonauswahlItemStateChanged(ItemEvent evt) {
    	if (evt.getStateChange() == ItemEvent.SELECTED) {
    		int index = jCBSaisonauswahl.getSelectedIndex();
            ligen[aktuelle_liga].speichern();
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
    	{
            Spieltage = new Spieltag(ligen[aktuelle_liga]);
            getContentPane().add(Spieltage);
            Spieltage.setLocation((1440 - Spieltage.getSize().width) / 2, (874 - 28 - Spieltage.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            Spieltage.setVisible(false);
        }
        {
            Tabelle = new Tabelle(ligen[aktuelle_liga]);
            getContentPane().add(Tabelle);
            Tabelle.setLocation((1440 - Tabelle.getSize().width) / 2, 50);
            Tabelle.setVisible(false);
        }
        {
        	uebersicht = new Uebersicht(ligen[aktuelle_liga]);
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
						teams_verbessern();
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
						mannschaft_hinzufuegen();
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
	private static void testAusgabeSpieltagHeimspielGegnerPosition() {
    	System.out.println("Um den Test auszuführen, müssen daten[][] und homeaway[] public sein!");
    	
        for (Mannschaft ms : ligen[aktuelle_liga].getMannschaften()) {
        	System.out.println("\nNeue Mannschaft: " + ms.getString(1));
        	for (int i = 0; i < ligen[aktuelle_liga].getAnzahlSpieltage(); i++) {
        		if (ligen[aktuelle_liga].spieltage_eingetragen[i]) {
//        			System.out.println("Spieltag = " + i + "\t\tHeimspiel = " + ms.homeaway[i] + 
//            				"\t\tGegner = " + ms.daten[i][Mannschaft.OPPONENT] + "\t\tPosition auf dem Spielplan: " + ms.positiononplan[i]);
        		}
        	}
        }
    }
    
    @SuppressWarnings("unused")
	private static void testAusgabeSpielplanUndErgebnisse() {
    	for (int i = 0; i < ligen[aktuelle_liga].getAnzahlSpieltage(); i++) {
        	if (ligen[aktuelle_liga].spieltage_eingetragen[i]) {
        		System.out.println("\n\nNeuer Spieltag");
            	for (int j = 0; j < ligen[aktuelle_liga].getAnzahlSpiele(); j++) {
            		System.out.println(	getMannschaftAt(ligen[aktuelle_liga].spieltage[i][j][0]) + " " + 
            							ligen[aktuelle_liga].ergebnis[i][j][0] + ":" + 
            							ligen[aktuelle_liga].ergebnis[i][j][1] + " " + 
            							getMannschaftAt(ligen[aktuelle_liga].spieltage[i][j][1]));
              	}
        	}
        }
    }
    
    public static String getMannschaftAt(int index) {
        if(index <= 0) 													return "n.def.";
        else if (index <= ligen[aktuelle_liga].getMannschaften().length)	return ligen[aktuelle_liga].getMannschaften()[index - 1].getName();
        else																return null;
    }
    
    public static int getIndexOfMannschaft(String name) {
    	for (Mannschaft ms : ligen[aktuelle_liga].getMannschaften()) {
    		if (ms.getName().equals(name)) {
    			return ms.getId();
    		}
    	}
    	return -1;
    }
    
    // Action- und ItemListener
    public void jBtnSpieltageActionPerformed() {
    	if (isCurrentlyALeague) {
    		// der Button wurde in einer Liga aufgerufen
    		if (ligen[aktuelle_liga].getAnzahlTeams() > 1) {
        		LigaHomescreen.setVisible(false);
                jBtnZurueck.setVisible(true);
                Spieltage.add(jBtnZurueck);
                Spieltage.setVisible(true);
                Spieltage.spieltag_anzeigen();
        	} else {
        		JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann kein Spieltag angezeigt werden.");
        	}
    	} else {
    		// TODO Spieltage: der Button wurde in einem Turnier aufgerufen
    		if(turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getNumberOfTeams() > 1) {
    			LigaHomescreen.setVisible(false);
    			Spieltag spieltag = turniere[aktuelles_turnier].getGruppen()[aktuelle_gruppe].getSpieltag();
    			getContentPane().add(spieltag);
    			spieltag.add(jBtnZurueck);
    			spieltag.setVisible(true);
    			spieltag.spieltag_anzeigen();
    		} else {
        		JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann kein Spieltag angezeigt werden.");
        	}
    		
    	}
    	
    	
    }
    
    public void jBtnTabelleActionPerformed() {
    	if (isCurrentlyALeague) {
    		// der Button wurde in einer Liga aufgerufen
    		if (Tabelle.aktualisieren(ligen[aktuelle_liga])) {
            	LigaHomescreen.setVisible(false);
                jBtnZurueck.setVisible(true);
                Tabelle.add(jBtnZurueck);
                Tabelle.setVisible(true);
        	}
    	} else {
    		// TODO Tabelle: der Button wurde in einem Turnier aufgerufen
    		
    		
    	}
    }
    
    public static void uebersicht_anzeigen(String name) {
        Tabelle.setVisible(false);
        
    	int index = getIndexOfMannschaft(name);
    	uebersicht.add(jBtnZurueck);
    	uebersicht.setVisible(false);
    	uebersicht.setMannschaft(ligen[aktuelle_liga], index);
    	uebersicht.labelsbefuellen();
        uebersicht.setVisible(true);
    }
    
    public void jBtnOptionenActionPerformed() {
    	LigaHomescreen.setVisible(false);
    	optionen.add(jBtnZurueck);
        optionen.setVisible(true);
    }
    
    public void teams_verbessern() {
    	for (int i = 0; i < ligen[aktuelle_liga].getAnzahlTeams(); i++) {
    		ligen[aktuelle_liga].getMannschaften()[i].setName(JOptionPane.showInputDialog("Korrekter Name für Mannschaft \"" + ligen[aktuelle_liga].getMannschaften()[i].getName() + "\""));
        }
    }
    
    public void mannschaft_hinzufuegen() {
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
        write_config();
        System.exit(0);
    }
    
    public void jBtnZurueckActionPerformed() {
    	if (isCurrentlyALeague) {
            if (Spieltage.isVisible()) {
                if (Spieltage.edited_matchday == -1) {
                	ligen[aktuelle_liga].ergebnisse_sichern();
                } else {
                    if (Spieltage.fertigActionPerformed() != 0){
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
            	Tabelle.add(jBtnZurueck);
            	uebersicht.setVisible(false);
                Tabelle.setVisible(true);
            } else {
            	LigaHomescreen.add(jBtnZurueck);
                Spieltage.setVisible(false);
                Tabelle.setVisible(false);
            	optionen.setVisible(false);
            	System.out.println("\n\nTest!!\n\n");
                LigaHomescreen.setVisible(true);
            }
		} else {
			// TODO zurück:  is a tournament
        	if (TurnierHomescreen.isVisible()) {
				turniere[aktuelles_turnier].speichern();
				TurnierHomescreen.setVisible(false);
				Homescreen.setVisible(true);
			} else {
				GruppenphaseHomescreen.setVisible(false);
				KORundeHomescreen.setVisible(false);
				TurnierHomescreen.add(jBtnZurueck);
				TurnierHomescreen.setVisible(true);
			}
		}
        
    }
    
    // Aus und in Dateien lesen/schreiben 
    public void aus_datei(String dateiname, DefaultListModel model) {
        try {
            File datei = new File(dateiname);
            BufferedReader in = null;
            if (!datei.exists()) {
                datei.createNewFile();
                System.out.println(" >>> File did not exist but was created! --> " + datei.getAbsolutePath());
            } else {
                String element;
                try {
                    in = new BufferedReader(new FileReader(datei));
                    model.clear();
                    while ((element = in.readLine()) != null) {
                        model.addElement(element);
                    }
                } catch (Exception e) {
                    System.out.println("Fehler beim Laden!");
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Programmfehler!");
            e.printStackTrace();
        }
    } 
    
    private void in_datei(String dateiname, DefaultListModel model) {
        try {
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(dateiname));
                for (int i = 0; i < model.getSize(); i++) {
                    out.write(model.get(i).toString());
                    out.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(" >> in_datei >> Exception caught at file: " + dateiname + "\n");
        }
    }
    
    private void load_config() {
        aus_datei(config, jListConfigModel);
        
        int counter = 0;
        
        anzahl_ligen = Integer.parseInt((String) jListConfigModel.get(counter));
        ligen = new Liga[anzahl_ligen];
        counter++;
        
        for (int i = 0; i < anzahl_ligen; i++) {
        	ligen[i] = new Liga(i, (String) jListConfigModel.get(counter));
            counter++;
        }
        
        anzahl_turniere = Integer.parseInt((String) jListConfigModel.get(counter));
        turniere = new Turnier[anzahl_turniere];
        counter++;
        
        for (int i = 0; i < anzahl_turniere; i++) {
        	turniere[i] = new Turnier(i, (String) jListConfigModel.get(counter));
            counter++;
        }
        
        jBtnsLigen = new JButton[anzahl_ligen + anzahl_turniere];
    }
    
    private void write_config() {
    	jListConfigModel.clear();
    	
		jListConfigModel.addElement(anzahl_ligen);
    	for(int i = 0; i < ligen.length; i++) {
    		jListConfigModel.addElement(ligen[i].toString());
    	}
    	
		jListConfigModel.addElement(anzahl_turniere);
    	for(int i = 0; i < turniere.length; i++) {
    		jListConfigModel.addElement(turniere[i].toString());
    	}
    	
    	
    	in_datei(config, jListConfigModel);
    }
    
}