package org.easydish.easydish.records;

import java.util.List;

public record Ingredient(int id, double amount, String unit, String unitLong, String unitShort, String aisle,
                         String name, String original, String originalName, List<String> meta, String extendedName,
                         String image, Measures measures) implements Comparable<Ingredient>
{
    @Override
    public int compareTo(Ingredient other)
    {
        return name.compareTo(other.name);
    }
}
