package org.example.proiect_gradle.Controller;

import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Service.AdminService;
import org.example.proiect_gradle.Service.UserService;
import org.example.proiect_gradle.Service.VisitorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    public final AdminService adminService;
    public final UserService userService;
    protected final VisitorService visitorService;

    public Controller(AdminService adminService, UserService userService, VisitorService visitorService) {
        this.adminService = adminService;
        this.userService = userService;
        this.visitorService = visitorService;
    }

    public boolean createAccount(String username, String password, String email, String phoneNumber) {
        if (username != null && password != null && email != null && phoneNumber != null && phoneNumber.length() == 10) {
            visitorService.createAccount(username, password, email, phoneNumber);
            return true;
        }
        else return false;
    }

    public int logIn(String username, String password) {
        if (username != null && password != null) {
            boolean isAdmin = adminService.authenticate(username, password);
            boolean isUser = userService.authenticate(username, password);
            if (isUser)
                return 1;
            if (isAdmin)
                return 2;
        }
        return 0;
    }

    public List<Product> sortProducts(int choice, int order) {
        List<Product> products = visitorService.seeAllProducts();
        return switch (choice) {
            case 1 -> order == 1 ? visitorService.sortProductsByPriceAscending(products) : visitorService.sortProductsByPriceDescending(products);
            case 2 -> order == 1 ? visitorService.sortProductsByLikesAscending(products) : visitorService.sortProductsByLikesDescending(products);
            case 3 -> order == 1 ? visitorService.sortProductsBySizeAscending(products) : visitorService.sortProductsBySizeDescending(products);
            case 4 -> order == 1 ? visitorService.sortProductsByViewsAscending(products) : visitorService.sortProductsByViewsDescending(products);
            default -> {
                yield products;
            }
        };
    }

    public List<Product> filterProductsByCategory(int category) {
       if (category > 0)
           return visitorService.searchProductsByCategory(category);
       else return new ArrayList<>();
    }

    public List<Product> filterProductsByBrand(String brand) {
        if (brand != null)
            return visitorService.searchProductsByBrand(brand);
        else return new ArrayList<>();
    }

    public List<Product> filterProductsByColor(String color) {
        if (color != null)
            return visitorService.searchProductsByColor(color);
        else return new ArrayList<>();
    }

    public List<Product> filterProductsByUserName(String username) {
        if (username != null)
            return visitorService.searchProductsByUsername(username);
        else return new ArrayList<>();
    }

    public List<Product> filterProductsByLikes(int minLikes, int maxLikes) {
        if (minLikes <= maxLikes)
            return visitorService.searchProductsByLikeRange(minLikes, maxLikes);
        else return new ArrayList<>();
    }

    public List<Product> filterProductsByViewRange(int minViews, int maxViews) {
        if (minViews <= maxViews) {
            return visitorService.searchProductsByViewRange(minViews, maxViews);
        }
        else return new ArrayList<>();
    }

    public List<Product> filterProductsByPriceRange(int minPrice, int maxPrice) {
        if (minPrice <= maxPrice) {
            return visitorService.searchProductsByPriceRange(minPrice, maxPrice);
        }
        else return new ArrayList<>();
    }

    public List<Product> filterProductsBySizeRange(int minSize, int maxSize) {
        if (minSize <= maxSize) {
            return visitorService.searchProductsBySizeRange(minSize, maxSize);
        }
        else return new ArrayList<>();
    }

    public List<Product> filterProductsByCondition(String condition) {
        if (condition != null)
            return visitorService.searchProductsByCondition(condition);
        else return new ArrayList<>();
    }


    //Offers
    public List<Offer> getMadeOffers(String username, String password) {
        return userService.displayMadeOffers(username,password);

    }

    public List<Offer> displayReceivedOffers(String username, String password){
        return userService.displayReceivedOffers(username,password);
    }

    public boolean sendOffer(String senderUsername,String senderPassword, String message, int selectedProduct, double offeredPrice){
        return userService.sendOffer(senderUsername, senderPassword, message, selectedProduct, offeredPrice);
    }


    //Orders
    public boolean declineOffer(String username,String password,int offerId){
        return userService.declineOffer(username, password, offerId);
    }

    public boolean acceptOffer(String username,String password,int offerId){
        return userService.acceptOffer(username,password,offerId);

    }

    public List<Order> getMadeOrders(String username, String password){
        return userService.displayMadeOrders(username,password);
    }

    public List<Order> getReceivedOrders(String username, String password){
        return userService.displayReceivedOrders(username, password);
    }

    public boolean makeOrder(String buyerUsername, String buyerPassword, List<Integer> selectedProductsIds, String status, String shippingAddress){
        return userService.placeOrder(buyerUsername, buyerPassword, selectedProductsIds,  status, shippingAddress);
    }



    //liked products

    public boolean likeProduct(String username, String password, int productId){
        return userService.addToFavorites(username,password,productId);
    }

    public boolean removeFromLiked(String username, String password, int productId){
        return userService.removeFromFavourites(username, password, productId);
    }

    public List<Product> displayLikedProducts(String username,String password){
        return userService.displayFavourites(username, password);
    }




    //Listings




    public boolean addToUserListings(String userName,String password, int category,String name,String color, int size, double price, String brand, String condition, int nrOfViews, int nrOfLikes){
        return userService.listProduct(userName, password, category, name, color, size, price, brand, condition, nrOfViews, nrOfLikes);
    }

    public boolean removeFromUserListings(String userName,String password, int productId){
        return userService.deleteListedProduct(userName, password, productId);
    }

    public List<Product> getUserListing(int userId){
        return visitorService.displayUserListings(userId);
    }



    //Sort and filter Users

    public List<User> sortUsers(int choice, int order) {
        List<User> users = visitorService.seeAllUsers();
        return switch (choice) {
            case 1 -> order == 1 ? visitorService.sortUsersAscendingByReviewCount(users) : visitorService.sortUsersDescendingByReviewCount(users);
            case 2 -> order == 1 ? visitorService.sortUsersAlphabeticallyAscending(users) : visitorService.sortUsersAlphabeticallyDescending(users);
            case 3 -> order == 1 ? visitorService.sortUsersAscendingByScore(users) : visitorService.sortUsersDescendingByScore(users);
            case 4 -> users;
            default -> {yield users;
            }
        };
    }

    public List<User> filterUsersByMinimumReviewCount(int minReviewCount) {
        if (minReviewCount >= 0)
            return visitorService.searchUsersByMinimumReviewCount(minReviewCount);
        else return new ArrayList<>();
    }

    public List<User> filterUsersByName(String name) {
        if (name != null)
            return visitorService.searchUsersByUsername(name);
        else return new ArrayList<>();
    }

    public List<User> filterUsersByMinimumScore(double score) {
        if (score >= 0)
            return visitorService.searchUsersByMinimumScore(score);
        else return new ArrayList<>();
    }


    //Review


    public boolean writeReview(String reviewerUsername, String reviewerPassword, double grade, String message, int revieweeId){
        if (grade >=1 && grade <= 5)
            return userService.writeReview(reviewerUsername, reviewerPassword, grade, message, revieweeId);
        else return false;
    }

    public boolean deleteReview(String username,String password, int reviewId){
        return userService.deleteReview(username, password, reviewId);
    }

    public List<Review> displayReviewsLeftForUser(int userId){
        return visitorService.displayReviewsLeftForUser(userId);
    }

    public List<Review> displayReviewsLeftByUser(String username, String password){
        return userService.displayMadePersonalReviews(username,password);
    }

    public List<Review> displayReviewsForMe(String username, String password){
        return userService.displayProfileReviews(username, password);
    }

    public double getProfileScore(String username, String password){
        return userService.getMyScore(username, password);
    }



    //admin


    public boolean deleteReviewAdmin(String adminUsername, String AdminPassword,int reviewId){
        return adminService.deleteReview(adminUsername, AdminPassword, reviewId);
    }

    public boolean deleteUser (String adminUsername, String adminPassword, int userId){
        return adminService.deleteUser(adminUsername, adminPassword, userId);
    }

    public boolean deleteProduct(String adminUsername, String adminPassword, int productId){
        return adminService.deleteProduct(adminUsername, adminPassword, productId);
    }



    public List<Category> getCategories(){
        return adminService.getAllCategories();
    }

    public boolean changeCategory(int productId, int categoryChoice, String adminUsername, String adminPassword) {
        return adminService.updateCategory(adminUsername, adminPassword, productId, categoryChoice);
    }

    public List<Product> getMyListings(String username, String password) {
        return userService.getMyListedProducts(username, password);
    }

    public int getUserTrustScore(int userId){
        return userService.calculateUserTrustScore(userId);
    }

    public double getUserAverageAcceptanceRate(int userId){
        return userService.userAverageOfferAcceptanceRate(userId);
    }

    public Map<String, Double> seeCategorySales() {
        return adminService.sortCategoriesByIncome();
    }



    //testing
    public int getUsersTotalNrOfSales(int userId){
        return userService.calculateNumberOfSales(userId);
    }

    public int getUserNegativeReviews(int userId){
        return userService.getUserNegativeReviews(userId);
    }

    public int getUserPositiveReviews(int userId){
        return userService.getUserPositiveReviews(userId);
    }

    public int getFlaggedActions(int userId){
        return userService.getNrOfFlaggedActions(userId);
    }

}










