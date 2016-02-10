package model;

import static util.Utilities.*;
import static model.Mannschaft.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SpielerInformationen extends JFrame {
	
	private static final long serialVersionUID = -8974853216829127796L;

	
	public static final int WIDTH = 870;
	
	public static final int HEIGHT = 840;
	
	private static final int maxNumberOfCharacters = 24;
	private static final int minimumAge = 14;
	private static final int averageAge = 25;
	private static final int maximumAge = 50;
	
	private Color background = new Color(255, 255, 255);
	private Font fontAtClubSince = new Font("Calibri", 0, 24);
	private Font fontBirthDate = new Font("Calibri", 0, 24);
	private Font fontCompetition = new Font("Calibri", 0, 18);
	private Font fontDescription = new Font("Calibri", 0, 15);
	private Font fontDetails = new Font("Calibri", 0, 14);
	private Font fontDetailsHeadline = new Font("Calibri", 1, 14);
	private Font fontMoreDetails = new Font("Calibri", 1, 15);
	private Font fontNames = new Font("Calibri", 0, 30);
	private Font fontNamesSmall = new Font("Calibri", 0, 26);
	private Font fontNationality = new Font("Calibri", 0, 24);
	private Font fontPerformance = new Font("Calibri", 0, 18);
	private Font fontPosition = new Font("Calibri", 0, 24);
	private Font fontPseudonym = new Font("Calibri", 0, 18);
	private Font fontSquadNumber = new Font("Calibri", 0, 65);
	
	private Rectangle REC_CHANGEINFO = new Rectangle(390, 140, 100, 30);
	
	private Rectangle REC_SQUADNUMBER = new Rectangle(390, 65, 100, 65);
	private Rectangle REC_FIRSTNAMES = new Rectangle(500, 60, 340, 35);
	private Rectangle REC_LASTNAMES = new Rectangle(500, 100, 340, 35);
	private Rectangle REC_PSEUDONYM = new Rectangle(500, 140, 240, 30);
	private Rectangle REC_BIRTHDATE = new Rectangle(390, 180, 110, 20);
	private Rectangle REC_BIRTHDATEVAL = new Rectangle(390, 200, 140, 30);
	private Rectangle REC_POSITION = new Rectangle(640, 180, 65, 20);
	private Rectangle REC_POSITIONVAL = new Rectangle(640, 200, 110, 30);
	private Rectangle REC_NATIONALITY = new Rectangle(390, 240, 120, 20);
	private Rectangle REC_NATIONALITYVAL = new Rectangle(390, 260, 410, 30);
	private Rectangle REC_ATCLUBSINCE = new Rectangle(390, 300, 110, 20);
	private Rectangle REC_ATCLUBSINCEVAL = new Rectangle(390, 320, 140, 30);
	private Rectangle REC_ATCLUBUNTIL = new Rectangle(625, 300, 110, 20);
	private Rectangle REC_ATCLUBUNTILVAL = new Rectangle(625, 320, 140, 30);
	private Rectangle REC_BIRTHDAY = new Rectangle(390, 200, 70, 30);
	private Rectangle REC_BIRTHMONTH = new Rectangle(460, 200, 70, 30);
	private Rectangle REC_BIRTHYEAR = new Rectangle(530, 200, 85, 30);
	private Rectangle REC_ATCLUBSINCEEVER = new Rectangle(510, 300, 80, 20);
	private Rectangle REC_ATCLUBSINCEDAY = new Rectangle(390, 320, 70, 30);
	private Rectangle REC_ATCLUBSINCEMONTH = new Rectangle(460, 320, 70, 30);
	private Rectangle REC_ATCLUBSINCEYEAR = new Rectangle(530, 320, 85, 30);
	private Rectangle REC_ATCLUBUNTILEVER = new Rectangle(745, 300, 70, 20);
	private Rectangle REC_ATCLUBUNTILDAY = new Rectangle(625, 320, 70, 30);
	private Rectangle REC_ATCLUBUNTILMONTH = new Rectangle(695, 320, 70, 30);
	private Rectangle REC_ATCLUBUNTILYEAR = new Rectangle(765, 320, 85, 30);
	
	private Rectangle REC_DETAILS = new Rectangle(390, 375, 80, 25);
	private Rectangle REC_PERFORMANCE = new Rectangle(390, 400, 140, 25);
	private Rectangle REC_COMPETITION = new Rectangle(550, 400, 200, 25);
	private int[] bndsPerf = new int[] {390, 430, 30, 30, 160, 25};
	private int[] bndsPerfV = new int[] {600, 430, 0, 30, 50, 25};
	private Rectangle REC_PNLPERFMBM = new Rectangle(390, 410, 470, 420);
	private int[] bndsPMbMMD = new int[] {0, 0, 0, 20, 25, 20};
	private int[] bndsPMbMO = new int[] {40, 0, 0, 20, 190, 20};
	private int[] bndsPMbMOn = new int[] {240, 0, 0, 20, 30, 20};
	private int[] bndsPMbMOff = new int[] {270, 0, 0, 20, 30, 20};
	private int[] bndsPMbMG = new int[] {300, 0, 0, 20, 30, 20};
	private int[] bndsPMbMA = new int[] {330, 0, 0, 20, 30, 20};
	private int[] bndsPMbMB = new int[] {360, 0, 0, 20, 30, 20};
	private int[] bndsPMbMBT = new int[] {390, 0, 0, 20, 30, 20};
	private int[] bndsPMbMR = new int[] {420, 0, 0, 20, 30, 20};
	
	private JLabel jLblImage;
	
	private JPanel jPnlPlayerInformation;
	private JButton jBtnChangeInformation;
	private JLabel jLblSquadNumber;
	private JLabel jLblFirstNames;
	private JLabel jLblLastNames;
	private JLabel jLblPseudonym;
	private JLabel jLblBirthDate;
	private JLabel jLblBirthDateVal;
	private JLabel jLblPosition;
	private JLabel jLblPositionVal;
	private JLabel jLblNationality;
	private JLabel jLblNationalityVal;
	private JLabel jLblAtClubSince;
	private JLabel jLblAtClubSinceVal;
	private JLabel jLblAtClubUntil;
	private JLabel jLblAtClubUntilVal;
	
	private JComboBox<String> jCBPositions;
	private JTextField jTFSquadNumber;
	private JTextField jTFFirstNames;
	private JTextField jTFLastNames;
	private JTextField jTFPseudonym;
	private JComboBox<String> jCBBirthDay;
	private JComboBox<String> jCBBirthMonth;
	private JComboBox<String> jCBBirthYear;
	private JTextField jTFNationality;
	private JCheckBox jChBAtClubSinceEver;
	private JComboBox<String> jCBAtClubSinceDay;
	private JComboBox<String> jCBAtClubSinceMonth;
	private JComboBox<String> jCBAtClubSinceYear;
	private JCheckBox jChBAtClubUntilEver;
	private JComboBox<String> jCBAtClubUntilDay;
	private JComboBox<String> jCBAtClubUntilMonth;
	private JComboBox<String> jCBAtClubUntilYear;
	
	private JLabel jLblMoreDetails;
	private JLabel jLblPerformance;
	private JLabel jLblCompetition;
	private JLabel[] jLblsPerformance;
	private JLabel[] jLblsPerformanceValues;
	private JPanel jPnlPerformanceMbM;
	private ArrayList<JLabel> jLblsPerformanceMbMMatchday;
	private ArrayList<JLabel> jLblsPerformanceMbMOpponent;
	private ArrayList<JLabel> jLblsPerformanceMbMSubOn;
	private ArrayList<JLabel> jLblsPerformanceMbMSubOff;
	private ArrayList<JLabel> jLblsPerformanceMbMGoals;
	private ArrayList<JLabel> jLblsPerformanceMbMAssists;
	private ArrayList<JLabel> jLblsPerformanceMbMBooked;
	private ArrayList<JLabel> jLblsPerformanceMbMBookedTwice;
	private ArrayList<JLabel> jLblsPerformanceMbMRedCard;
	
	private Uebersicht uebersicht;
	private Spieler player;
	private Wettbewerb wettbewerb;
	
	private boolean addingNewPlayer;
	private boolean changingInformation;
	private boolean atClubSinceEver;
	private boolean atClubUntilEver;
	private boolean moreDetails;
	
	public SpielerInformationen(Uebersicht uebersicht, Wettbewerb wettbewerb) {
		super();
		
		this.uebersicht = uebersicht;
		this.wettbewerb = wettbewerb;
		
		initGUI();
	}
	
	private void initGUI() {
		setLayout(null);
		setForeground(background);
		
		jLblsPerformance = new JLabel[NUMBEROFPERFORMANCEDATA];
		jLblsPerformanceValues = new JLabel[NUMBEROFPERFORMANCEDATA];
		jLblsPerformanceMbMMatchday = new ArrayList<>();
		jLblsPerformanceMbMOpponent = new ArrayList<>();
		jLblsPerformanceMbMSubOn = new ArrayList<>();
		jLblsPerformanceMbMSubOff = new ArrayList<>();
		jLblsPerformanceMbMGoals = new ArrayList<>();
		jLblsPerformanceMbMAssists = new ArrayList<>();
		jLblsPerformanceMbMBooked = new ArrayList<>();
		jLblsPerformanceMbMBookedTwice = new ArrayList<>();
		jLblsPerformanceMbMRedCard = new ArrayList<>();
		
		String[] positionen = new String[Position.values().length];
		for (int i = 0; i < positionen.length; i++) {
			positionen[i] = Position.values()[i].getName();
		}
		String[] monate = new String[12];
		for (int i = 1; i <= monate.length; i++) {
			monate[i - 1] = (i / 10) + "" + (i % 10) + ".";
		}
		int firstYear = wettbewerb.getYear() - maximumAge;
		String[] jahre = new String[maximumAge - minimumAge + 1];
		for (int i = 0; i < jahre.length; i++) {
			jahre[i] = firstYear + i + "";
		}
		String[] beimVereinJahre = new String[wettbewerb.isSTSS() ? 2 : 1];
		for (int i = 0; i < beimVereinJahre.length; i++) {
			beimVereinJahre[i] = wettbewerb.getYear() + i + "";
		}
		
		{
			jPnlPlayerInformation = new JPanel();
			getContentPane().add(jPnlPlayerInformation);
			jPnlPlayerInformation.setLayout(null);
			jPnlPlayerInformation.setBounds(0, 0, WIDTH, HEIGHT);
			jPnlPlayerInformation.setBackground(background);
		}
		{
			jBtnChangeInformation = new JButton();
			jPnlPlayerInformation.add(jBtnChangeInformation);
			jBtnChangeInformation.setBounds(REC_CHANGEINFO);
			jBtnChangeInformation.setText("ändern");
			jBtnChangeInformation.setFocusable(false);
			jBtnChangeInformation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnChangeInformationActionPerformed();
				}
			});
		}
		{
			jLblSquadNumber = new JLabel();
			jPnlPlayerInformation.add(jLblSquadNumber);
			jLblSquadNumber.setBounds(REC_SQUADNUMBER);
			alignCenter(jLblSquadNumber);
			jLblSquadNumber.setFont(fontSquadNumber);
		}
		{
			jLblFirstNames = new JLabel();
			jPnlPlayerInformation.add(jLblFirstNames);
			jLblFirstNames.setBounds(REC_FIRSTNAMES);
			jLblFirstNames.setFont(fontNames);
		}
		{
			jLblLastNames = new JLabel();
			jPnlPlayerInformation.add(jLblLastNames);
			jLblLastNames.setBounds(REC_LASTNAMES);
			jLblLastNames.setFont(fontNames);
		}
		{
			jLblPseudonym = new JLabel();
			jPnlPlayerInformation.add(jLblPseudonym);
			jLblPseudonym.setBounds(REC_PSEUDONYM);
			jLblPseudonym.setFont(fontPseudonym);
		}
		{
			jLblBirthDate = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDate);
			jLblBirthDate.setBounds(REC_BIRTHDATE);
			jLblBirthDate.setFont(fontDescription);
			jLblBirthDate.setText("Geburtsdatum:");
		}
		{
			jLblBirthDateVal = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDateVal);
			jLblBirthDateVal.setBounds(REC_BIRTHDATEVAL);
			jLblBirthDateVal.setFont(fontBirthDate);
		}
		{
			jLblPosition = new JLabel();
			jPnlPlayerInformation.add(jLblPosition);
			jLblPosition.setBounds(REC_POSITION);
			jLblPosition.setFont(fontDescription);
			jLblPosition.setText("Position:");
		}
		{
			jLblPositionVal = new JLabel();
			jPnlPlayerInformation.add(jLblPositionVal);
			jLblPositionVal.setBounds(REC_POSITIONVAL);
			jLblPositionVal.setFont(fontPosition);
		}
		{
			jLblNationality = new JLabel();
			jPnlPlayerInformation.add(jLblNationality);
			jLblNationality.setBounds(REC_NATIONALITY);
			jLblNationality.setFont(fontDescription);
			jLblNationality.setText("Nationalität(en):");
		}
		{
			jLblNationalityVal = new JLabel();
			jPnlPlayerInformation.add(jLblNationalityVal);
			jLblNationalityVal.setBounds(REC_NATIONALITYVAL);
			jLblNationalityVal.setFont(fontNationality);
		}
		{
			jLblAtClubSince = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubSince);
			jLblAtClubSince.setBounds(REC_ATCLUBSINCE);
			jLblAtClubSince.setFont(fontDescription);
			jLblAtClubSince.setText("Im Verein seit:");
		}
		{
			jLblAtClubSinceVal = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubSinceVal);
			jLblAtClubSinceVal.setBounds(REC_ATCLUBSINCEVAL);
			jLblAtClubSinceVal.setFont(fontAtClubSince);
		}
		{
			jLblAtClubUntil = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubUntil);
			jLblAtClubUntil.setBounds(REC_ATCLUBUNTIL);
			jLblAtClubUntil.setFont(fontDescription);
			jLblAtClubUntil.setText("Im Verein bis:");
		}
		{
			jLblAtClubUntilVal = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubUntilVal);
			jLblAtClubUntilVal.setBounds(REC_ATCLUBUNTILVAL);
			jLblAtClubUntilVal.setFont(fontAtClubSince);
		}
		// Change information
		{
			jCBPositions = new JComboBox<>();
			jPnlPlayerInformation.add(jCBPositions);
			jCBPositions.setBounds(REC_POSITIONVAL);
			jCBPositions.setModel(new DefaultComboBoxModel<>(positionen));
			jCBPositions.setVisible(false);
		}
		{
			jTFSquadNumber = new JTextField();
			jPnlPlayerInformation.add(jTFSquadNumber);
			jTFSquadNumber.setBounds(REC_SQUADNUMBER);
			alignCenter(jTFSquadNumber);
			jTFSquadNumber.setFont(fontSquadNumber);
			jTFSquadNumber.setVisible(false);
		}
		{
			jTFFirstNames = new JTextField();
			jPnlPlayerInformation.add(jTFFirstNames);
			jTFFirstNames.setBounds(REC_FIRSTNAMES);
			jTFFirstNames.setFont(fontNames);
			jTFFirstNames.setVisible(false);
			jTFFirstNames.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					jTFFirstNames.selectAll();
				}
			});
		}
		{
			jTFLastNames = new JTextField();
			jPnlPlayerInformation.add(jTFLastNames);
			jTFLastNames.setBounds(REC_LASTNAMES);
			jTFLastNames.setFont(fontNames);
			jTFLastNames.setVisible(false);
			jTFLastNames.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					jTFLastNames.selectAll();
				}
			});
		}
		{
			jTFPseudonym = new JTextField();
			jPnlPlayerInformation.add(jTFPseudonym);
			jTFPseudonym.setBounds(REC_PSEUDONYM);
			jTFPseudonym.setFont(fontPseudonym);
			jTFPseudonym.setVisible(false);
			jTFPseudonym.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					jTFPseudonym.selectAll();
				}
			});
		}
		{
			jCBBirthDay = new JComboBox<>();
			jPnlPlayerInformation.add(jCBBirthDay);
			jCBBirthDay.setBounds(REC_BIRTHDAY);
			jCBBirthDay.setVisible(false);
		}
		{
			jCBBirthMonth = new JComboBox<>();
			jPnlPlayerInformation.add(jCBBirthMonth);
			jCBBirthMonth.setBounds(REC_BIRTHMONTH);
			jCBBirthMonth.setModel(new DefaultComboBoxModel<>(monate));
			jCBBirthMonth.setVisible(false);
			jCBBirthMonth.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshCBDayModel();
					}
				}
			});
		}
		{
			jCBBirthYear = new JComboBox<>();
			jPnlPlayerInformation.add(jCBBirthYear);
			jCBBirthYear.setBounds(REC_BIRTHYEAR);
			jCBBirthYear.setModel(new DefaultComboBoxModel<>(jahre));
			jCBBirthYear.setVisible(false);
			jCBBirthYear.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshCBDayModel();
					}
				}
			});
		}
		{
			jTFNationality = new JTextField();
			jPnlPlayerInformation.add(jTFNationality);
			jTFNationality.setBounds(REC_NATIONALITYVAL);
			jTFNationality.setFont(fontNationality);
			jTFNationality.setVisible(false);
		}
		{
			jChBAtClubSinceEver = new JCheckBox();
			jPnlPlayerInformation.add(jChBAtClubSinceEver);
			jChBAtClubSinceEver.setBounds(REC_ATCLUBSINCEEVER);
			jChBAtClubSinceEver.setText("Anfang");
			jChBAtClubSinceEver.setOpaque(false);
			jChBAtClubSinceEver.setVisible(false);
			jChBAtClubSinceEver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeAtClubSinceEver();
				}
			});
		}
		{
			jCBAtClubSinceDay = new JComboBox<>();
			jPnlPlayerInformation.add(jCBAtClubSinceDay);
			jCBAtClubSinceDay.setBounds(REC_ATCLUBSINCEDAY);
			jCBAtClubSinceDay.setVisible(false);
		}
		{
			jCBAtClubSinceMonth = new JComboBox<>();
			jPnlPlayerInformation.add(jCBAtClubSinceMonth);
			jCBAtClubSinceMonth.setBounds(REC_ATCLUBSINCEMONTH);
			jCBAtClubSinceMonth.setModel(new DefaultComboBoxModel<>(monate));
			jCBAtClubSinceMonth.setVisible(false);
			jCBAtClubSinceMonth.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshCBACSinceDayModel();
					}
				}
			});
		}
		{
			jCBAtClubSinceYear = new JComboBox<>();
			jPnlPlayerInformation.add(jCBAtClubSinceYear);
			jCBAtClubSinceYear.setBounds(REC_ATCLUBSINCEYEAR);
			jCBAtClubSinceYear.setModel(new DefaultComboBoxModel<>(beimVereinJahre));
			jCBAtClubSinceYear.setVisible(false);
			jCBAtClubSinceYear.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshCBACSinceDayModel();
					}
				}
			});
		}
		{
			jChBAtClubUntilEver = new JCheckBox();
			jPnlPlayerInformation.add(jChBAtClubUntilEver);
			jChBAtClubUntilEver.setBounds(REC_ATCLUBUNTILEVER);
			jChBAtClubUntilEver.setText("Ende");
			jChBAtClubUntilEver.setOpaque(false);
			jChBAtClubUntilEver.setVisible(false);
			jChBAtClubUntilEver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeAtClubUntilEver();
				}
			});
		}
		{
			jCBAtClubUntilDay = new JComboBox<>();
			jPnlPlayerInformation.add(jCBAtClubUntilDay);
			jCBAtClubUntilDay.setBounds(REC_ATCLUBUNTILDAY);
			jCBAtClubUntilDay.setVisible(false);
		}
		{
			jCBAtClubUntilMonth = new JComboBox<>();
			jPnlPlayerInformation.add(jCBAtClubUntilMonth);
			jCBAtClubUntilMonth.setBounds(REC_ATCLUBUNTILMONTH);
			jCBAtClubUntilMonth.setModel(new DefaultComboBoxModel<>(monate));
			jCBAtClubUntilMonth.setVisible(false);
			jCBAtClubUntilMonth.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshCBACUntilDayModel();
					}
				}
			});
		}
		{
			jCBAtClubUntilYear = new JComboBox<>();
			jPnlPlayerInformation.add(jCBAtClubUntilYear);
			jCBAtClubUntilYear.setBounds(REC_ATCLUBUNTILYEAR);
			jCBAtClubUntilYear.setModel(new DefaultComboBoxModel<>(beimVereinJahre));
			jCBAtClubUntilYear.setVisible(false);
			jCBAtClubUntilYear.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshCBACUntilDayModel();
					}
				}
			});
		}
		// Performance data
		{
			jLblMoreDetails = new JLabel();
			jPnlPlayerInformation.add(jLblMoreDetails);
			jLblMoreDetails.setBounds(REC_DETAILS);
			jLblMoreDetails.setFont(fontMoreDetails);
			jLblMoreDetails.setText("mehr >");
			jLblMoreDetails.setCursor(handCursor);
			jLblMoreDetails.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					changeMoreDetails();
				}
			});
		}
		{
			jLblPerformance = new JLabel();
			jPnlPlayerInformation.add(jLblPerformance);
			jLblPerformance.setBounds(REC_PERFORMANCE);
			jLblPerformance.setFont(fontPerformance);
			jLblPerformance.setText("Leistungsdaten");
		}
		{
			jLblCompetition = new JLabel();
			jPnlPlayerInformation.add(jLblCompetition);
			jLblCompetition.setBounds(REC_COMPETITION);
			jLblCompetition.setFont(fontCompetition);
			jLblCompetition.setText(wettbewerb.getName());
		}
		for (int i = 0; i < NUMBEROFPERFORMANCEDATA; i++) {
			int offset = i >= MATCHES_STARTED && i <= MATCHES_SUB_OFF ? bndsPerf[GAPX] : 0;
			jLblsPerformance[i] = new JLabel();
			jPnlPlayerInformation.add(jLblsPerformance[i]);
			jLblsPerformance[i].setBounds(bndsPerf[STARTX] + offset, bndsPerf[STARTY] + i * bndsPerf[GAPY], bndsPerf[SIZEX] - offset, bndsPerf[SIZEY]);
			jLblsPerformance[i].setFont(fontCompetition);
			
			jLblsPerformanceValues[i] = new JLabel();
			jPnlPlayerInformation.add(jLblsPerformanceValues[i]);
			jLblsPerformanceValues[i].setBounds(bndsPerfV[STARTX], bndsPerfV[STARTY] + i * bndsPerfV[GAPY], bndsPerfV[SIZEX], bndsPerfV[SIZEY]);
			jLblsPerformanceValues[i].setFont(fontCompetition);
		}
		jLblsPerformance[MATCHES_PLAYED].setText("Gespielte Spiele");
		jLblsPerformance[MATCHES_STARTED].setText("in der Startelf");
		jLblsPerformance[MATCHES_SUB_ON].setText("eingewechselt");
		jLblsPerformance[MATCHES_SUB_OFF].setText("ausgewechselt");
		jLblsPerformance[MINUTES_PLAYED].setText("Gespielte Minuten");
		jLblsPerformance[GOALS_SCORED].setText("Tore");
		jLblsPerformance[GOALS_ASSISTED].setText("Vorlagen");
		jLblsPerformance[BOOKED].setText("Gelbe Karten");
		jLblsPerformance[BOOKED_TWICE].setText("Gelb-Rote Karten");
		jLblsPerformance[RED_CARDS].setText("Rote Karten");
		{
			jPnlPerformanceMbM = new JPanel();
			jPnlPlayerInformation.add(jPnlPerformanceMbM);
			jPnlPerformanceMbM.setBounds(REC_PNLPERFMBM);
			jPnlPerformanceMbM.setLayout(null);
			jPnlPerformanceMbM.setBackground(new Color(255, 255, 0));
			jPnlPerformanceMbM.setVisible(false);
		}
		
		setSize(WIDTH + getWindowDecorationWidth(), HEIGHT + getWindowDecorationHeight());
		setResizable(false);
	}
	
	private boolean jBtnChangeInformationActionPerformed() {
		if (!changingInformation) {
			if (moreDetails)	changeMoreDetails();
			refreshCBACSinceDayModel();
			refreshCBACUntilDayModel();
			jTFSquadNumber.setText(jLblSquadNumber.getText());
			jTFFirstNames.setText(player.getFirstNameFile());
			jTFLastNames.setText(player.getLastNameFile());
			jTFPseudonym.setText(jLblPseudonym.getText());
			jCBBirthYear.setSelectedItem(player.getBirthDate() / 10000 + "");
			jCBBirthMonth.setSelectedIndex((player.getBirthDate() % 10000) / 100 - 1);
			jCBBirthDay.setSelectedIndex(player.getBirthDate() % 100 - 1);
			jCBPositions.setSelectedItem(jLblPositionVal.getText());
			jTFNationality.setText(jLblNationalityVal.getText());
			boolean sinceSet = player.getFirstDate() != -1, untilSet = player.getLastDate() != -1;
			if (sinceSet == atClubSinceEver)	jChBAtClubSinceEver.doClick();
			jCBAtClubSinceYear.setSelectedItem(sinceSet ? player.getFirstDate() / 10000 + "" : "");
			jCBAtClubSinceMonth.setSelectedIndex(sinceSet ? player.getFirstDate() / 100 % 100 - 1 : 0);
			jCBAtClubSinceDay.setSelectedIndex(sinceSet ? player.getFirstDate() % 100 - 1 : 0);
			if (untilSet == atClubUntilEver)	jChBAtClubUntilEver.doClick();
			jCBAtClubUntilYear.setSelectedItem(untilSet ? player.getLastDate() / 10000 + "" : "");
			jCBAtClubUntilMonth.setSelectedIndex(untilSet ? player.getLastDate() / 100 % 100 - 1 : 0);
			jCBAtClubUntilDay.setSelectedIndex(untilSet ? player.getLastDate() % 100 - 1 : 0);
			jBtnChangeInformation.setText("speichern");
			jLblMoreDetails.setVisible(false);
		} else {
			String firstName = jTFFirstNames.getText();
			String lastName = jTFLastNames.getText();
			String pseudonym = jTFPseudonym.getText();
			if (pseudonym.isEmpty())	pseudonym = null;
			int birthDate = 10000 * Integer.parseInt((String) jCBBirthYear.getSelectedItem()) + 100 * (1 + jCBBirthMonth.getSelectedIndex()) + jCBBirthDay.getSelectedIndex() + 1;
			String nationality = jTFNationality.getText();
			String position = (String) jCBPositions.getSelectedItem();
			int firstDate = -1, lastDate = -1, secondFDate = -1;
			if (!atClubSinceEver)	firstDate = 10000 * Integer.parseInt((String) jCBAtClubSinceYear.getSelectedItem()) + 100 * (jCBAtClubSinceMonth.getSelectedIndex() + 1) + jCBAtClubSinceDay.getSelectedIndex() + 1;
			if (!atClubUntilEver)	lastDate = 10000 * Integer.parseInt((String) jCBAtClubUntilYear.getSelectedItem()) + 100 * (jCBAtClubUntilMonth.getSelectedIndex() + 1) + jCBAtClubUntilDay.getSelectedIndex() + 1;
			secondFDate = player.getSecondFirstDate();
			int squadNumber = Integer.parseInt(jTFSquadNumber.getText());
			// check if squad number is unique
			for (Spieler player : player.getTeam().getPlayers()) {
				if (player == this.player)	continue;
				if (player.getSquadNumber() == squadNumber) {
					if (player.playedAtTheSameTimeAs(firstDate, lastDate, secondFDate)) {
						message("Diese Rückennummer kann nicht verwendet werden, da sie bereits einem anderen Spieler zugeteilt ist.");
						return false;
					}
				}
			}
			player.updateInfo(firstName, lastName, pseudonym, birthDate, nationality, position, squadNumber, firstDate, lastDate, secondFDate);
			if (addingNewPlayer) {
				player.getTeam().addPlayer(player);
				addingNewPlayer = false;
			}
			uebersicht.showKader();
			setPlayerInformation();
			setPerformance();
			showPhoto();
			jBtnChangeInformation.setText("ändern");
		}
		changingInformation = !changingInformation;
		jLblPositionVal.setVisible(!changingInformation);
		jLblSquadNumber.setVisible(!changingInformation);
		jLblFirstNames.setVisible(!changingInformation);
		jLblLastNames.setVisible(!changingInformation);
		jLblPseudonym.setVisible(!changingInformation);
		jLblBirthDateVal.setVisible(!changingInformation);
		jLblNationalityVal.setVisible(!changingInformation);
		jLblAtClubSinceVal.setVisible(!changingInformation && player.getFirstDate() != -1);
		jLblAtClubUntilVal.setVisible(!changingInformation && player.getLastDate() != -1);
		jCBPositions.setVisible(changingInformation);
		jTFSquadNumber.setVisible(changingInformation);
		jTFFirstNames.setVisible(changingInformation);
		jTFLastNames.setVisible(changingInformation);
		jTFPseudonym.setVisible(changingInformation);
		jCBBirthDay.setVisible(changingInformation);
		jCBBirthMonth.setVisible(changingInformation);
		jCBBirthYear.setVisible(changingInformation);
		jTFNationality.setVisible(changingInformation);
		jChBAtClubSinceEver.setVisible(changingInformation);
		jCBAtClubSinceDay.setVisible(changingInformation && !atClubSinceEver);
		jCBAtClubSinceMonth.setVisible(changingInformation && !atClubSinceEver);
		jCBAtClubSinceYear.setVisible(changingInformation && !atClubSinceEver);
		jChBAtClubUntilEver.setVisible(changingInformation);
		jCBAtClubUntilDay.setVisible(changingInformation && !atClubUntilEver);
		jCBAtClubUntilMonth.setVisible(changingInformation && !atClubUntilEver);
		jCBAtClubUntilYear.setVisible(changingInformation && !atClubUntilEver);
		jLblAtClubSince.setVisible(changingInformation || player.getFirstDate() != -1);
		jLblAtClubUntil.setVisible(changingInformation || player.getLastDate() != -1);
		
		jLblPerformance.setVisible(!changingInformation);
		jLblCompetition.setVisible(!changingInformation);
		for (int i = 0; i < NUMBEROFPERFORMANCEDATA; i++) {
			jLblsPerformance[i].setVisible(!changingInformation);
			jLblsPerformanceValues[i].setVisible(!changingInformation);
		}
		return true;
	}
	
	private void changeAtClubSinceEver() {
		atClubSinceEver = !atClubSinceEver;
		jCBAtClubSinceDay.setVisible(!atClubSinceEver);
		jCBAtClubSinceMonth.setVisible(!atClubSinceEver);
		jCBAtClubSinceYear.setVisible(!atClubSinceEver);
	}
	
	private void changeAtClubUntilEver() {
		atClubUntilEver = !atClubUntilEver;
		jCBAtClubUntilDay.setVisible(!atClubUntilEver);
		jCBAtClubUntilMonth.setVisible(!atClubUntilEver);
		jCBAtClubUntilYear.setVisible(!atClubUntilEver);
	}
	
	private void changeMoreDetails() {
		moreDetails = !moreDetails;
		jLblMoreDetails.setText(moreDetails ? "< weniger" : "mehr >");
		
		jLblPerformance.setVisible(!moreDetails);
		jLblCompetition.setVisible(!moreDetails);
		jPnlPerformanceMbM.setVisible(moreDetails);
		for (int i = 0; i < NUMBEROFPERFORMANCEDATA; i++) {
			jLblsPerformance[i].setVisible(!moreDetails);
			jLblsPerformanceValues[i].setVisible(!moreDetails);
		}
		if (moreDetails) {
			ArrayList<int[]> performanceMbM = player.getTeam().getPerformanceDataMatchByMatch(player);
			createLabels(performanceMbM.size());
			int index = 0;
			for (int[] performance : performanceMbM) {
				index++;
				String nothing = performance[2] != 91 ? "." : "X";
				jLblsPerformanceMbMMatchday.get(index).setText(performance[0] + 1 + ".");
				jLblsPerformanceMbMOpponent.get(index).setText(player.getTeam().getWettbewerb().getMannschaften()[performance[1] - 1].getName());
				jLblsPerformanceMbMSubOn.get(index).setText((performance[2] - 1) % 90 != 0 ? performance[2] + "." : nothing);
				jLblsPerformanceMbMSubOff.get(index).setText(performance[3] != 91 ? performance[3] + "." : nothing);
				jLblsPerformanceMbMGoals.get(index).setText(performance[4] != 0 ? performance[4] + "" : nothing);
				jLblsPerformanceMbMAssists.get(index).setText(performance[5] != 0 ? performance[5] + "" : nothing);
				jLblsPerformanceMbMBooked.get(index).setText(performance[6] != 0 ? performance[6] + "." : nothing);
				jLblsPerformanceMbMBookedTwice.get(index).setText(performance[7] != 0 ? performance[7] + "." : nothing);
				jLblsPerformanceMbMRedCard.get(index).setText(performance[8] != 0 ? performance[8] + "." : nothing);
			}
			for (int i = 0; i < jLblsPerformanceMbMMatchday.size(); i++) {
				boolean showRow = i <= performanceMbM.size();
				jLblsPerformanceMbMMatchday.get(i).setVisible(showRow);
				jLblsPerformanceMbMOpponent.get(i).setVisible(showRow);
				jLblsPerformanceMbMSubOn.get(i).setVisible(showRow);
				jLblsPerformanceMbMSubOff.get(i).setVisible(showRow);
				jLblsPerformanceMbMGoals.get(i).setVisible(showRow);
				jLblsPerformanceMbMAssists.get(i).setVisible(showRow);
				jLblsPerformanceMbMBooked.get(i).setVisible(showRow);
				jLblsPerformanceMbMBookedTwice.get(i).setVisible(showRow);
				jLblsPerformanceMbMRedCard.get(i).setVisible(showRow);
			}
			jPnlPerformanceMbM.setSize(470, (performanceMbM.size() + 1) * 20);
		}
	}
	
	private void createLabels(int totalNumber) {
		int size = jLblsPerformanceMbMMatchday.size();
		while (size <= totalNumber) {
			JLabel label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignRight(label);
			label.setBounds(bndsPMbMMD[STARTX], bndsPMbMMD[STARTY] + size * bndsPMbMMD[GAPY], bndsPMbMMD[SIZEX], bndsPMbMMD[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("Sp.");
				label.setToolTipText("Spieltag");
			}
			jLblsPerformanceMbMMatchday.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignLeft(label);
			label.setBounds(bndsPMbMO[STARTX], bndsPMbMO[STARTY] + size * bndsPMbMO[GAPY], bndsPMbMO[SIZEX], bndsPMbMO[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0)	label.setText("Gegner");
			jLblsPerformanceMbMOpponent.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMOn[STARTX], bndsPMbMOn[STARTY] + size * bndsPMbMOn[GAPY], bndsPMbMOn[SIZEX], bndsPMbMOn[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("E");
				label.setToolTipText("eingewechselt");
			}
			jLblsPerformanceMbMSubOn.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMOff[STARTX], bndsPMbMOff[STARTY] + size * bndsPMbMOff[GAPY], bndsPMbMOff[SIZEX], bndsPMbMOff[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("A");
				label.setToolTipText("ausgewechselt");
			}
			jLblsPerformanceMbMSubOff.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMG[STARTX], bndsPMbMG[STARTY] + size * bndsPMbMG[GAPY], bndsPMbMG[SIZEX], bndsPMbMG[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("T");
				label.setToolTipText("Tore");
			}
			jLblsPerformanceMbMGoals.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMA[STARTX], bndsPMbMA[STARTY] + size * bndsPMbMA[GAPY], bndsPMbMA[SIZEX], bndsPMbMA[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("V");
				label.setToolTipText("Vorlagen");
			}
			jLblsPerformanceMbMAssists.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMB[STARTX], bndsPMbMB[STARTY] + size * bndsPMbMB[GAPY], bndsPMbMB[SIZEX], bndsPMbMB[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("G");
				label.setToolTipText("Gelbe Karte");
			}
			jLblsPerformanceMbMBooked.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMBT[STARTX], bndsPMbMBT[STARTY] + size * bndsPMbMBT[GAPY], bndsPMbMBT[SIZEX], bndsPMbMBT[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("GR");
				label.setToolTipText("Gelb-Rote Karte");
			}
			jLblsPerformanceMbMBookedTwice.add(label);
			
			label = new JLabel();
			jPnlPerformanceMbM.add(label);
			alignCenter(label);
			label.setBounds(bndsPMbMR[STARTX], bndsPMbMR[STARTY] + size * bndsPMbMR[GAPY], bndsPMbMR[SIZEX], bndsPMbMR[SIZEY]);
			label.setFont(size != 0 ? fontDetails : fontDetailsHeadline);
			if (size == 0) {
				label.setText("R");
				label.setToolTipText("Rote Karte");
			}
			jLblsPerformanceMbMRedCard.add(label);
			
			size++;
		}
	}
	
	private void refreshCBACSinceDayModel() {
		int month = jCBAtClubSinceMonth.getSelectedIndex() + 1, year = Integer.parseInt((String) jCBAtClubSinceYear.getSelectedItem());
		int daysInMonth = numberOfDaysInMonth(month, year);
		String[] days = new String[daysInMonth];
		for (int i = 1; i <= days.length; i++) {
			days[i - 1] = (i / 10) + "" + (i % 10) + ".";
		}
		int day = jCBAtClubSinceDay.getSelectedIndex();
		jCBAtClubSinceDay.setModel(new DefaultComboBoxModel<>(days));
		jCBAtClubSinceDay.setSelectedIndex(Math.min(day, days.length - 1));
	}
	
	private void refreshCBACUntilDayModel() {
		int month = jCBAtClubUntilMonth.getSelectedIndex() + 1, year = Integer.parseInt((String) jCBAtClubUntilYear.getSelectedItem());
		int daysInMonth = numberOfDaysInMonth(month, year);
		String[] days = new String[daysInMonth];
		for (int i = 1; i <= days.length; i++) {
			days[i - 1] = (i / 10) + "" + (i % 10) + ".";
		}
		int day = jCBAtClubUntilDay.getSelectedIndex();
		jCBAtClubUntilDay.setModel(new DefaultComboBoxModel<>(days));
		jCBAtClubUntilDay.setSelectedIndex(Math.min(day, days.length - 1));
	}
	
	private void refreshCBDayModel() {
		int month = jCBBirthMonth.getSelectedIndex() + 1, year = Integer.parseInt((String) jCBBirthYear.getSelectedItem());
		int daysInMonth = numberOfDaysInMonth(month, year);
		String[] days = new String[daysInMonth];
		for (int i = 1; i <= days.length; i++) {
			days[i - 1] = (i / 10) + "" + (i % 10) + ".";
		}
		int day = jCBBirthDay.getSelectedIndex();
		jCBBirthDay.setModel(new DefaultComboBoxModel<>(days));
		jCBBirthDay.setSelectedIndex(Math.min(day, days.length - 1));
	}
	
	public void addPlayer(Mannschaft team) {
		if (addingNewPlayer)  {
			message("Sie fügen bereits einen neuen Spieler hinzu.");
			return;
		}
		addingNewPlayer = true;
		String firstName = "Vorname";
		String lastName = "Nachname";
		int birthDate = (wettbewerb.getYear() - averageAge) * 10000 + 101;
		String nationality = "Deutschland";
		int squadNumber = team.getNextFreeSquadNumber();
		Spieler newPlayer = new Spieler(firstName, lastName, null, birthDate, nationality, Position.MITTELFELD, team, squadNumber);
		setPlayer(newPlayer);
		jBtnChangeInformation.doClick();
	}
	
	public void setPlayer(Spieler player) {
		if (moreDetails)	changeMoreDetails();
		if (changingInformation && !jBtnChangeInformationActionPerformed())	return;
		this.player = player;
		
		setPlayerInformation();
		if (!addingNewPlayer)	setPerformance();
		showPhoto();
	}
	
	private void setPlayerInformation() {
		jLblSquadNumber.setText("" + player.getSquadNumber());
		jLblFirstNames.setText(player.getFirstName());
		jLblLastNames.setText(player.getLastName());
		jLblFirstNames.setFont(player.getFirstName().length() < maxNumberOfCharacters ? fontNames : fontNamesSmall);
		jLblLastNames.setFont(player.getLastName().length() < maxNumberOfCharacters ? fontNames : fontNamesSmall);
		jLblPseudonym.setText(player.getPseudonym() != null ? player.getPseudonym() : "");
		jLblBirthDateVal.setText(MyDate.datum(player.getBirthDate()));
		jLblPositionVal.setText(player.getPosition().getName());
		jLblNationalityVal.setText(player.getNationality());
		
		int atClubSince = player.getFirstDate();
		int atClubUntil = player.getLastDate();
		boolean sinceSet = atClubSince != -1, untilSet = atClubUntil != -1;
		jLblAtClubSinceVal.setText(sinceSet ? MyDate.datum(atClubSince) : "");
		jLblAtClubSince.setVisible(sinceSet);
		jLblAtClubSinceVal.setVisible(sinceSet);
		jLblAtClubUntilVal.setText(untilSet ? MyDate.datum(atClubUntil) : "");
		jLblAtClubUntil.setVisible(untilSet);
		jLblAtClubUntilVal.setVisible(untilSet);
		setTitle(player.getFullNameShort() + " (#" + player.getSquadNumber() + ")");
	}
	
	private void setPerformance() {
		int[] performanceData = player.getTeam().getPerformanceData(player);
		jLblsPerformanceValues[MATCHES_PLAYED].setText("" + performanceData[MATCHES_PLAYED]);
		jLblsPerformanceValues[MATCHES_STARTED].setText("" + performanceData[MATCHES_STARTED]);
		jLblsPerformanceValues[MATCHES_SUB_ON].setText("" + performanceData[MATCHES_SUB_ON]);
		jLblsPerformanceValues[MATCHES_SUB_OFF].setText("" + performanceData[MATCHES_SUB_OFF]);
		jLblsPerformanceValues[MINUTES_PLAYED].setText("" + performanceData[MINUTES_PLAYED]);
		jLblsPerformanceValues[GOALS_SCORED].setText("" + performanceData[GOALS_SCORED]);
		jLblsPerformanceValues[GOALS_ASSISTED].setText("" + performanceData[GOALS_ASSISTED]);
		jLblsPerformanceValues[BOOKED].setText("" + performanceData[BOOKED]);
		jLblsPerformanceValues[BOOKED_TWICE].setText("" + performanceData[BOOKED_TWICE]);
		jLblsPerformanceValues[RED_CARDS].setText("" + performanceData[RED_CARDS]);
		jLblMoreDetails.setVisible(performanceData[MATCHES_PLAYED] != 0);
	}
	
	private void showPhoto() {
		String playerName = removeUmlaute(player.getFullNameShort());
		playerName = playerName.toLowerCase().replace(' ', '-');
		Mannschaft mannschaft = player.getTeam();
		String url = "file:///" + mannschaft.getPhotoDirectory() + playerName + ".jpg";
		String urlKlein = "file:///" + mannschaft.getPhotoDirectory() + "klein" + File.separator + playerName + "_klein.jpg";
		
		Image image = null;
		if (jLblImage != null) {
			jLblImage.setVisible(false);
		}
		try {
			image = ImageIO.read(new URL(url));
			double factor = Math.min((double) 350 / image.getWidth(null), (double) 800 / image.getHeight(null));
			image = resizeImage(image, (int) (image.getWidth(null) * factor), (int) (image.getHeight(null) * factor));
			jLblImage = new JLabel(new ImageIcon(image));
		} catch (Exception e) {
			try {
				image = ImageIO.read(new URL(urlKlein));
				double factor = Math.min((double) 350 / image.getWidth(null), (double) 800 / image.getHeight(null));
				image = resizeImage(image, (int) (image.getWidth(null) * factor), (int) (image.getHeight(null) * factor));
				jLblImage = new JLabel(new ImageIcon(image));
			} catch (Exception e2) {
				jLblImage = new JLabel("Es wurde kein Foto zu diesem Spieler gefunden.");
			}
		}
		
		jLblImage.setBounds(20, 10, 350, 800);
		jPnlPlayerInformation.add(jLblImage);
		jLblImage.setVisible(true);
	}
}
