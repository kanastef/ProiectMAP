package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.User;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUserRepository extends DBRepository<User> {
    public int currentId = 1;

    public DBUserRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Users WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select statement for user with id: "+ id);
        }
    }

    public List<Integer> getLikedProductsForUser(int userId, Connection c) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT productId FROM LikedProducts WHERE userId = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            List<Integer> likedProducts = new ArrayList<>();
            while (rs.next()) {
                likedProducts.add(rs.getInt("productId"));
            }
            return likedProducts;
        }catch(SQLException e){
            throw new DatabaseException("Error fetching products liked by user with id: "+ userId);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM Users");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for users");
        }
    }

    public PreparedStatement getInsertStatement(Connection c, User item) throws DatabaseException {
        try {
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
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for user with id: "+ item.getId());
        }
    }

    public void addLikedProduct(Connection c, int userId, int productId) throws SQLException {
        try {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO LikedProducts(userId, productId) VALUES(?, ?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DatabaseException("Error adding liked product with id: "+ productId+" for user with id: "+ userId);
        }
    }

    public PreparedStatement getUpdateStatement(Connection c, User item) throws DatabaseException {
        try {
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
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for user with id: "+ item.getId());
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM Users WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing delete statement for user with id: "+ id);
        }
    }

    public void removeLikedProduct(Connection c, int userId, int productId) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM LikedProducts WHERE userId = ? AND productId = ?");
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DatabaseException("Error removing liked product with id: "+ productId+" from user with id: "+ userId);
        }
    }

    public void clearLikedProducts(Connection c, int userId) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM LikedProducts WHERE userId = ?");
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DatabaseException("Error clearing liked products from user with id: "+ userId);
        }
    }

    private void syncLikedProducts(Connection connection, User user) throws SQLException {
        try {
            List<Integer> currentLikedProducts = getLikedProductsForUser(user.getId(), connection);
            List<Integer> productsToAdd = new ArrayList<>(user.getFavourites());
            productsToAdd.removeAll(currentLikedProducts);
            List<Integer> productsToRemove = new ArrayList<>(currentLikedProducts);
            productsToRemove.removeAll(user.getFavourites());
            for (int productId : productsToAdd) {
                addLikedProduct(connection, user.getId(), productId);
            }
            for (int productId : productsToRemove) {
                removeLikedProduct(connection, user.getId(), productId);
            }
        }catch(SQLException e){
            throw new DatabaseException("Error synchronizing liked products for user with id: " + user.getId());
        }
    }

    public User createEntity(ResultSet resultSet) throws DatabaseException {
        try {
            User user = new User(resultSet.getString("userName"),
                    resultSet.getString("password"),
                    resultSet.getString("email"), resultSet.getString("phone"),
                    resultSet.getDouble("score"));
            user.setId(resultSet.getInt("id"));
            user.setNrOfFlaggedActions(resultSet.getInt("nrOfFlaggedActions"));
            user.setFavourites(getLikedProductsForUser(user.getId(), connection));
            return user;
        }catch(SQLException e){
            throw new DatabaseException("Error creating user entity from result set");
        }
    }

    public User findByCriteria(String username, String password, Connection c) throws DatabaseException{
        String query = "SELECT * FROM users WHERE userName = ? AND password = ?";
        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createEntity(rs);
                }
            }
        }catch(SQLException e){
            throw new DatabaseException("Error finding user with username: " + username);
        }
        return null;
    }

    @Override
    public void update(User entity) {
        try{
            PreparedStatement statement = getUpdateStatement(connection, entity);
            statement.executeUpdate();
            syncLikedProducts(connection, entity);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
