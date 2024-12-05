package org.easydish.easydish;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class SavedIngredientController
{
    String ingredient;

    @FXML
    private Label ingredientName;

    public void setIngredientName(String name) {ingredientName.setText(name);}

    public void removeButton() throws IOException
    {
        EasyDishApplication.savedIngredients.remove(ingredientName.getText().replaceAll("\\s+", "+"));
        EasyDishApplication.removeIngredient();
    }
}
