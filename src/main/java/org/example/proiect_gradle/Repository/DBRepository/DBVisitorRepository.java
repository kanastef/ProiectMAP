package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Visitor;
import org.example.proiect_gradle.Exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DBVisitorRepository extends DBRepository<Visitor> {

    public DBVisitorRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Visitors WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select statement for visitor with id: "+ id);
        }
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException {
        try {
            return c.prepareStatement("SELECT * FROM Visitors");
        }catch(SQLException e){
            throw new DatabaseException("Error preparing select all statement for visitors");
        }
    }

    public PreparedStatement getInsertStatement(Connection c, Visitor item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement(
                    "INSERT INTO Visitors(visitDate) VALUES(?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(item.getVisitDate()));
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing insert statement for visitor with id: "+ item.getId());
        }
    }

    public PreparedStatement getUpdateStatement(Connection c, Visitor item) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("UPDATE Visitors SET visitDate = ? WHERE id = ?");
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(item.getVisitDate()));
            stmt.setInt(2, item.getId());
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing update statement for visitor with id: "+ item.getId());
        }
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException {
        try {
            PreparedStatement stmt = c.prepareStatement("DELETE FROM Visitors WHERE id = ?");
            stmt.setInt(1, id);
            return stmt;
        }catch(SQLException e){
            throw new DatabaseException("Error preparing delete statement for visitor with id: "+ id);
        }
    }

    public Visitor createEntity(ResultSet resultSet) throws DatabaseException {
        try {
            int id = resultSet.getInt("id");
            LocalDateTime visitDate = resultSet.getTimestamp("visitDate").toLocalDateTime();
            Visitor visitor = new Visitor(visitDate);
            visitor.setId(id);
            return visitor;
        }catch(SQLException e){
            throw new DatabaseException("Error creating user entity from result set");
        }
    }

}
