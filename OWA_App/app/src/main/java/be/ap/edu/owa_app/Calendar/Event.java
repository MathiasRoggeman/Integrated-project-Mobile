package be.ap.edu.owa_app.Calendar;

/**
 * Created by isa_l on 16-12-17.
 */

public class Event {
    private String id;
    private String subject;
    private String body;
    private String startDate;
    private String endDate;
    private String location;
    private String attendees;

    public Event(String id, String subject, String body, String startDate, String endDate, String location, String attendees) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.attendees = attendees;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }
}
