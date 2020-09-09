package com.example.accessories_rental;

public class Product {
    public String productName;
    public String productTime;
    public String productAmount;
    public String productDate;
    public String productImage1;
    public String productImage2;

    public Product() {

    }

    public Product(String productName, String productTime, String productAmount, String productDate, String productImage1, String productImage2) {
        this.productName = productName;
        this.productTime = productTime;
        this.productAmount = productAmount;
        this.productDate = productDate;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductImage1() {
        return productImage1;
    }

    public void setProductImage1(String productImage1) {
        this.productImage1 = productImage1;
    }

    public String getProductImage2() {
        return productImage2;
    }

    public void setProductImage2(String productImage2) {
        this.productImage2 = productImage2;
    }
}
