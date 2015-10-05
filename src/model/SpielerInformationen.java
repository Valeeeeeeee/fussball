package model;

import static util.Utilities.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SpielerInformationen extends JFrame {
	
	private static final long serialVersionUID = -8974853216829127796L;

	
	public static final int WIDTH = 840;
	
	public static final int HEIGHT = 830;
	
	private static final int maxNumberOfCharacters = 20;
	
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
	private Font fontSquadnumber = new Font("Calibri", 0, 65);
	
	private JPanel jPnlPlayerInformation;
	private JLabel jLblSquadnumber;
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
	
	private Wettbewerb wettbewerb;
	
	public SpielerInformationen(Wettbewerb wettbewerb) {
		super();
		
		this.wettbewerb = wettbewerb;
		
		initGUI();
	}
	
	private void initGUI() {
		setLayout(null);
		setForeground(background);
		
		// TODO Name & Co. veraenderbar
		{
			jPnlPlayerInformation = new JPanel();
			getContentPane().add(jPnlPlayerInformation);
			jPnlPlayerInformation.setLayout(null);
			jPnlPlayerInformation.setBounds(0, 0, WIDTH, HEIGHT);
			jPnlPlayerInformation.setBackground(background);
		}
		{
			jLblSquadnumber = new JLabel();
			jPnlPlayerInformation.add(jLblSquadnumber);
			jLblSquadnumber.setBounds(400, 65, 85, 65);
			alignCenter(jLblSquadnumber);
			jLblSquadnumber.setFont(fontSquadnumber);
		}
		{
			jLblFirstNames = new JLabel();
			jPnlPlayerInformation.add(jLblFirstNames);
			jLblFirstNames.setBounds(500, 60, 320, 35);
			jLblFirstNames.setFont(fontNames);
		}
		{
			jLblLastNames = new JLabel();
			jPnlPlayerInformation.add(jLblLastNames);
			jLblLastNames.setBounds(500, 100, 320, 35);
			jLblLastNames.setFont(fontNames);
		}
		{
			jLblPseudonym = new JLabel();
			jPnlPlayerInformation.add(jLblPseudonym);
			jLblPseudonym.setBounds(500, 140, 220, 30);
			jLblPseudonym.setFont(fontPseudonym);
		}
		{
			jLblBirthDate = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDate);
			jLblBirthDate.setBounds(410, 180, 110, 20);
			jLblBirthDate.setFont(fontDescription);
			jLblBirthDate.setText("Geburtsdatum:");
		}
		{
			jLblBirthDateVal = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDateVal);
			jLblBirthDateVal.setBounds(410, 200, 140, 30);
			jLblBirthDateVal.setFont(fontBirthDate);
		}
		{
			jLblPosition = new JLabel();
			jPnlPlayerInformation.add(jLblPosition);
			jLblPosition.setBounds(660, 180, 65, 20);
			jLblPosition.setFont(fontDescription);
			jLblPosition.setText("Position:");
		}
		{
			jLblPositionVal = new JLabel();
			jPnlPlayerInformation.add(jLblPositionVal);
			jLblPositionVal.setBounds(660, 200, 110, 30);
			jLblPositionVal.setFont(fontPosition);
		}
		{
			jLblNationality = new JLabel();
			jPnlPlayerInformation.add(jLblNationality);
			jLblNationality.setBounds(410, 240, 120, 20);
			jLblNationality.setFont(fontDescription);
			jLblNationality.setText("Nationalität(en):");
		}
		{
			jLblNationalityVal = new JLabel();
			jPnlPlayerInformation.add(jLblNationalityVal);
			jLblNationalityVal.setBounds(410, 260, 380, 30);
			jLblNationalityVal.setFont(fontNationality);
		}
		{
			jLblAtClubSince = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubSince);
			jLblAtClubSince.setBounds(410, 300, 110, 20);
			jLblAtClubSince.setFont(fontDescription);
			jLblAtClubSince.setText("Im Verein seit:");
		}
		{
			jLblAtClubSinceVal = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubSinceVal);
			jLblAtClubSinceVal.setBounds(410, 320, 140, 30);
			jLblAtClubSinceVal.setFont(fontAtClubSince);
		}
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
	
	public void setPlayer(Spieler player, String url) {
		jLblSquadnumber.setText("" + player.getSquadNumber());
		jLblFirstNames.setText(player.getFirstName());
		jLblLastNames.setText(player.getLastName());
		jLblFirstNames.setFont(player.getFirstName().length() < maxNumberOfCharacters ? fontNames : fontNamesSmall);
		jLblLastNames.setFont(player.getLastName().length() < maxNumberOfCharacters ? fontNames : fontNamesSmall);
		jLblPseudonym.setText(player.getPseudonym() != null ? player.getPseudonym() : "");
		jLblBirthDateVal.setText(MyDate.datum(player.getBirthDate()));
		jLblPositionVal.setText(player.getPosition().getName());
		jLblNationalityVal.setText(player.getNationality());
		int atClubSince = player.getFirstDate();
		if (atClubSince != -1) {
			jLblAtClubSinceVal.setText(MyDate.datum(atClubSince));
			jLblAtClubSince.setVisible(true);
			jLblAtClubSinceVal.setVisible(true);
		} else {
			jLblAtClubSinceVal.setText("");
			jLblAtClubSince.setVisible(false);
			jLblAtClubSinceVal.setVisible(false);
		}
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
		
		Image image = null;
		if (jLblImage != null) {
			jLblImage.setVisible(false);
		}
		try {
			image = ImageIO.read(new URL(url));
			double factor = (double) 800 / image.getHeight(null);
			image = resizeImage(image, (int) (image.getWidth(null) * factor), (int) (image.getHeight(null) * factor));
			jLblImage = new JLabel(new ImageIcon(image));
		} catch (Exception e) {
			jLblImage = new JLabel("Es wurde kein Foto zu diesem Spieler gefunden.");
		}
		jLblImage.setBounds(20, 10, 350, 800);
		jPnlPlayerInformation.add(jLblImage);
		jLblImage.setVisible(true);
		setTitle(player.getFullNameShort() + " (#" + player.getSquadNumber() + ")");
	}
}
