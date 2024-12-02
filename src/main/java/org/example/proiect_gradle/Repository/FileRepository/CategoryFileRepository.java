package org.example.proiect_gradle.Repository.FileRepository;

import org.example.proiect_gradle.Domain.Category;
import org.example.proiect_gradle.Domain.CategoryName;

public class CategoryFileRepository extends FileRepository<Category> {

    public CategoryFileRepository(String filename){
        super(filename);
    }

    @Override
    protected String convertObjectToString(Category category) {
        if(category==null){
            throw new IllegalArgumentException("Category object cannot be null");
        }

        return category.getId()+ "," +
                category.getName();
    }


    @Override
    protected Category createObjectFromString(String line) {

        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }

        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            CategoryName name = CategoryName.valueOf(parts[1]);


            Category category = new Category(name);
            category.setId(id);
            return category;
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }
    }
}
