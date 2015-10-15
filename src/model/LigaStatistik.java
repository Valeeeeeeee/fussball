package model;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import static util.Utilities.*;

public class LigaStatistik extends JPanel {
	
	private static final long serialVersionUID = -7289043093848224094L;
	
	private Dimension preferredSize = new Dimension(1100, 750);
	private LigaSaison season;
	private Mannschaft[] mannschaften;
	private int currentMatchday;
	
	// global variables for results
	private int numberOfHomeWins;
	private int numberOfDraws;
	private int numberOfAwayWins;
	private int numberOfHomeGoals;
	private int numberOfAwayGoals;
	private ArrayList<String> resultsHash;
	private ArrayList<Integer> resultsFrequency;
	
	// global variables for most/least comparisons
	private int value;
	private int maximum;
	private int minimum;
	private int maxIndex;
	private int minIndex;
	private int moreMax;
	private int moreMin;
	
	private static final int NUMBER_OF_HOMEAWAY = 5;
	private String[] homeAwayStrings = new String[] {"Heimsiege", "Unentschieden", "Auswärtssiege", "Heimtore", "Auswärtstore"};
	
	private static final int NUMBER_OF_RESULTS = 5;
	
	private static final int NUMBER_OF_SERIES = 9;
	private String[] seriesStrings = new String[] {"gewonnen", "unentschieden", "verloren", "unbesiegt", "sieglos", "mit Tor", "ohne Tor", "mit Gegentor", "ohne Gegentor"};
	
	private static final int NUMBER_OF_MOSTLEAST = 5;
	private int[] indices = new int[] {3, 4, 5, 6, 7};
	private String[] mostLeastStrings = new String[] {"Siege", "Unentschieden", "Niederlagen", "Tore", "Gegentore"};
	
	private Font fontWettbewerbLbl = new Font("Verdana", Font.PLAIN, 24);
	
	private JLabel jLblWettbewerb;
	private JLabel[] jLblsMost;
	private JLabel[] jLblsMostValues;
	private JLabel[] jLblsLeast;
	private JLabel[] jLblsLeastValues;

	private JLabel jLblsSerien;
	private JLabel[] jLblsSeries;
	private JLabel[] jLblsSeriesValues;
	
	private JLabel[] jLblsHomeAway;
	private JLabel[] jLblsHomeAwayPercentage;
	private JLabel[] jLblsHomeAwayAbsolute;
	private JLabel[] jLblsResults;
	private JLabel[] jLblsResultsPercentage;
	private JLabel[] jLblsResultsAbsolute;
	
	private Rectangle REC_LBLWETTBW = new Rectangle(250, 20, 340, 30);
	
	private int[] most = new int[] {20, 90, 0, 60, 160, 25};
	private int[] mostV = new int[] {200, 90, 0, 60, 280, 25};
	private int[] least = new int[] {20, 115, 0, 60, 160, 25};
	private int[] leastV = new int[] {200, 115, 0, 60, 280, 25};
	
	private Rectangle REC_LBLSERIEN = new Rectangle(530, 90, 150, 25);
	private int[] series = new int[] {530, 120, 0, 30, 110, 25};
	private int[] seriesV = new int[] {660, 120, 0, 30, 280, 25};
	
	// alte Position rechts oben x-Werte +465, y-Werte -350
	private static final int STARTX_HOMEAWAY = 135;
	private static final int SIZEX_HOMEAWAY = 99;
	private static final int SIZEY_HOMEAWAY = 25;
	private int[] homeAway = new int[] {20, 440, 0, 30, 110, 25};
	private int[] homeAwayP = new int[] {140, 440, 0, 30, 40, 25};
	private int[] homeAwayA = new int[] {240, 440, 0, 30, 30, 25};
	private static final int STARTX_RESULTS = 350;
	private static final int SIZEX_RESULTS = 99;
	private static final int SIZEY_RESULTS = 25;
	private int[] results = new int[] {320, 440, 0, 30, 30, 25};
	private int[] resultsP = new int[] {355, 440, 0, 30, 40, 25};
	private int[] resultsA = new int[] {455, 440, 0, 30, 30, 25};
	
	/* TODO
	 * meiste Tore in einem Spiel
	 */
	
	public LigaStatistik(LigaSaison season) {
		super();
		
		this.season = season;
		
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(null);
		
		resultsHash = new ArrayList<>();
		resultsFrequency = new ArrayList<>();
		
		{
			jLblWettbewerb = new JLabel();
			this.add(jLblWettbewerb);
			jLblWettbewerb.setBounds(REC_LBLWETTBW);
			jLblWettbewerb.setFont(fontWettbewerbLbl);
			jLblWettbewerb.setText(season.getName());
		}
		
		buildResults();
		buildMostLeast();
		buildSeries();
		
		
        this.setSize(preferredSize);
	}
	
	private void buildResults() {
		jLblsHomeAway = new JLabel[NUMBER_OF_HOMEAWAY];
		jLblsHomeAwayPercentage = new JLabel[NUMBER_OF_HOMEAWAY];
		jLblsHomeAwayAbsolute = new JLabel[NUMBER_OF_HOMEAWAY];
		for (int i = 0; i < NUMBER_OF_HOMEAWAY; i++) {
			jLblsHomeAway[i] = new JLabel();
			this.add(jLblsHomeAway[i]);
			jLblsHomeAway[i].setBounds(homeAway[STARTX], homeAway[STARTY] + i * homeAway[GAPY], homeAway[SIZEX], homeAway[SIZEY]);
			jLblsHomeAway[i].setText(homeAwayStrings[i] + ":");
			
			jLblsHomeAwayPercentage[i] = new JLabel();
			this.add(jLblsHomeAwayPercentage[i]);
			jLblsHomeAwayPercentage[i].setBounds(homeAwayP[STARTX], homeAwayP[STARTY] + i * homeAwayP[GAPY], homeAwayP[SIZEX], homeAwayP[SIZEY]);
			
			jLblsHomeAwayAbsolute[i] = new JLabel();
			this.add(jLblsHomeAwayAbsolute[i]);
			jLblsHomeAwayAbsolute[i].setBounds(homeAwayA[STARTX], homeAwayA[STARTY] + i * homeAwayA[GAPY], homeAwayA[SIZEX], homeAwayA[SIZEY]);
		}
		
		jLblsResults = new JLabel[NUMBER_OF_RESULTS];
		jLblsResultsPercentage = new JLabel[NUMBER_OF_RESULTS];
		jLblsResultsAbsolute = new JLabel[NUMBER_OF_RESULTS];
		for (int i = 0; i < NUMBER_OF_RESULTS; i++) {
			jLblsResults[i] = new JLabel();
			this.add(jLblsResults[i]);
			jLblsResults[i].setBounds(results[STARTX], results[STARTY] + i * results[GAPY], results[SIZEX], results[SIZEY]);
			
			jLblsResultsPercentage[i] = new JLabel();
			this.add(jLblsResultsPercentage[i]);
			jLblsResultsPercentage[i].setBounds(resultsP[STARTX], resultsP[STARTY] + i * resultsP[GAPY], resultsP[SIZEX], resultsP[SIZEY]);
			
			jLblsResultsAbsolute[i] = new JLabel();
			this.add(jLblsResultsAbsolute[i]);
			jLblsResultsAbsolute[i].setBounds(resultsA[STARTX], resultsA[STARTY] + i * resultsA[GAPY], resultsA[SIZEX], resultsA[SIZEY]);
		}
	}
	
	private void buildMostLeast() {
		jLblsMost = new JLabel[NUMBER_OF_MOSTLEAST];
		jLblsMostValues = new JLabel[NUMBER_OF_MOSTLEAST];
		jLblsLeast = new JLabel[NUMBER_OF_MOSTLEAST];
		jLblsLeastValues = new JLabel[NUMBER_OF_MOSTLEAST];
		for (int i = 0; i < NUMBER_OF_MOSTLEAST; i++) {
			jLblsMost[i] = new JLabel();
			this.add(jLblsMost[i]);
			jLblsMost[i].setBounds(most[STARTX], most[STARTY] + i * most[GAPY], most[SIZEX], most[SIZEY]);
			jLblsMost[i].setText("Meiste " + mostLeastStrings[i] + ":");
			
			jLblsMostValues[i] = new JLabel();
			this.add(jLblsMostValues[i]);
			jLblsMostValues[i].setBounds(mostV[STARTX], mostV[STARTY] + i * mostV[GAPY], mostV[SIZEX], mostV[SIZEY]);
			jLblsMostValues[i].setText("n/a");
			
			jLblsLeast[i] = new JLabel();
			this.add(jLblsLeast[i]);
			jLblsLeast[i].setBounds(least[STARTX], least[STARTY] + i * least[GAPY], least[SIZEX], least[SIZEY]);
			jLblsLeast[i].setText("Wenigste " + mostLeastStrings[i] + ":");
			
			jLblsLeastValues[i] = new JLabel();
			this.add(jLblsLeastValues[i]);
			jLblsLeastValues[i].setBounds(leastV[STARTX], leastV[STARTY] + i * leastV[GAPY], leastV[SIZEX], leastV[SIZEY]);
			jLblsLeastValues[i].setText("n/a");
		}
	}
	
	public void buildSeries() {
		jLblsSeries = new JLabel[NUMBER_OF_SERIES];
		jLblsSeriesValues = new JLabel[NUMBER_OF_SERIES];
		{
			jLblsSerien = new JLabel();
			this.add(jLblsSerien);
			jLblsSerien.setBounds(REC_LBLSERIEN);
			jLblsSerien.setText("Meiste Spiele in Folge ...");
		}
		
		for (int i = 0; i < NUMBER_OF_SERIES; i++) {
			jLblsSeries[i] = new JLabel();
			this.add(jLblsSeries[i]);
			jLblsSeries[i].setBounds(series[STARTX], series[STARTY] + i * series[GAPY], series[SIZEX], series[SIZEY]);
			jLblsSeries[i].setText("... " + seriesStrings[i]);
			
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
    	g.fillRect(STARTX_HOMEAWAY, 440, 1 + numberOfHomeWins * SIZEX_HOMEAWAY / maximum, SIZEY_HOMEAWAY);
    	g.fillRect(STARTX_HOMEAWAY, 470, 1 + numberOfDraws * SIZEX_HOMEAWAY / maximum, SIZEY_HOMEAWAY);
    	g.fillRect(STARTX_HOMEAWAY, 500, 1 + numberOfAwayWins * SIZEX_HOMEAWAY / maximum, SIZEY_HOMEAWAY);
    	
    	maximum = Math.max(numberOfHomeGoals, numberOfAwayGoals);
		if (maximum == 0)	maximum = 1;// to avoid dividing by zero
    	g.fillRect(STARTX_HOMEAWAY, 530, 1 + numberOfHomeGoals * SIZEX_HOMEAWAY / maximum, SIZEY_HOMEAWAY);
    	g.fillRect(STARTX_HOMEAWAY, 560, 1 + numberOfAwayGoals * SIZEX_HOMEAWAY / maximum, SIZEY_HOMEAWAY);
    	
    	maximum = 1;
    	if (resultsFrequency.size() > 0)	maximum = resultsFrequency.get(0);
    	for (int i = 0; i < 5; i++) {
    		int height = 0;
    		if (resultsFrequency.size() > i)	height = resultsFrequency.get(i);
        	g.fillRect(STARTX_RESULTS, 440 + i * 30, 1 + height * SIZEX_RESULTS / maximum, SIZEY_RESULTS);
		}
    }
	
	public void aktualisieren() {
		this.mannschaften = season.getMannschaften();
		this.currentMatchday = season.getCurrentMatchday();
		updateGUI();
	}
	
	private void resetValues() {
		maximum = maxIndex = minIndex = -1;
		minimum = 1000;
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
	
	private void addResult(int moreGoals, int lessGoals) {
		String hashKey = moreGoals + ":" + lessGoals;
		
		int index;
		for (index = 0; index < resultsHash.size(); index++) {
			if (hashKey.equals(resultsHash.get(index)))	break;
		}
		if (index == resultsHash.size()) {
			resultsHash.add(hashKey);
			resultsFrequency.add(index, 0);
		}
		resultsFrequency.add(index, resultsFrequency.remove(index) + 1);
	}
	
	private void orderLists() {
		ArrayList<String> oldHashOrder = new ArrayList<>();
		ArrayList<Integer> oldFrequencyOrder = new ArrayList<>();
		while(resultsHash.size() > 0) {
			oldHashOrder.add(resultsHash.remove(0));
			oldFrequencyOrder.add(resultsFrequency.remove(0));
		}
		
		int index;
		while(oldHashOrder.size() > 0) {
			for (index = 0; index < resultsFrequency.size(); index++) {
				if (oldFrequencyOrder.get(0) > resultsFrequency.get(index)) {
					break;
				}
			}
			resultsHash.add(index, oldHashOrder.remove(0));
			resultsFrequency.add(index, oldFrequencyOrder.remove(0));
		}
	}
	
	public void updateGUI() {
		updateResults();
		updateMostLeast();
		updateSeries();
	}
	
	private void updateResults() {
		int numberOfResults = 0;
		numberOfHomeWins = numberOfDraws = numberOfAwayWins = numberOfHomeGoals = numberOfAwayGoals = 0;
		resultsHash.clear();
		resultsFrequency.clear();
		for (int i = 0; i < season.getNumberOfMatchdays(); i++) {
			for (int j = 0; j < season.getNumberOfMatchesPerMatchday(); j++) {
				if (season.isErgebnisplanEntered(i, j)) {
					Ergebnis result = season.getErgebnis(i, j);
					addResult(Math.max(result.home(), result.away()), Math.min(result.home(), result.away()));
					if (result.home() > result.away())		numberOfHomeWins++;
					else if (result.home() < result.away())	numberOfAwayWins++;
					else									numberOfDraws++;
					numberOfHomeGoals += result.home();
					numberOfAwayGoals += result.away();
					numberOfResults++;
				}
			}
		}
		
		orderLists();
		if (numberOfResults == 0) numberOfResults = 1;
		for (int i = 0; i < 5; i++) {
			if (i < resultsHash.size()) {
				jLblsResults[i].setText(resultsHash.get(i));
				jLblsResultsPercentage[i].setText((100 * resultsFrequency.get(i) / numberOfResults) + "%");
				jLblsResultsAbsolute[i].setText("" + resultsFrequency.get(i));
			} else {
				jLblsResults[i].setText("n/a");
				jLblsResultsPercentage[i].setText("0%");
				jLblsResultsAbsolute[i].setText("");
			}
		}
		
		int sumOfMatches = numberOfHomeWins + numberOfDraws + numberOfAwayWins;
		if (sumOfMatches == 0) {
			sumOfMatches = 1;// to avoid dividing by zero
			jLblsHomeAwayAbsolute[0].setVisible(false);
			jLblsHomeAwayAbsolute[1].setVisible(false);
			jLblsHomeAwayAbsolute[2].setVisible(false);
		} else {
			jLblsHomeAwayAbsolute[0].setText("" + numberOfHomeWins);
			jLblsHomeAwayAbsolute[1].setText("" + numberOfDraws);
			jLblsHomeAwayAbsolute[2].setText("" + numberOfAwayWins);
			jLblsHomeAwayAbsolute[0].setVisible(true);
			jLblsHomeAwayAbsolute[1].setVisible(true);
			jLblsHomeAwayAbsolute[2].setVisible(true);
		}
		jLblsHomeAwayPercentage[0].setText((100 * numberOfHomeWins / sumOfMatches) + "%");
		jLblsHomeAwayPercentage[1].setText((100 * numberOfDraws / sumOfMatches) + "%");
		jLblsHomeAwayPercentage[2].setText((100 * numberOfAwayWins / sumOfMatches) + "%");
		
		int sumOfGoals = numberOfHomeGoals + numberOfAwayGoals;
		if (sumOfGoals == 0) {
			sumOfGoals = 1;// to avoid dividing by zero
			jLblsHomeAwayAbsolute[3].setVisible(false);
			jLblsHomeAwayAbsolute[4].setVisible(false);
		} else {
			jLblsHomeAwayAbsolute[3].setText("" + numberOfHomeGoals);
			jLblsHomeAwayAbsolute[4].setText("" + numberOfAwayGoals);
			jLblsHomeAwayAbsolute[3].setVisible(true);
			jLblsHomeAwayAbsolute[4].setVisible(true);
		}
		jLblsHomeAwayPercentage[3].setText((100 * numberOfHomeGoals / sumOfGoals) + "%");
		jLblsHomeAwayPercentage[4].setText((100 * numberOfAwayGoals / sumOfGoals) + "%");
	}
	
	private void updateMostLeast() {
		String weitere;
		
		for (int i = 0; i < NUMBER_OF_MOSTLEAST; i++) {
			resetValues();
			for (Mannschaft team : mannschaften) {
				value = team.get(indices[i], 0, currentMatchday);
				compareMinMax(team.getId());
			}
			weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
			jLblsMostValues[i].setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
			
			weitere = moreMin == 0 ? "" : " + " + moreMin + " weitere";
			jLblsLeastValues[i].setText(mannschaften[minIndex].getName() + weitere + " (" + minimum + ")");
		}
	}
	
	private void updateSeries() {
		String weitere;
		
		for (int i = 0; i < NUMBER_OF_SERIES; i++) {
			// Meiste Siege, ... in Serie
			resetValues();
			for (Mannschaft team : mannschaften) {
				value = team.getSeries(i + 1);
				compareMinMax(team.getId());
			}
			weitere = moreMax == 0 ? "" : " + " + moreMax + " weitere";
			jLblsSeriesValues[i].setText(mannschaften[maxIndex].getName() + weitere + " (" + maximum + ")");
		}
	}
}
