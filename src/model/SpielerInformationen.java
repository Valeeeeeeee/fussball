package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SpielerInformationen extends JFrame {
	
	private static final long serialVersionUID = -8974853216829127796L;

	public static final int WIDTH = 850 + 6;
	
	public static final int HEIGHT = 830 + 28;
	
	private Color background = new Color(255, 255, 255);
	private Font fontAtClubSince = new Font("Calibri", 0, 24);
	private Font fontBirthDate = new Font("Calibri", 0, 24);
	private Font fontCompetition = new Font("Calibri", 0, 18);
	private Font fontDescription = new Font("Calibri", 0, 15);
	private Font fontNames = new Font("Calibri", 0, 30);
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
	private JScrollPane jSPImage;
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
			jLblSquadnumber.setBounds(530, 65, 70, 65);
			jLblSquadnumber.setHorizontalAlignment(SwingConstants.CENTER);
			jLblSquadnumber.setFont(fontSquadnumber);
		}
		{
			jLblFirstNames = new JLabel();
			jPnlPlayerInformation.add(jLblFirstNames);
			jLblFirstNames.setBounds(610, 60, 230, 30);
			jLblFirstNames.setFont(fontNames);
		}
		{
			jLblLastNames = new JLabel();
			jPnlPlayerInformation.add(jLblLastNames);
			jLblLastNames.setBounds(610, 100, 230, 30);
			jLblLastNames.setFont(fontNames);
		}
		{
			jLblPseudonym = new JLabel();
			jPnlPlayerInformation.add(jLblPseudonym);
			jLblPseudonym.setBounds(530, 140, 220, 30);
			jLblPseudonym.setFont(fontPseudonym);
		}
		{
			jLblBirthDate = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDate);
			jLblBirthDate.setBounds(530, 180, 105, 20);
			jLblBirthDate.setFont(fontDescription);
			jLblBirthDate.setText("Geburtsdatum:");
		}
		{
			jLblBirthDateVal = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDateVal);
			jLblBirthDateVal.setBounds(530, 200, 120, 30);
			jLblBirthDateVal.setFont(fontBirthDate);
		}
		{
			jLblPosition = new JLabel();
			jPnlPlayerInformation.add(jLblPosition);
			jLblPosition.setBounds(700, 180, 65, 20);
			jLblPosition.setFont(fontDescription);
			jLblPosition.setText("Position:");
		}
		{
			jLblPositionVal = new JLabel();
			jPnlPlayerInformation.add(jLblPositionVal);
			jLblPositionVal.setBounds(700, 200, 110, 30);
			jLblPositionVal.setFont(fontPosition);
		}
		{
			jLblNationality = new JLabel();
			jPnlPlayerInformation.add(jLblNationality);
			jLblNationality.setBounds(530, 240, 120, 20);
			jLblNationality.setFont(fontDescription);
			jLblNationality.setText("Nationalitaet(en):");
		}
		{
			jLblNationalityVal = new JLabel();
			jPnlPlayerInformation.add(jLblNationalityVal);
			jLblNationalityVal.setBounds(530, 260, 310, 30);
			jLblNationalityVal.setFont(fontNationality);
		}
		{
			jLblAtClubSince = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubSince);
			jLblAtClubSince.setBounds(530, 300, 110, 20);
			jLblAtClubSince.setFont(fontDescription);
			jLblAtClubSince.setText("Im Verein seit:");
		}
		{
			jLblAtClubSinceVal = new JLabel();
			jPnlPlayerInformation.add(jLblAtClubSinceVal);
			jLblAtClubSinceVal.setBounds(530, 320, 120, 30);
			jLblAtClubSinceVal.setFont(fontAtClubSince);
		}
		{
			jLblPerformance = new JLabel();
			jPnlPlayerInformation.add(jLblPerformance);
			jLblPerformance.setBounds(530, 380, 120, 25);
			jLblPerformance.setFont(fontPerformance);
			jLblPerformance.setText("Leistungsdaten");
		}
		{
			jLblCompetition = new JLabel();
			jPnlPlayerInformation.add(jLblCompetition);
			jLblCompetition.setBounds(660, 380, 180, 25);
			jLblCompetition.setFont(fontCompetition);
			jLblCompetition.setText(wettbewerb.getName());
		}
		{
			jLblGamesPlayed = new JLabel();
			jPnlPlayerInformation.add(jLblGamesPlayed);
			jLblGamesPlayed.setBounds(530, 410, 160, 25);
			jLblGamesPlayed.setFont(fontCompetition);
			jLblGamesPlayed.setText("Gespielte Spiele");
		}
		{
			jLblGamesPlayedVal = new JLabel();
			jPnlPlayerInformation.add(jLblGamesPlayedVal);
			jLblGamesPlayedVal.setBounds(700, 410, 50, 25);
			jLblGamesPlayedVal.setFont(fontCompetition);
			jLblGamesPlayedVal.setText("Gespielte Spiele");
		}
		{
			jLblGamesStarted = new JLabel();
			jPnlPlayerInformation.add(jLblGamesStarted);
			jLblGamesStarted.setBounds(560, 440, 130, 25);
			jLblGamesStarted.setFont(fontCompetition);
			jLblGamesStarted.setText("in der Startelf");
		}
		{
			jLblGamesStartedVal = new JLabel();
			jPnlPlayerInformation.add(jLblGamesStartedVal);
			jLblGamesStartedVal.setBounds(700, 440, 50, 25);
			jLblGamesStartedVal.setFont(fontCompetition);
			jLblGamesStartedVal.setText("in der Startelf");
		}
		{
			jLblSubstitutedOn = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOn);
			jLblSubstitutedOn.setBounds(560, 470, 130, 25);
			jLblSubstitutedOn.setFont(fontCompetition);
			jLblSubstitutedOn.setText("eingewechselt");
		}
		{
			jLblSubstitutedOnVal = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOnVal);
			jLblSubstitutedOnVal.setBounds(700, 470, 50, 25);
			jLblSubstitutedOnVal.setFont(fontCompetition);
			jLblSubstitutedOnVal.setText("eingewechselt");
		}
		{
			jLblSubstitutedOff = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOff);
			jLblSubstitutedOff.setBounds(560, 500, 130, 25);
			jLblSubstitutedOff.setFont(fontCompetition);
			jLblSubstitutedOff.setText("ausgewechselt");
		}
		{
			jLblSubstitutedOffVal = new JLabel();
			jPnlPlayerInformation.add(jLblSubstitutedOffVal);
			jLblSubstitutedOffVal.setBounds(700, 500, 50, 25);
			jLblSubstitutedOffVal.setFont(fontCompetition);
			jLblSubstitutedOffVal.setText("ausgewechselt");
		}
		{
			jLblMinutesPlayed = new JLabel();
			jPnlPlayerInformation.add(jLblMinutesPlayed);
			jLblMinutesPlayed.setBounds(530, 530, 160, 25);
			jLblMinutesPlayed.setFont(fontCompetition);
			jLblMinutesPlayed.setText("Gespielte Minuten");
		}
		{
			jLblMinutesPlayedVal = new JLabel();
			jPnlPlayerInformation.add(jLblMinutesPlayedVal);
			jLblMinutesPlayedVal.setBounds(700, 530, 50, 25);
			jLblMinutesPlayedVal.setFont(fontCompetition);
			jLblMinutesPlayedVal.setText("Gespielte Minuten");
		}
		{
			jLblGoalsScored = new JLabel();
			jPnlPlayerInformation.add(jLblGoalsScored);
			jLblGoalsScored.setBounds(530, 560, 160, 25);
			jLblGoalsScored.setFont(fontCompetition);
			jLblGoalsScored.setText("Tore");
		}
		{
			jLblGoalsScoredVal = new JLabel();
			jPnlPlayerInformation.add(jLblGoalsScoredVal);
			jLblGoalsScoredVal.setBounds(700, 560, 50, 25);
			jLblGoalsScoredVal.setFont(fontCompetition);
			jLblGoalsScoredVal.setText("Tore");
		}
		
		setSize(WIDTH, HEIGHT);
		setResizable(false);
	}
	
	public void setPlayer(Spieler player, String url) {
		jLblSquadnumber.setText("" + player.getSquadNumber());
		jLblFirstNames.setText(player.getFirstName());
		jLblLastNames.setText(player.getLastName());
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
		jLblGamesPlayedVal.setText("" + performanceData[0]);
		jLblGamesStartedVal.setText("" + performanceData[1]);
		jLblSubstitutedOnVal.setText("" + performanceData[2]);
		jLblSubstitutedOffVal.setText("" + performanceData[3]);
		jLblMinutesPlayedVal.setText("" + performanceData[4]);
		jLblGoalsScoredVal.setText("" + performanceData[5]);
		
		Image image = null;
		if (jLblImage != null) {
			jLblImage.setVisible(false);
			jSPImage.setVisible(false);
		}
		try {
			image = ImageIO.read(new URL(url));
			jLblImage = new JLabel(new ImageIcon(image));
		} catch (Exception e) {
			jLblImage = new JLabel("Es wurde kein Foto zu diesem Spieler gefunden.");
		}
		jSPImage = new JScrollPane(jLblImage);
		jSPImage.setBounds(10, 10, 505, 810);
		jPnlPlayerInformation.add(jSPImage);
		jLblImage.setVisible(true);
		jSPImage.setVisible(true);
		jSPImage.getVerticalScrollBar().setUnitIncrement(20);
		setTitle(player.getFullNameShort() + " (#" + player.getSquadNumber() + ")");
	}
}
