package org.example.proiect_gradle.Domain;

import java.util.Objects;

public class Product implements Identifiable{

    private int id;
    private int category;
    private String name;
    private String color;
    private int size;
    private double price;
    private String brand;
    private String condition;
    private int nrViews;
    private int nrLikes;
    private int listedBy;
    private boolean available = true;
    private String imagePath;



    public Product(String name,String color, int size, double price, String brand, String condition, int nrViews, int nrLikes, int listedBy) {
        this.name=name;
        this.color = color;
        this.size = size;
        this.price = price;
        this.brand = brand;
        this.condition = condition;
        this.nrViews = nrViews;
        this.listedBy = listedBy;
        this.nrLikes = nrLikes;
        this.imagePath="product.png";
    }

    public int getListedBy() {
        return listedBy;
    }

    public void setListedBy(int listedBy) {
        this.listedBy = listedBy;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getNrViews() {
        return nrViews;
    }

    public void setNrViews(int nrViews) {
        this.nrViews = nrViews;
    }

    public int getNrLikes() {
        return nrLikes;
    }

    public void setNrLikes(int nrLikes) {
        this.nrLikes = nrLikes;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "\n==============================" +
                "\n        PRODUCT DETAILS       " +
                "\n==============================" +
                "\nID          : " + id +
                "\nCategory    : " + category +
                "\nName        : " + name +
                "\nColor       : " + color +
                "\nSize        : " + size +
                "\nPrice       : $" + String.format("%.2f", price) +
                "\nBrand       : " + brand +
                "\nCondition   : " + condition +
                "\nViews       : " + nrViews +
                "\nLikes       : " + nrLikes +
                "\nListed By   : " + listedBy +
                "\nAvailable   : " + (available ? "Yes" : "No") +
                "\n==============================";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return size == product.size && Double.compare(price, product.price) == 0 && nrViews == product.nrViews && nrLikes == product.nrLikes && listedBy == product.listedBy && available == product.available && Objects.equals(name, product.name) && Objects.equals(color, product.color) && Objects.equals(brand, product.brand) && Objects.equals(condition, product.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, size, price, brand, condition, nrViews, nrLikes, listedBy, available);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

