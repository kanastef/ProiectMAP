package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProductRepository extends DBRepository<Product>{
    public int currentId = 1;

    public DBProductRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM products WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM products");
        return stmt;
    }

    public PreparedStatement getInsertStatement(Connection c, Product item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO products(name, color, size, price, brand, condition, nrOfViews, nrOfLikes, available, listedBy, category) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        stmt.setString(1, item.getName());
        stmt.setString(2, item.getColor());
        stmt.setInt(3, item.getSize());
        stmt.setDouble(4, item.getPrice());
        stmt.setString(5, item.getBrand());
        stmt.setString(6, item.getCondition());
        stmt.setInt(7, item.getNrLikes());
        stmt.setInt(8, item.getNrViews());
        stmt.setBoolean(9, item.isAvailable());
        stmt.setInt(10, item.getListedBy());
        stmt.setInt(11, item.getCategory());
        item.setId(currentId);
        currentId++;
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Product item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE products SET name = ?, color = ?, size = ?, price = ?, brand = ?, condition = ?, nrOfViews = ?, nrOfLikes = ?, available = ?, listedBy = ?, category = ? WHERE id = ?");
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
        stmt.setInt(11, item.getId());
        stmt.setInt(12, item.getCategory());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM products WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public Product createEntity(ResultSet resultSet) throws SQLException {
        Product product = new Product(resultSet.getString("name"),
                resultSet.getString("color"), resultSet.getInt("size"),
                resultSet.getDouble("price"), resultSet.getString("brand"),
                resultSet.getString("condition"), resultSet.getInt("nrOfViews"),
                resultSet.getInt("nrOfLikes"), resultSet.getInt("listedBy"));
        product.setCategory(resultSet.getInt("category"));
        product.setId(resultSet.getInt("id"));
        return product;
    }
}
