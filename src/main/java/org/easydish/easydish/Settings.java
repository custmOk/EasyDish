package org.easydish.easydish;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.easydish.easydish.records.Setting;

import java.util.Objects;

public class Settings
{
    @FXML Label generatedRecipeAmount;
    @FXML Slider amountSlider;
    @FXML ToggleGroup uiTheme;
    @FXML ToggleButton lightButton;
    @FXML ToggleButton darkButton;
    @FXML TextField apiKey;
    String uiMode;

    public void initialize()
    {
        amountSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            setGeneratedRecipeAmount(newValue.intValue());
        });
    }

    public void setGeneratedRecipeAmount(int amount)
    {
        generatedRecipeAmount.setText(String.format("Generated Recipe Amount - %d", amount));
        EasyDishApplication.generatedRecipeAmount = amount;
    }

    public void loadSettings()
    {
        Setting setting = EasyDishApplication.setting;
        setGeneratedRecipeAmount(setting.generatedRecipeAmount());
        amountSlider.setValue(setting.generatedRecipeAmount());
        if (setting.uiTheme().equalsIgnoreCase("light"))
        {
            uiMode = "light";
            darkButton.setSelected(false);
            lightButton.setSelected(true);
        }
        if (setting.uiTheme().equalsIgnoreCase("dark"))
        {
            uiMode = "dark";
            lightButton.setSelected(false);
            darkButton.setSelected(true);
        }
        apiKey.setText(setting.apiKey());
    }
    public void saveSettings()
    {
        EasyDishApplication.setting = new Setting((int)amountSlider.getValue(), uiMode, apiKey.getText());
        EasyDishApplication.saveSettings();
    }
    public void toggleTheme(ActionEvent e)
    {
        Scene scene = EasyDishApplication.scene;
        scene.getStylesheets().clear();

        if (e.getSource() == lightButton)
        {
            uiMode = "light";
            darkButton.setSelected(false);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("light-mode.css")).toExternalForm());
        }
        if (e.getSource() == darkButton)
        {
            uiMode = "dark";
            lightButton.setSelected(false);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("dark-mode.css")).toExternalForm());
        }

        System.out.println(scene.getStylesheets());
    }

    public void clearIngredients()
    {
        EasyDishApplication.savedIngredients.clear();
        EasyDishApplication.saveIngredients();
    }
    public void clearRecipes()
    {
        EasyDishApplication.savedRecipes.clear();
        EasyDishApplication.saveRecipes();
    }
    public void contactUs()
    {

    }
}
