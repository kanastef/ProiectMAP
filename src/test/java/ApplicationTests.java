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

    DBAdminRepository adminRepo = new DBAdminRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBUserRepository userRepo = new DBUserRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBProductRepository productRepo = new DBProductRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBOfferRepository offerRepo = new DBOfferRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBOrderRepository orderRepo = new DBOrderRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBCategoryRepository categoryRepo = new DBCategoryRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBReviewRepository reviewRepo = new DBReviewRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    DBVisitorRepository visitorRepo = new DBVisitorRepository("jdbc:mysql://localhost:3306/marketplace_db", "root", "ana_db_505051");
    VisitorService visitorService = new VisitorService(userRepo, productRepo, reviewRepo, categoryRepo);
    UserService userService = new UserService(userRepo, productRepo, reviewRepo, categoryRepo, orderRepo, offerRepo);
    AdminService adminService = new AdminService(userRepo, productRepo, reviewRepo, adminRepo, categoryRepo, orderRepo);
    Controller controller = new Controller(adminService, userService, visitorService);
    ConsoleApp console = new ConsoleApp(controller);


    @Test
    public void testCrudUser() {
        User user1 = new User("MarryStone", "Secure123", "marrystone@gmail.com", "0789234123", 0);

        List<IRepository<User>> repositories = List.of(userIMRepository, userFileRepository, userRepo);

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

    }
}
