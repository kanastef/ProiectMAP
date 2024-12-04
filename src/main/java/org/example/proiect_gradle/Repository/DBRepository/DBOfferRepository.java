package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Offer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBOfferRepository extends DBRepository<Offer>{
    public int currentId = 1;

    public DBOfferRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM offers WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM offers");
        return stmt;
    }

    public PreparedStatement getInsertStatement(Connection c, Offer item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement
                ("INSERT INTO offers(message, offeredPrice, senderId, receiverId, productId, status) VALUES(?, ?, ?, ?, ?, ?)");
        stmt.setString(1, item.getMessage());
        stmt.setDouble(2, item.getOfferedPrice());
        stmt.setInt(3, item.getSender());
        stmt.setInt(4, item.getReceiver());
        stmt.setInt(5, item.getTargetedProduct());
        stmt.setBoolean(6, item.getStatus());
        item.setId(currentId);
        currentId++;
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Offer item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE offers SET message = ?, offeredPrice = ?, senderId = ?, receiverId = ?, productId = ?, status = ? WHERE id = ?");
        stmt.setString(1, item.getMessage());
        stmt.setDouble(2, item.getOfferedPrice());
        stmt.setInt(3, item.getSender());
        stmt.setInt(4, item.getReceiver());
        stmt.setInt(5, item.getTargetedProduct());
        stmt.setBoolean(6, item.getStatus());
        stmt.setInt(7, item.getId());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM offers WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public Offer createEntity(ResultSet resultSet) throws SQLException {
        Offer offer = new Offer(resultSet.getString("message"),
                resultSet.getDouble("offeredPrice"), resultSet.getInt("productId"),
                resultSet.getInt("senderId"), resultSet.getInt("receiverId"));
        offer.setId(resultSet.getInt("id"));
        return offer;
    }
}
