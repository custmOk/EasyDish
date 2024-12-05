package org.easydish.easydish;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class Book
{
    @FXML
    FlowPane scrollPaneFlowPane;
    @FXML
    TextField recipeNameTextField;

    private List<Node> allItems = new ArrayList<>();

    @FXML
    public void initialize()
    {
        Platform.runLater(() -> allItems.addAll(scrollPaneFlowPane.getChildren()));
    }

    public void searchButton(ActionEvent e)
    {
        String searchText = recipeNameTextField.getText().toLowerCase();

        List<Node> filteredItems = new ArrayList<>();
        for (Node node : allItems)
        {
            if (node instanceof StackPane stackPane)
            {
                for (Node child : stackPane.getChildren())
                {
                    if (child instanceof HBox hBox)
                    {
                        for (Node hBoxChild : hBox.getChildren())
                        {
                            if (hBoxChild instanceof Label label)
                            {
                                if (label.getText().toLowerCase().contains(searchText))
                                {
                                    filteredItems.add(node);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        scrollPaneFlowPane.getChildren().setAll(filteredItems);
    }
}
