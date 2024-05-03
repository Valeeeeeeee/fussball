package view.fixtures;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import dto.fixtures.SpielplanSpielDTO;
import model.Fussball;
import model.Mannschaft;
import model.SubjektivesErgebnis;
import model.Wettbewerb;

import static util.Utilities.*;

public class SpielplanSpiel extends SpielplanZeile {
	private static final long serialVersionUID = -3270642106744236802L;
	
	private static final int height = 15;
	
	private static final Color colorGrey = new Color(128, 128, 128);
	
	private static final Color colorHighlighted = Color.yellow;
	
	private static final Rectangle REC_MATCHDAY = new Rectangle(0, 0, 20, height);
	private static final Rectangle REC_DATE_AND_TIME = new Rectangle(25, 0, 120, height);
	private static final Rectangle REC_HOME_TEAM_NAME = new Rectangle(150, 0, 185, height);
	private static final Rectangle REC_RESULT = new Rectangle(344, 0, 37, height);
	private static final Rectangle REC_AWAY_TEAM_NAME = new Rectangle(390, 0, 185, height);
	
	private JLabel jLblMatchday;
	private JLabel jLblDateAndTime;
	private JLabel jLblHomeTeamName;
	private JLabel jLblResult;
	private JLabel jLblAwayTeamName;
	
	private boolean isHomeTeam;
	private boolean isAwayTeam;
	
	public int getHeight() {
		return height;
	}
	
	public void setSize() {
		setSize(width, height);
	}
	
	public void hideMatchIfHome() {
		if (isHomeTeam)	hideMatch();
	}
	
	public void hideMatchIfAway() {
		if (isAwayTeam)	hideMatch();
	}
	
	private void hideMatch() {
		jLblHomeTeamName.setOpaque(false);
		jLblResult.setOpaque(false);
		jLblAwayTeamName.setOpaque(false);
		setBackground(colorGrey);
		repaintImmediately(this);
	}
	
	public void showMatch() {
		setBackground(colorOverviewBackground);
		if (isHomeTeam)	jLblHomeTeamName.setOpaque(true);
		jLblResult.setOpaque(true);
		if (isAwayTeam)	jLblAwayTeamName.setOpaque(true);
		repaintImmediately(this);
	}
	
	private void showMatchday(Wettbewerb competition, int matchday) {
		Fussball.getInstance().showMatchday(competition, matchday);
	}
	
	private void showTeam(String jumpTo) {
		if (!jumpTo.equals(NOT_AVAILABLE) && !jumpTo.equals(SPIELFREI)) {
			Fussball.getInstance().uebersichtAnzeigen(jumpTo);
		}
	}
	
	public void setMatchday(Wettbewerb competition, String matchday) {
		jLblMatchday = new JLabel();
		add(jLblMatchday);
		alignCenter(jLblMatchday);
		jLblMatchday.setBounds(REC_MATCHDAY);
		jLblMatchday.setText(matchday);
		if (matchday.matches("[0-9]*")) {
			final int md = Integer.parseInt(matchday) - 1;
			jLblMatchday.setCursor(handCursor);
			jLblMatchday.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					showMatchday(competition, md);
				}
			});
		}
	}
	
	public void getDateAndTime(String dateAndTime) {
		jLblDateAndTime = new JLabel();
		add(jLblDateAndTime);
		alignCenter(jLblDateAndTime);
		jLblDateAndTime.setBounds(REC_DATE_AND_TIME);
		jLblDateAndTime.setText(dateAndTime);
	}
	
	public void setHomeTeamName(String homeTeamName, Mannschaft team) {
		jLblHomeTeamName = new JLabel();
		add(jLblHomeTeamName);
		alignRight(jLblHomeTeamName);
		jLblHomeTeamName.setBounds(REC_HOME_TEAM_NAME);
		jLblHomeTeamName.setText(homeTeamName);
		if (isHomeTeam = team.getName().equals(homeTeamName)) {
			jLblHomeTeamName.setOpaque(true);
			jLblHomeTeamName.setBackground(colorHighlighted);
		} else {
			jLblHomeTeamName.setCursor(handCursor);
			jLblHomeTeamName.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					showTeam(homeTeamName);
				}
			});
		}
	}
	
	public void setResult(String result, String resultToolTip, SubjektivesErgebnis subjectiveResult) {
		jLblResult = new JLabel();
		add(jLblResult);
		alignCenter(jLblResult);
		jLblResult.setBounds(REC_RESULT);
		jLblResult.setText(result);
		if (!resultToolTip.isEmpty())	jLblResult.setToolTipText(resultToolTip);
		jLblResult.setBackground(subjectiveResult.getColor());
		jLblResult.setOpaque(true);
	}
	
	public void setAwayTeamName(String awayTeamName, Mannschaft team) {
		jLblAwayTeamName = new JLabel();
		add(jLblAwayTeamName);
		alignLeft(jLblAwayTeamName);
		jLblAwayTeamName.setBounds(REC_AWAY_TEAM_NAME);
		jLblAwayTeamName.setText(awayTeamName);
		if (isAwayTeam = team.getName().equals(awayTeamName)) {
			jLblAwayTeamName.setOpaque(true);
			jLblAwayTeamName.setBackground(colorHighlighted);
		} else {
			jLblAwayTeamName.setCursor(handCursor);
			jLblAwayTeamName.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					showTeam(awayTeamName);
				}
			});
		}
	}

	public static SpielplanSpiel of(SpielplanSpielDTO matchDto, Mannschaft team) {
		SpielplanSpiel match = new SpielplanSpiel();
		
		match.setSize();
		
		match.setMatchday(matchDto.getCompetition(), matchDto.getMatchday());
		match.getDateAndTime(matchDto.getDateAndTime());
		match.setHomeTeamName(matchDto.getHomeTeamName(), team);
		match.setResult(matchDto.getResult(), matchDto.getResultToolTip(), matchDto.getSubjectiveResult(team));
		match.setAwayTeamName(matchDto.getAwayTeamName(), team);
		
		return match;
	}
}
