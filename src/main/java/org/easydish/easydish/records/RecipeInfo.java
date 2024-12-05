package org.easydish.easydish.records;

import java.util.List;

public record RecipeInfo(int id, String title, String image, String imageType, int servings, int readyInMinutes,
                         String sourceName, String sourceURL, List<String> dishTypes,
                         List<Ingredient> extendedIngredients)
{
}
