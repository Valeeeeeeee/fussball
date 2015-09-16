package model;

import static util.Utilities.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

public class Tabellenverlauf extends JPanel {
	
	private static final long serialVersionUID = 6093575133596035653L;
	
	private static final ToolTipManager ttManager = ToolTipManager.sharedInstance();
	
	private static final int WIDTH = 480;
	private static final int HEIGHT = 150;
	
	private static final Color background = new Color(255, 255, 255);
	private static final Color linesBG = new Color(200, 200, 200);
	private static final Color linesFG = new Color(50, 50, 0);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	
	private int numberOfTeams;
	private int[] rankings;
	private int[] anzahlen = new int[] {2, 0, 0, 0, 2};
	
	private int pixelsPerRank;
	private int pixelsPerMatchday;
	private int freeSpaceX;
	private int freeSpaceY;
	
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
		
		pixelsPerRank = (HEIGHT - 5) / numberOfTeams;
		pixelsPerMatchday = (WIDTH - 10) / rankings.length;
		freeSpaceX = (WIDTH - rankings.length * pixelsPerMatchday) / 2;
		freeSpaceY = (HEIGHT - numberOfTeams * pixelsPerRank) / 2;
		
		for (int i = 0; i < rankings.length; i++) {
			int thisX = freeSpaceX + pixelsPerMatchday / 2 + i * pixelsPerMatchday;
			int thisY = freeSpaceY + pixelsPerRank / 2 + pixelsPerRank * (rankings[i] - 1);
			
			if (rankings[i] != 0) {
				JLabel label = new JLabel();
				this.add(label);
				label.setBounds(thisX - 2, thisY - 2, 4, 4);
				label.setToolTipText((i + 1) + ". Spieltag, Platz " + rankings[i]);
				label.setCursor(handCursor);
				label.addMouseListener(new MouseAdapter() {
					public void mouseExited(MouseEvent e) {
						ttManager.setInitialDelay(750);
						ttManager.setDismissDelay(4000);
					}
					public void mouseEntered(MouseEvent e) {
						ttManager.setInitialDelay(0);
						ttManager.setDismissDelay(10000);
					}
				});
			}
		}
		
		setSize(WIDTH, HEIGHT);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
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
		int rightX = freeSpaceX + rankings.length * pixelsPerMatchday, bottomY = freeSpaceY + numberOfTeams * pixelsPerRank;
		g.drawLine(freeSpaceX, freeSpaceY, rightX, freeSpaceY);
		g.drawLine(freeSpaceX, bottomY, rightX, bottomY);
		g.drawLine(freeSpaceX, freeSpaceY, freeSpaceX, bottomY);
		g.drawLine(rightX, freeSpaceY, rightX, bottomY);
		
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
