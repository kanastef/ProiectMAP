package org.example.proiect_gradle.Presentation;
import com.mysql.cj.xdevapi.Schema;
import org.example.proiect_gradle.Controller.Controller;
import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Exceptions.ValidationException;
import org.example.proiect_gradle.Repository.IRepository;
import org.example.proiect_gradle.Repository.RepositoryFactory;
import org.example.proiect_gradle.Service.AdminService;
import org.example.proiect_gradle.Service.UserService;
import org.example.proiect_gradle.Service.VisitorService;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ConsoleApp {
    private Controller controller;
    private final Scanner scanner = new Scanner(System.in);
    private final DisplayGUI displayGUI;

    public ConsoleApp() {
        this.displayGUI=new DisplayGUI();
    }

    public void selectRepositoryType() {
        System.out.println("Select repository type: ");
        System.out.println("1. In-Memory Repository");
        System.out.println("2. File-Based Repository");
        System.out.println("3. Database Repository");
        System.out.print("Please select an option: ");
        int choice;
        while(true) {
            try {
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) {
                    throw new ValidationException("Input must be a number.");
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 3) {
                    throw new ValidationException("Please enter a number between 1 and 3.");
                }
                break;
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.print("Please select a valid option: ");
            }
        }

        String repoType = switch (choice) {
            case 1 -> "in-memory";
            case 2 -> "file";
            case 3 -> "db";
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

        RepositoryFactory factory = new RepositoryFactory(repoType);

        IRepository<Visitor> visitorRepo = factory.createVisitorRepo();
        IRepository<User> userRepo = factory.createUserRepository();
        IRepository<Product> productRepo = factory.createProductRepository();
        IRepository<Review> reviewRepo = factory.createReviewRepository();
        IRepository<Category> categoryRepo = factory.createCategoryRepository();
        IRepository<Order> orderRepo = factory.createOrderRepository();
        IRepository<Offer> offerRepo = factory.createOfferRepository();
        IRepository<Admin> adminRepo = factory.createAdminRepository();

        AdminService adminService = new AdminService(userRepo, productRepo, reviewRepo, adminRepo, categoryRepo, orderRepo, visitorRepo);
        UserService userService = new UserService(userRepo, productRepo, reviewRepo, categoryRepo, orderRepo, offerRepo);
        VisitorService visitorService = new VisitorService(userRepo, productRepo, reviewRepo, categoryRepo);

        this.controller = new Controller(adminService, userService, visitorService);

        System.out.println("Repository type selected successfully.");

    }
    public void start() {
        selectRepositoryType();
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
                case 0 -> {
                    System.out.println("Bye Bye!");
                    running = false;
                    displayGUI.closeGUI();
                }
            }
        }
    }

    //VISITOR

    private void signUp() {
        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneNumberField = new JTextField();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(new JLabel("Username:"));
        contentPanel.add(usernameField);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JLabel("Email:"));
        contentPanel.add(emailField);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JLabel("Password:"));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JLabel("Phone Number:"));
        contentPanel.add(phoneNumberField);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        int result = JOptionPane.showConfirmDialog(
                DisplayGUI.frame, scrollPane, "Sign Up", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String phoneNumber = phoneNumberField.getText();

            try {
                if (!username.matches("[a-zA-Z0-9_]+")) {
                    throw new ValidationException("Invalid username.");
                }
                if (!email.matches("^[^@\\s]+@[^@\\s]+$")) {
                    throw new ValidationException("Invalid email.");
                }
                if (!password.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    throw new ValidationException("Invalid password.");
                }
                if (!phoneNumber.matches("\\d{10}")) {
                    throw new ValidationException("Invalid phone number.");
                }

                boolean success = controller.createAccount(username, password, email, phoneNumber);
                if (success) {
                    JOptionPane.showMessageDialog(DisplayGUI.frame,
                            "Account created successfully! Please log in to continue.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(DisplayGUI.frame,
                            "Something went wrong. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(DisplayGUI.frame,
                        e.getMessage(),
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void logIn() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));


        contentPanel.add(new JLabel("Username:"));
        contentPanel.add(usernameField);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JLabel("Password:"));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));
        scrollPane.setMaximumSize(new Dimension(800, 700));

        int result = JOptionPane.showConfirmDialog(
                DisplayGUI.frame, contentPanel, "Log In", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                int loginResult = controller.logIn(username, password);

                if (loginResult == 1) {
                    userMenu(username, password);
                } else if (loginResult == 2) {
                    adminMenu(username, password);
                } else {
                    throw new ValidationException("Invalid username or password. Please try again.");
                }

            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(DisplayGUI.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                case 0 -> {
                    browsing = false;
                    displayGUI.refreshWelcomePage();
                }
            }
        }
    }

    private void browseUsersVisitor() {
        boolean browsing = true;
        List<User> displayedUsers = new ArrayList<>();

        displayGUI.setUserActionListener(new DisplayGUI.UserActionListener() {
            @Override
            public void onViewReviews(int userId) {

                viewUserReviews(userId);
            }

            @Override
            public void onViewListings(int userId) {
                System.out.println("Viewing user listings is disabled for visitors.");
            }

            @Override
            public void onLeaveReview(int userId) {
                System.out.println("Leaving a review is disabled for visitors.");
            }
        });

        while (browsing) {
            System.out.println("User Browsing Options: ");
            System.out.println("1. Sort Users");
            System.out.println("2. Filter Users");
            System.out.println("3.Select User via GUI for actions");
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid number.");
                }
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        displayedUsers = sortUsers();
                        displayGUI.updateUsers(displayedUsers);
                        System.out.println("Sorted users displayed in GUI.");
                    }
                    case 2 -> {
                        displayedUsers = filterUsers();
                        displayGUI.updateUsers(displayedUsers);
                        System.out.println("Filtered users displayed in GUI.");
                    }
                    case 3 -> {
                        if (displayedUsers.isEmpty()) {
                            System.out.println("Please sort or filter users first.");
                        } else {
                            displayGUI.updateUsers(displayedUsers);
                            System.out.println("Please select a user from the GUI to proceed.");
                        }
                    }
                    case 0 -> browsing = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
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
                case 0 ->{
                    loggedIn = false;
                    displayGUI.refreshWelcomePage();
                }
            }
        }
    }

    private void viewLikes(String username, String password) {
        List<Product> liked = controller.displayLikedProducts(username, password);

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

        if (liked == null || liked.isEmpty()) {
            JPanel noLikedPanel = new JPanel();
            noLikedPanel.add(new JLabel("You have no liked products."));
            productsPanel.add(noLikedPanel);
        } else {
            for (Product product : liked) {
                JPanel productPanel = new JPanel();
                productPanel.setLayout(new BorderLayout());

                JLabel imageLabel;
                if (product.getImagePath() != null) {
                    ImageIcon icon = new ImageIcon(product.getImagePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(img));
                } else {
                    imageLabel = new JLabel("No Image");
                }
                productPanel.add(imageLabel, BorderLayout.WEST);

                JTextArea details = new JTextArea(product.toString());
                details.setEditable(false);
                details.setMargin(new Insets(10, 10, 10, 10));
                productPanel.add(details, BorderLayout.CENTER);

                JButton removeButton = new JButton("Remove Like");
                removeButton.addActionListener(e -> {
                    int confirmation = JOptionPane.showConfirmDialog(DisplayGUI.frame,
                            "Are you sure you want to remove this product from your likes?",
                            "Confirm Removal",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        boolean success = controller.removeFromLiked(username, password, product.getId());
                        if (success) {
                            JOptionPane.showMessageDialog(DisplayGUI.frame,
                                    "Product removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            productsPanel.remove(productPanel);
                            productsPanel.revalidate();
                            productsPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(DisplayGUI.frame,
                                    "Failed to remove product.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                productPanel.add(removeButton, BorderLayout.EAST);

                productsPanel.add(productPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        JOptionPane.showMessageDialog(DisplayGUI.frame, scrollPane, "Liked Products", JOptionPane.PLAIN_MESSAGE);
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
        List<Review> reviews = controller.displayReviewsLeftByUser (username, password);

        JPanel reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));

        if (reviews.isEmpty()) {
            reviewsPanel.add(new JLabel("No reviews found. Please try again."));
        } else {
            for (Review review : reviews) {
                JPanel reviewPanel = new JPanel();
                reviewPanel.setLayout(new BorderLayout());

                JTextArea reviewTextArea = new JTextArea(review.toString());
                reviewTextArea.setEditable(false);
                reviewTextArea.setMargin(new Insets(10, 10, 10, 10));
                reviewPanel.add(reviewTextArea, BorderLayout.CENTER);

                JButton deleteButton = new JButton("Delete Review");
                deleteButton.addActionListener(e -> {
                    int confirmation = JOptionPane.showConfirmDialog(DisplayGUI.frame,
                            "Are you sure you want to delete this review?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        boolean success = controller.deleteReview(username, password, review.getId());
                        if (success) {
                            JOptionPane.showMessageDialog(DisplayGUI.frame,
                                    "Review deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            reviewsPanel.remove(reviewPanel);
                            reviewsPanel.revalidate();
                            reviewsPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(DisplayGUI.frame,
                                    "Failed to delete review.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                reviewPanel.add(deleteButton, BorderLayout.EAST);

                reviewsPanel.add(reviewPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        JOptionPane.showMessageDialog(DisplayGUI.frame, scrollPane, "Your Reviews", JOptionPane.PLAIN_MESSAGE);
    }

    private void reviewsForMe(String username, String password) {
        double rating = controller.getProfileScore(username, password);

        JPanel reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));

        reviewsPanel.add(new JLabel("Your rating is: " + rating));

        List<Review> reviews = controller.displayReviewsForMe(username, password);
        if (reviews.isEmpty()) {
            reviewsPanel.add(new JLabel("No reviews found. Please try again."));
        } else {
            for (Review review : reviews) {
                JTextArea reviewTextArea = new JTextArea(review.toString());
                reviewTextArea.setEditable(false);
                reviewTextArea.setMargin(new Insets(10, 10, 10, 10));
                reviewTextArea.setLineWrap(true);
                reviewTextArea.setWrapStyleWord(true);
                reviewsPanel.add(reviewTextArea);
            }
        }

        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        JOptionPane.showMessageDialog(DisplayGUI.frame, scrollPane, "Your Reviews", JOptionPane.PLAIN_MESSAGE);
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
            JOptionPane.showMessageDialog(DisplayGUI.frame, "You have no offers.", "No Offers", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel offersPanel = new JPanel();
        offersPanel.setLayout(new BoxLayout(offersPanel, BoxLayout.Y_AXIS));

        for (Offer offer : offers) {
            JPanel offerPanel = new JPanel();
            offerPanel.setLayout(new BorderLayout());

            JTextArea offerTextArea = new JTextArea(offer.toString());
            offerTextArea.setEditable(false);
            offerTextArea.setMargin(new Insets(10, 10, 10, 10));
            offerTextArea.setLineWrap(true);
            offerTextArea.setWrapStyleWord(true);
            offerPanel.add(offerTextArea, BorderLayout.CENTER);

            JButton acceptButton = new JButton("Accept Offer");
            acceptButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(DisplayGUI.frame,
                        "Are you sure you want to accept this offer?",
                        "Confirm Acceptance",
                        JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    acceptOffer(username, password, offer);
                    JOptionPane.showMessageDialog(DisplayGUI.frame, "Offer accepted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    offersPanel.remove(offerPanel);
                    offersPanel.revalidate();
                    offersPanel.repaint();
                }
            });

            JButton declineButton = new JButton("Decline Offer");
            declineButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(DisplayGUI.frame,
                        "Are you sure you want to decline this offer?",
                        "Confirm Decline",
                        JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    declineOffer(username, password, offer);
                    JOptionPane.showMessageDialog(DisplayGUI.frame, "Offer declined successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    offersPanel.remove(offerPanel);
                    offersPanel.revalidate();
                    offersPanel.repaint();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(acceptButton);
            buttonPanel.add(declineButton);
            offerPanel.add(buttonPanel, BorderLayout.SOUTH);

            offersPanel.add(offerPanel);
        }

        JScrollPane scrollPane = new JScrollPane(offersPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        JOptionPane.showMessageDialog(DisplayGUI.frame, scrollPane, "Your Offers", JOptionPane.PLAIN_MESSAGE);
    }

    private void sendOffer(String username, String password, int selectedProduct) {
        JTextField offerAmountField = new JTextField();
        JTextField offerMessageField = new JTextField();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(new JLabel("Enter Offer Amount:"));
        contentPanel.add(offerAmountField);
        contentPanel.add(Box.createVerticalStrut(10)); // Add spacing
        contentPanel.add(new JLabel("Enter Offer Message:"));
        contentPanel.add(offerMessageField);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        int result = JOptionPane.showConfirmDialog(
                DisplayGUI.frame, scrollPane, "Send Offer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            double offerAmount;
            String offerMessage = offerMessageField.getText();

            try {
                offerAmount = Double.parseDouble(offerAmountField.getText().trim());

                if (offerAmount <= 0.00) {
                    throw new ValidationException("The offered amount cannot be 0.00 or smaller than half the selected product's price.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(DisplayGUI.frame,
                        "Please enter a valid numeric value for the offer amount.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(DisplayGUI.frame,
                        e.getMessage(),
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (offerMessage.trim().isEmpty()) {
                JOptionPane.showMessageDialog(DisplayGUI.frame,
                        "The offer message cannot be empty.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = controller.sendOffer(username, password, offerMessage, selectedProduct, offerAmount);

            if (success) {
                JOptionPane.showMessageDialog(DisplayGUI.frame,
                        "Offer sent successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                System.out.println("Offer for selected product was successfully sent.");
            } else {
                JOptionPane.showMessageDialog(DisplayGUI.frame,
                        "Could not send offer. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
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

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        if (orders.isEmpty()) {
            ordersPanel.add(new JLabel("You have no orders."));
        } else {
            for (Order order : orders) {
                JTextArea orderTextArea = new JTextArea(order.toString());
                orderTextArea.setEditable(false);
                orderTextArea.setMargin(new Insets(10, 10, 10, 10));
                orderTextArea.setLineWrap(true);
                orderTextArea.setWrapStyleWord(true);
                ordersPanel.add(orderTextArea);
            }
        }

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));
        JOptionPane.showMessageDialog(DisplayGUI.frame, scrollPane, "Your Orders", JOptionPane.PLAIN_MESSAGE);
    }


    private void viewMyOrders(String username, String password) {
        List<Order> orders = controller.getMadeOrders(username, password);

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        if (orders.isEmpty()) {
            ordersPanel.add(new JLabel("You have no orders."));
        } else {
            for (Order order : orders) {
                JTextArea orderTextArea = new JTextArea(order.toString());
                orderTextArea.setEditable(false);
                orderTextArea.setMargin(new Insets(10, 10, 10, 10));
                orderTextArea.setLineWrap(true);
                orderTextArea.setWrapStyleWord(true);
                ordersPanel.add(orderTextArea);
            }
        }

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        JOptionPane.showMessageDialog(DisplayGUI.frame, scrollPane, "Your Orders", JOptionPane.PLAIN_MESSAGE);
    }


    private void browseProductsUser(String username, String password) {
        boolean browsing = true;
        List<Product> products = new ArrayList<>();

        displayGUI.setProductActionListener(product -> {
            System.out.println("Product selected: " + product.getName());

            String[] options = {"Like Product", "Send Offer"};
            int actionChoice = JOptionPane.showOptionDialog(null,
                    "Choose Action for Product:",
                    "Product Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (actionChoice == 0) {
                likeProducts(username, password, product);
            } else if (actionChoice == 1) {
                sendOffer(username, password, product.getId());
            } else {
                System.out.println("No action selected.");
            }

            System.out.println("Choose an option or select a product via GUI: ");
        });

        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("3. Place an Order");
            System.out.println("Select Product for Action (via GUI)");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option or select a product via GUI: ");

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Please enter a valid number.");
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
                case 1 -> products = sortProducts();
                case 2 -> products = filterProducts();
                case 3 -> {
                    displayGUI.setProductActionListener(null);
                    makeOrder(username, password, products);
                    displayGUI.updateProducts(products);
                    displayGUI.setProductActionListener(product -> {
                        System.out.println("Product selected: " + product.getName());
                        String[] options = {"Like Product", "Send Offer"};
                        int actionChoice = JOptionPane.showOptionDialog(null,
                                "Choose Action for Product:",
                                "Product Action",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        if (actionChoice == 0) {
                            likeProducts(username, password, product);
                        } else if (actionChoice == 1) {
                            sendOffer(username, password, product.getId());
                        } else {
                            System.out.println("No action selected.");
                        }
                    });
                }
                case 0 -> {
                    browsing = false;
                    displayGUI.refreshWelcomePage();
                }
            }

            if (!products.isEmpty()) {
                displayGUI.updateProducts(products);
            }
        }
    }

    private List<Product> sortProducts() {
        int choice, order;

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
        displayGUI.updateProducts(sortedProducts);

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
            System.out.println("No products available for ordering. Please search or filter products first.");
            return;
        }

        Map<Integer, List<Integer>> orderedProducts = new HashMap<>();
        CountDownLatch latch = new CountDownLatch(1);

        displayGUI.updateProductsWithOrderOptions(products, selectedProducts -> {
            for (Product product : selectedProducts) {
                if (product.isAvailable()) {
                    orderedProducts
                            .computeIfAbsent(product.getListedBy(), _ -> new ArrayList<>())
                            .add(product.getId());
                }
            }

            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted: " + e.getMessage());
            return;
        }

        if (orderedProducts.isEmpty()) {
            System.out.println("No products were selected for ordering.");
            return;
        }

        boolean validAddress = false;
        String address = "";

        while (!validAddress) {
            try {
                System.out.println("Enter your address: ");
                address = scanner.nextLine();

                if (address.isEmpty() || !address.matches("[a-zA-Z0-9, ]+")) {
                    throw new ValidationException("Invalid address: Address must be a non-empty string and contain only letters, numbers, commas, or spaces.");
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


    private void viewMyListings(String username, String password) {
        List<Product> myListings = controller.getMyListings(username, password);

        JDialog dialog = new JDialog(DisplayGUI.frame, "Your Current Listings", true);
        dialog.setLayout(new BorderLayout());

        JPanel listingsPanel = new JPanel();
        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));

        if (myListings.isEmpty()) {
            listingsPanel.add(new JLabel("You have no products listed."));
        } else {
            for (Product product : myListings) {
                JPanel productPanel = new JPanel();
                productPanel.setLayout(new BorderLayout());

                JLabel imageLabel;
                if (product.getImagePath() != null) {
                    ImageIcon icon = new ImageIcon(product.getImagePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(img));
                } else {
                    imageLabel = new JLabel("No Image");
                }
                productPanel.add(imageLabel, BorderLayout.WEST);

                JTextArea productTextArea = new JTextArea(product.toString());
                productTextArea.setEditable(false);
                productTextArea.setMargin(new Insets(10, 10, 10, 10));
                productTextArea.setLineWrap(true);
                productTextArea.setWrapStyleWord(true);
                productPanel.add(productTextArea, BorderLayout.CENTER);

                listingsPanel.add(productPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        JPanel optionsPanel = new JPanel();
        JButton addButton = new JButton("Add Product to My Listings");
        JButton deleteButton = new JButton("Delete Product from My Listings");
        JButton backButton = new JButton("Back to Main Menu");

        addButton.addActionListener(e -> {
            addProductToMyListings(username, password);
        });

        deleteButton.addActionListener(e -> {
            deleteProductFromMyListings(username, password, myListings);
        });

        backButton.addActionListener(e -> {
            dialog.dispose();
        });

        optionsPanel.add(addButton);
        optionsPanel.add(deleteButton);
        optionsPanel.add(backButton);


        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(optionsPanel, BorderLayout.SOUTH);


        dialog.pack();
        dialog.setLocationRelativeTo(DisplayGUI.frame);
        dialog.setVisible(true);
    }





    private void addProductToMyListings(String username, String password) {

        List<Category> categories = controller.getCategories();


        JDialog dialog = new JDialog(DisplayGUI.frame, "Add Product to Listings", true);
        dialog.setLayout(new GridLayout(0, 2));


        JTextField nameField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField conditionField = new JTextField();


        JComboBox<Category> categoryComboBox = new JComboBox<>(categories.toArray(new Category[0]));


        dialog.add(new JLabel("Choose a category:"));
        dialog.add(categoryComboBox);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Color:"));
        dialog.add(colorField);
        dialog.add(new JLabel("Size:"));
        dialog.add(sizeField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Brand:"));
        dialog.add(brandField);
        dialog.add(new JLabel("Condition:"));
        dialog.add(conditionField);


        JButton submitButton = new JButton("Add Product");
        submitButton.addActionListener(e -> {
            try {

                int categoryId = ((Category) categoryComboBox.getSelectedItem()).getId();
                String name = nameField.getText().trim();
                String color = colorField.getText().trim();
                int size = Integer.parseInt(sizeField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                String brand = brandField.getText().trim();
                String condition = conditionField.getText().trim();

                if (name.isEmpty() || !name.matches("[a-zA-Z0-9_ ]+")) {
                    throw new ValidationException("Invalid product name.");
                }
                if (color.isEmpty() || !color.matches("[a-zA-Z]+")) {
                    throw new ValidationException("Invalid product color.");
                }
                if (size <= 0) {
                    throw new ValidationException("Invalid product size.");
                }
                if (price < 0.00) {
                    throw new ValidationException("Invalid product price.");
                }
                if (brand.isEmpty() || !brand.matches("[a-zA-Z0-9_ ]+")) {
                    throw new ValidationException("Invalid product brand.");
                }
                if (condition.isEmpty() || !condition.matches("[a-zA-Z]+")) {
                    throw new ValidationException("Invalid product condition.");
                }


                boolean success = controller.addToUserListings(username, password, categoryId, name, color, size, price, brand, condition, 0, 0);
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Product added to your listings successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Could not add product to your listings. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numeric values for size and price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(submitButton);

        dialog.pack();
        dialog.setLocationRelativeTo(DisplayGUI.frame);
        dialog.setVisible(true);
    }




    private void deleteProductFromMyListings(String username, String password, List<Product> myListings) {
        if (myListings.isEmpty()) {
            JOptionPane.showMessageDialog(DisplayGUI.frame, "You have no products listed to delete.", "No Products", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        JDialog dialog = new JDialog(DisplayGUI.frame, "Delete Product", true);
        dialog.setLayout(new BorderLayout());

        JPanel listingsPanel = new JPanel();
        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));

        for (Product product : myListings) {
            JButton deleteButton = new JButton("Delete " + product.getName() + " (ID: " + product.getId() + ")");

            deleteButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(dialog,
                        "Are you sure you want to delete " + product.getName() + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    boolean success = controller.removeFromUserListings(username, password, product.getId());
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        listingsPanel.remove(deleteButton);
                        listingsPanel.revalidate();
                        listingsPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Product deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            listingsPanel.add(deleteButton);
        }


        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));


        dialog.add(scrollPane, BorderLayout.CENTER);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton, BorderLayout.SOUTH);


        dialog.pack();
        dialog.setLocationRelativeTo(DisplayGUI.frame);
        dialog.setVisible(true);
    }





    private void browseUsersUser(String username, String password) {
        boolean browsing = true;
        List<User> displayedUsers = new ArrayList<>();


        displayGUI.setUserActionListener(new DisplayGUI.UserActionListener() {
            @Override
            public void onViewReviews(int userId) {
                viewUserReviews(userId);
            }

            @Override
            public void onViewListings(int userId) {
                viewUserListings(userId);
            }

            @Override
            public void onLeaveReview(int userId) {
                selectUserForReview(username, password, userId);
            }
        });

        while (browsing) {
            System.out.println("User Browsing Options: ");
            System.out.println("1. Sort Users");
            System.out.println("2. Filter Users");
            System.out.println("3.Select User via GUI for actions");
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Choose an option: ");

            try {
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid number.");
                }
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        displayedUsers = sortUsers();
                        displayGUI.updateUsers(displayedUsers);
                        System.out.println("Sorted users displayed in GUI.");
                    }
                    case 2 -> {
                        displayedUsers = filterUsers();
                        displayGUI.updateUsers(displayedUsers);
                        System.out.println("Filtered users displayed in GUI.");
                    }
                    case 3 -> {
                        if (displayedUsers.isEmpty()) {
                            System.out.println("Please sort or filter users first.");
                        } else {
                            displayGUI.updateUsers(displayedUsers);
                            System.out.println("Please select a user from the GUI to proceed.");
                        }
                    }
                    case 0 -> browsing = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
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

    private void viewUserReviews(int userId) {
        List<Review> reviews = controller.displayReviewsLeftForUser(userId);
        double trustScore = controller.getUserTrustScore(userId);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel trustScorePanel = new JPanel();
        trustScorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel trustScoreLabel = new JLabel("User's Trust Score: " + trustScore);
        trustScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        trustScorePanel.add(trustScoreLabel);

        mainPanel.add(trustScorePanel, BorderLayout.NORTH);

        JPanel reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));

        if (reviews.isEmpty()) {
            reviewsPanel.add(new JLabel("No reviews available for this user."));
        } else {
            for (Review review : reviews) {
                JTextArea reviewDetails = new JTextArea(review.toString());
                reviewDetails.setEditable(false);
                reviewDetails.setLineWrap(true);
                reviewDetails.setWrapStyleWord(true);
                reviewDetails.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                reviewDetails.setBackground(new Color(240, 240, 240));
                reviewsPanel.add(reviewDetails);
                reviewsPanel.add(Box.createVerticalStrut(10));
            }
        }


        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));


        mainPanel.add(scrollPane, BorderLayout.CENTER);


        JOptionPane.showMessageDialog(DisplayGUI.frame, mainPanel, "User Reviews", JOptionPane.INFORMATION_MESSAGE);
    }

    private void selectUserForReview(String username, String password, int userId) {
        JTextField reviewContentField = new JTextField();
        JTextField ratingField = new JTextField();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(new JLabel("Enter Review Content:"));
        contentPanel.add(reviewContentField);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JLabel("Enter Rating (1-5):"));
        contentPanel.add(ratingField);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        int result = JOptionPane.showConfirmDialog(
                DisplayGUI.frame, scrollPane, "Leave Review", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String content = reviewContentField.getText();
            double rating;

            try {
                rating = Double.parseDouble(ratingField.getText());
                if (rating < 1 || rating > 5) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(DisplayGUI.frame, "Invalid rating. Please enter a value between 1 and 5.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = controller.writeReview(username, password, rating, content, userId);
            String message = success ? "Review added successfully." : "Review failed. Please try again.";
            JOptionPane.showMessageDialog(DisplayGUI.frame, message, "Review Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void viewUserListings(int userId) {
        List<Product> userProducts = controller.getUserListing(userId);

        JDialog dialog = new JDialog(DisplayGUI.frame, "User  Listings", true);
        dialog.setLayout(new BorderLayout());

        JPanel listingsPanel = new JPanel();
        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));

        if (userProducts.isEmpty()) {
            listingsPanel.add(new JLabel("This user has no listed products."));
        } else {
            for (Product product : userProducts) {
                JPanel productPanel = new JPanel();
                productPanel.setLayout(new BorderLayout());

                JLabel imageLabel;
                if (product.getImagePath() != null) {
                    ImageIcon icon = new ImageIcon(product.getImagePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(img));
                } else {
                    imageLabel = new JLabel("No Image");
                }
                productPanel.add(imageLabel, BorderLayout.WEST);

                // Display the product details
                JTextArea productDetails = new JTextArea(product.toString());
                productDetails.setEditable(false);
                productDetails.setMargin(new Insets(10, 10, 10, 10));
                productDetails.setLineWrap(true);
                productDetails.setWrapStyleWord(true);
                productPanel.add(productDetails, BorderLayout.CENTER);

                listingsPanel.add(productPanel);
            }
        }


        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 700));

        dialog.add(scrollPane, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(DisplayGUI.frame);
        dialog.setVisible(true);
    }

//ADMIN

    private void adminMenu(String username, String password) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Admin Menu:");
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Users");
            System.out.println("3. See Visitor Activity");
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
                    if (choice < 0 || choice > 3) {
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
                case 3 -> seeVisitorActivity(username, password);
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


        List<User> finalDisplayedUsers = displayedUsers;
        displayGUI.setAdminActionListener(userId -> {

            controller.deleteUser(username, password, userId);

            finalDisplayedUsers.removeIf(user -> user.getId() == userId);


            displayGUI.updateUsersAdmin(finalDisplayedUsers);
            System.out.println("User  deleted successfully.");
        });

        while (browsing) {
            System.out.println("User  Browsing Options: ");
            System.out.println("1. Sort Users");
            System.out.println("2. Filter Users");
            System.out.println("3. Delete User");
            System.out.println("4. Manage User Reviews");
            System.out.println("0. Go Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice;
            try {
                if (!scanner.hasNextInt()) {
                    throw new ValidationException("Invalid input. Please enter a valid number.");
                }
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        displayedUsers = sortUsers();
                        displayGUI.updateUsersAdmin(displayedUsers);
                        System.out.println("Sorted users displayed in GUI.");
                    }
                    case 2 -> {
                        displayedUsers = filterUsers();
                        displayGUI.updateUsersAdmin(displayedUsers);
                        System.out.println("Filtered users displayed in GUI.");
                    }
                    case 3 -> {
                        if (displayedUsers.isEmpty()) {
                            System.out.println("No users available. Please sort or filter users first.");
                        } else {
                            displayGUI.updateUsersAdmin(displayedUsers);
                            System.out.println("Please select a user from the GUI to proceed.");
                        }
                    }
                    case 4 ->{
                        if(displayedUsers.isEmpty()){
                            System.out.println("No users available. Please sort or filter users first.");
                        }else{
                            manageReviews(username,password,displayedUsers);
                        }
                    }

                    case 0 -> {
                        browsing = false;
                        System.out.println("Returning to the Main Menu...");
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 0 and 4.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }

    }

    private void manageReviews(String username, String password, List<User> displayedUsers) {
        if (displayedUsers.isEmpty()) {
            System.out.println("No users to select. Please search or filter first.");
            return;
        }

        System.out.println("Available Users:");
        for (User  user : displayedUsers) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUserName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the user you want to manage reviews for: ");
        int userId = scanner.nextInt();

        User selectedUser  = displayedUsers.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);

        if (selectedUser  == null) {
            System.out.println("Invalid user ID. Please try again.");
            return;
        }

        System.out.println("Select the type of reviews to manage:");
        System.out.println("1. Reviews Left for User");
        System.out.println("2. Reviews Left by User");
        int choice = scanner.nextInt();

        List<Review> reviews;
        if (choice == 1) {
            reviews = controller.displayReviewsLeftForUser (userId);
        } else if (choice == 2) {
            reviews = controller.displayReviewsLeftByUser (username, password);
        } else {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        if (reviews.isEmpty()) {
            System.out.println("No reviews found for user ID: " + userId);
        } else {
            for (Review review : reviews) {
                System.out.println("Review ID: " + review.getId());
                System.out.println("Review Details: " + review);

                // Prompt for deletion
                while (true) {
                    System.out.print("Do you want to delete this review? (yes/no): ");
                    String deleteChoice = scanner.next();

                    if (deleteChoice.equalsIgnoreCase("yes")) {
                        controller.deleteReview(username, password, review.getId());
                        System.out.println("Review deleted successfully.");
                    } else if (deleteChoice.equalsIgnoreCase("no")) {
                        System.out.println("Returning to the review menu...");
                        return;
                    } else {
                        System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
                    }
                }
            }
        }

        scanner.close();
    }



    private void manageProducts(String username, String password) {
        boolean browsing = true;
        List<Product> products = new ArrayList<>();

        List<Product> finalProducts1 = products;
        displayGUI.setProductActionListener(product -> {
            System.out.println("Product selected: " + product.getName());

            String[] options = {"Change Category", "Delete Product"};
            int actionChoice = JOptionPane.showOptionDialog(null,
                    "Choose Action for Product:",
                    "Product Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (actionChoice == 0) {
                changeProductCategory(product.getId(), username, password);
            } else if (actionChoice == 1) {
                deleteProductByAdmin(product.getId(), username, password);
                //finalProducts1.removeIf(p -> p.getId() == product.getId());
                //displayGUI.updateProducts(finalProducts1);
            } else {
                System.out.println("No action selected.");
            }
        });

        while (browsing) {
            System.out.println("Product Browsing Options: ");
            System.out.println("1. Sort Products");
            System.out.println("2. Filter Products");
            System.out.println("3. Select Product from GUI");
            System.out.println("4. Refresh ");
            System.out.println("5. See Category Sales Ranking");
            System.out.println("0. Go Back to Repo.Main Menu");
            System.out.print("Choose an option or select a product via GUI: ");

            int choice;
            while (true) {
                try {
                    String input = scanner.nextLine();
                    if (!input.matches("\\d+")) {
                        throw new ValidationException("Please enter a valid number.");
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 0 || choice > 5) {
                        throw new ValidationException("Please enter a number between 0 and 5.");
                    }
                    break;
                } catch (ValidationException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                    System.out.print("Please choose a valid option: ");
                }
            }

            List<Product> finalProducts = products;
            switch (choice) {
                case 1 -> {
                    products = sortProducts();

                }
                case 2 -> {
                    products = filterProducts();

                }
                case 3 -> {
                    displayGUI.setProductActionListener(null);
                    displayGUI.updateProducts(products);

                    displayGUI.setProductActionListener(product -> {
                        System.out.println("Product selected: " + product.getName());
                        String[] options = {"Change Category", "Delete Product"};
                        int actionChoice = JOptionPane.showOptionDialog(null,
                                "Choose Action for Product:",
                                "Product Action",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        if (actionChoice == 0) {
                            changeProductCategory(product.getId(), username, password);
                        } else if (actionChoice == 1) {
                            deleteProductByAdmin(product.getId(), username, password);
                            finalProducts.removeIf(p -> p.getId() == product.getId());
                            displayGUI.updateProducts(finalProducts);
                        } else {
                            System.out.println("No action selected.");
                        }
                    });
                }
                case 4 -> {

                    products = controller.sortProducts(1,1);
                    displayGUI.updateProducts(products);

                }

                case 5 ->{
                    showCategoriesByIncome();
                }

                case 0 -> {
                    browsing = false;
                    displayGUI.showWelcomeMessage();
                }
            }

            if (!products.isEmpty()) {
                displayGUI.updateProducts(products);
            }
        }
    }


    private void showCategoriesByIncome() {
        Map<String, Double> sortedIncomeByCategory = controller.seeCategorySales();

        if (sortedIncomeByCategory.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No income data available.", "Category Sales", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"Category", "Sales"};
        Object[][] data = new Object[sortedIncomeByCategory.size()][2];
        int index = 0;

        for (Map.Entry<String, Double> entry : sortedIncomeByCategory.entrySet()) {
            data[index][0] = entry.getKey();
            data[index][1] = String.format("$%.2f", entry.getValue());
            index++;
        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Category Sales"));
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JDialog dialog = new JDialog((Frame) null, "Category Sales", true);
        dialog.getContentPane().add(scrollPane);
        dialog.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = dialog.getWidth();
        int dialogHeight = dialog.getHeight();
        int x = screenSize.width - dialogWidth - 300;
        int y = (screenSize.height - dialogHeight) / 2;
        dialog.setLocation(x, y);

        dialog.setVisible(true);
    }

    private void deleteProductByAdmin(int productId, String username, String password) {

        int response = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this product?",
                "Confirm Deletion",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.OK_OPTION) {
            boolean success = controller.deleteProduct(username, password, productId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Product deletion canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
        }


    }

    private void seeVisitorActivity(String username, String password) {
        System.out.println("The Marketplace App has been visited by: ");
        List<Visitor> visitors = controller.getVisitors();
        for (Visitor visitor : visitors) {
            System.out.println(visitor);
        }
    }

    private void changeProductCategory(int productId, String username, String password) {
        JFrame frame = new JFrame("Select New Category");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Choose a new category for the product:");
        label.setFont(new Font("Arial", Font.BOLD, 16));

        JComboBox<Category> categoryComboBox = new JComboBox<>();
        List<Category> categories = controller.getCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            int categoryId = ((Category) Objects.requireNonNull(categoryComboBox.getSelectedItem())).getId();

            boolean success = controller.changeCategory(productId, categoryId, username, password);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Product category updated successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to change category.");
            }
            frame.dispose();
        });

        frame.add(label);
        frame.add(categoryComboBox);
        frame.add(confirmButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - frame.getWidth();
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);



        frame.setVisible(true);
    }
}

