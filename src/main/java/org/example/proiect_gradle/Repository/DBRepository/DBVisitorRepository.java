package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Visitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DBVisitorRepository extends DBRepository<Visitor> {

    public DBVisitorRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM Visitors WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        return c.prepareStatement("SELECT * FROM Visitors");
    }

    public PreparedStatement getInsertStatement(Connection c, Visitor item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement(
                "INSERT INTO Visitors(visitDate) VALUES(?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        stmt.setTimestamp(1, java.sql.Timestamp.valueOf(item.getVisitDate()));
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Visitor item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE Visitors SET visitDate = ? WHERE id = ?");
        stmt.setTimestamp(1, java.sql.Timestamp.valueOf(item.getVisitDate()));
        stmt.setInt(2, item.getId());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM Visitors WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public Visitor createEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        LocalDateTime visitDate = resultSet.getTimestamp("visitDate").toLocalDateTime();
        Visitor visitor = new Visitor(visitDate);
        visitor.setId(id);
        return visitor;
    }
}
