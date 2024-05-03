package model;

import static util.Utilities.colorOverviewBackground;

import java.awt.Color;

public enum SubjektivesErgebnis {
	WIN(Color.green),
	DRAW(Color.white),
	LOSS(Color.red),
	NOT_SET(colorOverviewBackground);
	
	private Color background;
	
	SubjektivesErgebnis(Color background) {
		this.background = background;
	}
	
	public Color getColor() {
		return background;
	}
}
