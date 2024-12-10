package org.example.proiect_gradle.Domain;

import java.util.Objects;

public class Category implements Identifiable {
    private int id;
    private CategoryName name;

    public Category(CategoryName name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoryName getName() {
        return name;
    }

    public void setName(CategoryName name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Domain.Category{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name == category.name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
