package com.example.truststock.model;

import java.util.List;

public class Order {
    private String id;
    private String customerName;
    private String address;
    private String phone;
    private List<CartItem> products;
    private double total;
    private String status;

    public Order() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<CartItem> getProducts() { return products; }
    public void setProducts(List<CartItem> products) { this.products = products; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
