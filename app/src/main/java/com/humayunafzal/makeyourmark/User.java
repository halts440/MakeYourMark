package com.humayunafzal.makeyourmark;

public class User {
    private String name;
    private String phone;
    private String location;
    private String imagePath;
    private String type;

    public User() {
    }

    public User(String name, String phone, String location, String imagePath, String type) {
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.imagePath = imagePath;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
