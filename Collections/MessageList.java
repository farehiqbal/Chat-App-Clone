package Collections;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import SessionManager.*;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

public class MessageList {
    
    public static User loggedInUser = Session.getLoggedInUser();



    public static List<Message> getContactMessages(User selectedUser){

        ListView<User> contactsList = UserContacts.getContacts(loggedInUser);
        ArrayList<Message> messages = new ArrayList<>();

        // now we have both the logged in user and the selected user
        // we can get the messages between them from the Message collection

        // we need to get the messages where the sender is the logged in user and the receiver is the selected user
        // and the messages where the sender is the selected user and the receiver is the logged in user

        // lets get them from collection

        if(loggedInUser != null && selectedUser != null){

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("ChatApp");

            MongoCollection<Document> messageCollection = database.getCollection("Message");  



             // Get the messages where the sender is the logged in user and the receiver is the selected user
            FindIterable<Document> senderMessages = messageCollection.find(new Document("sender_id", loggedInUser.getUserId()).append("recipient_id", selectedUser.getUserId()));

        // Get the messages where the sender is the selected user and the receiver is the logged in user
            FindIterable<Document> receiverMessages = messageCollection.find(new Document("sender_id", selectedUser.getUserId()).append("recipient_id", loggedInUser.getUserId()));



            


            // now we have the messages in the senderMessages and receiverMessages lists
            // we need to add them to the messages list

            // since these are messages between users, recipientType = "user"

            for(Document document : senderMessages){
                int messageId = document.getInteger("message_id");
                int senderId = document.getInteger("sender_id");
                int receiverId = document.getInteger("recipient_id");
                String content = document.getString("content");
                String recipient = document.getString("recipient_type");
                String time = document.getString("timestamp");
                
                Message message = new Message(messageId, senderId, receiverId, recipient, content, time);
                if (recipient.equals("user"))  messages.add(message);

            }

            for(Document document : receiverMessages){
                int messageId = document.getInteger("message_id");
                int senderId = document.getInteger("sender_id");
                int receiverId = document.getInteger("recipient_id");
                String content = document.getString("content");
                String recipient = document.getString("recipient_type");
                String time = document.getString("timestamp");
                
                Message message = new Message(messageId, senderId, receiverId, recipient, content, time);
                if (recipient.equals("user"))  messages.add(message);
            }

            return messages;
        }


        return null;
        
    }


    // public static void main(String[] args) {
        
    //     User loggedInUser = new User(1, "John", "null");
    //     User selectedUser = new User(2, "Jane", "null");

    //     List<Message> messages = getContactMessages(selectedUser);

    //     System.out.println(messages.size());

    //     // for(Message message : messages){
    //     //     System.out.println(message.getContent());
    //     // }

    // }
}
