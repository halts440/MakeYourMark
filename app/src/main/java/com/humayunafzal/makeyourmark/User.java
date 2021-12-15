package com.humayunafzal.makeyourmark;

public class User {
    private String name;
    private String phone;
    private String location;
    private String imagePath;
    private String type;
    private String balance;
    private String playerId;

    public User() {
    }

    public User(String name, String phone, String location, String imagePath, String type, String balance, String playerId) {
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.imagePath = imagePath;
        this.type = type;
        this.balance = balance;
        this.playerId = playerId;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
