package Collections;

public class Contact {
    private int userContactId;
    private int userId;
    private int contactId;
    private String contactDate;

    public Contact(int userContactId, int userId, int contactId, String contactDate) {
        this.userContactId = userContactId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactDate = contactDate;
    }

    public int getUserContactId() {
        return userContactId;
    }

    public void setUserContactId(int userContactId) {
        this.userContactId = userContactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactDate() {
        return contactDate;
    }

    public void setContactDate(String contactDate) {
        this.contactDate = contactDate;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "userContactId=" + userContactId +
                ", userId=" + userId +
                ", contactId=" + contactId +
                ", contactDate=" + contactDate +
                '}';
    }
}
