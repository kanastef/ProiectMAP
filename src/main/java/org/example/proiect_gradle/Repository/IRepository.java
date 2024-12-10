package org.example.proiect_gradle.Repository;

import java.util.List;
import java.util.function.Predicate;

public interface IRepository<T> {
    public void create(T object);
    public T read(int id);
    public void update(T object);
    public T delete(int id);
    public List<T> getAll();
    List<T> findByCriteria(Predicate<T> predicate);
}
