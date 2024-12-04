package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAdminRepository extends DBRepository<Admin> {
    public int currentId = 1;
    public DBAdminRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM admins WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM admins");
        return stmt;
    }

    public PreparedStatement getInsertStatement(Connection c, Admin item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO admins(userName, password, email, phone) VALUES(?, ?, ?, ?)");
        stmt.setString(1, item.getUserName());
        stmt.setString(2, item.getPassword());
        stmt.setString(3, item.getEmail());
        stmt.setString(4, item.getPhone());
        item.setId(currentId);
        currentId++;
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Admin item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE admins SET userName = ?, password = ?, email = ?, phone = ? WHERE id = ?");
        stmt.setString(1, item.getUserName());
        stmt.setString(2, item.getPassword());
        stmt.setString(3, item.getEmail());
        stmt.setString(4, item.getPhone());
        stmt.setInt(5, item.getId());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM admins WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public Admin createEntity(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("phone"));
        admin.setId(resultSet.getInt("id"));
        return admin;
    }
}
