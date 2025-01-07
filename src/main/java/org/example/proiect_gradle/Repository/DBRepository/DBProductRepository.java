package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Product;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProductRepository extends DBRepository<Product>{

    public DBProductRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM products WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select statement for product with id: "+ id);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM products");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for products");
        }

    }

    public PreparedStatement getInsertStatement(Connection c, Product item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO products(name, color, size, price, brand, cond, nrOfViews, nrOfLikes, available, listedBy, category, imagePath) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getColor());
            stmt.setInt(3, item.getSize());
            stmt.setDouble(4, item.getPrice());
            stmt.setString(5, item.getBrand());
            stmt.setString(6, item.getCondition());
            stmt.setInt(7, item.getNrViews());
            stmt.setInt(8, item.getNrLikes());
            stmt.setBoolean(9, item.isAvailable());
            stmt.setInt(10, item.getListedBy());
            stmt.setInt(11, item.getCategory());
            stmt.setString(12, item.getImagePath());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for product with id: "+ item.getId());
        }
    }

    public PreparedStatement getUpdateStatement(Connection c, Product item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("UPDATE products SET name = ?, color = ?, size = ?, price = ?, brand = ?, cond = ?, nrOfViews = ?, nrOfLikes = ?, available = ?, listedBy = ?, category = ?, imagePath = ? WHERE id = ?");
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getColor());
            stmt.setInt(3, item.getSize());
            stmt.setDouble(4, item.getPrice());
            stmt.setString(5, item.getBrand());
            stmt.setString(6, item.getCondition());
            stmt.setInt(7, item.getNrViews());
            stmt.setInt(8, item.getNrLikes());
            stmt.setBoolean(9, item.isAvailable());
            stmt.setInt(10, item.getListedBy());
            stmt.setInt(11, item.getCategory());
            stmt.setString(12, item.getImagePath());
            stmt.setInt(13, item.getId());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for product with id: "+ item.getId());
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM products WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error creating delete statement for product with id: "+ id);
        }
    }

    public Product createEntity(ResultSet resultSet) throws DatabaseException {
        try {
            Product product = new Product(resultSet.getString("name"),
                    resultSet.getString("color"), resultSet.getInt("size"),
                    resultSet.getDouble("price"), resultSet.getString("brand"),
                    resultSet.getString("cond"), resultSet.getInt("nrOfViews"),
                    resultSet.getInt("nrOfLikes"), resultSet.getInt("listedBy"));
            product.setCategory(resultSet.getInt("category"));
            product.setId(resultSet.getInt("id"));
            product.setImagePath(resultSet.getString("imagePath"));
            return product;
        }catch(SQLException e){
            throw new DatabaseException("Error creating product entity from result set");
        }
    }

}
