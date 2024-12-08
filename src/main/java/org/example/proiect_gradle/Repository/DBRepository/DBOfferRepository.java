package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Offer;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBOfferRepository extends DBRepository<Offer>{

    public DBOfferRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM offers WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select statement for offer with id: "+ id);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM offers");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for offers");
        }
    }

    public PreparedStatement getInsertStatement(Connection c, Offer item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement
                    ("INSERT INTO offers(message, offeredPrice, senderId, receiverId, productId, accepted) VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(1, item.getMessage());
            stmt.setDouble(2, item.getOfferedPrice());
            stmt.setInt(3, item.getSender());
            stmt.setInt(4, item.getReceiver());
            stmt.setInt(5, item.getTargetedProduct());
            stmt.setBoolean(6, item.getStatus());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for offer with id: "+ item.getId());
        }
    }

    public PreparedStatement getUpdateStatement(Connection c, Offer item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("UPDATE offers SET message = ?, offeredPrice = ?, senderId = ?, receiverId = ?, productId = ?, accepted = ? WHERE id = ?");
            stmt.setString(1, item.getMessage());
            stmt.setDouble(2, item.getOfferedPrice());
            stmt.setInt(3, item.getSender());
            stmt.setInt(4, item.getReceiver());
            stmt.setInt(5, item.getTargetedProduct());
            stmt.setBoolean(6, item.getStatus());
            stmt.setInt(7, item.getId());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for offer with id: "+ item.getId());
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM offers WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing delete statement for offer with id: "+ id);
        }
    }

    public Offer createEntity(ResultSet resultSet) throws DatabaseException {
        try {
            Offer offer = new Offer(resultSet.getString("message"),
                    resultSet.getDouble("offeredPrice"), resultSet.getInt("productId"),
                    resultSet.getInt("senderId"), resultSet.getInt("receiverId"));
            offer.setId(resultSet.getInt("id"));
            return offer;
        }catch(SQLException e){
            throw new DatabaseException("Error creating offer entity from result set");
        }
    }
}
