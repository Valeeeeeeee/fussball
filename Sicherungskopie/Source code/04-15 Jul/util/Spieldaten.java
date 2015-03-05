package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import static util.Utilities.*;


public class Spieldaten extends JFrame {
	private static final long serialVersionUID = -3165757640143923413L;
	
	JButton mannschaftLaden;
	JButton jBtnGelbeKarte;
	JButton jBtnRoteKarte;
	JButton jBtnTor;
	JButton jBtnNeuesSpiel;
	
	JButton[] btnsPlayers;
	
	boolean aTeamWasLoaded;
	int editedMatch = -1;
	int editedPlayer = -1;
	int numberOfPlayers;
	
	String[] playersnames;
	String[] zusaetze;
	/**
	 * [game][player][event]
	 */
	SpielEvent[][][] whathappened;
	
	public Spieldaten() {
		super();

		testCheckIfContains();
		
		initGUI();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Spieldaten inst = new Spieldaten();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);

			initializeArrays();
			
			
			{
				mannschaftLaden = new JButton();
				getContentPane().add(mannschaftLaden);
				mannschaftLaden.setBounds(420, 50, 150, 50);
				mannschaftLaden.setText("Mannschaft laden");
				mannschaftLaden.setVisible(true);
				mannschaftLaden.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mannschaftLadenPressed();
					}
				});
			}
			{
				jBtnGelbeKarte = new JButton();
				getContentPane().add(jBtnGelbeKarte);
				jBtnGelbeKarte.setBounds(120, 110, 150, 50);
				jBtnGelbeKarte.setText("Gelbe Karte");
				jBtnGelbeKarte.setVisible(true);
				jBtnGelbeKarte.setEnabled(false);
				jBtnGelbeKarte.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gelbeKartePressed();
					}
				});
			}
			{
				jBtnRoteKarte = new JButton();
				getContentPane().add(jBtnRoteKarte);
				jBtnRoteKarte.setBounds(120, 170, 150, 50);
				jBtnRoteKarte.setText("Rote Karte");
				jBtnRoteKarte.setVisible(true);
				jBtnRoteKarte.setEnabled(false);
				jBtnRoteKarte.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						roteKartePressed();
					}
				});
			}
			{
				jBtnTor = new JButton();
				getContentPane().add(jBtnTor);
				jBtnTor.setBounds(120, 230, 150, 50);
				jBtnTor.setText("Tor");
				jBtnTor.setVisible(true);
				jBtnTor.setEnabled(false);
				jBtnTor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						torPressed();
					}
				});
			}
			{
				jBtnNeuesSpiel = new JButton();
				getContentPane().add(jBtnNeuesSpiel);
				jBtnNeuesSpiel.setBounds(600, 50, 150, 50);
				jBtnNeuesSpiel.setText("Neues Spiel");
				jBtnNeuesSpiel.setVisible(true);
				jBtnNeuesSpiel.setEnabled(false);
				jBtnNeuesSpiel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						neuesSpielPressed();
					}
				});
			}
			
			
			for (int i = 0; i < btnsPlayers.length; i++) {
				final int x = i;
				btnsPlayers[i] = new JButton();
				getContentPane().add(btnsPlayers[i]);
				btnsPlayers[i].setBounds(420 + (i / 6) * (150 + 10), 130 + (i % 6) * (50 + 10), 150, 50);
				btnsPlayers[i].setVisible(true);
				btnsPlayers[i].setEnabled(false);
				btnsPlayers[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						playerSelected(x);
					}
				});
			}

			pack();
			setSize(1440, 874);
			setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeArrays() {
		numberOfPlayers = 23;
		
		btnsPlayers = new JButton[numberOfPlayers];
		
		playersnames = new String[numberOfPlayers];
		zusaetze = new String[numberOfPlayers];
		
		whathappened = new SpielEvent[0][numberOfPlayers][0];
	}
	
	private void setEventsEditable(boolean bool) {
		jBtnGelbeKarte.setEnabled(bool);
		jBtnRoteKarte.setEnabled(bool);
		jBtnTor.setEnabled(bool);
	}
	
	private void setPlayersEditable(boolean bool) {
		for (int i = 0; i < btnsPlayers.length; i++) {
			btnsPlayers[i].setEnabled(bool);
		}
	}
	
	private void neuesSpielPressed() {
		// TODO Test ob Infos komplett
		
		if (!aTeamWasLoaded) {
			JOptionPane.showMessageDialog(null, "Es wurde noch kein Team geladen!");
			return;
		}
		
		setPlayersEditable(true);
		
		if (editedMatch == -1) {
			whathappened = new SpielEvent[1][numberOfPlayers][0];
			editedMatch = 0;
			return;
		}
		
		testAusgabeWhatHappened();
		
		SpielEvent[][][] temp = whathappened;
		
		whathappened = new SpielEvent[whathappened.length + 1][numberOfPlayers][0];
		
		for (int i = 0; i < whathappened.length - 1; i++) {
			for (int j = 0; j < whathappened[i].length; j++) {
				try {
					whathappened[i][j] = temp[i][j];
				} catch (Exception e) {
					whathappened[i][j] = new SpielEvent[0];
					System.out.println("Keine Infos vorhanden gewesen.");
				}
			}
		}
		
		editedMatch = whathappened.length - 1;
		
		testAusgabeWhatHappened();
		
		// TODO whathappenend um eine Länge erhöhen!
	}
	
	private void testAusgabeWhatHappened() {
		System.out.println("\n");
		for (int i = 0; i < whathappened.length; i++) {
			System.out.println("\nSpiel " + (i + 1));
			for (int j = 0; j < whathappened[i].length; j++) {
				try {
					System.out.print("Spieler " + (j + 1) + "     What happened: ");
					System.out.print("  " + whathappened[i][j][0]);
					for (int k = 1; k < whathappened[i][j].length; k++) {
						System.out.print(" -> " + whathappened[i][j][k]);
					}
					System.out.println();
				} catch (Exception e) {
					System.out.println("  Keine Infos.");
				}
				
			}
		}
		System.out.println("\n\n");
	}
	
	private SpielEvent[] insertEvent(SpielEvent[] allother, SpielEvent toBeInserted) {
		boolean exists = false;
		
		for (SpielEvent event : allother) {
			if (event.equals(toBeInserted)) {
				exists = true;
			}
		}
		
		if (exists) {
			return allother;
		}
		
		SpielEvent[] allEvents = new SpielEvent[allother.length + 1];
		
		int indexOld = 0;
		int indexNew = 0;
		boolean newonewasused = false;
		for (indexNew = 0; indexNew < allEvents.length && indexOld < allother.length; indexNew++) {
			if (newonewasused || allother[indexOld].getMinute() < toBeInserted.getMinute()) {
				allEvents[indexNew] = allother[indexOld];
				indexOld++;
			} else {
				allEvents[indexNew] = toBeInserted;
				newonewasused = true;
			}
		}
		if (indexOld == indexNew) {
			allEvents[indexNew] = toBeInserted;
		}
		
		return allEvents;
	}
	
	private boolean checkIfContains(SpielEvent[] all, String toBeChecked) {
		
		for (int i = 0; i < all.length; i++) {
			if (all[i].getEventDescription().equals(toBeChecked)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void testCheckIfContains() {
		String[] strings = {"abc", "cde", "efg", "ghi"};
		for (int i = 0; i < strings.length; i++) {
			System.out.println("" + checkIfContains(new SpielEvent[] {new SpielEvent(23, "abcde;fghi")}, strings[i]));
		}
		
		
		// Test Insert
		SpielEvent yellowcard = new SpielEvent(23, "Y");
		SpielEvent redcard = new SpielEvent(89, "R");
		SpielEvent goal1 = new SpielEvent(12, "G");
		SpielEvent goal2 = new SpielEvent(67, "G");
		
		SpielEvent[] spielEvents = new SpielEvent[] {yellowcard};
		testAusgabeSpielEventArray(spielEvents);
		
		spielEvents = insertEvent(spielEvents, redcard);
		testAusgabeSpielEventArray(spielEvents);
		
		spielEvents = insertEvent(spielEvents, goal1);
		testAusgabeSpielEventArray(spielEvents);
		
		spielEvents = insertEvent(spielEvents, goal2);
		testAusgabeSpielEventArray(spielEvents);
		
		spielEvents = insertEvent(spielEvents, goal1);
		testAusgabeSpielEventArray(spielEvents);
		
	}
	
	private void testAusgabeSpielEventArray(SpielEvent[] events) {
		if (events == null) {
			return;
		}
		
		for (int i = 0; i < events.length; i++) {
			System.out.println((i + 1) + ":   " + events[i].toString());
		}
		System.out.println();
	}
	
	private void mannschaftLadenPressed() {
		
		JFileChooser fc = new JFileChooser("/Users/valentinschraub/Documents/workspace/Bundesliga/FIFA Weltmeisterschaft 2014/");
		fc.showOpenDialog(null);
		String filename = fc.getSelectedFile().getAbsolutePath();
		
		String[] kaderStrings;
		try {
			kaderStrings = ausDatei(filename);
		} catch (Exception e) {
			kaderStrings = new String[numberOfPlayers];
			for (int i = 0; i < kaderStrings.length; i++) {
				kaderStrings[i] = "Spieler " + (i + 1);
			}
		}
		
		
		for (int i = 0; i < playersnames.length; i++) {
			playersnames[i] = kaderStrings[i].split(";")[1];
			btnsPlayers[i].setText(playersnames[i]);
			try{
				zusaetze[i] = kaderStrings[i].split(";")[2];
			} catch (Exception e) {
				zusaetze[i] = "k. Zusätze";
			}
			System.out.println((i + 1) + ": " + zusaetze[i]);
		}
		
		aTeamWasLoaded = true;

		jBtnNeuesSpiel.setEnabled(true);
	}

	private void playerSelected(int index) {
		if (aTeamWasLoaded) {
			if (editedMatch != -1) {
				editedPlayer = index;
				setEventsEditable(true);
				setPlayersEditable(false);
				btnsPlayers[editedPlayer].setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(null, "Es wurde noch keine Matchdatenerfassung gestartet.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "No team loaded!");
		}
	}
	
	private void gelbeKartePressed() {
		if (aTeamWasLoaded && editedPlayer != -1) {
			String eingabe = JOptionPane.showInputDialog("Geben Sie bitte ein, in welcher Minute " + playersnames[editedPlayer] + " die Gelbe Karte gesehen hat.");
			int minute = -1;
			try {
				minute = Integer.parseInt(eingabe);
			} catch (Exception e) {
				
			}
			
			System.out.println(playersnames[editedPlayer] + " hat in der " + minute + ". Minute eine Gelbe Karte gesehen.");
			
//			if(checkIfContains(whathappened[editedMatch][editedPlayer], minute + "Y")) {
//				JOptionPane.showMessageDialog(null, "This yellow card was already given. Are you sure there were two yellow cards in the same minute?");
//				return;
//			}
			
			SpielEvent yellowCard = new SpielEvent(minute, "Y");
			SpielEvent yellowRedCard = new SpielEvent(minute, "YR");
			
			if (!checkIfContains(whathappened[editedMatch][editedPlayer], "Y")) {
				whathappened[editedMatch][editedPlayer] = insertEvent(whathappened[editedMatch][editedPlayer], yellowCard);
			} else {
				whathappened[editedMatch][editedPlayer] = insertEvent(whathappened[editedMatch][editedPlayer], yellowRedCard);
			}
			
			editedPlayer = -1;
			setEventsEditable(false);
			setPlayersEditable(true);
		} else {
			JOptionPane.showMessageDialog(null, "No team loaded or no player selected!");
		}
		
	}
	
	private void roteKartePressed() {
		if (aTeamWasLoaded && editedPlayer != -1) {
			String eingabe = JOptionPane.showInputDialog("Geben Sie bitte ein, in welcher Minute " + playersnames[editedPlayer] + " die Rote Karte gesehen hat.");
			int minute = -1;
			try {
				minute = Integer.parseInt(eingabe);
			} catch (Exception e) {
				
			}
			
			System.out.println(playersnames[editedPlayer] + " hat in der " + minute + ". Minute eine Rote Karte gesehen.");
			
			SpielEvent redCard = new SpielEvent(minute, "R");
			
			whathappened[editedMatch][editedPlayer] = insertEvent(whathappened[editedMatch][editedPlayer], redCard);
			
			editedPlayer = -1;
			setEventsEditable(false);
			setPlayersEditable(true);
		} else {
			JOptionPane.showMessageDialog(null, "No team loaded or no player selected!");
		}
	}
	
	private void torPressed() {
		if (aTeamWasLoaded && editedPlayer != -1) {
			String eingabe = JOptionPane.showInputDialog("Geben Sie bitte ein, in welcher Minute " + playersnames[editedPlayer] + " das Tor geschossen hat.");
			int minute = -1;
			try {
				minute = Integer.parseInt(eingabe);
			} catch (Exception e) {
				
			}
			
			System.out.println(playersnames[editedPlayer] + " hat in der " + minute + ". Minute ein Tor geschossen.");
			
			SpielEvent goal = new SpielEvent(minute, "G");
			
			whathappened[editedMatch][editedPlayer] = insertEvent(whathappened[editedMatch][editedPlayer], goal);
			
			editedPlayer = -1;
			setEventsEditable(false);
			setPlayersEditable(true);
		} else {
			JOptionPane.showMessageDialog(null, "No team loaded or no player selected!");
		}
	}
	
}

class SpielEvent {
	
	private int minute;
	private String eventDescription;
	
	public SpielEvent(int minute, String eventDescription) {
		this.minute = minute;
		this.eventDescription = eventDescription;
	}
	
	public int getMinute() {
		return this.minute;
	}
	
	public String getEventDescription() {
		return this.eventDescription;
	}
	
	public String toString() {
		return this.minute + this.eventDescription;
	}
}
