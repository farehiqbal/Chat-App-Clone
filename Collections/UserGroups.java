package Collections;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.ListView;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserGroups {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static ListView<Group> getGroups(User loggedInUser) {
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("ChatApp");

        ListView<Group> groups = new ListView<>();

        if (loggedInUser != null && database != null) {
            MongoCollection<Document> groupMembersCollection = database.getCollection("Group_Members");
            FindIterable<Document> userGroupMembers = groupMembersCollection.find(new Document("user_id", loggedInUser.getUserId()));

            for (Document document : userGroupMembers) {
                int groupId = document.getInteger("group_id");

                // Fetch the group details from the database
                MongoCollection<Document> groupsCollection = database.getCollection("Groups");
                FindIterable<Document> groupData = groupsCollection.find(new Document("group_id", groupId));

                for (Document groupDocument : groupData) {
                    String groupName = groupDocument.getString("group_name");
                    String description = groupDocument.getString("description");
                    int adminId = groupDocument.getInteger("admin");
                    String profilePicture = groupDocument.getString("profile_picture");
                    Group group = new Group(groupId, groupName, description, adminId, profilePicture);
                    groups.getItems().add(group);
                }
            }
        }

        return groups;
    }
}
