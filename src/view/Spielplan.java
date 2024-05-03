package view;

import java.util.ArrayList;

import javax.swing.JPanel;

import dto.fixtures.SpielplanHauptKategorieDTO;
import dto.fixtures.SpielplanLeereZeileDTO;
import dto.fixtures.SpielplanSpielDTO;
import dto.fixtures.SpielplanUnterKategorieDTO;
import dto.fixtures.SpielplanZeileDTO;
import model.Mannschaft;

import static util.Utilities.colorOverviewBackground;

public class Spielplan extends JPanel {
	
	private static final long serialVersionUID = 6178352900539798162L;

	private static final int outerMargin = 5;
	private static final int gapBetweenRows = 5;
	private static final int width = 585;

	private int nextStartY = outerMargin;
	
	private ArrayList<SpielplanZeile> fixtureRows;
	
	private void addFixtureRow(SpielplanZeileDTO fixtureRowDto, Mannschaft team) {
		SpielplanZeile fixtureRow = null;
		if (fixtureRowDto instanceof SpielplanHauptKategorieDTO) {
			fixtureRow = SpielplanHauptKategorie.of((SpielplanHauptKategorieDTO) fixtureRowDto);
		} else if (fixtureRowDto instanceof SpielplanUnterKategorieDTO) {
			fixtureRow = SpielplanUnterKategorie.of((SpielplanUnterKategorieDTO) fixtureRowDto);
		} else if (fixtureRowDto instanceof SpielplanSpielDTO) {
			fixtureRow = SpielplanSpiel.of((SpielplanSpielDTO) fixtureRowDto, team);
		} else if (fixtureRowDto instanceof SpielplanLeereZeileDTO) {
			fixtureRow = SpielplanLeereZeile.of((SpielplanLeereZeileDTO) fixtureRowDto);
		}
		
		fixtureRows.add(fixtureRow);
		add(fixtureRow);
		fixtureRow.setLocation(outerMargin, nextStartY);
		nextStartY += fixtureRow.getHeight() + gapBetweenRows;
	}
	
	public void showOnlyHomeMatches() {
		for (SpielplanZeile fixtureRow : fixtureRows) {
			if (fixtureRow instanceof SpielplanSpiel) {
				((SpielplanSpiel) fixtureRow).hideMatchIfAway();
			}
		}
	}
	
	public void showOnlyAwayMatches() {
		for (SpielplanZeile fixtureRow : fixtureRows) {
			if (fixtureRow instanceof SpielplanSpiel) {
				((SpielplanSpiel) fixtureRow).hideMatchIfHome();
			}
		}
	}
	
	public void showAllMatches() {
		for (SpielplanZeile fixtureRow : fixtureRows) {
			if (fixtureRow instanceof SpielplanSpiel) {
				((SpielplanSpiel) fixtureRow).showMatch();
			}
		}
	}
	
	private void setSize() {
		setSize(width, nextStartY - gapBetweenRows + outerMargin);
	}
	
	public static Spielplan of(ArrayList<SpielplanZeileDTO> allFixtureRows, Mannschaft team) {
		Spielplan fixtureOverview = new Spielplan();
		
		fixtureOverview.setLayout(null);
		fixtureOverview.setOpaque(true);
		fixtureOverview.setBackground(colorOverviewBackground);
		fixtureOverview.fixtureRows = new ArrayList<>();
		for (SpielplanZeileDTO fixtureRowDto : allFixtureRows) {
			fixtureOverview.addFixtureRow(fixtureRowDto, team);
		}
		fixtureOverview.setSize();
		
		return fixtureOverview;
	}
}
