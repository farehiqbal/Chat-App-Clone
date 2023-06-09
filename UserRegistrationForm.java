import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Collections.Login;
import Collections.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UserRegistrationForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Registration");

        // Create a grid pane for the registration form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(15);
        gridPane.setHgap(27);

        // Add name label and text field
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);

        // Add bio label and text area
        Label bioLabel = new Label("Bio:");
        GridPane.setConstraints(bioLabel, 0, 1);
        TextArea bioInput = new TextArea();
        bioInput.setPrefRowCount(3);
        bioInput.setEditable(true);
        GridPane.setConstraints(bioInput, 1, 1);

        // Add preferred language label and choice box
        Label languageLabel = new Label("Preferred Language:");
        GridPane.setConstraints(languageLabel, 0, 2);
        ChoiceBox<String> languageChoiceBox = new ChoiceBox<>();
        languageChoiceBox.getItems().addAll("English", "Spanish", "French", "German");
        languageChoiceBox.setValue("English");
        GridPane.setConstraints(languageChoiceBox, 1, 2);

        // Add register button
        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 3);


        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> userCollection = database.getCollection("User");
        Document highestUser = userCollection.find().sort(new Document("user_id", -1)).first();
        int maxUserId = highestUser != null ? highestUser.getInteger("user_id") : 0;

        MongoCollection<Document> loginCollection = database.getCollection("Login");
        Document highestLogin = loginCollection.find().sort(new Document("user_id", -1)).first();
        int maxLoginId = highestLogin != null ? highestLogin.getInteger("user_id") : 0;





        System.out.println(maxUserId);


        // Add event handler to handle register button click
        registerButton.setOnAction(e -> {

            int id = maxUserId + 1;
            String name = nameInput.getText();
            String bio = bioInput.getText();
            String language = languageChoiceBox.getValue();
            String profile_picture = "https://robohash.org/" + name + ".png";
            String preferred_language = languageChoiceBox.getValue();
            String created_at = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());


            // Create a new user object
            User user = new User(id, name, profile_picture,bio, preferred_language, created_at);

            // Insert the user into the database
            Document newUser = new Document("user_id", user.getUserId())
                    .append("name", user.getName())
                    .append("bio", user.getBio())
                    .append("profile_picture", user.getProfilePicture())
                    .append("preferred_language", user.getPreferredLanguage())
                    .append("created_at", user.getCreatedAt());

            Login login = new Login(maxLoginId + 1, user.getUserId(), user.getName(), user.getName()+"123");
            
            Document newLogin = new Document("login_id", login.getLoginId())
                    .append("user_id", login.getUserId())
                    .append("username", login.getUsername())
                    .append("password", login.getPassword());
            
            userCollection.insertOne(newUser);
            loginCollection.insertOne(newLogin);


            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
                    "User registration successful. Name: " + name);
            
            // display the Home page
            Home homePage = new Home();
            homePage.start(primaryStage);

        });

        // Add components to the grid pane
        gridPane.getChildren().addAll(nameLabel, nameInput, bioLabel, bioInput,
                languageLabel, languageChoiceBox, registerButton);

        // Create a vertical box to hold the grid pane
        VBox vbox = new VBox(gridPane);
        vbox.setStyle("-fx-background-color: #222; -fx-padding: 20;");

        Scene scene = new Scene(vbox, 700, 350);
        scene.getStylesheets().add(getClass().getResource("UserRegistrationForm.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
