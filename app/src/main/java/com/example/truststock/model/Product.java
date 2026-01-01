package com.example.truststock.model;

public class Product {

    private String id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private long stock;
    private String status;
    public Product() {
    }


    public Product(String name, double price, String description,
                   String imageUrl, long stock, String status) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getStock() {
        return stock;
    }

    public String getStatus() {
        return status;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
