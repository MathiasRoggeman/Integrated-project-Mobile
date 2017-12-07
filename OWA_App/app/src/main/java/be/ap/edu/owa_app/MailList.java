package be.ap.edu.owa_app;

/**
 * Created by isa_l on 06-12-17.
 */

public class MailList {
    private String subject;
    private String sender;
    private String body;
    private Boolean read;

    public MailList(String subject, String sender, String body, boolean read) {
        this.subject = subject;
        this.sender = sender;
        this.body = body;
        this.read = read;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
