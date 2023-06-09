package Collections;
import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String recipientType;
    private String content;
    private String timestamp;

    public Message(int messageId, int senderId, int receiverId, String recipientType ,String content, String timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.recipientType = recipientType;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
}
