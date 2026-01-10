package com.example.truststock.model;

public class Comment {
    private String id;
    private String productName;
    private String customerName;
    private String comment;
    private int rating;

    public Comment() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}

