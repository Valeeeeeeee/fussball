package model;

import static util.Utilities.*;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Tabellenverlauf extends JPanel {
	
	private static final long serialVersionUID = 6093575133596035653L;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 130;
	
	private static final Color background = new Color(255, 255, 255);
	private static final Color linesBG = new Color(200, 200, 200);
	private static final Color linesFG = new Color(50, 50, 0);
	
	private int numberOfTeams;
	private int[] rankings;
	private int[] anzahlen = new int[] {2, 0, 0, 0, 2};
	
	public Tabellenverlauf(int numberOfTeams, int[] rankings, Wettbewerb wettbewerb) {
		super();
		
		setLayout(null);
		this.numberOfTeams = numberOfTeams;
		this.rankings = rankings;
		if (wettbewerb instanceof LigaSaison) {
			LigaSaison lSeason = (LigaSaison) wettbewerb;
			for (int i = 0; i < 5; i++) {
				anzahlen[i] = lSeason.getAnzahl(i);
			}
		}
		
		setSize(WIDTH, HEIGHT);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int pixelsPerRank = (HEIGHT - 5) / numberOfTeams;
		int pixelsPerMatchday = (WIDTH - 10) / rankings.length;
		int freeSpaceX = (WIDTH - rankings.length * pixelsPerMatchday) / 2;
		int freeSpaceY = (HEIGHT - numberOfTeams * pixelsPerRank) / 2;
		
		// Background - Lines
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(linesBG);
		for (int i = 1; i < numberOfTeams; i++) {
			g.drawLine(freeSpaceX, freeSpaceY + pixelsPerRank * i, freeSpaceX + rankings.length * pixelsPerMatchday, freeSpaceY + pixelsPerRank * i);
		}
		g.setColor(background);
		for (int i = 0; i < rankings.length; i++) {
			g.drawLine(freeSpaceX + i * pixelsPerMatchday, freeSpaceY, freeSpaceX + i * pixelsPerMatchday, freeSpaceY + numberOfTeams * pixelsPerRank);
		}
		
		// Background - Coloured Parts
		int counter = 0;
		Color[] colors = new Color[] {colorCategory1, colorCategory2, colorCategory3, colorCategory4, colorCategory5};
		for (int i = 0; i < 5; i++) {
			g.setColor(colors[i]);
			for (int j = 0; j < anzahlen[i]; j++) {
				g.fillRect(freeSpaceX, freeSpaceY + 1 + counter++ * pixelsPerRank, rankings.length * pixelsPerMatchday, pixelsPerRank - 1);
			}
			if (i == 2)	counter = numberOfTeams - anzahlen[4] - anzahlen[3];
		}
		
		// Foreground - Lines and squares
		g.setColor(linesFG);
		g.drawLine(freeSpaceX, freeSpaceY, freeSpaceX + rankings.length * pixelsPerMatchday, freeSpaceY);
		g.drawLine(freeSpaceX, freeSpaceY + numberOfTeams * pixelsPerRank, freeSpaceX + rankings.length * pixelsPerMatchday, freeSpaceY + numberOfTeams * pixelsPerRank);
		g.drawLine(freeSpaceX, freeSpaceY, freeSpaceX, freeSpaceY + numberOfTeams * pixelsPerRank);
		g.drawLine(freeSpaceX + rankings.length * pixelsPerMatchday, freeSpaceY, freeSpaceX + rankings.length * pixelsPerMatchday, freeSpaceY + numberOfTeams * pixelsPerRank);
		
		int lastX = 0, lastY = 0, lastRank = 0, thisX, thisY;
		for (int i = 0; i < rankings.length; i++) {
			thisX = freeSpaceX + pixelsPerMatchday / 2 + i * pixelsPerMatchday;
			thisY = freeSpaceY + pixelsPerRank / 2 + pixelsPerRank * (rankings[i] - 1);
			
			if (rankings[i] != 0) {
				g.drawRect(thisX - 1, thisY - 1, 2, 2);
				if (lastRank != 0) {
					g.drawLine(lastX, lastY, thisX, thisY);
				}
			}
			lastX = thisX;
			lastY = thisY;
			lastRank = rankings[i];
		}
	}
}
