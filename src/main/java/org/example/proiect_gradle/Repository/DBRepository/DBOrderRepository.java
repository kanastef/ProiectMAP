package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Order;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOrderRepository extends DBRepository<Order>{

    public DBOrderRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Orders WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select statement for order with id: "+ id);
        }
    }

    public List<Integer> getProductsForOrder(Connection c, int orderId) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT productId FROM OrderedProducts WHERE orderId = ?");
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            List<Integer> productIds = new ArrayList<>();
            while (rs.next()) {
                productIds.add(rs.getInt("productId"));
            }
            return productIds;
        }catch(SQLException e){
            throw new DatabaseException("Error fetching products for order with id: " + orderId);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM Orders");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for orders");
        }

    }

    public Map<Integer, List<Integer>> getAllOrderedProducts(Connection c) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT orderId, productId FROM OrderedProducts");
            ResultSet rs = stmt.executeQuery();

            Map<Integer, List<Integer>> orderProducts = new HashMap<>();
            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                int productId = rs.getInt("productId");

                orderProducts.putIfAbsent(orderId, new ArrayList<>());
                orderProducts.get(orderId).add(productId);
            }
            return orderProducts;
        }catch(SQLException e){
            throw new DatabaseException("Error fetching all ordered products");
        }
    }

    public PreparedStatement getInsertStatement(Connection c, Order item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("INSERT INTO Orders(stat, totalPrice, shippingAddress, buyerId, sellerId) VALUES(?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, item.getStatus());
            stmt.setDouble(2, item.getTotalPrice());
            stmt.setString(3, item.getShippingAddress());
            stmt.setInt(4, item.getBuyer());
            stmt.setInt(5, item.getSeller());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for order with id: "+ item.getId());
        }
    }

    public void insertOrderedProducts(Connection c, int orderId, List<Integer> productIds) throws DatabaseException {
        String sql = "INSERT INTO OrderedProducts(orderId, productId) VALUES(?, ?)";
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            for (int productId : productIds) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, productId);
                stmt.addBatch();
            }
            stmt.executeBatch(); // Execute batch only once
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting ordered products for order with id: " + orderId);
        }
    }


    public PreparedStatement getUpdateStatement(Connection c, Order item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("UPDATE Orders SET stat = ?, totalPrice = ?, shippingAddress = ?, buyerId = ?, sellerId = ? WHERE id = ?");
            stmt.setString(1, item.getStatus());
            stmt.setDouble(2, item.getTotalPrice());
            stmt.setString(3, item.getShippingAddress());
            stmt.setInt(4, item.getBuyer());
            stmt.setInt(5, item.getSeller());
            stmt.setInt(6, item.getId());
            updateOrderedProducts(c, item.getId(), getProductsForOrder(c, item.getId()));
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for order with id: "+ item.getId());
        }
    }

    public void updateOrderedProducts(Connection c, int orderId, List<Integer> productIds) throws DatabaseException {
        try {
            PreparedStatement deleteStmt = c.prepareStatement("DELETE FROM OrderedProducts WHERE orderId = ?");
            deleteStmt.setInt(1, orderId);
            deleteStmt.executeUpdate();
            insertOrderedProducts(c, orderId, productIds);
        }catch(SQLException e){
            throw new DatabaseException("Error updating ordered products for order with id: " + orderId);
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM Orders WHERE id = ?");
            stmt.setInt(1, id);
            deleteOrderedProducts(c, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing delete statement for order with id: "+ id);
        }
    }

    public void deleteOrderedProducts(Connection c, int orderId) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM OrderedProducts WHERE orderId = ?");
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DatabaseException("Error deleting ordered products for order with id: " + orderId);
        }
    }

    public Order createEntity(ResultSet resultSet) throws DatabaseException {
        try {
            int id = resultSet.getInt("id");
            String status = resultSet.getString("stat");
            double totalPrice = resultSet.getDouble("totalPrice");
            String shippingAddress = resultSet.getString("shippingAddress");
            int buyerId = resultSet.getInt("buyerId");
            int sellerId = resultSet.getInt("sellerId");
            List<Integer> products = getProductsForOrder(resultSet.getStatement().getConnection(), id);
            Order order = new Order(products, status, shippingAddress, buyerId, sellerId);
            order.setId(id);
            order.setTotalPrice(totalPrice);
            return order;
        }catch(SQLException e){
            throw new DatabaseException("Error creating Order entity from result set");
        }
    }

    @Override
    public void create(Order entity) {
        if (entity == null) {
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");
        }

        try {
            // Insert Order and retrieve the generated ID
            PreparedStatement statement1 = getInsertStatement(connection, entity);
            statement1.executeUpdate();

            ResultSet rs = statement1.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entity.setId(id);
            } else {
                throw new IllegalArgumentException("Failed to retrieve the generated key for the order.");
            }

            // Insert Ordered Products using the generated Order ID
            insertOrderedProducts(connection, entity.getId(), entity.getProducts());

        } catch (SQLException e) {
            throw new IllegalArgumentException("ERROR CREATING ORDER", e);
        }
    }


}
