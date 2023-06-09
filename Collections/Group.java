package Collections;

public class Group {
    private int groupId;
    private String groupName;
    private String description;
    private int adminId;
    private String createdAt;
    private String profile_picture;

    public Group(int groupId, String groupName, String description, int adminId, String profile_picture) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.description = description;
        this.adminId = adminId;
        this.profile_picture = profile_picture;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    
}
