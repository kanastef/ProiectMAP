package org.example.proiect_gradle;
import org.example.proiect_gradle.Domain.User;
import org.example.proiect_gradle.Repository.IMRepository.*;
import org.example.proiect_gradle.Repository.DBRepository.*;
import org.example.proiect_gradle.Service.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;





public class ApplicationTests {

    private IMRepository<User> userIMRepository;

    @Test
    public void testCreateUser(){
        User user=new User("MarryStone","Secure123","marrystone@gamil.com","0789234123");
        userIMRepository.create(user);
        User retrievedUser=userIMRepository.read(user.getId());
        assertEquals("MarryStone",user.getUserName());
    }




}
