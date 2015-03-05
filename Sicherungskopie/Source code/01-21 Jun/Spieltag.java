import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Spieltag extends JPanel {
	private static final long serialVersionUID = 533273470193095401L;

	JComboBox jCBSpieltage;
	JLabel[] mannschaften;
	JTextField[] tore;
	JButton bearbeiten;
	JButton fertig;
	JLabel[] spieltagsdaten;

	int[][] array;
	int edited_date;
	int edited_team = -1;
	int edited_matchday = -1;
	int current_matchday = -1;

	final int STARTX = 0;
	final int STARTY = 1;
	final int GAPX = 2;
	final int GAPY = 3;
	final int WIDTH = 4;
	final int HEIGHT = 5;

	private int[] labels = { 160, 140, 180, 30, 120, 10 };
	private int m_lblsstartx = 160;
	private int m_lblsstarty = 50;
	private int m_lblswidth = 180;
	private int m_lblsheight = 30;
	private int m_lblsgapx = 120;
	private int m_lblsgapy = 10;

	private int[] textfields = { labels[STARTX] + labels[WIDTH] + 15,
			labels[STARTY], labels[GAPX] - 30 - 2 * 40, labels[GAPY], 40,
			labels[HEIGHT] };
	private int m_tfstartx = m_lblsstartx + m_lblswidth + 15;
	private int m_tfstarty = m_lblsstarty;
	private int m_tfwidth = 40;
	private int m_tfheight = m_lblsheight;
	private int m_tfgapx = m_lblsgapx - 30 - 2 * m_tfwidth;
	private int m_tfgapy = m_lblsgapy;

	Rectangle posbearbfertig = new Rectangle(440, 10, 160, 30);
	Rectangle poscombobox = new Rectangle(220, 10, 200, 30);

	public JPanel mannschaftenauswahl;
	public JButton[] mannschaftenbtns;
	int[] buttonsauswahl = new int[6];

	int countTeams;
	int numberOfMatches;
	int countMatchdays;
	int halfCountTeamsRoundUp;

	private boolean belongsToALeague = false;
	private Liga liga;
	private Turnier turnier;
	private Gruppe gruppe;

	public Spieltag(Liga liga) {
		super();

		this.liga = liga;
		belongsToALeague = true;

		numberOfMatches = liga.getAnzahlSpiele();
		countTeams = liga.getAnzahlTeams();
		countMatchdays = liga.getAnzahlSpieltage();
		halfCountTeamsRoundUp = liga.getHalbeAnzMSAuf();

		buttonsauswahl[STARTX] = 10;
		buttonsauswahl[STARTY] = 10;
		buttonsauswahl[GAPX] = 10;
		buttonsauswahl[GAPY] = 10;
		buttonsauswahl[WIDTH] = 200;
		buttonsauswahl[HEIGHT] = 60;

		if ((2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]) > 800) {
			buttonsauswahl[HEIGHT] = (800 - (halfCountTeamsRoundUp + 1) * buttonsauswahl[GAPY]) / halfCountTeamsRoundUp;
			JOptionPane.showMessageDialog(null, "Der berechnete Wert für height liegt bei " + buttonsauswahl[HEIGHT] + " mit hAnzAuf = "
							+ halfCountTeamsRoundUp, "\"height\" wurde berechnet", JOptionPane.INFORMATION_MESSAGE);
		} else {
			buttonsauswahl[HEIGHT] = 60;
		}

		initGUI();
	}

	public Spieltag(Gruppe gruppe, Turnier turnier) {
		super();

		this.gruppe = gruppe;
		this.turnier = turnier;
		belongsToALeague = false;

		numberOfMatches = gruppe.getNumberOfMatchesPerMatchday();
		countTeams = gruppe.getNumberOfTeams();
		countMatchdays = gruppe.getNumberOfMatchdays();
		halfCountTeamsRoundUp = (countTeams % 2 == 0 ? countTeams / 2 : countTeams / 2 + 1);

		buttonsauswahl[STARTX] = 10;
		buttonsauswahl[STARTY] = 10;
		buttonsauswahl[GAPX] = 10;
		buttonsauswahl[GAPY] = 10;
		buttonsauswahl[WIDTH] = 200;
		
		if ((2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY]) - buttonsauswahl[GAPY]) > 800) {
			buttonsauswahl[HEIGHT] = (800 - (halfCountTeamsRoundUp + 1) * buttonsauswahl[GAPY]) / halfCountTeamsRoundUp;
			JOptionPane.showMessageDialog(null, "Der berechnete Wert für height liegt bei " + buttonsauswahl[HEIGHT] + " mit hAnzAuf = "
							+ halfCountTeamsRoundUp, "\"height\" wurde berechnet", JOptionPane.INFORMATION_MESSAGE);
		} else {
			buttonsauswahl[HEIGHT] = 60;
		}

		initGUI();
	}

	public void initGUI() {
		try {
			this.setLayout(null);

			// TODO min und maxheight sinnvoll setzen
			int minimumheight = 400;
			int maximumheight = 800;

			Dimension dim = new Dimension();
			dim.width = 1200;

			int heightOfTeamLabels = m_lblsstarty + numberOfMatches * (m_lblsheight + m_lblsgapy) + 20;
			int heightOfTeamSelection = 2 * buttonsauswahl[STARTY] + halfCountTeamsRoundUp * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY])
					- buttonsauswahl[GAPY];

			dim.height = (heightOfTeamLabels > heightOfTeamSelection ? heightOfTeamLabels : heightOfTeamSelection);
			JOptionPane.showMessageDialog(null, "labelsHeight = " + heightOfTeamLabels + " oder buttonsHeight = " + heightOfTeamSelection
					+ " macht: " + dim.height);

			// correction to minimumheight or maximumheight if out of these bounds
			if (dim.height < minimumheight) {
				dim.height = minimumheight;
			} else if (dim.height > maximumheight) {
				dim.height = maximumheight;
			}

			this.setSize(dim);
			JOptionPane.showMessageDialog(null, "This is dim: " + dim);

			
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
				spieltagsdaten[i].setBounds(60, m_lblsstarty + i * (m_lblsheight + m_lblsgapy), 100, m_lblsheight);
				if (belongsToALeague)	spieltagsdaten[i].setText(liga.getDateOf(current_matchday, 0));
				else					spieltagsdaten[i].setText(gruppe.getDateOf(current_matchday, 0));
				spieltagsdaten[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				spieltagsdaten[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						datumslabelgeklickt(x);
					}
				});
			}
			
			for (int i = 0; i < mannschaften.length; i++) {
				final int x = i;
				int zeile = i % numberOfMatches;
				int spalte = i / numberOfMatches;

				mannschaften[i] = new JLabel();
				this.add(mannschaften[i]);
				mannschaften[i].setBounds(m_lblsstartx + spalte * (m_lblswidth + m_lblsgapx), 
						m_lblsstarty + zeile * (m_lblsheight + m_lblsgapy), m_lblswidth, m_lblsheight);
				if (belongsToALeague)	mannschaften[i].setText(liga.getMannschaften()[i].getName());
				else					mannschaften[i].setText(gruppe.getMannschaften()[i].getName());
				if (spalte == 0)	mannschaften[i].setHorizontalAlignment(SwingConstants.RIGHT);
				else				mannschaften[i].setHorizontalAlignment(SwingConstants.LEFT);
				mannschaften[i].setEnabled(false);
				mannschaften[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				mannschaften[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (mannschaften[x].isEnabled()) {
							mannschaftgeklickt(x);
						}
					}
				});
			}
			
			for (int i = 0; i < tore.length; i++) {
				final int x = i;
				tore[i] = new JTextField();
				this.add(tore[i]);
				tore[i].setBounds(m_tfstartx + (i / numberOfMatches) * (m_tfwidth + m_tfgapx), m_tfstarty + (i % numberOfMatches) * (m_tfheight + m_tfgapy), m_tfwidth, m_tfheight);
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
				if (belongsToALeague)	mannschaftenbtns[i].setText(liga.getMannschaften()[i].getName());
				else					mannschaftenbtns[i].setText(gruppe.getMannschaften()[i].getName());
				mannschaftenbtns[i].setBounds(buttonsauswahl[STARTX] + xfactor * (buttonsauswahl[WIDTH] + buttonsauswahl[GAPX]),
								buttonsauswahl[STARTY] + yfactor * (buttonsauswahl[HEIGHT] + buttonsauswahl[GAPY]), buttonsauswahl[WIDTH], buttonsauswahl[HEIGHT]);
				mannschaftenbtns[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mannschaftenbtngeklickt(x);
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
		g.setColor(Color.green);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.clearRect(2, 2, getWidth() - 4, getHeight() - 4);

		// wieder entfernen
		g.setColor(Color.gray);
		int stepdiff = 50;
		int count = 900 / stepdiff;
		for (int i = 0; i < count; i++) {
			g.drawLine(0, i * stepdiff, this.getWidth(), i * stepdiff);
		}
	}

	public void jCBSpieltageItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			if (belongsToALeague) {
				liga.ergebnisse_sichern();
				spieltag_anzeigen();
			} else {
				// TODO SpieltageItemStateChanged for tournament group
				
			}
		}
	}

	

	public void setLabelsEnabled(boolean bool) {
		for (JLabel lbl : mannschaften) {
			lbl.setEnabled(bool);
			lbl.setOpaque(false);
		}
	}

	public void setTFsEditable(boolean bool) {
		for (JTextField tf : tore) {
			tf.setEditable(bool);
		}
	}

	public void spieltagsdaten_befuellen(Liga lg) {
		String[] dateandtimeofmatches = new String[numberOfMatches];
		for (int i = 0; i < numberOfMatches; i++) {
			dateandtimeofmatches[i] = lg.getDateOf(current_matchday, i);
			if (i < 1
					|| !dateandtimeofmatches[i]
							.equals(dateandtimeofmatches[i - 1]))
				spieltagsdaten[i].setText(dateandtimeofmatches[i]);
			else
				spieltagsdaten[i].setText("");
		}
	}

	public void bearbeitenActionPerformed() {
		if (belongsToALeague) {
			edited_matchday = jCBSpieltage.getSelectedIndex();
			for (int i = 0; i < array.length; i++) {
				array[i][0] = Start.ligen[Start.aktuelle_liga].spieltage[edited_matchday][i][0];
				array[i][1] = Start.ligen[Start.aktuelle_liga].spieltage[edited_matchday][i][1];
			}
			setLabelsEnabled(true);
			setTFsEditable(false);
			bearbeiten.setVisible(false);
			fertig.setVisible(true);
			jCBSpieltage.setEnabled(false);
		} else {
			// TODO bearbeiten
		}
		
	}

	public int fertigActionPerformed() {
		if (!belongsToALeague) {
			// TODO die Unterschiede beseitigen
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
				Start.ligen[Start.aktuelle_liga].spieltage[edited_matchday][i][0] = array[i][0];
				Start.ligen[Start.aktuelle_liga].spieltage[edited_matchday][i][1] = array[i][1];

				if (array[i][0] != -1 && array[i][1] != -1) {
					Start.ligen[Start.aktuelle_liga].getMannschaften()[array[i][0] - 1].setGegner(edited_matchday, Mannschaft.HOME, array[i][1], i);
					Start.ligen[Start.aktuelle_liga].getMannschaften()[array[i][1] - 1].setGegner(edited_matchday, Mannschaft.AWAY, array[i][0], i);
				}
			}

			if (fehlerart == -1)		Start.ligen[Start.aktuelle_liga].spieltage_eingetragen[edited_matchday] = true;
			else						Start.ligen[Start.aktuelle_liga].spieltage_eingetragen[edited_matchday] = false;
			edited_matchday = -1;
			jCBSpieltage.setEnabled(true);
			mannschaftenauswahl.setVisible(false);
		}
		return saveanyway;
	}

	public void spieltag_anzeigen() {
		
		
		
		if (!belongsToALeague) {
			// TODO falls es eine Gruppe ist
			return;
		}
		
		if (current_matchday == -1) {
			current_matchday = liga.getCurrentMatchday();

			// damit nicht bei setSelectedIndex die default-Inhalte von tore in das ergebnis-Array kopiert werden muss das Befüllen davor erfolgen
			int anzahl_spiele = liga.getAnzahlSpiele();
			for (int i = 0; i < this.tore.length; i++) {
				this.tore[i].setText("" + liga.ergebnis[current_matchday][i % anzahl_spiele][i / anzahl_spiele]);
			}

			jCBSpieltage.setSelectedIndex(current_matchday);
		} else {
			current_matchday = jCBSpieltage.getSelectedIndex();

			spieltagsdaten_befuellen(liga);

			// TODO Umsortierung der Spiele bei Datumsveränderung ->
			// chronologisch

			int anzahl_spiele = liga.getAnzahlSpiele();
			for (int i = 0; i < this.mannschaften.length; i++) {
				this.mannschaften[i].setText(Start
						.getMannschaftAt(liga.spieltage[current_matchday][i
								% anzahl_spiele][i / anzahl_spiele]));
				this.tore[i].setText(""
						+ liga.ergebnis[current_matchday][i % anzahl_spiele][i
								/ anzahl_spiele]);
			}
			if (!liga.spieltage_eingetragen[current_matchday]) {
				this.setTFsEditable(false);
			} else {
				this.setTFsEditable(true);
			}
		}
	}

	public void datumslabelgeklickt(int index) {
		if (belongsToALeague) {
			edited_date = index;

			MyDateChooser mdc = new MyDateChooser(liga);
			mdc.setLocationRelativeTo(null);
			mdc.setVisible(true);

			mdc.setDate(liga.daten_uhrzeiten[current_matchday][0], liga.daten_uhrzeiten[current_matchday][edited_date + 1]);
		} else {
			
		}
	}

	public void datumeingegeben(int startdate, int aszindex, Liga lg) {
		System.out.println("Folgendes Datum wurde übergeben: " + startdate
				+ " mit Anstoss-Zeit-Index: " + aszindex);
		// spieltagsdaten[edited_date].setText(lg.datum(startdate) + " " +
		// lg.anstosszeiten[aszindex]);
		lg.daten_uhrzeiten[current_matchday][0] = startdate;
		lg.daten_uhrzeiten[current_matchday][edited_date + 1] = aszindex;
		edited_date = -1;

		spieltagsdaten_befuellen(lg);
	}

	public void mannschaftgeklickt(int index) {
		mannschaften[index].setOpaque(true);
		mannschaften[index].setBackground(new Color(255, 255, 0));
		mannschaften[index]
				.paintImmediately(0, 0, mannschaften[index].getWidth(),
						mannschaften[index].getHeight());
		for (int i = 0; i < mannschaften.length; i++) {
			if (i != index) {
				mannschaften[i].setBackground(null);
				mannschaften[i]
						.paintImmediately(0, 0, mannschaften[i].getWidth(),
								mannschaften[i].getHeight());
			}
		}
		mannschaftenauswahl.setVisible(true);
		edited_team = index;
	}

	public void mannschaftenbtngeklickt(int index) {
		array[edited_team % numberOfMatches][edited_team / numberOfMatches] = index + 1;
		mannschaften[edited_team].setText(Start.getMannschaftAt(index + 1));
		mannschaften[edited_team].setBackground(null);
	}
}