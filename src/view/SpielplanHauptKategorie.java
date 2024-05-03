package view;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;

import dto.fixtures.SpielplanHauptKategorieDTO;

public class SpielplanHauptKategorie extends SpielplanZeile {
	private static final long serialVersionUID = 4576226976555379793L;
	
	private static final Font fontMainCategory = new Font("Lucida Grande", Font.BOLD, 13);
	
	private static final int height = 15;
	
	private static final Rectangle REC_CATEGORY = new Rectangle(0, 0, 250, height);
	
	private JLabel jLblCategory;
	
	public int getHeight() {
		return height;
	}
	
	public void setSize() {
		setSize(width, height);
	}
	
	public void setCategory(String category) {
		jLblCategory = new JLabel();
		add(jLblCategory);
		jLblCategory.setBounds(REC_CATEGORY);
		jLblCategory.setFont(fontMainCategory);
		jLblCategory.setText(category);
	}

	public static SpielplanHauptKategorie of(SpielplanHauptKategorieDTO mainCategoryDto) {
		SpielplanHauptKategorie mainCategory = new SpielplanHauptKategorie();
		
		mainCategory.setSize();
		
		mainCategory.setCategory(mainCategoryDto.getCategory());
		
		return mainCategory;
	}
}
