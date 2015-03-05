package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static util.Utilities.*;

public class ExtendedErgebnisChooser extends JFrame {
	private static final long serialVersionUID = 7503825008840407522L;
	
	private final Dimension dim = new Dimension(250, 190);
	
	Spieltag spieltag;
	
	Ergebnis ergebnis;
	
	boolean isFinishedAfterRT = false;
	boolean isFinishedAfterET = false;
	
	private JButton go;
	private JButton afterET;
	private JButton afterPS;
	private JLabel[] descrLbls;
	private JTextField[][] goalsTFs;
	
	private Rectangle RECGO = new Rectangle(170, 10, 60, 30);
	private Rectangle RECAET = new Rectangle(170, 50, 50, 30);
	private Rectangle RECAPS = new Rectangle(170, 90, 50, 30);
	
	private int[] descr = new int[] {10, 10, 0, 10, 80, 30};
	private int[] goals = new int[] {90, 10, 10, 10, 30, 30};
	
	final int STARTX = 0;
	final int STARTY = 1;
	final int GAPX = 2;
	final int GAPY = 3;
	final int WIDTH = 4;
	final int HEIGHT = 5;
	
	public ExtendedErgebnisChooser(Spieltag spieltag, Ergebnis previous) {
		super();
		
		this.spieltag = spieltag;
		this.ergebnis = previous;
		
		initGUI();
		setErgebnis(ergebnis);
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		goalsTFs = new JTextField[4][2];
		descrLbls = new JLabel[4];
		
		for (int i = 0; i < goalsTFs.length; i++) {
			for (int j = 0; j < goalsTFs[i].length; j++) {
				final int x = i, y = j;
				goalsTFs[i][j] = new JTextField();
				getContentPane().add(goalsTFs[i][j]);
				goalsTFs[i][j].setBounds(goals[STARTX] + j * (goals[WIDTH] + goals[GAPX]), goals[STARTY] + i * (goals[HEIGHT] + goals[GAPY]), goals[WIDTH], goals[HEIGHT]);
				goalsTFs[i][j].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if ((goalsTFs[x][y].getText().length() >= 2 && !goalsTFs[x][y].getText().equals("-1")) || arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						}
					}
				});
			}
		}
		goalsTFs[0][0].setEnabled(false);
		goalsTFs[0][1].setEnabled(false);
		goalsTFs[1][0].requestFocus();
		
		for (int i = 0; i < descrLbls.length; i++) {
			final int x = i;
			descrLbls[i] = new JLabel();
			getContentPane().add(descrLbls[i]);
			descrLbls[i].setBounds(descr[STARTX], descr[STARTY] + i * (descr[HEIGHT] + descr[GAPY]), descr[WIDTH], descr[HEIGHT]);
			descrLbls[i].setText("Halbzeit");
		}
		
		{
			afterET = new JButton();
			this.add(afterET);
			afterET.setBounds(RECAET);
			afterET.setText("n.V.");
			afterET.setFocusable(false);
			afterET.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					afterETActionPerformed();
				}
			});
		}
		{
			afterPS = new JButton();
			this.add(afterPS);
			afterPS.setBounds(RECAPS);
			afterPS.setText("n.E.");
			afterPS.setFocusable(false);
			afterPS.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					afterPSActionPerformed();
				}
			});
		}
		{
			go = new JButton();
			this.add(go);
			go.setBounds(RECGO);
			go.setText("fertig");
			go.setFocusable(false);
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
			        goActionPerformed();
				}
			});
		}
		
		setSize(this.dim);
		setResizable(false);
	}
	
	private void afterETActionPerformed() {
		if (isFinishedAfterRT || !isFinishedAfterET)	showTextFields(true, false);
		else											showTextFields(false, false);
	}
	
	private void afterPSActionPerformed() {
		if (isFinishedAfterRT || isFinishedAfterET)	showTextFields(true, true);
		else										showTextFields(true, false);
	}
	
	private void showTextFields(boolean extraTime, boolean penalties) {
		if (!extraTime) {
			isFinishedAfterRT = true;
			isFinishedAfterET = false;
			goalsTFs[2][0].setVisible(false);
			goalsTFs[2][1].setVisible(false);
			goalsTFs[3][0].setVisible(false);
			goalsTFs[3][1].setVisible(false);
			descrLbls[2].setVisible(false);
			descrLbls[3].setVisible(false);
		} else if (!penalties) {
			isFinishedAfterRT = false;
			isFinishedAfterET = true;
			goalsTFs[2][0].setVisible(true);
			goalsTFs[2][1].setVisible(true);
			goalsTFs[3][0].setVisible(false);
			goalsTFs[3][1].setVisible(false);
			descrLbls[2].setVisible(true);
			descrLbls[3].setVisible(false);
		} else {
			isFinishedAfterRT = false;
			isFinishedAfterET = false;
			goalsTFs[2][0].setVisible(true);
			goalsTFs[2][1].setVisible(true);
			goalsTFs[3][0].setVisible(true);
			goalsTFs[3][1].setVisible(true);
			descrLbls[2].setVisible(true);
			descrLbls[3].setVisible(true);
		}
	}
	
	private void setErgebnis(Ergebnis ergebnis) {
		for (int i = 0; i < 4; i++) {
			goalsTFs[i][0].setText("-1");
			goalsTFs[i][1].setText("-1");
		}
		if (ergebnis != null) {
			if (ergebnis.toString().indexOf("n") == -1)	{
				isFinishedAfterRT = true;
				showTextFields(false, false);
			} else if (ergebnis.toString().indexOf("nE") == -1)	{
				isFinishedAfterET = true;
				showTextFields(true, false);
			}
			for (int i = 0; i < 4; i++) {
				goalsTFs[i][0].setText("" + ergebnis.home(i));
				goalsTFs[i][1].setText("" + ergebnis.away(i));
			}
		} else {
			showTextFields(false, false);
		}
	}
	
	private void goActionPerformed() {
		String resRT = goalsTFs[1][0].getText() + ":" + goalsTFs[1][1].getText();
		String resET = goalsTFs[2][0].getText() + ":" + goalsTFs[2][1].getText();
		String resPS = goalsTFs[3][0].getText() + ":" + goalsTFs[3][1].getText();
		
		Ergebnis ergebnis = null;
		
		if (isFinishedAfterRT) {
			if (resRT.indexOf("-1") == -1) {
				ergebnis = new Ergebnis(resRT);
			}
		} else if (isFinishedAfterET) {
			if (resRT.indexOf("-1") == -1 && resET.indexOf("-1") == -1) {
				ergebnis = new Ergebnis(resET + "nV (" + resRT + ")");
			}
		} else {
			if (resRT.indexOf("-1") == -1 && resET.indexOf("-1") == -1 && resPS.indexOf("-1") == -1) {
				ergebnis = new Ergebnis(resPS + "nE (" + resET + "," + resRT + ")");
			}
		}
		
		log("I returned " + this.ergebnis + "   oder: " + ergebnis);
		
		this.setVisible(false);
		
		spieltag.moreOptions(this.ergebnis);
	}
	
}
