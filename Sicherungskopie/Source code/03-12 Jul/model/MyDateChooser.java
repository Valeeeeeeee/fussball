package model;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MyDateChooser extends JFrame {
	private static final long serialVersionUID = -2889713010836842244L;
	
	private Liga liga;
	private Gruppe gruppe;
	private Spieltag spieltag;
	private boolean belongsToALeague;
	
	private int def_starttag;
	private int date;
	private int aszindex;
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
	
	private String[] wochentage = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
	private String[] wt_kurz = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};	// TODO Combobox mit Standardmöglichkeiten (lg.tageseitfr && anst.zeitn) 
																			// und eine Möglichkeit zum eingeben eines speziellen datums (neue ausnahmeanstosszeit)
	
	public MyDateChooser(Liga liga, Spieltag spieltag) {
		super();
		this.liga = liga;
		this.spieltag = spieltag;
		belongsToALeague = true;
		initGUI();
	}
	
	public MyDateChooser(Gruppe gruppe, Spieltag spieltag) {
		super();
		this.gruppe = gruppe;
		this.spieltag = spieltag;
		belongsToALeague = false;
		initGUI();
	}
	
	public void initGUI() {
		this.setLayout(null);
		
		if (belongsToALeague)	startjahr = liga.saisons[liga.aktuelle_saison];
		else					
		if (belongsToALeague)	def_starttag = liga.default_starttag;
		else					def_starttag = 0;
//		final Liga lgfinal = lg;
		
		String[] days = new String[31];
		for (int i = 0; i < days.length; i++) {
			days[i] = "" + (i + 1);
		}
		String[] months = new String[12];
		for (int i = 0; i < months.length; i++) {
			months[i] = "" + (i + 1);
		}
		String[] years = new String[2];
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
			lblstarttag = new JLabel();
			this.add(lblstarttag);
			lblstarttag.setBounds(20, 10, 175, 20);
			lblstarttag.setText("Standard-Starttag: " + wochentage[def_starttag]);
			lblstarttag.setOpaque(true);
			lblstarttag.setBackground(Color.yellow);
		}
		{
			ComboBoxModel jCBDayModel = new DefaultComboBoxModel(days);
	        jCBStDay = new JComboBox();
	        this.add(jCBStDay);
	        jCBStDay.setModel(jCBDayModel);
	        jCBStDay.setBounds(20, 40, 45, 20);
	        jCBStDay.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBStDayItemStateChanged(evt);
	            }
	        });
		}
		{
			ComboBoxModel jCBMonthModel = new DefaultComboBoxModel(months);
	        jCBStMonth = new JComboBox();
	        this.add(jCBStMonth);
	        jCBStMonth.setModel(jCBMonthModel);
	        jCBStMonth.setBounds(70, 40, 45, 20);
	        jCBStMonth.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBMonthItemStateChanged(evt, true);
	            }
	        });
		}
		{
			ComboBoxModel jCBYearModel = new DefaultComboBoxModel(years);
	        jCBStYear = new JComboBox();
	        this.add(jCBStYear);
	        jCBStYear.setModel(jCBYearModel);
	        jCBStYear.setBounds(120, 40, 60, 20);
	        jCBStYear.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBYearItemStateChanged(evt, true);
	            }
	        });
		}
		{
//			String[] asz = new String[lg.anstosszeiten.length + 1];
//			for (int i = 0; i < (asz.length - 1); i++) {
//				asz[i] = wt_kurz[(def_starttag + lg.tageseitfr[i]) % 7] + " " + lg.anstosszeiten[i];
//			}
//			asz[asz.length - 1] = "anderes";
//			ComboBoxModel jCBAnstosszeitenModel = new DefaultComboBoxModel(asz);
			jCBAnstosszeiten = new JComboBox();
	        this.add(jCBAnstosszeiten);
//	        jCBAnstosszeiten.setModel(jCBAnstosszeitenModel);
	        jCBAnstosszeiten.setBounds(20, 70, 80, 20);
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
			go.setBounds(110, 70, 70, 20);
			go.setText("fertig");
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
			        if (setDateFromCBs())		spieltag.datumEingegeben(date, aszindex);
				}
			});
		}
		{
			ComboBoxModel jCBDayModel = new DefaultComboBoxModel(days);
	        jCBDay = new JComboBox();
	        this.add(jCBDay);
	        jCBDay.setModel(jCBDayModel);
	        jCBDay.setBounds(20, 100, 45, 20);
//	        jCBDay.addItemListener(new ItemListener() {
//	            public void itemStateChanged(ItemEvent evt) {
//	                jCBDayItemStateChanged(evt, false);
//	            }
//	        });
	        jCBDay.setVisible(false);
		}
		{
			ComboBoxModel jCBMonthModel = new DefaultComboBoxModel(months);
	        jCBMonth = new JComboBox();
	        this.add(jCBMonth);
	        jCBMonth.setModel(jCBMonthModel);
	        jCBMonth.setBounds(70, 100, 45, 20);
	        jCBMonth.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	            	jCBMonthItemStateChanged(evt, false);
	            }
	        });
	        jCBMonth.setVisible(false);
		}
		{
			ComboBoxModel jCBYearModel = new DefaultComboBoxModel(years);
	        jCBYear = new JComboBox();
	        this.add(jCBYear);
	        jCBYear.setModel(jCBYearModel);
	        jCBYear.setBounds(120, 100, 60, 20);
	        jCBYear.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent evt) {
	                jCBYearItemStateChanged(evt, false);
	            }
	        });
	        jCBYear.setVisible(false);
		}
		{
			ComboBoxModel jCBHourModel = new DefaultComboBoxModel(hours);
	        jCBHour = new JComboBox();
	        this.add(jCBHour);
	        jCBHour.setModel(jCBHourModel);
	        jCBHour.setBounds(20, 130, 60, 20);
	        jCBHour.setVisible(false);
		}
		{
			ComboBoxModel jCBMinuteModel = new DefaultComboBoxModel(minutes);
	        jCBMinute = new JComboBox();
	        this.add(jCBMinute);
	        jCBMinute.setModel(jCBMinuteModel);
	        jCBMinute.setBounds(90, 130, 60, 20);
	        jCBMinute.setVisible(false);
		}
		
        
		setSize(400, 300);
		setResizable(false);
	}
	
	public int getDate() {
		return this.date;
	}
	
	public void setDate(int date, int aszindex) {
		try {
			jCBStYear.setSelectedIndex(date / 10000 - startjahr);
			jCBStMonth.setSelectedIndex(date % 10000 / 100 - 1);
			jCBStDay.setSelectedIndex(date % 100 - 1);
			jCBAnstosszeiten.setSelectedIndex(aszindex);
		} catch (IllegalArgumentException iae) {
			JOptionPane.showMessageDialog(null, "Es wurde ein illegales Argument übergeben!");
			jCBStYear.setSelectedIndex(0);
			jCBStMonth.setSelectedIndex(0);
			jCBStDay.setSelectedIndex(0);
			jCBAnstosszeiten.setSelectedIndex(0);
		}
		
		this.date = date;
	}
	
	public boolean setDateFromCBs() {
		if (belongsToALeague) {
			GregorianCalendar greg = new GregorianCalendar(jCBStYear.getSelectedIndex() + startjahr, jCBStMonth.getSelectedIndex(), jCBStDay.getSelectedIndex() + 1);
			System.out.println("\nder " + greg.get(Calendar.DAY_OF_MONTH) + "." + (greg.get(Calendar.MONTH) + 1) + "." + greg.get(Calendar.YEAR) + " ist ein " + wochentage[(greg.get(Calendar.DAY_OF_WEEK) + 5) % 7]);
			if ((greg.get(Calendar.DAY_OF_WEEK) - 2) != def_starttag) {
				int cancel = JOptionPane.showConfirmDialog(null,	"Der angegebene Tag entspricht nicht dem standardmäßigen Starttag für einen Spieltag. (" + wochentage[def_starttag] + ") \n" +
																	"Möchtest du trotzdem fortfahren?", "Bist du sicher?", JOptionPane.YES_NO_OPTION);
				if (cancel == JOptionPane.NO_OPTION) {
					return false;
				}
			}
			
			this.date = 10000 * (jCBStYear.getSelectedIndex() + startjahr) + 100 * (jCBStMonth.getSelectedIndex() + 1) + (jCBStDay.getSelectedIndex() + 1);
			this.aszindex = jCBAnstosszeiten.getSelectedIndex();
			
			if (this.jCBAnstosszeiten.getSelectedIndex() == (jCBAnstosszeiten.getModel().getSize() - 1)) { // dann wurde eine neue Anstosszeit ausgewählt
				int datenewasz = 10000 * (jCBYear.getSelectedIndex() + startjahr) + 100 * (jCBMonth.getSelectedIndex() + 1) + (jCBDay.getSelectedIndex() + 1);
				String timenewasz = jCBHour.getSelectedIndex() / 10 + "" + jCBHour.getSelectedIndex() % 10 + ":" + (5 * jCBMinute.getSelectedIndex()) / 10 + "" + (5 * jCBMinute.getSelectedIndex()) % 10;
				
				// herausfinden, ob die Anstosszeit bereits existiert
				int diff = compareDates(date, datenewasz);
				for (int i = 0; i < liga.tageseitfr.length; i++) {
					if (liga.tageseitfr[i] == diff) {
						if(liga.anstosszeiten[i].equals(timenewasz)) {
							this.aszindex = i;
							break;
						}
					}
				}
				
				if (this.aszindex == (jCBAnstosszeiten.getModel().getSize() - 1)) {
					int[] oldtageseitfr = liga.tageseitfr;
					String[] oldanstosszeiten = liga.anstosszeiten;
					liga.tageseitfr = new int[oldtageseitfr.length + 1];
					liga.anstosszeiten = new String[oldanstosszeiten.length + 1];
					
					for (int i = 0; i < oldtageseitfr.length; i++) {
						liga.tageseitfr[i] = oldtageseitfr[i];
						liga.anstosszeiten[i] = oldanstosszeiten[i];
					}
					
					liga.tageseitfr[liga.tageseitfr.length - 1] = diff;
					liga.anstosszeiten[liga.anstosszeiten.length - 1] = timenewasz;
				} else {
					JOptionPane.showMessageDialog(null, "Diese Anstosszeit gibt es bereits.");
				}
			}
			
			this.setVisible(false);
			return true;
		} else {
			// TODO setDateFromCBs Gruppe
			
			return true;
		}
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
			if (jahr % 4 == 0) {
				if (jahr % 100 != 0 || jahr % 400 == 0) {
					schaltjahr = true;
				} else {
					schaltjahr = false;
				}
			} else {
				schaltjahr = false;
			}
			
			// Tageanzahl des Februars kann betroffen sein
			int anzahltage;
			if (starttag) {
				anzahltage = jCBStDay.getModel().getSize();
			} else {
				anzahltage = jCBDay.getModel().getSize();
			}
			if (monat == 2 && schaltjahr) {
				anzahltage = 29;
			} else if (monat == 2) {
				anzahltage = 28;
			}
			if (tag > anzahltage) {
				tag = anzahltage;
			}
			
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
		if (belongsToALeague) {
			GregorianCalendar greg = new GregorianCalendar(jCBStYear.getSelectedIndex() + startjahr, jCBStMonth.getSelectedIndex(), jCBStDay.getSelectedIndex() + 1);
			
			int dayofweek = greg.get(Calendar.DAY_OF_WEEK);
			
			String[] asz = new String[liga.anstosszeiten.length + 1];
			for (int i = 0; i < (asz.length - 1); i++) {
				asz[i] = wt_kurz[(def_starttag + dayofweek + 1 + liga.tageseitfr[i]) % 7] + " " + liga.anstosszeiten[i];
			}
			asz[asz.length - 1] = "anderes";
			ComboBoxModel jCBAnstosszeitenModel = new DefaultComboBoxModel(asz);
			
	        jCBAnstosszeiten.setModel(jCBAnstosszeitenModel);
		} else {
			// TODO comboBoxAnstosszeitenAktualisieren für Gruppe
		}
		
		
	}
	
	public void jCBAnstosszeitenItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = jCBAnstosszeiten.getSelectedIndex();
			if (index == (jCBAnstosszeiten.getModel().getSize() - 1)) {
				jCBDay.setVisible(true);
				jCBMonth.setVisible(true);
				jCBYear.setVisible(true);
				jCBHour.setVisible(true);
				jCBMinute.setVisible(true);
				
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
			}
		}
	}
	
	public boolean validateDate(int date) {
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
	
	public int correctDate(int olddate) {
		int newdate = 0;
		
		if (validateDate(olddate)) {
			newdate = olddate;
		} else {
			int yyyy = olddate / 10000;
			int mm = (olddate % 10000) / 100 - 1;
			int dd = olddate % 100;
			System.out.println("This date is incorrect: "+ dd + "." + mm + "." + yyyy);
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
