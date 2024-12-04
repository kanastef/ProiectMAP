package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Admin;
import org.example.proiect_gradle.Domain.Category;
import org.example.proiect_gradle.Domain.CategoryName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCategoryRepository extends DBRepository<Category> {
    public int currentId = 1;

    public DBCategoryRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM categories WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM categories");
        return stmt;
    }

    public PreparedStatement getInsertStatement(Connection c, Category item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO categories(name) VALUES(?)");
        stmt.setString(1, item.getName().toString());
        item.setId(currentId);
        currentId++;
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Category item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE categories SET name = ? WHERE id = ?");
        stmt.setString(1, item.getName().toString());
        stmt.setInt(2, item.getId());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM categories WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public Category createEntity(ResultSet resultSet) throws SQLException {
        String categoryNameStr = resultSet.getString("name");
        CategoryName categoryName = CategoryName.valueOf(categoryNameStr.toUpperCase());
        Category category = new Category(categoryName);
        category.setId(resultSet.getInt("id"));
        return category;
    }
}
