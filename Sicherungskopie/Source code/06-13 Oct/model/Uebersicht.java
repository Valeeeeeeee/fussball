package model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static util.Utilities.*;

public class Uebersicht extends JPanel {
	private static final long serialVersionUID = 5249833173928569240L;

	
	private Start start;
	private Liga liga;
	private Gruppe gruppe;
	private Turnier turnier;
	
	private boolean belongsToLeague;
	private boolean belongsToGroup;
	private boolean belongsToKORound;
	
	private int mannschaftID;
	private Mannschaft mannschaft;
	private Mannschaft[] mannschaften;
	
	private int numberOfMatchdays;
	
	private JPanel spiele;
	private JLabel[][] spieltage;
	
	private JPanel informationen;
	private JLabel mannschaftsname;
	private JLabel gruendungsdatum;
	
	private static final int MATCHDAY = 0;
	private static final int DATE = 1;
	private static final int TEAMHOME = 2;
	private static final int GOALSHOME = 3;
	private static final int TRENNZEICHEN = 4;
	private static final int GOALSAWAY = 5;
	private static final int TEAMAWAY = 6;
	private static final int NUMBEROFFIELDS = 7;
	
	/** The left and right margin for spiele */
	int nstartx = 05;
    private int startx = 20;
    private int starty = 50;
    private int[] widthes = {20, 120, 175, 10, 5, 10, 175};
    private int height = 15;
    private int[] gapx = {5, 5, 10, 0, 0, 10, 0};
    private int gapy = 5;

    private Rectangle RECLBLNAME = new Rectangle(60, 10, 320, 30);
    private Rectangle RECLBLGRDATUM = new Rectangle(160, 50, 100, 30);
    
	private Color cbackground = new Color(255, 128, 128);

    public Uebersicht(Start start, Liga liga/*, Mannschaft mannschaft*/) {
        super();
        this.start = start;
        this.liga = liga;
        this.belongsToLeague = true;
        this.belongsToGroup = false;
        this.belongsToKORound = false;
    	mannschaften = liga.getMannschaften();
//        this.mannschaft = mannschaft;
        initGUI();
    }
    
    public Uebersicht(Start start, Gruppe gruppe/*, Mannschaft mannschaft*/) {
    	super();
        this.start = start;
    	this.gruppe = gruppe;
        this.belongsToLeague = false;
        this.belongsToGroup = true;
        this.belongsToKORound = false;
        mannschaften = gruppe.getMannschaften();
//        this.mannschaft = mannschaft;
    	initGUI();
    }
    
    public Uebersicht(Start start, Turnier turnier/*, Mannschaft mannschaft*/) {
    	super();
        this.start = start;
    	this.turnier = turnier;
        this.belongsToLeague = false;
        this.belongsToGroup = false;
        this.belongsToKORound = true;
//        this.mannschaft = mannschaft;
//    	initGUI();
    }
    
    
    public void initGUI() {
        try {
            this.setLayout(null);
            
            if (belongsToLeague)		numberOfMatchdays = liga.getNumberOfMatchdays();
            else if (belongsToGroup)	numberOfMatchdays = gruppe.getNumberOfMatchdays();
            
            spieltage = new JLabel[numberOfMatchdays][NUMBEROFFIELDS];
            
            // TODO: Infos zur angezeigten Mannschaft
            // TODO: (Ausschnitt aus der Tabelle, z.B. mit weiteren 4 drumherum, dazu Klasse TabellenAusschnitt)
            // TODO: allg. Infos zu Mannschaften, dazu Erweiterung der Klasse Mannschaft
            

            int sumofwidthes = 2 * nstartx;
            for (int i = 0; i < widthes.length; i++) {
				sumofwidthes += widthes[i];
			}
            for (int i = 0; i < gapx.length; i++) {
            	sumofwidthes += gapx[i];
			}
            {
            	spiele = new JPanel();
            	this.add(spiele);
            	spiele.setLayout(null);
            	spiele.setLocation(startx, starty);
            	spiele.setSize(sumofwidthes, 2 * 5 + numberOfMatchdays * height + (numberOfMatchdays + 2) * gapy);
            	spiele.setOpaque(true);
            	spiele.setBackground(cbackground);
            }
            
            for (int i = 0; i < spieltage.length; i++) {
            	final int x = i;
            	int diff = 0;
            	
                for (int j = 0; j < spieltage[i].length; j++) {
                    spieltage[i][j] = new JLabel();
                    spiele.add(spieltage[i][j]);
                    spieltage[i][j].setBounds(nstartx + diff, 5 + i * (height + gapy) + 3 * (i / (spieltage.length / 2)) * gapy, widthes[j], height);
//                    spieltage[i][j].setOpaque(true);
                    
                    diff += widthes[j] + gapx[j];
                }
            	spieltage[i][TEAMHOME].setCursor(new Cursor(Cursor.HAND_CURSOR));
            	spieltage[i][TEAMAWAY].setCursor(new Cursor(Cursor.HAND_CURSOR));
            	
            	spieltage[i][MATCHDAY].setHorizontalAlignment(SwingConstants.CENTER);
            	spieltage[i][DATE].setHorizontalAlignment(SwingConstants.CENTER);
            	spieltage[i][TEAMHOME].setHorizontalAlignment(SwingConstants.RIGHT);
            	spieltage[i][GOALSHOME].setHorizontalAlignment(SwingConstants.RIGHT);
            	spieltage[i][TRENNZEICHEN].setHorizontalAlignment(SwingConstants.CENTER);
            	spieltage[i][GOALSAWAY].setHorizontalAlignment(SwingConstants.LEFT);
            	spieltage[i][TEAMAWAY].setHorizontalAlignment(SwingConstants.LEFT);
            	
                spieltage[i][TEAMHOME].setBackground(Color.yellow);
                spieltage[i][TEAMAWAY].setBackground(Color.yellow);
            	
            	spieltage[i][TEAMHOME].addMouseListener(new MouseAdapter() {
            		public void mouseClicked(MouseEvent evt) {
                        start.uebersichtAnzeigen(spieltage[x][TEAMHOME].getText());
                    }
				});
            	spieltage[i][TEAMAWAY].addMouseListener(new MouseAdapter() {
            		public void mouseClicked(MouseEvent evt) {
                        start.uebersichtAnzeigen(spieltage[x][TEAMAWAY].getText());
                    }
				});
            }
            {
            	informationen = new JPanel();
            	this.add(informationen);
            	informationen.setLayout(null);
            	informationen.setLocation(startx + spiele.getSize().width + 5, starty);
            	informationen.setSize(420, 2 * 5 + numberOfMatchdays * height + (numberOfMatchdays + 2) * gapy);
            	informationen.setOpaque(true);
            	informationen.setBackground(cbackground);
            }
            {
            	mannschaftsname = new JLabel();
                informationen.add(mannschaftsname);
                mannschaftsname.setBounds(RECLBLNAME);
                mannschaftsname.setFont(new Font("Dialog", 0, 24));
                mannschaftsname.setHorizontalAlignment(SwingConstants.CENTER);
            }
            if (belongsToLeague) {
            	gruendungsdatum = new JLabel();
                informationen.add(gruendungsdatum);
                gruendungsdatum.setBounds(RECLBLGRDATUM);
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
    
    public void setMannschaft(int id) {
    	mannschaftID = id;
    	if (belongsToLeague)		mannschaft = mannschaften[mannschaftID - 1];
        else if (belongsToGroup)	mannschaft = mannschaften[mannschaftID - 1];
        mannschaftsname.setText(mannschaft.getName());
        if (belongsToLeague)	gruendungsdatum.setText(mannschaft.getGruendungsdatum());
    }
    
    public void labelsBefuellen() {
    	for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
    		int[] match = mannschaft.getMatch(matchday);
    		
    		spieltage[matchday][MATCHDAY].setText("" + (matchday + 1));
    		spieltage[matchday][TRENNZEICHEN].setText(":");
    		
    		spieltage[matchday][DATE].setText(mannschaft.getDateAndTime(matchday));
    		
    		if (mannschaft.isSpielEntered(matchday)) {
    			if (match[1] != 0) {
    				if (match[0] == Mannschaft.HOME) {
                		spieltage[matchday][TEAMHOME].setText(mannschaft.getName());
                		spieltage[matchday][TEAMAWAY].setText(mannschaften[match[1] - 1].getName());
                		spieltage[matchday][TEAMHOME].setOpaque(true);
                		spieltage[matchday][TEAMAWAY].setOpaque(false);
            		} else if (match[0] == Mannschaft.AWAY){
                		spieltage[matchday][TEAMHOME].setText(mannschaften[match[1] - 1].getName());
                		spieltage[matchday][TEAMAWAY].setText(mannschaft.getName());
                		spieltage[matchday][TEAMHOME].setOpaque(false);
                		spieltage[matchday][TEAMAWAY].setOpaque(true);
            		}
    			} else {
    				spieltage[matchday][TEAMHOME].setText("spielfrei");
            		spieltage[matchday][TEAMAWAY].setText("spielfrei");
    			}
    		} else {
        		spieltage[matchday][TEAMHOME].setText("n.a.");
        		spieltage[matchday][TEAMAWAY].setText("n.a.");
    		}
    		
			spieltage[matchday][TEAMHOME].repaint();
			spieltage[matchday][TEAMAWAY].repaint();
    		
    		if (mannschaft.isErgebnisEntered(matchday)) {
    			if (match[0] == Mannschaft.HOME) {
            		spieltage[matchday][GOALSHOME].setText("" + match[2]);
            		spieltage[matchday][GOALSAWAY].setText("" + match[3]);
        		} else if (match[0] == Mannschaft.AWAY){
            		spieltage[matchday][GOALSHOME].setText("" + match[3]);
            		spieltage[matchday][GOALSAWAY].setText("" + match[2]);
        		}
    		} else {
        		spieltage[matchday][GOALSHOME].setText("-");
        		spieltage[matchday][GOALSAWAY].setText("-");
    		}
    	}
    }
}

