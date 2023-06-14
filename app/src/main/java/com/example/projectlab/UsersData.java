package com.example.projectlab;

public class UsersData {
    public String name,price,date,type,forwho,id;

    public UsersData(String name, String price, String date, String type, String forwho){
        this.name = name;
        this.price = "$ " + price;
        this.date = date;
        this.type = type;
        this.forwho = forwho;
    }

    public UsersData(String id, String name, String price, String date, String type, String forwho){
        this.id = id;
        this.name = name;
        this.price = "$ " + price;
        this.date = date;
        this.type = type;
        this.forwho = forwho;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForwho() {
        return forwho;
    }

    public void setForwho(String forwho) {
        this.forwho = forwho;
    }

}