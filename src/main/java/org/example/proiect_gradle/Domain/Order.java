package org.example.proiect_gradle.Domain;

import java.util.List;
import java.util.Objects;

public class Order implements Identifiable{
    private int id;
    private List<Integer> products;
    private String status;
    private double totalPrice;
    private String shippingAddress;
    private int buyer;
    private int seller;

    public Order(List<Integer> products, String status, String shippingAddress, int buyer, int seller) {
        this.products = products;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.buyer=buyer;
        this.seller=seller;
    }

    @Override
    public int getId() {return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getProducts() {
        return products;
    }

    public void setProducts(List<Integer> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBuyer(int buyer){this.buyer=buyer;}

    public int getBuyer(){return buyer;}

    public int getSeller() {
        return seller;
    }

    public void setSeller(int seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", products=" + products +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", buyer=" + buyer +
                ", seller=" + seller +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(totalPrice, order.totalPrice) == 0 && buyer == order.buyer && seller == order.seller && Objects.equals(products, order.products) && Objects.equals(status, order.status) && Objects.equals(shippingAddress, order.shippingAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, status, totalPrice, shippingAddress, buyer, seller);
    }
}
