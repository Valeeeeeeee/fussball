package dto.fixtures;

public class SpielplanUnterKategorieDTO extends SpielplanZeileDTO {
	
	private String category;
	
	public String getCategory() {
		return category;
	}
	
	public static SpielplanUnterKategorieDTO of(String category) {
		SpielplanUnterKategorieDTO subCategory = new SpielplanUnterKategorieDTO();
		subCategory.category = category;
		return subCategory;
	}
}
