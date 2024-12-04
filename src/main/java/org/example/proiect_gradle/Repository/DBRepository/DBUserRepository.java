package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUserRepository extends DBRepository<User> {
    public int currentId = 1;

    public DBUserRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Users WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public List<Integer> getLikedProductsForUser(int userId, Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT productId FROM LikedProducts WHERE userId = ?");
        stmt.setInt(1, userId);

        ResultSet rs = stmt.executeQuery();
        List<Integer> likedProducts = new ArrayList<>();
        while (rs.next()) {
            likedProducts.add(rs.getInt("productId"));
        }
        return likedProducts;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        return c.prepareStatement("SELECT * FROM Users");
    }

    public PreparedStatement getInsertStatement(Connection c, User item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement(
                "INSERT INTO Users(userName, password, email, phone, score, nrOfFlaggedActions) VALUES(?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1, item.getUserName());
        stmt.setString(2, item.getPassword());
        stmt.setString(3, item.getEmail());
        stmt.setString(4, item.getPhone());
        stmt.setDouble(5, item.getScore());
        stmt.setInt(6, item.nrOfFlaggedActions);
        item.setId(currentId);
        currentId++;
        return stmt;
    }

    public void addLikedProduct(Connection c, int userId, int productId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO LikedProducts(userId, productId) VALUES(?, ?)");
        stmt.setInt(1, userId);
        stmt.setInt(2, productId);
        stmt.executeUpdate();
    }

    public PreparedStatement getUpdateStatement(Connection c, User item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement(
                "UPDATE Users SET userName = ?, password = ?, email = ?, phone = ?, score = ?, nrOfFlaggedActions = ? WHERE id = ?"
        );
        stmt.setString(1, item.getUserName());
        stmt.setString(2, item.getPassword());
        stmt.setString(3, item.getEmail());
        stmt.setString(4, item.getPhone());
        stmt.setDouble(5, item.getScore());
        stmt.setInt(6, item.nrOfFlaggedActions);
        stmt.setInt(7, item.getId());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM Users WHERE id = ?");
        stmt.setInt(1, id);
        clearLikedProducts(c, id);
        return stmt;
    }

    public void removeLikedProduct(Connection c, int userId, int productId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM LikedProducts WHERE userId = ? AND productId = ?");
        stmt.setInt(1, userId);
        stmt.setInt(2, productId);
        stmt.executeUpdate();
    }

    public void clearLikedProducts(Connection c, int userId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM LikedProducts WHERE userId = ?");
        stmt.setInt(1, userId);
        stmt.executeUpdate();
    }

    public User createEntity(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString("userName"),
                resultSet.getString("password"),
                resultSet.getString("email"), resultSet.getString("phone"),
                resultSet.getDouble("score"));
        return user;
    }

    public User findByCriteria(String username, String password, Connection c) throws SQLException {
        String query = "SELECT * FROM Users WHERE userName = ? AND password = ?";
        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createEntity(rs);
                }
            }
        }
        return null;
    }
}
