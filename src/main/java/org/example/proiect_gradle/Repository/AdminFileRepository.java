package org.example.proiect_gradle.Repository;

import org.example.proiect_gradle.Domain.Admin;

public class AdminFileRepository extends FileRepository<Admin> {

    public AdminFileRepository(String filename){
        super(filename);
    }

    @Override
    protected String convertObjectToString(Admin admin){
        if(admin==null){
            throw new IllegalArgumentException("Admin object cannot be null");
        }

        return admin.getId() + "," +
                admin.getUserName() + "," +
                admin.getPassword() + "," +
                admin.getEmail() + "," +
                admin.getPhone();
    }

    protected Admin createObjectFromString(String line){

        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }
        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String username = parts[1];
            String password = parts[2];
            String email = parts[3];
            String phone = parts[4];

            Admin admin = new Admin(username, password, email, phone);
            admin.setId(id);
            return admin;
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }
    }


}
