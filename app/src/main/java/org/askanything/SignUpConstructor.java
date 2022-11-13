package org.askanything;

public class SignUpConstructor {
    String name;
    String email;
    String picurl;


    public SignUpConstructor(String name, String email, String picurl) {
        this.name = name;
        this.email = email;
        this.picurl = picurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
