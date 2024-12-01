package org.example.proiect_gradle.Repository;

import org.example.proiect_gradle.Domain.Product;

public class ProductFileRepository extends FileRepository<Product> {
    public ProductFileRepository(String filepath){
        super(filepath);
    }

    protected String convertObjectToString(Product product){

        if(product==null){
            throw new IllegalArgumentException("Product object cannot be null");
        }

        String serialized= product.getId()+ "," +
                product.getCategory()+ "," +
                product.getName()+ "," +
                product.getColor()+ "," +
                product.getSize()+ "," +
                product.getPrice()+ "," +
                product.getBrand()+ "," +
                product.getCondition()+ "," +
                product.getNrViews()+ "," +
                product.getNrLikes()+ "," +
                product.getListedBy()+ "," +
                product.isAvailable();
        return serialized;
    }


    protected Product createObjectFromString(String line){


        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }


        try{
        String[] parts=line.split(",");
        int id=Integer.parseInt(parts[0]);
        int category=Integer.parseInt(parts[1]);
        String name=parts[2];
        String color=parts[3];
        int size=Integer.parseInt(parts[4]);
        double price=Double.parseDouble(parts[5]);
        String brand=parts[6];
        String condition=parts[7];
        int nrViews=Integer.parseInt(parts[8]);
        int nrLikes=Integer.parseInt(parts[9]);
        int listedBy=Integer.parseInt(parts[10]);
        boolean isAvailable=Boolean.parseBoolean(parts[11]);

        Product product= new Product(name,color,size,price,brand,condition,nrViews,nrLikes,listedBy);
        product.setCategory(category);
        product.setId(id);
        product.setAvailable(isAvailable);
        return product;
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }

    }

}