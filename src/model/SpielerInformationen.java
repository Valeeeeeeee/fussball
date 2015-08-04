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
	private Font fontBirthDate = new Font("Calibri", 0, 24);
	private Font fontNames = new Font("Calibri", 0, 30);
	private Font fontPosition = new Font("Calibri", 0, 24);
	private Font fontPseudonym = new Font("Calibri", 0, 18);
	private Font fontSquadnumber = new Font("Calibri", 0, 62);
	
	private JPanel jPnlPlayerInformation;
	private JLabel jLblSquadnumber;
	private JLabel jLblFirstNames;
	private JLabel jLblLastNames;
	private JLabel jLblPseudonym;
	private JLabel jLblBirthDate;
	private JLabel jLblPosition;
	private JScrollPane jSPImage;
	private JLabel jLblImage;
	
	public SpielerInformationen() {
		
		initGUI();
	}
	
	private void initGUI() {
		setLayout(null);
		setForeground(background);
		
		// TODO weitere Daten
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
			jLblSquadnumber.setBounds(530, 40, 70, 70);
			jLblSquadnumber.setHorizontalAlignment(SwingConstants.CENTER);
			jLblSquadnumber.setFont(fontSquadnumber);
			jLblSquadnumber.setOpaque(true);
		}
		{
			jLblFirstNames = new JLabel();
			jPnlPlayerInformation.add(jLblFirstNames);
			jLblFirstNames.setBounds(610, 40, 230, 30);
			jLblFirstNames.setFont(fontNames);
			jLblFirstNames.setOpaque(true);
		}
		{
			jLblLastNames = new JLabel();
			jPnlPlayerInformation.add(jLblLastNames);
			jLblLastNames.setBounds(610, 80, 230, 30);
			jLblLastNames.setFont(fontNames);
			jLblLastNames.setOpaque(true);
		}
		{
			jLblPseudonym = new JLabel();
			jPnlPlayerInformation.add(jLblPseudonym);
			jLblPseudonym.setBounds(530, 120, 220, 30);
			jLblPseudonym.setFont(fontPseudonym);
			jLblPseudonym.setOpaque(true);
		}
		{
			jLblBirthDate = new JLabel();
			jPnlPlayerInformation.add(jLblBirthDate);
			jLblBirthDate.setBounds(530, 160, 120, 30);
			jLblBirthDate.setFont(fontBirthDate);
			jLblBirthDate.setOpaque(true);
		}
		{
			jLblPosition = new JLabel();
			jPnlPlayerInformation.add(jLblPosition);
			jLblPosition.setBounds(530, 200, 110, 30);
			jLblPosition.setFont(fontPosition);
			jLblPosition.setOpaque(true);
		}
		
		setSize(WIDTH, HEIGHT);
		setResizable(false);
	}
	
	public void setPlayer(Spieler player, String url) {
		jLblSquadnumber.setText("" + player.getSquadNumber());
		jLblFirstNames.setText(player.getFirstName());
		jLblLastNames.setText(player.getLastName());
		jLblPseudonym.setText(player.getPseudonym() != null ? player.getPseudonym() : "kein Pseudonym");
		jLblBirthDate.setText(MyDate.datum(player.getBirthDate()));
		jLblPosition.setText(player.getPosition().getName());
		
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
		util.Utilities.log("scroll bar unit increment: " + jSPImage.getVerticalScrollBar().getUnitIncrement());
	}
}
