package view;

import java.awt.Rectangle;

import javax.swing.JLabel;

import dto.fixtures.SpielplanUnterKategorieDTO;

public class SpielplanUnterKategorie extends SpielplanZeile {
	private static final long serialVersionUID = -8539714895751503346L;
	
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
		jLblCategory.setText(category);
	}

	public static SpielplanUnterKategorie of(SpielplanUnterKategorieDTO subCategoryDto) {
		SpielplanUnterKategorie subCategory = new SpielplanUnterKategorie();
		
		subCategory.setSize();
		
		subCategory.setCategory(subCategoryDto.getCategory());
		
		return subCategory;
	}
}
