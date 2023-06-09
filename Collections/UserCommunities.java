package Collections;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.ListView;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserCommunities {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static ListView<Community> getCommunities(User loggedInUser) {
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("ChatApp");

        ListView<Community> communities = new ListView<>();

        if (loggedInUser != null && database != null) {
            MongoCollection<Document> userCommunitiesCollection = database.getCollection("User_Communities");
            FindIterable<Document> userCommunityData = userCommunitiesCollection.find(new Document("user_id", loggedInUser.getUserId()));

            for (Document document : userCommunityData) {
                int communityId = document.getInteger("community_id");

                // Fetch the community details from the database
                MongoCollection<Document> communitiesCollection = database.getCollection("Community");
                FindIterable<Document> communityData = communitiesCollection.find(new Document("community_id", communityId));

                for (Document communityDocument : communityData) {
                    // int communityId = communityDocument.getInteger("community_id");
                    String communityName = communityDocument.getString("name");
                    String description = communityDocument.getString("description");
                    int createdBy = communityDocument.getInteger("created_by");
                    String profilePicture = communityDocument.getString("profile_picture");
                    String createdAt = communityDocument.getString("created_at");
                    List<Integer> groups = communityDocument.getList("groups", Integer.class);

                    
                    // now we have group_ids of all the groups in the community
                    // we just to show those groups in the community, which the loggedinuser is part of
                    // if loggedinuser is part of any group that is further part of a community, show those communities only

                    
                    
                    


                    Community community = new Community(communityId, communityName, description, createdBy, profilePicture, createdAt, groups);
                    communities.getItems().add(community);
                }
            }
        }

        return communities;
    }
}
