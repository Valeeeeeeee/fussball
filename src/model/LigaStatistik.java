package model;

import java.awt.*;

import javax.swing.*;

import static util.Utilities.*;

public class LigaStatistik extends JPanel {
	
	private Dimension preferredSize = new Dimension(900, 600);
	private Liga liga;
	private Mannschaft[] mannschaften;
	private int currentMatchday;
	
	// global variables for bar chart
	private int numberOfHomeWins = 0;
	private int numberOfDraws = 0;
	private int numberOfAwayWins = 0;
	
	private Font fontWettbewerbLbl = new Font("Verdana", Font.PLAIN, 24);
	
	private JLabel jLblWettbewerb;
	
	private JLabel jLblMostGoals;
	private JLabel jLblLeastGoals;
	
	private JLabel jLblHomeWins;
	private JLabel jLblDraws;
	private JLabel jLblAwayWins;
	private JLabel jLblHomeWinsPercentage;
	private JLabel jLblDrawsPercentage;
	private JLabel jLblAwayWinsPercentage;
	private JLabel jLblHomeWinsAbsolute;
	private JLabel jLblDrawsAbsolute;
	private JLabel jLblAwayWinsAbsolute;
	
	private Rectangle REC_LBLWETTBW = new Rectangle(250, 20, 210, 30);
	
	private Rectangle REC_LBLMOSTGOALS = new Rectangle(20, 60, 140, 30);
	private Rectangle REC_LBLLEASTGOALS = new Rectangle(20, 90, 140, 30);
	
	private Rectangle REC_LBLHOMEWINS = new Rectangle(485, 90, 110, 25);
	private Rectangle REC_LBLDRAWS = new Rectangle(485, 120, 110, 25);
	private Rectangle REC_LBLAWAYWINS = new Rectangle(485, 150, 110, 25);
	private Rectangle REC_LBLHOMEWINSPERC = new Rectangle(605, 90, 40, 25);
	private Rectangle REC_LBLDRAWSPERC = new Rectangle(605, 120, 40, 25);
	private Rectangle REC_LBLAWAYWINSPERC = new Rectangle(605, 150, 40, 25);
	private Rectangle REC_LBLHOMEWINSABS = new Rectangle(705, 90, 30, 25);
	private Rectangle REC_LBLDRAWSABS = new Rectangle(705, 120, 30, 25);
	private Rectangle REC_LBLAWAYWINSABS = new Rectangle(705, 150, 30, 25);
	
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
		
		{
			jLblHomeWins = new JLabel();
			this.add(jLblHomeWins);
			jLblHomeWins.setBounds(REC_LBLHOMEWINS);
			jLblHomeWins.setText("Heimsiege:");
		}
		{
			jLblDraws = new JLabel();
			this.add(jLblDraws);
			jLblDraws.setBounds(REC_LBLDRAWS);
			jLblDraws.setText("Unentschieden:");
		}
		{
			jLblAwayWins = new JLabel();
			this.add(jLblAwayWins);
			jLblAwayWins.setBounds(REC_LBLAWAYWINS);
			jLblAwayWins.setText("Auswaertssiege:");
		}
		{
			jLblHomeWinsPercentage = new JLabel();
			this.add(jLblHomeWinsPercentage);
			jLblHomeWinsPercentage.setBounds(REC_LBLHOMEWINSPERC);
		}
		{
			jLblDrawsPercentage = new JLabel();
			this.add(jLblDrawsPercentage);
			jLblDrawsPercentage.setBounds(REC_LBLDRAWSPERC);
		}
		{
			jLblAwayWinsPercentage = new JLabel();
			this.add(jLblAwayWinsPercentage);
			jLblAwayWinsPercentage.setBounds(REC_LBLAWAYWINSPERC);
		}
		{
			jLblHomeWinsAbsolute = new JLabel();
			this.add(jLblHomeWinsAbsolute);
			jLblHomeWinsAbsolute.setBounds(REC_LBLHOMEWINSABS);
		}
		{
			jLblDrawsAbsolute = new JLabel();
			this.add(jLblDrawsAbsolute);
			jLblDrawsAbsolute.setBounds(REC_LBLDRAWSABS);
		}
		{
			jLblAwayWinsAbsolute = new JLabel();
			this.add(jLblAwayWinsAbsolute);
			jLblAwayWinsAbsolute.setBounds(REC_LBLAWAYWINSABS);
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
    	
    	int maximum = Math.max(Math.max(numberOfHomeWins, numberOfDraws), numberOfAwayWins);
    	g.setColor(Color.yellow);
    	g.fillRect(600, 90, numberOfHomeWins * 100 / maximum, 25);
    	g.setColor(Color.yellow);
    	g.fillRect(600, 120, numberOfDraws * 100 / maximum, 25);
    	g.setColor(Color.yellow);
    	g.fillRect(600, 150, numberOfAwayWins * 100 / maximum, 25);
    }
	
	public void updateGUI() {
		int maximum = 0, minimum = 1000, maxIndex = -1, minIndex = -1;
		
		numberOfHomeWins = numberOfDraws = numberOfAwayWins = 0;
		for (int i = 0; i < liga.getNumberOfMatchdays(); i++) {
			for (int j = 0; j < liga.getNumberOfMatchesPerMatchday(); j++) {
				if (liga.isErgebnisplanEntered(i, j)) {
					if (liga.getErgebnis(i, j).home() > liga.getErgebnis(i, j).away())		numberOfHomeWins++;
					else if (liga.getErgebnis(i, j).home() < liga.getErgebnis(i, j).away())	numberOfAwayWins++;
					else																	numberOfDraws++;
				}
			}
		}
		int sumOfMatches = numberOfHomeWins + numberOfDraws + numberOfAwayWins;
		jLblHomeWinsPercentage.setText((100 * numberOfHomeWins / sumOfMatches) + "%");
		jLblDrawsPercentage.setText((100 * numberOfDraws / sumOfMatches) + "%");
		jLblAwayWinsPercentage.setText((100 * numberOfAwayWins / sumOfMatches) + "%");
		jLblHomeWinsAbsolute.setText("" + numberOfHomeWins);
		jLblDrawsAbsolute.setText("" + numberOfDraws);
		jLblAwayWinsAbsolute.setText("" + numberOfAwayWins);
		log("Von " + sumOfMatches + " Spielen waren:");
		log(numberOfHomeWins + " Heimsiege");
		log(numberOfDraws + " Unentschieden");
		log(numberOfAwayWins + " Auswaertsiege");
		
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
