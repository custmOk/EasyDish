package org.easydish.easydish.records;

import java.util.List;

public record RecipeInstructions(String name, List<RecipeStep> steps)
{
}
