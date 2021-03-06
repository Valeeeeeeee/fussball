package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static util.Utilities.*;

public class Tabelle extends JPanel {
	private static final long serialVersionUID = 2308780445852600421L;
	
	private Wettbewerb competition;
	private LigaSaison season;
	private Gruppe group;
	
	private int currentMatchday = -1;
	private Tabellenart currentTableType;
	
	private boolean saveTableWithData = true;
	private boolean belongsToALeague = false;
	
	private int[] teamIndices;
	
	private int numberOfTeams;
	
	private int numberOfCat1;
	private int numberOfCat2;
	private int numberOfCat3;
	private int numberOfCat4;
	private int numberOfCat5;
	
	private String[] headers = {"Pl.", "Verein", "Sp.", "G", "U", "V", "T+", "T-", "+/-", "Pkt."};
	private Color colorTabellenart = new Color(255, 255, 128);
	
	private int startx = 10;
	private int starty = 110;
	private int[] widthes = {20, 220, 20, 20, 20, 20, 25, 25, 25, 25};
	private int height = 15;
	private int[] gapx = {5, 5, 5, 0, 0, 5, 0, 5, 5, 0};
	private int gapy = 15;
	private int widthPDtf = 35;
	private int widthPDlbl = 150;
	
	private Rectangle REC_CBMATCHDAYS = new Rectangle(120, 10, 130, 30);
	private Rectangle REC_BTNDEDUCTION = new Rectangle(260, 10, 110, 30);
	private Rectangle REC_SAVETABLE = new Rectangle(380, 10, 80, 30);
	private Rectangle REC_HOMETABLE = new Rectangle(10, 50, 80, 20);
	private Rectangle REC_COMPLETETABLE = new Rectangle(190, 50, 90, 20);
	private Rectangle REC_AWAYTABLE = new Rectangle(350, 50, 110, 20);
	private Rectangle REC_DONE = new Rectangle(380, 10, 80, 30);
	
	private JComboBox<String> jCBMatchdays;
	private JButton jBtnDeduction;
	private JButton jBtnSaveTable;
	private JLabel jLblHomeTable;
	private JLabel jLblCompleteTable;
	private JLabel jLblAwayTable;
	private JLabel[] jLblsHeaders;
	private JLabel[][] jLblsData;
	private JLabel jLblPointDeductions;
	private JTextField[] jTFsPointDeductions;
	private JButton jBtnDone;
	
	public Tabelle(Gruppe group) {
		super();
		this.group = group;
		competition = group;
		belongsToALeague = false;
		
		numberOfTeams = group.getNumberOfTeams();
		numberOfCat1 = 2;
		numberOfCat4 = 0;
		numberOfCat5 = numberOfTeams - numberOfCat1 - numberOfCat4;
		
		initGUI();
	}
	
	public Tabelle(LigaSaison season) {
		super();
		this.season = season;
		competition = season;
		belongsToALeague = true;
		
		numberOfTeams = season.getNumberOfTeams();
		numberOfCat1 = season.getNumberOf(0);
		numberOfCat2 = season.getNumberOf(1);
		numberOfCat3 = season.getNumberOf(2);
		numberOfCat4 = season.getNumberOf(3);
		numberOfCat5 = season.getNumberOf(4);
		
		initGUI();
	}
	
	public void initGUI() { 
		try { 
			this.setLayout(null);
			
			jLblsData = new JLabel[numberOfTeams][headers.length];
			jLblsHeaders = new JLabel[headers.length];
			
			teamIndices = new int[numberOfTeams];
			
			int sumofwidthes = 0;
			
			for (int j = 0; j < jLblsHeaders.length; j++) {
				jLblsHeaders[j] = new JLabel();
				this.add(jLblsHeaders[j]);
				if (j == 1) {
					alignLeft(jLblsHeaders[j]);
					jLblsHeaders[j].setCursor(handCursor);
				} else {
					alignCenter(jLblsHeaders[j]);
				}
				jLblsHeaders[j].setBounds(startx + sumofwidthes, starty - (height + gapy), widthes[j], height);
				jLblsHeaders[j].setText(headers[j]);
				sumofwidthes += widthes[j] + gapx[j];
			}
			
			for (int i = 0; i < jLblsData.length; i++) {
				sumofwidthes = 0;
				for (int j = 0; j < jLblsData[i].length; j++) {
					jLblsData[i][j] = new JLabel();
					this.add(jLblsData[i][j]);
					if (j == 1) {
						final int x = i;
						alignLeft(jLblsData[i][j]);
						jLblsData[i][j].setCursor(handCursor);
						jLblsData[i][j].addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								String teamName = jLblsData[x][1].getText();
								jBtnChangeTableType(Tabellenart.COMPLETE);
								if (belongsToALeague)	jCBMatchdays.setSelectedIndex(competition.getCurrentMatchday());
								Fussball.getInstance().uebersichtAnzeigen(teamName);
							}
						});
					} else {
						alignCenter(jLblsData[i][j]);
					}
					jLblsData[i][j].setBounds(startx + sumofwidthes, starty + i * (height + gapy), widthes[j], height);
					sumofwidthes += widthes[j] + gapx[j];
				}
			}
			{
				String[] hilfsarray = new String[competition.getNumberOfMatchdays()];
				for (int i = 0; i < competition.getNumberOfMatchdays(); i++) {
					hilfsarray[i] = (i + 1) + ". Spieltag";
				}
				jCBMatchdays = new JComboBox<String>();
				this.add(jCBMatchdays);
				jCBMatchdays.setModel(new DefaultComboBoxModel<String>(hilfsarray));
				jCBMatchdays.setBounds(REC_CBMATCHDAYS);
				jCBMatchdays.setFocusable(false);
				jCBMatchdays.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						jCBMatchdaysItemStateChanged(evt);
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
				jBtnSaveTable = new JButton();
				this.add(jBtnSaveTable);
				jBtnSaveTable.setBounds(REC_SAVETABLE);
				jBtnSaveTable.setText("Sichern");
				jBtnSaveTable.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnSaveTableActionPerformed();
					}
				});
			}
			{
				jLblHomeTable = new JLabel();
				this.add(jLblHomeTable);
				jLblHomeTable.setBounds(REC_HOMETABLE);
				jLblHomeTable.setText("Heimtabelle");
				alignCenter(jLblHomeTable);
				jLblHomeTable.setCursor(handCursor);
				jLblHomeTable.setBackground(colorTabellenart);
				jLblHomeTable.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jBtnChangeTableType(Tabellenart.HOME);
					}
				});
			}
			{
				jLblCompleteTable = new JLabel();
				this.add(jLblCompleteTable);
				jLblCompleteTable.setBounds(REC_COMPLETETABLE);
				jLblCompleteTable.setText("Gesamttabelle");
				alignCenter(jLblCompleteTable);
				jLblCompleteTable.setCursor(handCursor);
				jLblCompleteTable.setBackground(colorTabellenart);
				jLblCompleteTable.setOpaque(true);
				jLblCompleteTable.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jBtnChangeTableType(Tabellenart.COMPLETE);
					}
				});
			}
			{
				jLblAwayTable = new JLabel();
				this.add(jLblAwayTable);
				jLblAwayTable.setBounds(REC_AWAYTABLE);
				jLblAwayTable.setText("Auswärtstabelle");
				alignCenter(jLblAwayTable);
				jLblAwayTable.setCursor(handCursor);
				jLblAwayTable.setBackground(colorTabellenart);
				jLblAwayTable.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jBtnChangeTableType(Tabellenart.AWAY);
					}
				});
			}
			{
				jLblPointDeductions = new JLabel();
				this.add(jLblPointDeductions);
				jLblPointDeductions.setText("abgezogene Punkte");
				jLblPointDeductions.setVisible(false);
			}
			{
				jBtnDone = new JButton();
				this.add(jBtnDone);
				jBtnDone.setBounds(REC_DONE);
				jBtnDone.setText("Fertig");
				jBtnDone.setVisible(false);
				jBtnDone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnDoneActionPerformed();
					}
				});
			}
			
			setSize(sumofwidthes + 20, starty - (gapy / 2) + numberOfTeams * (height + gapy) + 10);
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
		
		g.setColor(colorCategory1);
		g.fillRect(hstartx, hstarty, sumofwidthes, numberOfCat1 * hheight);
		
		g.setColor(colorCategory2);
		g.fillRect(hstartx, hstarty + numberOfCat1 * hheight, sumofwidthes, numberOfCat2 * hheight);
		
		g.setColor(colorCategory3);
		g.fillRect(hstartx, hstarty + (numberOfCat1 + numberOfCat2) * hheight, sumofwidthes, numberOfCat3 * hheight);
		
		g.setColor(colorCategory4);
		g.fillRect(hstartx, hstarty + (numberOfTeams - numberOfCat5 - numberOfCat4) * hheight, sumofwidthes, numberOfCat4 * hheight);
		
		g.setColor(colorCategory5);
		g.fillRect(hstartx, hstarty + (numberOfTeams - numberOfCat5) * hheight, sumofwidthes, numberOfCat5 * hheight);
	}
	
	private void fillLabels() {
		int nextPlace = 0; // index in der Tabelle
		int lastIndexedPlace = 0;
		
		Mannschaft[] teams = competition.getTeams();
		
		for (int i = 0; i < jLblsData.length; i++) {
			for (Mannschaft ms : teams) {
				if (ms.getPlace() == i) {
					for (int j = 0; j < jLblsData[i].length; j++) {
						jLblsData[nextPlace][j].setText(ms.getString(j));
						jLblsData[nextPlace][j].repaint();
					}
					if (nextPlace >= 1) {
						if (jLblsData[nextPlace][0].getText().equals(jLblsData[lastIndexedPlace][0].getText())) {
							jLblsData[nextPlace][0].setText("");
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
	
	public void refresh() {
		if (currentMatchday == -1) {
			jCBMatchdays.setSelectedIndex(competition.getCurrentMatchday());
			if (competition.getCurrentMatchday() == 0)	currentMatchday = 0;
			else return;
		}
		if (currentTableType == null)	currentTableType = Tabellenart.COMPLETE;
		for (Mannschaft ms : competition.getTeams()) {
			ms.compareWithOtherTeams(competition.getTeams(), currentMatchday, currentTableType);
		}
		
		fillLabels();
	}

	private void jCBMatchdaysItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			currentMatchday = jCBMatchdays.getSelectedIndex();
			refresh();
		}
	}
	
	private void jBtnDeductionActionPerformed() {
		jCBMatchdays.setVisible(false);
		jBtnDeduction.setVisible(false);
		jBtnSaveTable.setVisible(false);
		jBtnDone.setVisible(true);
		
		for (int i = 0; i < jLblsData.length; i++) {
			for (int j = 0; j < jLblsData[i].length; j++) {
				if (j != 1) {
					jLblsData[i][j].setVisible(false);
					jLblsHeaders[j].setVisible(false);
				}
			}
		}
		
		Mannschaft[] teams = competition.getTeams();
		if (jTFsPointDeductions == null) {
			int offset = 0;
			for (int i = 0; i < 2; i++) {
				offset += widthes[i] + gapx[i];
			}
			jLblPointDeductions.setBounds(startx + offset, starty - (height + gapy), widthPDlbl, height);
			
			jTFsPointDeductions = new JTextField[teams.length];
			for (int i = 0; i < jTFsPointDeductions.length; i++) {
				final int x = i;
				jTFsPointDeductions[i] = new JTextField();
				this.add(jTFsPointDeductions[i]);
				jTFsPointDeductions[i].setBounds(startx + offset, starty + i * (height + gapy) - 3, widthPDtf, height + 6);
				alignCenter(jTFsPointDeductions[i]);
				jTFsPointDeductions[i].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if ((jTFsPointDeductions[x].getText().length() >= 2 && !jTFsPointDeductions[x].getText().equals("-1"))
								|| arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						}
					}
				});
				jTFsPointDeductions[i].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent arg0) {
						jTFsPointDeductions[x].selectAll();
					}
				});
			}
		}
		jLblPointDeductions.setVisible(true);
		for (int i = 0; i < jTFsPointDeductions.length; i++) {
			int dP = -teams[teamIndices[i] - 1].getDeductedPoints();
			jTFsPointDeductions[i].setText("" + dP);
			jTFsPointDeductions[i].setVisible(true);
		}
	}
	
	private void jBtnSaveTableActionPerformed() {
		String[] order = new String[jLblsData.length];
		String fileName = Fussball.getInstance().getWorkspace();
		log("There are " + order.length + " teams.");
		for (int i = 0; i < order.length; i++) {
			if (saveTableWithData) {
				order[i] = jLblsData[i][1].getText() + ";" + jLblsData[i][9].getText() + ";" + jLblsData[i][6].getText() + ";" + jLblsData[i][7].getText() + ";";
			} else {
				if (belongsToALeague)	order[i] = season.getTeamWithName(jLblsData[i][1].getText()).toString();
				else					order[i] = group.getTeamWithName(jLblsData[i][1].getText()).toString();
			}
			log((i + 1) + ". " + order[i]);
		}
		if (belongsToALeague) {
			fileName = season.getWorkspace() + "Tabelle.txt";
		} else {
			fileName += group.getWorkspace() + "Tabelle.txt";
		}
		log(fileName);
		writeFile(fileName, order);
	}
	
	private void jBtnChangeTableType(Tabellenart tableType) {
		if (currentTableType == tableType)	return;
		
		currentTableType = tableType;
		refresh();
		
		jLblHomeTable.setOpaque(false);
		jLblCompleteTable.setOpaque(false);
		jLblAwayTable.setOpaque(false);
		
		switch (currentTableType) {
			case HOME:
				jLblHomeTable.setOpaque(true);
				break;
			case COMPLETE:
				jLblCompleteTable.setOpaque(true);
				break;
			case AWAY:
				jLblAwayTable.setOpaque(true);
				break;
		}
		
		repaintImmediately(jLblHomeTable);
		repaintImmediately(jLblCompleteTable);
		repaintImmediately(jLblAwayTable);
	}
	
	private void jBtnDoneActionPerformed() {
		Mannschaft[] teams = competition.getTeams();
		jLblPointDeductions.setVisible(false);
		for (int i = 0; i < jTFsPointDeductions.length; i++) {
			int dP = -Integer.parseInt(jTFsPointDeductions[i].getText());
			teams[teamIndices[i] - 1].setDeductedPoints(dP);
			jTFsPointDeductions[i].setVisible(false);
		}
		
		for (int i = 0; i < jLblsData.length; i++) {
			for (int j = 0; j < jLblsData[i].length; j++) {
				jLblsData[i][j].setVisible(true);
				jLblsHeaders[j].setVisible(true);
			}
		}
		
		jCBMatchdays.setVisible(true);
		jBtnDeduction.setVisible(true);
		jBtnSaveTable.setVisible(true);
		jBtnDone.setVisible(false);
		
		refresh();
	}
}

enum Tabellenart {
	HOME, COMPLETE, AWAY
}
