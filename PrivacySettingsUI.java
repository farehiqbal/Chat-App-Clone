
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Collections.User;
import SessionManager.Session;

public class PrivacySettingsUI extends Application {

    private User loggedInUser= Session.getLoggedInUser(); // The logged-in user object


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Privacy Settings");

        // Create a grid pane for the settings form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(15);
        gridPane.setHgap(20);

        // Fetch the privacy settings from the database
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> settingsCollection = database.getCollection("Privacy_Settings");
        FindIterable<Document> settingsQuery = settingsCollection.find(new Document("user_id", loggedInUser.getUserId()));
        Document privacySettings = settingsQuery.first();

        // Get the visibility values as integers
        int dpVisibility = privacySettings.getInteger("dp_visibility");
        int activeVisibility = privacySettings.getInteger("active_visibility");
        int lastSeenVisibility = privacySettings.getInteger("last_seen_visiblity");

        // Convert the integer values to booleans
        boolean dpVisibilityValue = (dpVisibility == 1);
        boolean activeVisibilityValue = (activeVisibility == 1);
        boolean lastSeenVisibilityValue = (lastSeenVisibility == 1);

        // Add DP visibility checkbox
        CheckBox dpVisibilityCheckBox = new CheckBox("Display Picture Visibility");
        dpVisibilityCheckBox.setSelected(dpVisibilityValue);
        GridPane.setConstraints(dpVisibilityCheckBox, 0, 0);

        // Add active visibility checkbox
        CheckBox activeVisibilityCheckBox = new CheckBox("Active Status Visibility");
        activeVisibilityCheckBox.setSelected(activeVisibilityValue);
        GridPane.setConstraints(activeVisibilityCheckBox, 0, 1);

        // Add last seen visibility checkbox
        CheckBox lastSeenVisibilityCheckBox = new CheckBox("Last Seen Visibility");
        lastSeenVisibilityCheckBox.setSelected(lastSeenVisibilityValue);
        GridPane.setConstraints(lastSeenVisibilityCheckBox, 0, 2);

        // Add update button
        Button updateButton = new Button("Update");
        GridPane.setConstraints(updateButton, 0, 3);

        // Handle update button click event
        updateButton.setOnAction(e -> {
            // Get the updated visibility values
            int updatedDpVisibility = (dpVisibilityCheckBox.isSelected()) ? 1 : 0;
            int updatedActiveVisibility = (activeVisibilityCheckBox.isSelected()) ? 1 : 0;
            int updatedLastSeenVisibility = (lastSeenVisibilityCheckBox.isSelected()) ? 1 : 0;

            // Update the privacy settings in the database
            privacySettings.put("dp_visibility", updatedDpVisibility);
            privacySettings.put("active_visibility", updatedActiveVisibility);
            privacySettings.put("last_seen_visiblity", updatedLastSeenVisibility);
            settingsCollection.replaceOne(new Document("_id", privacySettings.getObjectId("_id")), privacySettings);

            // Show a confirmation message or perform any other action

            // Close the privacy settings window
            primaryStage.close();
        });

        // Add components to the grid pane
        gridPane.getChildren().addAll(dpVisibilityCheckBox, activeVisibilityCheckBox, lastSeenVisibilityCheckBox, updateButton);

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(getClass().getResource("css\\PrivacySettings.css").toExternalForm());
        // lets set the window size
        primaryStage.setWidth(350);
        primaryStage.setHeight(200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   
    public static void main(String[] args) {
        launch(args);
    }
}
