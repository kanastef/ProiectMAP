package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Admin;
import org.example.proiect_gradle.Domain.Category;
import org.example.proiect_gradle.Domain.CategoryName;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCategoryRepository extends DBRepository<Category> {

    public DBCategoryRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM categories WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select statement for category with id: "+ id);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM categories");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for categories");
        }
    }

    public PreparedStatement getInsertStatement(Connection c, Category item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO categories(name) VALUES(?)");
            stmt.setString(1, item.getName().toString());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for category with id: "+ item.getId());
        }
    }

    public PreparedStatement getUpdateStatement(Connection c, Category item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("UPDATE categories SET name = ? WHERE id = ?");
            stmt.setString(1, item.getName().toString());
            stmt.setInt(2, item.getId());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for category with id: "+ item.getId());
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM categories WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing delete statement for category with id: "+ id);
        }
    }

    public Category createEntity(ResultSet resultSet) throws DatabaseException {
        try {
            String categoryNameStr = resultSet.getString("name");
            CategoryName categoryName = CategoryName.valueOf(categoryNameStr.toUpperCase());
            Category category = new Category(categoryName);
            category.setId(resultSet.getInt("id"));
            return category;
        }catch(SQLException e){
            throw new DatabaseException("Error creating category entity from result set");
        }
    }
}
