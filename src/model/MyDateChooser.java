package model;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static util.Utilities.*;

public class MyDateChooser extends JFrame {
	private static final long serialVersionUID = -2889713010836842244L;
	
	/**
	 * The width of this window for leaague
	 */
	public final int WIDTH_LG = 400;
	/**
	 * The height of this window for league
	 */
	public final int HEIGHT_LG = 230;
	/**
	 * The width of this window for tournament
	 */
	public final int WIDTH_TOUR = 360;
	/**
	 * The height of this window for tournament
	 */
	public final int HEIGHT_TOUR = 130;
	
	private LigaSaison season;
	private Gruppe gruppe;
	private KORunde koRunde;
	private Spieltag spieltag;
	private boolean belongsToLeague = false;
	private boolean belongsToGroup = false;
	private boolean belongsToKORound = false;
	
	private boolean userCanMakeChanges = false;

	private int defaultMyDate = 20151210;
	private int defaultMyTime = 1900;
	private int date;
	private int time;
	private int aszindex;
	private int numberOfYears = -1;
	private int startjahr;
	private boolean schaltjahr;
	
	private JLabel jLblSpiel;
	private JLabel jLblStarttag;
	private JComboBox<String> jCBStDay;
	private JComboBox<String> jCBStMonth;
	private JComboBox<String> jCBStYear;
	private JComboBox<String> jCBAnstosszeiten;
	private JComboBox<String> jCBDay;
	private JComboBox<String> jCBMonth;
	private JComboBox<String> jCBYear;
	private JComboBox<String> jCBHour;
	private JComboBox<String> jCBMinute;
	private JButton jBtnGo;
	
	private Rectangle REC_LBLSPIEL =	new Rectangle(20, 10, 360, 20);
	private Rectangle REC_LBLSTARTTAG =	new Rectangle(15, 45, 60, 20);
	private Rectangle REC_STDAY =		new Rectangle(15, 70, 70, 30);
	private Rectangle REC_STMONTH =		new Rectangle(85, 70, 70, 30);
	private Rectangle REC_STYEAR =		new Rectangle(155, 70, 85, 30);
	private Rectangle REC_DAY =			new Rectangle(15, 110, 70, 30);
	private Rectangle REC_MONTH =		new Rectangle(85, 110, 70, 30);
	private Rectangle REC_YEAR =		new Rectangle(155, 110, 85, 30);
	private Rectangle REC_ANSTOSS =		new Rectangle(245, 70, 140, 30);
	private Rectangle REC_HOUR =		new Rectangle(15, 140, 70, 30);
	private Rectangle REC_MINUTE =		new Rectangle(85, 140, 70, 30);
	private Rectangle REC_GO =			new Rectangle(315, 40, 70, 25);

	private Rectangle REC_LBLSPIELTOUR =	new Rectangle(10, 10, 360, 20);
	private Rectangle REC_DAYTOUR =			new Rectangle(15, 40, 70, 30);
	private Rectangle REC_MONTHTOUR =		new Rectangle(85, 40, 70, 30);
	private Rectangle REC_YEARTOUR =		new Rectangle(155, 40, 85, 30);
	private Rectangle REC_HOURTOUR =		new Rectangle(15, 70, 70, 30);
	private Rectangle REC_MINUTETOUR =		new Rectangle(85, 70, 70, 30);
	private Rectangle REC_GOTOUR =			new Rectangle(255, 40, 70, 30);
	
	private String[] wt_kurz = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
	
	public MyDateChooser(LigaSaison season, Spieltag spieltag) {
		super();
		this.season = season;
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
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		if (belongsToLeague) {
			startjahr = season.getSeason();
			numberOfYears = season.isSTSS() ? 2 : 1;
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
		
		{
			jLblSpiel = new JLabel();
			this.add(jLblSpiel);
	        if (belongsToLeague)	jLblSpiel.setBounds(REC_LBLSPIEL);
	        else					jLblSpiel.setBounds(REC_LBLSPIELTOUR);
			jLblSpiel.setOpaque(true);
			jLblSpiel.setBackground(Color.yellow);
			alignCenter(jLblSpiel);
		}
		
		if (belongsToLeague) {
			jLblStarttag = new JLabel();
			this.add(jLblStarttag);
			jLblStarttag.setBounds(REC_LBLSTARTTAG);
			jLblStarttag.setText("Starttag");
		}
		if (belongsToLeague) {
	        jCBStDay = new JComboBox<>();
	        this.add(jCBStDay);
	        jCBStDay.setModel(new DefaultComboBoxModel<>(days));
	        jCBStDay.setBounds(REC_STDAY);
	        jCBStDay.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBStDayItemStateChanged(evt);
	            }
	        });
		}
		if (belongsToLeague) {
	        jCBStMonth = new JComboBox<>();
	        this.add(jCBStMonth);
	        jCBStMonth.setModel(new DefaultComboBoxModel<>(months));
	        jCBStMonth.setBounds(REC_STMONTH);
	        jCBStMonth.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBMonthItemStateChanged(evt, true);
	            }
	        });
		}
		if (belongsToLeague) {
	        jCBStYear = new JComboBox<>();
	        this.add(jCBStYear);
	        jCBStYear.setModel(new DefaultComboBoxModel<>(years));
	        jCBStYear.setBounds(REC_STYEAR);
	        jCBStYear.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBYearItemStateChanged(evt, true);
	            }
	        });
		}
		if (belongsToLeague) {
			jCBAnstosszeiten = new JComboBox<>();
	        this.add(jCBAnstosszeiten);
	        jCBAnstosszeiten.setBounds(REC_ANSTOSS);
	        jCBAnstosszeiten.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBAnstosszeitenItemStateChanged(evt);
	            }
	        });
	        comboBoxAnstosszeitenAktualisieren();
		}
		{
			jBtnGo = new JButton();
			this.add(jBtnGo);
			if (belongsToLeague)	jBtnGo.setBounds(REC_GO);
			else					jBtnGo.setBounds(REC_GOTOUR);
			jBtnGo.setText("fertig");
			jBtnGo.setFocusable(false);
			jBtnGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
			        if (belongsToLeague)	returnLeagueStyle();
			        else					returnTournamentStyle();
				}
			});
		}
		{
	        jCBDay = new JComboBox<>();
	        this.add(jCBDay);
	        jCBDay.setModel(new DefaultComboBoxModel<>(days));
	        if (belongsToLeague)	jCBDay.setBounds(REC_DAY);
	        else					jCBDay.setBounds(REC_DAYTOUR);
	        if (belongsToLeague)	jCBDay.setVisible(false);
		}
		{
	        jCBMonth = new JComboBox<>();
	        this.add(jCBMonth);
	        jCBMonth.setModel(new DefaultComboBoxModel<>(months));
	        if (belongsToLeague)	jCBMonth.setBounds(REC_MONTH);
	        else					jCBMonth.setBounds(REC_MONTHTOUR);
	        if (belongsToLeague)	jCBMonth.setVisible(false);
	        jCBMonth.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBMonthItemStateChanged(evt, false);
	            }
	        });
		}
		{
	        jCBYear = new JComboBox<>();
	        this.add(jCBYear);
	        jCBYear.setModel(new DefaultComboBoxModel<>(years));
	        if (belongsToLeague)	jCBYear.setBounds(REC_YEAR);
	        else					jCBYear.setBounds(REC_YEARTOUR);
	        if (belongsToLeague)	jCBYear.setVisible(false);
	        jCBYear.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBYearItemStateChanged(evt, false);
	            }
	        });
		}
		{
	        jCBHour = new JComboBox<>();
	        this.add(jCBHour);
	        jCBHour.setModel(new DefaultComboBoxModel<>(hours));
	        if (belongsToLeague)	jCBHour.setBounds(REC_HOUR);
	        else					jCBHour.setBounds(REC_HOURTOUR);
	        if (belongsToLeague)	jCBHour.setVisible(false);
		}
		{
	        jCBMinute = new JComboBox<>();
	        this.add(jCBMinute);
	        jCBMinute.setModel(new DefaultComboBoxModel<>(minutes));
	        if (belongsToLeague)	jCBMinute.setBounds(REC_MINUTE);
	        else					jCBMinute.setBounds(REC_MINUTETOUR);
	        if (belongsToLeague)	jCBMinute.setVisible(false);
		}
		
		if (belongsToLeague)	setSize(WIDTH_LG, HEIGHT_LG);
		else					setSize(WIDTH_TOUR, HEIGHT_TOUR);
		setResizable(false);
	}
	
	public int getDate() {
		return date;
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
			int today = 0;
			if (spieltag.getCurrentMatchday() > 0)	today = MyDate.verschoben(season.getDate(spieltag.getCurrentMatchday() - 1), 7);
			if (today < 19700101)	today = startjahr * 10000 + 824;
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
			
			date = myDate;
			time = myTime;
		} catch (Exception e) {
			int today = MyDate.verschoben(startjahr * 10000 + 815, spieltag.getCurrentMatchday() * 7);
			jCBYear.setSelectedIndex(today / 10000 - startjahr);
			jCBMonth.setSelectedIndex(today % 10000 / 100 - 1);
			jCBDay.setSelectedIndex(today % 100 - 1);
			jCBHour.setSelectedIndex(19);
			jCBMinute.setSelectedIndex(9);
			
			date = today;
			date = 2000;
		}
	}
	
	public void setMatch(Wettbewerb wettbewerb, int matchday, int matchID) {
		Spiel spiel = wettbewerb.getSpiel(matchday, matchID);
		String match = "";
		if (spiel != null)	match = spiel.getHomeTeam().getName() + " gegen " + spiel.getAwayTeam().getName();
		else				match = "n/a gegen n/a";
		
		jLblSpiel.setText(match);
	}
	
	private void returnTournamentStyle() {
		date = 10000 * (jCBYear.getSelectedIndex() + startjahr) + 100 * (jCBMonth.getSelectedIndex() + 1) + (jCBDay.getSelectedIndex() + 1);
		time = jCBHour.getSelectedIndex() * 100 + jCBMinute.getSelectedIndex() * 5;
		
		setVisible(false);
		
		spieltag.dateEnteredTournamentStyle(date, time);
	}
	
	private void returnLeagueStyle() {
		date = 10000 * (jCBStYear.getSelectedIndex() + startjahr) + 100 * (jCBStMonth.getSelectedIndex() + 1) + (jCBStDay.getSelectedIndex() + 1);
		aszindex = jCBAnstosszeiten.getSelectedIndex();
		
		if (jCBAnstosszeiten.getSelectedIndex() == (jCBAnstosszeiten.getModel().getSize() - 1)) { // dann wurde eine neue Anstosszeit ausgewaehlt
			int dateOfNewKOT = 10000 * (jCBYear.getSelectedIndex() + startjahr) + 100 * (jCBMonth.getSelectedIndex() + 1) + (jCBDay.getSelectedIndex() + 1);
			int timeOfNewKOT = jCBHour.getSelectedIndex() * 100 + 5 * jCBMinute.getSelectedIndex();
			
			// herausfinden, ob die Anstosszeit bereits existiert
			int diff = compareDates(date, dateOfNewKOT);
			aszindex = season.getIndexOfKOT(diff, timeOfNewKOT);
			
			if (aszindex == -1)	aszindex = season.addNewKickoffTime(diff, timeOfNewKOT);
			else				message("Diese Anstosszeit gibt es bereits.");
		}
		
		setVisible(false);
		
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
			
			// Re-Initialisierung der Tage-ComboBox
			String[] days = new String[anzahltage];
			for (int i = 0; i < days.length; i++) {
				days[i] = "" + (i + 1);
			}
			ComboBoxModel<String> jCBDayModel = new DefaultComboBoxModel<>(days);
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
			ComboBoxModel<String> jCBDayModel = new DefaultComboBoxModel<>(days);
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
			
			String[] asz = new String[season.getNumberOfKickoffTimes() + 2];
			ArrayList<AnstossZeit> kickOffTimes = season.getKickOffTimes();
			asz[0] = "Keine Angabe";
			for (int i = 1; i < (asz.length - 1); i++) {
				// nach dem ersten % 7 liegen die Zahlen zwischen -6 und 6, dann plus 12 [5(weil Calendar.MONDAY == 2) waere zu wenig] und noch mal % 7
				asz[i] = wt_kurz[((dayofweek + kickOffTimes.get(i).getDaysSince()) % 7 + 12) % 7] + " " + MyDate.uhrzeit(kickOffTimes.get(i).getTime());
			}
			asz[asz.length - 1] = "anderes";
			ComboBoxModel<String> jCBAnstosszeitenModel = new DefaultComboBoxModel<>(asz);
			
	        jCBAnstosszeiten.setModel(jCBAnstosszeitenModel);
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
	
	public void dispose() {
		spieltag.dateChooserClosed();
		setVisible(false);
	}
}
