package org.easydish.easydish.records;

import java.util.List;

public record Recipe(int id, String title, String image, String imageType, int usedIngredientCount,
                     int missedIngredientCount, List<Ingredient> missedIngredients, List<Ingredient> usedIngredients,
                     List<Ingredient> unusedIngredients, int likes) implements Comparable<Recipe>
{
    @Override
    public int compareTo(Recipe other)
    {
        return title.compareTo(other.title);
    }
}
