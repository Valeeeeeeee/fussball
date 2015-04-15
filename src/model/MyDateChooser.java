package model;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static util.Utilities.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MyDateChooser extends JFrame {
	private static final long serialVersionUID = -2889713010836842244L;
	
	/**
	 * The width of this window for leaague
	 */
	public final int WIDTH_LG = 450;
	/**
	 * The height of this window for league
	 */
	public final int HEIGHT_LG = 200;
	/**
	 * The width of this window for tournament
	 */
	public final int WIDTH_TOUR = 350;
	/**
	 * The height of this window for tournament
	 */
	public final int HEIGHT_TOUR = 100;
	
	private Liga liga;
	private Gruppe gruppe;
	private KORunde koRunde;
	private Spieltag spieltag;
	private boolean belongsToLeague = false;
	private boolean belongsToGroup = false;
	private boolean belongsToKORound = false;
	
	private boolean userCanMakeChanges = false;

	private int defaultMyDate = 20150908;
	private int defaultMyTime = 2045;
	private int defaultStarttag = -1;
	private int date;
	private int time;
	private int aszindex;
	private int numberOfYears = -1;
	private int startjahr;
	private boolean schaltjahr;
	
	private JLabel lblstarttag;
	private JComboBox jCBStDay;
	private JComboBox jCBStMonth;
	private JComboBox jCBStYear;
	private JComboBox jCBAnstosszeiten;
	private JComboBox jCBDay;
	private JComboBox jCBMonth;
	private JComboBox jCBYear;
	private JComboBox jCBHour;
	private JComboBox jCBMinute;
	private JButton go;
	
	private Rectangle RECSTDAY =	new Rectangle(20, 40, 70, 30);
	private Rectangle RECSTMONTH =	new Rectangle(90, 40, 70, 30);
	private Rectangle RECSTYEAR =	new Rectangle(160, 40, 85, 30);
	private Rectangle RECDAY =		new Rectangle(20, 80, 70, 30);
	private Rectangle RECMONTH =	new Rectangle(90, 80, 70, 30);
	private Rectangle RECYEAR =		new Rectangle(160, 80, 85, 30);
	private Rectangle RECANSTOSS =	new Rectangle(250, 40, 110, 30);
	private Rectangle RECHOUR =		new Rectangle(20, 110, 70, 30);
	private Rectangle RECMINUTE =	new Rectangle(90, 110, 70, 30);
	private Rectangle RECGO =		new Rectangle(370, 40, 70, 30);
	
	private Rectangle RECDAYTOUR =		new Rectangle(20, 10, 70, 30);
	private Rectangle RECMONTHTOUR =	new Rectangle(90, 10, 70, 30);
	private Rectangle RECYEARTOUR =		new Rectangle(160, 10, 85, 30);
	private Rectangle RECHOURTOUR =		new Rectangle(20, 40, 70, 30);
	private Rectangle RECMINUTETOUR =	new Rectangle(90, 40, 70, 30);
	private Rectangle RECGOTOUR =		new Rectangle(260, 10, 70, 30);
	
//	original
//	private Rectangle RECSTDAY =	new Rectangle(20, 40, 70, 30);
//	private Rectangle RECSTMONTH =	new Rectangle(90, 40, 70, 30);
//	private Rectangle RECSTYEAR =	new Rectangle(160, 40, 85, 30);
//	private Rectangle RECDAY =		new Rectangle(20, 130, 70, 30);
//	private Rectangle RECMONTH =	new Rectangle(100, 130, 70, 30);
//	private Rectangle RECYEAR =		new Rectangle(180, 130, 85, 30);
//	private Rectangle RECANSTOSS =	new Rectangle(20, 90, 110, 30);
//	private Rectangle RECHOUR =		new Rectangle(20, 160, 70, 30);
//	private Rectangle RECMINUTE =	new Rectangle(90, 160, 70, 30);
//	private Rectangle RECGO =		new Rectangle(170, 90, 70, 30);
	
	
	private String[] wochentage = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
	private String[] wt_kurz = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};	// TODO Combobox mit Standardmoeglichkeiten (lg.tageseitfr && anst.zeitn) 
																			// und eine Moeglichkeit zum eingeben eines speziellen datums (neue ausnahmeanstosszeit)
	
	public MyDateChooser(Liga liga, Spieltag spieltag) {
		super();
		this.liga = liga;
		this.spieltag = spieltag;
		belongsToLeague = true;
		initGUI();
	}
	
	public MyDateChooser(Gruppe gruppe, Spieltag spieltag) {
		super();
		this.gruppe = gruppe;
		this.spieltag = spieltag;
		belongsToGroup = true;
		initGUI();
	}
	
	public MyDateChooser(KORunde koRunde, Spieltag spieltag) {
		super();
		this.koRunde = koRunde;
		this.spieltag = spieltag;
		belongsToKORound = true;
		initGUI();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		if (belongsToLeague) {
			startjahr = liga.getAktuelleSaison();
			defaultStarttag = liga.getDefaultStarttag();
			if (liga.isSTSS())	numberOfYears = 2;
			else	numberOfYears = 1;
		} else if (belongsToGroup) {
			startjahr = gruppe.getStartDate() / 10000;
			numberOfYears = gruppe.getFinalDate() / 10000 - startjahr + 1;
		} else if (belongsToKORound) {
			startjahr = koRunde.getStartDate() / 10000;
			numberOfYears = koRunde.getFinalDate() / 10000 - startjahr + 1;
		}
		
		String[] days = new String[31];
		for (int i = 0; i < days.length; i++) {
			days[i] = "" + (i + 1);
		}
		String[] months = new String[12];
		for (int i = 0; i < months.length; i++) {
			months[i] = "" + (i + 1);
		}
		String[] years = new String[numberOfYears];
		for (int i = 0; i < years.length; i++) {
			years[i] = "" + (i + startjahr);
		}
		String[] hours = new String[24];
		for (int i = 0; i < hours.length; i++) {
			hours[i] = "" + i;
		}
		String[] minutes = new String[12];
		for (int i = 0; i < minutes.length; i++) {
			minutes[i] = "" + 5 * i;
		}
		
		if (belongsToLeague) {
			lblstarttag = new JLabel();
			this.add(lblstarttag);
			lblstarttag.setBounds(20, 10, 175, 20);
			lblstarttag.setText("Standard-Starttag: " + wochentage[defaultStarttag]);
			lblstarttag.setOpaque(true);
			lblstarttag.setBackground(Color.yellow);
		}
		if (belongsToLeague) {
	        jCBStDay = new JComboBox();
	        this.add(jCBStDay);
	        jCBStDay.setModel(new DefaultComboBoxModel(days));
	        jCBStDay.setBounds(RECSTDAY);
	        jCBStDay.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBStDayItemStateChanged(evt);
	            }
	        });
		}
		if (belongsToLeague) {
	        jCBStMonth = new JComboBox();
	        this.add(jCBStMonth);
	        jCBStMonth.setModel(new DefaultComboBoxModel(months));
	        jCBStMonth.setBounds(RECSTMONTH);
	        jCBStMonth.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBMonthItemStateChanged(evt, true);
	            }
	        });
		}
		if (belongsToLeague) {
	        jCBStYear = new JComboBox();
	        this.add(jCBStYear);
	        jCBStYear.setModel(new DefaultComboBoxModel(years));
	        jCBStYear.setBounds(RECSTYEAR);
	        jCBStYear.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBYearItemStateChanged(evt, true);
	            }
	        });
		}
		if (belongsToLeague) {
			jCBAnstosszeiten = new JComboBox();
	        this.add(jCBAnstosszeiten);
	        jCBAnstosszeiten.setBounds(RECANSTOSS);
	        jCBAnstosszeiten.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBAnstosszeitenItemStateChanged(evt);
	            }
	        });
	        comboBoxAnstosszeitenAktualisieren();
		}
		{
			go = new JButton();
			this.add(go);
			if (belongsToLeague)	go.setBounds(RECGO);
			else					go.setBounds(RECGOTOUR);
			go.setText("fertig");
			go.setFocusable(false);
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
			        if (belongsToLeague)	returnLeagueStyle();
			        else					returnTournamentStyle();
				}
			});
		}
		{
			ComboBoxModel jCBDayModel = new DefaultComboBoxModel(days);
	        jCBDay = new JComboBox();
	        this.add(jCBDay);
	        jCBDay.setModel(jCBDayModel);
	        if (belongsToLeague)	jCBDay.setBounds(RECDAY);
	        else					jCBDay.setBounds(RECDAYTOUR);
	        if (belongsToLeague)	jCBDay.setVisible(false);
		}
		{
			ComboBoxModel jCBMonthModel = new DefaultComboBoxModel(months);
	        jCBMonth = new JComboBox();
	        this.add(jCBMonth);
	        jCBMonth.setModel(jCBMonthModel);
	        if (belongsToLeague)	jCBMonth.setBounds(RECMONTH);
	        else					jCBMonth.setBounds(RECMONTHTOUR);
	        if (belongsToLeague)	jCBMonth.setVisible(false);
	        jCBMonth.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBMonthItemStateChanged(evt, false);
	            }
	        });
		}
		{
			ComboBoxModel jCBYearModel = new DefaultComboBoxModel(years);
	        jCBYear = new JComboBox();
	        this.add(jCBYear);
	        jCBYear.setModel(jCBYearModel);
	        if (belongsToLeague)	jCBYear.setBounds(RECYEAR);
	        else					jCBYear.setBounds(RECYEARTOUR);
	        if (belongsToLeague)	jCBYear.setVisible(false);
	        jCBYear.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBYearItemStateChanged(evt, false);
	            }
	        });
		}
		{
			ComboBoxModel jCBHourModel = new DefaultComboBoxModel(hours);
	        jCBHour = new JComboBox();
	        this.add(jCBHour);
	        jCBHour.setModel(jCBHourModel);
	        if (belongsToLeague)	jCBHour.setBounds(RECHOUR);
	        else					jCBHour.setBounds(RECHOURTOUR);
	        if (belongsToLeague)	jCBHour.setVisible(false);
		}
		{
			ComboBoxModel jCBMinuteModel = new DefaultComboBoxModel(minutes);
	        jCBMinute = new JComboBox();
	        this.add(jCBMinute);
	        jCBMinute.setModel(jCBMinuteModel);
	        if (belongsToLeague)	jCBMinute.setBounds(RECMINUTE);
	        else					jCBMinute.setBounds(RECMINUTETOUR);
	        if (belongsToLeague)	jCBMinute.setVisible(false);
		}
		
		if (belongsToLeague)	setSize(this.WIDTH_LG, this.HEIGHT_LG);
		else					setSize(this.WIDTH_TOUR, this.HEIGHT_TOUR);
		setResizable(false);
	}
	
	public int getDate() {
		return this.date;
	}
	
	public void setDateAndKOTindex(int date, int aszindex) {
		try {
			jCBStYear.setSelectedIndex(date / 10000 - startjahr);
			jCBStMonth.setSelectedIndex(date % 10000 / 100 - 1);
			jCBStDay.setSelectedIndex(date % 100 - 1);
			jCBAnstosszeiten.setSelectedIndex(aszindex);
			
			this.date = date;
			userCanMakeChanges = true;
		} catch (IllegalArgumentException iae) {
//			message("The given parameter was incorrect!");
			
			int today = 0;
			if (spieltag.getCurrentMatchday() > 0)	today = MyDate.verschoben(liga.getDate(spieltag.getCurrentMatchday() - 1), 7);
			else									today = startjahr * 10000 + 824;
			jCBStYear.setSelectedIndex(today / 10000 - startjahr);
			jCBStMonth.setSelectedIndex(today % 10000 / 100 - 1);
			jCBStDay.setSelectedIndex(today % 100 - 1);
			jCBAnstosszeiten.setSelectedIndex(0);
			this.date = today;
		}
	}
	
	public void setDateAndTime(int myDate, int myTime) {
		try {
			jCBYear.setSelectedIndex(myDate / 10000 - startjahr);
			jCBMonth.setSelectedIndex(myDate % 10000 / 100 - 1);
			jCBDay.setSelectedIndex(myDate % 100 - 1);
			jCBHour.setSelectedIndex(myTime / 100);
			jCBMinute.setSelectedIndex((myTime % 100) / 5);
			
			if (myDate == (belongsToGroup ? gruppe.getStartDate() : koRunde.getStartDate()) && myTime == 0) {
				jCBYear.setSelectedIndex(defaultMyDate / 10000 - startjahr);
				jCBMonth.setSelectedIndex(defaultMyDate % 10000 / 100 - 1);
				jCBDay.setSelectedIndex(defaultMyDate % 100 - 1);
				jCBHour.setSelectedIndex(defaultMyTime / 100);
				jCBMinute.setSelectedIndex(defaultMyTime % 100 / 5);
			}
			
			this.date = myDate;
			this.time = myTime;
		} catch (Exception e) {
			int today = MyDate.verschoben(startjahr * 10000 + 815, spieltag.getCurrentMatchday() * 7);
			jCBYear.setSelectedIndex(today / 10000 - startjahr);
			jCBMonth.setSelectedIndex(today % 10000 / 100 - 1);
			jCBDay.setSelectedIndex(today % 100 - 1);
			jCBHour.setSelectedIndex(19);
			jCBMinute.setSelectedIndex(9);
			
			this.date = today;
			this.date = 2000;
		}
	}
	
	private void returnTournamentStyle() {
		this.date = 10000 * (jCBYear.getSelectedIndex() + startjahr) + 100 * (jCBMonth.getSelectedIndex() + 1) + (jCBDay.getSelectedIndex() + 1);
		this.time = jCBHour.getSelectedIndex() * 100 + jCBMinute.getSelectedIndex() * 5;
		
		this.setVisible(false);
		
		spieltag.dateEnteredTournamentStyle(date, time);
	}
	
	private void returnLeagueStyle() {
//		GregorianCalendar greg = new GregorianCalendar(jCBStYear.getSelectedIndex() + startjahr, jCBStMonth.getSelectedIndex(), jCBStDay.getSelectedIndex() + 1);
//		if ((greg.get(Calendar.DAY_OF_WEEK) - 2) != defaultStarttag) {
//			int cancel = JOptionPane.showConfirmDialog(null,	"The day you chose is not the default startday for a matchday. (" + wochentage[def_starttag] + ") \n" +
//																"Do you want to continue anyway?", "Are you sure?", JOptionPane.YES_NO_OPTION);
//			if (cancel == JOptionPane.NO_OPTION) {
//				return false;
//			}
//		}
		
		this.date = 10000 * (jCBStYear.getSelectedIndex() + startjahr) + 100 * (jCBStMonth.getSelectedIndex() + 1) + (jCBStDay.getSelectedIndex() + 1);
		this.aszindex = jCBAnstosszeiten.getSelectedIndex();
		
		if (this.jCBAnstosszeiten.getSelectedIndex() == (jCBAnstosszeiten.getModel().getSize() - 1)) { // dann wurde eine neue Anstosszeit ausgewaehlt
			int dateOfNewKOT = 10000 * (jCBYear.getSelectedIndex() + startjahr) + 100 * (jCBMonth.getSelectedIndex() + 1) + (jCBDay.getSelectedIndex() + 1);
			int timeOfNewKOT = jCBHour.getSelectedIndex() * 100 + 5 * jCBMinute.getSelectedIndex();
			
			// herausfinden, ob die Anstosszeit bereits existiert
			int diff = compareDates(date, dateOfNewKOT);
			for (int i = 0; i < liga.daysSinceDST.length; i++) {
				if (liga.daysSinceDST[i] == diff) {
					if (liga.kickoffTimes[i] == timeOfNewKOT) {
						this.aszindex = i;
						break;
					}
				}
			}
			
			if (this.aszindex == (jCBAnstosszeiten.getModel().getSize() - 1))	liga.addNewKickoffTime(diff, timeOfNewKOT);
			else																message("Diese Anstosszeit gibt es bereits.");
		}
		
		this.setVisible(false);
		
		spieltag.dateEnteredLeagueStyle(date, aszindex);
	}
	
	public void jCBYearItemStateChanged(ItemEvent evt, boolean starttag) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int jahr, monat, tag;
			if (starttag) {
				jahr = jCBStYear.getSelectedIndex() + startjahr;
				monat = jCBStMonth.getSelectedIndex() + 1;
				tag = jCBStDay.getSelectedIndex() + 1;
			} else {
				jahr = jCBYear.getSelectedIndex() + startjahr;
				monat = jCBMonth.getSelectedIndex() + 1;
				tag = jCBDay.getSelectedIndex() + 1;
			}
			
			// Feststellung ob Schaltjahr
			if (jahr % 4 == 0 && (jahr % 100 != 0 || jahr % 400 == 0))	schaltjahr = true;
			else														schaltjahr = false;
			
			// Tageanzahl des Februars kann betroffen sein
			int anzahltage;
			if (starttag)	anzahltage = jCBStDay.getModel().getSize();
			else			anzahltage = jCBDay.getModel().getSize();
			
			if (monat == 2 && schaltjahr)	anzahltage = 29;
			else if (monat == 2)			anzahltage = 28;
			
			if (tag > anzahltage)	tag = anzahltage;
			
			//Re-Initialisierung der Tage-ComboBox
			String[] days = new String[anzahltage];
			for (int i = 0; i < days.length; i++) {
				days[i] = "" + (i + 1);
			}
			ComboBoxModel jCBDayModel = new DefaultComboBoxModel(days);
			if (starttag) {
				jCBStDay.setModel(jCBDayModel);
				jCBStDay.setSelectedIndex(tag - 1);
				comboBoxAnstosszeitenAktualisieren();
			} else {
				jCBDay.setModel(jCBDayModel);
				jCBDay.setSelectedIndex(tag - 1);
			}
		}
	}
	
	public void jCBMonthItemStateChanged(ItemEvent evt, boolean starttag) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int monat, tag;
			if (starttag) {
				monat = jCBStMonth.getSelectedIndex() + 1;
				tag = jCBStDay.getSelectedIndex() + 1;
			} else {
				monat = jCBMonth.getSelectedIndex() + 1;
				tag = jCBDay.getSelectedIndex() + 1;
			}
			
			int anzahltage = 31;
			
			// Reduktion der Anzahl an Tagen im Monat
			if (monat != 1 && monat != 3 && monat != 5 && monat != 7 && monat != 8 && monat != 10 && monat != 12) {
				anzahltage--;
				if (monat == 2) {
					if (schaltjahr) {
						anzahltage--;
					} else {
						anzahltage -= 2;
					}
				}
			}
			if (tag > anzahltage) {
				tag = anzahltage;
			}
			
			// Re-Initialisierung der Tage-ComboBox
			String[] days = new String[anzahltage];
			for (int i = 0; i < days.length; i++) {
				days[i] = "" + (i + 1);
			}
			ComboBoxModel jCBDayModel = new DefaultComboBoxModel(days);
			if (starttag) {
				jCBStDay.setModel(jCBDayModel);
		        jCBStDay.setSelectedIndex(tag - 1);
		        comboBoxAnstosszeitenAktualisieren();
			} else {
				jCBDay.setModel(jCBDayModel);
		        jCBDay.setSelectedIndex(tag - 1);
			}
		}
	}
	
	public void jCBStDayItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			comboBoxAnstosszeitenAktualisieren();
		}
	}
	
	public void comboBoxAnstosszeitenAktualisieren() {
		if (belongsToLeague) {
			GregorianCalendar greg = new GregorianCalendar(jCBStYear.getSelectedIndex() + startjahr, jCBStMonth.getSelectedIndex(), jCBStDay.getSelectedIndex() + 1);
			
			int dayofweek = greg.get(Calendar.DAY_OF_WEEK);
			
			String[] asz = new String[liga.kickoffTimes.length + 1];
			for (int i = 0; i < (asz.length - 1); i++) {
				// nach dem ersten % 7 liegen die Zahlen zwischen -6 und 6, dann plus 12 [5(weil Calendar.MONDAY == 2) waere zu wenig] und noch mal % 7
				asz[i] = wt_kurz[((dayofweek + liga.daysSinceDST[i]) % 7 + 12) % 7] + " " + MyDate.uhrzeit(liga.kickoffTimes[i]);
			}
			asz[asz.length - 1] = "anderes";
			ComboBoxModel jCBAnstosszeitenModel = new DefaultComboBoxModel(asz);
			
	        jCBAnstosszeiten.setModel(jCBAnstosszeitenModel);
		} else {
			// TODO comboBoxAnstosszeitenAktualisieren fuer Gruppe
		}
	}
	
	/**
	 * Can only be called if this belongs to a league.
	 * @param evt
	 */
	public void jCBAnstosszeitenItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = jCBAnstosszeiten.getSelectedIndex();
			if (index == (jCBAnstosszeiten.getModel().getSize() - 1)) {
				jCBDay.setVisible(true);
				jCBMonth.setVisible(true);
				jCBYear.setVisible(true);
				jCBHour.setVisible(true);
				jCBMinute.setVisible(true);
				
				jCBYear.setSelectedIndex(jCBStYear.getSelectedIndex());
				jCBMonth.setSelectedIndex(jCBStMonth.getSelectedIndex());
				jCBDay.setSelectedIndex(jCBStDay.getSelectedIndex());
				
				jCBStDay.setEnabled(false);
				jCBStMonth.setEnabled(false);
				jCBStYear.setEnabled(false);
			} else {
				jCBDay.setVisible(false);
				jCBMonth.setVisible(false);
				jCBYear.setVisible(false);
				jCBHour.setVisible(false);
				jCBMinute.setVisible(false);
				
				jCBStDay.setEnabled(true);
				jCBStMonth.setEnabled(true);
				jCBStYear.setEnabled(true);
				
				if (userCanMakeChanges) {
					returnLeagueStyle();
				}
			}
		}
	}
	
	private boolean validateDate(int date) {
		boolean isValidDate = false;
		
		int yyyy = date / 10000;
		int mm = (date % 10000) / 100 - 1;
		int dd = date % 100;
		Calendar greg = new GregorianCalendar(yyyy, mm, dd);
		
		if (dd == greg.get(Calendar.DAY_OF_MONTH) && mm == greg.get(Calendar.MONTH) && yyyy == greg.get(Calendar.YEAR)) {
			isValidDate = true;
		}
		
		return isValidDate;
	}
	
	private int correctDate(int olddate) {
		int newdate = 0;
		
		if (validateDate(olddate)) {
			newdate = olddate;
		} else {
			int yyyy = olddate / 10000;
			int mm = (olddate % 10000) / 100 - 1;
			int dd = olddate % 100;
			log("This date is incorrect: "+ dd + "." + mm + "." + yyyy);
		}
		
		return newdate;
	}
	
	public int compareDates(int dateone, int datetwo) {
		int compared = 0;
		
		int yyyy = dateone / 10000;
		int mm = (dateone % 10000) / 100 - 1;
		int dd = dateone % 100;
		Calendar greg = new GregorianCalendar(yyyy, mm, dd);
		
		yyyy = datetwo / 10000;
		mm = (datetwo % 10000) / 100 - 1;
		dd = datetwo % 100;
		Calendar greg2 = new GregorianCalendar(yyyy, mm, dd);
		
		long r1 = greg.getTimeInMillis() / 1000;
		long r2 = greg2.getTimeInMillis() / 1000;
		compared = (int) Math.round((double) (r2 - r1) / 86400); // muss gerundet werden, da mit Winter-/Sommerzeit eine Stunde fehlt/zu viel da ist
		
		return compared;
	}
}




