package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Admin;
import org.example.proiect_gradle.Domain.User;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.*;

public class DBAdminRepository extends DBRepository<Admin> {
    public DBAdminRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM admins WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException ("Error preparing select statement for admin with id: " +id);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM admins");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for admins");
        }

    }

    public PreparedStatement getInsertStatement(Connection c, Admin item) throws DatabaseException{
        try {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO admins(userName, password, email, phone) VALUES(?, ?, ?, ?)");
            stmt.setString(1, item.getUserName());
            stmt.setString(2, item.getPassword());
            stmt.setString(3, item.getEmail());
            stmt.setString(4, item.getPhone());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for admin with id: " +item.getId());
        }
    }

    public PreparedStatement getUpdateStatement(Connection c, Admin item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("UPDATE admins SET userName = ?, password = ?, email = ?, phone = ? WHERE id = ?");
            stmt.setString(1, item.getUserName());
            stmt.setString(2, item.getPassword());
            stmt.setString(3, item.getEmail());
            stmt.setString(4, item.getPhone());
            stmt.setInt(5, item.getId());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for admin with id: " +item.getId());
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM admins WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing delete statement for admin with id: "+ id);
        }
    }

    public Admin createEntity(ResultSet resultSet) throws SQLException {
        try {
            Admin admin = new Admin(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("phone"));
            admin.setId(resultSet.getInt("id"));
            return admin;
        }catch(SQLException e){
            throw new DatabaseException("Error creating admin entity from result set");
        }
    }

    public Admin findByCriteria(String username, String password, Connection c) throws DatabaseException {
        String query = "SELECT * FROM admins WHERE userName = ? AND password = ?";
        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createEntity(rs);
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error finding admin with username: " + username);
        }
        return null;
    }

}
