package Collections;
public class User {
    private int user_id;
    private String name;
    private String profile_picture;
    private String bio;
    private String preferred_language;
    private String created_at;

    // Constructor
    public User(int user_id, String name, String profile_picture, String bio, String preferred_language, String created_at) {
        this.user_id = user_id;
        this.name = name;
        this.profile_picture = profile_picture;
        this.bio = bio;
        this.preferred_language = preferred_language;
        this.created_at = created_at;
    }

    public User(int user_id, String name, String profile_picture) {
        this.user_id = user_id;
        this.name = name;
        this.profile_picture = profile_picture;
    }

    // Getters and setters
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profile_picture;
    }

    public void setProfilePicture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPreferredLanguage() {
        return preferred_language;
    }

    public void setPreferredLanguage(String preferred_language) {
        this.preferred_language = preferred_language;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "User [user_id=" + user_id + ", name=" + name + ", profile_picture=" + profile_picture + ", bio=" + bio
                + ", preferred_language=" + preferred_language + ", created_at=" + created_at + "]";
    }

    
}
