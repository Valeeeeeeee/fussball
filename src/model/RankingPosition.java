package model;

public class RankingPosition {
	
	private int rank;
	
	private int rowInTable;
	
	public RankingPosition() {
		rank = 0;
		rowInTable = -1;
	}
	
	private RankingPosition(int rank, int rowInTable) {
		this.rank = rank;
		this.rowInTable = rowInTable;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getRowInTable() {
		return rowInTable;
	}
	
	public RankingPosition nextTiedPosition() {
		return new RankingPosition(rank, rowInTable + 1);
	}
	
	public RankingPosition nextPosition() {
		return new RankingPosition(rowInTable + 2, rowInTable + 1);
	}
}
