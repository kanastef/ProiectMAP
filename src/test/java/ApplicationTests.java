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
    String listedProducts = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/listedProducts.txt";
    String orderedProducts = "src/main/java/org/example/proiect_gradle/Repository/FileRepository/ObjectFiles/orderedProducts.txt";
    VisitorFileRepository visitorFileRepository = new VisitorFileRepository(visitorsFilename);
    UserFileRepository userFileRepository = new UserFileRepository(userFilename, listedProducts, likedProducts);
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
    ConsoleApp console = new ConsoleApp(controller);


    @Test
    public void testCrudUser() {
        User user1 = new User("MarryStone", "Secure123", "marrystone@gmail.com", "0789234123", 0);
        List<IRepository<User>> repositories = List.of(userIMRepository, userFileRepository, dbUserRepository);

        for(IRepository<User> repository : repositories) {

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

        Admin admin1 =new Admin("AdamBeckner","Password1","adambeckner@email.com","0789447512");
        List<IRepository<Admin>> repositories=List.of(adminIMRepository,adminFileRepository,dbAdminRepository);

        for(IRepository<Admin> repository:repositories){

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
        LocalDateTime now = LocalDateTime.now();
        Visitor visitor1 = new Visitor(now);
        List<IRepository<Visitor>> repositories = List.of(visitorIMRepository, visitorFileRepository, dbVisitorRepository);

        for (IRepository<Visitor> repository : repositories) {

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
        Product product1 = new Product("Vintage Top","blue",38,8.96,"Nike","Good",0,0,1);
        List<IRepository<Product>> repositories = List.of(productIMRepository, productFileRepository, dbProductRepository);

        for (IRepository<Product> repository : repositories) {

            // Test Create
            repository.create(product1);
            Product retrievedProduct = repository.read(product1.getId());
            assertNotNull(retrievedProduct);
            assertEquals(1, retrievedProduct.getListedBy());

            // Test Update
            product1.setPrice(9.30);
            repository.update(product1);
            Product updatedProduct = repository.read(product1.getId());
            assertNotNull(updatedProduct);
            assertEquals(9.30, updatedProduct.getPrice());


            // Test Delete
            repository.delete(product1.getId());
            Product deletedProduct = repository.read(product1.getId());
            assertNull(deletedProduct);
        }
    }


    @Test
    public void testCrudCategory() {
        Category category1 = new Category(CategoryName.OUTERWEAR);
        List<IRepository<Category>> repositories = List.of(categoryFileRepository, dbCategoryRepository);

        for (IRepository<Category> repository : repositories) {

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
        Review review1 = new Review(4.5,"Excellent Service",2,1);
        List<IRepository<Review>> repositories = List.of(reviewIMRepository, reviewFileRepository, dbReviewRepository);

        for (IRepository<Review> repository : repositories) {

            // Test Create
            repository.create(review1);
            Review retrievedReview = repository.read(review1.getId());
            assertNotNull(retrievedReview);
            assertEquals(4.5, retrievedReview.getGrade());

            // Test Update
            review1.setMessage("Great seller!");
            repository.update(review1);
            Review updatedReview = repository.read(review1.getId());
            assertNotNull(updatedReview);
            assertEquals("Great seller!", updatedReview.getMessage());


            // Test Delete
            repository.delete(review1.getId());
            Review deletedReview = repository.read(review1.getId());
            assertNull(deletedReview);
        }
    }

    @Test
    public void testCrudOffer() {
        Offer offer1 = new Offer("Would you do 13.50 for this?",15.50,1,1,2);
        List<IRepository<Offer>> repositories = List.of(offerIMRepository, offerFileRepository, dbOfferRepository);

        for (IRepository<Offer> repository : repositories) {

            // Test Create
            repository.create(offer1);
            Offer retrievedOffer = repository.read(offer1.getId());
            assertNotNull(retrievedOffer);
            assertEquals(1, retrievedOffer.getTargetedProduct());

            // Test Update
            offer1.setOfferedPrice(13.20);
            repository.update(offer1);
            Offer updatedOffer = repository.read(offer1.getId());
            assertNotNull(updatedOffer);
            assertEquals(13.20, updatedOffer.getOfferedPrice());



            // Test Delete
            repository.delete(offer1.getId());
            Offer deletedOffer = repository.read(offer1.getId());
            assertNull(deletedOffer);
        }
    }


    @Test
    public void testCrudOrder(){
        Order order1=new Order(List.of(1),"pending","Strada Test",1,2);
        List<IRepository<Order>> repositories = List.of(orderIMRepository, orderFileRepository, dbOrderRepository);

        for (IRepository<Order> repository : repositories) {

            // Test Create
            repository.create(order1);
            Order retrievedOrder = repository.read(order1.getId());
            assertNotNull(retrievedOrder);
            assertEquals("Strada Test", retrievedOrder.getShippingAddress());

            // Test Update
            order1.setStatus("shipped");
            repository.update(order1);
            Order updatedOrder = repository.read(order1.getId());
            assertNotNull(updatedOrder);
            assertEquals("shipped", updatedOrder.getStatus());


            // Test Delete
            repository.delete(order1.getId());
            Order deletedOrder = repository.read(order1.getId());
            assertNull(deletedOrder);
        }


    }








}
