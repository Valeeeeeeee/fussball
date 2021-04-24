package model;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static util.Utilities.*;

public class MyDateChooser extends JFrame {
	private static final long serialVersionUID = -2889713010836842244L;
	
	/**
	 * The width of this window for league
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
	private Gruppe group;
	private KORunde koRound;
	private Spieltag spieltag;
	private boolean belongsToLeague = false;
	private boolean belongsToGroup = false;
	private boolean belongsToKORound = false;
	
	private boolean userCanMakeChanges = false;

	private Datum defaultDate;
	private Uhrzeit defaultTime;
	private Datum date;
	private Uhrzeit time;
	private int kotIndex;
	private int numberOfYears;
	private int startYear;
	private boolean leapYear;
	
	private JLabel jLblMatch;
	private JLabel jLblStarttag;
	private JComboBox<String> jCBStDay;
	private JComboBox<String> jCBStMonth;
	private JComboBox<String> jCBStYear;
	private JComboBox<String> jCBKickOffTimes;
	private JComboBox<String> jCBDay;
	private JComboBox<String> jCBMonth;
	private JComboBox<String> jCBYear;
	private JComboBox<String> jCBHour;
	private JComboBox<String> jCBMinute;
	private JButton jBtnGo;
	
	private Rectangle REC_LBLMATCH =	new Rectangle(20, 10, 360, 20);
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

	private Rectangle REC_LBLMATCHTOUR =	new Rectangle(10, 10, 360, 20);
	private Rectangle REC_DAYTOUR =			new Rectangle(15, 40, 70, 30);
	private Rectangle REC_MONTHTOUR =		new Rectangle(85, 40, 70, 30);
	private Rectangle REC_YEARTOUR =		new Rectangle(155, 40, 85, 30);
	private Rectangle REC_HOURTOUR =		new Rectangle(15, 70, 70, 30);
	private Rectangle REC_MINUTETOUR =		new Rectangle(85, 70, 70, 30);
	private Rectangle REC_GOTOUR =			new Rectangle(255, 40, 70, 30);
	
	public MyDateChooser(LigaSaison season, Spieltag spieltag) {
		super();
		this.season = season;
		this.spieltag = spieltag;
		belongsToLeague = true;
		initGUI();
	}
	
	public MyDateChooser(Gruppe group, Spieltag spieltag) {
		super();
		this.group = group;
		this.spieltag = spieltag;
		belongsToGroup = true;
		initGUI();
	}
	
	public MyDateChooser(KORunde koRound, Spieltag spieltag) {
		super();
		this.koRound = koRound;
		this.spieltag = spieltag;
		belongsToKORound = true;
		initGUI();
	}
	
	public void initGUI() {
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		if (belongsToLeague) {
			startYear = season.getYear();
			numberOfYears = season.isSTSS() ? 2 : 1;
		} else if (belongsToGroup) {
			startYear = group.getStartDate().getYear();
			numberOfYears = group.getFinalDate().getYear() - startYear + 1;
		} else if (belongsToKORound) {
			startYear = koRound.getStartDate().getYear();
			numberOfYears = koRound.getFinalDate().getYear() - startYear + 1;
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
			years[i] = "" + (i + startYear);
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
			jLblMatch = new JLabel();
			this.add(jLblMatch);
			if (belongsToLeague)	jLblMatch.setBounds(REC_LBLMATCH);
			else					jLblMatch.setBounds(REC_LBLMATCHTOUR);
			jLblMatch.setOpaque(true);
			jLblMatch.setBackground(Color.yellow);
			alignCenter(jLblMatch);
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
			jCBKickOffTimes = new JComboBox<>();
			this.add(jCBKickOffTimes);
			jCBKickOffTimes.setBounds(REC_ANSTOSS);
			jCBKickOffTimes.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					jCBKickOffTimesItemStateChanged(evt);
				}
			});
			refreshKickOffTimesComboBox();
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
	
	public Datum getDate() {
		return date;
	}
	
	public void setDateAndKOTIndex(Datum date, int kotIndex) {
		try {
			jCBStYear.setSelectedIndex(date.getYear() - startYear);
			jCBStMonth.setSelectedIndex(date.getMonth() - 1);
			jCBStDay.setSelectedIndex(date.getDay() - 1);
			jCBKickOffTimes.setSelectedIndex(kotIndex);
			
			this.date = date;
			userCanMakeChanges = true;
		} catch (IllegalArgumentException iae) {
			Datum guess = MIN_DATE;
			if (spieltag.getCurrentMatchday() > 0)	guess = new Datum(season.getDate(spieltag.getCurrentMatchday() - 1), 7);
			if (guess.getYear() >= DATE_UNDEFINED.getYear() || guess == MIN_DATE)	guess = new Datum(1, 8, startYear);
			jCBStYear.setSelectedIndex(guess.getYear() - startYear);
			jCBStMonth.setSelectedIndex(guess.getMonth() - 1);
			jCBStDay.setSelectedIndex(guess.getDay() - 1);
			jCBKickOffTimes.setSelectedIndex(0);
			this.date = guess;
		}
	}
	
	private void getDefaultDateAndTime() {
		try {
			ArrayList<String> dateAndTime = readFile("DefaultDate.txt");
			defaultDate = new Datum(dateAndTime.remove(0));
			defaultTime = new Uhrzeit(dateAndTime.remove(0));
		} catch (Exception e) {
			defaultDate = new Datum();
			defaultTime = MIDNIGHT;
		}
	}
	
	public void setDateAndTime(Datum date, Uhrzeit time) {
		getDefaultDateAndTime();
		try {
			jCBYear.setSelectedIndex(date.getYear() - startYear);
			jCBMonth.setSelectedIndex(date.getMonth() - 1);
			jCBDay.setSelectedIndex(date.getDay() - 1);
			jCBHour.setSelectedIndex(time.getHourOfDay());
			jCBMinute.setSelectedIndex(time.getMinute() / 5);
			
			this.date = date;
			this.time = time;
			
			if (date.equals(belongsToGroup ? group.getStartDate() : koRound.getStartDate()) && time.equals(MIDNIGHT)) {
				jCBYear.setSelectedIndex(defaultDate.getYear() - startYear);
				jCBMonth.setSelectedIndex(defaultDate.getMonth() - 1);
				jCBDay.setSelectedIndex(defaultDate.getDay() - 1);
				jCBHour.setSelectedIndex(defaultTime.getHourOfDay());
				jCBMinute.setSelectedIndex(defaultTime.getMinute() / 5);
				this.date = defaultDate;
			}
			
			jCBHour.requestFocus();
		} catch (Exception e) {
			jCBYear.setSelectedIndex(defaultDate.getYear() - startYear);
			jCBMonth.setSelectedIndex(defaultDate.getMonth() - 1);
			jCBDay.setSelectedIndex(defaultDate.getDay() - 1);
			jCBHour.setSelectedIndex(defaultTime.getHourOfDay());
			jCBMinute.setSelectedIndex(defaultTime.getMinute() / 5);
			
			this.date = defaultDate;
			this.time = defaultTime;
			returnTournamentStyle();
			
			jCBHour.requestFocus();
		}
	}
	
	public void setMatch(Wettbewerb competition, int matchday, int matchID) {
		Spiel match = competition.getMatch(matchday, matchID);
		String matchStr = "";
		matchStr += match != null && match.getHomeTeam() != null ? match.getHomeTeam().getName() : "n/a";
		matchStr += " gegen ";
		matchStr += match != null && match.getAwayTeam() != null ? match.getAwayTeam().getName() : "n/a";
		
		jLblMatch.setText(matchStr);
	}
	
	private void returnTournamentStyle() {
		date = new Datum(jCBDay.getSelectedIndex() + 1, jCBMonth.getSelectedIndex() + 1, jCBYear.getSelectedIndex() + startYear);
		time = new Uhrzeit(jCBHour.getSelectedIndex(), jCBMinute.getSelectedIndex() * 5);
		
		setVisible(false);
		
		spieltag.dateEnteredTournamentStyle(date, time);
	}
	
	private void returnLeagueStyle() {
		date = new Datum(jCBStDay.getSelectedIndex() + 1, jCBStMonth.getSelectedIndex() + 1, jCBStYear.getSelectedIndex() + startYear);
		kotIndex = jCBKickOffTimes.getSelectedIndex();
		
		if (jCBKickOffTimes.getSelectedIndex() == (jCBKickOffTimes.getModel().getSize() - 1)) { // dann wurde eine neue Anstoßzeit ausgewählt
			Datum dateOfNewKOT = new Datum(jCBDay.getSelectedIndex() + 1, jCBMonth.getSelectedIndex() + 1, jCBYear.getSelectedIndex() + startYear);
			Uhrzeit timeOfNewKOT = new Uhrzeit(jCBHour.getSelectedIndex(), 5 * jCBMinute.getSelectedIndex());
			
			// herausfinden, ob die Anstoßzeit bereits existiert
			int diff = date.daysUntil(dateOfNewKOT);
			kotIndex = season.getIndexOfKOT(diff, timeOfNewKOT);
			
			if (kotIndex == -1)	kotIndex = season.addNewKickoffTime(diff, timeOfNewKOT);
			else				message("Diese Anstoßzeit gibt es bereits.");
		}
		
		setVisible(false);
		
		spieltag.dateEnteredLeagueStyle(date, kotIndex);
	}
	
	public void jCBYearItemStateChanged(ItemEvent evt, boolean starttag) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int jahr, monat, tag;
			if (starttag) {
				jahr = jCBStYear.getSelectedIndex() + startYear;
				monat = jCBStMonth.getSelectedIndex() + 1;
				tag = jCBStDay.getSelectedIndex() + 1;
			} else {
				jahr = jCBYear.getSelectedIndex() + startYear;
				monat = jCBMonth.getSelectedIndex() + 1;
				tag = jCBDay.getSelectedIndex() + 1;
			}
			
			// Feststellung ob Schaltjahr
			if (jahr % 4 == 0 && (jahr % 100 != 0 || jahr % 400 == 0))	leapYear = true;
			else														leapYear = false;
			
			// Tageanzahl des Februars kann betroffen sein
			int anzahltage;
			if (starttag)	anzahltage = jCBStDay.getModel().getSize();
			else			anzahltage = jCBDay.getModel().getSize();
			
			if (monat == 2 && leapYear)	anzahltage = 29;
			else if (monat == 2)		anzahltage = 28;
			
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
				refreshKickOffTimesComboBox();
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
					if (leapYear) {
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
				refreshKickOffTimesComboBox();
			} else {
				jCBDay.setModel(jCBDayModel);
				jCBDay.setSelectedIndex(tag - 1);
			}
		}
	}
	
	public void jCBStDayItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			refreshKickOffTimesComboBox();
		}
	}
	
	public void refreshKickOffTimesComboBox() {
		if (belongsToLeague) {
			Datum date = new Datum(jCBStDay.getSelectedIndex() + 1, jCBStMonth.getSelectedIndex() + 1, jCBStYear.getSelectedIndex() + startYear);
			String[] kots = new String[season.getNumberOfKickoffTimes() + 2];
			ArrayList<AnstossZeit> kickOffTimes = season.getKickOffTimes();
			kots[0] = "Keine Angabe";
			for (int i = 1; i < (kots.length - 1); i++) {
				kots[i] = kickOffTimes.get(i).weekdayAndTime(date);
			}
			kots[kots.length - 1] = "anderes";
			ComboBoxModel<String> jCBKickOffTimesModel = new DefaultComboBoxModel<>(kots);
			
			jCBKickOffTimes.setModel(jCBKickOffTimesModel);
		}
	}
	
	/**
	 * Can only be called if this belongs to a league.
	 * @param evt
	 */
	public void jCBKickOffTimesItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = jCBKickOffTimes.getSelectedIndex();
			if (index == (jCBKickOffTimes.getModel().getSize() - 1)) {
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
	
	private boolean validateDate(Datum date) {
		boolean isValidDate = false;
		
		Calendar greg = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay());
		
		if (date.getDay() == greg.get(Calendar.DAY_OF_MONTH) && date.getMonth() == greg.get(Calendar.MONTH) && date.getYear() == greg.get(Calendar.YEAR)) {
			isValidDate = true;
		}
		
		return isValidDate;
	}
	
	private Datum correctDate(Datum oldDate) {
		Datum newDate = MIN_DATE;
		
		if (validateDate(oldDate)) {
			newDate = oldDate;
		} else {
			log("This date is incorrect: " + oldDate.withDividers());
		}
		
		return newDate;
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
