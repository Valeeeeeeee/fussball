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
	
	// global variables for most/least comparisons
	private int value = 0;
	private int maximum = -1;
	private int minimum = 1000;
	private int maxIndex = -1;
	private int minIndex = -1;
	private int moreMax = 0;
	private int moreMin = 0;
	
	private int numberOfSeries = 9;
	private String[] serien = new String[] {"gewonnen", "unentschieden", "verloren", "unbesiegt", "sieglos", "mit Tor", "ohne Tor", "mit Gegentor", "ohne Gegentor"};
	
	private Font fontWettbewerbLbl = new Font("Verdana", Font.PLAIN, 24);
	
	private JLabel jLblWettbewerb;
	
	private JLabel jLblMostGoals;
	private JLabel jLblMostGoalsValue;
	private JLabel jLblLeastGoals;
	private JLabel jLblLeastGoalsValue;
	private JLabel jLblMostConcededGoals;
	private JLabel jLblMostConcededGoalsValue;
	private JLabel jLblLeastConcededGoals;
	private JLabel jLblLeastConcededGoalsValue;
	private JLabel jLblMostWins;
	private JLabel jLblMostWinsValue;
	private JLabel jLblLeastWins;
	private JLabel jLblLeastWinsValue;
	private JLabel jLblMostDraws;
	private JLabel jLblMostDrawsValue;
	private JLabel jLblLeastDraws;
	private JLabel jLblLeastDrawsValue;
	private JLabel jLblMostLosses;
	private JLabel jLblMostLossesValue;
	private JLabel jLblLeastLosses;
	private JLabel jLblLeastLossesValue;

	private JLabel jLblsSerien;
	private JLabel[] jLblsSeries;
	private JLabel[] jLblsSeriesValues;
	
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
	
	private Rectangle REC_LBLMOSTGOALS = new Rectangle(20, 90, 150, 25);
	private Rectangle REC_LBLMOSTGOALSVAL = new Rectangle(190, 90, 200, 25);
	private Rectangle REC_LBLLEASTGOALS = new Rectangle(20, 115, 150, 25);
	private Rectangle REC_LBLLEASTGOALSVAL = new Rectangle(190, 115, 200, 25);
	private Rectangle REC_LBLMOSTCONCEDEDGOALS = new Rectangle(20, 150, 150, 25);
	private Rectangle REC_LBLMOSTCONCEDEDGOALSVAL = new Rectangle(190, 150, 200, 25);
	private Rectangle REC_LBLLEASTCONCEDEDGOALS = new Rectangle(20, 175, 150, 25);
	private Rectangle REC_LBLLEASTCONCEDEDGOALSVAL = new Rectangle(190, 175, 200, 25);
	private Rectangle REC_LBLMOSTWINS = new Rectangle(20, 210, 150, 25);
	private Rectangle REC_LBLMOSTWINSVAL = new Rectangle(190, 210, 200, 25);
	private Rectangle REC_LBLLEASTWINS = new Rectangle(20, 235, 150, 25);
	private Rectangle REC_LBLLEASTWINSVAL = new Rectangle(190, 235, 200, 25);
	private Rectangle REC_LBLMOSTDRAWS = new Rectangle(20, 270, 150, 25);
	private Rectangle REC_LBLMOSTDRAWSVAL = new Rectangle(190, 270, 200, 25);
	private Rectangle REC_LBLLEASTDRAWS = new Rectangle(20, 295, 150, 25);
	private Rectangle REC_LBLLEASTDRAWSVAL = new Rectangle(190, 295, 200, 25);
	private Rectangle REC_LBLMOSTLOSSES = new Rectangle(20, 330, 150, 25);
	private Rectangle REC_LBLMOSTLOSSESVAL = new Rectangle(190, 330, 200, 25);
	private Rectangle REC_LBLLEASTLOSSES = new Rectangle(20, 355, 150, 25);
	private Rectangle REC_LBLLEASTLOSSESVAL = new Rectangle(190, 355, 200, 25);
	
	private Rectangle REC_LBLSERIEN = new Rectangle(490, 90, 140, 25);
	
	// alte Position rechts oben x-Werte +465, y-Werte -380
	private Rectangle REC_LBLHOMEWINS = new Rectangle(20, 470, 110, 25);
	private Rectangle REC_LBLHOMEWINSPERC = new Rectangle(140, 470, 40, 25);
	private Rectangle REC_LBLHOMEWINSABS = new Rectangle(240, 470, 30, 25);
	private Rectangle REC_LBLDRAWS = new Rectangle(20, 500, 110, 25);
	private Rectangle REC_LBLDRAWSPERC = new Rectangle(140, 500, 40, 25);
	private Rectangle REC_LBLDRAWSABS = new Rectangle(240, 500, 30, 25);
	private Rectangle REC_LBLAWAYWINS = new Rectangle(20, 530, 110, 25);
	private Rectangle REC_LBLAWAYWINSPERC = new Rectangle(140, 530, 40, 25);
	private Rectangle REC_LBLAWAYWINSABS = new Rectangle(240, 530, 30, 25);
	
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
		
		buildMostLeast();
		buildSeries();
		
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
	
	private void buildMostLeast() {
		{
			jLblMostGoals = new JLabel();
			this.add(jLblMostGoals);
			jLblMostGoals.setBounds(REC_LBLMOSTGOALS);
			jLblMostGoals.setText("Meiste Tore:");
		}
		{
			jLblMostGoalsValue = new JLabel();
			this.add(jLblMostGoalsValue);
			jLblMostGoalsValue.setBounds(REC_LBLMOSTGOALSVAL);
			jLblMostGoalsValue.setText("n/a");
		}
		{
			jLblLeastGoals = new JLabel();
			this.add(jLblLeastGoals);
			jLblLeastGoals.setBounds(REC_LBLLEASTGOALS);
			jLblLeastGoals.setText("Wenigste Tore:");
		}
		{
			jLblLeastGoalsValue = new JLabel();
			this.add(jLblLeastGoalsValue);
			jLblLeastGoalsValue.setBounds(REC_LBLLEASTGOALSVAL);
			jLblLeastGoalsValue.setText("n/a");
		}
		{
			jLblMostConcededGoals = new JLabel();
			this.add(jLblMostConcededGoals);
			jLblMostConcededGoals.setBounds(REC_LBLMOSTCONCEDEDGOALS);
			jLblMostConcededGoals.setText("Meiste Gegentore:");
		}
		{
			jLblMostConcededGoalsValue = new JLabel();
			this.add(jLblMostConcededGoalsValue);
			jLblMostConcededGoalsValue.setBounds(REC_LBLMOSTCONCEDEDGOALSVAL);
			jLblMostConcededGoalsValue.setText("n/a");
		}
		{
			jLblLeastConcededGoals = new JLabel();
			this.add(jLblLeastConcededGoals);
			jLblLeastConcededGoals.setBounds(REC_LBLLEASTCONCEDEDGOALS);
			jLblLeastConcededGoals.setText("Wenigste Gegentore:");
		}
		{
			jLblLeastConcededGoalsValue = new JLabel();
			this.add(jLblLeastConcededGoalsValue);
			jLblLeastConcededGoalsValue.setBounds(REC_LBLLEASTCONCEDEDGOALSVAL);
			jLblLeastConcededGoalsValue.setText("n/a");
		}
		{
			jLblMostWins = new JLabel();
			this.add(jLblMostWins);
			jLblMostWins.setBounds(REC_LBLMOSTWINS);
			jLblMostWins.setText("Meiste Siege:");
		}
		{
			jLblMostWinsValue = new JLabel();
			this.add(jLblMostWinsValue);
			jLblMostWinsValue.setBounds(REC_LBLMOSTWINSVAL);
			jLblMostWinsValue.setText("n/a");
		}
		{
			jLblLeastWins = new JLabel();
			this.add(jLblLeastWins);
			jLblLeastWins.setBounds(REC_LBLLEASTWINS);
			jLblLeastWins.setText("Wenigste Siege:");
		}
		{
			jLblLeastWinsValue = new JLabel();
			this.add(jLblLeastWinsValue);
			jLblLeastWinsValue.setBounds(REC_LBLLEASTWINSVAL);
			jLblLeastWinsValue.setText("n/a");
		}
		{
			jLblMostDraws = new JLabel();
			this.add(jLblMostDraws);
			jLblMostDraws.setBounds(REC_LBLMOSTDRAWS);
			jLblMostDraws.setText("Meiste Unentschieden:");
		}
		{
			jLblMostDrawsValue = new JLabel();
			this.add(jLblMostDrawsValue);
			jLblMostDrawsValue.setBounds(REC_LBLMOSTDRAWSVAL);
			jLblMostDrawsValue.setText("n/a");
		}
		{
			jLblLeastDraws = new JLabel();
			this.add(jLblLeastDraws);
			jLblLeastDraws.setBounds(REC_LBLLEASTDRAWS);
			jLblLeastDraws.setText("Wenigste Unentschieden:");
		}
		{
			jLblLeastDrawsValue = new JLabel();
			this.add(jLblLeastDrawsValue);
			jLblLeastDrawsValue.setBounds(REC_LBLLEASTDRAWSVAL);
			jLblLeastDrawsValue.setText("n/a");
		}
		{
			jLblMostLosses = new JLabel();
			this.add(jLblMostLosses);
			jLblMostLosses.setBounds(REC_LBLMOSTLOSSES);
			jLblMostLosses.setText("Meiste Niederlagen:");
		}
		{
			jLblMostLossesValue = new JLabel();
			this.add(jLblMostLossesValue);
			jLblMostLossesValue.setBounds(REC_LBLMOSTLOSSESVAL);
			jLblMostLossesValue.setText("n/a");
		}
		{
			jLblLeastLosses = new JLabel();
			this.add(jLblLeastLosses);
			jLblLeastLosses.setBounds(REC_LBLLEASTLOSSES);
			jLblLeastLosses.setText("Wenigste Niederlagen:");
		}
		{
			jLblLeastLossesValue = new JLabel();
			this.add(jLblLeastLossesValue);
			jLblLeastLossesValue.setBounds(REC_LBLLEASTLOSSESVAL);
			jLblLeastLossesValue.setText("n/a");
		}
	}
	
	public void buildSeries() {
		jLblsSeries = new JLabel[numberOfSeries];
		jLblsSeriesValues = new JLabel[numberOfSeries];
		
		int[] series = new int[] {490, 120, 0, 30, 100, 25};
		int[] seriesV = new int[] {610, 120, 0, 30, 200, 25};
		
		{
			
			jLblsSerien = new JLabel();
			this.add(jLblsSerien);
			jLblsSerien.setBounds(REC_LBLSERIEN);
			jLblsSerien.setText("Meiste Spiele in Folge ...");
		}
		
		for (int i = 0; i < numberOfSeries; i++) {
			jLblsSeries[i] = new JLabel();
			this.add(jLblsSeries[i]);
			jLblsSeries[i].setBounds(series[STARTX], series[STARTY] + i * series[GAPY], series[SIZEX], series[SIZEY]);
			jLblsSeries[i].setText("... " + serien[i]);
			
			jLblsSeriesValues[i] = new JLabel();
			this.add(jLblsSeriesValues[i]);
			jLblsSeriesValues[i].setBounds(seriesV[STARTX], seriesV[STARTY] + i * seriesV[GAPY], seriesV[SIZEX], seriesV[SIZEY]);
			jLblsSeriesValues[i].setText("n/a");
		}
	}
	
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.setColor(Color.red);
    	g.fillRect(0, 0, getWidth(), getHeight());
    	
    	for (int i = 0; i < getHeight(); i++) {
    		int wert = (int) 192 + (64 * i / getHeight());
    		g.setColor(new Color(wert, 70, 70));
        	g.drawLine(0, i, getWidth() - 1, i);
    	}
    	
    	int maximum = Math.max(Math.max(numberOfHomeWins, numberOfDraws), numberOfAwayWins);
		if (maximum == 0)	maximum = 1;// to avoid dividing by zero
    	g.setColor(Color.yellow);
    	g.fillRect(135, 470, numberOfHomeWins * 100 / maximum, 25);
    	g.setColor(Color.yellow);
    	g.fillRect(135, 500, numberOfDraws * 100 / maximum, 25);
    	g.setColor(Color.yellow);
    	g.fillRect(135, 530, numberOfAwayWins * 100 / maximum, 25);
    }
	
	public void aktualisieren() {
		this.mannschaften = liga.getMannschaften();
		this.currentMatchday = liga.getCurrentMatchday();
		updateGUI();
	}
	
	private void resetValues() {
		maximum = -1;
		minimum = 1000;
		maxIndex = minIndex = -1;
		moreMax = moreMin = 0;
	}
	
	private void compareMinMax(int id) {
		if (value > maximum) {
			maximum = value;
			maxIndex = id - 1;
			moreMax = 0;
		} else if (value == maximum) {
			moreMax++;
		}
		if (value < minimum) {
			minimum = value;
			minIndex = id - 1;
			moreMin = 0;
		} else if (value == minimum) {
			moreMin++;
		}
	}
	
	public void updateGUI() {
		String weitere;
		
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
		if (sumOfMatches == 0)	sumOfMatches = 1;// to avoid dividing by zero
		jLblHomeWinsPercentage.setText((100 * numberOfHomeWins / sumOfMatches) + "%");
		jLblDrawsPercentage.setText((100 * numberOfDraws / sumOfMatches) + "%");
		jLblAwayWinsPercentage.setText((100 * numberOfAwayWins / sumOfMatches) + "%");
		jLblHomeWinsAbsolute.setText("" + numberOfHomeWins);
		jLblDrawsAbsolute.setText("" + numberOfDraws);
		jLblAwayWinsAbsolute.setText("" + numberOfAwayWins);
		
		sumOfMatches = numberOfHomeWins + numberOfDraws + numberOfAwayWins;
		log("Von " + sumOfMatches + " Spielen waren:");
		log(numberOfHomeWins + " Heimsiege");
		log(numberOfDraws + " Unentschieden");
		log(numberOfAwayWins + " Auswaertsiege");
		log();
		
		
		// Meiste - wenigste Tore
		resetValues();
		for (Mannschaft team : mannschaften) {
			value = team.get(6, 0, currentMatchday);
			compareMinMax(team.getId());
		}
		weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
		log("Meiste Tore: " + mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		jLblMostGoalsValue.setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		
		weitere = moreMin == 0 ? "" : " + " + moreMin + " weitere";
		log("Wenigste Tore: " + mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		jLblLeastGoalsValue.setText(mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		log();
		
		
		// Meiste - wenigste Gegentore
		resetValues();
		for (Mannschaft team : mannschaften) {
			value = team.get(7, 0, currentMatchday);
			compareMinMax(team.getId());
		}
		weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
		log("Meiste Gegentore: " + mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		jLblMostConcededGoalsValue.setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		
		weitere = moreMin == 0 ? "" : " + " + moreMin + " weitere";
		log("Wenigste Gegentore: " + mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		jLblLeastConcededGoalsValue.setText(mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		log();
		
		
		// Meiste - wenigste Siege
		resetValues();
		for (Mannschaft team : mannschaften) {
			value = team.get(3, 0, currentMatchday);
			compareMinMax(team.getId());
		}
		weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
		log("Meiste Siege: " + mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		jLblMostWinsValue.setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		
		weitere = moreMin == 0 ? "" : " + " + moreMin + " weitere";
		log("Wenigste Siege: " + mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		jLblLeastWinsValue.setText(mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		log();
		
		
		// Meiste - wenigste Unentschieden
		resetValues();
		for (Mannschaft team : mannschaften) {
			value = team.get(4, 0, currentMatchday);
			compareMinMax(team.getId());
		}
		weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
		log("Meiste Unentschieden: " + mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		jLblMostDrawsValue.setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		
		weitere = moreMin == 0 ? "" : " + " + moreMin + " weitere";
		log("Wenigste Unentschieden: " + mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		jLblLeastDrawsValue.setText(mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		log();
		
		
		// Meiste - wenigste Niederlagen
		resetValues();
		for (Mannschaft team : mannschaften) {
			value = team.get(5, 0, currentMatchday);
			compareMinMax(team.getId());
		}
		weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
		log("Meiste Niederlagen: " + mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		jLblMostLossesValue.setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		
		weitere = moreMin == 0 ? "" : " + " + moreMin + " weitere";
		log("Wenigste Niederlagen: " + mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		jLblLeastLossesValue.setText(mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		log();
		
		
		for (int i = 0; i < numberOfSeries; i++) {
			// Meiste Siege, ... in Serie
			resetValues();
			for (Mannschaft team : mannschaften) {
				value = team.getSeries(i + 1);
				compareMinMax(team.getId());
			}
			weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
			log("Meiste Spiele " + serien[i] + " in Folge: " + mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
			jLblsSeriesValues[i].setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		}
		log();
	}
}
