package com.example.kvbalu.model;

import java.io.Serializable;
import java.util.Collection;

public class UserModel implements Serializable {
    private long id;

    private String name;

    private String userName;

    private String password;

    private String avatar;

    private String phoneNumber;

    private String email;

    private Collection<String> roles;

    private Boolean isActive;

    public UserModel(long id, String username, String name, String email, String phone, String avatar) {
        this.id = id;
        this.name = name;
        this.userName = username;
        this.email = email;
        this.phoneNumber = phone;
        this.avatar = avatar;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public Boolean getActive() {
        return isActive;
    }

}
