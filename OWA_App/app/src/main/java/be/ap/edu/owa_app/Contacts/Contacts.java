package be.ap.edu.owa_app.Contacts;

/**
 * Created by isa_l on 20-12-17.
 */

public class Contacts {

    private String id;
    private String Birthday;
    private String displayName;
    private String name;
    private String surname;
    private String email;
    private String mobile;
    private Address address;
    private String Bedrijf;
    private String BedrijfsTitel;
    private String Opmerkingen;


    public Contacts(String id, String displayName, String name, String surname, String email, String mobile, Address address, String bedrijf, String opmerkingen) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.Bedrijf = bedrijf;
        this.Opmerkingen = opmerkingen;

    }

    public Contacts(String id, String displayName, String name, String surname, String email, String mobile, String bedrijf, String opmerkingen, String birthday, String bedrijfsTitel) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile = mobile;
        this.Bedrijf = bedrijf;
        this.Opmerkingen = opmerkingen;
        this.Birthday = birthday;
        this.BedrijfsTitel = bedrijfsTitel;
    }

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


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBedrijf() {
        return Bedrijf;
    }

    public void setBedrijf(String bedrijf) {
        Bedrijf = bedrijf;
    }

    public String getBedrijfsTitel() {
        return BedrijfsTitel;
    }

    public void setBedrijfsTitel(String bedrijfsTitel) {
        BedrijfsTitel = bedrijfsTitel;
    }

    public String getOpmerkingen() {
        return Opmerkingen;
    }

    public void setOpmerkingen(String opmerkingen) {
        Opmerkingen = opmerkingen;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
