package model;

import java.awt.*;

import javax.swing.*;

import static util.Utilities.*;

public class LigaStatistik extends JPanel {
	
	private Dimension preferredSize = new Dimension(900, 600);
	private Liga liga;
	
	private Font fontWettbewerbLbl = new Font("Verdana", Font.PLAIN, 24);
	
	private JLabel jLblWettbewerb;
	
	private JLabel jLblMostGoals;
	private JLabel jLblLeastGoals;
	
	private Rectangle REC_LBLWETTBW = new Rectangle(250, 10, 170, 40);
	
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
			jLblMostGoals.setFont(fontWettbewerbLbl);
			jLblMostGoals.setText("Meiste Tore:");
		}
		{
			jLblLeastGoals = new JLabel();
			this.add(jLblLeastGoals);
			jLblLeastGoals.setBounds(REC_LBLLEASTGOALS);
			jLblLeastGoals.setFont(fontWettbewerbLbl);
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
		
	}
}
