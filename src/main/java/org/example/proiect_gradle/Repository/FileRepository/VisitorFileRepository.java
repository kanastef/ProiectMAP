package org.example.proiect_gradle.Repository.FileRepository;

import org.example.proiect_gradle.Domain.Visitor;

import java.time.LocalDateTime;

public class VisitorFileRepository extends FileRepository<Visitor> {

    public VisitorFileRepository(String filename){
        super(filename);
    }

    public String convertObjectToString(Visitor visitor ){

        if(visitor==null){
            throw new IllegalArgumentException("Visitor object cannot be null");
        }

        return Integer.toString(visitor.getId());
    }

    public Visitor createObjectFromString(String line){

        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }

        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);

            Visitor visitor = new Visitor(LocalDateTime.now());
            visitor.setId(id);
            return visitor;
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }
    }
}
