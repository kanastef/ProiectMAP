package org.example.proiect_gradle;

import org.example.proiect_gradle.Controller.Controller;
import org.example.proiect_gradle.Domain.*;
import org.example.proiect_gradle.Presentation.ConsoleApp;
import org.example.proiect_gradle.Repository.DBRepository.*;
import org.example.proiect_gradle.Repository.FileRepository.*;
import org.example.proiect_gradle.Repository.IMRepository.IMRepository;
import org.example.proiect_gradle.Service.AdminService;
import org.example.proiect_gradle.Service.UserService;
import org.example.proiect_gradle.Service.VisitorService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        IMRepository<User> userRepo = new IMRepository<>();
        IMRepository<Admin> adminRepo = new IMRepository<>();
        IMRepository<Visitor> visitorRepo = new IMRepository<>();
        IMRepository<Order> orderRepo = new IMRepository<>();
        IMRepository<Offer> offerRepo = new IMRepository<>();
        IMRepository<Review> reviewRepo = new IMRepository<>();
        IMRepository<Product> productRepo = new IMRepository<>();
        IMRepository<Category> categoryRepo=new IMRepository<>();
        VisitorService visitorService = new VisitorService(userRepo, productRepo, reviewRepo, categoryRepo);
        UserService userService = new UserService(userRepo, productRepo, reviewRepo, categoryRepo, orderRepo, offerRepo);
        AdminService adminService = new AdminService(userRepo, productRepo, reviewRepo, adminRepo, categoryRepo, orderRepo, visitorRepo);
        Controller controller = new Controller(adminService, userService, visitorService);
        ConsoleApp console = new ConsoleApp();


        Category categoryTops = new Category(CategoryName.TOPS);
        Category categoryDresses= new Category(CategoryName.DRESSES);
        Category categoryShoes = new Category(CategoryName.FOOTWEAR);
        Category categoryAccessories = new Category(CategoryName.ACCESSORIES);
        Category categoryOuterwear = new Category(CategoryName.OUTERWEAR);
        Category categoryBottoms=new Category(CategoryName.BOTTOMS);

        categoryRepo.create(categoryTops);
        categoryRepo.create(categoryDresses);
        categoryRepo.create(categoryShoes);
        categoryRepo.create(categoryAccessories);
        categoryRepo.create(categoryOuterwear);
        categoryRepo.create(categoryBottoms);

        Admin a1=new Admin("JohnDoe","Abcdefg1","johnedoe@email.com","0747896547");
        Admin a2=new Admin("JaneSmith","Abcdefg1","janesmith@email.com","0748596321");
        Admin a3=new Admin("MikeSteel","Abcdefg1","mikesteel@email.com","0748693214");

        adminRepo.create(a1);
        adminRepo.create(a2);
        adminRepo.create(a3);

        Visitor v1=new Visitor(LocalDateTime.now());
        Visitor v2=new Visitor(LocalDateTime.now());

        visitorRepo.create(v1);
        visitorRepo.create(v2);


        //Users
        User u1= new User("LisaTeak","Abcdefg1","lisateak@gmail.com","0747558114",4.5);
        User u2= new User("TinaSilver","Abcdefg1","tinasilver@gmail.com","0758669327",3.5);
        User u3 = new User("JohnSmith", "Abcdefg1", "john.smith@gmail.com", "0711223344", 4.5);
        User u4 = new User("ChrisLee", "Abcdefg1", "chris.lee@gmail.com", "0755667788", 4.1);

        userRepo.create(u1);
        userRepo.create(u2);
        userRepo.create(u3);
        userRepo.create(u4);


//
//        //Products
//
        controller.addToUserListings(u1.getUserName(),u1.getPassword(),categoryShoes.getId(),"Sandals", "white", 39, 14.35, "Birkenstock", "New", 0, 0);
        controller.addToUserListings(u1.getUserName(),u1.getPassword(),categoryAccessories.getId(),"Sunglasses", "black", 0, 29.99, "Ray-Ban", "New", 0, 0);
        controller.addToUserListings(u2.getUserName(),u2.getPassword(),categoryOuterwear.getId(),"Blazer", "navy", 44, 30.00, "Zara", "Good", 0, 0);
        controller.addToUserListings(u2.getUserName(),u2.getPassword(),categoryBottoms.getId(),"Shorts", "beige", 34, 7.49, "Nike", "Good", 0, 0);
        controller.addToUserListings(u2.getUserName(),u2.getPassword(),categoryTops.getId(),"Coat", "black", 44, 40.00, "Carhartt", "Bad", 0, 0);
        controller.addToUserListings(u2.getUserName(),u2.getPassword(),categoryAccessories.getId(),"Skirt", "pink", 36, 15.00, "Orsay", "New", 0, 0);
        controller.addToUserListings(u2.getUserName(),u2.getPassword(),categoryOuterwear.getId(),"Dress", "purple", 38, 50.00, "C&A", "Worn", 0, 0);

//        //Offers
//
        controller.userService.sendOffer(u3.getUserName(),u3.getPassword(),"Would you consider..",1,14.00);
        controller.userService.sendOffer(u3.getUserName(),u3.getPassword(),"Would you consider..",2,28.00);
        controller.userService.sendOffer(u4.getUserName(),u4.getPassword(),"Would you consider..",3,28.00);
        controller.userService.sendOffer(u4.getUserName(),u4.getPassword(),"Would you consider..",4,7.00);

        controller.userService.acceptOffer("LisaTeak", "Abcdefg1", 1);

//        //orders
//
        controller.userService.placeOrder(u3.getUserName(),u3.getPassword(), List.of(1), "sent", "StradaX");

        List<Integer> orderedProducts4=new ArrayList<>();
        orderedProducts4.add(3);
        orderedProducts4.add(4);
        controller.userService.placeOrder(u4.getUserName(),u4.getPassword(), orderedProducts4, "sent", "StradaY");
        controller.userService.placeOrder(u2.getUserName(),u2.getPassword(), List.of(2), "sent", "StradaX");

//        //reviews
//
        controller.writeReview(u3.getUserName(),u3.getPassword(),2.5,"Not good",u1.getId());
        controller.writeReview(u4.getUserName(),u4.getPassword(),4.5,"Very good",u2.getId());
        controller.writeReview(u2.getUserName(),u2.getPassword(), 1.0, "Terrible", u1.getId());

        controller.userService.addToFavorites(u3.getUserName(),u3.getPassword(),6);
        controller.userService.addToFavorites(u4.getUserName(),u4.getPassword(),7);
        console.start();

    }
}
