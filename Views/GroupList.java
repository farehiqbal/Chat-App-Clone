package Views;

import Collections.*;
import SessionManager.Session;
import javafx.scene.control.ListView;

public class GroupList {
    User loggedInUser = Session.getLoggedInUser();

    ListView<Group> groupsList = UserGroups.getGroups(loggedInUser);

    // public static  getGroups() {
        
    // }
}
