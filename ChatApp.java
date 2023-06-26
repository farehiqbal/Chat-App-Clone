
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import Collections.Community;
import Collections.Group;
import Collections.MessageList;
import Collections.User;
import Collections.UserCommunities;
import Collections.UserContacts;
import Collections.UserGroupMessagesList;
import Collections.UserGroups;
import Collections.Message;
import SessionManager.Session;

import com.mongodb.*;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatApp extends Application {

    private User loggedInUser;
    private TextArea messageArea;
    private TextField messageInput;
    private VBox chatBox;
    ListView<User> contactsList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat App");

        // lets connect to exising database called ChatApp
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");


        if(Session.isLoggedIn()) {
            
           loggedInUser = Session.getLoggedInUser();
            System.out.println(loggedInUser.getName());
        } 




        BorderPane root = new BorderPane();

        // Create a VBox for the side panel (contacts)
        BorderPane sidePanel = new BorderPane();
        
        sidePanel.setPrefWidth(270);
        sidePanel.setPadding(new Insets(10));
        sidePanel.setStyle("-fx-background-color: #2C2F33");

        // Create a ListView for displaying contacts
       
       // lets make a new vertical panel that will have some UI buttons such as settings, story, etc,
       // it should be on the left most side and vertical

        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(8);
        leftPanel.setPadding(new Insets(2));
        leftPanel.setStyle("-fx-background-color: #2C2F33");
        leftPanel.setSpacing(10);

        // add some ui buttons to the left panel
        Button settingsButton = new Button("");
        settingsButton.setStyle("-fx-background-color: #2C2F33; -fx-text-fill: white;");
        Image settingsImage = new Image(getClass().getResourceAsStream("Images\\settings2.png"));
        ImageView settingsImageView = new ImageView(settingsImage);
        settingsImageView.setFitWidth(24);
        settingsImageView.setFitHeight(24);
        settingsImageView.setPreserveRatio(true);
        settingsImageView.setOpacity(1);

        settingsButton.setGraphic(settingsImageView);

        settingsButton.setOnAction(e -> {
            PrivacySettingsUI privacySettingsGUI = new PrivacySettingsUI();
            privacySettingsGUI.start(new Stage());
        });
        


        Button storyButton = new Button();
        storyButton.setStyle("-fx-background-color: #2C2F33; -fx-text-fill: white;");
        Image storyImage = new Image(getClass().getResourceAsStream("Images\\story.png"));
        ImageView storyImageView = new ImageView(storyImage);
        storyImageView.setFitWidth(24);
        storyImageView.setFitHeight(24);
        storyImageView.setPreserveRatio(true);
        storyImageView.setOpacity(1);

        storyButton.setGraphic(storyImageView);


        Button subscriptionButton = new Button();
        subscriptionButton.setStyle("-fx-background-color: #2C2F33; -fx-text-fill: white;");
        Image subscriptionImage = new Image(getClass().getResourceAsStream("Images\\pro.png"));
        ImageView subscriptionImageView = new ImageView(subscriptionImage);
        subscriptionImageView.setFitWidth(24);
        subscriptionImageView.setFitHeight(24);
        subscriptionImageView.setPreserveRatio(true);
        subscriptionImageView.setOpacity(1);

        subscriptionButton.setGraphic(subscriptionImageView);

        Button linkedDevicesButton = new Button();
        linkedDevicesButton.setStyle("-fx-background-color: #2C2F33; -fx-text-fill: white;");
        Image linkedDevicesImage = new Image(getClass().getResourceAsStream("Images\\linkeddevices.png"));
        ImageView linkedDevicesImageView = new ImageView(linkedDevicesImage);
        linkedDevicesImageView.setFitWidth(24);
        linkedDevicesImageView.setFitHeight(24);
        linkedDevicesImageView.setPreserveRatio(true);
        linkedDevicesImageView.setOpacity(1);

        linkedDevicesButton.setGraphic(linkedDevicesImageView);


        Button reportedUsersButton = new Button();
        reportedUsersButton.setStyle("-fx-background-color: #2C2F33; -fx-text-fill: white;");
        Image reportedUsersImage = new Image(getClass().getResourceAsStream("Images\\rep.png"));
        ImageView reportedUsersImageView = new ImageView(reportedUsersImage);
        reportedUsersImageView.setFitWidth(24);
        reportedUsersImageView.setFitHeight(24);
        reportedUsersImageView.setPreserveRatio(true);
        reportedUsersImageView.setOpacity(1);

        reportedUsersButton.setGraphic(reportedUsersImageView);
        




        VBox.setVgrow(storyButton, Priority.NEVER);
        VBox.setVgrow(settingsImageView, Priority.ALWAYS);

        leftPanel.setAlignment(Pos.BOTTOM_CENTER);
        leftPanel.setSpacing(10);
        leftPanel.getChildren().addAll(reportedUsersButton ,linkedDevicesButton ,subscriptionButton ,storyButton, settingsButton);

        contactsList = UserContacts.getContacts(loggedInUser);
        contactsList.setPrefHeight(500);
        contactsList.setStyle("-fx-control-inner-background: #2C2F33; -fx-background-insets: 0;");
        
        sidePanel.setLeft(leftPanel);
        sidePanel.setCenter(contactsList);


        // Create a VBox for the chat area
        chatBox = new VBox();
        chatBox.setPadding(new Insets(10));
        chatBox.setSpacing(10);
        chatBox.setStyle("-fx-background-color: #23272A");

        // Create a HBox for the top bar (chat types)
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setStyle("-fx-background-color: #23272A");

        // Create ToggleButtons for chat types
        ToggleButton contactsButton = new ToggleButton("Contacts");
        ToggleButton groupsButton = new ToggleButton("Groups");
        ToggleButton communitiesButton = new ToggleButton("Communities");
        contactsButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");
        groupsButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");
        communitiesButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");

        Button newContactButton = new Button("+");

        newContactButton.setStyle("-fx-background-color: #a1a4ad; -fx-text-fill: white;");
        newContactButton.setPrefWidth(30);
        newContactButton.setPrefHeight(26);
        newContactButton.setPadding(new Insets(0));
        newContactButton.setOpacity(1);
        // newContactButton.setGraphicTextGap(0);
        newContactButton.setFont(new Font("Arial", 20));


        //lets add a text label to the top bar
        Label nameLabel = new Label(loggedInUser.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;");
        nameLabel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // Create a HBox for the top bar (contacts, groups, communities, name)

        Label messageCount = new Label("Message Count: " + contactsList.getItems().size());
        messageCount.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");



        // Add the ToggleButtons to the top bar
        topBar.getChildren().addAll(contactsButton, groupsButton, communitiesButton, newContactButton,messageCount, spacer,nameLabel);
        topBar.setSpacing(10);

        // actionListener for newContactButton
        newContactButton.setOnAction(e -> {
            NewContactDialog.display();
        });


        contactsButton.setOnAction(event -> {
           
            if(contactsButton.isSelected()){
                
                contactsList.setPrefHeight(500);
                contactsList.setStyle("-fx-control-inner-background: #2C2F33; -fx-background-insets: 0;");

                contactsList.setCellFactory(lv -> new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (empty || user == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(user.getName());
                            ImageView imageView = new ImageView(new Image(user.getProfilePicture()));
                            imageView.setFitWidth(60);
                            imageView.setFitHeight(60);
                            setGraphic(imageView);
                            setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");
        
                        }
                        setStyle("-fx-text-fill: white;");
                    }
                });
                sidePanel.setCenter(contactsList);
            }else{

            }
    
          
        });

        ObjectProperty<User> selectedUserProperty = new SimpleObjectProperty<>();
        ObjectProperty<Message> selectedMessageProperty = new SimpleObjectProperty<>();


        contactsList.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(user.getName());
                    ImageView imageView = new ImageView(new Image(user.getProfilePicture()));
                    imageView.setFitWidth(60);
                    imageView.setFitHeight(60);
                    setGraphic(imageView);
                    setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");
                    
                    // Add mouse click event handler
                    setOnMouseClicked(event -> {
                        // Handle user selection
                        selectedUserProperty.set(user);
                        System.out.println(user.getName());

                        List<Message> messages = MessageList.getContactMessages(selectedUserProperty.get());
                        // sort messages by message_id
                        // refresh the top bar message count
                        messageCount.setText("Message Count: " + messages.size());
                        messages.sort(Comparator.comparing(Message::getMessageId));

                        System.out.println(messages.size());
                        
                        //lets display message in message area
                        chatBox.getChildren().clear();
                        for(Message message : messages){

                            Label messageLabel;
                            if (message.getSenderId() == loggedInUser.getUserId()) {

                                //
                                messageLabel = new Label( "You: "+ message.getContent());
                                messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
                                messageLabel.setPadding(new Insets(10));
                                messageLabel.setWrapText(true);
                                messageLabel.setMaxWidth(300);
                                messageLabel.setMinWidth(300);
                                messageLabel.setPrefWidth(300);
                                // lets make the labels rounded corners
                                
                                chatBox.getChildren().add(messageLabel);
                                // lets move these"you" messages to the right side of the chat box
                               

                            } else {
                                // lets display these messages in the right side of the chat box
                                messageLabel = new Label( message.getContent());

                                messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
                                messageLabel.setPadding(new Insets(10));
                                messageLabel.setWrapText(true);
                                messageLabel.setMaxWidth(300);
                                messageLabel.setMinWidth(300);
                                messageLabel.setPrefWidth(300);
                                // messageLabel.setAlignment(Pos.CENTER_LEFT);
                                // Region spacer = new Region();
                                // HBox.setHgrow(spacer, Priority.ALWAYS);

                                

                                chatBox.getChildren().add(messageLabel);
                            }

                            messageLabel.setOnMouseClicked(mouseevent -> {
                                // Set the selected message
                                selectedMessageProperty.set(message);
                                // Display options for editing and deleting the message
                                displayMessageOptions(selectedMessageProperty.get());
                            });
                           
                        }
                    });
                }
                setStyle("-fx-text-fill: white;");
            }
        });

        ObjectProperty<Group> selectedGroupProperty = new SimpleObjectProperty<>();
        ListView<Group> groupsList = UserGroups.getGroups(loggedInUser);
        System.out.println(groupsList.getItems().size());

        groupsButton.setOnAction(event -> {
            if (groupsButton.isSelected()) {
                // Toggle button is selected, show groups
                
                groupsList.setPrefHeight(500);
                groupsList.setStyle("-fx-control-inner-background: #2C2F33; -fx-background-insets: 0;");
                // Display the groups

                groupsList.setCellFactory(lv -> new ListCell<Group>() {
                    @Override
                    protected void updateItem(Group group, boolean empty) {
                        super.updateItem(group, empty);
                        if (empty || group == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(group.getGroupName());
                            ImageView imageView = new ImageView(new Image(group.getProfile_picture()));
                            imageView.setFitWidth(60);
                            imageView.setFitHeight(60);
                            setGraphic(imageView);
                            setStyle("-fx-background-color: #f0f2f5; -fx-padding: 5;");

        
                        }
                        setStyle("-fx-text-fill: white;");
                    }

                });
                
                sidePanel.setCenter(groupsList);
            } else {
                // 
                
            }
        });

        groupsList.setOnMouseClicked(event -> {
            selectedUserProperty.set(null);
            // Handle group selection
            Group selectedGroup = groupsList.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                selectedGroupProperty.set(selectedGroup);
                System.out.println(selectedGroup.getGroupName());
        
                List<Message> messages = UserGroupMessagesList.getGroupMessages(selectedGroupProperty.get());
                
                messageCount.setText("Message Count: " + messages.size());
                messages.sort(Comparator.comparing(Message::getMessageId));


                chatBox.getChildren().clear();
                for (Message message : messages) {
                    if (message.getSenderId() == loggedInUser.getUserId()) {
                        Label messageLabel = new Label("You: " + message.getContent());
                        messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
                        messageLabel.setPadding(new Insets(10));
                        messageLabel.setWrapText(true);
                        messageLabel.setMaxWidth(300);
                        messageLabel.setMinWidth(300);
                        messageLabel.setPrefWidth(300);
        
                        chatBox.getChildren().add(messageLabel);
                    } else {
                        Label messageLabel = new Label(message.getSenderId() + ": " + message.getContent());
                        messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
                        messageLabel.setPadding(new Insets(10));
                        messageLabel.setWrapText(true);
                        messageLabel.setMaxWidth(300);
                        messageLabel.setMinWidth(300);
                        messageLabel.setPrefWidth(300);
        
                        chatBox.getChildren().add(messageLabel);
                    }
                }
            }
        });
        
    
        
        // Create a VBox for the chat area
        chatBox.setPadding(new Insets(10));
        chatBox.setSpacing(30);
        chatBox.setStyle("-fx-background-color: #23272A");

        // Create a TextArea for displaying the messages
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setStyle("-fx-control-inner-background: #2C2F33; -fx-text-fill: white;");

        // Create a HBox for the bottom bar (message input and send button)
        HBox bottomBar = new HBox();
        bottomBar.setSpacing(0);
        bottomBar.setAlignment(Pos.BOTTOM_LEFT);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 0, 10, 0));

        
        // Create a TextField for entering messages
        messageInput = new TextField();
        // messageInput.setOnAction(e -> sendMessage());
        messageInput.setStyle("-fx-control-inner-background: #2C2F33; -fx-text-fill: white;");
        // messageInput.setPrefWidth(200);
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        // Create a Button for sending messages
        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> {
            if (selectedUserProperty.get() != null) {
                sendContactMessage(selectedUserProperty);
                System.out.println("Sending message to user");
            } else if (selectedGroupProperty.get() != null) {
                sendGroupMessage(selectedGroupProperty);
                System.out.println("Sending message to group");
            }else{
                System.out.println("No user or group selected");
            }
        });
        

        sendButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white;");


        // Add the message area and bottom bar to the chat box
        chatBox.getChildren().addAll(messageArea, bottomBar);
        bottomBar.getChildren().addAll(messageInput, sendButton);


        //set bottombar to the bottom of the chatbox
        VBox.setVgrow(chatBox, Priority.ALWAYS);


        BorderPane wholeChat = new BorderPane();
        wholeChat.setCenter(chatBox);
        wholeChat.setBottom(bottomBar);
   

        // Set the side panel, chat box, and top bar in the root layout
        root.setLeft(sidePanel);
        root.setCenter(wholeChat);
        root.setTop(topBar);
        // root.setBottom(wholeChat);
        

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("css\\darktheme.css"); // Load external CSS file for dark theme

        primaryStage.setScene(scene);
        primaryStage.show();

 
        
        User selectedUser = selectedUserProperty.get();
        if (selectedUser != null) {
            // Perform actions with the selected user
            System.out.println(selectedUser.toString());
        } else {
            System.out.println("No user selected.");
        }


        sidePanel.setCenter(contactsList);

    }


    private void sendContactMessage( ObjectProperty<User> selectedUserProperty) {
        // Get the content of the message from your input field
        String messageContent = messageInput.getText();
        
        User Recipient =  selectedUserProperty.get(); 

        // Create a unique ID for the message, by finding the highest ID in the Message collection and incrementing it by one
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> messageCollection = database.getCollection("Message");
        
        // Find the highest ID in the Message collection and increment it by one
        int messageId = 1;
        FindIterable<Document> results = messageCollection.find().sort(Sorts.descending("message_id")).limit(1);
        
        if (results.first() != null) {
            messageId = results.first().getInteger("message_id") + 1;
        }

        // Create a new instance of the Message class with the necessary information
        Message newMessage = new Message(messageId, loggedInUser.getUserId(), Recipient.getUserId(), "user", messageContent, LocalDateTime.now().toString());
        
        System.out.println(newMessage.toString());
        // Store the new message in the Message collection (replace this with your MongoDB logic)
        storeMessageInDatabase(newMessage);
    
        // Clear the input field after sending the message
        messageInput.clear();

                // Retrieve the updated list of messages
        List<Message> updatedMessages = MessageList.getContactMessages(selectedUserProperty.get());
        // Sort the updated messages
        updatedMessages.sort(Comparator.comparing(Message::getMessageId));

        // Clear the message area
        chatBox.getChildren().clear();

        // Iterate over the updated messages and display them
        for (Message message : updatedMessages) {
            Label messageLabel;
            if (message.getSenderId() == loggedInUser.getUserId()) {
                messageLabel = new Label("You: " + message.getContent());
                messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
            } else {
                messageLabel = new Label(message.getContent());
                messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
            }
            messageLabel.setPadding(new Insets(10));
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(300);
            messageLabel.setMinWidth(300);
            messageLabel.setPrefWidth(300);
            chatBox.getChildren().add(messageLabel);
        }

    }

    private void sendGroupMessage(ObjectProperty<Group> selectedGroupProperty) {
        // Get the content of the message from your input field
        String messageContent = messageInput.getText();
        
        Group group = selectedGroupProperty.get();
        
        // Create a unique ID for the message, by finding the highest ID in the Message collection and incrementing it by one
        int messageId = 1;
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> messageCollection = database.getCollection("Message");
        
        FindIterable<Document> results = messageCollection.find().sort(Sorts.descending("message_id")).limit(1);
        
        if (results.first() != null) {
            messageId = results.first().getInteger("message_id") + 1;
        }
        
        // Create a new instance of the Message class with the necessary information
        Message newMessage = new Message(messageId, loggedInUser.getUserId(), group.getGroupId(), "group", messageContent, LocalDateTime.now().toString());
        
        // Store the new message in the Message collection (replace this with your MongoDB logic)
        storeMessageInDatabase(newMessage);
        
        // Clear the input field after sending the message
        messageInput.clear();
        
        // Retrieve the updated list of messages
        List<Message> updatedMessages = UserGroupMessagesList.getGroupMessages(selectedGroupProperty.get());
        // Sort the updated messages
        updatedMessages.sort(Comparator.comparing(Message::getMessageId));
        
        // Clear the message area
        chatBox.getChildren().clear();
        
        // Iterate over the updated messages and display them
        for (Message message : updatedMessages) {
            Label messageLabel;
            if (message.getSenderId() == loggedInUser.getUserId()) {
                messageLabel = new Label("You: " + message.getContent());
                messageLabel.setStyle("-fx-background-color: #484b54; -fx-text-fill: white; -fx-background-radius: 10px;");
            } else {
                messageLabel = new Label(message.getSenderId() + ": " + message.getContent());
                messageLabel.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-background-radius: 10px;");
            }
            messageLabel.setPadding(new Insets(10));
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(300);
            messageLabel.setMinWidth(300);
            messageLabel.setPrefWidth(300);
            chatBox.getChildren().add(messageLabel);
        }
    }
    




    private void storeMessageInDatabase(Message message) {
        // Replace this with your MongoDB logic to store the message in the Message collection
        // You can use a MongoDB driver or an ORM library like Spring Data MongoDB
        // Example code to store the message:
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> messageCollection = database.getCollection("Message");
    
        // Create a new document for the message
        Document messageDocument = new Document()
            .append("message_id", message.getMessageId())
            .append("sender_id", message.getSenderId())
            .append("recipient_id", message.getReceiverId())
            .append("recipient_type", message.getRecipientType())
            .append("timestamp", message.getTimestamp())
            .append("content", message.getContent());
    
        // Insert the document into the Message collection
        messageCollection.insertOne(messageDocument);
    
        // Close the MongoDB client connection
        mongoClient.close();
    }
    
    private void displayMessageOptions(Message selectedMessage) {
        // Get the selected message
        // Message selectedMessage = selectedMessageProperty.get();
        if (selectedMessage == null) {
            return; // No message selected, do nothing
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String css = getClass().getResource("darktheme.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.setTitle("Message Options");
        alert.setHeaderText("Choose an option for the message:");
        alert.setContentText(selectedMessage.getContent());

    
        ButtonType editButton = new ButtonType("Edit");
        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    
        alert.getButtonTypes().setAll(editButton, deleteButton, cancelButton);
    
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == editButton) {
                // Handle the edit option
                editMessage(selectedMessage);
            } else if (result.get() == deleteButton) {
                // Handle the delete option
                deleteMessage(selectedMessage);
            }
        }
    }

    private void editMessage(Message message) {
        // Implement the logic to allow the user to edit the message
    
        TextInputDialog dialog = new TextInputDialog(message.getContent());
        dialog.setTitle("Edit Message");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the new message:");
        // input field?


        String css = getClass().getResource("darktheme.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(css);

        Optional<String> result = dialog.showAndWait();
    
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> messageCollection = database.getCollection("Message");

        // lets find that message in our database
        for (Document doc: messageCollection.find()){
            if (doc.getInteger("message_id") == message.getMessageId()) {
                // update the message
                messageCollection.updateOne(doc, new Document("$set", new Document("content", result.get())));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message Edited");
                alert.setHeaderText(null);
                alert.setContentText("Message has been edited");
                alert.showAndWait();

            }
        }

    }
    
    private void deleteMessage(Message message) {
        
        // lets just set the conent to ""
        // message.setContent("");
        // Update the message in the database or data source
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ChatApp");
        MongoCollection<Document> messageCollection = database.getCollection("Message");

       // lets find that message in our database
        for (Document doc : messageCollection.find()) {
            if (doc.getInteger("message_id") == message.getMessageId()) {
                // delete the message
                messageCollection.deleteOne(doc);
            }
        }

        mongoClient.close();

    }


    



    public static void main(String[] args) {
        launch(args);
    }

}   