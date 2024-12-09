package org.example.proiect_gradle.Domain;

import java.time.LocalDateTime;

public class Visitor implements Identifiable{
    private int id;
    private LocalDateTime visitDate;

    public Visitor(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }



}
