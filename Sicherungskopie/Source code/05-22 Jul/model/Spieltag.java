package model;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Spieltag extends JPanel {
	private static final long serialVersionUID = 533273470193095401L;

	private JComboBox jCBSpieltage;
	public JLabel[] mannschaften;
	public JTextField[] tore;
	private JButton bearbeiten;
	private JButton fertig;
	private JLabel[] spieltagsdaten;

	int[][] array;
	int edited_date;
	int edited_team = -1;
	int edited_matchday = -1;
	int currentMatchday = -1;

	final int STARTX = 0;
	final int STARTY = 1;
	final int GAPX = 2;
	final int GAPY = 3;
	final int WIDTH = 4;
	final int HEIGHT = 5;

	private int[] labels = { 160, 140, 120, 10, 120, 30 };

	private int[] textfields = { labels[STARTX] + labels[WIDTH] + 15, labels[STARTY], 
			labels[GAPX] - 30 - 2 * 40, labels[GAPY], 40, labels[HEIGHT] };

	private Rectangle posbearbfertig = new Rectangle(440, 10, 160, 30);
	private Rectangle poscombobox = new Rectangle(220, 10, 200, 30);

	private  JPanel mannschaftenauswahl;
	private  JButton[] mannschaftenbtns;
	int[] buttonsauswahl = new int[6];

	private int countTeams;
	private int numberOfMatches;
	private int countMatchdays;
	private int halfCountTeamsRoundUp;

	private boolean belongsToALeague = false;
	private boolean belongsToKORound = false;
	private Liga liga;
	private Gruppe gruppe;
	private KORunde koRunde;
	private int KORound = -1; // TODO muss weg

	public Spieltag(Liga liga) {
		super();

		this.liga = liga;
		this.belongsToALeague = true;
		this.belongsToKORound = false;

		this.numberOfMatches = liga.getAnzahlSpiele();
		this.countTeams = liga.getAnzahlTeams();
		this.countMatchdays = liga.getAnzahlSpieltage();
		this.halfCountTeamsRoundUp = liga.getHalbeAnzMSAuf();

		initGUI();
	}

	public Spieltag(Gruppe gruppe) {
		super();

		this.gruppe = gruppe;
		this.belongsToALeague = false;
		this.belongsToKORound = false;

		this.numberOfMatches = gruppe.getNumberOfMatchesPerMatchday();
		this.countTeams = gruppe.getNumberOfTeams();
		this.countMatchdays = gruppe.getNumberOfMatchdays();
		this.halfCountTeamsRoundUp = (this.countTeams % 2 == 0 ? this.countTeams / 2 : this.countTeams / 2 + 1);

		initGUI();
	}
	
	public Spieltag(KORunde koRunde) {
		super();
		
		this.koRunde = koRunde;
		belongsToALeague = false;
		belongsToKORound = true;
		this.KORound = this.koRunde.getID();
		
		this.numberOfMatches = this.koRunde.getNumberOfMatchesPerMatchday();
		this.countTeams = 2 * numberOfMatches;
		this.countMatchdays = this.koRunde.hasSecondLeg() ? 2 : 1;
		this.halfCountTeamsRoundUp = this.countTeams / 2;
		
		initGUI();
	}

	private void calculateButtonsauswahlBounds(int maxHeight) {
		buttonsauswahl[STARTX] = 10;
		buttonsauswahl[STARTY] = 10;
		buttonsauswahl[GAPX] = 10;
		buttonsauswahl[GAPY] = 10;
		buttonsauswahl[WIDTH] = 200;
		buttonsauswahl[HEIGHT] = 50;
		
		if ((2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]) > maxHeight) {
			buttonsauswahl[HEIGHT] = (maxHeight - (halfCountTeamsRoundUp + 1) * buttonsauswahl[GAPY]) / halfCountTeamsRoundUp;
			JOptionPane.showMessageDialog(null, "Der berechnete Wert für height liegt bei " + buttonsauswahl[HEIGHT] + " mit hAnzAuf = "
							+ halfCountTeamsRoundUp, "\"height\" wurde berechnet", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void initGUI() {
		try {
			this.setLayout(null);

			// TODO min und maxheight sinnvoll setzen
			int minimumheight = 450;
			int maximumheight = 800;
			calculateButtonsauswahlBounds(maximumheight);

			Dimension dim = new Dimension();
			dim.width = 1200;

			int heightOfTeamLabels = labels[STARTY] + numberOfMatches * (labels[HEIGHT] + labels[GAPY]) + 20;
			int heightOfTeamSelection = 2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY])
					- buttonsauswahl[GAPY];

			dim.height = (heightOfTeamLabels > heightOfTeamSelection ? heightOfTeamLabels : heightOfTeamSelection);
//			JOptionPane.showMessageDialog(null, "labelsHeight = " + heightOfTeamLabels + " oder buttonsHeight = " + heightOfTeamSelection
//					+ " macht: " + dim.height);

			// correction to minimumheight or maximumheight if out of these bounds
			if (dim.height < minimumheight) {
				dim.height = minimumheight;
			} else if (dim.height > maximumheight) {
				dim.height = maximumheight;
			}

			this.setSize(dim);
//			JOptionPane.showMessageDialog(null, "This is dim: " + dim);

			
			mannschaftenbtns = new JButton[countTeams];
			mannschaften = new JLabel[2 * numberOfMatches];
			tore = new JTextField[2 * numberOfMatches];
			array = new int[numberOfMatches][2];
			spieltagsdaten = new JLabel[numberOfMatches];
			
			{
				String[] hilfsarray = new String[countMatchdays];
				for (int i = 0; i < countMatchdays; i++) {
					hilfsarray[i] = (i + 1) + ". Spieltag";
				}
				ComboBoxModel jCB1Model = new DefaultComboBoxModel(hilfsarray);
				jCBSpieltage = new JComboBox();
				this.add(jCBSpieltage);
				jCBSpieltage.setModel(jCB1Model);
				jCBSpieltage.setBounds(poscombobox);
				jCBSpieltage.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						jCBSpieltageItemStateChanged(evt);
					}
				});
			}
			
			for (int i = 0; i < spieltagsdaten.length; i++) {
				final int x = i;
				spieltagsdaten[i] = new JLabel();
				this.add(spieltagsdaten[i]);
				spieltagsdaten[i].setBounds(30, labels[STARTY] + i * (labels[HEIGHT] + labels[GAPY]), 120, labels[HEIGHT]);
				spieltagsdaten[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
//				spieltagsdaten[i].setOpaque(true);
//				spieltagsdaten[i].setBackground(null);
				spieltagsdaten[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						datumsLabelClicked(x);
					}
				});
			}
			
			for (int i = 0; i < mannschaften.length; i++) {
				final int x = i;
				int zeile = i % numberOfMatches;
				int spalte = i / numberOfMatches;

				mannschaften[i] = new JLabel();
				this.add(mannschaften[i]);
				mannschaften[i].setBounds(labels[STARTX] + spalte * (labels[WIDTH] + labels[GAPX]), 
						labels[STARTY] + zeile * (labels[HEIGHT] + labels[GAPY]), labels[WIDTH], labels[HEIGHT]);
				if (spalte == 0)	mannschaften[i].setHorizontalAlignment(SwingConstants.RIGHT);
				else				mannschaften[i].setHorizontalAlignment(SwingConstants.LEFT);
				mannschaften[i].setEnabled(false);
				mannschaften[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
//				mannschaften[i].setOpaque(true);
//				mannschaften[i].setBackground(null);
				mannschaften[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (mannschaften[x].isEnabled()) {
							mannschaftClicked(x);
						}
					}
				});
			}
			
			for (int i = 0; i < tore.length; i++) {
				final int x = i;
				tore[i] = new JTextField();
				this.add(tore[i]);
				tore[i].setBounds(textfields[STARTX] + (i / numberOfMatches) * (textfields[WIDTH] + textfields[GAPX]), textfields[STARTY] + (i % numberOfMatches) * (textfields[HEIGHT] + textfields[GAPY]), textfields[WIDTH], textfields[HEIGHT]);
				tore[i].setHorizontalAlignment(SwingConstants.CENTER);
				tore[i].addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent arg0) {
						if ((tore[x].getText().length() >= 2 && !tore[x].getText().equals("-1")) || arg0.getKeyChar() <= 47 || arg0.getKeyChar() >= 58) {
							arg0.consume();
						}
					}
				});
				tore[i].addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent arg0) {
						tore[x].selectAll();
					}
				});
			}
			
			{
				bearbeiten = new JButton();
				this.add(bearbeiten);
				bearbeiten.setBounds(posbearbfertig);
				bearbeiten.setText("Spielplan bearbeiten");
				bearbeiten.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						bearbeitenActionPerformed();
					}
				});
			}
			{
				fertig = new JButton();
				this.add(fertig);
				fertig.setBounds(posbearbfertig);
				fertig.setText("Speichern");
				fertig.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fertigActionPerformed();
					}
				});
				fertig.setVisible(false);
			}
			
			{
				mannschaftenauswahl = new JPanel();
				this.add(mannschaftenauswahl);
				mannschaftenauswahl.setLayout(null);
				mannschaftenauswahl.setSize(2 * (buttonsauswahl[STARTX] + buttonsauswahl[WIDTH]) + buttonsauswahl[GAPX], 2 * buttonsauswahl[STARTY]
						+ halfCountTeamsRoundUp * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]);
				mannschaftenauswahl.setLocation(680, (this.getSize().height - mannschaftenauswahl.getSize().height) / 2);
				mannschaftenauswahl.setVisible(false);
				mannschaftenauswahl.setOpaque(true);
				mannschaftenauswahl.setBackground(Color.red);
			}

			for (int i = 0; i < countTeams; i++) {
				final int x = i; // für den ActionListener
				int xfactor = i / halfCountTeamsRoundUp, yfactor = i % halfCountTeamsRoundUp;
				mannschaftenbtns[i] = new JButton();
				mannschaftenauswahl.add(mannschaftenbtns[i]);
				mannschaftenbtns[i].setBounds(buttonsauswahl[STARTX] + xfactor * (buttonsauswahl[WIDTH] + buttonsauswahl[GAPX]),
								buttonsauswahl[STARTY] + yfactor * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY]), buttonsauswahl[WIDTH], buttonsauswahl[HEIGHT]);
				mannschaftenbtns[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mannschaftenButtonClicked(x);
						mannschaftenauswahl.setVisible(false);
					}
				});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.clearRect(2, 2, getWidth() - 4, getHeight() - 4);

		// Hintergrund
		for (int i = 2; i < this.getHeight() - 2; i++) {
			g.setColor(new Color(0, 128 + (128 * i / (this.getHeight() - 2)), 0));
			g.drawLine(2, i, this.getWidth() - 3, i);
		}
	}

	public int getNumberOfMatches() {
		return this.numberOfMatches;
	}
	
	public void jCBSpieltageItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			if (belongsToALeague) {
				liga.ergebnisseSichern();
				spieltagAnzeigen();
			} else if (!belongsToKORound) {
				gruppe.ergebnisseSichern();
				spieltagAnzeigen();
			} else {
				koRunde.ergebnisseSichern();
				spieltagAnzeigen();
			}
		}
	}
	
	private void setLabelsEnabled(boolean bool) {
		for (JLabel lbl : mannschaften) {
			lbl.setEnabled(bool);
			lbl.setOpaque(false);
		}
	}

	private void setTFsEditable(boolean bool) {
		for (JTextField tf : tore) {
			tf.setEditable(bool);
		}
	}

	private void spieltagsdatenBefuellen() {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		for (int i = 0; i < numberOfMatches; i++) {
			if (belongsToALeague)		dateandtimeofmatches[i] = liga.getDateOf(currentMatchday, i);
			else if (!belongsToKORound)	dateandtimeofmatches[i] = gruppe.getDateOf(currentMatchday, i);
			else						dateandtimeofmatches[i] = koRunde.getDateOf(KORound, currentMatchday, i);
			if (i < 1 || !dateandtimeofmatches[i].equals(dateandtimeofmatches[i - 1]))		spieltagsdaten[i].setText(dateandtimeofmatches[i]);
			else																			spieltagsdaten[i].setText("");
		}
	}
	
	private void setMannschaftenButtonsNames() {
		for (int i = 0; i < mannschaftenbtns.length; i++) {
			if (belongsToALeague)		mannschaftenbtns[i].setText(liga.getMannschaften()[i].getName());
			else if (!belongsToKORound)	mannschaftenbtns[i].setText(gruppe.getMannschaften()[i].getName());
			else						mannschaftenbtns[i].setText(koRunde.getMannschaftenIn(KORound)[i].getName());
		}
	}

	/**
	 * Für Gruppe optimiert, noch nicht für KORunde
	 */
	private void bearbeitenActionPerformed() {
		setMannschaftenButtonsNames();
		if (belongsToALeague) {
			edited_matchday = jCBSpieltage.getSelectedIndex();
			for (int i = 0; i < array.length; i++) {
				array[i][0] = liga.spieltage[edited_matchday][i][0];
				array[i][1] = liga.spieltage[edited_matchday][i][1];
			}
			setLabelsEnabled(true);
			setTFsEditable(false);
			bearbeiten.setVisible(false);
			fertig.setVisible(true);
			jCBSpieltage.setEnabled(false);
		} else if (!belongsToKORound) {
			edited_matchday = jCBSpieltage.getSelectedIndex();
			for (int i = 0; i < array.length; i++) {
				array[i][0] = gruppe.spiele[edited_matchday][i][0];
				array[i][1] = gruppe.spiele[edited_matchday][i][1];
			}
			setLabelsEnabled(true);
			setTFsEditable(false);
			bearbeiten.setVisible(false);
			fertig.setVisible(true);
			jCBSpieltage.setEnabled(false);
		} else {
			// TODO bearbeiten in KO Runde
		}
		
	}

	/**
	 * Für Gruppe optimiert, noch nicht für KORunde
	 * @return
	 */
	public int fertigActionPerformed() {
		if (belongsToKORound) {
			// TODO die Unterschiede beseitigen für KO Runde
			return 0;
		}
		
		
		int fehlerart = -1;
		// 1. Fehlerfall: es gibt doppelte Vorkommnisse im Array
		for (int i = 0; i < 2 * array.length; i++) {
			int r = array[i % array.length][i / array.length];
			for (int j = 0; j < 2 * array.length; j++) {
				if (i != j) {
					int s = array[j % array.length][j / array.length];
					if (r == s) {
						fehlerart = 2;
						break;
					}
				}
			}
			if (fehlerart == 2)		break;
		}
		// 2. Fehlerfall: es befinden sich noch ungesetzte Felder im Array
		for (int i = 0; i < array.length; i++) {
			if (array[i][0] == -1) {
				fehlerart = 1;
				break;
			} else if (array[i][1] == -1) {
				fehlerart = 1;
				break;
			}
		}
		
		int saveanyway = 0;
		if (fehlerart == 2) {
			JOptionPane.showMessageDialog(null, "Es gibt doppelte Vorkommnisse im Array. Beheben Sie diese bevor sie speichern können.", "Fehler", JOptionPane.ERROR_MESSAGE);
			saveanyway = 1;
		} else if (fehlerart == 1) {
			saveanyway = JOptionPane.showConfirmDialog(null, "Es befinden sich noch ungesetzte Felder im Array. \n"
									+ "Nicht vollständige Spieltage werden mit Beenden des Programms gelöscht und nicht gespeichert!\n"
									+ "Trotzdem fortfahren?", "Fehler", JOptionPane.YES_NO_OPTION);
		}

		if (saveanyway == 0) {
			setLabelsEnabled(false);
			if (fehlerart == -1)		setTFsEditable(true);
			else						setTFsEditable(false);
			bearbeiten.setVisible(true);
			fertig.setVisible(false);
			
			for (int i = 0; i < array.length; i++) {
				if (belongsToALeague) {
					liga.spieltage[edited_matchday][i][0] = array[i][0];
					liga.spieltage[edited_matchday][i][1] = array[i][1];
				} else if (!belongsToKORound) {
					gruppe.spiele[edited_matchday][i][0] = array[i][0];
					gruppe.spiele[edited_matchday][i][1] = array[i][1];
				}

				if (array[i][0] != -1 && array[i][1] != -1) {
					if (belongsToALeague) {
						liga.getMannschaften()[array[i][0] - 1].setGegner(edited_matchday, Mannschaft.HOME, array[i][1], i);
						liga.getMannschaften()[array[i][1] - 1].setGegner(edited_matchday, Mannschaft.AWAY, array[i][0], i);
					} else if (!belongsToKORound) {
						gruppe.getMannschaften()[array[i][0] - 1].setGegner(edited_matchday, Mannschaft.HOME, array[i][1], i);
						gruppe.getMannschaften()[array[i][1] - 1].setGegner(edited_matchday, Mannschaft.AWAY, array[i][0], i);
					}
				}
			}

			if (fehlerart == -1) {
				if (belongsToALeague)		liga.spieltageEingetragen[edited_matchday] = true;
				if (!belongsToKORound)		gruppe.spieleEingetragen[edited_matchday] = true;
			} else {
				if (belongsToALeague)		liga.spieltageEingetragen[edited_matchday] = false;
				if (!belongsToKORound)		gruppe.spieleEingetragen[edited_matchday] = false;
			}
			
			edited_matchday = -1;
			jCBSpieltage.setEnabled(true);
			mannschaftenauswahl.setVisible(false);
		}
		return saveanyway;
	}

	public void spieltagAnzeigen() {
		if (currentMatchday == -1) {
			if (belongsToALeague)		currentMatchday = liga.getCurrentMatchday();
			else if (!belongsToKORound)	currentMatchday = gruppe.getCurrentMatchday();
			else						currentMatchday = koRunde.getCurrentMatchday();

			// damit nicht bei setSelectedIndex die default-Inhalte von tore in das ergebnis-Array kopiert werden muss das Befüllen davor erfolgen
			for (int match = 0; match < numberOfMatches; match++) {
				try {
					if (belongsToALeague) {
						
						this.tore[match].setText("" + liga.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + liga.getErgebnis(currentMatchday, match).away());
					} else if (!belongsToKORound) {
						this.tore[match].setText("" + gruppe.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + gruppe.getErgebnis(currentMatchday, match).away());
					} else if (koRunde != null) {
						this.tore[match].setText("" + koRunde.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + koRunde.getErgebnis(currentMatchday, match).away());
					} else {
						this.tore[match].setText("" + koRunde.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + koRunde.getErgebnis(currentMatchday, match).away());
					}
				} catch (Exception e) {
					this.tore[match].setText("" + -1);
					this.tore[match + numberOfMatches].setText("" + -1);
				}
				
				
			}
			
			if (currentMatchday == jCBSpieltage.getSelectedIndex()) {
				// dann gibt es keinen ItemStateChange und die Methode wird nicht aufgerufen
				spieltagAnzeigen();
			}
			
			jCBSpieltage.setSelectedIndex(currentMatchday);
		} else {
			currentMatchday = jCBSpieltage.getSelectedIndex();

			spieltagsdatenBefuellen();
			
			// TODO Umsortierung der Spiele bei Datumsveränderung -> chronologisch
			
			for (int i = 0; i < this.mannschaften.length; i++) {
				this.mannschaften[i].setText("n. a.");
				this.tore[i].setText("" + -1);
			}

			this.setTFsEditable(false);
			
			if (belongsToALeague) {
				if (liga.spieltageEingetragen[currentMatchday]) {
					for (int i = 0; i < this.mannschaften.length; i++) {
						this.mannschaften[i].setText(liga.getMannschaften()[liga.spieltage[currentMatchday][i % numberOfMatches][i / numberOfMatches] - 1].getName());
					}
					this.setTFsEditable(true);
				}
				if (liga.ergebnisseEingetragen[currentMatchday]) {
					for (int match = 0; match < numberOfMatches; match++) {
						this.tore[match].setText("" + liga.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + liga.getErgebnis(currentMatchday, match).away());
					}
				}
			} else if (!belongsToKORound) {
				if (gruppe.spieleEingetragen[currentMatchday]) {
					for (int i = 0; i < this.mannschaften.length; i++) {
						this.mannschaften[i].setText(gruppe.getMannschaften()[gruppe.spiele[currentMatchday][i % numberOfMatches][i / numberOfMatches] - 1].getName());
					}
					this.setTFsEditable(true);
				}
				if (gruppe.ergebnisseEingetragen[currentMatchday]) {
					for (int match = 0; match < numberOfMatches; match++) {
						this.tore[match].setText("" + gruppe.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + gruppe.getErgebnis(currentMatchday, match).away());
					}
				}
			}
			else {
				if (koRunde.spielplanEingetragen[currentMatchday]) {
					for (int i = 0; i < this.mannschaften.length; i++) {
						this.mannschaften[i].setText(koRunde.getMannschaftsNameAt(KORound, currentMatchday, i % numberOfMatches, i / numberOfMatches));
					}
					this.setTFsEditable(true);
				}
				if (koRunde.ergebnisseEingetragen[currentMatchday]) {
					for (int match = 0; match < numberOfMatches; match++) {
						this.tore[match].setText("" + koRunde.getErgebnis(currentMatchday, match).home());
						this.tore[match + numberOfMatches].setText("" + koRunde.getErgebnis(currentMatchday, match).away());
					}
				}
			}
		}	
	}

	public void datumsLabelClicked(int index) {
		edited_date = index;
		
		if (belongsToALeague) {
			MyDateChooser mdc = new MyDateChooser(liga, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);

			mdc.setDate(liga.daten_uhrzeiten[currentMatchday][0], liga.daten_uhrzeiten[currentMatchday][edited_date + 1]);
		} else if (!belongsToKORound) {
			MyDateChooser mdc = new MyDateChooser(gruppe, this);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);

			mdc.setDate(gruppe.datesAndTimes[currentMatchday][0], gruppe.datesAndTimes[currentMatchday][edited_date + 1]);
		} else {
			// TODO
		}
	}

	public void datumEingegeben(int startdate, int aszindex) {
		if (belongsToKORound) {
			// TODO
			return;
		}
		
		System.out.println("Folgendes Datum wurde übergeben: " + startdate + " mit Anstoss-Zeit-Index: " + aszindex);
		// spieltagsdaten[edited_date].setText(lg.datum(startdate) + " " + lg.anstosszeiten[aszindex]);
		if (belongsToALeague)		liga.daten_uhrzeiten[currentMatchday][0] = startdate;
		else						gruppe.datesAndTimes[currentMatchday][0] = startdate;
		if (belongsToALeague)		liga.daten_uhrzeiten[currentMatchday][edited_date + 1] = aszindex;
		else						gruppe.datesAndTimes[currentMatchday][edited_date + 1] = aszindex;
		edited_date = -1;

		spieltagsdatenBefuellen();
	}

	public void mannschaftClicked(int index) {
		mannschaften[index].setOpaque(true);
		mannschaften[index].setBackground(new Color(255, 255, 0));
		mannschaften[index].paintImmediately(0, 0, mannschaften[index].getWidth(), mannschaften[index].getHeight());
		for (int i = 0; i < mannschaften.length; i++) {
			if (i != index) {
				mannschaften[i].setBackground(null);
				mannschaften[i].paintImmediately(0, 0, mannschaften[i].getWidth(), mannschaften[i].getHeight());
			}
		}
		mannschaftenauswahl.setVisible(true);
		edited_team = index;
	}

	public void mannschaftenButtonClicked(int index) {
		array[edited_team % numberOfMatches][edited_team / numberOfMatches] = index + 1;
		if (belongsToALeague)		mannschaften[edited_team].setText(liga.getMannschaften()[index].getName());
		else if (!belongsToKORound)	mannschaften[edited_team].setText(gruppe.getMannschaften()[index].getName());
		else						mannschaften[edited_team].setText(koRunde.getMannschaftenIn(KORound)[index].getName());
		mannschaften[edited_team].setBackground(null);
	}
}