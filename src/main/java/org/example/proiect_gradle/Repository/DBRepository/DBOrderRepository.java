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
        PreparedStatement stmt = c.prepareStatement("INSERT INTO Orders(stat, totalPrice, shippingAddress, buyerId, sellerId) VALUES(?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1, item.getStatus());
        stmt.setDouble(2, item.getTotalPrice());
        stmt.setString(3, item.getShippingAddress());
        stmt.setInt(4, item.getBuyer());
        stmt.setInt(5, item.getSeller());
        return stmt;
    }

    public PreparedStatement insertOrderedProducts(Connection c, int orderId, List<Integer> productIds) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("INSERT INTO OrderedProducts(orderId, productId) VALUES(?, ?)");

        for (int productId : productIds) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.addBatch();
        }
        stmt.executeBatch();
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Order item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE Orders SET stat = ?, totalPrice = ?, shippingAddress = ?, buyerId = ?, sellerId = ? WHERE id = ?");
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
    }

    @Override
    public void create(Order entity) {
        if(entity==null)
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");
        try{
            PreparedStatement statement1 = getInsertStatement(connection, entity);
            String strStatement = statement1.toString().split("Statement:")[1];
            statement1.executeUpdate(strStatement, PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement1.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            entity.setId(id);
            PreparedStatement statement2 = insertOrderedProducts(connection, entity.getId(), entity.getProducts());
            String strStatement2 = statement2.toString().split("Statement:")[1];
            statement2.executeUpdate(strStatement2, PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (Exception e){
            throw new IllegalArgumentException("ERROR CREATING ORDER");
        }
    }

//    @Override
//    public void update(Order entity) {
//        try {
//            Order object = read(entity.getId());
//            if (object != null) {
//                PreparedStatement statement = getUpdateStatement(connection, entity);
//                statement.executeUpdate();
//
//            }
//        } catch (Exception e){
//            throw new IllegalArgumentException("ERROR UPDATING ORDER");
//        }
//    }
}
