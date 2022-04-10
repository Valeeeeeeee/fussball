package model;

import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.swing.*; 

import static util.Utilities.*;

public class Fussball extends JFrame {
	private static final long serialVersionUID = -3201913070768333811L;
	
	/**
	 * The width of the frame.
	 */
	public static final int WIDTH = 1440;
	/**
	 * The height of the frame.
	 */
	public static final int HEIGHT = 874;
	
	
	private static Fussball singleton;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\myWorkspace\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
	
	private String filePlayers;
	private ArrayList<String> playersFromFile;
	
	private String fileConfiguration;
	private ArrayList<String> configurationFromFile;
	
	private String[] koRFull = new String[] {"1. Runde", "2. Runde", "Achtelfinale", "Viertelfinale", "Halbfinale", "Spiel um Platz 3", "Finale"};
	private String[] koRShort = new String[] {"1R", "2R", "AF", "VF", "HF", "P3", "FI"};
	private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private Font fontMissingResults = new Font("Lucida Grande", 1, 24);
	public static final int numberOfMissingResults = 20;
	
	public static final int numberOfRegularSubstitutions = 3;
	
	private int numberOfLeagues;
	private int numberOfTournaments;
	
	private static HashMap<Integer, Spieler> players;
	
	private ArrayList<Liga> leagues;
	private Liga currentLeague;
	private LigaSaison currentLSeason;
	
	private ArrayList<Turnier> tournaments;
	private Turnier currentTournament;
	private TurnierSaison currentTSeason;
	private Gruppe currentGroup;
	private KORunde currentKORound;
	
	private boolean addingNewSeason;
	
	private boolean isCurrentlyALeague = false;
	private boolean isCurrentlyInQualification = false;
	private boolean isCurrentlyInGroupStage = false;
	private boolean isCurrentlyInMatchdayView = false;
	private boolean isCurrentlyInPlayoffView = false;
	private boolean isCurrentlyInOverviewMode = false;
	
	private static final int SIZEX_BTNS = 400;
	private static final int SIZEY_BTNS = 90;
	private static final int STARTX_BTNS = 315;
	private static final int STARTY_BTNS = 120;
	private static final int SIZEX_LBLS = 60;
	private static final int SIZEY_LBLS = 60;
	
	private Rectangle REC_BTNBACK = new Rectangle(10, 10, 100, 30);
	
	private Rectangle REC_CBSEASONS = new Rectangle(655, 100, 130, 25);
	private Rectangle REC_BTNNEWSEASON = new Rectangle(800, 100, 120, 25);
	private Rectangle REC_BTNMATCHDAYS = new Rectangle(520, 150, 400, 100);
	private Rectangle REC_BTNTABLE = new Rectangle(520, 270, 400, 100);
	private Rectangle REC_BTNSTATISTICS = new Rectangle(520, 390, 400, 100);
	private Rectangle REC_BTNOPTIONS = new Rectangle(520, 510, 400, 100);
	
	private Rectangle REC_EXIT = new Rectangle(20, 20, 100, 40);
	private Rectangle REC_ADDLEAG = new Rectangle(520, 60, 180, 40);
	private Rectangle REC_ADDTOUR = new Rectangle(740, 60, 180, 40);
	
	// Homescreen
	private JPanel Homescreen;
	private JButton[] jBtnsLeagues;
	private JLabel[] jLblsRunningMatchesLeagues;
	private JLabel[] jLblsCompletedMatchesLeagues;
	private JLabel[] jLblsNotScheduledMatchesLeagues;
	private JButton[] jBtnsTournaments;
	private JLabel[] jLblsRunningMatchesTournaments;
	private JLabel[] jLblsCompletedMatchesTournaments;
	private JLabel[] jLblsNotScheduledMatchesTournaments;
	private JButton jBtnAddLeague;
	private JButton jBtnAddTournament;
	private JButton jBtnExit;
	
	// Liga - Homescreen
	private JPanel LeagueHomescreen;
	private JLabel jLblCompetition;
	private JComboBox<String> jCBSeasonSelection;
	private JButton jBtnNewSeason;
	private JButton jBtnMatchdays;
	private JButton jBtnTable;
	private JButton jBtnStatistics;
	private JButton jBtnOptions;
	
	// Turnier - Homescreen
	private JPanel TournamentHomescreen;
	private JButton jBtnQualification;
	private JButton jBtnGroupStage;
	private JButton jBtnKORounds;
	
	private JPanel QualificationHomescreen;
	private JButton[] qualificationButtons;
	
	private JPanel GroupStageHomescreen;
	private JButton[] groupStageButtons;
	
	private JPanel KORoundsHomescreen;
	private JButton[] KORoundsButtons;
	
	
	private JButton jBtnBack;
	
	private Spieltag aktuellerSpieltag;
	private Tabelle aktuelleTabelle;
	private LigaStatistik aktuelleStatistik;
	private Uebersicht uebersicht;
//	public Uebersicht aktuelleUebersicht;
	
	// Optionen-Panel
	public JPanel jPnlOptions;
	private JButton jBtnCorrectNames;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Fussball inst = new Fussball();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public Fussball() {
		super();

		singleton = this;
		
		checkOS();
		
		loadPlayers();
		loadConfiguration();
		initGUI();
		
		testSomethingBeforeIntroducingItIntoTheRealCode();
		
		log("\nProgramm erfolgreich gestartet ...\n\n");
	}
	
	private void testSomethingBeforeIntroducingItIntoTheRealCode() {
		// TODO do some testing
		
		
		log("\n\n");
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			getContentPane().setLayout(null);
			
			
			buildHomescreen();
			buildLeagueHomescreen();
			buildTournamentHomescreen();
			buildOptionenPanel();
			
			
			{
				jBtnBack = new JButton();
				getContentPane().add(jBtnBack);
				jBtnBack.setBounds(REC_BTNBACK);
				jBtnBack.setText("zurück");
				jBtnBack.setVisible(false);
				jBtnBack.setFocusable(false);
				jBtnBack.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBtnBackActionPerformed();
					}
				});
			}
			{
				uebersicht = new Uebersicht(currentTSeason);
				getContentPane().add(uebersicht);
				uebersicht.setLocation(145, 5);
				uebersicht.setVisible(false);
			}
			
			pack();
			setTitle("Fußball");
			setSize(WIDTH, HEIGHT);
			setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Fussball getInstance() {
		return singleton;
	}
	
	private void buildHomescreen() {
		{
			Homescreen = new JPanel();
			getContentPane().add(Homescreen);
			Homescreen.setLayout(null);
			Homescreen.setBounds(0, 0, WIDTH, HEIGHT);
			Homescreen.setBackground(colorHomescreen);
		}
		
		buildLeaguesButtons();
		
		{ 
			jBtnAddLeague = new JButton(); 
			Homescreen.add(jBtnAddLeague);
			jBtnAddLeague.setBounds(REC_ADDLEAG);
			jBtnAddLeague.setText("Neue Liga");
			jBtnAddLeague.setFocusable(false);
			jBtnAddLeague.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnAddLeagueActionPerformed();
				}
			});
		}
		{ 
			jBtnAddTournament = new JButton(); 
			Homescreen.add(jBtnAddTournament);
			jBtnAddTournament.setBounds(REC_ADDTOUR);
			jBtnAddTournament.setText("Neues Turnier");
			jBtnAddTournament.setFocusable(false);
			jBtnAddTournament.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnAddTournamentActionPerformed();
				}
			});
		}
		{ 
			jBtnExit = new JButton(); 
			Homescreen.add(jBtnExit);
			jBtnExit.setBounds(REC_EXIT);
			jBtnExit.setText("Beenden");
			jBtnExit.setFocusable(false);
			jBtnExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnExitActionPerformed();
				}
			});
		}
	}
	
	private void buildLeaguesButtons() {
		if (jBtnsLeagues != null) {
			for (int i = 0; i < jBtnsLeagues.length; i++)	jBtnsLeagues[i].setVisible(false);
		}
		
		jBtnsLeagues = new JButton[numberOfLeagues];
		jLblsRunningMatchesLeagues = new JLabel[numberOfLeagues];
		jLblsCompletedMatchesLeagues = new JLabel[numberOfLeagues];
		jLblsNotScheduledMatchesLeagues = new JLabel[numberOfLeagues];
		jBtnsTournaments = new JButton[numberOfTournaments];
		jLblsRunningMatchesTournaments = new JLabel[numberOfTournaments];
		jLblsCompletedMatchesTournaments = new JLabel[numberOfTournaments];
		jLblsNotScheduledMatchesTournaments = new JLabel[numberOfTournaments];
		
		for (int i = 0; i < numberOfLeagues; i++) {
			final int x = i;
			jBtnsLeagues[i] = new JButton();
			Homescreen.add(jBtnsLeagues[i]);
			jBtnsLeagues[i].setBounds(STARTX_BTNS, STARTY_BTNS + i * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
			jBtnsLeagues[i].setText(leagues.get(i).getName());
			jBtnsLeagues[i].setFocusable(false);
			jBtnsLeagues[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnsLeaguesPressed(x);
				}
			});
			
			jLblsNotScheduledMatchesLeagues[i] = new JLabel();
			Homescreen.add(jLblsNotScheduledMatchesLeagues[i]);
			jLblsNotScheduledMatchesLeagues[i].setBounds(STARTX_BTNS - 3 * (SIZEX_LBLS + 10), STARTY_BTNS + 15 + i * (SIZEY_BTNS + 10), SIZEX_LBLS, SIZEY_LBLS);
			alignCenter(jLblsNotScheduledMatchesLeagues[i]);
			jLblsNotScheduledMatchesLeagues[i].setFont(fontMissingResults);
			jLblsNotScheduledMatchesLeagues[i].setFocusable(false);
			jLblsNotScheduledMatchesLeagues[i].setBackground(colorCategory6);
			jLblsNotScheduledMatchesLeagues[i].setOpaque(true);
			
			jLblsCompletedMatchesLeagues[i] = new JLabel();
			Homescreen.add(jLblsCompletedMatchesLeagues[i]);
			jLblsCompletedMatchesLeagues[i].setBounds(STARTX_BTNS - 2 * (SIZEX_LBLS + 10), STARTY_BTNS + 15 + i * (SIZEY_BTNS + 10), SIZEX_LBLS, SIZEY_LBLS);
			alignCenter(jLblsCompletedMatchesLeagues[i]);
			jLblsCompletedMatchesLeagues[i].setFont(fontMissingResults);
			jLblsCompletedMatchesLeagues[i].setFocusable(false);
			jLblsCompletedMatchesLeagues[i].setBackground(colorCategory5);
			jLblsCompletedMatchesLeagues[i].setOpaque(true);
			
			jLblsRunningMatchesLeagues[i] = new JLabel();
			Homescreen.add(jLblsRunningMatchesLeagues[i]);
			jLblsRunningMatchesLeagues[i].setBounds(STARTX_BTNS - (SIZEX_LBLS + 10), STARTY_BTNS + 15 + i * (SIZEY_BTNS + 10), SIZEX_LBLS, SIZEY_LBLS);
			alignCenter(jLblsRunningMatchesLeagues[i]);
			jLblsRunningMatchesLeagues[i].setFont(fontMissingResults);
			jLblsRunningMatchesLeagues[i].setFocusable(false);
			jLblsRunningMatchesLeagues[i].setBackground(colorCategory2);
			jLblsRunningMatchesLeagues[i].setOpaque(true);
		}
		for (int i = 0; i < numberOfTournaments; i++) {
			final int x = i;
			jBtnsTournaments[i] = new JButton();
			Homescreen.add(jBtnsTournaments[i]);
			jBtnsTournaments[i].setBounds(STARTX_BTNS + SIZEX_BTNS + 10, STARTY_BTNS + i * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
			jBtnsTournaments[i].setText(tournaments.get(i).getName());
			jBtnsTournaments[i].setFocusable(false);
			jBtnsTournaments[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnsTournamentsPressed(x);
				}
			});
			
			jLblsNotScheduledMatchesTournaments[i] = new JLabel();
			Homescreen.add(jLblsNotScheduledMatchesTournaments[i]);
			jLblsNotScheduledMatchesTournaments[i].setBounds(STARTX_BTNS + 2 * (SIZEX_BTNS + 10) + 2 * (SIZEX_LBLS + 10), STARTY_BTNS + 15 + i * (SIZEY_BTNS + 10), SIZEX_LBLS, SIZEY_LBLS);
			alignCenter(jLblsNotScheduledMatchesTournaments[i]);
			jLblsNotScheduledMatchesTournaments[i].setFont(fontMissingResults);
			jLblsNotScheduledMatchesTournaments[i].setFocusable(false);
			jLblsNotScheduledMatchesTournaments[i].setBackground(colorCategory6);
			jLblsNotScheduledMatchesTournaments[i].setOpaque(true);
			
			jLblsCompletedMatchesTournaments[i] = new JLabel();
			Homescreen.add(jLblsCompletedMatchesTournaments[i]);
			jLblsCompletedMatchesTournaments[i].setBounds(STARTX_BTNS + 2 * (SIZEX_BTNS + 10) + (SIZEX_LBLS + 10), STARTY_BTNS + 15 + i * (SIZEY_BTNS + 10), SIZEX_LBLS, SIZEY_LBLS);
			alignCenter(jLblsCompletedMatchesTournaments[i]);
			jLblsCompletedMatchesTournaments[i].setFont(fontMissingResults);
			jLblsCompletedMatchesTournaments[i].setFocusable(false);
			jLblsCompletedMatchesTournaments[i].setBackground(colorCategory5);
			jLblsCompletedMatchesTournaments[i].setOpaque(true);
			
			jLblsRunningMatchesTournaments[i] = new JLabel();
			Homescreen.add(jLblsRunningMatchesTournaments[i]);
			jLblsRunningMatchesTournaments[i].setBounds(STARTX_BTNS + 2 * (SIZEX_BTNS + 10), STARTY_BTNS + 15 + i * (SIZEY_BTNS + 10), SIZEX_LBLS, SIZEY_LBLS);
			alignCenter(jLblsRunningMatchesTournaments[i]);
			jLblsRunningMatchesTournaments[i].setFont(fontMissingResults);
			jLblsRunningMatchesTournaments[i].setFocusable(false);
			jLblsRunningMatchesTournaments[i].setBackground(colorCategory2);
			jLblsRunningMatchesTournaments[i].setOpaque(true);
		}
		refreshRunningAndCompletedMatches();
	}
	
	private void buildLeagueHomescreen() {
		{
			LeagueHomescreen = new JPanel();
			getContentPane().add(LeagueHomescreen);
			LeagueHomescreen.setLayout(null);
			LeagueHomescreen.setBounds(0, 0, WIDTH, HEIGHT);
			LeagueHomescreen.setBackground(colorHomescreen);
			LeagueHomescreen.setVisible(false);
		}
		{
			jLblCompetition = new JLabel();
			LeagueHomescreen.add(jLblCompetition);
			jLblCompetition.setBounds(520, 100, 110, 25);
			alignCenter(jLblCompetition);
		}
		{
			jCBSeasonSelection = new JComboBox<>();
			jCBSeasonSelection.setBounds(REC_CBSEASONS);
			jCBSeasonSelection.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					jCBSeasonSelectionItemStateChanged(evt);
				}
			});
		}
		{
			jBtnNewSeason = new JButton();
			LeagueHomescreen.add(jBtnNewSeason);
			jBtnNewSeason.setBounds(REC_BTNNEWSEASON);
			jBtnNewSeason.setText("Neue Saison");
			jBtnNewSeason.setFocusable(false);
			jBtnNewSeason.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnNewSeasonActionPerformed();
				}
			});
		}
		{
			jBtnMatchdays = new JButton();
			LeagueHomescreen.add(jBtnMatchdays);
			jBtnMatchdays.setBounds(REC_BTNMATCHDAYS);
			jBtnMatchdays.setText("Spieltage");
			jBtnMatchdays.setFocusable(false);
			jBtnMatchdays.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnMatchdaysActionPerformed();
				}
			});
		}
		{ 
			jBtnTable = new JButton();
			LeagueHomescreen.add(jBtnTable);
			jBtnTable.setBounds(REC_BTNTABLE);
			jBtnTable.setText("Tabelle");
			jBtnTable.setFocusable(false);
			jBtnTable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnTableActionPerformed();
				}
			});
		}
		{ 
			jBtnStatistics = new JButton();
			LeagueHomescreen.add(jBtnStatistics);
			jBtnStatistics.setBounds(REC_BTNSTATISTICS);
			jBtnStatistics.setText("Statistik");
			jBtnStatistics.setFocusable(false);
			jBtnStatistics.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnStatisticsActionPerformed();
				}
			});
		}
		{
			jBtnOptions = new JButton();
			LeagueHomescreen.add(jBtnOptions);
			jBtnOptions.setBounds(REC_BTNOPTIONS);
			jBtnOptions.setText("Optionen");
			jBtnOptions.setFocusable(false);
			jBtnOptions.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnOptionsActionPerformed();
				}
			});
		}
	}
	
	private void buildTournamentHomescreen() {
		{
			TournamentHomescreen = new JPanel();
			getContentPane().add(TournamentHomescreen);
			TournamentHomescreen.setLayout(null);
			TournamentHomescreen.setBounds(0, 0, WIDTH, HEIGHT);
			TournamentHomescreen.setBackground(colorHomescreen);
			TournamentHomescreen.setVisible(false);
		}
		{ 
			jBtnQualification = new JButton();
			TournamentHomescreen.add(jBtnQualification);
			jBtnQualification.setBounds(520, 200, SIZEX_BTNS, SIZEY_BTNS);
			jBtnQualification.setText("Qualifikation");
			jBtnQualification.setFocusable(false);
			jBtnQualification.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnQualificationActionPerformed();
				}
			});
		}
		{ 
			jBtnGroupStage = new JButton();
			TournamentHomescreen.add(jBtnGroupStage);
			jBtnGroupStage.setBounds(520, 350, SIZEX_BTNS, SIZEY_BTNS);
			jBtnGroupStage.setText("Gruppenphase");
			jBtnGroupStage.setFocusable(false);
			jBtnGroupStage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnGroupStageActionPerformed();
				}
			});
		}
		{ 
			jBtnKORounds = new JButton();
			TournamentHomescreen.add(jBtnKORounds);
			jBtnKORounds.setBounds(520, 500, SIZEX_BTNS, SIZEY_BTNS);
			jBtnKORounds.setText("K.O.-Runde");
			jBtnKORounds.setFocusable(false);
			jBtnKORounds.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jBtnKORoundsActionPerformed();
				}
			});
		}
		{
			QualificationHomescreen = new JPanel();
			getContentPane().add(QualificationHomescreen);
			QualificationHomescreen.setLayout(null);
			QualificationHomescreen.setBounds(0, 0, WIDTH, HEIGHT);
			QualificationHomescreen.setBackground(colorHomescreen);
			QualificationHomescreen.setVisible(false);
		}
		{
			GroupStageHomescreen = new JPanel();
			getContentPane().add(GroupStageHomescreen);
			GroupStageHomescreen.setLayout(null);
			GroupStageHomescreen.setBounds(0, 0, WIDTH, HEIGHT);
			GroupStageHomescreen.setBackground(colorHomescreen);
			GroupStageHomescreen.setVisible(false);
		}
		{
			KORoundsHomescreen = new JPanel();
			getContentPane().add(KORoundsHomescreen);
			KORoundsHomescreen.setLayout(null);
			KORoundsHomescreen.setBounds(0, 0, WIDTH, HEIGHT);
			KORoundsHomescreen.setBackground(colorHomescreen);
			KORoundsHomescreen.setVisible(false);
		}
	}

	private void buildOptionenPanel() {
		{
			jPnlOptions = new JPanel();
			getContentPane().add(jPnlOptions);
			jPnlOptions.setLayout(null);
			jPnlOptions.setBounds(470, 0, 500, HEIGHT);
			jPnlOptions.setBackground(new Color(255, 255, 0));
			jPnlOptions.setVisible(false);
			
		}
		{
			jBtnCorrectNames = new JButton();
			jPnlOptions.add(jBtnCorrectNames);
			jBtnCorrectNames.setBounds(20, 80, 210, 30);
			jBtnCorrectNames.setText("Namen verbessern");
			jBtnCorrectNames.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					teamsVerbessern();
				}
			});
		}
	}
	
	public char[] getAlphabet() {
		return alphabet;
	}
	
	public String[] getKoRFull() {
		return koRFull;
	}

	public String[] getKoRShort() {
		return koRShort;
	}

	public String getWorkspace() {
		return workspace;
	}
	
	public boolean addingNewSeason() {
		return addingNewSeason;
	}
	
	private void refreshRunningAndCompletedMatches() {
		for (int i = 0; i < numberOfLeagues; i++) {
			int[] missing = leagues.get(i).checkMissingResults();
			jLblsNotScheduledMatchesLeagues[i].setText(missing[0] + (missing[0] == numberOfMissingResults ? "+" : ""));
			jLblsNotScheduledMatchesLeagues[i].setToolTipText(missing[0] + (missing[0] == numberOfMissingResults ? " or more" : "") + " not scheduled match" + (missing[0] == 1 ? "" : "es"));
			jLblsNotScheduledMatchesLeagues[i].setVisible(missing[0] != 0);
			jLblsCompletedMatchesLeagues[i].setText(missing[1] + (missing[0] + missing[1] == numberOfMissingResults ? "+" : ""));
			jLblsCompletedMatchesLeagues[i].setToolTipText(missing[1] + (missing[0] + missing[1] == numberOfMissingResults ? " or more" : "") + " finished match" + (missing[1] == 1 ? "" : "es"));
			jLblsCompletedMatchesLeagues[i].setVisible(missing[1] != 0);
			jLblsRunningMatchesLeagues[i].setText(missing[2] + (missing[0] + missing[1] + missing[2] == numberOfMissingResults ? "+" : ""));
			jLblsRunningMatchesLeagues[i].setToolTipText(missing[2] + (missing[0] + missing[1] + missing[2] == numberOfMissingResults ? " or more" : "") + " running match" + (missing[2] == 1 ? "" : "es"));
			jLblsRunningMatchesLeagues[i].setVisible(missing[2] != 0);
		}
		for (int i = 0; i < numberOfTournaments; i++) {
			int[] missing = tournaments.get(i).checkMissingResults();
			jLblsNotScheduledMatchesTournaments[i].setText(missing[0] + (missing[0] == numberOfMissingResults ? "+" : ""));
			jLblsNotScheduledMatchesTournaments[i].setToolTipText(missing[0] + (missing[0] == numberOfMissingResults ? " or more" : "") + " not scheduled match" + (missing[0] == 1 ? "" : "es"));
			jLblsNotScheduledMatchesTournaments[i].setVisible(missing[0] != 0);
			jLblsCompletedMatchesTournaments[i].setText(missing[1] + (missing[0] + missing[1] == numberOfMissingResults ? "+" : ""));
			jLblsCompletedMatchesTournaments[i].setToolTipText(missing[1] + (missing[0] + missing[1] == numberOfMissingResults ? " or more" : "") + " finished match" + (missing[1] == 1 ? "" : "es"));
			jLblsCompletedMatchesTournaments[i].setVisible(missing[1] != 0);
			jLblsRunningMatchesTournaments[i].setText(missing[2] + (missing[0] + missing[1] + missing[2] == numberOfMissingResults ? "+" : ""));
			jLblsRunningMatchesTournaments[i].setToolTipText(missing[2] + (missing[0] + missing[1] + missing[2] == numberOfMissingResults ? " or more" : "") + " running match" + (missing[2] == 1 ? "" : "es"));
			jLblsRunningMatchesTournaments[i].setVisible(missing[2] != 0);
		}
	}
	
	public void jBtnsLeaguesPressed(int index) {
		Homescreen.setVisible(false);
		jBtnBack.setVisible(true);
		
		// The pressed button leads to a league
		isCurrentlyALeague = true;
		LeagueHomescreen.setVisible(true);
		LeagueHomescreen.add(jBtnBack);
		
		currentLeague = leagues.get(index);
		jLblCompetition.setText(currentLeague.getName());
		
		// befüllt die ComboBox mit den verfügbaren Saisons
		jCBSeasonSelection.setModel(new DefaultComboBoxModel<>(currentLeague.getAllSeasons()));
		jCBSeasonSelection.setSelectedIndex(jCBSeasonSelection.getModel().getSize() - 1);
		LeagueHomescreen.add(jCBSeasonSelection);
		
		if (jCBSeasonSelection.getModel().getSize() == 1) {
			// dann passiert nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
			currentLeague.load(0);
			currentLSeason = currentLeague.getCurrentSeason();
			loadLeagueSpecificThings();
		}
	}
	
	public void jBtnsTournamentsPressed(int index) {
		Homescreen.setVisible(false);
		jBtnBack.setVisible(true);
		
		// The pressed button leads to a tournament
		isCurrentlyALeague = false;
		TournamentHomescreen.setVisible(true);
		TournamentHomescreen.add(jBtnBack);
		
		currentTournament = tournaments.get(index);
		jLblCompetition.setText(currentTournament.getName());
		
		// befüllt die ComboBox mit den verfügbaren Saisons
		jCBSeasonSelection.setModel(new DefaultComboBoxModel<>(currentTournament.getAllSeasons()));
		jCBSeasonSelection.setSelectedIndex(jCBSeasonSelection.getModel().getSize() - 1);
		TournamentHomescreen.add(jCBSeasonSelection);
		
		if (jCBSeasonSelection.getModel().getSize() == 1) {
			// dann passierte oben nichts, weil von 0 zu 0 kein ItemStateChange vorliegt
			currentTournament.load(0);
			currentTSeason = currentTournament.getCurrentSeason();
			loadTournamentSpecificThings();
		}
		
		if (!currentTSeason.hasQualification() && !currentTSeason.hasGroupStage()) {
			jBtnKORoundsActionPerformed();
			KORoundsHomescreen.add(jCBSeasonSelection);
		} else if (!currentTSeason.hasQualification() && !currentTSeason.hasKOStage()) {
			jBtnGroupStageActionPerformed();
			GroupStageHomescreen.add(jCBSeasonSelection);
		}
	}
	
	public void jBtnQualificationActionPerformed() {
		isCurrentlyInQualification = true;
		isCurrentlyInGroupStage = false;
		
		TournamentHomescreen.setVisible(false);
		QualificationHomescreen.setVisible(true);
		QualificationHomescreen.add(jBtnBack);
	}
	
	public void jBtnGroupStageActionPerformed() {
		isCurrentlyInQualification = false;
		isCurrentlyInGroupStage = true;
		
		TournamentHomescreen.setVisible(false);
		GroupStageHomescreen.setVisible(true);
		GroupStageHomescreen.add(jBtnBack);
	}
	
	public void jBtnKORoundsActionPerformed() {
		isCurrentlyInQualification = false;
		isCurrentlyInGroupStage = false;
		
		TournamentHomescreen.setVisible(false);
		KORoundsHomescreen.setVisible(true);
		KORoundsHomescreen.add(jBtnBack);
	}
	
	public void jBtnAllGroupsPressed() {
		isCurrentlyInOverviewMode = true;
		if (isCurrentlyInQualification) {
			QualificationHomescreen.setVisible(false);
			aktuellerSpieltag = currentTSeason.getQSpieltag();
		} else {
			GroupStageHomescreen.setVisible(false);
			aktuellerSpieltag = currentTSeason.getSpieltag();
		}
		
		isCurrentlyInMatchdayView = false;
		getContentPane().add(aktuellerSpieltag);
		aktuellerSpieltag.add(jBtnBack);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.showMatchday();
	}
	
	public void jBtnsGroupsPressed(int index) {
		if (isCurrentlyInQualification) {
			currentGroup = currentTSeason.getQGroups()[index];
			QualificationHomescreen.setVisible(false);
		} else {
			currentGroup = currentTSeason.getGroups()[index];
			GroupStageHomescreen.setVisible(false);
		}
		
		jLblCompetition.setText(currentGroup.getDescription());
		LeagueHomescreen.setVisible(true);
		LeagueHomescreen.add(jBtnBack);
		
		aktuellerSpieltag = currentGroup.getSpieltag();
		getContentPane().add(aktuellerSpieltag);
		aktuelleTabelle = currentGroup.getTabelle();
		getContentPane().add(aktuelleTabelle);
	}
	
	public void jBtnsKORoundsPressed(int index) {
		if (isCurrentlyInQualification) {
			currentKORound = currentTSeason.getQKORounds()[index];
			QualificationHomescreen.setVisible(false);
		} else {
			currentKORound = currentTSeason.getKORounds()[index];
			KORoundsHomescreen.setVisible(false);
		}
		
		isCurrentlyInMatchdayView = true;
		aktuellerSpieltag = currentKORound.getSpieltag();
		getContentPane().add(aktuellerSpieltag);
		aktuellerSpieltag.add(jBtnBack);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.showMatchday();
	}
	
	private void jCBSeasonSelectionItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			int index = jCBSeasonSelection.getSelectedIndex();
			if (isCurrentlyALeague) {
				currentLeague.save();
				currentLeague.load(index);
				currentLSeason = currentLeague.getCurrentSeason();
				loadLeagueSpecificThings();
			} else {
				currentTournament.save();
				currentTournament.load(index);
				currentTSeason = currentTournament.getCurrentSeason();
				loadTournamentSpecificThings();
			}
		}
	}
	
	private void loadLeagueSpecificThings() {
		aktuellerSpieltag = currentLSeason.getSpieltag();
		getContentPane().add(aktuellerSpieltag);
		if (currentLSeason.hasPlayoffs())	getContentPane().add(currentLSeason.getPlayoffSpieltag());
		aktuelleTabelle = currentLSeason.getTabelle();
		getContentPane().add(aktuelleTabelle);
		aktuelleStatistik = currentLSeason.getStatistik();
		getContentPane().add(aktuelleStatistik);
		setTitle(currentLSeason.getDescription());
	}
	
	private void loadTournamentSpecificThings() {
		// falls sie bereits existieren entfernen (wird benötigt, falls ein Turnier komplett neugestartet wird)
		// (im Gegensatz zur Liga müssen die Buttons jedes Mal neu geladen werden, da jedes Turnier unterschiedlich viele Gruppen/KO-Phasen hat)
		setTitle(currentTSeason.getDescription());
		if (currentTSeason.hasQualification()) {
			jBtnQualification.setVisible(true);
			try {
				for (int i = 0; i < qualificationButtons.length; i++) {
					QualificationHomescreen.remove(qualificationButtons[i]);
				}
			} catch(NullPointerException npe) {}
			
			int numberOfButtons = currentTSeason.hasQGroupStage() ? currentTSeason.getNumberOfQGroups() + 1 : 0;
			numberOfButtons += currentTSeason.getNumberOfQKORounds();
			qualificationButtons = new JButton[numberOfButtons];
			int ctr;
			for (ctr = 0; ctr < currentTSeason.getNumberOfQGroups(); ctr++) {
				final int x = ctr;
				qualificationButtons[ctr] = new JButton();
				QualificationHomescreen.add(qualificationButtons[ctr]);
				qualificationButtons[ctr].setBounds(295 + (ctr % 2) * (SIZEX_BTNS + 50), 150 + (ctr / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				qualificationButtons[ctr].setText(currentTSeason.getQGroups()[ctr].getDescription());
				qualificationButtons[ctr].setFocusable(false);
				qualificationButtons[ctr].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnsGroupsPressed(x);
					}
				});
			}
			if (currentTSeason.getNumberOfQGroups() > 0) {
				qualificationButtons[ctr] = new JButton();
				QualificationHomescreen.add(qualificationButtons[ctr]);
				qualificationButtons[ctr].setBounds(295 + (ctr % 2) * (SIZEX_BTNS + 50), 150 + (ctr / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				qualificationButtons[ctr].setText("Alle Gruppen");
				qualificationButtons[ctr].setFocusable(false);
				qualificationButtons[ctr].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnAllGroupsPressed();
					}
				});
				ctr++;
			}
			for (int i = 0; i < currentTSeason.getNumberOfQKORounds(); i++) {
				final int x = i, position = i + ctr;
				qualificationButtons[position] = new JButton();
				QualificationHomescreen.add(qualificationButtons[position]);
				qualificationButtons[position].setBounds(295 + (position % 2) * (SIZEX_BTNS + 50), 150 + position / 2 * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				qualificationButtons[position].setText(currentTSeason.getQKORounds()[x].getDescription());
				qualificationButtons[position].setFocusable(false);
				qualificationButtons[position].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnsKORoundsPressed(x);
					}
				});
			}
		} else {
			jBtnQualification.setVisible(false);
		}
		if (currentTSeason.hasGroupStage()) {
			jBtnGroupStage.setVisible(true);
			try {
				for (int i = 0; i < groupStageButtons.length; i++) {
					GroupStageHomescreen.remove(groupStageButtons[i]);
				}
			} catch(NullPointerException npe) {}
			
			groupStageButtons = new JButton[currentTSeason.getNumberOfGroups() + 1];
			for (int i = 0; i < groupStageButtons.length - 1; i++) {
				final int x = i;
				groupStageButtons[i] = new JButton();
				GroupStageHomescreen.add(groupStageButtons[i]);
				groupStageButtons[i].setBounds(295 + (i % 2) * (SIZEX_BTNS + 50), 150 + (i / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				groupStageButtons[i].setText(currentTSeason.getGroups()[i].getDescription());
				groupStageButtons[i].setFocusable(false);
				groupStageButtons[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnsGroupsPressed(x);
					}
				});
			}
			final int lastindex = groupStageButtons.length - 1;
			{
				groupStageButtons[lastindex] = new JButton();
				GroupStageHomescreen.add(groupStageButtons[lastindex]);
				groupStageButtons[lastindex].setBounds(295 + (lastindex % 2) * (SIZEX_BTNS + 50), 150 + (lastindex / 2) * (SIZEY_BTNS + 10), SIZEX_BTNS, SIZEY_BTNS);
				groupStageButtons[lastindex].setText("Alle Gruppen");
				groupStageButtons[lastindex].setFocusable(false);
				groupStageButtons[lastindex].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnAllGroupsPressed();
					}
				});
			}
		} else {
			jBtnGroupStage.setVisible(false);
		}
		if (currentTSeason.hasKOStage()) {
			jBtnKORounds.setVisible(true);
			try {
				for (int i = 0; i < KORoundsButtons.length; i++) {
					KORoundsHomescreen.remove(KORoundsButtons[i]);
				}
			} catch(NullPointerException npe) {}
			
			KORoundsButtons = new JButton[currentTSeason.getNumberOfKORounds()];
			for (int i = 0; i < KORoundsButtons.length; i++) {
				final int x = i;
				KORoundsButtons[i] = new JButton();
				KORoundsHomescreen.add(KORoundsButtons[i]);
				KORoundsButtons[i].setBounds(520, 150 + i * (SIZEY_BTNS + 15), SIZEX_BTNS, SIZEY_BTNS);
				KORoundsButtons[i].setText(currentTSeason.getKORounds()[i].getDescription());
				KORoundsButtons[i].setFocusable(false);
				KORoundsButtons[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jBtnsKORoundsPressed(x);
					}
				});
			}
		} else {
			jBtnKORounds.setVisible(false);
		}
	}
	
	// Action- und ItemListener
	private void jBtnNewSeasonActionPerformed() {
		addingNewSeason = true;
		
		NeueLigaSaisonDialog nlsd = new NeueLigaSaisonDialog();
		nlsd.setLocationRelativeTo(null);
		nlsd.setVisible(true);
		nlsd.setConfigurationFromPreviousSeason(currentLSeason);
		
		LeagueHomescreen.setVisible(false);
	}
	
	public void jBtnNewLeagueSeasonDoneActionPerformed(String toString, ArrayList<String> teamsNewSeasonOrder, String dKOTRep) {
		addingNewSeason = false;
		
		if (currentLeague.addSeason(toString, teamsNewSeasonOrder, dKOTRep)) {
			// befüllt die ComboBox mit den verfügbaren Saisons
			jCBSeasonSelection.setModel(new DefaultComboBoxModel<>(currentLeague.getAllSeasons()));
			jCBSeasonSelection.setSelectedIndex(jCBSeasonSelection.getModel().getSize() - 1);
			
			LeagueHomescreen.setVisible(true);
		}
	}
	
	public void jBtnMatchdaysActionPerformed() {
		if (isCurrentlyALeague) {
			// der Button wurde in einer Liga aufgerufen
			if (currentLSeason.getNumberOfTeams() > 1) {
				isCurrentlyInMatchdayView = true;
				LeagueHomescreen.setVisible(false);
				aktuellerSpieltag.add(jBtnBack);
				aktuellerSpieltag.setVisible(true);
				aktuellerSpieltag.showMatchday();
			} else {
				message("Es wurden noch keine Mannschaften angelegt. Davor kann kein Spieltag angezeigt werden.");
			}
		} else {
			if(currentGroup.getNumberOfTeams() > 1) {
				isCurrentlyInMatchdayView = true;
				LeagueHomescreen.setVisible(false);
				aktuellerSpieltag.add(jBtnBack);
				aktuellerSpieltag.setVisible(true);
				aktuellerSpieltag.showMatchday();
			} else {
				message("Es wurden noch keine Mannschaften angelegt. Davor kann kein Spieltag angezeigt werden.");
			}
		}
	}
	
	public void jBtnTableActionPerformed() {
		if (isCurrentlyALeague) {
			if (currentLSeason.getNumberOfTeams() <= 0) {
				message("Es wurden noch keine Mannschaften angelegt. Davor kann keine Tabelle angezeigt werden.");
				return;
			}
		} else {
			// der Button wurde in einer Gruppe aufgerufen
			if (currentGroup.getNumberOfTeams() <= 0) {
				message("Es wurden noch keine Mannschaften angelegt. Davor kann keine Tabelle angezeigt werden.");
				return;
			}
		}
		aktuelleTabelle.refresh();
		isCurrentlyInMatchdayView = false;
		LeagueHomescreen.setVisible(false);
		aktuelleTabelle.add(jBtnBack);
		aktuelleTabelle.setVisible(true);
	}
	
	public void jBtnStatisticsActionPerformed() {
		if (!isCurrentlyALeague) {
			message("Statistiken für Gruppen werden aktuell nicht unterstützt.");
			return;
		}
		aktuelleStatistik.refresh();
		LeagueHomescreen.setVisible(false);
		aktuelleStatistik.add(jBtnBack);
		aktuelleStatistik.setVisible(true);
	}
	
	public void jBtnOptionsActionPerformed() {
		isCurrentlyInMatchdayView = false;
		
		LeagueHomescreen.setVisible(false);
		jPnlOptions.add(jBtnBack);
		jPnlOptions.setVisible(true);
	}
	
	public static Spieler getPlayer(int id) {
		return players.get(id);
	}
	
	public static ArrayList<Spieler> getMatchingPlayers(String search) {
		ArrayList<Spieler> matchingPlayers = new ArrayList<>();
		
		Set<Integer> ids = players.keySet();
		for (Integer id : ids) {
			Spieler player = players.get(id);
			if (removeUmlaute(player.getFullNameShort().toLowerCase()).contains(removeUmlaute(search.toLowerCase())))	matchingPlayers.add(player);
		}
		
		return matchingPlayers;
	}
	
	public static boolean addNewPlayer(Spieler newPlayer) {
		if (players.containsKey(newPlayer.getID())) {
			message("Der Spieler konnte nicht hinzugefügt werden, weil die ID schon vergeben ist!");
			return false;
		}
		players.put(newPlayer.getID(), newPlayer);
		return true;
	}
	
	public static boolean idChanged(Spieler player, int oldID, int newID) {
		if (getPlayer(oldID) == null || !getPlayer(oldID).equals(player) || getPlayer(newID) != null)	return false;
		players.remove(oldID);
		players.put(newID, player);
		return true;
	}
	
	public void uebersichtAnzeigen(String teamName) {
		Mannschaft team = null;
		if (isCurrentlyALeague)	team = currentLSeason.getTeamWithName(teamName);
		else					team = currentTSeason.getTeamWithName(teamName);
		if (team == null)	return;
		
		aktuellerSpieltag.setVisible(false);
		if (aktuelleTabelle != null)	aktuelleTabelle.setVisible(false);
		
		uebersicht.setMannschaft(team);
		uebersicht.setVisible(true);
	}
	
	public void showMatchday(int matchday) {
		uebersicht.setVisible(false);
		aktuellerSpieltag.add(jBtnBack);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.showMatchday();
		aktuellerSpieltag.showMatchday(matchday);
		isCurrentlyInMatchdayView = true;
	}
	
	public void switchToMainSeason(int matchday) {
		if (!isCurrentlyALeague)	return;
		aktuellerSpieltag.setVisible(false);
		aktuellerSpieltag = currentLSeason.getSpieltag();
		aktuellerSpieltag.add(jBtnBack);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.showMatchday(matchday);
		isCurrentlyInPlayoffView = false;
	}
	
	public void switchToPlayoff(int matchday) {
		if (!isCurrentlyALeague)	return;
		aktuellerSpieltag.setVisible(false);
		aktuellerSpieltag = currentLSeason.getPlayoffSpieltag();
		aktuellerSpieltag.add(jBtnBack);
		aktuellerSpieltag.setVisible(true);
		aktuellerSpieltag.showMatchday(matchday);
		isCurrentlyInPlayoffView = true;
	}
	
	public void teamsVerbessern() {
		if (isCurrentlyALeague) {
			for (int i = 0; i < currentLSeason.getNumberOfTeams(); i++) {
				String newName = JOptionPane.showInputDialog("Korrekter Name für Mannschaft \"" + currentLSeason.getTeams()[i].getName() + "\"");
				if (newName != null && !newName.isEmpty()) {
					currentLSeason.getTeams()[i].setName(newName);
				}
			}
		} else {
			for (int i = 0; i < currentGroup.getNumberOfTeams(); i++) {
				String newName = JOptionPane.showInputDialog("Korrekter Name für Mannschaft \"" + currentGroup.getTeams()[i].getName() + "\"");
				if (newName != null && !newName.isEmpty()) {
					currentGroup.getTeams()[i].setName(newName);
				}
			}
		}
	}
	
	public String getLeagueWorkspaceFromShortName(String shortName, int season) {
		int index = 0;
		for (index = 0; index < leagues.size(); index++) {
			if (leagues.get(index).getShortName().equals(shortName))		break;
		}
		if (index != leagues.size()) {
			return leagues.get(index).getWorkspace(season);
		}
		return null;
	}
	
	public String getTournamentWorkspaceFromShortName(String shortName, int season) {
		int index = 0;
		for (index = 0; index < tournaments.size(); index++) {
			if (tournaments.get(index).getShortName().equals(shortName))		break;
		}
		if (index != tournaments.size()) {
			return tournaments.get(index).getWorkspace(season);
		}
		return null;
	}
	
	private void jBtnAddLeagueActionPerformed() {
		NewLeagueDialog nld = new NewLeagueDialog();
		nld.setLocationRelativeTo(null);
		nld.setVisible(true);

		this.toFront();
		nld.toFront();
	}
	
	private void jBtnAddTournamentActionPerformed() {
//		testAddNewTournament();
		NewTournamentDialog ntd = new NewTournamentDialog();
		ntd.setLocationRelativeTo(null);
		ntd.setVisible(true);

		this.toFront();
		ntd.toFront();
	}
	
	public void addNewLeague(String name, int season, boolean isSTSS, int numberOfTeams, int spGgSG, String defKOTsRep, boolean goalD, boolean tKader, String anzahlenRep, ArrayList<String> teamsNames, String KOTsRep) {
		for (Liga league : leagues) {
			if (league.getName().equals(name)) {
				message("A league with this name already exists.");
				return;
			}
		}
		
		try {
			File leagueFile = new File(workspace + File.separator + name);
			leagueFile.mkdir();
		} catch (Exception e) {
			error("Error while creating directories: " + e.getMessage());
			message("The league could not be created because of an unexpected file error.");
			return;
		}
		
		String toString = name + ";";
		Liga newLeague = new Liga(numberOfLeagues, toString);
		leagues.add(newLeague);
		numberOfLeagues++;
		
		toString = season + ";";
		toString += isSTSS + ";";
		toString += numberOfTeams + ";";
		toString += spGgSG + ";";
		toString += defKOTsRep + ";";
		toString += goalD + ";";
		toString += tKader + ";";
		toString += anzahlenRep + ";";
		
		newLeague.addSeason(toString, teamsNames, KOTsRep);
		
		for (int i = 0; i < leagues.size(); i++) {
			leagues.get(i).save();
		}
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new league.");
	}
	
	public void testAddNewTournament() {
		String name = "UEFA Europameisterschaft";
		String shortName = "EM";
		int season = 2016;
		boolean isSTSS = true;
		Datum stDate = new Datum(7, 9, 2014);
		Datum fiDate = new Datum(10, 7, 2016);
		boolean hasQ = true;
		boolean hasGrp = true;
		boolean hasKO = true;
		boolean has3pl = false;
		ArrayList<String> qConfig = new ArrayList<>(Arrays.asList(new String[] {"20140907", "20151117", "9", "true", "1", "Play-offs;PO;true;true;0;8;0"}));
		String[][] teamsQG = new String[][] {new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5", "Mannschaft 6"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4", "Mannschaft 5"}};
		String[][] teamsQKO = new String[][] {new String[] {"GA3", "GB3", "GC3", "GD3", "GE3", "GF3", "GG3", "GH3"}};
		ArrayList<String> grpConfig = new ArrayList<>(Arrays.asList(new String[] {"6", "false"}));
		String[][] teamsGrp = new String[][] {new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}, new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}, new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"},
												new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}, new String[] {"Mannschaft 1", "Mannschaft 2", "Mannschaft 3", "Mannschaft 4"}};
		ArrayList<String> koConfig = new ArrayList<>(Arrays.asList(new String[] {"Achtelfinale;AF;true;false;0;16;0", "Viertelfinale;VF;true;false;0;8;0", 
																					"Halbfinale;HF;true;false;0;4;0", "Finale;FI;true;false;0;2;0"}));
		String[][] teamsKO = new String[][] {new String[] {"GA1", "GA2", "GB1", "GB2", "GC1", "GC2", "GD1", "GD2", "GE1", "GE2", "GF1", "GF2", "GA3", "GB3", "GC3", "GD3"}, 
								new String[] {"AF1", "AF2", "AF3", "AF4", "AF5", "AF6", "AF7", "AF8"}, new String[] {"VF1", "VF2", "VF3", "VF4"}, new String[] {"HF1", "HF2"}};
		addNewTournament(name, shortName, season, isSTSS, stDate, fiDate, hasQ, hasGrp, hasKO, has3pl, qConfig, teamsQG, teamsQKO, grpConfig, teamsGrp, koConfig, teamsKO);
	}
	
	public void addNewTournament(String name, String shortName, int season, boolean isSTSS, Datum stDate, Datum fiDate, boolean hasQ, boolean hasGrp, boolean hasKO, boolean has3pl,
									ArrayList<String> qConfig, String[][] teamsQG, String[][] teamsQKO, ArrayList<String> grpConfig, String[][] teamsGrp, ArrayList<String> koConfig, String[][] teamsKO) {
		for (Turnier tournament : tournaments) {
			if (tournament.getName().equals(name)) {
				message("A tournament with this name already exists.");
				return;
			}
		}
		
		String toString = "NAME*" + name + ";";
		toString += "SHN*" + shortName + ";";
		
		Turnier newTournament = new Turnier(numberOfTournaments, toString);
		tournaments.add(newTournament);
		numberOfTournaments++;
		
		toString = season + ";";
		toString += isSTSS + ";";
		toString += stDate.comparable() + ";";
		toString += fiDate.comparable() + ";";
		toString += hasQ + ";";
		toString += hasGrp + ";";
		toString += hasKO + ";";
		toString += has3pl + ";";
		
		newTournament.addNewSeason(toString, qConfig, teamsQG, teamsQKO, grpConfig, teamsGrp, koConfig, teamsKO);
		
		for (int i = 0; i < tournaments.size(); i++) {
			tournaments.get(i).save();
		}
		
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new tournament.");
	}
	
	public void addNewTournament(String name, String shortName, int season, Datum stDate, Datum fiDate, boolean isSTSS, boolean hasQ, boolean hasGrp, boolean hasKO, boolean grp2leg, boolean ko2leg, boolean has3pl, 
									int nOTeam, int nOGrp, int nOKO, String[][] teamsGrp, String[][] teamsKO) {
		for (Turnier tournament : tournaments) {
			if (tournament.getName().equals(name)) {
				message("A tournament with this name already exists.");
				return;
			}
		}
		
		String daten = "";
		
		// Erstellung des config-strings
		
		daten = "NAME*" + name + ";";
		daten += "SHN*" + shortName + ";";
		daten += "ISSTSS*" + isSTSS + ";";
		daten += "STDATE*" + stDate.comparable() + ";";
		daten += "FIDATE*" + fiDate.comparable() + ";";
		daten += "NOFTEAMS*" + nOTeam + ";";
		daten += "QUALI*" + hasQ + ";";
		daten += "GRPSTG*" + hasGrp + ";";
		daten += "KOSTG*" + hasKO + ";";
		daten += "NOFGRPS*" + nOGrp + ";";
		daten += "GRPSECLEG*" + grp2leg + ";";
		daten += "KOSECLEG*"+ ko2leg + ";";
		daten += "MATCHF3PL*" + has3pl + ";";
		daten += "S0*" + season + "*S0;";
		
		File leagueFile = new File(workspace + File.separator + name);
		File seasonFile = new File(workspace + File.separator + name + File.separator + season + (isSTSS ? "_" + (season + 1) : ""));
		try {
			leagueFile.mkdir();
			seasonFile.mkdir();
		} catch (Exception e) {
			error("Error while creating season root directory!");
		}
		if (hasQ) {
			
		}
		if (hasGrp) {
			try {
				for (int index = 0; index < nOGrp; index++) {
					// Berechnung der Anzahlen
					int numberOfTeamsPerGroup = (nOTeam + nOGrp - 1) / nOGrp;
					if ((numberOfTeamsPerGroup * nOGrp - nOTeam) > (nOGrp - index - 1))	numberOfTeamsPerGroup--;
					
					int numberOfMatchdays = 2 * ((numberOfTeamsPerGroup + 1) / 2) - 1;
					numberOfMatchdays *= grp2leg ? 2 : 1;
					
					// Erstellung der Mannschaftsnamen sowie des Spielplans und des Ergebnisplans
					String[] teams = new String[numberOfTeamsPerGroup];
					String[] matches = new String[numberOfMatchdays];
					String[] results = new String[numberOfMatchdays];
					for (int team = 0; team < numberOfTeamsPerGroup; team++) {
						try {
							teams[team] = teamsGrp[index][team].substring(0);
						} catch (NullPointerException npe) {
							teams[team] = "Mannschaft " + (team + 1);
						}
					}
					
					String falseStr = "";
					for (int i = 0; i < numberOfTeamsPerGroup / 2; i++) {
						falseStr += "f";
					}
					
					for (int i = 0; i < numberOfMatchdays; i++) {
						matches[i] = falseStr;
						results[i] = falseStr;
					}

					// Erstellung des Ordners
					File groupFile = new File(seasonFile.getAbsolutePath() + File.separator + "Gruppe " + alphabet[index]);
					groupFile.mkdir();
					
					// Speicherung des Spielplans und des Ergebnisplans
					writeFile(groupFile.getAbsolutePath() + File.separator + "Mannschaften.txt", teams);
					writeFile(groupFile.getAbsolutePath() + File.separator + "Spielplan.txt", matches);
					writeFile(groupFile.getAbsolutePath() + File.separator + "Ergebnisse.txt", results);
				}
			} catch (Exception e) {
				error("Error while creating group root directory!");
			}
		}
		if (hasKO) {
			try {
				// Erstellung und Abspeicherung des KOconfig-strings
				int skip3pl = has3pl ? 0 : 1, diff = koRFull.length - nOKO;
				String[] koConfig = new String[nOKO];
				for (int i = 0; i < nOKO - 1; i++) {
					koConfig[i] = koRFull[i + diff - skip3pl] + ";" + koRShort[i + diff - skip3pl] + ";" + (!has3pl || i != nOKO - 2) + ";" + ko2leg + ";";
				}
				koConfig[koConfig.length - 1] = koRFull[koRFull.length - 1] + ";" + koRShort[koRShort.length - 1] + ";" + true + ";" + false + ";";
				
				for (int index = 0; index < nOKO; index++) {
					// Berechnung der Anzahlen
					int numberOfTeamsPerKO = 2;
					for (int i = index; i < nOKO - 1; i++) {
						numberOfTeamsPerKO *= 2;
					}
					if (has3pl && index < nOKO - 1)	numberOfTeamsPerKO /= 2;
					
					int numberOfMatchdays = (ko2leg && index != nOKO - 1) ? 2 : 1;
					
					// Vervollständigung der KO-Konfiguration
					int numberOfTeamsPrequalified = 0;
					int numberOfTeamsFromPreviousRound = 0;
					int numberOfTeamsFromOtherCompetition = 0;
					
					// Erstellung der Mannschaftsnamen sowie des Spielplans und des Ergebnisplans
					String[] teams = new String[numberOfTeamsPerKO];
					String[] matches = new String[numberOfMatchdays];
					String[] results = new String[numberOfMatchdays];
					for (int team = 0; team < numberOfTeamsPerKO; team++) {
						try {
							String[] split = teamsKO[index][team].split("#");
							teams[team] = split[0];
							if (split[1].equals("PQ"))		numberOfTeamsPrequalified++;
							else if (split[1].equals("PR"))	numberOfTeamsFromPreviousRound++;
							else if (split[1].equals("OC"))	numberOfTeamsFromOtherCompetition++;
						} catch (NullPointerException npe) {
							if (index == 0) {
								// TODO Gruppendritte
								// TODO Fehler finden: war überall eine Runde zu spät(im Finale: VF1:VF2)
								if (hasGrp) {
									teams[team] = "G" + alphabet[team / 2] + (team % 2 + 1);
									numberOfTeamsFromPreviousRound++;
								} else {
									teams[team] = "Mannschaft " + (team + 1);
									numberOfTeamsPrequalified++;
								}
							} else if (index == nOKO - 1) {
								teams[team] = koRShort[index + diff - (has3pl ? 3 : 2)] + (team + 1);
								numberOfTeamsFromPreviousRound++;
							} else {
								teams[team] = koRShort[index + diff - 2] + (team + 1);
								numberOfTeamsFromPreviousRound++;
							}
						}
					}
					
					koConfig[index] += numberOfTeamsPrequalified + ";" + numberOfTeamsFromPreviousRound + ";" + numberOfTeamsFromOtherCompetition + ";";
					
					String falseStr = "";
					for (int i = 0; i < numberOfTeamsPerKO / 2; i++) {
						falseStr += "f";
					}
					
					for (int i = 0; i < numberOfMatchdays; i++) {
						matches[i] = falseStr;
						results[i] = falseStr;
					}
					
					// Erstellung des Ordners
					File koFile = new File(seasonFile.getAbsolutePath() + File.separator + koConfig[index].split(";")[0]);
					koFile.mkdir();
					
					// Speicherung des Spielplans und des Ergebnisplans
					writeFile(koFile.getAbsolutePath() + File.separator + "Mannschaften.txt", teams);
					writeFile(koFile.getAbsolutePath() + File.separator + "Spielplan.txt", matches);
					writeFile(koFile.getAbsolutePath() + File.separator + "Ergebnisse.txt", results);
				}
				writeFile(seasonFile.getAbsolutePath() + File.separator + "KOconfig.txt", koConfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// Speicherung in config / turniere-Array
		numberOfTournaments++;
		tournaments.add(new Turnier(tournaments.size(), daten));
		
		saveConfiguration();
		loadConfiguration();
		buildLeaguesButtons();
		
		message("Successfully created new tournament.");
	}
	
	public void showGroup(int groupID) {
		jBtnBackActionPerformed();
		jBtnsGroupsPressed(groupID);
		jBtnMatchdaysActionPerformed();
	}
	
	public void jBtnExitActionPerformed() {
		saveConfiguration();
		savePlayers();
		System.exit(0);
	}
	
	public void jBtnBackActionPerformed() {
		if (isCurrentlyALeague) {
			// is a league
			if (aktuellerSpieltag.isVisible()) {
				aktuellerSpieltag.ensureNoOpenedMatchInfos();
				if (aktuellerSpieltag.getEditedMatchday() != -1) {
					if (aktuellerSpieltag.jBtnDoneActionPerformed() != 0) {
						return;
					}
				}
			}
			
			if (LeagueHomescreen.isVisible()) {
				currentLeague.save();
				savePlayers();
				refreshRunningAndCompletedMatches();
				LeagueHomescreen.setVisible(false);
				Homescreen.setVisible(true);
				setTitle("Fußball");
			} else if (uebersicht.isVisible()) {
				if (isCurrentlyInMatchdayView) {
					aktuellerSpieltag.setVisible(true);
				} else {
					aktuelleTabelle.setVisible(true);
				}
				uebersicht.setVisible(false);
			} else {
				LeagueHomescreen.add(jBtnBack);
				isCurrentlyInMatchdayView = false;
				aktuellerSpieltag.setVisible(false);
				aktuelleTabelle.setVisible(false);
				aktuelleStatistik.setVisible(false);
				jPnlOptions.setVisible(false);
				LeagueHomescreen.setVisible(true);
			}
		} else {
			// is a tournament
			if (currentGroup != null && isCurrentlyInMatchdayView) {
				aktuellerSpieltag.ensureNoOpenedMatchInfos();
				if (aktuellerSpieltag.getEditedMatchday() != -1) {
					if (aktuellerSpieltag.jBtnDoneActionPerformed() != 0) {
						message("Ein Fehler ist aufgetreten, zurück gehen war nicht möglich.");
						return;
					}
				}
			}
			
			if (currentKORound != null) {
				aktuellerSpieltag.ensureNoOpenedMatchInfos();
				if (aktuellerSpieltag.getEditedMatchday() != -1) {
					if (aktuellerSpieltag.jBtnDoneActionPerformed() != 0) {
						message("Ein Fehler ist aufgetreten, zurück gehen war nicht möglich.");
						return;
					}
				}
			}
			
			if (isCurrentlyInOverviewMode) {
				if (uebersicht.isVisible()) {
					uebersicht.setVisible(false);
					aktuellerSpieltag.setVisible(true);
					return;
				}
				aktuellerSpieltag.ensureNoOpenedMatchInfos();
				if (aktuellerSpieltag.getEditedMatchday() != -1) {
					if (aktuellerSpieltag.jBtnDoneActionPerformed() != 0) {
						message("Ein Fehler ist aufgetreten, zurück gehen war nicht möglich.");
						return;
					}
				}
			}
			
			if (TournamentHomescreen.isVisible()) {
				currentTournament.save();
				savePlayers();
				refreshRunningAndCompletedMatches();
				TournamentHomescreen.setVisible(false);
				Homescreen.setVisible(true);
				setTitle("Fußball");
			} else if (QualificationHomescreen.isVisible() || GroupStageHomescreen.isVisible() || KORoundsHomescreen.isVisible()) {
				QualificationHomescreen.setVisible(false);
				GroupStageHomescreen.setVisible(false);
				KORoundsHomescreen.setVisible(false);
				TournamentHomescreen.add(jBtnBack);
				TournamentHomescreen.setVisible(true);
				if ((!currentTSeason.hasQualification() && !currentTSeason.hasGroupStage()) || (!currentTSeason.hasQualification() && !currentTSeason.hasKOStage())) {
					jBtnBackActionPerformed();
				}
			} else if (LeagueHomescreen.isVisible()) {
				LeagueHomescreen.setVisible(false);
				if (isCurrentlyInQualification) {
					QualificationHomescreen.add(jBtnBack);
					QualificationHomescreen.setVisible(true);
				} else {
					GroupStageHomescreen.add(jBtnBack);
					GroupStageHomescreen.setVisible(true);
				}
				
				currentGroup = null;
			} else if (isCurrentlyInOverviewMode) {
				isCurrentlyInMatchdayView = false;
				aktuellerSpieltag.setVisible(false);
				if (isCurrentlyInQualification) {
					QualificationHomescreen.add(jBtnBack);
					QualificationHomescreen.setVisible(true);
				} else {
					GroupStageHomescreen.add(jBtnBack);
					GroupStageHomescreen.setVisible(true);
				}
				isCurrentlyInOverviewMode = false;
				aktuellerSpieltag = null;
			} else if (uebersicht != null && uebersicht.isVisible()) {
				if (isCurrentlyInMatchdayView) {
					aktuellerSpieltag.setVisible(true);
				} else {
					aktuelleTabelle.setVisible(true);
				}
				uebersicht.setVisible(false);
			} else if (isCurrentlyInQualification) {
				if (currentTSeason.hasQGroupStage() && aktuelleTabelle != null) {
					aktuelleTabelle.setVisible(false);
					jPnlOptions.setVisible(false);
				}
				isCurrentlyInMatchdayView = false;
				aktuellerSpieltag.setVisible(false);
				QualificationHomescreen.add(jBtnBack);
				QualificationHomescreen.setVisible(true);
				
				currentGroup = null;
				currentKORound = null;
			} else if (isCurrentlyInGroupStage) {
				isCurrentlyInMatchdayView = false;
				aktuellerSpieltag.setVisible(false);
				aktuelleTabelle.setVisible(false);
				jPnlOptions.setVisible(false);
				
				LeagueHomescreen.add(jBtnBack);
				LeagueHomescreen.setVisible(true);
			} else {
				isCurrentlyInMatchdayView = false;
				aktuellerSpieltag.setVisible(false);
				currentKORound = null;
				
				KORoundsHomescreen.add(jBtnBack);
				KORoundsHomescreen.setVisible(true);
			}
		}
	}
	
	private void loadPlayers() {
		filePlayers = workspace + File.separator + "players.txt";
		playersFromFile = readFile(filePlayers);
		
		players = new HashMap<>();
		
		for (int i = 0; i < playersFromFile.size(); i++) {
			Spieler player = new Spieler(playersFromFile.get(i));
			if (players.containsKey(player.getID()))	message("Doppelt vergebene ID!");
			players.put(player.getID(), player);
		}
	}
	
	private void savePlayers() {
		playersFromFile = new ArrayList<>();
		
		Set<Integer> ids = players.keySet();
		for (Integer id : ids) {
			addAscending(playersFromFile, players.get(id).toString());
		}
		
		writeFile(filePlayers, playersFromFile);
	}
	
	private void loadConfiguration() {
		fileConfiguration = workspace + File.separator + "config.txt";
		configurationFromFile = readFile(fileConfiguration);
		
		int counter = 0;
		
		numberOfLeagues = Integer.parseInt(configurationFromFile.get(counter++));
		leagues = new ArrayList<>();
		
		for (int i = 0; i < numberOfLeagues; i++) {
			leagues.add(new Liga(i, configurationFromFile.get(counter++)));
		}
		
		numberOfTournaments = Integer.parseInt(configurationFromFile.get(counter++));
		tournaments = new ArrayList<>();
		
		for (int i = 0; i < numberOfTournaments; i++) {
			tournaments.add(new Turnier(i, configurationFromFile.get(counter++)));
		}
	}
	
	private void saveConfiguration() {
		configurationFromFile = new ArrayList<>();
		
		
		configurationFromFile.add("" + numberOfLeagues);
		
		for (int i = 0; i < leagues.size(); i++) {
			configurationFromFile.add(leagues.get(i).toString());
		}
		
		configurationFromFile.add("" + numberOfTournaments);
		
		for (int i = 0; i < tournaments.size(); i++) {
			configurationFromFile.add(tournaments.get(i).toString());
		}
		
		writeFile(fileConfiguration, configurationFromFile);
	}
	
	public void checkOS() {
		if (new File(workspaceWIN).isDirectory()) {
			workspace = workspaceWIN;
		} else if (new File(workspaceMAC).isDirectory()) {
			workspace = workspaceMAC;
		} else {
			workspace = null;
		}
	}
}
