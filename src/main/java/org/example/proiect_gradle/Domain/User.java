package org.example.proiect_gradle.Domain;

import java.util.ArrayList;
import java.util.List;

public class User extends Account implements Identifiable {
    private int id;
    private double score;
    protected List<Integer> favourites;
    public int nrOfFlaggedActions;

    public User(String userName, String password, String email, String phone, double score){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.score=score;
        this.favourites=new ArrayList<>();
        this.nrOfFlaggedActions=0;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<Integer> getFavourites() {
        return favourites;
    }

    public void incrementFlaggedActions() {
        this.nrOfFlaggedActions++;
    }

    public int getFlaggedActions() {
        return nrOfFlaggedActions;
    }

    public void setNrOfFlaggedActions(int nrOfFlaggedActions) {
        this.nrOfFlaggedActions = nrOfFlaggedActions;
    }

    public void setFavourites(List<Integer> favourites) {
        this.favourites = favourites;
    }

    @Override
    public String toString() {
        return "\n==============================" +
                "\n         USER DETAILS        " +
                "\n==============================" +
                "\nID          : " + id +
                "\nUsername    : " + userName +
                "\nRating      : " + score +
                "\n==============================";
    }
}
