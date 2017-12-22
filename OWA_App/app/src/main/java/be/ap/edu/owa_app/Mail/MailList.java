package be.ap.edu.owa_app.Mail;

/**
 * Created by isa_l on 06-12-17.
 */

public class MailList {
    private String id;
    private String subject;
    private String sender;
    private String bodypreview;
    private Boolean read;
    private String message;
    private String date;

    public MailList(String id, String subject, String sender, String bodypreview, boolean read, String message, String date) {
        this.id = id;
        this.subject = subject;
        this.sender = sender;
        this.bodypreview = bodypreview;
        this.read = read;
        this.message = message;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBodyPreview() {
        return bodypreview;
    }

    public void setBodyPreview(String body) {
        this.bodypreview = body;
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getBodypreview() {
        return bodypreview;
    }

    public void setBodypreview(String bodypreview) {
        this.bodypreview = bodypreview;
    }

    public Boolean getRead() {
        return read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
