package be.ap.edu.owa_app;

/**
 * Created by isa_l on 13-12-17.
 */

public class OpenMail {
    private String subject;
    private String sender;
    private String date;
    private String message;

    public OpenMail(String subject, String sender, String date, String message) {
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.message = message;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
