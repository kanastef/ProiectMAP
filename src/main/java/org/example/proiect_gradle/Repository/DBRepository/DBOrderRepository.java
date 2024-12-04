package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOrderRepository extends DBRepository<Order>{
    public int currentId = 1;

    public DBOrderRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Orders WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public List<Integer> getProductsForOrder(Connection c, int orderId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT productId FROM OrderedProducts WHERE orderId = ?");
        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        List<Integer> productIds = new ArrayList<>();
        while (rs.next()) {
            productIds.add(rs.getInt("productId"));
        }
        return productIds;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Orders");
        return stmt;
    }

    public Map<Integer, List<Integer>> getAllOrderedProducts(Connection c) throws SQLException {
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
    }

    public PreparedStatement getInsertStatement(Connection c, Order item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO Orders(status, totalPrice, shippingAddress, buyerId, sellerId) VALUES(?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1, item.getStatus());
        stmt.setDouble(2, item.getTotalPrice());
        stmt.setString(3, item.getShippingAddress());
        stmt.setInt(4, item.getBuyer());
        stmt.setInt(5, item.getSeller());
        item.setId(currentId);
        currentId++;
        return stmt;
    }

    public void insertOrderedProducts(Connection c, int orderId, List<Integer> productIds) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO OrderedProducts(orderId, productId) VALUES(?, ?)");

        for (int productId : productIds) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.addBatch();
        }
        stmt.executeBatch();
    }

    public PreparedStatement getUpdateStatement(Connection c, Order item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE Orders SET status = ?, totalPrice = ?, shippingAddress = ?, buyerId = ?, sellerId = ? WHERE id = ?");
        stmt.setString(1, item.getStatus());
        stmt.setDouble(2, item.getTotalPrice());
        stmt.setString(3, item.getShippingAddress());
        stmt.setInt(4, item.getBuyer());
        stmt.setInt(5, item.getSeller());
        stmt.setInt(6, item.getId());
        updateOrderedProducts(c, item.getId(), getProductsForOrder(c, item.getId()));
        return stmt;
    }

    public void updateOrderedProducts(Connection c, int orderId, List<Integer> productIds) throws SQLException {
        PreparedStatement deleteStmt = c.prepareStatement("DELETE FROM OrderedProducts WHERE orderId = ?");
        deleteStmt.setInt(1, orderId);
        deleteStmt.executeUpdate();
        insertOrderedProducts(c, orderId, productIds);
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM Orders WHERE id = ?");
        stmt.setInt(1, id);
        deleteOrderedProducts(c, id);
        return stmt;
    }

    public void deleteOrderedProducts(Connection c, int orderId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM OrderedProducts WHERE orderId = ?");
        stmt.setInt(1, orderId);
        stmt.executeUpdate();
    }

    public Order createEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String status = resultSet.getString("status");
        double totalPrice = resultSet.getDouble("totalPrice");
        String shippingAddress = resultSet.getString("shippingAddress");
        int buyerId = resultSet.getInt("buyerId");
        int sellerId = resultSet.getInt("sellerId");
        List<Integer> products = getProductsForOrder(resultSet.getStatement().getConnection(), id);
        Order order = new Order(products, status, shippingAddress, buyerId, sellerId);
        order.setId(id);
        order.setTotalPrice(totalPrice);
        return order;
    }
}
