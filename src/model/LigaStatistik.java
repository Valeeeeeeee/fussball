package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

import static util.Utilities.*;

public class LigaStatistik extends JPanel {
	
	private Dimension preferredSize = new Dimension(900, 600);
	private Liga liga;
	
	
	/* TODO
	 * 
	 * haeufigstes Ergebnis
	 * Heim-/Auswaertssiege/Unentschieden
	 * 
	 * meiste Gegen-/Tore
	 * meiste Siege/Niederlagen
	 * 
	 * meiste Tore in einem Spiel
	 * laengste Sieges-/Niederlagenserie
	 * 
	 */
	
	public LigaStatistik(Liga liga) {
		super();
		
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(null);
		
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
		
	}
}
