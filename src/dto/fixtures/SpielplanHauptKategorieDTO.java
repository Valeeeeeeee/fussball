package dto.fixtures;

public class SpielplanHauptKategorieDTO extends SpielplanZeileDTO {
	
	private String category;
	
	public String getCategory() {
		return category;
	}
	
	public static SpielplanHauptKategorieDTO of(String category) {
		SpielplanHauptKategorieDTO subCategory = new SpielplanHauptKategorieDTO();
		subCategory.category = category;
		return subCategory;
	}
}
