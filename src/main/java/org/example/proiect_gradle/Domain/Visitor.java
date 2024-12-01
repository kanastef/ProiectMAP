package org.example.proiect_gradle.Domain;

public class Visitor implements Identifiable{
    private int id;

    public Visitor() {

    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
