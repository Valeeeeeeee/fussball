package model;

import static util.Utilities.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SpielerInformationen extends JFrame {
	
	private static final long serialVersionUID = -8974853216829127796L;

	
	public static final int WIDTH = 860;
	
	public static final int HEIGHT = 830;
	
	private static final int maxNumberOfCharacters = 20;
	private static final int minimumAge = 14;
	private static final int maximumAge = 50;
	
	private Color background = new Color(255, 255, 255);
	private Font fontAtClubSince = new Font("Calibri", 0, 24);
	private Font fontBirthDate = new Font("Calibri", 0, 24);
	private Font fontCompetition = new Font("Calibri", 0, 18);
	private Font fontDescription = new Font("Calibri", 0, 15);
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
	
	private JLabel jLblPerformance;
	private JLabel jLblCompetition;
	private JLabel jLblGamesPlayed;
	private JLabel jLblGamesPlayedVal;
	private JLabel jLblGamesStarted;
	private JLabel jLblGamesStartedVal;
	private JLabel jLblSubstitutedOn;
	private JLabel jLblSubstitutedOnVal;
	private JLabel jLblSubstitutedOff;
	private JLabel jLblSubstitutedOffVal;
	private JLabel jLblMinutesPlayed;
	private JLabel jLblMinutesPlayedVal;
	private JLabel jLblGoalsScored;
	private JLabel jLblGoalsScoredVal;
	private JLabel jLblGoalsAssisted;
	private JLabel jLblGoalsAssistedVal;
	private JLabel jLblBooked;
	private JLabel jLblBookedVal;
	private JLabel jLblBookedTwice;
	private JLabel jLblBookedTwiceVal;
	private JLabel jLblRedCards;
	private JLabel jLblRedCardsVal;
	private JLabel jLblImage;
	
	private Uebersicht uebersicht;
	private Spieler player;
	private Wettbewerb wettbewerb;
	
	private boolean addingNewPlayer;
	private boolean changingInformation;
	private boolean atClubSinceEver;
	private boolean atClubUntilEver;
	
	public SpielerInformationen(Uebersicht uebersicht, Wettbewerb wettbewerb) {
		super();
		
		this.uebersicht = uebersicht;
		this.wettbewerb = wettbewerb;
		
		initGUI();
	}
	
	private void initGUI() {
		setLayout(null);
		setForeground(background);
		
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
		}
		{
			jTFLastNames = new JTextField();
			jPnlPlayerInformation.add(jTFLastNames);
			jTFLastNames.setBounds(REC_LASTNAMES);
			jTFLastNames.setFont(fontNames);
			jTFLastNames.setVisible(false);
		}
		{
			jTFPseudonym = new JTextField();
			jPnlPlayerInformation.add(jTFPseudonym);
			jTFPseudonym.setBounds(REC_PSEUDONYM);
			jTFPseudonym.setFont(fontPseudonym);
			jTFPseudonym.setVisible(false);
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
			jLblPerformance = new JLabel();
			jPnlPlayerInformation.add(jLblPerformance);
			jLblPerformance.setBounds(410, 380, 140, 25);
			jLblPerformance.setFont(fontPerformance);
			jLblPerformance.setText("Leistungsdaten");
		}
		{
			jLblCompetition = new JLabel();
			jPnlPlayerInformation.add(jLblCompetition);
			jLblCompetition.setBounds(570, 380, 200, 25);
			jLblCompetition.setFont(fontCompetition);
			jLblCompetition.setText(wettbewerb.getName());
		}
		{
			jLblGamesPlayed = new JLabel();
			jPnlPlayerInformation.add(jLblGamesPlayed);
			jLblGamesPlayed.setBounds(410, 410, 160, 25);
			jLblGamesPlayed.setFont(fontCompetition);
			jLblGamesPlayed.setText("Gespielte Spiele");
		}
		{
			jLblGamesPlayedVal = new JLabel();
			jPnlPlayerInformation.add(jLblGamesPlayedVal);
			jLblGamesPlayedVal.setBounds(620, 410, 50, 25);
			jLblGamesPlayedVal.setFont(fontCompetition);
		}
		{
			jLblGamesStarted = new JLabel();
			jPnlPlayerInformation.add(jLblGamesStarted);
			jLblGamesStarted.setBounds(440, 440, 130, 25);
			jLblGamesStarted.setFont(fontCompetition);
			jLblGamesStarted.setText("in der Startelf");
		}
		{
			jLblGamesStartedVal = new JLabel();
			jPnlPlayerInformation.add(jLblGamesStartedVal);
			jLblGamesStartedVal.setBounds(620, 440, 50, 25);
			jLblGamesStartedVal.setFont(fontCompetition);
		}
		{
			jLblSubstitutedOn = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOn);
			jLblSubstitutedOn.setBounds(440, 470, 130, 25);
			jLblSubstitutedOn.setFont(fontCompetition);
			jLblSubstitutedOn.setText("eingewechselt");
		}
		{
			jLblSubstitutedOnVal = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOnVal);
			jLblSubstitutedOnVal.setBounds(620, 470, 50, 25);
			jLblSubstitutedOnVal.setFont(fontCompetition);
		}
		{
			jLblSubstitutedOff = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOff);
			jLblSubstitutedOff.setBounds(440, 500, 130, 25);
			jLblSubstitutedOff.setFont(fontCompetition);
			jLblSubstitutedOff.setText("ausgewechselt");
		}
		{
			jLblSubstitutedOffVal = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOffVal);
			jLblSubstitutedOffVal.setBounds(620, 500, 50, 25);
			jLblSubstitutedOffVal.setFont(fontCompetition);
		}
		{
			jLblMinutesPlayed = new JLabel();
			jPnlPlayerInformation.add(jLblMinutesPlayed);
			jLblMinutesPlayed.setBounds(410, 530, 160, 25);
			jLblMinutesPlayed.setFont(fontCompetition);
			jLblMinutesPlayed.setText("Gespielte Minuten");
		}
		{
			jLblMinutesPlayedVal = new JLabel();
			jPnlPlayerInformation.add(jLblMinutesPlayedVal);
			jLblMinutesPlayedVal.setBounds(620, 530, 50, 25);
			jLblMinutesPlayedVal.setFont(fontCompetition);
		}
		{
			jLblGoalsScored = new JLabel();
			jPnlPlayerInformation.add(jLblGoalsScored);
			jLblGoalsScored.setBounds(410, 560, 160, 25);
			jLblGoalsScored.setFont(fontCompetition);
			jLblGoalsScored.setText("Tore");
		}
		{
			jLblGoalsScoredVal = new JLabel();
			jPnlPlayerInformation.add(jLblGoalsScoredVal);
			jLblGoalsScoredVal.setBounds(620, 560, 50, 25);
			jLblGoalsScoredVal.setFont(fontCompetition);
		}
		{
			jLblGoalsAssisted = new JLabel();
			jPnlPlayerInformation.add(jLblGoalsAssisted);
			jLblGoalsAssisted.setBounds(410, 590, 160, 25);
			jLblGoalsAssisted.setFont(fontCompetition);
			jLblGoalsAssisted.setText("Vorlagen");
		}
		{
			jLblGoalsAssistedVal = new JLabel();
			jPnlPlayerInformation.add(jLblGoalsAssistedVal);
			jLblGoalsAssistedVal.setBounds(620, 590, 50, 25);
			jLblGoalsAssistedVal.setFont(fontCompetition);
		}
		{
			jLblBooked = new JLabel();
			jPnlPlayerInformation.add(jLblBooked);
			jLblBooked.setBounds(410, 620, 160, 25);
			jLblBooked.setFont(fontCompetition);
			jLblBooked.setText("Gelbe Karten");
		}
		{
			jLblBookedVal = new JLabel();
			jPnlPlayerInformation.add(jLblBookedVal);
			jLblBookedVal.setBounds(620, 620, 50, 25);
			jLblBookedVal.setFont(fontCompetition);
		}
		{
			jLblBookedTwice = new JLabel();
			jPnlPlayerInformation.add(jLblBookedTwice);
			jLblBookedTwice.setBounds(410, 650, 160, 25);
			jLblBookedTwice.setFont(fontCompetition);
			jLblBookedTwice.setText("Gelb-Rote Karten");
		}
		{
			jLblBookedTwiceVal = new JLabel();
			jPnlPlayerInformation.add(jLblBookedTwiceVal);
			jLblBookedTwiceVal.setBounds(620, 650, 50, 25);
			jLblBookedTwiceVal.setFont(fontCompetition);
		}
		{
			jLblRedCards = new JLabel();
			jPnlPlayerInformation.add(jLblRedCards);
			jLblRedCards.setBounds(410, 680, 160, 25);
			jLblRedCards.setFont(fontCompetition);
			jLblRedCards.setText("Rote Karten");
		}
		{
			jLblRedCardsVal = new JLabel();
			jPnlPlayerInformation.add(jLblRedCardsVal);
			jLblRedCardsVal.setBounds(620, 680, 50, 25);
			jLblRedCardsVal.setFont(fontCompetition);
		}
		
		setSize(WIDTH + getWindowDecorationWidth(), HEIGHT + getWindowDecorationHeight());
		setResizable(false);
	}
	
	private boolean jBtnChangeInformationActionPerformed() {
		if (!changingInformation) {
			refreshCBACSinceDayModel();
			refreshCBACUntilDayModel();
			jTFSquadNumber.setText(jLblSquadNumber.getText());
			jTFFirstNames.setText(jLblFirstNames.getText());
			jTFLastNames.setText(jLblLastNames.getText());
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
		} else {
			String firstName = jTFFirstNames.getText();
			String lastName = jTFLastNames.getText();
			String pseudonym = jTFPseudonym.getText();
			if (pseudonym.isEmpty())	pseudonym = null;
			int birthDate = 10000 * Integer.parseInt((String) jCBBirthYear.getSelectedItem()) + 100 * (1 + jCBBirthMonth.getSelectedIndex()) + jCBBirthDay.getSelectedIndex() + 1;
			String nationality = jTFNationality.getText();
			String position = (String) jCBPositions.getSelectedItem();
			int firstDate = -1, lastDate = -1;
			if (!atClubSinceEver)	firstDate = 10000 * Integer.parseInt((String) jCBAtClubSinceYear.getSelectedItem()) + 100 * (jCBAtClubSinceMonth.getSelectedIndex() + 1) + jCBAtClubSinceDay.getSelectedIndex() + 1;
			if (!atClubUntilEver)	lastDate = 10000 * Integer.parseInt((String) jCBAtClubUntilYear.getSelectedItem()) + 100 * (jCBAtClubUntilMonth.getSelectedIndex() + 1) + jCBAtClubUntilDay.getSelectedIndex() + 1;
			int squadNumber = Integer.parseInt(jTFSquadNumber.getText());
			// check if squad number is unique
			for (Spieler player : player.getTeam().getPlayers()) {
				if (player == this.player)	continue;
				if (player.getSquadNumber() == squadNumber) {
					if (player.playedAtTheSameTimeAs(firstDate, lastDate)) {
						message("Diese Rückennummer kann nicht verwendet werden, da sie bereits einem anderen Spieler zugeteilt ist.");
						return false;
					}
				}
			}
			player.updateInfo(firstName, lastName, pseudonym, birthDate, nationality, position, squadNumber, firstDate, lastDate);
			if (addingNewPlayer) {
				player.getTeam().addPlayer(player);
				addingNewPlayer = false;
			}
			uebersicht.showKader();
			setPlayerInformation();
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
		int birthDate = (wettbewerb.getYear() - 25) * 10000 + 101;
		String nationality = "Deutschland";
		int squadNumber = team.getNextFreeSquadNumber();
		Spieler newPlayer = new Spieler(firstName, lastName, null, birthDate, nationality, Position.MITTELFELD, team, squadNumber);
		setPlayer(newPlayer);
		jBtnChangeInformation.doClick();
	}
	
	public void setPlayer(Spieler player) {
		if (changingInformation && !jBtnChangeInformationActionPerformed())	return;
		this.player = player;
		
		setPlayerInformation();
		if (!addingNewPlayer)	setPerformance();
		showPhoto();
		
		setTitle(player.getFullNameShort() + " (#" + player.getSquadNumber() + ")");
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
	}
	
	private void setPerformance() {
		int[] performanceData = player.getTeam().getPerformanceData(player);
		jLblGamesPlayedVal.setText("" + performanceData[Mannschaft.MATCHES_PLAYED]);
		jLblGamesStartedVal.setText("" + performanceData[Mannschaft.MATCHES_STARTED]);
		jLblSubstitutedOnVal.setText("" + performanceData[Mannschaft.MATCHES_SUB_ON]);
		jLblSubstitutedOffVal.setText("" + performanceData[Mannschaft.MATCHES_SUB_OFF]);
		jLblMinutesPlayedVal.setText("" + performanceData[Mannschaft.MINUTES_PLAYED]);
		jLblGoalsScoredVal.setText("" + performanceData[Mannschaft.GOALS_SCORED]);
		jLblGoalsAssistedVal.setText("" + performanceData[Mannschaft.GOALS_ASSISTED]);
		jLblBookedVal.setText("" + performanceData[Mannschaft.BOOKED]);
		jLblBookedTwiceVal.setText("" + performanceData[Mannschaft.BOOKED_TWICE]);
		jLblRedCardsVal.setText("" + performanceData[Mannschaft.RED_CARDS]);
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
