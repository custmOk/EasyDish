package org.easydish.easydish;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeDisplay
{
    @FXML Label ingredientsTabTitle;
    @FXML VBox informationScrollPaneVBox;
    @FXML VBox ingredientsScrollPaneVBox;
    @FXML VBox instructionsScrollPaneVBox;
    @FXML ImageView recipeImageView;
    @FXML Label recipeTitle;

    public void setRecipeImageView(String imagePath)
    {
        if (imagePath != null)
            recipeImageView.setImage(new Image(imagePath));
        RoundImage.roundImage(recipeImageView, 20);
    }
    public void setRecipeTitle(String text)
    {
        recipeTitle.setText(text);
    }
    public void setIngredientsTabTitle(int used, int missed)
    {
        ingredientsTabTitle.setText(String.format("INGREDIENTS - %d/%d", used, used + missed));
    }

    public void closeWindow(ActionEvent e)
    {
        Stage currentStage = (Stage) informationScrollPaneVBox.getScene().getWindow();
        currentStage.close();
    }
}
