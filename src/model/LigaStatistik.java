package model;

import java.awt.*;

import javax.swing.*;

import static util.Utilities.*;

public class LigaStatistik extends JPanel {
	
	private Dimension preferredSize = new Dimension(900, 600);
	private Liga liga;
	private Mannschaft[] mannschaften;
	private int currentMatchday;
	
	private Font fontWettbewerbLbl = new Font("Verdana", Font.PLAIN, 24);
	
	private JLabel jLblWettbewerb;
	
	private JLabel jLblMostGoals;
	private JLabel jLblLeastGoals;
	
	private Rectangle REC_LBLWETTBW = new Rectangle(250, 20, 210, 30);
	
	private Rectangle REC_LBLMOSTGOALS = new Rectangle(20, 60, 140, 30);
	private Rectangle REC_LBLLEASTGOALS = new Rectangle(20, 90, 140, 30);
	
	/* TODO
	 * 
	 * haeufigstes Ergebnis
	 * Heim-/Auswaertssiege/Unentschieden
	 * 
	 * meiste/wenigste Gegen-/Tore/Punkte
	 * meiste/wenigste Siege/Niederlagen
	 * 
	 * meiste Tore in einem Spiel
	 * laengste Sieges-/Niederlagenserie
	 * 
	 */
	
	public LigaStatistik(Liga liga) {
		super();
		
		this.liga = liga;
		this.mannschaften = liga.getMannschaften();
		this.currentMatchday = liga.getCurrentMatchday();
		
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(null);
		
		{
			jLblWettbewerb = new JLabel();
			this.add(jLblWettbewerb);
			jLblWettbewerb.setBounds(REC_LBLWETTBW);
			jLblWettbewerb.setFont(fontWettbewerbLbl);
			jLblWettbewerb.setText(liga.getName());
		}
		
		{
			jLblMostGoals = new JLabel();
			this.add(jLblMostGoals);
			jLblMostGoals.setBounds(REC_LBLMOSTGOALS);
			jLblMostGoals.setText("Meiste Tore:");
		}
		{
			jLblLeastGoals = new JLabel();
			this.add(jLblLeastGoals);
			jLblLeastGoals.setBounds(REC_LBLLEASTGOALS);
			jLblLeastGoals.setText("Wenigste Tore:");
		}
		
        this.setSize(preferredSize);
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
	
	public void updateGUI() {
		int maximum = 0, minimum = 1000, maxIndex = -1, minIndex = -1;
		
		int heimsiege = 0, unentschieden = 0, auswaertssiege = 0;
		for (int i = 0; i < liga.getNumberOfMatchdays(); i++) {
			for (int j = 0; j < liga.getNumberOfMatchesPerMatchday(); j++) {
				if (liga.isErgebnisplanEntered(i, j)) {
					if (liga.getErgebnis(i, j).home() > liga.getErgebnis(i, j).away())		heimsiege++;
					else if (liga.getErgebnis(i, j).home() < liga.getErgebnis(i, j).away())	auswaertssiege++;
					else																	unentschieden++;
				}
			}
		}
		log("Von " + (heimsiege + unentschieden + auswaertssiege) + " Spielen waren:");
		log(heimsiege + " Heimsiege");
		log(unentschieden + " Unentschieden");
		log(auswaertssiege + " Auswaertsiege");
		
		for (Mannschaft team : mannschaften) {
			if (team.get(6, 0, currentMatchday) > maximum) {
				maximum = team.get(6, 0, currentMatchday);
				maxIndex = team.getId() - 1;
			}
			if (team.get(6, 0, currentMatchday) < minimum) {
				minimum = team.get(6, 0, currentMatchday);
				minIndex = team.getId() - 1;
			}
		}
		log("Meiste Tore: " + mannschaften[maxIndex].getName() + "(" + maximum + ")");
		log("Wenigste Tore: " + mannschaften[minIndex].getName() + "(" + minimum + ")");
		
		maximum = 0;
		minimum = 1000;
		maxIndex = minIndex = -1;
		
		for (Mannschaft team : mannschaften) {
			if (team.get(3, 0, currentMatchday) > maximum) {
				maximum = team.get(3, 0, currentMatchday);
				maxIndex = team.getId() - 1;
			}
			if (team.get(3, 0, currentMatchday) < minimum) {
				minimum = team.get(3, 0, currentMatchday);
				minIndex = team.getId() - 1;
			}
		}
		log("Meiste Siege: " + mannschaften[maxIndex].getName() + "(" + maximum + ")");
		log("Wenigste Siege: " + mannschaften[minIndex].getName() + "(" + minimum + ")");
	}
}
