package starlabs.noticeboard;

/**
 * Created by Rishabh on 16-03-2018.
 */

public class Students {
    String name,email,contact,branch,password;


    public Students() {

    }

    public Students(String name, String email,String contact,String branch, String password) {
        this.name = name;
        this.email = email;
        this.contact=contact;
        this.branch = branch;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getBranch() {
        return branch;
    }

    public String getPassword() {
        return password;
    }
}