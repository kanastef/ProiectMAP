package org.example.proiect_gradle.Presentation;



import org.example.proiect_gradle.Domain.*;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DisplayGUI {
    private final JPanel panel;
    public static JFrame frame = null;
    private ProductActionListener productActionListener;
    private static UserActionListener userActionListener;
    private static AdminActionListener adminActionListener;

    private final JLabel welcomeLabel;

    public DisplayGUI() {
        frame = new JFrame("MarketPlace App");
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        welcomeLabel = new JLabel("<html><pre>" +
                "  ╔══════════════════════════╗  \n" +
                "  ║     WELCOME TO THE       ║  \n" +
                "  ║      -MARKETPLACE-       ║  \n" +
                "  ╚══════════════════════════╝  \n" +
                "   /\\_/\\_/\\_/\\_/\\_/\\_/\\_/\\_/\\ \n" +
                "  |                          | \n" +
                "  |  Explore, Buy, and Sell  | \n" +
                "  |   More planet-friendly   | \n" +
                "  \\_/\\_/\\_/\\_/\\_/\\_/\\_/\\_/\\_/ \n" +
                "</pre></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(157, 81, 236));

        welcomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        panel.add(welcomeLabel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(800, 1400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }




    public void showWelcomeMessage() {
        panel.removeAll();
        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public void setProductActionListener(ProductActionListener listener) {
        this.productActionListener = listener;
    }

    public void setUserActionListener(UserActionListener listener) {
        userActionListener = listener;
    }

    public void setAdminActionListener(AdminActionListener listener){
        adminActionListener=listener;
    }



    public void updateProducts(List<Product> products) {
        panel.removeAll();

        if (products == null || products.isEmpty()) {
            panel.setLayout(new BorderLayout());
            panel.add(welcomeLabel, BorderLayout.CENTER);
        } else {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            for (Product product : products) {
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

                JButton selectButton = new JButton("Select");
                selectButton.addActionListener(e -> {
                    if (productActionListener != null) {
                        productActionListener.onProductSelected(product);
                    }else{
                        JOptionPane.showMessageDialog(panel, "Create an account or log in to interact with products", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                });
                productPanel.add(selectButton, BorderLayout.EAST);

                panel.add(productPanel);
            }
        }
        panel.revalidate();
        panel.repaint();
    }





    public void updateProductsWithOrderOptions(List<Product> products, Consumer<List<Product>> onFinishOrder) {
        panel.removeAll();

        List<Product> selectedProducts = new ArrayList<>();

        if (products == null || products.isEmpty()) {
            panel.setLayout(new BorderLayout());
            panel.add(welcomeLabel, BorderLayout.CENTER);
        } else {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (Product product : products) {
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

                JCheckBox selectCheckbox = new JCheckBox("Select for Order");
                selectCheckbox.addActionListener(e -> {
                    if (selectCheckbox.isSelected()) {
                        selectedProducts.add(product);
                    } else {
                        selectedProducts.remove(product);
                    }
                });
                productPanel.add(selectCheckbox, BorderLayout.EAST);

                panel.add(productPanel);
            }

            JButton finishOrderButton = new JButton("Finish Order");
            finishOrderButton.addActionListener(e -> {
                if (onFinishOrder != null) {
                    onFinishOrder.accept(selectedProducts);
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(finishOrderButton);
            panel.add(buttonPanel);
        }

        panel.revalidate();
        panel.repaint();
    }

    public void updateUsers(List<User> users) {
        panel.removeAll();

        if (users == null || users.isEmpty()) {
            panel.setLayout(new BorderLayout());
            panel.add(welcomeLabel, BorderLayout.CENTER);
        } else {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            for (User user : users) {
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BorderLayout());

                JLabel avatarLabel;
                if (user.getProfileImagePath() != null) {
                    ImageIcon icon = new ImageIcon(user.getProfileImagePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    avatarLabel = new JLabel(new ImageIcon(img));
                } else {
                    avatarLabel = new JLabel("No Avatar");
                }
                userPanel.add(avatarLabel, BorderLayout.WEST);

                JTextArea details = new JTextArea(user.toString());
                details.setEditable(false);
                details.setMargin(new Insets(10, 10, 10, 10));
                userPanel.add(details, BorderLayout.CENTER);

                JButton selectButton = new JButton("Select");
                selectButton.addActionListener(e -> showUserOptions(user));

                userPanel.add(selectButton, BorderLayout.EAST);
                panel.add(userPanel);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public static void showUserOptions(User user) {
        Object[] options = {
                "Leave Review for User",
                "View User Reviews",
                "View User Listings",
                "Cancel"
        };

        int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose an action for user: " ,
                "User Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0:
                if (userActionListener != null) {
                    userActionListener.onLeaveReview(user.getId());
                }
                break;
            case 1:
                if (userActionListener != null) {
                    userActionListener.onViewReviews(user.getId());
                }
                break;
            case 2:
                if (userActionListener != null) {
                    userActionListener.onViewListings(user.getId());
                }
                break;
            default:
                break;
        }
    }

    public void updateUsersAdmin(List<User> users) {
        panel.removeAll();

        if (users == null || users.isEmpty()) {
            panel.setLayout(new BorderLayout());
            JLabel noUsersLabel = new JLabel("No users to display. Please sort or filter users.");
            noUsersLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noUsersLabel, BorderLayout.CENTER);
        } else {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            for (User  user : users) {
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BorderLayout());

                JLabel avatarLabel;
                if (user.getProfileImagePath() != null) {
                    ImageIcon icon = new ImageIcon(user.getProfileImagePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    avatarLabel = new JLabel(new ImageIcon(img));
                } else {
                    avatarLabel = new JLabel("No Avatar");
                }
                avatarLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                userPanel.add(avatarLabel, BorderLayout.WEST);

                JTextArea details = new JTextArea(user.toString());
                details.setEditable(false);
                details.setMargin(new Insets(10, 10, 10, 10));
                details.setLineWrap(true);
                details.setWrapStyleWord(true);
                userPanel.add(details, BorderLayout.CENTER);

                JButton selectButton = new JButton("Select");
                selectButton.addActionListener(e -> showAdminOptions(user)); // Call the method to show admin options
                userPanel.add(selectButton, BorderLayout.EAST);

                userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
                panel.add(userPanel);
            }
        }

        panel.revalidate();
        panel.repaint();
    }



    public static void showAdminOptions(User user) {
        Object[] options = {
                "Delete User",
                "Manage User Reviews",
                "Cancel"
        };

        int choice = JOptionPane.showOptionDialog(
                DisplayGUI.frame,
                "Choose an action for user: " + user.getId(),
                "Admin Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0:
                int confirmDelete = JOptionPane.showConfirmDialog(
                        DisplayGUI.frame,
                        "Are you sure you want to delete user: " + user.getId() + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    if (adminActionListener != null) {
                        adminActionListener.onDeleteUser(user.getId());
                    }
                }
                break;

            case 1:
                if (adminActionListener != null) {
                    adminActionListener.onManageReviews(user.getId());
                }
                break;

            default:
                break;
        }
    }




    public void closeGUI() {
        frame.dispose(); // Close the JFrame
    }

    public interface ProductActionListener {
        void onProductSelected(Product product);
    }

    public interface UserActionListener {
        void onViewReviews(int userId);
        void onViewListings(int userId);
        void onLeaveReview(int userId);
    }


    public interface AdminActionListener {
        void onDeleteUser(int userId);
        void onManageReviews(int userId);

    }



    public void refreshWelcomePage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
}
