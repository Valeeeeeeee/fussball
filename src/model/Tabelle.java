package model;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import static util.Utilities.*;

public class Tabelle extends JPanel {
	private static final long serialVersionUID = 2308780445852600421L;
	
	private Start start;
	private Wettbewerb wettbewerb;
	private LigaSaison season;
	private Gruppe gruppe;
	
	private int currentMatchday = -1;
	private Tabellenart aktuelleTabellenart;
	
	private boolean belongsToALeague = false;
	
	private int[] teamIndices;
	
	private int ANZAHL_TEAMS;
	
	// for Liga
	private int ANZAHL_CL;
	private int ANZAHL_CLQ;
	private int ANZAHL_EL;
	private int ANZAHL_REL;
	private int ANZAHL_ABS;
	
	// for Turnier
	private int ANZAHL_KORUNDE;
	private int ANZAHL_ZWISCHENRUNDE;
	private int ANZAHL_AUSGESCHIEDEN;
	
	private String[] titelleist = {"Pl.", "Verein", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
	private Color colorTabellenart = new Color(255, 255, 128);
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	
	private int startx = 10;
	private int starty = 110;
	private int[] widthes = {20, 220, 20, 20, 20, 20, 25, 25, 25, 25};
	private int height = 15;
	private int[] gapx = {5, 5, 5, 0, 0, 5, 0, 5, 5, 0};
	private int gapy = 15;
	private int widthPAtf = 35;
	private int widthPAlbl = 150;
	
	private Rectangle REC_COMBO = new Rectangle(120, 10, 130, 30);
	private Rectangle REC_BTNDEDUCTION = new Rectangle(260, 10, 110, 30);
	private Rectangle REC_SAVETABLE = new Rectangle(380, 10, 80, 30);
	private Rectangle REC_HOMETABLE = new Rectangle(10, 50, 80, 20);
	private Rectangle REC_COMPLETETABLE = new Rectangle(190, 50, 90, 20);
	private Rectangle REC_AWAYTABLE = new Rectangle(350, 50, 110, 20);
	private Rectangle REC_FERTIG = new Rectangle(380, 10, 80, 30);
	
	private JComboBox<String> jCBSpieltage;
	private JButton jBtnDeduction;
	private JButton jBtnTabelleSichern;
	private JLabel jLblHeimtabelle;
	private JLabel jLblGesamttabelle;
	private JLabel jLblAuswaertstabelle;
	private JLabel[] titelleiste;
	private JLabel[][] tabelle;
	private JLabel jLblPunktabzuege;
	private JTextField[] jTFPunktabzuege;
	private JButton jBtnFertig;
	
	public Tabelle(Start start, Gruppe gruppe) {
		super();
		this.start = start;
		this.gruppe = gruppe;
		this.wettbewerb = gruppe;
		this.belongsToALeague = false;
		
		this.ANZAHL_TEAMS = gruppe.getNumberOfTeams();
		this.ANZAHL_KORUNDE = 2;
		this.ANZAHL_ZWISCHENRUNDE = 0;
		this.ANZAHL_AUSGESCHIEDEN = 2;
		
		initGUI();
	}
	
	public Tabelle(Start start, LigaSaison season) {
		super();
		this.start = start;
		this.season = season;
		this.wettbewerb = season;
		this.belongsToALeague = true;
		
		this.ANZAHL_TEAMS = season.getNumberOfTeams();
		this.ANZAHL_CL = season.getAnzahl(0);
		this.ANZAHL_CLQ = season.getAnzahl(1);
		this.ANZAHL_EL = season.getAnzahl(2);
		this.ANZAHL_REL = season.getAnzahl(3);
		this.ANZAHL_ABS = season.getAnzahl(4);
		
		initGUI();
	}
	
	public void initGUI() { 
		try { 
			this.setLayout(null);
			
			tabelle = new JLabel[ANZAHL_TEAMS][titelleist.length];
			titelleiste = new JLabel[titelleist.length];
			
			teamIndices = new int[ANZAHL_TEAMS];
			
			int sumofwidthes = 0;
			
			for (int j = 0; j < titelleiste.length; j++) {
				titelleiste[j] = new JLabel();
				this.add(titelleiste[j]);
				if (j == 1) {
					titelleiste[j].setHorizontalAlignment(SwingConstants.LEFT);
					titelleiste[j].setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					titelleiste[j].setHorizontalAlignment(SwingConstants.CENTER);
				}
				titelleiste[j].setBounds(startx + sumofwidthes, starty - (height + gapy), widthes[j], height);
				titelleiste[j].setText(titelleist[j]);
				sumofwidthes += widthes[j] + gapx[j];
			}
			
			for (int i = 0; i < tabelle.length; i++) {
				sumofwidthes = 0;
				for (int j = 0; j < tabelle[i].length; j++) {
					tabelle[i][j] = new JLabel();
					this.add(tabelle[i][j]);
					if (j == 1) {
						final int x = i;
						tabelle[i][j].setHorizontalAlignment(SwingConstants.LEFT);
						tabelle[i][j].setCursor(handCursor);
						tabelle[i][j].addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								int index = teamIndices[x];
								jBtnAndereTabellenart(Tabellenart.COMPLETE);
								jCBSpieltage.setSelectedIndex(season.getCurrentMatchday());
								start.uebersichtAnzeigen(index);
							}
						});
					} else {
						tabelle[i][j].setHorizontalAlignment(SwingConstants.CENTER);
					}
					tabelle[i][j].setBounds(startx + sumofwidthes, starty + i * (height + gapy), widthes[j], height);
					sumofwidthes += widthes[j] + gapx[j];
				}
			}
			{
				String[] hilfsarray = new String[wettbewerb.getNumberOfMatchdays()];
				for (int i = 0; i < wettbewerb.getNumberOfMatchdays(); i++) {
					hilfsarray[i] = (i + 1) + ". Spieltag";
				}
				jCBSpieltage = new JComboBox<String>();
				this.add(jCBSpieltage);
				jCBSpieltage.setModel(new DefaultComboBoxModel<String>(hilfsarray));
				jCBSpieltage.setBounds(REC_COMBO);
				jCBSpieltage.setFocusable(false);
				jCBSpieltage.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						jCBSpieltageItemStateChanged(evt);
					}
				});
			}
			{
				jBtnDeduction = new JButton();
				this.add(jBtnDeduction);
				jBtnDeduction.setBounds(REC_BTNDEDUCTION);
				jBtnDeduction.setText("Punktabzug");
				jBtnDeduction.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnDeductionActionPerformed();
					}
				});
			}
			{
				jBtnTabelleSichern = new JButton();
				this.add(jBtnTabelleSichern);
				jBtnTabelleSichern.setBounds(REC_SAVETABLE);
				jBtnTabelleSichern.setText("Sichern");
				jBtnTabelleSichern.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnTabelleSichernActionPerformed();
					}
				});
			}
			{
				jLblHeimtabelle = new JLabel();
				this.add(jLblHeimtabelle);
				jLblHeimtabelle.setBounds(REC_HOMETABLE);
				jLblHeimtabelle.setText("Heimtabelle");
				jLblHeimtabelle.setHorizontalAlignment(SwingConstants.CENTER);
				jLblHeimtabelle.setCursor(handCursor);
				jLblHeimtabelle.setBackground(colorTabellenart);
				jLblHeimtabelle.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jBtnAndereTabellenart(Tabellenart.HOME);
					}
				});
			}
			{
				jLblGesamttabelle = new JLabel();
				this.add(jLblGesamttabelle);
				jLblGesamttabelle.setBounds(REC_COMPLETETABLE);
				jLblGesamttabelle.setText("Gesamttabelle");
				jLblGesamttabelle.setHorizontalAlignment(SwingConstants.CENTER);
				jLblGesamttabelle.setCursor(handCursor);
				jLblGesamttabelle.setBackground(colorTabellenart);
				jLblGesamttabelle.setOpaque(true);
				jLblGesamttabelle.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jBtnAndereTabellenart(Tabellenart.COMPLETE);
					}
				});
			}
			{
				jLblAuswaertstabelle = new JLabel();
				this.add(jLblAuswaertstabelle);
				jLblAuswaertstabelle.setBounds(REC_AWAYTABLE);
				jLblAuswaertstabelle.setText("Auswaertstabelle");
				jLblAuswaertstabelle.setHorizontalAlignment(SwingConstants.CENTER);
				jLblAuswaertstabelle.setCursor(handCursor);
				jLblAuswaertstabelle.setBackground(colorTabellenart);
				jLblAuswaertstabelle.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jBtnAndereTabellenart(Tabellenart.AWAY);
					}
				});
			}
			{
				jLblPunktabzuege = new JLabel();
				this.add(jLblPunktabzuege);
				jLblPunktabzuege.setText("abgezogene Punkte");
				jLblPunktabzuege.setVisible(false);
			}
			{
				jBtnFertig = new JButton();
				this.add(jBtnFertig);
				jBtnFertig.setBounds(REC_FERTIG);
				jBtnFertig.setText("Fertig");
				jBtnFertig.setVisible(false);
				jBtnFertig.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnFertigActionPerformed();
					}
				});
			}
			
			this.setSize(sumofwidthes + 20, starty - (gapy / 2) + ANZAHL_TEAMS * (height + gapy) + 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Hintergrund
		g.setColor(new Color(212, 212, 212));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Color colorone = new Color(0, 223, 255);
		Color colortwo = new Color(79, 127, 255);
		
		int diffRed = colortwo.getRed() - colorone.getRed();
		int diffGreen = colortwo.getGreen() - colorone.getGreen();
		int diffBlue = colortwo.getBlue() - colorone.getBlue();
		
		for (int i = 0; i < this.getHeight(); i++) {
			g.setColor(new Color(colorone.getRed() + (diffRed * i / (this.getHeight())), colorone.getGreen() + (diffGreen * i / (this.getHeight())), colorone.getBlue() + (diffBlue * i / (this.getHeight()))));
			g.drawLine(0, i, this.getWidth() - 1, i);
		}
		
		int hstartx = 10;
		int hstarty = starty - (gapy / 2);
		int hheight = height + gapy;
		int sumofwidthes = 0;
		for (int j = 0; j < widthes.length; j++) {
			sumofwidthes += widthes[j] + gapx[j];
		}
		
		if (belongsToALeague) {
			// Champions League
			g.setColor(colorCategory1);
			g.fillRect(hstartx, hstarty, sumofwidthes, ANZAHL_CL * hheight);
			// CL Qualifikation
			g.setColor(colorCategory2);
			g.fillRect(hstartx, hstarty + ANZAHL_CL * hheight, sumofwidthes, ANZAHL_CLQ * hheight);
			// Europa League
			g.setColor(colorCategory3);
			g.fillRect(hstartx, hstarty + (ANZAHL_CL + ANZAHL_CLQ) * hheight, sumofwidthes, ANZAHL_EL * hheight);
			// Relegation
			g.setColor(colorCategory4);
			g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_ABS - ANZAHL_REL) * hheight, sumofwidthes, ANZAHL_REL * hheight);
			// Absteiger
			g.setColor(colorCategory5);
			g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_ABS) * hheight, sumofwidthes, ANZAHL_ABS * hheight);
		} else {
			// KO Runde
			g.setColor(colorCategory1);
			g.fillRect(hstartx, hstarty, sumofwidthes, ANZAHL_KORUNDE * hheight);
			// (optional) Zwischenrunde des naechsttieferen Wettbewerbs zB 3. Platz der CL-Gruppen steigt in die EL ab
			g.setColor(colorCategory4);
			g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_AUSGESCHIEDEN - ANZAHL_ZWISCHENRUNDE) * hheight, sumofwidthes, ANZAHL_ZWISCHENRUNDE * hheight);
			// Ausgeschieden
			g.setColor(colorCategory5);
			g.fillRect(hstartx, hstarty + (ANZAHL_TEAMS - ANZAHL_AUSGESCHIEDEN) * hheight, sumofwidthes, ANZAHL_AUSGESCHIEDEN * hheight);
		}
	}
	
	public void labelsbefuellen() {
		int nextPlace = 0; // index in der Tabelle
		int lastIndexedPlace = 0;
		
		Mannschaft[] mannschaften = wettbewerb.getMannschaften();
		
		for (int i = 0; i < tabelle.length; i++) {
			for (Mannschaft ms : mannschaften) {
				if (ms.getPlace() == i) {
					for (int j = 0; j < tabelle[i].length; j++) {
						tabelle[nextPlace][j].setText(ms.getString(j));
						tabelle[nextPlace][j].repaint();
					}
					if (nextPlace >= 1) {
						if (tabelle[nextPlace][0].getText().equals(tabelle[lastIndexedPlace][0].getText())) {
							tabelle[nextPlace][0].setText("");
						} else {
							lastIndexedPlace = nextPlace;
						}
					}
					teamIndices[nextPlace] = ms.getId();
					nextPlace++;
				}
			}
		}
	}
	
	public void resetCurrentMatchday() {
		currentMatchday = -1;
	}
	
	public void aktualisieren() {
		if (currentMatchday == -1) {
			jCBSpieltage.setSelectedIndex(wettbewerb.getCurrentMatchday());
			if (wettbewerb.getCurrentMatchday() == 0)	currentMatchday = 0;
			else return;
		}
		if (aktuelleTabellenart == null)	aktuelleTabellenart = Tabellenart.COMPLETE;
		for (Mannschaft ms : wettbewerb.getMannschaften()) {
			ms.compareWithOtherTeams(wettbewerb.getMannschaften(), currentMatchday, aktuelleTabellenart);
		}
		
		this.labelsbefuellen();
	}

	private void jCBSpieltageItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			currentMatchday = jCBSpieltage.getSelectedIndex();
			aktualisieren();
		}
	}
	
	private void jBtnDeductionActionPerformed() {
		jCBSpieltage.setVisible(false);
		jBtnDeduction.setVisible(false);
		jBtnTabelleSichern.setVisible(false);
		jBtnFertig.setVisible(true);
		
		for (int i = 0; i < tabelle.length; i++) {
			for (int j = 0; j < tabelle[i].length; j++) {
				if (j != 1) {
					tabelle[i][j].setVisible(false);
					titelleiste[j].setVisible(false);
				}
			}
		}
		
		Mannschaft[] mannschaften = wettbewerb.getMannschaften();
		if (jTFPunktabzuege == null) {
			int offset = 0;
			for (int i = 0; i < 2; i++) {
				offset += widthes[i] + gapx[i];
			}
			jLblPunktabzuege.setBounds(startx + offset, starty - (height + gapy), widthPAlbl, height);
			
			jTFPunktabzuege = new JTextField[mannschaften.length];
			for (int i = 0; i < jTFPunktabzuege.length; i++) {
				final int x = i;
				jTFPunktabzuege[i] = new JTextField();
				this.add(jTFPunktabzuege[i]);
				jTFPunktabzuege[i].setBounds(startx + offset, starty + i * (height + gapy) - 3, widthPAtf, height + 6);
				jTFPunktabzuege[i].setHorizontalAlignment(SwingConstants.CENTER);
				jTFPunktabzuege[i].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if ((jTFPunktabzuege[x].getText().length() >= 2 && !jTFPunktabzuege[x].getText().equals("-1"))
								|| arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						}
					}
				});
				jTFPunktabzuege[i].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent arg0) {
						jTFPunktabzuege[x].selectAll();
					}
				});
			}
		}
		jLblPunktabzuege.setVisible(true);
		for (int i = 0; i < jTFPunktabzuege.length; i++) {
			int dP = -mannschaften[teamIndices[i] - 1].getDeductedPoints();
			jTFPunktabzuege[i].setText("" + dP);
			jTFPunktabzuege[i].setVisible(true);
		}
	}
	
	private void jBtnTabelleSichernActionPerformed() {
		String[] order = new String[tabelle.length];
		String dateiname = start.workspace;
		log("There are " + order.length + " teams.");
		for (int i = 0; i < order.length; i++) {
			if (belongsToALeague)	order[i] = season.getTeamWithName(tabelle[i][1].getText()).toString();
			else					order[i] = gruppe.getTeamWithName(tabelle[i][1].getText()).toString();
			log((i + 1) + ". " + order[i]);
		}
		if (belongsToALeague) {
			dateiname = season.getWorkspace() + File.separator + "Tabelle.txt";
		} else {
			dateiname += gruppe.getWorkspace() + "Tabelle.txt";
		}
		log(dateiname);
		inDatei(dateiname, order);
	}
	
	private void jBtnAndereTabellenart(Tabellenart tabellenart) {
		if (aktuelleTabellenart == tabellenart)	return;
		
		aktuelleTabellenart = tabellenart;
		aktualisieren();
		
		jLblHeimtabelle.setOpaque(false);
		jLblGesamttabelle.setOpaque(false);
		jLblAuswaertstabelle.setOpaque(false);
		
		switch (aktuelleTabellenart) {
			case HOME:
				jLblHeimtabelle.setOpaque(true);
				break;
			case COMPLETE:
				jLblGesamttabelle.setOpaque(true);
				break;
			case AWAY:
				jLblAuswaertstabelle.setOpaque(true);
				break;
		}
		
		repaintImmediately(jLblHeimtabelle);
		repaintImmediately(jLblGesamttabelle);
		repaintImmediately(jLblAuswaertstabelle);
	}
	
	private void jBtnFertigActionPerformed() {
		Mannschaft[] mannschaften = wettbewerb.getMannschaften();
		jLblPunktabzuege.setVisible(false);
		for (int i = 0; i < jTFPunktabzuege.length; i++) {
			int dP = -Integer.parseInt(jTFPunktabzuege[i].getText());
			mannschaften[teamIndices[i] - 1].setDeductedPoints(dP);
			jTFPunktabzuege[i].setVisible(false);
		}
		
		for (int i = 0; i < tabelle.length; i++) {
			for (int j = 0; j < tabelle[i].length; j++) {
				tabelle[i][j].setVisible(true);
				titelleiste[j].setVisible(true);
			}
		}
		
		jCBSpieltage.setVisible(true);
		jBtnDeduction.setVisible(true);
		jBtnTabelleSichern.setVisible(true);
		jBtnFertig.setVisible(false);
		
		aktualisieren();
	}
}

enum Tabellenart {
	HOME, COMPLETE, AWAY
}
