package org.easydish.easydish;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;


public class EasyDishController
{
    @FXML ImageView logoImageView;
    @FXML StackPane contentArea;

    @FXML
    public void initialize() {RoundImage.roundImage(logoImageView, 40);}

    public void searchButton() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Search.class.getResource("search.fxml"));
        Parent fxml = fxmlLoader.load();

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);

        Search searchController = fxmlLoader.getController();
        EasyDishApplication.setSearchController(searchController);
        EasyDishApplication.saveIngredients();
        EasyDishApplication.saveRecipes();

        EasyDishApplication.addAllRecipes(true);
    }

    public void bagButton() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Bag.class.getResource("bag.fxml"));
        Parent fxml = fxmlLoader.load();

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);

        Bag bagController = fxmlLoader.getController();
        EasyDishApplication.setBagController(bagController);
        EasyDishApplication.saveRecipes();

        EasyDishApplication.readIngredients();
        EasyDishApplication.addAllIngredients();
    }

    public void bookButton() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Book.class.getResource("book.fxml"));
        Parent fxml = fxmlLoader.load();

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);

        Book bookController = fxmlLoader.getController();
        EasyDishApplication.setBookController(bookController);
        EasyDishApplication.saveIngredients();

        EasyDishApplication.readRecipes();
        EasyDishApplication.addAllRecipes(false);
    }

    public void calendarButton() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Calendar.class.getResource("calendar.fxml"));
        Parent fxml = fxmlLoader.load();

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);

        EasyDishApplication.saveIngredients();
        EasyDishApplication.saveRecipes();
    }

    public void settingsButton() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Settings.class.getResource("settings.fxml"));
        Parent fxml = fxmlLoader.load();

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);

        Settings settingsController = fxmlLoader.getController();
        EasyDishApplication.setSettingsController(settingsController);
        EasyDishApplication.saveIngredients();
        EasyDishApplication.saveRecipes();

        EasyDishApplication.readSettings();
        settingsController.loadSettings();
    }
}
