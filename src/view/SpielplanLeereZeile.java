package view;

import dto.fixtures.SpielplanLeereZeileDTO;

public class SpielplanLeereZeile extends SpielplanZeile {
	private static final long serialVersionUID = 1962081449286049229L;
	
	private static final int height = 5;
	
	public int getHeight() {
		return height;
	}
	
	public void setSize() {
		setSize(width, height);
	}

	public static SpielplanLeereZeile of(SpielplanLeereZeileDTO emptyRowDto) {
		SpielplanLeereZeile emptyRow = new SpielplanLeereZeile();
		
		emptyRow.setSize();
		
		return emptyRow;
	}
}
