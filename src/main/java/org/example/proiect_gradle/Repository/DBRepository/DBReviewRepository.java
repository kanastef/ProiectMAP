package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Offer;
import org.example.proiect_gradle.Domain.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBReviewRepository extends DBRepository<Review> {

    public DBReviewRepository(String url, String username, String password) {
        super(url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM reviews WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public PreparedStatement getSelectAllStatement(Connection c) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT * FROM reviews");
        return stmt;
    }

    public PreparedStatement getInsertStatement(Connection c, Review item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement
                ("INSERT INTO reviews(message, grade, reviewerId, revieweeId) VALUES(?, ?, ?, ?)");
        stmt.setString(1, item.getMessage());
        stmt.setDouble(2, item.getGrade());
        stmt.setInt(3, item.getReviewer());
        stmt.setInt(4, item.getReviewee());
        return stmt;
    }

    public PreparedStatement getUpdateStatement(Connection c, Review item) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("UPDATE reviews SET message = ?, grade = ?, reviewerId = ?, revieweeId = ? WHERE id = ?");
        stmt.setString(1, item.getMessage());
        stmt.setDouble(2, item.getGrade());
        stmt.setInt(3, item.getReviewer());
        stmt.setInt(4, item.getReviewee());
        stmt.setInt(5, item.getId());
        return stmt;
    }

    public PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("DELETE FROM reviews WHERE id = ?");
        stmt.setInt(1, id);
        return stmt;
    }

    public Review createEntity(ResultSet resultSet) throws SQLException {
        Review review = new Review(resultSet.getDouble("grade"), resultSet.getString("message"),
                resultSet.getInt("reviewerId"), resultSet.getInt("revieweeId"));
        review.setId(resultSet.getInt("id"));
        return review;
    }
}
