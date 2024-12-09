import org.example.proiect_gradle.Controller.Controller;
import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Presentation.ConsoleApp;
import org.example.proiect_gradle.Repository.IMRepository.*;
import org.example.proiect_gradle.Repository.IRepository;
import org.example.proiect_gradle.Service.AdminService;
import org.example.proiect_gradle.Service.UserService;
import org.example.proiect_gradle.Service.VisitorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.proiect_gradle.Repository.FileRepository.*;
import org.example.proiect_gradle.Repository.DBRepository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ApplicationTests {

    private final IMRepository<User> userIMRepository = new IMRepository<>();
    private final IMRepository<Admin> adminIMRepository = new IMRepository<>();
    private final IMRepository<Visitor> visitorIMRepository = new IMRepository<>();
    private final IMRepository<Order> orderIMRepository = new IMRepository<>();
    private final IMRepository<Offer> offerIMRepository = new IMRepository<>();
    private final IMRepository<Review> reviewIMRepository = new IMRepository<>();
    private final IMRepository<Product> productIMRepository = new IMRepository<>();


    String userFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/users.txt";
    String productFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/products.txt";
    String categoriesFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/categories.txt";
    String offersFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/offers.txt";
    String orderFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/orders.txt";
    String reviewsFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/reviews.txt";
    String adminsFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/admins.txt";
    String visitorsFilename = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/visitors.txt";
    String likedProducts = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/likedProducts.txt";
    String orderedProducts = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/orderedProducts.txt";
    VisitorFileRepository visitorFileRepository = new VisitorFileRepository(visitorsFilename);
    UserFileRepository userFileRepository = new UserFileRepository(userFilename, likedProducts);
    ProductFileRepository productFileRepository = new ProductFileRepository(productFilename);
    CategoryFileRepository categoryFileRepository = new CategoryFileRepository(categoriesFilename);
    OfferFileRepository offerFileRepository = new OfferFileRepository(offersFilename);
    OrderFileRepository orderFileRepository = new OrderFileRepository(orderFilename, orderedProducts);
    ReviewFileRepository reviewFileRepository = new ReviewFileRepository(reviewsFilename);
    AdminFileRepository adminFileRepository = new AdminFileRepository(adminsFilename);

    DBAdminRepository dbAdminRepository = new DBAdminRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBUserRepository dbUserRepository = new DBUserRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBProductRepository dbProductRepository = new DBProductRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBOfferRepository dbOfferRepository = new DBOfferRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBOrderRepository dbOrderRepository = new DBOrderRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBCategoryRepository dbCategoryRepository = new DBCategoryRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBReviewRepository dbReviewRepository = new DBReviewRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBVisitorRepository dbVisitorRepository = new DBVisitorRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    VisitorService visitorService = new VisitorService(dbUserRepository, dbProductRepository, dbReviewRepository, dbCategoryRepository);
    UserService userService = new UserService(dbUserRepository, dbProductRepository, dbReviewRepository, dbCategoryRepository, dbOrderRepository, dbOfferRepository);
    AdminService adminService = new AdminService(dbUserRepository, dbProductRepository, dbReviewRepository, dbAdminRepository, dbCategoryRepository, dbOrderRepository);
    Controller controller = new Controller(adminService, userService, visitorService);
    //ConsoleApp console = new ConsoleApp(controller);


    @Test
    public void testCrudUser() {
        List<IRepository<User>> repositories = List.of(userIMRepository, userFileRepository, dbUserRepository);

        for(IRepository<User> repository : repositories) {
            User user1 = new User("MarryStone", "Secure123", "marrystone@gmail.com", "0789234123", 0);

            //Test Create
            repository.create(user1);
            User retrievedUser = repository.read(user1.getId());
            assertNotNull(retrievedUser);
            assertEquals("MarryStone", retrievedUser.getUserName());

            //Test Update
            String newUsername = "AnnaSmith";
            user1.setUserName(newUsername);
            repository.update(user1);
            User updatedUser = repository.read(user1.getId());
            assertNotNull(updatedUser);
            assertEquals(newUsername, updatedUser.getUserName());

            //Test Find By Criteria
            List<User> foundUsers = repository.findByCriteria(user -> "AnnaSmith".equals(user.getUserName()));
            assertNotNull(foundUsers);
            assertEquals(1, foundUsers.size());
            assertEquals(user1, foundUsers.getFirst());


            // Test Delete
            repository.delete(user1.getId());
            User deletedUser = repository.read(user1.getId());
            assertNull(deletedUser);

        }

    }


    @Test
    public void testCrudAdmin(){

        List<IRepository<Admin>> repositories=List.of(adminIMRepository,adminFileRepository,dbAdminRepository);

        for(IRepository<Admin> repository:repositories){
            Admin admin1 =new Admin("AdamBeckner","Password1","adambeckner@email.com","0789447512");

            //Test Create
            repository.create(admin1);
            assertNotNull(admin1);
            Admin retrievedAdmin=repository.read(admin1.getId());
            assertEquals("AdamBeckner",admin1.getUserName());

            //Test Update
            String newAdminEmail="beckner@yahoo.com";
            admin1.setEmail(newAdminEmail);
            repository.update(admin1);
            Admin updatedAdmin=repository.read(admin1.getId());
            assertNotNull(admin1);
            assertEquals(newAdminEmail,updatedAdmin.getEmail());

            //Test Find By Criteria
            List<Admin> foundAdmins=repository.findByCriteria(admin -> "AdamBeckner".equals(admin1.getUserName()));

            //Test Delete
            repository.delete(admin1.getId());
            Admin deletedAdmin=repository.read(admin1.getId());
            assertNull(deletedAdmin);

        }

    }


    @Test
    public void testCrudVisitor() {
        LocalDateTime now = LocalDateTime.of(2023, 7, 15, 14, 37, 25);
        List<IRepository<Visitor>> repositories = List.of(visitorIMRepository, visitorFileRepository, dbVisitorRepository);

        for (IRepository<Visitor> repository : repositories) {
            Visitor visitor1 = new Visitor(now);

            // Test Create
            repository.create(visitor1);
            Visitor retrievedVisitor = repository.read(visitor1.getId());
            assertNotNull(retrievedVisitor);
            assertEquals(now, retrievedVisitor.getVisitDate());

            // Test Update
            LocalDateTime newVisitDate = now.plusDays(1);
            visitor1.setVisitDate(newVisitDate);
            repository.update(visitor1);
            Visitor updatedVisitor = repository.read(visitor1.getId());
            assertNotNull(updatedVisitor);
            assertEquals(newVisitDate, updatedVisitor.getVisitDate());

            // Test Delete
            repository.delete(visitor1.getId());
            Visitor deletedVisitor = repository.read(visitor1.getId());
            assertNull(deletedVisitor);
        }
    }



    @Test
    public void testCrudProduct() {

        User user1 = new User("JeremyReef", "Password2", "jeremyreef@gamil.com", "0784556211", 0.0);


        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryFileRepository, dbCategoryRepository);

        Category categoryTops = new Category(CategoryName.TOPS);



        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.create(categoryTops);
            Category retrievedCategory = categoryRepository.read(categoryTops.getId());
            assertNotNull(retrievedCategory);
            assertEquals(CategoryName.TOPS, retrievedCategory.getName());
        }


        for (IRepository<User> userRepository : userRepositories) {
            userRepository.create(user1);
            User retrievedUser = userRepository.read(user1.getId());
            assertNotNull(retrievedUser);
            assertEquals("JeremyReef", retrievedUser.getUserName());
        }


        for (IRepository<Product> productRepository : productRepositories) {

            Product product1 = new Product("Vintage Top", "blue", 38, 8.96, "Nike", "Good", 0, 0, user1.getId());
            product1.setCategory(categoryTops.getId());
            // Test Create
            productRepository.create(product1);
            Product retrievedProduct = productRepository.read(product1.getId());
            assertNotNull(retrievedProduct);
            assertEquals(user1.getId(), retrievedProduct.getListedBy());

            // Test Update
            product1.setPrice(9.30);
            productRepository.update(product1);
            Product updatedProduct = productRepository.read(product1.getId());
            assertNotNull(updatedProduct);
            assertEquals(9.30, updatedProduct.getPrice());

            // Test Delete
            productRepository.delete(product1.getId());
            Product deletedProduct = productRepository.read(product1.getId());
            assertNull(deletedProduct);
        }


        for (IRepository<User> userRepository : userRepositories) {
            userRepository.delete(user1.getId());
            User deletedUser = userRepository.read(user1.getId());
            assertNull(deletedUser);
        }


        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.delete(categoryTops.getId());
            Category deletedCategory = categoryRepository.read(categoryTops.getId());
            assertNull(deletedCategory);
        }
    }


    @Test
    public void testCrudCategory() {

        List<IRepository<Category>> repositories = List.of(categoryFileRepository, dbCategoryRepository);

        for (IRepository<Category> repository : repositories) {
            Category category1 = new Category(CategoryName.OUTERWEAR);

            // Test Create
            repository.create(category1);
            Category retrievedCategory = repository.read(category1.getId());
            assertNotNull(retrievedCategory);
            assertEquals(CategoryName.OUTERWEAR, retrievedCategory.getName());

            // Test Update
            category1.setName(CategoryName.DRESSES);
            repository.update(category1);
            Category updatedCategory = repository.read(category1.getId());
            assertNotNull(updatedCategory);
            assertEquals(CategoryName.DRESSES, updatedCategory.getName());

            // Test Delete
            repository.delete(category1.getId());
            Category deletedCategory = repository.read(category1.getId());
            assertNull(deletedCategory);
        }
    }


    @Test
    public void testCrudReview() {
        // Create User and Product
        User user1 = new User("JeremyReef", "Password2", "jeremyreef@gamil.com", "0784556211", 0.0);
        User user2 = new User("MarryStone", "Secure123", "marrystone@gmail.com", "0789234123", 0);
        Product product1 = new Product("Vintage Top", "blue", 38, 8.96, "Nike", "Good", 0, 0, 1);
        Category categoryTops = new Category(CategoryName.TOPS);
        product1.setCategory(1);



        List<IRepository<Review>> reviewRepositories = List.of(reviewIMRepository, reviewFileRepository, dbReviewRepository);
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryFileRepository, dbCategoryRepository);

        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.create(categoryTops);
            Category retrievedCategory = categoryRepository.read(categoryTops.getId());
            assertNotNull(retrievedCategory);
            assertEquals(CategoryName.TOPS, retrievedCategory.getName());
        }

        for (IRepository<User> userRepository : userRepositories) {
            userRepository.create(user1);
            userRepository.create(user2);
            User retrievedUser1 = userRepository.read(user1.getId());
            User retrievedUser2 = userRepository.read(user2.getId());
            assertNotNull(retrievedUser1);
            assertEquals("JeremyReef", retrievedUser1.getUserName());
            assertNotNull(retrievedUser2);
            assertEquals("MarryStone", retrievedUser2.getUserName());
        }

        for (IRepository<Product> productRepository : productRepositories) {
            productRepository.create(product1);
            Product retrievedProduct1 = productRepository.read(product1.getId());
            assertNotNull(retrievedProduct1);
            assertEquals(product1.getName(), retrievedProduct1.getName());
        }

        for (IRepository<Review> reviewRepository : reviewRepositories) {

            Review review1 = new Review(4.5, "Excellent Service", user2.getId(), user1.getId());
            // Test Create
            reviewRepository.create(review1);
            Review retrievedReview = reviewRepository.read(review1.getId());
            assertNotNull(retrievedReview);
            assertEquals(4.5, retrievedReview.getGrade());

            // Test Update
            review1.setMessage("Great seller!");
            reviewRepository.update(review1);
            Review updatedReview = reviewRepository.read(review1.getId());
            assertNotNull(updatedReview);
            assertEquals("Great seller!", updatedReview.getMessage());

            // Test Delete
            reviewRepository.delete(review1.getId());
            Review deletedReview = reviewRepository.read(review1.getId());
            assertNull(deletedReview);
        }


        for (IRepository<Product> productRepository : productRepositories) {
            productRepository.delete(product1.getId());
            Product deletedProduct = productRepository.read(product1.getId());
            assertNull(deletedProduct);
        }


        List<Integer> userIdsToDelete = List.of(user1.getId(), user2.getId());
        for (IRepository<User> userRepository : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepository.delete(userId);
            }
            for (Integer userId : userIdsToDelete) {
                User deletedUser = userRepository.read(userId);
                assertNull(deletedUser);
            }
        }


        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.delete(categoryTops.getId());
            Category deletedCategory = categoryRepository.read(categoryTops.getId());
            assertNull(deletedCategory);
        }
    }


    @Test
    public void testCrudOffer() {

        User user1 = new User("JeremyReef", "Password2", "jeremyreef@gamil.com", "0784556211", 0.0);
        User user2 = new User("MarryStone", "Secure123", "marrystone@gmail.com", "0789234123", 0);
        Product product1 = new Product("Vintage Top", "blue", 38, 8.96, "Nike", "Good", 0, 0, 1);


        Category categoryTops = new Category(CategoryName.TOPS);
        product1.setCategory(1);




        List<IRepository<Offer>> offerRepositories = List.of(offerIMRepository, offerFileRepository, dbOfferRepository);
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryFileRepository, dbCategoryRepository);


        for (IRepository<User> userRepository : userRepositories) {
            userRepository.create(user1);
            userRepository.create(user2);
            User retrievedUser1 = userRepository.read(user1.getId());
            User retrievedUser2 = userRepository.read(user2.getId());
            assertNotNull(retrievedUser1);
            assertEquals("JeremyReef", retrievedUser1.getUserName());
            assertNotNull(retrievedUser2);
            assertEquals("MarryStone", retrievedUser2.getUserName());
        }

        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.create(categoryTops);
            Category retrievedCategory = categoryRepository.read(categoryTops.getId());
            assertNotNull(retrievedCategory);
            assertEquals(CategoryName.TOPS, retrievedCategory.getName());
        }


        for (IRepository<Product> productRepository : productRepositories) {
            productRepository.create(product1);
            Product retrievedProduct = productRepository.read(product1.getId());
            assertNotNull(retrievedProduct);
            assertEquals(user1.getId(), retrievedProduct.getListedBy());
            assertEquals(categoryTops.getId(), retrievedProduct.getCategory());
        }


        for (IRepository<Offer> offerRepository : offerRepositories) {
            Offer offer1 = new Offer("Would you do 13.50 for this?", 15.50, product1.getId(), user2.getId(), user1.getId());

            // Test Create
            offerRepository.create(offer1);
            Offer retrievedOffer = offerRepository.read(offer1.getId());
            assertNotNull(retrievedOffer);
            assertEquals(15.50, retrievedOffer.getOfferedPrice());
            assertEquals(product1.getId(), retrievedOffer.getTargetedProduct());

            // Test Update
            offer1.setOfferedPrice(13.20);
            offerRepository.update(offer1);
            Offer updatedOffer = offerRepository.read(offer1.getId());
            assertNotNull(updatedOffer);
            assertEquals(13.20, updatedOffer.getOfferedPrice());

            // Test Delete
            offerRepository.delete(offer1.getId());
            Offer deletedOffer = offerRepository.read(offer1.getId());
            assertNull(deletedOffer);
        }


        for (IRepository<Product> productRepository : productRepositories) {
            productRepository.delete(product1.getId());
            Product deletedProduct = productRepository.read(product1.getId());
            assertNull(deletedProduct);
        }


        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.delete(categoryTops.getId());
            Category deletedCategory = categoryRepository.read(categoryTops.getId());
            assertNull(deletedCategory);
        }


        List<Integer> userIdsToDelete = List.of(user1.getId(), user2.getId());
        for (IRepository<User> userRepository : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepository.delete(userId);
            }
            for (Integer userId : userIdsToDelete) {
                User deletedUser = userRepository.read(userId);
                assertNull(deletedUser);
            }
        }
    }




    @Test
    public void testCrudOrder() {

        User user1 = new User("JeremyReef", "Password2", "jeremyreef@gamil.com", "0784556211", 0.0);
        User user2 = new User("MarryStone", "Secure123", "marrystone@gmail.com", "0789234123", 0);

        Product product1 = new Product("Vintage Top", "blue", 38, 8.96, "Nike", "Good", 0, 0, 1);


        Category categoryTops = new Category(CategoryName.TOPS);
        product1.setCategory(1);




        List<IRepository<Order>> orderRepositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryFileRepository, dbCategoryRepository);


        for (IRepository<User> userRepository : userRepositories) {
            userRepository.create(user1);
            userRepository.create(user2);
            User retrievedUser1 = userRepository.read(user1.getId());
            User retrievedUser2 = userRepository.read(user2.getId());
            assertNotNull(retrievedUser1);
            assertEquals("JeremyReef", retrievedUser1.getUserName());
            assertNotNull(retrievedUser2);
            assertEquals("MarryStone", retrievedUser2.getUserName());
        }


        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.create(categoryTops);
            Category retrievedCategory = categoryRepository.read(categoryTops.getId());
            assertNotNull(retrievedCategory);
            assertEquals(CategoryName.TOPS, retrievedCategory.getName());
        }


        for (IRepository<Product> productRepository : productRepositories) {
            productRepository.create(product1);
            Product retrievedProduct = productRepository.read(product1.getId());
            assertNotNull(retrievedProduct);
            assertEquals(user1.getId(), retrievedProduct.getListedBy());
            assertEquals(categoryTops.getId(), retrievedProduct.getCategory());
        }


        for (IRepository<Order> orderRepository : orderRepositories) {
            Order order1 = new Order(List.of(product1.getId()), "pending", "Strada Test", user2.getId(), user1.getId());


            // Test Create
            orderRepository.create(order1);
            Order retrievedOrder = orderRepository.read(order1.getId());
            assertNotNull(retrievedOrder);
            assertEquals("Strada Test", retrievedOrder.getShippingAddress());

            // Test Update
            order1.setStatus("shipped");
            orderRepository.update(order1);
            Order updatedOrder = orderRepository.read(order1.getId());
            assertNotNull(updatedOrder);
            assertEquals("shipped", updatedOrder.getStatus());

            // Test Delete
            orderRepository.delete(order1.getId());
            Order deletedOrder = orderRepository.read(order1.getId());
            assertNull(deletedOrder);
        }


        for (IRepository<Product> productRepository : productRepositories) {
            productRepository.delete(product1.getId());
            Product deletedProduct = productRepository.read(product1.getId());
            assertNull(deletedProduct);
        }


        for (IRepository<Category> categoryRepository : categoryRepositories) {
            categoryRepository.delete(categoryTops.getId());
            Category deletedCategory = categoryRepository.read(categoryTops.getId());
            assertNull(deletedCategory);
        }


        List<Integer> userIdsToDelete = List.of(user1.getId(), user2.getId());
        for (IRepository<User> userRepository : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepository.delete(userId);
            }
            for (Integer userId : userIdsToDelete) {
                User deletedUser = userRepository.read(userId);
                assertNull(deletedUser);
            }
        }
    }









}
