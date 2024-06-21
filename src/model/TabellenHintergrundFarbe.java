package model;

import java.awt.Color;
import java.util.stream.Stream;

public enum TabellenHintergrundFarbe {
	NONE(0, null),
	DARK_GREEN(1, util.Utilities.DARK_GREEN),
	LIGHT_GREEN(2, util.Utilities.LIGHT_GREEN),
	YELLOW(3, util.Utilities.YELLOW),
	LIGHT_RED(4, util.Utilities.LIGHT_RED),
	RED(5, util.Utilities.RED),
	UCL(6, util.Utilities.UCL),
	UCL_Q(7, util.Utilities.UCL_Q),
	UEL(8, util.Utilities.UEL),
	UEL_Q(9, util.Utilities.UEL_Q),
	UECL(10, util.Utilities.UECL_Q);
	
	private int category;
	
	private Color color;
	
	TabellenHintergrundFarbe(int category, Color color) {
		this.category = category;
		this.color = color;
	}
	
	public int getCategory() {
		return category;
	}
	
	public Color getColor() {
		return color;
	}
	
	public static TabellenHintergrundFarbe of(int category) {
		return Stream.of(values()).filter(bg -> bg.getCategory() == category).findFirst().orElse(NONE);
	}
}
