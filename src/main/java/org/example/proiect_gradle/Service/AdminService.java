package org.example.proiect_gradle.Service;

import java.util.*;

import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Exceptions.BusinessLogicException;
import org.example.proiect_gradle.Exceptions.EntityNotFoundException;
import org.example.proiect_gradle.Repository.FileRepository.FileRepository;

public class AdminService extends VisitorService {
    private final FileRepository<Admin> adminRepo;
    private final FileRepository<Category> categoryRepo;
    private final FileRepository<Order> orderRepo;



    /**
     * Constructor for the AdminService class. Initializes the service with the provided repositories for
     * users, products, reviews, admins, and categories.
     *
     * @param userRepo the repository to handle user-related operations
     * @param productRepo the repository to handle product-related operations
     * @param reviewRepo the repository to handle review-related operations
     * @param adminRepo the repository to handle admin-related operations
     * @param categoryRepo the repository to handle category-related operations
     */
    public AdminService(FileRepository<User> userRepo, FileRepository<Product> productRepo,
                        FileRepository<Review> reviewRepo, FileRepository<Admin> adminRepo,
                        FileRepository<Category> categoryRepo, FileRepository<Order> orderRepo) {
        super(userRepo, productRepo, reviewRepo, categoryRepo);
        this.adminRepo = adminRepo;
        this.categoryRepo=categoryRepo;
        this.orderRepo = orderRepo;
    }

    /**
     * Authenticates an admin based on their username and password.
     *
     * @param username the username of the admin.
     * @param password the password of the admin.
     * @return {@code true} if an admin with the specified username and password is found; {@code false} otherwise.
     */
    public boolean authenticate(String username, String password) {
        List<Admin> admins = adminRepo.findByCriteria(admin -> admin.getUserName().equals(username) && admin.getPassword().equals(password));
        try{
            Admin admin = admins.getFirst();
        }catch (EntityNotFoundException e){
            return false;
        }

        return true;
    }

    /**
     * Deletes a user from the repository if the admin is authenticated.
     *
     * @param adminUsername the username of the admin performing the action.
     * @param adminPassword the password of the admin performing the action.
     * @param userId the ID of the user to be deleted.
     * @return {@code true} if the user is successfully deleted; {@code false} otherwise.
     */
    public boolean deleteUser(String adminUsername, String adminPassword, int userId) {
        try {
            if (authenticate(adminUsername, adminPassword)) {
                List<User> users = userRepo.getAll();
                for (User user : users) {
                    if (user.getId() == userId) {
                        userRepo.delete(userId);
                        return true;
                    }
                }
                throw new EntityNotFoundException("User with ID " + userId + " does not exist.");
            } else {
                throw new EntityNotFoundException("Authentication failed for admin: " + adminUsername);
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }

        return false;
    }


    /**
     * Deletes a review from the repository if the admin is authenticated, and updates the nr of flagged actions for an user
     *
     * @param adminUsername the username of the admin performing the action.
     * @param adminPassword the password of the admin performing the action.
     * @param reviewId the ID of the review to be deleted.
     * @return {@code true} if the review is successfully deleted; {@code false} otherwise.
     */
    public boolean deleteReview(String adminUsername, String adminPassword, int reviewId) {

        try{
        if (authenticate(adminUsername, adminPassword)) {
            List<Review> reviews = reviewRepo.getAll();
            for (Review review:reviews) {
                if(review.getId()==reviewId){
                    User user = userRepo.read(reviewRepo.read(reviewId).getReviewer());
                    if (user == null) {
                        throw new EntityNotFoundException("Reviewer for review ID " + reviewId + " does not exist.");
                    }
                    user.incrementFlaggedActions();
                    reviewRepo.delete(reviewId);
                    return true;
                }
            }
            throw new EntityNotFoundException("Review with ID " + reviewId + " does not exist.");
        }
        }
        catch (BusinessLogicException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        }

        return false;
    }

    /**
     * Deletes a product from the repository if the admin is authenticated.
     *
     * @param adminUsername the username of the admin performing the action.
     * @param adminPassword the password of the admin performing the action.
     * @param productId the ID of the product to be deleted.
     * @return {@code true} if the product is successfully deleted; {@code false} otherwise.
     */
    public boolean deleteProduct(String adminUsername, String adminPassword, int productId) {

        try{
        if (authenticate(adminUsername,adminPassword)) {
            List<Product> products=productRepo.getAll();
            for(Product product:products){
                if(product.getId()==productId){
                    User user = userRepo.read(productRepo.read(productId).getListedBy());
                    user.incrementFlaggedActions();
                    productRepo.delete(productId);
                    return true;
                }
            }
            throw new EntityNotFoundException("Product with ID " + productId + " does not exist.");
        }
        } catch (BusinessLogicException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
        return false;
    }


    /**
     * Updates the category of a product if the admin is authenticated.
     *
     * @param adminUsername the username of the admin performing the action.
     * @param adminPassword the password of the admin performing the action.
     * @param productId the ID of the product to be updated.
     * @param newCategory the new category to set for the product.
     * @return {@code true} if the category is successfully updated; {@code false} otherwise.
     */
    public boolean updateCategory(String adminUsername,String adminPassword, int productId, int newCategory){

        try{
        if (authenticate(adminUsername,adminPassword)) {
            List<Product> products = productRepo.getAll();
            Product targetedProduct = productRepo.read(productId);

            if (targetedProduct == null) {
                throw new EntityNotFoundException("Product with ID " + productId + " does not exist.");
            }

            for (Product product : products) {
                if (product.equals(targetedProduct)) {
                    targetedProduct.setCategory(newCategory);
                    int userId = productRepo.read(productId).getListedBy();
                    User user = userRepo.read(userId);

                    if (user == null) {
                        throw new EntityNotFoundException("User listing product ID " + productId + " does not exist.");
                    }

                    user.incrementFlaggedActions();
                    productRepo.update(targetedProduct);
                    return true;
                }
            }

        }
        } catch (BusinessLogicException e) {
            System.err.println("Error updating category: " + e.getMessage());
        }
        return false;
    }


    /**
     * Retrieves all categories from the repository.
     *
     * @return a list of all categories.
     */
    public List<Category> getAllCategories(){
        return categoryRepo.getAll();
    }



    /**
     * Sorts product categories by the total income generated from their sales, in descending order.
     *
     * <p>This method aggregates the income for each category based on the products sold in all orders.
     * It retrieves all orders, calculates the total income for each category, and then sorts the categories
     * by their total income in descending order.</p>
     *
     * @return a {@link Map} where the keys are category names and the values are the total income
     *         for each category, sorted in descending order of income. If no orders are present,
     *         an empty map is returned.
     *
     * <p><b>Logic:</b>
     * <ul>
     *     <li>Retrieve all orders from the repository.</li>
     *     <li>For each order, iterate through its product IDs.</li>
     *     <li>For each product, retrieve its category and price.</li>
     *     <li>Aggregate the income for each category using a {@link Map}.</li>
     *     <li>Sort the categories by income in descending order.</li>
     * </ul>
     * </p>
     *
     * @throws NullPointerException if any of the repositories (orderRepo, productRepo, categoryRepo) are null.
     *
     * <p><b>Example:</b>
     * <pre>
     * {@code
     * OrderRepo orderRepo = new OrderRepo();
     * ProductRepo productRepo = new ProductRepo();
     * CategoryRepo categoryRepo = new CategoryRepo();
     *
     * Map<String, Double> sortedIncome = service.sortCategoriesByIncome();
     * System.out.println(sortedIncome);
     * // Output: {Tops=200.0, Bottoms=150.0}
     * }
     * </pre>
     */



    public Map<String, Double> sortCategoriesByIncome() {

        try {
            List<Order> allOrders = orderRepo.getAll();
            if (allOrders.isEmpty()) {
                return Map.of();
            }
            Map<String, Double> incomeByCategory = new HashMap<>();
            for (Order order : allOrders) {
                for (int productId : order.getProducts()) {

                    try {
                        Product product = productRepo.read(productId);
                        if (product != null) {
                            String categoryName = String.valueOf(categoryRepo.read(product.getCategory()).getName());
                            double productPrice = product.getPrice();
                            incomeByCategory.merge(categoryName, productPrice, Double::sum);
                        }
                    } catch (EntityNotFoundException e) {
                        System.err.println("Error processing product ID " + productId + ": " + e.getMessage());
                    }
                }
            }


            List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(incomeByCategory.entrySet());
            sortedEntries.sort(Map.Entry.<String, Double>comparingByValue().reversed());
            Map<String, Double> sortedIncomeByCategory = new LinkedHashMap<>();
            for (Map.Entry<String, Double> entry : sortedEntries) {
                sortedIncomeByCategory.put(entry.getKey(), entry.getValue());
            }
            return sortedIncomeByCategory;

        }catch (BusinessLogicException e) {
            System.err.println("Error sorting categories by income: " + e.getMessage());
            return Map.of();
        }

    }





}

