package model;

import static util.Utilities.log;

import java.util.ArrayList;

public class SubRanking {
	private int value;
	
	private ArrayList<Mannschaft> teams;
	
	private int untilMatchday;
	
	private Tabellenart tableType;
	
	private ArrayList<Integer> idsAtValue;
	
	private ArrayList<SubRanking> children;
	
	public SubRanking(int value, ArrayList<Mannschaft> teams, int untilMatchday, Tabellenart tableType) {
		this.value = value;
		this.teams = teams;
		this.untilMatchday = untilMatchday;
		this.tableType = tableType;
		idsAtValue = new ArrayList<>();
		children = new ArrayList<>();
	}
	
	public int getValue() {
		return value;
	}
	
	public ArrayList<Integer> getIdsAtValue() {
		return idsAtValue;
	}
	
	public void addIdAtValue(int id) {
		idsAtValue.add(id);
	}
	
	public boolean isOneTeam() {
		return idsAtValue.size() == 1;
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public void discriminate(RankingCriterion criterion) {
		if (hasChildren()) {
			for (SubRanking subRanking : children) {
				subRanking.discriminate(criterion);
			}
		} else if (!isOneTeam()) {
			ArrayList<Mannschaft> includedOpponents = criterion.includeAllGames() ? teams : getTeamsWithIds(idsAtValue);
			for (Integer id : idsAtValue) {
				int value = teams.get(id - 1).getValueForCriterion(includedOpponents, untilMatchday, tableType, criterion);
				
				boolean alreadyExisted = false;
				int index = 0;
				for (SubRanking child : children) {
					if (value < child.getValue()) {
						index++;
					} else if (value == child.getValue()) {
						child.addIdAtValue(id);
						alreadyExisted = true;
					}
				}
				if (!alreadyExisted) {							
					SubRanking newSubRanking = new SubRanking(value, teams, untilMatchday, tableType);
					newSubRanking.addIdAtValue(id);
					children.add(index, newSubRanking);
				}
			}
		}
	}
	
	private ArrayList<Mannschaft> getTeamsWithIds(ArrayList<Integer> ids) {
		ArrayList<Mannschaft> includedOpponents = new ArrayList<>();
		
		for (int id : ids) {
			includedOpponents.add(teams.get(id - 1));
		}
		
		return includedOpponents;
	}
	
	public RankingPosition setPlaces(RankingPosition lastRankingPosition) {
		if (hasChildren()) {
			for (SubRanking child : children) {
				lastRankingPosition = child.setPlaces(lastRankingPosition);
			}
		} else {
			boolean tied = false;
			for (int id : idsAtValue) {
				lastRankingPosition = tied ? lastRankingPosition.nextTiedPosition() : lastRankingPosition.nextPosition();
				teams.get(id - 1).setPlace(lastRankingPosition.getRank() - 1);
				tied = true;
			}
		}
		return lastRankingPosition;
	}
}
