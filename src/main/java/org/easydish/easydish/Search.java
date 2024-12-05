package org.easydish.easydish;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class Search
{
    @FXML
    VBox scrollPaneVBox;

    public void refreshRecipes(ActionEvent e)
    {
        EasyDishApplication.searchRecipes();
    }
}
