package org.easydish.easydish;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class Bag
{
    @FXML
    FlowPane scrollPaneFlowPane;
    @FXML
    TextField ingredientNameTextField;

    public void addButton(ActionEvent e) throws IOException
    {
        String ingredientName = ingredientNameTextField.getText().toLowerCase().replaceAll("\\s+", "+");

        if (!EasyDishApplication.savedIngredients.contains(ingredientName))
        {
            EasyDishApplication.savedIngredients.add(ingredientName);
            EasyDishApplication.addSingleIngredient(ingredientName);
            ingredientNameTextField.setText("");
        }
    }
}
