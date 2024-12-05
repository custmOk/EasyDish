package org.easydish.easydish;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.easydish.easydish.records.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class EasyDishApplication extends Application
{
    private static final String SAVED_RECIPES_FILE = "src/main/java/org/easydish/easydish/saved-recipes.json";
    private static final String SAVED_INGREDIENTS_FILE = "src/main/java/org/easydish/easydish/saved-ingredients.json";
    private static final String SAVED_SETTINGS_FILE = "src/main/java/org/easydish/easydish/saved-settings.json";
    private static final String API_KEY = "946226982e2342a59685c91c28bad433";
    private static final String BASE_URL = "https://api.spoonacular.com";
    static int generatedRecipeAmount = 1;
    private static final Set<Recipe> recommendedRecipes = new TreeSet<>();
    static Set<String> savedIngredients = new TreeSet<>();
    static Set<Recipe> savedRecipes = new TreeSet<>();
    private static final HashMap<Integer, Recipe> recipes = new HashMap<>();
    static Setting setting = new Setting(generatedRecipeAmount, "dark", API_KEY);
    static Scene scene;

    private static Search searchController;
    private static Bag bagController;
    private static Book bookController;
    private static Calendar calendarController;
    private static Settings settingsController;
    private static RecipeDisplay recipeDisplayController;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(EasyDishApplication.class.getResource("easydish.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(setting.uiTheme() + "-mode.css")).toExternalForm());
        root.getStylesheets().addAll(scene.getStylesheets());
        System.out.println(root.getStylesheets());
        stage.initStyle(StageStyle.DECORATED);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }

    public static void removeRecipe() throws IOException
    {
        if (bookController == null)
        {
            System.out.println("Book controller is not set!");
            return;
        }

        FlowPane scrollPaneFlowPane = bookController.scrollPaneFlowPane;
        scrollPaneFlowPane.getChildren().clear();
        recipes.clear();

        for (Recipe recipe : savedRecipes)
            addSavedRecipe(scrollPaneFlowPane, recipe);
    }

    private static void addListedRecipe(Pane pane, Recipe recipe) throws IOException
    {
        FXMLLoader recipeLoader = new FXMLLoader(EasyDishApplication.class.getResource("listed-recipe.fxml"));
        HBox item = recipeLoader.load();

        ListedRecipeController controller = recipeLoader.getController();
        controller.recipe = recipe;
        controller.setRecipeImage(validateImageURL(recipe.image()) ? recipe.image() : null);
        controller.setRecipeTitleText(recipe.title(), recipe.id());
        controller.setRecipeLikesText(recipe.likes() + "");
        controller.setRecipeStatusText(recipe.usedIngredientCount() + "/" + (recipe.missedIngredientCount() + recipe.usedIngredientCount()));

        pane.getChildren().add(item);

        recipes.put(recipe.id(), recipe);
    }

    private static void addSavedRecipe(Pane pane, Recipe recipe) throws IOException
    {
        FXMLLoader recipeLoader = new FXMLLoader(EasyDishApplication.class.getResource("saved-recipe.fxml"));
        StackPane item = recipeLoader.load();

        SavedRecipeController controller = recipeLoader.getController();
        controller.recipe = recipe;
        controller.setRecipeImage(validateImageURL(recipe.image()) ? recipe.image() : null);
        controller.setRecipeTitleText(recipe.title(), recipe.id());

        pane.getChildren().add(item);

        recipes.put(recipe.id(), recipe);
    }

    public static void addAllRecipes(boolean list) throws IOException
    {
        if (list)
        {
            if (searchController == null)
            {
                System.out.println("Search controller is not set!");
                return;
            }
            VBox scrollPaneVBox = searchController.scrollPaneVBox;
            scrollPaneVBox.getChildren().clear();
            for (Recipe recipe : recommendedRecipes)
                addListedRecipe(scrollPaneVBox, recipe);
        }
        else
        {
            if (bookController == null)
            {
                System.out.println("Book controller is not set!");
                return;
            }
            FlowPane scrollPaneFlowPane = bookController.scrollPaneFlowPane;
            for (Recipe recipe : savedRecipes)
                addSavedRecipe(scrollPaneFlowPane, recipe);
        }
    }


    public static void removeIngredient() throws IOException
    {
        if (bagController == null)
        {
            System.out.println("Bag controller is not set!");
            return;
        }

        FlowPane scrollPaneFlowPane = bagController.scrollPaneFlowPane;
        scrollPaneFlowPane.getChildren().clear();

        for (String ingredient : savedIngredients)
            addIngredient(scrollPaneFlowPane, ingredient);
    }

    private static void addIngredient(FlowPane scrollPaneFlowPane, String ingredient) throws IOException
    {
        FXMLLoader ingredientLoader = new FXMLLoader(EasyDishApplication.class.getResource("saved-ingredient.fxml"));
        StackPane item = ingredientLoader.load();

        SavedIngredientController controller = ingredientLoader.getController();
        controller.ingredient = ingredient;
        controller.setIngredientName(ingredient);

        scrollPaneFlowPane.getChildren().add(item);
    }

    public static void addSingleIngredient(String ingredient) throws IOException
    {
        if (bagController == null)
        {
            System.out.println("Bag controller is not set!");
            return;
        }
        FlowPane scrollPaneFlowPane = bagController.scrollPaneFlowPane;
        addIngredient(scrollPaneFlowPane, ingredient);
    }

    public static void addAllIngredients() throws IOException
    {
        if (bagController == null)
        {
            System.out.println("Bag controller is not set!");
            return;
        }
        FlowPane scrollPaneFlowPane = bagController.scrollPaneFlowPane;

        for (String ingredient : savedIngredients)
            addIngredient(scrollPaneFlowPane, ingredient);
    }

    public static void setRecipeInfo(List<RecipeNutrition> nutritions, RecipeInfo info, List<RecipeInstructions> instructions) throws IOException
    {
        if (recipeDisplayController == null)
        {
            System.out.println("RecipeDisplay controller is not set!");
            return;
        }
        recipeDisplayController.setRecipeImageView(validateImageURL(info.image()) ? info.image() : null);
        recipeDisplayController.setRecipeTitle(info.title());
        recipeDisplayController.setIngredientsTabTitle(recipes.get(info.id()).usedIngredientCount(), recipes.get(info.id()).missedIngredientCount());

        VBox informationScrollPaneVBox = recipeDisplayController.informationScrollPaneVBox;
        informationScrollPaneVBox.setSpacing(10);

        VBox ingredientsScrollPaneVBox = recipeDisplayController.ingredientsScrollPaneVBox;
        ingredientsScrollPaneVBox.setSpacing(10);

        VBox instructionsScrollPaneVBox = recipeDisplayController.instructionsScrollPaneVBox;
        instructionsScrollPaneVBox.setSpacing(10);


        for (RecipeNutrition nut : nutritions)
        {
            Text nutName = new Text(String.format("%s", nut.name()));
            nutName.getStyleClass().add("recipe-display-important");

            Text nutInfo = new Text(String.format(" - %.2f %s (%.2f%%)", nut.amount(), nut.unit(), nut.percentOfDailyNeeds()));
            nutInfo.getStyleClass().add("recipe-display-instructions-text");

            TextFlow textFlow = new TextFlow(nutName, nutInfo);
            textFlow.setMaxWidth(230);
            informationScrollPaneVBox.getChildren().add(textFlow);
        }
        for (Ingredient ing : info.extendedIngredients())
        {
            Text ingName = new Text(String.format("%s", ing.name()));
            ingName.getStyleClass().add("recipe-display-important");

            Text ingInfo = new Text(String.format(" - %.2f %s", ing.measures().us().amount(), ing.measures().us().unitShort()));
            ingInfo.getStyleClass().add("recipe-display-instructions-text");

            TextFlow textFlow = new TextFlow(ingName, ingInfo);
            textFlow.setMaxWidth(230);
            ingredientsScrollPaneVBox.getChildren().add(textFlow);
        }
        for (RecipeInstructions ins : instructions)
        {
            for (RecipeStep step : ins.steps())
            {
                Text stepNumber = new Text(String.format("Step %d", step.number()));
                stepNumber.getStyleClass().add("recipe-display-important");

                Text stepInfo = new Text(String.format(" - %s", step.step()));
                stepInfo.getStyleClass().add("recipe-display-instructions-text");

                TextFlow textFlow = new TextFlow(stepNumber, stepInfo);
                textFlow.setMaxWidth(230);
                instructionsScrollPaneVBox.getChildren().add(textFlow);
            }
        }
    }


    public static void searchRecipes()
    {
        String[] parameters = {"?ingredients=" + String.join(",", savedIngredients), "&number=" + generatedRecipeAmount, "&ranking=" + 1};
        String endpointExtension = "/recipes/findByIngredients" + String.join("", parameters) + "&apiKey=" + (setting.apiKey().isEmpty() ? API_KEY : setting.apiKey());

        try
        {
            String json = callAPI(endpointExtension);
            Gson gson = new Gson();
            Type recipeListType = new TypeToken<List<Recipe>>() {}.getType();
            ArrayList<Recipe> recipes = gson.fromJson(json, recipeListType);
            recommendedRecipes.clear();
            recommendedRecipes.addAll(recipes);
            addAllRecipes(true);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getRecipeInfo(int id)
    {
        String[] parameters;
        try
        {
            parameters = new String[]{(id + ""), "/information", "?includeNutrition=false"};
            String endpointExtension = "/recipes/" + String.join("", parameters) + "&apiKey=" + (setting.apiKey().isEmpty() ? API_KEY : setting.apiKey());
            String recipeInfoJson = callAPI(endpointExtension);

            parameters = new String[]{(String.valueOf(id)), "/analyzedInstructions", "?stepBreakdown=true"};
            endpointExtension = "/recipes/" + String.join("", parameters) + "&apiKey=" + (setting.apiKey().isEmpty() ? API_KEY : setting.apiKey());
            String instructionsJson = callAPI(endpointExtension);

            parameters = new String[]{(String.valueOf(id)), "/nutritionWidget.json"};
            endpointExtension = "/recipes/" + String.join("", parameters) + "?apiKey=" + (setting.apiKey().isEmpty() ? API_KEY : setting.apiKey());
            String nutritionsJson = callAPI(endpointExtension);

            Gson gson = new Gson();

            Type recipeInfoType = new TypeToken<RecipeInfo>() {}.getType();
            RecipeInfo recipeInfo = gson.fromJson(recipeInfoJson, recipeInfoType);

            Type instructionsType = new TypeToken<List<RecipeInstructions>>() {}.getType();
            List<RecipeInstructions> instructions = gson.fromJson(instructionsJson, instructionsType);

            JsonObject jsonObject = JsonParser.parseString(nutritionsJson).getAsJsonObject();
            JsonArray nutrientsArray = jsonObject.getAsJsonArray("nutrients");

            Type recipeNutritionType = new TypeToken<List<RecipeNutrition>>() {}.getType();
            List<RecipeNutrition> nutritions = gson.fromJson(nutrientsArray, recipeNutritionType);

            setRecipeInfo(nutritions, recipeInfo, instructions);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getIngredientInfo(int id)
    {
        String[] parameters = {(id + ""), "/information", "?amount=1"};
        String endpointExtension = "/food/ingredients/" + String.join("", parameters) + "&apiKey=" + API_KEY;

        try
        {
            String json = callAPI(endpointExtension);
            Gson gson = new Gson();
            Type ingredientInfoType = new TypeToken<IngredientInfo>() {}.getType();
            IngredientInfo ingredientInfo = gson.fromJson(json, ingredientInfoType);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String callAPI(String endpointExtension) throws IOException, InterruptedException
    {
        String urlString = BASE_URL + endpointExtension;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }


    public static void saveIngredients()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(SAVED_INGREDIENTS_FILE))
        {
            gson.toJson(savedIngredients, writer);
        }
        catch (IOException e)
        {
            System.out.println("File 'saved-ingredients.json' could not be written!");
        }
    }

    public static void readIngredients()
    {
        Gson gson = new Gson();
        Type recipeListType = new TypeToken<List<String>>() {}.getType();

        try (FileReader reader = new FileReader(SAVED_INGREDIENTS_FILE))
        {
            List<String> savedIngredientsList = gson.fromJson(reader, recipeListType);
            savedIngredients.addAll(savedIngredientsList);
        }
        catch (IOException e)
        {
            System.out.println("File 'saved-ingredients.json' could not be read!");
        }
    }

    public static void saveRecipes()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(SAVED_RECIPES_FILE))
        {
            gson.toJson(savedRecipes, writer);
        }
        catch (IOException e)
        {
            System.out.println("File 'saved-recipes.json' could not be written!");
        }
    }

    public static void readRecipes()
    {
        Gson gson = new Gson();
        Type recipeListType = new TypeToken<List<Recipe>>() {}.getType();

        try (FileReader reader = new FileReader(SAVED_RECIPES_FILE))
        {
            List<Recipe> savedRecipesList = gson.fromJson(reader, recipeListType);
            savedRecipes.addAll(savedRecipesList);
        }
        catch (IOException e)
        {
            System.out.println("File 'saved-recipes.json' could not be read!");
        }
    }

    public static void saveSettings()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(SAVED_SETTINGS_FILE))
        {
            gson.toJson(setting, writer);
        }
        catch (IOException e)
        {
            System.out.println("File 'saved-settings.json' could not be written!");
        }
    }

    public static void readSettings()
    {
        Gson gson = new Gson();
        Type settingType = new TypeToken<Setting>() {}.getType();

        try (FileReader reader = new FileReader(SAVED_SETTINGS_FILE))
        {
            setting = gson.fromJson(reader, settingType);
            System.out.println(setting);
        }
        catch (IOException e)
        {
            System.out.println("File 'saved-settings.json' could not be read!");
        }
    }

    public static void setSearchController(Search controller)
    {
        searchController = controller;
    }

    public static void setBagController(Bag controller)
    {
        bagController = controller;
    }

    public static void setBookController(Book controller)
    {
        bookController = controller;
    }

    public static void setCalendarController(Calendar controller)
    {
        calendarController = controller;
    }

    public static void setSettingsController(Settings controller)
    {
        settingsController = controller;
    }

    public static void setRecipeInfoController(RecipeDisplay controller)
    {
        recipeDisplayController = controller;
    }

    private static boolean validateImageURL(String imageURL) throws IOException
    {
        URI uri = URI.create(imageURL);
        URL u = uri.toURL();
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        int code = huc.getResponseCode();
        return code == 200;
    }

    public static void main(String[] args)
    {
        readIngredients();
        readRecipes();
        readSettings();
        launch();
        saveIngredients();
        saveRecipes();
        saveSettings();
    }
}
