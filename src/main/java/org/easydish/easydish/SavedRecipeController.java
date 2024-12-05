package org.easydish.easydish;

import javafx.event.ActionEvent;
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

public class SavedRecipeController
{
    Recipe recipe;

    @FXML
    private ImageView recipeImageView;
    @FXML
    private Label recipeLabel;

    public void setRecipeImage(String imagePath)
    {
        if (imagePath != null)
            recipeImageView.setImage(new Image(imagePath));
        RoundImage.roundImage(recipeImageView, 20);
    }
    public void setRecipeTitleText(String text, int id)
    {
        recipeLabel.setText(text);
        recipeLabel.setUserData(new String[]{text, String.valueOf(id)});
    }

    public void removeButton(ActionEvent e) throws IOException
    {
        EasyDishApplication.savedRecipes.remove(recipe);
        EasyDishApplication.removeRecipe();
    }

    public void showRecipeInfo(MouseEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(RecipeDisplay.class.getResource("recipe-display.fxml"));
            Parent popupContent = fxmlLoader.load();

            RecipeDisplay recipeDisplayController = fxmlLoader.getController();
            EasyDishApplication.setRecipeInfoController(recipeDisplayController);
            String[] data = (String[])(recipeLabel.getUserData());
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
