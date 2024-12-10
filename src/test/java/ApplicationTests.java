import org.example.proiect_gradle.Controller.Controller;
import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Exceptions.EntityNotFoundException;
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
    private final IMRepository<Category> categoryIMRepository=new IMRepository<>();


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

    VisitorService visitorIMService= new VisitorService(userIMRepository,productIMRepository,reviewIMRepository,categoryIMRepository);
    UserService userIMService=new UserService(userIMRepository,productIMRepository,reviewIMRepository,categoryIMRepository,orderIMRepository,offerIMRepository);
    AdminService adminIMService=new AdminService(userIMRepository,productIMRepository,reviewIMRepository,adminIMRepository,categoryIMRepository,orderIMRepository);

    VisitorService visitorFileService=new VisitorService(userFileRepository,productFileRepository,reviewFileRepository,categoryFileRepository);
    UserService userFileService=new UserService(userFileRepository,productFileRepository,reviewFileRepository,categoryFileRepository,orderFileRepository,offerFileRepository);
    AdminService adminFileService=new AdminService(userFileRepository,productFileRepository,reviewFileRepository,adminFileRepository,categoryFileRepository,orderFileRepository);


    VisitorService visitorDBService = new VisitorService(dbUserRepository, dbProductRepository, dbReviewRepository, dbCategoryRepository);
    UserService userDBService = new UserService(dbUserRepository, dbProductRepository, dbReviewRepository, dbCategoryRepository, dbOrderRepository, dbOfferRepository);
    AdminService adminDBService = new AdminService(dbUserRepository, dbProductRepository, dbReviewRepository, dbAdminRepository, dbCategoryRepository, dbOrderRepository);
//    Controller controller = new Controller(adminService, userService, visitorService);
//    ConsoleApp console = new ConsoleApp(controller);


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




        List<IRepository<Offer>> offerRepositories = List.of(offerIMRepository, offerFileRepository);//, dbOfferRepository);
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository);//, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository);//, dbProductRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryFileRepository);//, dbCategoryRepository);


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





    //User Service


    //Offer Tests

    @Test
    public void testSendOffer() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Offer>> offerRepositories = List.of(offerIMRepository, offerFileRepository, dbOfferRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository,categoryFileRepository, dbCategoryRepository);

        User user1 = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        User user2 = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(user1);
            userRepo.create(user2);
            //System.out.println(userRepo.getAll());
            assertEquals(1, user1.getId());
            assertEquals(2, user2.getId());
        }

        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }


        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );

        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(user1.getUserName(), user1.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }

        for (UserService userService : userServices) {
            boolean offerSent = userService.sendOffer(
                    user2.getUserName(),
                    user2.getPassword(),
                    "Would you accept $30?",
                    1,
                    30.00
            );
            assertTrue(offerSent);
        }


        for (UserService userService : userServices) {
            try {
                for (IRepository<Offer> offerRepo : offerRepositories) {
                    List<Offer> offers = offerRepo.findByCriteria(o -> o.getTargetedProduct() == 1);
                    assertEquals(1, offers.size());
                    Offer retrievedOffer = offers.getFirst();
                    assertEquals("Would you accept $30?", retrievedOffer.getMessage());
                    assertEquals(30.00, retrievedOffer.getOfferedPrice());
                    assertEquals(user2.getId(), retrievedOffer.getSender());
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail("Exception should not have been thrown in the success case.");
            }


            try {
                boolean offerSent = userService.sendOffer(
                        user2.getUserName(),
                        user2.getPassword(),
                        "Invalid product offer",
                        -1,
                        10.00
                );
                assertFalse(offerSent);
            } catch (EntityNotFoundException e) {
                assertEquals("Product with ID -1 not found.", e.getMessage());
            } catch (Exception e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }


        List<Integer> userIdsToDelete = List.of(user1.getId(), user2.getId());
        List<Integer> productIdsToDelete = List.of(1);
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());

        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            for (Integer productId : productIdsToDelete) {
                productRepo.delete(productId);
            }
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }

        for (IRepository<Offer> offerRepo : offerRepositories) {
                offerRepo.delete(1);
        }
    }


    @Test
    public void testAcceptOffer() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Offer>> offerRepositories = List.of(offerIMRepository, offerFileRepository, dbOfferRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);

        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(seller);
            userRepo.create(buyer);
            assertEquals(1, seller.getId());
            assertEquals(2, buyer.getId());
        }

        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(userIMService, userFileService);//, userDBService);


        for(UserService userService:userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(), categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }


        for (UserService userService : userServices) {
            boolean offerSent = userService.sendOffer(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    "Would you accept $30?",
                    1,
                    30.00
            );
            assertTrue(offerSent);
        }

        for (UserService userService : userServices) {
            boolean offerAccepted = userService.acceptOffer(
                        seller.getUserName(),
                        seller.getPassword(),
                        1
                );
                assertTrue(offerAccepted);

        }


        List<Integer> userIdsToDelete = List.of(seller.getId(), buyer.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());

        for (IRepository<Offer> offerRepo : offerRepositories) {
            offerRepo.delete(1);
        }
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }
    }

    @Test
    public void testDeclineOffer() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Offer>> offerRepositories = List.of(offerIMRepository, offerFileRepository, dbOfferRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);

        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(seller);
            userRepo.create(buyer);
            assertEquals(1, seller.getId());
            assertEquals(2, buyer.getId());
        }

        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(userIMService, userFileService);//, userDBService);


        for(UserService userService:userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(), categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }


        for (UserService userService : userServices) {
            boolean offerSent = userService.sendOffer(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    "Would you accept $30?",
                    1,
                    30.00
            );
            assertTrue(offerSent);
        }

        for (UserService userService : userServices) {
            boolean offerDeclined = userService.declineOffer(
                        seller.getUserName(),
                        seller.getPassword(),
                        1
                );

                assertTrue(offerDeclined);

        }


        List<Integer> userIdsToDelete = List.of(seller.getId(), buyer.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());

        for (IRepository<Offer> offerRepo : offerRepositories) {
            offerRepo.delete(1);
        }
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }
    }



    @Test
    public void testDisplayAllUsersOffers() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Offer>> offerRepositories = List.of(offerIMRepository, offerFileRepository, dbOfferRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);

        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);

        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(seller);
            userRepo.create(buyer);
        }

        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);

        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );

        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }



        for (UserService userService : userServices) {
            boolean offerSent = userService.sendOffer(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    "Would you accept $30?",
                    1,
                    30.00
            );
            assertTrue(offerSent);

            List<Offer> receivedOffers = userService.displayAllUserOffers(seller.getUserName(), seller.getPassword());

            for(Offer receivedOffer:receivedOffers) {
                assertEquals(30.00,receivedOffer.getOfferedPrice());
                assertEquals("Would you accept $30?", receivedOffer.getMessage());
                assertEquals(1,receivedOffer.getTargetedProduct());
                assertEquals(2,receivedOffer.getSender());
                assertEquals(1,receivedOffer.getReceiver());
            }
        }


        List<Integer> userIdsToDelete = List.of(seller.getId(), buyer.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());
        for (IRepository<Offer> offerRepo : offerRepositories) {
            offerRepo.delete(1);
        }
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }

    }


    //Order tests

    @Test
    public void testPlaceOrder() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);
        List<IRepository<Order>> orderRepositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);

        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }



        for (UserService userService : userServices) {
            boolean orderPlaced = userService.placeOrder(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    List.of(1),
                    "Pending",
                    "1234 Shipping St."
            );
            assertTrue(orderPlaced);
        }


            try {
                for (IRepository<Order> orderRepo : orderRepositories) {
                    List<Order> orders = orderRepo.findByCriteria(o -> o.getBuyer() == buyer.getId());
                    assertEquals(1, orders.size());
                    Order retrievedOrder = orders.getFirst();
                    assertEquals("Pending", retrievedOrder.getStatus());
                    assertEquals("1234 Shipping St.", retrievedOrder.getShippingAddress());
                    assertEquals(buyer.getId(), retrievedOrder.getBuyer());
                    for(int productId:retrievedOrder.getProducts()) {
                        assertEquals(1, productId);
                    }
                }
            } catch (Exception e) {
                fail("Exception should not have been thrown in the success case.");
            }

        for (UserService userService : userServices) {
            try {
                boolean orderPlaced = userService.placeOrder(
                        buyer.getUserName(),
                        buyer.getPassword(),
                        List.of(-1),
                        "Pending",
                        "1234 Shipping St."
                );
                assertFalse(orderPlaced);
            } catch (EntityNotFoundException e) {
                assertEquals("Product with ID -1 not found.", e.getMessage());
            } catch (Exception e) {
                fail("Unexpected exception: " + e.getMessage());
            }
    }




        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());


        for (IRepository<Order> orderRepo : orderRepositories) {
           orderRepo.delete(1);
        }
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }
    }

    @Test
    public void testDisplayAllUserOrders() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Order>> orderRepositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);

        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }


        for (UserService userService : userServices) {
            boolean orderPlaced = userService.placeOrder(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    List.of(1),
                    "Pending",
                    "1234 Shipping St."
            );
            assertTrue(orderPlaced);

            List<Order> receivedOrders = userService.displayAllUsersOrders(seller.getUserName(), seller.getPassword());

            for (Order receivedOrder : receivedOrders) {
                for (int orderedProduct : receivedOrder.getProducts()) {
                    assertEquals(1, orderedProduct);
                }
                assertEquals("1234 Shipping St.", receivedOrder.getShippingAddress());
                assertEquals(1, receivedOrder.getBuyer());
                assertEquals(2, receivedOrder.getSeller());
            }
        }
        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());


        for (IRepository<Order> orderRepo : orderRepositories) {
            orderRepo.delete(1);
        }

        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }
    }


    @Test
    public void writeReview() {
    List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
    List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
    List<IRepository<Review>> reviewRepositories=List.of(reviewIMRepository,reviewFileRepository,dbReviewRepository);
    List<IRepository<Order>> orderRepositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);
    List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);


        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
    User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
    Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


    for (IRepository<User> userRepo : userRepositories) {
        userRepo.create(buyer);
        userRepo.create(seller);
    }


    for (IRepository<Category> categoryRepo : categoryRepositories) {
        categoryRepo.create(categoryOuterwear);
    }

    List<UserService> userServices = List.of(
            userIMService,
            userFileService,
            userDBService
    );


    for (UserService userService : userServices) {
        boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
        assertTrue(productListed);
    }



    for (UserService userService : userServices) {
        boolean orderPlaced = userService.placeOrder(
                buyer.getUserName(),
                buyer.getPassword(),
                List.of(1),
                "Pending",
                "1234 Shipping St."
        );
        assertTrue(orderPlaced);

    }

    for(UserService userService:userServices){
        boolean reviewWritten=userService.writeReview(buyer.getUserName(), buyer.getPassword(), 4,"Very good service",2);
        assertTrue(reviewWritten);
    }


    try {
        for (IRepository<Review> reviewRepo : reviewRepositories) {
            List<Review> reviews = reviewRepo.findByCriteria(o -> o.getReviewer() == buyer.getId());
            assertEquals(1, reviews.size());
            Review retrievedReview = reviews.getFirst();
            assertEquals(4, retrievedReview.getGrade());
            assertEquals("Very good service", retrievedReview.getMessage());
            assertEquals(buyer.getId(), retrievedReview.getReviewer());
            assertEquals(2, retrievedReview.getReviewee());
        }
    } catch (Exception e) {
        fail("Exception should not have been thrown in the success case.");
    }


    try {
        for (UserService userService : userServices) {
            boolean reviewWritten = userService.writeReview(buyer.getUserName(), buyer.getPassword(), 5, "Invalid Review", -1);  // Invalid product ID
            assertFalse(reviewWritten);
        }
    } catch (EntityNotFoundException e) {
        assertEquals("Reviewee user not found.", e.getMessage());
    } catch (Exception e) {
        fail("Unexpected exception: " + e.getMessage());
    }




    List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
    List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());


    for (IRepository<Order> orderRepo : orderRepositories) {
        orderRepo.delete(1);
    }

    for (IRepository<User> userRepo : userRepositories) {
        for (Integer userId : userIdsToDelete) {
            userRepo.delete(userId);
        }
    }
    for (IRepository<Product> productRepo : productRepositories) {
        productRepo.delete(1);
    }
    for (IRepository<Category> categoryRepo : categoryRepositories) {
        for (Integer categoryId : categoryIdsToDelete) {
            categoryRepo.delete(categoryId);
        }
    }

    for(IRepository<Review> reviewRepo : reviewRepositories){
        reviewRepo.delete(1);
    }

}

    @Test
    public void testDisplayMadeReviews(){
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Review>> reviewRepositories=List.of(reviewIMRepository,reviewFileRepository,dbReviewRepository);
        List<IRepository<Order>> orderRepositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);


        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }



        for (UserService userService : userServices) {
            boolean orderPlaced = userService.placeOrder(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    List.of(1),
                    "Pending",
                    "1234 Shipping St."
            );
            assertTrue(orderPlaced);

        }

        for(UserService userService:userServices){
            boolean reviewWritten=userService.writeReview(buyer.getUserName(), buyer.getPassword(), 4,"Very good service",2);
            assertTrue(reviewWritten);

            List<Review> madeReviews=userService.displayMadePersonalReviews(buyer.getUserName(), buyer.getPassword());
            for(Review madeReview:madeReviews){
                assertEquals("Very good service",madeReview.getMessage());
                assertEquals(4,madeReview.getGrade());
                assertEquals(1,madeReview.getReviewer());
                assertEquals(2,madeReview.getReviewee());
            }
        }
        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());


        for (IRepository<Order> orderRepo : orderRepositories) {
            orderRepo.delete(1);
        }

        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }

        for(IRepository<Review> reviewRepo : reviewRepositories){
            reviewRepo.delete(1);
        }


    }

    @Test
    public void testDeleteReview(){

        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Review>> reviewRepositories=List.of(reviewIMRepository,reviewFileRepository,dbReviewRepository);
        List<IRepository<Order>> orderRepositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);


            User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }



        for (UserService userService : userServices) {
            boolean orderPlaced = userService.placeOrder(
                    buyer.getUserName(),
                    buyer.getPassword(),
                    List.of(1),
                    "Pending",
                    "1234 Shipping St."
            );
            assertTrue(orderPlaced);

        }

        for(UserService userService:userServices){
            boolean reviewWritten=userService.writeReview(buyer.getUserName(), buyer.getPassword(), 4,"Very good service",2);
            assertTrue(reviewWritten);
        }


        for (UserService userService : userServices) {
            boolean reviewDeleted = userService.deleteReview(buyer.getUserName(), buyer.getPassword(), 1);
            assertTrue(reviewDeleted);
        }

        for (IRepository<Review> reviewRepo : reviewRepositories) {
            List<Review> reviews = reviewRepo.findByCriteria(o -> o.getReviewer() == buyer.getId());
            assertEquals(0, reviews.size());
        }


        try {

            for (UserService userService : userServices) {
                boolean reviewDeleted = userService.deleteReview(buyer.getUserName(), buyer.getPassword(), -1);
                assertFalse(reviewDeleted);
            }
        } catch (EntityNotFoundException e) {
            assertEquals("Review with ID -1 not found.", e.getMessage());  // Expected exception message
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }




        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());


        for (IRepository<Order> orderRepo : orderRepositories) {
            orderRepo.delete(1);
        }

        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }
        }


    }

    @Test
    public void testLikeProduct() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);


        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }


        for (UserService userService : userServices) {
            boolean addedToFavorites = userService.addToFavorites(buyer.getUserName(), buyer.getPassword(), 1);
            assertTrue(addedToFavorites);

            List<Integer> likedProducts = buyer.getFavourites();
            for (int likedProductId : likedProducts) {
                assertEquals(1, likedProductId);
            }
        }

        try {
            for (UserService userService : userServices) {
                boolean addedToFavorites = userService.addToFavorites(buyer.getUserName(), buyer.getPassword(), -1);
                assertFalse(addedToFavorites);
            }
        } catch (EntityNotFoundException e) {
            assertEquals("Product with ID -1 not found.", e.getMessage());
        }


        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }

        }
    }

    @Test
    public void testRemoveFromFavourites() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);


        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }


        for (UserService userService : userServices) {
            boolean addedToFavorites = userService.addToFavorites(buyer.getUserName(), buyer.getPassword(), 1);
            assertTrue(addedToFavorites);

        }


        for (UserService userService : userServices) {
            boolean removedFromFavorites = userService.removeFromFavourites(buyer.getUserName(), buyer.getPassword(), 1);
            assertTrue(removedFromFavorites);

            assertEquals(0, buyer.getFavourites().size());
        }

        try {
            for (UserService userService : userServices) {
                boolean removedFromFavorites = userService.removeFromFavourites(buyer.getUserName(), buyer.getPassword(), -1);
                assertFalse(removedFromFavorites);
            }
        } catch (EntityNotFoundException e) {
            assertEquals("Product with ID -1 not found.", e.getMessage());
        }


        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Product> productRepo : productRepositories) {
            productRepo.delete(1);
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }

        }
    }


    @Test
    public void testDeleteListedProduct() {
        List<IRepository<User>> userRepositories = List.of(userIMRepository, userFileRepository, dbUserRepository);
        List<IRepository<Category>> categoryRepositories = List.of(categoryIMRepository, categoryFileRepository, dbCategoryRepository);
        List<IRepository<Product>> productRepositories = List.of(productIMRepository, productFileRepository, dbProductRepository);


        User buyer = new User("BuyerUser", "Password2", "buyer@gmail.com", "0789234567", 0.0);
        User seller = new User("SellerUser", "Password1", "seller@gmail.com", "0789123456", 0.0);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);


        for (IRepository<User> userRepo : userRepositories) {
            userRepo.create(buyer);
            userRepo.create(seller);
        }


        for (IRepository<Category> categoryRepo : categoryRepositories) {
            categoryRepo.create(categoryOuterwear);
        }

        List<UserService> userServices = List.of(
                userIMService,
                userFileService,
                userDBService
        );


        for (UserService userService : userServices) {
            boolean productListed = userService.listProduct(seller.getUserName(), seller.getPassword(),
                    categoryOuterwear.getId(), "Vintage Jacket", "Red", 40, 50.00, "BrandName", "Good condition", 0, 0);
            assertTrue(productListed);
        }

        for (UserService userService : userServices) {
            boolean deleted = userService.deleteListedProduct(seller.getUserName(), seller.getPassword(), 1);
            assertTrue(deleted);
        }

        try {
            for (UserService userService : userServices) {
                boolean deleted = userService.deleteListedProduct(seller.getUserName(), seller.getPassword(), -1);
                assertFalse(deleted);
            }
        } catch (EntityNotFoundException e) {
            assertEquals("Product with ID -1 not listed by the user.", e.getMessage());
        }

        List<Integer> userIdsToDelete = List.of(buyer.getId(), seller.getId());
        List<Integer> categoryIdsToDelete = List.of(categoryOuterwear.getId());
        for (IRepository<User> userRepo : userRepositories) {
            for (Integer userId : userIdsToDelete) {
                userRepo.delete(userId);
            }
        }
        for (IRepository<Category> categoryRepo : categoryRepositories) {
            for (Integer categoryId : categoryIdsToDelete) {
                categoryRepo.delete(categoryId);
            }

        }
    }



}
