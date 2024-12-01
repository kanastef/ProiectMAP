package org.example.proiect_gradle.Presentation;
import org.example.proiect_gradle.Controller.Controller;
import org.example.proiect_gradle.Domain.*;
import java.util.*;

public class ConsoleApp {
    private final Controller controller;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Marketplace App!");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("3. Browse products");
            System.out.println("4. Browse users");
            System.out.println("0. Exit");
            System.out.print("Please select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1-> logIn();
                case 2-> signUp();
                case 3-> browseProductsVisitor();
                case 4-> browseUsersVisitor();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    //VISITOR

    private void signUp() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your email address: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Please enter your phone number: ");
        String phoneNumber = scanner.nextLine();
        boolean success = controller.createAccount(username, password, email, phoneNumber);
        if (success){
            System.out.println("Domain.Account created successfully! Please log in to continue.");
        }
        else {
            System.out.println("Something went wrong. Please try again.");
        }
    }

    private void logIn() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        int result = controller.logIn(username, password);
        if (result == 1) {
            userMenu(username, password);
        }
        if (result == 2) {
            adminMenu(username, password);
        }
        else {
            System.out.println("Login failed. Please try again.");
        }

    }

    private void browseProductsVisitor() {
        boolean browsing = true;
        List<Product> products = new ArrayList<>();

        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> products = sortProducts();
                case 2 -> products = filterProducts();
                case 0 -> browsing = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void browseUsersVisitor() {
        boolean browsing = true;
        List<User> displayedUsers = new ArrayList<>();
        while (browsing) {
            System.out.println("User Browsing Options: ");
            System.out.println("1. Sort Users");
            System.out.println("2. Filter Users");
            System.out.println("3. View User Reviews");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> displayedUsers = sortUsers();
                case 2 -> displayedUsers = filterUsers();
                case 3 -> viewUserReviews(displayedUsers);
                case 0 -> browsing = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

//USER
    private void userMenu(String username, String password) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Welcome to your profile!");
            System.out.println("1. Browse Products");
            System.out.println("2. Browse Users");
            System.out.println("3. View My Listings");
            System.out.println("4. View My Orders");
            System.out.println("5. View Received Orders");
            System.out.println("6. View Received Offers");
            System.out.println("7. View Sent Offers");
            System.out.println("8. View My Reviews");
            System.out.println("9. View My Liked Products");
            System.out.println("0. Log Out");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> browseProductsUser(username, password);
                case 2 -> browseUsersUser(username, password);
                case 3 -> viewMyListings(username, password);
                case 4 -> viewMyOrders(username, password);
                case 5 -> viewReceivedOrders(username, password);
                case 6 -> viewOffers(username, password);
                case 7 -> viewSentOffers(username, password);
                case 8 -> viewMyReviews(username, password);
                case 9 -> viewLikes(username, password);
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewLikes(String username, String password) {
        List<Product> liked = controller.displayLikedProducts(username, password);
        for (Product product : liked) {
            System.out.println(product);
        }
        System.out.println("Would you like to remove a product from your likes?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice = scanner.nextInt();
        while (choice != 0) {
            switch (choice) {
                case 1 -> removeLike(liked, username, password);
                case 2 -> {
                    System.out.println(controller.userService.findByCriteriaHelper(username, password).getFavourites());
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            choice = scanner.nextInt();
        }
    }

    private void removeLike(List<Product> likedProducts, String username, String password) {
        System.out.println("Enter the ID of the product you would like to delete: ");
        int id = scanner.nextInt();
        if (likedProducts.stream().map(Product::getId).anyMatch(x -> x.equals(id))) {
            boolean success = controller.removeFromLiked(username, password, id);
            if (success) {
                System.out.println("Product deleted successfully!");
            }
            else {
                System.out.println("Something went wrong.");
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void viewMyReviews(String username, String password) {
        System.out.println("1. View Reviews Left by You");
        System.out.println("2. View Other Users Reviews for You");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> reviewsByMe(username, password);
            case 2 -> reviewsForMe(username, password);
        }
    }

    private void reviewsByMe(String username, String password) {
        List<Review> reviews = controller.displayReviewsLeftByUser(username, password);
        if (reviews.isEmpty()) {
            System.out.println("No reviews found. Please try again.");
        }
        else {
            for (Review review : reviews) {
                System.out.println(review);
            }
            System.out.println("Would you like to delete any of the reviews you have made?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> deleteMyReview(username, password, reviews);
                case 2 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void deleteMyReview(String username, String password, List<Review> reviews) {
        System.out.println("Enter the ID of the review you would like to delete: ");
        int id = scanner.nextInt();
        if (reviews.stream().map(Review::getId).anyMatch(x -> x.equals(id))) {
            boolean success = controller.deleteReview(username, password, id);
            if (success) {
                System.out.println("Review deleted successfully!");
            }
            else {
                System.out.println("Something went wrong.");
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void reviewsForMe(String username, String password) {
        System.out.println("Your rating is: ");
        double rating = controller.getProfileScore(username, password);
        System.out.println(rating);
        scanner.nextLine();
        List<Review> reviews = controller.displayReviewsForMe(username, password);
        if (reviews.isEmpty()) {
            System.out.println("No reviews found. Please try again.");
        }
        else {
            for (Review review : reviews) {
                System.out.println(review);
            }
        }
    }

    private void viewSentOffers(String username, String password) {
        List<Offer> madeOffers = controller.getMadeOffers(username, password);
        if (madeOffers.isEmpty()) {
            System.out.println("No sent offers found.");
        }
        else {
            for (Offer offer : madeOffers) {
                System.out.println(offer);
            }
        }
    }

    private void viewOffers(String username, String password) {
        List<Offer> offers = controller.displayReceivedOffers(username, password);
        if (offers.isEmpty()) {
            System.out.println("You have no offers.");
            return;
        }
        boolean managingOffers = true;
        while (managingOffers) {
            System.out.println("Your Offers:");
            for (int i = 0; i < offers.size(); i++) {
                System.out.println((i + 1) + ". " + offers.get(i));
            }
            System.out.println("0. Go Back");

            System.out.print("Select an offer ID to accept/decline or go back: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) {
                managingOffers = false;
            }
            else if (offers.stream().map(Offer::getId).anyMatch(x -> x.equals(choice))) {
                for (Offer offer: offers) {
                    if (offer.getId() == choice) {
                        Offer selectedOffer = offers.get(choice - 1);
                        System.out.println("You selected offer: " + selectedOffer);
                        System.out.println("1. Accept Offer");
                        System.out.println("2. Decline Offer");
                        System.out.print("Choose an option: ");
                        int action = scanner.nextInt();
                        scanner.nextLine();
                        switch (action) {
                            case 1 -> acceptOffer(username, password, selectedOffer);
                            case 2 -> declineOffer(username, password, selectedOffer);
                            default -> System.out.println("Invalid action. Returning to offers menu.");
                        }
                    }
                }
            }
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void sendOffer(String username, String password, int selectedProduct) {
        System.out.print("Enter your offer amount: ");
        double offerAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter your offer message: ");
        String message = scanner.nextLine();
        boolean success = controller.sendOffer(username, password, message, selectedProduct, offerAmount);
        if (success) {
            System.out.println("Offer sent successfully!");
        } else {
            System.out.println("Could not send offer. Please try again.");
        }
    }

    private void declineOffer(String username, String password,Offer selectedOffer) {
        boolean success = controller.declineOffer(username, password, selectedOffer.getId());
        if (success) {
            System.out.println("Offer declined successfully!");
        }
        else {
            System.out.println("Offer decline failed! Please try again.");
        }
    }

    private void acceptOffer(String username, String password, Offer selectedOffer) {
        boolean success = controller.acceptOffer(username, password, selectedOffer.getId());
        if (success) {
            System.out.println("Offer accepted successfully!");
        }
        else {
            System.out.println("Offer accept failed! Please try again.");
        }
    }

    private void viewReceivedOrders(String username, String password) {
        List<Order> orders = controller.getReceivedOrders(username, password);
        if (orders.isEmpty()) {
            System.out.println("You have no orders.");
            return;
        }
        System.out.println("Your Orders:");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    private void viewMyOrders(String username, String password) {
        List<Order> orders = controller.getMadeOrders(username, password);
        if (orders.isEmpty()) {
            System.out.println("You have no orders.");
            return;
        }
        System.out.println("Your Orders:");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    private void browseProductsUser(String username, String password) {
        boolean browsing = true;
        List<Product> products = new ArrayList<>();

        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("3. Select Product for Action (like, offer)");
            System.out.println("4. Place an Order");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> products = sortProducts();
                case 2 -> products = filterProducts();
                case 3 -> selectProductAction(username, password, products);
                case 4 -> makeOrder(username, password, products);
                case 0 -> browsing = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private List<Product> sortProducts() {
        System.out.println("Sort Products by:");
        System.out.println("1. Price");
        System.out.println("2. Likes");
        System.out.println("3. Size");
        System.out.println("4. Views");
        System.out.println("5. All products");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Sort in:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Choose an option: ");

        int order = scanner.nextInt();
        scanner.nextLine();

        List<Product> sortedProducts = controller.sortProducts(choice, order);
        sortedProducts.forEach(System.out::println);
        return sortedProducts;
    }

    private List<Product> filterProducts() {
        System.out.println("Filter Products by: ");
        System.out.println("1. Domain.Category");
        System.out.println("2. Brand");
        System.out.println("3. Color");
        System.out.println("4. Seller");
        System.out.println("5. Likes");
        System.out.println("6. Condition");
        System.out.println("7. Price");
        System.out.println("8. Size");
        System.out.println("9. Views");
        System.out.println("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        List<Product> filteredProducts = new ArrayList<>();
        switch (choice) {
            case 1 -> {
                List<Category> categories = controller.getCategories();
                categories.forEach(System.out::println);
                System.out.println("Type a category ID to filter by: ");
                int category = scanner.nextInt();
                if (categories.stream().map(Category::getId).anyMatch(x -> x.equals(category)))
                    filteredProducts = controller.filterProductsByCategory(category);
                else System.out.println("Invalid category ID. Please try again.");
            }
            case 2 -> {
                System.out.println("Type a brand name to filter by: ");
                String brand = scanner.nextLine();
                filteredProducts = controller.filterProductsByBrand(brand);
            }
            case 3 -> {
                System.out.println("Type a color to filter by: ");
                String color = scanner.nextLine();
                filteredProducts = controller.filterProductsByColor(color);
            }
            case 4 -> {
                System.out.println("Type a seller name to filter by: ");
                String name = scanner.nextLine();
                filteredProducts = controller.filterProductsByUserName(name);
            }
            case 5 -> {
                System.out.println("Type the like count range to filter by: ");
                int countMin = scanner.nextInt();
                int countMax = scanner.nextInt();
                filteredProducts = controller.filterProductsByLikes(countMin, countMax);
            }
            case 6 -> {
                System.out.println("Type a condition to filter by: ");
                String condition = scanner.nextLine();
                filteredProducts = controller.filterProductsByCondition(condition);
            }
            case 7 -> {
                System.out.println("Type a price range to filter by: ");
                int priceMin = scanner.nextInt();
                int priceMax = scanner.nextInt();
                filteredProducts = controller.filterProductsByPriceRange(priceMin, priceMax);
            }
            case 8 -> {
                System.out.println("Type a size range to filter by: ");
                int minSize = scanner.nextInt();
                int maxSize = scanner.nextInt();
                filteredProducts = controller.filterProductsBySizeRange(minSize, maxSize);
            }
            case 9 -> {
                System.out.println("Type a views range to filter by: ");
                int viewMin = scanner.nextInt();
                int viewMax = scanner.nextInt();
                filteredProducts = controller.filterProductsByViewRange(viewMin, viewMax);
            }
        }
        filteredProducts.forEach(System.out::println);
        return filteredProducts;
    }

    private void selectProductAction(String username, String password,List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products to select. Please search or filter products first.");
            return;
        }
        System.out.print("Enter Product ID to select for action: ");
        int productId = scanner.nextInt();
        if (products.stream().map(Product::getId).anyMatch(x -> x.equals(productId))) {
            for (Product product: products)
                if (product.getId() == productId){
                    scanner.nextLine();
                    System.out.println("Choose Action for Product:");
                    System.out.println("1. Like Product");
                    System.out.println("2. Send Offer");
                    System.out.print("Enter your choice: ");

                    int actionChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch (actionChoice) {
                        case 1 -> likeProducts(username, password, product);
                        case 2 -> sendOffer(username, password, product.getId());
                        default -> System.out.println("Invalid action choice. Please try again.");
                    }
                }
        }
    }


    private void likeProducts(String username, String password,Product selectedProduct) {
        boolean success = controller.likeProduct(username, password, selectedProduct.getId());
        if (success) {
            System.out.println("You liked the product: " + selectedProduct.getName());
        } else {
            System.out.println("Could not like the product. Please try again.");
        }
    }

    private void makeOrder(String username, String password, List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products to select. Please search or filter products first.");
            return;
        }
        System.out.print("Enter Product IDs to add to your order. Press 0 to stop.");
        int option = scanner.nextInt();
        Map<Integer, List<Integer>> orderedProducts = new HashMap<>();
        while (option != 0) {
            int finalOption = option;
            if(products.stream().map(Product::getId).anyMatch(x -> x.equals(finalOption))) {
                for (Product product : products) {
                    if (product.getId() == finalOption && product.isAvailable()) {
                        if (!orderedProducts.containsKey(product.getListedBy())) {
                            orderedProducts.put(product.getListedBy(), new ArrayList<>());
                        }
                        if (!orderedProducts.get(product.getListedBy()).contains(finalOption)) {
                            orderedProducts.get(product.getListedBy()).add(finalOption);
                        }
                    }
                }
            }
            option = scanner.nextInt();
        }
        scanner.nextLine();
        if (!orderedProducts.isEmpty()) {
            System.out.println("Enter your address: ");
            String address = scanner.nextLine();
            for (int sellerId : orderedProducts.keySet()) {
                boolean success = controller.makeOrder(username, password, orderedProducts.get(sellerId),
                        "processing", address);
                if (success) {
                    System.out.println("Order placed successfully!");
                } else {
                    System.out.println("Could not place order. Please try again.");
                }
            }
        }

    }

    private void viewMyListings(String username, String password) {
        System.out.println("Your Current Listings:");
        List<Product> myListings = controller.getMyListings(username, password);
        if (myListings.isEmpty()) {
            System.out.println("You have no products listed.");
        }
        else{
            myListings.forEach(System.out::println);
        }
        boolean managingListings = true;
        while (managingListings) {
            System.out.println("\nOptions:");
            System.out.println("1. Add Product to My Listings");
            System.out.println("2. Delete Product from My Listings");
            System.out.println("0. Back to Repo.Main Menu");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> addProductToMyListings(username, password);
                case 2 -> deleteProductFromMyListings(username, password, myListings);
                case 0 -> managingListings = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addProductToMyListings(String username, String password) {
        System.out.println("Enter product details to add to your listings:");
        List<Category> categories = controller.getCategories();
        System.out.println("Choose a category ID: ");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(categories.get(i));
        }
        int category = scanner.nextInt();
        if (categories.stream().map(Category::getId).anyMatch(x -> x.equals(category))){
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Color: ");
            String color = scanner.nextLine();
            System.out.print("Size: ");
            int size = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Price: ");
            double price = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Brand: ");
            String brand = scanner.nextLine();
            System.out.print("Condition (e.g., New, Used): ");
            String condition = scanner.nextLine();

            boolean success = controller.addToUserListings(username, password, category, name, color, size, price,
                    brand, condition, 0, 0);
            if (success) {
                System.out.println("Product added to your listings successfully!");
            } else {
                System.out.println("Could not add product to your listings. Please try again.");
            }
        }
        else System.out.println("Invalid category choice. Please choose a valid category ID.");

    }

    private void deleteProductFromMyListings(String username, String password, List<Product> myListings) {
        System.out.print("Enter the ID of the product you wish to delete: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        if(myListings.stream().map(Product::getId).anyMatch(x -> x.equals(productId))) {
            boolean success = controller.removeFromUserListings(username, password, productId);
            if (success) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product deletion failed.");
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void browseUsersUser(String username, String password) {
        boolean browsing = true;
        List<User> displayedUsers = new ArrayList<>();
        while (browsing) {
            System.out.println("User Browsing Options: ");
            System.out.println("1. Sort Users");
            System.out.println("2. Filter Users");
            System.out.println("3. Select User for Review");
            System.out.println("4. View User Reviews");
            System.out.println("5. View User Listings");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> displayedUsers = sortUsers();
                case 2 -> displayedUsers = filterUsers();
                case 3 -> selectUserForReview(username, password, displayedUsers);
                case 4 -> viewUserReviews(displayedUsers);
                case 5 -> viewUserListings(displayedUsers);
                case 0 -> browsing = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private List<User> sortUsers() {
        System.out.println("Sort Users by:");
        System.out.println("1. Review Count");
        System.out.println("2. Name");
        System.out.println("3. Score");
        System.out.println("4. All Users");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Sort in:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Choose an option: ");

        int order = scanner.nextInt();
        scanner.nextLine();



        List<User> sortedUsers = controller.sortUsers(choice,order);
        sortedUsers.forEach(System.out::println);
        return sortedUsers;
    }

    private List<User> filterUsers() {
        System.out.println("Filter Users by:");
        System.out.println("1. Minimum Review Count");
        System.out.println("2. Name");
        System.out.println("3. Minimum Score");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        List<User> filteredUsers = new ArrayList<>();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter a Minimum Review Count: ");
                int minReviewCount = scanner.nextInt();
                filteredUsers = controller.filterUsersByMinimumReviewCount(minReviewCount);}
            case 2 -> {
                System.out.println("Enter a Name: ");
                String name = scanner.nextLine();
                filteredUsers = controller.filterUsersByName(name);}
            case 3 -> {
                System.out.println("Enter a Minimum Score: ");
                double minScore = scanner.nextInt();
                filteredUsers = controller.filterUsersByMinimumScore(minScore);}
        }
        filteredUsers.forEach(System.out::println);
        return filteredUsers;
    }

    private void viewUserReviews(List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        System.out.print("Enter User ID to see their rating: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        List<Review> reviews = controller.displayReviewsLeftForUser(userId);
        if(displayedUsers.stream().map(User::getId).anyMatch(x -> x.equals(userId))) {
            System.out.println("Users trust score is: ");
            System.out.println(controller.getUserTrustScore(userId));
            for (Review review : reviews) {
                System.out.println(review);
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void selectUserForReview(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        System.out.print("Enter User ID to leave a review for: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        if(displayedUsers.stream().map(User::getId).anyMatch(x -> x.equals(userId))) {
            System.out.print("Enter review content: ");
            String content = scanner.nextLine();
            System.out.print("Enter rating (1-5): ");
            double rating = scanner.nextInt();
            scanner.nextLine();
            boolean success = controller.writeReview(username, password, rating, content, userId);
            if (success) {
                System.out.println("Review added successfully.");
            } else {
                System.out.println("Review failed. Please try again.");
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void viewUserListings(List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        System.out.print("Enter User ID to view their listings: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        if(displayedUsers.stream().map(User::getId).anyMatch(x -> x.equals(userId))) {
            List<Product> userProducts = controller.getUserListing(userId);
            if (userProducts.isEmpty()) {
                System.out.println("This user has no listed products.");
            } else {
                userProducts.forEach(System.out::println);
            }
        }
        else System.out.println("Invalid ID.");
    }

//ADMIN

    private void adminMenu(String username, String password) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Users");
            System.out.println("0. Log Out");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> manageProducts(username, password);
                case 2 -> manageUsers(username, password);
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void manageUsers(String username, String password) {
        boolean browsing = true;
        List<User> displayedUsers = new ArrayList<>();
        while (browsing) {
            System.out.println("User Browsing Options: ");
            System.out.println("1. Sort Users");
            System.out.println("2. Filter Users");
            System.out.println("3. Delete User");
            System.out.println("4. Manage User Reviews");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> displayedUsers = sortUsers();
                case 2 -> displayedUsers = filterUsers();
                case 3 -> deleteUser(username, password, displayedUsers);
                case 4 -> manageReviews(username, password, displayedUsers);
                case 0 -> browsing = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageReviews(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        System.out.println("Manage User Reviews Options: ");
        System.out.println("1. Delete Review Made by a User");
        System.out.println("2. Delete Review Left for a User");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> deleteReviewMadeByUser(username, password, displayedUsers);
            case 2 -> deleteReviewLeftForUser(username, password, displayedUsers);
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void deleteReviewLeftForUser(String username, String password, List<User> displayedUsers) {
        System.out.println("Select a User to see reviews left for them:");
        for (int i = 0; i < displayedUsers.size(); i++) {
            System.out.println((i + 1) + ". " + displayedUsers.get(i));
        }
        System.out.print("Enter the ID of the user to manage: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        if(displayedUsers.stream().map(User::getId).anyMatch(x -> x.equals(userId))) {
            List<Review> reviewsLeftForUser = controller.displayReviewsLeftForUser(userId);
            if (reviewsLeftForUser.isEmpty()) {
                System.out.println("No reviews found left for this user.");
                return;
            }
            System.out.println("Reviews left for this user: ");
            System.out.println("Reviews: ");
            for (Review review : reviewsLeftForUser) {
                System.out.println(review);
            }
            System.out.print("Enter Review ID to delete: ");
            int reviewId = scanner.nextInt();
            scanner.nextLine();
            if(reviewsLeftForUser.stream().map(Review::getId).anyMatch(x -> x.equals(reviewId))) {
                boolean success = controller.deleteReviewAdmin(username, password, reviewId);
                if (success) {
                    System.out.println("Review deleted successfully.");
                } else {
                    System.out.println("Failed to delete review. Please check the Review ID.");
                }
            }
            else System.out.println("Invalid ID.");
        }
        else System.out.println("Invalid ID.");
    }

    private void deleteReviewMadeByUser(String username, String password, List<User> displayedUsers) {
        System.out.println("Select a User to see reviews they have made:");
        for (int i = 0; i < displayedUsers.size(); i++) {
            System.out.println((i + 1) + ". " + displayedUsers.get(i));
        }
        System.out.print("Enter the ID of the user to manage: ");
        int userChoice = scanner.nextInt();
        scanner.nextLine();
        if(displayedUsers.stream().map(User::getId).anyMatch(x -> x.equals(userChoice))) {
            List<Review> reviewsLeftByUser = controller.displayReviewsLeftByUser(displayedUsers.get(userChoice)
                    .getUserName(), displayedUsers.get(userChoice).getPassword());
            if (reviewsLeftByUser.isEmpty()) {
                System.out.println("No reviews made by this user.");
                return;
            }
            System.out.println("Reviews made by this user: ");
            System.out.println("Reviews:");
            for (Review review : reviewsLeftByUser) {
                System.out.println(review);
            }
            System.out.print("Enter Review ID to delete: ");
            int reviewId = scanner.nextInt();
            scanner.nextLine();
            if(reviewsLeftByUser.stream().map(Review::getId).anyMatch(x -> x.equals(reviewId))) {
                boolean success = controller.deleteReviewAdmin(username, password, reviewId);
                if (success) {
                    System.out.println("Review deleted successfully.");
                } else {
                    System.out.println("Failed to delete review. Please check the Review ID.");
                }
            }
            else System.out.println("Invalid ID.");
        }
        else System.out.println("Invalid ID.");
    }

    private void deleteUser(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        System.out.println("Displayed Users:");
        for (User user : displayedUsers) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUserName());
        }
        System.out.print("Enter User ID to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        if(displayedUsers.stream().map(User::getId).anyMatch(x -> x.equals(userId))) {
            System.out.print("Are you sure you want to delete this account? (yes/no): ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("yes")) {
                boolean success = controller.deleteUser(username, password, userId);
                if (success)
                    System.out.println("Domain.Account deleted successfully.");
                else
                    System.out.println("Failed to delete user.");
            } else {
                System.out.println("Domain.Account deletion canceled.");
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void manageProducts(String username, String password) {
        boolean browsing = true;
        List<Product> products = new ArrayList<>();

        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("3. Select Product for Action (change category/delete product)");
            System.out.println("4. See Domain.Category Sales Ranking");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> products = sortProducts();
                case 2 -> products = filterProducts();
                case 3 -> selectProductActionAdmin(products, username, password);
                case 4 -> System.out.println(controller.seeCategorySales());
                case 0 -> browsing = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void selectProductActionAdmin(List<Product> products, String username, String password) {
        if (products.isEmpty()) {
            System.out.println("No products to select. Please search or filter products first.");
            return;
        }
        System.out.print("Enter Product ID to select for action: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        if(products.stream().map(Product::getId).anyMatch(x -> x.equals(productId))) {
            System.out.println("Choose Action for Product:");
            System.out.println("1. Change Product Domain.Category");
            System.out.println("2. Delete Product");
            System.out.print("Enter your choice: ");

            int actionChoice = scanner.nextInt();
            scanner.nextLine();
            switch (actionChoice) {
                case 1 -> changeProductCategory(productId, username, password);
                case 2 -> deleteProduct(productId, username, password);
                default -> System.out.println("Invalid action choice. Please try again.");
            }
        }
        else System.out.println("Invalid ID.");
    }

    private void deleteProduct(int productId, String username, String password) {
        System.out.print("Are you sure you want to delete this product? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            boolean success = controller.deleteProduct(username, password, productId);
            if (success)
                System.out.println("Product deleted successfully.");
            else
                System.out.println("Failed to delete product.");
        }
        else {
            System.out.println("Product deletion canceled.");
        }
    }

    private void changeProductCategory(int productId, String username, String password) {
        System.out.println("Available Categories:");
        List<Category> categories = controller.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(categories.get(i));
        }
        System.out.print("Choose a new category for the product: ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();
        if(categories.stream().map(Category::getId).anyMatch(x -> x.equals(categoryChoice))) {
            boolean success = controller.changeCategory(productId, categoryChoice, username, password);
            if (success)
                System.out.println("Product category updated successfully.");
            else
                System.out.println("Failed to change category.");
        }
        else System.out.println("Invalid ID.");
    }


}
