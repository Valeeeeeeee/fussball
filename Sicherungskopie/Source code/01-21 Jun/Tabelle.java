
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Tabelle extends JPanel {
    private static final long serialVersionUID = 2308780445852600421L;
    
    int ANZAHL_TEAMS;
    int ANZAHL_CL;
    int ANZAHL_CLQ;
    int ANZAHL_EL;
    int ANZAHL_REL;
    int ANZAHL_ABS;
    
    String[] titelleist = {"Pl.", "Name", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
    
    private JLabel[] titelleiste;
    private JLabel[][] tabelle;
    private int startx = 10;
    private int starty = 80;
    private int[] widthes = {20, 180, 20, 20, 20, 20, 25, 25, 25, 25};
    private int height = 15;
    private int[] gapx = {5, 5, 5, 0, 0, 5, 0, 5, 5, 0};
    private int gapy = 15;

    public Tabelle(int teams, int cl, int clq, int el, int rel, int abs) {
        super(); 
    	ANZAHL_TEAMS = teams;
        ANZAHL_CL = cl;
        ANZAHL_CLQ = clq;
        ANZAHL_EL = el;
        ANZAHL_REL = rel;
        ANZAHL_ABS = abs;
        initGUI(); 
    }
    
    public Tabelle(Liga lg) {
    	super();
    	
    	ANZAHL_TEAMS = lg.getAnzahlTeams();
        ANZAHL_CL = lg.getAnzahlCL();
        ANZAHL_CLQ = lg.getAnzahlCLQ();
        ANZAHL_EL = lg.getAnzahlEL();
        ANZAHL_REL = lg.getAnzahlREL();
        ANZAHL_ABS = lg.getAnzahlABS();
    	
    	initGUI();
    }
    
    public void initGUI() { 
        try { 
            this.setLayout(null);
            
            tabelle = new JLabel[ANZAHL_TEAMS][10];
            titelleiste = new JLabel[10];

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
                                Start.uebersicht_anzeigen(tabelle[x][1].getText());
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
    	
    	int hstartx = 10;
    	int hstarty = starty - (gapy / 2);
    	int hheight = height + gapy;
    	int sumofwidthes = 0;
    	for (int j = 0; j < widthes.length; j++) {
    		sumofwidthes += widthes[j] + gapx[j];
    	}
    	
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
    }
    
    public void labelsbefuellen() {
    	int next_place = 0; // index in der Tabelle
    	int last_indexed_place = 0;
    	for (int i = 0; i < tabelle.length; i++) {
    		for (Mannschaft ms : Start.ligen[Start.aktuelle_liga].getMannschaften()) {
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
    
    public boolean aktualisieren(Liga lg) {
    	if (lg.getAnzahlTeams() > 0) {
        	for (Mannschaft ms : lg.getMannschaften()) {
            	ms.set(0, 0);
            	for (Mannschaft ms2 : lg.getMannschaften()) {
            		int besser = ms.compareWith(ms2);
            		if (besser == 2) {
            			ms.set(0, ms.get(0) + 1);
            		}
            	}
            }
            
            this.labelsbefuellen();
            return true;
    	} else {
    		JOptionPane.showMessageDialog(null, "Es wurden noch keine Mannschaften angelegt. Davor kann keine Tabelle angezeigt werden.");
    		return false;
    	}
    }
    
}
