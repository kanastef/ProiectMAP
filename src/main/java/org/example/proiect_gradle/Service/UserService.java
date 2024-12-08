package org.example.proiect_gradle.Service;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.proiect_gradle.Exceptions.EntityNotFoundException;
import org.example.proiect_gradle.Exceptions.BusinessLogicException;
import org.example.proiect_gradle.Exceptions.CustomException;



import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Repository.FileRepository.FileRepository;
import org.example.proiect_gradle.Repository.IRepository;

public class UserService extends VisitorService {

    protected final IRepository<Order> orderRepo;
    protected final IRepository<Offer> offerRepo;



    /**
     * Constructor for the UserService class. Initializes the service with the provided repositories for
     * users, products, reviews, orders, and offers.
     *
     * @param userRepo the repository to handle user-related operations
     * @param productRepo the repository to handle product-related operations
     * @param reviewRepo the repository to handle review-related operations
     * @param orderRepo the repository to handle order-related operations
     * @param offerRepo the repository to handle offer-related operations
     */
    public UserService(IRepository<User> userRepo, IRepository<Product> productRepo,
                       IRepository<Review> reviewRepo, IRepository<Category> categoryRepo,
                       IRepository<Order> orderRepo, IRepository<Offer> offerRepo) {
        super(userRepo, productRepo, reviewRepo, categoryRepo);
        this.orderRepo=orderRepo;
        this.offerRepo=offerRepo;

    }


    /**
     * Authenticates a user based on their username and password.
     *
     * @param userName the username of the user.
     * @param password the password of the user.
     * @return {@code true} if the user is authenticated; {@code false} otherwise.
     */
    public boolean authenticate(String userName, String password){
        List<User> users = userRepo.findByCriteria(user -> user.getUserName().equals(userName) && user.getPassword().equals(password));
        return !users.isEmpty();
    }


    //Offer Methods
    /**
     * Sends an offer from a buyer to a seller for a specific product.
     *
     * @param senderUsername the username of the sender making the offer.
     * @param senderPassword the password of the sender for authentication.
     * @param message a message included with the offer.
     * @param selectedProductID the id of the  product for which the offer is being made.
     * @param offeredPrice the price offered by the sender.
     * @return {@code true} if the offer is created and sent successfully; {@code false} otherwise.
     */

    public boolean sendOffer(String senderUsername,String senderPassword, String message, int selectedProductID, double offeredPrice) {
        try {
            if (authenticate(senderUsername, senderPassword)) {
                Product product = productRepo.read(selectedProductID);

                if (product == null) {
                    throw new EntityNotFoundException("Product with ID " + selectedProductID + " not found.");
                }
                User sender = findByCriteriaHelper(senderUsername, senderPassword);
                User offerReceiver = userRepo.read(product.getListedBy());

                if (offerReceiver == null) {
                    throw new EntityNotFoundException("Receiver user for product not found.");
                }

                if (!offerReceiver.getUserName().equals(senderUsername) && offeredPrice >= product.getPrice() / 2) {
                    Offer offer = new Offer(message, offeredPrice, selectedProductID, sender.getId(), offerReceiver.getId());
                    offerRepo.create(offer);

                    return true;
                }

            }
        }catch (BusinessLogicException e) {
            System.err.println("Error sending offer: " + e.getMessage());
        }
        return false;
    }


    /**
     * Accepts an offer made to a seller.
     *
     * @param sellerUsername the username of the seller.
     * @param sellerPassword the password of the seller for authentication.
     * @param offerId the ID of the offer to be accepted.
     * @return {@code true} if the offer is accepted; {@code false} otherwise.
     */
    public boolean acceptOffer(String sellerUsername, String sellerPassword, int offerId) {

        try {
            if (authenticate(sellerUsername, sellerPassword)) {
                Offer offer = offerRepo.read(offerId);

                if (offer == null) {
                    throw new EntityNotFoundException("Offer with ID " + offerId + " not found.");
                }

                if (offer.getReceiver() == findByCriteriaHelper(sellerUsername, sellerPassword).getId()) {
                    offer.setStatus(true);
                    Product targetedProduct = productRepo.read(offer.getTargetedProduct());

                    if (targetedProduct == null) {
                        throw new EntityNotFoundException("Product in offer not found.");
                    }

                    targetedProduct.setPrice(offer.getOfferedPrice());
                    offerRepo.update(offer);
                    productRepo.update(targetedProduct);
                    return true;
                }
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error accepting offer: " + e.getMessage());
        }
        return false;
    }


    /**
     * Declines an offer made to a seller.
     *
     * @param sellerUsername the username of the seller.
     * @param sellerPassword the password of the seller for authentication.
     * @param offerId the ID of the offer to be declined.
     * @return {@code true} if the offer is declined; {@code false} otherwise.
     */
    public boolean declineOffer(String sellerUsername, String sellerPassword, int offerId){

        try {
            if (authenticate(sellerUsername, sellerPassword)) {
                Offer offer = offerRepo.read(offerId);

                if (offer == null) {
                    throw new EntityNotFoundException("Offer with ID " + offerId + " not found.");
                }

                if (offer.getReceiver() == findByCriteriaHelper(sellerUsername, sellerPassword).getId()) {
                    offer.setStatus(false);
                    offerRepo.update(offer);
                    return true;
                }
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error declining offer: " + e.getMessage());
        }
        return false;
    }


    /**
     * Displays the offers made by the user.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of offers made by the user.
     */
    public List<Offer> displayMadeOffers(String username, String password) {
        List<Offer> personalOffers = new ArrayList<>();

        try{
            User user = findByCriteriaHelper(username, password);

            if (user == null) {
                throw new EntityNotFoundException("User not found.");
            }

            List<Offer> offers = offerRepo.getAll();
            for (Offer offer : offers) {
                if (offer.getSender() == user.getId()) {
                    personalOffers.add(offer);
                }
            }


        }catch (BusinessLogicException e) {
            System.err.println("Error displaying made offers: " + e.getMessage());
        }
        return personalOffers;
    }


    /**
     * Displays the offers received by the user.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of offers received by the user.
     */
    public List<Offer> displayReceivedOffers(String username, String password) {
        List<Offer> personalOffers = new ArrayList<>();

        try {
            User user = findByCriteriaHelper(username, password);
            if (user == null) {
                throw new EntityNotFoundException("User not found.");
            }
                List<Offer> offers = offerRepo.getAll();
                for (Offer offer : offers) {
                    if (offer.getSender() == user.getId()) {
                        personalOffers.add(offer);
                    }
                }


        }catch (BusinessLogicException e) {
            System.err.println("Error displaying received offers: " + e.getMessage());
        }

        return personalOffers;
    }


    /**
     * Displays all offers involving the user (both sent and received).
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of all offers involving the user.
     */
    public List<Offer> displayAllUserOffers(String username, String password) {
        List<Offer> personalOffers = new ArrayList<>();

        try {
            User user = findByCriteriaHelper(username, password);
            if (user == null) {
                throw new EntityNotFoundException("User not found.");
            }
                List<Offer> offers = offerRepo.getAll();
                for (Offer offer : offers) {
                    if (offer.getReceiver() == user.getId() || offer.getSender() == user.getId()) {
                        personalOffers.add(offer);
                    }
                }

        }catch(BusinessLogicException e){
            System.err.println("Error displaying all user offers: " + e.getMessage());
        }
        return personalOffers;
    }




    //Order

    /**
     * Places an order for a list of selected products.
     *
     * @param buyerUsername the username of the buyer.
     * @param buyerPassword the password of the buyer for authentication.
     * @param selectedProductsIds a list of product IDs for the order.
     * @param status the status of the order.
     * @param shippingAddress the shipping address for the order.
     * @return {@code true} if the order is placed successfully; {@code false} otherwise.
     */

    public boolean placeOrder(String buyerUsername, String buyerPassword, List<Integer> selectedProductsIds, String status, String shippingAddress) {
        try {
            if (authenticate(buyerUsername, buyerPassword)) {
                User buyer = findByCriteriaHelper(buyerUsername, buyerPassword);

                if (buyer == null) {
                    throw new EntityNotFoundException("Buyer not found.");
                }

                Map<Integer, List<Integer>> productsBySeller = new HashMap<>();


                for (Integer selectedProductsId : selectedProductsIds) {
                    System.out.println(selectedProductsId);
                    System.out.flush();
                    Product product = productRepo.read(selectedProductsId);

                    if (product == null) {
                        throw new EntityNotFoundException("Product with ID " + selectedProductsId + " not found.");
                    }

                    product.setAvailable(false);
                    productRepo.update(product);
                    productsBySeller.computeIfAbsent(product.getListedBy(), k -> new ArrayList<>()).add(selectedProductsId);
                }

                for (Map.Entry<Integer, List<Integer>> entry : productsBySeller.entrySet()) {
                    Integer seller = entry.getKey();
                    List<Integer> productsForSeller = entry.getValue();
                    double totalAmount = 0;
                    for (Integer integer : productsForSeller) {
                        Product product = productRepo.read(integer);

                        if (product == null) {
                            throw new EntityNotFoundException("Product with ID " + integer + " not found.");
                        }

                        totalAmount += product.getPrice();
                    }
                    Order order = new Order(productsForSeller, status, shippingAddress, buyer.getId(), seller);
                    order.setTotalPrice(totalAmount);
                    orderRepo.create(order);
                }
                return true;

            }
        }catch (BusinessLogicException e) {
            System.err.println("Error placing order: " + e.getMessage());
        }
        return false;

    }


    /**
     * Displays orders made by the user.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of orders made by the user.
     */
    public List<Order> displayMadeOrders(String username, String password){
        List<Order> personalOrders=new ArrayList<>();

        try {
            User user = findByCriteriaHelper(username, password);

            if (user == null || !authenticate(username, password)) {
                throw new EntityNotFoundException("Invalid user credentials.");
            }

            List<Order> orders = orderRepo.getAll();
            for (Order order : orders) {
                if (order.getBuyer() == user.getId()) {
                    personalOrders.add(order);
                }
            }

        }catch (BusinessLogicException e) {
            System.err.println("Error displaying made orders: " + e.getMessage());
        }
        return personalOrders;
    }


    /**
     * Displays orders received by the user.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of orders received by the user.
     */
    public List<Order> displayReceivedOrders(String username, String password){
        List<Order> personalOrders=new ArrayList<>();

        try {
            User user = findByCriteriaHelper(username, password);

            if (user == null || !authenticate(username, password)) {
                throw new EntityNotFoundException("Invalid user credentials.");
            }

            List<Order> orders = orderRepo.getAll();
            for (Order order : orders) {
                if (order.getSeller() == user.getId()) {
                    personalOrders.add(order);
                }
            }

        }catch (BusinessLogicException e) {
            System.err.println("Error displaying received orders: " + e.getMessage());
        }
        return personalOrders;
    }


    /**
     * Displays all orders involving the user (both made and received).
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of all orders involving the user.
     */

    public List<Order> displayAllUsersOrders(String username, String password){
        List<Order> personalOrders=new ArrayList<>();

        try {
            User user = findByCriteriaHelper(username, password);
            if (user == null || !authenticate(username, password)) {
                throw new EntityNotFoundException("Invalid user credentials.");
            }
                List<Order> orders = orderRepo.getAll();
                for (Order order : orders) {
                    if (order.getSeller() == user.getId() || order.getBuyer() == user.getId()) {
                        personalOrders.add(order);
                    }
                }

        }catch (BusinessLogicException e) {
            System.err.println("Error displaying all users' orders: " + e.getMessage());
        }
        return personalOrders;
    }


    //Review


    /**
     * Writes a review from one user to another.
     *
     * @param reviewerUsername the username of the reviewer.
     * @param reviewerPassword the password of the reviewer for authentication.
     * @param grade the grade for the review.
     * @param message the message content of the review.
     * @param revieweeId the ID of the user being reviewed.
     * @return {@code true} if the review is written successfully; {@code false} otherwise.
     */
    public boolean writeReview(String reviewerUsername, String reviewerPassword, double grade, String message, int revieweeId ){
        try {
            if (authenticate(reviewerUsername, reviewerPassword)) {
                User reviewer = findByCriteriaHelper(reviewerUsername, reviewerPassword);
                User reviewee = userRepo.read(revieweeId);

                if (reviewee == null) {
                    throw new EntityNotFoundException("Reviewee user not found.");
                }

                if (!reviewee.getUserName().equals(reviewerUsername)) {
                    for (Order order : displayMadeOrders(reviewerUsername, reviewerPassword)) {
                        if (order.getSeller() == reviewee.getId()) {
                            Review review = new Review(grade, message, reviewer.getId(), reviewee.getId());
                            reviewRepo.create(review);
                            return true;
                        }
                    }

                }
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error writing review: " + e.getMessage());
        }
        return false;

    }


    /**
     * Deletes a review made by the user.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @param reviewId the ID of the review to be deleted.
     * @return {@code true} if the review is deleted successfully; {@code false} otherwise.
     */

    public boolean deleteReview(String username, String password,int reviewId) {
        try {
            if (authenticate(username, password)) {
                Review review = reviewRepo.read(reviewId);
                if (review == null) {
                    throw new EntityNotFoundException("Review with ID " + reviewId + " not found.");
                }

                reviewRepo.delete(reviewId);
                return true;
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        }
        return false;
    }



    /**
     * Displays personal reviews made by the user.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of reviews made by the user.
     */

    public List<Review> displayMadePersonalReviews(String username, String password){
        List<Review> personalReviews=new ArrayList<>();
        try {
            User user = findByCriteriaHelper(username, password);
            if (user != null) {
                List<Review> reviews = reviewRepo.getAll();
                for (Review review : reviews) {
                    if (review.getReviewer() == user.getId()) {
                        personalReviews.add(review);
                    }
                }
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error displaying made personal reviews: " + e.getMessage());
        }
        return personalReviews;
    }


    //Favorites



    /**
     * Adds a product to the user's favorites.
     *
     * @param userName the username of the user.
     * @param password the password of the user for authentication.
     * @param productId the ID of the product to be added to favorites.
     * @return {@code true} if the product is added successfully; {@code false} otherwise.
     */
    public boolean addToFavorites(String userName, String password,int productId){
        try {
            if (authenticate(userName, password)) {
                User user = findByCriteriaHelper(userName, password);
                Product product = productRepo.read(productId);

                if (product == null) {
                    throw new EntityNotFoundException("Product with ID " + productId + " not found.");
                }

                if (!user.getFavourites().contains(productId)) {
                    user.getFavourites().add(productId);
                    System.out.println(user.getFavourites());
                    userRepo.update(user);
                    int newNrOfLikes = product.getNrLikes() + 1;
                    product.setNrLikes(newNrOfLikes);
                    productRepo.update(product);
                    return true;
                }
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error adding to favorites: " + e.getMessage());
        }
        return false;

    }


    /**
     * Removes a product from the user's favorites.
     *
     * @param userName the username of the user.
     * @param password the password of the user for authentication.
     * @param productId the ID of the product to be removed from favorites.
     * @return {@code true} if the product is removed successfully; {@code false} otherwise.
     */

    public boolean removeFromFavourites(String userName,String password, int productId){
        try {
            if (authenticate(userName, password)) {
                User user = findByCriteriaHelper(userName, password);
                Product product = productRepo.read(productId);

                if (product == null) {
                    throw new EntityNotFoundException("Product with ID " + productId + " not found.");
                }
                List<Integer> favs = user.getFavourites();
                if (user.getFavourites().contains(productId)) {
                    user.getFavourites().remove((Integer)productId);
                    userRepo.update(user);
                    int newNrOfLikes = product.getNrLikes() - 1;
                    product.setNrLikes(newNrOfLikes);
                    productRepo.update(product);
                    return true;
                }

            }
        }catch (BusinessLogicException e) {
            System.err.println("Error removing from favorites: " + e.getMessage());
        }
        return false;

    }



    /**
     * Displays the user's favorite products.
     *
     * @param userName the username of the user.
     * @param password the password of the user for authentication.
     * @return a list of the user's favorite products.
     */
    public List<Product> displayFavourites(String userName, String password){
        User user=findByCriteriaHelper(userName,password);
        List<Product> favourites = new ArrayList<>();
        if(user!=null){
            for (int i = 0; i < user.getFavourites().size(); i++) {
                Product product=productRepo.read(user.getFavourites().get(i));
                favourites.add(product);
            }
        }
        return favourites;
    }



    //Product


    /**
     * Lists a new product for sale by the user.
     *
     * @param userName the username of the seller.
     * @param password the password of the seller for authentication.
     * @param category the category of the product.
     * @param name the name of the product.
     * @param color the color of the product.
     * @param size the size of the product.
     * @param price the price of the product.
     * @param brand the brand of the product.
     * @param condition the condition of the product.
     * @param nrOfViews the number of views the product has.
     * @param nrOfLikes the number of likes the product has.
     * @return {@code true} if the product is listed successfully; {@code false} otherwise.
     */
    public boolean listProduct(String userName,String password, int category,String name,String color, int size, double price, String brand, String condition, int nrOfViews, int nrOfLikes){
        try {
            if (authenticate(userName, password)) {
                User seller = findByCriteriaHelper(userName, password);

                Product product = new Product(name, color, size, price, brand, condition, nrOfViews, nrOfLikes, seller.getId());
                product.setCategory(category);
                productRepo.create(product);

                seller.getListedProducts().add(product.getId());
                userRepo.update(seller);
                return true;
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error listing product: " + e.getMessage());
        }
        return false;
    }



    /**
     * Deletes a product listed by the user.
     *
     * @param username the username of the seller.
     * @param password the password of the seller for authentication.
     * @param productId the ID of the product to be deleted.
     * @return {@code true} if the product is deleted successfully; {@code false} otherwise.
     */
    public boolean deleteListedProduct(String username,String password,int productId){
        try {
            if (authenticate(username, password)) {
                User user = findByCriteriaHelper(username, password);
                if (user == null) {
                    throw new EntityNotFoundException("User not found.");
                }

                for (int i = 0; i < user.getListedProducts().size(); i++) {
                    if (user.getListedProducts().get(i) == productId) {
                        user.getListedProducts().remove(i);
                        productRepo.delete(productId);
                        return true;
                    }
                }
                throw new EntityNotFoundException("Product with ID " + productId + " not listed by the user.");
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error deleting listed product: " + e.getMessage());
        }

        return false;

    }


    /**
     * Finds a user by their username and password.
     *
     * @param username the username of the user.
     * @param password the password of the user for authentication.
     * @return the user if found and authenticated; {@code null} otherwise.
     */
    public User findByCriteriaHelper(String username,String password){
        if(authenticate(username,password)){
            List<User> users=userRepo.findByCriteria(user -> user.getUserName().equals(username));
            return users.getFirst();
        }
        return null;
    }

    /**
     * Calculates the user's average offer acceptance rate.
     *
     * @param userId the ID of the user.
     * @return the acceptance rate as a percentage. Returns 0 if no offers are received.
     */
    public double userAverageOfferAcceptanceRate(int userId){
        try {
            User user = userRepo.read(userId);
            if (user == null) {
                throw new EntityNotFoundException("User with ID " + userId + " not found.");
            }

            List<Offer> receivedOffers = new ArrayList<>();
            List<Offer> offers = offerRepo.getAll();
            for (Offer offer : offers) {
                if (offer.getReceiver() == user.getId()) {
                    receivedOffers.add(offer);
                }

            }


            if (receivedOffers.isEmpty()) {
                return 0;
            }
            int nrOfAcceptedOffers = 0;
            for (Offer offer : receivedOffers) {

                if (offer.getStatus()) {
                    nrOfAcceptedOffers++;
                }
            }
            return ((double) nrOfAcceptedOffers / receivedOffers.size()) * 100;
        }catch (BusinessLogicException e) {
            System.err.println("Error calculating average offer acceptance rate: " + e.getMessage());
            return 0.0;
        }


    }

    /**
     * Retrieves the list of products listed by a user based on their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A list of products the user has listed, or an empty list if the user is not found or has no listed products.
     */
    public List<Product> getMyListedProducts(String username, String password) {
        List<Product> listedProducts = new ArrayList<>();

        try {
            User user = findByCriteriaHelper(username, password);
            if (user != null) {
                for (int i = 0; i < user.getListedProducts().size(); i++) {
                    Product product = productRepo.read(user.getListedProducts().get(i));
                    listedProducts.add(product);
                }
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error retrieving listed products: " + e.getMessage());
        }
        return listedProducts;
    }

    /**
     * Calculates the number of negative reviews received by a user.
     * A review is considered negative if its grade is 3.5 or below.
     *
     * @param userId The ID of the user for whom the count of negative reviews is being calculated.
     * @return The total number of negative reviews for the specified user.
     */
    public int getUserNegativeReviews(int userId){
        try {
            User user = userRepo.read(userId);
            if (user == null) {
                throw new EntityNotFoundException("User with ID " + userId + " not found.");
            }

            int nrOfNegativeReviews = 0;
            for (Review review : reviewRepo.getAll()) {
                if (review.getReviewee() == user.getId() && review.getGrade() <= 3.5) {
                    nrOfNegativeReviews++;
                }
            }
            return nrOfNegativeReviews;
        }catch (BusinessLogicException e) {
            System.err.println("Error counting negative reviews: " + e.getMessage());
            return 0;
        }


    }

    /**
     * Calculates the number of negative reviews received by a user.
     * A review is considered negative if its grade is 3.5 or below.
     *
     * @param userId The ID of the user for whom the count of negative reviews is being calculated.
     * @return The total number of negative reviews for the specified user.
     */
    public int getUserPositiveReviews(int userId){

        int nrOfPositiveReviews=0;
        User user=userRepo.read(userId);
        for(Review review:reviewRepo.getAll()){
            if (review.getReviewee() == user.getId() && review.getGrade()>3.5){
                nrOfPositiveReviews++;
            }
        }
        return nrOfPositiveReviews;

    }

    /**
     * Displays the reviews for a user's profile based on their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A list of reviews for the user’s profile, or an empty list if the user is not found or has no reviews.
     */
    public List<Review> displayProfileReviews(String username, String password) {
        List<Review> profileReviews=new ArrayList<>();
        try {
            User user = findByCriteriaHelper(username, password);
            if (user != null) {
                List<Review> reviews = reviewRepo.getAll();
                for (Review review : reviews) {
                    if (review.getReviewee() == user.getId()) {
                        profileReviews.add(review);
                    }
                }
                return profileReviews;
            }
        }catch (BusinessLogicException e) {
            System.err.println("Error displaying profile reviews: " + e.getMessage());
        }
        return new ArrayList<>();
    }



    /**
     * Calculates the number of individual sales made by a user.
     * Each product sold, even within the same order, counts as a separate sale.
     *
     * @param userId The user whose sales are being calculated.
     * @return The total number of individual sales made by the user.
     */
    public int calculateNumberOfSales(int userId) {
        User user=userRepo.read(userId);

        int totalSales = 0;
        for (Order order : orderRepo.getAll()) {
            for (int i = 0; i < order.getProducts().size(); i++) {
                Product product=productRepo.read(order.getProducts().get(i));
                if (product.getListedBy() == user.getId()) {
                    totalSales++;
                }
            }
        }
        return totalSales;
    }


    /**
     * Calculates the trust score for a user based on their activity and reputation.
     * The score is computed using the following factors:
     * <ul>
     *   <li>The number of sales made by the user (each sale contributes 10 points).</li>
     *   <li>The number of negative reviews received by the user (each negative review contributes 5 points).</li>
     *   <li>The number of flagged actions associated with the user (each flagged action subtracts from the score).</li>
     * </ul>
     */
    public int calculateUserTrustScore(int userId){
        try {
            User user = userRepo.read(userId);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + userId + " not found.");
            }

            int score = calculateNumberOfSales(userId) * 10;

            score += getUserPositiveReviews(userId) * 5;
            score -= getUserNegativeReviews(userId) * 15;
            score -= userRepo.read(userId).getFlaggedActions();
            return score;
        }catch (BusinessLogicException e) {
            System.err.println("Error calculating user trust score: " + e.getMessage());
            return 0;
        }
    }

    public int getNrOfFlaggedActions(int userId){
        User user= userRepo.read(userId);
        return user.getFlaggedActions();
    }


    /**
     * Retrieves the engagement score for a user based on their username and password.
     * This method authenticates the user and returns their current score if valid.
     *
     * @param username The username of the user whose score is being retrieved.
     * @param password The password of the user for authentication.
     * @return The user's engagement score if the user is found and authenticated;
     *         otherwise, returns 0 if the user is not found or authentication fails.
     */
    public double getMyScore(String username, String password) {
        User user=findByCriteriaHelper(username,password);
        if (user != null)
            return user.getScore();
        else return 0;
    }
}


