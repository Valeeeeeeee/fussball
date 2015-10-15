import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Uebersicht extends JPanel {
	private static final long serialVersionUID = 5249833173928569240L;

	private int mannschaftID;
	
	private int anzahl_spieltage;
	
	private JPanel spiele;
	private JLabel[][] spieltage;
	
	private JPanel informationen;
	private JLabel mannschaftsname;
	private JLabel gruendungsdatum;
	
    private int startx = 20;
    private int starty = 50;
    private int[] widthes = {20, 170, 10, 5, 10, 170};
    private int height = 15;
    private int[] gapx = {5, 10, 0, 0, 10, 0};
    private int gapy = 5;

	private Color cbackground = new Color(255, 128, 128);

    public Uebersicht(Liga lg) {
        super(); 
        initGUI(lg); 
    } 
    
    public void initGUI(Liga lg) { 
        try { 
            this.setLayout(null);
            
            anzahl_spieltage = Start.ligen[Start.aktuelle_liga].getAnzahlSpieltage();
            
            spieltage = new JLabel[anzahl_spieltage][6];
            
            // TODO: Infos zur angezeigten Mannschaft (Ausschnitt aus der Tabelle, z.B. mit weiteren 4 drumherum)
            // TODO: allg. Infos zu Mannschaften, dazu Erweiterung der Klasse Mannschaft
            
            {
            	spiele = new JPanel();
            	this.add(spiele);
            	spiele.setLayout(null);
            	spiele.setLocation(startx, starty);
            	spiele.setSize(420, 2 * 5 + lg.getAnzahlSpieltage() * height + (lg.getAnzahlSpieltage() + 2) * gapy);
            	spiele.setOpaque(true);
            	spiele.setBackground(cbackground);
            }
            
            for (int i = 0; i < spieltage.length; i++) {
            	final int x = i;
            	int nstartx = 05;
            	
                for (int j = 0; j < spieltage[i].length; j++) {
                    spieltage[i][j] = new JLabel();
                    spiele.add(spieltage[i][j]);
                    spieltage[i][j].setBounds(nstartx, 5 + i * (height + gapy) + 3 * (i / (spieltage.length / 2)) * gapy, widthes[j], height);
//                    spieltage[i][j].setOpaque(true);
                    
                    nstartx += widthes[j] + gapx[j];
                }
            	spieltage[i][1].setCursor(new Cursor(Cursor.HAND_CURSOR));
            	spieltage[i][5].setCursor(new Cursor(Cursor.HAND_CURSOR));
            	
            	spieltage[i][0].setHorizontalAlignment(SwingConstants.CENTER);
            	spieltage[i][1].setHorizontalAlignment(SwingConstants.RIGHT);
            	spieltage[i][2].setHorizontalAlignment(SwingConstants.RIGHT);
            	spieltage[i][3].setHorizontalAlignment(SwingConstants.CENTER);
            	spieltage[i][4].setHorizontalAlignment(SwingConstants.LEFT);
            	spieltage[i][5].setHorizontalAlignment(SwingConstants.LEFT);
            	
            	spieltage[i][1].addMouseListener(new MouseAdapter() {
            		public void mouseClicked(MouseEvent evt) {
                        Start.uebersicht_anzeigen(spieltage[x][1].getText());
                    }
				});
            	spieltage[i][5].addMouseListener(new MouseAdapter() {
            		public void mouseClicked(MouseEvent evt) {
                        Start.uebersicht_anzeigen(spieltage[x][5].getText());
                    }
				});
            }
            {
            	informationen = new JPanel();
            	this.add(informationen);
            	informationen.setLayout(null);
            	informationen.setLocation(startx + spiele.getSize().width + 5, starty);
            	informationen.setSize(420, 2 * 5 + lg.getAnzahlSpieltage() * height + (lg.getAnzahlSpieltage() + 2) * gapy);
            	informationen.setOpaque(true);
            	informationen.setBackground(cbackground);
            }
            {
            	mannschaftsname = new JLabel();
                informationen.add(mannschaftsname);
                mannschaftsname.setBounds(60, 10, 300, 30);
                mannschaftsname.setFont(new Font("Dialog", 0, 24));
                mannschaftsname.setHorizontalAlignment(SwingConstants.CENTER);
            }
            {
            	gruendungsdatum = new JLabel();
                informationen.add(gruendungsdatum);
                gruendungsdatum.setBounds(160, 50, 100, 30);
                gruendungsdatum.setFont(new Font("Dialog", 0, 12));
                gruendungsdatum.setHorizontalAlignment(SwingConstants.CENTER);
                gruendungsdatum.setOpaque(true);
            }
            
            int minimumheight = 600;
            int maximumheight = 840;
            Dimension dim = new Dimension();
            dim.width = startx + spiele.getSize().width + 5 + informationen.getSize().width + startx;
            dim.height = starty + spiele.getSize().height + 20;
            if (dim.height >= minimumheight && dim.height <= maximumheight) {
            	
            } else if (dim.height < minimumheight) {
            	dim.height = minimumheight;
            } else {
            	dim.height = maximumheight;
            }
            this.setSize(dim);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.setColor(Color.red);
    	g.fillRect(0, 0, getWidth(), getHeight());
    	
    	for (int i = 0; i < getHeight(); i++) {
    		int wert = (int) 128 + (128 * i / getHeight());
    		g.setColor(new Color(wert, 0, 0));
        	g.drawLine(0, i, getWidth() - 1, i);
    	}
    }
    
    public void setMannschaft(Liga lg, int id) {
    	mannschaftID = id;
        mannschaftsname.setText(Start.getMannschaftAt(mannschaftID));
        gruendungsdatum.setText(lg.getMannschaften()[mannschaftID - 1].getGruendungsdatum());
    }
    
    public void labelsbefuellen() {
    	for (int i = 0; i < anzahl_spieltage; i++) {
    		int[] match = Start.ligen[Start.aktuelle_liga].getMannschaften()[mannschaftID - 1].getMatch(i);
    		
    		spieltage[i][0].setText("" + (i + 1));
    		spieltage[i][3].setText(":");
    		if (Start.ligen[Start.aktuelle_liga].spieltage_eingetragen[i]) {
    			if (match[1] != 0) {
    				if (match[0] == Mannschaft.HOME) {
                		spieltage[i][1].setText(Start.getMannschaftAt(mannschaftID));
                		spieltage[i][5].setText(Start.getMannschaftAt(match[1]));
            		} else if (match[0] == Mannschaft.AWAY){
                		spieltage[i][1].setText(Start.getMannschaftAt(match[1]));
                		spieltage[i][5].setText(Start.getMannschaftAt(mannschaftID));
            		}
    			} else {
    				spieltage[i][1].setText("spielfrei");
            		spieltage[i][5].setText("spielfrei");
    			}
    		} else {
        		spieltage[i][1].setText("n.a.");
        		spieltage[i][5].setText("n.a.");
    		}
    		if (Start.ligen[Start.aktuelle_liga].ergebnisse_eingetragen[i] && match[1] != 0) {
    			if (match[0] == Mannschaft.HOME) {
            		spieltage[i][2].setText("" + match[2]);
            		spieltage[i][4].setText("" + match[3]);
        		} else if (match[0] == Mannschaft.AWAY){
            		spieltage[i][2].setText("" + match[3]);
            		spieltage[i][4].setText("" + match[2]);
        		}
    		} else {
        		spieltage[i][2].setText("-");
        		spieltage[i][4].setText("-");
    		}
    	}
    }
}
