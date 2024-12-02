package org.example.proiect_gradle.Repository.FileRepository;

import org.example.proiect_gradle.Domain.Order;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.readAllLines;

public class OrderFileRepository extends FileRepository<Order> {
    public String orderedProductsFilename;
    public OrderFileRepository(String filename, String orderedProducts){
        super(filename);
        this.orderedProductsFilename = orderedProducts;
    }

    @Override
    protected String convertObjectToString(Order order) {

        if(order==null){
            throw new IllegalArgumentException("Order object cannot be null");
        }

        return order.getId() + "," +
                order.getStatus() + "," +
                order.getTotalPrice() + "," +
                order.getShippingAddress() + "," +
                order.getBuyer() + "," +
                order.getSeller();
    }

    @Override
    protected Order createObjectFromString(String line) {

        try{
            String[] parts = line.split(",");
            int orderId = Integer.parseInt(parts[0]);
            String status = parts[1];
            double totalPrice = Double.parseDouble(parts[2]);
            String shippingAddress = parts[3];
            int buyer = Integer.parseInt(parts[4]);
            int seller = Integer.parseInt(parts[5]);

            Order order = new Order(new ArrayList<>(), status, shippingAddress, buyer, seller);
            order.setId(orderId);
            return order;
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }
    }

    @Override
    public void loadDataFromFile() {

        try {
            super.loadDataFromFile();
            loadOrderedProducts().forEach((key, value) -> {
                Order o = super.read(key);
                o.getProducts().clear();
                o.getProducts().addAll(value);
            });
        }catch(RuntimeException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }

    private Map<Integer, List<Integer>> loadOrderedProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(orderedProductsFilename))) {
            String line;
            Map<Integer, List<Integer>> orderedProducts = new HashMap<>();
            while ((line = reader.readLine()) != null) {


                String[] parts = line.split(",");
                int orderId = Integer.parseInt(parts[0]);
                int productId = Integer.parseInt(parts[1]);
                if(!orderedProducts.containsKey(orderId)) {
                    orderedProducts.put(orderId, new ArrayList<>());
                }
                orderedProducts.get(orderId).add(productId);
            }

            return orderedProducts;
        } catch (IOException e) {
            System.err.println("Error reading ordered products: " + e.getMessage());
        }
        return new HashMap<>();
    }

    private void writeOrderedProducts(Map<Integer, List<Integer>> orderedProducts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderedProductsFilename))) {
            for(Map.Entry<Integer, List<Integer>> entry : orderedProducts.entrySet()) {
                for(Integer productId : entry.getValue()) {
                    writer.write(entry.getKey() + "," + productId);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving listed products: " + e.getMessage());
        }
    }

    @Override
    public void create(Order order) {
        super.create(order);
        saveOrderedProducts(order);
    }

    @Override
    public void update(Order order) {
        super.update(order);
        saveOrderedProducts(order);
    }

    public void saveOrderedProducts(Order order) {
        Map<Integer, List<Integer>> orderedProducts = loadOrderedProducts();
        orderedProducts.put(order.getId(), order.getProducts());
        writeOrderedProducts(orderedProducts);
    }

    private Order findOrderById(int orderId) {
        for (Order order : getAll()) {
            if (order.getId() == orderId) {
                Map<Integer, List<Integer>> orderedProducts = loadOrderedProducts();
                order.setProducts(orderedProducts.get(order.getId()));
                return order;
            }
        }

        return null;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = super.getAll();
        Map<Integer, List<Integer>> orderedProducts = loadOrderedProducts();
        orders.forEach(order -> {
           order.setProducts(orderedProducts.get(order.getId()));
        });
        return orders;
    }
}
