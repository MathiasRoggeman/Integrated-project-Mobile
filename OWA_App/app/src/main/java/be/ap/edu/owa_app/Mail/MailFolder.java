package be.ap.edu.owa_app.Mail;

/**
 * Created by isa_l on 18-01-18.
 */

public class MailFolder {
    private String id;
    private String name;
    private int totalItemCount;

    public MailFolder(String id, String name, int totalItemCount) {
        this.id = id;
        this.name = name;
        this.totalItemCount = totalItemCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}
