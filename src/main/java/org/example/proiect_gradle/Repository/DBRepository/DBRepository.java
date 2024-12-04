package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Identifiable;
import org.example.proiect_gradle.Repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DBRepository<T extends Identifiable> implements IRepository<T> {
    private final String url;
    private final String username;
    private final String password;
    Connection connection;

    public DBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract PreparedStatement getSelectOneStatement(Connection c, int id) throws SQLException;
    public abstract PreparedStatement getSelectAllStatement(Connection c) throws SQLException;
    public abstract PreparedStatement getInsertStatement(Connection c, T item) throws SQLException;
    public abstract PreparedStatement getUpdateStatement(Connection c, T item) throws SQLException;
    public abstract PreparedStatement getDeleteStatement(Connection c, int id) throws SQLException;

    public abstract T createEntity(ResultSet resultSet) throws SQLException;

    @Override
    public T read(int id) {
        try {
            PreparedStatement statement = getSelectOneStatement(connection, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return createEntity(resultSet);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        try {
            PreparedStatement statement = getSelectAllStatement(connection);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                result.add(createEntity(resultSet));
            }

            return result;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(T entity) {
        if(entity==null)
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");
        try{
            PreparedStatement statement = getInsertStatement(connection, entity);

            statement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T entity) {
        try{
            PreparedStatement statement = getUpdateStatement(connection, entity);
            statement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public T delete(int id){
        try{
            T entity = read(id);
            if (entity!=null) {
                PreparedStatement statement = getDeleteStatement(connection, id);
                statement.executeUpdate();
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
