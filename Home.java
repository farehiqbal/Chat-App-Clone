
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Collections.User;
import SessionManager.Session;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Home extends Application {

    public User isAuthenticated = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Panel");

        // Create a grid pane for the login form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        // Add username label and text field
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        // Add password label and password field
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);

        // Add login button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        // Add event handler to handle login button click
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            // Authenticate user against the User collection in MongoDB
            isAuthenticated = authenticateUser(username, password);
            System.out.println(isAuthenticated.toString());
            if (isAuthenticated != null) {
                // If authentication is successful, show the main app window
                // System.out.println(isAuthenticated.toString());
                // Alert alert = new Alert(Alert.AlertType.INFORMATION);
                // alert.setTitle("Login Successful");
                // alert.setHeaderText(null);
                // alert.setContentText("Welcome " + isAuthenticated.getBio());
                ChatApp chatApp = new ChatApp();

                chatApp.start(primaryStage);
            } else {
                // If authentication fails, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
        });

        Button registerButton = new Button("Register");
        // place register button on the right to login button
        GridPane.setConstraints(registerButton, 0, 2);

        registerButton.setOnAction(e -> {
            UserRegistrationForm register = new UserRegistrationForm();
            register.start(primaryStage);
        });

        // Add components to the grid pane
        gridPane.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, registerButton);

        Scene scene = new Scene(gridPane, 300, 150);
        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private User authenticateUser(String username, String password) {
        // Perform authentication logic using the User collection in MongoDB
        // Replace this code with your MongoDB authentication logic
        // Query the User collection for the provided username and password
        // Return true if a matching user document is found, false otherwise
        // You can use a MongoDB driver or an ORM library like Spring Data MongoDB

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> userCollection = database.getCollection("Login");

        // Query the User collection for the provided username and password
        Document query = new Document("username", username).append("password", password);
        FindIterable<Document> result = userCollection.find(query);

        // Check if a matching user document is found
        Document userDocument = result.first();
        if (userDocument != null) {
            // Retrieve the user information from the document
            int userId = userDocument.getInteger("user_id");

            // our login table gives us the user_id, so we can use that to get the user's name
            MongoCollection<Document> userCollection2 = database.getCollection("User");
            Document query2 = new Document("user_id", userId);
            FindIterable<Document> result2 = userCollection2.find(query2);
            Document userDocument2 = result2.first();
            String name = userDocument2.getString("name");
            String profilePicture = userDocument2.getString("profile_picture");
            String bio = userDocument2.getString("bio");
            String preferredLanguage = userDocument2.getString("preferred_language");
            String createdAt = userDocument2.getString("created_at");

            // Create a User object
            User loggedInUser = new User(userId, name, profilePicture, bio, preferredLanguage, createdAt);
            // lets handle the session here
            Session.setLoggedInUser(loggedInUser);
           

            // Return the logged-in user object
            return loggedInUser;
        } else {
            // Authentication failed, return null
            return null;
        }

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
