package org.example.proiect_gradle.Repository;

import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Repository.DBRepository.*;
import org.example.proiect_gradle.Repository.FileRepository.*;
import org.example.proiect_gradle.Repository.IMRepository.IMRepository;

public class RepositoryFactory {
    private final String storageType;
    public RepositoryFactory(String storageType) {
        this.storageType = storageType;
    }

    public IRepository<Visitor> createVisitorRepo() {
        return switch(storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new VisitorFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/visitors.txt");
            case "db" -> new DBVisitorRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<User> createUserRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new UserFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/users.txt", "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/likedProducts.txt");
            case "db" -> new DBUserRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<Product> createProductRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new ProductFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/products.txt");
            case "db" -> new DBProductRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<Review> createReviewRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new ReviewFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/reviews.txt");
            case "db" -> new DBReviewRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<Category> createCategoryRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new CategoryFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/categories.txt");
            case "db" -> new DBCategoryRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<Order> createOrderRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new OrderFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/orders.txt", "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/orderedProducts.txt");
            case "db" -> new DBOrderRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<Offer> createOfferRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new OfferFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/offers.txt");
            case "db" -> new DBOfferRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }

    public IRepository<Admin> createAdminRepository() {
        return switch (storageType) {
            case "in-memory" -> new IMRepository<>();
            case "file" -> new AdminFileRepository("src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/admins.txt");
            case "db" -> new DBAdminRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
            default -> throw new IllegalArgumentException("Invalid repository type: " + storageType);
        };
    }
}
