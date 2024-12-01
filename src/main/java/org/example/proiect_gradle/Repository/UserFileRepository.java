package org.example.proiect_gradle.Repository;

import org.example.proiect_gradle.Domain.User;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class UserFileRepository extends FileRepository<User> {
    public String productsListedByUserFilename;
    public String productsLikedByUserFilename;
    public UserFileRepository(String filename, String productsListedByUserFilename,
                              String productsLikedByUserFilename) {
        super(filename);
        this.productsListedByUserFilename = productsListedByUserFilename;
        this.productsLikedByUserFilename = productsLikedByUserFilename;
    }

    @Override
    protected String convertObjectToString(User user) {

        if(user==null){
            throw new IllegalArgumentException("User object cannot be null");
        }
        return user.getId() + "," +
                user.getUserName() + "," +
                user.getPassword() + "," +
                user.getEmail() + "," +
                user.getPhone() + "," +
                user.getScore() + "," +
                user.nrOfFlaggedActions;
    }

    @Override
    protected User createObjectFromString(String line) {

        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }

        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String username = parts[1];
            String password = parts[2];
            String email = parts[3];
            String phone = parts[4];
            double score = Double.parseDouble(parts[5]);
            int flaggedActions = Integer.parseInt(parts[6]);

            User user = new User(username, password, email, phone, score);
            user.setId(id);
            user.nrOfFlaggedActions = flaggedActions;
            return user;
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }
    }

    @Override
    public void loadDataFromFile() {
        try {
            super.loadDataFromFile();

            loadLikedProducts().forEach((key, value) -> {
                User u = super.read(key);
                u.getFavourites().clear();
                u.getFavourites().addAll(value);
            });
            loadListedProducts().forEach((key, value) -> {
                User u = super.read(key);
                u.getListedProducts().clear();
                u.getListedProducts().addAll(value);
            });
        }catch(RuntimeException e){
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }

    private Map<Integer, List<Integer>> loadLikedProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(productsLikedByUserFilename))) {
            String line;
            Map<Integer, List<Integer>>  likedProducts = new HashMap<>();
            super.getAll().forEach(user -> {
                likedProducts.put(user.getId(), new ArrayList<>());
            });
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int userId = Integer.parseInt(parts[0]);
                int productId = Integer.parseInt(parts[1]);

                User user = super.read(userId);
                if (user != null) {
                    likedProducts.get(userId).add(productId);
                }
            }
            return likedProducts;
        } catch (IOException e) {
            System.err.println("Error reading liked products: " + e.getMessage());
        }
        return new HashMap<>();
    }

    private Map<Integer, List<Integer>>  loadListedProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(productsListedByUserFilename))) {
            String line;
            Map<Integer, List<Integer>> listedProducts = new HashMap<>();
            super.getAll().forEach(user -> {
                listedProducts.put(user.getId(), new ArrayList<>());
            });
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int userId = Integer.parseInt(parts[0]);
                int productId = Integer.parseInt(parts[1]);

                User user = super.read(userId);
                if (user != null) {
                    listedProducts.get(userId).add(productId);
                }
            }
            return listedProducts;
        } catch (IOException e) {
            System.err.println("Error reading listed products: " + e.getMessage());
        }
        return new HashMap<>();
    }

    private void writeLikedProducts(Map<Integer, List<Integer>> likedProducts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsLikedByUserFilename))) {
            for(Map.Entry<Integer, List<Integer>> entry : likedProducts.entrySet()) {
                for(Integer productId : entry.getValue()) {
                    writer.write(entry.getKey() + "," + productId);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving listed products: " + e.getMessage());
        }
    }

    private void writeListedProducts(Map<Integer, List<Integer>> listedProducts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsListedByUserFilename))) {
            for(Map.Entry<Integer, List<Integer>> entry : listedProducts.entrySet()) {
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
    public void create(User user) {
        super.create(user);
        saveLikedProducts(user);
        saveListedProducts(user);
    }

    @Override
    public void update(User user) {
        super.update(user);
        saveLikedProducts(user);
        saveListedProducts(user);
    }

    private void saveLikedProducts(User user) {

        Map<Integer, List<Integer>> likedProducts = loadLikedProducts();
        likedProducts.put(user.getId(), user.getFavourites());
        writeLikedProducts(likedProducts);
    }

    private void saveListedProducts(User user) {

        Map<Integer, List<Integer>> listedProducts = loadListedProducts();
        listedProducts.put(user.getId(), user.getListedProducts());
        writeListedProducts(listedProducts);
    }



    @Override
    public List<User> findByCriteria(Predicate<User> predicate) {
        List<User> matchingUsers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(super.filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = createObjectFromString(line);
                if (predicate.test(user)) {
                    matchingUsers.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users for criteria search: " + e.getMessage());
        }

        Map<Integer, List<Integer>> liked = loadLikedProducts();
        Map<Integer, List<Integer>> listed = loadListedProducts();

        matchingUsers.forEach(u->{
            u.getFavourites().clear();
            u.getFavourites().addAll(liked.get(u.getId()));
            u.getListedProducts().clear();
            u.getListedProducts().addAll(listed.get(u.getId()));
        });

        return matchingUsers;
    }

    @Override
    public List<User> getAll(){
        List<User> users = super.getAll();
        Map<Integer, List<Integer>> likedProducts = loadLikedProducts();
        Map<Integer, List<Integer>> listedProducts = loadListedProducts();

        users.forEach(user -> {
            user.getFavourites().clear();
            user.getFavourites().addAll(likedProducts.get(user.getId()));
            user.getListedProducts().clear();
            user.getListedProducts().addAll(listedProducts.get(user.getId()));
        });
        return users;
    }
}
