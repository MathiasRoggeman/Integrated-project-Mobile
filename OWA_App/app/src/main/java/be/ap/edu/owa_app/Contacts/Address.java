package be.ap.edu.owa_app.Contacts;

/**
 * Created by mathi on 16-1-2018.
 */

public class Address {
    private String Straat;
    private String Postbus;
    private String Omgeving;
    private String Plaats;
    private String Status;
    private String Postcode;
    private String Land;

    public String getStraat() {
        return Straat;
    }

    public void setStraat(String straat) {
        Straat = straat;
    }

    public String getPostbus() {
        return Postbus;
    }

    public void setPostbus(String postbus) {
        Postbus = postbus;
    }

    public String getOmgeving() {
        return Omgeving;
    }

    public void setOmgeving(String omgeving) {
        Omgeving = omgeving;
    }

    public String getPlaats() {
        return Plaats;
    }

    public void setPlaats(String plaats) {
        Plaats = plaats;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public String getLand() {
        return Land;
    }

    public void setLand(String land) {
        Land = land;
    }
}
