package be.ap.edu.owa_app.Contacts;

import java.io.Serializable;

/**
 * Created by isa_l on 20-12-17.
 */

public class Contacts implements Serializable {

    private String id;
    private String displayName;
    private String name;
    private String surname;
    private String email;
    private String mobile;

    public Contacts(String id, String displayName, String name, String surname, String email, String mobile) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
