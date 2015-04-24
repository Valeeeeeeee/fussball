package model;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import static util.Utilities.*;

public class Tabelle extends JPanel {
    private static final long serialVersionUID = 2308780445852600421L;
    
    private Start start;
    private Gruppe gruppe;
    private Liga liga;
    
    private boolean belongsToALeague = false;
    
    private int ANZAHL_TEAMS;
    
    // for Liga
    private int ANZAHL_CL;
    private int ANZAHL_CLQ;
    private int ANZAHL_EL;
    private int ANZAHL_REL;
    private int ANZAHL_ABS;
    
    // TODO ComboBox for tables until matchday XX (from matchday YY)
    // TODO GUI for point deduction
    
    // for Turnier
    private int ANZAHL_KORUNDE;
    private int ANZAHL_ZWISCHENRUNDE;
    private int ANZAHL_AUSGESCHIEDEN;
    
    private String[] titelleist = {"Pl.", "Name", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
    
    private JLabel[] titelleiste;
    private JLabel[][] tabelle;
    private int startx = 10;
    private int starty = 80;
    private int[] widthes = {20, 180, 20, 20, 20, 20, 25, 25, 25, 25};
    private int height = 15;
    private int[] gapx = {5, 5, 5, 0, 0, 5, 0, 5, 5, 0};
    private int gapy = 15;
    
    private Rectangle REC_SAVETABLE = new Rectangle(250, 10, 150, 30);
    
    private JButton tabelleSichern;
    
    public Tabelle(Start start, Gruppe gruppe) {
    	super();
    	this.start = start;
    	this.gruppe = gruppe;
    	this.belongsToALeague = false;
    	
    	this.ANZAHL_TEAMS = gruppe.getNumberOfTeams();
    	this.ANZAHL_KORUNDE = 2;
    	this.ANZAHL_ZWISCHENRUNDE = 0;
    	this.ANZAHL_AUSGESCHIEDEN = 2;
    	
    	initGUI();
    }
    
    public Tabelle(Start start, Liga liga) {
    	super();
    	this.start = start;
    	this.liga = liga;
    	this.belongsToALeague = true;
    	
    	this.ANZAHL_TEAMS = liga.getNumberOfTeams();
    	this.ANZAHL_CL = liga.getAnzahlCL();
    	this.ANZAHL_CLQ = liga.getAnzahlCLQ();
    	this.ANZAHL_EL = liga.getAnzahlEL();
    	this.ANZAHL_REL = liga.getAnzahlREL();
    	this.ANZAHL_ABS = liga.getAnzahlABS();
    	
    	initGUI();
    }
    
    public void initGUI() { 
        try { 
            this.setLayout(null);
            
            tabelle = new JLabel[ANZAHL_TEAMS][titelleist.length];
            titelleiste = new JLabel[titelleist.length];
            
            
        	int sumofwidthes = 0;
            
        	for (int j = 0; j < titelleiste.length; j++) {
            	titelleiste[j] = new JLabel();
                this.add(titelleiste[j]);
                if (j == 1) {
                	titelleiste[j].setHorizontalAlignment(SwingConstants.LEFT);
                	titelleiste[j].setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                	titelleiste[j].setHorizontalAlignment(SwingConstants.CENTER);
                }
                titelleiste[j].setBounds(startx + sumofwidthes, starty - (height + gapy), widthes[j], height);
                titelleiste[j].setText(titelleist[j]);
                sumofwidthes += widthes[j] + gapx[j];
            }
        	
            for (int i = 0; i < tabelle.length; i++) {
            	sumofwidthes = 0;
                for (int j = 0; j < tabelle[i].length; j++) {
                    tabelle[i][j] = new JLabel();
                    this.add(tabelle[i][j]);
                    if (j == 1) {
                    	final int x = i;
                    	tabelle[i][j].setHorizontalAlignment(SwingConstants.LEFT);
                    	tabelle[i][j].setCursor(new Cursor(Cursor.HAND_CURSOR));
                        tabelle[i][j].addMouseListener(new MouseAdapter() {
                        	public void mouseClicked(MouseEvent evt) {
                                start.uebersichtAnzeigen(tabelle[x][1].getText());
                            }
						});
                    } else {
                    	tabelle[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    tabelle[i][j].setBounds(startx + sumofwidthes, starty + i * (height + gapy), widthes[j], height);
//                    tabelle[i][j].setOpaque(true);
                    sumofwidthes += widthes[j] + gapx[j];
                }
            }
            
            {
            	tabelleSichern = new JButton();
            	this.add(tabelleSichern);
            	tabelleSichern.setBounds(REC_SAVETABLE);
            	tabelleSichern.setText("Tabelle sichern");
            	tabelleSichern.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tabelleSichern();
					}
				});
			}
            
            this.setSize(sumofwidthes + 20, starty - (gapy / 2) + ANZAHL_TEAMS * (height + gapy) + 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	// Hintergrund
    	g.setColor(new Color(212, 212, 212));
    	g.fillRect(0, 0, getWidth(), getHeight());
    	
    	Color colorone = new Color(0, 223, 255);
    	Color colortwo = new Color(79, 127, 255);
    	
    	int diffRed = colortwo.getRed() - colorone.getRed();
    	int diffGreen = colortwo.getGreen() - colorone.getGreen();
    	int diffBlue = colortwo.getBlue() - colorone.getBlue();
    	
    	for (int i = 0; i < this.getHeight(); i++) {
			g.setColor(new Color(colorone.getRed() + (diffRed * i / (this.getHeight())), colorone.getGreen() + (diffGreen * i / (this.getHeight())), colorone.getBlue() + (diffBlue * i / (this.getHeight()))));
			g.drawLine(0, i, this.getWidth() - 1, i);
		}
    	
    	int hstartx = 10;
    	int hstarty = starty - (gapy / 2);
    	int hheight = height + gapy;
    	int sumofwidthes = 0;
    	for (int j = 0; j < widthes.length; j++) {
    		sumofwidthes += widthes[j] + gapx[j];
    	}
    	
    	if (belongsToALeague) {
    		// Champions League
        	g.setColor(new Color(0, 200, 0));
        	g.fillRect(hstartx, hstarty, sumofwidthes, ANZAHL_CL * hheight);
        	// CL Qualifikation
        	g.setColor(new Color(128, 255, 0));
        	g.fillRect(hstartx, hstarty + ANZAHL_CL * hheight, sumofwidthes, ANZAHL_CLQ * hheight);
        	// Europa League
        	g.setColor(new Color(255, 255, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_CL + ANZAHL_CLQ) * hheight, sumofwidthes, ANZAHL_EL * hheight);
        	// Relegation
        	g.setColor(new Color(255, 128, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_ABS - ANZAHL_REL) * hheight, sumofwidthes, ANZAHL_REL * hheight);
        	// Absteiger
        	g.setColor(new Color(255, 0, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_ABS) * hheight, sumofwidthes, ANZAHL_ABS * hheight);
    	} else {
    		// KO Runde
        	g.setColor(new Color(0, 200, 0));
        	g.fillRect(hstartx, hstarty, sumofwidthes, ANZAHL_KORUNDE * hheight);
        	// (optional) Zwischenrunde des naechsttieferen Wettbewerbs zB 3. Platz der CL-Gruppen steigt in die EL ab
        	g.setColor(new Color(255, 128, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_AUSGESCHIEDEN - ANZAHL_ZWISCHENRUNDE) * hheight, sumofwidthes, ANZAHL_ZWISCHENRUNDE * hheight);
        	// Ausgeschieden
        	g.setColor(new Color(255, 0, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_AUSGESCHIEDEN) * hheight, sumofwidthes, ANZAHL_AUSGESCHIEDEN * hheight);
    	}
    }
    
    public void labelsbefuellen() {
    	int nextPlace = 0; // index in der Tabelle
    	int lastIndexedPlace = 0;
    	
    	Mannschaft[] mannschaften;
    	if (belongsToALeague)	mannschaften = liga.getMannschaften();
    	else					mannschaften = gruppe.getMannschaften();
    	
    	for (int i = 0; i < tabelle.length; i++) {
    		for (Mannschaft ms : mannschaften) {
    			if (ms.get(0) == i) {
    				for (int j = 0; j < tabelle[i].length; j++) {
    	                tabelle[nextPlace][j].setText(ms.getString(j));
    	            }
    				if (nextPlace >= 1) {
    					if (tabelle[nextPlace][0].getText().equals(tabelle[lastIndexedPlace][0].getText())) {
    						tabelle[nextPlace][0].setText("");
    					} else {
    						lastIndexedPlace = nextPlace;
    					}
    				}
    				nextPlace++;
    			}
    		}
        }
    }
    
    public void aktualisieren() {
    	if (belongsToALeague) {
    		// is a league
    		for (Mannschaft ms : liga.getMannschaften()) {
            	ms.set(0, 0);
            	for (Mannschaft ms2 : liga.getMannschaften()) {
            		int besser = ms.compareWith(ms2);
            		if (besser == 2) {
            			ms.set(0, ms.get(0) + 1);
            		}
            	}
            }
    	} else {
    		// is a tournament
    		for (Mannschaft ms : gruppe.getMannschaften()) {
            	ms.set(0, 0);
            	for (Mannschaft ms2 : gruppe.getMannschaften()) {
            		int besser = ms.compareWith(ms2);
            		if (besser == 2) {
            			ms.set(0, ms.get(0) + 1);
            		}
            	}
            }
    	}
        
        this.labelsbefuellen();
    }
    
    public void tabelleSichern() {
    	String[] order = new String[tabelle.length];
    	String dateiname = start.workspace;
    	log("There are " + order.length + " teams.");
    	for (int i = 0; i < order.length; i++) {
			if (belongsToALeague)	order[i] = liga.getTeamwithName(tabelle[i][1].getText()).toString();
			else					order[i] = gruppe.getTeamwithName(tabelle[i][1].getText()).toString();
			log((i + 1) + ". " + order[i]);
		}
    	if (belongsToALeague) {
			dateiname += File.separator + liga.getName() + File.separator + "NeueReihenfolge.txt";
    	} else {
    		dateiname += File.separator + gruppe.getTournamentName() + File.separator + "Gruppe " + (gruppe.getID() + 1) + File.separator + "NeueReihenfolge.txt";
    	}
    	log(dateiname);
    	inDatei(dateiname, order);
    }
    
}
