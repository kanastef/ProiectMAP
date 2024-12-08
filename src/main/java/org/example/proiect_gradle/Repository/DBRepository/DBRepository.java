package org.example.proiect_gradle.Repository.DBRepository;

import org.example.proiect_gradle.Domain.Identifiable;
import org.example.proiect_gradle.Exceptions.CustomException;
import org.example.proiect_gradle.Repository.IRepository;
import org.example.proiect_gradle.Exceptions.DatabaseException;
import org.example.proiect_gradle.Exceptions.EntityNotFoundException;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    public abstract PreparedStatement getSelectOneStatement(Connection c, int id) throws DatabaseException;
    public abstract PreparedStatement getSelectAllStatement(Connection c) throws DatabaseException;
    public abstract PreparedStatement getInsertStatement(Connection c, T item) throws DatabaseException;
    public abstract PreparedStatement getUpdateStatement(Connection c, T item) throws DatabaseException;
    public abstract PreparedStatement getDeleteStatement(Connection c, int id) throws DatabaseException;

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
            throw new DatabaseException(e.getMessage());
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
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void create(T entity) {
        if(entity==null)
            throw new EntityNotFoundException("ENTITY CANNOT BE NULL");
        try{
            PreparedStatement statement = getInsertStatement(connection, entity);
            String strStatement = statement.toString().split("Statement:")[1];
            statement.executeUpdate(strStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            entity.setId(id);
        }catch (Exception e){
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        try{
            PreparedStatement statement = getUpdateStatement(connection, entity);
            statement.executeUpdate();
        }catch (Exception e){
            throw new DatabaseException(e.getMessage());
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
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public List<T> findByCriteria(Predicate<T> predicate) {
        List<T> matchingObjects = new ArrayList<>();
        List<T> allEntities = getAll();

        for (T entity : allEntities) {
            if (predicate.test(entity)) {
                matchingObjects.add(entity);
            }
        }
        return matchingObjects;
    }

}
