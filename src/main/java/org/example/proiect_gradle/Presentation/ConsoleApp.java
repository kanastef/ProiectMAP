package org.example.proiect_gradle.Presentation;
import com.mysql.cj.xdevapi.Schema;
import org.example.proiect_gradle.Controller.Controller;
import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Exceptions.ValidationException;

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

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException ("Input must be a number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 4) {
                        throw new ValidationException("Please enter a number between 0 and 4.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please select a valid option: ");
                }
            }

            switch (choice) {
                case 1 -> logIn();
                case 2 -> signUp();
                case 3 -> browseProductsVisitor();
                case 4 -> browseUsersVisitor();
                case 0 -> running = false;
            }
        }
    }

    //VISITOR

    private void signUp() {
        String username, email, password, phoneNumber;



        while (true) {
            try {
                System.out.println("Please enter your username: ");
                username = scanner.nextLine();
                if (username.isEmpty() || !username.matches("[a-zA-Z0-9_]+")) {
                    throw new ValidationException("Invalid username: Username must be a non-empty string and contain only letters, numbers, or underscores. ");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }


        while (true) {
            try {
                System.out.println("Please enter your email address: ");
                email = scanner.nextLine();
                if (email.isEmpty() || !email.matches("^[^@\\s]+@[^@\\s]+$")) {
                    throw new ValidationException("Invalid email: Email must be a non-empty string and contain no spaces. ");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }


        while (true) {
            try {
                System.out.println("Please enter your password: ");
                password = scanner.nextLine();
                if (password.isEmpty() || !password.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    throw new ValidationException("Invalid password: Password must be a non-empty string and must be at least 8 characters long, contain at least one uppercase letter and one digit. ");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }


        while (true) {
            try {
                System.out.println("Please enter your phone number: ");
                phoneNumber = scanner.nextLine();
                if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10}")) {
                    throw new ValidationException("Invalid phone number: Phone number must consist of exactly 10 digits. ");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }


        boolean success = controller.createAccount(username, password, email, phoneNumber);
        if (success) {
            System.out.println("Account created successfully! Please log in to continue. ");
        } else {
            System.out.println("Something went wrong. Please try again. ");
        }
    }


    private void logIn() {
        String username, password;



        while (true) {
            try {
                System.out.println("Please enter your username: ");
                username = scanner.nextLine();
                if (username.isEmpty()) {
                    throw new ValidationException("Username cannot be empty");
                }

                System.out.println("Please enter your password: ");
                password = scanner.nextLine();
                if (password.isEmpty()) {
                    throw new ValidationException("Password cannot be empty");
                }


                int result = controller.logIn(username, password);


                if (result == 1) {
                    userMenu(username, password);
                    break;
                } else if (result == 2) {
                    adminMenu(username, password);
                    break;
                } else {

                    throw new ValidationException("Invalid username or password. Please try again.");
                }

            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void browseProductsVisitor() {
        boolean browsing = true;


        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option: ");

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Input must be a number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 2) {
                        throw new ValidationException("Please enter a number between 0 and 2.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please choose a valid option: ");
                }
            }

            switch (choice) {
                case 1 -> sortProducts();
                case 2 -> filterProducts();
                case 0 -> browsing = false;
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
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Input must be a number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 3) {
                        throw new ValidationException("Please enter a number between 0 and 3.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please choose a valid option: ");
                }
            }

            switch (choice) {
                case 1 -> displayedUsers = sortUsers();
                case 2 -> displayedUsers = filterUsers();
                case 3 -> viewUserReviews(displayedUsers);
                case 0 -> browsing = false;
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

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Input must be a number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 9) {
                        throw new ValidationException("Please enter a number between 0 and 9.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please select a valid option: ");
                }
            }

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

        int choice;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Input must be a number.");
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 2) {
                    throw new ValidationException("Please enter a number between 1 and 2.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.print("Please select a valid option: ");
            }
        }

        switch (choice) {
            case 1 -> removeLike(liked, username, password);
            case 2 -> System.out.println(controller.userService.findByCriteriaHelper(username, password).getFavourites());
        }
    }


    private void removeLike(List<Product> likedProducts, String username, String password) {
        System.out.println("Enter the ID of the product you would like to delete: ");

        int id;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Input must be a number.");
                }
                id = Integer.parseInt(input);
                int finalId = id;
                if (likedProducts.stream().map(Product::getId).noneMatch(x -> x.equals(finalId))) {
                    throw new ValidationException("Invalid ID: No such product found in your likes.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.print("Please enter a valid ID: ");
            }
        }

        boolean success = controller.removeFromLiked(username, password, id);
        if (success) {
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Something went wrong.");
        }
    }




    private void viewMyReviews(String username, String password) {
        System.out.println("1. View Reviews Left by You");
        System.out.println("2. View Other Users Reviews for You");

        int choice;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Input must be a number.");
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 2) {
                    throw new ValidationException("Please enter a number between 1 and 2.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.print("Please enter a valid option: ");
            }
        }

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

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Input must be a number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 1 || choice > 2) {
                        throw new ValidationException("Please enter a number between 1 and 2.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please enter a valid option: ");
                }
            }

            switch (choice) {
                case 1 -> deleteMyReview(username, password, reviews);
                case 2 -> {return;
                }
            }
        }
    }

    private void deleteMyReview(String username, String password, List<Review> reviews) {
        System.out.println("Enter the ID of the review you would like to delete: ");

        int id;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Input must be a valid number.");
                }
                id = Integer.parseInt(input);
                int finalId = id;
                if (reviews.stream().map(Review::getId).noneMatch(x -> x.equals(finalId))) {
                    throw new ValidationException("Invalid review ID.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.print("Please enter a valid review ID: ");
            }
        }

        boolean success = controller.deleteReview(username, password, id);
        if (success) {
            System.out.println("Review deleted successfully!");
        } else {
            System.out.println("Something went wrong.");
        }
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

            int choice;
            while (true) {
                try {
                    System.out.print("Select an offer ID to accept/decline or go back: ");
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Input must be a valid number.");
                    }
                    choice = Integer.parseInt(input);
                    int finalChoice = choice;
                    if (choice == 0 || offers.stream().map(Offer::getId).anyMatch(x -> x.equals(finalChoice))) {
                        break;
                    } else {
                        throw new ValidationException("Invalid offer ID. Please try again.");
                    }
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                }
            }

            if (choice == 0) {
                managingOffers = false;
            } else {
                int finalChoice1 = choice;
                Offer selectedOffer = offers.stream()
                        .filter(offer -> offer.getId() == finalChoice1)
                        .findFirst()
                        .orElse(null);
                if (selectedOffer != null) {
                    System.out.println("You selected offer: " + selectedOffer);
                    int action;
                    while (true) {
                        try {
                            System.out.println("1. Accept Offer");
                            System.out.println("2. Decline Offer");
                            System.out.print("Choose an option: ");
                            String actionInput = scanner.nextLine();
                            if (!actionInput.matches("\\d+")) {
                                throw new ValidationException("Input must be a valid number.");
                            }
                            action = Integer.parseInt(actionInput);
                            if (action == 1 || action == 2) {
                                break;
                            } else {
                                throw new ValidationException("Invalid action. Please select 1 or 2.");
                            }
                        } catch (ValidationException e) {
                            System.out.println("Invalid input: " + e.getMessage());
                        }
                    }
                    switch (action) {
                        case 1 -> acceptOffer(username, password, selectedOffer);
                        case 2 -> declineOffer(username, password, selectedOffer);
                    }
                }
            }
        }
    }

    private void sendOffer(String username, String password, int selectedProduct) {
        double offerAmount;
        String message;

        while(true) {
            try {
                System.out.print("Enter your offer amount: ");
                offerAmount = scanner.nextDouble();
                if(offerAmount==0.00){
                    throw new ValidationException("Invalid offered amount: the offered amount can't be 0.00 or smaller then half of the selected product's price");
                }break;
            }catch(ValidationException e){
                System.out.print("Invalid input: Please enter a valid price."+e.getMessage());
            }
        }

        while(true) {
            try {
                System.out.print("Enter your offer message: ");
                message = scanner.nextLine();
                if(message.isEmpty()){
                    throw new ValidationException("Invalid message: The offers message cannot be an empty string");
                }
                break;
            }catch(ValidationException e){
                System.out.print("Invalid input: Please enter a valid price."+e.getMessage());

            }

        }
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

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Please enter a valid number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 4) {
                        throw new ValidationException("Please enter a number between 0 and 4.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please choose a valid option: ");
                }
            }

            switch (choice) {
                case 1 -> products = sortProducts();
                case 2 -> products = filterProducts();
                case 3 -> selectProductAction(username, password, products);
                case 4 -> makeOrder(username, password, products);
                case 0 -> browsing = false;
            }
        }
    }

    private List<Product> sortProducts() {
        int choice,order;



        while (true) {
            try {
                System.out.println("Sort Products by:");
                System.out.println("1. Price");
                System.out.println("2. Likes");
                System.out.println("3. Size");
                System.out.println("4. Views");
                System.out.println("5. All products");
                System.out.print("Choose an option: ");

                String choiceInput = scanner.nextLine();
                if (choiceInput.isEmpty() || !choiceInput.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid number between 1 and 5.");
                }
                choice = Integer.parseInt(choiceInput);
                if (choice < 1 || choice > 5) {
                    throw new ValidationException("Invalid choice: Please enter a number between 1 and 5.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }


        while (true) {
            try {
                System.out.println("Sort in:");
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.print("Choose an option: ");

                String orderInput = scanner.nextLine();
                if (orderInput.isEmpty() || !orderInput.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter 1 for Ascending or 2 for Descending.");
                }
                order = Integer.parseInt(orderInput);
                if (order < 1 || order > 2) {
                    throw new ValidationException("Invalid order: Please enter 1 for Ascending or 2 for Descending.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }


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

        int choice;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Please enter a valid number.");
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 9) {
                    throw new ValidationException("Please enter a number between 1 and 9.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.print("Please choose a valid option: ");
            }
        }

        List<Product> filteredProducts = new ArrayList<>();
        boolean found = false;

        try {
            switch (choice) {
                case 1 -> {
                    List<Category> categories = controller.getCategories();
                    categories.forEach(System.out::println);
                    boolean validCategory = false;
                    while (!validCategory) {
                        System.out.print("Type a category ID to filter by: ");
                        int category = scanner.nextInt();
                        if (categories.stream().map(Category::getId).anyMatch(x -> x.equals(category))) {
                            filteredProducts = controller.filterProductsByCategory(category);
                            validCategory = true;
                            if (!filteredProducts.isEmpty()) found = true;
                        } else {
                            System.out.println("Invalid category ID. Please try again.");
                        }
                    }
                }
                case 2 -> {
                    boolean validBrand = false;
                    while (!validBrand) {
                        System.out.print("Type a brand name to filter by: ");
                        String brand = scanner.nextLine();
                        if (!brand.matches("[A-Za-z0-9_ ]+")) {
                            System.out.println("The product brand must be a non-empty string containing only letters, numbers, underscores, or spaces.");
                        } else {
                            filteredProducts = controller.filterProductsByBrand(brand);
                            validBrand = true;
                            if (!filteredProducts.isEmpty()) found = true;
                        }
                    }
                }
                case 3 -> {
                    boolean validColor = false;
                    while (!validColor) {
                        System.out.print("Type a color to filter by: ");
                        String color = scanner.nextLine();
                        if (!color.matches("[A-Za-z]+")) {
                            System.out.println("The product color must be a non-empty string containing only letters.");
                        } else {
                            filteredProducts = controller.filterProductsByColor(color);
                            validColor = true;
                            if (!filteredProducts.isEmpty()) found = true;
                        }
                    }
                }
                case 4 -> {
                    boolean validSeller = false;
                    while (!validSeller) {
                        System.out.print("Type a seller name to filter by: ");
                        String name = scanner.nextLine();
                        if (!name.matches("[A-Za-z0-9_]+")) {
                            System.out.println("The seller username must be a non-empty string containing only letters, numbers, or underscores.");
                        } else {
                            filteredProducts = controller.filterProductsByUserName(name);
                            validSeller = true;
                            if (!filteredProducts.isEmpty()) found = true;
                        }
                    }
                }
                case 5, 7, 8, 9 -> {
                    String rangeType = switch (choice) {
                        case 5 -> "like range";
                        case 7 -> "price range";
                        case 8 -> "size range";
                        case 9 -> "views range";
                        default -> throw new ValidationException("Unexpected value: ");
                    };
                    boolean validRange = false;
                    while (!validRange) {
                        try {
                            System.out.print("Type the " + rangeType + " to filter by (two numbers separated by space): ");
                            String rangeInput = scanner.nextLine();
                            String[] rangeParts = rangeInput.split("\\s+");
                            if (rangeParts.length != 2 ||
                                    !rangeParts[0].matches("-?\\d+(\\.\\d+)?") ||
                                    !rangeParts[1].matches("-?\\d+(\\.\\d+)?")) {
                                throw new ValidationException("The range should only consist of two valid numbers.");
                            }
                            double min = Double.parseDouble(rangeParts[0]);
                            double max = Double.parseDouble(rangeParts[1]);
                            if (min < 0 || max < 0) {
                                System.out.println("The range values must be greater than or equal to 0.");
                            } else {
                                switch (choice) {
                                    case 5 -> filteredProducts = controller.filterProductsByLikes((int) min, (int) max);
                                    case 7 -> filteredProducts = controller.filterProductsByPriceRange(min, max);
                                    case 8 -> filteredProducts = controller.filterProductsBySizeRange((int) min, (int) max);
                                    case 9 -> filteredProducts = controller.filterProductsByViewRange((int) min, (int) max);
                                }
                                validRange = true;
                                if (!filteredProducts.isEmpty()) found = true;
                            }
                        } catch (ValidationException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
                case 6 -> {
                    boolean validCondition = false;
                    while (!validCondition) {
                        System.out.print("Type a condition to filter by New/Good/Worn: ");
                        String condition = scanner.nextLine();
                        if (!condition.matches("[A-Za-z]+")) {
                            System.out.println("The condition must be a non-empty string containing only letters.");
                        } else {
                            filteredProducts = controller.filterProductsByCondition(condition);
                            validCondition = true;
                            if (!filteredProducts.isEmpty()) found = true;
                        }
                    }
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        if (!found) {
            System.out.println("No products found with the given criteria.");
        } else {
            filteredProducts.forEach(System.out::println);
        }

        return filteredProducts;
    }

    private void selectProductAction(String username, String password,List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products to select. Please search or filter products first.");
            return;
        }
        int productId;


        while (true) {
            System.out.print("Enter Product ID to select for action: ");
            try {
                productId = scanner.nextInt();
                scanner.nextLine();
                int finalProductId = productId;
                Product selectedProduct = products.stream()
                        .filter(product -> product.getId() == finalProductId)
                        .findFirst()
                        .orElse(null);

                if (selectedProduct != null) {

                    boolean validAction = false;
                    while (!validAction) {
                        System.out.println("Choose Action for Product:");
                        System.out.println("1. Like Product");
                        System.out.println("2. Send Offer");
                        System.out.print("Enter your choice: ");
                        try {
                            int actionChoice = scanner.nextInt();
                            scanner.nextLine();
                            switch (actionChoice) {
                                case 1 -> {
                                    likeProducts(username, password, selectedProduct);
                                    validAction = true;
                                }
                                case 2 -> {
                                    sendOffer(username, password, selectedProduct.getId());
                                    validAction = true;
                                }
                                default -> throw new ValidationException("Invalid action choice. Please try again.");
                            }
                        } catch (ValidationException e) {
                            System.out.println("Error: " + e.getMessage());
                            scanner.nextLine();
                        }
                    }
                    break;
                } else {
                    throw new ValidationException("Invalid Product ID. Please try again.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
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

        Map<Integer, List<Integer>> orderedProducts = new HashMap<>();
        int option;


        while (true) {
            System.out.print("Enter Product IDs to add to your order. Press 0 to stop: ");
            try {
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid product ID.");
                }

                option = scanner.nextInt();
                scanner.nextLine();

                if (option == 0) {
                    break;
                }

                int finalOption = option;
                boolean validProduct = products.stream().anyMatch(product -> product.getId() == finalOption);
                if (!validProduct) {
                    throw new ValidationException("Invalid Product ID. Please try again.");
                }


                for (Product product : products) {
                    if (product.getId() == option && product.isAvailable()) {
                        orderedProducts
                                .computeIfAbsent(product.getListedBy(), _ -> new ArrayList<>())
                                .add(option);
                    }
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }


        if (!orderedProducts.isEmpty()) {
            boolean validAddress = false;
            String address = "";


            while (!validAddress) {
                try {
                    System.out.println("Enter your address: ");
                    address = scanner.nextLine();

                    if (address.isEmpty() || !address.matches("[a-zA-Z0-9, ]+")) {
                        throw new ValidationException("Invalid address: Address must be a non-empty string and contain only letters, numbers, commas or spaces");
                    }
                    validAddress = true;
                } catch (ValidationException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }


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
            boolean validInput = false;
            int choice = -1;


            while (!validInput) {
                System.out.print("Select an option: ");
                try {
                    if (!scanner.hasNextInt()) {
                        throw new ValidationException("Invalid input. Please enter a valid number.");
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    validInput = true;
                } catch (ValidationException e) {
                    System.out.println("Error: " + e.getMessage());
                    scanner.nextLine();
                }
            }


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
        for (Category value : categories) {
            System.out.println(value);
        }

        try {
            int category;
            while (true) {
                try {
                    System.out.print("Category ID: ");
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Invalid category ID: Please enter a valid integer.");
                    }
                    category = Integer.parseInt(input);
                    int finalCategory = category;
                    if (categories.stream().map(Category::getId).noneMatch(x -> x.equals(finalCategory))) {
                        throw new ValidationException("Invalid category ID: Please select a valid category from the list.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }

            String name,color,brand,condition;
            int size;
            double price;


            while (true) {
                try {
                    System.out.print("Name: ");
                    name = scanner.nextLine();
                    if (name.isEmpty() || !name.matches("[a-zA-Z0-9_ ]+")) {
                        throw new ValidationException("Invalid product name: The product name must be a non-empty string and contain only letters, numbers, underscores, or spaces.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }


            while (true) {
                try {
                    System.out.print("Color: ");
                    color = scanner.nextLine();
                    if (color.isEmpty() || !color.matches("[a-zA-Z]+")) {
                        throw new ValidationException("Invalid product color: The product color must be a non-empty string and contain only letters.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }


            while (true) {
                try {
                    System.out.print("Size: ");
                    String sizeInput = scanner.nextLine();
                    if (!sizeInput.matches("\\d+")) {
                        throw new ValidationException("Invalid product size: The size must be an integer greater than zero.");
                    }
                    size = Integer.parseInt(sizeInput);
                    if (size <= 0) {
                        throw new ValidationException("Invalid product size: The size must be greater than zero.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }


            while (true) {
                try {
                    System.out.print("Price: ");
                    String priceInput = scanner.nextLine();
                    if (!priceInput.matches("\\d+(\\.\\d{1,2})?")) {
                        throw new ValidationException("Invalid product price: The price must be a positive number with up to two decimal places.");
                    }
                    price = Double.parseDouble(priceInput);
                    if (price < 0.00) {
                        throw new ValidationException("Invalid product price: The price cannot be negative.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }


            while (true) {
                try {
                    System.out.print("Brand: ");
                    brand = scanner.nextLine();
                    if (brand.isEmpty() || !brand.matches("[a-zA-Z0-9_ ]+")) {
                        throw new ValidationException("Invalid product brand: The brand must be a non-empty string containing only letters, numbers, underscores, or spaces.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }


            while (true) {
                try {
                    System.out.print("Condition (e.g., New, Used): ");
                    condition = scanner.nextLine();
                    if (condition.isEmpty() || !condition.matches("[a-zA-Z]+")) {
                        throw new ValidationException("Invalid product condition: The condition must be a non-empty string containing only letters.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }

            boolean success = controller.addToUserListings(username, password, category, name, color, size, price, brand, condition, 0, 0);
            if (success) {
                System.out.println("Product added to your listings successfully!");
            } else {
                System.out.println("Could not add product to your listings. Please try again.");
            }

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteProductFromMyListings(String username, String password, List<Product> myListings) {
        if (myListings.isEmpty()) {
            System.out.println("You have no products listed to delete.");
            return;
        }

        int productId = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter the ID of the product you wish to delete: ");
            try {
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid product ID.");
                }
                productId = scanner.nextInt();
                scanner.nextLine();

                int finalProductId = productId;
                if (myListings.stream().map(Product::getId).anyMatch(x -> x.equals(finalProductId))) {
                    validInput = true;
                } else {
                    System.out.println("Invalid product ID. Please enter a valid ID from your listings.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }


        boolean success = controller.removeFromUserListings(username, password, productId);
        if (success) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product deletion failed.");
        }
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

            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Choose an option: ");
                    if (!scanner.hasNextInt()) {
                        throw new ValidationException("Invalid input. Please enter a valid number.");
                    }
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    validInput = true;

                    switch (choice) {
                        case 1 -> displayedUsers = sortUsers();
                        case 2 -> displayedUsers = filterUsers();
                        case 3 -> selectUserForReview(username, password, displayedUsers);
                        case 4 -> viewUserReviews(displayedUsers);
                        case 5 -> viewUserListings(displayedUsers);
                        case 0 -> browsing = false;
                        default -> {
                            System.out.println("Invalid choice. Please try again.");
                            validInput = false;
                        }
                    }
                } catch (ValidationException e) {
                    System.out.println("Error: " + e.getMessage());
                    scanner.nextLine();
                }
            }
        }
    }

    private List<User> sortUsers() {
        int choice, order;


        while (true) {
            try {
                System.out.println("Sort Users by:");
                System.out.println("1. Review Count");
                System.out.println("2. Name");
                System.out.println("3. Score");
                System.out.println("4. All Users");
                System.out.print("Choose an option: ");
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid number.");
                }
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

        while (true) {
            try {
                System.out.println("Sort in:");
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.print("Choose an option: ");
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid number.");
                }
                order = scanner.nextInt();
                scanner.nextLine();

                if (order < 1 || order > 2) {
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }


        List<User> sortedUsers = controller.sortUsers(choice,order);
        sortedUsers.forEach(System.out::println);
        return sortedUsers;
    }

    private List<User> filterUsers() {
        int choice;
        while (true) {
            try {
                System.out.println("Filter Users by:");
                System.out.println("1. Minimum Review Count");
                System.out.println("2. Name");
                System.out.println("3. Minimum Score");
                System.out.print("Choose an option: ");
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid number.");
                }
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > 3) {
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

        List<User> filteredUsers = new ArrayList<>();
        boolean found = false;

        switch (choice) {
            case 1 -> {
                int minReviewCount;
                while (true) {
                    try {
                        System.out.println("Enter a Minimum Review Count: ");
                        if (!scanner.hasNextInt()) {
                            throw new ValidationException("Invalid input. Please enter a valid number.");
                        }
                        minReviewCount = scanner.nextInt();
                        scanner.nextLine();
                        if (minReviewCount < 0) {
                            System.out.println("Review count must be 0 or greater. Please try again.");
                        } else {
                            break;
                        }
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                        scanner.nextLine();
                    }
                }
                filteredUsers = controller.filterUsersByMinimumReviewCount(minReviewCount);
                if (!filteredUsers.isEmpty()) {
                    found = true;
                }
            }
            case 2 -> {
                String name;
                while (true) {
                    try {
                        System.out.println("Enter a Name: ");
                        name = scanner.nextLine();
                        if (!name.matches("[a-zA-Z0-9_]+")) {
                            throw new ValidationException("Invalid input: Username should contain only letters, numbers or underscores.");
                        }
                        break;
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                filteredUsers = controller.filterUsersByName(name);
                if (!filteredUsers.isEmpty()) {
                    found = true;
                }
            }
            case 3 -> {
                double minScore;
                while (true) {
                    try {
                        System.out.println("Enter a Minimum Score: ");
                        if (!scanner.hasNextDouble()) {
                            throw new ValidationException("Invalid input. Please enter a valid number.");
                        }
                        minScore = scanner.nextDouble();
                        scanner.nextLine();
                        if (minScore < 0) {
                            System.out.println("Score must be 0 or greater. Please try again.");
                        } else {
                            break;
                        }
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                        scanner.nextLine();
                    }
                }
                filteredUsers = controller.filterUsersByMinimumScore(minScore);
                if (!filteredUsers.isEmpty()) {
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No users found based on the given criteria.");
        } else {
            filteredUsers.forEach(System.out::println);
        }

        return filteredUsers;
    }

    private void viewUserReviews(List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        int userId;
        while (true) {
            try {
                System.out.print("Enter User ID to see their rating: ");
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid User ID.");
                }
                userId = scanner.nextInt();
                scanner.nextLine();
                int finalUserId = userId;
                if (displayedUsers.stream().map(User::getId).noneMatch(x -> x.equals(finalUserId))) {
                    System.out.println("Invalid User ID. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

        List<Review> reviews = controller.displayReviewsLeftForUser(userId);
        System.out.println("User's trust score is: ");
        System.out.println(controller.getUserTrustScore(userId));
        for (Review review : reviews) {
            System.out.println(review);
        }
    }

    private void selectUserForReview(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        int userId;
        while (true) {
            try {
                System.out.print("Enter User ID to leave a review for: ");
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid User ID.");
                }
                userId = scanner.nextInt();
                scanner.nextLine();
                int finalUserId = userId;
                if (displayedUsers.stream().map(User::getId).noneMatch(x -> x.equals(finalUserId))) {
                    System.out.println("Invalid User ID. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

        String content;
        while (true) {
            System.out.print("Enter review content: ");
            content = scanner.nextLine();
            if (!content.isEmpty()) {
                break;
            } else {
                System.out.println("Review content cannot be empty. Please try again.");
            }
        }

        double rating;
        while (true) {
            try {
                System.out.print("Enter rating (1-5): ");
                if (!scanner.hasNextDouble()) {
                    throw new ValidationException("Invalid input. Please enter a valid rating between 1 and 5.");
                }
                rating = scanner.nextDouble();
                scanner.nextLine();
                if (rating < 1 || rating > 5) {
                    System.out.println("Rating must be between 1 and 5. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

        boolean success = controller.writeReview(username, password, rating, content, userId);
        if (success) {
            System.out.println("Review added successfully.");
        } else {
            System.out.println("Review failed. Please try again.");
        }
    }


    private void viewUserListings(List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }
        int userId;
        while (true) {
            try {
                System.out.print("Enter User ID to view their listings: ");
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid User ID.");
                }
                userId = scanner.nextInt();
                scanner.nextLine();
                int finalUserId = userId;
                if (displayedUsers.stream().map(User::getId).noneMatch(x -> x.equals(finalUserId))) {
                    System.out.println("Invalid User ID. Please try again.");
                } else {
                    break;
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

        List<Product> userProducts = controller.getUserListing(userId);
        if (userProducts.isEmpty()) {
            System.out.println("This user has no listed products.");
        } else {
            userProducts.forEach(System.out::println);
        }
    }

//ADMIN

    private void adminMenu(String username, String password) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Admin Menu:");
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Users");
            System.out.println("0. Log Out");

            int choice = -1;
            while (true) {
                try {
                    System.out.print("Select an option: ");
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Invalid input: Please enter a valid number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 2) {
                        throw new ValidationException("Invalid choice: Please select 0, 1, or 2.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }

            switch (choice) {
                case 1 -> manageProducts(username, password);
                case 2 -> manageUsers(username, password);
                case 0 -> {
                    loggedIn = false;
                    System.out.println("Logging out...");
                }
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
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice;


            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Input must be a number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 4) {
                        throw new ValidationException("Please select a number between 0 and 4.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please choose a valid option: ");
                }
            }


            switch (choice) {
                case 1 -> {
                    displayedUsers = sortUsers();
                    System.out.println("Users sorted successfully.");
                }
                case 2 -> {
                    displayedUsers = filterUsers();
                    System.out.println("Users filtered successfully.");
                }
                case 3 -> {
                    if (displayedUsers.isEmpty()) {
                        System.out.println("No users to delete. Please sort or filter users first.");
                    } else {
                        deleteUser(username, password, displayedUsers);
                    }
                }
                case 4 -> {
                    if (displayedUsers.isEmpty()) {
                        System.out.println("No users to manage reviews for. Please sort or filter users first.");
                    } else {
                        manageReviews(username, password, displayedUsers);
                    }
                }
                case 0 -> {
                    browsing = false;
                    System.out.println("Returning to the Main Menu...");
                }
                default -> System.out.println("Unexpected error occurred. Please try again.");
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

        int choice;
        while (true) {
            try {
                System.out.print("Choose an option: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid number.");
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 2) {
                    throw new ValidationException("Invalid choice: Please select 1 or 2.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        switch (choice) {
            case 1 -> deleteReviewMadeByUser(username, password, displayedUsers);
            case 2 -> deleteReviewLeftForUser(username, password, displayedUsers);
        }
    }


    private void deleteReviewLeftForUser(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }

        int userId;
        while (true) {
            try {
                System.out.println("Select a User to see reviews left for them:");
                for (int i = 0; i < displayedUsers.size(); i++) {
                    System.out.println((i + 1) + ". " + displayedUsers.get(i));
                }
                System.out.print("Enter the ID of the user to manage: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid user ID.");
                }
                userId = Integer.parseInt(input);
                int finalUserId = userId;
                if (displayedUsers.stream().map(User::getId).noneMatch(id -> id == finalUserId)) {
                    throw new ValidationException("Invalid ID: Please select a user ID from the list.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Review> reviewsLeftForUser = controller.displayReviewsLeftForUser(userId);
        if (reviewsLeftForUser.isEmpty()) {
            System.out.println("No reviews found left for this user.");
            return;
        }

        System.out.println("Reviews left for this user:");
        for (Review review : reviewsLeftForUser) {
            System.out.println(review);
        }

        int reviewId;
        while (true) {
            try {
                System.out.print("Enter Review ID to delete: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid review ID.");
                }
                reviewId = Integer.parseInt(input);
                int finalReviewId = reviewId;
                if (reviewsLeftForUser.stream().map(Review::getId).noneMatch(id -> id == finalReviewId)) {
                    throw new ValidationException("Invalid Review ID: Please select a review ID from the list.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        boolean success = controller.deleteReviewAdmin(username, password, reviewId);
        if (success) {
            System.out.println("Review deleted successfully.");
        } else {
            System.out.println("Failed to delete review. Please check the Review ID and try again.");
        }
    }


    private void deleteReviewMadeByUser(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }

        int userId;
        while (true) {
            try {
                System.out.println("Select a User to see reviews they have made:");
                for (int i = 0; i < displayedUsers.size(); i++) {
                    System.out.println((i + 1) + ". " + displayedUsers.get(i));
                }
                System.out.print("Enter the ID of the user to manage: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid user ID.");
                }
                userId = Integer.parseInt(input);
                int finalUserId1 = userId;
                if (displayedUsers.stream().map(User::getId).noneMatch(id -> id == finalUserId1)) {
                    throw new ValidationException("Invalid ID: Please select a user ID from the list.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        int finalUserId = userId;
        User selectedUser = displayedUsers.stream()
                .filter(user -> user.getId() == finalUserId)
                .findFirst()
                .orElse(null);

        if (selectedUser == null) {
            System.out.println("Unexpected error: Could not find the selected user.");
            return;
        }

        List<Review> reviewsLeftByUser = controller.displayReviewsLeftByUser(selectedUser.getUserName(), selectedUser.getPassword());
        if (reviewsLeftByUser.isEmpty()) {
            System.out.println("No reviews made by this user.");
            return;
        }

        System.out.println("Reviews made by this user:");
        for (Review review : reviewsLeftByUser) {
            System.out.println(review);
        }

        int reviewId;
        while (true) {
            try {
                System.out.print("Enter Review ID to delete: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid review ID.");
                }
                reviewId = Integer.parseInt(input);
                int finalReviewId = reviewId;
                if (reviewsLeftByUser.stream().map(Review::getId).noneMatch(id -> id == finalReviewId)) {
                    throw new ValidationException("Invalid Review ID: Please select a review ID from the list.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        boolean success = controller.deleteReviewAdmin(username, password, reviewId);
        if (success) {
            System.out.println("Review deleted successfully.");
        } else {
            System.out.println("Failed to delete review. Please check the Review ID and try again.");
        }
    }

    private void deleteUser(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }

        int userId;
        while (true) {
            try {
                System.out.println("Displayed Users:");
                for (User user : displayedUsers) {
                    System.out.println("ID: " + user.getId() + ", Username: " + user.getUserName());
                }
                System.out.print("Enter User ID to delete: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input: Please enter a valid user ID.");
                }
                userId = Integer.parseInt(input);
                int finalUserId = userId;
                if (displayedUsers.stream().map(User::getId).noneMatch(id -> id == finalUserId)) {
                    throw new ValidationException("Invalid ID: Please select a user ID from the list.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        String confirmation;
        while (true) {
            try {
                System.out.print("Are you sure you want to delete this account? (yes/no): ");
                confirmation = scanner.nextLine().trim().toLowerCase();
                if (!confirmation.equals("yes") && !confirmation.equals("no")) {
                    throw new ValidationException("Invalid input: Please enter 'yes' or 'no'.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        if (confirmation.equals("yes")) {
            boolean success = controller.deleteUser(username, password, userId);
            if (success) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Failed to delete user. Please check the User ID and try again.");
            }
        } else {
            System.out.println("Account deletion canceled.");
        }
    }



    private void manageProducts(String username, String password) {
        boolean browsing = true;
        List<Product> products = new ArrayList<>();

        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("3. Select Product for Action (change category/delete product)");
            System.out.println("4. See Category Sales Ranking");
            System.out.println("0. Go Back to Main Menu");

            int choice = -1;
            while (true) {
                try {
                    System.out.print("Choose an option: ");
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Invalid input: Please enter a valid number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 4) {
                        throw new ValidationException("Invalid choice: Please select a number between 0 and 4.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }

            switch (choice) {
                case 1 -> products = sortProducts();
                case 2 -> products = filterProducts();
                case 3 -> selectProductActionAdmin(products, username, password);
                case 4 -> System.out.println(controller.seeCategorySales());
                case 0 -> browsing = false;
            }
        }
    }


    private void selectProductActionAdmin(List<Product> products, String username, String password) {
        if (products.isEmpty()) {
            System.out.println("No products to select. Please search or filter products first.");
            return;
        }

        boolean validProductId = false;
        while (!validProductId) {
            try {
                System.out.print("Enter Product ID to select for action: ");
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Invalid input. Please enter a valid product ID.");
                }
                int productId = Integer.parseInt(input);

                if (products.stream().map(Product::getId).anyMatch(x -> x.equals(productId))) {
                    validProductId = true;
                    System.out.println("Choose Action for Product:");
                    System.out.println("1. Change Product Category");
                    System.out.println("2. Delete Product");
                    System.out.print("Enter your choice: ");

                    int actionChoice;
                    while (true) {
                        try {
                            String actionInput = scanner.nextLine();
                            if (!actionInput.matches("\\d+")) {
                                throw new ValidationException("Invalid input. Please enter a valid action choice.");
                            }
                            actionChoice = Integer.parseInt(actionInput);
                            if (actionChoice == 1 || actionChoice == 2) {
                                break;
                            } else {
                                throw new ValidationException("Invalid choice. Please choose either 1 or 2.");
                            }
                        } catch (ValidationException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    switch (actionChoice) {
                        case 1 -> changeProductCategory(productId, username, password);
                        case 2 -> deleteProduct(productId, username, password);
                    }
                } else {
                    System.out.println("Invalid Product ID. Please try again.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void deleteProduct(int productId, String username, String password) {
        String confirmation;
        while (true) {
            try {
                System.out.print("Are you sure you want to delete this product? (yes/no): ");
                confirmation = scanner.nextLine().trim().toLowerCase();
                if (!confirmation.equals("yes") && !confirmation.equals("no")) {
                    throw new ValidationException("Invalid input. Please enter 'yes' or 'no'.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (confirmation.equals("yes")) {
            boolean success = controller.deleteProduct(username, password, productId);
            if (success)
                System.out.println("Product deleted successfully.");
            else
                System.out.println("Failed to delete product.");
        } else {
            System.out.println("Product deletion canceled.");
        }
    }

    private void changeProductCategory(int productId, String username, String password) {
        boolean validCategory = false;
        while (!validCategory) {
            try {
                System.out.println("Available Categories:");
                List<Category> categories = controller.getCategories();
                for (Category category : categories) {
                    System.out.println(category);
                }
                System.out.print("Choose a new category for the product: ");
                int categoryChoice;
                while (true) {
                    try {
                        if (!scanner.hasNextInt()) {
                            throw new ValidationException("Invalid input. Please enter a valid category ID.");
                        }
                        categoryChoice = scanner.nextInt();
                        scanner.nextLine();
                        int finalCategoryChoice = categoryChoice;
                        if (categories.stream().map(Category::getId).noneMatch(x -> x.equals(finalCategoryChoice))) {
                            throw new ValidationException("Invalid category ID. Please choose a valid category.");
                        }
                        break;
                    } catch (ValidationException e) {
                        System.out.println("Error: " + e.getMessage());
                        scanner.nextLine();
                    }
                }

                boolean success = controller.changeCategory(productId, categoryChoice, username, password);
                if (success)
                    System.out.println("Product category updated successfully.");
                else
                    System.out.println("Failed to change category.");
                validCategory = true;
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

