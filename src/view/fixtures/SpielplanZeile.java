package view.fixtures;

import javax.swing.JPanel;

import static util.Utilities.colorOverviewBackground;

public abstract class SpielplanZeile extends JPanel {

	private static final long serialVersionUID = 4575623697218080185L;
	
	protected static final int width = 575;
	
	protected SpielplanZeile() {
		setLayout(null);
		setBackground(colorOverviewBackground);
	}
	
	public abstract int getHeight();
	
	public abstract void setSize();
}
