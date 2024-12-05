package org.easydish.easydish;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.easydish.easydish.records.Recipe;

import java.io.IOException;

public class ListedRecipeController
{
    Recipe recipe;

    @FXML
    private ImageView recipeImageView;
    @FXML
    private Label recipeTitle;
    @FXML
    private Label recipeLikes;
    @FXML
    private Label recipeStatus;

    public void setRecipeImage(String imagePath)
    {
        if (imagePath != null)
            recipeImageView.setImage(new Image(imagePath));
        RoundImage.roundImage(recipeImageView, 20);
    }

    public void setRecipeTitleText(String text, int id)
    {
        recipeTitle.setText(text);
        recipeTitle.setUserData(new String[]{text, String.valueOf(id)});
    }

    public void setRecipeLikesText(String text)
    {
        recipeLikes.setText(text);
    }

    public void setRecipeStatusText(String text)
    {
        recipeStatus.setText(text);
    }

    public void saveButton()
    {
        EasyDishApplication.savedRecipes.add(recipe);
    }

    public void showRecipeInfo(MouseEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(RecipeDisplay.class.getResource("recipe-display.fxml"));
            Parent popupContent = fxmlLoader.load();

            RecipeDisplay recipeDisplayController = fxmlLoader.getController();
            EasyDishApplication.setRecipeInfoController(recipeDisplayController);
            String[] data = (String[])(recipeTitle.getUserData());
            EasyDishApplication.getRecipeInfo(Integer.parseInt(data[1]));

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(popupContent));
            popupStage.initOwner(((Node)event.getSource()).getScene().getWindow());
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
        }
        catch (IOException e)
        {
            System.out.println("Error?");
        }
    }
}
