package com.appjava.aplicativotcc.model;

public class ModelUser {

    private String user;
    private String email;
    private String password;
    private String avatar;

    public ModelUser() {
    }

    // GET method

    public String getUser() {
        return user;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }



    // SET method

    public void setUser(String parameterUser) {
        this.user = parameterUser;
    }
    public void setEmail(String parameterEmail) {
        this.email = parameterEmail;
    }
    public void setPassword(String parameterPassword) {
        this.password = parameterPassword;
    }
}
