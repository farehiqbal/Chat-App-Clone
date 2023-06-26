
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import SessionManager.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewContactDialog {
    public static void display() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Create New Contact");
        // dialogStage.set
        // Create labels and text fields
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        
        // Create a button to create the new contact
        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            String contactName = nameTextField.getText();
            
            // setup the database connection
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("ChatApp");
            MongoCollection<Document> userCollection = database.getCollection("User");
            MongoCollection<Document> contactCollection = database.getCollection("User_Contacts");

            // find the entered username in the database, if it exisists
            Document requiredContact = userCollection.find(new Document("name", contactName)).first();
            
            // find the next available contact id
            int contactId = 1;
            com.mongodb.client.FindIterable<Document> results = contactCollection.find().sort(Sorts.descending("contact_id")).limit(1);
            if (results.first() != null) {
                contactId = results.first().getInteger("contact_id") + 1;
            }
            
            System.out.println(contactId);
            
            if (requiredContact != null) {
                // if the contact exists, add it to the current user's contact list
                // and close the dialog

                // create a new document to insert into the database
                Document newContact = new Document("user_contact_id", contactId)
                        .append("user_id", Session.getLoggedInUser().getUserId())
                        .append("contact_id", requiredContact.getInteger("user_id"))
                        .append("contact_date", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                



                // there should alse be a reverse entry in the database
                Document reverseContact = new Document("user_contact_id", contactId + 1)
                        .append("user_id", requiredContact.getInteger("user_id"))
                        .append("contact_id", Session.getLoggedInUser().getUserId())
                        .append("contact_date", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                // insert the document into the database
                contactCollection.insertOne(newContact);
                contactCollection.insertOne(reverseContact);
                
                // display ok message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Contact Created");
                alert.setHeaderText("Contact Created");
                alert.setContentText("The contact has been created successfully.");
                alert.showAndWait();

            } else{

                // display error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Contact Not Found");
                alert.setHeaderText("Contact Not Found");
                alert.setContentText("The contact you entered does not exist.");
                alert.showAndWait();
            }



            // Close the dialog
            dialogStage.close();
        });
        
        // Create a grid pane and set the spacing and padding
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10));
        
        // Add the labels and text fields to the grid pane
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);
        
        // Add the create button to the grid pane
        gridPane.add(createButton, 1, 1);
        
        // Set the alignment of the grid pane
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getStylesheets().add(NewContactDialog.class.getResource("css\\darktheme.css").toExternalForm());
        // Create a scene with the grid pane and set it to the dialog stage
        Scene scene = new Scene(gridPane);
        dialogStage.setScene(scene);
        
        scene.getStylesheets().add(NewContactDialog.class.getResource("css\\darktheme.css").toExternalForm());
        // Show the dialog
        dialogStage.showAndWait();
    }
}
