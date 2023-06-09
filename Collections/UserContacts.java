package Collections;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import SessionManager.Session;
import javafx.scene.control.ListView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserContacts {
    // private static User loggedInUser;
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    

    public static ListView<User> getContacts(User loggedInUser) {

        // if(Session.isLoggedIn()) {
            
        //     loggedInUser = Session.getLoggedInUser();
        //     //  System.out.println(loggedInUser.getName());
        // } 
        
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("ChatApp");

        ListView<User> contacts = new ListView<>();

        if (loggedInUser != null && database != null) {
            MongoCollection<Document> contactsCollection = database.getCollection("User_Contacts");
            FindIterable<Document> userContacts = contactsCollection.find(new Document("user_id", loggedInUser.getUserId()));

            for (Document document : userContacts) {
                int contactId = document.getInteger("contact_id");
                // Fetch the contact details from the database
                MongoCollection<Document> contactedCollection = database.getCollection("User");
                FindIterable<Document> contacted = contactedCollection.find(new Document("user_id", contactId));
                for (Document document1 : contacted) {
                    String name = document1.getString("name");
                    String profilePicture = document1.getString("profile_picture");
                    User contact = new User(contactId, name, profilePicture);
                    contacts.getItems().add(contact);
                }
            }
        }

        return contacts;
    }
    


    // public static void main(String[] args) {
        
    //     User loggedInUser = new User(1, "John Doe", "https://www.google.com");
    //     ListView<User> contactedUsers=  getContacts(loggedInUser);

    //     contactedUsers.getItems().forEach((user) -> {
    //         System.out.println(user.getName());
    //     });
    // }
}
