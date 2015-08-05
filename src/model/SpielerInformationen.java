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
	private Font fontDescription = new Font("Calibri", 0, 15);
	private Font fontNames = new Font("Calibri", 0, 30);
	private Font fontNationality = new Font("Calibri", 0, 24);
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
	private JScrollPane jSPImage;
	private JLabel jLblImage;
	
	public SpielerInformationen() {
		
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
		
		setSize(WIDTH, HEIGHT);
		setResizable(false);
	}
	
	public void setPlayer(Spieler player, String url) {
		jLblSquadnumber.setText("" + player.getSquadNumber());
		jLblFirstNames.setText(player.getFirstName());
		jLblLastNames.setText(player.getLastName());
		jLblPseudonym.setText(player.getPseudonym() != null ? player.getPseudonym() : "kein Pseudonym");
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
	}
}
