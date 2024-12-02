package org.example.proiect_gradle.Repository.IMRepository;

import org.example.proiect_gradle.Domain.Identifiable;
import org.example.proiect_gradle.Repository.IRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IMRepository<T extends Identifiable> implements IRepository<T> {
    private final Map<Integer, T> storage = new HashMap<>();
    public int currentId = 1;

    @Override
    public void create(T object) {
        if (storage.values().stream().anyMatch(existing -> existing.equals(object))) {
            throw new IllegalArgumentException("Duplicate object detected");
        }
        object.setId(currentId);
        storage.put(currentId++, object);
    }

    @Override
    public T read(int id) {
        return storage.get(id);
    }

    @Override
    public void update(T object) {
        if (object instanceof Identifiable identifiableEntity) {
            storage.put(identifiableEntity.getId(), object);
        } else {
            throw new IllegalArgumentException("Entity must implement Domain.Identifiable interface.");
        }
    }

    @Override
    public T delete(int id) {
        return storage.remove(id);
    }


    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public List<T> findByCriteria(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }





}


