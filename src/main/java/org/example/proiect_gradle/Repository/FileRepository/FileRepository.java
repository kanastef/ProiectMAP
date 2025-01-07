package org.example.proiect_gradle.Repository.FileRepository;

import org.example.proiect_gradle.Domain.Identifiable;
import org.example.proiect_gradle.Repository.IMRepository.IMRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class FileRepository<T extends Identifiable> extends IMRepository<T> {
    public final String filename;


    public FileRepository(String filename) {
        super();
        this.filename = filename;
    }

    @Override
    public void create(T object) {

        int existing_id = exists(object);

        if (existing_id == 0) {
            object.setId(currentId);
            saveObjectToFile(object);
            currentId++;
        }
        else
            object.setId(existing_id);

    }

    private int exists(T object) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T existingObject = createObjectFromString(line);
                if (existingObject.equals(object)) {
                    return existingObject.getId();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void update(T object) {
        List<T> allObjects = getAll();
        boolean updated = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T existingObject : allObjects) {
                if (existingObject.getId() == object.getId()) {
                    writer.write(convertObjectToString(object));
                    writer.newLine();
                    updated = true;
                } else {
                    writer.write(convertObjectToString(existingObject));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        if (!updated) {
            throw new IllegalArgumentException("Object with ID " + object.getId() + " not found");
        }
    }

    @Override
    public T delete(int id) {
        List<T> allObjects = getAll();
        T removedObject = null;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T existingObject : allObjects) {
                if (existingObject.getId() == id) {
                    removedObject = existingObject;
                } else {
                    writer.write(convertObjectToString(existingObject));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        if (removedObject == null) {
            throw new IllegalArgumentException("Object with ID " + id + " not found");
        }

        return removedObject;
    }

    @Override
    public T read(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T existingObject = createObjectFromString(line);
                if (existingObject.getId() == id) {
                    return existingObject;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return null;
    }

    public void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T object = createObjectFromString(line);
                if (object.getId() >= currentId) {
                    currentId = object.getId() + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private void saveObjectToFile(T object) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(convertObjectToString(object));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public List<T> getAll() {
        List<T> objects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T object = createObjectFromString(line);
                objects.add(object);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return objects;
    }

    @Override
    public List<T> findByCriteria(Predicate<T> predicate) {
        List<T> matchingObjects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T object = createObjectFromString(line);
                if (predicate.test(object)) {
                    matchingObjects.add(object);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return matchingObjects;
    }

    protected abstract String convertObjectToString(T object);

    protected abstract T createObjectFromString(String line);

    public static void clearFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {

        } catch (IOException e) {
            System.err.println("Error clearing file: " + filePath);
            e.printStackTrace();
        }
    }
}
