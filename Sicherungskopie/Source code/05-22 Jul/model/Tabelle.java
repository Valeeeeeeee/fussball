package model;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Tabelle extends JPanel {
    private static final long serialVersionUID = 2308780445852600421L;
    
    private Start start;
    private Gruppe gruppe;
    private Liga liga;
    
    private boolean belongsToALeague = false;
    
    int ANZAHL_TEAMS;
    
    // for Liga
    int ANZAHL_CL;
    int ANZAHL_CLQ;
    int ANZAHL_EL;
    int ANZAHL_REL;
    int ANZAHL_ABS;
    
    // for Turnier
    int ANZAHL_KORUNDE;
    int ANZAHL_ZWISCHENRUNDE;
    int ANZAHL_AUSGESCHIEDEN;
    
    String[] titelleist = {"Pl.", "Name", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
    
    private JLabel[] titelleiste;
    private JLabel[][] tabelle;
    private int startx = 10;
    private int starty = 80;
    private int[] widthes = {20, 180, 20, 20, 20, 20, 25, 25, 25, 25};
    private int height = 15;
    private int[] gapx = {5, 5, 5, 0, 0, 5, 0, 5, 5, 0};
    private int gapy = 15;
    
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
    	
    	this.ANZAHL_TEAMS = liga.getAnzahlTeams();
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
    	for (int i = 0; i < this.getHeight(); i++) {
			g.setColor(new Color(0 + (64 * i / (this.getHeight())), 0 + (128 * i / (this.getHeight())), 255));
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
        	// (optional) Zwischenrunde des nächsttieferen Wettbewerbs zB 3. Platz der CL-Gruppen steigt in die EL ab
        	g.setColor(new Color(255, 128, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_AUSGESCHIEDEN - ANZAHL_ZWISCHENRUNDE) * hheight, sumofwidthes, ANZAHL_ZWISCHENRUNDE * hheight);
        	// Ausgeschieden
        	g.setColor(new Color(255, 0, 0));
        	g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_AUSGESCHIEDEN) * hheight, sumofwidthes, ANZAHL_AUSGESCHIEDEN * hheight);
    	}
    }
    
    public void labelsbefuellen() {
    	int next_place = 0; // index in der Tabelle
    	int last_indexed_place = 0;
    	
    	Mannschaft[] mannschaften;
    	if (belongsToALeague)	mannschaften = liga.getMannschaften();
    	else					mannschaften = gruppe.getMannschaften();
    	
    	for (int i = 0; i < tabelle.length; i++) {
    		for (Mannschaft ms : mannschaften) {
    			if (ms.get(0) == i) {
    				for (int j = 0; j < tabelle[i].length; j++) {
    	                tabelle[next_place][j].setText(ms.getString(j));
    	            }
    				if (next_place >= 1) {
    					if (tabelle[next_place][0].getText().equals(tabelle[last_indexed_place][0].getText())) {
    						tabelle[next_place][0].setText("");
    					} else {
    						last_indexed_place = next_place;
    					}
    				}
    				next_place++;
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
    
}
