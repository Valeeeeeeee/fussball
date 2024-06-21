package model;

import static util.Utilities.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

public class Tabellenverlauf extends JPanel {
	
	private static final long serialVersionUID = 6093575133596035653L;
	
	private static final ToolTipManager ttManager = ToolTipManager.sharedInstance();
	
	private static final int WIDTH = 530;
	private static final int HEIGHT = 150;
	
	private static final Color background = new Color(255, 255, 255);
	private static final Color linesBG = new Color(200, 200, 200);
	private static final Color linesFG = new Color(50, 50, 0);
	
	private int numberOfTeams;
	private int[] rankings;
	private HashMap<Integer, TabellenHintergrundFarbe> tableBackgroundColors;
	
	private int pixelsPerRank;
	private int pixelsPerMatchday;
	private int freeSpaceX;
	private int freeSpaceY;
	
	public Tabellenverlauf(int numberOfTeams, int[] rankings, Wettbewerb competition) {
		super();
		
		setLayout(null);
		this.numberOfTeams = numberOfTeams;
		this.rankings = rankings;
		if (competition instanceof LigaSaison) {
			LigaSaison lSeason = (LigaSaison) competition;
			tableBackgroundColors = lSeason.getTableBackgroundColors();
		} else if (competition instanceof Gruppe) {
			Gruppe group = (Gruppe) competition;
			tableBackgroundColors = group.getTableBackgroundColors();
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
		
		for (int i = 0; i < numberOfTeams; i++) {
			Optional<TabellenHintergrundFarbe> background = Optional.ofNullable(tableBackgroundColors.get(i + 1));
			if (background.isPresent()) {
				g.setColor(background.get().getColor());
				g.fillRect(freeSpaceX, freeSpaceY + 1 + i * pixelsPerRank, rankings.length * pixelsPerMatchday, pixelsPerRank - 1);
			}
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
