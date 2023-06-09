import java.util.List;

import Collections.Group;
import Collections.MessageList;
import Collections.User;
import Collections.UserContacts;
import Collections.UserGroups;
import Collections.Message;
import javafx.application.Application;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TestMain extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        User loggedInUser = new User(1, "John Doe", "https://www.google.com");
        
        List<Message> messages = MessageList.getContactMessages(loggedInUser);
        ListView<User> contacts = UserContacts.getContacts(loggedInUser);
        ListView<Group> groups = UserGroups.getGroups(loggedInUser);

        //loop thorugh and print all messages
        for (Message message : messages) {
            System.out.println(message.getContent());
        }
    }   
    
    public static void main(String[] args) {
        launch(args);
    }
}

